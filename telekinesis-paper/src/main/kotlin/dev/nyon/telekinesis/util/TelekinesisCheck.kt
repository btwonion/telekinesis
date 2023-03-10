package dev.nyon.telekinesis.util

import dev.nyon.telekinesis.bukkitEnchantment
import org.bukkit.entity.Player

fun Player.hasTelekinesis(): Boolean = listOf(inventory.itemInOffHand, inventory.itemInMainHand).any {
    it.hasItemMeta() && it.itemMeta.enchants.contains(bukkitEnchantment)
}