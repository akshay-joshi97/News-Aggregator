package org.example;
import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseTest {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://db.xfyltptitzikpzwnzblo.supabase.co:5432/postgres?sslmode=require";
        String user = "postgres";
        String password = "Akshay@123@supabase";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("✅ Connected successfully to Supabase!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
