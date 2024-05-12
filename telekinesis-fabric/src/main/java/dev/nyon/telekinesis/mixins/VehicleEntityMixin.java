package dev.nyon.telekinesis.mixins;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.nyon.telekinesis.utils.EntityUtils;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

@Pseudo
@Mixin(targets = "net.minecraft.world.entity.vehicle.VehicleEntity")
public class VehicleEntityMixin {

    @ModifyExpressionValue(
        method = "destroy(Lnet/minecraft/world/damagesource/DamageSource;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/vehicle/VehicleEntity;getDropItem()Lnet/minecraft/world/item/Item;"
        )
    )
    private Item changeDroppedItem(
        Item original,
        DamageSource damageSource
    ) {
        return EntityUtils.getDropItemInject(original, damageSource);
    }
}
