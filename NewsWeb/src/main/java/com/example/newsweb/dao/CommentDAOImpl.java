package com.example.newsweb.dao;

import com.example.newsweb.model.Comment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentDAOImpl implements CommentDAO{

    @Override
    public boolean addComment(Comment comment) {
        String sql = "INSERT INTO comments (news_id, username, comment_text, date) VALUES (?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, comment.getNewsId());
            stmt.setString(2, comment.getUsername());
            stmt.setString(3, comment.getCommentText());  // 保持换行符原样存储
            stmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));  // 保存评论时间

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Comment> getCommentsByNewsId(int newsId) {
        List<Comment> comments = new ArrayList<>();
        String sql = "SELECT * FROM comments WHERE news_id = ? ORDER BY date DESC";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, newsId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Comment comment = new Comment();
                comment.setId(rs.getInt("id"));
                comment.setNewsId(rs.getInt("news_id"));
                comment.setUsername(rs.getString("username"));
                comment.setCommentText(rs.getString("comment_text"));
                comment.setDate(rs.getTimestamp("date"));
                comments.add(comment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return comments;
    }
}
