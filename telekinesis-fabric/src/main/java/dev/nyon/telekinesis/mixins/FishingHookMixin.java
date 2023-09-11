package dev.nyon.telekinesis.mixins;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import dev.nyon.telekinesis.TelekinesisPolicy;
import dev.nyon.telekinesis.utils.PlayerUtils;
import dev.nyon.telekinesis.utils.TelekinesisUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FishingHook.class)
public abstract class FishingHookMixin {

    @Shadow
    @Nullable
    public abstract Player getPlayerOwner();

    @WrapWithCondition(
        method = "retrieve",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"
        )
    )
    private boolean redirectFishingHookDrops(
        Level instance,
        Entity entity,
        ItemStack stack
    ) {
        if (!(getPlayerOwner() instanceof ServerPlayer _serverPlayer)) return true;

        if (entity instanceof ExperienceOrb expOrb) {
            final var hasTelekinesis = TelekinesisUtils.handleTelekinesis(
                TelekinesisPolicy.ExpDrops,
                _serverPlayer,
                stack,
                serverPlayer -> {
                    PlayerUtils.addExpToPlayer(serverPlayer, expOrb.getValue());
                }
            );

            return !hasTelekinesis;
        }

        if (entity instanceof ItemEntity itemEntity) {
            final var hasTelekinesis = TelekinesisUtils.handleTelekinesis(
                TelekinesisPolicy.FishingDrops,
                _serverPlayer,
                stack,
                serverPlayer -> {
                    if (!serverPlayer.addItem(itemEntity.getItem())) instance.addFreshEntity(itemEntity);
                }
            );

            return !hasTelekinesis;
        }
        return true;
    }
}
