package dev.nyon.telekinesis

import com.akuleshov7.ktoml.Toml
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import java.nio.file.Path
import kotlin.io.path.readText
import kotlin.io.path.writeText

@Serializable
data class TelekinesisConfig(
    /*@TomlComments(
        lines = ["decides whether telekinesis should work without the enchantment or not"],
        inline = "false by default"
    )*/
    val onByDefault: Boolean = false,
    //@TomlComments(lines = ["decides if the enchantment is enabled"], inline = "true by default")
    val enchantment: Boolean = true,
    //@TomlComments(lines = ["decides if telekinesis is enabled for block drops"])
    val blockDrops: Boolean = true,
    //@TomlComments(lines = ["decides if telekinesis is enabled for exp drops"])
    val expDrops: Boolean = true,
    //@TomlComments(lines = ["decides if telekinesis is enabled for mob drops"])
    val mobDrops: Boolean = true,
    //@TomlComments(lines = ["decides if telekinesis is enabled for entity drops such as boats and minecarts"])
    val entityDrops: Boolean = true,
    //@TomlComments(lines = ["decides if telekinesis is enabled for shearing drops"])
    val shearingDrops: Boolean = true
)

var config = TelekinesisConfig()
lateinit var configPath: Path

fun saveConfig() = configPath.writeText(Toml.encodeToString(config))

fun loadConfig() {
    if (configPath.readText().isEmpty()) {
        saveConfig()
        return
    }

    config = Toml.decodeFromString(configPath.readText())
}