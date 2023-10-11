package me.anemoi.rendertools.utils.other;

import me.anemoi.rendertools.config.modules.AnimationsConfig;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.potion.Potion;
import net.minecraft.util.MovingObjectPosition;

import static me.anemoi.rendertools.RenderTools.mc;

public class SwingHelper {
    public float previosSwingProg;
    public float swingProgress;
    public int swProgInteger;
    public boolean swingInProgress;

    //get the current swing progress i would suggest using this instead of the one in mc
    public float getSwingProgress(float partialTickTime) {
        float actualP = this.swingProgress - this.previosSwingProg;
        if (!swingInProgress) return mc.thePlayer.getSwingProgress(partialTickTime);
        if (actualP < 0.0F) actualP++;
        return this.previosSwingProg + actualP * partialTickTime;
    }

    //get the max swing progress for the current item
    public int getSwingAnimMax(EntityPlayerSP player) {
        if (player.isPotionActive(Potion.digSpeed)) {
            return 5 - player.getActivePotionEffect(Potion.digSpeed).getAmplifier();
        } else if (player.isPotionActive(Potion.digSlowdown)) {
            return 8 + player.getActivePotionEffect(Potion.digSlowdown).getAmplifier() * 2;
        } else {
            return 6;
        }
    }

    public void update() {
        if (mc.thePlayer == null) return;
        previosSwingProg = swingProgress;
        int mxSwingP = getSwingAnimMax(mc.thePlayer);

        //check if the player is swinging
        if (AnimationsConfig.special && mc.thePlayer.isBlocking() && mc.gameSettings.keyBindAttack.isKeyDown() && mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && (!this.swingInProgress || this.swProgInteger >= mxSwingP >> 1 || this.swProgInteger < 0)) {
            //set it to swinging
            swingInProgress = true;
            swProgInteger = -1;
        }

        //reset swing progress if there is not swing in progress
        if (!this.swingInProgress) this.swProgInteger = 0;
        else this.swProgInteger++;

        if (this.swProgInteger >= mxSwingP || !mc.thePlayer.isBlocking()) {
            //reset it and stop
            this.swProgInteger = 0;
            this.swingInProgress = false;
        }
        //update swing progress
        this.swingProgress = (float) this.swProgInteger / (float) mxSwingP;
    }
}
