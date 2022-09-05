package me.anemoi.rendertools.config;

import cc.polyfrost.oneconfig.config.annotations.Page;
import cc.polyfrost.oneconfig.config.data.PageLocation;
import me.anemoi.rendertools.RenderTools;
import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.data.Mod;
import cc.polyfrost.oneconfig.config.data.ModType;
import me.anemoi.rendertools.config.modules.ChinaHatConfig;

public class MainConfig extends Config {

    @Page(name = "China Hat", location = PageLocation.TOP)
    public static ChinaHatConfig chinaHat = new ChinaHatConfig();

    public MainConfig() {
        super(new Mod(RenderTools.NAME, ModType.UTIL_QOL), RenderTools.MODID + ".json");
        initialize();
    }
}

