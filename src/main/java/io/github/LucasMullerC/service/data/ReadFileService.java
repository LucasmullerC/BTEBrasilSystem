package io.github.LucasMullerC.service.data;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import io.github.LucasMullerC.BTEBrasilSystem.BTEBrasilSystem;
import io.github.LucasMullerC.util.DatabaseConnection;

public class ReadFileService {
    private File storageFile;

    public void convertOldFilesToSql() {
        BTEBrasilSystem plugin = BTEBrasilSystem.getPlugin();
        try {
            Connection conn = DatabaseConnection.getConnection();
            String pluginFolder = plugin.getDataFolder().getAbsolutePath();
            (new File(pluginFolder)).mkdirs();
            loadToSql(new File(pluginFolder + File.separator + "areas.txt"), conn, "claims");
            loadToSql(new File(pluginFolder + File.separator + "builders.txt"), conn, "builders");
            loadToSql(new File(pluginFolder + File.separator + "conquistas.txt"), conn, "awards");
            loadToSql(new File(pluginFolder + File.separator + "pendente.txt"), conn, "pending");
            loadToSql(new File(pluginFolder + File.separator + "zonas.txt"), conn, "applicationzones");
            loadToSql(new File(pluginFolder + File.separator + "aplicantes.txt"), conn, "applicants");
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadToSql(File file, Connection connection, String tableName) {
        this.storageFile = file;

        if (this.storageFile.exists() == true) {
            List<String> lines = readLinesFromFile(file);

            if (!lines.isEmpty()) {
                String firstLine = lines.get(0);
                String[] columnNames = firstLine.split(";");

                for (int i = 1; i < lines.size(); i++) {
                    String[] values = lines.get(i).split(";");
                    DatabaseService databaseService = new DatabaseService();
                    databaseService.addRecord(connection, tableName, columnNames, values);
                }
            }
        }
    }

    private List<String> readLinesFromFile(File file) {
        List<String> lines = new ArrayList<>();

        try (DataInputStream input = new DataInputStream(new FileInputStream(file));
                BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {

            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return lines;
    }

}
