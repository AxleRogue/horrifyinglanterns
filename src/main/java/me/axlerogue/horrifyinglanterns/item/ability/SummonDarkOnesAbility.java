package me.axlerogue.horrifyinglanterns.item.ability;

import me.axlerogue.horrifyinglanterns.api.ability.BaseAbility;
import me.axlerogue.horrifyinglanterns.api.entity.DarkOnesEntity;
import me.axlerogue.horrifyinglanterns.item.client.ModEntities;
import me.axlerogue.horrifyinglanterns.api.LanternBaseItem;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.UUID;

public class SummonDarkOnesAbility extends BaseAbility {

    public SummonDarkOnesAbility() {
        super("summon_dark_ones", 1200);
    }

    @Override
    public void execute(ServerPlayer player, ItemStack stack) {
        if (!LanternBaseItem.isLit(stack)) {
            player.displayClientMessage(Component.translatable("message.horrifyinglanterns.ability_disabled"), true);
            return;
        }

        Level level = player.level();
        if (!(level instanceof ServerLevel serverLevel)) return;

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

        if (isOnCooldown(player, stack.getItem())) {
            player.displayClientMessage(Component.translatable("message.horrifyinglanterns.ability_on_cooldown", getDisplayName()), true);
            return;
        }

        serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, player.getX(), player.getY() + 1.0, player.getZ(), 50, 0.5, 0.5, 0.5, 0.1);
        serverLevel.sendParticles(ParticleTypes.LARGE_SMOKE, player.getX(), player.getY() + 1.0, player.getZ(), 20, 0.5, 0.5, 0.5, 0.05);

        HitResult hitResult = player.pick(16.0D, 0.0F, false);
        LivingEntity target = null;
        Vec3 hitVec = hitResult.getLocation();
        AABB searchBox = new AABB(hitVec.subtract(2, 2, 2), hitVec.add(2, 2, 2));
        List<LivingEntity> nearbyEntities = level.getEntitiesOfClass(LivingEntity.class, searchBox, e -> e != player && e.isAlive());
        
        if (!nearbyEntities.isEmpty()) {
            target = nearbyEntities.get(0);
        }

        ListTag darkOnesList = new ListTag();
        Component customName = Component.literal(player.getName().getString() + "'s Dark One");
        
        for (int i = 0; i < 4; i++) {
            DarkOnesEntity darkOne = ModEntities.DARK_ONES.get().create(serverLevel);
            if (darkOne != null) {
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
        
        startCooldown(player, stack.getItem());
    }
}
