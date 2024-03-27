package dev.nyon.telekinesis.mixins;

import dev.nyon.telekinesis.MainKt;
import dev.nyon.telekinesis.TelekinesisEnchantment;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantments.class)
public abstract class EnchantmentsMixin {

    @Unique
    private static boolean isTelekinesisRegistered = false;

    @Inject(
        method = "register",
        at = @At("RETURN")
    )
    private static void registerTelekinesis(
        String identifier,
        Enchantment enchantment,
        CallbackInfoReturnable<Enchantment> cir
    ) {
        if (!isTelekinesisRegistered) {
            MainKt.setTelekinesis(new TelekinesisEnchantment());
            isTelekinesisRegistered = true;
            Registry.register(
                BuiltInRegistries.ENCHANTMENT,
                new ResourceLocation("telekinesis", "telekinesis"),
                MainKt.getTelekinesis()
            );
        }
    }
}
