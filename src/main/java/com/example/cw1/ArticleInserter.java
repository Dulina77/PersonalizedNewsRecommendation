package com.example.cw1;


import java.io.*;

import java.sql.*;


public class ArticleInserter {

    public static void main(String[] args) throws IOException, InterruptedException {

        CSVconverter csvconverter = new CSVconverter();
        String[][] articles = csvconverter.ArticleCollector();

        for (String[] article : articles) {
            String title = article[0];
            String content = article[1];
            String description = article[2];

            KeywordExtraction keywordExtraction = new KeywordExtraction();
            String category = keywordExtraction.categorizeArticle(description);

            try {
                DataBaseHandler.insertArticle(title, content, category);
                System.out.println("Article inserted successfully: " + title);
            } catch (SQLException e) {
                System.out.println("Error inserting article: " + e.getMessage());
            }
        }


    }





}
