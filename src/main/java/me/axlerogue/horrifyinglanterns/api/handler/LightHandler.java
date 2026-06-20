package me.axlerogue.horrifyinglanterns.api.handler;

import me.axlerogue.horrifyinglanterns.Config;
import me.axlerogue.horrifyinglanterns.HorrifyingLanterns;
import me.axlerogue.horrifyinglanterns.api.LanternBaseItem;
import me.axlerogue.horrifyinglanterns.api.entity.BlueLightningBolt;
import me.axlerogue.horrifyinglanterns.api.entity.DarkOnesEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.LightBlock;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = HorrifyingLanterns.MODID)
public class LightHandler {
    private static final Map<UUID, BlockPos> PLAYER_LIGHT_POSITIONS = new HashMap<>();
    private static final Map<UUID, BlockPos> ENTITY_LIGHT_POSITIONS = new HashMap<>();

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Player player = event.player;
        Level level = player.level();
        UUID uuid = player.getUUID();

        boolean shouldEmitLight = false;
        ItemStack mainHand = player.getItemInHand(InteractionHand.MAIN_HAND);
        ItemStack offHand = player.getItemInHand(InteractionHand.OFF_HAND);

        if (mainHand.getItem() instanceof LanternBaseItem && LanternBaseItem.isLit(mainHand)) {
            shouldEmitLight = true;
        } else if (offHand.getItem() instanceof LanternBaseItem && LanternBaseItem.isLit(offHand)) {
            shouldEmitLight = true;
        }

        handleEntityLighting(player, shouldEmitLight, Config.lightLevel, PLAYER_LIGHT_POSITIONS);

        if (shouldEmitLight && level.isClientSide) {
            spawnParticles(player);
        }
    }

    @SubscribeEvent
    public static void onEntityTick(net.minecraftforge.event.entity.living.LivingEvent.LivingTickEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player) return; // Handled in onPlayerTick

        Level level = entity.level();
        boolean shouldEmitLight = false;
        int levelInt = Config.lightLevel;

        if (entity instanceof DarkOnesEntity) {
            shouldEmitLight = true;
            levelInt = Config.lightLevel / 2;
        } else if (entity instanceof BlueLightningBolt) {
            shouldEmitLight = true;
        }

        if (shouldEmitLight) {
            handleEntityLighting(entity, true, levelInt, ENTITY_LIGHT_POSITIONS);
        } else {
            UUID uuid = entity.getUUID();
            if (ENTITY_LIGHT_POSITIONS.containsKey(uuid)) {
                removeLight(level, ENTITY_LIGHT_POSITIONS.get(uuid));
                ENTITY_LIGHT_POSITIONS.remove(uuid);
            }
        }

        // Tint animals and mobs and bosses
        if (level.isClientSide && !(entity instanceof Player)) {
            float intensity = LightColorHandler.getIntensity();
            if (intensity > 0) {
                // If the player is nearby holding a lantern, spawn particles on the entity
                Player localPlayer = net.minecraft.client.Minecraft.getInstance().player;
                if (localPlayer != null && entity.distanceToSqr(localPlayer) < 64.0) {
                    spawnEntityParticles(entity, LightColorHandler.getCurrentColor());
                }
            }
        }
    }

    private static void spawnEntityParticles(Entity entity, int color) {
        float r = ((color >> 16) & 0xFF) / 255.0f;
        float g = ((color >> 8) & 0xFF) / 255.0f;
        float b = (color & 0xFF) / 255.0f;
        
        if (entity.level().random.nextFloat() < 0.2f) { // Only spawn occasionally
            entity.level().addParticle(new net.minecraft.core.particles.DustParticleOptions(new org.joml.Vector3f(r, g, b), 1.0f),
                    entity.getX() + (entity.level().random.nextDouble() - 0.5) * entity.getBbWidth(),
                    entity.getY() + entity.level().random.nextDouble() * entity.getBbHeight(),
                    entity.getZ() + (entity.level().random.nextDouble() - 0.5) * entity.getBbWidth(),
                    0, 0, 0);
        }
    }

    private static void handleEntityLighting(Entity entity, boolean shouldEmitLight, int lightLevel, Map<UUID, BlockPos> positionsMap) {
        Level level = entity.level();
        UUID uuid = entity.getUUID();
        BlockPos currentPos = entity.blockPosition(); // Use feet position
        BlockPos lastPos = positionsMap.get(uuid);

        if (shouldEmitLight) {
            if (lastPos != null && !lastPos.equals(currentPos)) {
                removeLight(level, lastPos);
            }
            placeLight(level, currentPos, lightLevel);
            positionsMap.put(uuid, currentPos);
        } else if (lastPos != null) {
            removeLight(level, lastPos);
            positionsMap.remove(uuid);
        }
    }

    private static void spawnParticles(Player player) {
        ItemStack mainHand = player.getItemInHand(InteractionHand.MAIN_HAND);
        ItemStack offHand = player.getItemInHand(InteractionHand.OFF_HAND);
        ItemStack lantern = mainHand.getItem() instanceof LanternBaseItem ? mainHand : offHand;
        
        if (lantern.getItem() instanceof LanternBaseItem baseItem) {
            int color = baseItem.getLightColor();
            float r = ((color >> 16) & 0xFF) / 255.0f;
            float g = ((color >> 8) & 0xFF) / 255.0f;
            float b = (color & 0xFF) / 255.0f;
            
            // Tint the ground or blocks only for player
            for (int i = 0; i < 3; i++) {
                player.level().addParticle(new net.minecraft.core.particles.DustParticleOptions(new org.joml.Vector3f(r, g, b), 1.0f),
                        player.getX() + (player.level().random.nextDouble() - 0.5) * 3.0,
                        player.getY() - 0.5 + player.level().random.nextDouble() * 0.5, // Closer to ground
                        player.getZ() + (player.level().random.nextDouble() - 0.5) * 3.0,
                        0, 0, 0);
            }
        }
    }

    private static void placeLight(Level level, BlockPos pos, int lightLevel) {
        if (level.getBlockState(pos).isAir()) {
            BlockState lightState = Blocks.LIGHT.defaultBlockState().setValue(LightBlock.LEVEL, lightLevel);
            level.setBlock(pos, lightState, 3);
        }
    }

    private static void removeLight(Level level, BlockPos pos) {
        if (level.getBlockState(pos).is(Blocks.LIGHT)) {
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
        }
    }
}
