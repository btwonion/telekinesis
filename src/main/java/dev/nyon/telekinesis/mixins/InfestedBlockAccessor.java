package dev.nyon.telekinesis.mixins;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.InfestedBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(InfestedBlock.class)
public interface InfestedBlockAccessor {

    @Invoker("spawnInfestation")
    void invokeSpawnInfestation(ServerLevel serverLevel, BlockPos blockPos);
}
