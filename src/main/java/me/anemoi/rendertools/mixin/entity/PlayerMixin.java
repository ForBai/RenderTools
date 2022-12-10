package me.anemoi.rendertools.mixin.entity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = {EntityPlayer.class}, priority = 20000)
public abstract class PlayerMixin extends MixinEntityLivingBase {
    public PlayerMixin(World worldIn) {
        super(worldIn);
    }

    @Override
    public boolean isEntityInsideOpaqueBlock() {
        return false;
    }
}
