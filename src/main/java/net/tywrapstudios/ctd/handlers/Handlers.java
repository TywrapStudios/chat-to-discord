package net.tywrapstudios.ctd.handlers;

import net.tywrapstudios.ctd.ChatToDiscord;
import net.tywrapstudios.ctd.config.Manager;
import net.tywrapstudios.ctd.config.config.Config;
import net.tywrapstudios.ctd.discord.Discord;

import java.util.List;
import java.util.Objects;

public class Handlers {

    public static void handleChatMessage(String messageStr, String authorUUID, String authorName) {
        Config config = Manager.getConfig();
        List<String> webhookUrls = config.discord_webhooks;
        if (!webhookUrls.isEmpty()) {
            for (String url : webhookUrls) {
                if (!config.embed_mode) {
                    Discord.sendChatMessageToDiscord(messageStr, authorName, url, authorUUID);
                } else {
                    Discord.sendEmbedToDiscord(messageStr, authorName, url, authorUUID, config.embed_color_rgb_int);
                }
            }
        } else {
            if (!config.suppress_warns) {
                ChatToDiscord.LOGGER.error("[Discord] No webhooks configured! Please configure your webhooks in the Config file: ctd.json");
            }
        }
    }

    public static void handlePasteBinError() {
        Config config = Manager.getConfig();
        List<String> webhookUrls = config.discord_webhooks;
        for (String url : webhookUrls) {
            if (Objects.equals(config.pastebin_api_key, "")&&!config.suppress_warns) {
                Discord.sendEmbedToDiscord("No Pastebin API Key Defined! Please Configure a Key in the Config file: ctd.json", "CTD-Internals", url, "console", 7864320);
            }
        }
    }

    public static void handleGameMessage(String message) {
        Config config = Manager.getConfig();
        boolean embedMode = config.embed_mode;
        String messageFin = message;
        List<String> webhookUrls = config.discord_webhooks;
        if (!embedMode) {
            messageFin = "**"+messageFin+"**";
        }
        if (!webhookUrls.isEmpty()) {
            for (String url : webhookUrls) {
                Discord.sendLiteralToDiscord(messageFin,embedMode,url);
            }
        } else {
            if (!config.suppress_warns) {
                ChatToDiscord.LOGGER.error("[Discord] No webhooks configured! Please configure your webhooks in the Config file: ctd.json");
            }
        }
    }

    public static void handleCrash(String cause, String stack) {
        Config config = Manager.getConfig();
        List<String> webhookUrls = config.discord_webhooks;
        for (String url : webhookUrls) {
            Discord.sendCrashEmbed(cause, 7864320, url, stack);
        }
    }
}
