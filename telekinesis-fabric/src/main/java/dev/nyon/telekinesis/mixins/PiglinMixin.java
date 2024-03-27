package dev.nyon.telekinesis.mixins;

import dev.nyon.telekinesis.TelekinesisPolicy;
import dev.nyon.telekinesis.utils.TelekinesisUtils;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import java.util.function.Consumer;

@Mixin(Piglin.class)
public class PiglinMixin {

    @Redirect(
        method = "dropCustomDeathLoot",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/List;forEach(Ljava/util/function/Consumer;)V"
        )
    )
    public void redirectDrops(
        List<ItemStack> instance,
        Consumer consumer,
        DamageSource damageSource,
        int i,
        boolean bl
    ) {
        var piglin = (Piglin) (Object) this;

        boolean hasTelekinesis = TelekinesisUtils.handleTelekinesis(TelekinesisPolicy.MobDrops,
            damageSource,
            player -> instance.forEach(item -> {
                if (!player.addItem(item)) piglin.spawnAtLocation(item);
            })
        );

        if (!hasTelekinesis) instance.forEach(piglin::spawnAtLocation);
    }
}
