package dev.nyon.telekinesis.mixins.compat.betternether;

/*
 * @Pseudo
 * @Mixin(targets = "org.betterx.betternether.enchantments.RubyFire")
 * public class RubyFireMixin {
 * @WrapWithCondition( method = "getDrops",
 * at = @At(
 * value = "INVOKE",
 * target = "Lorg/betterx/betternether/enchantments/RubyFire;popExperience(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;I)V"
 * )
 * )
 * private static boolean redirectExp(
 * ServerLevel level,
 * BlockPos blockPos,
 * int amount,
 * BlockState brokenBlock,
 * ServerLevel level1,
 * BlockPos blockPos1,
 * Player player,
 * ItemStack breakingItem
 * ) {
 * if (!(player instanceof ServerPlayer _serverPlayer)) return true;
 * final var hasTelekinesis = TelekinesisUtils.handleTelekinesis(
 * TelekinesisPolicy.ExpDrops,
 * _serverPlayer,
 * breakingItem,
 * serverPlayer -> PlayerUtils.addExpToPlayer(serverPlayer, amount));
 * return !hasTelekinesis;
 * }
 * @Redirect( method = "getDrops",
 * at = @At(
 * value = "INVOKE",
 * target = "Ljava/util/List;forEach(Ljava/util/function/Consumer;)V"
 * )
 * )
 * private static void redirectCommonDrops(
 * List<ItemStack> instance, Consumer<ItemStack> consumer, BlockState brokenBlock, ServerLevel level, BlockPos blockPos, Player player, ItemStack breakingItem
 * ) {
 * if (!(player instanceof ServerPlayer _serverPlayer)) {
 * instance.forEach(consumer);
 * return;
 * }
 * boolean hasTelekinesis = TelekinesisUtils.handleTelekinesis(
 * TelekinesisPolicy.MobDrops,
 * _serverPlayer,
 * breakingItem,
 * serverPlayer -> instance.forEach(item -> {
 * if (!serverPlayer.addItem(item)) consumer.accept(item);
 * })
 * );
 * <p>
 * if (!hasTelekinesis) instance.forEach(consumer);
 * }
 * @WrapWithCondition( method = "getDrops",
 * at = @At(
 * value = "INVOKE",
 * target = "Lnet/minecraft/world/level/block/state/BlockState;spawnAfterBreak(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/item/ItemStack;Z)V"
 * )
 * )
 * private static boolean redirectAfterDrops(
 * BlockState instance,
 * ServerLevel world,
 * BlockPos pos,
 * ItemStack tool,
 * boolean dropExperience,
 * BlockState brokenBlock,
 * ServerLevel level,
 * BlockPos blockPos,
 * Player _player,
 * ItemStack breakingItem
 * ) {
 * if (!(_player instanceof ServerPlayer _serverPlayer)) return true;
 * <p>
 * Block block = brokenBlock.getBlock();
 * if (EnchantmentHelper.hasSilkTouch(breakingItem)) return true;
 * boolean hasTelekinesis = TelekinesisUtils.handleTelekinesisBlock(
 * TelekinesisPolicy.ExpDrops,
 * _serverPlayer,
 * breakingItem,
 * player -> {
 * int expToAdd = 0;
 * if (block instanceof DropExperienceBlock expBlock)
 * expToAdd = ((DropExperienceBlockAccessor) expBlock).getXpRange().sample(level.random);
 * if (block instanceof RedStoneOreBlock) expToAdd = 1 + level.random.nextInt(5);
 * if (block instanceof SculkCatalystBlock catalystBlock)
 * expToAdd = ((CatalystBlockAccessor) catalystBlock).getXpRange().sample(level.random);
 * if (block instanceof SculkSensorBlock || block instanceof SculkShriekerBlock)
 * expToAdd = ConstantInt.of(5).sample(level.random);
 * if (block instanceof SpawnerBlock) expToAdd = level.random.nextInt(15) + level.random.nextInt(15);
 * if (block instanceof InfestedBlock infestedBlock)
 * infestedBlock.spawnAfterBreak(brokenBlock, level, blockPos, breakingItem, true);
 * PlayerUtils.addExpToPlayer(player, expToAdd);
 * });
 * <p>
 * return !hasTelekinesis;
 * }
 * }
 */