package me.axlerogue.horrifyinglanterns.network;

import me.axlerogue.horrifyinglanterns.api.ability.AbilityType;
import me.axlerogue.horrifyinglanterns.item.items.DarkSkiesLanternItem;
import me.axlerogue.horrifyinglanterns.item.items.SanguineMoonLanternItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class LanternAbilityPacket {

    private final AbilityType type;

    public LanternAbilityPacket(AbilityType type) {
        this.type = type;
    }

    public LanternAbilityPacket(FriendlyByteBuf buf) {
        this.type = buf.readEnum(AbilityType.class);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeEnum(this.type);
    }

    public static LanternAbilityPacket decode(FriendlyByteBuf buf) {
        return new LanternAbilityPacket(buf);
    }

    public static void handle(LanternAbilityPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                ItemStack mainHand = player.getMainHandItem();
                ItemStack offHand = player.getOffhandItem();
                
                if (mainHand.getItem() instanceof SanguineMoonLanternItem sanguineItem) {
                    if (player.getCooldowns().isOnCooldown(sanguineItem)) {
                        player.displayClientMessage(net.minecraft.network.chat.Component.translatable("chat.horrifyinglanterns.cooldown").withStyle(net.minecraft.ChatFormatting.RED), true);
                        return;
                    }
                    if (msg.type == AbilityType.BURST) {
                        sanguineItem.performSanguineBurst(player, mainHand);
                        player.getCooldowns().addCooldown(sanguineItem, 200); // 10 seconds
                        player.displayClientMessage(net.minecraft.network.chat.Component.translatable("chat.horrifyinglanterns.burst_used").withStyle(net.minecraft.ChatFormatting.DARK_RED), true);
                    } else {
                        sanguineItem.performLifeLeech(player, mainHand);
                        player.getCooldowns().addCooldown(sanguineItem, 100); // 5 seconds
                        player.displayClientMessage(net.minecraft.network.chat.Component.translatable("chat.horrifyinglanterns.leech_used").withStyle(net.minecraft.ChatFormatting.RED), true);
                    }
                } else if (mainHand.getItem() instanceof DarkSkiesLanternItem darkSkiesItem) {
                    if (player.getCooldowns().isOnCooldown(darkSkiesItem)) {
                        player.displayClientMessage(net.minecraft.network.chat.Component.translatable("chat.horrifyinglanterns.cooldown").withStyle(net.minecraft.ChatFormatting.RED), true);
                        return;
                    }
                    if (msg.type == AbilityType.BURST) {
                        darkSkiesItem.performDarkBurst(player, mainHand);
                        player.getCooldowns().addCooldown(darkSkiesItem, 200);
                        player.displayClientMessage(net.minecraft.network.chat.Component.translatable("chat.horrifyinglanterns.dark_burst_used").withStyle(net.minecraft.ChatFormatting.BLUE), true);
                    } else {
                        darkSkiesItem.performSkyLeech(player, mainHand);
                        player.getCooldowns().addCooldown(darkSkiesItem, 100);
                        player.displayClientMessage(net.minecraft.network.chat.Component.translatable("chat.horrifyinglanterns.sky_leech_used").withStyle(net.minecraft.ChatFormatting.AQUA), true);
                    }
                } else if (offHand.getItem() instanceof SanguineMoonLanternItem sanguineItem) {
                    if (player.getCooldowns().isOnCooldown(sanguineItem)) {
                        player.displayClientMessage(net.minecraft.network.chat.Component.translatable("chat.horrifyinglanterns.cooldown").withStyle(net.minecraft.ChatFormatting.RED), true);
                        return;
                    }
                    if (msg.type == AbilityType.BURST) {
                        sanguineItem.performSanguineBurst(player, offHand);
                        player.getCooldowns().addCooldown(sanguineItem, 200);
                        player.displayClientMessage(net.minecraft.network.chat.Component.translatable("chat.horrifyinglanterns.burst_used").withStyle(net.minecraft.ChatFormatting.DARK_RED), true);
                    } else {
                        sanguineItem.performLifeLeech(player, offHand);
                        player.getCooldowns().addCooldown(sanguineItem, 100);
                        player.displayClientMessage(net.minecraft.network.chat.Component.translatable("chat.horrifyinglanterns.leech_used").withStyle(net.minecraft.ChatFormatting.RED), true);
                    }
                } else if (offHand.getItem() instanceof DarkSkiesLanternItem darkSkiesItem) {
                    if (player.getCooldowns().isOnCooldown(darkSkiesItem)) {
                        player.displayClientMessage(net.minecraft.network.chat.Component.translatable("chat.horrifyinglanterns.cooldown").withStyle(net.minecraft.ChatFormatting.RED), true);
                        return;
                    }
                    if (msg.type == AbilityType.BURST) {
                        darkSkiesItem.performDarkBurst(player, offHand);
                        player.getCooldowns().addCooldown(darkSkiesItem, 200);
                        player.displayClientMessage(net.minecraft.network.chat.Component.translatable("chat.horrifyinglanterns.dark_burst_used").withStyle(net.minecraft.ChatFormatting.BLUE), true);
                    } else {
                        darkSkiesItem.performSkyLeech(player, offHand);
                        player.getCooldowns().addCooldown(darkSkiesItem, 100);
                        player.displayClientMessage(net.minecraft.network.chat.Component.translatable("chat.horrifyinglanterns.sky_leech_used").withStyle(net.minecraft.ChatFormatting.AQUA), true);
                    }
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
