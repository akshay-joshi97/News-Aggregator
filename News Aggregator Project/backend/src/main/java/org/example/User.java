package org.example;

import java.util.*;

public class User {
    private int id;
    private String name;
    private String email;
    private String passwordHash; // Securely hashed password
    private Set<String> bookmarkedArticleIds;
    private Set<String> interestTopics;

    public User(int id, String name, String email, String passwordHash, Set<String> interestTopics) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.bookmarkedArticleIds = new HashSet<>();
        this.interestTopics = interestTopics;
    }

    public User(int id, String name, String email, Set<String> interestTopics){
        this.id = id;
        this.name = name;
        this.email = email;
        this.interestTopics = interestTopics;
        this.bookmarkedArticleIds = new HashSet<>();
    }

    // Bookmarking logic
    public void bookmarkArticle(String articleId) {
        bookmarkedArticleIds.add(articleId);
    }

    public void removeBookmarkedArticle(String articleId){
        bookmarkedArticleIds.remove(articleId);
    }

    public boolean isArticleBookmarked(String articleId) {
        return bookmarkedArticleIds.contains(articleId);
    }

    public Set<String> getBookmarkedArticleIds() {
        return bookmarkedArticleIds;
    }

    // Interest topics
    public void addInterestTopic(String topic) {
        interestTopics.add(topic.toLowerCase()); // Normalize for comparison
    }

    public void removeInterestTopic(String topic) {
        interestTopics.remove(topic.toLowerCase());
    }

    public Set<String> getInterestTopics() {
        return interestTopics;
    }

    // User identity
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String toString(){
        return "User {" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                ", interests=" + interestTopics +
                '}';
    }
}

