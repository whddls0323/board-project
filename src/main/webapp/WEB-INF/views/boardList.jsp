<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.sql.Timestamp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>로그인 후 메인 화면</title>
<link rel="stylesheet" href="/logout.css">
<link rel="stylesheet" href="/search.css">
</head>
<body>
	<div id="user">
		<ul>
			<li>
				<a id="user-menu"><span class="session-name">${sessionScope.username}</span></a>
			</li>
		</ul>
	</div>
	<div id="logout-menu" style="display: none;">
		<a href="/logout" id="logout-link">로그아웃</a>
	</div>
	<h1>게시글 목록</h1>
	<table border="1" cellpadding="0" cellspacing="0" width="700">
		<tr>
			<th bgcolor="orange" width="50">번호</th>
			<th bgcolor="orange" width="400">제목</th>
			<th bgcolor="orange" width="100">작성자</th>
			<th bgcolor="orange" width="100">등록일</th>
			<th bgcolor="orange" width="50">조회수</th>
		</tr>
		<c:if test="${not empty boardList}">
    		<c:forEach var="board" items="${boardList}">
		        <tr>
		            <td>${board.seq}</td>
		            <td><a class="title" href="/update/${board.seq}">${board.title}</a></td>
		            <td>${board.writer}</td>
		            <td>
                        <fmt:formatDate value="${board.regdate}" pattern="yyyy-MM-dd HH:mm:ss" />
                    </td>
		            <td>${board.cnt}</td>
		        </tr>
    	</c:forEach>
	</c:if>
	<c:if test="${empty boardList}">
	    <tr>
	        <td colspan="5">게시글이 없습니다.</td>
	    </tr>
	</c:if>
	</table>
	<br>
	<button id="board-save" type="button" onclick="location.href='/insert';">게시글 등록</button>
	<form action="/search" method="get" class="search-form">
		<div class="input-group">
			<div class="input-group-prepend">
				<select name="type">
					<option value="title">제목</option>
					<option value="nickname">글쓴이</option>
				</select>
			</div>
			<input type="text" name="keyword" class="search-text"/>
			<div class="input-group-append">
				<button class="search-button" type="submit">검색</button>
			</div>
		</div>
	</form>
	<div class="paging_wrap">
		<c:if test="${totalPages > 1}">
			<c:forEach begin="0" end="${totalPages - 1}" var="i">
				<c:choose>
					<c:when test="${i == currentPage}">
						<em>${i + 1}</em>
					</c:when>
					<c:otherwise>
						<a class="other-page" href="/boardList?page=${i}">${i + 1}</a>
					</c:otherwise>
				</c:choose>
			</c:forEach>
		</c:if>
	</div>
<script>
    document.addEventListener("DOMContentLoaded", function () {
        let storedServerToken = sessionStorage.getItem("serverToken");
        let currentServerToken = "${serverToken}"; // 서버에서 전달된 토큰

        // 서버 토큰이 변경되었으면 localStorage 초기화
        if (storedServerToken !== currentServerToken) {
            localStorage.clear(); // 방문 기록 초기화
            sessionStorage.setItem("serverToken", currentServerToken);
        }

        let visitedLinks = JSON.parse(localStorage.getItem("visitedLinks")) || {};

        // 방문 여부에 따라 색상 설정
        document.querySelectorAll(".title").forEach(link => {
            if (visitedLinks[link.href]) {
                link.style.color = "purple"; // 방문한 경우
            } else {
                link.style.color = "black"; // 방문하지 않은 경우
            }

            link.addEventListener("click", function () {
                visitedLinks[link.href] = true;
                localStorage.setItem("visitedLinks", JSON.stringify(visitedLinks));
                link.style.color = "purple"; // 클릭 후 바로 색상 변경
            });
        });
    });
</script>
<script src="/logout.js"></script>
</body>
</html>