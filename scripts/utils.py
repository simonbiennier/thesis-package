import json
import ast
import subprocess as sub
import numpy as np
import pandas as pd
import yaml
import os
import re
from datetime import datetime
from rich.console import Console
from dataclasses import MISSING, dataclass, field, fields
from argparse import ArgumentParser
from datetime import datetime
from natsort import natsorted

SCRIPTS_DIR = os.path.abspath(os.path.dirname(__file__))
ROOT_DIR = os.path.abspath(os.path.join(SCRIPTS_DIR, ".."))


def resolve_path(
    path: str | os.PathLike | None, base_dir: str = ROOT_DIR
) -> str | None:
    if path is None:
        return None
    expanded = os.path.expandvars(os.path.expanduser(str(path)))
    if not os.path.isabs(expanded):
        expanded = os.path.abspath(os.path.join(base_dir, expanded))
    return expanded


TIME_COL = "Time"

CORE_ENERGY_COL = "PP0_ENERGY (J)"  # energy consumed by the CPU cores
UNCORE_ENERGY_COL = "PP1_ENERGY (J)"  # energy consumed by (uncore) components close to the CPU (embedded GPU chipset most of the time)
PACKAGE_ENERGY_COL = "PACKAGE_ENERGY (J)"  # includes "core" and "uncore".
DRAM_ENERGY_COL = "DRAM_ENERGY (J)"  # energy consumed by memory

USED_MEMORY_COL = "USED_MEMORY"
CPU_USAGE_COL = "CPU_USAGE"

BEST_PERFORMANCE_GUID = "8c5e7fda-e8bf-4a96-9a85-a6e23a8c635c"
BALANCED_GUID = "381b4222-f694-41f0-9685-ff5bb260df2e"

MVN_FLAGS = " ".join(
    [
        "-Drat.skip",
        "-Dcheckstyle.skip",
        "-Dmaven.javadoc.skip=true",
        "-Dmdep.analyze.skip=true",
        "-Denforcer.skip=true",
        "-Dmaven.compiler.failOnWarning=false",
        # "-o",  # offline
    ]
)

console = Console()
INDENT = 2


def _get_timestamp():
    now = datetime.now().strftime("%H:%M:%S")
    return f"[grey62]\[{now}][/grey62]"


VERSION_COLOURS = {"OR": "blue", "RF": "yellow"}


def _log(style, content, level=0, instance_id=None, version=None):
    padding = " " * INDENT * level

    log_str = [f"{_get_timestamp()}"]

    if instance_id is not None:
        log_str.append(f"[grey50][{instance_id}][/]")
    if version is not None:
        log_str.append(f"[{VERSION_COLOURS[version]}][{version}][/]")
    log_str.append(f"{padding}[{style}]{content}[/]")

    console.print(" ".join(log_str))


class Log:
    def error(msg, level=0, instance_id=None, version=None):
        _log("red", msg, level, instance_id, version)

    def info(msg, level=0, instance_id=None, version=None):
        _log("blue", msg, level, instance_id, version)

    def success(msg, level=0, instance_id=None, version=None):
        _log("green", msg, level, instance_id, version)

    def warning(msg, level=0, instance_id=None, version=None):
        _log("yellow", msg, level, instance_id, version)

    def debug(msg, level=0, instance_id=None, version=None):
        _log("magenta", msg, level, instance_id, version)

    def command(msg, level=0, instance_id=None, version=None):
        _log("white", msg, level, instance_id, version)


