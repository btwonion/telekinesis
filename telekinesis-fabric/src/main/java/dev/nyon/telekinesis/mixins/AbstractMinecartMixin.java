package dev.nyon.telekinesis.mixins;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.nyon.telekinesis.utils.MixinHelper;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractMinecart.class)
public class AbstractMinecartMixin {

    /*? <=1.20.2 {*/
    /*@ModifyExpressionValue(
        method = "destroy",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/vehicle/AbstractMinecart;getDropItem()Lnet/minecraft/world/item/Item;"
        )
    )
    private Item redirectMinecartDrops(
        Item original,
        DamageSource damageSource
    ) {
        return MixinHelper.modifyExpressionValueOldVehicle(original, damageSource);
    }
    *//*?}*/
}
