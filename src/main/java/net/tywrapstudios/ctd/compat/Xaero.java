package net.tywrapstudios.ctd.compat;

public class Xaero {
    public static String convertWayPointMessage(String message) {
        String[] vars = message.split(":");
        if (vars.length < 10) {
            return message;
        }
        if (!message.startsWith("xaero")) {
            return message;
        }

        String name = vars[1];
        String x = vars[3];
        String y = vars[4];
        String z = vars[5];

        return String.format("Shared Waypoint \"%s\" with Coordinates %s %s %s.", name, x, y, z);
    }
}
