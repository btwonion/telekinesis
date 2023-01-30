package dev.nyon.telekinesis.util

import dev.nyon.telekinesis.bukkitEnchantment
import org.bukkit.entity.Player

fun Player.hasTelekinesis(): Boolean = this.itemInUse?.containsEnchantment(bukkitEnchantment) == true