<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 수정</title>
</head>
<body>
<form method=POST action="/app/update">
<input type=text id=bbs_id name=bbs_id value='${update.bbs_id}'>
<table>
<tr><td>제목</td><td><input type=text name=title value='${update.title}'></td><tr>
<tr><td valign=top>내용</td></tr><tr><td colspan=2>
<textarea name=content rows=20 cols=60>${update.content}</textarea></td></tr>
<tr><td>작성자</td><td><input type=text name=writer value='${update.writer}'></td></tr>
<tr><td>작성시각</td><td>${update.created}</td></tr>
<tr><td>수정시각</td><td>${update.updated}</td></tr>
<tr><td colspan=2><input type=submit value='수정'>&nbsp; 
		<input type=button value='취소(목록보기)' id=btnlist >
		<!-- OnClick="javascript:history.back(-1)": 바로 전페이지로이동 -->
</table>
</form>
</body>
<script src='http://code.jquery.com/jQuery-3.5.0.js'></script>
<script>
	
</script>
</html>