package dev.nyon.telekinesis

import net.minecraft.network.chat.Component
import net.minecraft.network.chat.Style
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.MobType
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.EnchantmentCategory


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