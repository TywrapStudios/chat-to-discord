package net.tywrapstudios.ctd.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.tywrapstudios.ctd.ChatToDiscord;
import net.tywrapstudios.ctd.compat.Spark;
import net.tywrapstudios.ctd.config.Config;
import net.tywrapstudios.ctd.handlers.Handlers;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class CTDCommand {
    public CTDCommand() {
    }

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("ctd").requires((source) -> source.hasPermissionLevel(2))
                .executes(CTDCommand::execute)
                .then(CommandManager.literal("reload")
                        .executes(CTDCommand::reload))
                .then(CommandManager.literal("debug")
                        .requires((source) -> source.hasPermissionLevel(3))
                        .then(CommandManager.literal("dump_config")
                                .executes(CTDCommand::dumpConfig))
                        .then(CommandManager.literal("force_chat")
                                .executes(CTDCommand::forceChat))
                        .then(CommandManager.literal("force_game")
                                .executes(CTDCommand::forceGameMessage))
                        .then(CommandManager.literal("force_crash")
                                .executes(CTDCommand::forceCrashMessage))
                        .then(CommandManager.literal("force_timeout")
                                .executes(CTDCommand::forceTimeoutMessage))
                )
        );
    }

    private static int forceTimeoutMessage(CommandContext<ServerCommandSource> context) {
        Spark.handleSparkWorldTimeOut(new TimeoutException("DEBUG TIMEOUT"));
        return 1;
    }

    private static int forceCrashMessage(CommandContext<ServerCommandSource> context) {
        try {
            Handlers.handleCrash("DEBUG CAUSE", "~~DEBUG CAUSE~~");
        } catch (ExecutionException | InterruptedException ignored) {}
        return 1;
    }

    private static int forceGameMessage(CommandContext<ServerCommandSource> context) {
        Handlers.handleGameMessage("Debug message");
        return 1;
    }

    private static int forceChat(CommandContext<ServerCommandSource> context) {
        Handlers.handleChatMessage("Debug message", context.getSource().getPlayer().getUuidAsString(), context.getSource().getPlayer().getEntityName());
        return 1;
    }

    private static int dumpConfig(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        String message = String.format("""
                    --------[Config]---------
                    %s
                    -----------------------""",
                ChatToDiscord.CONFIG_MANAGER.getConfigJsonAsString(false, true));
        source.sendFeedback(() -> Text.literal(message).formatted(Formatting.GRAY), false);
        return 1;
    }

    private static int execute(CommandContext<ServerCommandSource> context) {
        Config config = ChatToDiscord.CONFIG_MANAGER.getConfig();
        List<String> webhooks = config.discord_config.discord_webhooks;

        ServerCommandSource source = context.getSource();
        String whenDebug = "";
        if (config.util_config.debug_mode) {
            whenDebug = "\n> DEBUG: ENABLED";
        }
        String message = String.format("""
                    -----[ChatToDiscord]-----
                    > Mod Version: %s
                    > Config Version: %s
                    > Embed Mode: %s
                    > Only Messages Mode: %s
                    > Webhooks Defined: %s%s
                    -----------------------""", ChatToDiscord.MOD_V, ChatToDiscord.CONFIG_V, config.discord_config.embed_mode, config.discord_config.only_send_messages, webhooks.size(), whenDebug);
        source.sendFeedback(() -> {
            return Text.literal(message).formatted(Formatting.BLUE);
        }, false);
        return 1;
    }

    private static int reload(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        if (Objects.equals(ChatToDiscord.CONFIG_MANAGER.getConfig().format_version, ChatToDiscord.CONFIG_V)) {
            source.sendFeedback(() -> {
                return Text.literal("[ChatToDiscord] Reloading!").formatted(Formatting.GRAY);
            }, true);
            ChatToDiscord.CONFIG_MANAGER.loadConfig();
        } else {
            source.sendFeedback(() -> Text.literal("[ChatToDiscord] Could not reload CTD Config: Version out of sync, please delete your config file and rerun Minecraft.").formatted(Formatting.RED), false);
            if (!ChatToDiscord.CONFIG_MANAGER.getConfig().util_config.suppress_warns) {
                ChatToDiscord.LOGGING.error("[Config] Your Config Version is out of Sync, please delete your config file and reload Minecraft.");
            }
        }
        return 1;
    }
}
