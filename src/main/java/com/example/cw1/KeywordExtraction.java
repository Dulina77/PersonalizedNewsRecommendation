package com.example.cw1;

import java.io.*;
import java.net.*;
import java.util.*;
import java.sql.*;

public class KeywordExtraction {

    private static final String FASTAPI_URL_KEYWORD_EXTRACTION = "http://127.0.0.1:8000/extract_keywords";

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

    final List<String> entertainment = Arrays.asList(
            "movies", "television", "music", "concerts", "theater", "celebrities", "comedy", "entertainment news",
            "video games", "streaming services", "Netflix", "Hollywood", "blockbusters", "awards shows",
            "film festivals", "pop culture", "cinema", "performances", "amusement parks", "sitcoms",
            "binge-watching", "reality TV", "animation", "trailers", "entertainers", "box office",
            "albums", "music videos", "songs", "actors", "actresses", "producers", "directors",
            "genres", "thrillers", "romantic comedies", "action movies", "live performances",
            "gaming tournaments", "game streaming", "YouTube", "viral videos", "podcasts", "stand-up comedy",
            "dance", "art exhibitions", "cultural events", "independent films", "music festivals",
            "fan communities", "merchandise", "soundtracks", "special effects", "celebrity interviews",
            "social media influencers", "fashion shows", "entertainment platforms", "virtual reality",
            "eSports", "behind-the-scenes", "documentaries", "short films", "interactive media", "dramas"
    );

    final List<String> education = Arrays.asList(
            "learning", "teaching", "school", "college", "university", "curriculum", "online courses",
            "e-learning", "educational technology", "students", "teachers", "education system", "classroom",
            "homework", "assignments", "academic performance", "research", "scholarships", "educational resources",
            "higher education", "vocational training", "distance learning", "MOOCs", "STEM education",
            "textbooks", "lectures", "tutorials", "virtual classrooms", "academic success", "study materials",
            "learning apps", "educational platforms", "examinations", "grades", "academic calendar",
            "lesson plans", "special education", "adult education", "professional development", "certifications",
            "extracurricular activities", "lifelong learning", "language learning", "career counseling",
            "standardized tests", "study habits", "academic integrity", "group projects", "internships",
            "educational psychology", "tutoring", "learning disabilities", "teacher training", "student engagement",
            "learning outcomes", "education policies", "academic research", "interactive learning",
            "knowledge sharing", "school management", "class schedules", "study tips", "open educational resources",
            "global education", "educational games", "learning styles", "campus life", "education funding"
    );


    final List<String> business = Arrays.asList("market", "economy", "finance", "investment", "stock", "revenue", "profit", "trade", "company", "entrepreneur", "startup",
            "shares", "tax", "industry", "business strategy", "economics", "marketplace", "corporate", "fintech", "enterprise", "mergers",
            "acquisitions", "capital", "branding", "business plan", "loss", "dividends", "assets", "liabilities", "commerce", "retail",
            "supply chain", "operations", "logistics", "financial", "market research", "globalization", "economic growth", "business news",
            "investor", "portfolio", "stock market", "real estate", "e-commerce", "consumer goods", "financial planning", "business development",
            "economic policy", "economic forecast", "entrepreneurship", "small business", "venture capital", "startup ecosystem", "brand loyalty",
            "consumer behavior", "market trends", "sustainability", "corporate governance");


    public String categorizeArticle(String content) throws IOException {
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
        int entertainmentMatches = countMatches(keywords, entertainment );
        int educationMatches = countMatches(keywords, education );
        int businessMatches = countMatches(keywords, business);



        if (businessMatches >= sportsMatches && businessMatches >= techMatches && businessMatches >= entertainmentMatches && businessMatches >= educationMatches) {
            return "Business";
        }
        if (educationMatches >= sportsMatches && educationMatches >= techMatches && educationMatches >= entertainmentMatches && educationMatches >= businessMatches) {
            return "Education";
        }
        if (entertainmentMatches >= sportsMatches && entertainmentMatches >= techMatches && entertainmentMatches >= educationMatches && entertainmentMatches >= businessMatches ) {
            return "Entertainment";
        }
        if (sportsMatches >= techMatches && sportsMatches >= entertainmentMatches && sportsMatches >= educationMatches && sportsMatches >= businessMatches ) {
            return "Sports";
        }
        if (techMatches >= sportsMatches && techMatches >= entertainmentMatches && techMatches >= educationMatches && techMatches >= businessMatches) {
            return "Technology";
        }


        return "Uncategorized";
    }

    private int countMatches(List<String> words, List<String> categoryKeywords) {
        return (int) words.stream().filter(categoryKeywords::contains).count();
    }
}
