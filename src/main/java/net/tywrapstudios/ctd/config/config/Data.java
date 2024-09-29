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

    public void update() {
    }
}
