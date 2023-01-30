package telekinesis.mixins;

import telekinesis.check.TelekinesisUtils;
import dev.nyon.telekinesis.config.ConfigKt;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EnderMan.class)
public class EnderManMixin {

    @Redirect(
        method = "dropCustomDeathLoot",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/monster/EnderMan;spawnAtLocation(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/entity/item/ItemEntity;"
        )
    )
    public ItemEntity manipulateDrops(EnderMan instance, ItemStack itemStack, DamageSource damageSource, int i, boolean bl) {
        var enderman = (EnderMan) (Object) this;
        var telekinesisResult = TelekinesisUtils.hasNoTelekinesis(damageSource, enderman);
        if (
            !ConfigKt.getConfig().getMobDrops()
            || (telekinesisResult.component1() && !ConfigKt.getConfig().getOnByDefault())
            || telekinesisResult.component2() == null
        ) return enderman.spawnAtLocation(itemStack);
        var player = telekinesisResult.component2();
        if (!player.addItem(itemStack)) return enderman.spawnAtLocation(itemStack);
        return null;
    }
}
