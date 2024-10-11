package net.tywrapstudios.ctd.handlers;

import net.tywrapstudios.ctd.ChatToDiscord;
import net.tywrapstudios.ctd.config.Manager;
import net.tywrapstudios.ctd.config.config.Config;
import org.slf4j.Logger;

public class LoggingHandlers {
    static Logger logger = ChatToDiscord.LOGGER;
    static Logger debug = ChatToDiscord.DEBUG;
    static Config config = Manager.getConfig();

    public static void info(String message) {
        logger.info(message);
    }

    public static void warn(String message) {
        if (!config.suppress_warns) {
            logger.warn(message);
        }
    }

    public static void error(String message) {
        if (!config.suppress_warns) {
            logger.error(message);
        }
    }

    public static void debug(String message) {
        if (config.debug_mode) {
            debug.info(message);
        }
    }

    public static void debugWarning(String message) {
        if (config.debug_mode&&!config.suppress_warns) {
            debug.warn(message);
        }
    }
}
