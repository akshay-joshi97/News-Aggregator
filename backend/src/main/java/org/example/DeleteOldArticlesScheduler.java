package org.example;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DeleteOldArticlesScheduler {

    public static void main(String[] args) {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

        executor.scheduleAtFixedRate(
                () -> deleteOldArticles(2),
                30,        // 30 seconds initial delay
                86400,     // 24 hours = 86400 seconds
                TimeUnit.SECONDS
        );
    }

    public static void deleteOldArticles(int daysOld) {
        String sql = "{ ? = call delete_old_articles(?) }";

        try (Connection conn = DatabaseManager.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.registerOutParameter(1, Types.INTEGER); // for rows_deleted
            stmt.setInt(2, daysOld); // input param

            stmt.execute();

            int rowsDeleted = stmt.getInt(1);

            if (rowsDeleted != -1)
                System.out.println("Deleted " + rowsDeleted + " articles older than " + daysOld + " days.");
            else
                System.out.println("Failed to delete articles older than " + daysOld + " days.");


        } catch (Exception e) {
            Logger.addLog("Failed to call delete_old_articles(" + daysOld + ")", "DeleteOldArticlesScheduler");
            System.err.println("Failed to call delete_old_articles(" + daysOld + ")");
            e.printStackTrace();
        }
    }
}
