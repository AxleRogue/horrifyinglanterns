# API Reference

The `LanternBaseItem` class is the core of the Horrifying Lanterns API.

## Class: `me.axlerogue.horrifyinglanterns.api.LanternBaseItem`

### Fields
- `protected int lightColor`: The color of the light in ARGB format. Default is `0xFFFFFF` (white).

### Methods

#### `public int getLightColor()`
Returns the `lightColor` of the lantern.

#### `public static boolean isLit(ItemStack stack)`
Checks the NBT of the given `ItemStack` to see if the `isLit` tag is true.

#### `public static void toggle(ItemStack stack)`
Flips the `isLit` NBT tag on the given `ItemStack`.

## Client-Side Details
- **Dynamic Lighting**: Handled automatically for any item that inherits from this class.
- **Animations**: The `HumanoidModelMixin` checks if the held item is an instance of `LanternBaseItem` to apply the custom holding pose.
