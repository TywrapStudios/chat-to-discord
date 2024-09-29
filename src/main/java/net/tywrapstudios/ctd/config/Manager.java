package net.tywrapstudios.ctd.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.tywrapstudios.ctd.ChatToDiscord;
import net.tywrapstudios.ctd.config.config.Config;
import net.tywrapstudios.ctd.config.config.Data;
import org.slf4j.Logger;

import java.io.*;
import java.nio.file.Paths;

public class Manager {
    /**
     * The manager makes/edits the config file, and reads (loads) from it as well.
     */
    private static final Gson GSON_OBJECT = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    private static Config CONFIG;

    protected static Logger logger = ChatToDiscord.LOGGER;

    public static Config getConfig() {
        return CONFIG;
    }

    public static void loadConfig() {
        Config oldConfig = CONFIG;
        CONFIG = null;
        try {
            ensureConfig("Successfully read Config file.");
        }
        catch(IOException exception) {
            CONFIG = oldConfig;
            logger.error("[Config] Something went wrong while reading config!");
        }
    }

    public static void reloadConfig(CommandContext context) {
        Config oldConfig = CONFIG;

        CONFIG = null;
        try {
            logger.info("[Config] Reloading config...");
            ensureConfig("Successfully Reloaded Config file.");
        }
        catch(IOException exception) {
            CONFIG = oldConfig;
            logger.error("[Config] Something went wrong while reloading Config!");
        }
    }

    public static void ensureConfig(String logMessage) throws IOException {
        File dir = Paths.get("", "config").toFile();

        dir.mkdirs();

        File config = new File(dir, "ctd.json");

        Data data = config.exists() ? GSON_OBJECT.fromJson(new InputStreamReader(new FileInputStream(config), "UTF-8"), Data.class) : new Data();
        data.update();

        CONFIG = new Config(data);

        {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(config), "UTF-8"));
            writer.write(GSON_OBJECT.toJson(data));
            writer.close();
        }
        logger.info("[Config] {}", logMessage);
    }

}
