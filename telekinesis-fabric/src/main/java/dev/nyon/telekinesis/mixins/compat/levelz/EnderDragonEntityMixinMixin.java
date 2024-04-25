package dev.nyon.telekinesis.mixins.compat.levelz;

/*
 * @Pseudo
 * @Mixin( value = EnderDragon.class,
 * priority = 1500
 * )
 * public class EnderDragonEntityMixinMixin {
 * @Unique final EnderDragon instance = (EnderDragon) (Object) this;
 * @TargetHandler( mixin = "net.levelz.mixin.entity.EnderDragonEntityMixin",
 * name = "updatePostDeathMixin"
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
 * int amount
 * ) {
 * if (!(instance.getLastAttacker() instanceof ServerPlayer _serverPlayer)) return true;
 * final var hasTelekinesis = TelekinesisUtils.handleTelekinesis(TelekinesisPolicy.ExpDrops,
 * _serverPlayer,
 * null,
 * serverPlayer -> ((PlayerSyncAccess) serverPlayer).addLevelExperience(amount)
 * );
 * return !hasTelekinesis;
 * }
 * @TargetHandler( mixin = "net.levelz.mixin.entity.EnderDragonEntityMixin",
 * name = "updatePostDeathXPMixin"
 * )
 * @WrapWithCondition( method = "@MixinSquared:Handler",
 * at = @At(
 * value = "INVOKE",
 * target = "net/levelz/entity/LevelExperienceOrbEntity.spawn (Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/phys/Vec3;I)V"
 * )
 * )
 * private boolean redirectExp1(
 * ServerLevel world,
 * Vec3 pos,
 * int amount
 * ) {
 * if (!(instance.getLastAttacker() instanceof ServerPlayer _serverPlayer)) return true;
 * final var hasTelekinesis = TelekinesisUtils.handleTelekinesis(TelekinesisPolicy.ExpDrops,
 * _serverPlayer,
 * null,
 * serverPlayer -> ((PlayerSyncAccess) serverPlayer).addLevelExperience(amount)
 * );
 * return !hasTelekinesis;
 * }
 * }
 */
