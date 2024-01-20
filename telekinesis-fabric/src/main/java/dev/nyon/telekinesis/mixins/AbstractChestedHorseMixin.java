package dev.nyon.telekinesis.mixins;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import dev.nyon.telekinesis.TelekinesisPolicy;
import dev.nyon.telekinesis.utils.TelekinesisUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;
import net.minecraft.world.level.ItemLike;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractChestedHorse.class)
public class AbstractChestedHorseMixin {

    @WrapWithCondition(
        method = "dropEquipment",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/animal/horse/AbstractChestedHorse;spawnAtLocation(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/entity/item/ItemEntity;"
        )
    )
    public boolean redirectEquipmentDrop(
        AbstractChestedHorse instance,
        ItemLike item
    ) {
        final var attacker = instance.getLastAttacker();
        if (!(attacker instanceof ServerPlayer serverPlayer)) return true;

        boolean hasTelekinesis = TelekinesisUtils.handleTelekinesis(TelekinesisPolicy.MobDrops,
            serverPlayer,
            serverPlayer.getMainHandItem(),
            player -> {
                if (!player.addItem(item.asItem()
                    .getDefaultInstance())) instance.spawnAtLocation(item);
            });

        return !hasTelekinesis;
    }
}
