package dev.nyon.telekinesis

import com.akuleshov7.ktoml.Toml
import com.akuleshov7.ktoml.TomlInputConfig
import com.akuleshov7.ktoml.TomlOutputConfig
import com.akuleshov7.ktoml.annotations.TomlComments
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import java.nio.file.Path
import kotlin.io.path.notExists
import kotlin.io.path.readText
import kotlin.io.path.writeText

@Serializable
data class TelekinesisConfig(
    var onByDefault: Boolean = false,
    @TomlComments(
        "Uncomment this by removing '# ' to block functionality for those who don't have the required permission.",
        "onByDefaultPermissionRequirement = 'permission'"
    ) var onByDefaultPermissionRequirement: String? = null,
    var onlyOnSneak: Boolean = false,
    var enchantment: Boolean = true,
    var blockDrops: Boolean = true,
    @TomlComments(
        "blockDropsPermissionRequirement = 'permission'"
    ) var blockDropsPermissionRequirement: String? = null,
    var expDrops: Boolean = true,
    @TomlComments(
        "expDropsPermissionRequirement = 'permission'"
    ) var expDropsPermissionRequirement: String? = null,
    var entityDrops: Boolean = true,
    @TomlComments(
        "entityDropsPermissionRequirement = 'permission'"
    ) var entityDropsPermissionRequirement: String? = null
)

var config: TelekinesisConfig = TelekinesisConfig()
lateinit var configPath: Path
val toml = Toml(
    inputConfig = TomlInputConfig(ignoreUnknownNames = true), outputConfig = TomlOutputConfig(ignoreNullValues = false)
)

fun saveConfig() = configPath.writeText(toml.encodeToString(TelekinesisConfig.serializer(), config))

fun loadConfig() {
    if (configPath.notExists() || configPath.readText().isEmpty()) {
        saveConfig()
        return
    }

    try {
        config = toml.decodeFromString(configPath.readText())
    } catch (e: Exception) {
        saveConfig()
    }
}
