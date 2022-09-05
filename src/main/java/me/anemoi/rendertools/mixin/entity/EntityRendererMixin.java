package me.anemoi.rendertools.mixin.entity;

import me.anemoi.rendertools.RenderTools;
import me.anemoi.rendertools.config.modules.CameraConfig;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.util.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={EntityRenderer.class})
public class EntityRendererMixin {
    @Shadow
    private float thirdPersonDistanceTemp;
    @Shadow
    private float thirdPersonDistance;

    @Inject(method={"hurtCameraEffect"}, at={@At(value="HEAD")}, cancellable=true)
    public void hurtCam(float entitylivingbase, CallbackInfo ci) {
        if (CameraConfig.noHurtCam && CameraConfig.toggled) {
            ci.cancel();
        }
    }

    @Redirect(method={"orientCamera"}, at=@At(value="FIELD", target="Lnet/minecraft/client/renderer/EntityRenderer;thirdPersonDistanceTemp:F"))
    public float thirdPersonDistanceTemp(EntityRenderer instance) {
        return CameraConfig.toggled ? (float)CameraConfig.cameraDistance : this.thirdPersonDistanceTemp;
    }

    @Redirect(method={"orientCamera"}, at=@At(value="FIELD", target="Lnet/minecraft/client/renderer/EntityRenderer;thirdPersonDistance:F"))
    public float thirdPersonDistance(EntityRenderer instance) {
        return CameraConfig.toggled ? (float)CameraConfig.cameraDistance : this.thirdPersonDistance;
    }

    @Redirect(method={"orientCamera"}, at=@At(value="INVOKE", target="Lnet/minecraft/util/Vec3;distanceTo(Lnet/minecraft/util/Vec3;)D"))
    public double onCamera(Vec3 instance, Vec3 vec) {
        return CameraConfig.toggled && CameraConfig.noClip ? CameraConfig.cameraDistance : instance.distanceTo(vec);
    }

}