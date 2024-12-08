package dev.nyon.magnetic.mixins;

import net.minecraft.core.MappedRegistry;
import org.spongepowered.asm.mixin.Mixin;
/*? if >=1.21 {*/
import dev.nyon.magnetic.MagneticEnchantmentKt;
import dev.nyon.magnetic.config.ConfigKt;
import net.minecraft.core.Holder;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.resources.ResourceKey;
/*?}*/

@Mixin(MappedRegistry.class)
public class MappedRegistryMixin<T> {

    /*? if >=1.21 {*/
    @Inject(
        method = "register",
        at = @At("HEAD")
    )
    public void cancelMagneticEnchantmentRegister(
        ResourceKey<T> key,
        T entry,
        RegistrationInfo info,
        CallbackInfoReturnable<Holder.Reference<T>> cir
    ) {
        if (!ConfigKt.getConfig().getNeedEnchantment() && key.location().equals(MagneticEnchantmentKt.getMagneticEnchantmentId())) cir.cancel();
    }
     /*?}*/
}
