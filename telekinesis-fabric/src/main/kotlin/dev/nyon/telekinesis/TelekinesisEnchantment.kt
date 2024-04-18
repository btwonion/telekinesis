package dev.nyon.telekinesis

import net.minecraft.network.chat.Component
import net.minecraft.network.chat.Style
import net.minecraft.tags.ItemTags
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.item.enchantment.Enchantment

/**
 * The telekinesis enchantment model
 */
class TelekinesisEnchantment : Enchantment(
    definition(
        ItemTags.DURABILITY_ENCHANTABLE,
        2,
        1,
        dynamicCost(25, 25),
        dynamicCost(75, 25),
        5,
        *EquipmentSlot.entries.toTypedArray()
    )
) {
    override fun getFullname(i: Int): Component =
        Component.translatable("enchantment.telekinesis.telekinesis.name").withStyle(Style.EMPTY.withColor(0xFFB64C))
}
