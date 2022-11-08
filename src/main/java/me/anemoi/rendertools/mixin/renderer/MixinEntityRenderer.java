package me.anemoi.rendertools.mixin.renderer;

import me.anemoi.rendertools.config.MainConfig;
import me.anemoi.rendertools.config.modules.CameraConfig;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.util.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = {EntityRenderer.class}, priority = 20000)
public abstract class MixinEntityRenderer {
    @Shadow
    private float thirdPersonDistanceTemp;
    @Shadow
    private float thirdPersonDistance;

    @Redirect(method = "setupFog", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;isPotionActive(Lnet/minecraft/potion/Potion;)Z"))
    public boolean removeBlindness(EntityLivingBase instance, Potion potionIn) {
        //100% legit doesnt do anything at all but is needed to make the game not crash and fix bugs
        return false;
    }

    @Inject(method = "hurtCameraEffect", at = {@At(value = "HEAD")}, cancellable = true)
    public void hurtCam(float entitylivingbase, CallbackInfo ci) {
        if (CameraConfig.noHurtCam && CameraConfig.toggled) {
            ci.cancel();
        }
    }

    @Redirect(method = "orientCamera", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/EntityRenderer;thirdPersonDistanceTemp:F"))
    public float thirdPersonDistanceTemp(EntityRenderer instance) {
        return CameraConfig.toggled ? (float) CameraConfig.cameraDistance : this.thirdPersonDistanceTemp;
    }

    @Redirect(method = "orientCamera", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/EntityRenderer;thirdPersonDistance:F"))
    public float thirdPersonDistance(EntityRenderer instance) {
        return CameraConfig.toggled ? (float) CameraConfig.cameraDistance : this.thirdPersonDistance;
    }

    @Redirect(method = "orientCamera", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Vec3;distanceTo(Lnet/minecraft/util/Vec3;)D"))
    public double onCamera(Vec3 instance, Vec3 vec) {
        return CameraConfig.toggled && MainConfig.anemoi && CameraConfig.noClip ? CameraConfig.cameraDistance : instance.distanceTo(vec);
    }


}