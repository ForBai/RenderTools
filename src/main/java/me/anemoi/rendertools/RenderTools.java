package me.anemoi.rendertools;

import cc.polyfrost.oneconfig.events.EventManager;
import cc.polyfrost.oneconfig.utils.NetworkUtils;
import cc.polyfrost.oneconfig.utils.commands.CommandManager;
import me.anemoi.rendertools.command.ExampleCommand;
import me.anemoi.rendertools.config.MainConfig;
import me.anemoi.rendertools.modules.*;
import me.anemoi.rendertools.utils.SwingHelper;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@net.minecraftforge.fml.common.Mod(modid = RenderTools.MODID, name = RenderTools.NAME, version = RenderTools.VERSION)
public class RenderTools {
    public static final String MODID = "@ID@";
    public static final String NAME = "@NAME@";
    public static final String VERSION = "@VER@";
    @net.minecraftforge.fml.common.Mod.Instance(MODID)
    public static RenderTools INSTANCE;
    public static Minecraft mc = Minecraft.getMinecraft();
    public static SwingHelper swingHelper = new SwingHelper();
    public static boolean canUseHidden = false;
    public MainConfig config;
    private List<String> whiteListedUUIDs = new ArrayList<>();
    public static boolean isDev = true; //if this is true in a release i am dumb

    @net.minecraftforge.fml.common.Mod.EventHandler
    public void onFMLInitialization(net.minecraftforge.fml.common.event.FMLInitializationEvent event) {
        config = new MainConfig();
        CommandManager.INSTANCE.registerCommand(new ExampleCommand());

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new ChinaHat());
        MinecraftForge.EVENT_BUS.register(new GhostBlocker());
        //EventManager.INSTANCE.register(new Bre());
        MinecraftForge.EVENT_BUS.register(new BreadCrumbsNew());
        MinecraftForge.EVENT_BUS.register(new TestModule());
        EventManager.INSTANCE.register(new TestModule());
        EventManager.INSTANCE.register(new GhostBlocker());
        EventManager.INSTANCE.register(new Trajectories());
        MinecraftForge.EVENT_BUS.register(new Trajectories());
        MinecraftForge.EVENT_BUS.register(new BlockOverlay());


        if (!isDev) {
            //get the white list from https://gist.githubusercontent.com/ForBai/d455aa0be5602bb91900858e3d4760eb/raw/a65f20b7bfc55eaae2d3dbdb2be24d1ab5824eca/wihtelist
            //and put it in the whiteListedUUIDs
            String[] lines = NetworkUtils.getString("https://gist.githubusercontent.com/ForBai/d455aa0be5602bb91900858e3d4760eb/raw/a65f20b7bfc55eaae2d3dbdb2be24d1ab5824eca/wihtelist")
                    .split("\n");
            whiteListedUUIDs.addAll(Arrays.asList(lines));

            //get the uuid of the player and check if it is in the white list
            canUseHidden = whiteListedUUIDs.contains(mc.getSession().getPlayerID().replaceAll("-", ""));
        } else {
            canUseHidden = true;
        }

    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (!canUseHidden) {
            MainConfig.anemoi = false;
        }
        if (event.phase == TickEvent.Phase.END && swingHelper != null) {
            swingHelper.update();
        }
    }

}
