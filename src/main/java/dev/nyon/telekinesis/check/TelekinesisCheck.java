package dev.nyon.telekinesis.check;

import dev.nyon.telekinesis.TelekinesisKt;
import kotlin.Pair;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class TelekinesisCheck {
    public static Pair<Boolean, Player> hasNoTelekinesis(DamageSource source, LivingEntity entity) {
        Player player = null;
        if (source.getEntity() instanceof Player) player = (Player) source.getEntity();
        if (entity.getKillCredit() instanceof Player) player = (Player) entity.getKillCredit();
        if (player == null) return new Pair<>(true, null);
        return new Pair<>(
            !EnchantmentHelper.getEnchantments(player.getOffhandItem()).containsKey(TelekinesisKt.getTelekinesis())
            && !EnchantmentHelper.getEnchantments(player.getInventory().getSelected()).containsKey(TelekinesisKt.getTelekinesis()),
            player
        );
    }
}
