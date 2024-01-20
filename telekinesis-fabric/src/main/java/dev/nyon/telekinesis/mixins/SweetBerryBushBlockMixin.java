package dev.nyon.telekinesis.mixins;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import dev.nyon.telekinesis.TelekinesisPolicy;
import dev.nyon.telekinesis.utils.TelekinesisUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SweetBerryBushBlock.class)
public class SweetBerryBushBlockMixin {
    @WrapWithCondition(
        method = "use",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/SweetBerryBushBlock;popResource(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/item/ItemStack;)V"
        )
    )
    private boolean manipulateBerryDrops(
        Level level,
        BlockPos blockPos,
        ItemStack itemStack,
        BlockState state,
        Level _level,
        BlockPos pos,
        Player player,
        InteractionHand hand,
        BlockHitResult hit
    ) {
        if (!(player instanceof ServerPlayer serverPlayer)) return true;

        boolean hasTelekinesis = TelekinesisUtils.handleTelekinesis(TelekinesisPolicy.BlockDrops,
            serverPlayer,
            serverPlayer.getItemInHand(hand),
            cPlayer -> {
                if (!player.addItem(itemStack)) Block.popResource(level, pos, itemStack);
            });

        return !hasTelekinesis;
    }
}