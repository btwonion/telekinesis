package dev.nyon.telekinesis.mixins;

import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.block.SculkCatalystBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SculkCatalystBlock.class)
public interface CatalystBlockAccessor {

    @Accessor
    IntProvider getXpRange();
}
