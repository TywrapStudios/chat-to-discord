package net.tywrapstudios.ctd;

import net.fabricmc.api.ModInitializer;

import net.tywrapstudios.ctd.config.Manager;
import net.tywrapstudios.ctd.config.config.Config;
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
		LOGGER.info("[CTD] CTD Ready.");
		Manager.loadConfig();
		Config config = Manager.getConfig();
		List<String> webhookUrlsList = config.discord_webhooks;
		if (!Objects.equals(config.CONFIG_DO_NOT_TOUCH, CONFIG_V)) {
			LOGGER.warn("[Config] Your Config somehow got out of sync with the version it's supposed to be. This can be dangerous. Try to re-run the instance or delete the log file.");
		}
		if (webhookUrlsList.isEmpty()) {
			LOGGER.error("[Discord] No webhooks configured! Please configure your webhooks in the Config file: ctd.json");
		}
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