package io.github.LucasMullerC.service.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseService {
    public void addRecord(Connection connection, String tableName, String[] values) {
        String sql = buildInsertSQL(tableName, values.length);
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            for (int i = 0; i < values.length; i++) {
                preparedStatement.setString(i + 1, values[i]);
            }
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateRecord(Connection connection, String tableName, String[] values,String[] columns, String condition) {
        String sql = buildUpdateSQL(tableName,columns, condition);
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            for (int i = 0; i < values.length; i++) {
                preparedStatement.setString(i + 1, values[i]);
            }
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeRecord(Connection connection, String tableName, String condition) {
        String sql = "DELETE FROM " + tableName + " WHERE " + condition;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String[]> getRecord(Connection connection, String tableName, String condition) {
        List<String[]> records = new ArrayList<>();
        String sql = "SELECT "+condition+" FROM " + tableName;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
                ResultSet resultSet = preparedStatement.executeQuery()) {

            int columnNum = resultSet.getMetaData().getColumnCount();

            while (resultSet.next()) {
                String[] values = new String[columnNum];
                for (int i = 0; i < columnNum; i++) {
                    values[i] = resultSet.getString(i + 1);
                }
                records.add(values);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return records;
    }

    private String buildInsertSQL(String tableName, int numValues) {
        StringBuilder sql = new StringBuilder("INSERT INTO " + tableName + " VALUES (");
        for (int i = 0; i < numValues; i++) {
            sql.append("?, ");
        }
        sql.setLength(sql.length() - 2);
        sql.append(")");
        return sql.toString();
    }

    private String buildUpdateSQL(String tableName, String[]columns, String condition) {
        StringBuilder sql = new StringBuilder("UPDATE " + tableName + " SET ");
        for (int i = 0; i < columns.length; i++) {
            sql.append(columns[i]).append(" = ?, ");
        }
        sql.setLength(sql.length() - 2);
        sql.append(" WHERE ").append(condition);
        return sql.toString();
    }
}
