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
                    .description(OptionDescription.of(Component.literal("Should the feature work without the enchantment?")))
                    .binding(config.onByDefault, { config.onByDefault }, { config.onByDefault = it })
                    .controller {
                        TickBoxControllerBuilder.create(it)
                    }.build()
            )
            .option(
                Option.createBuilder<Boolean>()
                    .name(Component.literal("Only while sneaking"))
                    .description(OptionDescription.of(Component.literal("Should the feature work only while sneaking?")))
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
                            Component.literal("Should the enchantment be available?"),
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
                    .description(OptionDescription.of(Component.literal("Should block drops instantly be moved into your inventory?")))
                    .binding(config.blockDrops, { config.blockDrops }, { config.blockDrops = it })
                    .controller {
                        TickBoxControllerBuilder.create(it)
                    }.build()
            )
            .option(
                Option.createBuilder<Boolean>()
                    .name(Component.literal("Exp drops"))
                    .description(OptionDescription.of(Component.literal("Should exp drops instantly be moved into your inventory?")))
                    .binding(config.expDrops, { config.expDrops }, { config.expDrops = it })
                    .controller {
                        TickBoxControllerBuilder.create(it)
                    }.build()
            )
            .option(
                Option.createBuilder<Boolean>()
                    .name(Component.literal("Mob drops"))
                    .description(OptionDescription.of(Component.literal("Should mob drops instantly be moved into your inventory?")))
                    .binding(config.mobDrops, { config.mobDrops }, { config.mobDrops = it })
                    .controller {
                        TickBoxControllerBuilder.create(it)
                    }.build()
            )
            .option(
                Option.createBuilder<Boolean>()
                    .name(Component.literal("Entity drops"))
                    .description(OptionDescription.of(Component.literal("Should entity drops instantly be moved into your inventory?")))
                    .binding(config.entityDrops, { config.entityDrops }, { config.entityDrops = it })
                    .controller {
                        TickBoxControllerBuilder.create(it)
                    }.build()
            )
            .option(
                Option.createBuilder<Boolean>()
                    .name(Component.literal("Shearing drops"))
                    .description(OptionDescription.of(Component.literal("Should shearing drops instantly be moved into your inventory?")))
                    .binding(config.shearingDrops, { config.shearingDrops }, { config.shearingDrops = it })
                    .controller {
                        TickBoxControllerBuilder.create(it)
                    }.build()
            )
            .build()
    )
}