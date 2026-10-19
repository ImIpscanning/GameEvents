---
description: here we teach you how to add rewards to games
icon: treasure-chest
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

# \[NEW] Rewards



***

## Options

```yaml
rewards:
  play_reward:
    type: PLAY
    actions:
      - "message: &eThis reward is given to everyone who plays."
  play_reward_permission:
    type: PLAY
    permission: gameevent.test
    actions:
      - "message: &eThis reward is given to all who play and have the indicated permission."
  win_reward:
    type: WIN
    actions:
      - "message: &eThis reward is given to the winner."
  lose_reward:
    type: LOSE
    actions:
      - "message: &eThis reward is given to everyone who played, except the winner."
```

They are added at the end of any game file. \
It works this way so that any game can have its own rewards, and they may not have them.&#x20;

Types of rewards: \
message: Sends a message to the player \
command: Executes a command in the console.&#x20;

%prefix% and %player% can be used in actions.
