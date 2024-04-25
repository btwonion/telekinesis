package dev.nyon.telekinesis.mixins.compat.levelz;

/*
 * @Pseudo
 * @Mixin(net.levelz.entity.LevelExperienceOrbEntity.class) public class LevelExperienceOrbEntityMixin {
 * @WrapWithCondition( method = "spawn",
 * at = @At(
 * value = "INVOKE",
 * target = "Lnet/minecraft/server/level/ServerLevel;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"
 * )
 * )
 * private static boolean redirectSpawnOnTelekinesis(
 * ServerLevel level,
 * Entity entity
 * ) {
 * if (!(entity instanceof LevelExperienceOrbEntity expOrb)) return true;
 * <p>
 * Player target = ((LevelExperienceOrbAccessor) expOrb).getTarget();
 * if (target == null || target.distanceToSqr(expOrb) > 64.0) {
 * target = level.getNearestPlayer(expOrb, 8.0);
 * }
 * if (!(target instanceof ServerPlayer _serverPlayer)) return true;
 * <p>
 * final var hasTelekinesis = TelekinesisUtils.handleTelekinesis(
 * TelekinesisPolicy.ExpDrops,
 * _serverPlayer,
 * null,
 * serverPlayer -> ((LevelExperienceOrbAccessor) expOrb).invokeGetClumpedMap()
 * .forEach((value, amount) -> ((PlayerSyncAccess) serverPlayer).addLevelExperience(value * amount))
 * );
 * return !hasTelekinesis;
 * }
 * }
 */