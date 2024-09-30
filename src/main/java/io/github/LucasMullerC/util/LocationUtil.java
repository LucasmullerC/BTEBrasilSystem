package io.github.LucasMullerC.util;

import org.bukkit.Location;
import org.bukkit.Bukkit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LocationUtil {
    public static Location fromString(String locationString) {
        Pattern pattern = Pattern.compile("Location\\{world=CraftWorld\\{name=(.*?)\\},x=(-?\\d+\\.\\d+),y=(-?\\d+\\.\\d+),z=(-?\\d+\\.\\d+),pitch=(-?\\d+\\.\\d+),yaw=(-?\\d+\\.\\d+)\\}");
        Matcher matcher = pattern.matcher(locationString);

        if (matcher.matches()) {
            String worldName = matcher.group(1);
            double x = Double.parseDouble(matcher.group(2));
            double y = Double.parseDouble(matcher.group(3));
            double z = Double.parseDouble(matcher.group(4));
            float pitch = Float.parseFloat(matcher.group(5));
            float yaw = Float.parseFloat(matcher.group(6));

            return new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
        } else {
            throw new IllegalArgumentException("Invalid location format: " + locationString);
        }
    }
}

