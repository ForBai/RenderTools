package me.anemoi.rendertools.mixin.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntityPlayer.class)
public abstract class PlayerMixin extends MixinEntityLivingBase {
    @Override
    public boolean isEntityInsideOpaqueBlock() {
        return false;
    }
}
