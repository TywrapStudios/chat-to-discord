package net.tywrapstudios.ctd.mixin;

import net.minecraft.network.message.SignedMessage;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.tywrapstudios.ctd.ChatToDiscord;
import net.tywrapstudios.ctd.config.Manager;
import net.tywrapstudios.ctd.config.config.Config;
import net.tywrapstudios.ctd.discord.Discord;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class SendWebhookOnChatMessage {
    @Shadow public ServerPlayerEntity player;

    @Inject(method = "handleDecoratedMessage",
            at = @At(value = "TAIL"))
    private void ctd$sendDiscordWebhookMessage(SignedMessage message, CallbackInfo ci) {
        String messageStr = message.getContent().getString();
        String authorUUID = message.getSender().toString();
        String authorName = player.getEntityName();

        Config config = Manager.getConfig();
        List<String> webhookUrls = config.discord_webhooks;
        if (!webhookUrls.isEmpty()) {
            for (String url : webhookUrls) {
                if (!config.embed_mode) {
                    Discord.sendMessageToDiscord(messageStr, authorName, url, authorUUID);
                } else {
                    Discord.sendEmbedToDiscord(messageStr, authorName, url, authorUUID);
                }
            }
        } else {
            ChatToDiscord.LOGGER.error("[Discord] No webhooks configured! Please configure your webhooks in the Config file: ctd.json");
        }
    }
}
