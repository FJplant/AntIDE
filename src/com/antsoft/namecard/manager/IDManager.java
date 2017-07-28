package com.antsoft.namecard.manager;

import java.awt.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;

/**
 *  IDManager - ID manager
 *  
 *  This class create new ID and delete existed ID.
 */
class IDManager {
	
	/** unicode to asc code */
	Uni2Asc uni;

	/** asccode to uni code */
	Asc2Uni asc;

	/** dbmanager */
	DBManager dbmanager;

	/** cardboxmanager */
	CardBoxManager boxmanager;

	/** namecare site */
	NameCardSite site;

	static String head = "<html><body background=/bg.jpg>";
	static String end = "</body></html>";

	/**
	 *  constructor
	 */
	public IDManager( ) {
		uni = new Uni2Asc();
		asc = new Asc2Uni();
		dbmanager = new DBManager();
		boxmanager = new CardBoxManager();
		site = new NameCardSite();
	} 	


	/////////////////////////////////  Admin 관리  /////////////////////////////
	
	/**
	 *  verifyAdmin - verifyAdmin command를 처리하는 method
	 *  			  DB에 들어있는 어드민의 패스워드와 입력받은 데이타가 맞는지 확인한다. 
	 *
	 *  @param req  폼에서 입력받은 데이터가 들어있는 곳
	 *  @param out  결과를 출력할 곳
	 */
	public boolean verifyAdmin(HttpServletRequest req, PrintWriter out) throws UnsupportedEncodingException { 
		boolean result=false;
		String id = req.getParameter("id");
		String pass = req.getParameter("password");
					
		try{
			//데이타베이스 안의 어드민 아이디와 패스워드비교 	
			ResultSet admin = dbmanager.getMemberPerson("admin");	
			if( !admin.next() ) {
				out.println( head );
				out.println( uni.convert ("관리자 정보를 얻는데 실패했습니다. 다시 시도하세요"  ));
				out.println( end );
				return false;
			}
	
			if((id.equals("admin")) && (pass.equals(admin.getString("password")))){
				System.out.println("session access");	
				
				//httpSession에 어드민 키 넣기
				HttpSession httpSession = req.getSession(true);	
				if(httpSession == null){
					out.println( head );
					System.out.println("session null");
					out.println( end );
					return false;
				}
	
				String adminKey = (String)httpSession.getValue(httpSession.getId());
				if(adminKey == null){
					//System.out.println("session putting");
					httpSession.putValue(httpSession.getId(),"admin"); 				
					System.out.println("admin key put ");	
				}
			
				result = true;
			
			}

		} catch( SQLException e) {
			out.println( head );
			out.println( uni.convert( "DB와의 접속에 문제가 있습니다. 다시 시도하세요" )  + e.toString() );
			out.println( end );
			return false;
		} catch( CommandException e ) {
			out.println( head );
			out.println( uni.convert( "DB와의 접속에 문제가 있습니다. 다시 시도하세요" ) + e.toString());
			out.println( end );
			return false;
		
		}catch(Exception e){
			out.println( head );
			out.println("adminkey put fail");	
			out.println( end );
			return false;	
		}		
		return result;
	}
	
	/**
	 *  changePassword -  changePassword command 처리하는 method
	 *					 admin의 password를 바꿈다.
	 *
	 *  @param req  폼에서 입력받은 데이터가 들어있는 곳
	 *  @param out  결과를 출력할 곳
	 */
	public void changePassword( HttpServletRequest req, PrintWriter out ) throws UnsupportedEncodingException {
		System.out.println( "admin의 암호를 바꾸는 중..." );
		// 권한 검사
		HttpSession httpsession = req.getSession( false );
		if( httpsession == null ) {
			out.println( head );
			out.println( "<h1>Unauthorized User! </h1>" );
			out.println( end );
			return;
		}

		// parameter parsing
		String newpass, checkpass;
		
		newpass = req.getParameter( "newpass" );
		checkpass = req.getParameter( "checkpass" );
		
		// 입력이 잘못 된 경우 메세지를 출력한다.
		if( ( newpass == null ) || ( checkpass == null ) ) {
			out.println( head );
			out.println( "입력이 바르지 않습니다. 다시 입력하시고 시도하십시요." );
			out.println( end );
			return;
		}
		
		String id = (String)httpsession.getValue("id");
		
		boolean result;
		
		try{	
			// admin objcet에서 password를 바꾼다.
			dbmanager.updatePassword( "admin", newpass );
			result = true;	
		} catch( CommandException e ) {
			out.println( head );
			out.println( uni.convert( "패스워드 변경이 실패했습니다. 다시 시도하세요" ) + e.toString() );
			out.println( end );
			return;
		}
		
	 
		if( result ) {
			// 수정되었다는 메세지를 보여준다.
			out.println( head );
			out.println( uni.convert( "<h2>암호가 " + newpass + "로 수정되었습니다.</h2>" ));
			out.println( end );
			return;
		}
		// 입력이 잘못되었다는 메세지를 보여주고 다시 폼을 보여준다.
		out.println( uni.convert( "<h2>입력이 올바르지 않습니다. 다시 시도해주십시요.</h2>"  ));
		
			
	}



	
	//////////////////////////////////  Login  //////////////////////////////////

	/**
	 *  login - login command 를 처리하는 method
	 *          사용자 인증한다.
	 *
	 *  @param  req  폼에서 입력받은 데이터가 들어있는 곳
	 *  @param  out  결과를 출력할 곳
	 */
	public void login( HttpServletRequest req, PrintWriter out ) throws UnsupportedEncodingException{
		// 권한 검사
		HttpSession httpsession = req.getSession( true );
		if( httpsession == null ) {
			out.println( head );
			out.println( "<h1>Unauthorized User! </h1>" );
			out.println( end );
			return  ;
		}

		// parameter parsing
		String id = null;
		String passwd = null;
		
		String s_id = asc.convert(req.getParameter( "id" ));
		String s_passwd = asc.convert(req.getParameter( "password" ) );
	
		//tokenizing 해서 space문자를 없애준다.
		StringTokenizer st1 = new StringTokenizer(s_id);
		while(st1.hasMoreTokens())
			if(id == null) id = st1.nextToken();
			else id = id + st1.nextToken();

		StringTokenizer st2 = new StringTokenizer(s_passwd);
		while(st2.hasMoreTokens())
 			if(passwd == null) passwd = st2.nextToken();
			else passwd = passwd + st2.nextToken(); 
		
		// 입력이 잘못 된 경우 메세지를 출력한다.
		if( id == null ) {
			out.println( head );
			out.println( uni.convert( "아이디가 없습니다. 아이디를 입력하세요"  ) );
			out.println( end );
			return;
		}
		if( passwd == null ) {
			out.println( head );
			out.println(uni.convert( "패스워드가 없습니다. 패스워드를 입력하세요." ) );
			out.println( end );
			return ;
		}

		ResultSet member = null;
		boolean result = false;	
		//입력받은 id로 가입자 찾기
		try {
			member = dbmanager.getMemberPerson( uni.convert(id) );			
			if( !member.next()  ) {
				out.println( head );
				out.println( uni.convert( "등록되지 않은 아이디입니다. 먼저 등록을 하세요" ) );
				out.println( end );
				return;
			}
			if(  uni.convert(passwd).equals( member.getString( "password" ) ) ) {
				httpsession.putValue( "id", uni.convert(id) );
				result = true;
			}				
		} catch( SQLException e ){
	    		out.println(  uni.convert( "DB와의 연결에 문제가 있습니다. 다시 시도하세요" ) );
		} catch( CommandException e ) {
			out.println( uni.convert( "DB와의 연결에 문제가 있습니다. 다시 시도하세요" ) );
		}

		if( result ) {
			boxmanager.showDirList( req, out );
		} else {
			out.println(uni.convert( "<center><font size=4><b>password가 맞지 않습니다.<br>") );
			out.println( uni.convert( "다시 입력해 주십시오.<br><p>") );
			out.println("<a href=\"javaScript:window.history.go(-1);\"><font size=2><b><-Back</a></center>");
		}
		
	}

	////////////////////////////// Logout  ///////////////////////////////

	public void logout( HttpServletRequest req, PrintWriter out ) throws ServletException , UnsupportedEncodingException {
	// 권한 검사
		HttpSession httpsession = req.getSession( false );
		if( httpsession == null ) {
			out.println( head );
			out.println( "<h1>Unauthorized User! </h1>" );
			out.println( end );
			return;
		}	

		httpsession.invalidate();
		
		out.println( head );
		out.println( uni.convert("<font size=5 color=white><center><br><br><br><br>안녕히 가십시요.</center></font>" ));
		out.println( end );	
				out.println( "<meta http-equiv=\"refresh\" content=\"0;url=/cardSystem/userLogin.html\">" );
	}

	
	//////////////////////////////  ID 신청  //////////////////////////////
	
