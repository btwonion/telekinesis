package dev.nyon.telekinesis

import dev.nyon.telekinesis.config.config
import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.EnchantmentHelper
import org.apache.commons.lang3.mutable.MutableInt

object DropEvent {
    val event: Event<DropEventConsumer> = EventFactory.createArrayBacked(DropEventConsumer::class.java) { listeners ->
        DropEventConsumer { items, exp, player, tool ->
            listeners.forEach {
                it(items, exp, player, tool)
            }
        }
    }

    @Suppress("unused")
    private val listener = event.register { items, exp, player, tool ->
        if (config.needSneak && !player.isCrouching) return@register
        if (config.needEnchantment && !EnchantmentHelper.hasTag(tool, tagKey)) return@register

        if (config.itemsAllowed) items.removeIf(player::addItem)
        if (config.expAllowed) {
            player.giveExperiencePoints(exp.value)
            exp.setValue(0)
        }
    }
}

fun interface DropEventConsumer {
    operator fun invoke(items: MutableList<ItemStack>, exp: MutableInt, player: ServerPlayer, tool: ItemStack)
}