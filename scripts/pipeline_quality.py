import xml.etree.ElementTree as ET
import subprocess as sub
import pandas as pd
import shutil
import os

from utils import (
    MVN_FLAGS,
    Config,
    Log,
    _get_timestamp,
    _log,
    console,
    get_full_test_path,
    parse_args,
    run_command,
)
from bs4 import BeautifulSoup


def prepare_pom_xml(system_path: str, instance_id: str):
    pom_path = os.path.join(system_path, "pom.xml")

    # register standard mvn namespace
    ET.register_namespace("", "http://maven.apache.org/POM/4.0.0")
    tree = ET.parse(pom_path)
    root = tree.getroot()
    ns = {"mvn": "http://maven.apache.org/POM/4.0.0"}

    # locate <build><plugins> section
    build = root.find("mvn:build", ns)
    if build is None:
        build = ET.SubElement(root, "build")

    plugins = build.find("mvn:plugins", ns)
    if plugins is None:
        plugins = ET.SubElement(build, "plugins")

    # jacoco (coverage)
    jacoco_xml = """
    <plugin>
      <groupId>org.jacoco</groupId>
      <artifactId>jacoco-maven-plugin</artifactId>
      <version>0.8.11</version>
      <executions>
        <execution>
          <id>prepare-agent</id>
          <goals>
            <goal>prepare-agent</goal>
          </goals>
        </execution>
        <execution>
          <id>report</id>
          <phase>test</phase>
          <goals>
            <goal>report</goal>
          </goals>
        </execution>
      </executions>
    </plugin>
    """

    # pitest (mutation)
    # ${pit.targetTests} to switch classes via command line
    pitest_xml = """
    <plugin>
      <groupId>org.pitest</groupId>
      <artifactId>pitest-maven</artifactId>
      <version>1.15.3</version>
      <configuration>
        <targetClasses>${pit.targetClasses}</targetClasses>
        <targetTests>${pit.targetTests}</targetTests>
        <threads>4</threads>
      </configuration>
    </plugin>
    """

    def inject_if_missing(plugin_xml):
        new_plugin = ET.fromstring(plugin_xml)
        artifact_id = new_plugin.find("artifactId").text

        # check if exists
        exists = any(
            p.find("mvn:artifactId", ns).text == artifact_id
            for p in plugins.findall("mvn:plugin", ns)
            if p.find("mvn:artifactId", ns) is not None
        )

        if not exists:
            plugins.append(new_plugin)
            Log.success(f"{artifact_id} injected", level=1, instance_id=instance_id)
        else:
            Log.warning(
                f"{artifact_id} already present", level=1, instance_id=instance_id
            )

    inject_if_missing(jacoco_xml)
    inject_if_missing(pitest_xml)

    # ensure pitest plugin uses direct property mapping for filters.
    # this allows comma-separated values in -Dpit.targetClasses to be interpreted as multiple classes.
    def ensure_pitest_filter_properties():
        def local_name(tag: str) -> str:
            return tag.split("}", 1)[-1] if "}" in tag else tag

        def find_plugin_by_artifact_id(artifact_id: str):
            for plugin in list(plugins):
                for child in list(plugin):
                    if (
                        local_name(child.tag) == "artifactId"
                        and child.text == artifact_id
                    ):
                        return plugin
            return None

        def find_child(parent, name: str):
            for child in list(parent):
                if local_name(child.tag) == name:
                    return child
            return None

        pitest_plugin = find_plugin_by_artifact_id("pitest-maven")
        if pitest_plugin is None:
            return

        config_elem = find_child(pitest_plugin, "configuration")
        if config_elem is None:
            config_elem = ET.SubElement(pitest_plugin, "configuration")

        for child in list(config_elem):
            if local_name(child.tag) in {"targetClasses", "targetTests"}:
                config_elem.remove(child)

        target_classes = ET.SubElement(config_elem, "targetClasses")
        target_classes.text = "${pit.targetClasses}"

        target_tests = ET.SubElement(config_elem, "targetTests")
        target_tests.text = "${pit.targetTests}"

        if find_child(config_elem, "threads") is None:
            threads = ET.SubElement(config_elem, "threads")
            threads.text = "4"

    ensure_pitest_filter_properties()

    # ensure Surefire uses @{argLine} so the JaCoCo agent is actually attached. JaCoCo's prepare-agent sets the "argLine" Maven property, but Surefire only passes it to the JVM if its <argLine> config references @{argLine}. Without this, no jacoco.exec file is created and jacoco:report skips silently.
    def ensure_surefire_argline():
        surefire = None
        for p in plugins.findall("mvn:plugin", ns):
            artifact = p.find("mvn:artifactId", ns)
            if artifact is not None and artifact.text == "maven-surefire-plugin":
                surefire = p
                break

        if surefire is None:
            surefire_xml = """
    <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-surefire-plugin</artifactId>
      <configuration>
        <argLine>@{argLine}</argLine>
      </configuration>
    </plugin>
    """
            plugins.append(ET.fromstring(surefire_xml))
            Log.success(
                "maven-surefire-plugin injected with @{argLine}",
                level=1,
                instance_id=instance_id,
            )
        else:
            config_elem = surefire.find("mvn:configuration", ns)
            if config_elem is None:
                config_elem = ET.SubElement(surefire, "configuration")
            argline_elem = config_elem.find("mvn:argLine", ns)
            if argline_elem is None:
                argline_elem = ET.SubElement(config_elem, "argLine")
                argline_elem.text = "@{argLine}"
                Log.success(
                    "maven-surefire-plugin: argLine set to @{argLine}",
                    level=1,
                    instance_id=instance_id,
                )
            elif "@{argLine}" not in (argline_elem.text or ""):
                argline_elem.text = f"@{{argLine}} {argline_elem.text or ''}".strip()
                Log.success(
                    "maven-surefire-plugin: @{argLine} prepended to existing argLine",
                    level=1,
                    instance_id=instance_id,
                )
            else:
                Log.warning(
                    "maven-surefire-plugin: @{argLine} already present",
                    level=1,
                    instance_id=instance_id,
                )

    ensure_surefire_argline()

    # save
    tree.write(pom_path, encoding="utf-8", xml_declaration=True)
    # Log.success("pom.xml updated", instance_id=instance_id)


