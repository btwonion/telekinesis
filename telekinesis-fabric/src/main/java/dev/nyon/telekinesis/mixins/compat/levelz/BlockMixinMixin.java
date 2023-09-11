package dev.nyon.telekinesis.mixins.compat.levelz;

import com.bawnorton.mixinsquared.TargetHandler;
import com.llamalad7.mixinextras.injector.WrapWithCondition;
import dev.nyon.telekinesis.TelekinesisPolicy;
import dev.nyon.telekinesis.utils.PlayerUtils;
import dev.nyon.telekinesis.utils.TelekinesisUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Pseudo
@Mixin(value = Block.class, priority = 1500)
public abstract class BlockMixinMixin {
    @Unique
    private static @Nullable ServerPlayer serverPlayer;

    @TargetHandler(
        mixin = "net.levelz.mixin.block.BlockMixin",
        name = "getDroppedStacksMixin"
    )
    @WrapWithCondition(
        method = "@MixinSquared:Handler",
        at = @At(
            value = "INVOKE",
            target = "net/minecraft/world/level/block/Block.popResource (Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/item/ItemStack;)V"
        )
    )
    private static boolean redirectDroppedStacks(
        Level world,
        BlockPos pos,
        ItemStack stack,
        BlockState state,
        ServerLevel world1,
        BlockPos pos1,
        @Nullable BlockEntity blockEntity,
        @Nullable Entity entity,
        ItemStack tool,
        CallbackInfoReturnable<List<ItemStack>> info,
        LootParams.Builder builder
    ) {
        if (!(entity instanceof ServerPlayer _serverPlayer)) return true;
        final var hasTelekinesis = TelekinesisUtils.handleTelekinesis(
            TelekinesisPolicy.BlockDrops,
            _serverPlayer,
            tool,
            serverPlayer -> {
                if (!_serverPlayer.addItem(stack)) Block.popResource(world, pos, stack);
            });
        return !hasTelekinesis;
    }

    @TargetHandler(
        mixin = "net.levelz.mixin.block.BlockMixin",
        name = "dropExperienceMixin"
    )
    @WrapWithCondition(
        method = "@MixinSquared:Handler",
        at = @At(
            value = "INVOKE",
            target = "net/levelz/entity/LevelExperienceOrbEntity.spawn (Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/phys/Vec3;I)V"
        )
    )
    private static boolean redirectExp(
        ServerLevel world,
        Vec3 pos,
        int amount
    ) {
        final var hasTelekinesis = TelekinesisUtils.handleTelekinesis(
            TelekinesisPolicy.ExpDrops,
            serverPlayer,
            null,
            serverPlayer -> PlayerUtils.addExpToPlayer(serverPlayer, amount));
        return !hasTelekinesis;
    }

    @Inject(
        method = "playerWillDestroy",
        at = {@At("HEAD")}
    )
    private void onDestroy(Level world, BlockPos pos, BlockState state, Player player, CallbackInfo info) {
        if (!world.isClientSide) {
            serverPlayer = (ServerPlayer) player;
        }
    }
}
