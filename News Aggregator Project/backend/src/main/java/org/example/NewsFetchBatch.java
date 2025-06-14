package org.example;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NewsFetchBatch {
    private static final String API_KEY = "pub_8806661796a6d04414da1b3619474a6be6f83";
    private static final String BASE_URL = "https://newsdata.io/api/1/news";

    public static void runBatchTopicWise(List<String> topicList) {
        for (String topic : topicList) {
            try {
                String endpoint = BASE_URL + "?apiKey=" + API_KEY + "&q=" + topic + "&language=en";
                System.out.println("URL: " + endpoint);

                URL url = new URL(endpoint);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                in.close();

                JSONObject json = new JSONObject(response.toString());
                JSONArray articles = json.getJSONArray("results");

                for (int i = 0; i < articles.length(); i++) {
                    JSONObject request = articles.getJSONObject(i);
                    Article article = mapToArticle(request);
                    if (article != null) {
                        ArticleSaver.saveArticleToDb(article);
                    }
                }

            } catch (Exception e) {
                Logger.addLog("Failed to fetch articles for topic: " + topic, "NewsFetchBatch");
                e.printStackTrace();
            }
        }
    }

    public static Article mapToArticle(JSONObject request) {
        try {
            Article article = new Article();
            article.setId(request.optString("article_id"));

            if (!request.isNull("creator")) {
                JSONArray creatorArray = request.getJSONArray("creator");
                if (creatorArray.length() > 0) {
                    article.setAuthor(creatorArray.optString(0, ""));
                }
            }

            article.setTitle(request.optString("title"));
            article.setContent(request.optString("content"));
            article.setUrl(request.optString("link"));
            article.setLanguage(request.optString("language"));
            article.setPublishedAt(request.optString("pubDate"));
            article.setDescription(request.optString("description"));
            article.setImageUrl(request.optString("image_url"));
            article.setSourceName(request.optString("source_id"));
            article.setPopularity(request.optInt("popularity", 0));
            article.setSourcePriority(request.optInt("source_priority", 0));
            article.setSourceUrl(request.optString("source_url"));
            article.setSourceIcon(request.optString("source_icon"));

            if (!request.isNull("category")) {
                JSONArray categoryArray = request.getJSONArray("category");
                List<String> topics = new ArrayList<>();
                for (int i = 0; i < categoryArray.length(); i++) {
                    topics.add(categoryArray.getString(i));
                }
                article.setTopics(topics);
            }

            return article;
        } catch (Exception e) {
            Logger.addLog("Failed to map article to Article object: " + request.toString(), "NewsFetchBatch");
            e.printStackTrace();
            return null;
        }
    }
}
