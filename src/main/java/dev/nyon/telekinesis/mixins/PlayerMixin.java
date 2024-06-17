package dev.nyon.telekinesis.mixins;

import dev.nyon.telekinesis.utils.MixinHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(Player.class)
public class PlayerMixin {

    /*? if >=1.20.5 {*/@Shadow/*?} else {*/ /*@Unique *//*?}*/
    @Final
    Inventory inventory;
    @Unique
    final Player instance = (Player) (Object) this;

    @Inject(
        method = "dropEquipment",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/player/Inventory;dropAll()V"
        )
    )
    public void modifyEquipmentDrops(CallbackInfo ci) {
        if (inventory == null) return;
        ArrayList<ItemStack> items = new ArrayList<>(inventory.items);
        List<ItemStack> processedItems = MixinHelper.entityDropEquipmentMultiple(instance, items);
        inventory.clearContent();
        inventory.items.addAll(processedItems);
    }
}
