package dev.nyon.telekinesis.mixins;

import dev.nyon.telekinesis.TelekinesisPolicy;
import dev.nyon.telekinesis.utils.TelekinesisUtils;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SnowGolem.class)
public class SnowgolemMixin {

    @Redirect(
        method = "mobInteract",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/animal/SnowGolem;shear(Lnet/minecraft/sounds/SoundSource;)V"
        )
    )
    public void manipulateWoolDrops(
        SnowGolem instance,
        SoundSource soundSource,
        Player player,
        InteractionHand interactionHand
    ) {
        instance.level()
            .playSound(null, instance, SoundEvents.SNOW_GOLEM_SHEAR, soundSource, 1.0F, 1.0F);
        if (!instance.level()
            .isClientSide()) {
            instance.setPumpkin(false);

            ItemStack item = new ItemStack(Items.CARVED_PUMPKIN);

            boolean hasTelekinesis = TelekinesisUtils.handleTelekinesis(TelekinesisPolicy.ShearingDrops,
                player,
                serverPlayer -> {
                    if (!serverPlayer.addItem(item)) instance.spawnAtLocation(item, 1.7F);
                }
            );

            if (!hasTelekinesis) instance.spawnAtLocation(item, 1.7F);
        }
    }
}
