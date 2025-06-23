package org.example;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {

    private static final String URL = "jdbc:postgresql://aws-0-ap-south-1.pooler.supabase.com:6543/postgres?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory&pgbouncer=true";
    private static final String USER = "postgres.xfyltptitzikpzwnzblo";
    private static final String PASSWORD = "Akshay@123@supabase";

    static {
        try {
            // Load PostgreSQL driver
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("PostgreSQL JDBC Driver not found.");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Test connection
    public static void main(String[] args) {
        try (Connection conn = getConnection()) {
            if (conn != null) {
                System.out.println("Connected to Supabase successfully!");
            }
        } catch (SQLException e) {
            System.err.println("Connection failed!");
            e.printStackTrace();
        }
    }
}
