---
description: Things that don't fit on the other pages
---

# Extra

All Games:\
They share these 5 configs:
---------------------------

```yaml
fancy_name: "Name" -> Custom name of the game
enabled: false -> Whether enabled or not
Warmup: 10
min_players: 5
max_players: 20
spawn_zone: -> Zone where players spawn
corner1: empty
corner2: empty
```

Almost all of them end up with a single winner\
Except for some that can end in a draw if they are all eliminated at the same time

\
They can be forcibly stopped early with /ge stop&#x20;

\
Can be started manually with /ge start&#x20;

\
They can be set up in-game with /ge configure&#x20;

Some games have only configurable options in their file

\
All games end if they don't start after 90 seconds

***

## GameZone games (e.g. tnt run)

The game zone is the limit of where the plugin can act\
If a game regenerates blocks, for example, it only does so in that area

```yaml
gameZone:
  corner1: empty
  corner2: empty
```
