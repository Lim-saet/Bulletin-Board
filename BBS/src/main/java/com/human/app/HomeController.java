package com.human.app;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	@Autowired
	private SqlSession sqlSession;
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		/*Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );*/
		
		return "home";
	}
	
	@RequestMapping("/home")
	   public String home() {
	      return "home";
	   }
	
	@RequestMapping(value = {"/list/{pageno}"}, method = RequestMethod.GET,
			produces = "application/text; charset=utf8") 
  	public String selectBBS(@PathVariable("pageno") int pageno,HttpServletRequest hsr,Model model) { 
		//MyBatis 호출해서 가져오기 
  		iBBS bbs=sqlSession.getMapper(iBBS.class);
  		int start=20*(pageno-1)+1;
  		int end=20*pageno;
  		ArrayList<Listinfo> alBBS=bbs.getList(start,end);
  		//System.out.println(alBBS.size());
  		String pDirection = "";
  		if(pageno==1) {
  			pDirection="<a href='/app/list/"+(pageno+1)+"'>다음페이지</a>";
  		} else {
  			pDirection="<a href='/app/list/"+(pageno-1)+"'>이전페이지</a>&nbsp;&nbsp;"+
  					"<a href='/app/list/"+(pageno+1)+"'>다음페이지</a>";
  		}
  		model.addAttribute("direct",pDirection);///여기까지 페이징처리
  		
  		HttpSession session=hsr.getSession();
  		String userid=(String) session.getAttribute("loginid");
  		System.out.println("userid ["+userid+"]");
  		
  		if(userid==null || userid.equals("")) {
  			model.addAttribute("loggined","0");
  		} else { //로그인한 사용자
  			model.addAttribute("loggined","1");
  			model.addAttribute("userid",userid);
  		}
  		model.addAttribute("listBBS",alBBS);
  	
  		return "list";
  	}
	
	@RequestMapping("/logout")
	   public String logout(HttpServletRequest hsr) {
		  HttpSession session=hsr.getSession();
		  session.invalidate();//세션 삭제
	      return "redirect:/list/1";
	     }
	   
	   
	 @RequestMapping("/login")
	   public String login() {
	      return "login";
	   }
	 @RequestMapping("/newPerson")
	   public String newPerson(HttpServletRequest hsr) {
		  if(loginUser(hsr)) return "redirect:/list";
	      return "newPerson";
	   }
	 
	 @RequestMapping(value = "/check_user", method = RequestMethod.POST,
			produces = "application/text; charset=utf8")
		public String check_user(HttpServletRequest hsr,Model model) {
			String userid=hsr.getParameter("userid");
			//System.out.println("id ["+userid+"]");
			String pw=hsr.getParameter("pw");
			//System.out.println("pw ["+pw+"]");
			//DB에서 유저확인: 기존유저면 리스트(새글쓰기 나오게) 없는 유저면 login창 그대로 
			iMember member=sqlSession.getMapper(iMember.class);
				int n = member.doCheckuser(userid,pw);
				if(n==1) {
					HttpSession session=hsr.getSession();
					session.setAttribute("loginid", userid);
					return "redirect:/list/1";
				} else {
					return "redirect:/login";
				}
	}
	
  	@RequestMapping(value = "/view/{bbs_id}", method = RequestMethod.GET) 
  	public String selectoneBBS(@PathVariable("bbs_id") int bbs_id, Model model,
  			HttpServletRequest hsr) {
  	
  		//System.out.println("bbs_id ["+bbs_id+"]"); 
  		iBBS bbs=sqlSession.getMapper(iBBS.class);
  		Listinfo post=bbs.getPost(bbs_id);//제이쿼리(list)로 받은 아이디로 호출...
  		model.addAttribute("post",post);
  		
  		//세션을 받아 게시글 작성자인지 대조해야됌
  		HttpSession session=hsr.getSession();//세션을 받아서
  		String userid=(String) session.getAttribute("loginid");
  		model.addAttribute("userid",userid);//모델로 비교 
//  		String userid=(String) session.getAttribute("loginid");
//  		System.out.println("userid ["+userid+"]");
  		
  		//로그인 여부 체크 
  		if(userid==null || userid.equals("")) {
  			model.addAttribute("loggined","0");
  		} else { //로그인한 사용자
  			model.addAttribute("loggined","1");
  			//model.addAttribute("userid",userid);
  		}
  		
  		//현재 게시물에 속한 댓글 리스트 가져오기
  		iReply r=sqlSession.getMapper(iReply.class);
  		ArrayList<Replyinfo> replyList=r.getReplyList(bbs_id);
  		model.addAttribute("reply_list",replyList);
  		
  		return "view"; 
  	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String writenew(HttpServletRequest hsr, Model model) {
		//세션체크로 url주소로 바로 새글쓰기로 넘어가지 못하도록 설정
		HttpSession s=hsr.getSession();
		String userid=(String) s.getAttribute("loginid");
		if(userid==null || userid.equals("")) {
			return "redirect:/list/1";
		}else {
			model.addAttribute("userid",userid);//모델로 비교			
		}
		return "new"; 
	}
	
	@Resource(name="uploadPath")
	String uploadPath;
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String insertBBS(HttpServletRequest hsr,MultipartFile ufile) {
		if(!loginUser(hsr)) return "redirect:/list/1";
		
		String fileName= ufile.getOriginalFilename();
		System.out.println("filename ["+fileName+"]");
		File target = new File(uploadPath, fileName);
		
		if(!new File(uploadPath).exists()) {
			new File(uploadPath).mkdir();
		}
		try {
			FileCopyUtils.copy(ufile.getBytes(),target);
		} catch(Exception e) {
			e.printStackTrace();
		}
		HttpSession s= hsr.getSession();
		String userid=(String) s.getAttribute("userid");
		String pTitle = hsr.getParameter("title");
		String pContent = hsr.getParameter("content");
		String pWriter = hsr.getParameter("writer");

		//String pPasscode = hsr.getParameter("passcode");
		//System.out.println("title ["+pTitle+"] content ["+pContent+"] writer ["+pWriter+"] passcode ["+pPasscode+"]");
		//출력완료-->insert into DB
		iBBS bbs = sqlSession.getMapper(iBBS.class);
		
		bbs.writebbs(pTitle, pContent, pWriter,fileName);
		
		return "redirect:/list/1";
	}
	
	@RequestMapping(value = "/update_view/{bbs_id}", method = RequestMethod.GET)
	public String upviewBBS(@PathVariable("bbs_id") int bbs_id, Model model,
			HttpServletRequest hsr) {

		HttpSession session=hsr.getSession();
		model.addAttribute("userid",session.getAttribute("loginid"));
		
		if(!loginUser(hsr)) return "redirect:/list/1";
		
		iBBS bbs=sqlSession.getMapper(iBBS.class);
  		Listinfo post=bbs.getPost(bbs_id);//제이쿼리(list)로 받은 아이디로 호출...
  		model.addAttribute("update",post);
  		//URL로 못넘어가도록 업데이트jsp에서 c:if로 막아줘야하나??
		return "update";
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.POST,
			produces = "application/text; charset=utf8")
	public String updateBBS(HttpServletRequest hsr
			) {
		//몇번게시글을 업데이트 할것인지 추가
		//mybatis 호출 
		//-> 받아온 데이터를 update.jsp에 전달 
		
		int uBbsid= Integer.parseInt(hsr.getParameter("bbs_id"));
		String uTitle = hsr.getParameter("title");
		String uContent = hsr.getParameter("content");
		//System.out.println("content ["+uContent+"]");
		iBBS bbs= sqlSession.getMapper(iBBS.class);
		//System.out.println("title ["+uTitle+"] content ["+uContent+"] bbs_id ["+uBbsid+"]");
		bbs.updatebbs(uBbsid,uTitle, uContent);
		
//		
//		String content=hsr.getParameter("content");
//		String title= hsr.getParameter("title");
//		bbs.updatebbs(bbsid,content,title);
		return "redirect:/list/1";
	}
	
	
	@RequestMapping(value = "/delete/{bbs_id}", method = RequestMethod.GET,
			produces = "application/text; charset=utf8")
	public String deleteBBS(@PathVariable("bbs_id") int bbs_id, Model model,
			HttpServletRequest hsr) {
		if(!loginUser(hsr)) return "redirect:/list/1";
		
		System.out.println("bbs_id**["+bbs_id+"]");//출력완료
		model.addAttribute("bbsid",bbs_id);
		//int bbsid=Integer.parseInt(hsr.getParameter("bbs_id"));
		iBBS bbs=sqlSession.getMapper(iBBS.class);
		bbs.deletebbs(bbs_id);
		System.out.println("삭제완료");
		return "redirect:/list/1";
	}
	
	@RequestMapping(value="/signin",method=RequestMethod.POST,
			produces = "application/text; charset=utf8")	
	public String signin(HttpServletRequest hsr) {
		if(loginUser(hsr)) return "redirect:/list/1";
		
		String realname=hsr.getParameter("realname");
		String loginid=hsr.getParameter("loginid");
		String password=hsr.getParameter("password");
		//MyBatis를 이용해서 회원추가(member table)
		iMember member=sqlSession.getMapper(iMember.class);
		member.doSignin(realname, loginid, password);
		return "login";
	}
	
	public boolean loginUser(HttpServletRequest hsr) {
	   HttpSession s= hsr.getSession();
	   String userid=(String) s.getAttribute("loginid");
	   if(userid==null || userid.equals("")) return false;
	   return true;
   }
	
	@RequestMapping(value="/ReplyControl",method=RequestMethod.POST)
	@ResponseBody
	public String doReplyControl(HttpServletRequest hsr) {
		String result="";
		
		try{
		iReply reply=sqlSession.getMapper(iReply.class); //try문 안에서 Mybatis호출
		String optype=hsr.getParameter("optype");

		//System.out.println("optype ["+optype+"]");
		
		if(optype.equals("add")) { //댓글등록(추가)
			//MyBatis 호출
			String reply_content=hsr.getParameter("reply_content");
			//왜 null이 올까아
			//System.out.println("replycontent ["+reply_content+"]");
			int bbs_id=Integer.parseInt(hsr.getParameter("bbs_id"));
			HttpSession s= hsr.getSession();
			String userid=(String) s.getAttribute("loginid");
			reply.addReply(bbs_id,reply_content,userid);
			
		} else if(optype.equals("delete")) { //댓글삭제
			//System.out.println(hsr.getParameter("reply_id"));
			int reply_id=Integer.parseInt(hsr.getParameter("reply_id"));
			System.out.println("replyid["+reply_id+"]");

			reply.delete(reply_id);
		  } else if(optype.equals("update")) { //댓글수정
			  String content=hsr.getParameter("reply_update");
			  int reply_id=Integer.parseInt(hsr.getParameter("reply_id")); 
			  System.out.println("reply_id ["+reply_id+"]");
			   //왜 null인데
			  reply.update(content,reply_id);
		    }
		result= "ok";
		} catch (Exception e) {
			result="fail";
		  } finally {
			return result;
		    }
	}
		
}
	