@dataclass
class Config:
    """Unified configuration dataclass for all experiment parameters."""

    # system
    driver_path: str
    junit_path: str
    instances_path: str
    systems_path: str

    # experiment
    system_cooldown: int

    baseline_duration: int

    warmup_runs: int
    warmup_cooldown: int

    runs: int
    repetitions: int
    run_cooldown: int

    # target
    results_base_dir: str
    timestamp_format: str

    label_original: str
    label_refactored: str

    template_warmup_file: str
    template_baseline_file: str
    template_run_file: str
    summary_file_template: str

    instance_ids: list[str]

    # computed properties
    system_paths: dict[str, str] = field(default_factory=dict)
    test_paths: dict[str, str] = field(default_factory=dict)
    targets: dict[str, list[str]] = field(default_factory=dict)

    classpaths: dict[str, str] = field(default_factory=dict)
    output_dir_energy: dict[str, str] = field(default_factory=dict)
    output_dir_quality: dict[str, str] = field(default_factory=dict)

    @staticmethod
    def _parse_targets(value):
        """Parse targets from CSV supporting JSON and Python-list formats."""
        if pd.isna(value):
            return []
        if isinstance(value, list):
            return value
        if not isinstance(value, str):
            return [str(value)]

        raw = value.strip()
        if not raw:
            return []

        try:
            parsed = json.loads(raw)
            if isinstance(parsed, list):
                return parsed
            return [str(parsed)]
        except json.JSONDecodeError:
            pass

        try:
            parsed = ast.literal_eval(raw)
            if isinstance(parsed, list):
                return parsed
            return [str(parsed)]
        except (ValueError, SyntaxError):
            raise ValueError(f"Invalid targets format: {value}")

    def __post_init__(self):
        """Compute derived configuration values."""
        base_dir = ROOT_DIR
        self.driver_path = resolve_path(self.driver_path, base_dir)
        self.junit_path = resolve_path(self.junit_path, base_dir)
        self.instances_path = resolve_path(self.instances_path, base_dir)
        self.systems_path = resolve_path(self.systems_path, base_dir)
        self.results_base_dir = resolve_path(self.results_base_dir, base_dir)

        check_paths = [
            self.driver_path,
            self.junit_path,
            self.instances_path,
            self.systems_path,
        ]
        for path in check_paths:
            if not os.path.exists(path):
                raise FileNotFoundError(f"Path not found: {path}")

        df_instances = pd.read_csv(
            self.instances_path,
            dtype={"targets": "string"},
            keep_default_na=False,
            engine="python",
        )
        df_instances["targets"] = df_instances["targets"].apply(self._parse_targets)

        for instance_id in self.instance_ids:
            matches = df_instances[df_instances["instance_id"] == instance_id]
            if matches.empty:
                raise ValueError(f"Test data for ID {instance_id} not found")
            df_test = matches.iloc[0]

            self.system_paths[instance_id] = f"{self.systems_path}/{df_test['system']}"
            self.test_paths[instance_id] = df_test["test_path"]
            self.targets[instance_id] = df_test["targets"]

            for version in [self.label_original, self.label_refactored]:
                file = f"{instance_id}_{version}"

                full_path = f"{self.system_paths[instance_id]}/src/test/java/{self.test_paths[instance_id].replace('.', '/')}/{file}.java"
                if not os.path.exists(full_path):
                    raise ValueError(f"Test file {file} does not exist")

                # check if test class name is correct
                with open(full_path, "r", encoding="utf-8", errors="replace") as f:
                    content = f.read()
                    match = re.search(rf"class {instance_id}_{version}", content)
                    if not match:
                        raise ValueError(
                            f"Test file {file} does not contain expected class name"
                        )

            self.classpaths[instance_id] = (
                f"{self.junit_path};"
                f"{self.system_paths[instance_id]}/target/test-classes;"
                f"{self.system_paths[instance_id]}/target/classes;"
                f"{self.system_paths[instance_id]}/target/dependency/*"
            )

            timestamp = datetime.now().strftime(self.timestamp_format)
            self.output_dir_energy[instance_id] = os.path.join(
                self.results_base_dir, instance_id, "energy", timestamp
            )
            self.output_dir_quality[instance_id] = os.path.join(
                self.results_base_dir, instance_id, "quality", timestamp
            )

    @classmethod
    def from_yaml(cls, *yaml_paths: str):
        """Merges YAMLs and validates required keys."""
        combined_dict = {}
        for path in yaml_paths:
            with open(path, "r") as f:
                data = yaml.safe_load(f)
                if data:
                    combined_dict.update({k.lower(): v for k, v in data.items()})

        # check for missing required keys
        required = {
            f.name
            for f in fields(cls)
            if f.default is MISSING and f.default_factory is MISSING
        }
        missing = required - set(combined_dict.keys())

        if missing:
            raise KeyError(f"Key missing in yaml: {', '.join(missing)}")

        # filter out extra yaml keys
        allowed = {f.name for f in fields(cls)}
        filtered = {k: v for k, v in combined_dict.items() if k in allowed}

        return cls(**filtered)


