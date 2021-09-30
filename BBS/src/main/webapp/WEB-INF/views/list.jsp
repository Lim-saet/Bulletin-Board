<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시물 목록보기</title>
</head>
<style>

</style>
<body>
 <table algin=center valign=top>
  	<thead>
  		<tr><th>게시물번호<th>제목</th><th>작성자</th><th>작성시각</th><th>수정시각</th></tr>
  	</thead>
    <c:forEach items="${listBBS}" var="list">
    	<tr><td>${list.bbs_id}</td><td>${list.title}</td><td>${list.writer}</td>
    	<td>${list.created}</td><td>${list.updated}</td>
    	</tr>
    </c:forEach>
</table>
</body>
<script src='http://code.jquery.com/jQuery-3.5.0.js'></script>
<script>
$(document)
	.on('click','tr',function(){
		let bbs_id=$(this).find('td:eq(0)').text();
		console.log('bbs_id ['+bbs_id+']');
		document.location="/app/view/"+bbs_id;
		return false;
	})
</script>
</html>