package dev.nyon.telekinesis.mixins;

import dev.nyon.telekinesis.check.TelekinesisUtils;
import dev.nyon.telekinesis.config.ConfigKt;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.level.ItemLike;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EnderMan.class)
public class EnderManMixin {

    @Redirect(
        method = "dropCustomDeathLoot",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/monster/EnderMan;spawnAtLocation(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/entity/item/ItemEntity;"
        )
    )
    public ItemEntity manipulateDrops(EnderMan instance, ItemLike itemLike, DamageSource damageSource) {
        var enderman = (EnderMan) (Object) this;
        var telekinesisResult = TelekinesisUtils.hasNoTelekinesis(damageSource, enderman);
        if (
            !ConfigKt.getConfig().getMobDrops()
            || (telekinesisResult.component1() && !ConfigKt.getConfig().getOnByDefault())
        ) return enderman.spawnAtLocation(itemLike);
        var player = telekinesisResult.component2();
        if (!player.addItem(itemLike.asItem().getDefaultInstance())) return enderman.spawnAtLocation(itemLike);
        return null;
    }
}
