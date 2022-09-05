package me.anemoi.rendertools.config.modules;

import cc.polyfrost.oneconfig.config.annotations.Info;
import cc.polyfrost.oneconfig.config.annotations.Slider;
import cc.polyfrost.oneconfig.config.annotations.Switch;
import cc.polyfrost.oneconfig.config.data.InfoType;

public class CameraConfig {
    @Switch(name = "Toggled")
    public static boolean toggled = false;

    @Slider(name = "Camera Distance", min = 2.0F, max = 10.0F)
    public static float cameraDistance = 4.0F;

    @Switch(name = "No hurt cam")
    public static boolean noHurtCam = false;

    @Info(
            text = "Warning: No Clip could cause to get you banned on some servers.",
            type = InfoType.WARNING
    )
    public static boolean ignored;


    @Switch(name = "No Clip")
    public static boolean noClip = false;
}
