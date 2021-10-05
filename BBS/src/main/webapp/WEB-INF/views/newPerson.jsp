<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원가입</title>
</head>
<body>
<form method="post" action="/app/signin">
실명: <input type=text name=realname><br>
로그인아이디: <input type=text name=loginid><br>
비밀번호: <input type=password name=password><br>
비밀번호확인: <input type=password name=ch_password><br>
모바일 : <input type=text name=mobile><br><br>
<input type=submit value='가입하기'>
<input type=reset value='취소'>
<input type=button value='목록보기' onClick="location.href='list'">
<input type=button value='로그인' onClick="location.href='login'">
</form>
</body>
</html>