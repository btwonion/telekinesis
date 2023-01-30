package telekinesis.mixins;

import telekinesis.check.TelekinesisUtils;
import dev.nyon.telekinesis.config.ConfigKt;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AbstractMinecart.class)
public abstract class MinecartMixin {

    @Shadow abstract Item getDropItem();

    @Redirect(
        method = "destroy",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/vehicle/AbstractMinecart;spawnAtLocation(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/entity/item/ItemEntity;"
        )
    )
    public ItemEntity redirectDrops(AbstractMinecart instance, ItemStack itemStack, DamageSource damageSource) {
        var boat = (AbstractMinecart) (Object) this;
        var item = getDropItem();
        if (
            !ConfigKt.getConfig().getEntityDrops()
            || (TelekinesisUtils.hasNoTelekinesis(damageSource) && !ConfigKt.getConfig().getOnByDefault())
        ) return boat.spawnAtLocation(item);
        var player = (Player) damageSource.getEntity();
        if (!player.getInventory().add(new ItemStack(item)))
            return boat.spawnAtLocation(item);

        return null;
    }
}
