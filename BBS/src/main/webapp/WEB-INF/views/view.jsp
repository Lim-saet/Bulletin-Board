<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
<c:if test="${userid} eq ${post.writer}">
<tr><td><input type=button id=btnUpdate value='수정'></td> 
<!-- 해당 게시글 작성자만 수정,삭제 버튼이 나와야함->세션을 받아 세션이 같으면 버튼이 나오도록 다르면 안나오도록-->
<td><input type=button id=btnDelete value='삭제'></td></tr>
</c:if>
<c:if test="${userid ne null}">
<table >
<tr>
	<td>
		<tr>
			<td><textarea row=5 cols=60 id=reply_content></textarea></td>
			<td id=btnAddReply style='background-color:pink'>댓글등록</td>	
		</tr>

	<tr>
		<td>댓글내용</td>
	</tr>
	
</table>
</c:if>
</body>
<c:forEach items="${reply_list}" var="reply">
<tr><td>${reply.content}<br>${reply.writer}&nbsps;[${reply.created}]<br><hr></td></tr>
</c:forEach>
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
	.on('click','#btnAddReply',function(){
		let pstr=$('#reply_content').val();
		pstr=$.trim(pstr);
		if(pstr=='') return false;
		console.log($('#bbs_id').val()+','+pstr);
		$.post('http://localhost:8080/app/ReplyControl',
				{optype:'add',reply_content:pstr,bbs_id:$('#bbs_id').val()},
				function(result){
					console.log(result);
			if(result=="fail") return false;
		
			let str='<tr><td>'+pstr+'<td><tr>';
			$(this).closest('table').parent().parent().after(str);
			$('#reply_content').val('');
		},'text')
		return false;
	})
	//삭제는 reply_id를 받아서 sql에서 삭제 
</script>
</html>