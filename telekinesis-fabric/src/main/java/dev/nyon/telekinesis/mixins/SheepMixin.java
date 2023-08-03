package dev.nyon.telekinesis.mixins;

import dev.nyon.telekinesis.TelekinesisPolicy;
import dev.nyon.telekinesis.utils.TelekinesisUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Map;

@Mixin(Sheep.class)
public abstract class SheepMixin {

    @Shadow
    @Final
    private static Map<DyeColor, ItemLike> ITEM_BY_DYE;
    private final RandomSource random = RandomSource.create();


    @Redirect(method = "mobInteract", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/Sheep;shear(Lnet/minecraft/sounds/SoundSource;)V"))
    public void manipulateWoolDrops(Sheep instance, SoundSource soundSource, Player player, InteractionHand interactionHand) {
        instance.level().playSound(null, instance, SoundEvents.SHEEP_SHEAR, soundSource, 1.0F, 1.0F);
        instance.setSheared(true);
        int i = 1 + random.nextInt(3);

        if (!(player instanceof ServerPlayer serverPlayerr)) {
            dropAllNormally(i, instance);
            return;
        }

        boolean hasTelekinesis = TelekinesisUtils.handleTelekinesis(TelekinesisPolicy.ShearingDrops, serverPlayerr, player.getItemInHand(interactionHand), serverPlayer -> {
            for (int j = 0; j < i; ++j) {
                if (!serverPlayer.addItem(new ItemStack(ITEM_BY_DYE.get(instance.getColor()), 1))) {
                    ItemEntity entity = instance.spawnAtLocation(ITEM_BY_DYE.get(instance.getColor()), 1);
                    entity.setDeltaMovement(entity.getDeltaMovement().add((random.nextFloat() - random.nextFloat()) * 0.1F, random.nextFloat() * 0.05F, (random.nextFloat() - random.nextFloat()) * 0.1F));
                }
            }
        });

        if (!hasTelekinesis) dropAllNormally(i, instance);
    }

    @Unique
    void dropAllNormally(int i, Sheep instance) {
        for (int j = 0; j < i; ++j) {
            ItemEntity entity = instance.spawnAtLocation(ITEM_BY_DYE.get(instance.getColor()), 1);
            entity.setDeltaMovement(entity.getDeltaMovement().add((random.nextFloat() - random.nextFloat()) * 0.1F, random.nextFloat() * 0.05F, (random.nextFloat() - random.nextFloat()) * 0.1F));
        }
    }
}