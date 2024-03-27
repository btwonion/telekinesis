package dev.nyon.telekinesis.mixins.compat.levelz;

/*
@Pseudo
@Mixin(
    value = Block.class,
    priority = 1500
)
public abstract class BlockMixinMixin {
    @Unique
    private static @Nullable ServerPlayer serverPlayer;

    @TargetHandler(
        mixin = "net.levelz.mixin.block.BlockMixin",
        name = "getDroppedStacksMixin"
    )
    @WrapWithCondition(
        method = "@MixinSquared:Handler",
        at = @At(
            value = "INVOKE",
            target = "net/minecraft/world/level/block/Block.popResource (Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/item/ItemStack;)V"
        )
    )
    private static boolean redirectDroppedStacks(
        Level world,
        BlockPos pos,
        ItemStack stack,
        BlockState state,
        ServerLevel world1,
        BlockPos pos1,
        @Nullable BlockEntity blockEntity,
        @Nullable Entity entity,
        ItemStack tool,
        CallbackInfoReturnable<List<ItemStack>> info,
        LootParams.Builder builder
    ) {
        if (!(entity instanceof ServerPlayer _serverPlayer)) return true;
        final var hasTelekinesis = TelekinesisUtils.handleTelekinesis(TelekinesisPolicy.BlockDrops,
            _serverPlayer,
            tool,
            serverPlayer -> {
                if (!_serverPlayer.addItem(stack)) Block.popResource(world, pos, stack);
            }
        );
        return !hasTelekinesis;
    }

    @TargetHandler(
        mixin = "net.levelz.mixin.block.BlockMixin",
        name = "dropExperienceMixin"
    )
    @WrapWithCondition(
        method = "@MixinSquared:Handler",
        at = @At(
            value = "INVOKE",
            target = "net/levelz/entity/LevelExperienceOrbEntity.spawn (Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/phys/Vec3;I)V"
        )
    )
    private static boolean redirectExp(
        ServerLevel world,
        Vec3 pos,
        int amount
    ) {
        final var hasTelekinesis = TelekinesisUtils.handleTelekinesis(TelekinesisPolicy.ExpDrops,
            serverPlayer,
            null,
            serverPlayer -> ((PlayerSyncAccess) serverPlayer).addLevelExperience(amount)
        );
        return !hasTelekinesis;
    }

    @Inject(
        method = "playerWillDestroy",
        at = { @At("HEAD") }
    )
    private void onDestroy(
        Level world,
        BlockPos pos,
        BlockState state,
        Player player,
        CallbackInfo info
    ) {
        if (!world.isClientSide) {
            serverPlayer = (ServerPlayer) player;
        }
    }
}
*/