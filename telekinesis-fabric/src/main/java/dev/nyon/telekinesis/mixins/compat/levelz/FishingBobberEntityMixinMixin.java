package dev.nyon.telekinesis.mixins.compat.levelz;

import com.bawnorton.mixinsquared.TargetHandler;
import com.llamalad7.mixinextras.injector.WrapWithCondition;
import dev.nyon.telekinesis.TelekinesisPolicy;
import dev.nyon.telekinesis.utils.PlayerUtils;
import dev.nyon.telekinesis.utils.TelekinesisUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(value = FishingHook.class, priority = 1500)
public abstract class FishingBobberEntityMixinMixin {

    @Shadow @Nullable
    public abstract Player getPlayerOwner();

    @TargetHandler(
        mixin = "net.levelz.mixin.entity.FishingBobberEntityMixin",
        name = "useMixin"
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
        int amount,
        ItemStack usedItem,
        CallbackInfoReturnable<Integer> info
    ) {
        if (!(getPlayerOwner() instanceof ServerPlayer _serverPlayer)) return true;
        final var hasTelekinesis = TelekinesisUtils.handleTelekinesis(
            TelekinesisPolicy.ExpDrops,
            _serverPlayer,
            usedItem,
            serverPlayer -> PlayerUtils.addExpToPlayer(serverPlayer, amount));
        return !hasTelekinesis;
    }
}