	/**
	 *  checkCompany - checkCompany command 처리하는 method
	 *                 ID신청폼에서 입력된 정보로 
	 *                 회사가 있는지를 확인해서 다시 신청폼을 보여준다.
	 *
	 *  @param req  폼에서 입력받은 데이터를 갖고 있는 곳
	 *  @param out  결과를 출력할 곳
	 *  @throws ServletException req에서 얻으려고 하는 것이 없을 때
	 *  @throws UnsupportedEncodingException
	 */
	public void checkCompany( HttpServletRequest req, PrintWriter out ) 
	            throws ServletException, UnsupportedEncodingException {
		// 권한 검사
		HttpSession httpsession = req.getSession( true );
		if( httpsession == null ) {
			out.println( head );
			out.println( "<h1>Unauthorized User! </h1>" );
			out.println( end );
			return;
		}
		
		// request로부터 회사이름과 지역, 지점을 알아낸다.
		String icomname = asc.convert(req.getParameter( "s_name" ));
		String comregion  = req.getParameter( "s_region" ); 
		String icomsite = asc.convert(req.getParameter( "s_site" )); 

		String comname = null;
		String comsite = null;
		String message = "tokenizing...";
			
		//tokenizing 해서 space문자를 없애준다.
		StringTokenizer st1 = new StringTokenizer(icomname);
		while(st1.hasMoreTokens())
			if(comname == null) comname = st1.nextToken();
			else comname = comname + st1.nextToken();

		StringTokenizer st3 = new StringTokenizer(icomsite);
		while(st3.hasMoreTokens())
 			if(comsite == null) comsite = st3.nextToken();
			else comsite = comsite + st3.nextToken(); 
		if( comsite == null ) comsite="";
	

		// 입력이 잘못되었을 경우 에러 메세지 출력
		if( comname == null ) {
			out.println( head );
			out.println( uni.convert( "회사이름이  잘못되었습니다. 다시 입력하십시요" ) );
			out.println( end );
			return;
		}
		message = " in time calculation";	
		// object생성 시간 입력
		Calendar date = Calendar.getInstance();
		
		String id = String.valueOf(date.get(date.YEAR)) + String.valueOf(date.get(date.MONTH)) + String.valueOf(date.get(date.DATE)) + String.valueOf(date.get(date.HOUR)) + String.valueOf(date.get(date.MINUTE)) + String.valueOf(date.get(date.SECOND));

		httpsession.putValue("id", id);
		
		// ComInfo를 위해서 회사이름과 지역으로 회사가 있는지 확인한다.
		ResultSet com = null;
		String com_num = null;
		
		try{
			message = " searchCompany";
			// searchCompany method call한다.	
			com = dbmanager.searchCompany( uni.convert(comname), Integer.parseInt(comregion), uni.convert(comsite) );
		
			// 등록하려는 회사가 이미 존재하는 회사인지 체크한다.
			if( com.next() ) {           // 기존의 회사인 경우
				message = " com is not null" ;
				com_num = com.getString( "id" );		
				addPersonForm1( req, out, com_num );
			} else {  
				message = " com is not exist";
				// 새로운 회사인 경우
				addPersonForm2( req, out );
			} 
		} catch( SQLException e) {
				out.println( head );
	    		out.println( uni.convert( "DB와의 연결에 문제가 있습니다. 다시 시도하세요" ) + e.toString()  );
				out.println( end );
	    		return;
		} catch( CommandException e) {
				out.println( head );
    			out.println( uni.convert( "DB와의 연결에 문제가 있습니다. 다시 시도하세요" ) + e.toString() );
				out.println( end );
				return;
		} catch( Exception e) {
				out.println( head );
    			out.println( message + e.toString() );
				out.println( end );
				return;
		} 

	}	



	
	/**
	 *  addPersonForm1 -  아이디 등록을 위한 폼 제공한다.(이미 회사가 존재하는 경우)
	 *  
	 *  @param req  form에 입력된 정보를 담아두는 곳
	 *  @param out  결과를 출력할 곳
	 *  @param num  IC신청자가 등록하려는 회사
	 *  @throws ServletException  req로부터 정보얻기가 실패할 때
	 *  @throws UnsupportedEncodingException  
	 */
	public void addPersonForm1( HttpServletRequest req, PrintWriter out, String num ) 
	throws ServletException, UnsupportedEncodingException {
		// request에서 parameter를 parsing한다.
		String s_id 		= asc.convert(req.getParameter( "id" ) );
		String s_passwd		= req.getParameter( "password" );
		String s_checkPasswd= req.getParameter("checkPasswd"); 
		String s_name 		= asc.convert(req.getParameter( "name" ));
		String s_birth 		= req.getParameter( "birth" );
		String s_ssn 		= req.getParameter( "ssn" );
		String sex			= req.getParameter( "sex" );
		String desc       	= asc.convert(req.getParameter( "description" ));
		// person info
		String s_address   	= asc.convert(req.getParameter( "address" ));
		String beeper 		= req.getParameter( "beeper" );
		String s_email 		= req.getParameter( "email" );
		String homepage 	= req.getParameter( "homepage" );
		String phone 		= req.getParameter( "handphone" );
		String tel 			= req.getParameter( "tel" );
		// job info
		String j_fax		= req.getParameter( "c_fax" );
		String s_j_tel		= req.getParameter( "c_tel" );
		String j_kind 		= req.getParameter( "j_kind" );
		String j_part 		= asc.convert(req.getParameter( "part" ));
		String s_j_position	= asc.convert(req.getParameter( "position" ));
		// com info
		String s_c_name 	= asc.convert(req.getParameter( "s_name" ));
		String c_region		= req.getParameter( "s_region" );
		String s_c_site		= asc.convert(req.getParameter( "s_site" ));

		String id = null; 
		String passwd = null;
 		String checkPasswd = null;   
		String name = null; 
		String birth = null;  
		String ssn = null; 
		String address = null; 
		String email = null;    
		String c_name = null; 
		String c_site = null;
		String j_position = null;
		String j_tel = null; 

		//tokenizing 해서 space문자를 없애준다.
		StringTokenizer st1 = new StringTokenizer(s_id);
		while(st1.hasMoreTokens())
			if(id == null) id = st1.nextToken();
			else id = id + st1.nextToken();
		if( id.length() > 10 ) {
			out.println( head );
			out.println( uni.convert( "ID는 10자 이내로 해주세요" ) );
			out.println( end );
			return;
		}

		StringTokenizer st2 = new StringTokenizer(s_passwd);
		while(st2.hasMoreTokens())
			if( passwd== null) passwd = st2.nextToken();
			else passwd = passwd + st2.nextToken();

		StringTokenizer st3 = new StringTokenizer(s_checkPasswd);
		while(st3.hasMoreTokens())
 			if(checkPasswd == null) checkPasswd = st3.nextToken();
			else checkPasswd = checkPasswd + st3.nextToken(); 	

		StringTokenizer st4 = new StringTokenizer(s_name);
		while(st4.hasMoreTokens())
			if( name== null) name = st4.nextToken();
			else name = name + st4.nextToken();

		StringTokenizer st5 = new StringTokenizer(s_ssn);
		while(st5.hasMoreTokens())
			if( ssn== null) ssn = st5.nextToken();
			else ssn = ssn + st5.nextToken();
		if( ssn.length() != 13 ){
			out.println( head );
			out.println( uni.convert( "ssn은 '-'없이 13자로 해주세요" ) );
			out.println( end );
			return;
		}
		StringTokenizer st6 = new StringTokenizer(s_address);
		while(st6.hasMoreTokens())
			if(address == null)  address = st6.nextToken();
			else  address= address + st6.nextToken();

		StringTokenizer st7 = new StringTokenizer(s_email);
		while(st7.hasMoreTokens())
			if(email == null) email = st7.nextToken();
			else email = email + st7.nextToken();

		StringTokenizer st8 = new StringTokenizer(s_c_name);
		while(st8.hasMoreTokens())
			if( c_name== null) c_name = st8.nextToken();
			else  c_name= c_name + st8.nextToken();

		StringTokenizer st9 = new StringTokenizer(s_j_position);
		while(st9.hasMoreTokens())
			if( j_position == null) j_position = st9.nextToken();
			else j_position = j_position + st9.nextToken();

			
		StringTokenizer st10 = new StringTokenizer(s_j_tel);
		while(st10.hasMoreTokens())
			if( j_tel == null) j_tel = st10.nextToken();
			else j_tel = j_tel + st10.nextToken();

		StringTokenizer st11 = new StringTokenizer(s_birth);
		while(st11.hasMoreTokens()) 
			if( birth == null) birth = st11.nextToken();
			else birth = birth + st11.nextToken();

		StringTokenizer st12 = new StringTokenizer(s_c_site);
		while(st12.hasMoreTokens()) 
			if( c_site == null) c_site = st12.nextToken();
			else c_site = c_site + st12.nextToken();

		if( c_site == null ) c_site = "";

		// 입력자료가 모자라면 다시 폼으로 돌아간다.
		if( ( id == null ) || ( passwd == null ) || ( checkPasswd == null )  
				|| ( name == null ) || ( birth == null ) 
				|| ( ssn == null ) || ( address == null ) 
				|| ( c_name == null )  || ( j_position == null )
				|| ( j_tel == null ) || ( email == null ) ) {
			// 폼으로 다시 돌아간다.
			out.println( head );
			out.println( "<center><h3>" + uni.convert( "입력자료가 부족합니다. 다시 입력하세요" ) + "<h3>" );
			out.println("<br><p><a href=\"javaScript:window.history.go(-1);\"><font size=2><b><-Back</a></center>");
			out.println( end );
			return;
		}

		//패스워드 확인하기
		if(!s_passwd.equals(checkPasswd)){
			out.println( head );
			out.println("<center><h3>" + uni.convert( "확인용 패스워드와 일치하지 않습니다.다시 입력해 주세요." ) + "</h3>");
			out.println("<br><p><a href=\"javaScript:window.history.go(-1);\"><font size=2><b><-Back</a></center>");
			out.println( end );
			return;
		}
		
		
		// 이미 존재하는 회사가 있다는 flag와 Company object를 session에 넣는다.
		HttpSession httpsession = req.getSession( true );
		if( httpsession == null ) {
			out.println( head );
			out.println( "Unauthorized User!!" );
			out.println( end );
		    	return;	    
		}
		
		httpsession.putValue( "exist", "true" );
		httpsession.putValue( "com", num );
		
		// 회사 고유번호를 통해서 회사정보를 가져온다.
		try {
			ResultSet com = dbmanager.getCompany( num );
			if( !com.next() ) {
				out.println( head );
				out.println( uni.convert( "DB와의 연결에 문제가 있습니다." ) );
				out.println( end );
				return;
			}
			ResultSet info = dbmanager.getComInfo( num );	
			if( !info.next() ) {
				out.println( head );
				out.println( uni.convert( "DB와의 연결에 문제가 있습니다." ) );
				out.println( end );
				return;
			} 

			String c_tel = info.getString( "tel" );
			String c_fax = info.getString( "fax" );
			String c_address = info.getString("address");
			String c_email = info.getString( "email" );
			String c_homepage = info.getString( "homepage" );

			// 이미 입력한 정보들을 보여주고 action을 기다린다. 
			out.println("<html>");
			out.println("<head>");
			out.println("<title>id" + uni.convert( "등록" ) + "</title>");
			out.println("</head>");
			out.println("<body background=/bg.jpg>");
			out.println("<table border=0 width=600 cellspacing=0>");
  			out.println("<tr>");
			out.println("<form method=post action=/namecard/UserServlet >");
      		out.println("<input type=hidden name=cmd value=registerMemberPerson>");
			out.println("<input type=hidden name=id value="+uni.convert(id)+">");
    		out.println("<td height=28 align=center><font size=4 color=#ffff00><b><img src=/icon.gif>" + uni.convert( "개인정보" ) + "</b></font></td>");
  			out.println("</tr>");
  			out.println("<tr>");
    		out.println("<td height=300 align=center valign=top>");
      		out.println("<br><p>");
      		out.println("<table border=0 width=80% align=center cellspacing=0>");
  			out.println("<tr>"); 
       		out.println("<td><font size=2 color=#fffacd><b>ID</b></font></td>");
   			out.println("<td><input type=text name=id  size=10 maxlength=10 value="+uni.convert(id)+"> </td>");
			out.println("</tr><tr>");
   			out.println("<td><font size=2 color=#fffacd><b>password</b></font></td>");
   			out.println("<td><input type=password name=password size=10 maxlength=10 value="+passwd+"></td>");
       		out.println("</tr>");
  			out.println("<tr>");
       		out.println("<td><font size=2 color=#fffacd><b>" + uni.convert( "이름" ) + "</b></td>");
   			out.println("<td><input type=text name=name size=5 maxlength=5 value=\""+uni.convert(name)+"\"></td>");
			out.println("</tr><tr>");
   			out.println("<td><font size=2 color=#fffacd><b>" + uni.convert( "주민등록번호" ) + "</b></td>");
   			out.println("<td> <input type=text name=ssn size=13 maxlength=13 value=\""+ssn+"\"></td>");
       		out.println("</tr>");
			out.println("<tr>");
       		out.println("<td><font size=2 color=#fffacd><b>" + uni.convert( "생년월일" ) + "</b></td>");
   			out.println("<td> <input type=text name=birth size=10 maxlength=10 value=\""+birth+"\"></td>");
			out.println("</tr><tr>");
   			out.println("<td><font size=2 color=#fffacd><b>" + uni.convert( "성별" ) + "</b></td>");
			if(sex.equals("m")){
    			out.println("<td><font size=2 color=#fffacd><b>" + uni.convert( "남" ) + "<input type=radio name=sex checked value=m>" + uni.convert( "여" ) + "<input type=radio name=sex value=f></b></td>");
			}else{
				out.println("<td><font size=2 color=#fffacd><b>" + uni.convert( "남" ) + "<input type=radio name=sex value=m>" + uni.convert( "여" ) + "<input type=radio name=sex checked value=f></b></td>");
			}
       		out.println("</tr>");
  			out.println("<tr>");
       		out.println("<td><font size=2 color=#fffacd><b>" + uni.convert( "주소" ) + "</b></td>");
    		out.println("<td> <input type=text name=address size=40 value=\""+uni.convert(s_address)+"\"></td>");
			out.println("</tr><tr>");
  	   		out.println("<td><font size=2 color=#fffacd><b>" + uni.convert( "전화" ) + "</b></td>");
    		out.println("<td> <input type=text name=tel size=12 value=\""+tel+"\"> </td>");
       		out.println("</tr>");
  			out.println("<tr>");
       		out.println("<td><font size=2 color=#fffacd><b>" + uni.convert( "삐삐" ) + "</b></td>");
    		out.println("<td> <input type=text name=beeper size=13 value=\""+beeper+"\"></td>");
			out.println("</tr><tr>");
       		out.println("<td><font size=2 color=#fffacd><b>" + uni.convert( "휴대폰" ) + "</b></td>");
    		out.println("<td> <input type=text name=handphone size=13 value=\""+phone+"\"></td>");
       		out.println("</tr>");
  			out.println("<tr>");
       		out.println("<td><font size=2 color=#fffacd><b>" + uni.convert( "홈페이지" ) + "</b></td>");
    		out.println("<td><input type=text name=homepage size=40 value=\""+homepage+"\"></td>");
			out.println("</tr><tr>");
    		out.println("<td><font size=2 color=#fffacd><b>e-mail<b></td>");
    		out.println("<td><input type=text name=email size=40 value=\""+email+"\"></td>");
       		out.println("</tr>");
  			out.println("<tr>");
       		out.println("<td><font size=2 color=#fffacd><b>" + uni.convert( "지위" ) + "</b></td>");
    		out.println("<td><input type=text name=position size=20 value=\""+uni.convert(s_j_position)+"\"></td>");
			out.println("</tr><tr>");
    		out.println("<td><font size=2 color=#fffacd><b>" + uni.convert( "부서" ) + "</b></td>");
    		out.println("<td><input type=text name=part size=20 value=\""+uni.convert(j_part)+"\"></td>");
       		out.println("</tr>");
  			out.println("<tr>");
       		out.println("<td><font size=2 color=#fffacd><b>" + uni.convert( "회사개인전화" ) + "</b></td>"); 
			out.println("<td> <input type=text name=j_tel size=13 value=\""+j_tel+"\"></td>");
			out.println("</tr><tr>");
    		out.println("<td><font size=2 color=#fffacd><b>" + uni.convert( "회사팩스" ) + "</b></td>");
    		out.println("<td><input type=text name=j_fax size=13 value=\""+j_fax+"\"></td>");
       		out.println("</tr>");
			out.println("<tr>");
			out.println("<td><font size=2 color=#fffacd valign=top><b>description</b></font></td>");
			out.println("<td><textarea name=description cols=50 rows=4>"+uni.convert(desc)+"</textarea></td>");
			out.println("</tr>");
      		out.println("</table><br><p></td>");
    		out.println("</tr> ");
    		out.println("<tr>");
      		out.println("<td align=center height=28><b><font size=4 color=#ffff00><img src=/icon.gif>" + uni.convert( "회사정보" ) + "</b></td>");
    		out.println("</tr>");
    		out.println("<tr>  ");
  	  		out.println("<td valign=top align=center><br><p>");   
       		out.println("<table border=0 width=80% cellspacing=0>");
			out.println("<tr>");
       		out.println("<td width=90><font size=2 color=#fffacd><b>" + uni.convert( "회사이름" ) + "</b></td>");
 			out.println("<td><font size=2><b> "+uni.convert(s_c_name)+"<input type=hidden name=c_name value=\"" + uni.convert(s_c_name) + "\"></td></tr>");
			out.println("<tr>");
      		out.println("<td><font size=2 color=#fffacd><b>" + uni.convert( "지역" ) + "</b></td>");
      		out.println("<td><font size=2><b>");
			
			if(c_region.equals("1")) out.println( uni.convert( "서울특별시" ) );
	   		if(c_region.equals("2")) out.println( uni.convert( "부산광역시" ) );
			if(c_region.equals("3")) out.println( uni.convert( "대구광역시" ) );
			if(c_region.equals("4")) out.println( uni.convert( "인천광역시" ) );
			if(c_region.equals("5")) out.println( uni.convert( "광주광역시" ) );
			if(c_region.equals("6")) out.println( uni.convert( "울산광역시" ) );
    		if(c_region.equals("7")) out.println( uni.convert( "대전광역시" ) );
			if(c_region.equals("8")) out.println( uni.convert( "경기" ) );
			if(c_region.equals("9")) out.println( uni.convert( "강원" ) );
			if(c_region.equals("10")) out.println( uni.convert( "충북" ) );
			if(c_region.equals("11")) out.println( uni.convert( "충남" ) );
			if(c_region.equals("12")) out.println( uni.convert( "전북" ) );
			if(c_region.equals("13")) out.println( uni.convert( "전남" ) );
			if(c_region.equals("14")) out.println( uni.convert( "경북" ) );
			if(c_region.equals("15")) out.println( uni.convert( "경남" ) );
			if(c_region.equals("16")) out.println( uni.convert( "제주" ) );
 			out.println("<input type=hidden name=c_region value=\"" + c_region + "\"></td></tr>");
			
      		if( !c_site.equals( "" ) ) {	
				out.println("<tr>");
       			out.println("<td><font size=2 color=#fffacd><b>" + uni.convert( "지점" ) + "</b></td>");
       			out.println("<td><font size=2><b>" + uni.convert( com.getString( "site" ) ) );
				out.println("<input type=hidden name=c_site value=\""+com.getString( "site" )+"\"></td>");
				out.println("</tr>");
			} else {
				out.println( "<input type=hidden name=c_site value=\"" + c_site + "\">" );
			} 
			out.println( "<tr>" );	
			out.println("<td><font size=2 color=#fffacd><b>" + uni.convert( "업종" ) + "</b></font></td>");
			out.println("<td><font size=2><b>");
		
			if(j_kind.equals("1")) out.println( uni.convert( "공무원" ) );
       		if(j_kind.equals("2")) out.println( uni.convert( "교원" ) );
			if(j_kind.equals("3")) out.println( uni.convert( "의료" ) );
			if(j_kind.equals("4")) out.println( uni.convert( "유통" ) );
			if(j_kind.equals("5")) out.println( uni.convert( "기계/자동차" ) );
			if(j_kind.equals("6")) out.println( uni.convert( "컴퓨터/통신" ) );
			if(j_kind.equals("7")) out.println( uni.convert( "서비스업" ) );
       		if(j_kind.equals("8")) out.println( uni.convert( "금융" ) );
       		if(j_kind.equals("9")) out.println( uni.convert( "건축/인테리어" ) );
       		if(j_kind.equals("10")) out.println( uni.convert( "방송" ) );
			if(j_kind.equals("11")) out.println( uni.convert( "예술" ) );
			if(j_kind.equals("12")) out.println( uni.convert( "무역" ) );
			if(j_kind.equals("13")) out.println( uni.convert( "스포츠" ) );
			if(j_kind.equals("14")) out.println( uni.convert( "프리랜서" ));
			if(j_kind.equals("15")) out.println( uni.convert( "기타" ) );
			out.println("<input type=hidden name=j_kind value=\"" + j_kind + "\"></td></tr>");
      		out.println("</table>");
	  		out.println("<br><p><table border=0 width=80% cellspacing=0>");

	   		out.println("<tr>");
  			out.println("<td><font size=2 color=#fffacd><b>" + uni.convert( "대표전화" ) + "</b></font>");
			out.println("<input type=hidden name=c_tel value=\""+c_tel+"\"></td>");
			out.println("<td><font size=2><b>"+c_tel+"</td>");
			out.println("</tr><tr>");
			out.println("<td><font size=2 color=#fffacd><b>" + uni.convert( "팩스" ) + "</b></font>");
			out.println("<input type=hidden name=c_fax value=\""+c_fax +"\"></td>");
			out.println("<td><font size=2><b>"+c_fax +"</td>");
   	   		out.println("</tr>");
   	   		out.println("<tr>");
   	   		out.println("<td><font size=2 color=#fffacd><b>e-mail</b></font>");
			out.println("<input type=hidden name=c_email value=\""+c_email +"\"></td>");
   	   		out.println("<td><font size=2><b>"+c_email+"</td>");
			out.println("</tr><tr>");
   	   		out.println("<td><font size=2 color=#fffacd><b>homepage</b></font>");
			out.println("<input type=hidden name=c_homepage value=\""+c_homepage+"\"></td>");
   	   		out.println("<td><font size=2><b>"+c_homepage+"</td>");
   	   		out.println("</tr>");
   	   		out.println("<tr>");
   	   		out.println("<td><font size=2 color=#fffacd><b>" + uni.convert( "주소" ) + "</b></font>");
			out.println("<input type=hidden name=c_address value=\""+c_address+"\"></td>");
 	   		out.println("<td><font size=2><b>"+c_address+"</td>");
    		out.println("</tr>");
    		out.println("<tr><td colspan=2 align=center><font size=2 color=#e6e6fa><br><p><br><p>" + uni.convert("검색한 회사가 현재 다니고 있는 직장과 같으면 '등록'버튼을 눌러주세요.") + "<br>" + uni.convert(" 같지 않으면 취소버튼을 누르세요.") + "</td></tr>" );
       		out.println("<tr>");
       		out.println("<td colspan=2 align=center><br><p>");
       		out.println("<input type=submit value="+ uni.convert( "등록" ) +">");
       		out.println("<input type=button value=" + uni.convert( "취소" ) + " onClick=\"window.history.go(-1);\"></td>");
       		out.println("</tr></table>");
	 		out.println("</td>");
    		out.println("</form></tr>");
			out.println("</table>");
			out.println("</body>");
			out.println("</html>");
		} catch( SQLException e) {
			out.println( head );
			out.println( uni.convert( "DB와의 연결에 문제가 있습니다. 다시 시도하세요" )  + e.toString());
			out.println( end );
			return;
		} catch( CommandException e) {
			out.println( head );
			out.println( e.toString());
			out.println( end );
			return;
		} 
	}
		
	
	/**
	 *  addPersonForm2 - show ID input form ( 새로운 회사인 경우 )
	 *
	 *  @param req  form에서 입력받은 데이터가 들어있는  곳
	 *  @param out  결과를 출력하는 곳
	 */
	public void addPersonForm2( HttpServletRequest req, PrintWriter out ) throws UnsupportedEncodingException {
		// request에서 parameter를 parsing한다.
		String s_id 		= asc.convert(req.getParameter( "id" ));
		String s_passwd		= req.getParameter( "password" );
		String s_checkPasswd= req.getParameter( "checkPasswd" );
		String s_name 		= asc.convert(req.getParameter( "name" ));
		String s_birth 		= req.getParameter( "birth" );
		String s_ssn 		= req.getParameter( "ssn" );
		String sex        	= req.getParameter( "sex" );
		String desc       	= asc.convert(req.getParameter( "description" ));
		// person info
		String s_address    = asc.convert(req.getParameter( "address" ));
		String beeper 		= req.getParameter( "beeper" );
		String s_email 		= req.getParameter( "email" );
		String homepage 	= req.getParameter( "homepage" );
		String phone 		= req.getParameter( "handphone" );
		String tel 			= req.getParameter( "tel" );
		// job info
		String j_fax		= req.getParameter( "c_fax" );
		String s_j_tel		= req.getParameter( "c_tel" );
		String j_kind 		= req.getParameter( "j_kind" );
		String j_part 		= asc.convert(req.getParameter( "part" ));
		String s_j_position	= asc.convert(req.getParameter( "position" ));
		// com info
		String s_c_name 	= asc.convert(req.getParameter( "s_name" ));
		String c_region		= req.getParameter( "s_region" );
		String s_c_site		= asc.convert(req.getParameter( "s_site" ));

		String id = ""; 
		String passwd = "";
 		String checkPasswd = "";   
		String name = ""; 
		String birth = "";  
		String ssn = ""; 
		String address = ""; 
		String email = "";    
		String c_name = ""; 
		String c_site = "";
		String j_position = "";
		String j_tel = ""; 

		//tokenizing 해서 space문자를 없애준다.
		StringTokenizer st1 = new StringTokenizer(s_id);
		while(st1.hasMoreTokens())
			if(id == "") id = st1.nextToken();
			else id = id + st1.nextToken();

		StringTokenizer st2 = new StringTokenizer(s_passwd);
		while(st2.hasMoreTokens())
			if( passwd== "") passwd = st2.nextToken();
			else passwd = passwd + st2.nextToken();

		StringTokenizer st3 = new StringTokenizer(s_checkPasswd);
		while(st3.hasMoreTokens())
 			if(checkPasswd == "") checkPasswd = st3.nextToken();
			else checkPasswd = checkPasswd + st3.nextToken(); 	

		StringTokenizer st4 = new StringTokenizer(s_name);
		while(st4.hasMoreTokens())
			if( name== "") name = st4.nextToken();
			else name = name + st4.nextToken();

		StringTokenizer st5 = new StringTokenizer(s_ssn);
		while(st5.hasMoreTokens())
			if( ssn== "") ssn = st5.nextToken();
			else ssn = ssn + st5.nextToken();

		StringTokenizer st6 = new StringTokenizer(s_address);
		while(st6.hasMoreTokens())
			if(address == "")  address = st6.nextToken();
			else  address= address + st6.nextToken();

		StringTokenizer st7 = new StringTokenizer(s_email);
		while(st7.hasMoreTokens())
			if(email == "") email = st7.nextToken();
			else email = email + st7.nextToken();

		StringTokenizer st8 = new StringTokenizer(s_c_name);
		while(st8.hasMoreTokens())
			if( c_name== "") c_name = st8.nextToken();
			else  c_name= c_name + st8.nextToken();

		StringTokenizer st9 = new StringTokenizer(s_j_position);
		while(st9.hasMoreTokens())
			if( j_position== "") j_position = st9.nextToken();
			else j_position = j_position + st9.nextToken();

		StringTokenizer st10 = new StringTokenizer(s_j_tel);
		while(st10.hasMoreTokens())
			if( j_tel== "") j_tel = st10.nextToken();
			else j_tel = j_tel + st10.nextToken();

		StringTokenizer st11 = new StringTokenizer(s_birth);
		while(st11.hasMoreTokens())
			if( birth== "") birth = st11.nextToken();
			else birth = birth + st11.nextToken();

		StringTokenizer st12 = new StringTokenizer(s_c_site);
		while(st12.hasMoreTokens())
			if( c_site== "") c_site = st12.nextToken();
			else c_site = c_site + st12.nextToken();

		// 입력자료가 모자라면 다시 폼으로 돌아간다.
		if( ( id== "" ) || ( passwd == "" ) 
			|| ( checkPasswd == "" ) || ( name == "" ) 
			|| ( birth == "" ) || ( ssn == "" )
			|| ( address == "" ) || ( email == "" )   
			|| ( c_name == "" ) || ( j_position == "" )
			|| ( j_tel == "" ) ) {
			// 폼으로 다시 돌아간다.
			out.println( head );
			out.println( "<center><h3>" + uni.convert( "입력자료가 부족합니다. 다시 입력하세요" ) + "<h3>" );
			out.println("<br><p><a href=\"javaScript:window.history.go(-1);\"><font size=2><b><-Back</a></center>");
			out.println( end );
			return;
		}

		//패스워드 확인하기
		if(!passwd.equals(checkPasswd)){
			out.println("<center><h3>" + uni.convert( "확인용 패스워드와 일치하지 않습니다.다시 입력해 주세요." ) + "</h3>");
			out.println("<br><p><a href=\"javaScript:window.history.go(-1);\"><font size=2><b><-Back</a></center>");
		}

		// 회사가 존재하지 않는다는 flag를 session에 넣는다.
		HttpSession httpsession = req.getSession( true );
		httpsession.putValue( "exist", "false" );
		
		
		// 이미 입력한 정보들을 보여주고 회사정보를 입력하라는 메세지를 보내준다. 
		// 회사정보는 비워둔다.
		out.println("<html>");
		out.println("<head>");
		out.println("<title>id" + uni.convert( "등록" ) + "</title>");
		out.println("</head>");
		out.println("<body background=/bg.jpg>");
		out.println("<table width=600 border=0 cellspacing=0>");
  		out.println("<tr>");
    	out.println("<td height=28 align=center><font size=4 color=#ffff00><b><img src=/icon.gif>" + uni.convert( "개인정보" ) + "</b></font></td>");
  		out.println("</tr>");
  		out.println("<tr>");
    	out.println("<td height=300 align=center valign=top>");
      	out.println("<form method=post action=/namecard/UserServlet >");
      	out.println("<input type=hidden name=cmd value=registerMemberPerson>");
		out.println("<input type=hidden name=id value="+uni.convert(id)+">");
      	out.println("<br><p>");
      	out.println("<table border=0 width=80% align=center cellspacing=0>");
  		out.println("<tr>"); 
        out.println("<td><font size=2 color=#fffacd><b>ID</b></font></td>");
    	out.println("<td><input type=text name=id  size=10 maxlength=10 value="+uni.convert(id)+"> </td>");
		out.println("</tr><tr>");
    	out.println("<td><font size=2 color=#fffacd><b>password</b></font></td>");
    	out.println("<td><input type=password name=password size=10 maxlength=10 value="+passwd+"></td>");
        out.println("</tr>");
  		out.println("<tr>");
        out.println("<td><font size=2 color=#fffacd><b>" + uni.convert( "이름" ) + "</b></td>");
    	out.println("<td><input type=text name=name size=5 maxlength=5 value=\""+uni.convert( name )+"\"></td>");
		out.println("</tr><tr>");
    	out.println("<td><font size=2 color=#fffacd><b>" + uni.convert( "주민등록번호" ) + "</b></td>");
    	out.println("<td> <input type=text name=ssn size=13 maxlength=13 value=\""+ssn+"\"></td>");
        out.println("</tr>");
  		out.println("<tr>");
        out.println("<td><font size=2 color=#fffacd><b>" + uni.convert( "생년월일" ) + "</b></td>");
    	out.println("<td> <input type=text name=birth size=10 maxlength=10 value=\""+birth+"\"></td>");
		out.println("</tr><tr>");
    	out.println("<td><font size=2 color=#fffacd><b>" + uni.convert( "성별" ) + "</b></td>");
		if(sex.equals("m")){
    		out.println("<td><font size=2 color=#fffacd><b>" + uni.convert( "남" ) + "<input type=radio name=sex checked value=m>" + uni.convert( "여" ) + "<input type=radio name=sex value=f></b></td>");
		}else{
			out.println("<td><font size=2 color=#fffacd><b>" + uni.convert( "남" ) + "<input type=radio name=sex value=m>" + uni.convert( "여" ) + "<input type=radio name=sex checked value=f></b></td>");
		}
        out.println("</tr>");
  		out.println("<tr>");
        out.println("<td><font size=2 color=#fffacd><b>" + uni.convert( "주소" ) + "</b></td>");
    	out.println("<td> <input type=text name=address size=40 value=\""+ uni.convert( s_address )+"\"></td>");
		out.println("</tr><tr>");
  	    out.println("<td><font size=2 color=#fffacd><b>" + uni.convert( "전화" ) + "</b></td>");
    	out.println("<td> <input type=text name=tel size=12 value=\""+tel+"\"> </td>");
        out.println("</tr>");
  		out.println("<tr>");
        out.println("<td><font size=2 color=#fffacd><b>" + uni.convert( "삐삐" ) + "</b></td>");
    	out.println("<td> <input type=text name=beeper size=13 value=\""+beeper+"\"></td>");
		out.println("</tr><tr>");
        out.println("<td><font size=2 color=#fffacd><b>" + uni.convert( "휴대폰" ) + "</b></td>");
    	out.println("<td> <input type=text name=handphone size=13 value=\""+phone+"\"></td>");
        out.println("</tr>");
  		out.println("<tr>");
        out.println("<td><font size=2 color=#fffacd><b>" + uni.convert( "홈페이지" ) + "</b></td>");
    	out.println("<td><input type=text name=homepage size=40 value=\""+homepage+"\"></td>");
		out.println("</tr><tr>");
    	out.println("<td><font size=2 color=#fffacd><b>e-mail<b></td>");
    	out.println("<td><input type=text name=email size=40 value=\""+email+"\"></td>");
        out.println("</tr>");
  		out.println("<tr>");
        out.println("<td><font size=2 color=#fffacd><b>" + uni.convert( "지위" ) + "</b></td>");
    	out.println("<td><input type=text name=position size=20 value=\""+ uni.convert( s_j_position )+"\"></td>");
		out.println("</tr><tr>");
    	out.println("<td><font size=2 color=#fffacd><b>" + uni.convert( "부서" ) + "</b></td>");
    	out.println("<td><input type=text name=part size=20 value=\""+ uni.convert( j_part )+"\"></td>");
        out.println("</tr>");
  		out.println("<tr>");
        out.println("<td><font size=2 color=#fffacd><b>" + uni.convert( "회사개인전화" ) + "</b></td>"); 
		out.println("<td> <input type=text name=j_tel size=13 value=\""+j_tel+"\"></td>");
		out.println("</tr><tr>");
    	out.println("<td><font size=2 color=#fffacd><b>" + uni.convert( "회사팩스" ) + "</b></td>");
    	out.println("<td><input type=text name=j_fax size=13 value=\""+j_fax+"\"></td>");
        out.println("</tr>");
		out.println("<tr>");
		out.println("<td><font size=2 color=#fffacd valign=top><b>description</b></font></td>");
		out.println("<td><textarea name=description cols=50 rows=4>"+ uni.convert( desc )+"</textarea></td>");
		out.println("</tr>");
      	out.println("</table><br><p></td>");
    	out.println("</tr> ");
    	out.println("<tr>");
      	out.println("<td align=center height=28><b><font size=4 color=#ffff00><img src=/icon.gif>" + uni.convert( "회사정보" ) + "</b></td>");
    	out.println("</tr>");
    	out.println("<tr>  ");
  	  	out.println("<td valign=top><center><br><p>");   
       	out.println("<table border=0 width=80% cellspacing=0>");
		out.println("<tr><td colspan=3 align=center><font size=2 color=#e6e6fa><b>" + uni.convert( "검색한 회사가 없습니다." ) + "<br>");
		out.println( uni.convert( "아래 사항을 기입해 주세요.*표가 되어있는 사항은 반드시 기입해 주세요." ) + "</td></tr>");
		out.println("<tr>");
        out.println("<td><br><p><font size=2 color=#e0ffff><b>*</td>");
		out.println("<td><font size=2 color=#fffacd><b>" + uni.convert( "회사이름" ) + "</b></td>");
 		out.println("<td> <input type=text name=c_name size=20 value=\""+uni.convert(c_name)+"\"></td>");
		out.println("</tr><tr>");
      	out.println("<td><font size=2 color=#e0ffff><b>*</td>");
		out.println("<td><font size=2 color=#fffacd><b>" + uni.convert( "지역" ) + "</b></td>");
      	out.println("<td><select name=c_region size=1>");
		if(c_region.equals("0")) out.println("<option value=0 selected>" + uni.convert( "선택사항없음" ) + "</option>");
		else out.println("<option value=0>" + uni.convert( "선택사항없음" ) + "</option>");
		
		if(c_region.equals("1")) out.println("<option value=1 selected>" + uni.convert( "서울특별시" ) + "</option>");
		else out.println("<option value=1>" + uni.convert( "서울특별시" ) + "</option>");
 		 	
	   	if(c_region.equals("2")) out.println("<option value=2 selected>" + uni.convert( "부산광역시" ) + "</option>");
		else out.println("<option value=2>" + uni.convert( "부산광역시" ) + "</option>");

		if(c_region.equals("3")) out.println("<option value=3 selected>" + uni.convert( "대구광역시" ) + "</option>");
 		else out.println("<option value=3>" + uni.convert( "대구광역시" ) + "</option>");
	
		if(c_region.equals("4")) out.println("<option value=4 selected>" + uni.convert( "인천광역시" ) + "</option>");
		else out.println("<option value=3>" + uni.convert( "인천광역시" ) + "</option>");
            		
		if(c_region.equals("5")) out.println("<option value=5 selected>" + uni.convert( "광주광역시" ) + "</option>");
		else out.println("<option value=5>" + uni.convert( "광주광역시" ) + "</option>");

		if(c_region.equals("6")) out.println("<option value=6 selected>" + uni.convert( "울산광역시" ) + "</option>");
		else out.println("<option value=6>" + uni.convert( "울산광역시" ) + "</option>");
        
   		if(c_region.equals("7")) out.println("<option value=7 selected>" + uni.convert( "대전광역시" ) + "</option>");
		else out.println("<option value=7>" + uni.convert( "대전광역시" ) + "</option>");

		if(c_region.equals("8")) out.println("<option value=8 selected>" + uni.convert( "경기" ) + "</option>");
		else out.println("<option value=8>" + uni.convert( "경기" ) + "</option>");
            
		if(c_region.equals("9")) out.println("<option value=9 selected>" + uni.convert( "강원" ) + "</option>");
		else out.println("<option value=9>" + uni.convert( "강원" ) + "</option>");
	
		if(c_region.equals("10")) out.println("<option value=10 selected>" + uni.convert( "충북" ) + "</option>");
		else out.println("<option value=10>" + uni.convert( "충북" ) + "</option>");
            
		if(c_region.equals("11")) out.println("<option value=11 selected>" + uni.convert( "충남" ) + "</option>");
		else out.println("<option value=11>" + uni.convert( "충남" ) + "</option>");

		if(c_region.equals("12")) out.println("<option value=12 selected>" + uni.convert( "전북" ) + "</option>");
		else out.println("<option value=12>" + uni.convert( "전북" ) + "</option>");
            
		if(c_region.equals("13")) out.println("<option value=13 selected>" + uni.convert( "전남" ) + "</option>");
		else out.println("<option value=13>" + uni.convert( "전남" ) + "</option>");

		if(c_region.equals("14")) out.println("<option value=14 selected>" + uni.convert( "경북" ) + "</option>");
		else out.println("<option value=14>" + uni.convert( "경북" ) + "</option>");
            		
		if(c_region.equals("15")) out.println("<option value=15 selected>" + uni.convert( "경남" ) + "</option>");
		else out.println("<option value=15>" + uni.convert( "경남" ) + "</option>");

		if(c_region.equals("16")) out.println("<option value=16 selected>" + uni.convert( "제주" ) + "</option>");
		else out.println("<option value=16>" + uni.convert( "제주" ) + "</option>");
          
 		out.println("</select> </td></tr>");
		out.println("<tr>");
        out.println("<td></td><td><font size=2 color=#fffacd><b>" + uni.convert( "지점" ) + "</b></td>");
      	if( !s_c_site.equals( "" ) ) {	
			out.println("<td><input type=text name=c_site size=20 value=\""+ uni.convert( s_c_site )+"\"></td>");
		} else {
			out.println( "<td><input type=text name=c_site size=10 ></td>" );
		}	
		out.println("</tr><tr>");
		out.println("<td><font size=2 color=#e0ffff><b>*</td>");
		out.println("<td><font size=2 color=#fffacd><b>" + uni.convert( "업종" ) + "</b></font></td>");
		out.println("<td><select name=j_kind size=1>");

		if(j_kind.equals("1")) out.println("<option value=1 selected>" + uni.convert( "공무원" ) + "</option>");
		else out.println("<option value=1>" + uni.convert( "공무원" ) + "</option>");

       	if(j_kind.equals("2")) out.println("<option value=2 selected>" + uni.convert( "교원" ) + "</option>");
		else out.println("<option value=2>" + uni.convert( "교원" ) + "</option>");
        
		if(j_kind.equals("3")) out.println("<option value=3 selected>" + uni.convert( "의료" ) + "</option>");
		else out.println("<option value=3>" + uni.convert( "의료" ) + "</option>");
        
		if(j_kind.equals("4")) out.println("<option value=4 selected>" + uni.convert( "유통" ) + "</option>");
		else out.println("<option value=4>" + uni.convert( "유통" ) + "</option>");
        
		if(j_kind.equals("5")) out.println("<option value=5 selected>" + uni.convert( "기계/자동차" ) + "</option>");
		else out.println("<option value=5>" + uni.convert( "기계/자동차" ) + "</option>");
       
		if(j_kind.equals("6")) out.println("<option value=6 selected>" + uni.convert( "컴퓨터/통신" ) + "</option>");
		else out.println("<option value=6>" + uni.convert( "컴퓨터/통신" ) + "</option>");
        	
		if(j_kind.equals("7")) out.println("<option value=7 selected>" + uni.convert( "서비스업" ) + "</option>");
		else out.println("<option value=7>" + uni.convert( "서비스업" ) + "</option>");

       	if(j_kind.equals("8")) out.println("<option value=8 selected>" + uni.convert( "금융" ) + "</option>");
		else out.println("<option value=8>" + uni.convert( "금융" ) + "</option>");

       	if(j_kind.equals("9")) out.println("<option value=9 selected>" + uni.convert( "건축/인테리어" ) + "</option>");
		else out.println("<option value=9>" + uni.convert( "건축/인테리어" ) + "</option>");

       	if(j_kind.equals("10")) out.println("<option value=10 selected>" + uni.convert( "방송" ) + "</option>");
		else out.println("<option value=10>" + uni.convert( "방송" ) + "</option>");
        
		if(j_kind.equals("11")) out.println("<option value=11 selected>" + uni.convert( "예술" ) + "</option>");
		else out.println("<option value=11>" + uni.convert( "예술" ) + "</option>");
        	
		if(j_kind.equals("12")) out.println("<option value=12 selected>" + uni.convert( "무역" ) + "</option>");
		else out.println("<option value=12>" + uni.convert( "무역" ) + "</option>");
        
		if(j_kind.equals("13")) out.println("<option value=13 selected>" + uni.convert( "스포츠" ) + "</option>");
		else out.println("<option value=13>" + uni.convert( "스포츠" ) + "</option>");
        
		if(j_kind.equals("14")) out.println("<option value=14 selected>" + uni.convert( "프리랜서" ) + "</option>");
		else out.println("<option value=14>" + uni.convert( "프리랜서" ) + "</option>");
        
		if(j_kind.equals("15")) out.println("<option value=15 selected>" + uni.convert( "기타" ) + "</option>");
		else out.println("<option value=15>" + uni.convert( "기타" ) + "</option>");
		
		out.println("</select></td></tr>");
      	out.println("</table>");
	  	out.println("<br><p><table border=0 width=80% cellspacing=0>");
	    out.println("<tr>");
  		out.println("<td><font size=2 color=#e0ffff><b>*</td>");
		out.println("<td><font size=2 color=#fffacd><b>" + uni.convert( "회사대표전화 " ) +"</b></font></td>");
		out.println("<td><input type=text name=c_tel size=13=></td>");
		out.println("</tr><tr>");
		out.println("<td></td>");
		out.println( uni.convert("<td><font size=2 color=#fffacd><b>팩스</b></font></td>" ));
		out.println("<td><input type=text name=c_fax size=13></td>");
        out.println("</tr>");
        out.println("<tr>");
        out.println("<td></td><td><font size=2 color=#fffacd><b>e-mail</b></font></td>");
		out.println("<td><input type=text name=c_email size=40></td>");
		out.println("</tr><tr>");
        out.println("<td></td><td><font size=2 color=#fffacd><b>homepage</b></font></td>");
		out.println("<td><input type=text name=c_homepage size=40></td>");
        out.println("</tr>");
        out.println("<tr>");
        out.println("<td><font size=2 color=#e0ffff><b>*</td>");
		out.println( uni.convert( "<td><font size=2 color=#fffacd><b>주소</b></font></td>") );
		out.println("<td><input type=text name=c_address size=40></td>");
        out.println("</tr>");
        out.println("<tr>");
        out.println("<td colspan=3 align=center><br><p>");
        out.println( uni.convert( "<input type=submit value=등록>") );
        out.println( uni.convert( "<input type=button value=취소 onClick=\"window.history.go(-1);\"></td>") );
        out.println("</tr></table>");
	 	out.println("</form></td>");
    	out.println("</tr>");
		out.println("</table></center>");
		out.println("</body>");
		out.println("</html>");
	}
		
	
	/**
	 *  registerMemberPerson - registerMemberPerson command를 처리하는 method
	 *                         입력받은 정보로 아이디 신청을 등록한다. 
	 *
	 *  @param req 		 폼에서 받은 데이터가 들어있는 곳
	 *  @param out       결과를 출력할 곳
	 *  @throws UnsupportedEncodingException  
	 */
	public void registerMemberPerson( HttpServletRequest req, PrintWriter out ) throws UnsupportedEncodingException  
	{
		// session을 얻어온다.
		HttpSession httpsession = req.getSession( false );
		// session이 없으면 등록이 제대로 되지 않았다는 말이므로 error 처리한다.
		if( httpsession == null) {
			out.println( head );
			out.println( "<h1>Error !!! </h1>" );
			out.println( end );
			return;
		}
			
		// request에서 parameter를 parsing한다.
		String s_id         = asc.convert(req.getParameter( "id" ));
		String s_passwd     = req.getParameter( "password" );
		String s_name       = asc.convert(req.getParameter( "name" ));
		String s_birth      = req.getParameter( "birth" );
		String s_ssn        = req.getParameter( "ssn" );
		String sex          = req.getParameter( "sex" );
		String desc         = asc.convert(req.getParameter( "description" ));
		// person info
		String s_address    = asc.convert(req.getParameter( "address" ));
		String beeper       = req.getParameter( "beeper" );
		String s_email      = req.getParameter( "email" );
		String homepage     = req.getParameter( "homepage" );
		String phone        = req.getParameter( "handphone" );
		String tel          = req.getParameter( "tel" );
		// job info
		String j_fax        = req.getParameter( "j_fax" );
		String s_j_tel      = req.getParameter( "j_tel" );
		String j_kind       = req.getParameter( "j_kind" );
		String j_part       = asc.convert(req.getParameter( "part" ));
		String s_j_position = asc.convert(req.getParameter( "position" ));
		// com info
		String s_c_name     = asc.convert(req.getParameter( "c_name" ));
		String c_region     = req.getParameter( "c_region" );
		String s_c_site     = asc.convert(req.getParameter( "c_site" ));
		String s_c_address  = asc.convert(req.getParameter( "c_address" ));
		String c_email      = req.getParameter( "c_email" );
		String c_fax        = req.getParameter( "c_fax" );
		String c_tel        = req.getParameter( "c_tel" );
		String c_homepage   = req.getParameter( "c_homepage" );

		String id = ""; 
		String passwd = "";
		String name = ""; 
		String birth = "";  
		String ssn = ""; 
		String address = ""; 
		String email = "";    
		String c_name = ""; 
		String c_address = "";
		String j_position = "";
		String j_tel = ""; 
		String c_site = "";

		//tokenizing 해서 space문자를 없애준다.
		StringTokenizer st1 = new StringTokenizer(s_id);
		while(st1.hasMoreTokens())
			if(id.equals( "" ) ) id = st1.nextToken();
			else id = id + st1.nextToken();

		StringTokenizer st2 = new StringTokenizer(s_passwd);
		while(st2.hasMoreTokens())
			if( passwd.equals( "" ) ) passwd = st2.nextToken();
			else passwd = passwd + st2.nextToken();

		StringTokenizer st3 = new StringTokenizer(s_birth);
		while(st3.hasMoreTokens())
 			if(birth.equals( "" ) ) birth = st3.nextToken();
			else birth = birth + st3.nextToken(); 	

		StringTokenizer st4 = new StringTokenizer(s_name);
		while(st4.hasMoreTokens())
			if( name.equals( "" ) ) name = st4.nextToken();
			else name = name + st4.nextToken();

		StringTokenizer st5 = new StringTokenizer(s_ssn);
		while(st5.hasMoreTokens())
			if( ssn.equals( "" ) ) ssn = st5.nextToken();
			else ssn = ssn + st5.nextToken();

		StringTokenizer st6 = new StringTokenizer(s_address);
		while(st6.hasMoreTokens())
			if(address.equals( "" ) )  address = st6.nextToken();
			else  address= address + st6.nextToken();

		StringTokenizer st7 = new StringTokenizer(s_email);
		while(st7.hasMoreTokens())
			if(email.equals( "" ) ) email = st7.nextToken();
			else email = email + st7.nextToken();

		StringTokenizer st8 = new StringTokenizer(s_c_name);
		while(st8.hasMoreTokens())
			if( c_name.equals( "" ) ) c_name = st8.nextToken();
			else  c_name= c_name + st8.nextToken();

		StringTokenizer st9 = new StringTokenizer(s_j_position);
		while(st9.hasMoreTokens())
			if( j_position.equals( "" ) ) j_position = st9.nextToken();
			else j_position = j_position + st9.nextToken();

		StringTokenizer st10 = new StringTokenizer(s_j_tel);
		while(st10.hasMoreTokens())
			if( j_tel.equals( "" ) ) j_tel = st10.nextToken();
			else j_tel = j_tel + st10.nextToken();

		StringTokenizer st11 = new StringTokenizer(s_c_address);
		while(st11.hasMoreTokens())
			if( c_address.equals( "" ) ) c_address  = st11.nextToken();
			else c_address = c_address + st11.nextToken();

		StringTokenizer st12 = new StringTokenizer(s_c_site);
		while(st12.hasMoreTokens())
			if( c_site.equals( "" ) ) c_site  = st12.nextToken();
			else c_site = c_site + st12.nextToken();
		

		// 입력자료가 모자라면 다시 폼으로 돌아간다.
		if( ( id.equals( "" ) ) || ( passwd.equals( "" )   ) 
			|| ( name.equals( "" )  ) || ( birth.equals( "" )   ) 
			|| ( ssn.equals( "" )   ) || ( address.equals( "" )   ) 
			|| ( c_name.equals( "" )   )|| ( j_tel.equals( "" )   )   
			|| ( email.equals( "" )  ) || (c_address.equals( "" )  )
			|| ( j_position.equals( "" )   )  ) {
			// 폼으로 다시 돌아간다.
			out.println( head );
			out.println( "<center><h3>" + uni.convert( "입력자료가 부족합니다. 다시 입력하세요" ) + "<h3>" );
			out.println("<br><p><a href=\"javaScript:window.history.go(-1);\"><font size=2><b><-Back</a></center>");
			out.println( end );
			return;
		}	
		try{
			// 입력 데이터에서 공백문자를 제거한다.
			if( desc.trim() == null ) desc = "";
			if( beeper.trim() == null ) beeper = "";
			if( homepage.trim() == null ) homepage = "";
			if( phone.trim() == null ) phone = "";
			if( tel.trim() == null ) tel = "";
			if( j_fax.trim() == null ) j_fax = "";
			if( j_part.trim() == null ) j_part = "";
			if( c_email.trim() == null ) c_email = "";
			if( c_fax.trim() == null ) c_fax = "";
			if( c_tel.trim() == null ) c_tel = "";
			if( c_homepage.trim() == null ) c_homepage = "";
		} catch( Exception e ) {
		}

		// 회사가 존재하는지 확인해서 없으면 새로 만든다.
		String exist = (String)httpsession.getValue( "exist" );
		String com = null;
		String cominfo_id = null;
		//out.println( "exist : " + exist );
		Connection con = null;
		Statement stmt = null;
		ResultSet result = null;		 
					 
		//DB connection
		try {
	 		Class.forName( NameCardSite.DRIVER );
		} catch( ClassNotFoundException e) {
			out.println( head );
	    	out.println( "ClassNotFoundException: " + e.toString() );
			out.println( end );
			return;
		}
			 
		// connection열기 		 
	    try {
			con = (Connection)DriverManager.getConnection( site.URL, site.ID, site.PASSWD );
			stmt = con.createStatement();	
			con.setAutoCommit( false );
		} catch( SQLException e ) {
			out.println( head );
			out.println( "Fail to connect DB: " +  e.toString() );
			out.println( end );
			return;	
		}

		String message = "";
		String query1 = null;
		String query2 = null;
		String query3 = null;
		String query4 = null;
		String query5 = null;
		String query6 = null;
		String query7 = null;
		String query8 = null;

		try {
			if( exist.equals( "false" ) ) {
				// 새로운 회사정보 만들기
				message = uni.convert( "회사정보 입력실패 :" );
				// object생성 시간 입력
				Calendar date1 = Calendar.getInstance();
		
				String time1 = String.valueOf(date1.get(date1.YEAR)) + String.valueOf(date1.get(date1.MONTH)) + String.valueOf(date1.get(date1.DATE)) + String.valueOf(date1.get(date1.HOUR)) + String.valueOf(date1.get(date1.MINUTE)) + String.valueOf(date1.get(date1.SECOND)); 
				cominfo_id = id +time1;	
				// cominfo 저장  
				query2 = new String( "insert into ComInfo (id,address,tel,fax,homepage,email,back) values('"+uni.convert( cominfo_id )+"','"+uni.convert( s_c_address )+"','"+c_tel+"','"+c_fax+"','"+c_homepage+"','"+c_email+"','0');");	
				stmt.executeUpdate( query2 );

				// 새로운 회사만들기
				message = uni.convert( "회사만들기 실패 :" );
				// object생성 시간 입력
				Calendar date2 = Calendar.getInstance();
		
				String time2 = String.valueOf(date2.get(date2.YEAR)) + String.valueOf(date2.get(date2.MONTH)) + String.valueOf(date2.get(date2.DATE)) + String.valueOf(date2.get(date2.HOUR)) + String.valueOf(date2.get(date2.MINUTE)) + String.valueOf(date2.get(date2.SECOND)); 
				
				com = time2 + id ;

				if( c_site != null ) s_c_site = s_c_site.trim(); 
				query3 = "insert into Company (id, name, site, region, cominfo, employee) values ('" + uni.convert(com) + "', '" + uni.convert(c_name) + "', '" + uni.convert(c_site) + "', " + Integer.parseInt( c_region ) + ", '" + uni.convert(cominfo_id) + "', '1');";

				//qeury 문 실행
				stmt.executeUpdate( query3 );	
								
			} else {
				message = " update employee num ";
				com = (String)httpsession.getValue( "com" );	

				// company를 조사한다.
				query2 = "select employee from Company where id='" + uni.convert(com) + "';";
				ResultSet company = stmt.executeQuery( query2 );
				company.next();

				int employee = company.getInt( "employee" );
				employee++;
				message = " in query3 ";	
				query3 = "update Company set employee=" + employee + " where id='" + com + "';";
				stmt.executeUpdate( query3 );
				
			}

			// object생성 시간 입력
			Calendar date3 = Calendar.getInstance();
		
			String time3 = String.valueOf(date3.get(date3.YEAR)) + String.valueOf(date3.get(date3.MONTH)) + String.valueOf(date3.get(date3.DATE)) + String.valueOf(date3.get(date3.HOUR)) + String.valueOf(date3.get(date3.MINUTE)) + String.valueOf(date3.get(date3.SECOND)); 

			String personinfo_id = id +time3;	

			// PersonInfo 를 DB에 넣는다.
			message = uni.convert( "개인정보등록 실패 : " );	
			query4 = "insert into PersonInfo (id, address, tel, beeper, handphone, email, homepage, back) values ('" + uni.convert(personinfo_id) + "', '" + uni.convert(s_address) + "', '" + tel + "', '" + beeper + "', '" + phone + "', '" + email + "', '" + homepage + "', '0');";
			stmt.executeUpdate( query4 );			


			// object생성 시간 입력
			Calendar date4 = Calendar.getInstance();
		
			String time4 = String.valueOf(date4.get(date4.YEAR)) + String.valueOf(date4.get(date4.MONTH)) + String.valueOf(date4.get(date4.DATE)) + String.valueOf(date4.get(date4.HOUR)) + String.valueOf(date4.get(date4.MINUTE)) + String.valueOf(date4.get(date4.SECOND)); 

			String jobinfo_id = time4 + id;	

			// JobInfo를 DB에 넣는다.
			message = uni.convert( "직장정보등록 실패 : " );
			query5 = "insert into JobInfo (id, posi, part, tel, fax, kind, company, back) values ('" + uni.convert(jobinfo_id) + "', '" + uni.convert( j_position ) + "', '" + uni.convert( j_part ) + "', '" + j_tel + "', '" + j_fax + "', '" + Integer.parseInt(j_kind) + "', '" + uni.convert(com) + "', '0');";

			//query 문 실행
			stmt.executeUpdate( query5 );			

			// MemberPerson object생성하고 ID Request List에 넣는다.
			message = uni.convert( "아이디 신청 실패 : " );
			query6 = "insert into IDReqList (id, password, name, ssn, sex, birthday, description, personinfo, jobinfo, com_id) values ('" + uni.convert(id) + "','" + uni.convert(passwd) + "','" + uni.convert( name ) + "','" + ssn + "','" + sex + "','" + birth + "','" + uni.convert( desc ) + "','" + uni.convert(personinfo_id) + "','" + uni.convert(jobinfo_id) + "','" + uni.convert(com) + "')";
			//query 문 실행
			stmt.executeUpdate( query6 ); 	

			// connection닫기
			con.commit();
			stmt.close();
			con.close();		

			// 세션을 종료하고 등록되었으니 메일을 기다리라는 메세지 출력
			httpsession.invalidate();
		
			out.println( head );
			out.println( uni.convert("<font size=5 color=white><center><br><br><br><br>등록되었습니다. 메일을 기다리십시오.</center></font>" ));
			out.println( end );	
			out.println( "<meta http-equiv=\"refresh\" content=\"0;url=/cardSystem/userLogin.html\">" );

			
		} catch( SQLException  e) {
			out.println( head );
			out.println( uni.convert( "아이디 등록중 SQLException발생 " ) + e.toString() );
			out.println( end );
			return;
		} catch( Exception e ) {
			out.println( head );
			out.println( "fail to registerPerson " + message + e.toString()  );
			out.println( end );
			return;
		}
	}
	
	
	///////////////////////////// ID Req List 보여주기 ///////////////////////
	
