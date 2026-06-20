package me.axlerogue.horrifyinglanterns.api;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * Base class for all horrifying lanterns. 
 * Addon creators should extend this class to create their own lanterns.
 */
public abstract class LanternBaseItem extends Item {
    protected int lightColor = 0xFFFFFF;

    public LanternBaseItem(Item.Properties properties) {
        super(properties);
    }

    /**
     * Returns the color of the light emitted by this lantern in ARGB format.
     */
    public int getLightColor() {
        return this.lightColor;
    }

    /**
     * Checks if the lantern stack is currently lit.
     */
    public static boolean isLit(ItemStack stack) {
        return stack.getOrCreateTag().getBoolean("isLit");
    }

    /**
     * Toggles the lit state of the lantern stack.
     */
    public static void toggle(ItemStack stack) {
        stack.getOrCreateTag().putBoolean("isLit", !isLit(stack));
    }
}
