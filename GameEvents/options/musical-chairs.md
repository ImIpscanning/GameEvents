---
description: The configuration file will be described here
icon: chair
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

# Musical Chairs



***

## Options

```yaml
eliminationSound: BLOCK_NOTE_BLOCK_DIDGERIDOO
music: MUSIC_DISC_CHIRP # The music that plays

fastestTime: 5 # It's always random between min and max
slowestTime: 20

chairSpawnZone: # Area where chairs appear, appear and fall by gravity itself, so it can occupy any height
  corner1: empty
  corner2: empty
```

***

All those who are not seated lose after 10 seconds of the music being turned off

\
Like all games, custom sounds can be used

\
They can all lose if in the last round no one sits down
