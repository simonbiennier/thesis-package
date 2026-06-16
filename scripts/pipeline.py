import subprocess as sub
import time
import random
import os
from utils import (
    Log,
    Config,
    collect_results,
    build_system,
    get_full_test_path,
    manage_system,
    run_command,
    get_test_cmd,
    manage_driver,
    parse_args,
)


def measure(command, out_file, cwd, repetitions=1, verbose=False):
    if repetitions > 1:
        _cmd = f'cmd /c "FOR /L %i IN (1,1,{repetitions}) DO {command}"'
    else:
        _cmd = command
    full_cmd = f'energibridge -o "{out_file}" -- {_cmd}'

    try:
        run_command(full_cmd, cwd=cwd, verbose=verbose)
    except sub.CalledProcessError as e:
        Log.error(f"Failed: {e.stderr}", level=1)
        raise


def cooldown(duration: int, level=1):
    if duration <= 0:
        Log.warning("Skipping cooldown", level=level)
        return

    Log.info(f"Cooling down {duration}s", level=level)
    time.sleep(duration)


def measure_baseline(config: Config, instance_id: str, verbose=False):
    """Measure baseline energy consumption."""

    if config.baseline_duration <= 0:
        Log.warning("Skipping", level=1, instance_id=instance_id)
        return

    # baseline_results = []
    # for i in range(1, config.baseline_runs + 1):
    # print(f"[BASELINE {i}/{config.baseline_runs}]")
    Log.info(
        f"Measuring for {config.baseline_duration}s", level=1, instance_id=instance_id
    )
    out_file = os.path.abspath(
        os.path.join(
            config.output_dir_energy[instance_id],
            f"{config.template_baseline_file.format(id=1)}",
        )
    )

    cmd = f"timeout /t {config.baseline_duration} /nobreak > nul"
    measure(
        cmd, out_file, config.system_paths[instance_id], repetitions=1, verbose=verbose
    )


def warmup_runs(config: Config, instance_id: str, verbose=False):
    if config.warmup_runs <= 0:
        Log.warning("Skipping", level=1, instance_id=instance_id)
        return

    for run_id in range(1, config.warmup_runs + 1):
        # alternate tests
        is_original = run_id % 2 == 0
        class_name = get_full_test_path(
            config,
            instance_id,
            config.label_original if is_original else config.label_refactored,
        )
        version = config.label_original if is_original else config.label_refactored

        Log.info(
            f"[WARMUP {run_id}/{config.warmup_runs}]",
            level=1,
            instance_id=instance_id,
            version=version,
        )
        out_file = os.path.abspath(
            os.path.join(
                config.output_dir_energy[instance_id],
                f"{config.template_warmup_file.format(id=run_id, label=version.upper())}",
            )
        )

        cmd = get_test_cmd(
            class_name, classpath=config.classpaths[instance_id], verbose=verbose
        )
        measure(
            command=cmd,
            out_file=out_file,
            cwd=config.system_paths[instance_id],
            repetitions=config.repetitions,
            verbose=verbose,
        )
        cooldown(config.warmup_cooldown)


def shuffle_runs(config: Config, instance_id: str):
    runs = []
    for _ in range(1, config.runs + 1):
        runs.append(
            (
                config.label_original,
                get_full_test_path(config, instance_id, config.label_original),
            )
        )
        runs.append(
            (
                config.label_refactored,
                get_full_test_path(config, instance_id, config.label_refactored),
            )
        )
    rng = random.Random()
    rng.shuffle(runs)
    return runs


def run(config: Config, instance_id: str, runs: list, verbose=False):
    for run_id, (version, class_name) in enumerate(runs, 1):
        Log.info(
            f"[RUN {run_id}/{len(runs)}]",
            level=1,
            instance_id=instance_id,
            version=version,
        )
        out_file = os.path.abspath(
            os.path.join(
                config.output_dir_energy[instance_id],
                f"{config.template_run_file.format(id=run_id, label=version.upper())}",
            )
        )

        cmd = get_test_cmd(
            class_name, classpath=config.classpaths[instance_id], verbose=verbose
        )
        measure(
            command=cmd,
            out_file=out_file,
            cwd=config.system_paths[instance_id],
            repetitions=config.repetitions,
            verbose=verbose,
        )
        if run_id < len(runs):  # skip cooldown after last run
            cooldown(config.run_cooldown)
        else:
            Log.info("Skipping cooldown", level=1, instance_id=instance_id)


