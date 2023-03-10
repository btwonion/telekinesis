package dev.nyon.telekinesis

import io.papermc.paper.enchantments.EnchantmentRarity
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.enchantments.EnchantmentTarget
import org.bukkit.entity.EntityCategory
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack

class BukkitTelekinesis : Enchantment(NamespacedKey("telekinesis", "telekinesis")) {
    override fun translationKey(): String = "telekinesis.telekinesis"

    override fun getName(): String = "Telekinesis"

    override fun getMaxLevel(): Int = 1

    override fun getStartLevel(): Int = 1

    override fun getItemTarget(): EnchantmentTarget = EnchantmentTarget.BREAKABLE

    override fun isTreasure(): Boolean = false

    override fun isCursed(): Boolean = false

    override fun conflictsWith(other: Enchantment): Boolean = false

    override fun canEnchantItem(item: ItemStack): Boolean = true

    override fun displayName(level: Int): Component = Component.text("Telekinesis").color(TextColor.color(0xFFB64C))

    override fun isTradeable(): Boolean = true

    override fun isDiscoverable(): Boolean = true

    override fun getRarity(): EnchantmentRarity = EnchantmentRarity.RARE

    override fun getDamageIncrease(level: Int, entityCategory: EntityCategory): Float = 0.0f

    override fun getActiveSlots(): MutableSet<EquipmentSlot> = mutableSetOf(EquipmentSlot.OFF_HAND, EquipmentSlot.HAND)
}