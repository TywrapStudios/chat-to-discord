package net.tywrapstudios.ctd;

import gs.mclo.api.MclogsClient;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.tywrapstudios.ctd.command.CTDCommand;
import net.tywrapstudios.ctd.config.Config;
import net.tywrapstudios.ctd.config.ConfigManager;
import net.tywrapstudios.ctd.handlers.Handlers;
import net.tywrapstudios.ctd.handlers.LoggingHandlers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ChatToDiscord implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("CTD");
	public static final Logger DEBUG = LoggerFactory.getLogger("CTD-Debug");
	public static final String CONFIG_V = "2.0";
	public static final String MOD_V = FabricLoader.getInstance().getModContainer("ctd").orElseThrow().getMetadata().getVersion().getFriendlyString();
	public static final MclogsClient MCL = new MclogsClient("Chat To Discord");

	@Override
	public void onInitialize() {
		LOGGER.info("[CTD] CTD Loading up.");

		Optional<ModContainer> ctdModContainer = FabricLoader.getInstance().getModContainer("ctd");

		MCL.setProjectVersion(ctdModContainer.isPresent() ? ctdModContainer.get().getMetadata().getVersion().getFriendlyString() : "unknown");

		ConfigManager.loadConfig();
		registerCTDCommand();

		initializeCTD();

		ServerLifecycleEvents.SERVER_STARTED.register(this::onServerStart);
		ServerLifecycleEvents.SERVER_STOPPING.register(this::onServerStop);
		ServerMessageEvents.CHAT_MESSAGE.register(this::onChatMessage);
		ServerMessageEvents.GAME_MESSAGE.register(this::onGameMessage);
		ServerMessageEvents.COMMAND_MESSAGE.register(this::onCommandMessage);
	}

	private void onCommandMessage(SignedMessage signedMessage, ServerCommandSource serverCommandSource, MessageType.Parameters parameters) {
		String message = signedMessage.getContent().getString();
		String authorUUID = signedMessage.getSender().toString();
		String authorName = serverCommandSource.getName();

		Handlers.handleChatMessage(message, authorUUID, authorName);
	}

	private void onGameMessage(MinecraftServer minecraftServer, Text text, boolean b) {
		Handlers.handleGameMessage(text.getString());
	}

	private void onChatMessage(SignedMessage signedMessage, ServerPlayerEntity serverPlayerEntity, MessageType.Parameters parameters) {
		String message = signedMessage.getContent().getString();
		String authorUUID = signedMessage.getSender().toString();
		String authorName = serverPlayerEntity.getEntityName();

		Handlers.handleChatMessage(message, authorUUID, authorName);
	}

	private void onServerStart(MinecraftServer server) {
		Handlers.handleChatMessage("Server started.","console","Console");
	}

	private void onServerStop(MinecraftServer server) {
		Handlers.handleChatMessage("Server stopped.","console","Console");
	}

	public static void registerCTDCommand() {
		Config config = ConfigManager.config;
		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated, registrationEnvironment) -> {
			CTDCommand.register(dispatcher);
		});
		if (config.util_config.debug_mode) {
			DEBUG.info("[CTD] Registered commands.");
		}
	}

	private static void initializeCTD() {
		Config config = ConfigManager.config;
		List<String> webhookUrlsList = config.discord_config.discord_webhooks;

		if (!Objects.equals(config.format_version, CONFIG_V)) {
			LoggingHandlers.error("[Config] Your Config somehow got out of sync with the version it's supposed to be. This can be dangerous. Try to re-run the instance after deleting the initial config file.");
		}
		if (webhookUrlsList.isEmpty()) {
			LoggingHandlers.error("[Discord] No Webhooks Defined! Please Configure your webhooks in the Config file: ctd.json5");
		}
		LoggingHandlers.debug("[CTD] Debug mode enabled.");
		if (config.discord_config.embed_mode) {
			LoggingHandlers.info("[CTD] Embed mode enabled.");
		} else {
			LoggingHandlers.info("[CTD] Embed mode disabled.");
		}
	}
}