<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 상세 정보</title>
<link rel="stylesheet" href="/update.css">
</head>
<body>
	<div id="user">
		<ul>
			<li>
				<a id="user-menu"><span class="sessionName">${sessionScope.username}</span></a>
			</li>
		</ul>
	</div>
	<div id="logout-menu" style="display: none;">
		<a href="/logout" id="logout-link">로그아웃</a>
	</div>
	<h1 style="width:573.6222px;">게시글 상세 정보</h1>
	<hr>
	<c:if test="${board != null}">
		<form action="/update/${board.seq}" method="post" style="display:inline-block;width:100%;">
			<input type="hidden" name="seq" value="${board.seq}" />
			<table border="1" cellpadding="0" cellspacing="0" style="width:100%;">
				<tr>
					<td bgcolor="orange" width="70">제목</td>
					<td align="left">
						<c:if test="${board.writer == sessionScope.username}">
							<input type="text" class="boardTitle" name="title" value="${board.title}"/>
						</c:if>
						<c:if test="${board.writer != sessionScope.username}">
							${board.title}
						</c:if>
					</td>
				</tr>
				<td bgcolor="orange">작성자</td>
				<td align="left" id="boardNickname" data-board-nickname="${board.writer}">
						${board.writer}
					</td>
				<tr>
					<td bgcolor="orange">내용</td>
					<td align="left">
						<c:if test="${images != null && not empty images}">
							<c:forEach var="image" items="${images}">
								<c:if test="${image != null && not empty image.url}">
									<img src="${image.url}" style="width:400px; height:400px;object-fit:contain">
								</c:if>
							</c:forEach>
						</c:if>
						<c:if test="${board.writer == sessionScope.username}">
							<textarea class="reply-form-textarea" name="content" cols="40" rows="10" style="box-sizing: border-box;width: 100%;">${board.content}</textarea>
						</c:if>
						<c:if test="${board.writer != sessionScope.username}">
							<textarea class="reply-form-textarea" name="content" readonly cols="40" rows="10" style="box-sizing: border-box;width: 100%;">${board.content}</textarea>
						</c:if>
					</td>
				</tr>
				<tr>
					<td bgcolor="orange">등록일</td>
					<td align="left">
						<fmt:formatDate value="${board.regdate}" pattern="yyyy-MM-dd HH:mm:ss" />
					</td>
				</tr>
				<tr>
					<td bgcolor="orange">조회수</td>
					<td align="left">${board.cnt}</td>
				</tr>
				<tr>
					<c:if test="${board.writer == sessionScope.username}">
						<td colspan="2" align="center">
							<input type="submit" value="게시글 수정" />
						</td>
					</c:if>
				</tr>
			</table>	
		</form>
	</c:if>
	<br>
	<c:if test="${commentDtos != null && not empty commentDtos}">
	    <div id="comments-list" style="padding:11.25px;">
	        <c:forEach var="comment" items="${commentDtos}">
	        	<div class="edge" data-comment-id="${comment.id}">
	        		<div class="title">
		                <div class="left" data-comment-nickname="${comment.nickname}">
		                    ${comment.nickname}
		                </div>	    
		                <div class="right">
		                    <time datetime="${comment.regdate}">
							    <fmt:formatDate value="${comment.updatedate}" pattern="yyyy-MM-dd HH:mm:ss" />
							</time>
		                    <span class="sep">&#124;</span>
		                    <a class="commentUpdate">수정</a>
		                    <span class="sep">&#124;</span>
		                    <a class="commentDelete" data-comment-id="${comment.id}">삭제</a>
		                </div>
	            	</div>
	            	<div class="body"> 
	            		<div class="content">
	            			<pre>${comment.body}</pre>
	            		</div>	   	
	            	</div>
	        	</div>	            	        
	        </c:forEach>
	    </div>
	</c:if>
	
	<form style="width:638px;padding:11.25px;">
		<div class="reply-form__container">
			<div class="reply-form__info">
				<div class="reply-form__user-info">
					<span class="replay-form-title">댓글 작성</span>
					<c:if test="${not empty sessionScope.username}">
						<input class="reply-form-user-input" type="text" disabled="disabled" value="${sessionScope.username}">
					</c:if>
				</div>
			</div>
			<div class="reply-form-textarea-wrapper">
				<textarea id="commentTextarea" class="reply-form-textarea" name="comment" maxlength="8000" 
				required="required" style="height:99.5px;"></textarea>
				<div class="reply-form__submit-button-wrapper">
					<button class="reply-form__submit-button" type="button">작성</button>
					<button class="reply-form__submit-button" type="button" onclick="generateComment()">AI 댓글 생성</button>
				</div>
				<c:if test="${board != null}">
					<input type="hidden" id="new-comment-board-id" value="${board.seq}"/>
				</c:if>
			</div>
		</div>
	</form>
	
	<c:if test="${board.writer == sessionScope.username}">
		<button type="button" onclick="location.href='/delete/${board.seq}';" class="boardDelete">게시글 삭제</button>
	</c:if>
	<button type="button" onclick="location.href='/boardList';" class="boardList">게시글 목록</button>
	
<script src="/update.js"></script>
<script src="/logout.js"></script>
<script>
	function generateComment() {
		const boardId = document.querySelector("#new-comment-board-id").value;
		const url = "/api/board/" + boardId + "/ai-comment";
		fetch(url, {
			method: 'POST',
			headers: {
				'Content-Type':'application/json'
			},
			body: JSON.stringify({})
		})
		.then(res => res.json())
		.then(data => {
			const aiComment = data.aiComment || data.message || "AI 댓글 생성 실패";
			document.getElementById("commentTextarea").value = aiComment;
		})
		.catch(err => {
	        alert("AI 댓글 생성 중 오류 발생");
	        console.error(err);
	    });
	}
</script>
</body>
</html>