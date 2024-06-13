package dev.nyon.telekinesis.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.vehicle.AbstractMinecartContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static dev.nyon.telekinesis.utils.MixinHelper.threadLocal;

@Mixin(AbstractMinecartContainer.class)
public class AbstractMinecartContainerMixin {

    @WrapOperation(
        method = "destroy(Lnet/minecraft/world/damagesource/DamageSource;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/vehicle/AbstractMinecart;destroy(Lnet/minecraft/world/damagesource/DamageSource;)V"
        )
    )
    private void checkForPlayer(
        AbstractMinecartContainer instance,
        DamageSource source,
        Operation<Void> original
    ) {
        if (!(source.getEntity() instanceof ServerPlayer player)) {
            original.call(instance, source);
            return;
        }

        ServerPlayer previous = threadLocal.get();
        threadLocal.set(player);
        try {
            original.call(instance, source);
        } finally {
            threadLocal.set(previous);
        }
    }
}