	/** 
	 *  showIDReqList - 관리자에게 ID생성을 위해 ID Request list를 보여준다.
	 * 
	 *  @param req  폼에서 입력받은 데이터가 들어있는 곳 
	 *  @param out 	결과를 출력할 곳
	 */
	public void showIDReqList( HttpServletRequest req, PrintWriter out ) throws UnsupportedEncodingException {
		HttpSession httpsession = req.getSession( false );
		if( httpsession == null ) {
			out.println( "<h1>Unauthorized User! </h1>" );
			return;
		}
		
		try {
			ResultSet idReqList = dbmanager.getIDReqList();
			ResultSet person = null;
			ResultSet job = null;
			ResultSet com = null;
	
			// IDReqList에 있는 Person object list를 보여준다.
			out.println( head ); 
			out.println( "<center><form method=post action=/namecard/AdminServlet>" );
			out.println( "<input type=hidden name=cmd value=showPersonInfo>" );	
			out.println( "<br><br><br><table>" );
			out.println( "<tr><td></td><td><font size=3 color=#fffacd>ID</td><td><font size=3 color=#fffacd>" + uni.convert( "이름" ) + "</td><td><font size=3 color=#fffacd>" ); 
			out.println( uni.convert( "회사" ) + "</td><td><font size=3 color=#fffacd>" + uni.convert( "지역" ) + "</td><td><font size=3 color=#fffacd>" );
			out.println( uni.convert( "부서" ) + "</td><td><font size=3 color=#fffacd>" + uni.convert( "직책" ) + "</td></tr>" );	
			int num = 0;

			while( idReqList.next() ) {
				num++;
				String id = idReqList.getString( "id" );
				// ID에 input type을 hidden으로 해서 element number를 넘겨준다. 
				out.println( "<tr><td><input type=radio name=id value=\""
					+ id
					+ "\"></td><td>" + id 
					+ "</td><td>" + idReqList.getString( "name" ) 
					+ "</td><td>" + idReqList.getString( 3 ) 
					+ "</td><td>" + idReqList.getString( "site" ) 
					+ "</td><td>" + idReqList.getString( "part" ) 
					+ "</td><td>" + idReqList.getString( "posi" ) 
					+ "</td></tr>" );
											
			}
			out.println( "</table><br><font size=2 color=#fffacd>" );
			out.println( num + uni.convert( "명의 신청자가 있습니다.</font>") );
			out.println(  uni.convert( "<table><td><input type=submit value=보기></td>"  ));
			out.println(  uni.convert( "<td><iput type=reset value=취소></td></table>" ) );
			out.println( "</form></center>" );
			out.println( end );	
		} catch( SQLException e ){
			out.println( head );
	   		out.println(  uni.convert( "DB와의 연결에 문제가 있습니다. 다시 시도하세요" ) + e.toString() );
			out.println( end );
	   		return;		
		} catch( CommandException e ) {
			out.println( head );
			out.println( uni.convert( "DB에서 리스트 얻어오는 것이 실패했습니다. 다시 시도하세요!" ) + e.toString());
			out.println( end );

		} catch( Exception e ){
			out.println( head );
			out.println( "fail to showIDReQList" + e.toString() );
			out.println( end );
		}
		
	}
	
