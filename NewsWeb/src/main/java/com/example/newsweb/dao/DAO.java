package com.example.newsweb.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public interface DAO {
    // 提供数据库连接的具体实现
    public default Connection getConnection() throws SQLException {
        String URL = "jdbc:mysql://localhost:3306/news?useUnicode=true&characterEncoding=UTF-8";
        String USER = "root";
        String PASSWORD = "111111";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // 注册数据库驱动，mySQL版本8.0
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // 获取并返回数据库连接
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}