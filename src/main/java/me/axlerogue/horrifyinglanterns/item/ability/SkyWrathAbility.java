package me.axlerogue.horrifyinglanterns.item.ability;

import me.axlerogue.horrifyinglanterns.api.ability.BaseAbility;
import me.axlerogue.horrifyinglanterns.api.entity.BlueLightningBolt;
import me.axlerogue.horrifyinglanterns.item.client.ModEntities;
import me.axlerogue.horrifyinglanterns.api.LanternBaseItem;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class SkyWrathAbility extends BaseAbility {

    public SkyWrathAbility() {
        super("sky_wrath", 40);
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

        HitResult hitResult = player.pick(32.0D, 0.0F, false);
        Vec3 targetPos;
        if (hitResult.getType() != HitResult.Type.MISS) {
            targetPos = hitResult.getLocation();
        } else {
            targetPos = player.getEyePosition().add(player.getLookAngle().scale(32.0D));
        }

        BlueLightningBolt lightningBolt = ModEntities.BLUE_LIGHTNING_BOLT.get().create(player.level());
        if (lightningBolt != null) {
            lightningBolt.moveTo(targetPos);
            lightningBolt.setCause(player);
            player.level().addFreshEntity(lightningBolt);
        }

        startCooldown(player, stack.getItem());
    }
}
