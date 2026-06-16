import subprocess
from pathlib import Path
import csv

RUN_DIR = Path.cwd()
OUT_DIR = RUN_DIR / "output"
TOOLS_DIR = RUN_DIR / "tools"

TEST_FILE_DETECTOR = TOOLS_DIR / "TestFileDetector.jar"
TEST_FILE_MAPPING = TOOLS_DIR / "TestFileMapping.jar"
TEST_SMELL_DETECTOR = TOOLS_DIR / "TestSmellDetector.jar"


def build_mapping_input(detector_csv: Path, module: Path, out_dir: Path):
    """Constructs the mapping input CSV for TestFileMapping based on the
    output from TestFileDetector.

    TestFileMapping requires a CSV with columns:
    - path of src/main (eg. C:\system\src\main)
    - path of test file (eg. C:\system\src\test\MyTest.java)
    """
    mapping_input = out_dir / "mapping_input.csv"
    src_main_path = module / "src" / "main"

    with open(detector_csv, newline="", encoding="utf-8") as inp, open(
        mapping_input, "w", newline="", encoding="utf-8"
    ) as out:
        reader = csv.DictReader(inp)
        writer = csv.writer(out)
        # writer.writerow(["pathToProdDir", "pathToTestFile"])

        for row in reader:
            test_path = Path(row["FilePath"]).resolve()
            writer.writerow([src_main_path.resolve(), test_path])

    return mapping_input


def build_smell_input(detector_csv: Path, mapping, module: Path, out_dir: Path):
    smell_input = out_dir / "smell_input.csv"
    app_name = module.name

    with open(detector_csv, newline="", encoding="utf-8") as inp, open(
        smell_input, "w", newline="", encoding="utf-8"
    ) as out:
        reader = csv.DictReader(inp)
        writer = csv.writer(out)
        # writer.writerow(["appName", "pathToTestFile", "pathToProductionFile"])

        for row in reader:
            test_file = Path(row["FilePath"]).resolve()
            prod_file = mapping.get(str(test_file))
            writer.writerow([app_name, test_file, prod_file])

    return smell_input


def is_submodule(path: Path) -> bool:
    return (path / "src" / "main").exists()


def has_tests(path: Path) -> bool:
    return (path / "src" / "test").exists()


def find_submodules(root: Path):
    return [
        p for p in root.rglob("*") if p.is_dir() and is_submodule(p) and has_tests(p)
    ]


def run(cmd):
    subprocess.run(cmd, shell=False, check=True)


def new_csvs(dir: Path):
    return list(dir.glob("Output_*.csv"))


def process_submodule(module: Path, root_name=None):
    module_name = module.name
    if root_name:
        out_dir = (OUT_DIR / root_name / module_name).resolve()
    else:
        out_dir = (OUT_DIR / module_name).resolve()
    out_dir.mkdir(parents=True, exist_ok=True)

    print(f"\nProcessing submodule: {module_name}")

    # run TestFileDetector
    run(
        [
            "java",
            "-jar",
            str(TEST_FILE_DETECTOR),
            str(module),
        ]
    )

    created = new_csvs(RUN_DIR)
    if not created:
        print("  No test files detected.")
        return
    print(f"  Test file detection completed.")

    # Output_Class, Output_Debt, Output_Method
    for c in created:
        new_path = c.replace(out_dir / c.name)
        if "Output_Class" in c.name:
            class_csv = new_path

    # build mapping
    mapping_input_csv = build_mapping_input(class_csv, module, out_dir)
    run(
        [
            "java",
            "-jar",
            str(TEST_FILE_MAPPING),
            str(mapping_input_csv),
        ]
    )
    created = new_csvs(RUN_DIR)
    for c in created:
        new_path = c.replace(out_dir / c.name)
        if "Mapping" in c.name:
            mapping_csv = new_path
    print("  Test mapping completed.")

    mapping = {}
    with open(mapping_csv, newline="", encoding="utf-8") as f:
        reader = csv.reader(f)
        for row in reader:
            mapping[row[0]] = row[1]

    # run smell detection
    smell_input_csv = build_smell_input(class_csv, mapping, module, out_dir)
    run(
        [
            "java",
            "-jar",
            str(TEST_SMELL_DETECTOR),
            str(smell_input_csv),
            "-t",
            "spadini", # use severity thresholds from Spadini et al. (2020)
        ]
    )

    created = new_csvs(RUN_DIR)
    for c in created:
        c.replace(out_dir / c.name)
    print("  Test smell detection completed.")


def main(root_dir):
    root = Path(root_dir).resolve()
    root_name = root.name.replace("-main", "").replace("-master", "").replace(" ", "_")
    submodules = find_submodules(root)

    if not submodules:
        # check if root itself is a submodule
        if is_submodule(root) and has_tests(root):
            submodules = [root]
        else:
            print("No submodules found.")
            return

    print(f"Found {len(submodules)} submodules.")
    if len(submodules) == 1:
        process_submodule(submodules[0])
    else:
        for module in submodules:
            process_submodule(module, root_name=root_name)

        # combine csvs from all submodules into one
        combined_out_dir = OUT_DIR / root_name / "combined"
        combined_out_dir.mkdir(parents=True, exist_ok=True)
        combined_file = combined_out_dir / "Output_TestSmellDetection.csv"
        with open(combined_file, "w+", encoding="utf-8") as out:
            out.write(
                "App,TestClass,TestFilePath,ProductionFilePath,RelativeTestFilePath,RelativeProductionFilePath,NumberOfMethods,Assertion Roulette,Conditional Test Logic,Constructor Initialization,Default Test,EmptyTest,Exception Catching Throwing,General Fixture,Mystery Guest,Print Statement,Redundant Assertion,Sensitive Equality,Verbose Test,Sleepy Test,Eager Test,Lazy Test,Duplicate Assert,Unknown Test,IgnoredTest,Resource Optimism,Magic Number Test,Dependent Test,\n"
            )  # create/clear file with header

        for module in submodules:
            module_name = module.name
            mod_out_dir = OUT_DIR / root_name / module_name

            for csv_file in mod_out_dir.glob("Output_TestSmellDetection*.csv"):
                with open(csv_file, "r", encoding="utf-8") as inp, open(
                    combined_file, "a", encoding="utf-8"
                ) as out:
                    next(inp)  # skip header
                    out.write(inp.read())


if __name__ == "__main__":
    import sys

    if len(sys.argv) != 2:
        print("Usage: python run_test_smells.py <system_path>")
        sys.exit(1)

    main(sys.argv[1])
