package net.tywrapstudios.ctd.config.config;

import java.util.List;

public class Config {
    // Config Version;
    public final String CONFIG_DO_NOT_TOUCH;
    // Webhook Links;
    public final List<String> discord_webhooks;
    // If only chat should be sent.
    public final boolean only_send_messages;
    // If we should log;
    public final boolean debug_mode;
    // Embed mode;
    public final boolean embed_mode;
    // Pastebin API Key;
    public final String pastebin_api_key;
    // Embed Colour;
    public final int embed_color_rgb_int;
    // Allowed role ID's;
    public final List<String> role_ids;
    // Suppress Warnings;
    public boolean suppress_warns;

    public Config(Data data) {
        this.CONFIG_DO_NOT_TOUCH = data.CONFIG_DO_NOT_TOUCH;
        this.discord_webhooks = data.discord_webhooks;
        this.only_send_messages = data.only_send_messages;
        this.debug_mode = data.debug_mode;
        this.embed_mode = data.embed_mode;
        this.pastebin_api_key = data.pastebin_api_key;
        this.embed_color_rgb_int = data.embed_color_rgb_int;
        this.role_ids = data.role_ids;
        this.suppress_warns = data.suppress_warns;
    }
}
