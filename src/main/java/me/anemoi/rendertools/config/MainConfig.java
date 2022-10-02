package me.anemoi.rendertools.config;

import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.annotations.KeyBind;
import cc.polyfrost.oneconfig.config.annotations.Page;
import cc.polyfrost.oneconfig.config.annotations.Switch;
import cc.polyfrost.oneconfig.config.core.OneKeyBind;
import cc.polyfrost.oneconfig.config.data.Mod;
import cc.polyfrost.oneconfig.config.data.ModType;
import cc.polyfrost.oneconfig.config.data.PageLocation;
import cc.polyfrost.oneconfig.libs.universal.UKeyboard;
import me.anemoi.rendertools.RenderTools;
import me.anemoi.rendertools.config.modules.*;

public class MainConfig extends Config {

    @Page(name = "About", location = PageLocation.BOTTOM)
    public static About ignore = new About();

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

    @Page(name = "Nick Hider", location = PageLocation.TOP)
    public static NickHiderConfig nickHider = new NickHiderConfig();

    @Page(name = "Bread Crumbs",location = PageLocation.BOTTOM)
    public static BreadCrumbsConfig breadCrumbs = new BreadCrumbsConfig();

    @KeyBind(name = "Create Ghost Block's", category = "Other")
    public static OneKeyBind createGhostBlocks = new OneKeyBind(UKeyboard.KEY_NONE);

    @Switch(name = "Push Out Of Blocks", category = "Other")
    public static boolean pushOutOfBlocks = false;

    public MainConfig() {
        super(new Mod(RenderTools.NAME, ModType.UTIL_QOL), RenderTools.MODID + ".json");
        initialize();
    }
}

