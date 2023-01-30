package telekinesis.mixins;

import dev.nyon.telekinesis.TelekinesisKt;
import dev.nyon.telekinesis.config.ConfigKt;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SnowGolem.class)
public class SnowGolemMixin {

    @Redirect(
        method = "mobInteract",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/animal/SnowGolem;shear(Lnet/minecraft/sounds/SoundSource;)V"
        )
    )
    public void manipulateWoolDrops(SnowGolem instance, SoundSource soundSource, Player player, InteractionHand interactionHand) {
        SnowGolem golem = (SnowGolem) (Object) this;
        ItemStack itemStack = player.getItemInHand(interactionHand);
        golem.level.playSound(null, golem, SoundEvents.SNOW_GOLEM_SHEAR, soundSource, 1.0F, 1.0F);
        if (!golem.level.isClientSide()) {
            var item = new ItemStack(Items.CARVED_PUMPKIN);
            golem.setPumpkin(false);
            if (
                !ConfigKt.getConfig().getShearingDrops()
                || (
                    EnchantmentHelper.getItemEnchantmentLevel(TelekinesisKt.getTelekinesis(), itemStack) == 0
                        && !ConfigKt.getConfig().getOnByDefault()
                )
                    || !player.getInventory().add(item)
            )
                golem.spawnAtLocation(item, 1.7F);
        }
    }
}
