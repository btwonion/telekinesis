package dev.nyon.telekinesis.mixins;

import dev.nyon.telekinesis.config.ConfigKt;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import dev.nyon.telekinesis.check.TelekinesisUtils;

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
        var telekinesisResult = TelekinesisUtils.hasNoTelekinesis(damageSource, (WitherBoss) (Object) this);
        if ((
            !ConfigKt.getConfig().getMobDrops()
                || (telekinesisResult.component1() && !ConfigKt.getConfig().getOnByDefault())
                || telekinesisResult.component2() == null
        )) return;
        var player = telekinesisResult.component2();
        if (!player.addItem(new ItemStack(Items.NETHER_STAR))) return;
        ci.cancel();
    }
}
