package dev.nyon.telekinesis.mixins;

import dev.nyon.telekinesis.MainKt;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

    @Inject(
            method = "runServer",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/MinecraftServer;initServer()Z",
                    shift = At.Shift.BEFORE
            )
    )
    private void onBeforeInit(CallbackInfo ci) {
        MainKt.setServer((MinecraftServer) (Object) this);
    }

}
