package dev.nyon.telekinesis.mixins;

import dev.nyon.telekinesis.check.TelekinesisUtils;
import dev.nyon.telekinesis.config.ConfigKt;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.monster.Strider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.Blocks;
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

    @Inject(
        method = "dropAllDeathLoot",
        at = @At(
            value = "HEAD"
        ),
        cancellable = true
    )
    public void checkDrops(DamageSource damageSource, CallbackInfo ci) {
        var telekinesisResult = TelekinesisUtils.hasNoTelekinesis(damageSource, livingEntity);
        if (
            !ConfigKt.getConfig().getMobDrops()
            || (telekinesisResult.component1() && !ConfigKt.getConfig().getOnByDefault())
            || telekinesisResult.component2() == null
        ) return;
        var player = telekinesisResult.component2();

        manipulateDrops(player, damageSource);
        if(ConfigKt.getConfig().getExpDrops()) manipulateXp(player);
        handleEquipmentDrops(player);
        ci.cancel();
    }

    private void manipulateXp(Player player) {
        if (
            livingEntity.level instanceof ServerLevel
                && !wasExperienceConsumed()
                && (
                    isAlwaysExperienceDropper()
                    || this.lastHurtByPlayerTime > 0
                    && shouldDropExperience()
                    && livingEntity.level.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)
                )
        ) {
            TelekinesisUtils.addXPToPlayer(player, getExperienceReward());
        }
    }

    private void manipulateDrops(Player player, DamageSource damageSource) {
        var loot = EnchantmentHelper.getMobLooting(player);
        ResourceLocation resourceLocation = getLootTable();
        LootTable lootTable = livingEntity.level.getServer().getLootTables().get(resourceLocation);
        LootContext.Builder builder = createLootContext(lastHurtByPlayerTime > 0, damageSource);
        lootTable.getRandomItems(builder.create(LootContextParamSets.ENTITY), item -> {
            if (!player.addItem(item)) livingEntity.spawnAtLocation(item);
        });

        dropCustomDeathLoot(damageSource, loot, lastHurtByPlayerTime > 0);
    }

    private void handleEquipmentDrops(Player player) {
        if (livingEntity instanceof Pig pig) {
            if (pig.isSaddled()) if (!player.addItem(new ItemStack(Items.SADDLE))) pig.spawnAtLocation(Items.SADDLE);
        } else if (livingEntity instanceof Allay allay) {
            allay.getInventory().removeAllItems().forEach(item -> {
                if (!player.addItem(item)) allay.spawnAtLocation(item);
            });
            ItemStack itemStack = allay.getItemBySlot(EquipmentSlot.MAINHAND);
            if (!itemStack.isEmpty() && !EnchantmentHelper.hasVanishingCurse(itemStack)) {
                if (!player.addItem(itemStack)) allay.spawnAtLocation(itemStack);
                allay.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
            }
        } else if (livingEntity instanceof AbstractChestedHorse horse) {
            if (horse.hasChest()) {
                if (!horse.level.isClientSide)
                    if (!player.addItem(new ItemStack(Blocks.CHEST))) horse.spawnAtLocation(Blocks.CHEST);
                horse.setChest(false);
            }
        } else if (livingEntity instanceof AbstractHorse abstractHorse) {
            AbstractHorseAccessor horse = (AbstractHorseAccessor) abstractHorse;
            if (horse.getInventory() != null) {
                for (int i = 0; i < horse.getInventory().getContainerSize(); ++i) {
                    ItemStack itemStack = horse.getInventory().getItem(i);
                    if (!itemStack.isEmpty() && !EnchantmentHelper.hasVanishingCurse(itemStack))
                        if (!player.addItem(itemStack)) abstractHorse.spawnAtLocation(itemStack);
                }
            }
        } else if (livingEntity instanceof Strider strider) {
            if (strider.isSaddled())
                if (!player.addItem(new ItemStack(Items.SADDLE))) strider.spawnAtLocation(Items.SADDLE);
        } else if (livingEntity instanceof Player target) {
            if (!target.level.getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY)) {
                for (int i = 0; i < target.getInventory().getContainerSize(); ++i) {
                    ItemStack itemStack = target.getInventory().getItem(i);
                    if (!itemStack.isEmpty() && EnchantmentHelper.hasVanishingCurse(itemStack))
                        target.getInventory().removeItemNoUpdate(i);
                    else if (!itemStack.isEmpty()) if (!player.addItem(itemStack)) target.spawnAtLocation(itemStack);
                }
            }
        }
    }
}
