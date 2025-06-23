package org.example;

import com.google.gson.*;
import spark.Spark;
import com.google.gson.Gson;

import java.sql.Array;
import java.sql.Connection;
import java.util.*;

public class SparkApp {

    public static void main(String[] args) {

        Gson gson = new Gson();

        // Set port from environment variable or default to 4567
        String port = System.getenv("PORT");
        if (port != null) {
            Spark.port(Integer.parseInt(port));
        } else {
            Spark.port(4567);
        }

        // Enable CORS for frontend
        Spark.before((request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Allow-Methods", "GET,PUT,POST,DELETE,OPTIONS");
            response.header("Access-Control-Allow-Headers", "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin");
        });

        // Root endpoint to verify server is running
        Spark.get("/", (request, response) -> {
            response.type("application/json");
            Map<String, Object> result = new HashMap<>();
            result.put("status", "Server is running");
            result.put("message", "Welcome to News Aggregator API");
            return gson.toJson(result);
        });

        // Hello endpoint
        Spark.get("/hello", (request, response) -> {
            response.type("application/json");
            return "{\"message\":\"Hello from Spark Java!\"}";
        });

        // Handle preflight requests
        Spark.options("/*", (request, response) -> {
            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }
            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }
            return "OK";
        });

        // Login endpoint
        Spark.post("/login", (request, response) -> {
            response.type("application/json");

            try(Connection conn = DatabaseManager.getConnection()) {
                // Parse JSON request
                Map<String, String> requestData = gson.fromJson(request.body(), Map.class);
                String email = requestData.get("email");
                String password = requestData.get("password");

                JdbcUserRepository userRepository = new JdbcUserRepository(conn);
                int userId = userRepository.loginUser(email, password);
                Map<String, Object> result = new HashMap<>();
                if(userId != 0){
                    result.put("success", true);
                    result.put("message", "Login successful!");
                    result.put("status", 200);
                }else{
                    result.put("success", false);
                    result.put("message", "Login unsuccessful.");
                    result.put("status", 500);
                }
                result.put("userId", userId);
                return gson.toJson(result);

            } catch (Exception e) {
                Logger.addLog("User login unsuccessful for : " + request.body(), "SparkApp, /login");
                response.status(500);
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", "Login failed: " + e.getMessage());
                return gson.toJson(error);
            }
        });

        // Register endpoint
        Spark.post("/register", (request, response) -> {
            response.type("application/json");

            try(Connection conn = DatabaseManager.getConnection()) {
                // Parse JSON request
                Map<String, String> requestData = gson.fromJson(request.body(), Map.class);
                String name = requestData.get("name");
                String email = requestData.get("email");
                String password = requestData.get("password");

                JdbcUserRepository userRepository = new JdbcUserRepository(conn);
                int userId = userRepository.createUser(name, email, password);
                Map<String, Object> result = new HashMap<>();
                if (userId != 0){
                    result.put("success", true);
                    result.put("message", "Registration successful!");
                    result.put("status", 201);
                }else{
                    result.put("success", false);
                    result.put("message", "Registration unsuccessful.");
                }
                result.put("userId", userId);
                return gson.toJson(result);

            } catch (Exception e) {
                Logger.addLog("User registration unsuccessful for: "+ request.body(), "SparkApp, /register");
                response.status(500);
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", "Registration failed: " + e.getMessage());
                return gson.toJson(error);
            }
        });

        // Update Interests endpoint
        Spark.post("/updateinterests", (request, response) -> {
            response.type("application/json");

            try(Connection conn = DatabaseManager.getConnection()) {
                // Parse JSON request safely
                JsonObject requestJson = JsonParser.parseString(request.body()).getAsJsonObject();
                int userId = requestJson.get("userId").getAsInt();

                JsonArray interestsJsonArray = requestJson.getAsJsonArray("interests");
                List<String> interestsList = new ArrayList<>();
                for (JsonElement element : interestsJsonArray) {
                    interestsList.add(element.getAsString());
                }

                // Update DB
                JdbcUserRepository userRepository = new JdbcUserRepository(conn);
                boolean isSuccess = userRepository.updateInterests(userId, interestsList);

                Map<String, Object> result = new HashMap<>();
                result.put("userId", userId);

                if (isSuccess) {
                    result.put("success", true);
                    result.put("message", "Interests updated successfully.");
                    result.put("status", 200);
                } else {
                    result.put("success", false);
                    result.put("message", "Failed to update interests.");
                    result.put("status", 400);
                }

                return gson.toJson(result);

            } catch (Exception e) {
                Logger.addLog("Interests update failed for: " + request.body(), "SparkApp, /updateinterests");
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", "Interests update failed: " + e.getMessage());
                error.put("status", 500);
                return gson.toJson(error);
            }
        });

//        // Exposing endpoint for triggering batch jobs from cron-job
//        Spark.get("/triggerbatch", (request, response) -> {
//            System.out.println("spark endpoint trigger batch hit >>>");
//            response.type("application/json");
//
//            try {
//                // Run Article Deletion Batch
//                //DeleteOldArticlesScheduler.deleteOldArticles(2);
//
//                // Run Article Fetch Batch
//                System.out.println("before runTopicWise batch>>>");
//                NewsFetchBatch.runBatchTopicWise(AppConstants.TOPIC_LIST);
//                System.out.println("after runTopicWise batch>>>");
//                // Run Topic Cache Batch
//                //TopicCacheUpdationBatch.topicCacheUpdationProcedure(AppConstants.TOPIC_LIST, 10);
//
//                // Success response
//                Map<String, Object> success = new HashMap<>();
//                success.put("success", true);
//                success.put("message", "Batch jobs triggered successfully");
//                success.put("status", 200);
//                return gson.toJson(success);
//
//            } catch (Exception e) {
//                Logger.addLog("Batch trigger failed: " + request.body(), "SparkApp, /triggerbatch");
//
//                Map<String, Object> error = new HashMap<>();
//                error.put("success", false);
//                error.put("message", "Batch trigger failed: " + e.getMessage());
//                error.put("status", 500);
//                return gson.toJson(error);
//            }
//        });

        System.out.println("Server started on https://news-aggregator-0w52.onrender.com/");
    }
}