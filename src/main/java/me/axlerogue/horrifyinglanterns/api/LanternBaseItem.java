/*
 * Horrifying Lanterns - A Minecraft mod that introduces cursed and powerful lanterns.
 * Copyright (C) 2026 AxleRogue
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package me.axlerogue.horrifyinglanterns.api;

import me.axlerogue.horrifyinglanterns.Config;
import me.axlerogue.horrifyinglanterns.api.ability.BaseAbility;
import me.axlerogue.horrifyinglanterns.api.ability.AbilityType;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.UUID;

/**
 * Base class for all horrifying lanterns. 
 * Addon creators should extend this class to create their own lanterns.
 */
public abstract class LanternBaseItem extends Item {
    protected int lightColor = 0xFFFFFF;
    protected final Map<AbilityType, BaseAbility> abilities = new HashMap<>();

    public LanternBaseItem(Item.Properties properties) {
        super(properties);
    }

    public BaseAbility getAbility(AbilityType type) {
        return abilities.get(type);
    }

    /**
     * Returns the color of the light emitted by this lantern in ARGB format.
     */
    public int getLightColor() {
        return this.lightColor;
    }

    /**
     * Checks if the lantern stack is currently lit.
     */
    public static boolean isLit(ItemStack stack) {
        return stack.getOrCreateTag().getBoolean("isLit");
    }

    /**
     * Toggles the lit state of the lantern stack.
     */
    public static void toggle(ItemStack stack) {
        stack.getOrCreateTag().putBoolean("isLit", !isLit(stack));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide) {
            checkAndSetOwner(stack, player);
            if (!isOwner(stack, player)) {
                player.displayClientMessage(Component.translatable("message.horrifyinglanterns.not_owner"), true);
                return InteractionResultHolder.fail(stack);
            }
        }
        return super.use(level, player, hand);
    }

    public static void checkAndSetOwner(ItemStack stack, Player player) {
        if (!stack.getOrCreateTag().contains("Owner")) {
            stack.getOrCreateTag().putUUID("Owner", player.getUUID());
            stack.getOrCreateTag().putString("OwnerName", player.getName().getString());
        }
    }

    public static boolean isOwner(ItemStack stack, Player player) {
        if (!stack.hasTag() || !stack.getTag().contains("Owner")) return true;
        return stack.getTag().getUUID("Owner").equals(player.getUUID());
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        if (stack.hasTag() && stack.getTag().contains("OwnerName")) {
            String ownerName = stack.getTag().getString("OwnerName");
            tooltip.add(Component.translatable("tooltip.horrifyinglanterns.tethered", ownerName).withStyle(ChatFormatting.DARK_PURPLE, ChatFormatting.ITALIC));
        }
        super.appendHoverText(stack, level, tooltip, flag);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        // Prevent the re-equip animation from playing every tick when LitTicks NBT updates
        if (oldStack.getItem() == newStack.getItem() && !slotChanged) {
            return false;
        }
        return super.shouldCauseReequipAnimation(oldStack, newStack, slotChanged);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, net.minecraft.world.entity.Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);
        if (!level.isClientSide) {
            if (isLit(stack)) {
                int litTicks = stack.getOrCreateTag().getInt("LitTicks");
                litTicks++;
                
                int maxTicks = Config.autoExtinguishMinutes * 1200; // Minutes to ticks (20 ticks/sec * 60 sec = 1200 ticks/min)
                
                if (litTicks >= maxTicks) {
                    stack.getOrCreateTag().putBoolean("isLit", false);
                    stack.getOrCreateTag().putInt("LitTicks", 0);
                    if (entity instanceof Player player && isOwner(stack, player)) {
                        String playerName = player.getName().getString();
                        player.sendSystemMessage(Component.literal(playerName + ": Your lantern has extinguished, reignite it quickly hurry now!").withStyle(ChatFormatting.RED));
                        level.playSound(null, player.getX(), player.getY(), player.getZ(), net.minecraft.sounds.SoundEvents.GENERIC_EXTINGUISH_FIRE, net.minecraft.sounds.SoundSource.PLAYERS, 1.0F, 1.0F);
                    }
                } else {
                    stack.getOrCreateTag().putInt("LitTicks", litTicks);
                }
            } else {
                if (stack.hasTag() && stack.getTag().contains("LitTicks")) {
                    stack.getTag().remove("LitTicks");
                }
            }
        }
    }
}
