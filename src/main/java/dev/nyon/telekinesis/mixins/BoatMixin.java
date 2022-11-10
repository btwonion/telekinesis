package dev.nyon.telekinesis.mixins;

import dev.nyon.telekinesis.check.TelekinesisUtils;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Boat.class)
public abstract class BoatMixin {

    @Shadow public abstract Item getDropItem();

    @Redirect(
        method = "hurt",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/vehicle/Boat;destroy(Lnet/minecraft/world/damagesource/DamageSource;)V"
        )
    )
    public void manipulateDrop(Boat instance, DamageSource damageSource) {
        var boat = (Boat) (Object) this;
        var item = getDropItem();
        if (TelekinesisUtils.hasNoTelekinesis(damageSource)) {
            boat.spawnAtLocation(item);
            return;
        }

        var player = (Player) damageSource.getEntity();
        if (!player.getInventory().add(new ItemStack(item))) boat.spawnAtLocation(item);
    }
}
