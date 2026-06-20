package me.axlerogue.horrifyinglanterns;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Forge's config APIs
@Mod.EventBusSubscriber(modid = HorrifyingLanterns.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.ConfigValue<Integer> LIGHT_LEVEL;
    public static final ForgeConfigSpec.ConfigValue<Double> LIGHT_RADIUS;

    static {
        BUILDER.push("Lantern Settings");
        LIGHT_LEVEL = BUILDER.comment("The brightness of the lantern light (0-15)").defineInRange("lightLevel", 15, 0, 15);
        LIGHT_RADIUS = BUILDER.comment("The radius of the dynamic light").defineInRange("lightRadius", 10.0, 1.0, 16.0);
        BUILDER.pop();
    }

    public static final ForgeConfigSpec SPEC = BUILDER.build();

    public static int lightLevel;
    public static double lightRadius;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        lightLevel = LIGHT_LEVEL.get();
        lightRadius = LIGHT_RADIUS.get();
    }
}
