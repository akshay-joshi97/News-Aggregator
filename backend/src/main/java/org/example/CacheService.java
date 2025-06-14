package org.example;

import java.util.*;

public class CacheService {
    private final Map<Integer, LRUCache<Integer, Article>> userCacheMap;
    private final int cacheCapacity;

    public CacheService(int capacity) {
        this.userCacheMap = new HashMap<>();
        this.cacheCapacity = capacity;
    }

    public Article getUserArticle(int userId, int articleId) {
        LRUCache<Integer, Article> userCache = userCacheMap.computeIfAbsent(
                userId, k -> new LRUCache<>(cacheCapacity)
        );

        Article article = userCache.get(articleId);
        if (article != null) {
            System.out.println("✅ Article found in cache for user " + userId + ": " + article);
            return article;
        }

        System.out.println("❌ Cache miss for user " + userId + ". Fetching from database...");
        article = FakeDatabase.getArticle(articleId);

        if (article != null) {
            userCache.put(articleId, article);
        }

        return article;
    }

    public void printUserCache(int userId) {
        LRUCache<Integer, Article> cache = userCacheMap.get(userId);
        if (cache == null) {
            System.out.println("ℹ️ No cache found for user " + userId);
        } else {
            System.out.println("🧠 Cache for user " + userId + ":");
            cache.printCache();
        }
    }
}

