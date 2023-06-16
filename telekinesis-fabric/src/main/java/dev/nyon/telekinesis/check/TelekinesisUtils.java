package dev.nyon.telekinesis.check;

import dev.nyon.telekinesis.TelekinesisKt;
import kotlin.Pair;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TelekinesisUtils {
    public static Pair<Boolean, Player> hasNoTelekinesis(DamageSource source, LivingEntity entity) {
        Player player = null;
        if (source.getEntity() instanceof Player) player = (Player) source.getEntity();
        else if (entity.getKillCredit() instanceof Player) player = (Player) entity.getKillCredit();
        if (player == null) return new Pair<>(true, null);
        ArrayList<ItemStack> acceptedItems = new ArrayList<>(List.of(player.getOffhandItem(), player.getInventory().getSelected()));
        player.getArmorSlots().forEach(acceptedItems::add);
        return new Pair<>(
            acceptedItems.stream().allMatch(item -> EnchantmentHelper.getItemEnchantmentLevel(TelekinesisKt.getTelekinesis(), item) == 0),
            player
        );
    }

    public static Boolean hasNoTelekinesis(DamageSource source) {
        if (!(source.getEntity() instanceof Player player)) return true;
        ArrayList<ItemStack> acceptedItems = new ArrayList<>(List.of(player.getOffhandItem(), player.getInventory().getSelected()));
        player.getArmorSlots().forEach(acceptedItems::add);
        return acceptedItems.stream().allMatch(item -> EnchantmentHelper.getItemEnchantmentLevel(TelekinesisKt.getTelekinesis(), item) == 0);
    }

    public static void addXPToPlayer(Player player, Integer xp) {
        player.giveExperiencePoints(xp);
        Map.Entry<EquipmentSlot, ItemStack> entry = EnchantmentHelper.getRandomItemWith(Enchantments.MENDING, player, ItemStack::isDamaged);
        if (entry != null) {
            ItemStack itemStack = entry.getValue();
            int j = Math.min(xp * 2, itemStack.getDamageValue());
            itemStack.setDamageValue(itemStack.getDamageValue() - j);
        }
    }
}