def to_str_id(val, prefix="AA"):
    """
    Ensures the output is always a formatted string (e.g., 'AA_0028').
    """
    return f"{prefix}_{val:04d}" if isinstance(val, int) else val


def to_int_id(val):
    """
    Converts a formatted string back to an integer (e.g., 'AA_0028' -> 28).
    """
    return int(val.split("_")[-1]) if isinstance(val, str) else val


def _collect_data(output_dir, prefix):
    """Generic helper to parse files starting with a specific prefix."""
    out_dir = os.path.join(output_dir, "raw")

    files = natsorted(
        [f for f in os.listdir(out_dir) if f.startswith(prefix) and f.endswith(".csv")]
    )

    if not files:
        return pd.DataFrame()

    results = []
    for f in files:
        parts = f.replace(".csv", "").split("_")

        run_id = parts[1]
        version = parts[2] if len(parts) > 2 else "base"

        data = pd.read_csv(os.path.join(out_dir, f))

        duration_total = (
            data[TIME_COL].iloc[-1] - data[TIME_COL].iloc[0]
        ) / 1000  # in s
        package_energy_total = (
            data[PACKAGE_ENERGY_COL].iloc[-1] - data[PACKAGE_ENERGY_COL].iloc[0]
        )  # in J
        power = (
            package_energy_total / duration_total if duration_total > 0 else 0
        )  # in W (J/s)

        dram_energy_total = (
            data[DRAM_ENERGY_COL].iloc[-1] - data[DRAM_ENERGY_COL].iloc[0]
        )  # in J
        memory_mean = data[USED_MEMORY_COL].mean() / (1024 * 1024)  # in MB
        memory_median = data[USED_MEMORY_COL].median() / (1024 * 1024)  # in MB
        memory_delta = (data[USED_MEMORY_COL].max() - data[USED_MEMORY_COL].iloc[0]) / (
            1024 * 1024
        )  # in MB
        cpu_usage_mean = (
            data[[col for col in data.columns if CPU_USAGE_COL in col]].mean().mean()
        )  # in %
        cpu_usage_median = (
            data[[col for col in data.columns if CPU_USAGE_COL in col]]
            .median()
            .median()
        )  # in %

        results.append(
            {
                "run_id": run_id,
                "version": version,
                "duration": duration_total,
                "energy": package_energy_total,
                "power": power,
                "dram_energy": dram_energy_total,
                "memory_mean": memory_mean,
                "memory_median": memory_median,
                "memory_delta": memory_delta,
                "cpu_usage_mean": cpu_usage_mean,
                "cpu_usage_median": cpu_usage_median,
                "raw_data": data,  # keep ref for delta calculation
            }
        )

    return pd.DataFrame(results)


