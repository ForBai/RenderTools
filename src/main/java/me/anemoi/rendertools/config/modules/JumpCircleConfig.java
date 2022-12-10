package me.anemoi.rendertools.config.modules;

import cc.polyfrost.oneconfig.config.annotations.Color;
import cc.polyfrost.oneconfig.config.annotations.Switch;
import cc.polyfrost.oneconfig.config.core.OneColor;

public class JumpCircleConfig {
    @Switch(name = "Toggled")
    public static boolean toggled = false;

    @Color(name = "Color")
    public static OneColor color = new OneColor(new java.awt.Color(105, 242, 255, 255));
}
