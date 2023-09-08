package dev.nyon.telekinesis.util

import dev.nyon.telekinesis.TelekinesisPolicy
import dev.nyon.telekinesis.bukkitEnchantment
import dev.nyon.telekinesis.config
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

fun ItemStack.hasTelekinesis(): Boolean = hasItemMeta() && itemMeta.enchants.contains(bukkitEnchantment)

fun Player.handleTelekinesis(policy: TelekinesisPolicy, itemStack: ItemStack?, callback: Player.() -> Unit): Boolean {
    if (!policy.isEnabled()) return false
    if (!meetCondition(policy, itemStack)) return false

    callback()
    return true
}

private fun Player.meetCondition(policy: TelekinesisPolicy, itemStack: ItemStack?): Boolean {
    var conditionsMet: Boolean

    val isEnabledByDefault = config.onByDefault

    val hasArmorTelekinesis: Boolean = inventory.armorContents.all { it?.hasTelekinesis() == true }
    val hasMainHandTelekinesis =
        itemStack != null && itemStack.hasTelekinesis() || inventory.itemInMainHand.hasTelekinesis()
    val hasOffHandTelekinesis = inventory.itemInOffHand.hasTelekinesis()

    conditionsMet = if (isEnabledByDefault) true else when (policy) {
        TelekinesisPolicy.ExpDrops -> hasArmorTelekinesis || hasMainHandTelekinesis || hasOffHandTelekinesis
        TelekinesisPolicy.EntityDrops -> hasMainHandTelekinesis || hasOffHandTelekinesis
        TelekinesisPolicy.BlockDrops -> hasMainHandTelekinesis
    }

    if (config.onlyOnSneak && !isSneaking) conditionsMet = false

    return conditionsMet
}