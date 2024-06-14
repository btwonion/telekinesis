package dev.nyon.telekinesis.mixins;

import dev.nyon.telekinesis.utils.MixinHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(WitherBoss.class)
public abstract class WitherBossMixin {
    @ModifyArgs(
        method = "dropCustomDeathLoot",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/boss/wither/WitherBoss;spawnAtLocation(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/entity/item/ItemEntity;"
        )
    )
    protected void redirectEquipmentDrop(
        Args args, /*? if >=1.21 {*/ ServerLevel serverLevel, DamageSource damageSource, boolean bl /*?} else {*/ /*DamageSource damageSource, int looting, boolean hitByPlayer *//*?}*/
    ) {
        ItemLike original = args.get(0);

        if (MixinHelper.entityCustomDeathLootSingle(damageSource, new ItemStack(original))) return;
        args.set(0, Items.AIR);
    }
}
