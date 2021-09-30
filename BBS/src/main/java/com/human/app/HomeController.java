package com.human.app;

import java.util.ArrayList;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
  	public String selectBBS(Model model) { 
		//MyBatis 호출해서 가져오기 
  		iBBS bbs=sqlSession.getMapper(iBBS.class);
  		ArrayList<Listinfo> alBBS=bbs.getList();
  		System.out.println(alBBS.size());
  		model.addAttribute("listBBS",alBBS);
  		
  		return "list"; 
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
	@RequestMapping(value = "/update", method = RequestMethod.POST,
			produces = "application/text; charset=utf8")
	//@ResponseBody
	public String updateBBS(HttpServletRequest hsr) {
		iBBS bbs= sqlSession.getMapper(iBBS.class);
		int bbs_id= Integer.parseInt(hsr.getParameter("bbs_id"));
		String content=hsr.getParameter("content");
		String title= hsr.getParameter("title");
		bbs.updatebbs(bbs_id,content,title);
		return "redirect:/list";
	}
	@RequestMapping(value = "/delete", method = RequestMethod.POST,
			produces = "application/text; charset=utf8")
	public String deleteBBS(HttpServletRequest hsr) {
		int bbsid=Integer.parseInt(hsr.getParameter("bbs_id"));
		iBBS bbs=sqlSession.getMapper(iBBS.class);
		bbs.deletebbs(bbsid);
		return "redirect:/list";
	}
}	