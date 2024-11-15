package net.tywrapstudios.ctd.config;

import blue.endless.jankson.Jankson;
import blue.endless.jankson.JsonElement;
import blue.endless.jankson.api.SyntaxError;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.tywrapstudios.ctd.handlers.LoggingHandlers;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * The manager makes/edits the config file, and reads (loads) from it as well.
 * <br/>This is utilizing {@link blue.endless.jankson.Jankson}, which is a different method of adding Config than our older mods.
 */
public class ConfigManager {
    private static final Jankson jankson = Jankson.builder().build();
    private static final File configFile = new File(FabricLoader.getInstance().getConfigDir().toFile(), "ctd.json5");
    public static Config config;

    public static void loadConfig() {
        if (configFile.exists()) {
            try {
                config = jankson.fromJson(jankson.load(configFile), Config.class);
                LoggingHandlers.info("[Config] Loaded configuration file.");
            } catch (IOException | SyntaxError e) {
                LoggingHandlers.error("[Config] Error loading configuration file; using default values.");
                e.printStackTrace();
                config = new Config();
            }
        } else {
            config = new Config();
            LoggingHandlers.info("[Config] No configuration file found, created new one.");
            LoggingHandlers.info("[Config] `.../config/ctd.json5`.");
            saveConfig();
        }
    }

    public static void reloadConfig(CommandContext ctx) {
        ServerCommandSource source = (ServerCommandSource) ctx.getSource();
        if (configFile.exists()) {
            try {
                config = jankson.fromJson(jankson.load(configFile), Config.class);
                LoggingHandlers.info("[Config] Reloaded configuration file.");
                source.sendFeedback(() -> Text.literal("[Config] Reloaded configuration file.").formatted(Formatting.GRAY), true);
            } catch (IOException | SyntaxError e) {
                LoggingHandlers.error("[Config] Error loading configuration file; using default values.");
                e.printStackTrace();
                source.sendFeedback(() -> Text.literal("[Config] Error loading configuration file; using default values.").formatted(Formatting.RED), true);
                config = new Config();
            }
        } else {
            config = new Config();
            LoggingHandlers.info("[Config] No configuration file found, created new one.");
            LoggingHandlers.info("[Config] `.../config/ctd.json5`.");
            LoggingHandlers.warn("[Config] Note that this generally shouldn't be happening, a file should be made and available before your run!");
            source.sendFeedback(() -> Text.literal("""
                    [Config] No configuration file found, created new one.
                    [Config] `.../config/ctd.json5`.""").formatted(Formatting.GRAY), true);
            source.sendFeedback(() -> Text.literal("[Config] Note that this generally shouldn't be happening, a file should be made and available before your run!").formatted(Formatting.GOLD), true);
            saveConfig();
        }
    }

    public static void saveConfig() {
        try (FileOutputStream stream = new FileOutputStream(configFile)) {
            JsonElement json = jankson.toJson(config);
            stream.write(json.toJson(true, true).getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
