# API Reference

The Horrifying Lanterns API is organized into several packages under `me.axlerogue.horrifyinglanterns.api`.

## Core Class: `me.axlerogue.horrifyinglanterns.api.LanternBaseItem`

### Fields
- `protected int lightColor`: The color of the light in ARGB format. Default is `0xFFFFFF` (white).

### Methods

#### `public int getLightColor()`
Returns the `lightColor` of the lantern.

#### `public void registerAbility(AbilityType type, BaseAbility ability)`
Registers a custom ability to the lantern for the specified `AbilityType`.

#### `public static boolean isLit(ItemStack stack)`
Checks the NBT of the given `ItemStack` to see if the `isLit` tag is true.

#### `public static void toggle(ItemStack stack)`
Flips the `isLit` NBT tag on the given `ItemStack`.

## New API Base Classes

The API now provides several base classes to simplify content creation:

- **`BaseAbility`**: Logic for active lantern powers. Handles cooldowns and feedback automatically.
- **`BaseEntity`**: Base for special lantern-summoned entities (extends `LightningBolt`).
- **`BaseLivingEntity`**: Base for custom living entities (extends `TamableAnimal`).
- **`BaseEntityRenderer`**: Base for entity renderers.
- **`BaseLivingEntityRenderer`**: Base for living entity renderers with model support.
- **`BaseKeyMapping`**: Base for custom key bindings.

## Client-Side Details
- **Dynamic Lighting**: Handled automatically via `LightHandler`. Uses `Blocks.LIGHT` placed at the player's feet.
- **Atmospheric Tinting**: Managed by `LightColorHandler` and `LightHandler` to spawn colored particles on the ground and nearby entities.
- **Animations**: `PlayerAnimationHandler` applies custom holding poses to any item extending `LanternBaseItem`.
- **Action Bar Feedback**: All ability messages are displayed on the action bar for better visibility.

## Mod Registries
When creating addons or referencing the base mod's items/entities, you can use these internal registries located in `me.axlerogue.horrifyinglanterns.item.client` and `me.axlerogue.horrifyinglanterns.network`:
- **`ModItems`**: Registry for built-in lanterns (e.g., `SanguineMoonLanternItem`, `DarkSkiesLanternItem`).
- **`ModEntities`**: Registry containing `DarkOnesEntity`, `BlueLightningBolt`, and `ServantOfTheDarkHeartEntity`.
- **`ModCreativeModeTabs`**: The dedicated creative tab for lanterns.
- **`ModPackets`**: Handles the `ToggleLanternPacket` and `LanternAbilityPacket` channels.

## Handlers API
Addon developers can reference or utilize the following handlers located in `me.axlerogue.horrifyinglanterns.api.handler`:
- `LightHandler`: Manages block-based light placement and particle effects.
- `LightColorHandler`: Manages colored lighting data and intensity.
- `LanternInteractionHandler`: Manages interaction logic while holding lanterns.
- `PlayerAnimationHandler`: Handles custom player animations.
- `SanguineEffectHandler`: Manages special effects for sanguine-themed items.
- `ServantSpawnHandler`: Automatically detects dropped lanterns and full player inventories to dynamically spawn `ServantOfTheDarkHeartEntity` instances.
