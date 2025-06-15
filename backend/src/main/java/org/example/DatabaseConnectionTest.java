package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DatabaseConnectionTest {
    public static void main(String[] args) {
        // Using IPv6 address directly
        String url = "jdbc:postgresql://[2406:da1a:6b0:f608:c50c:e077:d3bb:b06f]:5432/postgres";
        Properties props = new Properties();
        props.setProperty("user", "postgres");
        props.setProperty("password", "Akshay@123@supabase");
        props.setProperty("ssl", "true");
        props.setProperty("sslmode", "require");
        props.setProperty("connectTimeout", "30");
        props.setProperty("socketTimeout", "30");
        
        System.out.println("Attempting to connect to: " + url);
        System.out.println("With properties: " + props);
        
        try {
            // First, try to load the driver
            System.out.println("Loading PostgreSQL driver...");
            Class.forName("org.postgresql.Driver");
            System.out.println("Driver loaded successfully");
            
            // Then try to connect
            System.out.println("Attempting connection...");
            Connection conn = DriverManager.getConnection(url, props);
            System.out.println("Connection successful!");
            conn.close();
        } catch (Exception e) {
            System.out.println("Connection failed!");
            System.out.println("Error type: " + e.getClass().getName());
            System.out.println("Error message: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 