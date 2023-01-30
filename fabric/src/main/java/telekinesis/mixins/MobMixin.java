package telekinesis.mixins;

import telekinesis.check.TelekinesisUtils;
import dev.nyon.telekinesis.config.ConfigKt;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Mob.class)
public class MobMixin {

    @Redirect(
        method = "dropCustomDeathLoot",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/Mob;spawnAtLocation(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/entity/item/ItemEntity;"
        )
    )
    public ItemEntity checkDrop(Mob instance, ItemStack itemStack, DamageSource damageSource) {
        Mob mob = (Mob) (Object) this;
        var telekinesisResult = TelekinesisUtils.hasNoTelekinesis(damageSource, mob);
        if (
            !ConfigKt.getConfig().getMobDrops()
            || (telekinesisResult.component1() && !ConfigKt.getConfig().getOnByDefault())
            || telekinesisResult.component2() == null
        ) return mob.spawnAtLocation(itemStack);
        var player = telekinesisResult.component2();
        if (!player.addItem(itemStack)) return mob.spawnAtLocation(itemStack);
        return null;
    }
}
