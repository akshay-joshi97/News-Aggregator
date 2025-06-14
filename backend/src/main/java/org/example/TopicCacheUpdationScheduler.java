package org.example;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TopicCacheUpdationScheduler {
    public static void main(String[] args) {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        List<String> topicList = Arrays.asList(
                "technology", "top", "sports", "business", "science",
                "entertainment", "health", "world", "politics", "crime",
                "environment", "tourism", "education"
        );

        // Schedule task: initial delay of 30 seconds, then run once every 1 day
        executor.scheduleAtFixedRate(
                () -> TopicCacheUpdationBatch.topicCacheUpdationProcedure(topicList, 10),
                10,
                86400,
                TimeUnit.SECONDS
        );
    }
}
