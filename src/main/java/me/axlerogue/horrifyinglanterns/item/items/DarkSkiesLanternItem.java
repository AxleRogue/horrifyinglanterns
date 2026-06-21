package me.axlerogue.horrifyinglanterns.item.items;

import me.axlerogue.horrifyinglanterns.api.ability.AbilityType;
import me.axlerogue.horrifyinglanterns.item.ability.SkyWrathAbility;
import me.axlerogue.horrifyinglanterns.item.ability.SummonDarkOnesAbility;
import me.axlerogue.horrifyinglanterns.api.entity.BlueLightningBolt;
import me.axlerogue.horrifyinglanterns.api.entity.DarkOnesEntity;
import me.axlerogue.horrifyinglanterns.item.client.ModEntities;
import me.axlerogue.horrifyinglanterns.api.LanternBaseItem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DarkSkiesLanternItem extends LanternBaseItem {

    public DarkSkiesLanternItem(Properties properties) {
        super(properties);
        this.lightColor = 0x000033; // Dark blue
        this.abilities.put(AbilityType.SUMMON, new SummonDarkOnesAbility());
        this.abilities.put(AbilityType.WRATH, new SkyWrathAbility());
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        if (Screen.hasShiftDown()) {
            tooltip.add(Component.translatable("tooltip.horrifyinglanterns.dark_skies_info_1").withStyle(ChatFormatting.BLUE));
            tooltip.add(Component.translatable("tooltip.horrifyinglanterns.dark_skies_info_2").withStyle(ChatFormatting.BLUE));
            tooltip.add(Component.translatable("tooltip.horrifyinglanterns.dark_skies_info_3").withStyle(ChatFormatting.BLUE));
        } else {
            tooltip.add(Component.translatable("tooltip.horrifyinglanterns.press_shift").withStyle(ChatFormatting.GREEN));
        }
        super.appendHoverText(stack, level, tooltip, flag);
    }
}
