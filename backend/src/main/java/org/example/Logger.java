package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Logger {
    public static void addLog(String message, String source) {
        String insertSQL = "INSERT INTO logger (message, source) " +
                "VALUES (?, ?) " +
                "ON CONFLICT (id) DO NOTHING";  // Avoid duplicate primary key errors

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(insertSQL)){
            stmt.setString(1, message);
            stmt.setString(2, source);

            stmt.executeUpdate();
            System.out.println(message + " successfully logged.");
        } catch (SQLException e) {
            System.err.println("Failed to log message: " + message);
            throw new RuntimeException(e);
        }
    }
}
