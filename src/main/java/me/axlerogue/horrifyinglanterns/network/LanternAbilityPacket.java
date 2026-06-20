package me.axlerogue.horrifyinglanterns.network;

import me.axlerogue.horrifyinglanterns.item.items.DarkSkiesLanternItem;
import me.axlerogue.horrifyinglanterns.item.items.SanguineMoonLanternItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class LanternAbilityPacket {
    public enum AbilityType {
        BURST,
        LEECH
    }

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
                    if (msg.type == AbilityType.BURST) {
                        sanguineItem.performSanguineBurst(player, mainHand);
                    } else {
                        sanguineItem.performLifeLeech(player, mainHand);
                    }
                } else if (mainHand.getItem() instanceof DarkSkiesLanternItem darkSkiesItem) {
                    if (msg.type == AbilityType.BURST) {
                        darkSkiesItem.performDarkBurst(player, mainHand);
                    } else {
                        darkSkiesItem.performSkyLeech(player, mainHand);
                    }
                } else if (offHand.getItem() instanceof SanguineMoonLanternItem sanguineItem) {
                    if (msg.type == AbilityType.BURST) {
                        sanguineItem.performSanguineBurst(player, offHand);
                    } else {
                        sanguineItem.performLifeLeech(player, offHand);
                    }
                } else if (offHand.getItem() instanceof DarkSkiesLanternItem darkSkiesItem) {
                    if (msg.type == AbilityType.BURST) {
                        darkSkiesItem.performDarkBurst(player, offHand);
                    } else {
                        darkSkiesItem.performSkyLeech(player, offHand);
                    }
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
