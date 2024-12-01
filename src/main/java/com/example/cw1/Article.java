package com.example.cw1;

public class Article {
    private int ArticleId;
    private String Title;
    private String Content;
    private String category;

    public Article(int ArticleId, String Title, String category){
        this.ArticleId = ArticleId;
        this.Title = Title;
        this.category = category;
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


    public void setCategory(String category) {
        this.category = category;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setContent(String content) {
        Content = content;
    }

}
