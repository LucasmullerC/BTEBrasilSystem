package io.github.LucasMullerC.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import io.github.LucasMullerC.service.data.DatabaseService;

public class DatabaseUtils {
    public static void addToDatabase(String[] values, String table) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            DatabaseService databaseService = new DatabaseService();
            databaseService.addRecord(conn, table, values);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void removeFromDatabase(String table, String condition) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            DatabaseService databaseService = new DatabaseService();
            databaseService.removeRecord(conn, table, condition);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateDatabase(String table, String[] values, String[] columns, String condition) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            DatabaseService databaseService = new DatabaseService();
            databaseService.updateRecord(conn, table, values, columns, condition);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<String[]> getFromDatabase(String table, String condition) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            DatabaseService databaseService = new DatabaseService();
            return databaseService.getRecord(conn, table, condition);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
