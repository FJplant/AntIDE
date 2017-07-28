package com.antsoft.namecard.manager;

import com.antsoft.namecard.*;
import java.awt.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;


/**
 *  MemberInfoManager - Member Info manager
 *  
 *  This class manage member's information, Person Info and Job Info.
 */
class MemberInfoManager {
	
	static Asc2Uni asc = new Asc2Uni();
	static Uni2Asc uni = new Uni2Asc();

	/** dbmanager */
	static DBManager dbmanager = new DBManager();
		
	/** namecardsite */
	static NameCardSite site = new NameCardSite();

	static String head = "<html><body background=/bg.jpg>";
	static String end = "</body></html>";


	/**
	 *  constructor
	 */
	public MemberInfoManager() {
	}
	
	
	/**
	 *  updatePersonForm -  개인정보수정을 위한 폼을 보여주는 method
	 *
	 *  @param  req      폼에서 입력받은 데이터가 들어있는 곳
	 *  @param  out      결과를 출력할 곳
	 */
	public void updatePersonForm( HttpServletRequest req, PrintWriter out )  throws UnsupportedEncodingException {
		// 권한 검사
		HttpSession httpsession = req.getSession( false );
		if( httpsession == null ) {
			out.println( head );
			out.println( "<h1>Unauthorized User! </h1>" );
			out.println( end );
			return;
		}
		
		String id = (String)httpsession.getValue( "id" );
	
		ResultSet member = null;

		// httpsession에서 아이디를 얻어서 사용자에 대한 관련 정보를 얻는다.
		try {
			member = dbmanager.getMemberInfo( id );
		 	member.next();	
	
			out.println("<body background=/bg.jpg>");
			out.println("<table width=680 border=0 cellspacing=0>");
			out.println("<tr>");
			out.println("<td height=28 align=center><font color=#ffff00><img src=/icon.gif><b> " + uni.convert( "개인정보수정폼") +"</b></font></td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td align=center valign=top>");
			out.println("<br><p><br><p><table border=0 width=80% align=center cellspacing=0>");
			out.println("<tr><form method=post action=/namecard/UserServlet>");
			out.println("<input type=hidden name=cmd value=updatePersonInfo>"); 
			out.println("<td><font color=#fffacd size=2><b>ID</b></font></td>");
			out.println("<td><font size=2 color=#e0ffff><b>"+id+"</font></td>");
			out.println("</tr>");
			out.println("<td><font size=2 color=#fffacd ><b>" + uni.convert( "패스워드 " ) + "</b></font></td>");
			out.println("<td><input type=password name=passwd size=10 maxlength=10></td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td><font size=2 color=#fffacd><b>" + uni.convert("패스워드확인") + "</b></font></td>");
			out.println("<td><input type=password name=checkpass size=10 maxlength=10></td>");
			out.println("</tr>");
			out.println("<tr><td><font size=2 color=#fffacd><b>" + uni.convert( "이름" ) + "</b></font></td>");
			out.println("<td><font size=2 color=#e0ffff><b>"+member.getString( 3 )+"</font></td></tr>");
			out.println("<tr><td><font size=2 color=#fffacd><b>" + uni.convert( "주민등록번호" ) + "</b></font></td>");
			out.println("<td><font size=2 color=#e0ffff><b>"+member.getString( 4 )+"</font></td></tr>");
			out.println("<tr><td><font size=2 color=#fffacd><b> " + uni.convert( "생년월일" ) + "</b></font></td>");
			out.println("<td><font size=2 color=#e0ffff><b>"+member.getString( 5 )+"</font></td></tr>");
			out.println("<tr><td><font size=2 color=#fffacd><b>" + uni.convert( "성별" ) + "</b></font></td>");
			
			if(member.getString( 6 ).equals("m")) out.println("<td><font size=2 color=#e0ffff><b>" + uni.convert( "남" ) + "</font></td></tr>");
			else out.println("<td><font size=2 color=#e0ffff><b>" + uni.convert( "여" ) +"</font></td></tr>");
  		
			out.println("<tr><td><font size=2 color=#fffacd><b>" + uni.convert( "주소" ) + "</b></font></td>");
			out.println("<td><input type=text name=address size=40 value=\""+ member.getString( 7 ) +"\"></td></tr>");
			out.println("<tr> <td><font size=2 color=#fffacd><b>" + uni.convert( "전화" ) +"</b></font></td>");
	    	out.println("<td><input type=text name=tel size=12 value=\""+member.getString( 8 )+"\"> </td></tr>");
	  		out.println("<tr><td><font size=2 color=#fffacd><b>" + uni.convert( "삐삐" ) +"</b></font></td>");
	    	out.println("<td><input type=text name=beeper size=13 value=\""+member.getString( 9 )+"\"></td> </tr>");
			out.println("<tr><td><font size=2 color=#fffacd><b>" + uni.convert( "휴대폰" ) +"</b></font></td>");
	    	out.println("<td><input type=text name=phone size=13 value=\""+member.getString( 10 )+"\"></td> </tr>");
	  		out.println("<tr><td><font size=2 color=#fffacd><b>" + uni.convert( "홈페이지" )+"</b></font></td>");
	    	out.println("<td><input type=text name=homepage size=25 value=\""+member.getString( 11 )+"\"></td></tr>");
			out.println("<tr> <td><font size=2 color=#fffacd><b>e-mail</b></font></td>");
   		 	out.println("<td><input type=text name=email size=25 value=\""+member.getString( 12 )+"\"></td></tr>");
 	 		out.println("<tr> <td><font size=2 color=#fffacd><b>" + uni.convert( "지위" ) + "</b></font></td>");
	    	out.println("<td><input type=text name=position size=10 value="+member.getString( 13 )+"></td> </tr>");
			out.println("<tr> <td><font size=2 color=#fffacd><b>" + uni.convert( "부서" ) + "</b></font></td>");
	    	out.println("<td><input type=text name=part size=15 value=\""+member.getString( 14 )+"\"></td> </tr>");
	  		out.println("<tr> <td><font size=2 color=#fffacd><b>" + uni.convert( "회사개인전화" ) + "</b></font></td> ");
			out.println("<td> <input type=text name=j_tel size=13 value=\""+member.getString( 15 )+"\"></td> </tr>");
			out.println("<tr> <td><font size=2 color=#fffacd><b>" + uni.convert( "회사팩스" ) + "</b></font></td>");
	  	  	out.println("<td><input type=text name=j_fax size=13 value=\""+member.getString( 16 )+"\"><br><p></td> </tr>");
			out.println("<tr> <td align=right><br><p><input type=submit value=" + uni.convert( "등록" ) +"></form></td>");
			out.println("<form method=get action=/namecard/UserServlet><td align=left>");
			out.println("<input type=hidden name=cmd value=showDirList>");
			out.println("<input type=submit value=" + uni.convert( "취소" ) + "></td> </form></tr>");
	        out.println("</table></td> </tr> </table></center> </body> </html>");			
	
		} catch( SQLException e ) {
			out.println( head );
			out.println( e.toString() + uni.convert(" 다시 시도하세요" ));
			out.println( end );
			return;
		} catch( CommandException e ) {
			out.println( head );
			out.println( "DB와의 접속에 문제가 있습니다. 다시 시도하세요"  + e.toString() );
			out.println( end );
			return;
		}	
	}
	
