package dev.nyon.telekinesis.mixins;

import dev.nyon.telekinesis.TelekinesisKt;
import dev.nyon.telekinesis.config.ConfigKt;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ItemLike;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Map;

@Mixin(Sheep.class)
public abstract class SheepMixin {

    @Shadow
    @Final
    private static Map<DyeColor, ItemLike> ITEM_BY_DYE;

    @Redirect(
        method = "mobInteract",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/animal/Sheep;shear(Lnet/minecraft/sounds/SoundSource;)V"
        )
    )
    public void manipulateWoolDrops(Sheep instance, SoundSource soundSource, Player player, InteractionHand interactionHand) {
        Sheep sheep = (Sheep) (Object) this;
        ItemStack itemStack = player.getItemInHand(interactionHand);
        sheep.level.playSound(null, sheep, SoundEvents.SHEEP_SHEAR, soundSource, 1.0F, 1.0F);
        sheep.setSheared(true);
        var random = RandomSource.create();
        int i = 1 + random.nextInt(3);

        for (int j = 0; j < i; ++j) {
            var item = ITEM_BY_DYE.get(sheep.getColor());

            if (
                !ConfigKt.getConfig().getShearingDrops()
                || (
                    EnchantmentHelper.getItemEnchantmentLevel(TelekinesisKt.getTelekinesis(), itemStack) == 0
                        && !ConfigKt.getConfig().getOnByDefault()) || !player.getInventory().add(new ItemStack(item)
                )
            ) {
                ItemEntity itemEntity = sheep.spawnAtLocation(item, 1);
                if (itemEntity != null) {
                    itemEntity.setDeltaMovement(itemEntity.getDeltaMovement().add((random.nextFloat() - random.nextFloat()) * 0.1F, random.nextFloat() * 0.05F, (random.nextFloat() - random.nextFloat()) * 0.1F));
                }
            }
        }
    }
}
