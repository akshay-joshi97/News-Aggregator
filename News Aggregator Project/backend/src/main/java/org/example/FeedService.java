package org.example;

import java.util.*;

public class FeedService {

    // Returns personalized feed based on user's topic interests
    public List<Article> getPersonalizedFeed(User user, List<Article> articles) {
        List<Article> result = new ArrayList<>();

        Set<String> userTopics = user.getInterestTopics();

        for (Article article : articles) {
            for(String topic : article.getTopics()){
                if (userTopics.contains(topic.toLowerCase())) {
                    result.add(article);
                    break;
                }
            }
        }

        // Sort by popularity descending
        result.sort((a, b) -> Integer.compare(b.getPopularity(), a.getPopularity()));

        return result;
    }

    //Returns topic cache for a particular user, based on their interests
    /*public static List<Article> getInterestedTopicFeed(String userId){
        String userSql = 'SELECT FROM User (id, name, email, )'
    }*/

    // Prints feed with a star (*) next to bookmarked articles
    public void printFeedWithBookmarks(User user, List<Article> feed) {
        for (Article article : feed) {
            boolean isBookmarked = user.isArticleBookmarked(article.getId());
            String marker = isBookmarked ? "*" : " ";
            System.out.println(marker + " " + article.getTitle());
        }
    }
}