	/**
	 *  showUpdatedPersonInfo - 수정된 개인정보를 보여주는 폼 
	 *
	 *  @param  req      폼에서 입력받은 데이터가 들어있는 곳
	 *  @param  out      결과를 출력할 곳
	 */
	public void showUpdatedPersonInfo( HttpServletRequest req, PrintWriter out ) throws UnsupportedEncodingException {
		// 권한 검사
		HttpSession httpsession = req.getSession( false );
		if( httpsession == null ) {
			out.println( head );
			out.println( "<h1>Unauthorized User! </h1>" );
			out.println( end );
			return;
		}
		
		String id = (String)httpsession.getValue( "id" );
		ResultSet member = null;
	
		// httpsession에서 아이디를 얻어서 사용자에 대한 관련 정보를 얻는다.
		try {
			member = dbmanager.getMemberInfo( id );
			member.next();
		
			out.println("<body background=/bg.jpg>");
			out.println("<table width=600 border=0 cellspacing=0>");
			out.println("<tr>");
			out.println("<td height=28 align=center><br><p><font color=#ffff00><img src=/icon.gif><b>" + uni.convert( "개인정보" ) + "</b></font></td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td align=center valign=top>");
			out.println("<br><p><table border=0 width=80% align=center cellspacing=0>");
			out.println("<td><font size=2 color=#fffacd><b>ID</b></font></td>");
			out.println("<td><font size=2><b>"+id+"</font></td>");
			out.println("</tr>");
			out.println("<tr><td height=20><font size=2 color=#fffacd><b>" + uni.convert( "이름" ) + "</b></font></td>");
			out.println("<td><font size=2><b>"+member.getString( 3 )+"</font></td></tr>");
			out.println("<td height=20><font size=2 color=#fffacd><b>"+ uni.convert( "패스워드" ) + "</b></font></td>" );
			out.println("<td><font size=2><b>"+member.getString("password")+"</font></td></tr>"); 
			out.println("<tr height=20><td><font size=2 color=#fffacd><b>" + uni.convert( "주민등록번호" ) + "</b></font></td>");
			out.println("<td><font size=2><b>"+member.getString(4 )+"</font></td></tr>");
			out.println("<tr><td height=20><font size=2 color=#fffacd><b>" + uni.convert( "생년월일" ) + "</b></font></td>");
			out.println("<td><font size=2><b>"+member.getString( 5 )+"</font></td></tr>");
			out.println("<tr><td height=20><font size=2 color=#fffacd><b>" + uni.convert( "성별" ) +"</b></font></td>");
			
			if(member.getString( 6 ).equals("m")) out.println("<td><font size=2><b>" + uni.convert( "남")+ "</font></td></tr>");
			else out.println("<td><font size=2><b>" + uni.convert( "여" ) +"</font></td></tr>");
 	 		
			out.println("<tr><td height=20><font size=2 color=#fffacd><b>" + uni.convert( "주소" ) + "</b></font></td>");
	    	out.println("<td><font size=2><b>"+member.getString( 7 ) +"</td></tr>");
			out.println("<tr> <td height=20><font size=2 color=#fffacd><b>" + uni.convert( "전화" ) + "</b></font></td>");
	    	out.println("<td><font size=2><b>"+member.getString( 8 )+" </td></tr>");
	  		out.println("<tr><td height=20><font size=2 color=#fffacd><b>" + uni.convert( "삐삐" ) + "</b></font></td>");
	    	out.println("<td><font size=2><b>"+member.getString( 9 )+"</td> </tr>");
			out.println("<tr><td height=20><font size=2 color=#fffacd><b>" + uni.convert( "휴대폰" ) + "</b></font></td>");
	    	out.println("<td><font size=2><b>"+member.getString( 10 )+"</td> </tr>");
	  		out.println("<tr><td height=20><font size=2 color=#fffacd><b>" + uni.convert( "홈페이지" ) + "</b></font></td>");
	    	out.println("<td><font size=2><b>"+member.getString( 11 )+"</td></tr>");
			out.println("<tr> <td height=20><font size=2 color=#fffacd><b>e-mail</b></font></td>");
	    	out.println("<td><font size=2><b>"+member.getString( 12 )+"</td></tr>");
	  		out.println("<tr> <td height=20><font size=2 color=#fffacd><b>" + uni.convert( "지위" ) + "</b></font></td>");
	    	out.println("<td><font size=2><b>"+member.getString( 13 )+"</td> </tr>");
			out.println("<tr><td height=20><font size=2 color=#fffacd><b>" + uni.convert( "부서" ) + "</b></font></td>");
	    	out.println("<td><font size=2><b>"+member.getString( 14 )+"</td> </tr>");
	  		out.println("<tr> <td height=20><font size=2 color=#fffacd><b>" + uni.convert( "회사개인전화" ) + "</b></font></td> ");
			out.println("<td><font size=2><b>"+member.getString( 15 )+"</td> </tr>");
			out.println("<tr> <td height=20><font size=2 color=#fffacd><b>" + uni.convert( "회사팩스" ) + "</b></font></td>");
	    	out.println("<td><font size=2>"+member.getString( 16 )+"<br><p></td> </tr>");
			out.println("<tr> <td colspan=2 align=center>");
			out.println("<a href=\"javaScript:window.history.go(-2);\"><font size=2 color=#ffffff><b><-Back</a><br><p></td></tr>");
	        out.println("</table></td></tr></table></center> </body> </html>");			
	
		} catch( SQLException e ) {
			out.println(head );
			out.println( e.toString() + uni.convert("다시 시도하세요" ) );
			out.println(end );
		 	return;
		} catch( CommandException e ) {
			out.println(head );
			out.println( e.toString() + uni.convert( "다시시도하세요" ) );
			out.println(end );
			return;
		}	
	}

