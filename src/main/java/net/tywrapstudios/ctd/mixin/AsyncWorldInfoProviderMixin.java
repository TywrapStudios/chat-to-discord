package net.tywrapstudios.ctd.mixin;

import me.lucko.spark.common.platform.world.AsyncWorldInfoProvider;
import net.tywrapstudios.ctd.handlers.Handlers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Mixin(AsyncWorldInfoProvider.class)
public class AsyncWorldInfoProviderMixin {
    @Inject(
            method = "get(Ljava/util/concurrent/CompletableFuture;)Ljava/lang/Object;",
            at = @At(value = "INVOKE",
                    target = "Lme/lucko/spark/common/SparkPlugin;log(Ljava/util/logging/Level;Ljava/lang/String;)V"),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void ctd$printTimedOutStackTrace(CompletableFuture<?> future, CallbackInfoReturnable<?> cir, TimeoutException e) throws ExecutionException, InterruptedException {
        Handlers.handleWorldTimeOut(e);
    }
}
