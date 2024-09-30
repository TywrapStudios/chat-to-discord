package net.tywrapstudios.ctd.discord;

import com.pastebin.api.Expiration;
import com.pastebin.api.Format;
import com.pastebin.api.PastebinClient;
import com.pastebin.api.Visibility;
import com.pastebin.api.request.PasteRequest;
import net.tywrapstudios.ctd.ChatToDiscord;
import net.tywrapstudios.ctd.config.Manager;
import net.tywrapstudios.ctd.config.config.Config;
import net.tywrapstudios.ctd.discord.messagetypes.Embed;
import net.tywrapstudios.ctd.discord.messagetypes.PlainMessage;
import net.tywrapstudios.ctd.discord.resources.Footer;
import net.tywrapstudios.ctd.discord.webhook.WebhookClient;
import net.tywrapstudios.ctd.discord.webhook.WebhookConnector;
import org.slf4j.Logger;

public class Discord {
    static Config config = Manager.getConfig();
    static Logger logger = ChatToDiscord.LOGGER;
    static Logger debug = ChatToDiscord.DEBUG;
    static String key = config.pastebin_api_key;

    public static void sendMessageToDiscord(String chatMessage, String playerName, String webhookUrl, String UUID) {
        PlainMessage message = new PlainMessage()
                .setContent("**"+playerName+":** "+chatMessage);
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
        Footer footer = new Footer(playerName+": "+chatMessage,"https://mc-heads.net/avatar/"+UUID+"/90");
        Embed embed = new Embed()
                .setColor(embedColor)
                .setFooter(footer);
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

    public static void sendCrashEmbed(String cause, int embedColor, String webhookUrl, String stack) {
        PasteRequest request = PasteRequest
                .content(stack)
                .visibility(Visibility.UNLISTED)
                .name("Crash with: "+cause)
                .expiration(Expiration.ONE_DAY)
                .format(Format.LOGTALK)
                .build();
        PastebinClient client = PastebinClient.builder().developerKey(key).build();
        String url = client.paste(request);
        String description = "**Minecraft crashed with the following given cause:**\n```\n"+cause+"\n```\n\n### \uD83D\uDD17 [STACKTRACE]("+url+")";
        Embed embed = new Embed()
                .setColor(embedColor)
                .setTitle("MINECRAFT EXPERIENCED AN EXCEPTION!")
                .setDescription(description);
        PlainMessage message = new PlainMessage()
                .setContent("");
        new WebhookConnector()
                .setChannelUrl(webhookUrl)
                .setEmbeds(new Embed[]{embed})
                .setMessage(message)
                .setListener(new WebhookClient.Callback() {
                    @Override
                    public void onSuccess(String response) {
                        logSuccess("CTD", "CTD-Internals", "Sent a Crash notice to the webhook(s).");
                    }

                    @Override
                    public void onFailure(int statusCode, String errorMessage) {
                        logFailure(cause, statusCode, errorMessage, "CTD", "CTD-Internals");
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