	/** 
	 *  showPersonInfo -  showPersonInfo command를 처리하는 method 
	 * 				      ID Requestlist에서 아이디를 클릭하면 상세정보를 보여준다.
	 * 
	 *  @param req 	폼에서 받은 데이터를 가지고 있는 곳
	 *  @param out 	결과를 출력할 곳
	 */
	public void showPersonInfo( HttpServletRequest req, PrintWriter out )  throws UnsupportedEncodingException {
		HttpSession httpsession = req.getSession( false );
		if( httpsession == null ) {
			out.println( head );
			out.println( "<h1>Unauthorized User! </h1>" );
			out.println( end );
			return;
		}
		String id = null;
		// request에서 parameter parsing 한다.
		try{
			id = req.getParameter( "id" ) ;
			if( id == null ) {
				out.println( head );
				out.println( "<br><br><br><font size=3 color=white>" );
				out.println( uni.convert( "<center>더 이상 볼 정보가 없습니다. 돌아가십시오.<br>" )); 
				out.println("<a href=\"javaScript:window.history.go(-4);\"><font size=2><b><-Back</a></center></font>");	 
				out.println( end );
				return;
			}
		} catch( Exception e ) {
			out.println( head );
			out.println( "<br><br><br>" );
			out.println( uni.convert( "<center>더 이상 볼 정보가 없습니다. 돌아가십시오.<br>" )); 
			out.println("<a href=\"javaScript:window.history.go(-1);\"><font size=2><b><-Back</a></center>");	 
			out.println( end );
			return;
		}

		ResultSet member =  null;

		try {
			// IDReqList에서 선택된 사람의 정보를 DB에서 얻는다. 
			member = dbmanager.getIDRequest( id );
			member.next();
			out.println( head );
			out.println( "<br><br><br>" );
			//out.println( "id : " + member.getString( "id" ) );	
			out.println( "<center> " );
			out.println( uni.convert( "<h3><font color=#fffacd>ID Request List에 등록된<font color=white> " ) + id + uni.convert( "</font>의 정보</h3><br><br> ") );
			out.println( "<table>" );
			out.println( "<tr><td>" + uni.convert( "이름</td><td> :</td><td> " )+ member.getString( 2 ) );
			out.println( "</td></tr><tr><td>" + uni.convert( "근무지</td><td> :</td><td> " ) + member.getString( 3 ) );
			out.println( "</td></tr><tr><td>" + uni.convert( "부서 </td><td>:</td><td> " ) + member.getString( 4 ) );
			out.println( "</td></tr><tr><td>" + uni.convert( "직책</td><td> :</td><td> " ) + member.getString( 5 ) );
			out.println( "</td></tr><tr><td>" + uni.convert( "주소</td><td> :</td><td> ") + member.getString( 6 ) );
			out.println( "</td></tr><tr><td>" + uni.convert( "메일</td><td> :</td><td> ") + member.getString( 7 ) );
			out.println( "</td></tr><tr><td>" + uni.convert( "전화</td><td> :</td><td> ") + member.getString( 8 ) );
			out.println( "</td></tr></table> " );
			out.println( "<form method=post action=/namecard/AdminServlet >" );
			out.println( "<input type=hidden name=cmd value=addMemberPerson>" );
			out.println( "<input type=hidden name=add_id value=" + member.getString( 1 ) + "> " );
			out.println( uni.convert( "<input type=submit value=등록></form>"  ));
			out.println( "<form method=post action=/namecard/AdminServlet> " );
			out.println( "<input type=hidden name=cmd value=deleteIDRequest>" );
			out.println( "<input type=hidden name=add_id value="+member.getString( 1 ) +">" );
			out.println( uni.convert( "<input type=submit value=취소></form> "  ));
			out.println( "</center>" );
			out.println( end );
		} catch( SQLException e ){
				out.println( head );
	    		out.println( uni.convert( "DB와의 연결에 문제가 있습니다. 다시 시도하세요"  ) + e.toString() );
				out.println( end );
	    		return;
		} catch( CommandException e ){
				out.println( head );
    			out.println( e.toString() );
				out.println( end );
    			return;
		} catch( Exception e ){
			out.println( e.toString() );
		}

	}

	
	/////////////////////////////////  ID 등록  ///////////////////////////////
	
