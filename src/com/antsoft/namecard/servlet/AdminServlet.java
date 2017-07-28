package com.antsoft.namecard.servlet;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
//import namecard.*;
//import namecard.manager.*;

public class AdminServlet extends HttpServlet{

	//html tag 정의	
	static String htmlHeader = "<html><head><title></title></head><body>";
	static String htmlEnd = "</body></html>";
	static IDManager idmanager = new IDManager();
	static ComInfoManager commanager = new ComInfoManager();
	
	public void doGet ( HttpServletRequest req , HttpServletResponse res )
	throws ServletException , IOException
	{
		HttpSession httpSession = req.getSession(true);

		res.setContentType("text/html");

		PrintWriter out = res.getWriter();

		/*if ( httpSession == null ) {
			out.println("<h1>Unauthorized access.</h1>");
			return;
		}*/

		/*Admin admin = (Admin)httpSession.getValue(id);
		if ( admin == null ) {
			out.println("<h1>Unauthorized access.</h1>");
			return;
		}*/
		
		//cmd받아오기
		String cmd = req.getParameter("cmd");
		if(cmd == null || cmd == ""){
			out.println("<h1>Invalid command</h1>");
			return;
		}

		//cmd 체크해서 해당 함수 호출하기
		System.out.println(cmd);
		if(cmd.equals("showComInfo")){
			commanager.showComInfo(req, out);
		}
	
		out.close();
	}

	public void doPost ( HttpServletRequest req , HttpServletResponse res )
	throws ServletException , IOException
	{
		//session 받아오기
		HttpSession httpSession = req.getSession(true);

		res.setContentType("text/html");

		
		PrintWriter out = res.getWriter();

    	//cmd받아오기
		String cmd = req.getParameter("cmd");
		if(cmd == null || cmd == ""){
			out.println("<h1>Invalid command</h1>");
			return;
		}

		//cmd 체크해서 해당 함수 호출하기
		try{
			if(cmd.equals("login")){
				showAdminMenu(out);
			}
			else if(cmd.equals("showIDReqList")){
				idmanager.showIDReqList(req,out);
			}
			else if(cmd.equals("showPersonInfo")){
				idmanager.showPersonInfo(req, out);
			}
			else if(cmd.equals("addMemberPerson")){
				idmanager.addMemberPerson(req, out);
			}
			else if(cmd.equals("deleteIDRequest")){
				idmanager.deleteIDRequest(req, out);
			}
			else if(cmd.equals("comChangeList")){
				commanager.comChangeList(req, out);
			}
			else if(cmd.equals("updateCompanyInfo")){
				commanager.updateCompanyInfo(req, out);
			}
			else if(cmd.equals("cancelCompanyInfo")){
				commanager.cancelCompanyInfo(req, out);
			}
		}catch( Exception e){
			out.println("command Error!"+e);
		}

		out.close();

	}

	/** admin menu 보여주는 폼 
	*/
	public void showAdminMenu( PrintWriter out )
	{
		/*res.setContentType("text/html");		

		//session 체크해서 어드민인지 확인
		HttpSession httpSession = req.getSession(false);
		
			
	
		if(httpSession == null)out.println("<h1>Unauthorized access.</h1>");
		
		String adminKey = (String)httpSession.getValue(httpSession.getId());
		if(!adminKey.equals("admin")){
			System.out.println("<h1>Unauthorized access</h1>");
		}else{*/
			out.println( htmlHeader );	
			out.println("<center><h1>admin menu</h1>");
			out.println("<table border=0>");
			out.println("<form method=post action=/namecard/AdminServlet>");	
			out.println("<input type=hidden name=cmd value=showIDReqList>");
			out.println("<tr align=center><td><input type=submit value=RequestID></td></form>");
			out.println("<form method=post action=/namecard/AdminServlet>");
			out.println("<input type=hidden name=cmd value=comChangeList>");		
			out.println("<td><input type=submit value=ComChange></td></form></tr>");
			out.println("</table></center>");
			out.println(htmlEnd);
		//}
	}
}
