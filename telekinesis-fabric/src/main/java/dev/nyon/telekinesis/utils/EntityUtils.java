package dev.nyon.telekinesis.utils;

import dev.nyon.telekinesis.TelekinesisPolicy;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.concurrent.atomic.AtomicReference;

public class EntityUtils {

    public static boolean spawnAtLocationInject(
        LivingEntity entity,
        ItemLike item
    ) {
        return spawnAtLocationInject(
            entity,
            item.asItem()
                .getDefaultInstance()
        );
    }

    public static boolean spawnAtLocationInject(
        LivingEntity entity,
        ItemStack item
    ) {
        final var attacker = entity.getLastAttacker();
        if (!(attacker instanceof ServerPlayer serverPlayer)) return true;

        boolean hasTelekinesis = TelekinesisUtils.handleTelekinesis(TelekinesisPolicy.MobDrops,
            serverPlayer,
            serverPlayer.getMainHandItem(),
            player -> {
                if (!player.addItem(item)) entity.spawnAtLocation(item);
            }
        );

        return !hasTelekinesis;
    }

    public static Item getDropItemInject(
        Item original,
        DamageSource source
    ) {
        final var attacker = source.getEntity();
        if (!(attacker instanceof ServerPlayer)) return original;

        AtomicReference<Item> toReturn = new AtomicReference<>(original);

        TelekinesisUtils.handleTelekinesis(TelekinesisPolicy.VehicleDrops, source, player -> {
            if (player.addItem(original.asItem()
                .getDefaultInstance())) toReturn.set(ItemStack.EMPTY.getItem());
        });

        return toReturn.get();
    }
}
