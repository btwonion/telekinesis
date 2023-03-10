@file:Suppress("unused")

package dev.nyon.telekinesis

import net.fabricmc.loader.api.FabricLoader
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.MinecraftServer
import kotlin.io.path.createFile
import kotlin.io.path.exists

var server: MinecraftServer? = null

fun init() {
    configPath = FabricLoader.getInstance().configDir.toAbsolutePath().resolve("telekinesis.toml")
        .also { if (!it.exists()) it.createFile() }
    Telekinesis.init()
    if (config.enchantment)
        Registry.register(BuiltInRegistries.ENCHANTMENT, ResourceLocation("telekinesis", "telekinesis"), telekinesis)
}