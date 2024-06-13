@file:Suppress("unused")

package dev.nyon.telekinesis

import dev.nyon.konfig.config.config
import dev.nyon.konfig.config.loadConfig
import dev.nyon.telekinesis.config.Config
import net.fabricmc.loader.api.FabricLoader
import dev.nyon.telekinesis.config.config as internalConfig

/*? if <=1.20.6 {*//*
lateinit var telekinesis: TelekinesisEnchantment
*//*?}*/

fun init() {
    config(FabricLoader.getInstance().configDir.resolve("telekinesis.json"), 1, Config()) { _, _ -> null }
    internalConfig = loadConfig()
    DropEvent
}
