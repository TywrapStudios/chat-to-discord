package net.tywrapstudios.ctd.compat;

import net.tywrapstudios.ctd.ChatToDiscord;
import net.tywrapstudios.ctd.config.Config;
import net.tywrapstudios.ctd.discord.Discord;
import net.tywrapstudios.ctd.discord.messagetypes.Embed;
import net.tywrapstudios.ctd.discord.messagetypes.PlainMessage;
import net.tywrapstudios.ctd.discord.webhook.WebhookClient;
import net.tywrapstudios.ctd.discord.webhook.WebhookConnector;

import java.util.List;
import java.util.concurrent.TimeoutException;

public class Spark {
    public static void handleSparkWorldTimeOut(TimeoutException e) {
        Config config = ChatToDiscord.CONFIG_MANAGER.getConfig();
        List<String> webhookUrls = config.discord_config.discord_webhooks;
        for (String url : webhookUrls) {
            sendTimeOutEmbed(url);
        }
        e.printStackTrace();
    }

    private static void sendTimeOutEmbed(String webhookUrl) {
        String description = """
                **Timed out waiting for world statistics.**
                **Stacktrace:**
                -> View your console logs for more information.""";
        Embed embed = new Embed()
                .setColor(7864320)
                .setTitle("Spark Profiler:")
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
                        Discord.logSuccess("CTD", "CTD-Compat", "Sent a Timeout notice to the webhook(s).");
                    }

                    @Override
                    public void onFailure(int statusCode, String errorMessage) {
                        Discord.logFailure("Spark Timeout notice", statusCode, errorMessage, "CTD", "CTD-Compat");
                    }
                })
                .exec();
    }
}
