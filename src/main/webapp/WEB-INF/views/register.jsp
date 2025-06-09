<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원 가입</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
<style>
	body {
		margin:0;
		text-align:center;
	}
	
	.login-button {
		background-color: rgb(9, 170, 92); 
		border: none;
		border-radius:8px;
		margin-top:30px;
		width:410px;
		height:50px;
		color:white;
		font-family:-apple-system, BlinkMacSystemFont,helvetica,"Apple SD Gothic Neo",sans-serif;
		font-size:17px;
		font-weight:700;
	}
	
	#username,#password {
		border: 1px solid rgb(197, 204, 210);
		width:410px;
		padding:15px 27px;
	}
	#username {
		border-top-left-radius:8px;
		border-top-right-radius:8px;
		height:60px;
	}
	#password {
		border-bottom-left-radius:8px;
		border-bottom-right-radius:8px;
		height:59px;
	}
	
	#username:focus, #password:focus {
            outline: none; /* 기본 포커스 아웃라인 제거 */
            border: 1px solid rgb(9, 170, 92); /* 포커스 시 테두리 색상 */
        }
        
</style>
</head>
<body>
	<h1 style="color:#03CF5D;font-weight:bold;margin-bottom:30px">회원가입</h1>
	<form action="/register" method="post">
		<div>
			<input type="text" name="username" id="username" placeholder="아이디" required/><br>
			<input type="password" name="password" id="password" placeholder="비밀번호" required/><br>
		</div>
		<input type="submit" class="login-button" value="회원가입"/>
	</form>
	<c:if test="${not empty errorMessage}">
        <div style="color: red;">
            ${errorMessage}
        </div>
    </c:if>
</body>
</html>