package net.tywrapstudios.ctd.mixin;

import net.minecraft.util.crash.CrashReport;
import net.tywrapstudios.ctd.ChatToDiscord;
import net.tywrapstudios.ctd.handlers.Handlers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;
import java.util.concurrent.ExecutionException;

@Mixin(CrashReport.class)
public abstract class SendWebhookOnCrash {
    @Shadow public abstract String getMessage();
    @Shadow public abstract String getStackTrace();

    @Inject(method = "writeToFile(Ljava/io/File;)Z",
            at = @At(value = "HEAD"))
    private void ctd$sendWebhookOnCrash(File file, CallbackInfoReturnable<Boolean> cir) {
        String cause = getMessage();
        String stack = getStackTrace();
        try {
            Handlers.handleCrash(cause, stack);
        } catch (Exception e) {
            ChatToDiscord.LOGGING.error("An error occurred while trying to send the crash report to Discord. Please check the logs for more information.");
            e.printStackTrace();
        }
    }
}
