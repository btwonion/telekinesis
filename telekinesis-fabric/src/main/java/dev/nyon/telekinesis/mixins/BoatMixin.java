package dev.nyon.telekinesis.mixins;

import net.minecraft.world.entity.vehicle.Boat;
import org.spongepowered.asm.mixin.Mixin;

/*? <=1.20.2 {*/
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.nyon.telekinesis.utils.EntityUtils;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.injection.At;
/*?}*/

@Mixin(Boat.class)
public class BoatMixin {
    /*? <=1.20.2 {*/
    @ModifyExpressionValue(
        method = "destroy(Lnet/minecraft/world/damagesource/DamageSource;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/vehicle/Boat;getDropItem()Lnet/minecraft/world/item/Item;"
        )
    )
    private Item changeDroppedItem(
        Item original,
        DamageSource damageSource
    ) {
        return EntityUtils.getDropItemInject(original, damageSource);
    }
    /*?}*/
}
