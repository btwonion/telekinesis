package dev.nyon.telekinesis.mixins;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import dev.nyon.telekinesis.utils.MixinHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(WitherBoss.class)
public abstract class WitherBossMixin {
    @WrapWithCondition(
        method = "dropCustomDeathLoot",
        at = @At(
            value = "INVOKE",
            target = "net/minecraft/world/entity/boss/wither/WitherBoss.spawnAtLocation (Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/entity/item/ItemEntity;"
        )
    )
    protected boolean redirectEquipmentDrop(
        WitherBoss instance,
        ItemLike itemLike,
        ServerLevel serverLevel,
        DamageSource damageSource,
        boolean bl
    ) {
        return MixinHelper.entityCustomDeathLootSingle(damageSource, new ItemStack(itemLike));
    }
}
