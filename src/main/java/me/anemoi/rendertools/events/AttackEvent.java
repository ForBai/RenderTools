package me.anemoi.rendertools.events;

import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.eventhandler.Event;

public class AttackEvent extends Event {
    private final Entity target;

    public AttackEvent(Entity target) {
        this.target = target;
    }

    public Entity getTarget() {
        return target;
    }
}
