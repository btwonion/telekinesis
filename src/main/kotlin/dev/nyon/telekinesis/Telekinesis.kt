package dev.nyon.telekinesis

import net.minecraft.core.Registry
import net.minecraft.resources.ResourceLocation

val telekinesis = TelekinesisEnchantment()

class Telekinesis {
    fun init() {
        Registry.register(Registry.ENCHANTMENT, ResourceLocation("telekinesis", "telekinesis"), telekinesis)
    }
}