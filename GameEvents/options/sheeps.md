---
description: Here I will teach you everything you need to know about this Mini Game
icon: sheep
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

# Exploding sheeps



***

## Options

```yaml
# Cuánto van a tardar las ovejas en explotar desde que spawnean
explodeTimer: 3 

gameZone:
  corner1: empty
  corner2: empty
```

Sheep explode blocks if for some reason they end up outside the GameZone. \
It is important to notify this so that the owners can ensure that users cannot leave the game zones. \
Also, they can only appear inside the game zone, so there is no danger.&#x20;

When players receive damage from an explosion, they are eliminated.&#x20;

The sheep appear every 4 seconds initially, decreasing to one appearing every second.&#x20;

The game ends when there is one player left standing.
