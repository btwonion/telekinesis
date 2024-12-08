@file:Suppress("unused")

package dev.nyon.magnetic

import dev.nyon.konfig.config.config
import dev.nyon.konfig.config.loadConfig
import dev.nyon.magnetic.config.Config
import net.fabricmc.loader.api.FabricLoader
import dev.nyon.magnetic.config.config as internalConfig

//? if <=1.20.6
/*lateinit var magnetic: MagneticEnchantment*/

fun init() {
    config(FabricLoader.getInstance().configDir.resolve("magnetic.json"), 1, Config()) { _, _ -> null }
    internalConfig = loadConfig()
    DropEvent
}
