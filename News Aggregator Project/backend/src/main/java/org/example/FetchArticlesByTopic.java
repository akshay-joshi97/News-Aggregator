package org.example;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FetchArticlesByTopic {
    public static void getArticlesByTopic(String topic){
        System.out.println("Hello World!");
        String apiKey = "pub_8806661796a6d04414da1b3619474a6be6f83";
        String endpoint = "https://newsdata.io/api/1/news?apikey=" + apiKey + "&q=" + topic + "&language=en";

        try {
            URL url = new URL(endpoint);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream())
            );
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = in.readLine()) != null) {
                response.append(line);
            }

            in.close();

            JSONObject json = new JSONObject(response.toString());
            JSONArray articles = json.getJSONArray("results");

            List<Article> articleList = new ArrayList<>();

            for (int i = 0; i < articles.length(); i++) {
                JSONObject request = articles.getJSONObject(i);
                Article article = new Article();

                article.setId(request.isNull("article_id") ? "" : request.getString("article_id"));
                article.setTitle(request.isNull("title") ? "" : request.getString("title"));
                article.setContent(request.isNull("content") ? "" : request.getString("content"));

                if(!request.isNull("category")) {
                    JSONArray categoryArray = request.getJSONArray("category");
                    List<Object> categories = categoryArray.toList();

                    if (!categories.isEmpty()){
                        List<String> topics = new ArrayList<>();
                        for(Object category : categories){
                            topics.add((String) category);
                        }
                        article.setTopics(topics);
                    }
                }

                if (!request.isNull("creator")) {
                    JSONArray creatorArray = request.getJSONArray("creator");
                    List<Object> creators = creatorArray.toList();
                    if (!creators.isEmpty()) {
                        article.setAuthor((String) creators.get(0));
                    }
                }

                article.setUrl(request.isNull("link") ? "" : request.getString("link"));
                article.setPublishedAt(request.isNull("pubDate") ? "" : request.getString("pubDate"));
                article.setDescription(request.isNull("description") ? "" : request.getString("description"));
                article.setImageUrl(request.isNull("image_url") ? "" : request.getString("image_url"));
                article.setSourceName(request.isNull("source_name") ? "" : request.getString("source_name"));
                article.setSourcePriority(request.isNull("source_priority") ? 0 : request.getInt("source_priority"));
                article.setSourceUrl(request.isNull("source_url") ? "" : request.getString("source_url"));
                article.setSourceIcon(request.isNull("source_icon") ? "" : request.getString("source_icon"));
                article.setLanguage(request.isNull("language") ? "" : request.getString("language"));

                articleList.add(article);

                // Save article to DB
                ArticleSaver.saveArticleToDb(article);
            }

            System.out.println("Finished processing " + articleList.size() + " articles.");

        } catch (Exception e) {
            Logger.addLog("Failed to fetch articles for topic: " + topic, "FetchArticlesByTopic");
            e.printStackTrace();
        }
    }
}
