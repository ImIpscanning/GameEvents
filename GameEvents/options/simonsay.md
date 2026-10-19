---
description: Here I will teach you everything you need to know about this Mini Game
icon: bullhorn
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

# Simon Says



***

## Options

```yaml
order_prefix: "&bSimon says: "
fake_order_prefixes: # -> The prefix of false orders
- "&bSimon tells: "
- "&bSimon orders: "
- ""

base_timer: 10# -> Base timer, the higher it is set the longer the players will have in order
# Even so, this timer is not final and is updated as the difficulty increases

rounds_to_mid_game: 10# -> Rounds before reaching the mid game
rounds_to_late_game: 20# -> Rounds before reaching the late game
late_game_timer: 15# The late game timer acts the same as the base timer but from the late game.

max_rounds: 50# -> Maximum Rounds, after this is a draw
valid_orders:
-JUMP
-SNEAK
- CLICK_PLAYER
- CRAFT_ITEM
- RIGHT_CLICK_BLOCK

craft_items: # Items used in the CRAFT_ITEM order. The plugin calculates the ingredients
- NONE

right_click_blocks: # Items used in RIGHT_CLICK_BLOCK
- NONE
```

Early game: The last one has a time limit to complete the order, less and less

Mid game: No time, the last one to complete the order dies

Late game: Once the first one completes it, time runs out for everyone and it is less and less

The only way it will end in a draw is if in the last round the finalists complete the order at the exact same moment.\
It's basically never going to happen
