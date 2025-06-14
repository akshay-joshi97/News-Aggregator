package org.example;

import java.util.*;

public class FakeDatabase {

    private static final Map<Integer, Article> articleMap = new HashMap<>();

    static {
        // Simulated articles with category/topic strings
        articleMap.put(1, new Article("1", "AI and the Future", "Content about AI...", new ArrayList<String>(Arrays.asList("Technology")), 120));
        articleMap.put(2, new Article("2", "Global Warming Update", "Content about climate...", new ArrayList<String>(Arrays.asList("Environment")), 85));
        articleMap.put(3, new Article("3", "SpaceX Mars Plans", "Content about space...", new ArrayList<String>(Arrays.asList("Science")), 95));
        articleMap.put(4, new Article("4", "Stock Market Today", "Market trends and news...", new ArrayList<String>(Arrays.asList("Finance")), 70));
        articleMap.put(5, new Article("5", "ChatGPT Tips", "Using AI tools smartly...", new ArrayList<>(Arrays.asList("Technology")), 110));

    }

    // Fetch single article by ID
    public static Article getArticle(int id) {
        return articleMap.get(id);
    }

    // Fetch all articles
    public static List<Article> getAllArticles() {
        return new ArrayList<>(articleMap.values());
    }
}
