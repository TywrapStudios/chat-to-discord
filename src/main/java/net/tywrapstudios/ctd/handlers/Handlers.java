package net.tywrapstudios.ctd.handlers;

import net.tywrapstudios.ctd.compat.DiscordSafety;
import net.tywrapstudios.ctd.config.Config;
import net.tywrapstudios.ctd.config.ConfigManager;
import net.tywrapstudios.ctd.discord.Discord;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class Handlers {

    public static void handleChatMessage(String messageStr, String authorUUID, String authorName) {
        Config config = ConfigManager.config;
        List<String> webhookUrls = config.discord_config.discord_webhooks;

        messageStr = CompatHandlers.handleCompat(messageStr);
        if (!config.discord_config.embed_mode) {
            authorName = DiscordSafety.modifyToNegateMarkdown(authorName);
        }

        if (!webhookUrls.isEmpty()) {
            if (!Objects.equals(authorName, "Rcon")) {
                for (String url : webhookUrls) {
                    if (!config.discord_config.embed_mode) {
                        Discord.sendChatMessageToDiscord(messageStr, authorName, url, authorUUID);
                    } else {
                        Discord.sendEmbedToDiscord(messageStr, authorName, url, authorUUID, config.discord_config.embed_color_rgb_int);
                    }
                }
            } else {
                LoggingHandlers.debug("The sender was the Server or a Remote Console (RCON).");
            }
        } else {
            LoggingHandlers.error("[Discord] No webhooks configured! Please configure your webhooks in the Config file: ctd.json");
        }
    }

    public static void handleGameMessage(String message) {
        Config config = ConfigManager.config;
        boolean embedMode = config.discord_config.embed_mode;
        List<String> webhookUrls = config.discord_config.discord_webhooks;

        message = CompatHandlers.handleCompat(message);
        if (!config.discord_config.only_send_messages) {
            if (!embedMode) {
                message = "**" + message + "**";
            }
            if (!webhookUrls.isEmpty()) {
                for (String url : webhookUrls) {
                    Discord.sendLiteralToDiscord(message, embedMode, url);
                }
            } else {
                LoggingHandlers.error("[Discord] No webhooks configured! Please configure your webhooks in the Config file: ctd.json");
            }
        }
    }

    public static void handleCrash(String cause, String stack) throws ExecutionException, InterruptedException {
        Config config = ConfigManager.config;
        List<String> webhookUrls = config.discord_config.discord_webhooks;
        for (String url : webhookUrls) {
            Discord.sendCrashEmbed(cause, 7864320, url, stack);
        }
    }
}
