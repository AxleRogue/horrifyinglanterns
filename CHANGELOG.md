# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.0.4] - 2026-06-23
### Added
- **Community Support**: Created `ISSUE_TRACKER.md` with contribution guidelines, reporting rules, and issue labels.
- **Custom Sounds**: Integrated new atmospheric sounds for Dark Ones and Servants of the Dark Heart.
  - Added `minion_idle` (breathing) and `minion_attack` (random screech) sound events.
  - Added localized subtitles for minion sounds.
- Updated `README.md` to include custom sound documentation and links to the issue tracker.

### Changed
- Set the breathing sound as the death sound for both Dark Ones and Servants of the Dark Heart.

### Fixed
- Fixed a bug where lanterns would duplicate if the player died in a different dimension than their current one.
- Improved tethering logic to ensure lanterns are properly removed from the world after being returned to the player.
- Fixed an issue where custom sounds were not playing due to incorrect file naming conventions.

## [1.0.3] - 2026-06-22
### Added
- **Servant of the Dark Heart**: A new tamable, flying, and invulnerable entity that carries tethered lanterns for the player when their inventory is full.
  - Automatically summoned when a tethered lantern is dropped (e.g., from a broken chest or manually tossed).
  - Displays the carried lantern in hand with a unique animation.
  - Automatically returns the lantern to the player's inventory when space becomes available.
  - Always displays its nametag above its head.
- **Lantern Tethering Enhancements**:
  - Prevented lantern storage in Ender Chests to ensure dark energy remains within the physical world.
  - Improved logic for returning tethered items to owners upon death or world events.
- Added carrying animations and inventory support for the Servant of the Dark Heart.

### Changed
- Removed the experimental "Tethered Heart" GUI and related configuration settings.
- Increased default lantern storage reliability.

## [1.0.2] - 2026-06-20
### Added
- New `SUMMON` and `WRATH` ability types for Dark Skies Lantern.
- Modular ability system using `BaseAbility` classes.
- API base classes: `BaseEntity`, `BaseLivingEntity`, `BaseEntityRenderer`, `BaseLivingEntityRenderer`, and `BaseKeyMapping`.
- Standardized action bar feedback for all abilities and cooldowns.

### Changed
- Refactored `DarkSkiesLanternItem` to use the new Summon and Wrath abilities.
- Relocated and renamed existing Dark Skies Lantern logic to align with new ability types.
- Updated `LanternBaseItem` to include an internal ability registry for easier extension.
- Moved ability feedback (cooldowns, status) from network packets to item/ability classes for better encapsulation.

## [1.0.1] - 2026-06-20
### Added
- AGPL-3.0 license file and headers.
- Comprehensive wiki documentation covering lanterns, abilities, models, recipes, and advancements.
- Project README with license details.

### Changed
- Improved lighting system by implementing a custom solution.
- Refactored codebase to remove unused legacy entity, handler, and client-side classes.

### Fixed
- Cleaned up legacy lantern features.

## [1.0.0] - 2026-06-20
### Added
- Initial release of Horrifying Lanterns.
- Spooky lantern items with unique abilities.
- Network packets for lantern toggling and abilities.
- Player animation and lighting handlers.
- Language support (en_us).
