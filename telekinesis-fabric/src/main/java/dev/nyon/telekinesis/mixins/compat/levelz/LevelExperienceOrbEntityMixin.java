package dev.nyon.telekinesis.mixins.compat.levelz;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import dev.nyon.telekinesis.TelekinesisPolicy;
import dev.nyon.telekinesis.utils.PlayerUtils;
import dev.nyon.telekinesis.utils.TelekinesisUtils;
import net.levelz.entity.LevelExperienceOrbEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

@Pseudo
@Mixin(net.levelz.entity.LevelExperienceOrbEntity.class)
public class LevelExperienceOrbEntityMixin {

    @WrapWithCondition(
        method = "spawn",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/level/ServerLevel;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"
        )
    )
    private static boolean redirectSpawnOnTelekinesis(
        ServerLevel level,
        Entity entity
    ) {
        if (!(entity instanceof LevelExperienceOrbEntity expOrb)) return true;

        Player target = ((LevelExperienceOrbAccessor) expOrb).getTarget();
        if (target == null || target.distanceToSqr(expOrb) > 64.0) {
            target = level.getNearestPlayer(expOrb, 8.0);
        }
        if (!(target instanceof ServerPlayer _serverPlayer)) return true;

        final var hasTelekinesis = TelekinesisUtils.handleTelekinesis(
            TelekinesisPolicy.ExpDrops,
            _serverPlayer,
            null,
            serverPlayer -> PlayerUtils.addExpToPlayer(serverPlayer, expOrb.getExperienceAmount())
        );
        return !hasTelekinesis;
    }
}
