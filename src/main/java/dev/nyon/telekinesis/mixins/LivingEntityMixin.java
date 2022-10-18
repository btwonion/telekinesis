package dev.nyon.telekinesis.mixins;

import dev.nyon.telekinesis.TelekinesisKt;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Shadow
    public abstract ResourceLocation getLootTable();

    @Shadow
    protected abstract LootContext.Builder createLootContext(boolean bl, DamageSource damageSource);

    LivingEntity livingEntity = (LivingEntity) (Object) this;
    @Shadow
    protected int lastHurtByPlayerTime;

    @Shadow
    public abstract boolean wasExperienceConsumed();

    @Shadow
    protected abstract boolean isAlwaysExperienceDropper();

    @Shadow
    public abstract boolean shouldDropExperience();

    @Shadow
    public abstract int getExperienceReward();

    @Shadow
    protected abstract void dropCustomDeathLoot(DamageSource damageSource, int i, boolean bl);

    @Shadow protected abstract void dropEquipment();

    @Inject(method = "dropAllDeathLoot", at = @At(value = "HEAD"))
    public void checkDrops(DamageSource damageSource, CallbackInfo ci) {
        var entity = damageSource.getEntity();
        if (entity instanceof Player) return;
        if (!EnchantmentHelper.getEnchantments(((Player) entity).getOffhandItem()).containsKey(TelekinesisKt.getTelekinesis()) && !EnchantmentHelper.getEnchantments(((Player) entity).getUseItem()).containsKey(TelekinesisKt.getTelekinesis()))
            return;

        var loot = EnchantmentHelper.getMobLooting((LivingEntity) entity);

        ResourceLocation resourceLocation = getLootTable();
        LootTable lootTable = livingEntity.level.getServer().getLootTables().get(resourceLocation);
        LootContext.Builder builder = createLootContext(lastHurtByPlayerTime > 0, damageSource);
        lootTable.getRandomItems(builder.create(LootContextParamSets.ENTITY), item -> {
            ((Player) entity).addItem(item);
        });
        dropCustomDeathLoot(damageSource, loot, lastHurtByPlayerTime > 0);

        if (livingEntity.level instanceof ServerLevel && !wasExperienceConsumed() && (isAlwaysExperienceDropper() || this.lastHurtByPlayerTime > 0 && shouldDropExperience() && livingEntity.level.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT))) {
            ((Player) entity).giveExperiencePoints(getExperienceReward());
        }
        dropEquipment();
        ci.cancel();
    }
}
