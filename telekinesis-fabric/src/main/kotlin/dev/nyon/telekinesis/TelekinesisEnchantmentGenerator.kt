package dev.nyon.telekinesis

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider
import net.minecraft.core.HolderLookup
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.ItemTags
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.Enchantment.EnchantmentDefinition
import net.minecraft.world.item.enchantment.Enchantment.dynamicCost
import java.util.concurrent.CompletableFuture

class TelekinesisEnchantmentGenerator : DataGeneratorEntrypoint {
    override fun onInitializeDataGenerator(generator: FabricDataGenerator) {/*? if >=1.21 {*/
        val pack = generator.createPack()

        pack.addProvider(::EnchantmentProvider)/*?}*/
    }

    override fun buildRegistry(registryBuilder: RegistrySetBuilder) {
        registryBuilder.add(Registries.ENCHANTMENT) { context ->
            val enchantmentDefinition: EnchantmentDefinition = Enchantment.definition(
                context.lookup(Registries.ITEM).getOrThrow(ItemTags.DURABILITY_ENCHANTABLE),
                2,
                1,
                dynamicCost(25, 25),
                dynamicCost(75, 25),
                5,
                EquipmentSlotGroup.ANY
            )

            context.register(
                ResourceKey.create(
                    Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath("telekinesis", "telekinesis")
                ), Enchantment.enchantment(enchantmentDefinition).build(
                    ResourceLocation.fromNamespaceAndPath("telekinesis", "telekinesis.name")
                )
            )
        }
    }
}

private class EnchantmentProvider(
    output: FabricDataOutput, registriesFuture: CompletableFuture<HolderLookup.Provider>
) : FabricDynamicRegistryProvider(output, registriesFuture) {
    override fun getName(): String {
        return "Enchantment"
    }

    override fun configure(registries: HolderLookup.Provider, entries: Entries) {
        entries.addAll(registries.lookupOrThrow(Registries.ENCHANTMENT))
    }
}

