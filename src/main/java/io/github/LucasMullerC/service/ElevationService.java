package io.github.LucasMullerC.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ElevationService {
    public String getElevationOpenElevation(double latitude, double longitude){
        String apiUrl = "https://api.open-elevation.com/api/v1/lookup?locations="+latitude+","+longitude;

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();

            String json = response.toString();
            System.out.println(json);
            String elevation = parseElevation(json);
            return elevation;
        } catch (Exception e) {
            e.printStackTrace();
            return "-1"; // Retorna -1 em caso de erro
        }
    }

    private String parseElevation(String json) {
        String key = "\"elevation\":";
        int index = json.indexOf(key);

        if (index == -1) {
            throw new RuntimeException("Elevação não encontrada no JSON.");
        }

        int start = index + key.length();
        int end = json.indexOf(",", start);
        if (end == -1) end = json.indexOf("}", start);

        String elevationValue = json.substring(start, end).trim();
        return elevationValue;
    }
}
