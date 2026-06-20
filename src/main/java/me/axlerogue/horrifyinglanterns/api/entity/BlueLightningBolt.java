package me.axlerogue.horrifyinglanterns.api.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class BlueLightningBolt extends LightningBolt {
    private int life;
    private int flashes;

    public BlueLightningBolt(EntityType<? extends BlueLightningBolt> type, Level level) {
        super(type, level);
        this.life = 2;
        this.flashes = this.random.nextInt(3) + 1;
        this.setDamage(2.0F); // Low damage
    }

    @Override
    public void tick() {
        // Re-implementation of Entity.tick()
        if (!this.level().isClientSide) {
            this.setSharedFlag(6, this.isCurrentlyGlowing());
        }
        this.baseTick();
        
        // Custom logic based on LightningBolt.tick()
        if (this.life == 2) {
            if (this.level().isClientSide()) {
                this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.WEATHER, 10000.0F, 0.8F + this.random.nextFloat() * 0.2F, false);
                this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.LIGHTNING_BOLT_IMPACT, SoundSource.WEATHER, 2.0F, 0.5F + this.random.nextFloat() * 0.2F, false);
            }
        }

        --this.life;
        if (this.life < 0) {
            if (this.flashes == 0) {
                this.discard();
            } else if (this.life < -this.random.nextInt(10)) {
                --this.flashes;
                this.life = 1;
                this.seed = this.random.nextLong();
                // NO SPAWN FIRE
            }
        }

        if (this.life >= 0) {
            if (!(this.level() instanceof ServerLevel serverLevel)) {
                this.level().setSkyFlashTime(2);
            } else {
                // Strike entities
                List<Entity> list1 = this.level().getEntities(this, new AABB(this.getX() - 3.0D, this.getY() - 3.0D, this.getZ() - 3.0D, this.getX() + 3.0D, this.getY() + 6.0D + 3.0D, this.getZ() + 3.0D), Entity::isAlive);

                for (Entity entity : list1) {
                    // Strike entities but bypass thunderHit if it's a LivingEntity to avoid supercharging/transforming
                    if (entity instanceof LivingEntity) {
                        // Don't damage the lantern holder
                        if (entity != this.getCause()) {
                            entity.hurt(this.damageSources().lightningBolt(), this.getDamage());
                            
                            // Healing logic: 2 hearts (4.0F) from struck entity
                            if (this.getCause() != null) {
                                this.getCause().heal(4.0F);
                            }
                        }
                    } else {
                        entity.thunderHit(serverLevel, this);
                    }
                }
            }
        }
    }
}
