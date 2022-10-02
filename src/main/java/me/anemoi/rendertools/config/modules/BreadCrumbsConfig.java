package me.anemoi.rendertools.config.modules;

import cc.polyfrost.oneconfig.config.annotations.*;
import cc.polyfrost.oneconfig.config.core.OneColor;

/*
Credits to FDPClient for the original code
https://github.com/SkidderMC/FDPClient/blob/main/LICENSE
if there is any issue with this code please contact me on discord: Anemoi#7990
 */

public class BreadCrumbsConfig {
    @Header(text = "BreadCrumbs")
    public static boolean ignored;
    @Switch(
            name = "Toggled",
            description = "Toggles the BreadCrumbs module."
    )
    public static boolean toggled = false;

    @Dropdown(
            name = "Mode",
            description = "The mode of the BreadCrumbs module.",
            options = {"Line", "Rect", "Sphere", "Rise"}
    )
    public static int mode = 0;

    @Color(
            name = "Color",
            description = "The color of the BreadCrumbs module."
    )
    public static OneColor color = new OneColor(0, 255, 0, 255);

    @Switch(
            name = "Fade Away",
            description = "Makes the BreadCrumbs fade away."
    )
    public static boolean fadeValue = true;

    @Switch(
            name = "Draw Own",
            description = "Draws your own Player."
    )
    public static boolean drawOwn = true;

    @Switch(
            name = "Draw Others",
            description = "Draws other Players."
    )
    public static boolean drawOthers = false;

    @Slider(
            name = "Fade Time",
            description = "The time it takes for the BreadCrumbs to fade away.",
            min = 1.0F,
            max = 20.0F,
            step = 1
    )
    public static int fadeTime = 5;

    @Slider(
            name = "Precision",
            description = "The precision of the BreadCrumbs.",
            min = 1.0F,
            max = 20.0F,
            step = 1
    )
    public static int precision = 4;

    @Slider(
            name = "Width",
            description = "The width of the BreadCrumbs.",
            min = 1.0F,
            max = 10.0F,
            step = 1
    )
    public static int width = 1;

    @Slider(
            name = "Sphere Scale",
            description = "The scale of the Sphere BreadCrumbs.",
            min = 0.1F,
            max = 2.0F
    )
    public static float sphereScale = 0.6F;

    @Switch(
            name = "Only 3'd Person",
            description = "Only draws the BreadCrumbs in 3'd Person."
    )
    public static boolean only3dPerson = true;

    @Slider(
            name = "Alpha",
            description = "The alpha of the BreadCrumbs.",
            min = 1.0F,
            max = 255.0F
    )
    public static int alpha = 255;
}
