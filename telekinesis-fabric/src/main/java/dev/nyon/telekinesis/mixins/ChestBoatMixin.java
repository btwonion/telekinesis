package dev.nyon.telekinesis.mixins;

import net.minecraft.world.entity.vehicle.ChestBoat;
import org.spongepowered.asm.mixin.Mixin;

/*? if >1.20.2 {*//*
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.nyon.telekinesis.utils.EntityUtils;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.injection.At;
 *//*?}*/

@Mixin(ChestBoat.class)
public class ChestBoatMixin {

    /*? if >1.20.2 {*//*
    @ModifyExpressionValue(
        method = "destroy(Lnet/minecraft/world/damagesource/DamageSource;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/vehicle/ChestBoat;getDropItem()Lnet/minecraft/world/item/Item;"
        )
    )
    private Item changeDroppedItem(
        Item original,
        DamageSource damageSource
    ) {
        return EntityUtils.getDropItemInject(original, damageSource);
    }
    *//*?}*/
}
