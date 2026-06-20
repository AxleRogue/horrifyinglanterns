# API Reference

The Horrifying Lanterns API is organized into several packages under `me.axlerogue.horrifyinglanterns.api`.

## Core Class: `me.axlerogue.horrifyinglanterns.api.LanternBaseItem`

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
- **Dynamic Lighting**: Handled automatically via `LightHandler`. Uses `Blocks.LIGHT` placed at the player's feet.
- **Atmospheric Tinting**: Managed by `LightColorHandler` and `LightHandler` to spawn colored particles on the ground and nearby entities.
- **Animations**: `PlayerAnimationHandler` applies custom holding poses to any item extending `LanternBaseItem`.

## Handlers API
Addon developers can reference or utilize the following handlers located in `me.axlerogue.horrifyinglanterns.api.handler`:
- `LightHandler`: Manages block-based light placement and particle effects.
- `LightColorHandler`: Manages colored lighting data and intensity.
- `LanternInteractionHandler`: Manages interaction logic while holding lanterns.
- `PlayerAnimationHandler`: Handles custom player animations.
- `SanguineEffectHandler`: Manages special effects for sanguine-themed items.
