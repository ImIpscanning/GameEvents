---
description: Here I will tell you the commands and their basic functionality
icon: sidebar-flip
---

# Permissions and Variables

## Permissions:

| Permission                | Command          | Description                                                   |
| ------------------------- | ---------------- | ------------------------------------------------------------- |
| autobuild.debug           | autobuild debug  | debugging of the plugin                                       |
| autobuild.use             |                  | Allows to use the plugin                                      |
| autobuild.reload          | autobuild reload | Allows to reload the plugin                                   |
| autobuild.wand            |  autobuild wand  | Allows to use the wand                                        |
| autobuild.save            | autobuild save   | Allows to save builds                                         |
| autobuild.give            | autobuild give   | Allows to give build items                                    |
| autobuild.queue.clear     | queue clear      | Allows cleaning the queues of other players                   |
| autobuild.queue.bypass    |                  | To allow players to skip the queue                            |
| autobuild.queue.unlimited |                  | To allow players to have more builds queued than the limit    |
| autobuild.queue.nolimit   |                  | To allow players to use buildings that exceed the block limit |

***

## Variables

{% hint style="info" %}
Compatibility with PlaceHolderAPI coming soon
{% endhint %}

| Variable | where it is used | description                              |
| -------- | ---------------- | ---------------------------------------- |
| %name%   | config.yml       | Name of the construction as it was saved |
