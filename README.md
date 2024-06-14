# telekinesis

This mod/plugin adds the telekinesis enchantment, allowing you to instantly move drops into your inventory.

## Configuration

The configuration file can be found in the client/server directory.
-> configuration file: `/config/telekinesis.json`

<details>
<summary>telekinesis.toml</summary>

```json
{
    "version": 1, // Only for migration purposes, just ignore this.
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

Currently supported versions are: 1.20.1, 1.20.4, 1.20.6 and 1.21. This can change in the future!

If you need help with any of my mods, just join my [discord server](https://nyon.dev/discord)

#### Paper Compatibility

The paper module of telekinesis is as of Minecraft version 1.20.2 discontinued cause of the lack of ability to
register the enchantment (When Paper's registry modification api is merged I will continue.)
