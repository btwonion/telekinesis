package dev.nyon.telekinesis.mixins;

import dev.nyon.telekinesis.TelekinesisPolicy;
import dev.nyon.telekinesis.utils.PlayerUtils;
import dev.nyon.telekinesis.utils.TelekinesisUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.function.Consumer;

@Mixin(Block.class)
public abstract class BlockMixin {

    @ModifyArgs(
        method = "dropResources(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/item/ItemStack;)V",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/List;forEach(Ljava/util/function/Consumer;)V"
        )
    )
    private static void redirectDrops(
        Args args,
        BlockState blockState,
        Level level,
        BlockPos blockPos,
        @Nullable BlockEntity blockEntity,
        @Nullable Entity entity,
        ItemStack itemStack
    ) {
        args.<Consumer<ItemStack>>set(0, item -> {
            if (!TelekinesisUtils.handleTelekinesisBlock(
                TelekinesisPolicy.BlockDrops,
                entity,
                itemStack,
                player -> {
                    if (!player.addItem(item)) {
                        Block.popResource(level, blockPos, item);
                        System.out.println("dont know why");
                    }
                })
            ) Block.popResource(level, blockPos, item);
        });
    }

    @Inject(
        method = "dropResources(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/item/ItemStack;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/state/BlockState;spawnAfterBreak(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/item/ItemStack;Z)V"
        ),
        cancellable = true
    )
    private static void manipulateDrops(
        BlockState blockState, Level level, BlockPos blockPos, BlockEntity blockEntity, Entity entity, ItemStack itemStack, CallbackInfo ci
    ) {
        if (!(level instanceof ServerLevel serverLevel)) return;
        Block block = blockState.getBlock();
        if (EnchantmentHelper.hasSilkTouch(itemStack)) return;
        boolean hasTelekinesis = TelekinesisUtils.handleTelekinesisBlock(TelekinesisPolicy.ExpDrops, entity, itemStack, player -> {
            int expToAdd = 0;
            if (block instanceof DropExperienceBlock expBlock)
                expToAdd = ((DropExperienceBlockAccessor) expBlock).getXpRange().sample(level.random);
            if (block instanceof RedStoneOreBlock) expToAdd = 1 + level.random.nextInt(5);
            if (block instanceof SculkCatalystBlock catalystBlock)
                expToAdd = ((CatalystBlockAccessor) catalystBlock).getXpRange().sample(level.random);
            if (block instanceof SculkSensorBlock || block instanceof SculkShriekerBlock)
                expToAdd = ConstantInt.of(5).sample(level.random);
            if (block instanceof SpawnerBlock) expToAdd = level.random.nextInt(15) + level.random.nextInt(15);
            if (block instanceof InfestedBlock infestedBlock)
                infestedBlock.spawnAfterBreak(blockState, serverLevel, blockPos, itemStack, true);
            PlayerUtils.addExpToPlayer(player, expToAdd);
        });

        if (hasTelekinesis) ci.cancel();
    }
}