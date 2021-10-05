<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시물 보기</title>
</head>
<style>

</style>
<body>
<input type=hidden id=bbs_id value='${post.bbs_id}'>

<table>
<tr><td>
<table align=center valign=top>
<tr><td>제목</td><td>${post.title}</td></tr>
<tr><td>내용</td><td>${post.content}</td></tr>
<tr><td>작성자</td><td>${post.writer}</td></tr>
<tr><td>작성시각</td><td>${post.created}</td></tr>
<tr><td>수정시각</td><td>${post.updated}</td></tr>
</table>
</td></tr>
<tr><td><input type=button id=btnUpdate value='수정'></td>
<td><input type=button id=btnDelete value='삭제'></td></tr>
</body>
<script src='http://code.jquery.com/jQuery-3.5.0.js'></script>
<script>
$(document)
	.on('click','#btnUpdate',function(){
		let bbs_id=$('#bbs_id').val();
		console.log('bbs_id2 ['+bbs_id+']');
		alert('수정하시겠습니까?');
		document.location="/app/update/view/"+bbs_id;
		return false;
		
	  /*let bbs_id=$(this).find('td:eq(0)').text();
		console.log('bbs_id ['+bbs_id+']');
		document.location="/app/view/"+bbs_id;
		return false;*/
	})
	.on('click','#btnDelete',function(){
		let bbs_id=$('#bbs_id').val();
		console.log('bbs_id3 ['+bbs_id+']');
		alert('정말 삭제하시겠습니까?');
		document.location="/app/delete/"+bbs_id;
		return false;
	})
</script>
</html>