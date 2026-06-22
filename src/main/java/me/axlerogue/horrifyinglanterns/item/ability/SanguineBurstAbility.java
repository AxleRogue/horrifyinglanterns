package me.axlerogue.horrifyinglanterns.item.ability;

import me.axlerogue.horrifyinglanterns.api.ability.BaseAbility;
import me.axlerogue.horrifyinglanterns.api.handler.SanguineEffectHandler;
import me.axlerogue.horrifyinglanterns.api.LanternBaseItem;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector3f;

public class SanguineBurstAbility extends BaseAbility {

    public SanguineBurstAbility() {
        super("sanguine_burst", 1200);
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

        triggerSanguineEffect(player);
        startCooldown(player, stack.getItem());
    }

    private void triggerSanguineEffect(ServerPlayer player) {
        ServerLevel level = player.serverLevel();

        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.WITHER_SPAWN, SoundSource.PLAYERS, 1.0F, 1.0F);

        for (int i = 0; i < 360; i += 10) {
            double rad = Math.toRadians(i);
            double x = Math.cos(rad) * 1.5;
            double z = Math.sin(rad) * 1.5;
            level.sendParticles(new DustParticleOptions(new Vector3f(1.0F, 0.0F, 0.0F), 1.5F),
                    player.getX() + x, player.getY() + 1.0, player.getZ() + z, 1, 0, 0, 0, 0);
        }

        for (int i = 0; i < 8; i++) {
            Bat bat = EntityType.BAT.create(level);
            if (bat != null) {
                double angle = i * (Math.PI * 2 / 8);
                double x = player.getX() + Math.cos(angle) * 2;
                double z = player.getZ() + Math.sin(angle) * 2;
                bat.moveTo(x, player.getY() + 1.5, z, level.getRandom().nextFloat() * 360, 0);
                level.addFreshEntity(bat);
                SanguineEffectHandler.scheduleRemoval(bat, 600);
            }
        }

        player.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 600, 0, false, false, true));
    }
}
