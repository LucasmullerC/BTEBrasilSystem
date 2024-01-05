package io.github.LucasMullerC.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.bukkit.configuration.file.FileConfiguration;

import io.github.LucasMullerC.BTEBrasilSystem.BTEBrasilSystem;

public class DatabaseConnection {
    private static String HOST;
    private static String PORT;
    private static String NAME;
    private static String USER;
    private static String PASSWORD;

    public static Connection getConnection() throws SQLException {
        CreateDatabaseConfig();
        String url = "jdbc:mysql://" + HOST +
                     ":" + PORT +
                     "/" + NAME;

        return DriverManager.getConnection(url, USER, PASSWORD);
    }

    private static void CreateDatabaseConfig(){
        BTEBrasilSystem plugin = BTEBrasilSystem.getPlugin();
        FileConfiguration configFile = plugin.getConfig();

        if (configFile.getKeys(true).size() == 0 ){
            configFile.set("database.host","hostname");
            configFile.set("database.port","port");
            configFile.set("database.name","name");
            configFile.set("database.user","user");
            configFile.set("database.password","password");   

            plugin.saveConfig();
        }
        else{
            HOST = configFile.getString("database.host");
            PORT = configFile.getString("database.port");
            NAME = configFile.getString("database.name");
            USER = configFile.getString("database.user");
            PASSWORD = configFile.getString("database.password");
        }
    }
}

