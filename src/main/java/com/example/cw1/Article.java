package com.example.cw1;

public class Article {
    private int ArticleId;
    private final String Title;
    private final String Content;
    private String category;

    public Article(int ArticleId, String Title, String Content){
        this.ArticleId = ArticleId;
        this.Title = Title;
        this.Content = Content;
    }

    public Article(int ArticleId, String Title, String Content,String category){
        this.ArticleId = ArticleId;
        this.Title = Title;
        this.Content = Content;
        this.category = category;
    }


    public String getContent() {
        return Content;
    }

    public String getTitle() {
        return Title;
    }

    public int getArticleId() {
        return ArticleId;
    }

    public String getCategory() {
        return category;
    }
}
