package me.axlerogue.horrifyinglanterns.network;

import me.axlerogue.horrifyinglanterns.api.LanternBaseItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ToggleLanternPacket {
    public ToggleLanternPacket() {
    }

    public static void encode(ToggleLanternPacket msg, FriendlyByteBuf buffer) {
    }

    public static ToggleLanternPacket decode(FriendlyByteBuf buffer) {
        return new ToggleLanternPacket();
    }

    public static void handle(ToggleLanternPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                ItemStack mainHand = player.getItemInHand(InteractionHand.MAIN_HAND);
                ItemStack offHand = player.getItemInHand(InteractionHand.OFF_HAND);

                ItemStack itemToToggle = ItemStack.EMPTY;
                if (mainHand.getItem() instanceof LanternBaseItem) {
                    itemToToggle = mainHand;
                } else if (offHand.getItem() instanceof LanternBaseItem) {
                    itemToToggle = offHand;
                }

                if (!itemToToggle.isEmpty()) {
                    LanternBaseItem.checkAndSetOwner(itemToToggle, player);
                    if (!LanternBaseItem.isOwner(itemToToggle, player)) {
                        player.displayClientMessage(Component.translatable("message.horrifyinglanterns.not_owner"), true);
                        return;
                    }
                    
                    if (player.getCooldowns().isOnCooldown(itemToToggle.getItem())) {
                        float percent = player.getCooldowns().getCooldownPercent(itemToToggle.getItem(), 0);
                        int seconds = (int) Math.ceil(percent * 15.0); // 15 seconds total cooldown
                        player.displayClientMessage(Component.translatable("message.horrifyinglanterns.cooldown", seconds), true);
                    } else {
                        LanternBaseItem.toggle(itemToToggle);
                        if (LanternBaseItem.isLit(itemToToggle)) {
                            player.displayClientMessage(Component.translatable("message.horrifyinglanterns.enabled"), true);
                            player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.FIRECHARGE_USE, SoundSource.PLAYERS, 1.0F, 1.0F);
                        } else {
                            player.displayClientMessage(Component.translatable("message.horrifyinglanterns.disabled"), true);
                            player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.PLAYERS, 1.0F, 1.0F);
                        }
                        player.getCooldowns().addCooldown(itemToToggle.getItem(), 300); // 15 seconds = 300 ticks
                    }
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
