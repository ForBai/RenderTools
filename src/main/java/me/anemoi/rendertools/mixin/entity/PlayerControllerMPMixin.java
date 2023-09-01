package me.anemoi.rendertools.mixin.entity;


import me.anemoi.rendertools.events.AttackEvent;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerControllerMP.class)
public class PlayerControllerMPMixin {
    @Inject(method = "attackEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/PlayerControllerMP;syncCurrentPlayItem()V"), cancellable = true)
    private void attackEntity(EntityPlayer entityPlayer, Entity targetEntity, CallbackInfo callbackInfo) {
        if (targetEntity == null) return;
        AttackEvent event = new AttackEvent(targetEntity);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCanceled()) callbackInfo.cancel();
    }
}
