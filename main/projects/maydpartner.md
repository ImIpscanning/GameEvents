---
icon: bullhorn
---

# \[NEW] MayDPartner

## What is this?

This is a plugin

***

***

## Project Description

***

***

***

<figure><img src="../.gitbook/assets/MayDPartner.jpg" alt=""><figcaption></figcaption></figure>

#### ​

Partner system for your Minecraft server. Your players will be able to vote for their favorite content creator, and you'll be able to see which partner has recruited new players. Remember, users will receive rewards for voting, and the influencer will be able to see how many referrals they've earned. ![🏆](https://cdn.jsdelivr.net/joypixels/assets/6.5/png/unicode/64/1f3c6.png)\
\
[![👾](https://cdn.jsdelivr.net/joypixels/assets/6.5/png/unicode/64/1f47e.png) Join our support discord:](https://discord.gg/fcg6HDVKXy)\
[![\[IMG\]](https://img.builtbybit.com/cG80TNtf0X9oOn1w4nxvxbc4qwmPLFcPeenCdGKbHI8/resize:fill:2000/format:webp/aHR0cHM6Ly9kaXNjb3JkLmNvbS9hcGkvZ3VpbGRzLzEyODEwMzgyNjEzNTkyMTQ2MjQvd2lkZ2V0LnBuZz9zdHlsZT1iYW5uZXIy)](https://discord.gg/fcg6HDVKXy)\
\
\
Partner system where players can vote for them and receive rewards​\


```yaml
[/CENTER]
# [CONSOLE_COMMAND] [MESSAGE] [TITLE] [SUBTITLE] [ACTIONBAR] [SOUND] [PARTICLE]

#HEADS MATERIAL:
#playerhead-<username>
#head-<base64>

messages:
  no-permission: "&cYou don´t have permission to do that"
  already-voted: "&cYou have already voted for &d&l%partner%"
  reset-time-message: "&cYou can only vote every %time% minutes"

options:
  votes: 1 # votes per player
  vote-same-player: false # if true, you can vote for the same player
  reset-time: 0 #<- 0 = no reset time
  claim:
    item: BARRIER
    name: "&c&lPARTNER CLAIMED"
    lore:
      - "&fYou have already claimed for the partner &d&l%partners%" # if you have more than 1 vote it will appear player1, player2, player3

top-votes-partner:
  permission: default # default, all players will be able to use it, for permission needed put something like maydpartner.topvotes
  command: topvotes
  top-votes:
    - "&7&m---------------------"
    - "&d&lTOP VOTES PARTNERS"
    - "&7&m---------------------"
    - "&dPlayer1: &8[&a%player1_votes%&8]"
    - "&dPlayer2: &8[&a%player2_votes%&8]"
    - "&dPlayer3: &8[&a%player3_votes%&8]"
    - "&7&m---------------------"

# Partners list - add any partner name here and it will automatically create %partnername_votes% variable
partners:
  - player1
  - player2
  - player3

# Dynamic variables are created automatically:
# - %player1_votes% for partner "player1"
# - %player2_votes% for partner "player2"
# - %player3_votes% for partner "player3"
# etc.

panels:
  partner:
    perm: default
    rows: '6'
    title: "&d&lMayDPartner"
    empty: AIR
    item:
      '0-8,9,18,27,36,45-53': # <- These numbers are the menu slots
        material: orange_stained_glass_pane
        name: '&d&lMayDPartner'
        lore:
          - "&aVote for you favorite partner"

      '20':
        material: playerhead-Indefenso
        name: "&dPlayer1 &lPartner &8[&fVotes: &a%player1_votes%&8]"
        lore:
          - "&aVote for player1"
        actions:
          - "[CONSOLE_COMMAND] give %player% diamond 1"
          - "[MESSAGE] &aYou just voted for player1"

      '21':
        material: playerhead-Indefenso
        name: "&dPlayer2 &lPartner &8[&fVotes: &a%player2_votes%&8]"
        lore:
          - "&aVote for player2"
        actions:
          - "[CONSOLE_COMMAND] give %player% diamond 1"
          - "[MESSAGE] &aYou just voted for player2"

      '22':
        material: playerhead-Indefenso
        name: "&dPlayer3 &lPartner &8[&fVotes: &a%player3_votes%&8]"
        lore:
          - "&aVote for player3"
        actions:
          - "[CONSOLE_COMMAND] give %player% diamond 1"
          - "[MESSAGE] &aYou just voted for player3"

    commands:
      - maydpartner
      - partner
      - partners
[CENTER]
```

| ![123.png](https://builtbybit.com/attachments/123-png.1042994/?preset=fullr1) | ![1.png](https://builtbybit.com/attachments/1-png.1042995/?preset=fullr1) | ![2.png](https://builtbybit.com/attachments/2-png.1042996/?preset=fullr1) |
| ----------------------------------------------------------------------------- | ------------------------------------------------------------------------- | ------------------------------------------------------------------------- |

\


> TOS\
> \
> By purchasing this plugin, you agree to the following:\
>
>
> * You have read the plugin page in its entirety.
> * You are not allowed to redistribute this plugin in any way shape or form.
> * You are not allowed to decompile or attempt to reverse engineer the plugin in any way shape or form.
> * We are not responsible for any issues caused by external plugins.
> * Refunds will vary on a case-by-case basis; buyers may be only eligible for a partial refund depending on the situation. You are not eligible for a refund if 3 days have passed since you purchased the plugin. Refunds will not be given for any reason/disclaimer that is clearly listed on the plugin page. Refunds will not be given for any reason that has nothing to do with this resource (i.e. shutting down your server).
> * If you are to be given a refund, you agree to delete your review/not review the product
> * Transferring ownership of the plugin from one account to another is not allowed.
> * You are responsible for your copy of MAYDPARTNER. If your copy of MAYDPARTNER is found to be redistributed, you will still be held responsible.

## Link to purchase: [https://builtbybit.com/resources/maydpartner.67698/](https://builtbybit.com/resources/maydpartner.67698/)

##
