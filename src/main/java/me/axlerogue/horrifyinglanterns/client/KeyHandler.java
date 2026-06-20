package me.axlerogue.horrifyinglanterns.client;

import com.mojang.blaze3d.platform.InputConstants;
import me.axlerogue.horrifyinglanterns.HorrifyingLanterns;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = HorrifyingLanterns.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class KeyHandler {
    public static final String KEY_CATEGORY_LANTERNS = "key.categories.horrifyinglanterns";
    public static final KeyMapping TOGGLE_LANTERN = new KeyMapping("key.horrifyinglanterns.toggle", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_L, KEY_CATEGORY_LANTERNS);
    public static final KeyMapping LANTERN_BURST = new KeyMapping("key.horrifyinglanterns.burst", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_V, KEY_CATEGORY_LANTERNS);
    public static final KeyMapping LANTERN_LEECH = new KeyMapping("key.horrifyinglanterns.leech", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_B, KEY_CATEGORY_LANTERNS);

    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event) {
        event.register(TOGGLE_LANTERN);
        event.register(LANTERN_BURST);
        event.register(LANTERN_LEECH);
    }
}
