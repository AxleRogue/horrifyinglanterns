package me.axlerogue.horrifyinglanterns.client;

import me.axlerogue.horrifyinglanterns.Config;
import me.axlerogue.horrifyinglanterns.HorrifyingLanterns;
import me.axlerogue.horrifyinglanterns.api.LanternBaseItem;
import me.axlerogue.horrifyinglanterns.api.ability.AbilityType;
import me.axlerogue.horrifyinglanterns.network.LanternAbilityPacket;
import me.axlerogue.horrifyinglanterns.network.ModPackets;
import me.axlerogue.horrifyinglanterns.network.ToggleLanternPacket;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = HorrifyingLanterns.MODID, value = Dist.CLIENT)
public class ClientEvents {
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            while (KeyHandler.TOGGLE_LANTERN.consumeClick()) {
                ModPackets.INSTANCE.sendToServer(new ToggleLanternPacket());
            }
            while (KeyHandler.LANTERN_BURST.consumeClick()) {
                ModPackets.INSTANCE.sendToServer(new LanternAbilityPacket(AbilityType.BURST));
            }
            while (KeyHandler.LANTERN_LEECH.consumeClick()) {
                ModPackets.INSTANCE.sendToServer(new LanternAbilityPacket(AbilityType.LEECH));
            }
        }
    }
}
