package me.anemoi.rendertools.mixin.entity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = {net.minecraft.client.renderer.EntityRenderer.class}, priority = 20000)
public interface IEntityRenderer {
    @Invoker(value = "setupCameraTransform")
    void iSetupCameraTransform(float partialTicks, int pass);
}
