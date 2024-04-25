package dev.nyon.telekinesis.mixins.compat.levelz;

/*
 * @Pseudo
 * @Mixin( value = BushBlock.class,
 * priority = 1500
 * )
 * public class PlantBlockMixinMixin extends Block {
 * <p>
 * public PlantBlockMixinMixin(Properties properties) {
 * super(properties);
 * }
 * @WrapWithCondition( method = "playerWillDestroy(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/entity/player/Player;)V",
 * at = @At(
 * value = "INVOKE",
 * target = "net/minecraft/world/level/block/Block.popResource (Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/item/ItemStack;)V"
 * )
 * )
 * private boolean checkTelekinesisAndRedirect(
 * Level world,
 * BlockPos pos,
 * ItemStack stack,
 * Level world1,
 * BlockPos pos1,
 * BlockState state,
 * Player player
 * ) {
 * if (!(player instanceof ServerPlayer _serverPlayer)) return true;
 * final var hasTelekinesis = TelekinesisUtils.handleTelekinesis(TelekinesisPolicy.BlockDrops,
 * _serverPlayer,
 * null,
 * serverPlayer -> {
 * if (!serverPlayer.addItem(stack)) Block.popResource(world, pos, stack);
 * }
 * );
 * return !hasTelekinesis;
 * }
 * }
 */