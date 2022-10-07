package me.anemoi.rendertools.mixin.entity;

import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Entity.class)
public abstract class EntitiyMixin {
    @Shadow
    public abstract boolean isEntityInsideOpaqueBlock();
}
