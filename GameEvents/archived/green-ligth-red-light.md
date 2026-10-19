---
description: The configuration file will be described here
hidden: true
icon: play-pause
---

# Copy of Green Ligth Red Light

***

## Default File

```yaml
fancy_name: "&cRed light&7, &aGreen light"
enabled: false
warmup: 10
min_players: 5
max_players: 20

pvp: true
forgiving_time_in_ticks: 10
red_light_sound: BLOCK_NOTE_BLOCK_DIDGERIDOO
green_light_sound: BLOCK_NOTE_BLOCK_BANJO

fastestTime: 2
slowestTime: 5

finish_line:
  corner1: empty
  corner2: empty

spawn_zone:
  corner1: empty
  corner2: empty

```

***

## File description

This file controls the settings and manages the game

***

## Details of each thing

{% tabs %}
{% tab title="Line 1" %}
`fancy_name: "&cRed light&7, &aGreen light"`\
Display name of the mini-game
{% endtab %}

{% tab title="Line 2" %}
`enabled: false`\
Turn the game on or off
{% endtab %}

{% tab title="Line 3" %}
`warmup: 10`\
This is the time it takes to start the game (in seconds)
{% endtab %}

{% tab title="Line 4" %}
`min_players: 5`\
Minimum Players
{% endtab %}

{% tab title="Line 5" %}
`max_players: 20`\
Maximum players
{% endtab %}

{% tab title="Line 7" %}
`pvp: true`\
Enabling PvP between players in game
{% endtab %}

{% tab title="Line 8" %}
`forgiving_time_in_ticks: 7`\
The amount in ticks that allows you to move in red light without being removed

Recommended: 0\
Default: 7
{% endtab %}

{% tab title="Line 9" %}
`red_light_sound: BLOCK_NOTE_BLOCK_DIDGERIDOO`\
PlaySound to put red light
{% endtab %}

{% tab title="Line 10" %}
`green_light_sound: BLOCK_NOTE_BLOCK_BANJO`\
PlaySound to give the green light
{% endtab %}

{% tab title="Line 12" %}
`fastestTime: 2`\
It is the minimum it can take to change the light
{% endtab %}

{% tab title="Line 13" %}
`slowestTime: 5`\
is the maximum it can take to change the light
{% endtab %}

{% tab title="Line 15 - 17" %}
`finish_line: corner1: empty corner2: empty`\
The finish line
{% endtab %}

{% tab title="Line 19 - 21" %}
`spawn_zone: corner1: empty corner2: empty`\
The Spawning Zone
{% endtab %}
{% endtabs %}

***

## File Name

`redGreenLight.yml`
