package me.axlerogue.horrifyinglanterns.api.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.Level;

/**
 * Base class for all horrifying lantern special entities.
 * Note: Extends LightningBolt as the current implementation uses it as a base.
 */
public abstract class BaseEntity extends LightningBolt {
    protected BaseEntity(EntityType<? extends LightningBolt> type, Level level) {
        super(type, level);
    }
}
