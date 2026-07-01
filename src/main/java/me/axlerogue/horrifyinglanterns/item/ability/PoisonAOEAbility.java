package me.axlerogue.horrifyinglanterns.item.ability;

import me.axlerogue.horrifyinglanterns.api.LanternBaseItem;
import me.axlerogue.horrifyinglanterns.api.ability.BaseAbility;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class PoisonAOEAbility extends BaseAbility {

    public PoisonAOEAbility() {
        super("poison_aoe", 600); // 30 seconds cooldown
    }

    @Override
    public void execute(ServerPlayer player, ItemStack stack) {
        if (!LanternBaseItem.isLit(stack)) {
            player.displayClientMessage(Component.translatable("message.horrifyinglanterns.ability_disabled"), true);
            return;
        }

        if (isOnCooldown(player, stack.getItem())) {
            player.displayClientMessage(Component.translatable("message.horrifyinglanterns.ability_on_cooldown", getDisplayName()), true);
            return;
        }

        Level level = player.level();
        AABB area = player.getBoundingBox().inflate(8.0);
        List<LivingEntity> targets = level.getEntitiesOfClass(LivingEntity.class, area, entity -> {
            if (entity == player) return false;
            if (entity instanceof Player) return false;
            return true;
        });

        boolean anyPoisoned = false;
        for (LivingEntity target : targets) {
            target.addEffect(new MobEffectInstance(MobEffects.POISON, 200, 1)); // 10 seconds of Poison II
            anyPoisoned = true;
        }

        if (anyPoisoned) {
            startCooldown(player, stack.getItem());
        }
    }
}
