package dev.nyon.telekinesis.check;

import dev.nyon.telekinesis.TelekinesisKt;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class TelekinesisCheck {
    public static boolean hasNoTelekinesis(DamageSource source) {
        if (!(source.getEntity() instanceof Player player)) return true;
        return !EnchantmentHelper.getEnchantments(player.getOffhandItem()).containsKey(TelekinesisKt.getTelekinesis())
            && !EnchantmentHelper.getEnchantments(player.getUseItem()).containsKey(TelekinesisKt.getTelekinesis());
    }
}
