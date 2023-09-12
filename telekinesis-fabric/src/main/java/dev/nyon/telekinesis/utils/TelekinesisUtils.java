package dev.nyon.telekinesis.utils;

import dev.nyon.telekinesis.MainKt;
import dev.nyon.telekinesis.TelekinesisConfigKt;
import dev.nyon.telekinesis.TelekinesisPolicy;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class TelekinesisUtils {

    public static boolean handleTelekinesisBlock(TelekinesisPolicy neededPolicy, Entity entity, ItemStack tool, Consumer<ServerPlayer> consumer) {
        if (!(entity instanceof ServerPlayer serverPlayer)) return false;
        return handleTelekinesis(neededPolicy, serverPlayer, tool, consumer);
    }

    public static boolean handleTelekinesis(TelekinesisPolicy neededPolicy, LivingEntity target, Consumer<ServerPlayer> consumer) {
        LivingEntity attacker = target.getLastAttacker();
        if (attacker == null) return false;
        if (!(attacker instanceof ServerPlayer serverPlayer)) return false;
        return handleTelekinesis(neededPolicy, serverPlayer, null, consumer);
    }

    public static boolean handleTelekinesis(TelekinesisPolicy neededPolicy, DamageSource source, Consumer<ServerPlayer> consumer) {
        if (source == null) return false;
        Entity attacker = source.getEntity();
        if (attacker == null) return false;
        if (!(attacker instanceof ServerPlayer serverPlayer)) return false;
        return handleTelekinesis(neededPolicy, serverPlayer, null, consumer);
    }


    public static boolean handleTelekinesis(TelekinesisPolicy neededPolicy, ServerPlayer player, @Nullable ItemStack itemStack, Consumer<ServerPlayer> consumer) {
        if (!neededPolicy.isEnabled()) return false;
        if (!playerMeetsConditions(neededPolicy, player, itemStack)) return false;

        consumer.accept(player);
        return true;
    }

    private static boolean playerMeetsConditions(TelekinesisPolicy policy, ServerPlayer player, @Nullable ItemStack itemStack) {
        boolean conditionsMet = false;

        boolean isEnabledByDefault = TelekinesisConfigKt.getConfig().getOnByDefault();

        boolean hasArmorTelekinesis = player.getInventory().armor.stream().allMatch(item -> EnchantmentHelper.getItemEnchantmentLevel(MainKt.getTelekinesis(), item) > 0);
        boolean hasMainHandTelekinesis = (itemStack != null && EnchantmentHelper.getItemEnchantmentLevel(MainKt.getTelekinesis(), itemStack) > 0) ||
            EnchantmentHelper.getItemEnchantmentLevel(MainKt.getTelekinesis(), player.getMainHandItem()) > 0;
        boolean hasOffHandTelekinesis = EnchantmentHelper.getItemEnchantmentLevel(MainKt.getTelekinesis(), player.getOffhandItem()) > 0;

        if (isEnabledByDefault) conditionsMet = true;
        else switch (policy) {
            case ExpDrops -> conditionsMet = hasArmorTelekinesis || hasMainHandTelekinesis || hasOffHandTelekinesis;
            case MobDrops, ShearingDrops, VehicleDrops, FishingDrops ->
                conditionsMet = hasMainHandTelekinesis || hasOffHandTelekinesis;
            case BlockDrops -> conditionsMet = hasMainHandTelekinesis;
        }

        if (TelekinesisConfigKt.getConfig().getOnlyOnSneak() && !player.isCrouching()) conditionsMet = false;

        return conditionsMet;
    }
}
