package org.example;

import static spark.Spark.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Server is running!!!");
        port(4567);

        // Enable CORS
        options("/*", (request, response) -> {
            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }
            System.out.println("Server is running!!!");
            return "OK";
        });

        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");  // You can restrict this to your domain
            response.header("Access-Control-Allow-Methods", "GET,POST,OPTIONS");
            response.header("Access-Control-Allow-Headers", "Content-Type,Authorization");
        });

        // Your endpoint
        get("/hello", (req, res) -> {
            res.type("application/json");
            return "{\"message\":\"Hello from Spark Java!\"}";
        });
    }
}
