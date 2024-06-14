package dev.nyon.telekinesis

import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.entity.EquipmentSlot
/*? if >=1.21 {*/
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey

val telekinesisEffectId: TagKey<Enchantment> = TagKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath("telekinesis", "auto_move"))
val telekinesisEnchantmentId: ResourceLocation = ResourceLocation.fromNamespaceAndPath("telekinesis", "telekinesis")
/*?} elif <1.21 && >=1.20.5 {*/
/*import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags
*//*?} elif <=1.20.4 {*/
/*import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.MobType
import net.minecraft.world.item.enchantment.EnchantmentCategory
*//*?}*/


/*? if =1.20.6 {*/
/*class TelekinesisEnchantment : Enchantment(
    definition(
        ConventionalItemTags.TOOLS,
        2,
        1,
        dynamicCost(25, 25),
        dynamicCost(75, 75),
        7,
        EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND
    )
) {
    override fun getFullname(i: Int): Component =
        Component.translatable("enchantment.telekinesis.telekinesis.name").withStyle(ChatFormatting.GRAY)
}
*//*?} elif <1.20.5 {*/
/*class TelekinesisEnchantment : Enchantment(
    Rarity.RARE, EnchantmentCategory.BREAKABLE, listOf(EquipmentSlot.OFFHAND, EquipmentSlot.MAINHAND).toTypedArray()
) {
    override fun getMinLevel(): Int = 1
    override fun getMaxLevel(): Int = 1

    override fun getMinCost(i: Int): Int = 25
    override fun getMaxCost(i: Int): Int = 75

    override fun getDamageProtection(i: Int, damageSource: DamageSource): Int = 0
    override fun getDamageBonus(i: Int, mobType: MobType): Float = 0F

    override fun getFullname(i: Int): Component =
        Component.translatable("enchantment.telekinesis.telekinesis.name").withStyle(ChatFormatting.GRAY)
}
*//*?}*/