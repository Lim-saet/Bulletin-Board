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
<tr><td>내용</td>
	<td>${post.content}
<c:if test="${post.img_loc ne ''}">
	<img src="/app/resources/${post.img_loc}">
</c:if>	
	</td></tr>
<tr><td>작성자</td><td>${post.writer}</td></tr>
<tr><td>작성시각</td><td>${post.created}</td></tr>
<tr><td>수정시각</td><td>${post.updated}</td></tr>
</table>
</td></tr>
<c:if test="${userid eq post.writer}">
<tr><td><input type=button id=btnUpdate value='수정'></td> 
<!-- 해당 게시글 작성자만 수정,삭제 버튼이 나와야함->세션을 받아 세션이 같으면 버튼이 나오도록 다르면 안나오도록-->
<td><input type=button id=btnDelete value='삭제'></td></tr>
</c:if>
<c:if test="${userid ne ''}">
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
</c:if>
</c:if>

	<!-- 아래는 댓글 등록하고 내용 나오고 새로고침하면 등록한 날짜를 나오게 하는 댓글 리스트 -->
	<c:forEach items="${reply_list}" var="reply">
		<tr><td>
		<table id="udTable">
			<tr><td>${reply.content}</td></tr>
			<tr><td>${reply.writer}&nbsp;[${reply.updated}]</td></tr>
			<tr><td align=right>
			<!-- 댓글 수정삭제-->
			<c:if test="${userid eq reply.writer}">
				<input type=button id=btnUpdateReply value="수정" reply_id='${reply.reply_id}'>
				<input type=button id=btnDeleteReply value="삭제" reply_id='${reply.reply_id}'>
			</c:if>
			<tr><td><hr></td></tr>
			</table>
			</td></tr>
	</c:forEach>

</table>

</body>

<script src='http://code.jquery.com/jQuery-3.5.0.js'></script>
<script>
$(document)
	.on('click','#btnUpdate',function(){
		let bbs_id=$('#bbs_id').val();
		console.log('bbs_id2 ['+bbs_id+']');
		alert('수정하시겠습니까?');
		document.location="/app/update_view/"+bbs_id;
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
		
			location.reload();
			let str='<tr><td>'+pstr+'</td></tr>';
			$(this).closest('table').parent().parent().after(str);
			$('#reply_content').val(''); 
		},'text')
		return false;
	})
 	.on('click','#btnDeleteReply',function(){
		if(!confirm("정말로 지울까요?")) return false;
		let reply_id=$(this).attr('reply_id');
		let thisButton=$(this);
		//location.reload();
		$.post('http://localhost:8080/app/ReplyControl',
				{optype:'delete',reply_id:reply_id},function(result){
					console.log(result);
			if(result=="ok") {
				thisButton.closest('table').closest('tr').remove();
			}
	},'text')
	return false;
	}) 
	
  	.on('click','#btnUpdateReply',function(){
  		let reply_id=$(this).attr('reply_id');
 		let content=$(this).closest('table').find('tr:eq(0) td:eq(0)').text();
 		$(this).closest('#udTable').find('tr:lt(2)').hide();//tr:lt(2)-tr의 인덱스번호 2보다 작은
		let str='<textarea rows=5 cols=60 id=reply_update>'+content+
			'</textarea><br>'+
			'<input type=button id=btnComplete value="댓글수정완료" reply_id="'+reply_id+'">&nbsp;'+
			'<input type=button value="취소" onclick="location.reload();">';
			//console.log('reply_id="'+reply_id+'"');
			$(this).closest('td').html(str);
			return false;
	}) 
	
	.on('click','#btnComplete',function(){
		let pstr=$('#reply_update').val();
		pstr=$.trim(pstr);
		let reply_id=$(this).attr('reply_id');
		if(pstr=='') return false;
		console.log($('#bbs_id').val()+','+pstr);
		$.post('http://localhost:8080/app/ReplyControl',
				{optype:'update',reply_update:pstr,reply_id:reply_id},
				function(result){
					console.log(result);
				if(result=="fail") return false;
				location.reload();
				},'text')
	})
	//삭제는 reply_id를 받아서 sql에서 삭제 
</script>
</html>