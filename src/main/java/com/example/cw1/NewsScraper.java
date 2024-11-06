package com.example.cw1;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class NewsScraper {
    private static final String site_URL = "https://www.bbc.com";
    private static final int max_articles = 30;     

    public static void main(String[] args) throws IOException {
        List<Article> articles = new ArrayList<>();
        Set<String> articleLinks = new HashSet<>();

        Document homepage = Jsoup.connect(site_URL).get();

    }

}
