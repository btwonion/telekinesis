package dev.nyon.telekinesis.mixins.compat.treeharvester;

/*
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
    private static void redirectDrop(
        Level level,
        BlockPos blockPos,
        Level _level,
        Player player,
        BlockPos bpos,
        BlockState state,
        BlockEntity blockEntity
    ) {
        if ((EnchantmentHelper.getItemEnchantmentLevel(
            MainKt.getTelekinesis(),
            player.getItemInHand(InteractionHand.MAIN_HAND)
        ) == 0 && !TelekinesisConfigKt.getConfig()
            .getOnByDefault()) || !TelekinesisConfigKt.getConfig()
            .getBlockDrops() || !(level instanceof ServerLevel serverLevel)) return;

        BlockState blockState = level.getBlockState(blockPos);
        BlockEntity tileEntity = level.getBlockEntity(blockPos);

        List<ItemStack> drops = Block.getDrops(blockState, serverLevel, blockPos, tileEntity);

        drops.forEach(item -> {
            if (!player.addItem(item)) Block.popResource(serverLevel, blockPos, item);
        });

        serverLevel.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 3);
    }
}

 */