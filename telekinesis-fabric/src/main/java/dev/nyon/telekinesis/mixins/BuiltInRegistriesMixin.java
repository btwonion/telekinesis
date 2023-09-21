package dev.nyon.telekinesis.mixins;

import dev.nyon.telekinesis.MainKt;
import dev.nyon.telekinesis.TelekinesisConfigKt;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BuiltInRegistries.class)
public class BuiltInRegistriesMixin {

    @Inject(
        method = "bootStrap",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/core/registries/BuiltInRegistries;createContents()V",
            shift = At.Shift.AFTER
        )
    )
    private static void registerTelekinesis(CallbackInfo ci) {
        TelekinesisConfigKt.loadConfig();
        if (TelekinesisConfigKt.getConfig().getEnchantment())
            Registry.register(BuiltInRegistries.ENCHANTMENT, new ResourceLocation("telekinesis", "telekinesis"), MainKt.getTelekinesis());
    }
}
