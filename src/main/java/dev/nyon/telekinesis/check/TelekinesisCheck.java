package dev.nyon.telekinesis.check;

import dev.nyon.telekinesis.TelekinesisKt;
import kotlin.Pair;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.util.ArrayList;
import java.util.List;

public class TelekinesisCheck {
    public static Pair<Boolean, Player> hasNoTelekinesis(DamageSource source, LivingEntity entity) {
        Player player = null;
        if (source.getEntity() instanceof Player) player = (Player) source.getEntity();
        if (entity.getKillCredit() instanceof Player) player = (Player) entity.getKillCredit();
        if (player == null) return new Pair<>(true, null);
        ArrayList<ItemStack> acceptedItems = new ArrayList<>(List.of(player.getOffhandItem(), player.getInventory().getSelected()));
        player.getArmorSlots().forEach(acceptedItems::add);
        return new Pair<>(
            acceptedItems.stream().noneMatch(item -> EnchantmentHelper.getEnchantments(item).containsKey(TelekinesisKt.getTelekinesis())),
            player
        );
    }
}
