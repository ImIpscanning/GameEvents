---
description: The configuration file will be described here
icon: raygun
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

# Gun Game



***

## Options

```yaml
levels:# All inventories for each level. They are given in order and they all have the same order.
# It has to be configured in game
  1: empty

killSound: BLOCK_NOTE_BLOCK_BANJO
deathSound: ENTITY_GENERIC_HURT
respawnLocations: # It's not one area, it's different points.
  location1: none
```

***

At the beginning of the game, everyone is teleported to a respawn point

\
As long as there are the same or more respawns than players, 2 people will never appear together

\
Whenever someone dies, they respawn at some respawn point after 5 seconds.

\
It would be good to recommend that you make as many spawn points as possible.

\
Every time someone kills someone, it goes up a level. When he dies he goes down one level.

\
If someone dies without anyone killing them, they simply lose a level and no one gains anything

\
The first to get a kill wins while in the last level

\
The idea is that they make the last level the most difficult to complete (Like a sword)
