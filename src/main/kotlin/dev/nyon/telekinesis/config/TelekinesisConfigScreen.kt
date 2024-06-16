package dev.nyon.telekinesis.config

import dev.isxander.yacl3.api.ConfigCategory
import dev.isxander.yacl3.api.Option
import dev.isxander.yacl3.api.OptionDescription
import dev.isxander.yacl3.api.YetAnotherConfigLib
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder
import dev.nyon.konfig.config.saveConfig
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component

fun generateConfigScreen(parent: Screen? = null): Screen {
    val configScreenBuilder = YetAnotherConfigLib.createBuilder()
    configScreenBuilder.title(Component.translatable("menu.telekinesis.name"))
    configScreenBuilder.appendOptionCategory()
    configScreenBuilder.save { saveConfig(config) }
    val configScreen = configScreenBuilder.build()
    return configScreen.generateScreen(parent)
}

fun YetAnotherConfigLib.Builder.appendOptionCategory() {
    this.category(
        ConfigCategory.createBuilder()
            .name(Component.translatable("menu.telekinesis.config.general"))
            .option(
                Option.createBuilder<Boolean>()
                    .name(Component.translatable("menu.telekinesis.config.general.needEnchantment"))
                    .description(
                        OptionDescription
                            .of(Component.translatable("menu.telekinesis.config.general.needEnchantment.description"))
                    )
                    .binding(config.needEnchantment, { config.needEnchantment }, { config.needEnchantment = it })
                    .controller {
                        TickBoxControllerBuilder.create(it)
                    }.build()
            )
            .option(
                Option.createBuilder<Boolean>()
                    .name(Component.translatable("menu.telekinesis.config.general.needSneak"))
                    .description(
                        OptionDescription
                            .of(
                                Component.translatable("menu.telekinesis.config.general.needSneak.description")
                            )
                    )
                    .binding(config.needSneak, { config.needSneak }, { config.needSneak = it })
                    .controller {
                        TickBoxControllerBuilder.create(it)
                    }.build()
            )
            .option(
                Option.createBuilder<Boolean>()
                    .name(Component.translatable("menu.telekinesis.config.general.expAllowed"))
                    .description(
                        OptionDescription
                            .of(Component.translatable("menu.telekinesis.config.general.expAllowed.description"))
                    )
                    .binding(config.expAllowed, { config.expAllowed }, { config.expAllowed = it })
                    .controller {
                        TickBoxControllerBuilder.create(it)
                    }.build()
            )
            .option(
                Option.createBuilder<Boolean>()
                    .name(Component.translatable("menu.telekinesis.config.general.itemsAllowed"))
                    .description(
                        OptionDescription
                            .of(Component.translatable("menu.telekinesis.config.general.itemsAllowed.description"))
                    )
                    .binding(config.itemsAllowed, { config.itemsAllowed }, { config.itemsAllowed = it })
                    .controller {
                        TickBoxControllerBuilder.create(it)
                    }.build()
            )
            .build()
    )
}
