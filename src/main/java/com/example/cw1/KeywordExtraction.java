package com.example.cw1;

import java.io.*;
import java.net.*;
import java.util.*;
import java.sql.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.stream.Collectors;

public class KeywordExtraction {

    private static final String FASTAPI_URL_KEYWORD_EXTRACTION = "http://127.0.0.1:8000/extract_keywords";


    final List<String> sports = Arrays.asList("match", "india", "test", "tournament", "captain", "odi", "hyderabad", "matches", "spinner", "australia","tennis","players");
    final List<String> technology = Arrays.asList("google", "microsoft", "ai", "apple", "ios", "samsung","hardware", "amd", "capabilities", "technology",  "products", "launched");
    final List<String> education = Arrays.asList("exam", "enrollment", "candidates", "courses", "pg", "website", "teachers", "students", "research","university", "school","scholarships","study","learning");
    final List<String> business = Arrays.asList("cent", "equity", "shares", "profit", "stock", "rs", "crore", "merger", "sector", "investors","pay","bank","cdgd","xzvsv","cxczz","dsfdssg","sfdfas","dsfdsga","sffsgag");


    public String categorizeArticle(String content) throws IOException, InterruptedException {
        List<String> keywords = getKeywordsFromAPI(content);
        return determineCategory(keywords);


    }


    private List<String> getKeywordsFromAPI(String content) throws IOException {
        String jsonInput = "{\"text\": \"" + content.replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r") + "\"}";
        URL url = new URL(FASTAPI_URL_KEYWORD_EXTRACTION);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInput.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                return parseKeywords(response.toString());
            }
        } else {
            throw new IOException("API request failed with response code: " + responseCode);
        }
    }


    private List<String> parseKeywords(String jsonResponse) {
        List<String> keywords = new ArrayList<>();
        String[] parts = jsonResponse.split("\"keywords\":\\[")[1].split("]")[0].split(",");
        for (String keyword : parts) {
            keywords.add(keyword.replace("\"", "").trim());
        }
        return keywords;
    }


    private String determineCategory(List<String> keywords) {
        int sportsMatches = countMatches(keywords, sports);
        int techMatches = countMatches(keywords, technology);
        int educationMatches = countMatches(keywords, education);
        int businessMatches = countMatches(keywords, business);


        Map<String, Integer> categoryScores = new HashMap<>();
        categoryScores.put("Business", businessMatches);
        categoryScores.put("Sports", sportsMatches);
        categoryScores.put("Technology", techMatches);
        categoryScores.put("Education", educationMatches);

        return categoryScores.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .get()
                .getKey();
    }


    private int countMatches(List<String> words, List<String> categoryKeywords) {
        return (int) words.stream().filter(categoryKeywords::contains).count();
    }
}
