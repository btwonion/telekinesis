# telekinesis

Adds auto-pickup functionality to the player. Also know as telekinesis from Hypixel Skyblock.

## Functionality

Check out the [Gallery](https://modrinth.com/plugin/telekinesis/gallery) for a preview.

With this mod you can automatically pickup drops, including experience, from mobs, blocks and entities.

## Can I use the functionality without the enchantment?

Yes, in the [config](#Configuration) you can change the `needEnchantment` option to `false`. If you want to only apply this 
functionality while sneaking, you can also enable `needSneak` 

## Where can I find the enchantment?

You can trade the enchantment with villagers, enchant it in an enchanting table, or you can find it in treasures
enchanting on tools.

## Configuration

The configuration file can be found in the client/server directory.
-> `/config/telekinesis.json`

<details>
<summary>telekinesis.json</summary>

```json5
{
    "version": 1, // For migration purposes only, just ignore this.
    "config": {
        "needEnchantment": true, // Defines, whether telekinesis should without or with the enchantment on the tool.
        "needSneak": false, // Defines. whether the player should have to sneak in order to use telekinesis.
        "expAllowed": true, // Enables the use of telekinesis for exp drops.
        "itemsAllowed": true // Enables the use of telekinesis for item drops.
    }
}
```

</details>

### Other

If you need help with any of my mods, join my [discord server](https://nyon.dev/discord).

#### Paper Compatibility

The paper module of telekinesis is as of Minecraft version 1.20.2 discontinued cause of the lack of ability to
register the enchantment.
