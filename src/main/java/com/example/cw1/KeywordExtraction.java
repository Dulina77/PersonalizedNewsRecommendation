package com.example.cw1;
import java.util.*;
import java.sql.*;

public class KeywordExtraction {
    final List<String> sports = Arrays.asList("game", "team", "player", "match", "score", "tournament", "league", "coach", "season", "competition", "win", "athlete", "goal", "stadium", "championship");
    final List<String> technology = Arrays.asList("software", "hardware", "computer", "AI", "tech", "robotics", "cybersecurity", "programming", "innovation", "smartphone", "machine learning", "cloud", "data", "network", "blockchain");
    final List<String> politics = Arrays.asList("government", "policy", "election", "vote", "democracy", "president", "minister", "parliament", "senate", "law", "constitution", "political party", "candidate", "campaign", "referendum");
    final List<String> health = Arrays.asList("disease", "medicine", "doctor", "hospital", "treatment", "vaccine", "healthcare", "pandemic", "nutrition", "mental health", "wellness", "exercise", "infection", "virus", "epidemic");
    final List<String> business = Arrays.asList("market", "economy", "finance", "investment", "stock", "revenue", "profit", "trade", "company", "entrepreneur", "startup", "shares", "tax", "industry", "business strategy");

    public static final String url = "jdbc:mysql://localhost:3306/news";
    public static final String user = "root";
    public static final String password = "Dulina@123";

    private Connection connection;

    private void initializeConnection() throws SQLException, ClassNotFoundException {
        connection = connect();
    }


    public ArrayList<String> articleFetching() throws SQLException {
        ArrayList<String> articles = new ArrayList<>();
        String query = "SELECT content from article";
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery(query);

        while(resultSet.next()){
            articles.add(resultSet.getString("content"));
        }
        return articles;

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

        if (sportsMatches > techMatches && sportsMatches > politicsMatches && sportsMatches > healthMatches && sportsMatches > businessMatches) {
            return "Sports";
        }
        if (techMatches > sportsMatches && techMatches > politicsMatches && techMatches > healthMatches && techMatches > businessMatches) {
            return "Technology";
        }
        if (politicsMatches > sportsMatches && politicsMatches > techMatches && politicsMatches > healthMatches && politicsMatches > businessMatches) {
            return "Politics";
        }
        if (healthMatches > sportsMatches && healthMatches > techMatches && healthMatches > politicsMatches && healthMatches > businessMatches) {
            return "Health";
        }
        if (businessMatches > sportsMatches && businessMatches > techMatches && businessMatches > politicsMatches && businessMatches > healthMatches) {
            return "Business";
        }
        return "Uncategorized";
    }

    private int countMatches(Set<String> words, List<String> categoryKeywords) {
        return (int) words.stream().filter(categoryKeywords::contains).count();
    }









}
