package dev.nyon.telekinesis.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.nyon.telekinesis.utils.MixinHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.vehicle.MinecartTNT;
import org.spongepowered.asm.mixin.Mixin;

/*? if >1.20.2 {*/
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
 /*?}*/

@Mixin(MinecartTNT.class)
public class MinecartTNTMixin {

    /*? if >1.20.2 {*/
    @Unique
    private static final ThreadLocal<ServerPlayer> threadLocal = new ThreadLocal<>();

    @WrapOperation(
        method = "destroy(Lnet/minecraft/world/damagesource/DamageSource;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/vehicle/MinecartTNT;destroy(Lnet/minecraft/world/item/Item;)V"
        )
    )
    private void checkForPlayer(
        MinecartTNT instance,
        Item dropItem,
        Operation<Void> original,
        DamageSource source
    ) {
        MixinHelper.prepareVehicleServerPlayer(instance, dropItem, original, source, threadLocal);
    }
    /*?}*/
}
