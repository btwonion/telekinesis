package dev.nyon.telekinesis.utils;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.Map;

public class PlayerUtils {
    public static void addExpToPlayer(Player player, Integer exp) {
        var remainingExp = repairPlayerItems(player, exp);
        if (remainingExp > 0) player.giveExperiencePoints(remainingExp);
    }

    private static int repairPlayerItems(Player player, Integer exp) {
        Map.Entry<EquipmentSlot, ItemStack> entry = EnchantmentHelper.getRandomItemWith(Enchantments.MENDING, player, ItemStack::isDamaged);
        if (entry != null) {
            ItemStack itemStack = entry.getValue();
            int j = Math.min(exp * 2, itemStack.getDamageValue());
            itemStack.setDamageValue(itemStack.getDamageValue() - j);
            int k = exp - j * 2;
            return k > 0 ? repairPlayerItems(player, k) : 0;
        } else return exp;
    }
}
