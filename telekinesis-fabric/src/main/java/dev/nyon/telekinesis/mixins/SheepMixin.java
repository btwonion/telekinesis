package dev.nyon.telekinesis.mixins;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.nyon.telekinesis.utils.MixinHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Sheep.class)
public abstract class SheepMixin {

    @Unique
    private static final ThreadLocal<ServerPlayer> threadLocal = new ThreadLocal<>();

    @WrapOperation(
        method = "mobInteract",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/animal/Sheep;shear(Lnet/minecraft/sounds/SoundSource;)V"
        )
    )
    private void prepareThreadLocalForShearing(
        Sheep instance,
        SoundSource source,
        Operation<Void> original,
        Player _player,
        InteractionHand hand
    ) {
        MixinHelper.prepareShearableServerPlayer(instance, source, original, _player, threadLocal);
    }

    @ModifyExpressionValue(
        method = "shear",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;"
        )
    )
    private Object modifyShearDrops(Object original) {
        ServerPlayer player = threadLocal.get();
        if (!(original instanceof Block block)) return original;
        if (player == null) return original;

        if (MixinHelper.wrapWithConditionPlayerItemSingle(player, new ItemStack(block))) return original;
        else return ItemStack.EMPTY;
    }
}