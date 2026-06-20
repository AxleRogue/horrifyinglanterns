# Horrifying Lanterns

Horrifying Lanterns is a Minecraft mod for version 1.20.1 (Forge) that introduces cursed and powerful lanterns to your world. These aren't just light sources; they are mystical artifacts that grant the wielder terrifying abilities at the cost of being tethered to their dark energies.

## 🏮 Key Features

- **Dynamic Lighting**: Lanterns emit light while held in your main hand or off-hand.
- **Unique Abilities**: Each lantern comes with two distinct active abilities (Burst and Leech).
- **Custom Animations**: Players holding a lantern will have a unique "zombie-like" arm pose.
- **Atmospheric Effects**: Colored particles and sounds enhance the horror experience.
- **Addon API**: A built-in API allows other modders to easily create their own custom lanterns.

---

## 🕯️ The Lanterns

### 🔴 Sanguine Moon Lantern
A cursed lantern that feeds on the life essence of those around it. It glows with a deep red light.

*   **Sanguine Burst (Key: V)**: Triggers a burst of red energy, granting the player **Invisibility** for 30 seconds and summoning a swarm of **8 Bats** that swirl around the wielder.
*   **Life Leech (Key: B)**: Drains health from all nearby living entities (excluding players) within a 5-block radius. Each hit heals the player and **permanently increases the player's max health** (up to a limit of 50 total health).
*   **Crafting**: Blackstone, Ink Sac, Redstone Block, Glass, and Blaze Powder.

### 🔵 Dark Skies Lantern
A lantern that harnesses the power of the dark abyss and the storm. It glows with a dark blue light.

*   **Dark Burst (Key: V)**: Summons **4 Dark Ones** (loyal shadow entities) that hunt down the entity you are currently looking at. If no target is found, they simply guard you. You can only have one set of Dark Ones active at a time.
*   **Sky Leech (Key: B)**: Strikes the target location (up to 32 blocks away) with a bolt of **Abyssal Lightning**, dealing magic damage to anything caught in the strike.
*   **Crafting**: Similar to the Sanguine Moon Lantern, but utilizing abyssal materials.

---

## ⚙️ Configuration

The mod is highly configurable via the in-game config screen (found in the Mod List) or the `horrifyinglanterns-common.toml` file.

*   **Light Level**: Adjust the brightness of the lantern's light (0-15).
*   **Light Radius**: Change how far the dynamic light reaches (1.0-16.0 blocks).
*   **Lucent Integration**: If the **Lucent** mod is installed, lanterns will provide true colored dynamic lighting for an even more immersive experience.

---

## 🏆 Advancements

*   **Sanguine Discovery**: Obtain a Sanguine Moon Lantern.
*   **Abyssal Discovery**: Obtain a Dark Skies Lantern.
*   **Master of Horrifying Lanterns**: Obtain both lanterns to unlock a special challenge advancement with a custom horror-themed background.

---

## 🛠️ For Developers: Addon API

Horrifying Lanterns provides a public API for addon creators. You can create your own custom lanterns by extending `me.axlerogue.horrifyinglanterns.api.LanternBaseItem`.

### Features provided by the API:
*   Automatic dynamic lighting when held.
*   Automatic "zombie-arm" holding animation.
*   Easy color customization via `lightColor`.
*   Built-in toggle mechanism (standard keybind 'L').

Check out the `API_GUIDE.md` in the mod files for a full tutorial and code examples.

---

## 📜 License & Credits

*   **License**: This project is licensed under the [GNU Affero General Public License v3.0 (AGPL-3.0)](LICENSE).
*   **Created by**: AxleRogue
*   **Developed with**: Minecraft Forge 1.20.1
