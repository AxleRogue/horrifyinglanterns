package me.axlerogue.horrifyinglanterns.api.handler;

import me.axlerogue.horrifyinglanterns.HorrifyingLanterns;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = HorrifyingLanterns.MODID)
public class SanguineEffectHandler {
    private static final List<DelayedRemoval> PENDING_REMOVALS = new ArrayList<>();
    private static final UUID HEALTH_MODIFIER_UUID = UUID.fromString("f4b162f4-500e-4367-936b-d82006733a7d");

    public static void scheduleRemoval(Entity entity, int ticks) {
        PENDING_REMOVALS.add(new DelayedRemoval(entity, ticks));
    }

    @SubscribeEvent
    public static void onPlayerHurt(LivingHurtEvent event) {
        if (event.getEntity() instanceof Player player && !player.level().isClientSide) {
            AttributeInstance maxHealthAttr = player.getAttribute(Attributes.MAX_HEALTH);
            if (maxHealthAttr != null) {
                AttributeModifier modifier = maxHealthAttr.getModifier(HEALTH_MODIFIER_UUID);
                if (modifier != null) {
                    double extraHealth = modifier.getAmount();
                    float currentHealth = player.getHealth();
                    float damage = event.getAmount();
                    float baseMaxHealth = (float) (maxHealthAttr.getBaseValue());

                    if (currentHealth - damage < baseMaxHealth) {
                        // If damage exceeds the current extra health, remove the modifier
                        maxHealthAttr.removeModifier(HEALTH_MODIFIER_UUID);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            Iterator<DelayedRemoval> iterator = PENDING_REMOVALS.iterator();
            while (iterator.hasNext()) {
                DelayedRemoval removal = iterator.next();
                removal.ticks--;
                if (removal.ticks <= 0) {
                    if (removal.entity != null && removal.entity.isAlive()) {
                        removal.entity.discard();
                    }
                    iterator.remove();
                }
            }
        }
    }

    private static class DelayedRemoval {
        final Entity entity;
        int ticks;

        DelayedRemoval(Entity entity, int ticks) {
            this.entity = entity;
            this.ticks = ticks;
        }
    }
}
