package dev.nyon.telekinesis

import com.akuleshov7.ktoml.Toml
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import java.nio.file.Path
import kotlin.io.path.readText
import kotlin.io.path.writeText

@Serializable
data class TelekinesisConfig(
    val onByDefault: Boolean = false,
    val onlyOnSneak: Boolean = false,
    val enchantment: Boolean = true,
    val blockDrops: Boolean = true,
    val expDrops: Boolean = true,
    val mobDrops: Boolean = true,
    val entityDrops: Boolean = true,
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