def collect_results(config: Config, out_dir):
    """Unified entry point to collect all experimental data."""
    # collect baseline
    df_base = _collect_data(out_dir, "base")
    if not df_base.empty:
        power_idle = df_base["power"].median()
        df_base.drop(columns=["raw_data"]).to_csv(
            os.path.join(
                out_dir,
                config.summary_file_template.format(label="BASE"),
            ),
            index=False,
            lineterminator="\n",
        )
    else:
        power_idle = 0

    # collect warmup runs
    df_warmup = _collect_data(out_dir, "warmup")
    if not df_warmup.empty:
        # E_delta = E_total - (P_idle * t)
        df_warmup["energy_delta"] = df_warmup.apply(
            lambda row: row["energy"] - (power_idle * row["duration"]), axis=1
        )

        # reorder columns
        cols = list(df_warmup.columns)
        # move `energy_delta` to right after `energy`
        cols.insert(cols.index("energy") + 1, cols.pop(cols.index("energy_delta")))
        df_warmup = df_warmup[cols]

        # save summary
        path = os.path.join(
            out_dir, config.summary_file_template.format(label="WARMUP")
        )
        df_warmup.drop(columns=["raw_data"]).to_csv(
            path, index=False, lineterminator="\n"
        )
        # print(f"Results consolidated in {config.summary_file_template.format(label="ALL")}")

    # collect all runs
    df_runs = _collect_data(out_dir, "run")
    if not df_runs.empty:
        # E_delta = E_total - (P_idle * t)
        df_runs["energy_delta"] = df_runs.apply(
            lambda row: row["energy"] - (power_idle * row["duration"]), axis=1
        )

        # reorder columns
        cols = list(df_runs.columns)
        # move `energy_delta` to right after `energy`
        cols.insert(cols.index("energy") + 1, cols.pop(cols.index("energy_delta")))
        df_runs = df_runs[cols]

        # path = os.path.join(out_dir, config.summary_file_template.format(label="ALL"))
        # df_runs.drop(columns=["raw_data"]).to_csv(path, index=False, lineterminator="\n")

        # collect runs per version
        for version in df_runs["version"].unique():
            df_version = df_runs[df_runs["version"] == version]
            version_path = os.path.join(
                out_dir, config.summary_file_template.format(label=version.upper())
            )
            df_version.drop(columns=["raw_data"]).to_csv(
                version_path,
                index=False,
                lineterminator="\n",
            )
            # print(f"Results for version '{version}' saved in {label_path}")


def get_mz_stats(df, column="energy", threshold=3.5):
    results = {}

    const = 0.6745
    median = df[column].median()
    mad = np.median(np.abs(df[column] - median))
    z_scores = const * (df[column] - median) / mad
    upper_limit = median + (threshold * mad / const)
    lower_limit = median - (threshold * mad / const)

    results["median"] = median
    results["mad"] = mad
    results["upper_limit"] = upper_limit
    results["lower_limit"] = lower_limit
    results["z_scores"] = z_scores
    results["outliers"] = df[np.abs(z_scores) >= threshold]
    results["df"] = df[np.abs(z_scores) < threshold]
    return results


def set_timeouts(val):
    """Set system timeouts."""
    for timeout in [
        "monitor-timeout-ac",
        "standby-timeout-ac",
        "monitor-timeout-dc",
        "standby-timeout-dc",
    ]:
        run_command(f"powercfg /change {timeout} {val}")


def run_command(cmd, cwd=None, verbose=False):
    """Run a command silently."""
    sub.run(
        cmd,
        cwd=cwd,
        shell=True,
        stdout=sub.DEVNULL if not verbose else None,
        stderr=sub.STDOUT if not verbose else None,
        check=True,
    )


def set_brightness(val):
    """Set brightness to a specific level (0-100)."""
    run_command(
        f"powershell (Get-WmiObject -Namespace root/WMI -Class WmiMonitorBrightnessMethods).WmiSetBrightness(1, {val})"
    )


def set_power_plan(guid):
    """Set power plan."""
    run_command(f"powercfg /setactive {guid}")


def set_wifi(enable=True):
    admin = "enable" if enable else "disable"
    run_command(f"netsh interface set interface Wi-Fi admin={admin}")


def manage_system(action: str):
    if action == "prepare":
        power_plan = BEST_PERFORMANCE_GUID
        brightness = 0
        timeouts = 0
        wifi = False
    else:
        power_plan = BALANCED_GUID
        brightness = 100
        timeouts = 30
        wifi = True

    set_power_plan(power_plan)
    set_brightness(brightness)
    set_timeouts(timeouts)
    set_wifi(wifi)


