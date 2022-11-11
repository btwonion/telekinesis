package dev.nyon.telekinesis.config

import com.akuleshov7.ktoml.Toml
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import net.fabricmc.loader.api.FabricLoader
import kotlin.io.path.createFile
import kotlin.io.path.exists
import kotlin.io.path.readText
import kotlin.io.path.writeText

@Serializable
data class Config(/*@TomlComments(
        "decides whether telekinesis should work without the enchantment or not", "false by default"
    )*/ val onByDefault: Boolean = false,/*@TomlComments("decides if the enchantment is enabled", "true by default")*/
                  val enchantment: Boolean = true,/*@TomlComments("decides if telekinesis is enabled for block drops")*/
                  val blockDrops: Boolean = true,/*@TomlComments("decides if telekinesis is enabled for exp drops")*/
                  val expDrops: Boolean = true,/*@TomlComments("decides if telekinesis is enabled for mob drops")*/
                  val mobDrops: Boolean = true,/*@TomlComments("decides if telekinesis is enabled for entity drops such as boats and minecarts")*/
                  val entityDrops: Boolean = true,/*@TomlComments("decides if telekinesis is enabled for shearing drops")*/
                  val shearingDrops: Boolean = true
)

var config = Config()
private val path = FabricLoader.getInstance().configDir.toAbsolutePath().resolve("telekinesis.toml")
    .also { if (!it.exists()) it.createFile() }

fun saveConfig() = path.writeText(Toml.encodeToString(config))

fun loadConfig() {
    if (path.readText().isEmpty()) {
        saveConfig()
        return
    }

    config = Toml.decodeFromString(path.readText())
}