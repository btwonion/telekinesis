package dev.nyon.telekinesis.mixins;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.nyon.telekinesis.utils.MixinHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
/*? if >=1.20.4*/ import net.minecraft.world.entity.vehicle.VehicleEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

import static dev.nyon.telekinesis.utils.MixinHelper.threadLocal;

@Pseudo
@Mixin(targets = "net.minecraft.world.entity.vehicle.VehicleEntity")
public class VehicleEntityMixin {
    /*? if >=1.20.4 {*/
    @WrapOperation(
        method = "destroy(Lnet/minecraft/world/damagesource/DamageSource;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/vehicle/VehicleEntity;destroy(Lnet/minecraft/world/item/Item;)V"
        )
    )
    private void checkForPlayer(
        VehicleEntity instance,
        Item dropItem,
        Operation<Void> original,
        DamageSource source
    ) {
        MixinHelper.prepareVehicleServerPlayer(instance, dropItem, original, source);
    }

    @WrapWithCondition(
        method = "destroy(Lnet/minecraft/world/item/Item;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/vehicle/VehicleEntity;spawnAtLocation(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/entity/item/ItemEntity;"
        )
    )
    private boolean replaceDropItem(
        VehicleEntity instance,
        ItemStack itemStack
    ) {
        ServerPlayer player = threadLocal.get();
        if (player == null) return true;

        return MixinHelper.wrapWithConditionPlayerItemSingle(player, itemStack);
    }
    /*?}*/
}