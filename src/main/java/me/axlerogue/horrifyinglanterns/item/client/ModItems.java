package me.axlerogue.horrifyinglanterns.item.client;

import me.axlerogue.horrifyinglanterns.HorrifyingLanterns;
import me.axlerogue.horrifyinglanterns.item.items.DarkSkiesLanternItem;
import me.axlerogue.horrifyinglanterns.item.items.SanguineMoonLanternItem;
import me.axlerogue.horrifyinglanterns.item.items.VenomFangLanternItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, HorrifyingLanterns.MODID);

    public static final RegistryObject<Item> SANGUINE_MOON_LANTERN = ITEMS.register("sanguine_moon_lantern", () -> new SanguineMoonLanternItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> DARK_SKIES_LANTERN = ITEMS.register("dark_skies_lantern", () -> new DarkSkiesLanternItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> VENOM_FANG_LANTERN = ITEMS.register("venom_fang_lantern", () -> new VenomFangLanternItem(new Item.Properties().stacksTo(1)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