	/**
	 *  addMemberPerson - addMemberPerson command를 처리하는 method
	 *                    IDReqList에서 정식 사용자로 아이디를 등록시킨다.	
	 * 
	 *  @param req 	form에서 입력받은 데이터가 들어있는 곳
	 *  @param out 	결과를 보여줄 곳
	 */
	public void addMemberPerson( HttpServletRequest req, PrintWriter out )throws UnsupportedEncodingException {
		HttpSession httpsession = req.getSession( false );
		if( httpsession == null ) {
			out.println( head );
			out.println( "<h1>Unauthorized User! </h1>" );
			out.println( end );
			return;
		}	
		String id = null;
	
		try {	
			// request에서 parameter parsing 한다.
			id = asc.convert( req.getParameter( "add_id" ) );
		} catch( NullPointerException e ) {
			out.println( head );
			out.println( uni.convert( "등록할 사람이 없습니다. 다시 돌아가세요" ));
			out.println("<a href=\"javaScript:window.history.go(-1);\"><font size=2><b><-Back</a></center>");	
			out.println( end );
			return;
		}
		//out.println( uni.convert(id) + uni.convert( " 를  등록하려함" ));

		//DB connection
	 	try {
	 		Class.forName( site.DRIVER );
		} catch( ClassNotFoundException e) {
	    	out.println( "ClassNotFoundException: " + e.toString() );
		}
			 
		Connection con = null;
		Statement stmt = null;
		ResultSet result = null;
	
	    try {
			con = (Connection)DriverManager.getConnection( site.URL, site.ID, site.PASSWD );
			stmt = con.createStatement();
			con.setAutoCommit( false );	
		} catch( SQLException e ) {
			out.println(" fail to connec DB " +  e.toString() );
		}

		//새로운 jobInfo를 DB에 추가한다.
		try{
			result = dbmanager.getIDInfo( uni.convert(id) );
			result.next();	
			//query statement
			String query1 = "insert into MemberPerson (id, password, name, ssn, sex, birthday, description, personinfo, jobinfo, com_id) values ('" + result.getString( "id" ) + "', '" + result.getString( "password" ) + "', '" + result.getString( "name" ) + "', '" + result.getString( "ssn" ) + "', '" + result.getString( "sex" ) + "', '" + result.getString( "birthday" ) + "', '" + result.getString( "description" ) + "', '" + result.getString( "personinfo" ) + "', '" + result.getString( "jobinfo" ) + "', '" + result.getString( "com_id" ) + "');";

			//query 문 실행
			stmt.executeUpdate( query1 ); 

			//query statement
			String query2 = "delete from IDReqList  where id='" + uni.convert(id) + "';";

			//query 문 실행
			stmt.executeUpdate( query2 ); 

			if( !result.getString( "com_id" ).endsWith( id ) ) {
				// 이미 있는 회사의 직원수를 늘린다.
				String query3 = "select employee from Company where id='" + result.getString( "com_id" ) + "';";
				ResultSet com = stmt.executeQuery( query3 );
				com.next();
				int employee = com.getInt( "employee" );
				employee++;
		
				String query4 = "update Company set employee='" + employee + "' where id='" + result.getString( "com_id" )+ "';";
				stmt.executeUpdate( query4 );
			}
	
			// commit하고 AutoCommit를 false로 한다.	
			con.commit();
			out.println( "<center><br><br>id : " + uni.convert(id + " 으로 등록하였습니다.</center>" ));	
			stmt.close();
			con.close();
			showIDReqList( req, out );
		}catch( SQLException e ){
			out.println( "fail to query in query" + e.toString() );
		}catch( Exception e ){
			out.println( "fail to addIDReq " + e.toString() );
		}	
		
	}
	
