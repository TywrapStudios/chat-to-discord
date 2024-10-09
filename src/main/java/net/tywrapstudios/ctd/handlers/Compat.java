package net.tywrapstudios.ctd.handlers;

import net.tywrapstudios.ctd.compat.Xaero;

public class Compat {
    public static String handleCompat(String message) {
        message = Xaero.convertWayPointMessage(message);

        return message;
    }
}
