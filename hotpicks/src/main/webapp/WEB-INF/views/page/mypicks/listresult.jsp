<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

    <c:forEach var="article" items="${articleList}" varStatus="status">
    <c:if test="${article.wanna == 0}">
      <div class="one-fourth went" style="margin:0 1% 1% 0; width:24%; height: 300px;"> <a href="#"><img src="${root}/resources/style/images/img_dog/007.jpg" alt="" /></a>
        <h4>${article.title}</h4>
         <p><input type="checkbox"></p>
      </div>
     </c:if>
     </c:forEach>