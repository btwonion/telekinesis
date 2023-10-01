package dev.nyon.telekinesis.mixins.compat.levelz;

import com.bawnorton.mixinsquared.TargetHandler;
import com.llamalad7.mixinextras.injector.WrapWithCondition;
import dev.nyon.telekinesis.TelekinesisPolicy;
import dev.nyon.telekinesis.utils.PlayerUtils;
import dev.nyon.telekinesis.utils.TelekinesisUtils;
import net.levelz.access.PlayerSyncAccess;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Pseudo
@Mixin(value = EnderDragon.class, priority = 1500)
public class EnderDragonEntityMixinMixin {

    @Unique
    final EnderDragon instance = (EnderDragon) (Object) this;

    @TargetHandler(
        mixin = "net.levelz.mixin.entity.EnderDragonEntityMixin",
        name = "updatePostDeathMixin"
    )
    @WrapWithCondition(
        method = "@MixinSquared:Handler",
        at = @At(
            value = "INVOKE",
            target = "net/levelz/entity/LevelExperienceOrbEntity.spawn (Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/phys/Vec3;I)V"
        )
    )
    private boolean redirectExp(
        ServerLevel world,
        Vec3 pos,
        int amount
    ) {
        if (!(instance.getLastAttacker() instanceof ServerPlayer _serverPlayer)) return true;
        final var hasTelekinesis = TelekinesisUtils.handleTelekinesis(
            TelekinesisPolicy.ExpDrops,
            _serverPlayer,
            null,
            serverPlayer -> ((PlayerSyncAccess) serverPlayer).addLevelExperience(amount));
        return !hasTelekinesis;
    }

    @TargetHandler(
        mixin = "net.levelz.mixin.entity.EnderDragonEntityMixin",
        name = "updatePostDeathXPMixin"
    )
    @WrapWithCondition(
        method = "@MixinSquared:Handler",
        at = @At(
            value = "INVOKE",
            target = "net/levelz/entity/LevelExperienceOrbEntity.spawn (Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/phys/Vec3;I)V"
        )
    )
    private boolean redirectExp1(
        ServerLevel world,
        Vec3 pos,
        int amount
    ) {
        if (!(instance.getLastAttacker() instanceof ServerPlayer _serverPlayer)) return true;
        final var hasTelekinesis = TelekinesisUtils.handleTelekinesis(
            TelekinesisPolicy.ExpDrops,
            _serverPlayer,
            null,
            serverPlayer -> ((PlayerSyncAccess) serverPlayer).addLevelExperience(amount));
        return !hasTelekinesis;
    }
}
