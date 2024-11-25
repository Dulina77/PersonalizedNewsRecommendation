package com.example.cw1;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;


public class FullArticleController {

    @FXML
    private Button BackButton;
    @FXML
    private Button LikeButton;
    @FXML
    private Button DislikeButton;
    @FXML
    private TextArea contentSpace;
    @FXML
    private Label TitleSpace;



    public void setArticleDetails(String title, String content){
        contentSpace.setText(content);
        TitleSpace.setText(title);
    }

}
