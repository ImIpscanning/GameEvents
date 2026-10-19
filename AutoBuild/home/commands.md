---
description: Here I will tell you the commands and their basic functionality
icon: square-terminal
---

# Commands

## Mains Command:

/autobuild\
/queue

***

## Arguments

Arguments to be used in the main command

Meaning of table variables Null = empty <> = mandatory \[] = optional

{% tabs %}
{% tab title="Null or help" %}
Show Help Menu

examples:

/autobuild\
/autobuild help
{% endtab %}

{% tab title="give <BuildName> <player> [amount]" %}
It gives you the construction with the name (you need to create it first)

example:

/autobuild give VillagerHouse1 Dianelito 1
{% endtab %}

{% tab title="wand" %}
You get the bar to mark the buildings to create the build

example:

/autobuild wand
{% endtab %}

{% tab title="save <BuildName>" %}
Save the selected build with the /autobuild wand stick

example:

/autobuild save VillagerHouse1
{% endtab %}

{% tab title="list or Null" %}
View builds waiting to be built by the plugin

examples:

/queue list\
/queue
{% endtab %}

{% tab title="clear" %}
Clean and cancel the Auto Build waitlist

example:

/queue clear
{% endtab %}

{% tab title="[NEW] remove" %}
Delete a saved construction

example:

/autobuild save VillagerHouse1
{% endtab %}
{% endtabs %}
