package me.anemoi.rendertools.mixin.entity;

import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = {Entity.class},priority = 20000)
public abstract class EntitiyMixin {
    @Shadow
    public abstract boolean isEntityInsideOpaqueBlock();
}
