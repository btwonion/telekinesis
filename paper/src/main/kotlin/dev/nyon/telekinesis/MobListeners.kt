package dev.nyon.telekinesis

import dev.nyon.telekinesis.util.hasTelekinesis
import dev.nyon.telekinesis.util.listen
import org.bukkit.event.entity.EntityDeathEvent

fun initMobListeners() {
    mobDie
}

val mobDie = listen<EntityDeathEvent> {
    val killer = this.entity.killer ?: return@listen
    if (!killer.hasTelekinesis()) return@listen

    killer.inventory.addItem(*drops.toTypedArray()).forEach { (_, itemStack) ->
        drops.remove(itemStack)
    }

    killer.giveExp(droppedExp, true)
    droppedExp = 0
}