package me.anemoi.rendertools.config.modules;

import cc.polyfrost.oneconfig.config.annotations.Color;
import cc.polyfrost.oneconfig.config.annotations.Dropdown;
import cc.polyfrost.oneconfig.config.annotations.Slider;
import cc.polyfrost.oneconfig.config.annotations.Switch;
import cc.polyfrost.oneconfig.config.core.OneColor;

public class ChinaHatConfig {
    @Switch(name = "Toggled")
    public static boolean toggled = true;

    @Slider(name = "Radius", min = 0.5F, max = 1.0F)
    public static float radius = 0.7F;

    @Slider(name = "Height", min = 0.1F, max = 0.7F)
    public static float height = 0.3F;

    @Slider(name = "Position", min = -0.5F, max = 0.5F)
    public static float pos = 0.1F;

    @Slider(name = "Rotation", min = 0.0F, max = 5F)
    public static float rotation = 5.0F;

    @Slider(name = "Angles", min = 4.0F, max = 90.0F)
    public static float angles = 8.0F;

    @Switch(name = "Show in First Person")
    public static boolean firstPerson = false;

    @Dropdown(name = "Mode", options = {"Normal", "Gradient", "Rainbow"})
    public static int mode = 0;

    @Color(name = "Color")
    public static OneColor color = new OneColor(255, 255, 255, 255);

    @Color(name = "Color 2")
    public static OneColor color2 = new OneColor(255, 255, 255, 255);
}
