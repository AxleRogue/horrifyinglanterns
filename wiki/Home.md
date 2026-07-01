# Welcome to the Horrifying Lanterns Wiki

This wiki provides comprehensive documentation and code examples for developers looking to create addons using the Horrifying Lanterns API.

## Table of Contents
1. [[Creating a Custom Lantern]]
2. [[Adding Abilities]]
3. [[Models and Textures]]
4. [[Recipes and Advancements]]
5. [[API Reference]]

## Overview
Horrifying Lanterns is designed to be extensible. By using our `LanternBaseItem` API, you can add new mystical lanterns that integrate perfectly with the mod's dynamic lighting and animation systems.

### Key Features
- **Dynamic Lighting**: Real-time light block placement at the player's feet for consistent performance and visuals.
- **Atmospheric Tinting**: Colored particle systems that tint the environment and nearby entities.
- **Ability System**: Integrated support for active abilities with cooldowns and notifications.
- **Animations**: Custom hand-holding animations for players and servants carrying lanterns.
- **Ownership System**: Lanterns automatically bind to the first player that uses them, preventing other players from toggling them or using their abilities.
- **Servants**: Automatic lantern retrieval system with invulnerable spectral servants that spawn dynamically to fetch dropped lanterns for players with full inventories, prioritizing players based on the amount of dropped lanterns.
- **Minion Alliance**: Minions are strictly allied with their owner and each other, preventing friendly fire.
- **Creeper Repellent**: The terrifying aura of lit lanterns naturally scares away any Creepers that get too close.
- **Auto-Extinguish**: Lit lanterns automatically extinguish after a configurable amount of in-game minutes to balance their power.
