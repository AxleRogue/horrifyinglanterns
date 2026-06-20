package me.axlerogue.horrifyinglanterns.client;

import me.axlerogue.horrifyinglanterns.api.LanternBaseItem;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import org.joml.Vector3f;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class LightColorHandler {
    private static int currentColor = 0xFFFFFF;
    private static float intensity = 0f;

    public static void update() {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null) {
            intensity = 0f;
            return;
        }

        ItemStack mainHand = player.getItemInHand(InteractionHand.MAIN_HAND);
        ItemStack offHand = player.getItemInHand(InteractionHand.OFF_HAND);

        if (mainHand.getItem() instanceof LanternBaseItem lantern && LanternBaseItem.isLit(mainHand)) {
            currentColor = lantern.getLightColor();
            intensity = 1f;
        } else if (offHand.getItem() instanceof LanternBaseItem lantern && LanternBaseItem.isLit(offHand)) {
            currentColor = lantern.getLightColor();
            intensity = 1f;
        } else {
            intensity = Math.max(0f, intensity - 0.05f);
        }
    }

    public static void modifyLightColor(int blockLightLevel, Vector3f color) {
        if (intensity <= 0) return;

        float r = ((currentColor >> 16) & 0xFF) / 255f;
        float g = ((currentColor >> 8) & 0xFF) / 255f;
        float b = (currentColor & 0xFF) / 255f;

        // Only tint if it's block light (blockLightLevel > 0)
        // AND don't tint if the light level is 15 (max), which is often used for GUI/Fullbright
        if (blockLightLevel > 0 && blockLightLevel < 15) {
            float ratio = (blockLightLevel / 15f) * intensity;
            color.lerp(new Vector3f(r, g, b).mul(color.length()), ratio * 0.8f);
        }
    }
}
