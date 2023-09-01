package me.anemoi.rendertools.config.modules;

import cc.polyfrost.oneconfig.config.annotations.Color;
import cc.polyfrost.oneconfig.config.annotations.Slider;
import cc.polyfrost.oneconfig.config.annotations.Switch;
import cc.polyfrost.oneconfig.config.core.OneColor;

public class JumpCircleConfig {
    @Switch(name = "Toggled")
    public static boolean toggled = false;

    @Color(name = "Color")
    public static OneColor color = new OneColor(new java.awt.Color(105, 242, 255, 255));

    @Slider(name = "Time", min = 1000, max = 3000)
    public static int time = 1000;

    @Slider(name = "Radius", min = 1, max = 5)
    public static float radius = 2f;

    @Switch(name = "Better Rainbow")
    public static boolean rainbow = false;

}
