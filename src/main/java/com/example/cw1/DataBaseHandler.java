package com.example.cw1;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataBaseHandler {

    public static void insertUser(User user) throws SQLException {
        String query = "INSERT INTO user_final (user_name,first_name, last_name, email,password) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DataBase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, user.getUserName());
            stmt.setString(2, user.getFirstName());
            stmt.setString(3,user.getLastName());
            stmt.setString(4, user.getEmail());
            stmt.setString(5,user.getPassword());
            stmt.executeUpdate();
        }
    }

    public static void insertAdmin(Admin admin) {
        String query = "INSERT INTO admin (user_name,first_name, last_name, email,password) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DataBase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, admin.getUserName());
            stmt.setString(2, admin.getFirstName());
            stmt.setString(3,admin.getLastName());
            stmt.setString(4, admin.getEmail());
            stmt.setString(5,admin.getPassword());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean userNameCheckAdmin(String username){
        String query = "SELECT * FROM admin WHERE user_name = ?";
        boolean result = false;

        try(Connection connection = DataBase.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement((query));
            stmt.setString(1,username);
            ResultSet resultSet = stmt.executeQuery();
            result = resultSet.next();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;

    }

    public static boolean userNameCheck(String username){
        String query = "SELECT * FROM user_final WHERE user_name = ?";
        boolean result = false;

        try(Connection connection = DataBase.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement((query));
            stmt.setString(1,username);
            ResultSet resultSet = stmt.executeQuery();
            result = resultSet.next();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;

    }

    public static List<String> getUserAttributes(String username) throws SQLException {

        List<String> result = new ArrayList<>();

        String query = "Select first_name,last_name,email FROM user_final WHERE user_name = ?";
        try(Connection connection = DataBase.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement((query));
            stmt.setString(1, username);

            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {

                result.add(resultSet.getString("first_name"));
                result.add(resultSet.getString("last_name"));
                result.add(resultSet.getString("email"));
            }
        }

        return result;

    }

    public static String passwordChecker(String username){
        String query = "SELECT password from user_final WHERE user_name = ?";
        String result = null;
        try(Connection connection = DataBase.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement((query));
            stmt.setString(1,username);

            ResultSet resultSet = stmt.executeQuery();

            if(resultSet.next()){
                result = resultSet.getString("password");
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public static String passwordCheckerAdmin(String username){
        String query = "SELECT password from admin WHERE user_name = ?";
        String result = null;
        try(Connection connection = DataBase.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement((query));
            stmt.setString(1,username);
            ResultSet resultSet = stmt.executeQuery();

            if(resultSet.next()){
                result = resultSet.getString("password");
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public static List<String> getAdminAttributes(String username) throws SQLException {

        List<String> result = new ArrayList<>();

        String query = "Select first_name,last_name,email FROM admin WHERE user_name = ?";
        try(Connection connection = DataBase.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement((query));
            stmt.setString(1, username);

            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {

                result.add(resultSet.getString("first_name"));
                result.add(resultSet.getString("last_name"));
                result.add(resultSet.getString("email"));
            }
        }

        return result;

    }


    public static List<Article> getRecommendation(String userName){
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


        try (Connection connection =  DataBase.getConnection()) {
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

    public static void insertArticle(String title, String content, String category) throws SQLException {
        String insertSQL = "INSERT INTO article (title, content, category) VALUES (?, ?, ?)" + "ON DUPLICATE KEY UPDATE content = ?, category = ?";
        try (Connection connection = DataBase.getConnection();
             PreparedStatement stmt = connection.prepareStatement(insertSQL)){
            stmt.setString(1, title);
            stmt.setString(2, content);
            stmt.setString(3, category);
            stmt.setString(4, content);
            stmt.setString(5, category);

            stmt.executeUpdate();
        }

    }

    public static Article getArticleDetails(String title) throws SQLException {
        int articleId = 0;
        String category = null;
        String queryId = "SELECT articleId FROM article WHERE Title = ?";
        String queryCategory = "SELECT category FROM article WHERE Title = ?";

        try (Connection connection = DataBase.getConnection()) {
            PreparedStatement stmt1 = connection.prepareStatement(queryId);
            stmt1.setString(1, title);
            ResultSet resultSet1 = stmt1.executeQuery();

            PreparedStatement stmt2 = connection.prepareStatement(queryCategory);
            stmt2.setString(1, title);
            ResultSet resultSet2 = stmt2.executeQuery();

            if (resultSet1.next()) {
                articleId = resultSet1.getInt("articleId");
            }
            if (resultSet2.next()) {
                category = resultSet2.getString("category");
            }
            Article article = new Article(articleId, title, category);
            return article;
        }
    }


    public static void recordAction(String username, int articleId, String action){
        String query = "INSERT INTO history(user_name, article_Id, action) values (?,?,?)";
        try(Connection connection = DataBase.getConnection();
            PreparedStatement stmt = connection.prepareStatement(query)){
            stmt.setString(1,username);
            stmt.setInt(2,articleId);
            stmt.setString(3,action);

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static void recordScores(String username,int articleId,String category , int score) throws SQLException {
        String query = "INSERT into user_scores(user_name, articleid, category, score) VALUES (?,?,?,?) "+ "ON DUPLICATE KEY UPDATE score = score + ?";

        try (Connection connection = DataBase.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setInt(2, articleId);
            stmt.setString(3, category);
            stmt.setInt(4, score);
            stmt.setInt(5, score);
            stmt.executeUpdate();
            System.out.println(score);
        }
    }

    public static ArrayList<String> articleTitleFetcher(){
        ArrayList<String> title = new ArrayList<>();
        String query = "SELECT Title FROM article ";
        try(Connection connection = DataBase.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement((query));
            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()){
                title.add(resultSet.getString("Title"));
            }
            return title;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static String articleContentFetcher(String title){
        String query = "SELECT content FROM article WHERE Title = ?";
        try(Connection connection = DataBase.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement((query));
            stmt.setString(1,title);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()){
                return resultSet.getString("content");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return "Article content not accessible";
    }



}
