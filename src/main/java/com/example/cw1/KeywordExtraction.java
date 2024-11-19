package com.example.cw1;

import java.io.*;
import java.net.*;
import java.util.*;
import java.sql.*;

public class KeywordExtraction {

    private static final String FASTAPI_URL = "http://127.0.0.1:8000/extract_keywords";

    // Existing category keyword lists
    final List<String> sports = Arrays.asList("game", "team", "player", "match", "score", "tournament", "league", "coach", "season", "competition",
            "win", "athlete", "goal", "stadium", "championship", "athletics", "tournament", "champion", "competition",
            "fitness", "training", "medal", "trophy", "pitch", "scoreboard", "spectator", "matchup", "teamwork", "sportsmanship",
            "referee", "defense", "offense", "exercise", "practice", "uniform", "teammate", "penalty", "dribble", "dunk", "assist",
            "rebound", "basket", "soccer", "baseball", "football", "basketball", "cricket", "hockey", "rugby", "boxing", "wrestling",
            "mma", "cycling", "golf", "tennis", "swimming", "track and field", "marathon", "world cup", "super bowl", "olympics",
            "sports news", "fitness level", "injury", "matchday", "goalkeeper", "striker", "midfielder", "defender", "referee", "coach");
    final List<String> technology = Arrays.asList("software", "hardware", "computer", "AI", "tech", "robotics", "cybersecurity", "programming", "innovation",
            "smartphone", "machine learning", "cloud", "data", "network", "blockchain", "gadgets", "devices", "automation", "IoT",
            "quantum computing", "virtual reality", "cloud computing", "5G", "cybersecurity", "digital", "software development",
            "coding", "programming", "big data", "internet", "artificial intelligence", "data science", "analytics", "network",
            "database", "encryption", "algorithm", "VR", "AR", "wearables", "smart home", "3D printing", "autonomous vehicles",
            "machine vision", "voice recognition", "edge computing", "big data analytics", "cloud infrastructure", "data center",
            "distributed computing", "open-source", "developer", "startup", "fintech", "smart devices", "network security", "digital transformation");
    final List<String> politics = Arrays.asList("government", "policy", "election", "vote", "democracy", "president", "minister", "parliament", "senate", "law",
            "constitution", "political party", "candidate", "campaign", "referendum", "regulation", "congress", "representative",
            "ballot", "legislation", "executive", "judiciary", "administration", "bill", "rights", "diplomacy", "sanction", "treaty",
            "citizenship", "leader", "ambassador", "debate", "committee", "opposition", "republic", "state", "political reform",
            "government transparency", "foreign policy", "social policy", "public opinion", "lobbying", "political activism", "federal",
            "local government", "political unrest", "electoral system", "civil rights", "legislature", "voter turnout", "coalition",
            "constitutional amendment", "human rights", "social justice", "advocacy", "political protest");
    final List<String> health = Arrays.asList("disease", "medicine", "doctor", "hospital", "treatment", "vaccine", "healthcare", "pandemic", "nutrition", "mental health",
            "wellness", "exercise", "infection", "virus", "epidemic", "disease prevention", "clinic", "healthcare system", "pharmacy",
            "surgery", "medical research", "immunization", "public health", "fitness", "mental health", "wellness", "health insurance",
            "emergency room", "nurse", "patient care", "diagnosis", "therapist", "psychology", "medication", "lifestyle", "treatment plan",
            "preventive care", "hospitalization", "symptoms", "health risks", "nutritionist", "obesity", "chronic disease", "diabetes",
            "heart disease", "cancer", "mental illness", "stress management", "self-care", "rehabilitation", "treatment options",
            "medical technology", "health screenings", "mental wellness", "fitness trackers", "exercise routine", "dietary supplements");
    final List<String> business = Arrays.asList("market", "economy", "finance", "investment", "stock", "revenue", "profit", "trade", "company", "entrepreneur", "startup",
            "shares", "tax", "industry", "business strategy", "economics", "marketplace", "corporate", "fintech", "enterprise", "mergers",
            "acquisitions", "capital", "branding", "business plan", "loss", "dividends", "assets", "liabilities", "commerce", "retail",
            "supply chain", "operations", "logistics", "financial", "market research", "globalization", "economic growth", "business news",
            "investor", "portfolio", "stock market", "real estate", "e-commerce", "consumer goods", "financial planning", "business development",
            "economic policy", "economic forecast", "entrepreneurship", "small business", "venture capital", "startup ecosystem", "brand loyalty",
            "consumer behavior", "market trends", "sustainability", "corporate governance");
    final List<String> weather = Arrays.asList("forecast", "temperature", "climate", "humidity", "precipitation", "rain", "snow", "storm", "wind", "hurricane", "tornado",
            "drought", "flood", "heatwave", "cold front", "blizzard", "lightning", "thunder", "UV index", "barometer", "dew point",
            "meteorology", "weather patterns", "atmosphere", "seasonal", "tropical", "air pressure", "jet stream", "el niño", "la niña",
            "monsoon", "cyclone", "cloud cover", "cloudburst", "weather system", "rainfall", "temperature rise", "weather warning",
            "natural disaster", "climate change", "weather satellite", "weather station", "forecast models", "storm surge", "weather alert",
            "frost", "wind chill", "weather prediction", "high pressure", "low pressure", "severe weather", "climate report", "global warming");

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
