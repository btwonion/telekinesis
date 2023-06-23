package dev.nyon.telekinesis.mixins;

import dev.nyon.telekinesis.TelekinesisPolicy;
import dev.nyon.telekinesis.utils.TelekinesisUtils;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Boat.class)
public abstract class BoatMixin {

    @Redirect(
        method = "destroy",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/vehicle/Boat;spawnAtLocation(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/entity/item/ItemEntity;"
        )
    )
    public ItemEntity manipulateDrop(Boat instance, ItemLike itemLike, DamageSource damageSource) {
        Boat boat = (Boat) (Object) this;
        ItemStack item = new ItemStack(itemLike);

        boolean hasTelekinesis = TelekinesisUtils.handleTelekinesis(TelekinesisPolicy.EntityDrops, damageSource, player -> {
            if (!player.addItem(item)) boat.spawnAtLocation(item);
        });

        if (hasTelekinesis) return null;
        else return boat.spawnAtLocation(item);
    }
}