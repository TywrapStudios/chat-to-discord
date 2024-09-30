package net.tywrapstudios.ctd.discord;

import net.tywrapstudios.ctd.ChatToDiscord;
import net.tywrapstudios.ctd.config.Manager;
import net.tywrapstudios.ctd.config.config.Config;
import net.tywrapstudios.ctd.discord.messagetypes.Embed;
import net.tywrapstudios.ctd.discord.messagetypes.PlainMessage;
import net.tywrapstudios.ctd.discord.webhook.WebhookClient;
import net.tywrapstudios.ctd.discord.webhook.WebhookConnector;
import org.slf4j.Logger;

public class Discord {
    static Config config = Manager.getConfig();
    static Logger logger = ChatToDiscord.LOGGER;
    static Logger debug = ChatToDiscord.DEBUG;

    public static void sendMessageToDiscord(String chatMessage, String playerName, String webhookUrl, String UUID) {
        PlainMessage message = new PlainMessage()
                .setContent(playerName+": "+chatMessage);
        new WebhookConnector()
                .setChannelUrl(webhookUrl)
                .setMessage(message)
                .setListener(new WebhookClient.Callback() {
                    @Override
                    public void onSuccess(String response) {
                        logSuccess(playerName, UUID, chatMessage);
                    }
                    @Override
                    public void onFailure(int statusCode, String errorMessage) {
                        logFailure(chatMessage, statusCode, errorMessage, playerName, UUID);
                    }
                })
                .exec();
    }

    public static void sendEmbedToDiscord(String chatMessage, String playerName, String webhookUrl, String UUID, int embedColor) {
        Embed embed = new Embed()
                .setColor(embedColor)
                .setTitle(playerName+":")
                .setDescription(chatMessage);
        PlainMessage message = new PlainMessage()
                .setContent("");
        new WebhookConnector()
                .setChannelUrl(webhookUrl)
                .setEmbeds(new Embed[]{embed})
                .setMessage(message)
                .setListener(new WebhookClient.Callback() {
                    @Override
                    public void onSuccess(String response) {
                        logSuccess(playerName, UUID, chatMessage);
                    }

                    @Override
                    public void onFailure(int statusCode, String errorMessage) {
                        logFailure(chatMessage, statusCode, errorMessage, playerName, UUID);
                    }
                })
                .exec();
    }

    private static void logSuccess(String playerName, String UUID, String chatMessage) {
        if (config.debug_mode) {
            debug.info("[{}[{}]: {}]", playerName, UUID, chatMessage);
        }
    }
    private static void logFailure(String chatMessage, int statusCode, String errorMessage, String playerName, String UUID) {
        if (!config.suppress_warns) {
            logger.warn("Message \"{}\" by {}[{}] failed to send. ", chatMessage, playerName, UUID);
            logger.warn("Code: {} Error: {}", statusCode, errorMessage);
        }
    }
}
