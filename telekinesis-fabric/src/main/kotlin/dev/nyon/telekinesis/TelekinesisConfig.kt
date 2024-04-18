package dev.nyon.telekinesis

import com.akuleshov7.ktoml.Toml
import com.akuleshov7.ktoml.annotations.TomlComments
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import net.fabricmc.loader.api.FabricLoader
import java.nio.file.Path
import kotlin.io.path.createFile
import kotlin.io.path.exists
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
    @TomlComments(
        "Decides whether players should be required to sneak to use telekinesis."
    )
    var onlyOnSneak: Boolean = false,
    @TomlComments(
        "Decides whether to add the enchantment to the game."
    )
    var enchantment: Boolean = true,
    @TomlComments(
        "Decides whether telekinesis can be used for block drops."
    )
    var blockDrops: Boolean = true,
    @TomlComments(
        "Decides whether telekinesis can be used for exp drops."
    )
    var expDrops: Boolean = true,
    @TomlComments(
        "Decides whether telekinesis can be used for mob drops."
    )
    var mobDrops: Boolean = true,
    @TomlComments(
        "Decides whether telekinesis can be used for vehicle drops."
    )
    var vehicleDrops: Boolean = true,
    @TomlComments(
        "Decides whether telekinesis can be used for shearing drops."
    )
    var shearingDrops: Boolean = true,
    @TomlComments(
        "Decides whether telekinesis can be used for fishing drops."
    )
    var fishingDrops: Boolean = true
)

var config: TelekinesisConfig = TelekinesisConfig()
lateinit var configPath: Path

fun saveConfig() = configPath.writeText(Toml.encodeToString(TelekinesisConfig.serializer(), config))

fun loadConfig() {
    configPath =
        FabricLoader.getInstance().configDir.toAbsolutePath().resolve("telekinesis.toml")
            .also { if (!it.exists()) it.createFile() }

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
