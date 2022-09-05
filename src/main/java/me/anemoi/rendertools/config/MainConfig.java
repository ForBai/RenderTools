package me.anemoi.rendertools.config;

import cc.polyfrost.oneconfig.config.annotations.Page;
import cc.polyfrost.oneconfig.config.data.PageLocation;
import me.anemoi.rendertools.RenderTools;
import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.data.Mod;
import cc.polyfrost.oneconfig.config.data.ModType;
import me.anemoi.rendertools.config.modules.*;

public class MainConfig extends Config {

    @Page(name = "China Hat", location = PageLocation.TOP)
    public static ChinaHatConfig chinaHat = new ChinaHatConfig();

    @Page(name = "AnimationCreator", location = PageLocation.TOP)
    public static AnimationCreatorConfig animationCreator = new AnimationCreatorConfig();

    @Page(name = "Animations", location = PageLocation.TOP)
    public static AnimationsConfig animations = new AnimationsConfig();

    @Page(name = "Hit Animation", location = PageLocation.TOP)
    public static HitAnimationConfig hitAnimation = new HitAnimationConfig();

    @Page(name = "Camera Helper", location = PageLocation.TOP)
    public static CameraConfig cameraHelper = new CameraConfig();

    public MainConfig() {
        super(new Mod(RenderTools.NAME, ModType.UTIL_QOL), RenderTools.MODID + ".json");
        initialize();
    }
}

