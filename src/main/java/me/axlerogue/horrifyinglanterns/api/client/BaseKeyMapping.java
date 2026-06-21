package me.axlerogue.horrifyinglanterns.api.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;

/**
 * Base class for all horrifying lantern key mappings.
 */
public class BaseKeyMapping extends KeyMapping {
    public BaseKeyMapping(String description, int keyCode, String category) {
        super(description, keyCode, category);
    }

    public BaseKeyMapping(String description, InputConstants.Type type, int keyCode, String category) {
        super(description, type, keyCode, category);
    }
}
