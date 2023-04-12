package me.anemoi.rendertools.config.modules;

import cc.polyfrost.oneconfig.config.annotations.Checkbox;
import cc.polyfrost.oneconfig.config.annotations.Color;
import cc.polyfrost.oneconfig.config.annotations.Slider;
import cc.polyfrost.oneconfig.config.annotations.Switch;
import cc.polyfrost.oneconfig.config.core.OneColor;

public class ProjectilesConfig {
    @Switch(name = "Togled", description = "Toggles the module")
    public static boolean toggled = false;

    @Checkbox(name = "Arrow", description = "Toggles arrows")
    public static boolean arrow = true;
    @Checkbox(name = "Snowball", description = "Toggles snowballs")
    public static boolean snowball = true;
    @Checkbox(name = "Egg", description = "Toggles eggs")
    public static boolean egg = true;
    @Checkbox(name = "Ender Pearl", description = "Toggles ender pearls")
    public static boolean enderPearl = true;
    @Checkbox(name = "Splash Potion", description = "Toggles splash potions")
    public static boolean splashPotion = true;

    @Slider(name = "Time", description = "Render time", min = 1, max = 20, step = 1)
    public static int time = 2;
    @Slider(name = "Width", description = "Render width", min = 0.1f, max = 10.0f, step = 1)
    public static float width = 1.5f;

    @Color(name = "Color", description = "Render color")
    public static OneColor color = new OneColor(new java.awt.Color(255, 47, 0, 255));
}
