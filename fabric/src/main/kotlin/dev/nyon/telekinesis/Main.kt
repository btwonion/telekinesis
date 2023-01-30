@file:Suppress("unused")

package dev.nyon.telekinesis

import dev.nyon.telekinesis.config.configPath
import net.fabricmc.loader.api.FabricLoader
import kotlin.io.path.createFile
import kotlin.io.path.exists

fun init() {
    configPath = FabricLoader.getInstance().configDir.toAbsolutePath().resolve("telekinesis.toml")
        .also { if (!it.exists()) it.createFile() }
    Telekinesis.init()
}