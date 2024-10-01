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
import java.util.Objects;

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
            if (Objects.equals(config.pastebin_api_key, "")&&!config.suppress_warns) {
                Discord.sendEmbedToDiscord("No Pastebin API Key Defined! Please Configure a Key in the Config file: ctd.json", "CTD-Internals", url, "console", 7864320);
            } else {
                Discord.sendCrashEmbed(cause, rgb, url, stack);
            }
        }
    }
}
