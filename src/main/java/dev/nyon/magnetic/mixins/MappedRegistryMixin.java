package dev.nyon.magnetic.mixins;

import net.minecraft.resources.RegistryDataLoader;
import org.spongepowered.asm.mixin.Mixin;
/*? if >=1.21 {*/
import com.google.gson.JsonElement;
import com.mojang.serialization.Decoder;
import dev.nyon.magnetic.MagneticEnchantmentKt;
import dev.nyon.magnetic.config.ConfigKt;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.core.WritableRegistry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.RegistryOps;
import net.minecraft.server.packs.resources.Resource;
/*?}*/

@Mixin(RegistryDataLoader.class)
public class MappedRegistryMixin {

    /*? if >=1.21 {*/
    @Inject(
        method = "loadElementFromResource",
        at = @At("HEAD"),
        cancellable = true
    )
    private static <E> void cancelMagneticEnchantmentRegister(
        WritableRegistry<E> registry,
        Decoder<E> decoder,
        RegistryOps<JsonElement> ops,
        ResourceKey<E> registryKey,
        Resource resource,
        RegistrationInfo info,
        CallbackInfo ci
    ) {
        if (!ConfigKt.getConfig().getNeedEnchantment() && registryKey.location().equals(MagneticEnchantmentKt.getMagneticEnchantmentId())) ci.cancel();
    }
     /*?}*/
}