def get_jacoco_metrics(report_path):
    """
    Extracts absolute instruction and branch counts from JaCoCo tfoot.
    Returns: (inst, inst_total, br, br_total)
    """
    index_file = os.path.join(report_path, "index.html")
    # default vals
    data = {
        "inst": 0,
        "inst_total": 0,
        "br": 0,
        "br_total": 0,
    }

    if not os.path.exists(index_file):
        return data

    with open(index_file, "r", encoding="utf-8") as f:
        soup = BeautifulSoup(f, "html.parser")

    # jacoco puts totals in the tfoot section
    footer = soup.find("tfoot")
    if not footer:
        return data

    # find all cells in the footer row
    cells = footer.find_all("td")

    # helper to parse "72,724 of 76,475" style strings
    def parse_bar_cell(cell):
        text = cell.get_text(strip=True)
        if "of" in text:
            parts = text.replace(",", "").split(" of ")
            missed = int(parts[0])
            total = int(parts[1])
            return total - missed, total
        return 0, 0

    # instructions are typically the second cell (index 1) with class "bar"
    if len(cells) > 1:
        data["inst"], data["inst_total"] = parse_bar_cell(cells[1])

    # branches are typically the fourth cell (index 3) with class "bar"
    if len(cells) > 3:
        data["br"], data["br_total"] = parse_bar_cell(cells[3])

    return data


