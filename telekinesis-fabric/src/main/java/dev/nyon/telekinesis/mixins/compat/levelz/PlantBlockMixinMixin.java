package dev.nyon.telekinesis.mixins.compat.levelz;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import dev.nyon.telekinesis.TelekinesisPolicy;
import dev.nyon.telekinesis.utils.TelekinesisUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

@Pseudo
@Mixin(value = BushBlock.class, priority = 1500)
public class PlantBlockMixinMixin extends Block {

    public PlantBlockMixinMixin(Properties properties) {
        super(properties);
    }

    @WrapWithCondition(
        method = "playerWillDestroy(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/entity/player/Player;)V",
        at = @At(
            value = "INVOKE",
            target = "net/minecraft/world/level/block/Block.popResource (Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/item/ItemStack;)V"
        )
    )
    private boolean checkTelekinesisAndRedirect(
        Level world,
        BlockPos pos,
        ItemStack stack,
        Level world1,
        BlockPos pos1,
        BlockState state,
        Player player
    ) {
        if (!(player instanceof ServerPlayer _serverPlayer)) return true;
        final var hasTelekinesis = TelekinesisUtils.handleTelekinesis(
            TelekinesisPolicy.BlockDrops,
            _serverPlayer,
            null,
            serverPlayer -> {
                if (!serverPlayer.addItem(stack)) Block.popResource(world, pos, stack);
            });
        return !hasTelekinesis;
    }
}