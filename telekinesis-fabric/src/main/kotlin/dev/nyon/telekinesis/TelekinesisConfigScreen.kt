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
    configScreenBuilder.title(Component.translatable("menu.telekinesis.name"))
    configScreenBuilder.appendOptionCategory()
    configScreenBuilder.save { saveConfig() }
    val configScreen = configScreenBuilder.build()
    return configScreen.generateScreen(parent)
}

fun YetAnotherConfigLib.Builder.appendOptionCategory() {
    this.category(
        ConfigCategory.createBuilder()
            .name(Component.translatable("menu.telekinesis.config.general"))
            .option(
                Option.createBuilder<Boolean>()
                    .name(Component.translatable("menu.telekinesis.config.general.onbydefault"))
                    .description(OptionDescription.of(Component.translatable("menu.telekinesis.config.general.onbydefault.description")))
                    .binding(config.onByDefault, { config.onByDefault }, { config.onByDefault = it })
                    .controller {
                        TickBoxControllerBuilder.create(it)
                    }.build()
            )
            .option(
                Option.createBuilder<Boolean>()
                    .name(Component.translatable("menu.telekinesis.config.general.onlywhilesneaking"))
                    .description(OptionDescription.of(Component.translatable("menu.telekinesis.config.general.onlywhilesneaking.description")))
                    .binding(config.onlyOnSneak, { config.onlyOnSneak }, { config.onlyOnSneak = it })
                    .controller {
                        TickBoxControllerBuilder.create(it)
                    }.build()
            )
            .option(
                Option.createBuilder<Boolean>()
                    .name(Component.translatable("menu.telekinesis.config.general.enchantment"))
                    .description(
                        OptionDescription.of(
                            Component.translatable("menu.telekinesis.config.general.enchantment.description1"),
                            Component.translatable("menu.telekinesis.config.general.enchantment.description2")
                        )
                    )
                    .binding(config.enchantment, { config.enchantment }, { config.enchantment = it })
                    .controller {
                        TickBoxControllerBuilder.create(it)
                    }.build()
            )
            .option(
                Option.createBuilder<Boolean>()
                    .name(Component.translatable("menu.telekinesis.config.general.blockdrops"))
                    .description(OptionDescription.of(Component.translatable("menu.telekinesis.config.general.blockdrops.description")))
                    .binding(config.blockDrops, { config.blockDrops }, { config.blockDrops = it })
                    .controller {
                        TickBoxControllerBuilder.create(it)
                    }.build()
            )
            .option(
                Option.createBuilder<Boolean>()
                    .name(Component.translatable("menu.telekinesis.config.general.expdrops"))
                    .description(OptionDescription.of(Component.translatable("menu.telekinesis.config.general.expdrops.description")))
                    .binding(config.expDrops, { config.expDrops }, { config.expDrops = it })
                    .controller {
                        TickBoxControllerBuilder.create(it)
                    }.build()
            )
            .option(
                Option.createBuilder<Boolean>()
                    .name(Component.translatable("menu.telekinesis.config.general.mobdrops"))
                    .description(OptionDescription.of(Component.translatable("menu.telekinesis.config.general.mobdrops.description")))
                    .binding(config.mobDrops, { config.mobDrops }, { config.mobDrops = it })
                    .controller {
                        TickBoxControllerBuilder.create(it)
                    }.build()
            )
            .option(
                Option.createBuilder<Boolean>()
                    .name(Component.translatable("menu.telekinesis.config.general.vehicledrops"))
                    .description(OptionDescription.of(Component.translatable("menu.telekinesis.config.general.vehicledrops.description")))
                    .binding(config.vehicleDrops, { config.vehicleDrops }, { config.vehicleDrops = it })
                    .controller {
                        TickBoxControllerBuilder.create(it)
                    }.build()
            )
            .option(
                Option.createBuilder<Boolean>()
                    .name(Component.translatable("menu.telekinesis.config.general.shearingdrops"))
                    .description(OptionDescription.of(Component.translatable("menu.telekinesis.config.general.shearingdrops.description")))
                    .binding(config.shearingDrops, { config.shearingDrops }, { config.shearingDrops = it })
                    .controller {
                        TickBoxControllerBuilder.create(it)
                    }.build()
            )
            .option(
                Option.createBuilder<Boolean>()
                    .name(Component.translatable("menu.telekinesis.config.general.fishingdrops"))
                    .description(OptionDescription.of(Component.translatable("menu.telekinesis.config.general.fishingdrops.description")))
                    .binding(config.fishingDrops, { config.fishingDrops }, { config.fishingDrops = it })
                    .controller {
                        TickBoxControllerBuilder.create(it)
                    }.build()
            )
            .build()
    )
}