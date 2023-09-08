package dev.nyon.telekinesis.listeners

import dev.nyon.telekinesis.TelekinesisPolicy
import dev.nyon.telekinesis.util.handleTelekinesis
import dev.nyon.telekinesis.util.listen
import org.bukkit.event.entity.EntityDeathEvent

fun initMobListeners() {
    mobDie
    mobExpDie
}

val mobDie = listen<EntityDeathEvent> {
    println(entityType.name)
    val killer = this.entity.killer ?: return@listen

    killer.handleTelekinesis(TelekinesisPolicy.EntityDrops, null) {
        val copy = mutableListOf(drops).flatten()
        drops.clear()
        copy.forEach {
            val result = inventory.addItem(it)
            if (result.isNotEmpty()) drops.add(it)
        }
    }
}

val mobExpDie = listen<EntityDeathEvent> {
    val killer = this.entity.killer ?: return@listen

    killer.handleTelekinesis(TelekinesisPolicy.ExpDrops, null) {
        giveExp(droppedExp, true)
        droppedExp = 0
    }
}