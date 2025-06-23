package org.example;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseManager {

    private static final HikariDataSource dataSource;

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://aws-0-ap-south-1.pooler.supabase.com:6543/postgres?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory&pgbouncer=true");
        config.setUsername("postgres.xfyltptitzikpzwnzblo");
        config.setPassword("Akshay@123@supabase");

        // Optional but important
        config.setMaximumPoolSize(5);  // Don't exceed Supabase limit (15)
        config.setMinimumIdle(1);
        config.setIdleTimeout(10000);
        config.setConnectionTimeout(30000);
        config.setMaxLifetime(600000); // 10 mins
        config.setPoolName("SupabasePool");

        dataSource = new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
