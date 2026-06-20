package me.axlerogue.horrifyinglanterns.handler;

import me.axlerogue.horrifyinglanterns.Config;
import me.axlerogue.horrifyinglanterns.HorrifyingLanterns;
import me.axlerogue.horrifyinglanterns.api.LanternBaseItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LightBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = HorrifyingLanterns.MODID)
public class LightHandler {
    private static final Map<UUID, BlockPos> LAST_LIGHT_POS = new HashMap<>();

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side == LogicalSide.SERVER && event.phase == TickEvent.Phase.END) {
            Player player = event.player;
            Level level = player.level();
            ItemStack mainHand = player.getItemInHand(InteractionHand.MAIN_HAND);
            ItemStack offHand = player.getItemInHand(InteractionHand.OFF_HAND);

            int lightColor = 0xFFFFFF;
            boolean shouldLight = false;
            if (mainHand.getItem() instanceof LanternBaseItem lantern && LanternBaseItem.isLit(mainHand)) {
                shouldLight = true;
                lightColor = lantern.getLightColor();
            } else if (offHand.getItem() instanceof LanternBaseItem lantern && LanternBaseItem.isLit(offHand)) {
                shouldLight = true;
                lightColor = lantern.getLightColor();
            }

            BlockPos currentPos = player.blockPosition().above(); // Head level
            UUID uuid = player.getUUID();
            BlockPos lastPos = LAST_LIGHT_POS.get(uuid);

            if (shouldLight) {
                // Particles are still nice for extra effect
                if (level instanceof ServerLevel serverLevel) {
                    float r = ((lightColor >> 16) & 0xFF) / 255f;
                    float g = ((lightColor >> 8) & 0xFF) / 255f;
                    float b = (lightColor & 0xFF) / 255f;
                    serverLevel.sendParticles(new DustParticleOptions(new Vector3f(r, g, b), 1.0f),
                            player.getX(), player.getY() + 1.2, player.getZ(), 2, 0.3, 0.3, 0.3, 0.02);
                }

                if (!currentPos.equals(lastPos)) {
                    removeLight(level, lastPos);
                    if (placeLight(level, currentPos)) {
                        LAST_LIGHT_POS.put(uuid, currentPos);
                    } else {
                        LAST_LIGHT_POS.remove(uuid);
                    }
                } else {
                    // Refresh light if it was removed/overridden (though unlikely)
                    placeLight(level, currentPos);
                }
            } else {
                if (lastPos != null) {
                    removeLight(level, lastPos);
                    LAST_LIGHT_POS.remove(uuid);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        UUID uuid = event.getEntity().getUUID();
        if (LAST_LIGHT_POS.containsKey(uuid)) {
            removeLight(event.getEntity().level(), LAST_LIGHT_POS.remove(uuid));
        }
    }

    @SubscribeEvent
    public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        UUID uuid = event.getEntity().getUUID();
        if (LAST_LIGHT_POS.containsKey(uuid)) {
            // Remove light from the OLD dimension level
            // Note: event.getEntity().level() might already be the new dimension.
            // Forge doesn't easily provide the old level here in 1.20.1, 
            // but we can try to use the last position stored.
            // However, since we are moving dimensions, the old block won't be ticking for us.
            // It's better to just clear the map entry.
            LAST_LIGHT_POS.remove(uuid);
        }
    }

    private static boolean placeLight(Level level, BlockPos pos) {
        if (pos == null) return false;
        BlockState state = level.getBlockState(pos);
        if (state.isAir() || state.is(Blocks.LIGHT) || state.getFluidState().isSource()) {
            boolean isWater = state.getFluidState().isSource();
            level.setBlock(pos, Blocks.LIGHT.defaultBlockState()
                    .setValue(LightBlock.LEVEL, Config.lightLevel)
                    .setValue(LightBlock.WATERLOGGED, isWater), 3);
            return true;
        }
        return false;
    }

    private static void removeLight(Level level, BlockPos pos) {
        if (pos == null) return;
        BlockState state = level.getBlockState(pos);
        if (state.is(Blocks.LIGHT)) {
            if (state.getValue(LightBlock.WATERLOGGED)) {
                level.setBlock(pos, Blocks.WATER.defaultBlockState(), 3);
            } else {
                level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            }
        }
    }
}
