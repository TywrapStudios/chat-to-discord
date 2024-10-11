package net.tywrapstudios.ctd.handlers;

import net.tywrapstudios.ctd.compat.DiscordSafety;
import net.tywrapstudios.ctd.compat.Xaero;

public class CompatHandlers {
    public static String handleCompat(String message) {
        message = Xaero.convertWayPointMessage(message);
        message = DiscordSafety.modifyToNegateDangerousPings(message);
        message = DiscordSafety.modifyToNegateMarkdown(message);

        return message;
    }
}
