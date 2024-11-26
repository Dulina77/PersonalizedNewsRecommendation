package com.example.cw1;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArticleRecommendation {
    private static final String url = "jdbc:mysql://localhost:3306/news";
    private static final String user_db = "root";
    private static final String password = "Dulina@123";

    public List<Article> getRecommendation(String userName){
        List<Article> recommendedArticles = new ArrayList<>();


        String query = "SELECT a.articleId, a.Title, a.category " +
                "FROM article a " +
                "WHERE a.category IN ( " +
                "    SELECT DISTINCT category " +
                "    FROM ( " +
                "        SELECT category " +
                "        FROM user_scores " +
                "        WHERE user_name = ? " +
                "        ORDER BY score DESC " +
                "        LIMIT 5 " +
                "    ) AS top_categories " +
                ") " +
                "AND a.articleId NOT IN ( " +
                "    SELECT articleId " +
                "    FROM user_scores " +
                "    WHERE user_name = ? " +
                ") " +
                "ORDER BY a.articleId DESC " +
                "LIMIT 5";


        try (Connection connection = DriverManager.getConnection(url, user_db, password)) {
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, userName);
                statement.setString(2, userName);
                ResultSet rs = statement.executeQuery();
                System.out.println(rs);
                while (rs.next()) {
                    int articleId = rs.getInt("articleId");
                    String title = rs.getString("Title");
                    String category = rs.getString("category");

                    recommendedArticles.add(new Article(articleId, title, category));
                }
                System.out.println(recommendedArticles);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return recommendedArticles;

    }

}
