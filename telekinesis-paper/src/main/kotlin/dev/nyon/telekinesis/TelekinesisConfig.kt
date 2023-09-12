package dev.nyon.telekinesis

import com.akuleshov7.ktoml.Toml
import com.akuleshov7.ktoml.TomlInputConfig
import com.akuleshov7.ktoml.annotations.TomlComments
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import java.nio.file.Path
import kotlin.io.path.notExists
import kotlin.io.path.readText
import kotlin.io.path.writeText

@Serializable
data class TelekinesisConfig(
    @TomlComments(
        "Uncomment the following values if you want to change them:",
        "",
        "Decides whether telekinesis can be used without the enchantment."
    )
    var onByDefault: Boolean = false,
    var onByDefaultPermissionRequirement: String? = null,
    @TomlComments(
        "Uncomment this to block functionality for those who don't have the required permission.",
        "onByDefaultPermissionRequirement = 'permission'",
        "",
        "Decides whether players should be required to sneak to use telekinesis."
    )
    var onlyOnSneak: Boolean = false,
    @TomlComments(
        "Decides whether telekinesis can be used for block drops."
    )
    var blockDrops: Boolean = true,
    var blockDropsPermissionRequirement: String? = null,
    @TomlComments(
        "blockDropsPermissionRequirement = 'permission'",
        "",
        "Decides whether telekinesis can be used for exp drops."
    )
    var expDrops: Boolean = true,
    var expDropsPermissionRequirement: String? = null,
    @TomlComments(
        "expDropsPermissionRequirement = 'permission'",
        "",
        "Decides whether telekinesis can be used for entity drops."
    )
    var entityDrops: Boolean = true,
    var entityDropsPermissionRequirement: String? = null,
    @TomlComments(
        "entityDropsPermissionRequirement = 'permission'",
        "",
        "Decides whether to add the enchantment to the game."
    )
    var enchantment: Boolean = true,
)

var config: TelekinesisConfig = TelekinesisConfig()
lateinit var configPath: Path
val toml = Toml(
    inputConfig = TomlInputConfig(ignoreUnknownNames = true)
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
