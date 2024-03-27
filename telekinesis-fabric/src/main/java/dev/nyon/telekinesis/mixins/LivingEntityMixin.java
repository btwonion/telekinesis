package dev.nyon.telekinesis.mixins;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import dev.nyon.telekinesis.TelekinesisPolicy;
import dev.nyon.telekinesis.utils.PlayerUtils;
import dev.nyon.telekinesis.utils.TelekinesisUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.function.Consumer;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Unique
    final LivingEntity livingEntity = (LivingEntity) (Object) this;

    @WrapWithCondition(
        method = "dropExperience",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/ExperienceOrb;award(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/phys/Vec3;I)V"
        )
    )
    public boolean redirectExp(
        ServerLevel world,
        Vec3 pos,
        int amount
    ) {
        final var attacker = livingEntity.getLastAttacker();
        if (!(attacker instanceof ServerPlayer serverPlayer)) return true;

        boolean hasTelekinesis = TelekinesisUtils.handleTelekinesis(TelekinesisPolicy.ExpDrops,
            serverPlayer,
            serverPlayer.getMainHandItem(),
            player -> PlayerUtils.addExpToPlayer(player, amount)
        );

        return !hasTelekinesis;
    }

    @ModifyArgs(
        method = "dropFromLootTable",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/storage/loot/LootTable;getRandomItems(Lnet/minecraft/world/level/storage/loot/LootParams;JLjava/util/function/Consumer;)V"
        )
    )
    public void redirectCommonDrops(
        Args args,
        DamageSource damageSource,
        boolean bl
    ) {
        args.<Consumer<ItemStack>>set(2, item -> {
            boolean hasTelekinesis = TelekinesisUtils.handleTelekinesis(TelekinesisPolicy.MobDrops,
                damageSource,
                player -> {
                    if (!player.addItem(item)) livingEntity.spawnAtLocation(item);
                }
            );

            if (!hasTelekinesis) livingEntity.spawnAtLocation(item);
        });
    }
}
