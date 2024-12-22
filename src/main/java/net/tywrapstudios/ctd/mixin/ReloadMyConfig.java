package net.tywrapstudios.ctd.mixin;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ReloadCommand;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.tywrapstudios.ctd.ChatToDiscord;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(ReloadCommand.class)
public abstract class ReloadMyConfig {
    @Inject(method = "method_13530",
            at = @At(value = "HEAD")
    )
    private static void ctd$reloadMyConfig(CommandContext context, CallbackInfoReturnable<Integer> cir) {
        ServerCommandSource source = (ServerCommandSource) context.getSource();
        if (Objects.equals(ChatToDiscord.CONFIG_MANAGER.getConfig().format_version, ChatToDiscord.CONFIG_V)) {
            ChatToDiscord.CONFIG_MANAGER.loadConfig();
        } else {
            source.sendFeedback(() -> Text.literal("[ChatToDiscord] Could not reload CTD Config: Version out of sync, please delete your config file and rerun Minecraft.").formatted(Formatting.DARK_RED), false);
            if (!ChatToDiscord.CONFIG_MANAGER.getConfig().util_config.suppress_warns) {
                ChatToDiscord.LOGGING.error("[Config] Your Config Version is out of Sync, please delete your config file and reload Minecraft.");
            }
        }
    }
}
