package me.axlerogue.horrifyinglanterns.api.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class LanternKeyMappings {
    public static final String KEY_CATEGORY_LANTERNS = "key.categories.horrifyinglanterns";
    
    public static final KeyMapping TOGGLE_LANTERN = new KeyMapping("key.horrifyinglanterns.toggle", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_L, KEY_CATEGORY_LANTERNS);
    public static final KeyMapping LANTERN_BURST = new KeyMapping("key.horrifyinglanterns.burst", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_V, KEY_CATEGORY_LANTERNS);
    public static final KeyMapping LANTERN_LEECH = new KeyMapping("key.horrifyinglanterns.leech", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_B, KEY_CATEGORY_LANTERNS);
}
