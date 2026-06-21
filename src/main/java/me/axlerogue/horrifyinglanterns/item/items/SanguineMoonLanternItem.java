package me.axlerogue.horrifyinglanterns.item.items;

import me.axlerogue.horrifyinglanterns.api.ability.AbilityType;
import me.axlerogue.horrifyinglanterns.item.ability.LifeLeechAbility;
import me.axlerogue.horrifyinglanterns.item.ability.SanguineBurstAbility;
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
        this.abilities.put(AbilityType.BURST, new SanguineBurstAbility());
        this.abilities.put(AbilityType.LEECH, new LifeLeechAbility());
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
}
