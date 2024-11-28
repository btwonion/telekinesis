package dev.nyon.telekinesis.mixins;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import dev.nyon.telekinesis.utils.MixinHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Mob.class)
public class MobMixin {

    @WrapWithCondition(
        method = "dropCustomDeathLoot",
        at = @At(
            value = "INVOKE",
            target = /*? if needsWorldNow {*//*"Lnet/minecraft/world/entity/Mob;spawnAtLocation(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/entity/item/ItemEntity;"*//*?} else {*/  "Lnet/minecraft/world/entity/Mob;spawnAtLocation(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/entity/item/ItemEntity;" /*?}*/
        )
    )
    public boolean modifyCustomDeathLoot(
        Mob instance,
        /*$ serverLevel {*//*$}*/
        ItemStack itemStack,
        /*? if >=1.21 {*/ ServerLevel serverLevel, DamageSource damageSource, boolean bl /*?} else {*/ /*DamageSource damageSource, int looting, boolean hitByPlayer *//*?}*/
    ) {
        return MixinHelper.entityCustomDeathLootSingle(damageSource, itemStack);
    }

    /*? if >=1.21 {*/
    @WrapWithCondition(
        method = /*? if needsWorldNow {*//*"Lnet/minecraft/world/entity/Mob;dropPreservedEquipment(Lnet/minecraft/server/level/ServerLevel;Ljava/util/function/Predicate;)Ljava/util/Set;"*//*?} else {*/  "dropPreservedEquipment(Ljava/util/function/Predicate;)Ljava/util/Set;" /*?}*/,
        at = @At(
            value = "INVOKE",
            target = /*? if needsWorldNow {*//*"Lnet/minecraft/world/entity/Mob;spawnAtLocation(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/entity/item/ItemEntity;"*//*?} else {*/  "Lnet/minecraft/world/entity/Mob;spawnAtLocation(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/entity/item/ItemEntity;" /*?}*/
        )
    )
    public boolean modifyPreservedEquipment(
        Mob instance,
        /*$ serverLevel {*//*$}*/
        ItemStack itemStack
    ) {
        return MixinHelper.entityDropEquipmentSingle(instance, itemStack);
    }
    /*?}*/
}
