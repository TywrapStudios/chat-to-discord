package net.tywrapstudios.ctd;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import net.tywrapstudios.ctd.config.Manager;
import net.tywrapstudios.ctd.config.config.Config;
import net.tywrapstudios.ctd.discord.Discord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

public class ChatToDiscord implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("CTD");
	public static final Logger DEBUG = LoggerFactory.getLogger("CTD-Debug");
	public static final String CONFIG_V = "1";

	@Override
	public void onInitialize() {
		LOGGER.info("[CTD] CTD Loading up.");
		Manager.loadConfig();
		Config config = Manager.getConfig();
		List<String> webhookUrlsList = config.discord_webhooks;
		if (!Objects.equals(config.CONFIG_DO_NOT_TOUCH, CONFIG_V)&&!config.suppress_warns) {
			LOGGER.error("[Config] Your Config somehow got out of sync with the version it's supposed to be. This can be dangerous. Try to re-run the instance or delete the log file.");
		}
		if (webhookUrlsList.isEmpty()&&!config.suppress_warns) {
			LOGGER.error("[Discord] No Webhooks Defined! Please Configure your webhooks in the Config file: ctd.json");
		}
		if (Objects.equals(config.pastebin_api_key, "")&&!config.suppress_warns) {
			LOGGER.error("[Pastebin] No Pastebin API Key Defined! Please Configure a Key in the Config file: ctd.json");
		}
		initializeCTD(config);
		ServerLifecycleEvents.SERVER_STARTED.register(this::onServerStart);
		ServerLifecycleEvents.SERVER_STOPPING.register(this::onServerStop);
	}

	private void onServerStart(MinecraftServer server) {
		Config config = Manager.getConfig();
		List<String> webhookUrls = config.discord_webhooks;
		for (String url : webhookUrls) {
			if (!config.embed_mode) {
				Discord.sendMessageToDiscord("Server started.", "Console", url, "console");
			} else {
				Discord.sendEmbedToDiscord("Server started.", "Console", url, "console", config.embed_color_rgb_int);
			}
			if (Objects.equals(config.pastebin_api_key, "")&&!config.suppress_warns) {
				Discord.sendEmbedToDiscord("No Pastebin API Key Defined! Please Configure a Key in the Config file: ctd.json", "CTD-Internals", url, "console", 7864320);
			}
		}
	}

	private void onServerStop(MinecraftServer server) {
		Config config = Manager.getConfig();
		List<String> webhookUrls = config.discord_webhooks;
		for (String url : webhookUrls) {
			if (!config.embed_mode) {
				Discord.sendMessageToDiscord("Server stopped.", "Console", url, "console");
			} else {
				Discord.sendEmbedToDiscord("Server stopped.", "Console", url, "console", config.embed_color_rgb_int);
			}
			if (Objects.equals(config.pastebin_api_key, "")&&!config.suppress_warns) {
				Discord.sendEmbedToDiscord("No Pastebin API Key Defined! Please Configure a Key in the Config file: ctd.json", "CTD-Internals", url, "console", 7864320);
			}
		}
	}

	private static void initializeCTD(Config config) {
		if (config.debug_mode) {
			DEBUG.info("[CTD] Debug mode enabled.");
		}
		if (config.embed_mode) {
			LOGGER.info("[CTD] Embed mode enabled.");
		} else {
			LOGGER.info("[CTD] Embed mode disabled.");
		}
	}
}