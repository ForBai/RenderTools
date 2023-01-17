package me.anemoi.rendertools.mixin.renderer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = {net.minecraft.client.renderer.entity.RenderManager.class},priority = 20000)
public interface IRenderManager {
    @Accessor(value = "renderPosX")
    double getRenderPosX();

    @Accessor(value = "renderPosY")
    double getRenderPosY();

    @Accessor(value = "renderPosZ")
    double getRenderPosZ();
}
