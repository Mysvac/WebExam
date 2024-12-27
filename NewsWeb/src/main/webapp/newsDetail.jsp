<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="zh">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <link rel="stylesheet" type="text/css" href="style.css">
  <title>${news.title}</title>
</head>
<body>
<h1>${news.title}</h1>
<div class="news-detail">

  <div class="news-date">${news.date}</div>

  <div class="news-content">
    <c:forEach var="contentItem" items="${news.content}">
      <p>${contentItem}</p>
    </c:forEach>
  </div>

  <c:if test="${not empty news.imageLink}">
    <img src="${news.imageLink}" alt="新闻图片" />
  </c:if>

  <div class="news-author">
    <c:forEach var="author" items="${news.author}">
      <span>${author}</span>
    </c:forEach>
  </div>
</div>

</body>
</html>
