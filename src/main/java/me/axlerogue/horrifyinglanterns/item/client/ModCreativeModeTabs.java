package me.axlerogue.horrifyinglanterns.item.client;

import me.axlerogue.horrifyinglanterns.HorrifyingLanterns;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, HorrifyingLanterns.MODID);

    public static final RegistryObject<CreativeModeTab> HORRIFYING_LANTERNS_TAB = CREATIVE_MODE_TABS.register("horrifying_lanterns",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModItems.SANGUINE_MOON_LANTERN.get()))
                    .title(Component.translatable("itemgroup.horrifyinglanterns.horrifying_lanterns").withStyle(ChatFormatting.RED))
                    .withLabelColor(0xFF0000) // Red color for the title when active
                    .withSlotColor(0x80FF0000) // Red tint for slots (ARGB)
                    .withBackgroundLocation(new ResourceLocation(HorrifyingLanterns.MODID, "textures/gui/container/creative_inventory/tab_horrifying_lanterns.png"))
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.SANGUINE_MOON_LANTERN.get());
                        output.accept(ModItems.DARK_SKIES_LANTERN.get());
                        output.accept(ModItems.VENOM_FANG_LANTERN.get());
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