def pipeline_energy(config: Config, skip_prep=False, skip_build=False, verbose=False):
    def ensure_dependency_classpath(instance_id: str):
        deps_dir = os.path.join(
            config.system_paths[instance_id], "target", "dependency"
        )
        if not os.path.isdir(deps_dir):
            raise FileNotFoundError(
                f"Missing dependency directory for {instance_id}: {deps_dir}. "
                "Run build (mvn package + dependency:copy-dependencies) before java -cp runs."
            )

        has_jars = any(name.lower().endswith(".jar") for name in os.listdir(deps_dir))
        if not has_jars:
            raise FileNotFoundError(
                f"Dependency directory is empty for {instance_id}: {deps_dir}. "
                "No jars available for java -cp runtime classpath."
            )

    try:
        Log.command("Preparing system")
        if not skip_prep:
            manage_system("prepare")
            cooldown(config.system_cooldown)  # wait for stable
        else:
            Log.warning("Skipping", level=1)

        Log.command("Starting RAPL driver")
        manage_driver(config, "start")

        Log.command(f"Building system{'s' if len(config.instance_ids) > 1 else ''}")
        if not skip_build:
            for i, instance_id in enumerate(config.instance_ids, 1):
                Log.info(
                    f"BUILD [{i}/{len(config.instance_ids)}]",
                    level=1,
                    instance_id=instance_id,
                )
                build_system(config, instance_id, verbose=verbose)
                ensure_dependency_classpath(instance_id)
            cooldown(config.system_cooldown)  # let mvn cleanup
        else:
            Log.warning("Skipping", level=1)
            for instance_id in config.instance_ids:
                ensure_dependency_classpath(instance_id)

        for instance_id in config.instance_ids:
            os.makedirs(config.output_dir_energy[instance_id], exist_ok=True)
            os.makedirs(
                os.path.join(config.output_dir_energy[instance_id], "raw"),
                exist_ok=True,
            )

            Log.command(f"Measuring baseline", instance_id=instance_id)
            measure_baseline(config, instance_id, verbose=verbose)

            Log.command(f"Running warmup runs", instance_id=instance_id)
            warmup_runs(config, instance_id, verbose=verbose)
            cooldown(config.system_cooldown)

            runs = shuffle_runs(config, instance_id)
            Log.command(f"Running {len(runs)} test runs", instance_id=instance_id)
            run(config, instance_id, runs, verbose=verbose)

            Log.command(f"Collecting results", instance_id=instance_id)
            collect_results(config, config.output_dir_energy[instance_id])

            if len(config.instance_ids) > 1:
                cooldown(config.system_cooldown)
    except KeyboardInterrupt:
        Log.error(f"Pipeline interrupted by user")
    except Exception as e:
        Log.error(f"Pipeline failed: {type(e).__name__}: {e}")
    finally:
        Log.command("Stopping RAPL driver")
        try:
            manage_driver(config, "stop")
        except Exception as e:
            Log.error(f"Failed: {type(e).__name__}: {e}", level=1)
        if not skip_prep:
            Log.command("Restoring system")
            try:
                manage_system("restore")
            except Exception as e:
                Log.error(f"Failed: {type(e).__name__}: {e}", level=1)


if __name__ == "__main__":
    Log.debug("Loading configs")
    try:
        args = parse_args()
        skip_prep = args.skip_prep

        TEST_CONFIG = "config/exp_test.yaml"

        exp_config = args.exp_config
        if exp_config is None:
            Log.warning("No experiment config provided", level=1)
            Log.command("Using test config", level=1)
            exp_config = TEST_CONFIG

        if exp_config == TEST_CONFIG:
            skip_prep = True

        config = Config.from_yaml(exp_config, args.target_config, args.paths_config)
    except Exception as e:
        Log.error(f"Failed: {type(e).__name__}: {e}", level=1)
        exit(1)

    Log.info("Starting pipeline")
    pipeline_energy(
        config, skip_prep=skip_prep, skip_build=args.skip_build, verbose=args.verbose
    )

    # open vscode when finished
    # run_command("code .")
