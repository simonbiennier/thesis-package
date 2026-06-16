# Replication package: Is cleaner greener? On the energy impact of refactored test smells

This repository contains the full replication package for the paper "_Is cleaner greener? On the energy impact of refactored test smells_". It includes the data, scripts, and configuration files needed to reproduce the analysis and, where possible, the experimental pipelines.

## Repository layout

```
thesis-package/
├── config/
│   ├── paths.yaml           # local path configuration
│   ├── target.yaml          # experiment settings
│   ├── exp_full.yaml        # full experiment configuration
│   └── exp_test.yaml        # test experiment configuration
├── data/
│   ├── artifacts/           # generated figures and tables
│   ├── detection/           # tsDetect outputs per system
│   ├── instances/           # original and refactored test code (prefixed AA_)
│   ├── results/             # precomputed energy and quality results
│   │   ├── energy/          # raw and processed energy measurements
│   │   └── quality/         # coverage and mutation analysis results
│   ├── systems.csv          # system metadata
│   ├── instances.csv        # instance metadata
│   └── instances_text.csv   # smell descriptions and refactorings
├── scripts/
│   ├── analysis.ipynb       # statistical analysis and figure generation
│   ├── pipeline.py          # energy measurement pipeline (Energibridge)
│   ├── pipeline_quality.py  # test quality pipeline (JaCoCo/PIT)
│   ├── detect.py            # tsDetect runner
│   ├── metrics.py           # system-level metrics computation
│   ├── analysis_utils.py    # analysis helpers
│   ├── theme.py             # visualisation helpers
│   └── utils.py             # shared pipeline utilities
├── systems/                 # source code for the eight systems under study
├── tools/                   # external tools and drivers
├── requirements.txt         # Python package dependencies
└── README.md                # README
```

## Hardware requirements

For analysis-only runs:

- Any operating system with Python 3.10 or later

For the full energy pipeline:

- Windows 10 or 11 for energy measurements (RAPL driver plus LibreHardwareMonitor)
- Intel CPU with RAPL support is recommended
- Administrator privileges to load the kernel driver

## Quick start for analysis only

Use the precomputed results to regenerate the figures and tables without running the experimental pipelines.

1. Install the Python dependencies: `pip install -r requirements.txt`
2. Open `scripts/analysis.ipynb`.
3. Run all cells.

Figures are written to `data/artifacts/figures/` and tables to `data/artifacts/tables/`.

## Full reproduction

### 1. Extract systems and configure paths

Download and extract the source code of the systems under test from our replication package and edit `config/paths.yaml` if your local folder structure differs. The defaults match the repository layout.

### 2. Install the required tools

Versions used in the study:

- Python 3.10.0
- Java JDK 17 or later
- JUnit Console Launcher 1.10.2
- Maven 3.9 or later
- [Energibridge](https://github.com/tdurieux/EnergiBridge) 0.0.7

### 3. Run the pipelines

Quality pipeline:

- `python scripts/pipeline_quality.py`

Energy pipeline:

- `python scripts/pipeline.py -f` for the full run used in the experiments
- `python scripts/pipeline.py -t` for the test run, which executes only once

Results are written to `data/results/*/quality/` and `data/results/*/energy/`.

### 4. Run the analysis

Open `scripts/analysis.ipynb` and run all cells.

## Pipeline details

### Energy pipeline (`scripts/pipeline.py`)

The energy pipeline measures the energy usage of each test case, comparing the original and refactored versions using Energibridge on Windows. Each test is executed multiple times in randomised order, with warm-up and cool-down phases to reduce variance.

High-level flow:

1. System preparation, if enabled

- `manage_system("prepare")` disables configured noise sources such as background tasks.

2. Start the RAPL and energy driver

- `manage_driver(config, "start")` loads the kernel driver through LibreHardwareMonitor.

3. Build each system

- `build_system(..)` invokes Maven and copies dependencies.
- `ensure_dependency_classpath(..)` checks that `target/dependency` contains the required JARs for the runtime classpath.

4. Measure the baseline

- `measure_baseline(..)` runs a fixed-duration idle command (`timeout /t N`) to capture the baseline power draw.

5. Run warm-up trials

- `warmup_runs(..)` alternates original and refactored tests for the configured number of runs.
- Each run executes the test via `java -cp` with the system classpath.

6. Run the shuffled measurements

- `shuffle_runs(..)` creates a randomised list of original and refactored tests.
- `run(..)` executes each test and records energy usage.

7. Collect the results

- `collect_results(..)` merges Energibridge output into per-instance CSV summaries.

8. Clean up

- `manage_driver(config, "stop")` unloads the driver.
- `manage_system("restore")` reverts any system changes.

Implementation details:

- Energibridge is invoked as `energibridge -o <output.csv> -- <command>`.
- When repetitions are greater than 1, the command is wrapped in a Windows `FOR` loop so the same test is executed multiple times under one measurement.
- Tests are executed with the JUnit Console Launcher and an explicit classpath assembled in `utils.py`.
- Output files use the templates defined in the configuration (`baseline`, `warmup`, `run`). Each run produces a raw CSV file and a merged summary.

Key configuration options in `config/exp_full.yaml`, `config/exp_test.yaml`, and `config/target.yaml`:

- `baseline_duration`, `baseline_runs`
- `warmup_runs`, `warmup_cooldown`
- `runs`, `run_cooldown`
- `repetitions` per test execution
- `instance_ids` for selecting which instances to run

### Quality pipeline (`scripts/pipeline_quality.py`)

The quality pipeline computes coverage with JaCoCo and mutation score with PIT for the original and refactored tests of each instance.

High-level flow:

1. Patch `pom.xml` for each system

- `prepare_pom_xml(..)` injects the JaCoCo and PIT plugins if they are missing.
- It also ensures that Surefire uses `@{argLine}` so the JaCoCo agent is attached correctly.

2. Measure coverage

- Runs: `mvn clean test jacoco:report -Dtest=<TestClass>`
- Moves the generated `target/site/jacoco` report into a per-instance `quality_reports` directory.

3. Run mutation analysis

- Runs: `mvn pitest:mutationCoverage`
- Passes `-Dpit.targetClasses` and `-Dpit.targetTests` to scope the mutants and tests.
- Moves `target/pit-reports` into the same results folder.

4. Extract metrics and summarise

- `get_jacoco_metrics(..)` parses `index.html` (`tfoot`) for instruction and branch counts.
- `get_pit_metrics(..)` parses PIT `index.html` for killed and total mutants, as well as mutants covered.
- Results are written to a CSV summary for each instance.

Implementation details:

- JaCoCo coverage uses the test class as the unit of execution, not the full test suite.
- PIT uses the classes listed in `config/target.yaml` for `targetClasses` and the test class for `targetTests`.
- Reports are stored under `<system>/quality_reports` with per-version subfolders for the original and refactored tests.

Key configuration options:

- `targets`: list of target production classes per instance
- `instance_ids`: which instances to run

## Expected runtime

Runtime depends on the target systems and hardware. On the original study machine:

- Test configuration (`config/exp_test.yaml`): minutes per instance
- Full configuration (`config/exp_full.yaml`): 30+ minutes per instance
