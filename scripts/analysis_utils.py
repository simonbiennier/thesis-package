import os
import re
import numpy as np
import pandas as pd
from plotly.subplots import make_subplots
from scipy.stats import linregress, shapiro, spearmanr, ttest_ind
import plotly.graph_objects as go
import yaml
from utils import ROOT_DIR, get_mz_stats, to_int_id, to_str_id
from theme import COLORS, COLORS_RGB, apply_chart_theme, rgba

df_instances = pd.read_csv("../data/instances.csv")
df_instances_text = pd.read_csv("../data/instances_text.csv")
df_systems = pd.read_csv("../data/systems.csv")

results_path = "../data/results"

OR = "OR"
RF = "RF"
VERSIONS = [OR, RF]

TYPE_MAP = {
    "CTL": "Conditional test logic",
    "DT": "Default test",
    "DA": "Duplicate assert",
    "EmT": "Empty test",
    "EmT/f": "Empty test (fixtures)",
    "ET": "Eager test",
    "GF": "General fixture",
    "IgT": "Ignored test",
    "LT/4": "Lazy test (JUnit~4)",
    "LT/5": "Lazy test (JUnit~5)",
    "MG": "Mystery guest",
    "RA": "Redundant assert",
    "RP": "Redundant print",
    "UT": "Unknown test",
}
TYPE_KEYS = list(TYPE_MAP.keys())
TYPE_VALUES = list(TYPE_MAP.values())

KEY_SMELLS = [
    "ET",
    "GF",
    "IgT",
    "LT/4",
    "LT/5",
]

KEY_SMELLS_FULL = [TYPE_MAP[smell] for smell in KEY_SMELLS]

METRICS = {
    "energy": "Energy",
    "duration": "Duration",
    "dram_energy": "DRAM Energy",
    "memory": "Memory usage",
    "cpu_usage": "CPU Usage",
}

FALLBACKS = {
    "cpu_usage": "cpu_usage_mean",
    "memory": "memory_delta",
}

RENDER_KEYS = {
    "fmt",
    "bold",
    "italic",
    "threshold_min",
    "threshold_max",
    "min_val",
    "max_val",
    "abs_threshold",
    "abs",
    "show_sign",
    "thousands",
}

OR = "OR"
RF = "RF"
VERSIONS = [OR, RF]

COLS = [
    "energy",
    # "energy_delta",
    "dram_energy",
    "power",
    "cpu_usage_mean",
    "memory_mean",
    # "memory_median",
    "memory_delta",
    # "cpu_usage_median",
]


