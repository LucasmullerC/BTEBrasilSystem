package io.github.LucasMullerC.util;

import org.bukkit.Location;
import org.bukkit.World;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.bukkit.Bukkit;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Locale;
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

    public static Location getLocationFromPoints(String coordinatePoints, World world) {
        String[] ary = coordinatePoints.split(",");

        int spawnY = world.getHighestBlockYAt(Integer.parseInt(ary[0].split("\\.")[0]),
                Integer.parseInt(ary[1].split("\\.")[0]));

        int x = Integer.parseInt(ary[0].split("\\.")[0]);
        int y = spawnY + 10;
        int z = Integer.parseInt(ary[1].split("\\.")[0]);
        Location loc = new Location(world, x, y, z);
        return loc;
    }

    public static String getCityName(double latitude, double longitude) {
        String cityName = null;
        String url = String.format(Locale.ENGLISH,
            "https://nominatim.openstreetmap.org/reverse?format=json&lat=%f&lon=%f", 
            latitude, longitude
        );
        try {
            // Configurando o cliente HTTP
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("User-Agent", "JavaLocationApp/1.0")
                .GET()
                .build();

            // Enviando a requisição e obtendo a resposta
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();
            // Obtendo o nome da cidade a partir da resposta JSON
            JsonObject address = json.getAsJsonObject("address");
            if (address != null && address.has("city_district")) {
                cityName = address.get("city_district").getAsString();
            } else if (address != null && address.has("town")) {
                cityName = address.get("town").getAsString();
            } else if (address != null && address.has("village")) {
                cityName = address.get("village").getAsString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cityName != null ? cityName : "Brasil";
    }

    public static int[] getCentralPoint(String[] coordinates) {
        int[] centralCoordinate = new int[2];
    
        if (coordinates.length < 2) {
            return new int[]{0, 0}; 
        }
    
        int sumX = 0, sumZ = 0;
        int count = 0;
    
        for (int i = 0; i < coordinates.length; i += 2) {
            int x = Integer.parseInt(coordinates[i].split("\\.")[0]);
            int z = Integer.parseInt(coordinates[i + 1].split("\\.")[0]);
    
            sumX += x;
            sumZ += z;
            count++;
        }
    
        centralCoordinate[0] = sumX / count;
        centralCoordinate[1] = sumZ / count;
    
        return centralCoordinate;
    }
    
}