def parse_args():
    """Handles all CLI flag logic and returns the parsed arguments."""
    parser = ArgumentParser(description="Pipeline")

    group = parser.add_mutually_exclusive_group(required=False)
    group.add_argument(
        "-f",
        "--full",
        action="store_const",
        dest="exp_config",
        const="config/exp_full.yaml",
        help="Use full config",
    )
    group.add_argument(
        "-t",
        "--test",
        action="store_const",
        dest="exp_config",
        const="config/exp_test.yaml",
        help="Use test config",
    )

    parser.add_argument(
        "--target-config",
        type=str,
        default="config/target.yaml",
        help="Path to target configuration YAML file",
    )
    parser.add_argument(
        "--paths-config",
        type=str,
        default="config/paths.yaml",
        help="Path to paths configuration YAML file",
    )
    parser.add_argument(
        "--skip-prep",
        action="store_true",
        help="Run pipeline without changing system settings (WiFi, brightness, etc.)",
    )
    parser.add_argument(
        "--skip-build",
        action="store_true",
        help="Run pipeline without rebuilding the system",
    )
    parser.add_argument(
        "--verbose",
        "-v",
        action="store_true",
        default=False,
        help="Enable verbose output",
    )

    return parser.parse_args()


def manage_driver(config: Config, action: str):
    """Handles RAPL driver lifecycle."""
    driver_path = config.driver_path

    if action == "start":
        try:
            run_command("sc start rapl")
        except:
            try:
                run_command(f"sc create rapl type=kernel binPath={driver_path}")
                run_command("sc start rapl")
            except:
                raise
    elif action == "stop":
        run_command("sc stop rapl")


def get_full_test_path(config: Config, instance_id: str, label: str):
    file = f"{instance_id}_{label}"
    path = f"{config.test_paths[instance_id]}.{file}"

    return path


def get_test_cmd(class_name, classpath, verbose=False):
    full_cp = classpath

    return " ".join(
        [
            f'java -cp "{full_cp}"',
            f"org.junit.platform.console.ConsoleLauncher",
            f"execute --select-class {class_name}",
            f"--details none" if not verbose else "",
        ]
    )


def get_maven_context(config: Config, instance_id: str):
    """Return Maven cwd and module selector for single- or multi-module systems."""
    system_path = config.system_paths[instance_id]
    rel_system_path = os.path.relpath(system_path, config.systems_path).replace(
        "\\", "/"
    )
    path_parts = [p for p in rel_system_path.split("/") if p and p != "."]

    if len(path_parts) > 1:
        build_cwd = os.path.join(config.systems_path, path_parts[0])
        module_arg = f"-pl {'/'.join(path_parts[1:])} -am"
    else:
        build_cwd = system_path
        module_arg = ""

    return build_cwd, module_arg


def build_system(config: Config, instance_id: str, verbose=False):
    build_cwd, module_arg = get_maven_context(config, instance_id)
    module_cwd = config.system_paths[instance_id]
    deps_out = os.path.join(module_cwd, "target", "dependency")
    cmd_build = (
        f"mvn package -DskipTests=true "
        f"{'-q -B ' if not verbose else ''}"
        f"{module_arg} {MVN_FLAGS}"
    )
    cmd_copy_deps = (
        f"mvn dependency:copy-dependencies "
        f"-DincludeScope=test "
        f"-DexcludeGroupIds=org.junit,org.junit.jupiter,org.junit.platform "
        f'-DoutputDirectory="{deps_out}" '
        f"{'-q -B ' if not verbose else ''}"
        f"{MVN_FLAGS}"
    )

    try:
        run_command(cmd_build, cwd=build_cwd, verbose=verbose)
        run_command(cmd_copy_deps, cwd=module_cwd, verbose=verbose)
        Log.success("Done", level=1)
    except sub.CalledProcessError as e:
        Log.error(f"Failed: {e.stderr}", level=1)
        raise
