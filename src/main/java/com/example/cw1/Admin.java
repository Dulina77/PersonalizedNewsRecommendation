package com.example.cw1;

import java.io.IOException;
import java.sql.SQLException;

public class Admin extends User{

    public Admin(String username, String password) {
        super(username, password);
    }

    public Admin(String username, String password,String email, String firstName, String lastName){
        super(username, password, email, firstName, lastName);
    }

    public void AddArticle(String newArticleTitle, String newArticleContent ) throws IOException, SQLException {
        DataBaseHandler.getArticleDetails(newArticleTitle);
        KeywordExtraction keywordExtraction = new KeywordExtraction();
        String category = keywordExtraction.categorizeArticle(newArticleContent);
        DataBaseHandler.insertArticle(newArticleTitle,newArticleContent,category);
    }

    public boolean DeleteArticle(String articleTitle){
        boolean success = DataBaseHandler.deleteArticle(articleTitle);
        return success;
    }

    public void UpdateArticle(String articleTitle, String updatedContent){
        DataBaseHandler.articleContentUpdater(articleTitle, updatedContent);
    }


}
