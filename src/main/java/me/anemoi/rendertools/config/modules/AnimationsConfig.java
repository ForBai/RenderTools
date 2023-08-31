package me.anemoi.rendertools.config.modules;

import cc.polyfrost.oneconfig.config.annotations.Dropdown;
import cc.polyfrost.oneconfig.config.annotations.Slider;
import cc.polyfrost.oneconfig.config.annotations.Switch;

public class AnimationsConfig {
    @Switch(name = "Toggled")
    public static boolean toggled = true;

    @Slider(name = "x", min = 0.01F, max = 3.0F)
    public static float x = 1.0F;

    @Slider(name = "y", min = 0.01F, max = 3.0F)
    public static float y = 1.0F;

    @Slider(name = "z", min = 0.01F, max = 3.0F)
    public static float z = 1.0F;

    @Slider(name = "Pitch", min = -180.0F, max = 180.0F, step = 5)
    public static float pitch = 0.0F;

    @Slider(name = "Yaw", min = -180.0F, max = 180.0F, step = 5)
    public static float yaw = 0.0F;

    @Slider(name = "Roll", min = -180.0F, max = 180.0F, step = 5)
    public static float roll = 0.0F;

    @Slider(name = "size", min = 0.01F, max = 3.0F)
    public static float size = 1.0F;

    @Slider(name = "Scaled Swing Size", min = -1.5f, max = 1.5f)
    public static float scaledSwingSize = 0.0f;

    @Dropdown(name = "Mode", options = {"1.7", "chill", "push", "spin", "vertical spin", "helicopter", "custom", "long hit", "old"})
    public static int mode = 0;

    @Switch(name = "Swing progress")
    public static boolean swingProgress = false;

    @Switch(name = "Special")
    public static boolean special = true;

    @Switch(name = "Rainbow Enchant")
    public static boolean rainbowEnchant = false;

    @Switch(name = "Scaled Swing")
    public static boolean scaledSwing = false;

    @Switch(name = "Disable Equip Progress Y")
    public static boolean disableEquipProgressY = false;

}
