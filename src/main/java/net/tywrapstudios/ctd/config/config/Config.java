package net.tywrapstudios.ctd.config.config;

import java.util.List;

public class Config {
    // Config Version;
    public final String CONFIG_DO_NOT_TOUCH;
    // Webhook Links;
    public final List<String> discord_webhooks;
    // If we should log;
    public final boolean debug_mode;
    // Embed mode;
    public final boolean embed_mode;

    public Config(Data data) {
        this.CONFIG_DO_NOT_TOUCH = data.CONFIG_DO_NOT_TOUCH;
        this.discord_webhooks = data.discord_webhooks;
        this.debug_mode = data.debug_mode;
        this.embed_mode = data.embed_mode;
    }
}
