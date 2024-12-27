package com.example.newsweb.controller;

import com.example.newsweb.model.News;
import com.example.newsweb.service.NewsList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "NewsListServlet", value = "/newsservlet")
public class NewsListServlet extends HttpServlet {

    private NewsList newsListService = new NewsList();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        String action = request.getParameter("action");
        String searchQuery = request.getParameter("searchQuery");
        //String month = request.getParameter("month");
        String type = request.getParameter("type"); // 根据类型筛选新闻

        try {
            List<News> newsList = null;

//            // 如果有月份筛选
//            if (month != null && !month.isEmpty()) {
//                newsList = newsListService.getNewsByMonth(month);
//            }
            // 如果有类型筛选
            if (type != null && !type.isEmpty()) {
                if ("上理要闻".equals(type)) {
                    newsList = newsListService.getNewsByType("");  // 空值处理
                } else {
                    newsList = newsListService.getNewsByType(type); // 根据实际类型查询
                }
            }
            // 如果有搜索关键词
            else if (searchQuery != null && !searchQuery.isEmpty()) {
                newsList = newsListService.searchNews(searchQuery);
            }
            // 如果没有筛选条件，默认显示所有新闻
            else {
                newsList = newsListService.getAllNews();
            }

            request.setAttribute("newsList", newsList);


        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
            return;
        }

        // 转发到 JSP 页面显示结果
        request.getRequestDispatcher("newsList.jsp").forward(request, response);
    }
}

