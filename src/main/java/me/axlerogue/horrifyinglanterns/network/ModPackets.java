package me.axlerogue.horrifyinglanterns.network;

import me.axlerogue.horrifyinglanterns.HorrifyingLanterns;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModPackets {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(HorrifyingLanterns.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void register() {
        int id = 0;
        INSTANCE.registerMessage(id++, ToggleLanternPacket.class, ToggleLanternPacket::encode, ToggleLanternPacket::decode, ToggleLanternPacket::handle);
        INSTANCE.registerMessage(id++, LanternAbilityPacket.class, LanternAbilityPacket::encode, LanternAbilityPacket::decode, LanternAbilityPacket::handle);
    }
}
