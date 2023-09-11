package dev.nyon.telekinesis

import com.akuleshov7.ktoml.Toml
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import java.nio.file.Path
import kotlin.io.path.readText
import kotlin.io.path.writeText

@Serializable
data class TelekinesisConfig(
    var onByDefault: Boolean = false,
    var onlyOnSneak: Boolean = false,
    var enchantment: Boolean = true,
    var blockDrops: Boolean = true,
    var expDrops: Boolean = true,
    var mobDrops: Boolean = true,
    var entityDrops: Boolean = true,
    var shearingDrops: Boolean = true,
    var fishingDrops: Boolean = true
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