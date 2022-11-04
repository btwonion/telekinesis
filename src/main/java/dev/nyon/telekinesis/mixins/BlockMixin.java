package dev.nyon.telekinesis.mixins;

import dev.nyon.telekinesis.TelekinesisKt;
import dev.nyon.telekinesis.check.TelekinesisUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.minecraft.world.level.block.Block.getDrops;
import static net.minecraft.world.level.block.Block.popResource;

@Mixin(Block.class)
public abstract class BlockMixin {

    @Inject(
        method = "dropResources(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/item/ItemStack;)V",
        at = @At(
            value = "HEAD"
        ),
        cancellable = true
    )
    private static void manipulateDrops(
        BlockState blockState, Level level, BlockPos blockPos, BlockEntity blockEntity, Entity entity, ItemStack itemStack, CallbackInfo ci
    ) {
        if (
            !EnchantmentHelper.getEnchantments(itemStack).containsKey(TelekinesisKt.getTelekinesis())
            || !(entity instanceof Player player)
        ) return;
        getDrops(blockState, (ServerLevel) level, blockPos, blockEntity, entity, itemStack).forEach(item -> {
            if (!player.addItem(item)) popResource(level, blockPos, item);
        });

        if (blockState.getBlock() instanceof DropExperienceBlock expBlock && EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, itemStack) == 0)
            TelekinesisUtils.addXPToPlayer(player, ((DropExperienceBlockAccessor) expBlock).getXpRange().sample(level.random));
        else if (blockState.getBlock() instanceof RedStoneOreBlock && EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, itemStack) == 0)
            TelekinesisUtils.addXPToPlayer(player, 1 + level.random.nextInt(5));
        else if (blockState.getBlock() instanceof SculkCatalystBlock catalystBlock && EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, itemStack) == 0)
            TelekinesisUtils.addXPToPlayer(player, ((CatalystBlockAccessor) catalystBlock).getXpRange().sample(level.random));
        else if (blockState.getBlock() instanceof SculkSensorBlock && EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, itemStack) == 0)
            TelekinesisUtils.addXPToPlayer(player, ConstantInt.of(5).sample(level.random));
        else if (blockState.getBlock() instanceof SculkShriekerBlock && EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, itemStack) == 0)
            TelekinesisUtils.addXPToPlayer(player, ConstantInt.of(5).sample(level.random));
        else if (blockState.getBlock() instanceof SpawnerBlock && EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, itemStack) == 0)
            TelekinesisUtils.addXPToPlayer(player, level.random.nextInt(15) + level.random.nextInt(15));
        else if (blockState.getBlock() instanceof InfestedBlock infestedBlock)
            if (level.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS) && EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, itemStack) == 0)
                ((InfestedBlockAccessor) infestedBlock).invokeSpawnInfestation((ServerLevel) level, blockPos);

        ci.cancel();
    }
}