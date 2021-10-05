package com.human.app;

import java.util.ArrayList;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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
	
	@RequestMapping(value = "/list", method = RequestMethod.GET,
			produces = "application/text; charset=utf8") 
  	public String selectBBS(HttpServletRequest hsr,Model model) { 
		//MyBatis 호출해서 가져오기 
  		iBBS bbs=sqlSession.getMapper(iBBS.class);
  		ArrayList<Listinfo> alBBS=bbs.getList();
  		System.out.println(alBBS.size());

  		HttpSession session=hsr.getSession();
  		String userid=(String) session.getAttribute("loginid");
  		System.out.println("userid ["+userid+"]");
  		
  		if(userid==null || userid.equals("")) {
  			model.addAttribute("loggined","0");
  		} else {
  			model.addAttribute("loggined","1");
  			model.addAttribute("userid",userid);
  		}
  		model.addAttribute("listBBS",alBBS);
  		return "list";
  	}
	
	@RequestMapping("/logout")
	   public String logout(HttpServletRequest hsr) {
		  HttpSession session=hsr.getSession();
		  session.invalidate();
	      return "redirect:/list";
	   }
	 @RequestMapping("/login")
	   public String login() {
	      return "login";
	   }
	 @RequestMapping("/newPerson")
	   public String newPerson() {
	      return "newPerson";
	   }
	@RequestMapping(value = "/check_user", method = RequestMethod.POST,
			produces = "application/text; charset=utf8")
		public String check_user(HttpServletRequest hsr,Model model) {
			String userid=hsr.getParameter("userid");
			System.out.println("id ["+userid+"]");
			String pw=hsr.getParameter("pw");
			System.out.println("pw ["+pw+"]");
			//DB에서 유저확인: 기존유저면 리스트(새글쓰기 나오게) 없는 유저면 login창 그대로 
			iMember member=sqlSession.getMapper(iMember.class);
				int n = member.doCheckuser(userid,pw);
				if(n==1) {
					HttpSession session=hsr.getSession();
					session.setAttribute("loginid", userid);
					return "redirect:/list";
				} else {
					return "redirect:/login";
				}
	}
	
  	@RequestMapping(value = "/view/{bbs_id}", method = RequestMethod.GET) 
  	public String selectoneBBS(@PathVariable("bbs_id") int bbs_id, Model model) {
  		System.out.println("bbs_id ["+bbs_id+"]"); 
  		iBBS bbs=sqlSession.getMapper(iBBS.class);
  		Listinfo post=bbs.getPost(bbs_id);//제이쿼리(list)로 받은 아이디로 호출...
  		model.addAttribute("post",post);
  		return "view"; 
  	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String brandnew() {
		//
		return "new";
	}
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String insertBBS(HttpServletRequest hsr) {
		String pTitle = hsr.getParameter("title");
		String pContent = hsr.getParameter("content");
		String pWriter = hsr.getParameter("writer");
		String pPasscode = hsr.getParameter("passcode");
		System.out.println("title ["+pTitle+"] content ["+pContent+"] writer ["+pWriter+"] passcode ["+pPasscode+"]");
		//출력완료-->insert into DB
		iBBS bbs = sqlSession.getMapper(iBBS.class);
		
		bbs.writebbs(pTitle, pContent, pWriter, pPasscode);
		
		return "redirect:/list";
	}
	@RequestMapping(value = "/update/view/{bbs_id}", method = RequestMethod.GET)
	public String upviewBBS(@PathVariable("bbs_id") int bbs_id, Model model) {
		iBBS bbs=sqlSession.getMapper(iBBS.class);
  		Listinfo post=bbs.getPost(bbs_id);//제이쿼리(list)로 받은 아이디로 호출...
  		model.addAttribute("update",post);
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
		return "redirect:/list";
	}
	
	
	@RequestMapping(value = "/delete/{bbs_id}", method = RequestMethod.GET,
			produces = "application/text; charset=utf8")
	public String deleteBBS(@PathVariable("bbs_id") int bbs_id, Model model
			) {
		System.out.println("bbs_id**["+bbs_id+"]");//출력완료
		model.addAttribute("bbsid",bbs_id);
		//int bbsid=Integer.parseInt(hsr.getParameter("bbs_id"));
		iBBS bbs=sqlSession.getMapper(iBBS.class);
		bbs.deletebbs(bbs_id);
		System.out.println("삭제완료");
		return "redirect:/list";
	}
	
	@RequestMapping(value="/signin",method=RequestMethod.POST,
			produces = "application/text; charset=utf8")	
	public String signin(HttpServletRequest hsr) {
		String realname=hsr.getParameter("realname");
		String loginid=hsr.getParameter("loginid");
		String password=hsr.getParameter("password");
		//MyBatis를 이용해서 회원추가(member table)
		iMember member=sqlSession.getMapper(iMember.class);
		member.doSignin(realname, loginid, password);
		return "login";
	}
}	