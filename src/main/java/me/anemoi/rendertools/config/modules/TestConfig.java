package me.anemoi.rendertools.config.modules;

import cc.polyfrost.oneconfig.config.annotations.Color;
import cc.polyfrost.oneconfig.config.annotations.Switch;
import cc.polyfrost.oneconfig.config.core.OneColor;

public class TestConfig {
    @Switch(name = "Toggled", description = "Toggles the module on or off")
    public static boolean toggled = false;

    @Color(name = "Color", description = "The color of the module")
    public static OneColor color = new OneColor(new java.awt.Color(255, 0, 0, 255));

}
