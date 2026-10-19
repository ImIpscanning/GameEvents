---
description: The configuration file will be described here
icon: block-brick
layout:
  title:
    visible: true
  description:
    visible: true
  tableOfContents:
    visible: true
  outline:
    visible: true
  pagination:
    visible: true
---

# Blocks

This file controls the blocks, their levels, the level name, upgrade cost, recharge cost, recharge time, material, and the default hologram comes with 3 chargers: basic, advanced, and premium.

***

## Options

```yaml
blocks:
  basic: # Basic chunk loader
    material: CHEST # Block material (Bukkit Material name)
    display-name: "&a&lBasic Chunk Loader" # Display name for the item
    lore:
      - "&7Keep your chunks loaded even when offline!"
      - "&7Right-click to manage your chunk loader."
      - ""
      - "&eLevel: &f%level%"
      - "&eChunks: &f%chunks%"
    base-duration: 3600 # Base duration in seconds (1 hour = 3600 seconds)
    hologram:
      enabled: true
      lines:
        - "&a&lChunk Loader"
        - "&7Owner: &e%owner%"
        - "&7Level: &e%level%"
        - "&7Chunks: &e%chunks%"
        - "&7Time: &e%time%"
        - "&7Block: &e%block%"

    levels:
      1:
        chunks: 1
        upgrade-cost: 0
        renewal-cost: 100.0
      2:
        chunks: 4
        upgrade-cost: 2500.0
        renewal-cost: 200.0
      3:
        chunks: 9
        upgrade-cost: 5000.0
        renewal-cost: 350.0
      4:
        chunks: 16
        upgrade-cost: 10000.0
        renewal-cost: 500.0
      5:
        chunks: 25
        upgrade-cost: 20000.0
        renewal-cost: 750.0

  advanced:
    material: ENDER_CHEST
    display-name: "&b&lAdvanced Chunk Loader"
    lore:
      - "&7Advanced chunk loading technology!"
      - "&7Loads more chunks with better efficiency."
      - "&7Right-click to manage your chunk loader."
      - ""
      - "&eLevel: &f%level%"
      - "&eChunks: &f%chunks%"

    base-duration: 7200

    hologram:
      enabled: true
      lines:
        - "&b&lAdvanced Loader"
        - "&7Owner: &e%owner%"
        - "&7Level: &e%level%"
        - "&7Chunks: &e%chunks%"
        - "&7Time: &e%time%"

    levels:
      1:
        chunks: 4
        upgrade-cost: 0
        renewal-cost: 150.0
      2:
        chunks: 9
        upgrade-cost: 5000.0
        renewal-cost: 300.0
      3:
        chunks: 16
        upgrade-cost: 10000.0
        renewal-cost: 500.0
      4:
        chunks: 25
        upgrade-cost: 20000.0
        renewal-cost: 750.0
      5:
        chunks: 36
        upgrade-cost: 40000.0
        renewal-cost: 1000.0

  premium:
    material: BEACON
    display-name: "&6&lPremium Chunk Loader"
    lore:
      - "&7The ultimate chunk loading solution!"
      - "&7Maximum efficiency and coverage."
      - "&7Right-click to manage your chunk loader."
      - ""
      - "&eLevel: &f%level%"
      - "&eChunks: &f%chunks%"

    base-duration: 14400

    hologram:
      enabled: true
      lines:
        - "&6&lPremium Loader"
        - "&7Owner: &e%owner%"
        - "&7Level: &e%level%"
        - "&7Chunks: &e%chunks%"
        - "&7Time: &e%time%"

    levels:
      1:
        chunks: 9
        upgrade-cost: 0
        renewal-cost: 250.0
      2:
        chunks: 16
        upgrade-cost: 10000.0
        renewal-cost: 500.0
      3:
        chunks: 25
        upgrade-cost: 20000.0
        renewal-cost: 750.0
      4:
        chunks: 36
        upgrade-cost: 40000.0
        renewal-cost: 1000.0
      5:
        chunks: 49
        upgrade-cost: 80000.0
        renewal-cost: 1500.0
```



***

