package dev.nyon.telekinesis

import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation

val telekinesis = TelekinesisEnchantment()

object Telekinesis {
    fun init() {
        loadConfig()
        if (config.enchantment) Registry.register(
            BuiltInRegistries.ENCHANTMENT,
            ResourceLocation("telekinesis", "telekinesis"),
            telekinesis
        )
    }
}