	/** 
	 *  deleteIDRequest - ID신청을 DB에서 삭제하는 method 
	 *
	 *  @param req	폼에서 받은 데이터가 들어있는 곳
	 *  @param out 	결과를 출력하는 곳
	 *  @throws  ServletException 
	 *  @throws  IOException  
	 */
	public void deleteIDRequest( HttpServletRequest req, PrintWriter out ) 
	throws ServletException, UnsupportedEncodingException {
		HttpSession httpsession = req.getSession( false );
		if( httpsession == null ) {
			out.println( head );
			out.println( "<h1>Unauthorized User! </h1>" );
			out.println( end );
			return;
		}

		// request에서 parameter parsing 한다.
		String id = asc.convert( req.getParameter( "add_id" ) );
		
		// vector 에서 object를 삭제한다.
		try {
			dbmanager.cancelIDRequest( uni.convert(id) );
			showIDReqList( req, out );	
		} catch( CommandException e ) {
			out.println( head );
			out.println( uni.convert( "DB 와 접속에 문제가 있습니다. 다시 시도하세요" ) + e.toString() );
			out.println( end );
			return; 		
		}	catch( Exception e ) {
			out.println( uni.convert( "아이디 등록요청 삭제가 실패하였습니다." ) + e.toString() );			
		}
		
	}

