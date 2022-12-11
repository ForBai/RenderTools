package me.anemoi.rendertools.modules;

import cc.polyfrost.oneconfig.events.event.TickEvent;
import cc.polyfrost.oneconfig.libs.eventbus.Subscribe;
import cc.polyfrost.oneconfig.libs.universal.UKeyboard;
import me.anemoi.rendertools.config.MainConfig;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.Arrays;

import static me.anemoi.rendertools.RenderTools.mc;

public class GhostBlocker {

    @Subscribe
    public void onTick(TickEvent event) {
        if (mc.thePlayer == null || mc.theWorld == null || !MainConfig.anemoi || MainConfig.createGhostBlocks.getKeyBinds().isEmpty()) return;
        for (Integer inte : MainConfig.createGhostBlocks.getKeyBinds()) {
            if (inte == UKeyboard.KEY_NONE) return;
            if (!Keyboard.isKeyDown(inte)) return;
        }
        if (mc.objectMouseOver.getBlockPos() == null) return;
        Block block = (Minecraft.getMinecraft()).theWorld.getBlockState(mc.objectMouseOver.getBlockPos()).getBlock();
        ArrayList<Block> interactables = new ArrayList<>(Arrays.asList(Blocks.acacia_door, Blocks.anvil, Blocks.beacon, Blocks.bed, Blocks.birch_door, Blocks.brewing_stand, Blocks.command_block, Blocks.crafting_table, Blocks.chest, Blocks.dark_oak_door,
                Blocks.daylight_detector, Blocks.daylight_detector_inverted, Blocks.dispenser, Blocks.dropper, Blocks.enchanting_table, Blocks.ender_chest, Blocks.furnace, Blocks.hopper, Blocks.jungle_door, Blocks.lever,
                Blocks.noteblock, Blocks.powered_comparator, Blocks.unpowered_comparator, Blocks.powered_repeater, Blocks.unpowered_repeater, Blocks.standing_sign, Blocks.wall_sign, Blocks.trapdoor, Blocks.trapped_chest, Blocks.wooden_button,
                Blocks.stone_button, Blocks.oak_door, Blocks.skull));
        if (!interactables.contains(block)) {
            mc.theWorld.setBlockToAir(mc.objectMouseOver.getBlockPos());
        }
    }
}