def get_pit_metrics(report_path):
    """
    Extracts absolute mutation counts from PIT index.html using BeautifulSoup.
    Returns: dict with killed, total, and covered mutants.
    """
    actual_index = None
    for root, dirs, files in os.walk(report_path):
        if "index.html" in files and "org.apache.commons" not in root:
            actual_index = os.path.join(root, "index.html")
            break

    data = {"mut": 0, "mut_cov": 0, "mut_total": 0}

    if not actual_index or not os.path.exists(actual_index):
        return data

    with open(actual_index, "r", encoding="utf-8") as f:
        soup = BeautifulSoup(f, "html.parser")

    # find the 'Project Summary' table (the first table in the report)
    summary_table = soup.find("table")
    if not summary_table:
        return data

    headers = [th.get_text(strip=True) for th in summary_table.find_all("th")]
    # the values are in the first (and only) row of the tbody
    values = summary_table.find("tbody").find_all("td")

    for i, header in enumerate(headers):
        # we look for the 'coverage_legend' div inside the specific <td>
        legend = values[i].find("div", class_="coverage_legend")
        if not legend:
            continue

        text = legend.get_text(strip=True)  # 130/142
        try:
            numerator = int(text.split("/")[0])
            denominator = int(text.split("/")[1])

            if "Mutation Coverage" in header:
                data["mut"] = numerator
                data["mut_total"] = denominator
            elif "Test Strength" in header:
                data["mut_cov"] = denominator  # total mutants reached by tests
        except (ValueError, IndexError):
            continue

    return data


def print_summary(results, instance_id: str):
    # print summary
    print("\n")
    _log(
        "bold blue",
        f"{'Id':<12} {'Version':<6} {'Inst':>8} {'Br':>8} {'Mut':>8} {'MutCov':>8}",
    )
    _log("bold blue", "-" * 69)

    inst_or = results[0][1]["inst"]
    inst_rf = results[1][1]["inst"]
    diff_inst = abs(inst_rf - inst_or)

    br_or = results[0][1]["br"]
    br_rf = results[1][1]["br"]
    diff_br = abs(br_rf - br_or)

    mut_or = results[0][2]["mut"]
    mut_rf = results[1][2]["mut"]

    diff_mut = abs(mut_rf - mut_or)

    mut_cov_or = results[0][2]["mut_cov"]
    mut_cov_rf = results[1][2]["mut_cov"]
    diff_mut_cov = abs(mut_cov_rf - mut_cov_or)

    _log(
        "bold white",
        f"{instance_id:<12} {config.label_original:<6} {inst_or:>8} {br_or:>8} {mut_or:>8} {mut_cov_or:>8}",
    )
    _log(
        "bold white",
        f"{instance_id:<12} {config.label_refactored:<6} {inst_rf:>8} {br_rf:>8} {mut_rf:>8} {mut_cov_rf:>8}",
    )

    console.print(
        f"{_get_timestamp()} {'':<12} {'':<6} [bold {'red' if diff_inst > 0 else 'green'}]{diff_inst:>8}[/bold {'red' if diff_inst > 0 else 'green'}] [bold {'red' if diff_br > 0 else 'green'}]{diff_br:>8}[/bold {'red' if diff_br > 0 else 'green'}] [bold {'red' if diff_mut > 0 else 'green'}]{diff_mut:>8}[/bold {'red' if diff_mut > 0 else 'green'}] [bold {'red' if diff_mut_cov > 0 else 'green'}]{diff_mut_cov:>8}[/bold {'red' if diff_mut_cov > 0 else 'green'}]"
    )


