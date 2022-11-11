package dev.nyon.telekinesis

import dev.nyon.telekinesis.config.config
import dev.nyon.telekinesis.config.loadConfig
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceLocation

val telekinesis = TelekinesisEnchantment()

class Telekinesis {
    fun init() {
        loadConfig()
        if (config.enchantment) Registry.register(Registry.ENCHANTMENT, ResourceLocation("telekinesis", "telekinesis"), telekinesis)
    }
}