package com.example.newsweb.dao;

import com.example.newsweb.model.News;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NewsDAOImpl implements NewsDAO {

    private final Gson gson = new Gson();

    // 获取所有新闻
    @Override
    public List<News> getAllNews() throws SQLException {
        List<News> newsList = new ArrayList<>();
        String sql = "SELECT * FROM newsdetail ORDER BY id ASC "; // 按照 id 排序

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                News news = new News();
                news.setId(rs.getInt("id"));  // 获取数据库中的 id
                news.setTitle(rs.getString("title"));
                news.setDate(rs.getString("date"));
                news.setSummary(rs.getString("summary"));
                news.setLink(rs.getString("link"));
                news.setImageLink(rs.getString("image_link"));

                // 解析 JSON 数据，将 JSON 字符串转换为 List<String>
                String contentJson = rs.getString("content");
                if (contentJson != null && !contentJson.isEmpty()) {
                    List<String> contentList = gson.fromJson(contentJson, new TypeToken<List<String>>(){}.getType());
                    news.setContent(contentList);
                }

                String imagesJson = rs.getString("images");
                if (imagesJson != null && !imagesJson.isEmpty()) {
                    List<String> imagesList = gson.fromJson(imagesJson, new TypeToken<List<String>>(){}.getType());
                    news.setImages(imagesList);
                }

                String authorJson = rs.getString("author");
                if (authorJson != null && !authorJson.isEmpty()) {
                    List<String> authorList = gson.fromJson(authorJson, new TypeToken<List<String>>(){}.getType());
                    news.setAuthor(authorList);
                }

                newsList.add(news);
            }
        }
        return newsList;
    }

    // 根据 ID 获取新闻详情
    @Override
    public News getNewsById(int id) throws SQLException {
        String sql = "SELECT * FROM newsdetail WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                News news = new News();
                news.setId(rs.getInt("id"));  // 获取数据库中的 id
                news.setTitle(rs.getString("title"));
                news.setDate(rs.getString("date"));
                news.setLink(rs.getString("link"));
                news.setImageLink(rs.getString("image_link"));

                // 解析 JSON 数据，将 JSON 字符串转换为 List<String>
                String contentJson = rs.getString("content");
                if (contentJson != null && !contentJson.isEmpty()) {
                    List<String> contentList = gson.fromJson(contentJson, new TypeToken<List<String>>(){}.getType());
                    news.setContent(contentList);
                }

                String imagesJson = rs.getString("images");
                if (imagesJson != null && !imagesJson.isEmpty()) {
                    List<String> imagesList = gson.fromJson(imagesJson, new TypeToken<List<String>>(){}.getType());
                    news.setImages(imagesList);
                }

                String authorJson = rs.getString("author");
                if (authorJson != null && !authorJson.isEmpty()) {
                    List<String> authorList = gson.fromJson(authorJson, new TypeToken<List<String>>(){}.getType());
                    news.setAuthor(authorList);
                }

                return news;
            }
        }
        return null;
    }

    public List<News> getNewsByMonth(String month) throws SQLException {
        List<News> newsList = new ArrayList<>();
        String sql = "SELECT * FROM newsdetail WHERE DATE_FORMAT(date, '%Y-%m') = ? ORDER BY date DESC";  // 按年月查询

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, month);  // 设置月份条件，格式为 "YYYY-MM"
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                News news = new News();
                news.setId(rs.getInt("id"));
                news.setTitle(rs.getString("title"));
                news.setDate(rs.getString("date"));
                news.setLink(rs.getString("link"));
                news.setImageLink(rs.getString("image_link"));

                String contentJson = rs.getString("content");
                if (contentJson != null && !contentJson.isEmpty()) {
                    List<String> contentList = gson.fromJson(contentJson, new TypeToken<List<String>>(){}.getType());
                    news.setContent(contentList);
                }

                String imagesJson = rs.getString("images");
                if (imagesJson != null && !imagesJson.isEmpty()) {
                    List<String> imagesList = gson.fromJson(imagesJson, new TypeToken<List<String>>(){}.getType());
                    news.setImages(imagesList);
                }

                String authorJson = rs.getString("author");
                if (authorJson != null && !authorJson.isEmpty()) {
                    List<String> authorList = gson.fromJson(authorJson, new TypeToken<List<String>>(){}.getType());
                    news.setAuthor(authorList);
                }

                newsList.add(news);
            }
        }
        return newsList;
    }


    // 插入新闻
    @Override
    public boolean addNews(News news) throws SQLException {
        String sql = "INSERT INTO newsdetail (title, date, summary, link, image_link, content, images, author) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, news.getTitle());
            statement.setString(2, news.getDate());
            statement.setString(3, news.getSummary());
            statement.setString(4, news.getLink());
            statement.setString(5, news.getImageLink());

            // 将 List<String> 转换为 JSON 字符串
            statement.setString(6, gson.toJson(news.getContent()));
            statement.setString(7, gson.toJson(news.getImages()));
            statement.setString(8, gson.toJson(news.getAuthor()));

            return statement.executeUpdate() > 0;  // 如果插入成功，则返回 true
        }
    }

    // 根据搜索关键词获取新闻
    public List<News> searchNews(String searchQuery) throws SQLException {
        List<News> newsList = new ArrayList<>();
        String sql = "SELECT * FROM newsdetail WHERE title LIKE ? ORDER BY date DESC";  // 仅搜索标题

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            String query = "%" + searchQuery + "%";  // 使用通配符 % 进行模糊查询
            statement.setString(1, query);

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                News news = new News();
                news.setId(rs.getInt("id"));
                news.setTitle(rs.getString("title"));
                news.setDate(rs.getString("date"));
                news.setLink(rs.getString("link"));
                news.setImageLink(rs.getString("image_link"));

                String contentJson = rs.getString("content");
                if (contentJson != null && !contentJson.isEmpty()) {
                    List<String> contentList = gson.fromJson(contentJson, new TypeToken<List<String>>(){}.getType());
                    news.setContent(contentList);
                }

                String imagesJson = rs.getString("images");
                if (imagesJson != null && !imagesJson.isEmpty()) {
                    List<String> imagesList = gson.fromJson(imagesJson, new TypeToken<List<String>>(){}.getType());
                    news.setImages(imagesList);
                }

                String authorJson = rs.getString("author");
                if (authorJson != null && !authorJson.isEmpty()) {
                    List<String> authorList = gson.fromJson(authorJson, new TypeToken<List<String>>(){}.getType());
                    news.setAuthor(authorList);
                }

                newsList.add(news);
            }
        }
        return newsList;
    }


}
