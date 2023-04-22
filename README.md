# telekinesis

This mod/plugin adds the telekinesis enchantment, which allows you to instantly move drops into your inventory, to:

- block drops
- mob drops
- exp drops
- entity drops (boats, minecarts)
- shearing drops

The mod requires [Fabric API](https://github.com/fabricmc/fabric)
and [Fabric Language Kotlin](https://github.com/fabricmc/fabric-language-kotlin)

## Configuration

The configuration file can be found in the client/server directory:

-> mod configuration file: `/config/telekinesis.toml`
<br>
-> plugin configuration file: `/plugins/telekinesis.toml`

<details>
<summary>telekinesis.toml</summary>

```toml
onByDefault = false # should telekinesis work without the enchantment?
enchantment = true # should the telekinesis enchantment be enabled?
blockDrops = true # should telekinesis work for block drops?
shearingDrops = true # should telekinesis work for shearing drops?
mobDrops = true # should telekinesis work for mob drops?
entityDrops = true # should telekinesis work for entity drops (boat, minecarts)?
expDrops = true # should telekinesis work for exp drops?
```

</details>

### Other
If you need help with any of my mods just join my [discord server](https://nyon.dev/discord)
