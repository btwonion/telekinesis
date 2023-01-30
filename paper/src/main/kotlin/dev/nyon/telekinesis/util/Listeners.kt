package dev.nyon.telekinesis.util

import dev.nyon.telekinesis.Plugin
import org.bukkit.Bukkit
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener

abstract class SingleListener<T : Event> : Listener {
    abstract fun onEvent(event: T)
}

inline fun <reified T : Event> listen(crossinline event: T.() -> Unit) {
    val listener = object : SingleListener<T>() {
        override fun onEvent(event: T) = event(event)
    }
    Bukkit.getPluginManager()
        .registerEvent(
            T::class.java,
            listener,
            EventPriority.NORMAL,
            { _, event -> listener.onEvent(event as T) },
            Plugin
        )
}