package me.anemoi.rendertools.config;

import cc.polyfrost.oneconfig.config.annotations.KeyBind;
import cc.polyfrost.oneconfig.config.annotations.Page;
import cc.polyfrost.oneconfig.config.core.OneKeyBind;
import cc.polyfrost.oneconfig.config.data.PageLocation;
import cc.polyfrost.oneconfig.libs.universal.UKeyboard;
import cc.polyfrost.oneconfig.utils.TickDelay;
import me.anemoi.rendertools.RenderTools;
import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.data.Mod;
import cc.polyfrost.oneconfig.config.data.ModType;
import me.anemoi.rendertools.config.modules.*;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;

import java.util.ArrayList;
import java.util.Arrays;

import static me.anemoi.rendertools.RenderTools.mc;

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

    @Page(name = "Nick Hider", location = PageLocation.TOP)
    public static NickHiderConfig nickHider = new NickHiderConfig();

    @KeyBind(name = "Create Ghost Block's", category = "Other")
    public static OneKeyBind createGhostBlocks = new OneKeyBind(UKeyboard.KEY_NONE);

    public MainConfig() {
        super(new Mod(RenderTools.NAME, ModType.UTIL_QOL), RenderTools.MODID + ".json");
        initialize();
    }
}

