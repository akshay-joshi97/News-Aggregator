package org.example;
import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

public class ArticleSaver {
    public static void saveArticleToDb(Article article) {
        String insertSQL = "INSERT INTO articles (id, title, content, topics, author, url, published_at, description, image_url, source_name, source_priority, source_url, source_icon, language, popularity) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                "ON CONFLICT (id) DO NOTHING";  // Avoid duplicate primary key errors

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(insertSQL)) {

            stmt.setString(1, article.getId());
            stmt.setString(2, article.getTitle());
            stmt.setString(3, article.getContent());
            List<String> topicsList = article.getTopics();
            if (topicsList != null && !topicsList.isEmpty()) {
                // Convert List<String> to SQL Array
                Array sqlArray = conn.createArrayOf("text", topicsList.toArray());
                stmt.setArray(4, sqlArray);
            } else {
                stmt.setArray(4, null); // or empty array as needed
            }
            stmt.setString(5, article.getAuthor());
            stmt.setString(6, article.getUrl());
            stmt.setTimestamp(7, article.getPublishedAt());
            stmt.setString(8, article.getDescription());
            stmt.setString(9, article.getImageUrl());
            stmt.setString(10, article.getSourceName());
            stmt.setInt(11, article.getSourcePriority());
            stmt.setString(12, article.getSourceUrl());
            stmt.setString(13, article.getSourceIcon());
            stmt.setString(14, article.getLanguage());
            stmt.setInt(15, article.getPopularity());

            stmt.executeUpdate();
            System.out.println(article.getId() + " successfully saved to database.");

        } catch (Exception e) {
            System.err.println("Failed to save article ID: " + article.getId());
            Logger.addLog("Failed to save article ID: " + article.getId(), "ArticleSaver");
            e.printStackTrace();
        }
    }
}

