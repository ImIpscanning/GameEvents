# config.yml

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

  # GUI Item names and descriptions
  gui:
    upgrade:
      name: "&aUpgrade Chunk Loader"
      lore:
        - "&7Click to upgrade this chunk loader"
        - "&7to the next level."
        - ""
        - "&7Current Level: &e%level%"
        - "&7Next Level: &e%next_level%"
        - "&7Cost: &e%cost%"

    renew:
      name: "&bRenew Time"
      lore:
        - "&7Click to renew the time on"
        - "&7this chunk loader."
        - ""
        - "&7Cost per hour: &e%cost%"

    info:
      name: "&eChunk Loader Info"
      lore:
        - "&7Owner: &e%owner%"
        - "&7Level: &e%level%"
        - "&7Chunks: &e%chunks%"
        - "&7Status: %status%"
        - "&7Time Left: &e%time%"

    permissions:
      name: "&dManage Permissions"
      lore:
        - "&7Click to manage who can"
        - "&7access this chunk loader."

    remove:
      name: "&cRemove Chunk Loader"
      lore:
        - "&7Click to remove this"
        - "&7chunk loader permanently."
        - ""
        - "&c&lWARNING: This cannot be undone!"

    close:
      name: "&cClose"
      lore:
        - "&7Click to close this menu."

    back:
      name: "&7Back"
      lore:
        - "&7Click to go back."

    confirm:
      name: "&aConfirm"
      lore:
        - "&7Click to confirm the action."

    cancel:
      name: "&cCancel"
      lore:
        - "&7Click to cancel the action."
```
