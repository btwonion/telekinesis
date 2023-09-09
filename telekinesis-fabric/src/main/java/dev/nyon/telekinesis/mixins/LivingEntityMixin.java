package dev.nyon.telekinesis.mixins;

import dev.nyon.telekinesis.TelekinesisPolicy;
import dev.nyon.telekinesis.utils.PlayerUtils;
import dev.nyon.telekinesis.utils.TelekinesisUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;
import net.minecraft.world.entity.monster.Strider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Unique
    LivingEntity livingEntity = (LivingEntity) (Object) this;

    @Redirect(
        method = "dropExperience",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/ExperienceOrb;award(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/phys/Vec3;I)V"
        )
    )
    public void redirectExp(ServerLevel serverLevel, Vec3 vec3, int i) {
        final var attacker = livingEntity.getLastAttacker();
        if (!(attacker instanceof ServerPlayer)) ExperienceOrb.award(serverLevel, vec3, i);
        final var serverPlayer = (ServerPlayer) attacker;

        boolean hasTelekinesis = TelekinesisUtils.handleTelekinesis(
            TelekinesisPolicy.ExpDrops,
            serverPlayer,
            serverPlayer.getMainHandItem(),
            player -> {
                PlayerUtils.addExpToPlayer(player, i);
            }
        );

        if (!hasTelekinesis) ExperienceOrb.award(serverLevel, vec3, i);
    }

    @ModifyArgs(
        method = "dropFromLootTable",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/storage/loot/LootTable;getRandomItems(Lnet/minecraft/world/level/storage/loot/LootParams;JLjava/util/function/Consumer;)V"
        )
    )
    public void redirectCommonDrops(Args args, DamageSource damageSource, boolean bl) {
        args.<Consumer<ItemStack>>set(2, item -> {
            boolean hasTelekinesis = TelekinesisUtils.handleTelekinesis(
                TelekinesisPolicy.MobDrops,
                damageSource,
                player -> {
                    if (!player.addItem(item)) livingEntity.spawnAtLocation(item);
                }
            );

            if (!hasTelekinesis) livingEntity.spawnAtLocation(item);
        });
    }

    @Redirect(
        method = "dropAllDeathLoot",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/LivingEntity;dropEquipment()V"
        )
    )
    public void redirectEquipmentDrops(LivingEntity instance, DamageSource damageSource) {
        List<ItemStack> itemsToDrop = new ArrayList<>();

        if (instance instanceof Allay allay) addAllayDrops(allay, itemsToDrop);
        if (instance instanceof AbstractChestedHorse abstractChestedHorse)
            addChestedHorseDrops(abstractChestedHorse, itemsToDrop);
        if (instance instanceof Strider strider) addStriderDrops(strider, itemsToDrop);
        if (instance instanceof Pig pig) addPigDrops(pig, itemsToDrop);
        if (instance instanceof Player player) addPlayerDrops(player, itemsToDrop);

        boolean hasTelekinesis = TelekinesisUtils.handleTelekinesis(
            TelekinesisPolicy.MobDrops,
            damageSource,
            player -> {
                itemsToDrop.forEach(item -> {
                    if (!player.addItem(item)) instance.spawnAtLocation(item);
                });
            }
        );

        if (!hasTelekinesis) itemsToDrop.forEach(instance::spawnAtLocation);
    }

    private void addAllayDrops(Allay instance, List<ItemStack> items) {
        items.addAll(instance.getInventory().removeAllItems());

        ItemStack itemStack = instance.getItemBySlot(EquipmentSlot.MAINHAND);
        if (!itemStack.isEmpty() && !EnchantmentHelper.hasVanishingCurse(itemStack)) {
            items.add(itemStack);
            instance.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
        }
    }

    private void addChestedHorseDrops(AbstractChestedHorse instance, List<ItemStack> items) {
        if (instance.hasChest()) {
            if (!instance.level().isClientSide) {
                items.add(new ItemStack(Blocks.CHEST));
            }

            instance.setChest(false);
        }
    }

    private void addStriderDrops(Strider strider, List<ItemStack> items) {
        if (strider.isSaddled()) {
            items.add(new ItemStack(Items.SADDLE));
        }
    }

    private void addPigDrops(Pig pig, List<ItemStack> items) {
        if (pig.isSaddled()) {
            items.add(new ItemStack(Items.SADDLE));
        }
    }

    private void addPlayerDrops(Player player, List<ItemStack> items) {
        if (!player.level().getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY)) {
            ((PlayerAccessor) player).invokeDestroyVanishingCursedItems();

            for (List<ItemStack> list : ((InventoryAccessor) player.getInventory()).getCompartments()) {
                for (int i = 0; i < list.size(); ++i) {
                    ItemStack itemStack = list.get(i);
                    if (!itemStack.isEmpty()) {
                        items.add(itemStack);
                        list.set(i, ItemStack.EMPTY);
                    }
                }
            }
        }
    }
}
