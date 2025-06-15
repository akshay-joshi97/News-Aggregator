package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    private static final String URL = System.getenv("DATABASE_URL") != null ? 
        System.getenv("DATABASE_URL") : 
        "jdbc:postgresql://db.xfyltptitzikpzwnzblo.supabase.co:5432/postgres?sslmode=require";
    private static final String USER = System.getenv("DATABASE_USER") != null ? 
        System.getenv("DATABASE_USER") : 
        "postgres";
    private static final String PASSWORD = System.getenv("DATABASE_PASSWORD") != null ? 
        System.getenv("DATABASE_PASSWORD") : 
        "Akshay@123@supabase";

    static {
        try {
            // Register the PostgreSQL driver
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("PostgreSQL JDBC Driver not found", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
