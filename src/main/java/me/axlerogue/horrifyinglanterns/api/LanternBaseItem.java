/*
 * Horrifying Lanterns - A Minecraft mod that introduces cursed and powerful lanterns.
 * Copyright (C) 2026 AxleRogue
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package me.axlerogue.horrifyinglanterns.api;

import me.axlerogue.horrifyinglanterns.api.ability.BaseAbility;
import me.axlerogue.horrifyinglanterns.api.ability.AbilityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import java.util.Map;
import java.util.HashMap;

/**
 * Base class for all horrifying lanterns. 
 * Addon creators should extend this class to create their own lanterns.
 */
public abstract class LanternBaseItem extends Item {
    protected int lightColor = 0xFFFFFF;
    protected final Map<AbilityType, BaseAbility> abilities = new HashMap<>();

    public LanternBaseItem(Item.Properties properties) {
        super(properties);
    }

    public BaseAbility getAbility(AbilityType type) {
        return abilities.get(type);
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
