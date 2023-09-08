package dev.nyon.telekinesis

import io.papermc.paper.enchantments.EnchantmentRarity
import net.kyori.adventure.text.format.TextColor
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.Style
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.MobType
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.EnchantmentCategory
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.EnchantmentTarget
import org.bukkit.entity.EntityCategory
import org.bukkit.inventory.ItemStack

/**
 * The telekinesis enchantment model
 */
class TelekinesisEnchantment : Enchantment(
    Rarity.RARE, EnchantmentCategory.BREAKABLE, listOf(EquipmentSlot.OFFHAND, EquipmentSlot.MAINHAND).toTypedArray()
) {

    override fun getMinLevel(): Int = 1
    override fun getMaxLevel(): Int = 1

    override fun getMinCost(i: Int): Int = 30
    override fun getMaxCost(i: Int): Int = 30

    override fun getDamageProtection(i: Int, damageSource: DamageSource): Int = 0
    override fun getDamageBonus(i: Int, mobType: MobType): Float = 0F

    override fun getFullname(i: Int): Component =
        Component.literal("Telekinesis").withStyle(Style.EMPTY.withColor(0xFFB64C))
}

class BukkitTelekinesis : org.bukkit.enchantments.Enchantment(NamespacedKey("telekinesis", "telekinesis")) {
    override fun translationKey(): String = "telekinesis.telekinesis"

    @Deprecated("Deprecated in Java", ReplaceWith("\"Telekinesis\""))
    override fun getName(): String = "Telekinesis"

    override fun getMaxLevel(): Int = 1

    override fun getStartLevel(): Int = 1

    override fun getItemTarget(): EnchantmentTarget = EnchantmentTarget.BREAKABLE

    override fun isTreasure(): Boolean = false

    override fun isCursed(): Boolean = false

    override fun conflictsWith(other: org.bukkit.enchantments.Enchantment): Boolean = false

    override fun canEnchantItem(item: ItemStack): Boolean = true

    override fun displayName(level: Int): net.kyori.adventure.text.Component = net.kyori.adventure.text.Component.text("Telekinesis").color(
        TextColor.color(0xFFB64C))

    override fun isTradeable(): Boolean = true

    override fun isDiscoverable(): Boolean = true

    override fun getRarity(): EnchantmentRarity = EnchantmentRarity.RARE

    override fun getDamageIncrease(level: Int, entityCategory: EntityCategory): Float = 0.0f

    override fun getActiveSlots(): MutableSet<org.bukkit.inventory.EquipmentSlot> = mutableSetOf(org.bukkit.inventory.EquipmentSlot.OFF_HAND, org.bukkit.inventory.EquipmentSlot.HAND)
}