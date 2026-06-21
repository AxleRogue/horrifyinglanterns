package me.axlerogue.horrifyinglanterns.api.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.level.Level;

/**
 * Base class for all horrifying lantern living entities.
 */
public abstract class BaseLivingEntity extends TamableAnimal {
    protected BaseLivingEntity(EntityType<? extends TamableAnimal> type, Level level) {
        super(type, level);
    }
}
