package com.example.cw1;

import java.util.ArrayList;
import java.util.List;

public class ArticleHandler {

    static String articleSelectionMain(String clickedTitle){
        String articleContent = DataBaseHandler.articleContentFetcher(clickedTitle);
        return articleContent;
    }

    static String articleSelectionRecommendation(String clickedTitle){
        String articleContent = DataBaseHandler.articleContentFetcher(clickedTitle);
        return articleContent;
    }

    public static List<String> recommendationTitles(User user){
        List<String> recommendedTitles = new ArrayList<>();
        ArticleRecommendation articleRecommendation =  new ArticleRecommendation();

        List<Article> recommendedArticles = articleRecommendation.getRecommendation(user.getUserName());

        for (int i = 0; i < recommendedArticles.size(); i++) {
            recommendedTitles.add(recommendedArticles.get(i).getTitle());
        }

        return recommendedTitles;
    }

    public static ArrayList<String> articleTitleFetcher(){
        return DataBaseHandler.articleTitleFetcher();
    }
}