def pipeline_quality(config: Config, verbose=False):
    """Measures coverage and mutation for both original and refactored classes."""
    Log.info("Starting pipeline")

    for instance_id in config.instance_ids:
        Log.command("")
        Log.command("Updating pom.xml", instance_id=instance_id)
        prepare_pom_xml(config.system_paths[instance_id], instance_id)

        # maven_cwd, module_arg = get_maven_context(config, instance_id)
        module_cwd = config.system_paths[instance_id]

        tests = [
            (
                config.label_original,
                get_full_test_path(config, instance_id, config.label_original),
            ),
            (
                config.label_refactored,
                get_full_test_path(config, instance_id, config.label_refactored),
            ),
        ]
        os.makedirs(config.output_dir_quality[instance_id], exist_ok=True)

        results_dir = os.path.join(config.system_paths[instance_id], "quality_reports")
        if os.path.exists(results_dir):
            shutil.rmtree(results_dir)
        os.makedirs(results_dir)

        results = []
        for version, target_test in tests:
            # run jacoco on test class and generate report
            # -Dtest=ClassName runs all tests inside that class
            Log.command(
                "Calculating code coverage", instance_id=instance_id, version=version
            )
            coverage_cmd = (
                f"mvn clean test jacoco:report -Dtest={target_test} {MVN_FLAGS}"
            )
            if verbose:
                Log.command(
                    f"Running: {coverage_cmd}",
                    level=1,
                    instance_id=instance_id,
                    version=version,
                )
            run_command(
                coverage_cmd,
                cwd=module_cwd,
                verbose=verbose,
            )

            jacoco_dest = os.path.join(results_dir, f"jacoco_{version.lower()}")
            jacoco_src = os.path.join(
                config.system_paths[instance_id], "target", "site", "jacoco"
            )
            if os.path.exists(jacoco_src):
                os.rename(jacoco_src, jacoco_dest)
            else:
                Log.warning(
                    "JaCoCo report not found (execution data missing), skipping rename",
                    instance_id=instance_id,
                    version=version,
                )

            # run pitest mutation analysis
            # pass vars defined in pom.xml
            target_classes = ",".join(config.targets[instance_id])
            Log.command(
                "Calculating mutation score", instance_id=instance_id, version=version
            )
            mutation_cmd = (
                "mvn pitest:mutationCoverage "
                f"-Dpit.targetClasses={target_classes} "
                f"-Dpit.targetTests={target_test} "
                f"{MVN_FLAGS}"
            )
            if verbose:
                Log.command(
                    "Running mutation analysis",
                    level=1,
                    instance_id=instance_id,
                    version=version,
                )
            run_command(
                mutation_cmd,
                cwd=module_cwd,
                verbose=verbose,
            )

            pit_dest = os.path.join(results_dir, f"pit_{version.lower()}")
            pit_src = os.path.join(
                config.system_paths[instance_id], "target", "pit-reports"
            )
            os.rename(pit_src, pit_dest)

            cov = get_jacoco_metrics(jacoco_dest)
            mut = get_pit_metrics(pit_dest)
            results.append((version, cov, mut))

        if verbose:
            print_summary(results, instance_id)

        # save to csv
        summary_path = os.path.join(
            config.output_dir_quality[instance_id],
            config.summary_file_template.format(label="QUALITY"),
        )
        df = pd.DataFrame(
            [
                {
                    "id": instance_id,
                    "version": version,
                    "inst": cov["inst"],
                    "inst_total": cov["inst_total"],
                    "br": cov["br"],
                    "br_total": cov["br_total"],
                    "mut": mut["mut"],
                    "mut_cov": mut["mut_cov"],
                    "mut_total": mut["mut_total"],
                }
                for version, cov, mut in results
            ]
        )
        df.to_csv(summary_path, index=False)
        if verbose:
            Log.success(
                f"Summary saved in {summary_path}",
                instance_id=instance_id,
                version=version,
            )


if __name__ == "__main__":
    Log.debug("Loading configs")
    args = parse_args()

    TEST_CONFIG = "config/exp_test.yaml"

    exp_config = args.exp_config
    if exp_config is None:
        Log.warning("No experiment config provided", level=1)
        Log.command("Using test config", level=1)
        exp_config = TEST_CONFIG

    Log.command("Loading configs")
    config = Config.from_yaml(exp_config, args.target_config, args.paths_config)

    pipeline_quality(config, verbose=args.verbose)
