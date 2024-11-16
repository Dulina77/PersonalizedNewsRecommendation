package com.example.cw1;

import java.io.*;
import java.net.*;
import java.util.*;
import java.sql.*;

public class KeywordExtraction {

    private static final String FASTAPI_URL = "http://127.0.0.1:8000/extract_keywords";

    // Existing category keyword lists
    final List<String> sports = Arrays.asList("game", "team", "player", "match", "score", "tournament", "league", "coach", "season", "competition", "win", "athlete", "goal", "stadium", "championship", "athletics", "tournament", "champion", "competition", "fitness", "training", "medal", "trophy", "pitch", "scoreboard", "spectator", "matchup" ,"teamwork", "athlete", "sportsmanship", "referee", "season", "defense", "offense", "fitness", "exercise", "practice", "stadium", "uniform", "coach", "teammate");
    final List<String> technology = Arrays.asList("software", "hardware", "computer", "AI", "tech", "robotics", "cybersecurity", "programming", "innovation", "smartphone", "machine learning", "cloud", "data", "network", "blockchain",  "innovation", "gadgets", "devices", "automation", "AI", "IoT", "quantum computing", "virtual reality", "cloud computing", "5G", "blockchain", "cybersecurity", "digital", "software development", "coding", "programming", "big data", "internet", "artificial intelligence", "machine learning", "data science", "analytics", "network", "database", "encryption", "algorithm", "VR", "AR");
    final List<String> politics = Arrays.asList("government", "policy", "election", "vote", "democracy", "president", "minister", "parliament", "senate", "law", "constitution", "political party", "candidate", "campaign", "referendum","policy", "regulation", "election", "democracy", "congress", "senate", "representative", "candidate", "campaign", "ballot", "legislation", "executive", "judiciary", "parliament", "government", "administration", "bill", "law", "rights", "diplomacy", "sanction", "treaty", "constitution", "citizenship", "leader", "ambassador", "debate", "committee", "opposition", "republic");
    final List<String> health = Arrays.asList("disease", "medicine", "doctor", "hospital", "treatment", "vaccine", "healthcare", "pandemic", "nutrition", "mental health", "wellness", "exercise", "infection", "virus", "epidemic","well-being", "disease prevention", "treatment", "clinic", "healthcare system", "medicine", "pharmacy", "surgery", "medical research", "epidemic", "immunization", "pandemic", "public health", "nutrition", "fitness", "exercise", "mental health", "wellness", "hospital", "health insurance", "emergency room", "doctor", "nurse", "patient care", "symptoms", "diagnosis", "therapist", "psychology", "medication", "lifestyle");
    final List<String> business = Arrays.asList("market", "economy", "finance", "investment", "stock", "revenue", "profit", "trade", "company", "entrepreneur", "startup", "shares", "tax", "industry", "business strategy","economics", "startup", "marketplace", "stock market", "corporate", "investment", "fintech", "enterprise", "mergers", "acquisitions", "entrepreneur", "capital", "shares", "branding", "strategy", "business plan", "revenue", "profit", "loss", "dividends", "assets", "liabilities", "commerce", "trade", "globalization", "retail", "customer", "supply chain", "operations", "logistics", "financial", "market research");
    final List<String> weather = Arrays.asList("forecast", "temperature", "climate", "humidity", "precipitation", "rain", "snow", "storm", "wind", "hurricane", "tornado", "drought", "flood", "heatwave", "cold front", "blizzard", "lightning", "thunder", "UV index", "barometer", "dew point", "meteorology", "weather patterns", "atmosphere", "seasonal", "tropical", "air pressure", "jet stream", "el niño", "la niña", "monsoon", "cyclone", "cloud cover");

    public String categorizeArticle(String content) throws IOException {
        List<String> keywords = getKeywordsFromAPI(content);
        return determineCategory(keywords);
    }

    private List<String> getKeywordsFromAPI(String content) throws IOException {
        String jsonInput = "{\"text\": \"" + content.replace("\"", "\\\"") + "\"}";

        URL url = new URL(FASTAPI_URL);
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
        int politicsMatches = countMatches(keywords, politics);
        int healthMatches = countMatches(keywords, health);
        int businessMatches = countMatches(keywords, business);
        int weatherMatches = countMatches(keywords, weather);

        if (sportsMatches >= techMatches && sportsMatches >= politicsMatches && sportsMatches >= healthMatches && sportsMatches >= businessMatches && sportsMatches >= weatherMatches) {
            return "Sports";
        }
        if (techMatches >= sportsMatches && techMatches >= politicsMatches && techMatches >= healthMatches && techMatches >= businessMatches && techMatches >= weatherMatches) {
            return "Technology";
        }
        if (politicsMatches >= sportsMatches && politicsMatches >= techMatches && politicsMatches >= healthMatches && politicsMatches >= businessMatches && politicsMatches >= weatherMatches) {
            return "Politics";
        }
        if (healthMatches >= sportsMatches && healthMatches >= techMatches && healthMatches >= politicsMatches && healthMatches >= businessMatches && healthMatches >= weatherMatches) {
            return "Health";
        }
        if (businessMatches >= sportsMatches && businessMatches >= techMatches && businessMatches >= politicsMatches && businessMatches >= healthMatches && businessMatches >= weatherMatches) {
            return "Business";
        }
        if (weatherMatches >= sportsMatches && weatherMatches >= techMatches && weatherMatches >= politicsMatches && weatherMatches >= healthMatches && weatherMatches >= businessMatches) {
            return "Weather";
        }
        return "Uncategorized";
    }

    private int countMatches(List<String> words, List<String> categoryKeywords) {
        return (int) words.stream().filter(categoryKeywords::contains).count();
    }
}
