package me.anemoi.rendertools.config.modules;

import cc.polyfrost.oneconfig.config.annotations.Slider;
import cc.polyfrost.oneconfig.config.annotations.Switch;

public class CameraConfig {
    @Switch(name = "Toggled")
    public static boolean toggled = false;

    @Slider(name = "Camera Distance", min = 2.0F, max = 10.0F)
    public static float cameraDistance = 4.0F;

    @Switch(name = "No hurt cam")
    public static boolean noHurtCam = false;
}
