@file:Suppress("unused")

package dev.nyon.telekinesis

import net.fabricmc.loader.api.FabricLoader
import net.minecraft.core.Registry
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.MinecraftServer
import kotlin.io.path.createFile
import kotlin.io.path.exists

lateinit var server: MinecraftServer

fun init() {
    configPath = FabricLoader.getInstance().configDir.toAbsolutePath().resolve("telekinesis.toml")
        .also { if (!it.exists()) it.createFile() }
    Telekinesis.init()
    if (config.enchantment) {
        val enchantmentRegistry = server.registryAccess().registryOrThrow(Registries.ENCHANTMENT)
        Registry.register(
            enchantmentRegistry,
            ResourceLocation("telekinesis", "telekinesis"),
            telekinesis
        )
    }
}