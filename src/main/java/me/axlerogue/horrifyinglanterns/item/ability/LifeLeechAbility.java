package me.axlerogue.horrifyinglanterns.item.ability;

import me.axlerogue.horrifyinglanterns.api.ability.BaseAbility;
import me.axlerogue.horrifyinglanterns.api.LanternBaseItem;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.UUID;

public class LifeLeechAbility extends BaseAbility {
    private static final UUID HEALTH_MODIFIER_UUID = UUID.fromString("f4b162f4-500e-4367-936b-d82006733a7d");
    private static final String HEALTH_MODIFIER_NAME = "Sanguine Health Boost";

    public LifeLeechAbility() {
        super("life_leech", 100);
    }

    @Override
    public void execute(ServerPlayer player, ItemStack stack) {
        if (!LanternBaseItem.isLit(stack)) {
            player.displayClientMessage(Component.translatable("message.horrifyinglanterns.ability_disabled"), true);
            return;
        }

        if (isOnCooldown(player, stack.getItem())) {
            float percent = player.getCooldowns().getCooldownPercent(stack.getItem(), 0);
            int seconds = (int) Math.ceil(percent * (getCooldownTicks() / 20.0));
            player.displayClientMessage(Component.translatable("message.horrifyinglanterns.ability_cooldown", seconds), true);
            return;
        }

        Level level = player.level();
        AABB area = player.getBoundingBox().inflate(5.0);
        List<LivingEntity> targets = level.getEntitiesOfClass(LivingEntity.class, area, entity -> {
            if (entity == player) return false;
            if (entity instanceof Player) return false;
            if (!entity.canChangeDimensions()) return false;
            return true;
        });

        boolean anyHurt = false;
        for (LivingEntity target : targets) {
            float damageAmount = 1.0f;
            if (target.hurt(target.damageSources().magic(), damageAmount)) {
                anyHurt = true;
                AttributeInstance maxHealthAttr = player.getAttribute(Attributes.MAX_HEALTH);
                if (maxHealthAttr != null) {
                    double currentExtra = 0;
                    AttributeModifier oldModifier = maxHealthAttr.getModifier(HEALTH_MODIFIER_UUID);
                    if (oldModifier != null) {
                        currentExtra = oldModifier.getAmount();
                        maxHealthAttr.removeModifier(HEALTH_MODIFIER_UUID);
                    }

                    double newExtra = Math.min(currentExtra + 1.0, 30.0);
                    maxHealthAttr.addPermanentModifier(new AttributeModifier(HEALTH_MODIFIER_UUID, HEALTH_MODIFIER_NAME, newExtra, AttributeModifier.Operation.ADDITION));
                    player.heal(1.0f);

                    ((ServerLevel) level).sendParticles(DustParticleOptions.REDSTONE, target.getX(), target.getY() + 1.0, target.getZ(), 10, 0.2, 0.2, 0.2, 0);
                }
            }
        }

        if (anyHurt) {
            startCooldown(player, stack.getItem());
        }
    }
}
