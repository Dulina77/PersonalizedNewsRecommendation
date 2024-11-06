package com.example.cw1;

public class Article {
    private final String Title;
    private final String Content;

    public Article(String Title, String Content){
        this.Title = Title;
        this.Content = Content;
    }

    public String getContent() {
        return Content;
    }

    public String getTitle() {
        return Title;
    }
}
