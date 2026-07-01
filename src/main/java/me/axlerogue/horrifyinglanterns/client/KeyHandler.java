package me.axlerogue.horrifyinglanterns.client;

import me.axlerogue.horrifyinglanterns.HorrifyingLanterns;
import me.axlerogue.horrifyinglanterns.api.client.BaseKeyMapping;
import me.axlerogue.horrifyinglanterns.api.client.LanternKeyMappings;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = HorrifyingLanterns.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class KeyHandler {
    public static final String KEY_CATEGORY_LANTERNS = LanternKeyMappings.KEY_CATEGORY_LANTERNS;
    public static final BaseKeyMapping TOGGLE_LANTERN = LanternKeyMappings.TOGGLE_LANTERN;
    public static final BaseKeyMapping LANTERN_BURST = LanternKeyMappings.LANTERN_BURST;
    public static final BaseKeyMapping LANTERN_LEECH = LanternKeyMappings.LANTERN_LEECH;
    public static final BaseKeyMapping LANTERN_SUMMON = LanternKeyMappings.LANTERN_SUMMON;
    public static final BaseKeyMapping LANTERN_WRATH = LanternKeyMappings.LANTERN_WRATH;
    public static final BaseKeyMapping LANTERN_POISON_AOE = LanternKeyMappings.LANTERN_POISON_AOE;
    public static final BaseKeyMapping LANTERN_PACIFY_SPIDERS = LanternKeyMappings.LANTERN_PACIFY_SPIDERS;

    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event) {
        event.register(TOGGLE_LANTERN);
        event.register(LANTERN_BURST);
        event.register(LANTERN_LEECH);
        event.register(LANTERN_SUMMON);
        event.register(LANTERN_WRATH);
        event.register(LANTERN_POISON_AOE);
        event.register(LANTERN_PACIFY_SPIDERS);
    }
}
