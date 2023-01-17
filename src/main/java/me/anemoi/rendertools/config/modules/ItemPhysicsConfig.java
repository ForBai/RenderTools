package me.anemoi.rendertools.config.modules;

import cc.polyfrost.oneconfig.config.annotations.Slider;
import cc.polyfrost.oneconfig.config.annotations.Switch;

public class ItemPhysicsConfig {
    @Switch(name = "Item Physics", description = "Enable item physics! Dont work")
    public static boolean itemPhysics = true;

    @Slider(name = "Item Physics - Strength", description = "Item physics strength", min = 0, max = 10)
    public static int strength = 5;

    @Slider(name = "Item Size", description = "Item size", min = 0f, max = 5f)
    public static float itemSize = 1f;

}
