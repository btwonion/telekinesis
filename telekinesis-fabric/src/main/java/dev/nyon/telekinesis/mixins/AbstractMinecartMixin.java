package dev.nyon.telekinesis.mixins;

import net.minecraft.world.entity.vehicle.AbstractMinecart;
import org.spongepowered.asm.mixin.Mixin;

/*? <=1.20.2 {*/
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import dev.nyon.telekinesis.utils.EntityUtils;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.injection.At;
/*?}*/

@Mixin(AbstractMinecart.class)
public class AbstractMinecartMixin {

    /*? <=1.20.2 {*/
    @WrapWithCondition(method = "destroy", at = @At(
        value = "INVOKE",
        target = "Lnet/minecraft/world/entity/vehicle/AbstractMinecart;spawnAtLocation(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/entity/item/ItemEntity;"
    ))
    private boolean redirectMinecartDrops(
        AbstractMinecart instance,
        ItemStack itemStack,
        DamageSource damageSource
    ) {
        if (!(damageSource.getEntity() instanceof LivingEntity livingEntity)) return true;
        return EntityUtils.spawnAtLocationAttacker(livingEntity, itemStack);
    }
    /*?}*/
}
