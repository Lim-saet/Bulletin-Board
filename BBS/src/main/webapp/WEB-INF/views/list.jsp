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
table{
	border: 1px solid black;
	border-collapse: collapse;
	margin-left:auto; 
    margin-right:auto;
}
table,tr,td{
 	border:1px solid black;
}
td:hover {
	cursor: pointer;
}
th{
	border:1px solid black;
}
th:hover {
	cursor: default; 
}
thead{
	 pointer-events : none;
}
input[type=button] {
	margin: auto;
	display:block;
}
</style>
<body>
<div class='content'>

<table id=tbllist>
	
  	<thead>
  		<tr><th>게시물번호<th>제목</th><th>작성자</th><th>작성시각</th><th>수정시각</th></tr>
  	</thead>
    <c:forEach items="${listBBS}" var="list">
    	<tr><td>${list.bbs_id}</td><td>${list.title}</td><td>${list.writer}</td>
    	<td>${list.created}</td><td>${list.updated}</td>
    	</tr>
    </c:forEach>
</table>
<c:if test="${loggined eq '1'}">
	${userid}&nbsp;<a href="/app/logout">로그아웃</a>
	<input type=button value='새글쓰기' id=btnNew>
</c:if>
<c:if test="${loggined eq '0'}">
	<input type=button value='로그인 'id=btnLogin>  
</c:if>
<!--  onClick="location.href='new'" -->
</div>
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
 	.on('click','#btnNew',function(){
		document.location='/app/new';
		return false;
	}) 
	.on('click','#btnLogin',function(){
		document.location='/app/login';
		return false;
	})
</script>
</html>