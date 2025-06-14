package org.example;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Article {
    private String id;
    private String title;
    private String content;
    private List<String> topics;
    private int popularity;
    private String author;
    private String url;
    private Timestamp publishedAt;
    private String description;
    private String imageUrl;
    private String sourceName;
    private int sourcePriority;
    private String sourceUrl;
    private String sourceIcon;
    private String language;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static final DateTimeFormatter[] FORMATTERS = {
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
            DateTimeFormatter.ISO_DATE_TIME,
            DateTimeFormatter.ISO_OFFSET_DATE_TIME,
            DateTimeFormatter.RFC_1123_DATE_TIME
    };


    public Article(String id, String title, String content, List<String> topics, int popularity) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.topics = topics;
        this.popularity = popularity;
    }

    public Article(String id, String title){
        this.id = id;
        this.title = title;
        this.topics = new ArrayList<>();
    }

    public Article() {
        this.topics = new ArrayList<>();
    }

    //Getters

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public List<String> getTopics() {
        return topics;
    }

    public int getPopularity() {
        return popularity;
    }

    public String getAuthor() {
        return author;
    }

    public String getUrl() {
        return url;
    }

    public Timestamp getPublishedAt() {
        return publishedAt;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getSourceName() {
        return sourceName;
    }

    public int getSourcePriority() {
        return sourcePriority;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public String getSourceIcon() {
        return sourceIcon;
    }

    public String getLanguage() {
        return language;
    }

    //Setters

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setPublishedAt(String publishedAt) {
        if (publishedAt == null || publishedAt.trim().isEmpty()) {
            this.publishedAt = new Timestamp(System.currentTimeMillis());
            return;
        }

        for (DateTimeFormatter formatter : FORMATTERS) {
            try {
                LocalDateTime dateTime = LocalDateTime.parse(publishedAt, formatter);
                this.publishedAt = Timestamp.valueOf(dateTime);
                return;
            } catch (Exception ignored) {
                Logger.addLog("Failed to parse datetime: " + publishedAt + " with formatter: " + formatter.toString() + ". Trying next formatter...", "Article class, setPublishedAt() method, line 100.");
            }
        }
        this.publishedAt = new Timestamp(System.currentTimeMillis());
    }


    public void setDescription(String description) {
        this.description = description;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public void setSourcePriority(int sourcePriority) {
        this.sourcePriority = sourcePriority;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public void setSourceIcon(String sourceIcon) {
        this.sourceIcon = sourceIcon;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public String toString() {
        return "[" + id + "] " + title + " (Topics: " + topics + ", Popularity: " + popularity + ")";
    }
}

