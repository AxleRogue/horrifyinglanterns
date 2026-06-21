package me.axlerogue.horrifyinglanterns.network;

import me.axlerogue.horrifyinglanterns.api.LanternBaseItem;
import me.axlerogue.horrifyinglanterns.api.ability.AbilityType;
import me.axlerogue.horrifyinglanterns.api.ability.BaseAbility;
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
                executeAbility(player, player.getMainHandItem(), msg.type);
                executeAbility(player, player.getOffhandItem(), msg.type);
            }
        });
        ctx.get().setPacketHandled(true);
    }

    private static void executeAbility(ServerPlayer player, ItemStack stack, AbilityType type) {
        if (stack.getItem() instanceof LanternBaseItem lantern) {
            BaseAbility ability = lantern.getAbility(type);
            if (ability != null) {
                ability.execute(player, stack);
                
                // Keep the specific messages for Sanguine Lantern as requested in previous sessions
                if (type == AbilityType.BURST && lantern instanceof me.axlerogue.horrifyinglanterns.item.items.SanguineMoonLanternItem) {
                    player.displayClientMessage(net.minecraft.network.chat.Component.translatable("chat.horrifyinglanterns.burst_used").withStyle(net.minecraft.ChatFormatting.DARK_RED), true);
                } else if (type == AbilityType.LEECH && lantern instanceof me.axlerogue.horrifyinglanterns.item.items.SanguineMoonLanternItem) {
                    player.displayClientMessage(net.minecraft.network.chat.Component.translatable("chat.horrifyinglanterns.leech_used").withStyle(net.minecraft.ChatFormatting.RED), true);
                }
            }
        }
    }
}
