package me.axlerogue.horrifyinglanterns.client;

import me.axlerogue.horrifyinglanterns.HorrifyingLanterns;
import me.axlerogue.horrifyinglanterns.api.client.LanternKeyMappings;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = HorrifyingLanterns.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class KeyHandler {
    public static final String KEY_CATEGORY_LANTERNS = LanternKeyMappings.KEY_CATEGORY_LANTERNS;
    public static final net.minecraft.client.KeyMapping TOGGLE_LANTERN = LanternKeyMappings.TOGGLE_LANTERN;
    public static final net.minecraft.client.KeyMapping LANTERN_BURST = LanternKeyMappings.LANTERN_BURST;
    public static final net.minecraft.client.KeyMapping LANTERN_LEECH = LanternKeyMappings.LANTERN_LEECH;

    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event) {
        event.register(TOGGLE_LANTERN);
        event.register(LANTERN_BURST);
        event.register(LANTERN_LEECH);
    }
}
