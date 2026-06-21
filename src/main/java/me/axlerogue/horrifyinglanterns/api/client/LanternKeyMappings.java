package me.axlerogue.horrifyinglanterns.api.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class LanternKeyMappings {
    public static final String KEY_CATEGORY_LANTERNS = "key.categories.horrifyinglanterns";
    
    public static final BaseKeyMapping TOGGLE_LANTERN = new BaseKeyMapping("key.horrifyinglanterns.toggle", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_L, KEY_CATEGORY_LANTERNS);
    public static final BaseKeyMapping LANTERN_BURST = new BaseKeyMapping("key.horrifyinglanterns.burst", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_V, KEY_CATEGORY_LANTERNS);
    public static final BaseKeyMapping LANTERN_LEECH = new BaseKeyMapping("key.horrifyinglanterns.leech", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_B, KEY_CATEGORY_LANTERNS);
    public static final BaseKeyMapping LANTERN_SUMMON = new BaseKeyMapping("key.horrifyinglanterns.summon", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_V, KEY_CATEGORY_LANTERNS);
    public static final BaseKeyMapping LANTERN_WRATH = new BaseKeyMapping("key.horrifyinglanterns.wrath", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_B, KEY_CATEGORY_LANTERNS);
}
