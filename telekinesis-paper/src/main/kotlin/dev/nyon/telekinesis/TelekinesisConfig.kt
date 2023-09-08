package dev.nyon.telekinesis

import com.akuleshov7.ktoml.Toml
import com.akuleshov7.ktoml.TomlInputConfig
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
    var entityDrops: Boolean = true
)

var config: TelekinesisConfig = TelekinesisConfig()
lateinit var configPath: Path
val toml = Toml(inputConfig = TomlInputConfig(ignoreUnknownNames = true))

fun saveConfig() = configPath.writeText(toml.encodeToString(TelekinesisConfig.serializer(), config))

fun loadConfig() {
    if (configPath.readText().isEmpty()) {
        saveConfig()
        return
    }

    try {
        config = toml.decodeFromString(configPath.readText())
    } catch (e: Exception) {
        saveConfig()
    }
}