	/** 
	 *  updatePersonInfo -  updatePersonInfo command를 처리하는 method
	 *  				    개인정보를 수정하는 method
	 *
	 *  @param  req      폼에서 입력받은 데이터가 들어있는 곳
	 *  @param  out      결과를 출력할 곳
	 *  @throws UnsupportedEncodingException 
	 */
	public void updatePersonInfo( HttpServletRequest req, PrintWriter out ) throws UnsupportedEncodingException {
		// 권한 검사
		HttpSession httpsession = req.getSession( false );
		if( httpsession == null ) {
			out.println(head );
			out.println( "<h1>Unauthorized User! </h1>" );
			out.println(end );
			return;
		}
				
		// req에서 파라메터 파싱한다.
		String s_passwd    = asc.convert( req.getParameter( "passwd" ) );
		String s_checkpass = asc.convert( req.getParameter( "checkpass" ) );
		String s_tel       = req.getParameter( "tel" );
		String s_beeper    = req.getParameter( "beeper" );
		String s_phone     = req.getParameter( "phone" );
		String s_email     = req.getParameter( "email" );
		String s_homepage  = req.getParameter( "homepage" );
		String s_address   = asc.convert(req.getParameter( "address" ));
		String s_j_tel     = req.getParameter( "j_tel" );
		String s_j_fax     = req.getParameter( "j_fax" );
		String s_part		 = asc.convert(req.getParameter( "part"  ));	
		String s_position  = asc.convert(req.getParameter( "position"  ));
 	
		String passwd    = "";
		String checkpass = "";
		String tel       = "";
		String beeper    = "";
		String phone     = "";
		String email     = "";
		String homepage  = "";
		String address   = "";
		String j_tel     = "";
		String j_fax     = "";
		String part		 = "";	
		String position  = "";
 		String d_tel     = "";
		String d_beeper  = "";
		String d_phone   = "";
		String d_email   = "";
		String d_homepage= "";
		String d_address = "";
		String d_j_tel   = "";
		String d_j_fax   = "";
		String d_part    = "";	
		String d_position= "";	

		//tokenizing 해서 space문자를 없애준다.
		StringTokenizer st = new StringTokenizer( s_passwd );
		while(st.hasMoreTokens())
			if( passwd == "")  passwd = st.nextToken();
			else passwd  = passwd  + st.nextToken();

		st = new StringTokenizer(s_checkpass);
		while(st.hasMoreTokens())
			if( checkpass== "") checkpass = st.nextToken();
			else checkpass = checkpass + st.nextToken();

		st = new StringTokenizer( s_tel);
		while(st.hasMoreTokens())
			if( tel== "") tel = st.nextToken();
			else tel =tel  + st.nextToken();

		st = new StringTokenizer( s_beeper );
		while(st.hasMoreTokens())
			if( beeper == "")  beeper = st.nextToken();
			else  beeper =  beeper + st.nextToken();

		st = new StringTokenizer( s_phone);
		while(st.hasMoreTokens())
			if( phone == "") phone  = st.nextToken();
			else phone  = phone  + st.nextToken();

	 	st = new StringTokenizer( s_email);
		while(st.hasMoreTokens())
			if( email == "")  email = st.nextToken();
			else  email = email  + st.nextToken();	

	 	st = new StringTokenizer(s_homepage);
		while(st.hasMoreTokens())
			if( homepage == "")  homepage = st.nextToken();
			else  homepage = homepage  + st.nextToken();	

 		st = new StringTokenizer( s_address);
		while(st.hasMoreTokens())
			if(address == "") address = st.nextToken();
			else address = address + st.nextToken();

		st = new StringTokenizer(s_j_tel);
		while(st.hasMoreTokens())
			if(j_tel == "") j_tel = st.nextToken();
			else j_tel = j_tel + st.nextToken();

		st = new StringTokenizer( s_j_fax);
		while(st.hasMoreTokens())
			if( j_fax== "")j_fax  = st.nextToken();
			else j_fax = j_fax + st.nextToken();

		st = new StringTokenizer( s_position );
		while(st.hasMoreTokens())
			if(position == "") position = st.nextToken();
			else  position= position + st.nextToken();

		st = new StringTokenizer( s_part );
		while(st.hasMoreTokens())
			if(part == "") part = st.nextToken();
			else part = part + st.nextToken();

		// httpsession에서 아이디를 얻는다.
		String id = (String)httpsession.getValue( "id" );
		ResultSet member = null;
		
		// 개인정보를 얻는다.
		try {
			member = dbmanager.getMemberInfo( id );
			member.next();	
		
			// DB에 들어있는 것을 parsing한다.
			st = new StringTokenizer( member.getString( 8 ));
			while(st.hasMoreTokens())
				if( d_tel== "") d_tel = st.nextToken();
				else d_tel =d_tel  + st.nextToken();
	
			st = new StringTokenizer( member.getString( "beeper") );
			while(st.hasMoreTokens())
				if( d_beeper == "")  d_beeper = st.nextToken();
				else d_beeper =  d_beeper + st.nextToken();
	
			st = new StringTokenizer(  member.getString("handphone"));
			while(st.hasMoreTokens())
				if( d_phone == "") d_phone  = st.nextToken();
				else d_phone  = d_phone  + st.nextToken();
	
		 	st = new StringTokenizer( member.getString( "email")  );
			while(st.hasMoreTokens())
				if( d_email == "")  d_email = st.nextToken();
				else  d_email = d_email  + st.nextToken();	
	
		 	st = new StringTokenizer(member.getString( "homepage") );
			while(st.hasMoreTokens())
				if( d_homepage == "")  d_homepage = st.nextToken();
				else  d_homepage = d_homepage  + st.nextToken();	
	
 			st = new StringTokenizer( member.getString("address"));
			while(st.hasMoreTokens())
				if(d_address == "") d_address = st.nextToken();
				else d_address = d_address + st.nextToken();
	
			st = new StringTokenizer( member.getString( 15 ) );
			while(st.hasMoreTokens())
				if(d_j_tel == "") d_j_tel = st.nextToken();
				else d_j_tel = d_j_tel + st.nextToken();
	
			st = new StringTokenizer(member.getString( "fax") );
			while(st.hasMoreTokens())
				if( d_j_fax== "")d_j_fax  = st.nextToken();
				else d_j_fax = d_j_fax + st.nextToken();
	
			st = new StringTokenizer( member.getString( "posi") );
			while(st.hasMoreTokens())
				if( d_position == "")  d_position = st.nextToken();
				else   d_position=  d_position + st.nextToken();
	
			st = new StringTokenizer( member.getString( "part" ) );
			while(st.hasMoreTokens())
				if( d_part == "")  d_part = st.nextToken();
				else  d_part =  d_part + st.nextToken();
		} catch( SQLException e ) {
			out.println(head );
			out.println( e.toString() + uni.convert(" 다시 시도하세요" ));
			out.println(end );
			return;
		} catch( Exception e ) {
			out.println(head );
			out.println( uni.convert( "DB와의 연결에 문제가 있습니다. 다시 시도하세요" ) + e.toString() );
			out.println(end );
			return;
		}	
		boolean changed = false;

		// 패스워드 변경
		try {	
			if( passwd != "" ) {
				if( passwd.equals( checkpass ) ) {
					dbmanager.updatePassword( id, uni.convert(passwd) );
					changed = true;
				} else {
					out.println(head );
					out.println( uni.convert( " 패스워드를 다시 입력하세요"  ));
					out.println(end );
					return;
				}
			}
			// person info중 변경된 사항이 하나라도 있으면 update한다.
			if( ( ( address != "" ) && !uni.convert(address).equals( d_address ) )  || 
				( ( tel != "" ) && !tel.equals( d_tel ) )  || 
				( ( beeper != "" ) && !beeper.equals( d_beeper ) ) || 
				( ( phone != "" ) && !phone.equals( d_phone) )  || 
				( ( email != "" ) && !email.equals( d_email) ) || 
				( ( homepage != "" ) && !homepage.equals( d_homepage) ) ) {
				dbmanager.updatePersonInfo( id, uni.convert(s_address), tel, beeper, s_phone, email, homepage );
				changed = true;
			}
		
			// job info중 변경된 사항이 하나라도 있으면 update한다.
			if( ( ( j_tel != "" ) && !j_tel.equals( d_j_tel ) ) || 
				( ( j_fax != "" ) && !j_fax.equals( d_j_fax ) ) || 
				( ( position != "" ) && !uni.convert(position).equals( d_position ) ) || 
				( ( part != "" ) && !uni.convert(part).equals( d_part ) ) ) {
				dbmanager.updateJobInfo( id, j_tel, j_fax, uni.convert(position), uni.convert(part) );
				changed = true;
			}

			if( changed )showUpdatedPersonInfo( req, out );
			else {
				out.println(head );
				out.println( uni.convert( "<center>수정할 정보가 없습니다. 확인하시고 다시 입력하세요<br>" ) );
				out.println("<br><a href=\"javaScript:window.history.go(-1);\"><font size=2 color=#ffffff><b><-Back</a><br><p></center>");
				out.println(end );
			}	
		} catch( CommandException e ) {
			out.println(head );
			out.println( e.toString() + uni.convert("다시 시도하세요" ));
			out.println(end );
			return;
		}
		
	}
	
	
	/**
	 *  jobChangeForm - jobChangeForm command 처리하는 method
	 *                  회사이전을 위한 폼을 보여준다.
	 *
	 *  @param  req  폼에서 입력받은 데이터가 들어있는 곳
	 *  @param  out  결과를 출력할 곳
	 */
	public void jobChangeForm( HttpServletRequest req, PrintWriter out )  throws UnsupportedEncodingException {
		// 권한 검사
		HttpSession httpsession = req.getSession( false );
		if( httpsession == null ) {
			out.println(head );
			out.println( "<h1>Unauthorized User! </h1>" );
			out.println(end );
			return;
		}
		String id = (String)httpsession.getValue("id");
		ResultSet member = null;
		
		// memberPerson을 얻는다.
		try {
			member = dbmanager.getMemberPerson( id );
			member.next();
		
		
			// 입력폼을 보여준다.
			out.println( "<html><head><title></title></head>");
			out.println("<body background=/bg.jpg><table border=0 width=600 cellspacing=0>");
			out.println("<tr><td align=center><br><p>");
			out.println( "<form method=post action=/namecard/UserServlet> " );
			out.println( "<input type=hidden name=cmd value=jobChangeCheck>" );
			out.println( "<img src=/icon.gif><font size=4 color=#ffff00><b>" + uni.convert( "회사이전" ) +"</font></td></tr>" );
			out.println("<tr><td align=center><br><p><font size=2 color=#e0ffff>");
			out.println( uni.convert("기존 회사인지 검색을 합니다. *표 사항을 꼭 채워주시기 바랍니다.</td></tr>"));
			out.println("<tr><td align=center><br><p>");
			out.println("<table border=0 width=90% align=center cellspacing=0>");
			
			out.println("<tr><td><font size=2 color=#fffacd><b></td>");
			out.println("<td><font size=2 color=#fffacd><b>ID</b></font></td>");
			out.println("<td><font size=2><b>"+id+"</td></tr>"); 
			out.println("<tr><td><font size=2 color=#fffacd><b></b></font></td>");
			out.println("<td><font size=2 color=#fffacd><b>" + uni.convert( "이름" ) +"</b></font></td>");
			out.println("<td><font size=2><b>"+ member.getString( "name" ) +"</td></tr>");
			out.println("<tr><td align=right><font size=2 color=#fffacd><b>*</td>");
			out.println("<td><font size=2 color=#fffacd><b>" + uni.convert( "회사이름" ) + "</b></font></td>");
			out.println("<td><input type=text name=name size=20></td></tr>");
			out.println( "<tr><td align=right><font size=2 color=#fffacd><b>*</td>");
			out.println( "<td><font size=2 color=#fffacd><b>" + uni.convert( "지역" ) + "<td><select name=region size=1>");
			out.println("<option value=1>" + uni.convert( "서울특별시" ) + "</option><option value=2>" + uni.convert( "부산광역시" ) + "</option>");
			out.println("<option value=3>" + uni.convert( "대구광역시" ) + "</option><option value=4>" + uni.convert( "인천광역시" ) + "</option>");
			out.println("<option value=5>" + uni.convert( "광주광역시" ) +"</option><option value=6>" + uni.convert( "울산광역시" ) + "</option>");
			out.println( "<option value=7>" + uni.convert( "대전광역시" ) + "</option><option value=8>" + uni.convert( "경기" ) + "</option>");
			out.println( "<option value=9>" + uni.convert( "강원" ) + "</option><option value=10>" + uni.convert( "충북" ) + "</option>");
			out.println( "<option value=11>" + uni.convert( "충남" ) + "</option><option value=12>" + uni.convert( "전북" ) + "</option>");
			out.println( "<option value=13>" + uni.convert( "전남" ) + "</option><option value=14>" + uni.convert( "경북" ) + "</option>");
			out.println( "<option value=15>" + uni.convert( "경남") + "</option><option value=16>" + uni.convert( "제주" ) + "</option>");
			out.println( "</select> </td></tr>");
			out.println( "<tr><td><font size=2 color=#fffacd><b></td>");
			out.println( "<td><font size=2 color=#fffacd><b>" + uni.convert( "지점" ) + "</b></td><td><input type=text name=site size=18></td>");
			out.println( "<tr><td align=right><font size=2 color=#fffacd><b>*</td>");
			out.println( "<td><font size=2 color=#fffacd><b>" + uni.convert( "업종" ) + "</b></font></td>");
			out.println( "<td><select name=kind size=1>");
			out.println( "<option value=1>" + uni.convert( "공무원" ) + "</option>");
			out.println( "<option value=2>" + uni.convert( "교원" ) + "</option>");
			out.println( "<option value=3>" + uni.convert( "의료" ) + "</option>");
	 		out.println( "<option value=4>" + uni.convert( "유통" ) + "</option>");
			out.println( "<option value=5>" + uni.convert( "기계/자동차" ) + "</option>");
			out.println( "<option value=6>" + uni.convert( "컴퓨터/통신" ) + "</option>");
			out.println( "<option value=7>" + uni.convert( "서비스업" ) + "</option>");
		  	out.println( "<option value=8>" + uni.convert( "금융" ) + "</option>");
		  	out.println( "<option value=9>" + uni.convert( "건축/인테리어" ) + "</option>");
			out.println( "<option value=10>" + uni.convert( "방송" ) + "</option>");
			out.println( "<option value=11>" + uni.convert( "예술" ) + "</option>");
			out.println( "<option value=12>" + uni.convert( "무역" ) + "</option>");
			out.println( "<option value=13>" + uni.convert( "스포츠" ) + "</option>");
			out.println( "<option value=14>" + uni.convert( "프리랜서" ) + "</option>");
			out.println( "<option value=15>" + uni.convert( "기타" ) + "</option>");
			out.println( "</select></td></tr>"); 
			out.println( "<tr><td colspan=3 align=center><br><p>");
			out.println( "<input type=submit value=" + uni.convert( "검색" ) + ">");
			out.println( "<input type=button value=" + uni.convert( "취소" ) + " onClick=\"window.history.go(-1);\"></td></tr>" );
			out.println( "</table></body></html>" );
		} catch( SQLException e ) {
			out.println( head );
			out.println( e.toString() + uni.convert(" 다시 시도하세요" ) );
			out.println( end );
			return;
	
		} catch( CommandException e ){
			out.println( head );
			out.println( e.toString() );
			out.println( end );
			return;
		}	catch( Exception e ) {
			out.println( head );
			out.println( e.toString() );
			out.println( end );
		}	
	}	
	
