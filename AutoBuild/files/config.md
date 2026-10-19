---
description: The configuration file will be described here
icon: page
---

# config

***

## Default File

```yaml
Database:
  type: SQLite  # SQLite or MySQL
  MySQL:
    host: localhost
    port: 3306
    database: autobuild
    username: root
    password: password
    table-prefix: autobuild_

Config:
  reload: "&a¡The plugin has been reloaded successfully!"
  no-permission: "&cYou are not allowed to do that."

Animation:
  blocksPerTick: 5
  tickDelay: 1
  generating-build: "&aThe build is being generated, please wait a moment..."
  build-completed: "&aConstruction has been completed successfully. &7(%count% blocks)"

BuildValidation:
  particleType: REDSTONE
  particleDuration: 60
  particleCount: 1
  particleOffset: 0.2
  replaceableMaterials:
    - DIRT
    - GRASS_BLOCK
    - SAND
    - GRAVEL
    - STONE
    - COBBLESTONE
    - DEAD_BUSH
    - FERN
    - DANDELION
    - POPPY
    - BLUE_ORCHID
    - RED_TULIP
    - ORANGE_TULIP
    - WHITE_TULIP
    - PINK_TULIP
    - OXEYE_DAISY
    - CORNFLOWER
    - LILY_OF_THE_VALLEY
    - BROWN_MUSHROOM
    - RED_MUSHROOM

  # Messages
  build-not-found: "&cThe build '%name%' not exist."
  build-empty: "&cThe build'%name%' is empty."
  cannot-place: "&cThe build cannot be generated because there are %count% blocks in the path. They have been marked with particles.."

Help-Commands:
  - ""
  - "&b/autobuild help &7> help message"
  - "&b/autobuild reload &7> reload configuration"
  - "&b/autobuild wand &7> To select the positions to save construction"
  - "&b/autobuild save <name> &7> save build"
  - "&b/autobuild give <name> <player> <amount> &7> give construction item"
  - ""

Queue:
  enabled: true
  maxQueueSize: 50
  maxQueuePerPlayer: 3
  notifyQueueStatus: true
  priorityForStaff: true
  maxBlocksPerBuild: 5000
  checkInterval: 5

AutoBuild:
  no-permission: "&cYou do not have permission to use this command."
  no-console: "&cThis command can only be used by players.."
  unknown-command: "&cUnknown command. Use /autobuild help to see available commands.."

  # Position selection
  no-positions: "&cYou must select two positions first."
  different-worlds: "&cThe two positions must be in the same world."
  first-position-set: "&aFirst position set at &e%x%, %y%, %z%"
  second-position-set: "&aSecond position set at &e%x%, %y%, %z%"

  # Build operations
  build-exists: "&cA build with the name '%name%' already exists"
  build-loaded: "&aBuild '%name%' successfully loaded"
  build-deleted: "&aBuild '%name%' successfully deleted"
  build-info: "&aName: &e%name%&a, Dimensions: &e%width%x%height%x%length%&a, Blocks: &e%blocks%"

  # Build placement
  cannot-place-here: "&cYou can't place the build here (WorldGuard protection)"
  error-generating: "&cThe build could not be generated..."

  # Queue messages
  queue-added: "&aThe build has been added to the queue. Position: &e%position%"
  queue-processing: "&aThe queue is being processed. Please wait..."
  queue-empty: "&cThe queue is empty."
  queue-position: "&aYour build is in position &e%position%&a of the queue."
  queue-cleared: "&aThe queue has been cleared."
  queue-limit: "&cYou have reached the queue limit for builds (%limit%)"

  # Commands message
  save: "&aThe build has been successfully saved as %name%"
  wand: "&aYou have just received the item to save builds!"
  give: "&aYou have received the build %name%"

save-build:
  name: "&a%name% &ebuild"
  material: STONE
  lore:
    - ""
    - "&a&l¿HOW USE?"
    - "&7Place this item in a location with no"
    - "&7nearby structure to generate the building."
    - ""
  disabled-worlds:
    - survival_world
    - pvp_world
  disabled-regions:
    - spawn
    - lobby
```

***

## File description

This file contains the config of the plugin

***

## File Name

`config.yml`
