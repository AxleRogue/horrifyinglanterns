package me.axlerogue.horrifyinglanterns.item.items;

import me.axlerogue.horrifyinglanterns.entity.BlueLightningBolt;
import me.axlerogue.horrifyinglanterns.entity.DarkOnesEntity;
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

    public void performDarkBurst(ServerPlayer player, ItemStack stack) {
        if (!isLit(stack)) {
            player.displayClientMessage(Component.translatable("message.horrifyinglanterns.ability_disabled"), true);
            return;
        }

        Level level = player.level();
        if (!(level instanceof ServerLevel serverLevel)) return;

        // Check for existing Dark Ones
        CompoundTag tag = stack.getOrCreateTag();
        if (tag.contains("DarkOnes", Tag.TAG_LIST)) {
            ListTag list = tag.getList("DarkOnes", Tag.TAG_COMPOUND);
            boolean anyAlive = false;
            for (int i = 0; i < list.size(); i++) {
                CompoundTag entry = list.getCompound(i);
                UUID uuid = entry.getUUID("UUID");
                Entity entity = serverLevel.getEntity(uuid);
                if (entity != null && entity.isAlive() && entity.getType() == ModEntities.DARK_ONES.get()) {
                    anyAlive = true;
                    break;
                }
            }
            
            if (anyAlive) {
                player.displayClientMessage(Component.translatable("message.horrifyinglanterns.dark_ones_alive").withStyle(ChatFormatting.RED), true);
                return;
            }
        }

        if (player.getCooldowns().isOnCooldown(this)) {
            float percent = player.getCooldowns().getCooldownPercent(this, 0);
            int seconds = (int) Math.ceil(percent * 60.0);
            player.displayClientMessage(Component.translatable("message.horrifyinglanterns.ability_cooldown", seconds), true);
            return;
        }

        // Particle Burst
        serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, player.getX(), player.getY() + 1.0, player.getZ(), 50, 0.5, 0.5, 0.5, 0.1);
        serverLevel.sendParticles(ParticleTypes.LARGE_SMOKE, player.getX(), player.getY() + 1.0, player.getZ(), 20, 0.5, 0.5, 0.5, 0.05);

        // Find target (closest living entity within 16 blocks, looking at)
        HitResult hitResult = player.pick(16.0D, 0.0F, false);
        LivingEntity target = null;
        
        // Try to find a living entity in a small area around the hit location
        Vec3 hitVec = hitResult.getLocation();
        AABB searchBox = new AABB(hitVec.subtract(2, 2, 2), hitVec.add(2, 2, 2));
        List<LivingEntity> nearbyEntities = level.getEntitiesOfClass(LivingEntity.class, searchBox, e -> e != player && e.isAlive());
        
        if (!nearbyEntities.isEmpty()) {
            target = nearbyEntities.get(0);
        }

        // Summon 4 Dark Ones
        ListTag darkOnesList = new ListTag();
        Component customName = Component.literal(player.getName().getString() + "'s Dark One");
        
        for (int i = 0; i < 4; i++) {
            DarkOnesEntity darkOne = ModEntities.DARK_ONES.get().create(serverLevel);
            if (darkOne != null) {
                // Spawn in a small circle/offset around player
                double angle = i * (Math.PI / 2);
                double offsetX = Math.cos(angle) * 1.5;
                double offsetZ = Math.sin(angle) * 1.5;
                
                darkOne.moveTo(player.getX() + offsetX, player.getY() + 1.0, player.getZ() + offsetZ, player.getYRot(), player.getXRot());
                darkOne.tame(player);
                darkOne.setCustomName(customName);
                darkOne.setCustomNameVisible(true);
                serverLevel.addFreshEntity(darkOne);
                
                if (target != null) {
                    darkOne.setTarget(target);
                }

                CompoundTag entityTag = new CompoundTag();
                entityTag.putUUID("UUID", darkOne.getUUID());
                darkOnesList.add(entityTag);
            }
        }
        
        tag.put("DarkOnes", darkOnesList);

        if (target != null) {
            player.displayClientMessage(Component.translatable("message.horrifyinglanterns.dark_burst_target", target.getDisplayName()).withStyle(ChatFormatting.BLUE), true);
        } else {
            player.displayClientMessage(Component.translatable("message.horrifyinglanterns.dark_burst_summoned").withStyle(ChatFormatting.BLUE), true);
        }
        
        // 60 second cooldown (1200 ticks)
        player.getCooldowns().addCooldown(this, 1200);
    }

    public void performSkyLeech(ServerPlayer player, ItemStack stack) {
        if (!isLit(stack)) {
            player.displayClientMessage(Component.translatable("message.horrifyinglanterns.ability_disabled"), true);
            return;
        }

        if (player.getCooldowns().isOnCooldown(this)) {
            return;
        }

        // Raytrace to find target position (up to 32 blocks)
        HitResult hitResult = player.pick(32.0D, 0.0F, false);
        Vec3 targetPos;
        if (hitResult.getType() != HitResult.Type.MISS) {
            targetPos = hitResult.getLocation();
        } else {
            // If miss, just put it in front of the player
            targetPos = player.getEyePosition().add(player.getLookAngle().scale(32.0D));
        }

        BlueLightningBolt lightningBolt = ModEntities.BLUE_LIGHTNING_BOLT.get().create(player.level());
        if (lightningBolt != null) {
            lightningBolt.moveTo(targetPos);
            lightningBolt.setCause(player);
            player.level().addFreshEntity(lightningBolt);
        }

        // 2 second cooldown (40 ticks)
        player.getCooldowns().addCooldown(this, 40);
    }
}
