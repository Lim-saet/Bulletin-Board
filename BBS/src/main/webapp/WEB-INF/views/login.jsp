<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>로그인</title>
</head>
<body>
<form method="post" action="/app/check_user">
ID: <input type=text name=userid>
PW: <input type=password name=pw>
<input type=submit value=로그인>
<input type=reset value=취소>
<a href=selected?path= name=back>처음페이지로</a>
</body>
</html>