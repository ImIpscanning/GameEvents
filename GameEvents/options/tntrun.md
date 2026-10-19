---
description: The configuration file will be described here
icon: bomb
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

# TNT Run



***

## Options

```yaml
blockDecayDelayInTicks: 5
eliminationSound: ENTITY_GENERIC_HURT
gameZone:
  corner1: empty
  corner2: empty
deathZone:
  corner1: empty
  corner2: empty
```

The map is physically constructed, allowing each person to make their maps as they please.

\
The plugin regenerates ALL the TNT blocks that were inside the game zone at the end of the game

\
The blok decay delay is the delay that a block has to disappear once it is stepped on. It is important that

&#x20;there are at least 5 because otherwise the players will not have time to jump

\
Players are eliminated by touching water (Only in 1.0.0)\
The death zone is where they will die upon entering that zone (1.0.1 and above)

***

