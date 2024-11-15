package com.example.cw1;
import java.util.*;
import java.sql.*;

public class KeywordExtraction {
    final List<String> sports = Arrays.asList("game", "team", "player", "match", "score", "tournament", "league", "coach", "season", "competition", "win", "athlete", "goal", "stadium", "championship", "athletics", "tournament", "champion", "competition", "fitness", "training", "medal", "trophy", "pitch", "scoreboard", "spectator", "matchup" ,"teamwork", "athlete", "sportsmanship", "referee", "season", "defense", "offense", "fitness", "exercise", "practice", "stadium", "uniform", "coach", "teammate");
    final List<String> technology = Arrays.asList("software", "hardware", "computer", "AI", "tech", "robotics", "cybersecurity", "programming", "innovation", "smartphone", "machine learning", "cloud", "data", "network", "blockchain",  "innovation", "gadgets", "devices", "automation", "AI", "IoT", "quantum computing", "virtual reality", "cloud computing", "5G", "blockchain", "cybersecurity", "digital", "software development", "coding", "programming", "big data", "internet", "artificial intelligence", "machine learning", "data science", "analytics", "network", "database", "encryption", "algorithm", "VR", "AR");
    final List<String> politics = Arrays.asList("government", "policy", "election", "vote", "democracy", "president", "minister", "parliament", "senate", "law", "constitution", "political party", "candidate", "campaign", "referendum","policy", "regulation", "election", "democracy", "congress", "senate", "representative", "candidate", "campaign", "ballot", "legislation", "executive", "judiciary", "parliament", "government", "administration", "bill", "law", "rights", "diplomacy", "sanction", "treaty", "constitution", "citizenship", "leader", "ambassador", "debate", "committee", "opposition", "republic");
    final List<String> health = Arrays.asList("disease", "medicine", "doctor", "hospital", "treatment", "vaccine", "healthcare", "pandemic", "nutrition", "mental health", "wellness", "exercise", "infection", "virus", "epidemic","well-being", "disease prevention", "treatment", "clinic", "healthcare system", "medicine", "pharmacy", "surgery", "medical research", "epidemic", "immunization", "pandemic", "public health", "nutrition", "fitness", "exercise", "mental health", "wellness", "hospital", "health insurance", "emergency room", "doctor", "nurse", "patient care", "symptoms", "diagnosis", "therapist", "psychology", "medication", "lifestyle");
    final List<String> business = Arrays.asList("market", "economy", "finance", "investment", "stock", "revenue", "profit", "trade", "company", "entrepreneur", "startup", "shares", "tax", "industry", "business strategy","economics", "startup", "marketplace", "stock market", "corporate", "investment", "fintech", "enterprise", "mergers", "acquisitions", "entrepreneur", "capital", "shares", "branding", "strategy", "business plan", "revenue", "profit", "loss", "dividends", "assets", "liabilities", "commerce", "trade", "globalization", "retail", "customer", "supply chain", "operations", "logistics", "financial", "market research");
    final List<String> weather = Arrays.asList("forecast", "temperature", "climate", "humidity", "precipitation", "rain", "snow", "storm", "wind", "hurricane", "tornado", "drought", "flood", "heatwave", "cold front", "blizzard", "lightning", "thunder", "UV index", "barometer", "dew point", "meteorology", "weather patterns", "atmosphere", "seasonal", "tropical", "air pressure", "jet stream", "el niño", "la niña", "monsoon", "cyclone", "cloud cover");

    public static final String url = "jdbc:mysql://localhost:3306/news";
    public static final String user = "root";
    public static final String password = "Dulina@123";

    private Connection connection;

    private void initializeConnection() throws SQLException, ClassNotFoundException {
        connection = connect();
    }



    private Connection connect() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(url, user, password);
    }

    public Map<String, Double> getTFvalue(String article){
        Map<String, Integer> wordCounts = new HashMap<>();
        String[] words = article.split("\\s+");

        for(String word: words){
            word = word.toLowerCase().replaceAll("[^a-zA-Z]", "");
            wordCounts.put(word, wordCounts.getOrDefault(word,0)+1);
        }

        Map<String,Double> tfValues = new HashMap<>();
        int wordcountTotal = words.length;

        for(Map.Entry<String, Integer> entry : wordCounts.entrySet()){
            tfValues.put(entry.getKey(), (double) entry.getValue() / wordcountTotal);
        }
        return tfValues;


    }
    public String categorizeArticle(Map<String, Double> tfValues) {
        int sportsMatches = countMatches(tfValues.keySet(), sports);
        int techMatches = countMatches(tfValues.keySet(), technology);
        int politicsMatches = countMatches(tfValues.keySet(), politics);
        int healthMatches = countMatches(tfValues.keySet(), health);
        int businessMatches = countMatches(tfValues.keySet(), business);
        int weatherMatches = countMatches(tfValues.keySet(), weather);

        if (sportsMatches > techMatches && sportsMatches > politicsMatches && sportsMatches > healthMatches && sportsMatches > businessMatches && sportsMatches > weatherMatches) {
            return "Sports";
        }
        if (techMatches > sportsMatches && techMatches > politicsMatches && techMatches > healthMatches && techMatches > businessMatches && techMatches > weatherMatches) {
            return "Technology";
        }
        if (politicsMatches > sportsMatches && politicsMatches > techMatches && politicsMatches > healthMatches && politicsMatches > businessMatches && politicsMatches > weatherMatches) {
            return "Politics";
        }
        if (healthMatches > sportsMatches && healthMatches > techMatches && healthMatches > politicsMatches && healthMatches > businessMatches && healthMatches > weatherMatches) {
            return "Health";
        }
        if (businessMatches > sportsMatches && businessMatches > techMatches && businessMatches > politicsMatches && businessMatches > healthMatches && businessMatches > weatherMatches) {
            return "Business";
        }
        if (weatherMatches > sportsMatches && weatherMatches > techMatches && weatherMatches > politicsMatches && weatherMatches > healthMatches && weatherMatches > businessMatches) {
            return "Business";
        }
        return "Uncategorized";
    }

    private int countMatches(Set<String> words, List<String> categoryKeywords) {
        return (int) words.stream().filter(categoryKeywords::contains).count();
    }









}
