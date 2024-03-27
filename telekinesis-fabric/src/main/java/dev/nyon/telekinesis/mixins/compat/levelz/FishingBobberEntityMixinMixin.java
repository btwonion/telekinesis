package dev.nyon.telekinesis.mixins.compat.levelz;

/*
 * @Pseudo
 * @Mixin( value = FishingHook.class,
 * priority = 1500
 * )
 * public abstract class FishingBobberEntityMixinMixin {
 * @Shadow
 * @Nullable public abstract Player getPlayerOwner();
 * @TargetHandler( mixin = "net.levelz.mixin.entity.FishingBobberEntityMixin",
 * name = "useMixin"
 * )
 * @WrapWithCondition( method = "@MixinSquared:Handler",
 * at = @At(
 * value = "INVOKE",
 * target = "net/levelz/entity/LevelExperienceOrbEntity.spawn (Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/phys/Vec3;I)V"
 * )
 * )
 * private boolean redirectExp(
 * ServerLevel world,
 * Vec3 pos,
 * int amount,
 * ItemStack usedItem,
 * CallbackInfoReturnable<Integer> info
 * ) {
 * if (!(getPlayerOwner() instanceof ServerPlayer _serverPlayer)) return true;
 * final var hasTelekinesis = TelekinesisUtils.handleTelekinesis(TelekinesisPolicy.ExpDrops,
 * _serverPlayer,
 * usedItem,
 * serverPlayer -> ((PlayerSyncAccess) serverPlayer).addLevelExperience(amount)
 * );
 * return !hasTelekinesis;
 * }
 * }
 */
