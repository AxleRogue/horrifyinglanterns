package me.axlerogue.horrifyinglanterns.api.entity;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * Base class for all horrifying lantern living entities.
 */
public abstract class BaseLivingEntity extends TamableAnimal {
    protected BaseLivingEntity(EntityType<? extends TamableAnimal> type, Level level) {
        super(type, level);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        }
        Entity attacker = source.getEntity();
        if (attacker != null && this.isAlliedTo(attacker)) {
            return false;
        }
        return super.hurt(source, amount);
    }

    @Override
    public boolean isAlliedTo(Entity entity) {
        if (super.isAlliedTo(entity)) {
            return true;
        }
        
        // Allied to owner
        if (entity == this.getOwner()) {
            return true;
        }

        // Allied to other entities with the same owner
        if (entity instanceof TamableAnimal tamable) {
            return this.getOwnerUUID() != null && this.getOwnerUUID().equals(tamable.getOwnerUUID());
        }

        return false;
    }
}
