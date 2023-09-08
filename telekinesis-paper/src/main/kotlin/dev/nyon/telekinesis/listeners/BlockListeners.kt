package dev.nyon.telekinesis.listeners

import dev.nyon.telekinesis.TelekinesisPolicy
import dev.nyon.telekinesis.util.handleTelekinesis
import dev.nyon.telekinesis.util.listen
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockDropItemEvent

fun initBlockListeners() {
    dropEvent
    blockBreakEvent
}

val dropEvent = listen<BlockDropItemEvent> {
    player.handleTelekinesis(TelekinesisPolicy.BlockDrops, player.inventory.itemInMainHand) {
        val copy = mutableListOf(items).flatten()
        items.clear()
        copy.forEach {
            val result = inventory.addItem(it.itemStack)
            if (result.isNotEmpty()) items.add(it)
        }
    }
}

val blockBreakEvent = listen<BlockBreakEvent> {
    player.handleTelekinesis(TelekinesisPolicy.ExpDrops, null) {
        giveExp(expToDrop)
        expToDrop = 0
    }
}