# Horrifying Lanterns - Project Specific Guidelines

## General Rules
- All lantern items must extend `me.axlerogue.horrifyinglanterns.api.LanternBaseItem`.
- **Ownership System**: Lanterns automatically bind to their first user to prevent unauthorized interactions or ability use.
- **Minion Alliance**: Mod entities (`BaseLivingEntity`) must be allied to their owner and other tamed minions, preventing friendly fire.
- Dynamic lighting must be configurable via `Config.java`.
- Use Forge's `DeferredRegister` for all registry entries.
- Addon creators can use the `LanternBaseItem` API to easily integrate new lanterns.

## Dynamic Lighting
- Lanterns should only emit light when held in either the main hand or offhand.
- The light should follow the player holding the lantern.
- Dynamic light color is determined by the lantern's `getLightColor()` method.
- Toggling the light state must be handled via a registered keybind (`KeyMapping`).
- The light state (ON/OFF) should be persisted in the item's NBT data.

## Configuration
- `lightLevel`: Integer range [0, 15], defines the brightness of the lantern light.
- `lightRadius`: Double range [1.0, 16.0], defines the radius of the dynamic light.
- All configuration values must be synchronized between client and server if necessary (though dynamic light is primarily client-side).

## Sound Effects
- Toggling the lantern light state triggers fire-related sound effects (lighting/extinguishing).
- Sound events should be registered in a dedicated `ModSounds` class.

## Implementation Details
- `KeyHandler`: Registers the 'L' keybind for toggling lanterns.
- `ClientEvents`: Handles the keybind press and sends a `ToggleLanternPacket` to the server.
- `ToggleLanternPacket`: Handles the logic of toggling the "isLit" NBT tag on the lantern item. Verifies the user is the lantern's owner. Includes a 15-second cooldown, plays fire-related sounds on success (lit/extinguish), and sends feedback messages (enabled/disabled/cooldown/not_owner) to the player.
- `LanternAbilityPacket`: Handles ability casting over the network. Verifies the user is the lantern's owner before executing the logic.
- `PlayerAnimationHandler`: Tracks and smooths player arm animations when holding lanterns.
- `HumanoidModelMixin`: Injects into the player model to apply the "zombie-like" arm pose when a lantern is held.
- `LanternBaseItem` (API): Base class for all lanterns. Handles Ownership binding (`checkAndSetOwner` / `isOwner`), auto-extinguish ticking logic, and prevents re-equip animation glitches via `shouldCauseReequipAnimation`. Includes a `lightColor` variable that should be set in the constructor of subclasses.
- `ServantOfTheDarkHeartEntity`: A spectral entity with custom AI (`ServantFindLanternGoal`, `ServantFetchLanternGoal`, `ServantFollowOwnerWaitGoal`, `ServantReturnLanternGoal`) that automatically retrieves dropped lanterns for its bound owner when the owner's inventory is completely full. Spawning is handled by `ServantSpawnHandler`.
- `SanguineMoonLanternItem`: An example lantern that sets `lightColor` to red (0xFF0000).
- `Config`: Stores the global `lightLevel`, `lightRadius`, and `autoExtinguishMinutes`.
- `ModConfigScreen`: In-game GUI for editing configuration values.
- `CreeperScareHandler`: Injects `AvoidEntityGoal` into Creepers so they flee from players holding lit lanterns.

## Recipes & Advancements
- `sanguine_moon_lantern` is craftable using: Blackstone, Ink Sac, Redstone Block, Glass, and Blaze Powder.
- Obtaining `minecraft:blackstone` automatically unlocks the `sanguine_moon_lantern` recipe.
- Obtaining a `sanguine_moon_lantern` triggers the "Sanguine Discovery" advancement.

## Animations
- When a player holds a `LanternBaseItem` (in either hand), the corresponding arm will smoothly transition to a raised position, similar to a Minecraft zombie.
- If the lantern is removed from the hand, the arm will smoothly swing back down to its normal position.
- This is achieved via client-side progress tracking and a mixin to `HumanoidModel`.

## Light Logic
- The dynamic lighting is handled by `LightHandler.java` on the server side for world lighting and client side for visual effects.
- It uses vanilla `Blocks.LIGHT` to create a real light source that follows the player and specific mod entities.
- **Colored Light**: Since vanilla Minecraft does not support colored light blocks, `LightHandler` also spawns colored `DustParticles` based on the lantern's `lightColor`.
  - For **Players**: Particles are spawned near the ground to simulate a colored glow on blocks.
  - For **Mobs/Animals**: When near a lantern-holding player, entities are tinted with colored particles.
- **Advanced Colored Light**: It is recommended to use colored particles to enhance the atmosphere, as vanilla light blocks only provide white light.
