package com.example.newsweb.controller;

import com.example.newsweb.dao.NewsDAOImpl;
import com.example.newsweb.model.News;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "NewsDetailServlet", value = "/news-detail")
public class NewsDetailServlet extends HttpServlet {

    private NewsDAOImpl newsDAOImpl = new NewsDAOImpl();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        // 获取URL中的新闻ID参数
        String newsIdStr = request.getParameter("id");
        if (newsIdStr != null) {
            try {
                int newsId = Integer.parseInt(newsIdStr);

                // 调用 DAO 层的 getNewsById 方法获取新闻详情
                News news = newsDAOImpl.getNewsById(newsId);

                // 如果获取到新闻信息，传递给 JSP 页面显示
                if (news != null) {
                    request.setAttribute("news", news);
                    request.getRequestDispatcher("newsDetail.jsp").forward(request, response);
                } else {
                    // 如果没有找到该新闻，跳转到错误页面
                    response.sendRedirect("error.jsp");
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
                response.sendRedirect("error.jsp");
            } catch (SQLException e) {
                e.printStackTrace();
                response.sendRedirect("error.jsp");
            }
        } else {
            // 如果没有传递 id 参数，跳转到错误页面
            response.sendRedirect("error.jsp");
        }
    }
}
