package me.axlerogue.horrifyinglanterns.item.client;

import me.axlerogue.horrifyinglanterns.HorrifyingLanterns;
import me.axlerogue.horrifyinglanterns.api.entity.BlueLightningBolt;
import me.axlerogue.horrifyinglanterns.api.entity.DarkOnesEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, HorrifyingLanterns.MODID);

    public static final RegistryObject<EntityType<BlueLightningBolt>> BLUE_LIGHTNING_BOLT = ENTITIES.register("blue_lightning_bolt",
            () -> EntityType.Builder.of(BlueLightningBolt::new, MobCategory.MISC)
                    .sized(0.0F, 0.0F)
                    .clientTrackingRange(16)
                    .updateInterval(Integer.MAX_VALUE)
                    .build("blue_lightning_bolt"));

    public static final RegistryObject<EntityType<DarkOnesEntity>> DARK_ONES = ENTITIES.register("dark_ones",
            () -> EntityType.Builder.of(DarkOnesEntity::new, MobCategory.CREATURE)
                    .sized(0.6F, 1.8F)
                    .clientTrackingRange(10)
                    .build("dark_ones"));

    public static void register(IEventBus eventBus) {
        ENTITIES.register(eventBus);
    }
}
