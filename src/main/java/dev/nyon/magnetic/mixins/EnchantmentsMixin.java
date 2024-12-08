package dev.nyon.magnetic.mixins;

/*? if <1.21 {*/
/*import dev.nyon.magnetic.MainKt;
import dev.nyon.magnetic.MagneticEnchantment;
import dev.nyon.magnetic.config.ConfigKt;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
*//*?}*/
import net.minecraft.world.item.enchantment.Enchantments;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Enchantments.class)
public abstract class EnchantmentsMixin {

    /*? if <1.21 {*/
    /*@Unique
    private static boolean isEnchantmentRegistered = false;

    @Inject(
        method = "register",
        at = @At("RETURN")
    )
    private static void registerEnchantment(
        String identifier,
        Enchantment enchantment,
        CallbackInfoReturnable<Enchantment> cir
    ) {
        if (!isEnchantmentRegistered && ConfigKt.getConfig().getNeedEnchantment()) {
            MainKt.setMagnetic(new MagneticEnchantment());
            isEnchantmentRegistered = true;
            Registry.register(BuiltInRegistries.ENCHANTMENT, new ResourceLocation("magnetic", "magnetic"), MainKt.getMagnetic());
        }
    }
    *//*?}*/
}
