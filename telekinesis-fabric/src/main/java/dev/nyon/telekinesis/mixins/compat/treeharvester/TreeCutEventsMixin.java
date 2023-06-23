package dev.nyon.telekinesis.mixins.compat.treeharvester;

import dev.nyon.telekinesis.TelekinesisConfigKt;
import dev.nyon.telekinesis.TelekinesisKt;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Pseudo
@Mixin(targets = "com.natamus.treeharvester_common_fabric.events.TreeCutEvents")
public class TreeCutEventsMixin {

    @Redirect(
        method = "onTreeHarvest",
        at = @At(
            value = "INVOKE",
            target = "Lcom/natamus/collective_common_fabric/functions/BlockFunctions;dropBlock(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)V"
        )
    )
    private static void redirectDrop(Level level, BlockPos blockPos, Level _level, Player player, BlockPos bpos, BlockState state, BlockEntity blockEntity) {
        System.out.println("sadawdadwda");

        if (
            (
                EnchantmentHelper.getItemEnchantmentLevel(TelekinesisKt.getTelekinesis(), player.getItemInHand(InteractionHand.MAIN_HAND)) == 0
                    && !TelekinesisConfigKt.getConfig().getOnByDefault()
            )
                || !TelekinesisConfigKt.getConfig().getBlockDrops()
                || !(level instanceof ServerLevel serverLevel)
        ) return;

        BlockState blockState = level.getBlockState(blockPos);
        BlockEntity tileEntity = level.getBlockEntity(blockPos);

        List<ItemStack> drops = Block.getDrops(blockState, serverLevel, blockPos, tileEntity);

        drops.forEach(item -> {
            if (!player.addItem(item)) Block.popResource(serverLevel, blockPos, item);
        });

        serverLevel.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 3);
    }

    @Inject(
        method = "onTreeHarvest",
        at = @At(
            value = "INVOKE",
            target = "Lcom/natamus/collective_common_fabric/functions/BlockFunctions;dropBlock(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)V"
        )
    )
    private static void testDrop(Level level, Player player, BlockPos bpos, BlockState state, BlockEntity blockEntity, CallbackInfoReturnable<Boolean> cir) {
        System.out.println("cooooll");
    }
}