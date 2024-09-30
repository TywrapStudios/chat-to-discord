package net.tywrapstudios.ctd.config.config;

import java.util.ArrayList;
import java.util.List;

public class Data {
    /**
     * This is where the order of the entries and the entries themselves are defined.
     */
    public String CONFIG_DO_NOT_TOUCH = "1";
    public List<String> discord_webhooks = new ArrayList<>();
    public boolean debug_mode = false;
    public boolean embed_mode = false;
    public String c1 = "The setting above must be an RGB int, so not a `255, 255, 255` type of thing. Use this site if you want to use this feature:";
    public String c2 = "http://www.shodor.org/~efarrow/trunk/html/rgbint.html";
    public int embed_color_rgb_int = 5489270;
    public String c3 = "Set this to true if you wish not to receive LOGGER.error messages. NOT RECOMMENDED!!";
    public boolean suppress_warns = false;

    public void update() {
    }
}
