package dev.nyon.telekinesis.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.nyon.telekinesis.TelekinesisPolicy;
import dev.nyon.telekinesis.utils.TelekinesisUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.ItemLike;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(WitherBoss.class)
public abstract class WitherBossMixin {
    @WrapOperation(
        method = "dropCustomDeathLoot",
        at = @At(
            value = "INVOKE",
            target = "net/minecraft/world/entity/boss/wither/WitherBoss.spawnAtLocation (Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/entity/item/ItemEntity;"
        )
    )
    protected ItemEntity redirectEquipmentDrop(WitherBoss instance, ItemLike stack, Operation<ItemEntity> original) {
        final var attacker = instance.getLastAttacker();
        if (!(attacker instanceof ServerPlayer serverPlayer)) return original.call(stack);

        boolean hasTelekinesis = TelekinesisUtils.handleTelekinesis(TelekinesisPolicy.MobDrops, serverPlayer, null, player -> {
            if (!player.addItem(stack.asItem().getDefaultInstance())) instance.spawnAtLocation(stack);
        });

        if (!hasTelekinesis) return original.call(stack);
        else return null;
    }
}