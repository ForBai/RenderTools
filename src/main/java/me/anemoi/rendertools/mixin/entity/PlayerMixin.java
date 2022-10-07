package me.anemoi.rendertools.mixin.entity;

import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EntityPlayer.class)
public abstract class PlayerMixin extends MixinEntityLivingBase {
    @Override
    public boolean isEntityInsideOpaqueBlock() {
        return false;
    }
}
