# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

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
