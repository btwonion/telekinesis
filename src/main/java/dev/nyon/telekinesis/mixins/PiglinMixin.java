package dev.nyon.telekinesis.mixins;

import dev.nyon.telekinesis.check.TelekinesisCheck;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.monster.piglin.Piglin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Piglin.class)
public class PiglinMixin {

    @Inject(
        method = "dropCustomDeathLoot",
        at = @At(
            value = "HEAD"
        ),
        cancellable = true
    )
    public void manipulateDrops(DamageSource damageSource, int i, boolean bl, CallbackInfo ci) {
        var piglin = (Piglin) (Object) this;
        var telekinesisResult = TelekinesisCheck.hasNoTelekinesis(damageSource, piglin);
        if (telekinesisResult.component1()) return;
        var player = telekinesisResult.component2();
        piglin.getInventory().removeAllItems().forEach(item -> {
            if (!player.addItem(item)) piglin.spawnAtLocation(item);
        });

        ci.cancel();
    }
}
