package me.axlerogue.horrifyinglanterns.client;

import me.axlerogue.horrifyinglanterns.HorrifyingLanterns;
import me.axlerogue.horrifyinglanterns.api.LanternBaseItem;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = HorrifyingLanterns.MODID, value = Dist.CLIENT)
public class PlayerAnimationHandler {
    private static final Map<UUID, Float> RIGHT_ARM_PROG = new HashMap<>();
    private static final Map<UUID, Float> PREV_RIGHT_ARM_PROG = new HashMap<>();
    private static final Map<UUID, Float> LEFT_ARM_PROG = new HashMap<>();
    private static final Map<UUID, Float> PREV_LEFT_ARM_PROG = new HashMap<>();
    
    private static final float TRANSITION_SPEED = 0.1f;

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) {
            RIGHT_ARM_PROG.clear();
            PREV_RIGHT_ARM_PROG.clear();
            LEFT_ARM_PROG.clear();
            PREV_LEFT_ARM_PROG.clear();
            return;
        }

        for (Player player : mc.level.players()) {
            boolean leftHanded = player.getMainArm() == HumanoidArm.LEFT;
            updateArm(player, InteractionHand.MAIN_HAND, leftHanded ? LEFT_ARM_PROG : RIGHT_ARM_PROG, leftHanded ? PREV_LEFT_ARM_PROG : PREV_RIGHT_ARM_PROG);
            updateArm(player, InteractionHand.OFF_HAND, leftHanded ? RIGHT_ARM_PROG : LEFT_ARM_PROG, leftHanded ? PREV_RIGHT_ARM_PROG : PREV_LEFT_ARM_PROG);
        }
    }

    private static void updateArm(Player player, InteractionHand hand, Map<UUID, Float> currentMap, Map<UUID, Float> prevMap) {
        UUID uuid = player.getUUID();
        boolean holding = player.getItemInHand(hand).getItem() instanceof LanternBaseItem;
        
        float current = currentMap.getOrDefault(uuid, 0f);
        prevMap.put(uuid, current);
        
        if (holding) {
            current = Math.min(1f, current + TRANSITION_SPEED);
        } else {
            current = Math.max(0f, current - TRANSITION_SPEED);
        }
        currentMap.put(uuid, current);
    }

    public static float getRightArmProgress(UUID uuid, float partialTicks) {
        float current = RIGHT_ARM_PROG.getOrDefault(uuid, 0f);
        float prev = PREV_RIGHT_ARM_PROG.getOrDefault(uuid, 0f);
        return prev + (current - prev) * partialTicks;
    }

    public static float getLeftArmProgress(UUID uuid, float partialTicks) {
        float current = LEFT_ARM_PROG.getOrDefault(uuid, 0f);
        float prev = PREV_LEFT_ARM_PROG.getOrDefault(uuid, 0f);
        return prev + (current - prev) * partialTicks;
    }
}
