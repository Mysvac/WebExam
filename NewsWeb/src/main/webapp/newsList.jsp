<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" type="text/css" href="style.css">
    <title>上理工新闻</title>
</head>
<body>
<h1><a href="newsservlet">新闻列表</a></h1>
<!-- 导航栏 -->
<div class="navbar">
    <ul>
        <li><a href="newsservlet?action=month&month=2024-05">5月</a></li>
        <li><a href="newsservlet?action=month&month=2024-06">6月</a></li>
        <li><a href="newsservlet?action=month&month=2024-07">7月</a></li>
        <li><a href="newsservlet?action=month&month=2024-08">8月</a></li>
        <li><a href="newsservlet?action=month&month=2024-09">9月</a></li>
        <li><a href="newsservlet?action=month&month=2024-10">10月</a></li>
        <li><a href="newsservlet?action=month&month=2024-11">11月</a></li>
        <li><a href="newsservlet?action=month&month=2024-12">12月</a></li>
    </ul>
</div>

<form action="newsservlet" method="get">
    <input type="text" name="searchQuery" placeholder="请输入标题搜索" />
    <button type="submit">搜索</button>
</form>
<br>

<!-- 遍历并展示新闻列表 -->
<c:forEach var="news" items="${newsList}">
    <div class="news-preview">
        <a href="news-detail?id=${news.id}" class="news-title">${news.title}</a>
        <c:if test="${not empty news.imageLink}">
            <img src="${news.imageLink}" alt="News Image"/>
        </c:if>
        <div class="news-date">${news.date}</div>
        <div class="news-summary">${news.summary}</div>
    </div>
</c:forEach>

</body>
</html>
