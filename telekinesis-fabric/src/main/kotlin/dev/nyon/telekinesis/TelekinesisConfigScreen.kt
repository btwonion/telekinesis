package dev.nyon.telekinesis

import dev.isxander.yacl3.api.ConfigCategory
import dev.isxander.yacl3.api.Option
import dev.isxander.yacl3.api.OptionDescription
import dev.isxander.yacl3.api.YetAnotherConfigLib
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component

fun generateConfigScreen(parent: Screen? = null): Screen {
    val configScreenBuilder = YetAnotherConfigLib.createBuilder()
    configScreenBuilder.title(Component.literal("telekinesis"))
    configScreenBuilder.appendOptionCategory()
    configScreenBuilder.save { saveConfig() }
    val configScreen = configScreenBuilder.build()
    return configScreen.generateScreen(parent)
}

fun YetAnotherConfigLib.Builder.appendOptionCategory() {
    this.category(
        ConfigCategory.createBuilder()
            .name(Component.literal("General"))
            .option(
                Option.createBuilder<Boolean>()
                    .name(Component.literal("On by default"))
                    .description(OptionDescription.of(Component.literal("Decides whether telekinesis can be used without the enchantment.")))
                    .binding(config.onByDefault, { config.onByDefault }, { config.onByDefault = it })
                    .controller {
                        TickBoxControllerBuilder.create(it)
                    }.build()
            )
            .option(
                Option.createBuilder<Boolean>()
                    .name(Component.literal("Only while sneaking"))
                    .description(OptionDescription.of(Component.literal("Decides whether players should be required to sneak to use telekinesis.")))
                    .binding(config.onlyOnSneak, { config.onlyOnSneak }, { config.onlyOnSneak = it })
                    .controller {
                        TickBoxControllerBuilder.create(it)
                    }.build()
            )
            .option(
                Option.createBuilder<Boolean>()
                    .name(Component.literal("Enchantment"))
                    .description(
                        OptionDescription.of(
                            Component.literal("Decides whether to add the enchantment to the game."),
                            Component.literal("Requires a restart to work!")
                        )
                    )
                    .binding(config.enchantment, { config.enchantment }, { config.enchantment = it })
                    .controller {
                        TickBoxControllerBuilder.create(it)
                    }.build()
            )
            .option(
                Option.createBuilder<Boolean>()
                    .name(Component.literal("Block drops"))
                    .description(OptionDescription.of(Component.literal("Decides whether telekinesis can be used for block drops.")))
                    .binding(config.blockDrops, { config.blockDrops }, { config.blockDrops = it })
                    .controller {
                        TickBoxControllerBuilder.create(it)
                    }.build()
            )
            .option(
                Option.createBuilder<Boolean>()
                    .name(Component.literal("Exp drops"))
                    .description(OptionDescription.of(Component.literal("Decides whether telekinesis can be used for exp drops.")))
                    .binding(config.expDrops, { config.expDrops }, { config.expDrops = it })
                    .controller {
                        TickBoxControllerBuilder.create(it)
                    }.build()
            )
            .option(
                Option.createBuilder<Boolean>()
                    .name(Component.literal("Mob drops"))
                    .description(OptionDescription.of(Component.literal("Decides whether telekinesis can be used for mob drops.")))
                    .binding(config.mobDrops, { config.mobDrops }, { config.mobDrops = it })
                    .controller {
                        TickBoxControllerBuilder.create(it)
                    }.build()
            )
            .option(
                Option.createBuilder<Boolean>()
                    .name(Component.literal("Vehicle drops"))
                    .description(OptionDescription.of(Component.literal("Decides whether telekinesis can be used for vehicle drops.")))
                    .binding(config.vehicleDrops, { config.vehicleDrops }, { config.vehicleDrops = it })
                    .controller {
                        TickBoxControllerBuilder.create(it)
                    }.build()
            )
            .option(
                Option.createBuilder<Boolean>()
                    .name(Component.literal("Shearing drops"))
                    .description(OptionDescription.of(Component.literal("Decides whether telekinesis can be used for shearing drops.")))
                    .binding(config.shearingDrops, { config.shearingDrops }, { config.shearingDrops = it })
                    .controller {
                        TickBoxControllerBuilder.create(it)
                    }.build()
            )
            .option(
                Option.createBuilder<Boolean>()
                    .name(Component.literal("Fishing drops"))
                    .description(OptionDescription.of(Component.literal("Decides whether telekinesis can be used for fishing drops.")))
                    .binding(config.fishingDrops, { config.fishingDrops }, { config.fishingDrops = it })
                    .controller {
                        TickBoxControllerBuilder.create(it)
                    }.build()
            )
            .build()
    )
}