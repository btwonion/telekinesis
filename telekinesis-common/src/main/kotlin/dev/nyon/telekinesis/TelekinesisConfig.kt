package dev.nyon.telekinesis

import com.akuleshov7.ktoml.Toml
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import java.nio.file.Path
import kotlin.io.path.readText
import kotlin.io.path.writeText

@Serializable
data class TelekinesisConfig(
    /*@TomlComments(
        lines = ["decides whether telekinesis should work without the enchantment or not"], inline = "false by default"
    )*/
    val onByDefault: Boolean = false,
    //@TomlComments(lines = ["decides if the enchantment is enabled"], inline = "true by default")
    val enchantment: Boolean = true,
    //@TomlComments(lines = ["decides if telekinesis is enabled for block drops"], inline = "true by default")
    val blockDrops: Boolean = true,
    //@TomlComments(lines = ["decides if telekinesis is enabled for exp drops"], inline = "true by default")
    val expDrops: Boolean = true,
    //@TomlComments(lines = ["decides if telekinesis is enabled for mob drops"], inline = "true by default")
    val mobDrops: Boolean = true,
    /*@TomlComments(
        lines = ["decides if telekinesis is enabled for entity drops such as boats and minecarts"],
        inline = "true by default"
    )*/
    val entityDrops: Boolean = true,
    //@TomlComments("decides if telekinesis is enabled for shearing drops", inline = "true by default")
    val shearingDrops: Boolean = true
)

var config: TelekinesisConfig = TelekinesisConfig()
lateinit var configPath: Path

fun saveConfig() = configPath.writeText(Toml.encodeToString(TelekinesisConfig.serializer(), config))

fun loadConfig() {
    if (configPath.readText().isEmpty()) {
        saveConfig()
        return
    }

    try {
        config = Toml.decodeFromString(configPath.readText())
    } catch (e: Exception) {
        saveConfig()
    }
}