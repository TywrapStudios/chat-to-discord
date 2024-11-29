package net.tywrapstudios.ctd.config;

import blue.endless.jankson.Comment;
import net.tywrapstudios.blossombridge.api.config.AbstractConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * This is where the Configuration Options are defined, you can make use of {@link blue.endless.jankson.Comment} to add a // type comment to the file.
 * <br/> Comments are allowed as the file is initialized as .json5 and not plain .json.
 * <br/> This is also considerably nicer for us to manage.
 * <br/>
 * <br/> DON'T FORGET TO TRANSFER NEW DETAILS OVER TO {@code README}
 */
public class Config extends AbstractConfig {
    public String format_version = "2.0";
    @Comment("All configurations for the Discord integration.")
    public DiscordConfig discord_config = new DiscordConfig();
    public static class DiscordConfig {
        @Comment("A list of webhooks in Strings that the mod will send messages to. eg: \"https://discord.com/api/webhooks/...\"")
        public List<String> discord_webhooks = new ArrayList<>();
        @Comment("Whether to only send player messages to Discord, and not game related messages (e.g. join/leave messages, deaths, etc.).")
        public boolean only_send_messages = false;
        @Comment("Whether to send messages as an embed. If false, messages will be sent as plain text.")
        public boolean embed_mode = false;
        @Comment("""
                The setting below must be an RGB int, so not a `255, 255, 255` type of thing.
                Use this site if you want to use this feature:
                http://www.shodor.org/~efarrow/trunk/html/rgbint.html""")
        public int embed_color_rgb_int = 5489270;
        @Comment("A list of role ID's in Strings that users are allowed to ping from MC. e.g. \"123456789012345678\"")
        public List<String> role_ids = new ArrayList<>();
    }
    @Comment("Several configurations for utility features.")
    public UtilConfig util_config = new UtilConfig();
    public static class UtilConfig {
        @Comment("Whether to display debug information in the console.")
        public boolean debug_mode = false;
        @Comment("Whether to suppress all warnings from this mod. NOT RECOMMENDED.")
        public boolean suppress_warns = false;
    }
}
