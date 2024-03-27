package dev.nyon.telekinesis.mixins.compat.levelz;

/*
 * @Pseudo
 * @Mixin( value = LivingEntity.class,
 * priority = 1500
 * )
 * public class LivingEntityMixinMixin {
 * @Unique final LivingEntity instance = (LivingEntity) (Object) this;
 * @TargetHandler( mixin = "net.levelz.mixin.entity.LivingEntityMixin",
 * name = "dropXpMixin"
 * )
 * @WrapWithCondition( method = "@MixinSquared:Handler",
 * at = @At(
 * value = "INVOKE",
 * target = "net/levelz/entity/LevelExperienceOrbEntity.spawn (Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/phys/Vec3;I)V"
 * )
 * )
 * protected boolean redirectLevelZExpDrop(
 * ServerLevel world,
 * Vec3 pos,
 * int amount
 * ) {
 * if (!(instance.getLastAttacker() instanceof ServerPlayer _serverPlayer)) return true;
 * System.out.println("wtf" + amount);
 * System.out.println(amount);
 * final var hasTelekinesis = TelekinesisUtils.handleTelekinesis(TelekinesisPolicy.ExpDrops,
 * _serverPlayer,
 * null,
 * serverPlayer -> ((PlayerSyncAccess) serverPlayer).addLevelExperience(amount)
 * );
 * return !hasTelekinesis;
 * }
 * }
 */