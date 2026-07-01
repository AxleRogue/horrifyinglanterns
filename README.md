# Horrifying Lanterns

Horrifying Lanterns is a Minecraft mod for version 1.20.1 (Forge) that introduces cursed and powerful lanterns to your world. These aren't just light sources; they are mystical artifacts that grant the wielder terrifying abilities at the cost of being tethered to their dark energies.

## 🏮 Key Features

- **Dynamic Lighting**: Lanterns emit light while held in your main hand or off-hand.
- **Unique Abilities**: Each lantern comes with two distinct active abilities (Sanguine Moon: Burst and Leech | Dark Skies: Summon and Wrath).
- **Custom Sounds**: New atmospheric sounds for minions, including idle breathing and attack screeches.
- **Ownership System**: Lanterns bind to their first user automatically. Only the bound owner can toggle them or use their abilities.
- **Servant of the Dark Heart**: When a player drops their owned lanterns and their inventory is full, loyal, invulnerable spectral servants will spawn dynamically based on the amount of dropped lanterns. They prioritize players based on the number of dropped lanterns, fetch them, and follow the player until inventory space frees up.
- **Minion Alliance**: Minions (like Servants and Dark Ones) are strictly allied with their owner. They will not attack each other, and friendly fire from the owner is prevented.
- **Creeper Repellent**: The terrifying aura of lit lanterns naturally scares away any Creepers that get too close.
- **Auto-Extinguish**: To maintain balance, any lit lantern will automatically extinguish itself after 10 in-game minutes of continuous use, warning the player to reignite it quickly.
- **Custom Animations**: Players and Servants holding a lantern will have a unique "zombie-like" arm pose.
- **Atmospheric Effects**: Colored particles and sounds enhance the horror experience.
- **Addon API**: A built-in API with base classes for items, abilities, entities, and renderers allows other modders to easily create their own custom lanterns.

---

## 🕯️ The Lanterns

### 🔴 Sanguine Moon Lantern
A cursed lantern that feeds on the life essence of those around it. It glows with a deep red light.

*   **Sanguine Burst (Key: V)**: Triggers a burst of red energy, granting the player **Invisibility** for 30 seconds and summoning a swarm of **8 Bats** that swirl around the wielder.
*   **Life Leech (Key: B)**: Drains health from all nearby living entities (excluding players) within a 5-block radius. Each hit heals the player and **permanently increases the player's max health** (up to a limit of 50 total health).
*   **Crafting**: Blackstone, Ink Sac, Redstone Block, Glass, and Blaze Powder.

### 🔵 Dark Skies Lantern
A lantern that harnesses the power of the dark abyss and the storm. It glows with a dark blue light.

*   **Summon (Key: V)**: Summons **4 Dark Ones** (loyal shadow entities) that hunt down the entity you are currently looking at. If no target is found, they simply guard you. You can only have one set of Dark Ones active at a time.
*   **Wrath (Key: B)**: Strikes the target location (up to 32 blocks away) with a bolt of **Abyssal Lightning**, dealing magic damage to anything caught in the strike.
*   **Crafting**: Blackstone, Ink Sac, Amethyst Block, Glass, and Blaze Powder.

### 🟢 Venom Fang Lantern
A cursed lantern glowing with a sickly green light that channels the essence of venomous creatures.

*   **Poison AOE (Key: V)**: Releases a toxic cloud that inflicts **Poison II** on all nearby living entities within an 8-block radius for 10 seconds.
*   **Spider Pacification (Key: B)**: Calms the minds of arachnids, pacifying all spiders within a 16-block radius, removing their aggression, and causing them to scatter away in fear.
*   **Crafting**: Blackstone, Ink Sac, Emerald Block, Glass, Blaze Powder, and Spider Eye.

---

## ⚙️ Configuration

The mod is highly configurable via the in-game config screen (found in the Mod List) or the `horrifyinglanterns-common.toml` file.

*   **Light Level**: Adjust the brightness of the lantern's light (0-15).
*   **Auto-Extinguish Time**: Configure the amount of in-game minutes (1-60) before a lit lantern automatically turns itself off (default is 10).
*   **Abilities**: Each lantern has two unique abilities with dedicated cooldowns.
*   **Feedback**: All ability status and cooldown messages are displayed clearly on the action bar.
*   **Lighting System**: A custom block-based system provides real light at the player's feet.
*   **Atmospheric Tinting**: Colored particles provide an immersive glow on the ground and entities.

---

## 🏆 Advancements

*   **Sanguine Discovery**: Obtain a Sanguine Moon Lantern.
*   **Abyssal Discovery**: Obtain a Dark Skies Lantern.
*   **Venomous Discovery**: Obtain a Venom Fang Lantern.
*   **Master of Horrifying Lanterns**: Obtain all three lanterns to unlock a special challenge advancement with a custom horror-themed background.

---

## 🛠️ For Developers: Addon API

Horrifying Lanterns provides a public API for addon creators. You can create your own custom lanterns by extending `me.axlerogue.horrifyinglanterns.api.LanternBaseItem`.

### Features provided by the API:
*   Automatic dynamic lighting when held.
*   Automatic custom arm holding animation.
*   Easy color customization via `lightColor`.
*   Built-in toggle mechanism (standard keybind 'L').
*   Full access to key mappings, abilities, entities, and handlers.

Check out the `API_GUIDE.md` in the mod files for a full tutorial and code examples.
Check out the `ISSUE_TRACKER.md` for information on how to report bugs or suggest new features.

---

## 📜 License & Credits

*   **License**: This project is licensed under the [GNU Affero General Public License v3.0 (AGPL-3.0)](LICENSE).
*   **Created by**: AxleRogue
*   **Developed with**: Minecraft Forge 1.20.1
