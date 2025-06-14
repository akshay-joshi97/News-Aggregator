package org.example;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.util.*;

public class TopicCacheUpdationBatch {

    public static void topicCacheUpdationProcedure(List<String> topics, int articlesPerTopic) {
        for (String topic : topics) {
            String sql = "{ ? = call refresh_topic_cache(?, ?) }";

            try (Connection conn = DatabaseManager.getConnection();
                 CallableStatement stmt = conn.prepareCall(sql)) {

                stmt.registerOutParameter(1, Types.INTEGER);

                stmt.setString(2, topic);
                stmt.setInt(3, articlesPerTopic);

                // Execute the function
                stmt.execute();

                // Get the return value
                int isProcessSuccessful = stmt.getInt(1);

                if (isProcessSuccessful == 1) {
                    System.out.println("Successfully updated topic cache for: " + topic);
                } else {
                    System.out.println("Error occurred while updating topic cache for: " + topic);
                    Logger.addLog("refresh_topic_cache returned error code: " + isProcessSuccessful,
                            "TopicCacheUpdationBatch");
                }

            } catch (Exception e) {
                Logger.addLog("Failed to call refresh_topic_cache for topic: " + topic +
                        ". Error: " + e.getMessage(), "TopicCacheUpdationBatch");
                System.err.println("Failed to call refresh_topic_cache for topic: " + topic +
                        ". Error: " + e.getMessage());
            }
        }
    }
}