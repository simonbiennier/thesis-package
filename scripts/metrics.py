import os
import re
import sys
import argparse


JAVA_TYPE_DECLARATION_PATTERN = re.compile(
    r"(?m)^\s*(?:(?:public|protected|private|abstract|final|static|sealed|non-sealed|strictfp)\s+)*"
    r"(?:class)\s+\w+"
)


def is_test_source(file_path):
    """Return True when a Java file belongs to test sources."""
    normalized = file_path.replace("\\", "/").lower()
    file_name = os.path.basename(normalized)

    return (
        "/src/test/" in normalized
        or file_name.endswith("test.java")
        or file_name.endswith("tests.java")
    )


def count_java_type_declarations(source):
    """Count class/interface/enum/record declarations in a Java source file."""
    return len(JAVA_TYPE_DECLARATION_PATTERN.findall(source))


def measure_src_folders_size_mb(root_dir):
    """Measure total size (MB) of folders whose name contains 'src'."""
    total_bytes = 0

    for root, dirs, _ in os.walk(root_dir):
        src_dirs = [d for d in dirs if "src" in d.lower()]

        for src_dir in src_dirs:
            src_path = os.path.join(root, src_dir)
            for sub_root, _, files in os.walk(src_path):
                for file_name in files:
                    file_path = os.path.join(sub_root, file_name)
                    try:
                        total_bytes += os.path.getsize(file_path)
                    except OSError:
                        # ignore unreadable/missing files
                        pass

        # avoid descending into matched src folders (prevents double-counting)
        dirs[:] = [d for d in dirs if d not in src_dirs]

    return round(total_bytes / (1024 * 1024), 2)


def measure_java_system(root_dir):
    metrics = {
        "Classes": 0,
        "KLOC": 0,
        "JUnit_Classes": 0,
        "JUnit_Methods": 0,
        "JUnit_KLOC": 0,
        "Source_MB": 0,
    }

    metrics["Source_MB"] = measure_src_folders_size_mb(root_dir)

    # regex patterns
    junit_method_pattern = re.compile(r"@Test")  # JUnit 4/5

    for root, dirs, files in os.walk(root_dir):
        for file in files:
            if file.endswith(".java"):
                file_path = os.path.join(root, file)
                is_test_file = is_test_source(file_path)

                with open(file_path, "r", encoding="utf-8", errors="ignore") as f:
                    lines = f.readlines()
                    code_lines = [
                        l
                        for l in lines
                        if l.strip() and not l.strip().startswith(("//", "/*", "*"))
                    ]
                    line_count = len(code_lines)

                    # Update KLOC (Total vs Test)
                    metrics["KLOC"] += line_count
                    if is_test_file:
                        metrics["JUnit_KLOC"] += line_count

                    content = "".join(lines)
                    class_count = count_java_type_declarations(content)
                    if is_test_file:
                        metrics["JUnit_Classes"] += class_count
                    else:
                        metrics["Classes"] += class_count

                    if is_test_file:
                        metrics["JUnit_Methods"] += len(
                            junit_method_pattern.findall(content)
                        )

    # convert to KLOC
    metrics["KLOC"] = round(metrics["KLOC"] / 1000)
    metrics["JUnit_KLOC"] = round(metrics["JUnit_KLOC"] / 1000)

    return metrics


def measure_junit3_system(root_dir):
    metrics = {
        "Classes": 0,
        "KLOC": 0,
        "JUnit_Classes": 0,
        "JUnit_Methods": 0,
        "JUnit_KLOC": 0,
        "Source_MB": 0,
    }

    metrics["Source_MB"] = measure_src_folders_size_mb(root_dir)

    # patterns for JUnit 3
    junit3_class_pattern = re.compile(
        r"class\s+\w+\s+extends\s+(?:junit\.framework\.)?TestCase"
    )
    junit3_method_pattern = re.compile(r"public\s+void\s+test\w+\s*\(")

    for root, dirs, files in os.walk(root_dir):
        for file in files:
            if file.endswith(".java"):
                file_path = os.path.join(root, file)

                with open(file_path, "r", encoding="utf-8", errors="ignore") as f:
                    lines = f.readlines()
                    code_lines = [
                        l
                        for l in lines
                        if l.strip() and not l.strip().startswith(("//", "/*", "*"))
                    ]
                    line_count = len(code_lines)

                    metrics["KLOC"] += line_count

                    content = "".join(lines)

                    # JUnit 3 test class (extends TestCase)
                    if junit3_class_pattern.search(content):
                        metrics["JUnit_Classes"] += 1
                        metrics["JUnit_Methods"] += len(
                            junit3_method_pattern.findall(content)
                        )
                        metrics["JUnit_KLOC"] += line_count
                        metrics["Classes"] += 1
                    else:
                        metrics["Classes"] += count_java_type_declarations(content)

    metrics["KLOC"] = round(metrics["KLOC"] / 1000)
    metrics["JUnit_KLOC"] = round(metrics["JUnit_KLOC"] / 1000)

    return metrics


def parse_args(argv):
    parser = argparse.ArgumentParser(description="Measure Java system metrics.")
    parser.add_argument("system_path", help="Path to the Java system")
    parser.add_argument(
        "--junit3",
        action="store_true",
        help="Use JUnit 3 detection (TestCase + test* methods)",
    )
    return parser.parse_args(argv)


if __name__ == "__main__":
    args = parse_args(sys.argv[1:])
    if args.junit3:
        results = measure_junit3_system(args.system_path)
    else:
        results = measure_java_system(args.system_path)

    print("--- System metrics ---")
    for key, value in results.items():
        print(f"{key}: {value}")
    print(",".join(str(v) for v in results.values()))
