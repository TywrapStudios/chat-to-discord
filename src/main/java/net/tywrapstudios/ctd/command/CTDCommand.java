package net.tywrapstudios.ctd.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.tywrapstudios.ctd.ChatToDiscord;
import net.tywrapstudios.ctd.config.Manager;
import net.tywrapstudios.ctd.config.config.Config;
import net.tywrapstudios.ctd.handlers.LoggingHandlers;
import org.slf4j.Logger;

import java.util.List;
import java.util.Objects;

public class CTDCommand {
    public CTDCommand() {
    }

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("ctd").requires((source) -> {
            return source.hasPermissionLevel(2);
        }).executes((context) -> {
            return execute(context);
        }).then(CommandManager.literal("reload").executes((context) -> {
            return reload(context);
        })));
    }

    private static int execute(CommandContext<ServerCommandSource> context) {
        Config config = Manager.getConfig();
        List<String> webhooks = config.discord_webhooks;

        ServerCommandSource source = context.getSource();
        String whenDebug = "";
        if (config.debug_mode) {
            whenDebug = "\n> DEBUG: ENABLED";
        }
        String message = String.format("""
                    -----[ChatToDiscord]-----
                    > Mod Version: %s
                    > Config Version: %s
                    > Embed Mode: %s
                    > Only Messages Mode: %s
                    > Webhooks Defined: %s%s
                    -----------------------""", ChatToDiscord.MOD_V, ChatToDiscord.CONFIG_V, config.embed_mode, config.only_send_messages, webhooks.size(), whenDebug);
        source.sendFeedback(() -> {
            return Text.literal(message).formatted(Formatting.BLUE);
        }, false);
        return 1;
    }

    private static int reload(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        if (Objects.equals(Manager.getConfig().CONFIG_DO_NOT_TOUCH, ChatToDiscord.CONFIG_V)) {
            source.sendFeedback(() -> {
                return Text.literal("[ChatToDiscord] Reloading!").formatted(Formatting.GRAY);
            }, true);
            Manager.reloadConfig(context);
        } else {
            source.sendFeedback(() -> {
                return Text.literal("[ChatToDiscord] Could not reload CTD Config: Version out of sync, please delete your config file and rerun Minecraft.").formatted(Formatting.RED);
            }, false);
            if (!Manager.getConfig().suppress_warns) {
                LoggingHandlers.error("[Config] Your Config Version is out of Sync, please delete your config file and reload Minecraft.");
            }
        }
        return 1;
    }
}
