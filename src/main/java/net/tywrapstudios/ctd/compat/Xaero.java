package net.tywrapstudios.ctd.compat;

import org.jetbrains.annotations.NotNull;

public class Xaero {
    public static String convertWayPointMessage(String message) {
        String[] vars = message.split(":");
        if (vars.length <= 10) {
            return message;
        }
        if (!message.startsWith("xaero")) {
            return message;
        }

        String name = vars[1];
        String x = vars[3];
        String y = vars[4];
        String z = vars[5];
        String dimension = getDimension(vars);

        return String.format("Shared Waypoint \"%s\" with Coordinates %s %s %s in %s.", name, x, y, z, dimension);
    }

    private static @NotNull String getDimension(String[] vars) {
        String dimension = vars[9];
        if (dimension.contains("end")||dimension.contains("nether")||dimension.contains("overworld")) {
            if (dimension.contains("end")) {
                dimension = "the End";
            }
            if (dimension.contains("nether")) {
                dimension = "the Nether";
            }
            if (dimension.contains("overworld")) {
                dimension = "the Overworld";
            }
        } else {
            dimension = "an Unknown Dimension";
        }
        return dimension;
    }
}
