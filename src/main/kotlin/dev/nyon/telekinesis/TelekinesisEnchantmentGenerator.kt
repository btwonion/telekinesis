package dev.nyon.telekinesis

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
/*? if >=1.21 {*/
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider.EnchantmentTagProvider
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.Enchantment.EnchantmentDefinition
import net.minecraft.world.item.enchantment.Enchantment.dynamicCost
import java.util.concurrent.CompletableFuture
/*?}*/

class TelekinesisEnchantmentGenerator : DataGeneratorEntrypoint {
    override fun onInitializeDataGenerator(generator: FabricDataGenerator) {
        /*? if >=1.21 {*/
        val pack = generator.createPack()

        pack.addProvider(::EnchantmentProvider)
        pack.addProvider(::TelekinesisEnchantmentTagProvider)
        /*?}*/
    }
}

/*? if >=1.21 {*/
private class TelekinesisEnchantmentTagProvider(
    output: FabricDataOutput, completableFuture: CompletableFuture<HolderLookup.Provider>
) : EnchantmentTagProvider(output, completableFuture) {
    override fun addTags(registries: HolderLookup.Provider) {
        fun minecraftTagKey(value: String): TagKey<Enchantment> = TagKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath("minecraft", value))
        listOf(
            getOrCreateTagBuilder(telekinesisEffectId),
            getOrCreateTagBuilder(minecraftTagKey("tradeable")),
            getOrCreateTagBuilder(minecraftTagKey("treasure")),
            getOrCreateTagBuilder(minecraftTagKey("in_enchanting_table"))
        ).forEach {
            it.add(telekinesisEnchantmentId)
        }
    }
}

private class EnchantmentProvider(
    output: FabricDataOutput, registriesFuture: CompletableFuture<HolderLookup.Provider>
) : FabricDynamicRegistryProvider(output, registriesFuture) {
    override fun getName(): String {
        return "Telekinesis Enchantment Generation"
    }

    override fun configure(registries: HolderLookup.Provider, entries: Entries) {
        val enchantmentDefinition: EnchantmentDefinition = Enchantment.definition(
            registries.lookupOrThrow(Registries.ITEM).getOrThrow(ConventionalItemTags.TOOLS),
            2,
            1,
            dynamicCost(25, 25),
            dynamicCost(75, 75),
            7,
            EquipmentSlotGroup.HAND
        )

        val enchantment = Enchantment.enchantment(enchantmentDefinition).build(
            ResourceLocation.fromNamespaceAndPath("telekinesis", "telekinesis.name")
        )
        entries.add(ResourceKey.create(Registries.ENCHANTMENT, telekinesisEnchantmentId), enchantment)
    }
}
/*?}*/