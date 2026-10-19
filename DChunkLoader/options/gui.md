---
description: The configuration file will be described here
icon: browser
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

# GUI

These options control the GUI

***

## Options

```yaml
gui:
  main:
    title: "&8Chunk Loader Manager"
    size: 27
  permissions:
    title: "&8Chunk Loader Permissions"
    size: 27
```

```yaml
  # GUI Item names and descriptions
  gui:
    upgrade:
      name: "&aUpgrade Chunk Loader"
      lore:
        - "&7Click to upgrade this chunk loader"
        - "&7to the next level."
        - ""
        - "&7Current Level: &e%level%"
        - "&7Next Level: &e%next_level%"
        - "&7Cost: &e%cost%"

    renew:
      name: "&bRenew Time"
      lore:
        - "&7Click to renew the time on"
        - "&7this chunk loader."
        - ""
        - "&7Cost per hour: &e%cost%"

    info:
      name: "&eChunk Loader Info"
      lore:
        - "&7Owner: &e%owner%"
        - "&7Level: &e%level%"
        - "&7Chunks: &e%chunks%"
        - "&7Status: %status%"
        - "&7Time Left: &e%time%"

    permissions:
      name: "&dManage Permissions"
      lore:
        - "&7Click to manage who can"
        - "&7access this chunk loader."

    remove:
      name: "&cRemove Chunk Loader"
      lore:
        - "&7Click to remove this"
        - "&7chunk loader permanently."
        - ""
        - "&c&lWARNING: This cannot be undone!"

    close:
      name: "&cClose"
      lore:
        - "&7Click to close this menu."

    back:
      name: "&7Back"
      lore:
        - "&7Click to go back."

    confirm:
      name: "&aConfirm"
      lore:
        - "&7Click to confirm the action."

    cancel:
      name: "&cCancel"
      lore:
        - "&7Click to cancel the action."
```



***

