from __future__ import annotations
from dataclasses import asdict, dataclass, field
import xml.etree.ElementTree as ET
from plotly.graph_objects import Figure, Layout, layout
import plotly.io as pio
from tailwind_colors import TAILWIND_COLORS_HEX as TW_HEX


def hex_to_rgb(hex_str):
    h = hex_str.lstrip("#")
    if len(h) == 3:
        h = "".join([c * 2 for c in h])
    return tuple(int(h[i : i + 2], 16) for i in (0, 2, 4))


def rgba(first, *args, alpha=1.0):
    # hex
    if isinstance(first, str) and first.startswith("#"):
        r, g, b = hex_to_rgb(first)
        a = alpha

    # list or tuple
    elif isinstance(first, (list, tuple)):
        vals = list(first)
        # Handle (r, g, b, a) tuples if alpha wasn't explicitly changed
        if len(vals) == 4 and alpha == 1.0:
            r, g, b, a = vals
        else:
            r, g, b = vals[:3]
            a = alpha

    # rgba(255, 255, 255)
    else:
        r = first
        g, b = args[0], args[1]
        a = alpha

    return f"rgba({r}, {g}, {b}, {a})"


class COLORS:
    ERROR = "#EF553B"  # red
    SUCCESS = "#00CC96"  # green
    PRIMARY = "#636EFA"  # blue
    SECONDARY = "#FFA15A"  # orange
    PRIMARY_2 = "#AB63FA"  # purple
    SECONDARY_2 = "#B6E880"  # lime
    PRIMARY_3 = "#19D3F3"  # cyan
    SECONDARY_3 = "#FECB52"  # yellow
    MUTED = TW_HEX.NEUTRAL_400  # grey
    BLACK = "black"
    WHITE = "white"
    TRANSPARENT = "rgba(0,0,0,0)"


class COLORS_RGB:
    ERROR = (239, 85, 59)  # red
    SUCCESS = (0, 204, 150)  # green
    PRIMARY = (99, 110, 250)  # blue
    SECONDARY = (255, 161, 90)  # orange
    PRIMARY_2 = (171, 99, 250)  # purple
    SECONDARY_2 = (182, 232, 128)  # lime
    PRIMARY_3 = (25, 211, 243)  # cyan
    SECONDARY_3 = (254, 203, 82)  # yellow
    MUTED = (163, 163, 163)  # grey


colorway = [
    COLORS.PRIMARY,
    COLORS.SECONDARY,
    COLORS.PRIMARY_2,
    COLORS.SECONDARY_2,
    COLORS.PRIMARY_3,
    COLORS.SECONDARY_3,
]


# charting theme
@dataclass
class Light:
    template_name: str = "light"
    radius: int = 0
    bg_color: str = "white"
    font_family: str = "Nimbus Sans L, sans-serif"
    font_color: str = "black"
    title_color: str = "black"
    legend_color: str = "black"
    legend_bg_color: str = COLORS.TRANSPARENT
    subtitle_color: str = TW_HEX.NEUTRAL_500
    x_tick_color: str = COLORS.MUTED
    x_line_color: str = COLORS.MUTED
    y_tick_color: str = COLORS.MUTED
    y_line_color: str = COLORS.MUTED


@dataclass
class ThemePalette(Light):
    colorway: list[str] | None = field(default_factory=lambda: colorway.copy())


@dataclass(slots=True)
class ChartDefaults:
    static_plot: bool = False

    # title
    title: str | None = None
    title_size: int = 22
    title_x: float = 0.5
    title_y: float = 0.95
    title_xanchor: str = "center"
    title_yanchor: str = "top"

    # subtitle
    subtitle: str | None = None
    subtitle_size: int = 14
    subtitle_x: float = 0.5
    subtitle_y: float = 1.08
    subtitle_xanchor: str = "center"
    subtitle_yanchor: str = "top"

    # x-axis
    x_title: str | None = None
    x_autorange: bool = True
    x_showgrid: bool = False
    x_zeroline: bool = False
    x_showline: bool = True
    x_line_width: float = 1
    x_title_standoff: float = 12
    x_tick_size: float = 14
    x_tick_standoff: float = 6
    x_tick_len: float = 6
    x_tick_pos: str = "outside"

    # y-axis
    y_title: str | None = None
    y_autorange: bool = True
    y_showgrid: bool = False
    y_zeroline: bool = False
    y_showline: bool = True
    y_line_width: float = 1
    y_title_standoff: float = 12
    y_tick_size: float = 14
    y_tick_standoff: float = 6
    y_tick_len: float = 6
    y_tick_pos: str = "outside"

    # legend
    legend_size: float = 14
    legend_x: float = 0.5
    legend_y: float = 1.05
    legend_xanchor: str = "center"
    legend_yanchor: str = "bottom"
    legend_orientation: str = "h"

    # margins
    margin_l: float = 100
    margin_r: float = 30
    margin_t: float = 120
    margin_b: float = 100

    # padding
    padding_t: float = 0
    padding_b: float = 0
    padding_l: float = 0
    padding_r: float = 0


