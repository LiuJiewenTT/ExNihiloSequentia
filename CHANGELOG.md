# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/), and this project adheres to [NeoForged Semantic Versioning](https://docs.neoforged.net/docs/gettingstarted/versioning).

## [7.0.3](https://github.com/LiuJiewenTT/ExNihiloSequentia/compare/v7.0.2...v7.0.3) - 2026-01-07

### Added
- Add bamboo crook durability configuration, and the tier is sugguested to be changed from stone to wood.
- Add basalt crook durability configuration.
- Add blackstone crook durability configuration.
- Add calcite crook durability configuration.
- Add dark oak sieve crafting recipe that was removed from initiating when upgrading to 1.21.3+.

### Changed
- Bamboo crook no longer uses wood crook durability configuration, for now.
- Basalt crook no longer uses andesite crook durability configuration, for now.
- Blackstone crook no longer uses andesite crook durability configuration, for now.
- Copper hammer no longer uses iron crook durability configuration, for now. It is using copper crook value.
- Never enabled prismarine crook is now enabled in `EXNItems` and uses stone crook durability configuration, for now.
- Never enabled prismarine hammer is now enabled in `EXNItems` and uses stone crook durability configuration, for now.

### Fixed
- Displayed default durability value for iron crook is corrected to actual value of 512.

### Suggestion to Devs
- Consider adding independent configuration for some materials of crooks and hammers. Currently some hammers share the value with the crooks.

## [7.0.2](https://github.com/NovaMachina-Mods/ExNihiloSequentia/compare/v7.0.1...v7.0.2) - 2025-01-29

### Added
- Client Item files
- Item factory methods
- Furnace fuel files
- 

### Changed
- Recipes now match vanilla item fields
- Move block and item properties to definition
- Solid and fluid render algorithms
- Crucible consumption algorithm

## [7.0.1](https://github.com/NovaMachina-Mods/ExNihiloSequentia/compare/v7.0.0...v7.0.1) - 2024-07-19

### Changed
- Config moved to STARTUP rather than COMMON
- Update to NeoForge 21.0.106-beta

## [7.0.0](https://github.com/NovaMachina-Mods/ExNihiloSequentia/compare/v6.0.0...v7.0.0) - 2024-07-09

### Changed
- Update to NeoForge 21.0.40-beta
- Update to NovaCore 3.0.0
- Moved common tags from `forge` to `c`

## [6.0.0](https://github.com/NovaMachina-Mods/ExNihiloSequentia/compare/v5.0.0...v6.0.0) - 2024-01-01

### Added
- `ITooltipProvider` interface for blocks
- Codecs for all recipes
- Advancements for all recipes
### Changed
- Update to NeoForge 20.4.167
- Update to NovaCore 2.0.0
- Replace all references of Forge with NeoForge
- Recipes with varargs parameters replaced with `List` parameter
- `MeshType` and `CrucibleType` now implement `StringRepresentable`
- Updated ore networking with ConfigurationTasks
### Removed
- CraftTweaker, Jade, JEI, KubeJS and TOP compatibility and moved them to their own addon mod
- Useless `EventBusSubscriber` annotations
- Id parameter from recipes

## [5.0.0](https://github.com/NovaMachina-Mods/ExNihiloSequentia/compare/v5.0.0) - 2023-10-27

### Added
- Dependency on [NovaCore](https://github.com/NovaMachina-Mods/NovaCore)
- Cherry barrel, crook, crucible, hammer, and sieve
- Bamboo barrel, crook, crucible, hammer, and sieve
- Christmas and Halloween Crook
### Changed
- Replace Forge with [NeoForge](https://neoforged.net/)
  - Requires NeoForge 47.1.55+
- Use NovaCore definitions, registries, and common data generation classes
- Replaced CraftTweaker builder functions with single `addRecipe` method
- Updated textures
### Removed
- Common Data Generation classes for:
  - Recipes
  - Loot Tables
  - Tags
  - Language
