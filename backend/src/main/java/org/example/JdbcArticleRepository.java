package org.example;

import java.sql.*;
import java.util.*;

public class JdbcArticleRepository {
    private final Connection connection;

    public JdbcArticleRepository(Connection connection) {
        this.connection = connection;
    }

    // Get full articles from db based on article ids
    public List<Article> getArticles(Collection<String> articleIds) {
        if (articleIds == null || articleIds.isEmpty()) return List.of();

        List<Article> articles = new ArrayList<>();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < articleIds.size(); i++) {
            sb.append("?");
            if (i < articleIds.size() - 1) sb.append(",");
        }

        String sql = "SELECT * FROM articles WHERE id IN (" + sb.toString() + ") ORDER BY popularity DESC, COALESCE(source_priority, 0) DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            int index = 1;
            for (String articleId : articleIds) {
                stmt.setString(index++, articleId);
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Array topicsArray = rs.getArray("topics");
                String[] topics = topicsArray != null ? (String[]) topicsArray.getArray() : new String[0];
                List<String> topicList = Arrays.asList(topics);

                Article article = new Article(
                        rs.getString("id"),
                        rs.getString("title"),
                        rs.getString("content"),
                        topicList,
                        rs.getInt("popularity")
                );
                articles.add(article);
            }
        } catch (Exception e) {
            Logger.addLog("Error while fetching articles from DB.", "JdbcArticleRepository Class, getArticles method");
            System.out.println("Error in JdbcArticleRepository Class at :: " + Arrays.toString(e.getStackTrace()) + " error:: " + e.getMessage());
            throw new RuntimeException(e);
        }

        return articles;
    }

    // Fetch articles from topic_cache based on user's interest topics
    public List<Article> getRecommendedArticlesForUser(int userId) {
        Set<String> userInterests = new JdbcUserRepository(connection).getUserData(userId).getInterestTopics();
        if (userInterests == null || userInterests.isEmpty()) return List.of();

        List<String> articleIds = new ArrayList<>();
        StringBuilder topicPlaceholders = new StringBuilder();
        for (int i = 0; i < userInterests.size(); i++) {
            topicPlaceholders.append("?");
            if (i < userInterests.size() - 1) topicPlaceholders.append(",");
        }

        String sql = "SELECT DISTINCT article_id FROM topic_cache WHERE topic IN (" + topicPlaceholders + ")";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            int index = 1;
            for (String topic : userInterests) {
                stmt.setString(index++, topic.toLowerCase());
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                articleIds.add(rs.getString("article_id"));
            }

            return getArticlesSorted(articleIds); // get sorted articles
        } catch (Exception e) {
            System.out.println("Error in getRecommendedArticlesForUser: " + e.getMessage());
            e.printStackTrace();
            return List.of();
        }
    }

    //Sort the list of articles according to popularity and source_priority and return it
    public List<Article> getArticlesSorted(List<String> articleIds){
        if(articleIds == null || articleIds.isEmpty()) return List.of();

        List<Article> articles = new ArrayList<>();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < articleIds.size(); i++) {
            sb.append("?");
            if (i < articleIds.size() - 1) sb.append(",");
        }

        String sql = "SELECT * FROM articles WHERE id IN (" + sb + ") ORDER BY popularity DESC, COALESCE(source_priority, 0) DESC";
        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            int index = 1;
            for(String articleId : articleIds){
                stmt.setString(index++, articleId);
            }

            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                Array topicArray = rs.getArray("topics");
                List<String> topics = new ArrayList<>();
                if (topicArray != null) {
                    String[] topicStrings = (String[]) topicArray.getArray();
                    topics = Arrays.asList(topicStrings);
                }
                Article article = new Article(
                    rs.getString("id"),
                        rs.getString("title"),
                        rs.getString("content"),
                        topics,
                        rs.getInt("popularity")
                );
                articles.add(article);
            }
            return articles;
        } catch (Exception e) {
            Logger.addLog("Error while sorting articles, error: " + e.getMessage() + " at : " + Arrays.toString(e.getStackTrace()), "JdbcArticleRepository, getArticlesSorted method");
            throw new RuntimeException(e);
        }
    }
}
