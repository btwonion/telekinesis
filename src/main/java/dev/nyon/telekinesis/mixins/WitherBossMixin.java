package dev.nyon.telekinesis.mixins;

import dev.nyon.telekinesis.check.TelekinesisCheck;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WitherBoss.class)
public abstract class WitherBossMixin {

    @Inject(
        method = "dropCustomDeathLoot",
        at = @At(
            value = "HEAD"
        ),
        cancellable = true
    )
    public void checkDrop(DamageSource damageSource, int i, boolean bl, CallbackInfo ci) {
        var telekinesisResult = TelekinesisCheck.hasNoTelekinesis(damageSource, (WitherBoss) (Object) this);
        if (telekinesisResult.component1()) return;
        var player = telekinesisResult.component2();
        if (!player.addItem(new ItemStack(Items.NETHER_STAR))) return;
        ci.cancel();
    }
}
