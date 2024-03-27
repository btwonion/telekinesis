package dev.nyon.telekinesis.mixins;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import dev.nyon.telekinesis.utils.EntityUtils;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EnderMan.class)
public class EnderManMixin {

    @WrapWithCondition(
        method = "dropCustomDeathLoot",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/monster/EnderMan;spawnAtLocation(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/entity/item/ItemEntity;"
        )
    )
    public boolean redirectEquipmentDrop(
        EnderMan instance,
        ItemStack stack,
        DamageSource damageSource,
        int lootingMultiplier,
        boolean allowDrops
    ) {
        return EntityUtils.spawnAtLocationInject(instance, stack);
    }
}
