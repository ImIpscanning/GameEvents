---
icon: treasure-chest
---

# \[NEW] DChunkLoader

## What is this?

This is a plugin

***

***

## Project Description

***

***

***

<figure><img src="../.gitbook/assets/DCHUNKLOADER-PLUGIN.jpg" alt=""><figcaption></figcaption></figure>

#### ​

### DChunkLoader

[![image.png](https://builtbybit.com/attachments/image-png.1054608/?preset=fullr1)](https://dianelitostudios.gitbook.io/dchunkloader/)

#### ![✨](https://cdn.jsdelivr.net/joypixels/assets/6.5/png/unicode/64/2728.png)**Advanced Chunk Loader System**

Keep your chunks always active, even while you’re offline,

with a fully customizable **chunk loader system**.

### Where can I use DChunkLoader?

| <p><img src="https://builtbybit.com/attachments/image-4-png.1054604/?preset=fullr1" alt="image (4).png"></p> | <p><img src="https://builtbybit.com/attachments/image-3-png.1054605/?preset=fullr1" alt="image (3).png"></p> | <p><img src="https://builtbybit.com/attachments/image-2-png.1054606/?preset=fullr1" alt="image (2).png"></p> | <p><img src="https://builtbybit.com/attachments/image-1-png.1054607/?preset=fullr1" alt="image (1).png"></p> |
| ------------------------------------------------------------------------------------------------------------ | ------------------------------------------------------------------------------------------------------------ | ------------------------------------------------------------------------------------------------------------ | ------------------------------------------------------------------------------------------------------------ |

\


![🔹](https://cdn.jsdelivr.net/joypixels/assets/6.5/png/unicode/64/1f539.png)**Key Features:**

![📦](https://cdn.jsdelivr.net/joypixels/assets/6.5/png/unicode/64/1f4e6.png) **Loader Types:** Basic, Advanced, and Premium — each with different duration, range, and efficiency.

![⏳](https://cdn.jsdelivr.net/joypixels/assets/6.5/png/unicode/64/23f3.png) **Configurable Duration:** Loaders have a base time that can be easily renewed through the economy system.

![💰](https://cdn.jsdelivr.net/joypixels/assets/6.5/png/unicode/64/1f4b0.png) **Economy Integration:** Players are charged when placing, renewing, or upgrading their loaders (with fully adjustable prices).

![📈](https://cdn.jsdelivr.net/joypixels/assets/6.5/png/unicode/64/1f4c8.png) **Level System:** Upgrade your loader up to level 5 to increase the number of loaded chunks.

![🖼️](https://cdn.jsdelivr.net/joypixels/assets/6.5/png/unicode/64/1f5bc.png) **Dynamic Holograms:** Each loader displays real-time information such as owner, level, loaded chunks, and remaining time.

![👥](https://cdn.jsdelivr.net/joypixels/assets/6.5/png/unicode/64/1f465.png) **Permission Management:** Share your loader with other players through an intuitive GUI menu.

\


![⚙️](https://cdn.jsdelivr.net/joypixels/assets/6.5/png/unicode/64/2699.png)**Complete Administration Tools:**

Limit the maximum loaders per player.

Automatic saving at set intervals.

Auto-cleanup of expired loaders.

Detailed server statistics (total, active, inactive loaders, and server load).

\


***

\


![👉](https://cdn.jsdelivr.net/joypixels/assets/6.5/png/unicode/64/1f449.png) With this system, you can give your players a

**balanced and competitive way to keep their farms, bases, or systems running**,

without hurting overall server performance.

\


![descarga (2).png](https://builtbybit.com/attachments/descarga-2-png.1054576/?preset=fullr1)

Config.yml ![⚙️](https://cdn.jsdelivr.net/joypixels/assets/6.5/png/unicode/64/2699.png)\



```yaml
settings:
  max-blocks-per-player: 3 # Maximum number of chunk loaders per player
  auto-save-interval: 300 # Auto-save interval in seconds (0 to disable)

economy:
  deduct-on-place: true # Whether to charge players when placing chunk loaders
  place-price: 1000.0 # Base price for placing a chunk loader
  currency-symbol: "$" # Currency symbol to display in messages

blocks:
  basic: # Basic chunk loader
    material: CHEST # Block material (Bukkit Material name)
    display-name: "&a&lBasic Chunk Loader" # Display name for the item
    lore:
      - "&7Keep your chunks loaded even when offline!"
      - "&7Right-click to manage your chunk loader."
      - ""
      - "&eLevel: &f%level%"
      - "&eChunks: &f%chunks%"
    base-duration: 3600 # Base duration in seconds (1 hour = 3600 seconds)
    hologram:
      enabled: true
      lines:
        - "&a&lChunk Loader"
        - "&7Owner: &e%owner%"
        - "&7Level: &e%level%"
        - "&7Chunks: &e%chunks%"
        - "&7Time: &e%time%"
        - "&7Block: &e%block%"

    levels:
      1:
        chunks: 1
        upgrade-cost: 0
        renewal-cost: 100.0
      2:
        chunks: 4
        upgrade-cost: 2500.0
        renewal-cost: 200.0
      3:
        chunks: 9
        upgrade-cost: 5000.0
        renewal-cost: 350.0
      4:
        chunks: 16
        upgrade-cost: 10000.0
        renewal-cost: 500.0
      5:
        chunks: 25
        upgrade-cost: 20000.0
        renewal-cost: 750.0

  advanced:
    material: ENDER_CHEST
    display-name: "&b&lAdvanced Chunk Loader"
    lore:
      - "&7Advanced chunk loading technology!"
      - "&7Loads more chunks with better efficiency."
      - "&7Right-click to manage your chunk loader."
      - ""
      - "&eLevel: &f%level%"
      - "&eChunks: &f%chunks%"

    base-duration: 7200

    hologram:
      enabled: true
      lines:
        - "&b&lAdvanced Loader"
        - "&7Owner: &e%owner%"
        - "&7Level: &e%level%"
        - "&7Chunks: &e%chunks%"
        - "&7Time: &e%time%"

    levels:
      1:
        chunks: 4
        upgrade-cost: 0
        renewal-cost: 150.0
      2:
        chunks: 9
        upgrade-cost: 5000.0
        renewal-cost: 300.0
      3:
        chunks: 16
        upgrade-cost: 10000.0
        renewal-cost: 500.0
      4:
        chunks: 25
        upgrade-cost: 20000.0
        renewal-cost: 750.0
      5:
        chunks: 36
        upgrade-cost: 40000.0
        renewal-cost: 1000.0

  premium:
    material: BEACON
    display-name: "&6&lPremium Chunk Loader"
    lore:
      - "&7The ultimate chunk loading solution!"
      - "&7Maximum efficiency and coverage."
      - "&7Right-click to manage your chunk loader."
      - ""
      - "&eLevel: &f%level%"
      - "&eChunks: &f%chunks%"

    base-duration: 14400

    hologram:
      enabled: true
      lines:
        - "&6&lPremium Loader"
        - "&7Owner: &e%owner%"
        - "&7Level: &e%level%"
        - "&7Chunks: &e%chunks%"
        - "&7Time: &e%time%"

    levels:
      1:
        chunks: 9
        upgrade-cost: 0
        renewal-cost: 250.0
      2:
        chunks: 16
        upgrade-cost: 10000.0
        renewal-cost: 500.0
      3:
        chunks: 25
        upgrade-cost: 20000.0
        renewal-cost: 750.0
      4:
        chunks: 36
        upgrade-cost: 40000.0
        renewal-cost: 1000.0
      5:
        chunks: 49
        upgrade-cost: 80000.0
        renewal-cost: 1500.0

gui:
  main:
    title: "&8Chunk Loader Manager"
    size: 27
  permissions:
    title: "&8Chunk Loader Permissions"
    size: 27

messages:
  prefix: "&8[&aDChunkLoader&8] "
  no-permission: "&cYou don't have permission to do that!"
  player-not-found: "&cPlayer not found!"

  # Chunk loader placement
  place-success: "&aChunk loader placed successfully! &7(Type: &e%block%&7, Level: &e%level%&7)"
  place-failed: "&cFailed to place chunk loader!"
  max-blocks-reached: "&cYou've reached the maximum number of chunk loaders! &7(Max: &e%max%&7)"
  break-success: "&aChunk loader broken successfully!"

  # Chunk loader removal
  remove-success: "&aChunk loader removed successfully!"
  remove-confirm: "&eAre you sure you want to remove &c%owner%'s &echunk loader? Break it again to confirm."
  not-authorized: "&cYou are not authorized to interact with this chunk loader!"

  # Upgrades
  upgrade-success: "&aChunk loader upgraded to level &e%level%&a! &7(Chunks: &e%chunks%&7)"
  upgrade-failed: "&cFailed to upgrade chunk loader!"
  upgrade-max: "&cThis chunk loader is already at maximum level!"

  # Renewals
  renewal-success: "&aChunk loader renewed for &e%time%&a!"
  renewal-failed: "&cFailed to renew chunk loader!"

  # Economy
  not-enough-money: "&cYou don't have enough money! Required: &e%s"
  payment-success: "&aPayment successful! Amount: &e%s"

  # Expiration
  expired: "&cOne of your chunk loaders has expired!"
  login-expired-notification: "&cYou have &e%count% &cexpired chunk loaders!"
  login-stats: "&7You have &e%total% &7chunk loaders (&a%active% &7active)"

  # Commands
  give-success: "&aGave &e%player% &aa &e%block% &achunk loader (Level %level%)"
  give-received: "&aYou received a &e%block% &achunk loader (Level %level%)"
  invalid-block: "&cInvalid block type: &e%block%"
  invalid-level: "&cInvalid level for block type: &e%block%"

  # GUI Messages
  menu-title: "&8Chunk Loader Menu"

  # Permissions
  user-already-authorized: "&cPlayer %player% is already authorized."
  user-added-success: "&aPlayer %player% added successfully."
  user-removed-success: "&aUser successfully removed."
  user-removed-error: "There was an error trying to remove the user or it does not exist."
  chunk-loader-not-found: "&cError: Chunk loader not found."
  already-max-level: "&cThis chunk loader is already at maximum level!"
  upgrade-success-simple: "&aChunk loader upgraded successfully!"
  upgrade-failed-balance: "&cUpgrade failed! Check your balance and try again."

  # Administration messages
  admin-cleanup-success: "&aExpired chunk loaders cleaned up!"
  admin-unknown-action: "&cUnknown admin action: %action%"

  # Detailed statistics messages
  stats-header: "&a=== DChunkLoader Statistics ==="
  stats-total-loaders: "&7Total Loaders: &e%total%"
  stats-active-loaders: "&7Active Loaders: &a%active%"
  stats-inactive-loaders: "&7Inactive Loaders: &c%inactive%"
  stats-server-load: "&7Server Load: &b%load%%"

  # Permissions messages in GUI
  no-permission-modify-users: "&cYou do not have permissions to modify users."
  no-permission-add-users: "&cYou do not have permissions to add users."
  type-player-name: "&eType in chat the name of the player to add:"
  user-removed-from-permissions: "&cUser removed from permissions."

  # Status messages
  status-active: "&aActive"
  status-expired: "&cExpired"
  status-inactive: "&7Inactive"

  # Time formatting
  time-format:
    days: "%d days"
    hours: "%h hours"
    minutes: "%m minutes"
    seconds: "%s seconds"
    expired: "Expired"
```

#### [![👾](https://cdn.jsdelivr.net/joypixels/assets/6.5/png/unicode/64/1f47e.png) **Join our support discord:**](https://discord.gg/fcg6HDVKXy)

[![\[IMG\]](https://discord.com/api/guilds/1281038261359214624/widget.png?style=banner2)](https://discord.gg/fcg6HDVKXy)

\


\[MEDIA=youtube]oFNNWt1k-1U\[/MEDIA]

\


> \
>
>
> <img src="https://builtbybit.com/attachments/1750045921031-png.976406/?preset=fullr1" alt="1750045921031.png" data-size="original"><img src="https://builtbybit.com/attachments/1750045937999-png.976408/?preset=fullr1" alt="1750045937999.png" data-size="original">
>
> \
>
>
> <img src="https://builtbybit.com/attachments/2025-06-15_21-55-14-png.976411/?preset=fullr1" alt="2025-06-15_21.55.14.png" data-size="original">
>
> \
>
>
> <img src="https://builtbybit.com/attachments/2025-06-15_21-55-22-png.976413/?preset=fullr1" alt="2025-06-15_21.55.22.png" data-size="original"><img src="https://builtbybit.com/attachments/2025-06-15_21-55-22-png.976413/?preset=fullr1" alt="2025-06-15_21.55.22.png" data-size="original">
>
> <img src="https://builtbybit.com/attachments/2025-06-15_21-55-22-png.976413/?preset=fullr1" alt="2025-06-15_21.55.22.png" data-size="original">
>
> <img src="https://builtbybit.com/attachments/2025-06-15_21-55-22-png.976413/?preset=fullr1" alt="2025-06-15_21.55.22.png" data-size="original">

\


> TOS
>
> \
>
>
> By purchasing this plugin, you agree to the following:
>
> * You have read the plugin page in its entirety.
> * You are not allowed to redistribute this plugin in any way shape or form.
> * You are not allowed to decompile or attempt to reverse engineer the plugin in any way shape or form.
> * We are not responsible for any issues caused by external plugins.
> * Refunds will vary on a case-by-case basis; buyers may be only eligible for a partial refund depending on the situation. You are not eligible for a refund if 3 days have passed since you purchased the plugin. Refunds will not be given for any reason/disclaimer that is clearly listed on the plugin page. Refunds will not be given for any reason that has nothing to do with this resource (i.e. shutting down your server).
> * If you are to be given a refund, you agree to delete your review/not review the product
> * Transferring ownership of the plugin from one account to another is not allowed.
> * You are responsible for your copy of DCHUNKLOADER
> * . If your copy of DCHUNKLOADER is found to be redistributed, you will still be held responsible.

## Link to purchase: [https://builtbybit.com/resources/dchunkloader.69228/](https://builtbybit.com/resources/dchunkloader.69228/)

##
