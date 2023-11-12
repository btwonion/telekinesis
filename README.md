# telekinesis

This mod/plugin adds the telekinesis enchantment, allowing you to instantly move drops into your inventory:

## Supported drops
- block drops
- mob drops
- exp drops
- shearing drops (fabric only)
- vehicle drops (fabric only)
- fishing drops (fabric only)

## Compatibility
The paper module of telekinesis is as of Minecraft version 1.20.2 discontinued cause of the lack of ability to
register the enchantment (I will figure a way out when I have time to, but it seems like this step is linked with way more reflection than before.)

## Configuration

The configuration file can be found in the client/server directory.
The file differs cause of the supported drops between fabric and paper environments.

-> mod configuration file: `/config/telekinesis.toml`
<br>
-> plugin configuration file: `/plugins/telekinesis.toml`

<details>
<summary>telekinesis.toml</summary>

<details>
<summary>paper config</summary>

```toml
# Uncomment the following values if you want to change them:
# 
# Decides whether telekinesis can be used without the enchantment.
onByDefault = false
# Uncomment this to block functionality for those who don't have the required permission.
# onByDefaultPermissionRequirement = 'permission'
# 
# Decides whether players should be required to sneak to use telekinesis.
onlyOnSneak = false
# Decides whether telekinesis can be used for block drops.
blockDrops = true
# blockDropsPermissionRequirement = 'permission'
# 
# Decides whether telekinesis can be used for exp drops.
expDrops = true
# expDropsPermissionRequirement = 'permission'
# 
# Decides whether telekinesis can be used for entity drops.
entityDrops = true
# entityDropsPermissionRequirement = 'permission'
# 
# Decides whether to add the enchantment to the game.
enchantment = true
```
</details>

<details>
<summary>fabric config</summary>

```toml
# Uncomment the following values if you want to change them:
# 
# Decides whether telekinesis can be used without the enchantment.
onByDefault = false
# Decides whether players should be required to sneak to use telekinesis.
onlyOnSneak = false
# Decides whether to add the enchantment to the game.
enchantment = true
# Decides whether telekinesis can be used for block drops.
blockDrops = true
# Decides whether telekinesis can be used for exp drops.
expDrops = true
# Decides whether telekinesis can be used for mob drops.
mobDrops = true
# Decides whether telekinesis can be used for vehicle drops.
vehicleDrops = true
# Decides whether telekinesis can be used for shearing drops.
shearingDrops = true
# Decides whether telekinesis can be used for fishing drops.
fishingDrops = true
```
</details>

</details>

### Other
⚠️ The development version is always the latest stable release of Minecraft. 
Therefore, new features will only be available for the current and following Minecraft versions.

If you need help with any of my mods, just join my [discord server](https://nyon.dev/discord)
