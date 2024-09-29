package net.tywrapstudios.ctd.mixin;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ReloadCommand;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.tywrapstudios.ctd.ChatToDiscord;
import net.tywrapstudios.ctd.config.Manager;
import org.slf4j.Logger;
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
    private static void pekilog$ReloadMyConfig(CommandContext context, CallbackInfoReturnable<Integer> cir) {
        Logger logger = ChatToDiscord.LOGGER;
        ServerCommandSource source = (ServerCommandSource) context.getSource();
        if (Objects.equals(Manager.getConfig().CONFIG_DO_NOT_TOUCH, ChatToDiscord.CONFIG_V)) {
            Manager.reloadConfig(context);
        } else {
            source.sendFeedback(() -> {
                return Text.literal("[ChatToDiscord] Could not reload CTD Config: Version out of sync, please delete your config file and rerun Minecraft.").formatted(Formatting.DARK_RED);
            }, false);
            logger.error("[Config] Your Config Version is out of Sync, please delete your config file and reload Minecraft.");
        }
    }
}
