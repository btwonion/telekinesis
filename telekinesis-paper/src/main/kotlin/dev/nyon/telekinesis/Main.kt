package dev.nyon.telekinesis

import net.minecraft.core.Registry
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.craftbukkit.v1_19_R2.CraftServer
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
        if (dev.nyon.telekinesis.config.enchantment) {
            val server = (Bukkit.getServer() as CraftServer).handle.server
            val enchantmentRegistry = server.registryAccess().registryOrThrow(Registries.ENCHANTMENT)
            Registry.register(
                enchantmentRegistry,
                ResourceLocation("telekinesis", "telekinesis"),
                telekinesis
            )
        }

        bukkitEnchantment = Enchantment.getByKey(NamespacedKey("telekinesis", "telekinesis"))
            ?: error("Telekinesis enchantment was not registered!")

        initBlockListeners()
        initMobListeners()
    }

}


val Plugin by lazy { Main.INSTANCE }