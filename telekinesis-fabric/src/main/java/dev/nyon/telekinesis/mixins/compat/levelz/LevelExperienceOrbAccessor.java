package dev.nyon.telekinesis.mixins.compat.levelz;

import net.levelz.entity.LevelExperienceOrbEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.gen.Accessor;

@Pseudo
@Mixin(LevelExperienceOrbEntity.class)
public interface LevelExperienceOrbAccessor {

    @Accessor
    Player getTarget();
}
