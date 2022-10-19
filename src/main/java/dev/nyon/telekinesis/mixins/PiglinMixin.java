package dev.nyon.telekinesis.mixins;

import dev.nyon.telekinesis.check.TelekinesisCheck;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.player.Player;
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
        if (TelekinesisCheck.hasNoTelekinesis(damageSource)) return;
        var player = (Player) damageSource.getEntity();
        var piglin = (Piglin) (Object) this;
        piglin.getInventory().removeAllItems().forEach(item -> {
            if (!player.addItem(item)) piglin.spawnAtLocation(item);
        });

        ci.cancel();
    }
}
