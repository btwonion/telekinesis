package dev.nyon.telekinesis.mixins;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.nyon.telekinesis.TelekinesisPolicy;
import dev.nyon.telekinesis.utils.TelekinesisUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.concurrent.atomic.AtomicReference;

@Mixin(ChestBoat.class)
public class ChestBoatMixin {

    @ModifyExpressionValue(
        method = "destroy(Lnet/minecraft/world/damagesource/DamageSource;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/vehicle/ChestBoat;getDropItem()Lnet/minecraft/world/item/Item;"
        )
    )
    private Item changeDroppedItem(Item original, DamageSource damageSource) {
        final var attacker = damageSource.getEntity();
        if (!(attacker instanceof ServerPlayer)) return original;

        AtomicReference<Item> toReturn = new AtomicReference<>(original);

        TelekinesisUtils.handleTelekinesis(TelekinesisPolicy.VehicleDrops, damageSource, player -> {
            if (player.addItem(original.asItem().getDefaultInstance())) toReturn.set(ItemStack.EMPTY.getItem());
        });

        return toReturn.get();
    }
}
