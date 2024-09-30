package net.tywrapstudios.ctd.mixin;

import net.minecraft.util.crash.CrashReport;
import net.tywrapstudios.ctd.config.Manager;
import net.tywrapstudios.ctd.config.config.Config;
import net.tywrapstudios.ctd.discord.Discord;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;
import java.util.List;

@Mixin(CrashReport.class)
public abstract class SendWebhookOnCrash {
    @Shadow public abstract String getMessage();

    @Shadow public abstract String getStackTrace();

    @Inject(method = "writeToFile(Ljava/io/File;)Z",
            at = @At(value = "HEAD"))
    private void ctd$sendWebhookOnCrash(File file, CallbackInfoReturnable<Boolean> cir) {
        Config config = Manager.getConfig();
        String cause = getMessage();
        String stack = getStackTrace();
        int rgb = 7864320;
        List<String> webhookUrls = config.discord_webhooks;
        for (String url : webhookUrls) {
            Discord.sendCrashEmbed(cause, rgb, url, stack);
        }
    }
}
