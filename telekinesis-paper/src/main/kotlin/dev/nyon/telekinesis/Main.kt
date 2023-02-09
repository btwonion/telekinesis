package dev.nyon.telekinesis

import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.plugin.java.JavaPlugin
import kotlin.io.path.createFile
import kotlin.io.path.exists

lateinit var bukkitEnchantment: Enchantment

class Main : JavaPlugin() {
    companion object {
        lateinit var INSTANCE: Main; private set
    }

    override fun onLoad() {
        configPath = Bukkit.getPluginsFolder().toPath().resolve("telekinesis.toml")
            .also { if (!it.exists()) it.createFile() }
        Telekinesis.init()

        bukkitEnchantment = Enchantment.getByKey(NamespacedKey("telekinesis", "telekinesis"))
            ?: error("Telekinesis enchantment was not registered!")

        initBlockListeners()
        initMobListeners()
    }

}


val Plugin by lazy { Main.INSTANCE }