package dev.nyon.telekinesis.mixins;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import dev.nyon.telekinesis.TelekinesisPolicy;
import dev.nyon.telekinesis.utils.EntityUtils;
import dev.nyon.telekinesis.utils.TelekinesisUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import java.util.function.Consumer;

@Mixin(Allay.class)
public class AllayMixin {

    @Unique
    final Allay allay = (Allay) (Object) this;

    @WrapWithCondition(
        method = "dropEquipment",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/animal/allay/Allay;spawnAtLocation(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/entity/item/ItemEntity;"
        )
    )
    public boolean redirectEquipmentDrop(
        Allay instance,
        ItemStack stack
    ) {
        return EntityUtils.spawnAtLocationInject(instance, stack);
    }

    @Redirect(
        method = "dropEquipment",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/List;forEach(Ljava/util/function/Consumer;)V"
        )
    )
    public void redirectInventoryDrops(
        List<ItemStack> instance,
        Consumer<ItemStack> consumer
    ) {
        final var attacker = allay.getLastAttacker();
        if (!(attacker instanceof ServerPlayer serverPlayer)) {
            instance.forEach(consumer);
            return;
        }

        boolean hasTelekinesis = TelekinesisUtils.handleTelekinesis(
            TelekinesisPolicy.MobDrops,
            serverPlayer,
            serverPlayer.getMainHandItem(),
            player -> instance.forEach(item -> {
                if (!player.addItem(item)) consumer.accept(item);
            })
        );

        if (!hasTelekinesis) instance.forEach(consumer);
    }
}
