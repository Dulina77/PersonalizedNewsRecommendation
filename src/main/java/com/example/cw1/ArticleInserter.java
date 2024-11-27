package com.example.cw1;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.util.List;

public class ArticleInserter {
    private static final String NEWS_API_URL = "https://newsapi.org/v2/top-headlines";
    private static final String NEWS_API_KEY = "3d3c3a25dd3743769ac9069407290e8a";  // Replace with your actual NewsAPI key



    public static void main(String[] args) throws IOException {

        CSVconverter csvconverter = new CSVconverter();
        String[][] articles = csvconverter.ArticleCollector();

        String url = "jdbc:mysql://localhost:3306/news";
        String user = "root";
        String password = "Dulina@123";


        String insertSQL = "INSERT INTO article (title, content, category) VALUES (?, ?, ?)" + "ON DUPLICATE KEY UPDATE content = ?, category = ?";


        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            try (PreparedStatement statement = connection.prepareStatement(insertSQL)) {
                for (String[] article : articles) {
                    String title = article[0];
                    String content = article[1];

                    KeywordExtraction keywordExtraction = new KeywordExtraction();
                    String category = keywordExtraction.categorizeArticle(article[1]);


                    statement.setString(1, title);
                    statement.setString(2, content);
                    statement.setString(3, category);
                    statement.setString(4, content);
                    statement.setString(5, category);

                    statement.executeUpdate();
                }

                System.out.println("Articles inserted successfully.");
            } catch (SQLException | IOException e) {
                System.out.println("Error while inserting data: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
    }





}
