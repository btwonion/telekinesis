@file:Suppress("unused")

package dev.nyon.telekinesis

import net.minecraft.server.MinecraftServer

var server: MinecraftServer? = null
lateinit var telekinesis: TelekinesisEnchantment

fun init() {
    loadConfig()
}
