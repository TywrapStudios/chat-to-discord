package net.tywrapstudios.ctd.compat;

public class DiscordSafety {
    public static String modifyToNegateDangerousPings(String message) {
        if (!message.contains("@everyone")&&!message.contains("@here")) {
            return message;
        }
        message = message.replace("@everyone", "`@everyone`[ping negated]");
        message = message.replace("@here", "`@here`[ping negated]");
        return message;
    }
    
    public static String modifyToNegateMarkdown(String message) {
        message = message.replace("_", "\\_");
        return message;
    }
}
