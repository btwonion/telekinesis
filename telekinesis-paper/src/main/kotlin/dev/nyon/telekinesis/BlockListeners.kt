package dev.nyon.telekinesis

import dev.nyon.telekinesis.util.hasTelekinesis
import dev.nyon.telekinesis.util.listen
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockDropItemEvent

fun initBlockListeners() {
    dropEvent
    blockBreakEvent
}

val dropEvent = listen<BlockDropItemEvent> {
    if (!player.hasTelekinesis()) return@listen

    player.inventory.addItem(*items.map { it.itemStack }.toTypedArray()).forEach { (_, itemStack) ->
        player.world.dropItemNaturally(block.location, itemStack)
    }

    items.clear()
}

val blockBreakEvent = listen<BlockBreakEvent> {
    if (!player.hasTelekinesis()) return@listen

    if (!isCancelled && expToDrop != 0) {
        player.giveExp(expToDrop, true)
        expToDrop = 0
    }
}