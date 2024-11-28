package com.example.cw1;

import java.util.ArrayList;

public class UserProfile {
    private ArrayList<Article> readingHistory = new ArrayList<>();
    private ArrayList<Article> likeHistory = new ArrayList<>();;
    private ArrayList<Article> dislikeHistory =new ArrayList<>();


    public void addToHistory(Article article) {
        readingHistory.add(article);
    }

    public void addToLiked(Article article) {
        likeHistory.add(article);
    }

    public void addToDisliked(Article article) {
        dislikeHistory.add(article);
    }

}
