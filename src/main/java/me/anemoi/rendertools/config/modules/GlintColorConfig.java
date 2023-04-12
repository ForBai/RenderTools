package me.anemoi.rendertools.config.modules;

import cc.polyfrost.oneconfig.config.annotations.Color;
import cc.polyfrost.oneconfig.config.annotations.Switch;
import cc.polyfrost.oneconfig.config.core.OneColor;

public class GlintColorConfig {
    @Switch(name = "Togled", description = "Toggles the module")
    public static boolean toggled = false;

    @Color(name = "Color", description = "Render color")
    public static OneColor color = new OneColor(new java.awt.Color(255, 255, 255, 255));
}
