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
ID: <input type=text name=userid><br>
PW: <input type=password name=pw><br>
<input type=submit value=로그인 id=btnLogin>
<input type=reset value=취소>
<input type=button value='목록보기' onClick="location.href='list'">
<input type=button value='회원가입' onClick="location.href='newPerson'">
</form>
</body>
<script src='http://code.jquery.com/jQuery-3.5.0.js'></script>
<script>

</script>
</html>