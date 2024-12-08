package dev.nyon.magnetic.mixins.invokers;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ExperienceOrb.class)
public interface ExperienceOrbInvoker {

    @Invoker("repairPlayerItems")
    int invokeRepairPlayerItems(
        /*? if >=1.21 {*/ ServerPlayer serverPlayer /*?} else {*/ /*Player player*//*?}*/,
        int i
    );
}
