package com.example.cw1;

import java.io.IOException;
import java.sql.SQLException;

public class Admin extends User{

    public Admin(String username, String password) {
        super(username, password);
    }

    public void AddArticle(String newArticleTitle, String newArticleContent ) throws IOException, SQLException {
        KeywordExtraction keywordExtraction = new KeywordExtraction();
        String category = keywordExtraction.categorizeArticle(newArticleContent);
        DataBaseHandler.insertArticle(newArticleTitle,newArticleContent,category);
    }

    public boolean DeleteArticle(String articleTitle){
        boolean success = DataBaseHandler.deleteArticle(articleTitle);
        return success;
    }



}
