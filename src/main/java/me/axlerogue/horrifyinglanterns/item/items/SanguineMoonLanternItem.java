package me.axlerogue.horrifyinglanterns.item.items;

import me.axlerogue.horrifyinglanterns.api.handler.SanguineEffectHandler;
import me.axlerogue.horrifyinglanterns.api.LanternBaseItem;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.ChatFormatting;
import net.minecraft.client.GuiMessage;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.UUID;

public class SanguineMoonLanternItem extends LanternBaseItem {
    private static final UUID HEALTH_MODIFIER_UUID = UUID.fromString("f4b162f4-500e-4367-936b-d82006733a7d");
    private static final String HEALTH_MODIFIER_NAME = "Sanguine Health Boost";

    public SanguineMoonLanternItem(Properties properties) {
        super(properties);
        this.lightColor = 0xFF0000; // Set to red
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        return InteractionResult.PASS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        return InteractionResultHolder.pass(player.getItemInHand(hand));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        if (Screen.hasShiftDown()) {
            tooltip.add(Component.translatable("tooltip.horrifyinglanterns.info_1").withStyle(ChatFormatting.RED));
            tooltip.add(Component.translatable("tooltip.horrifyinglanterns.info_2").withStyle(ChatFormatting.RED));
            tooltip.add(Component.translatable("tooltip.horrifyinglanterns.info_3").withStyle(ChatFormatting.RED));
        } else {
            tooltip.add(Component.translatable("tooltip.horrifyinglanterns.press_shift").withStyle(ChatFormatting.GREEN));
        }
        super.appendHoverText(stack, level, tooltip, flag);
    }

    public void performLifeLeech(ServerPlayer player, ItemStack stack) {
        if (!isLit(stack)) {
            player.displayClientMessage(Component.translatable("message.horrifyinglanterns.ability_disabled"), true);
            return;
        }

        if (player.getCooldowns().isOnCooldown(this)) {
            return;
        }

        Level level = player.level();
        AABB area = player.getBoundingBox().inflate(5.0);
        List<LivingEntity> targets = level.getEntitiesOfClass(LivingEntity.class, area, entity -> {
            if (entity == player) return false;
            if (entity instanceof Player) return false;
            if (!entity.canChangeDimensions()) return false; // Basic boss check
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

                    double newExtra = currentExtra + 1.0;
                    if (newExtra > 30.0) {
                        newExtra = 30.0;
                    }

                    maxHealthAttr.addPermanentModifier(new AttributeModifier(HEALTH_MODIFIER_UUID, HEALTH_MODIFIER_NAME, newExtra, AttributeModifier.Operation.ADDITION));
                    player.heal(1.0f);

                    // Particles on target
                    ((ServerLevel) level).sendParticles(DustParticleOptions.REDSTONE, target.getX(), target.getY() + 1.0, target.getZ(), 10, 0.2, 0.2, 0.2, 0);
                }
            }
        }

        if (anyHurt) {
            // 2 second cooldown (40 ticks) for life steal nerf
            player.getCooldowns().addCooldown(this, 40);
        }
    }

    public void performSanguineBurst(ServerPlayer player, ItemStack stack) {
        if (!isLit(stack)) {
            player.displayClientMessage(Component.translatable("message.horrifyinglanterns.ability_disabled"), true);
            return;
        }

        if (player.getCooldowns().isOnCooldown(this)) {
            float percent = player.getCooldowns().getCooldownPercent(this, 0);
            int seconds = (int) Math.ceil(percent * 60.0);
            player.displayClientMessage(Component.translatable("message.horrifyinglanterns.ability_cooldown", seconds), true);
            return;
        }

        triggerSanguineEffect(player);
        // 60 second cooldown (1200 ticks)
        player.getCooldowns().addCooldown(this, 1200);
    }

    private void triggerSanguineEffect(ServerPlayer player) {
        ServerLevel level = player.serverLevel();

        // Play Wither sound (spawn sound)
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.WITHER_SPAWN, SoundSource.PLAYERS, 1.0F, 1.0F);

        // 1. Red dust particles twirling around the player
        for (int i = 0; i < 360; i += 10) {
            double rad = Math.toRadians(i);
            double x = Math.cos(rad) * 1.5;
            double z = Math.sin(rad) * 1.5;
            level.sendParticles(new DustParticleOptions(new Vector3f(1.0F, 0.0F, 0.0F), 1.5F),
                    player.getX() + x, player.getY() + 1.0, player.getZ() + z, 1, 0, 0, 0, 0);
        }

        // 2. Spawn 8 bats around the player
        for (int i = 0; i < 8; i++) {
            Bat bat = EntityType.BAT.create(level);
            if (bat != null) {
                double angle = i * (Math.PI * 2 / 8);
                double x = player.getX() + Math.cos(angle) * 2;
                double z = player.getZ() + Math.sin(angle) * 2;
                bat.moveTo(x, player.getY() + 1.5, z, level.getRandom().nextFloat() * 360, 0);
                level.addFreshEntity(bat);

                // 4. Bats disappear after 30 seconds (600 ticks)
                SanguineEffectHandler.scheduleRemoval(bat, 600);
            }
        }

        // 3. Player goes invisible for 30 seconds
        player.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 600, 0, false, false, true));
    }
}