	/**
	 *  jobChangeCheck -  회사이전을 위한 폼을 보여주는 method
	 *
	 *  @param  req      폼에서 입력받은 데이터가 들어있는 곳
	 *  @param  out      결과를 출력할 곳
	 */
	public void jobChangeCheck( HttpServletRequest req, PrintWriter out ) throws UnsupportedEncodingException {
		// 권한 검사
		HttpSession httpsession = req.getSession( false );
		if( httpsession == null ) {
			out.println( head );
			out.println( "<h1>Unauthorized User! </h1>" );
			out.println( end );
			return;
		}
		
		String id = (String)httpsession.getValue( "id" );
		
		// 파라메터 파싱한다.
		String kind   = req.getParameter( "kind" );
		String s_name = asc.convert(req.getParameter( "name" ));
		String region = req.getParameter( "region" );
		String s_site = asc.convert(req.getParameter( "site" ));

		String name= "";
		String c_site = "";

		//tokenizing 해서 space문자를 없애준다.
		StringTokenizer st1 = new StringTokenizer(s_name);
		while(st1.hasMoreTokens())
			if(name == "") name = st1.nextToken();
			else name = name + st1.nextToken();

		StringTokenizer st3 = new StringTokenizer(s_site);
		while(st3.hasMoreTokens())
 			if(c_site == "") c_site = st3.nextToken();
			else c_site = c_site + st3.nextToken(); 
		
		if(  name.equals( "" ) ) {
			out.println( head );
			out.println(uni.convert( "<center><br><br><font size=2 color=white> 입력이 부족합니다. 다시 시도하십시요.<br><br>" ));
			out.println("<a href=\"javaScript:window.history.go(-1);\"><font size=2><b><-back</a></center>");
			out.println( end );
			return;
		}
		
		ResultSet member = null;
		ResultSet company = null;
		String com_id = null;
		boolean com = false;		
		// httpsession에서 아이디를 얻어서 사용자에 대한 관련 정보를 얻는다.
		try {
			member = dbmanager.getJobChange( id );
			if( !member.next()) out.println( "member is null" );	

			// 현재 근무하고 있는 회사와 비교한다.
			if( uni.convert(name).equals( member.getString( 4 ) ) &&  (Integer.parseInt( region ) == member.getInt( "region" )  ) &&  (Integer.parseInt( kind ) == member.getInt( "kind" ) ) && uni.convert( c_site ).equals( member.getString( "site" ) ) ) {
				out.println( head );
				out.println( uni.convert( "현재 근무하고 있는 회사입니다. 이전할 회사의 이름과 정보를 입력하세요" ) );
				out.println( end );
				return;
			} 

			company = dbmanager.searchCompany( uni.convert(name), Integer.parseInt( region ), uni.convert( c_site) );
			if( company.next() ) {
				com = true;
				com_id = company.getString( "id" );
			}
		} catch( SQLException e ){
			out.println( head );
			out.println( e.toString() + uni.convert(" 다시 시도하세요" ));
			out.println( end );
			return;	
		} catch( CommandException e ) {
			out.println( head );
			out.println( uni.convert("DB와의 연결에 문제가 있습니다 다시 시도하세요") + e.toString() );
			out.println( end );
			return;	
		} catch( Exception e ){
			out.println( head );
			out.println( e.toString() );
			out.println( end );
			return;
		} 

		String kind_num = kind;
		String region_num = region;
	
		// selection에서 입력을 받아서 바꾼다.
		if( !region.equals( "0" ) ) {
			if( region.equals( "1" ) )  region = "서울특별시" ;	
			if( region.equals( "2" ) )  region = "부산광역시";
			if( region.equals( "3" ) )  region = "대구광역시";
			if( region.equals( "4" ) )  region = "인천광역시";
			if( region.equals( "5" ) )  region = "광주광역시";
			if( region.equals( "6" ) )  region = "울산광역시";
			if( region.equals( "7" ) )  region = "대전광역시";
			if( region.equals( "8" ) )  region = "경기";
			if( region.equals( "9" ) )  region = "강원";;	
			if( region.equals( "10" ) ) region = "충북" ;
			if( region.equals( "11" ) ) region = "충남";
			if( region.equals( "12" ) ) region = "전북";		
			if( region.equals( "13" ) ) region = "전남";
			if( region.equals( "14" ) ) region = "경북";
			if( region.equals( "15" ) ) region = "경남";
			if( region.equals( "16" ) ) region = "제주";
		}
		
		if( kind != null ) {
			if( kind.equals( "1" ) )  kind = "공무원";
			if( kind.equals( "2" ) )  kind = "교원";
			if( kind.equals( "3" ) )  kind = "의료";
			if( kind.equals( "4" ) )  kind = "유통";
			if( kind.equals( "5" ) )  kind = "기계/자동차";
			if( kind.equals( "6" ) )  kind = "컴퓨터/통신";
			if( kind.equals( "7" ) )  kind = "서비스업";
			if( kind.equals( "8" ) )  kind = "금융";
			if( kind.equals( "9" ) )  kind = "건축/인테리어";
			if( kind.equals( "10" ) ) kind = "방송";
			if( kind.equals( "11" ) ) kind = "예술";
			if( kind.equals( "12" ) ) kind = "무역";
			if( kind.equals( "13" ) ) kind = "스포츠";
			if( kind.equals( "14" ) ) kind = "프리랜서";
			if( kind.equals( "15" ) ) kind = "기타" ;
		}			
		// 입력폼을 보여준다.
		out.println("<html><head><title></title></head>");
		out.println("<body background=/bg.jpg><form method=post action=/namecard/UserServlet>" );
		out.println( "<input type=hidden name=cmd value=jobChange>" );
		out.println( "<input type=hidden name=name value=\"" + uni.convert(name) + "\">" );
		out.println( "<input type=hidden name=kind value=\"" + kind_num + "\">" );
		out.println( "<input type=hidden name=region value=" + region_num + ">" );
		out.println( "<input type=hidden name=site value=\"" + uni.convert(c_site) + "\">" );
		
		try {	
			out.println("<table border=0 cellspacing=0 width=600>");
			out.println("<tr><td align=center><br><p><img src=/icon.gif><font size=4 color=#ffff00><b>" + uni.convert( "회사이전" ) + "</td></tr>" );
			if( !com ) out.println("<tr><td align=center><br><p><font size=2 color=#e0ffff>" + uni.convert( "검색한 회사가 없습니다. 아래 폼을 채워주십시오.( *사항은 반드시 기입해 주십시오. )" ) + "</font></td></tr>");	
			out.println( "<tr><td align=center><br><p>");
			out.println("<table border=0 cellspacing=0 width=80%>" );
			out.println( "<tr><td></td><td><font size=2 color=#fffacd><b>ID </td><td><font size=2 color=#e0ffff><b>" + id + "</td></tr>" );
			out.println( "<tr><td></td><td><font size=2 color=#fffacd><b>" + uni.convert( "이름" ) + "</tr><td><font size=2 color=#e0ffff><b>" + member.getString( 2 ) + "</td></tr>" );
			out.println( "<tr><td></td><td><font size=2 color=#fffacd><b>" + uni.convert( "회사이름" ) + "</td><td><font size=2 color=#e0ffff><b>" + uni.convert(name) + "</td></tr> ");
			out.println( "<tr><td></td><td><font size=2 color=#fffacd><b>" + uni.convert( "지역" ) + " </td><td><font size=2><b>" + uni.convert(region) + "</td></tr> " );
			if( !c_site.equals( "" ) ) 	out.println( "<tr><td><font size=2 color=#fffacd><b>" + uni.convert( "지점" ) + "</td><td><font size=2 color=#e0ffff><b>" + uni.convert(c_site) + "</td></tr>");
			out.println( "<tr><td></td><td><font size=2 color=#fffacd><b>" + uni.convert( "업종" ) + "</b></font></td><td><font size=2 color=#e0ffff><b>" + uni.convert(kind) + "</td></tr>" );
				
			if( com ) {     // 이미 존재하는 회사인 경우
				httpsession.putValue( "com", com_id );
				httpsession.putValue( "exist", "true" );
				if( !company.getString( "cominfo" ).equals( "none" ) ) {
					company = dbmanager.getComInfo( com_id );
					if( company.next( ) ) {	
						httpsession.putValue( "cominfo", "true" );
					}
					out.println( "<tr><td></td><td><font size=2 color=#fffacd><b>" + uni.convert( "전화" ) + " </td><td><font size=2 color=#e0ffff><b>" + member.getString( 6  ) + "</td></tr>" );
					out.println( "<tr><td></td><td><font size=2 color=#fffacd><b>" + uni.convert( "팩스" ) + " </td><td><font size=2 color=#e0ffff><b>" + member.getString( 7 ) + "</td></tr>" );
					out.println( "<tr><td></td><td><font size=2 color=#fffacd><b>" + uni.convert( "이메일" ) + " </td><td><font size=2 color=#e0ffff><b>" + member.getString( 8 ) + "</td></tr>" );
					out.println( "<tr><td></td><td><font size=2 color=#fffacd><b>" + uni.convert( "홈페이지" ) + " </td><td><font size=2 color=#e0ffff><b>" + member.getString( 9 ) + "</td></tr>" );
					out.println( "<tr><td></td><td><font size=2 color=#fffacd><b>" + uni.convert( "주소" ) + " </td><td><font size=2 color=#e0ffff><b>" + member.getString(  10 ) + "</td></tr>" );
					out.println("<input type=hidden name=tel value=\""+member.getString( 6 )+"\">");
					out.println("<input type=hidden name=fax value=\""+member.getString(  7 )+"\">");
					out.println("<input type=hidden name=email value=\""+member.getString(  8 )+"\">");
					out.println("<input type=hidden name=homepage value=\""+member.getString(  9 )+"\">");
					out.println("<input type=hidden name=address value=\""+member.getString( 10 )+"\">");
				} else {
					httpsession.putValue( "cominfo", "false" );

					out.println( "<tr><td align=right><font size=2 color=#fffacd><b>*</td><td><font size=2 color=#fffacd><b>" + uni.convert( "전화" ) + " </td><td><input type=text name=tel></td></tr>" );
					out.println( "<tr><td></td><td><font size=2 color=#fffacd><b>" + uni.convert( "팩스" ) + " </td><td><input type=text name=fax></td></tr>" );
					out.println( "<tr><td></td><td><font size=2 color=#fffacd><b>" + uni.convert( "이메일" ) + " </td><td><input type=text name=email></td></tr>" );
					out.println( "<tr><td></td><td><font size=2 color=#fffacd><b>" + uni.convert( "홈페이지" ) + " </td><td><input type=text name=homepage></td></tr>" );
					out.println( "<tr><td align=right><font size=2 color=#fffacd><b>*</td><td><font size=2 color=#fffacd><b>" + uni.convert( "주소") + " </td><td><input type=text name=address></td></tr>" );
				}
	
			} else { // 새로운 회사인 경우
				httpsession.putValue( "exist", "false" );
				 	
				out.println( "<tr><td align=right><font size=2 color=#fffacd><b>*</td><td><font size=2 color=#fffacd><b>" + uni.convert( "전화" ) + " </td><td><input type=text name=tel></td></tr>" );
				out.println( "<tr><td></td><td><font size=2 color=#fffacd><b>" + uni.convert( "팩스" ) + " </td><td><input type=text name=fax></td></tr>" );
				out.println( "<tr><td></td><td><font size=2 color=#fffacd><b>" + uni.convert( "이메일" ) + " </td><td><input type=text name=email></td></tr>" );
				out.println( "<tr><td></td><td><font size=2 color=#fffacd><b>" + uni.convert( "홈페이지" ) + " </td><td><input type=text name=homepage></td></tr>" );
				out.println( "<tr><td align=right><font size=2 color=#fffacd><b>*</td><td><font size=2 color=#fffacd><b>" + uni.convert( "주소") + " </td><td><input type=text name=address></td></tr>" );
			}
		
			out.println( "<tr><td></td><td><font size=2 color=#fffacd><b>" + uni.convert( "부서" ) + " </td><td><input type=text name=part></td></tr>" );
			out.println( "<tr><td align=right><font size=2 color=#ffffacd><b>*</td><td><font size=2 color=#fffacd><b>" + uni.convert( "직책" ) + " </td><td><input type=text name=position></td></tr>" );  	
			out.println( "<tr><td align=right><font size=2 color=#fffacd><b>*</td><td><font size=2 color=#fffacd><b>" + uni.convert( "부서전화" ) + " </td><td><input type=text name=j_tel></td></tr>" );
			out.println( "<tr><td></td><td><font size=2 color=#fffacd><b>" + uni.convert( "부서팩스" ) + " </td><td><input type=text name=j_fax ></td></tr>" );
			out.println( "<tr><td colspan=3 align=center><br><p><input type=submit value=" + uni.convert( "수정" ) + ">" );
			out.println( "<input type=button value=" + uni.convert( "취소" ) + " onClick=\"window.history.go(-2);\"></td></tr>" );
			out.println("</table></form></body></html>");
		} catch( SQLException e ) {
			out.println( head );
			out.println( e.toString() );
			out.println( end );
			return;
		} catch( CommandException e ){
			out.println( head );
			out.println( e.toString() );
			out.println( end );
			return;
		}catch( Exception e ){
			out.println( head );
			out.println( e.toString() );
			out.println( end );
			return;
		}	
	}
		
		
	/** 
	 *  jobChange -  jobChange command를 처리하는 method
	 *               회사정보를 수정한다.
	 *
	 *  @param  req      폼에서 입력받은 데이터가 들어있는 곳
	 *  @param  out      결과를 출력할 곳
	 */
	public void jobChange( HttpServletRequest req, PrintWriter out )throws UnsupportedEncodingException  {
		// 권한 검사
		HttpSession httpsession = req.getSession( false );
		if( httpsession == null ) {
			out.println( head );
			out.println( "<h1>Unauthorized User! </h1>" );
			out.println( end );
			return;
		}
				
		// req에서 파라메터 파싱한다.
		String kind        = req.getParameter( "kind" );
		String s_c_name    = asc.convert(req.getParameter( "name" ));
		String region      = req.getParameter( "region" );
		String s_site      = asc.convert(req.getParameter( "site" ));
		String s_tel       = req.getParameter( "tel" );
		String s_fax       = req.getParameter( "fax" );
		String s_email     = req.getParameter( "email" );
		String s_homepage  = req.getParameter( "homepage" );
		String s_address   = asc.convert(req.getParameter( "address" ));
		String s_j_tel     = req.getParameter( "j_tel" );
		String s_j_fax     = req.getParameter( "j_fax" );
		String s_position  = asc.convert(req.getParameter( "position" ));
		String s_part      = asc.convert( req.getParameter( "part" ) );
		String c_name = "";
		String c_site = "";
		String tel = "";
		String fax = "";
		String email = "";
		String homepage = "";
		String address = "";
		String j_tel = "";
		String j_fax = "";
		String position = "";
		String part = "";
		
		//tokenizing 해서 space문자를 없애준다.
		StringTokenizer st = new StringTokenizer(s_c_name);
		while(st.hasMoreTokens())
			if(c_name == "") c_name = st.nextToken();
			else c_name = c_name + st.nextToken();

		st = new StringTokenizer(s_site);
		while(st.hasMoreTokens())
 			if(c_site == "") c_site = st.nextToken();
			else c_site = c_site + st.nextToken(); 


		st = new StringTokenizer(s_tel);
		while(st.hasMoreTokens())
 			if(tel == "") tel = st.nextToken();
			else tel = tel + st.nextToken(); 

		st = new StringTokenizer(s_fax);
		while(st.hasMoreTokens())
 			if(fax == "") fax = st.nextToken();
			else fax = fax + st.nextToken(); 

		st = new StringTokenizer(s_email);
		while(st.hasMoreTokens())
 			if(email == "") email = st.nextToken();
			else email = email + st.nextToken(); 
		
		st = new StringTokenizer(s_homepage);
		while(st.hasMoreTokens())
 			if(homepage == "") homepage = st.nextToken();
			else homepage = homepage + st.nextToken(); 

		st = new StringTokenizer(s_address);
		while(st.hasMoreTokens())
 			if(address == "") address = st.nextToken();
			else address = address + st.nextToken(); 

		st = new StringTokenizer(s_j_tel);
		while(st.hasMoreTokens())
 			if(j_tel == "") j_tel = st.nextToken();
			else j_tel = j_tel + st.nextToken(); 

		st = new StringTokenizer(s_j_fax);
		while(st.hasMoreTokens())
 			if(j_fax == "") j_fax = st.nextToken();
			else j_fax = j_fax + st.nextToken(); 

		st = new StringTokenizer(s_part);
		while(st.hasMoreTokens())
 			if(part == "") part = st.nextToken();
			else part = part + st.nextToken(); 

		st = new StringTokenizer(s_position);
		while(st.hasMoreTokens())
 			if(position == "") position = st.nextToken();
			else position = position + st.nextToken(); 	

		if(  c_name.equals("" )  ||  address.equals( "" ) || tel.equals( "" ) ||  
				j_tel.equals("" ) ||  position.equals("" ) ) {
			out.println( head );
			out.println( "<center><br><br><font size=2 color=white> 입력이 부족합니다. 다시 시도하십시요.<br><br>" );
		out.println("<a href=\"javaScript:window.history.go(-1);\"><font size=2><b><-back</a></font></center>"); 
			out.println( end );
			return;
		}
	
		// httpsession에서 아이디를 얻는다.
		String id    = (String)httpsession.getValue( "id" );
		String exist = (String)httpsession.getValue( "exist" );

		try { 	
			Class.forName( NameCardSite.DRIVER );
		} catch( ClassNotFoundException e) {
			out.println( head );
	    	out.println( "ClassNotFoundException: " + e.toString() );
			out.println( end );
			return;
		}
	 
		Connection con = null;
		Statement stmt = null;
		ResultSet member = null;
		ResultSet newcom = null;
		ResultSet company = null;
		String current = null;
		String message = null;
 
		// DB connection열기
	    try {
			con = (Connection)DriverManager.getConnection( NameCardSite.URL, NameCardSite.ID, NameCardSite.PASSWD );
			stmt = con.createStatement();
			con.setAutoCommit( false );
		} catch( SQLException e ) {
			out.println( " fail to connect DB " + e.toString() );
			return;
		}

		try{	
			message = "fail to get MemberInfo in query1 " ;
			// 개인정보를 얻는다.
			String query1 = "select MemberPerson.com_id, JobInfo.kind, " +
					" MemberPerson.jobinfo, Company.employee,MemberPerson.name " +
					" from  MemberPerson, Company, JobInfo  " +
					" where MemberPerson.id='" + id + "'" +
					" and JobInfo.id=MemberPerson.jobinfo " +
					" and Company.id=MemberPerson.com_id;"; 
			member = stmt.executeQuery( query1 );	
			member.next();

			String num = null;   // 새로운 회사의 아이디	
			// 회사가 존재하는 경우
			if( exist.equals( "true" ) ) {
				num =  (String)httpsession.getValue( "com" ); 	
				String infonum = (String)httpsession.getValue( "cominfo" );

				// cominfo가 없으면 새로 마든다.
				if( infonum.equals("false"  )) {
					message = "fail to add ComInfo in query2 ";
					// object생성 시간 입력
					Calendar date1 = Calendar.getInstance();
			
					String time1 = String.valueOf(date1.get(date1.YEAR)) + String.valueOf(date1.get(date1.MONTH)) + String.valueOf(date1.get(date1.DATE)) + String.valueOf(date1.get(date1.HOUR)) + String.valueOf(date1.get(date1.MINUTE)) + String.valueOf(date1.get(date1.SECOND));
					// 회사 아이디 세팅
					String cominfo = time1 + id;

					// 새로운 회사정보를 만들어서 등록시킨다.
					String query2 = "insert into ComInfo " +
							" (id,address,tel,fax,homepage,email,back) " +
							"values('" +cominfo+"','"+uni.convert(s_address)+"','"+
							tel+"','"+fax+"','"+
							homepage+"','"+email+"','0');";	
					stmt.executeUpdate( query2 );		

					message = "fail to add Column ComInfo in query3";
					String query3 = "update Company set cominfo='" + cominfo +
									"' where id='" + num + "';";
					stmt.executeUpdate( query3 );
				} else {
				}
			} else {
				message = "fail to add ComInfo in query2 ";
				// object생성 시간 입력
				Calendar date1 = Calendar.getInstance();
		
				String time1 = String.valueOf(date1.get(date1.YEAR)) + String.valueOf(date1.get(date1.MONTH)) + String.valueOf(date1.get(date1.DATE)) + String.valueOf(date1.get(date1.HOUR)) + String.valueOf(date1.get(date1.MINUTE)) + String.valueOf(date1.get(date1.SECOND));
				// 회사 아이디 세팅
				String cominfo = time1 + id;

				// 새로운 회사정보를 만들어서 등록시킨다.
				String query2 = "insert into ComInfo " +
							" (id,address,tel,fax,homepage,email,back) " +
							"values('" +cominfo+"','"+uni.convert(s_address)+"','"+
							tel+"','"+fax+"','"+
							homepage+"','"+email+"','0');";	
				stmt.executeUpdate( query2 );	

				message = "fail to add Company in query3";
				// object생성 시간 입력
				Calendar date2 = Calendar.getInstance();
				String time2 = String.valueOf(date2.get(date2.YEAR)) + String.valueOf(date2.get(date2.MONTH)) + String.valueOf(date2.get(date2.DATE)) + String.valueOf(date2.get(date2.HOUR)) + String.valueOf(date2.get(date2.MINUTE)) + String.valueOf(date2.get(date2.SECOND));
				// 회사 아이디 세팅
				num = id + time2;
				//query statement
				String query3 = "insert into Company " +
								" (id, name, site, region, cominfo, employee) " +
								"values ('" + num + "', '" + uni.convert(c_name) + "', '" + 
								uni.convert(c_site) + "', " + 
								region + ", '" + cominfo + "', '0');";
				stmt.executeUpdate( query3 );
			} // end of if-else  - 회사생성
				
			// 기존의 회사 직원 리스트에서 지운다.
			message = "fail to get Company for increasing employee number in query4 ";
			// company를 조사한다.
			String query4 = "select employee, cominfo from Company where id='" + member.getString( "com_id" ) + "';";
			ResultSet com = stmt.executeQuery( query4 );
			com.next();
			int employee = com.getInt( "employee" );

			// 직원이 1명뿐이면 회사도 삭제한다.
			if( employee == 1 ) {
				ResultSet cominfo = null;
				String query5 = null;
				String query6 = null;

				// connection에서 statement를 생성해서 query한다.
				message = "fail to get ComInfo for deleting ComInfo in query5";
				String back = com.getString( "cominfo" );

				while( !back.equals( "0" ) ) {			
					// com을 아이디로 하는 cominfo의 back을 얻는다. 
					query5 = "select back from ComInfo where id='" + back + "';";
					cominfo = stmt.executeQuery( query5 );
					cominfo.next();

					// cominfo를 삭제한다.	
					query6 = "delete from ComInfo where id='" + back + "';";
					stmt.executeUpdate( query6 ); 
					back = cominfo.getString( "back" ); 
				}	
				
				message = "fail to delete Company in query7";
				// Company를 삭제한다.
				String query7 = "delete from Company where id='" + member.getString( "com_id" ) + "';";
				stmt.executeUpdate( query7 );
			} else { 
				message = "fail to update employee number in query8";
				// company에서 직원수를 얻어와서 1 감소시킨다.
				employee = employee - 1;

				String query8 = "update Company set employee='" + employee + "' where id='" + member.getString( "com_id" )  + "';"; 
				stmt.executeUpdate( query8 ); 	
			} // end of if-else  - employee update
			
			// 새로운 직장 정보를 등록한다.	
			message = "fail to add JobInfo in query9";
			current = member.getString( "jobinfo" );
	
			// object생성 시간 입력
			Calendar date3 = Calendar.getInstance();
			String time3 = String.valueOf(date3.get(date3.YEAR)) + String.valueOf(date3.get(date3.MONTH)) + String.valueOf(date3.get(date3.DATE)) + String.valueOf(date3.get(date3.HOUR)) + String.valueOf(date3.get(date3.MINUTE)) + String.valueOf(date3.get(date3.SECOND));
	
			String new_jobInfo = time3 + id;	

			//  새로운 jobInfo 등록	
			String query9 = "insert into JobInfo (id, posi, part, tel, fax, kind, company, back) values ('" + new_jobInfo + "', '" + uni.convert(position) + "', '" + uni.convert(part) + "', '" + j_tel + "', '" + j_fax + "', '" + member.getString( "kind" ) + "', '" + num + "', '" + current + "');";
			stmt.executeUpdate( query9 );			

			message = "fail to update JobInfo in query10";
			// MemberPerson에 최신 jobInfo update	
			String query10 ="update MemberPerson set jobinfo='" + new_jobInfo + "' where id='" + id + "';";
			stmt.executeUpdate( query10  ); 			


			message = "fail to get employee number for increasing in query11";
			// 새로운 직장에 회사직원을 증가시킨다.
			String query11 = "select employee from Company where id='" + num + "';"; 
			company = stmt.executeQuery(query11 );
			if( !company.next() ) 
				out.println( "fail to get employee number from company " );

			// company에서 직원수를 얻어와서 1 증가시킨다.
			employee = Integer.parseInt( company.getString( "employee" ) );
			employee++;
			
			message = "fail to update employee number for newcom in query12";
			String query12 = "update Company set employee='" + employee + "' where id='" + num + "';";
			stmt.executeUpdate( query12 );

			message = "fail to get ComInfo for show info in query13"; 
			// 등록된 회사정보를 얻는다.	
			String query13 = "select Company.name, Company.region, Company.site, " +
				"ComInfo.email, ComInfo.fax, ComInfo.tel, ComInfo.homepage, " +
				"ComInfo.address  from ComInfo, Company " +
				"where Company.id='" + num + "' and ComInfo.id=Company.cominfo;";
			newcom = stmt.executeQuery( query13 );	
			newcom.next();

			message = "fail to update com_id in query14";
			String query14 = "update MemberPerson set com_id='" + num +
								"' where id='" + id + "';";
			stmt.executeUpdate( query14 );

			con.commit();
			con.close();
		} catch( SQLException e ) {
			out.println( message + e.toString()  );
			return;
		}

		if( region.equals( "1" ) )  region = "서울특별시" ;	
		else if( region.equals( "2" ) )  region = "부산광역시";
		else if( region.equals( "3" ) )  region = "대구광역시";
		else if( region.equals( "4" ) )  region = "인천광역시";
		else if( region.equals( "5" ) )  region = "광주광역시";
		else if( region.equals( "6" ) )  region = "울산광역시";
		else if( region.equals( "7" ) )  region = "대전광역시";
		else if( region.equals( "8" ) )  region = "경기";
		else if( region.equals( "9" ) )  region = "강원";	
		else if( region.equals( "10" ) ) region = "충북" ;
		else if( region.equals( "11" ) ) region = "충남";
		else if( region.equals( "12" ) ) region = "전북";		
		else if( region.equals( "13" ) ) region = "전남";
		else if( region.equals( "14" ) ) region = "경북";
		else if( region.equals( "15" ) ) region = "경남";
		else if( region.equals( "16" ) ) region = "제주";

		if( kind.equals( "1" ) )  kind = "공무원";
		else if( kind.equals( "2" ) )  kind = "교원";
		else if( kind.equals( "3" ) )  kind = "의료";
		else if( kind.equals( "4" ) )  kind = "유통";
		else if( kind.equals( "5" ) )  kind = "기계/자동차";
		else if( kind.equals( "6" ) )  kind = "컴퓨터/통신";
		else if( kind.equals( "7" ) )  kind = "서비스업";
		else if( kind.equals( "8" ) )  kind = "금융";
		else if( kind.equals( "9" ) )  kind = "건축/인테리어";
		else if( kind.equals( "10" ) ) kind = "방송";
		else if( kind.equals( "11" ) ) kind = "예술";
		else if( kind.equals( "12" ) ) kind = "무역";
		else if( kind.equals( "13" ) ) kind = "스포츠";
		else if( kind.equals( "14" ) ) kind = "프리랜서";
		else if( kind.equals( "15" ) ) kind = "기타" ;

		//이전한 회사에 대한 정보를 보여준다.
		try {	
			out.println("<html><head><title></title></head>");
			out.println("<body background=/bg.jpg><form method=post action=/namecard/UserServlet>" );
			out.println( "<input type=hidden name=cmd value=jobChange>" );
			out.println("<table border=0 cellspacing=0 width=600>");
			out.println("<tr><td align=center><br><p><font size=4 color=#ffff00><b>" + uni.convert( "입력한 회사정보로 바뀌었습니다." ) + "</td></tr>" );
			out.println( "<tr><td align=center>");
			out.println("<table border=0 cellspacing=0 width=80%>" );
			out.println( "<tr><td><font size=2 color=#fffacd><b>ID </td><td><font size=2 color=#e0ffff><b>" + id + "</td></tr>" );
			out.println( "<tr><td><font size=2 color=#fffacd><b>" + uni.convert( "이름" ) + " </tr><td><font size=2 color=#e0ffff><b>" + member.getString( "name" ) + "</td></tr>" );
			out.println( "<tr><td><font size=2 color=#fffacd><b>" + uni.convert( "회사이름" ) + "</td><td><font size=2 color=#e0ffff><b>" + uni.convert( c_name ) + "</td></tr> ");
			out.println( "<tr><td><font size=2 color=#fffacd><b>" + uni.convert( "지역" ) + " </td><td><font size=2 color=#e0ffff><b>" + uni.convert( region ) + "</td></tr> " );
			if( !c_site.equals( "" ) ){
			 	out.println( "<tr><td><font size=2 color=#fffacd><b>" +   
	        	uni.convert( "지점" ) + "</b></td><td><font size=2 color=#e0ffff><b>"
			 	+ newcom.getString( "site" ) + "</td></tr>");
			}
			out.println( "<tr><td><font size=2 color=#fffacd><b>" + uni.convert( "업종" ) + "</b></font></td><td><font size=2 color=#e0ffff><b>" + uni.convert( kind ) + "</td></tr>" );
			out.println( "<tr><td><font size=2 color=#fffacd><b>" + uni.convert( "전화" ) + " </td><td><font size=2 color=#e0ffff><b>" + newcom.getString( "tel"  ) + "</td></tr>" );
			out.println( "<tr><td><font size=2 color=#fffacd><b>" + uni.convert( "팩스" ) + " </td><td><font size=2 color=#e0ffff><b>" + newcom.getString( "fax" ) + "</td></tr>" );
			out.println( "<tr><td><font size=2 color=#fffacd><b>" + uni.convert( "이메일" ) + " </td><td><font size=2 color=#e0ffff><b>" + newcom.getString( "email" ) + "</td></tr>" );
			out.println( "<tr><td><font size=2 color=#fffacd><b>" + uni.convert( "홈페이지" ) + " </td><td><font size=2 color=#e0ffff><b>" + newcom.getString( "homepage") + "</td></tr>" );
			out.println( "<tr><td><font size=2 color=#fffacd><b>" + uni.convert( "주소" ) + " </td><td><font size=2 color=#e0ffff><b>" + newcom.getString( "address" )  + "</td></tr>" );
			out.println( "<tr><td><font size=2 color=#fffacd><b>" + uni.convert( "부서" ) + " </td><td><font size=2 color=#e0ffff><b>" + uni.convert( position )  + "</td></tr>" );
			out.println( "<tr><td><font size=2 color=#fffacd><b>" + uni.convert( "직책" ) + " </td><td><font size=2 color=#e0ffff><b>" + uni.convert( part ) + "</td></tr>" );
			out.println( "<tr><td><font size=2 color=#fffacd><b>" + uni.convert( "부서전화" ) + " </td><td><font size=2 color=#e0ffff><b>"+ j_tel +"</td></tr>" );
			out.println( "<tr><td><font size=2 color=#fffacd><b>" + uni.convert( "부서팩스" ) + " </td><td><font size=2 color=#e0ffff><b>"+ j_fax +"</td></tr>" );
			out.println("<tr><td colspan=2 align=center><br><p>");
			out.println("<input type=button value=Back onClick=\"window.history.go(-3);\"></td></tr>"); 
			out.println("</table></form></body></html>");	
		} catch( SQLException e ) {
			out.println( "fail to jobchange " + e.toString()  );
		} catch( Exception e ) {
			out.println( "fail to jobchange " + e.toString() );
		}	
	}
	

}
