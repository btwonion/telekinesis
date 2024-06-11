package dev.nyon.telekinesis.mixins;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import dev.nyon.telekinesis.DropEvent;
import dev.nyon.telekinesis.TelekinesisPolicy;
import dev.nyon.telekinesis.utils.MixinHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.apache.commons.lang3.mutable.MutableInt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Mixin(SweetBerryBushBlock.class)
public class SweetBerryBushBlockMixin {

    @WrapWithCondition(
        method = /*? >1.20.5 {*/ "useWithoutItem" /*?} else {*//* "use" *//*?}*/,
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/SweetBerryBushBlock;popResource(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/item/ItemStack;)V"
        )
    )
    private boolean manipulateBerryDrops(
        Level level,
        BlockPos blockPos,
        ItemStack itemStack,
        BlockState blockState,
        Level _level,
        BlockPos _blockPos,
        Player player,
        BlockHitResult blockHitResult
    ) {
        if (!(player instanceof ServerPlayer serverPlayer)) return true;

        return MixinHelper.wrapWithConditionPlayerItemSingle(serverPlayer, itemStack);
    }
}
