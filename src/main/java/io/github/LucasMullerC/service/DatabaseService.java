package io.github.LucasMullerC.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseService {

    public void addRecord(Connection connection, String tableName, String[] columnNames, String[] values) {
        if (columnNames.length != values.length) {
            System.out.println("Incorrect number of values. Unable to add record.");
            return;
        }

        String sql = buildInsertSQL(tableName, columnNames.length);
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            for (int i = 0; i < values.length; i++) {
                preparedStatement.setString(i + 1, values[i]);
            }
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private String buildInsertSQL(String tableName, int numValores) {
        StringBuilder sql = new StringBuilder("INSERT INTO " + tableName + " VALUES (");
        for (int i = 0; i < numValores; i++) {
            sql.append("?, ");
        }
        sql.setLength(sql.length() - 2);
        sql.append(")");
        return sql.toString();
    }
}