@dataclass
class PlotConfig(ChartDefaults, ThemePalette):
    pass


PALETTE_PRESETS: dict[str, ThemePalette] = {
    Light.template_name: ThemePalette(**asdict(Light())),
}


def build_config(
    palette: str,
    **overrides,
) -> PlotConfig:
    base = {
        **asdict(ChartDefaults()),
        **asdict(PALETTE_PRESETS[palette]),
    }
    base.update(overrides)
    return PlotConfig(**base)


def register_config(config: dict) -> None:
    """Register a Plotly template driven by config values."""
    pio.templates[config["template_name"]] = layout.Template(
        layout=Layout(
            paper_bgcolor=config["bg_color"],
            plot_bgcolor=config["bg_color"],
            font=dict(color=config["font_color"], family=config["font_family"]),
            colorway=config["colorway"],
            title=dict(
                font=dict(size=config["title_size"], color=config["title_color"]),
            ),
            legend=dict(
                x=config["legend_x"],
                y=config["legend_y"],
                orientation=config["legend_orientation"],
                xanchor=config["legend_xanchor"],
                yanchor=config["legend_yanchor"],
                bgcolor=config["legend_bg_color"],
                font=dict(size=config["legend_size"], color=config["legend_color"]),
            ),
            margin=dict(
                l=config["margin_l"],
                r=config["margin_r"],
                t=config["margin_t"],
                b=config["margin_b"],
            ),
            barcornerradius=config["radius"],
            xaxis=dict(
                showgrid=config["x_showgrid"],
                zeroline=config["x_zeroline"],
                showline=config["x_showline"],
                linecolor=config["x_line_color"],
                linewidth=config["x_line_width"],
                title_standoff=config["x_title_standoff"],
                ticks=config["x_tick_pos"],
                ticklen=config["x_tick_len"],
                tickcolor=config["x_tick_color"],
                ticklabelstandoff=config["x_tick_standoff"],
                tickfont=dict(size=config["x_tick_size"]),
            ),
            yaxis=dict(
                showgrid=config["y_showgrid"],
                zeroline=config["y_zeroline"],
                showline=config["y_showline"],
                linecolor=config["y_line_color"],
                linewidth=config["y_line_width"],
                title_standoff=config["y_title_standoff"],
                ticks=config["y_tick_pos"],
                ticklen=config["y_tick_len"],
                tickcolor=config["y_tick_color"],
                ticklabelstandoff=config["y_tick_standoff"],
                tickfont=dict(size=config["y_tick_size"]),
            ),
        )
    )


def apply_chart_theme(
    fig: Figure,
    config_name: str = Light.template_name,
    overrides: dict | None = None,
) -> Figure:
    """Apply a fully configurable theme and optional chart decorations."""
    config = asdict(build_config(palette=config_name, **(overrides or {})))
    register_config(config)

    fig.update_layout(template=config["template_name"])

    if config["title"] is not None:
        fig.update_layout(title=config["title"])

    if config["subtitle"] is not None:
        fig.add_annotation(
            xref="paper",
            yref="paper",
            xanchor=config["subtitle_xanchor"],
            yanchor=config["subtitle_yanchor"],
            x=config["subtitle_x"],
            y=config["subtitle_y"],
            showarrow=False,
            text=config["subtitle"],
            font=dict(
                size=config["subtitle_size"],
                color=config["subtitle_color"],
            ),
        )

    if config["x_title"] is not None:
        fig.update_xaxes(title_text=config["x_title"])
    if config["y_title"] is not None:
        fig.update_yaxes(title_text=config["y_title"])

    fig.update_xaxes(autorange=config["x_autorange"])
    fig.update_yaxes(autorange=config["y_autorange"])

    if not config["y_autorange"]:
        y_range = fig.layout.yaxis.range
        if y_range is not None and len(y_range) == 2:
            padding_t = config["padding_t"]
            padding_b = config["padding_b"]
            fig.update_yaxes(
                range=[
                    y_range[0] - padding_b,
                    y_range[1] + padding_t,
                ]
            )

    if not config["x_autorange"]:
        x_range = fig.layout.xaxis.range
        if x_range is not None and len(x_range) == 2:
            padding_l = config["padding_l"]
            padding_r = config["padding_r"]
            fig.update_xaxes(
                range=[
                    x_range[0] - padding_l,
                    x_range[1] + padding_r,
                ]
            )

    if config["static_plot"]:
        fig.update_layout(dragmode=False)
        fig.update_xaxes(fixedrange=True)
        fig.update_yaxes(fixedrange=True)

    return fig
