package me.axlerogue.horrifyinglanterns;

import com.mojang.logging.LogUtils;
import me.axlerogue.horrifyinglanterns.client.renderer.BlueLightningBoltRenderer;
import me.axlerogue.horrifyinglanterns.client.renderer.DarkOnesRenderer;
import me.axlerogue.horrifyinglanterns.client.screen.ModConfigScreen;
import me.axlerogue.horrifyinglanterns.entity.DarkOnesEntity;
import me.axlerogue.horrifyinglanterns.item.client.ModEntities;
import me.axlerogue.horrifyinglanterns.item.client.ModCreativeModeTabs;
import me.axlerogue.horrifyinglanterns.item.client.ModItems;
import me.axlerogue.horrifyinglanterns.network.ModPackets;
import me.axlerogue.horrifyinglanterns.api.LanternBaseItem;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(HorrifyingLanterns.MODID)
public class HorrifyingLanterns {

    // Define mod id in a common place for everything to reference
    public static final String MODID = "horrifyinglanterns";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public HorrifyingLanterns() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModCreativeModeTabs.register(modEventBus);

        ModItems.register(modEventBus);
        ModEntities.register(modEventBus);

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::onEntityAttributeCreation);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);


        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory((mc, lastScreen) -> new ModConfigScreen(lastScreen)));
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(ModPackets::register);

    }

    @SubscribeEvent
    public void onEntityAttributeCreation(EntityAttributeCreationEvent event) {
        event.put(ModEntities.DARK_ONES.get(), DarkOnesEntity.createAttributes().build());
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(() -> {
                ItemProperties.register(ModItems.SANGUINE_MOON_LANTERN.get(),
                        new ResourceLocation(MODID, "lit"),
                        (stack, level, entity, seed) -> LanternBaseItem.isLit(stack) ? 1.0F : 0.0F);
                ItemProperties.register(ModItems.DARK_SKIES_LANTERN.get(),
                        new ResourceLocation(MODID, "lit"),
                        (stack, level, entity, seed) -> LanternBaseItem.isLit(stack) ? 1.0F : 0.0F);
            });
        }

        @SubscribeEvent
        public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(ModEntities.BLUE_LIGHTNING_BOLT.get(), BlueLightningBoltRenderer::new);
            event.registerEntityRenderer(ModEntities.DARK_ONES.get(), DarkOnesRenderer::new);
        }
    }
}