	////////////////////////////  가입해제  //////////////////////////////

	/**
	 *  checkDeleteID - checkDeleteID command를 처리하는 method
	 *                  정말로 아이디를 삭제할 것인지 확인한다.
	 *
	 *  @param req  폼에서 입력받은 데이터가 들어있는 곳
	 *  @param out  결과를 출력할 곳
	 */
	public  void checkDeleteID( HttpServletRequest req, PrintWriter out ) throws UnsupportedEncodingException {
		// 권한 검사
		HttpSession httpsession = req.getSession( false );
		if( httpsession == null ) {
			out.println( head );
			out.println( "<h1>Unauthorized User! </h1>" );
			out.println( end );
			return;
		}
		
		//ID삭제하겠다는 메세지를 보여준다.
		out.println("<html><head><title></title></head>");
		out.println("<body background=/bg.jpg><table width=600 border=0 cellspacing=0>");
		out.println("<tr><td align=center>");
		
		out.println(uni.convert("<br><p><img src=/icon.gif><font size=3 color=#ffff00><b>ID를 삭제하려 합니다.<br>"));
		out.println(uni.convert("정말 삭제하시겠습니까?<Br><p></td></tr>"));
		out.println("<tr><td align=center>");
		out.println("<form method=post action=/namecard/UserServlet>");
		out.println("<input type=hidden name=cmd value=deleteMemberPerson>");
		out.println("<input type=submit value=Yes>");
		out.println("<input type=button value=No onClick=\"window.history.go(-1);\"></form></td></tr>");
		out.println("</table></body></html>");
	}


	
	/**
	 *  deleteMemberPerson - deleteMemberPerson command 처리하는 method
	 *			             사용자가 가입해제를 원할 경우 DB에서 정보를 삭제한다.
	 *
	 *  @param req  폼에서 입력받은 데이터가 들어있는 곳
	 *  @param out  결과를 출력할 곳
	 */
	public  void deleteMemberPerson( HttpServletRequest req, PrintWriter out )throws UnsupportedEncodingException { 
		// 사용자 권한검사를 한다.
		HttpSession httpsession = req.getSession( false );
		if( httpsession == null ) {
			out.println( head );
			out.println( "Unauthorized User !" );
			out.println( end );
			return;
		}
	
		String id = (String)httpsession.getValue( "id" );
		
		try {
			// id를 지우기 전에 회사를 먼저 체크한다.
			dbmanager.deleteMemberPerson( id );
			out.println("<html><head><title></title></head><body background=/bg.jpg>");
			out.println("<font size=3 color=#fffacd><b>" + uni.convert( "아이디가 삭제되었습니다!" ) );
			out.println("<br><p><a href=\"http://ant/cardSystem/userLogin.html\"><font size=2 color=#fffacd><b><-Back</a>" );
			out.println("</body></html>");
			
		} catch( CommandException e ) {
			out.println( uni.convert("아이디 삭제가 실패하였습니다. 다시 시도하세요" ) + e.toString() );
		}	
	
	}


}
