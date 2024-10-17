package dev.nyon.telekinesis.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.nyon.telekinesis.utils.MixinHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/*? if <1.21.2 {*/
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.level.block.Block;
import static dev.nyon.telekinesis.utils.MixinHelper.threadLocal;
/*?} else {*/
/*import dev.nyon.telekinesis.DropEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import org.apache.commons.lang3.mutable.MutableInt;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
*//*?}*/

@Mixin(Sheep.class)
public abstract class SheepMixin {

    @WrapOperation(
        method = "mobInteract",
        at = @At(
            value = "INVOKE",
            target = /*? if needsWorldNow {*//*"Lnet/minecraft/world/entity/animal/Sheep;shear(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/sounds/SoundSource;Lnet/minecraft/world/item/ItemStack;)V"
            *//*?} else {*/
            "Lnet/minecraft/world/entity/animal/Sheep;shear(Lnet/minecraft/sounds/SoundSource;)V" 
            /*?}*/
        )
    )
    private void prepareThreadLocalForShearing(
        Sheep instance,
        /*$ serverLevel {*//*$}*/
        SoundSource source,
        /*? if >=1.21.2 {*/
        /*ItemStack itemStack,
        *//*?}*/
        Operation<Void> original,
        Player player,
        InteractionHand hand
    ) {
        MixinHelper.prepareShearableServerPlayer(instance, source, original, player);
    }

    /*? if <1.21.2 {*/
    @ModifyExpressionValue(
        method = "shear",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;"
        )
    )
    private Object modifyShearDrops(Object original) {
        ServerPlayer player = threadLocal.get();
        if (!(original instanceof Block block)) return original;
        if (player == null) return original;

        if (MixinHelper.wrapWithConditionPlayerItemSingle(player, new ItemStack(block))) return original;
        else return ItemStack.EMPTY;
    }
    /*?} else {*/
    
    /*@Unique
    private Sheep instance = (Sheep) (Object) this;

    @ModifyArg(
        method = "shear",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/animal/Sheep;dropFromShearingLootTable(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/item/ItemStack;Ljava/util/function/BiConsumer;)V"
        ),
        index = 3
    )
    private BiConsumer<ServerLevel, ItemStack> changeOriginalDropConsumer(BiConsumer<ServerLevel, ItemStack> original) {
        DamageSource source = instance.getLastDamageSource();
        if (source == null || !(source.getEntity() instanceof ServerPlayer player)) return original;

        return (world, item) -> {
            ArrayList<ItemStack> mutableList = new ArrayList<>(List.of(item));
            DropEvent.INSTANCE.getEvent()
                .invoker()
                .invoke(mutableList, new MutableInt(0), player, player.getMainHandItem());

            if (!mutableList.isEmpty()) original.accept(world, item);
        };
    }
    *//*?}*/
}