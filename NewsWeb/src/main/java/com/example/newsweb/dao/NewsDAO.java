package com.example.newsweb.dao;

import com.example.newsweb.model.News;

import java.sql.SQLException;
import java.util.List;

public interface NewsDAO extends DAO{
    public boolean addNews(News news) throws SQLException;
    public News getNewsById(int id) throws SQLException;
    public List<News> getAllNews() throws SQLException;
    public List<News> searchNews(String searchQuery) throws SQLException;
    public List<News> getNewsByMonth(String month) throws SQLException;

}
