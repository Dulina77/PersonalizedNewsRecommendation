package com.example.cw1;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArticleRecommendation {

    private DataBaseHandler dbHandler;

    public ArticleRecommendation() {
        dbHandler = new DataBaseHandler();
    }

    public List<Article> getRecommendation(String userName) {
        List<Article> recommendedArticles;
        recommendedArticles = dbHandler.getRecommendation(userName);
        System.out.println("Recommended Articles: " + recommendedArticles);
        return recommendedArticles;
    }



}