def build_col_plot(data_raw: pd.DataFrame, n_cols=3):
    x = "run_id"
    nrows = (len(COLS) + n_cols - 1) // n_cols
    subplot_titles = [f"{col} over {x}" for col in COLS]

    fig = make_subplots(
        rows=nrows,
        cols=n_cols,
        subplot_titles=subplot_titles,
        # shared_xaxes=True,
        vertical_spacing=0.12,
        horizontal_spacing=0.12,
    )

    for i, col in enumerate(COLS):
        row = (i // n_cols) + 1
        c = (i % n_cols) + 1

        df = data_raw[[x, col]].dropna().copy()
        if col == "energy":
            df = df[df["energy"] > 0].copy()
        status_text = "Insufficient data"
        status_color = COLORS.SUCCESS

        if df[x].nunique() > 1 and len(df) >= 3:
            x_fit = df[x].astype(float).to_numpy()
            y_fit = df[col].astype(float).to_numpy()

            slope, intercept, r_val, p_val, std_err = linregress(x_fit, y_fit)
            x_line = np.linspace(x_fit.min(), x_fit.max(), 100)
            y_line = slope * x_line + intercept

            n = len(x_fit)
            y_hat = slope * x_fit + intercept
            residuals = y_fit - y_hat
            s_err = np.sqrt(np.sum(residuals**2) / (n - 2))
            x_mean = np.mean(x_fit)
            sxx = np.sum((x_fit - x_mean) ** 2)

            if sxx > 0:
                se_fit = s_err * np.sqrt((1 / n) + ((x_line - x_mean) ** 2) / sxx)
                ci = 1.96 * se_fit
                y_upper = y_line + ci
                y_lower = y_line - ci

                # plot 95% confidence band
                fig.add_trace(
                    go.Scatter(
                        x=np.concatenate([x_line, x_line[::-1]]),
                        y=np.concatenate([y_upper, y_lower[::-1]]),
                        fill="toself",
                        fillcolor=rgba(COLORS_RGB.MUTED, alpha=0.2),
                        line=dict(width=0),
                        hoverinfo="skip",
                        showlegend=False,
                        name=f"ci_{col}",
                    ),
                    row=row,
                    col=c,
                )

            # plot reg line
            fig.add_trace(
                go.Scatter(
                    x=x_line,
                    y=y_line,
                    mode="lines",
                    line=dict(color=rgba(COLORS_RGB.MUTED), width=2),
                    name=f"fit_{col}",
                    legendgroup=f"fit_{col}",
                    showlegend=False,
                    hoverinfo="skip",
                ),
                row=row,
                col=c,
            )

            is_stable = p_val > 0.05
            status_text = "Stable (p > 0.05)" if is_stable else "Drifting (p < 0.05)"
            status_color = COLORS.SUCCESS if is_stable else COLORS.ERROR

        # plot scatter last so points are above line and confidence band
        for version in VERSIONS:
            subset = data_raw[data_raw["version"] == version][[x, col]].dropna().copy()
            name = "Original" if version == OR else "Refactored"

            if subset.empty:
                continue

            jitter = np.random.uniform(-0.2, 0.2, size=subset.shape[0])
            subset[x] = subset[x].astype(float) + jitter
            fig.add_trace(
                go.Scatter(
                    x=subset[x],
                    y=subset[col],
                    customdata=subset[["run_id"]],
                    mode="markers",
                    name=name,
                    legendgroup=name,
                    showlegend=(i == 0),
                    marker=dict(
                        size=6,
                        color=(COLORS.PRIMARY if version == OR else COLORS.SECONDARY),
                        opacity=0.8,
                    ),
                    hovertemplate=(
                        f"{x}=%{{customdata[0]}}, {col}=%{{y:.2f}}"
                        + f"<extra>{name}</extra>"
                    ),
                ),
                row=row,
                col=c,
            )

        title_index = i
        if title_index < len(fig.layout.annotations):
            fig.layout.annotations[title_index].text = status_text
            fig.layout.annotations[title_index].font = dict(color=status_color, size=14)

        col_map = {
            "energy": "Energy (J)",
            "power": "Power (W)",
            "dram_energy": "DRAM energy (J)",
            "memory_delta": "Memory delta (MB)",
            "memory_mean": "Memory mean (MB)",
            "cpu_usage_mean": "CPU usage mean (%)",
        }

        is_last_row = row == nrows
        fig.update_xaxes(
            showgrid=False,
            title_text="Run" if is_last_row else None,
            # title_font=dict(color=x_color),
            # tickfont=dict(color=x_color),
            # tickcolor=COLORS.MUTED,
            # minor_tickcolor=x_color,
            # ticks="outside",
            showticklabels=True if is_last_row else False,
            # linecolor=COLORS.MUTED,
            # showline=True,
            dtick=10,
            row=row,
            col=c,
        )
        fig.update_yaxes(title_text=col_map[col], row=row, col=c)

    return fig


def build_energy_plots(data_raw: pd.DataFrame, instance_id: str):
    fig = make_subplots(
        rows=1,
        cols=2,
        subplot_titles=["Including outliers", "Excluding outliers"],
        horizontal_spacing=0.2,
    )

    data = {k: {} for k in VERSIONS}
    data[OR]["df"] = data_raw[data_raw["version"] == OR]
    data[RF]["df"] = data_raw[data_raw["version"] == RF]

    data[OR]["center"], data[RF]["center"] = 0.90, 1.10
    data[OR]["pointpos"], data[RF]["pointpos"] = -0.45, 0.45

    # limit segments for split violin sides in left subplot
    data[OR]["limits_x0"], data[OR]["limits_x1"] = 0.62, 0.98
    data[RF]["limits_x0"], data[RF]["limits_x1"] = 1.02, 1.38

    for version in VERSIONS:
        label = "Original" if version == OR else "Refactored"
        dv = data[version]
        df = dv["df"]
        df = df[df["energy"] > 0].copy()

        # get stats
        dv["mz"] = get_mz_stats(df)
        dfmz = dv["mz"]["df"]

        # shapiro wilk on both z and mz
        _, dv["p_mz"] = shapiro(dfmz["energy"])

        outlier_ids = set(dv["mz"]["outliers"]["run_id"])
        is_outlier = df["run_id"].isin(outlier_ids).to_numpy()

        violin_color = COLORS.PRIMARY if version == OR else COLORS.SECONDARY
        x0_lim, x1_lim = dv["limits_x0"], dv["limits_x1"]

        # left frame, mz limits
        for l in ["upper", "lower"]:
            y_val = dv["mz"][f"{l}_limit"]
            fig.add_shape(
                type="line",
                x0=x0_lim,
                x1=x1_lim,
                y0=y_val,
                y1=y_val,
                xref="x",
                yref="y",
                layer="below",
                line=dict(color=COLORS.ERROR, width=1, dash="dot"),
            )

        fig.add_trace(
            go.Violin(
                x0=1,
                y=df["energy"],
                customdata=df[["run_id"]].to_numpy(),
                hovertemplate=f"run_id=%{{customdata[0]}}, energy=%{{y:.2f}}J"
                + f"<extra>{label}</extra>",
                legendgroup=label,
                scalegroup=label,
                name=label,
                side="negative" if version == OR else "positive",
                showlegend=True,
                line_color=violin_color,
                points=False,
            ),
            row=1,
            col=1,
        )

        jitter = 0.03
        x_center = dv["center"]
        x_jitter = np.random.uniform(-jitter, jitter, size=len(df))
        point_x = x_center + x_jitter

        fig.add_trace(
            go.Scatter(
                x=point_x,
                y=df["energy"],
                mode="markers",
                customdata=df[["run_id"]].to_numpy(),
                marker=dict(
                    color=np.where(is_outlier, COLORS.ERROR, violin_color).tolist(),
                    size=6,
                ),
                hovertemplate=f"run_id=%{{customdata[0]}}, energy=%{{y:.2f}}J<extra>{label}</extra>",
                showlegend=False,
            ),
            row=1,
            col=1,
        )

        # right frame, analysis (mz-filtered violins)
        fig.add_trace(
            go.Violin(
                x0=1,
                y=dfmz["energy"],
                customdata=dfmz[["run_id"]].to_numpy(),
                hovertemplate=f"run_id=%{{customdata[0]}}, energy=%{{y:.2f}}J<extra>{label}</extra>",
                legendgroup=label,
                scalegroup=label,
                name=label,
                side="negative" if version == OR else "positive",
                showlegend=False,
                line_color=violin_color,
                points="all",
                jitter=0.1,
                pointpos=dv["pointpos"],
            ),
            row=1,
            col=2,
        )

    # t-test on filtered data (right plot)
    _, p_t = ttest_ind(
        data[OR]["mz"]["df"]["energy"],
        data[RF]["mz"]["df"]["energy"],
        equal_var=False,
    )

    print(
        f"OR: {'normal' if data[OR]['p_mz'] >= 0.05 else 'not normal'} (p={data[OR]['p_mz']:.2f})",
    )
    print(
        f"RF: {'normal' if data[RF]['p_mz'] >= 0.05 else 'not normal'} (p={data[RF]['p_mz']:.2f})"
    )
    print(f"Significant: {p_t < 0.05} (p={p_t:.2f})")

    fig.update_traces(
        meanline_visible=True,
        box_visible=True,
        scalemode="width",
        width=0.5,
        selector=dict(type="violin"),
    )

    fig.update_xaxes(
        range=[0.6, 1.4],
        tickmode="array",
        tickvals=[1],
        ticktext=[str(to_int_id(instance_id))],
        row=1,
        col=1,
    )
    fig.update_xaxes(
        range=[0.6, 1.4],
        tickmode="array",
        tickvals=[1],
        ticktext=[str(to_int_id(instance_id))],
        row=1,
        col=2,
    )

    return fig


def check_batch(instances: list[str], verbose=False):
    for instance_id in instances:
        test_results_path = os.path.join(results_path, instance_id, "energy")
        subfolders = [f.path for f in os.scandir(test_results_path) if f.is_dir()]
        latest_subfolder = max(subfolders)

        if verbose:
            print(f"[{instance_id}] Pulling from {latest_subfolder}")

        df_or = pd.read_csv(os.path.join(latest_subfolder, f"summary_{OR}.csv"))
        df_rf = pd.read_csv(os.path.join(latest_subfolder, f"summary_{RF}.csv"))
        data_raw = pd.concat([df_or, df_rf], ignore_index=True)
        data_raw = data_raw[data_raw["energy"] > 0]

        data = {k: {} for k in VERSIONS}
        data[OR]["df"] = data_raw[data_raw["version"] == OR]
        data[RF]["df"] = data_raw[data_raw["version"] == RF]

        outliers = 0
        for version in VERSIONS:
            dv = data[version]
            df = dv["df"]

            # get stats
            dv["mz"] = get_mz_stats(df)
            dfmz = dv["mz"]["df"]

            # shapiro wilk
            _, dv["p_mz"] = shapiro(dfmz["energy"])
            if verbose:
                print(f"[{instance_id}] [{version}] p={dv['p_mz']:.2f}")

            outlier_ids = set(dv["mz"]["outliers"]["run_id"])
            if verbose:
                print(f"[{instance_id}] [{version}] {len(outlier_ids)} outliers")
            outliers += len(outlier_ids)

        # t-test on filtered data (right plot)
        _, p_t = ttest_ind(
            data[OR]["mz"]["df"]["energy"],
            data[RF]["mz"]["df"]["energy"],
            equal_var=False,
        )
        if verbose:
            print(f"[{instance_id}] p_t={p_t:.2f}")

        print(
            f"[{instance_id}] normal: {data[OR]['p_mz'] > 0.05}, {data[RF]['p_mz'] > 0.05} outliers: {outliers}"
        )


def check_instance(instance_id: str, folder=None):
    test_results_path = os.path.join(results_path, instance_id, "energy")
    subfolders = [f.path for f in os.scandir(test_results_path) if f.is_dir()]

    try:
        latest_subfolder = os.path.join(results_path, instance_id, "energy", folder)
        if not os.path.exists(latest_subfolder):
            raise FileNotFoundError(
                f"Specified folder {latest_subfolder} does not exist."
            )
    except:
        latest_subfolder = max(subfolders)

    print(f"Pulling from {latest_subfolder}")

    df_or = pd.read_csv(os.path.join(latest_subfolder, f"summary_OR.csv"))
    df_rf = pd.read_csv(os.path.join(latest_subfolder, f"summary_RF.csv"))
    df = pd.concat([df_or, df_rf], ignore_index=True)
    df = df[df["energy"] > 0]

    col_fig = build_col_plot(df, n_cols=2)
    col_fig = apply_chart_theme(
        col_fig,
        overrides={
            # "title": "Metric stability",
            "static_plot": True,
            "title": None,
            "static_plot": True,
            "margin_t": 80,
            "margin_b": 80,
            "y_showline": True,
            "y_tick_pos": "outside",
            "y_tick_len": 6,
        },
    )
    col_fig.update_layout(
        height=800,
        width=1000,
    )
    col_fig.show()

    dist_fig = build_energy_plots(df, instance_id)
    dist_fig = apply_chart_theme(
        dist_fig,
        overrides={
            # "title": "Energy variance",
            "static_plot": True,
            "y_title": "Energy (J)",
            "margin_t": 60,
            "margin_b": 20,
            "y_showline": True,
            "y_tick_pos": "outside",
            "y_tick_len": 6,
        },
    )

    dist_fig.update_xaxes(
        showticklabels=False,
        linecolor="white",
        ticks="",
    )
    # fig.update_yaxes(
    #     showline=True,
    #     linewidth=1,
    #     ticks="outside",
    #     # ticklen=6,
    # )

    # remove the y-axis title for the second column
    dist_fig.update_yaxes(title_text=None, row=1, col=2)

    dist_fig.update_layout(
        width=1000,
        height=400,
        violingap=0,
    )

    dist_fig.show()

    return col_fig, dist_fig


def plot_counts():
    counts = df_instances.groupby("type").size().reindex(TYPE_KEYS, fill_value=0)

    # positive delta = higher energy
    fig = go.Figure(
        data=[
            go.Bar(
                name="positive",
                x=[x.replace("~", " ") for x in TYPE_VALUES],
                y=counts,
                width=[0.6 if i > 0 else 0 for i in counts],
                marker=dict(
                    color="black",
                    # line=dict(color="black", width=[1.5 if i > 0 else 0 for i in counts]),
                ),
                # text=total_counts.astype(int).astype(str),
                # textposition="inside",
                hoverinfo="skip",
                showlegend=False,
            ),
        ]
    )

    # color x tick labels green if total >= 5, else red
    # x_tick_text = TYPE_KEYS

    fig = apply_chart_theme(
        fig,
        overrides={
            "y_title": "Number of instances",
            # "x_title": "Type",
            "static_plot": True,
            "margin_l": 80,
            "margin_r": 30,
            "margin_t": 30,
            "margin_b": 120,
            "y_line_color": COLORS.BLACK,
            "y_tick_color": COLORS.BLACK,
            "y_showline": True,
            "y_tick_pos": "outside",
            "y_tick_len": 6,
            "x_line_color": COLORS.BLACK,
            "x_tick_color": COLORS.BLACK,
        },
    )

    # slanted x-axis labels for readability
    fig.update_xaxes(tickangle=-45, tickfont=dict(color=COLORS.BLACK, size=14))

    # remove y-axis tick where y=0
    y_max = int(np.ceil(counts.max()))
    fig.update_yaxes(
        tickmode="array",
        tickvals=list(range(1, y_max + 1)),  # excludes 0
        ticktext=[str(i) for i in range(1, y_max + 1)],
    )

    fig.update_layout(
        barmode="stack",
        width=1000,
        height=400,
    )

    fig.show()
    return fig


def cohen_d(d1, d2) -> float:
    n1, n2 = len(d1), len(d2)
    s1, s2 = np.var(d1, ddof=1), np.var(d2, ddof=1)
    s = np.sqrt(((n1 - 1) * s1 + (n2 - 1) * s2) / (n1 + n2 - 2))
    u1, u2 = np.mean(d1), np.mean(d2)
    return (u1 - u2) / s


def _spearman_from_rows(rows: list[dict], metric_key: str) -> float:
    df_list = [
        row.get("df_total")
        for row in rows
        if isinstance(row.get("df_total"), pd.DataFrame)
    ]
    if not df_list:
        return np.nan
    df_all = pd.concat(df_list, ignore_index=True)
    x_vals = pd.to_numeric(df_all.get("energy"), errors="coerce")
    y_vals = pd.to_numeric(
        df_all.get(FALLBACKS.get(metric_key, metric_key)), errors="coerce"
    )
    combined = pd.DataFrame({"x": x_vals, "y": y_vals}).dropna()
    if len(combined) < 2:
        return np.nan
    rho, _ = spearmanr(combined["x"], combined["y"])
    return rho


def _safe_percent_diff(delta: float, base: float) -> float:
    return (delta / base * 100) if base != 0 else 0


def _compute_resource_stats(
    df_or: pd.DataFrame, df_rf: pd.DataFrame, metric: str
) -> dict:
    col = FALLBACKS.get(metric, metric)

    s_or = df_or[col].astype(float)
    s_rf = df_rf[col].astype(float)

    mean_or, mean_rf = s_or.mean(), s_rf.mean()
    std_or, std_rf = s_or.std(ddof=1), s_rf.std(ddof=1)
    diff = mean_rf - mean_or
    pct = _safe_percent_diff(diff, mean_or)

    # Spearman correlation between this metric and energy,
    # using all OR+RF filtered points.
    metric_series = pd.concat([df_or[col], df_rf[col]], ignore_index=True)
    energy_series = pd.concat([df_or["energy"], df_rf["energy"]], ignore_index=True)
    combined = pd.DataFrame(
        {
            "metric_val": pd.to_numeric(metric_series, errors="coerce"),
            "energy_val": pd.to_numeric(energy_series, errors="coerce"),
        }
    ).dropna()

    if len(combined) >= 2:
        energy_spearman_rho, energy_spearman_p = spearmanr(
            combined["energy_val"],
            combined["metric_val"],
        )
    else:
        energy_spearman_rho, energy_spearman_p = np.nan, np.nan

    return {
        f"{metric}_mean_or": mean_or,
        f"{metric}_mean_rf": mean_rf,
        f"{metric}_std_or": std_or,
        f"{metric}_std_rf": std_rf,
        f"{metric}_diff": diff,
        f"{metric}_pct": pct,
        f"{metric}_cohen_d": cohen_d(s_rf, s_or),
        f"{metric}_energy_spearman_rho": energy_spearman_rho,
        f"{metric}_energy_spearman_p": energy_spearman_p,
    }


def _compute_normality_fields(df_or: pd.DataFrame, df_rf: pd.DataFrame) -> dict:
    out_or = 30 - len(df_or)
    out_rf = 30 - len(df_rf)
    n_or = 30
    n_rf = 30
    out_total = out_or + out_rf
    n_total = n_or + n_rf
    out_pct = (out_total / n_total * 100) if n_total else np.nan

    # Shapiro-Wilk (filtered, energy)
    energy_or = df_or["energy"].astype(float)
    energy_rf = df_rf["energy"].astype(float)
    sh_stat_or, sh_p_or = shapiro(energy_or)
    sh_stat_rf, sh_p_rf = shapiro(energy_rf)

    # Welch t-test (filtered, energy)
    t_stat, t_p = ttest_ind(energy_or, energy_rf, equal_var=False)

    return {
        "n_or": n_or,
        "n_rf": n_rf,
        "shapiro_stat_or": sh_stat_or,
        "shapiro_p_or": sh_p_or,
        "shapiro_stat_rf": sh_stat_rf,
        "shapiro_p_rf": sh_p_rf,
        "t_stat": t_stat,
        "t_p": t_p,
        "outliers_or": out_or,
        "outliers_rf": out_rf,
        "outliers_total": out_total,
        "outliers_pct": out_pct,
    }


def _compute_coverage_fields(
    df_quality_or: pd.DataFrame, df_quality_rf: pd.DataFrame
) -> dict:
    branches_total = df_quality_or["br_total"].iloc[0]
    mutants_total = df_quality_or["mut_total"].iloc[0]
    instructions_total = df_quality_or["inst_total"].iloc[0]

    instructions_or = df_quality_or["inst"].iloc[0]
    branches_or = df_quality_or["br"].iloc[0]
    mutants_or = df_quality_or["mut"].iloc[0]
    mutants_covered_or = df_quality_or["mut_cov"].iloc[0]

    instructions_rf = df_quality_rf["inst"].iloc[0]
    branches_rf = df_quality_rf["br"].iloc[0]
    mutants_rf = df_quality_rf["mut"].iloc[0]
    mutants_covered_rf = df_quality_rf["mut_cov"].iloc[0]

    delta_branches = branches_rf - branches_or
    delta_mutants = mutants_rf - mutants_or

    return {
        "branches_total": branches_total,
        "mutants_total": mutants_total,
        "instructions_total": instructions_total,
        "instructions_or": instructions_or,
        "branches_or": branches_or,
        "branch_score_or": (branches_or / branches_total if branches_total != 0 else 0),
        "mutants_or": mutants_or,
        "mutants_covered_or": mutants_covered_or,
        "mutation_score_or": (mutants_or / mutants_total if mutants_total != 0 else 0),
        "instructions_rf": instructions_rf,
        "branches_rf": branches_rf,
        "branch_score_rf": (branches_rf / branches_total if branches_total != 0 else 0),
        "mutants_rf": mutants_rf,
        "mutants_covered_rf": mutants_covered_rf,
        "mutation_score_rf": (mutants_rf / mutants_total if mutants_total != 0 else 0),
        "delta_branches": delta_branches,
        "delta_mutants": delta_mutants,
    }


ASSERT_METHODS = [
    "assertEqualsIsConsistentWithArraysEquals",
    "assertIntParameter",
    "assertQuotient",
    "assertCorrectlyDeserialized",
    "assertExceptionTypeAndMessage",
    "assertRejectedInStackTrace",
    "assertMessageContains",
    "assertWithMessage",
    "assertAtan2",
    "assertExpectedDecoderException",
    "assertRoundTrip",
    "assertUnexpectedStructureError",
    "assertPutAndGet",
    "assertArrayLengthsEqual",
    "assertCustomGson",
    "assertIncludesClass",
    "assertAcceptedInStackTrace",
    "assertSum",
    "assertFormats",
    "assertInstanceOf",
    "assertSplitMedian",
    "assertSameAfterSerialization",
    "assertLanguageByCountry",
    "assertEquals",
    "assertTrue",
    "assertReflectionArray",
    "assertGetClassThrowsException",
    "assertZeroCounters",
    "assertDoubleValue",
    "assertEscapeJava",
    "assertParse",
    "assertErrorStatistics",
    "assertRms",
    "assertUnescapeJava",
    "assertUnmodifiableCollection",
    "assertNotClosed",
    "assertData",
    "assertPeek",
    "assertXYZCompareOrder",
    "assertSupportedEncoding",
    "assertDoubleSortInternal",
    "assertFraction",
    "assertRelativelyEquals",
    "assertAbs",
    "assertFit",
    "assertEqualsAfterSerialization",
    "assertLines",
    "assertEqualDuration",
    "assertValidToLocale",
    "assertEnabled",
    "assertCorrect",
    "assertCompatibleTypes",
    "assertConjugateEquality",
    "assertExactlyOneFailure",
    "assertVectorEquals",
    "assertStateFor",
    "assertDoubleMetaphone",
    "assertEqualsTime",
    "assertBase64DecodingOfTrailingBits",
    "assertNonFiniteNumbersExceptions",
    "assertSplitMiddleIndices",
    "assertCounts",
    "assertCounter",
    "assertOverridesMethods",
    "assertFloorCeil",
    "assertThrowsProperExceptionWithKeySize",
    "assertDocument",
    "assertChiSquareAccept",
    "assertComplex",
    "assertElapsedTimeIsLessThan",
    "assertEqualsRelativeOrAbsolute",
    "assertFooBarFileFiltering",
    "assertProductLow",
    "assertWordForms",
    "assertExcludesField",
    "assertFunctionType",
    "assertSortWithCompareTo",
    "assertEqualsWithAllowedUlps",
    "assertFormatted",
    "assertOfIndices",
    "assertDoubleMinMax",
    "assertValidMockClass",
    "assertFalseFiltersInvoked",
    "assertSumExact",
    "assertByteArrayEquals",
    "assertNumberType",
    "assertMedian",
    "assertPivots",
    "assertIndexing",
    "assertArgumentEquals",
    "assertContentNotEquals",
    "assertNoAnnotations",
    "assertThrowsUnchecked",
    "assertIterator",
    "assertOperation",
    "assertGoodMock",
    "assertReflectionCompareContract",
    "assertFileContentEquals",
    "assertPrint",
    "assertCalendar",
    "assertPartitionRange",
    "assertErf",
    "assertCloseCalled",
    "assertSetRange",
    "assertSortIndices",
    "assertThresholdingInitialState",
    "assertEqualsAndNoLineBreaks",
    "assertEnsureBufferSizeExpandsToMaxBufferSize",
    "assertNoPrint",
    "assertFloatEqualsWithAllowedDelta",
    "assertOverloadCases",
    "assertThrowable",
    "assertFileFiltering",
    "assertAbbreviateWithAbbrevMarkerAndOffset",
    "assertException",
    "assertSerializable",
    "assertPeekArray",
    "assertQuickSelectAdaptive",
    "assertPartitionPaired",
    "assertInstantiable",
    "assertSingleRange",
    "assertSumOfProducts",
    "assertThrows",
    "assertMocksNotEmpty",
    "assertNonFiniteFloatsExceptions",
    "assertOngoingStubbingIsReset",
    "assertEof",
    "assertMapContainsExpectedValues",
    "assertContentEquals",
    "assertScaledEquals",
    "assertTimeoutPreemptively",
    "assertThrowsStackOverflow",
    "assertVisibility",
    "assertGetEnumFromParam",
    "assertEuclidean2dVersusHypot",
    "assertEnumParameter",
    "assertEqualsArchNotNull",
    "assertCaptor",
    "assertPartitionQA2",
    "assertSerialized",
    "assertElapsedTimeIsMoreThan",
    "assertWeekIterator",
    "assertSquare",
    "assertIndexOutOfBoundsException",
    "assertDate",
    "assertWantedIsVerifiable",
    "assertEqualsTypeNotNull",
    "assertMatch",
    "assertSplit",
    "assertFiltering",
    "assertLog",
    "assertUncheckedIOException",
    "assertEmpty",
    "assertDirectoryAndFileContentEquals",
    "assertTimeout",
    "assertDefaultGson",
    "assertEqualsAndHashCode",
    "assertNullPointerException",
    "assertFilenameFiltering",
    "assertGetClassThrowsClassNotFound",
    "assertCreateNumberZero",
    "assertNotEquals",
    "assertDoesNotThrow",
    "assertEncodings",
    "assertEvenNumbers",
    "assertSort",
    "assertUpdate",
    "assertEqualAndHashCodeEqual",
    "assertParsed",
    "assertNaN",
    "assertArrayEquals",
    "assertPartition",
    "assertReadLines",
    "assertNull",
    "assertThatThrownBy",
    "assertFileWithShrinkingTestLines",
    "assertValue",
    "assertInvalidIndicesThrows",
    "assertNotNull",
    "assertExceptionTypeCanNotBeNull",
    "assertNormalized",
    "assertExcludesClass",
    "assertDoubleMedian",
    "assertIncrementalHash32x86",
    "assertSame",
    "assertMatrixEquals",
    "assertPlatformParsesCorrectlyVariousVersionScheme",
    "assertSignedZeroArithmetic",
    "assertDoubleSort",
    "assertDirectoryAndFileContentNotEquals",
    "assertBiOperation",
    "assertDoubleMedian5",
    "assertFunction",
    "assertIsEquals",
    "assertThrowsChecked",
    "assertThat",
    "assertIllegalArgumentException",
    "assertExceptionMessageContains",
    "assertEqualsAndHashCodeForNumericPair",
    "assertResult",
    "assertAbsVsSqrt",
    "assertDecodeObject",
    "assertIsMetaphoneEqual",
    "assertNotSame",
    "assertSortTransformer",
    "assertAclEntryList",
    "assertCalendarsEquals",
    "assertContainsExactly",
    "assertDoubleParameter",
    "assertPivot",
    "assertPatterns",
    "assertArg",
    "assertProduct",
    "assertFloatEqualsWithAllowedUlps",
    "assertInaccessibleException",
    "assertOperationThrows",
    "assertPowScalarZeroBase",
    "assertPreviousNextIndex",
    "assertCountriesByLanguage",
    "assertDeferredInitialState",
    "assertArgument",
    "assertEqualsDate",
    "assertIsEmpty",
    "assertContentMatchesAfterCopyURLToFileFor",
    "assertNotEmpty",
    "assertPatternPrintsForZones",
    "assertSortConsistently",
    "assertThatCaptor",
    "assertContainsNoWhiteSpace",
    "assertEqualsAndHashCodeContract",
    "assertNoIncompatibleAnnotations",
    "assertParseFails",
    "assertStrictError",
    "assertNotTerminated",
    "assertCloser",
    "assertNotEqualsArchNotNull",
    "assertFalse",
    "assertNotEqualsTypeNotNull",
    "assertLazily",
    "assertMetaphoneEqual",
    "assertThrowsExactly",
    "assertBase32DecodingOfTrailingBits",
    "assertArrayState",
    "assertAbbreviateWithOffset",
    "assertFileContentNotEquals",
    "assertEqualContent",
    "assertNextPrimeException",
    "assertValues",
    "assertEqual",
    "assertDoubleMetaphoneAlt",
    "assertBinomial",
    "assertPowComplexZeroBase",
    "assertIncrementalHash32",
    "assertEqualsWithAllowedDelta",
    "assertDoubleConstructorOverflow",
    "assertIncludesField",
    "assertAbsVsLog",
    "assertGetClassThrowsNullPointerException",
    "assertLocaleLookupList",
    "assertMarkSupportedEquals",
    "assertRegularizedBeta",
    "assertSerialization",
    "assertRegularizedGamma",
    "assertNonFiniteDoublesExceptions",
    "assertIsNoMock",
    "assertSortUnique",
    "assertClose",
    "assertStreamOutput",
    "assertGetClassReturnsClass",
    "assertQuaternion",
    "assertTrueFiltersInvoked",
    "assertCriticalValue",
    "assertIgammaLargeX",
    "assertFullConsumption",
    "assertContains",
    "assertNotStubOnlyMock",
    "assertSortInternal",
    "assertFormattingAlwaysEmitsUsLocale",
    "assertPrimeFactorsException",
    "assertNotANumber",
]

_ASSERT_RE = re.compile(
    r"\b(?:" + "|".join(re.escape(x) for x in ASSERT_METHODS) + r")\s*\("
)
_DISABLED_ANN_RE = re.compile(
    r"@\s*(?:[A-Za-z_$][\w$]*\.)*(?:Disable|Disabled|Ignore|Ignored)\b"
)


def _strip_java_comments(text: str) -> str:
    out = []
    i = 0
    n = len(text)
    in_line_comment = False
    in_block_comment = False
    in_string = False
    in_char = False

    while i < n:
        ch = text[i]
        nxt = text[i + 1] if i + 1 < n else ""

        if in_line_comment:
            if ch == "\n":
                in_line_comment = False
                out.append(ch)
            i += 1
            continue

        if in_block_comment:
            if ch == "*" and nxt == "/":
                in_block_comment = False
                i += 2
            else:
                if ch == "\n":
                    out.append("\n")
                i += 1
            continue

        if in_string:
            out.append(ch)
            if ch == "\\" and i + 1 < n:
                out.append(text[i + 1])
                i += 2
                continue
            if ch == '"':
                in_string = False
            i += 1
            continue

        if in_char:
            out.append(ch)
            if ch == "\\" and i + 1 < n:
                out.append(text[i + 1])
                i += 2
                continue
            if ch == "'":
                in_char = False
            i += 1
            continue

        if ch == "/" and nxt == "/":
            in_line_comment = True
            i += 2
            continue

        if ch == "/" and nxt == "*":
            in_block_comment = True
            i += 2
            continue

        if ch == '"':
            in_string = True
            out.append(ch)
            i += 1
            continue

        if ch == "'":
            in_char = True
            out.append(ch)
            i += 1
            continue

        out.append(ch)
        i += 1

    return "".join(out)


def _remove_disabled_regions(code: str) -> str:
    lines = code.splitlines(keepends=True)
    kept = []

    pending_disabled = False
    skip_depth = 0

    for line in lines:
        opens = line.count("{")
        closes = line.count("}")

        if skip_depth > 0:
            skip_depth += opens - closes
            if skip_depth <= 0:
                skip_depth = 0
            continue

        if pending_disabled:
            if opens > 0:
                depth = opens - closes
                if depth > 0:
                    skip_depth = depth
                pending_disabled = False
            continue

        if _DISABLED_ANN_RE.search(line):
            if opens > 0:
                depth = opens - closes
                if depth > 0:
                    skip_depth = depth
            else:
                pending_disabled = True
            continue

        kept.append(line)

    return "".join(kept)


def _compute_quality_fields(full_path):
    fields = {}
    version_map = {OR: f"{full_path}_{OR}.java", RF: f"{full_path}_{RF}.java"}

    for version, path in version_map.items():
        try:
            with open(path, "r", encoding="utf-8") as f:
                lines = f.readlines()
        except FileNotFoundError:
            print(f"Warning: File not found at {path}")
            continue

        # filter out imports, package declarations, and empty lines
        filtered_sloc = []
        for line in lines:
            clean_line = line.strip()
            if not clean_line:
                continue
            if clean_line.startswith("import ") or clean_line.startswith("package "):
                continue
            filtered_sloc.append(clean_line)

        loc_count = len(filtered_sloc)

        # count only selected primitive asserts (exclude comments and disabled regions)
        content = "\n".join(filtered_sloc)
        content_no_comments = _strip_java_comments(content)
        active_content = _remove_disabled_regions(content_no_comments)
        assertion_count = len(_ASSERT_RE.findall(active_content))
        density = assertion_count / loc_count if loc_count > 0 else 0

        fields[f"loc_{version.lower()}"] = loc_count
        fields[f"assertions_{version.lower()}"] = assertion_count
        fields[f"density_{version.lower()}"] = density

    # calculate deltas
    if f"loc_{OR.lower()}" in fields and f"loc_{RF.lower()}" in fields:
        fields["delta_loc"] = fields[f"loc_{RF.lower()}"] - fields[f"loc_{OR.lower()}"]
        fields["loc_pct"] = (
            fields["delta_loc"] / fields[f"loc_{OR.lower()}"] * 100
            if fields[f"loc_{OR.lower()}"] != 0
            else 0
        )
        fields["delta_assertions"] = (
            fields[f"assertions_{RF.lower()}"] - fields[f"assertions_{OR.lower()}"]
        )
        fields["assertions_pct"] = (
            fields["delta_assertions"] / fields[f"assertions_{OR.lower()}"] * 100
            if fields[f"assertions_{OR.lower()}"] != 0
            else 0
        )

    return fields


def fetch_rows():
    rows = []
    missing = []
    with open("../config/paths.yaml", "r") as f:
        paths_config = yaml.safe_load(f)
    systems_root = paths_config["SYSTEMS_PATH"]

    for _, instance in df_instances.iterrows():
        instance_str_id = instance["instance_id"]
        instance_int_id = to_int_id(instance_str_id)
        type = instance["type"]
        instance_text = df_instances_text[
            df_instances_text["instance_id"] == instance_str_id
        ].iloc[0]

        energy_path = os.path.join(
            ROOT_DIR, "data", "results", instance_str_id, "energy"
        )
        quality_path = os.path.join(
            ROOT_DIR, "data", "results", instance_str_id, "quality"
        )

        if not os.path.isdir(energy_path):
            missing.append((instance_int_id, "missing energy folder"))
            continue

        energy_subfolders = [f.path for f in os.scandir(energy_path) if f.is_dir()]
        if not energy_subfolders:
            missing.append((instance_int_id, "no subfolders in energy"))
            continue

        quality_subfolders = [f.path for f in os.scandir(quality_path) if f.is_dir()]
        if not quality_subfolders:
            missing.append((instance_int_id, "missing quality subfolder"))
            continue

        energy_folder = max(energy_subfolders)
        quality_folder = max(quality_subfolders)

        df_quality = pd.read_csv(os.path.join(quality_folder, "summary_QUALITY.csv"))
        df_quality_or = df_quality[df_quality["version"] == OR]
        df_quality_rf = df_quality[df_quality["version"] == RF]
        coverage_fields = _compute_coverage_fields(df_quality_or, df_quality_rf)

        or_path = os.path.join(energy_folder, f"summary_OR.csv")
        rf_path = os.path.join(energy_folder, f"summary_RF.csv")

        if not (os.path.exists(or_path) and os.path.exists(rf_path)):
            missing.append((instance_int_id, "missing energy/summary_*.csv"))
            continue

        df_or = pd.read_csv(or_path)
        df_rf = pd.read_csv(rf_path)

        # keep positive energies for consistency with the existing notebook analysis
        df_or = df_or[df_or["energy"] > 0].copy()
        df_rf = df_rf[df_rf["energy"] > 0].copy()

        if len(df_or) < 3 or len(df_rf) < 3:
            missing.append((instance_int_id, "<3 samples in OR or RF after filtering"))
            continue

        # outliers by modified z-score (same approach as existing analysis)
        mz_or = get_mz_stats(df_or)
        mz_rf = get_mz_stats(df_rf)

        df_or = mz_or["df"]
        df_rf = mz_rf["df"]

        if len(df_or) < 3 or len(df_rf) < 3:
            missing.append(
                (instance_int_id, "<3 samples in OR or RF after outlier removal")
            )
            continue

        normality_fields = _compute_normality_fields(df_or, df_rf)

        resource_fields = {}
        for metric in METRICS.keys():
            resource_fields.update(_compute_resource_stats(df_or, df_rf, metric))

            path = instance["test_path"].replace(".", os.sep)
            full_path = os.path.join(
                systems_root,
                instance["system"],
                "src",
                "test",
                "java",
                path,
                instance_str_id,
            )
        quality_fields = _compute_quality_fields(full_path)

        system: str = instance["system"]
        if "/" in system:
            system = system.split("/")[0]

        rows.append(
            {
                "instance_id": instance_int_id,
                "type": type,
                "type_full": TYPE_MAP.get(type),
                "system": system,
                "smell": instance_text["smell"],
                "refactoring": instance_text["refactoring"],
                "df_or": df_or,
                "df_rf": df_rf,
                "df_total": pd.concat([df_or, df_rf], ignore_index=True),
                **normality_fields,
                **resource_fields,
                **coverage_fields,
                **quality_fields,
            }
        )

    if missing:
        print(f"Skipped {len(missing)} instances:")
        for tid, reason in missing:
            print(f"  - {tid}: {reason}")

    return rows


def build_table_groups(columns: list[dict]) -> list[tuple[int, int, str]]:
    """Build (start_col, span, label) groups from consecutive `group` values."""
    groups = []
    i = 0
    while i < len(columns):
        label = columns[i].get("group")
        if not label:
            i += 1
            continue

        start = i + 1  # 1-based LaTeX column index
        j = i + 1
        while j < len(columns) and columns[j].get("group") == label:
            j += 1

        groups.append((start, j - i, label))
        i = j

    return groups


def build_group_header(n_cols: int, groups: list[tuple[int, int, str]]) -> str:
    if not groups:
        return ""

    grouped_by_start = {start: (span, label) for start, span, label in groups}
    cells = []
    col = 1

    while col <= n_cols:
        if col in grouped_by_start:
            span, label = grouped_by_start[col]
            cells.append(rf"\multicolumn{{{span}}}{{c}}{{{label}}}")
            col += span
        else:
            cells.append("")
            col += 1

    return " & ".join(cells) + r" \\"


def build_cmidrule_line(groups: list[tuple[int, int, str]]) -> str:
    return " ".join(
        rf"\cmidrule(lr){{{start}-{start + span - 1}}}" for start, span, _ in groups
    )


def infer_col_align(col: dict) -> str:
    """Infer LaTeX alignment from format, with optional explicit override via `align`.

    If `align` is set to one of { "l", "c", "r", "X" }, it is used.
    Otherwise alignment is inferred from `fmt` (`str` -> `l`, everything else -> `r`).
    """
    align = str(col.get("align", "")).strip()
    if align in {"l", "c", "r", "X"}:
        return align

    fmt = col.get("fmt", "str")
    return "l" if fmt == "str" else "r"


def _to_number(value) -> float:
    if value is None:
        return np.nan
    if isinstance(value, (int, float, np.number)):
        return float(value)

    text = str(value).replace(",", "").strip()
    if text == "":
        return np.nan

    try:
        return float(text)
    except ValueError:
        return np.nan


def _with_grouping(fmt_spec: str) -> str:
    """Inject ',' grouping flag into a Python format spec if missing."""
    if "," in fmt_spec or "_" in fmt_spec:
        return fmt_spec
    if "." in fmt_spec:
        idx = fmt_spec.index(".")
        return fmt_spec[:idx] + "," + fmt_spec[idx:]
    return "," + fmt_spec


def _render_value(raw, col_cfg: dict) -> str:
    if raw is None or pd.isna(raw):
        return ""

    def _style_text(text: str, bold: bool, italic: bool) -> str:
        if bold:
            text = rf"\textbf{{{text}}}"
        if italic:
            text = rf"\textit{{{text}}}"
        return text

    fmt = col_cfg.get("fmt", "str")

    if callable(fmt):
        text = fmt(raw)
        if text is None:
            return ""
        text = str(text)
        return _style_text(
            text,
            bool(col_cfg.get("bold", False)),
            bool(col_cfg.get("italic", False)),
        )

    if fmt == "str":
        text = str(raw)
        return _style_text(
            text,
            bool(col_cfg.get("bold", False)),
            bool(col_cfg.get("italic", False)),
        )

    value = float(raw)
    display_value = abs(value) if col_cfg.get("abs", False) else value

    show_sign = bool(col_cfg.get("show_sign", False))
    thousands = bool(col_cfg.get("thousands", True))

    if fmt == "int":
        int_value = int(display_value)
        text = f"{int_value:,}" if thousands else str(int_value)
        if show_sign and int_value > 0:
            text = f"+{text}"
    elif fmt == "pct":
        fmt_spec = str(col_cfg.get("pct_fmt", ".1f"))
        if show_sign and ("+" not in fmt_spec and " " not in fmt_spec):
            fmt_spec = "+" + fmt_spec
        if thousands:
            fmt_spec = _with_grouping(fmt_spec)
        text = rf"{format(display_value, fmt_spec)}\%"
    else:
        fmt_spec = str(fmt)
        if show_sign and ("+" not in fmt_spec and " " not in fmt_spec):
            fmt_spec = "+" + fmt_spec
        if thousands:
            fmt_spec = _with_grouping(fmt_spec)
        text = format(display_value, fmt_spec)

    min_value = col_cfg.get("min_val")
    max_value = col_cfg.get("max_val")
    if min_value is not None and display_value < min_value:
        text = f"< {min_value}"
    if max_value is not None and display_value > max_value:
        text = f"> {max_value}"

    compare_value = (
        abs(display_value) if col_cfg.get("abs_threshold", False) else display_value
    )
    is_bold = bool(col_cfg.get("bold", False))
    is_italic = bool(col_cfg.get("italic", False))
    threshold_min = col_cfg.get("threshold_min")
    threshold_max = col_cfg.get("threshold_max")

    if threshold_min is not None and compare_value >= threshold_min:
        is_bold = True
    if threshold_max is not None and compare_value <= threshold_max:
        is_bold = True

    return _style_text(text, is_bold, is_italic)


def average_from_rows(rows: list[dict], key: str):
    values = [_to_number(row.get(key)) for row in rows if row.get(key) is not None]
    return np.mean(values) if values else "N/A"


def median_from_rows(rows: list[dict], key: str):
    values = [_to_number(row.get(key)) for row in rows if row.get(key) is not None]
    return np.median(values) if values else "N/A"


def average_std_from_rows(rows: list[dict], key: str):
    values = [_to_number(row.get(key)) for row in rows if row.get(key) is not None]
    return np.sqrt(np.mean(np.square(values))) if values else "N/A"


def sum_from_rows(rows: list[dict], key: str):
    values = [_to_number(row.get(key)) for row in rows if row.get(key) is not None]
    return np.sum(values) if values else "N/A"


def _compute_footer_value(rows: list[dict], col: dict, fcfg: dict):
    if callable(fcfg.get("value_fn")):
        return fcfg["value_fn"](rows, col)
    if "value" in fcfg:
        return fcfg["value"]
    raise ValueError("Footer config must provide value or value_fn.")


def _merge_render_cfg(col: dict, fcfg: dict) -> dict:
    render_cfg = dict(col)
    for key in RENDER_KEYS:
        if key in fcfg:
            render_cfg[key] = fcfg[key]
    if isinstance(fcfg.get("render"), dict):
        render_cfg.update(fcfg["render"])
    return render_cfg


def build_footer_row(
    rows: list[dict],
    columns: list[dict],
    footer_key: str = "footer",
) -> str | None:
    """Build a single footer row from per-column footer configs.

    Supported footer config keys per column:
      - value: explicit literal text (e.g. r"\textbf{All systems}")
      - value_fn: callable (rows, col) -> raw value
      - fmt/min_val/max_val/threshold_min/threshold_max/abs/abs_threshold/bold
      - render: dict of render overrides (same keys as above)
    """
    cells = []
    has_footer = False

    for col in columns:
        fcfg = col.get(footer_key)
        if not fcfg:
            cells.append("")
            continue

        has_footer = True
        raw = _compute_footer_value(rows, col, fcfg)
        render_cfg = _merge_render_cfg(col, fcfg)
        text = _render_value(raw, render_cfg)
        cells.append(text)

    if not has_footer:
        return None

    return "    " + " & ".join(cells) + r" \\"


def render_cell(row: dict, col: dict) -> str:
    """Render one LaTeX cell with optional threshold/min/max formatting.

    Supported column config keys:
      - `fmt`: "str" | "int" | "p" | python format string (e.g. ".2f")
      - `bold`: always bold
      - `threshold_min`: bold when comparison value >= threshold_min
      - `threshold_max`: bold when comparison value <= threshold_max
      - `min_val`: display as "< min_val" when display value < min_val
      - `max_val`: display as "> max_val" when display value > max_val
      - `abs_threshold`: compare thresholds on absolute value when True
      - `abs`: display absolute value when True
    """
    key = col["key"]
    raw = row[key]
    return _render_value(raw, col)


def _apply_text_color(text: str, color: str | None) -> str:
    if not color or text == "":
        return text
    return rf"\textcolor{{{color}}}{{{text}}}"


def build_body_rows(
    rows: list[dict],
    columns: list[dict],
    group_by_key: str | None = None,
    group_separator: str = r"\midrule",
    row_color=None,
    group_footer: bool = False,
    separate_group_footer: bool = True,
    separate_groups: bool = True,
    separate_final_footer: bool = False,
    separate_last_group_footer: bool = False,
    include_separator_before_footer: bool = True,
) -> str:
    lines = []
    group_start = 0
    for idx, row in enumerate(rows):
        current_color = row_color(row) if callable(row_color) else row_color
        cells = []
        for col in columns:
            if col.get("show_first_only") and idx > 0:
                if rows[idx - 1].get(col["key"]) == row.get(col["key"]):
                    cells.append("")
                    continue
            cells.append(_apply_text_color(render_cell(row, col), current_color))
        lines.append("    " + " & ".join(cells) + r" \\")

        if group_by_key is None or idx == len(rows) - 1:
            if group_by_key is None:
                continue
            if group_footer:
                group_rows = rows[group_start : idx + 1]
                if include_separator_before_footer:
                    lines.append(f"  {group_separator}")
                footer_row = build_footer_row(
                    group_rows, columns, footer_key="footer_group"
                )
                if footer_row is not None:
                    lines.append(footer_row)
                if separate_last_group_footer:
                    lines.append(f"  {group_separator}")
            continue

        if row.get(group_by_key) != rows[idx + 1].get(group_by_key):
            if group_footer:
                group_rows = rows[group_start : idx + 1]
                if include_separator_before_footer:
                    lines.append(f"  {group_separator}")
                footer_row = build_footer_row(
                    group_rows, columns, footer_key="footer_group"
                )
                if footer_row is not None:
                    lines.append(footer_row)
                if separate_group_footer:
                    lines.append(f"  {group_separator}")
            if separate_groups:
                lines.append(f"  {group_separator}")
            group_start = idx + 1

    return "\n".join(lines)


def build_latex_table(
    rows: list[dict],
    columns: list[dict],
    caption: str,
    label: str,
    group_by_key: str | None = "type",
    group_separator: str = r"\addlinespace",
    row_color=None,
    group_footer: bool = False,
    separate_group_footer: bool = False,
    separate_groups: bool = True,
    separate_final_footer: bool = False,
    separate_last_group_footer: bool = False,
    include_separator_before_footer: bool = True,
) -> str:
    groups = build_table_groups(columns)
    alignment = " ".join(infer_col_align(col) for col in columns)
    header_row = " & ".join(col["header"] for col in columns) + r" \\"
    group_row = build_group_header(len(columns), groups)
    cmidrule_row = build_cmidrule_line(groups)

    show_group_header = (group_by_key is not None) and bool(groups)
    group_header_block = (
        f"    {group_row}\n    {cmidrule_row}\n" if show_group_header else ""
    )

    has_footer_group_cfg = any(bool(col.get("footer_group")) for col in columns)
    effective_group_footer = group_footer or (
        group_by_key is not None and has_footer_group_cfg
    )

    body_rows = build_body_rows(
        rows,
        columns,
        group_by_key=group_by_key,
        group_separator=group_separator,
        row_color=row_color,
        group_footer=effective_group_footer,
        separate_group_footer=separate_group_footer,
        separate_groups=separate_groups,
        separate_final_footer=separate_final_footer,
        separate_last_group_footer=separate_last_group_footer,
        include_separator_before_footer=include_separator_before_footer,
    )

    footer_row = build_footer_row(rows, columns)
    footer_block = ""
    if footer_row is not None:
        footer_block = f"    \\midrule\n{footer_row}"

    return (
        rf"""
  \begin{{xltabular}}{{\linewidth}}{{{alignment}}}
    \caption{{{caption}}}\label{{{label}}} \\[10pt]
    \toprule
{group_header_block}    {header_row}
    \midrule
{body_rows}
{footer_block}
    \bottomrule
  \end{{xltabular}}""".replace("&  &", "& &")
        .replace("&  \\", "& \\")
        .replace("&  &", "& &")
        .strip()
    )


def replace_table(tex_path: str, target_label: str, new_table: str) -> bool:
    """Replace the full `table` block containing `target_label`."""
    xltabular_pattern = re.compile(
        r"\\begin\{xltabular\}.*?\\end\{xltabular\}", re.DOTALL
    )

    with open(tex_path, "r+", encoding="utf-8") as f:
        content = f.read()

        match_to_replace = None
        for m in xltabular_pattern.finditer(content):
            if target_label in m.group(0):
                match_to_replace = m
                break

        if match_to_replace is None:
            print(f"No table containing {target_label} was found. Added to the end.")
            with open(tex_path, "a", encoding="utf-8") as f:
                f.write("\n\n" + new_table + "\n")
            return True

        content = (
            content[: match_to_replace.start()]
            + new_table
            + content[match_to_replace.end() :]
        )
        f.seek(0)
        f.write(content)
        f.truncate()
        print("Table saved.")

    return True


def generate_and_replace_table(
    columns: list[dict],
    caption: str,
    label: str,
    tex_path: str,
    rows: list[dict] | None = None,
    sort_keys: tuple[str, ...] = ("type", "instance_id"),
    group_by_key: str | None = "type",
    group_separator: str = r"\midrule",
    row_color=None,
    group_footer: bool = False,
    separate_group_footer: bool = False,
    separate_groups: bool = True,
    separate_final_footer: bool = False,
    separate_last_group_footer: bool = False,
    include_separator_before_footer: bool = True,
) -> str:
    os.makedirs(os.path.dirname(tex_path), exist_ok=True)
    if not os.path.exists(tex_path):
        with open(tex_path, "w") as f:
            f.write("")

    rows = rows if rows is not None else fetch_rows()
    ordered_rows = (
        sorted(rows, key=lambda r: tuple(r[k] for k in sort_keys))
        if sort_keys is not None
        else rows
    )

    latex_table = build_latex_table(
        ordered_rows,
        columns,
        caption=caption,
        label=label,
        group_by_key=group_by_key,
        group_separator=group_separator,
        row_color=row_color,
        group_footer=group_footer,
        separate_group_footer=separate_group_footer,
        separate_groups=separate_groups,
        separate_final_footer=separate_final_footer,
        separate_last_group_footer=separate_last_group_footer,
        include_separator_before_footer=include_separator_before_footer,
    )
    replace_table(
        tex_path=tex_path,
        target_label=rf"\label{{{label}}}",
        new_table=latex_table,
    )
    # return latex_table


SYSTEM_TABLE = [
    dict(
        header=r"\textbf{System}",
        key="system",
        fmt="str",
        footer=dict(value=r"\textbf{All systems}"),
    ),
    dict(
        header=r"\textbf{Version}",
        key="version",
        fmt="str",
        group="Production code",
        footer=dict(value=""),
    ),
    dict(
        header=r"\textbf{Classes}",
        key="classes",
        fmt="int",
        group="Production code",
        footer=dict(
            value_fn=lambda rows, col: sum_from_rows(rows, col["key"]),
            fmt="int",
            bold=True,
        ),
    ),
    dict(
        header=r"\textbf{KLOC}",
        key="kloc",
        fmt="int",
        group="Production code",
        footer=dict(
            value_fn=lambda rows, col: sum_from_rows(rows, col["key"]),
            fmt="int",
            bold=True,
        ),
    ),
    dict(
        header=r"\textbf{Size (MB)}",
        key="size",
        fmt=".1f",
        group="Production code",
        footer=dict(
            value_fn=lambda rows, col: sum_from_rows(rows, col["key"]),
            fmt=".1f",
            bold=True,
        ),
    ),
    dict(
        header=r"\textbf{JUnit version}",
        key="junit_version",
        fmt="str",
        group="Test code",
        footer=dict(value=""),
    ),
    dict(
        header=r"\textbf{Classes}",
        key="junit_classes",
        fmt="int",
        group="Test code",
        footer=dict(
            value_fn=lambda rows, col: sum_from_rows(rows, col["key"]),
            fmt="int",
            bold=True,
        ),
    ),
    dict(
        header=r"\textbf{Methods}",
        key="junit_methods",
        fmt="int",
        group="Test code",
        footer=dict(
            value_fn=lambda rows, col: sum_from_rows(rows, col["key"]),
            fmt="int",
            bold=True,
        ),
    ),
    dict(
        header=r"\textbf{KLOC}",
        key="junit_kloc",
        fmt="int",
        group="Test code",
        footer=dict(
            value_fn=lambda rows, col: sum_from_rows(rows, col["key"]),
            fmt="int",
            bold=True,
        ),
    ),
]

INSTANCE_TABLE = [
    dict(
        header=r"\textbf{Instance}",
        key="instance_id",
        fmt="int",
        # bold=True,
    ),
    dict(
        header=r"\textbf{Test smell}",
        key="type_full",
        fmt="str",
        show_first_only=True,
    ),
    dict(
        header=r"\textbf{Explanation}",
        key="smell",
        fmt=lambda x: re.sub(r"`([^`]*)`", r"\\texttt{\1}", str(x)),
        align="X",
    ),
    dict(
        header=r"\textbf{Refactoring}",
        key="refactoring",
        fmt=lambda x: re.sub(r"`([^`]*)`", r"\\texttt{\1}", str(x)),
        align="l",
    ),
]

NORMALITY_TABLE = [
    dict(
        header=r"\textbf{Test smell}",
        key="type_full",
        fmt="str",
        show_first_only=True,
        footer_group=dict(value=r"Total", fmt="str", bold=True),
        footer=dict(value=r"All instances", fmt="str", bold=True),
    ),
    dict(
        header=r"\textbf{Instance}",
        key="instance_id",
        fmt="int",
        # bold=True,
    ),
    # sample size
    dict(
        header="OR",
        key="n_or",
        fmt="int",
        group="Sample size",
        footer_group=dict(
            value_fn=lambda rows, col: sum(
                _to_number(row.get(col["key"]))
                for row in rows
                if row.get(col["key"]) is not None
            ),
            fmt="int",
            bold=True,
        ),
        footer=dict(
            value_fn=lambda rows, col: sum(
                _to_number(row.get(col["key"]))
                for row in rows
                if row.get(col["key"]) is not None
            ),
            fmt="int",
            bold=True,
        ),
    ),
    dict(
        header="RF",
        key="n_rf",
        fmt="int",
        group="Sample size",
        footer_group=dict(
            value_fn=lambda rows, col: sum(
                _to_number(row.get(col["key"]))
                for row in rows
                if row.get(col["key"]) is not None
            ),
            fmt="int",
            bold=True,
        ),
        footer=dict(
            value_fn=lambda rows, col: sum(
                _to_number(row.get(col["key"]))
                for row in rows
                if row.get(col["key"]) is not None
            ),
            fmt="int",
            bold=True,
        ),
    ),
    # outliers
    dict(
        header="OR",
        key="outliers_or",
        fmt="int",
        group="Outliers",
        footer_group=dict(
            value_fn=lambda rows, col: sum(
                _to_number(row.get(col["key"]))
                for row in rows
                if row.get(col["key"]) is not None
            ),
            fmt="int",
            bold=True,
        ),
        footer=dict(
            value_fn=lambda rows, col: sum(
                _to_number(row.get(col["key"]))
                for row in rows
                if row.get(col["key"]) is not None
            ),
            fmt="int",
            bold=True,
        ),
    ),
    dict(
        header="RF",
        key="outliers_rf",
        fmt="int",
        group="Outliers",
        footer_group=dict(
            value_fn=lambda rows, col: sum(
                _to_number(row.get(col["key"]))
                for row in rows
                if row.get(col["key"]) is not None
            ),
            fmt="int",
            bold=True,
        ),
        footer=dict(
            value_fn=lambda rows, col: sum(
                _to_number(row.get(col["key"]))
                for row in rows
                if row.get(col["key"]) is not None
            ),
            fmt="int",
            bold=True,
        ),
    ),
    dict(
        header="Total",
        key="outliers_total",
        fmt="int",
        group="Outliers",
        footer_group=dict(
            value_fn=lambda rows, col: sum(
                _to_number(row.get(col["key"]))
                for row in rows
                if row.get(col["key"]) is not None
            ),
            fmt="int",
            bold=True,
        ),
        footer=dict(
            value_fn=lambda rows, col: sum(
                _to_number(row.get(col["key"]))
                for row in rows
                if row.get(col["key"]) is not None
            ),
            fmt="int",
            bold=True,
        ),
    ),
    dict(
        header=r"\%",
        key="outliers_pct",
        fmt=".1f",
        group="Outliers",
        threshold_min=10,
        footer_group=dict(
            value_fn=lambda rows, col: np.mean(
                [
                    _to_number(row.get(col["key"]))
                    for row in rows
                    if row.get(col["key"]) is not None
                ]
            ),
            fmt=".1f",
            threshold_min=10,
            bold=True,
        ),
        footer=dict(
            value_fn=lambda rows, col: np.mean(
                [
                    _to_number(row.get(col["key"]))
                    for row in rows
                    if row.get(col["key"]) is not None
                ]
            ),
            fmt=".1f",
            threshold_min=10,
            bold=True,
        ),
    ),
    # Shapiro OR
    dict(
        header=r"\( \mathrm{stat}_{\OR} \)",
        key="shapiro_stat_or",
        fmt=".3f",
        min_val=0.001,
        group="Shapiro-Wilk",
    ),
    dict(
        header=r"\( p_{\OR} \)",
        key="shapiro_p_or",
        fmt=".3f",
        threshold_min=0.05,
        min_val=0.001,
        max_val=0.999,
        group="Shapiro-Wilk",
    ),
    # Shapiro RF
    dict(
        header=r"\( \mathrm{stat}_{\RF} \)",
        key="shapiro_stat_rf",
        fmt=".3f",
        min_val=0.001,
        group="Shapiro-Wilk",
    ),
    dict(
        header=r"\( p_{\RF} \)",
        key="shapiro_p_rf",
        fmt=".3f",
        threshold_min=0.05,
        min_val=0.001,
        max_val=0.999,
        group="Shapiro-Wilk",
    ),
]

ENERGY_TABLE = [
    dict(
        header=r"\textbf{Test smell}",
        key="type_full",
        fmt="str",
        show_first_only=True,
        footer_group=dict(value=r"Total", fmt="str", bold=True),
        footer=dict(value=r"All instances", fmt="str", bold=True),
    ),
    dict(
        header=r"\textbf{Instance}",
        key="instance_id",
        fmt="int",
        # bold=True,
    ),
    dict(
        header=r"\( \mu_{\OR} \)",
        key="energy_mean_or",
        fmt=".2f",
        group="Energy (J)",
        footer_group=dict(
            value_fn=lambda rows, col: average_from_rows(rows, col["key"]),
            fmt=".2f",
            bold=True,
        ),
        footer=dict(
            value_fn=lambda rows, col: average_from_rows(rows, col["key"]),
            fmt=".2f",
            bold=True,
        ),
    ),
    dict(
        header=r"\( \sigma_{\OR} \)",
        key="energy_std_or",
        fmt=".2f",
        group="Energy (J)",
        footer_group=dict(
            value_fn=lambda rows, col: average_std_from_rows(rows, col["key"]),
            fmt=".2f",
            bold=True,
        ),
        footer=dict(
            value_fn=lambda rows, col: average_std_from_rows(rows, col["key"]),
            fmt=".2f",
            bold=True,
        ),
    ),
    dict(
        header=r"\( \mu_{\RF} \)",
        key="energy_mean_rf",
        fmt=".2f",
        group="Energy (J)",
        footer_group=dict(
            value_fn=lambda rows, col: average_from_rows(rows, col["key"]),
            fmt=".2f",
            bold=True,
        ),
        footer=dict(
            value_fn=lambda rows, col: average_from_rows(rows, col["key"]),
            fmt=".2f",
            bold=True,
        ),
    ),
    dict(
        header=r"\( \sigma_{\RF} \)",
        key="energy_std_rf",
        fmt=".2f",
        group="Energy (J)",
        footer_group=dict(
            value_fn=lambda rows, col: average_std_from_rows(rows, col["key"]),
            fmt=".2f",
            bold=True,
        ),
        footer=dict(
            value_fn=lambda rows, col: average_std_from_rows(rows, col["key"]),
            fmt=".2f",
            bold=True,
        ),
    ),
    dict(
        header=r"\( t \)",
        key="t_stat",
        fmt=".3f",
        group=r"Welch's \(t\)-test",
    ),
    dict(
        header=r"\( p \)",
        key="t_p",
        fmt=".3f",
        threshold_max=0.05,
        min_val=0.001,
        group=r"Welch's \(t\)-test",
    ),
    dict(
        header=r"\( \Delta \mu \)",
        key="energy_diff",
        fmt=".2f",
        group="Effect size",
        footer_group=dict(
            value_fn=lambda rows, _: average_from_rows(rows, "energy_mean_rf")
            - average_from_rows(rows, "energy_mean_or"),
            bold=True,
            fmt=".2f",
        ),
        footer=dict(
            value_fn=lambda rows, _: average_from_rows(rows, "energy_mean_rf")
            - average_from_rows(rows, "energy_mean_or"),
            fmt=".2f",
            bold=True,
        ),
    ),
    dict(
        header=r"\( \Delta \% \)",
        key="energy_pct",
        fmt=".2f",
        threshold_min=5,
        abs_threshold=True,
        group="Effect size",
        footer_group=dict(
            value_fn=lambda rows, col: median_from_rows(rows, col["key"]),
            fmt=".2f",
            bold=True,
        ),
        footer=dict(
            value_fn=lambda rows, col: median_from_rows(rows, col["key"]),
            fmt=".2f",
            bold=True,
        ),
    ),
    dict(
        header=r"\( d \)",
        key="energy_cohen_d",
        fmt=".2f",
        threshold_min=0.8,
        abs=False,
        abs_threshold=True,
        group="Effect size",
    ),
]

COVERAGE_TABLE = [
    dict(
        header=r"\textbf{Test smell}",
        key="type_full",
        fmt="str",
        show_first_only=True,
        footer_group=dict(value=r"Total", fmt="str", bold=True),
        footer=dict(value=r"All instances", fmt="str", bold=True),
    ),
    dict(
        header=r"\textbf{Instance}",
        key="instance_id",
        fmt="int",
        # bold=True,
    ),
    # branches
    dict(
        header="OR",
        key="branches_or",
        fmt="int",
        group="Branches",
        footer_group=dict(
            value_fn=lambda rows, col: sum_from_rows(rows, col["key"]),
            fmt="int",
            bold=True,
        ),
        footer=dict(
            value_fn=lambda rows, col: sum_from_rows(rows, col["key"]),
            fmt="int",
            bold=True,
        ),
    ),
    dict(
        header="RF",
        key="branches_rf",
        fmt="int",
        group="Branches",
        footer_group=dict(
            value_fn=lambda rows, col: sum_from_rows(rows, col["key"]),
            fmt="int",
            bold=True,
        ),
        footer=dict(
            value_fn=lambda rows, col: sum_from_rows(rows, col["key"]),
            fmt="int",
            bold=True,
        ),
    ),
    dict(
        header=r"\( \Delta \)",
        key="delta_branches",
        fmt="int",
        group="Branches",
        show_sign=True,
        footer_group=dict(
            value_fn=lambda rows, col: sum_from_rows(rows, col["key"]),
            fmt="int",
            bold=True,
        ),
        footer=dict(
            value_fn=lambda rows, col: sum_from_rows(rows, col["key"]),
            fmt="int",
            bold=True,
        ),
    ),
    dict(
        header="Total",
        key="branches_total",
        fmt="int",
        group="Branches",
        footer_group=dict(
            value_fn=lambda rows, col: sum_from_rows(rows, col["key"]),
            fmt="int",
            bold=True,
        ),
        footer=dict(
            value_fn=lambda rows, col: sum_from_rows(rows, col["key"]),
            fmt="int",
            bold=True,
        ),
    ),
    # code coverage
    dict(
        header="OR",
        key="branch_score_or",
        fmt=".3f",
        group="Code coverage",
        footer_group=dict(
            value_fn=lambda rows, col: average_from_rows(rows, col["key"]),
            fmt=".3f",
            bold=True,
        ),
        footer=dict(
            value_fn=lambda rows, col: average_from_rows(rows, col["key"]),
            fmt=".3f",
            bold=True,
        ),
    ),
    dict(
        header="RF",
        key="branch_score_rf",
        fmt=".3f",
        group="Code coverage",
        footer_group=dict(
            value_fn=lambda rows, col: average_from_rows(rows, col["key"]),
            fmt=".3f",
            bold=True,
        ),
        footer=dict(
            value_fn=lambda rows, col: average_from_rows(rows, col["key"]),
            fmt=".3f",
            bold=True,
        ),
    ),
    # mutants
    dict(
        header="OR",
        key="mutants_or",
        fmt="int",
        group="Mutants",
        footer_group=dict(
            value_fn=lambda rows, col: sum_from_rows(rows, col["key"]),
            fmt="int",
            bold=True,
        ),
        footer=dict(
            value_fn=lambda rows, col: sum_from_rows(rows, col["key"]),
            fmt="int",
            bold=True,
        ),
    ),
    dict(
        header="RF",
        key="mutants_rf",
        fmt="int",
        group="Mutants",
        footer_group=dict(
            value_fn=lambda rows, col: sum_from_rows(rows, col["key"]),
            fmt="int",
            bold=True,
        ),
        footer=dict(
            value_fn=lambda rows, col: sum_from_rows(rows, col["key"]),
            fmt="int",
            bold=True,
        ),
    ),
    dict(
        header=r"\( \Delta \)",
        key="delta_mutants",
        fmt="int",
        group="Mutants",
        show_sign=True,
        footer_group=dict(
            value_fn=lambda rows, col: sum_from_rows(rows, col["key"]),
            fmt="int",
            bold=True,
        ),
        footer=dict(
            value_fn=lambda rows, col: sum_from_rows(rows, col["key"]),
            fmt="int",
            bold=True,
        ),
    ),
    # mutants total
    dict(
        header="Total",
        key="mutants_total",
        fmt="int",
        group="Mutants",
        footer_group=dict(
            value_fn=lambda rows, col: sum_from_rows(rows, col["key"]),
            fmt="int",
            bold=True,
        ),
        footer=dict(
            value_fn=lambda rows, col: sum_from_rows(rows, col["key"]),
            fmt="int",
            bold=True,
        ),
    ),
    # mutation score
    dict(
        header="OR",
        key="mutation_score_or",
        fmt=".3f",
        group="Mutation score",
        footer_group=dict(
            value_fn=lambda rows, col: average_from_rows(rows, col["key"]),
            fmt=".3f",
            bold=True,
        ),
        footer=dict(
            value_fn=lambda rows, col: average_from_rows(rows, col["key"]),
            fmt=".3f",
            bold=True,
        ),
    ),
    dict(
        header="RF",
        key="mutation_score_rf",
        fmt=".3f",
        group="Mutation score",
        footer_group=dict(
            value_fn=lambda rows, col: average_from_rows(rows, col["key"]),
            fmt=".3f",
            bold=True,
        ),
        footer=dict(
            value_fn=lambda rows, col: average_from_rows(rows, col["key"]),
            fmt=".3f",
            bold=True,
        ),
    ),
]

QUALITY_TABLE = [
    dict(
        header=r"\textbf{Test smell}",
        key="type_full",
        fmt="str",
        show_first_only=True,
        footer_group=dict(value=r"Total", fmt="str", bold=True),
        footer=dict(value=r"All instances", fmt="str", bold=True),
    ),
    dict(
        header=r"\textbf{Instance}",
        key="instance_id",
        fmt="int",
        # bold=True,
    ),
    dict(
        header="OR",
        key="loc_or",
        fmt="int",
        group="Unit size (LOC)",
        footer_group=dict(
            value_fn=lambda rows, col: sum_from_rows(rows, col["key"]),
            fmt="int",
            bold=True,
        ),
        footer=dict(
            value_fn=lambda rows, col: sum_from_rows(rows, col["key"]),
            fmt="int",
            bold=True,
        ),
    ),
    dict(
        header="RF",
        key="loc_rf",
        fmt="int",
        group="Unit size (LOC)",
        footer_group=dict(
            value_fn=lambda rows, col: sum_from_rows(rows, col["key"]),
            fmt="int",
            bold=True,
        ),
        footer=dict(
            value_fn=lambda rows, col: sum_from_rows(rows, col["key"]),
            fmt="int",
            bold=True,
        ),
    ),
    dict(
        header=r"\( \Delta \)",
        key="delta_loc",
        fmt="int",
        show_sign=True,
        group="Unit size (LOC)",
        footer_group=dict(
            value_fn=lambda rows, col: sum_from_rows(rows, col["key"]),
            fmt="int",
            show_sign=True,
            bold=True,
        ),
        footer=dict(
            value_fn=lambda rows, col: sum_from_rows(rows, col["key"]),
            fmt="int",
            show_sign=True,
            bold=True,
        ),
    ),
    dict(
        header="OR",
        key="assertions_or",
        fmt="int",
        group="Assertions",
        footer_group=dict(
            value_fn=lambda rows, col: sum_from_rows(rows, col["key"]),
            fmt="int",
            bold=True,
        ),
        footer=dict(
            value_fn=lambda rows, col: sum_from_rows(rows, col["key"]),
            fmt="int",
            bold=True,
        ),
    ),
    dict(
        header="RF",
        key="assertions_rf",
        fmt="int",
        group="Assertions",
        footer_group=dict(
            value_fn=lambda rows, col: sum_from_rows(rows, col["key"]),
            fmt="int",
            bold=True,
        ),
        footer=dict(
            value_fn=lambda rows, col: sum_from_rows(rows, col["key"]),
            fmt="int",
            bold=True,
        ),
    ),
    dict(
        header=r"\( \Delta \)",
        key="delta_assertions",
        fmt="int",
        show_sign=True,
        group="Assertions",
        footer_group=dict(
            value_fn=lambda rows, col: sum_from_rows(rows, col["key"]),
            fmt="int",
            bold=True,
            show_sign=True,
        ),
        footer=dict(
            value_fn=lambda rows, col: sum_from_rows(rows, col["key"]),
            fmt="int",
            bold=True,
            show_sign=True,
        ),
    ),
    dict(
        header="OR",
        key="density_or",
        fmt=".2f",
        group="Assertion density",
        footer_group=dict(
            value_fn=lambda rows, col: average_from_rows(rows, col["key"]),
            fmt=".2f",
            bold=True,
        ),
        footer=dict(
            value_fn=lambda rows, col: average_from_rows(rows, col["key"]),
            fmt=".2f",
            bold=True,
        ),
    ),
    dict(
        header="RF",
        key="density_rf",
        fmt=".2f",
        group="Assertion density",
        footer_group=dict(
            value_fn=lambda rows, col: average_from_rows(rows, col["key"]),
            fmt=".2f",
            bold=True,
        ),
        footer=dict(
            value_fn=lambda rows, col: average_from_rows(rows, col["key"]),
            fmt=".2f",
            bold=True,
        ),
    ),
]


# compute Spearman rho per type
def spearman_metrics_per_type(rows):
    rows_by_type = {}
    for row in rows:
        t = row.get("type")
        rows_by_type.setdefault(t, []).append(row)

    results = []
    for type_name, type_rows in sorted(rows_by_type.items()):
        df_list = [
            row.get("df_total")
            for row in type_rows
            if isinstance(row.get("df_total"), pd.DataFrame)
        ]
        df_type = pd.concat(df_list, ignore_index=True) if df_list else pd.DataFrame()

        for metric in METRICS.keys():
            metric_col = FALLBACKS.get(metric, metric)
            if metric_col == "energy":
                continue

            if (
                df_type.empty
                or ("energy" not in df_type.columns)
                or (metric_col not in df_type.columns)
            ):
                results.append(
                    {
                        "type": type_name,
                        "metric": metric,
                        "rho": np.nan,
                        "p": np.nan,
                        "n": 0,
                    }
                )
                continue

            combined = pd.DataFrame(
                {
                    "energy": pd.to_numeric(df_type["energy"], errors="coerce"),
                    "metric": pd.to_numeric(df_type[metric_col], errors="coerce"),
                }
            ).dropna()

            if len(combined) >= 2:
                rho, p = spearmanr(combined["energy"], combined["metric"])
                n = len(combined)
            else:
                rho, p, n = np.nan, np.nan, len(combined)

            results.append(
                {
                    "type": type_name,
                    "metric": metric,
                    "rho": rho,
                    "p": p,
                    "n": n,
                }
            )

    return pd.DataFrame(results).sort_values(["type"]).reset_index(drop=True)


def spearman_metrics(rows):
    df_list = [
        row.get("df_total")
        for row in rows
        if isinstance(row.get("df_total"), pd.DataFrame)
    ]
    df_all = pd.concat(df_list, ignore_index=True) if df_list else pd.DataFrame()

    results = []
    for metric in METRICS.keys():
        metric_col = FALLBACKS.get(metric, metric)
        if metric_col == "energy":
            continue

        if (
            df_all.empty
            or ("energy" not in df_all.columns)
            or (metric_col not in df_all.columns)
        ):
            results.append({"metric": metric, "rho": np.nan, "p": np.nan, "n": 0})
            continue

        combined = pd.DataFrame(
            {
                "energy": pd.to_numeric(df_all["energy"], errors="coerce"),
                "metric": pd.to_numeric(df_all[metric_col], errors="coerce"),
            }
        ).dropna()

        if len(combined) >= 2:
            rho, p = spearmanr(combined["energy"], combined["metric"])
            n = len(combined)
        else:
            rho, p, n = np.nan, np.nan, len(combined)

        results.append({"metric": metric, "rho": rho, "p": p, "n": n})

    return pd.DataFrame(results).sort_values(["metric"]).reset_index(drop=True)
