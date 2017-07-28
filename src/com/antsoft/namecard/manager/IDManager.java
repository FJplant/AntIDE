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


	/////////////////////////////////  Admin ����  /////////////////////////////
	
	/**
	 *  verifyAdmin - verifyAdmin command�� ó���ϴ� method
	 *  			  DB�� ����ִ� ������ �н������ �Է¹��� ����Ÿ�� �´��� Ȯ���Ѵ�. 
	 *
	 *  @param req  ������ �Է¹��� �����Ͱ� ����ִ� ��
	 *  @param out  ����� ����� ��
	 */
	public boolean verifyAdmin(HttpServletRequest req, PrintWriter out) throws UnsupportedEncodingException { 
		boolean result=false;
		String id = req.getParameter("id");
		String pass = req.getParameter("password");
					
		try{
			//����Ÿ���̽� ���� ���� ���̵�� �н������ 	
			ResultSet admin = dbmanager.getMemberPerson("admin");	
			if( !admin.next() ) {
				out.println( head );
				out.println( uni.convert ("������ ������ ��µ� �����߽��ϴ�. �ٽ� �õ��ϼ���"  ));
				out.println( end );
				return false;
			}
	
			if((id.equals("admin")) && (pass.equals(admin.getString("password")))){
				System.out.println("session access");	
				
				//httpSession�� ���� Ű �ֱ�
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
			out.println( uni.convert( "DB���� ���ӿ� ������ �ֽ��ϴ�. �ٽ� �õ��ϼ���" )  + e.toString() );
			out.println( end );
			return false;
		} catch( CommandException e ) {
			out.println( head );
			out.println( uni.convert( "DB���� ���ӿ� ������ �ֽ��ϴ�. �ٽ� �õ��ϼ���" ) + e.toString());
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
	 *  changePassword -  changePassword command ó���ϴ� method
	 *					 admin�� password�� �ٲ޴�.
	 *
	 *  @param req  ������ �Է¹��� �����Ͱ� ����ִ� ��
	 *  @param out  ����� ����� ��
	 */
	public void changePassword( HttpServletRequest req, PrintWriter out ) throws UnsupportedEncodingException {
		System.out.println( "admin�� ��ȣ�� �ٲٴ� ��..." );
		// ���� �˻�
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
		
		// �Է��� �߸� �� ��� �޼����� ����Ѵ�.
		if( ( newpass == null ) || ( checkpass == null ) ) {
			out.println( head );
			out.println( "�Է��� �ٸ��� �ʽ��ϴ�. �ٽ� �Է��Ͻð� �õ��Ͻʽÿ�." );
			out.println( end );
			return;
		}
		
		String id = (String)httpsession.getValue("id");
		
		boolean result;
		
		try{	
			// admin objcet���� password�� �ٲ۴�.
			dbmanager.updatePassword( "admin", newpass );
			result = true;	
		} catch( CommandException e ) {
			out.println( head );
			out.println( uni.convert( "�н����� ������ �����߽��ϴ�. �ٽ� �õ��ϼ���" ) + e.toString() );
			out.println( end );
			return;
		}
		
	 
		if( result ) {
			// �����Ǿ��ٴ� �޼����� �����ش�.
			out.println( head );
			out.println( uni.convert( "<h2>��ȣ�� " + newpass + "�� �����Ǿ����ϴ�.</h2>" ));
			out.println( end );
			return;
		}
		// �Է��� �߸��Ǿ��ٴ� �޼����� �����ְ� �ٽ� ���� �����ش�.
		out.println( uni.convert( "<h2>�Է��� �ùٸ��� �ʽ��ϴ�. �ٽ� �õ����ֽʽÿ�.</h2>"  ));
		
			
	}



	
	//////////////////////////////////  Login  //////////////////////////////////

	/**
	 *  login - login command �� ó���ϴ� method
	 *          ����� �����Ѵ�.
	 *
	 *  @param  req  ������ �Է¹��� �����Ͱ� ����ִ� ��
	 *  @param  out  ����� ����� ��
	 */
	public void login( HttpServletRequest req, PrintWriter out ) throws UnsupportedEncodingException{
		// ���� �˻�
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
	
		//tokenizing �ؼ� space���ڸ� �����ش�.
		StringTokenizer st1 = new StringTokenizer(s_id);
		while(st1.hasMoreTokens())
			if(id == null) id = st1.nextToken();
			else id = id + st1.nextToken();

		StringTokenizer st2 = new StringTokenizer(s_passwd);
		while(st2.hasMoreTokens())
 			if(passwd == null) passwd = st2.nextToken();
			else passwd = passwd + st2.nextToken(); 
		
		// �Է��� �߸� �� ��� �޼����� ����Ѵ�.
		if( id == null ) {
			out.println( head );
			out.println( uni.convert( "���̵� �����ϴ�. ���̵� �Է��ϼ���"  ) );
			out.println( end );
			return;
		}
		if( passwd == null ) {
			out.println( head );
			out.println(uni.convert( "�н����尡 �����ϴ�. �н����带 �Է��ϼ���." ) );
			out.println( end );
			return ;
		}

		ResultSet member = null;
		boolean result = false;	
		//�Է¹��� id�� ������ ã��
		try {
			member = dbmanager.getMemberPerson( uni.convert(id) );			
			if( !member.next()  ) {
				out.println( head );
				out.println( uni.convert( "��ϵ��� ���� ���̵��Դϴ�. ���� ����� �ϼ���" ) );
				out.println( end );
				return;
			}
			if(  uni.convert(passwd).equals( member.getString( "password" ) ) ) {
				httpsession.putValue( "id", uni.convert(id) );
				result = true;
			}				
		} catch( SQLException e ){
	    		out.println(  uni.convert( "DB���� ���ῡ ������ �ֽ��ϴ�. �ٽ� �õ��ϼ���" ) );
		} catch( CommandException e ) {
			out.println( uni.convert( "DB���� ���ῡ ������ �ֽ��ϴ�. �ٽ� �õ��ϼ���" ) );
		}

		if( result ) {
			boxmanager.showDirList( req, out );
		} else {
			out.println(uni.convert( "<center><font size=4><b>password�� ���� �ʽ��ϴ�.<br>") );
			out.println( uni.convert( "�ٽ� �Է��� �ֽʽÿ�.<br><p>") );
			out.println("<a href=\"javaScript:window.history.go(-1);\"><font size=2><b><-Back</a></center>");
		}
		
	}

	////////////////////////////// Logout  ///////////////////////////////

	public void logout( HttpServletRequest req, PrintWriter out ) throws ServletException , UnsupportedEncodingException {
	// ���� �˻�
		HttpSession httpsession = req.getSession( false );
		if( httpsession == null ) {
			out.println( head );
			out.println( "<h1>Unauthorized User! </h1>" );
			out.println( end );
			return;
		}	

		httpsession.invalidate();
		
		out.println( head );
		out.println( uni.convert("<font size=5 color=white><center><br><br><br><br>�ȳ��� ���ʽÿ�.</center></font>" ));
		out.println( end );	
				out.println( "<meta http-equiv=\"refresh\" content=\"0;url=/cardSystem/userLogin.html\">" );
	}

	
	//////////////////////////////  ID ��û  //////////////////////////////
	
	/**
	 *  checkCompany - checkCompany command ó���ϴ� method
	 *                 ID��û������ �Էµ� ������ 
	 *                 ȸ�簡 �ִ����� Ȯ���ؼ� �ٽ� ��û���� �����ش�.
	 *
	 *  @param req  ������ �Է¹��� �����͸� ���� �ִ� ��
	 *  @param out  ����� ����� ��
	 *  @throws ServletException req���� �������� �ϴ� ���� ���� ��
	 *  @throws UnsupportedEncodingException
	 */
	public void checkCompany( HttpServletRequest req, PrintWriter out ) 
	            throws ServletException, UnsupportedEncodingException {
		// ���� �˻�
		HttpSession httpsession = req.getSession( true );
		if( httpsession == null ) {
			out.println( head );
			out.println( "<h1>Unauthorized User! </h1>" );
			out.println( end );
			return;
		}
		
		// request�κ��� ȸ���̸��� ����, ������ �˾Ƴ���.
		String icomname = asc.convert(req.getParameter( "s_name" ));
		String comregion  = req.getParameter( "s_region" ); 
		String icomsite = asc.convert(req.getParameter( "s_site" )); 

		String comname = null;
		String comsite = null;
		String message = "tokenizing...";
			
		//tokenizing �ؼ� space���ڸ� �����ش�.
		StringTokenizer st1 = new StringTokenizer(icomname);
		while(st1.hasMoreTokens())
			if(comname == null) comname = st1.nextToken();
			else comname = comname + st1.nextToken();

		StringTokenizer st3 = new StringTokenizer(icomsite);
		while(st3.hasMoreTokens())
 			if(comsite == null) comsite = st3.nextToken();
			else comsite = comsite + st3.nextToken(); 
		if( comsite == null ) comsite="";
	

		// �Է��� �߸��Ǿ��� ��� ���� �޼��� ���
		if( comname == null ) {
			out.println( head );
			out.println( uni.convert( "ȸ���̸���  �߸��Ǿ����ϴ�. �ٽ� �Է��Ͻʽÿ�" ) );
			out.println( end );
			return;
		}
		message = " in time calculation";	
		// object���� �ð� �Է�
		Calendar date = Calendar.getInstance();
		
		String id = String.valueOf(date.get(date.YEAR)) + String.valueOf(date.get(date.MONTH)) + String.valueOf(date.get(date.DATE)) + String.valueOf(date.get(date.HOUR)) + String.valueOf(date.get(date.MINUTE)) + String.valueOf(date.get(date.SECOND));

		httpsession.putValue("id", id);
		
		// ComInfo�� ���ؼ� ȸ���̸��� �������� ȸ�簡 �ִ��� Ȯ���Ѵ�.
		ResultSet com = null;
		String com_num = null;
		
		try{
			message = " searchCompany";
			// searchCompany method call�Ѵ�.	
			com = dbmanager.searchCompany( uni.convert(comname), Integer.parseInt(comregion), uni.convert(comsite) );
		
			// ����Ϸ��� ȸ�簡 �̹� �����ϴ� ȸ������ üũ�Ѵ�.
			if( com.next() ) {           // ������ ȸ���� ���
				message = " com is not null" ;
				com_num = com.getString( "id" );		
				addPersonForm1( req, out, com_num );
			} else {  
				message = " com is not exist";
				// ���ο� ȸ���� ���
				addPersonForm2( req, out );
			} 
		} catch( SQLException e) {
				out.println( head );
	    		out.println( uni.convert( "DB���� ���ῡ ������ �ֽ��ϴ�. �ٽ� �õ��ϼ���" ) + e.toString()  );
				out.println( end );
	    		return;
		} catch( CommandException e) {
				out.println( head );
    			out.println( uni.convert( "DB���� ���ῡ ������ �ֽ��ϴ�. �ٽ� �õ��ϼ���" ) + e.toString() );
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
	 *  addPersonForm1 -  ���̵� ����� ���� �� �����Ѵ�.(�̹� ȸ�簡 �����ϴ� ���)
	 *  
	 *  @param req  form�� �Էµ� ������ ��Ƶδ� ��
	 *  @param out  ����� ����� ��
	 *  @param num  IC��û�ڰ� ����Ϸ��� ȸ��
	 *  @throws ServletException  req�κ��� ������Ⱑ ������ ��
	 *  @throws UnsupportedEncodingException  
	 */
	public void addPersonForm1( HttpServletRequest req, PrintWriter out, String num ) 
	throws ServletException, UnsupportedEncodingException {
		// request���� parameter�� parsing�Ѵ�.
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

		//tokenizing �ؼ� space���ڸ� �����ش�.
		StringTokenizer st1 = new StringTokenizer(s_id);
		while(st1.hasMoreTokens())
			if(id == null) id = st1.nextToken();
			else id = id + st1.nextToken();
		if( id.length() > 10 ) {
			out.println( head );
			out.println( uni.convert( "ID�� 10�� �̳��� ���ּ���" ) );
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
			out.println( uni.convert( "ssn�� '-'���� 13�ڷ� ���ּ���" ) );
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

		// �Է��ڷᰡ ���ڶ�� �ٽ� ������ ���ư���.
		if( ( id == null ) || ( passwd == null ) || ( checkPasswd == null )  
				|| ( name == null ) || ( birth == null ) 
				|| ( ssn == null ) || ( address == null ) 
				|| ( c_name == null )  || ( j_position == null )
				|| ( j_tel == null ) || ( email == null ) ) {
			// ������ �ٽ� ���ư���.
			out.println( head );
			out.println( "<center><h3>" + uni.convert( "�Է��ڷᰡ �����մϴ�. �ٽ� �Է��ϼ���" ) + "<h3>" );
			out.println("<br><p><a href=\"javaScript:window.history.go(-1);\"><font size=2><b><-Back</a></center>");
			out.println( end );
			return;
		}

		//�н����� Ȯ���ϱ�
		if(!s_passwd.equals(checkPasswd)){
			out.println( head );
			out.println("<center><h3>" + uni.convert( "Ȯ�ο� �н������ ��ġ���� �ʽ��ϴ�.�ٽ� �Է��� �ּ���." ) + "</h3>");
			out.println("<br><p><a href=\"javaScript:window.history.go(-1);\"><font size=2><b><-Back</a></center>");
			out.println( end );
			return;
		}
		
		
		// �̹� �����ϴ� ȸ�簡 �ִٴ� flag�� Company object�� session�� �ִ´�.
		HttpSession httpsession = req.getSession( true );
		if( httpsession == null ) {
			out.println( head );
			out.println( "Unauthorized User!!" );
			out.println( end );
		    	return;	    
		}
		
		httpsession.putValue( "exist", "true" );
		httpsession.putValue( "com", num );
		
		// ȸ�� ������ȣ�� ���ؼ� ȸ�������� �����´�.
		try {
			ResultSet com = dbmanager.getCompany( num );
			if( !com.next() ) {
				out.println( head );
				out.println( uni.convert( "DB���� ���ῡ ������ �ֽ��ϴ�." ) );
				out.println( end );
				return;
			}
			ResultSet info = dbmanager.getComInfo( num );	
			if( !info.next() ) {
				out.println( head );
				out.println( uni.convert( "DB���� ���ῡ ������ �ֽ��ϴ�." ) );
				out.println( end );
				return;
			} 

			String c_tel = info.getString( "tel" );
			String c_fax = info.getString( "fax" );
			String c_address = info.getString("address");
			String c_email = info.getString( "email" );
			String c_homepage = info.getString( "homepage" );

			// �̹� �Է��� �������� �����ְ� action�� ��ٸ���. 
			out.println("<html>");
			out.println("<head>");
			out.println("<title>id" + uni.convert( "���" ) + "</title>");
			out.println("</head>");
			out.println("<body background=/bg.jpg>");
			out.println("<table border=0 width=600 cellspacing=0>");
  			out.println("<tr>");
			out.println("<form method=post action=/namecard/UserServlet >");
      		out.println("<input type=hidden name=cmd value=registerMemberPerson>");
			out.println("<input type=hidden name=id value="+uni.convert(id)+">");
    		out.println("<td height=28 align=center><font size=4 color=#ffff00><b><img src=/icon.gif>" + uni.convert( "��������" ) + "</b></font></td>");
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
       		out.println("<td><font size=2 color=#fffacd><b>" + uni.convert( "�̸�" ) + "</b></td>");
   			out.println("<td><input type=text name=name size=5 maxlength=5 value=\""+uni.convert(name)+"\"></td>");
			out.println("</tr><tr>");
   			out.println("<td><font size=2 color=#fffacd><b>" + uni.convert( "�ֹε�Ϲ�ȣ" ) + "</b></td>");
   			out.println("<td> <input type=text name=ssn size=13 maxlength=13 value=\""+ssn+"\"></td>");
       		out.println("</tr>");
			out.println("<tr>");
       		out.println("<td><font size=2 color=#fffacd><b>" + uni.convert( "�������" ) + "</b></td>");
   			out.println("<td> <input type=text name=birth size=10 maxlength=10 value=\""+birth+"\"></td>");
			out.println("</tr><tr>");
   			out.println("<td><font size=2 color=#fffacd><b>" + uni.convert( "����" ) + "</b></td>");
			if(sex.equals("m")){
    			out.println("<td><font size=2 color=#fffacd><b>" + uni.convert( "��" ) + "<input type=radio name=sex checked value=m>" + uni.convert( "��" ) + "<input type=radio name=sex value=f></b></td>");
			}else{
				out.println("<td><font size=2 color=#fffacd><b>" + uni.convert( "��" ) + "<input type=radio name=sex value=m>" + uni.convert( "��" ) + "<input type=radio name=sex checked value=f></b></td>");
			}
       		out.println("</tr>");
  			out.println("<tr>");
       		out.println("<td><font size=2 color=#fffacd><b>" + uni.convert( "�ּ�" ) + "</b></td>");
    		out.println("<td> <input type=text name=address size=40 value=\""+uni.convert(s_address)+"\"></td>");
			out.println("</tr><tr>");
  	   		out.println("<td><font size=2 color=#fffacd><b>" + uni.convert( "��ȭ" ) + "</b></td>");
    		out.println("<td> <input type=text name=tel size=12 value=\""+tel+"\"> </td>");
       		out.println("</tr>");
  			out.println("<tr>");
       		out.println("<td><font size=2 color=#fffacd><b>" + uni.convert( "�߻�" ) + "</b></td>");
    		out.println("<td> <input type=text name=beeper size=13 value=\""+beeper+"\"></td>");
			out.println("</tr><tr>");
       		out.println("<td><font size=2 color=#fffacd><b>" + uni.convert( "�޴���" ) + "</b></td>");
    		out.println("<td> <input type=text name=handphone size=13 value=\""+phone+"\"></td>");
       		out.println("</tr>");
  			out.println("<tr>");
       		out.println("<td><font size=2 color=#fffacd><b>" + uni.convert( "Ȩ������" ) + "</b></td>");
    		out.println("<td><input type=text name=homepage size=40 value=\""+homepage+"\"></td>");
			out.println("</tr><tr>");
    		out.println("<td><font size=2 color=#fffacd><b>e-mail<b></td>");
    		out.println("<td><input type=text name=email size=40 value=\""+email+"\"></td>");
       		out.println("</tr>");
  			out.println("<tr>");
       		out.println("<td><font size=2 color=#fffacd><b>" + uni.convert( "����" ) + "</b></td>");
    		out.println("<td><input type=text name=position size=20 value=\""+uni.convert(s_j_position)+"\"></td>");
			out.println("</tr><tr>");
    		out.println("<td><font size=2 color=#fffacd><b>" + uni.convert( "�μ�" ) + "</b></td>");
    		out.println("<td><input type=text name=part size=20 value=\""+uni.convert(j_part)+"\"></td>");
       		out.println("</tr>");
  			out.println("<tr>");
       		out.println("<td><font size=2 color=#fffacd><b>" + uni.convert( "ȸ�簳����ȭ" ) + "</b></td>"); 
			out.println("<td> <input type=text name=j_tel size=13 value=\""+j_tel+"\"></td>");
			out.println("</tr><tr>");
    		out.println("<td><font size=2 color=#fffacd><b>" + uni.convert( "ȸ���ѽ�" ) + "</b></td>");
    		out.println("<td><input type=text name=j_fax size=13 value=\""+j_fax+"\"></td>");
       		out.println("</tr>");
			out.println("<tr>");
			out.println("<td><font size=2 color=#fffacd valign=top><b>description</b></font></td>");
			out.println("<td><textarea name=description cols=50 rows=4>"+uni.convert(desc)+"</textarea></td>");
			out.println("</tr>");
      		out.println("</table><br><p></td>");
    		out.println("</tr> ");
    		out.println("<tr>");
      		out.println("<td align=center height=28><b><font size=4 color=#ffff00><img src=/icon.gif>" + uni.convert( "ȸ������" ) + "</b></td>");
    		out.println("</tr>");
    		out.println("<tr>  ");
  	  		out.println("<td valign=top align=center><br><p>");   
       		out.println("<table border=0 width=80% cellspacing=0>");
			out.println("<tr>");
       		out.println("<td width=90><font size=2 color=#fffacd><b>" + uni.convert( "ȸ���̸�" ) + "</b></td>");
 			out.println("<td><font size=2><b> "+uni.convert(s_c_name)+"<input type=hidden name=c_name value=\"" + uni.convert(s_c_name) + "\"></td></tr>");
			out.println("<tr>");
      		out.println("<td><font size=2 color=#fffacd><b>" + uni.convert( "����" ) + "</b></td>");
      		out.println("<td><font size=2><b>");
			
			if(c_region.equals("1")) out.println( uni.convert( "����Ư����" ) );
	   		if(c_region.equals("2")) out.println( uni.convert( "�λ걤����" ) );
			if(c_region.equals("3")) out.println( uni.convert( "�뱸������" ) );
			if(c_region.equals("4")) out.println( uni.convert( "��õ������" ) );
			if(c_region.equals("5")) out.println( uni.convert( "���ֱ�����" ) );
			if(c_region.equals("6")) out.println( uni.convert( "��걤����" ) );
    		if(c_region.equals("7")) out.println( uni.convert( "����������" ) );
			if(c_region.equals("8")) out.println( uni.convert( "���" ) );
			if(c_region.equals("9")) out.println( uni.convert( "����" ) );
			if(c_region.equals("10")) out.println( uni.convert( "���" ) );
			if(c_region.equals("11")) out.println( uni.convert( "�泲" ) );
			if(c_region.equals("12")) out.println( uni.convert( "����" ) );
			if(c_region.equals("13")) out.println( uni.convert( "����" ) );
			if(c_region.equals("14")) out.println( uni.convert( "���" ) );
			if(c_region.equals("15")) out.println( uni.convert( "�泲" ) );
			if(c_region.equals("16")) out.println( uni.convert( "����" ) );
 			out.println("<input type=hidden name=c_region value=\"" + c_region + "\"></td></tr>");
			
      		if( !c_site.equals( "" ) ) {	
				out.println("<tr>");
       			out.println("<td><font size=2 color=#fffacd><b>" + uni.convert( "����" ) + "</b></td>");
       			out.println("<td><font size=2><b>" + uni.convert( com.getString( "site" ) ) );
				out.println("<input type=hidden name=c_site value=\""+com.getString( "site" )+"\"></td>");
				out.println("</tr>");
			} else {
				out.println( "<input type=hidden name=c_site value=\"" + c_site + "\">" );
			} 
			out.println( "<tr>" );	
			out.println("<td><font size=2 color=#fffacd><b>" + uni.convert( "����" ) + "</b></font></td>");
			out.println("<td><font size=2><b>");
		
			if(j_kind.equals("1")) out.println( uni.convert( "������" ) );
       		if(j_kind.equals("2")) out.println( uni.convert( "����" ) );
			if(j_kind.equals("3")) out.println( uni.convert( "�Ƿ�" ) );
			if(j_kind.equals("4")) out.println( uni.convert( "����" ) );
			if(j_kind.equals("5")) out.println( uni.convert( "���/�ڵ���" ) );
			if(j_kind.equals("6")) out.println( uni.convert( "��ǻ��/���" ) );
			if(j_kind.equals("7")) out.println( uni.convert( "���񽺾�" ) );
       		if(j_kind.equals("8")) out.println( uni.convert( "����" ) );
       		if(j_kind.equals("9")) out.println( uni.convert( "����/���׸���" ) );
       		if(j_kind.equals("10")) out.println( uni.convert( "���" ) );
			if(j_kind.equals("11")) out.println( uni.convert( "����" ) );
			if(j_kind.equals("12")) out.println( uni.convert( "����" ) );
			if(j_kind.equals("13")) out.println( uni.convert( "������" ) );
			if(j_kind.equals("14")) out.println( uni.convert( "��������" ));
			if(j_kind.equals("15")) out.println( uni.convert( "��Ÿ" ) );
			out.println("<input type=hidden name=j_kind value=\"" + j_kind + "\"></td></tr>");
      		out.println("</table>");
	  		out.println("<br><p><table border=0 width=80% cellspacing=0>");

	   		out.println("<tr>");
  			out.println("<td><font size=2 color=#fffacd><b>" + uni.convert( "��ǥ��ȭ" ) + "</b></font>");
			out.println("<input type=hidden name=c_tel value=\""+c_tel+"\"></td>");
			out.println("<td><font size=2><b>"+c_tel+"</td>");
			out.println("</tr><tr>");
			out.println("<td><font size=2 color=#fffacd><b>" + uni.convert( "�ѽ�" ) + "</b></font>");
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
   	   		out.println("<td><font size=2 color=#fffacd><b>" + uni.convert( "�ּ�" ) + "</b></font>");
			out.println("<input type=hidden name=c_address value=\""+c_address+"\"></td>");
 	   		out.println("<td><font size=2><b>"+c_address+"</td>");
    		out.println("</tr>");
    		out.println("<tr><td colspan=2 align=center><font size=2 color=#e6e6fa><br><p><br><p>" + uni.convert("�˻��� ȸ�簡 ���� �ٴϰ� �ִ� ����� ������ '���'��ư�� �����ּ���.") + "<br>" + uni.convert(" ���� ������ ��ҹ�ư�� ��������.") + "</td></tr>" );
       		out.println("<tr>");
       		out.println("<td colspan=2 align=center><br><p>");
       		out.println("<input type=submit value="+ uni.convert( "���" ) +">");
       		out.println("<input type=button value=" + uni.convert( "���" ) + " onClick=\"window.history.go(-1);\"></td>");
       		out.println("</tr></table>");
	 		out.println("</td>");
    		out.println("</form></tr>");
			out.println("</table>");
			out.println("</body>");
			out.println("</html>");
		} catch( SQLException e) {
			out.println( head );
			out.println( uni.convert( "DB���� ���ῡ ������ �ֽ��ϴ�. �ٽ� �õ��ϼ���" )  + e.toString());
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
	 *  addPersonForm2 - show ID input form ( ���ο� ȸ���� ��� )
	 *
	 *  @param req  form���� �Է¹��� �����Ͱ� ����ִ�  ��
	 *  @param out  ����� ����ϴ� ��
	 */
	public void addPersonForm2( HttpServletRequest req, PrintWriter out ) throws UnsupportedEncodingException {
		// request���� parameter�� parsing�Ѵ�.
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

		//tokenizing �ؼ� space���ڸ� �����ش�.
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

		// �Է��ڷᰡ ���ڶ�� �ٽ� ������ ���ư���.
		if( ( id== "" ) || ( passwd == "" ) 
			|| ( checkPasswd == "" ) || ( name == "" ) 
			|| ( birth == "" ) || ( ssn == "" )
			|| ( address == "" ) || ( email == "" )   
			|| ( c_name == "" ) || ( j_position == "" )
			|| ( j_tel == "" ) ) {
			// ������ �ٽ� ���ư���.
			out.println( head );
			out.println( "<center><h3>" + uni.convert( "�Է��ڷᰡ �����մϴ�. �ٽ� �Է��ϼ���" ) + "<h3>" );
			out.println("<br><p><a href=\"javaScript:window.history.go(-1);\"><font size=2><b><-Back</a></center>");
			out.println( end );
			return;
		}

		//�н����� Ȯ���ϱ�
		if(!passwd.equals(checkPasswd)){
			out.println("<center><h3>" + uni.convert( "Ȯ�ο� �н������ ��ġ���� �ʽ��ϴ�.�ٽ� �Է��� �ּ���." ) + "</h3>");
			out.println("<br><p><a href=\"javaScript:window.history.go(-1);\"><font size=2><b><-Back</a></center>");
		}

		// ȸ�簡 �������� �ʴ´ٴ� flag�� session�� �ִ´�.
		HttpSession httpsession = req.getSession( true );
		httpsession.putValue( "exist", "false" );
		
		
		// �̹� �Է��� �������� �����ְ� ȸ�������� �Է��϶�� �޼����� �����ش�. 
		// ȸ�������� ����д�.
		out.println("<html>");
		out.println("<head>");
		out.println("<title>id" + uni.convert( "���" ) + "</title>");
		out.println("</head>");
		out.println("<body background=/bg.jpg>");
		out.println("<table width=600 border=0 cellspacing=0>");
  		out.println("<tr>");
    	out.println("<td height=28 align=center><font size=4 color=#ffff00><b><img src=/icon.gif>" + uni.convert( "��������" ) + "</b></font></td>");
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
        out.println("<td><font size=2 color=#fffacd><b>" + uni.convert( "�̸�" ) + "</b></td>");
    	out.println("<td><input type=text name=name size=5 maxlength=5 value=\""+uni.convert( name )+"\"></td>");
		out.println("</tr><tr>");
    	out.println("<td><font size=2 color=#fffacd><b>" + uni.convert( "�ֹε�Ϲ�ȣ" ) + "</b></td>");
    	out.println("<td> <input type=text name=ssn size=13 maxlength=13 value=\""+ssn+"\"></td>");
        out.println("</tr>");
  		out.println("<tr>");
        out.println("<td><font size=2 color=#fffacd><b>" + uni.convert( "�������" ) + "</b></td>");
    	out.println("<td> <input type=text name=birth size=10 maxlength=10 value=\""+birth+"\"></td>");
		out.println("</tr><tr>");
    	out.println("<td><font size=2 color=#fffacd><b>" + uni.convert( "����" ) + "</b></td>");
		if(sex.equals("m")){
    		out.println("<td><font size=2 color=#fffacd><b>" + uni.convert( "��" ) + "<input type=radio name=sex checked value=m>" + uni.convert( "��" ) + "<input type=radio name=sex value=f></b></td>");
		}else{
			out.println("<td><font size=2 color=#fffacd><b>" + uni.convert( "��" ) + "<input type=radio name=sex value=m>" + uni.convert( "��" ) + "<input type=radio name=sex checked value=f></b></td>");
		}
        out.println("</tr>");
  		out.println("<tr>");
        out.println("<td><font size=2 color=#fffacd><b>" + uni.convert( "�ּ�" ) + "</b></td>");
    	out.println("<td> <input type=text name=address size=40 value=\""+ uni.convert( s_address )+"\"></td>");
		out.println("</tr><tr>");
  	    out.println("<td><font size=2 color=#fffacd><b>" + uni.convert( "��ȭ" ) + "</b></td>");
    	out.println("<td> <input type=text name=tel size=12 value=\""+tel+"\"> </td>");
        out.println("</tr>");
  		out.println("<tr>");
        out.println("<td><font size=2 color=#fffacd><b>" + uni.convert( "�߻�" ) + "</b></td>");
    	out.println("<td> <input type=text name=beeper size=13 value=\""+beeper+"\"></td>");
		out.println("</tr><tr>");
        out.println("<td><font size=2 color=#fffacd><b>" + uni.convert( "�޴���" ) + "</b></td>");
    	out.println("<td> <input type=text name=handphone size=13 value=\""+phone+"\"></td>");
        out.println("</tr>");
  		out.println("<tr>");
        out.println("<td><font size=2 color=#fffacd><b>" + uni.convert( "Ȩ������" ) + "</b></td>");
    	out.println("<td><input type=text name=homepage size=40 value=\""+homepage+"\"></td>");
		out.println("</tr><tr>");
    	out.println("<td><font size=2 color=#fffacd><b>e-mail<b></td>");
    	out.println("<td><input type=text name=email size=40 value=\""+email+"\"></td>");
        out.println("</tr>");
  		out.println("<tr>");
        out.println("<td><font size=2 color=#fffacd><b>" + uni.convert( "����" ) + "</b></td>");
    	out.println("<td><input type=text name=position size=20 value=\""+ uni.convert( s_j_position )+"\"></td>");
		out.println("</tr><tr>");
    	out.println("<td><font size=2 color=#fffacd><b>" + uni.convert( "�μ�" ) + "</b></td>");
    	out.println("<td><input type=text name=part size=20 value=\""+ uni.convert( j_part )+"\"></td>");
        out.println("</tr>");
  		out.println("<tr>");
        out.println("<td><font size=2 color=#fffacd><b>" + uni.convert( "ȸ�簳����ȭ" ) + "</b></td>"); 
		out.println("<td> <input type=text name=j_tel size=13 value=\""+j_tel+"\"></td>");
		out.println("</tr><tr>");
    	out.println("<td><font size=2 color=#fffacd><b>" + uni.convert( "ȸ���ѽ�" ) + "</b></td>");
    	out.println("<td><input type=text name=j_fax size=13 value=\""+j_fax+"\"></td>");
        out.println("</tr>");
		out.println("<tr>");
		out.println("<td><font size=2 color=#fffacd valign=top><b>description</b></font></td>");
		out.println("<td><textarea name=description cols=50 rows=4>"+ uni.convert( desc )+"</textarea></td>");
		out.println("</tr>");
      	out.println("</table><br><p></td>");
    	out.println("</tr> ");
    	out.println("<tr>");
      	out.println("<td align=center height=28><b><font size=4 color=#ffff00><img src=/icon.gif>" + uni.convert( "ȸ������" ) + "</b></td>");
    	out.println("</tr>");
    	out.println("<tr>  ");
  	  	out.println("<td valign=top><center><br><p>");   
       	out.println("<table border=0 width=80% cellspacing=0>");
		out.println("<tr><td colspan=3 align=center><font size=2 color=#e6e6fa><b>" + uni.convert( "�˻��� ȸ�簡 �����ϴ�." ) + "<br>");
		out.println( uni.convert( "�Ʒ� ������ ������ �ּ���.*ǥ�� �Ǿ��ִ� ������ �ݵ�� ������ �ּ���." ) + "</td></tr>");
		out.println("<tr>");
        out.println("<td><br><p><font size=2 color=#e0ffff><b>*</td>");
		out.println("<td><font size=2 color=#fffacd><b>" + uni.convert( "ȸ���̸�" ) + "</b></td>");
 		out.println("<td> <input type=text name=c_name size=20 value=\""+uni.convert(c_name)+"\"></td>");
		out.println("</tr><tr>");
      	out.println("<td><font size=2 color=#e0ffff><b>*</td>");
		out.println("<td><font size=2 color=#fffacd><b>" + uni.convert( "����" ) + "</b></td>");
      	out.println("<td><select name=c_region size=1>");
		if(c_region.equals("0")) out.println("<option value=0 selected>" + uni.convert( "���û��׾���" ) + "</option>");
		else out.println("<option value=0>" + uni.convert( "���û��׾���" ) + "</option>");
		
		if(c_region.equals("1")) out.println("<option value=1 selected>" + uni.convert( "����Ư����" ) + "</option>");
		else out.println("<option value=1>" + uni.convert( "����Ư����" ) + "</option>");
 		 	
	   	if(c_region.equals("2")) out.println("<option value=2 selected>" + uni.convert( "�λ걤����" ) + "</option>");
		else out.println("<option value=2>" + uni.convert( "�λ걤����" ) + "</option>");

		if(c_region.equals("3")) out.println("<option value=3 selected>" + uni.convert( "�뱸������" ) + "</option>");
 		else out.println("<option value=3>" + uni.convert( "�뱸������" ) + "</option>");
	
		if(c_region.equals("4")) out.println("<option value=4 selected>" + uni.convert( "��õ������" ) + "</option>");
		else out.println("<option value=3>" + uni.convert( "��õ������" ) + "</option>");
            		
		if(c_region.equals("5")) out.println("<option value=5 selected>" + uni.convert( "���ֱ�����" ) + "</option>");
		else out.println("<option value=5>" + uni.convert( "���ֱ�����" ) + "</option>");

		if(c_region.equals("6")) out.println("<option value=6 selected>" + uni.convert( "��걤����" ) + "</option>");
		else out.println("<option value=6>" + uni.convert( "��걤����" ) + "</option>");
        
   		if(c_region.equals("7")) out.println("<option value=7 selected>" + uni.convert( "����������" ) + "</option>");
		else out.println("<option value=7>" + uni.convert( "����������" ) + "</option>");

		if(c_region.equals("8")) out.println("<option value=8 selected>" + uni.convert( "���" ) + "</option>");
		else out.println("<option value=8>" + uni.convert( "���" ) + "</option>");
            
		if(c_region.equals("9")) out.println("<option value=9 selected>" + uni.convert( "����" ) + "</option>");
		else out.println("<option value=9>" + uni.convert( "����" ) + "</option>");
	
		if(c_region.equals("10")) out.println("<option value=10 selected>" + uni.convert( "���" ) + "</option>");
		else out.println("<option value=10>" + uni.convert( "���" ) + "</option>");
            
		if(c_region.equals("11")) out.println("<option value=11 selected>" + uni.convert( "�泲" ) + "</option>");
		else out.println("<option value=11>" + uni.convert( "�泲" ) + "</option>");

		if(c_region.equals("12")) out.println("<option value=12 selected>" + uni.convert( "����" ) + "</option>");
		else out.println("<option value=12>" + uni.convert( "����" ) + "</option>");
            
		if(c_region.equals("13")) out.println("<option value=13 selected>" + uni.convert( "����" ) + "</option>");
		else out.println("<option value=13>" + uni.convert( "����" ) + "</option>");

		if(c_region.equals("14")) out.println("<option value=14 selected>" + uni.convert( "���" ) + "</option>");
		else out.println("<option value=14>" + uni.convert( "���" ) + "</option>");
            		
		if(c_region.equals("15")) out.println("<option value=15 selected>" + uni.convert( "�泲" ) + "</option>");
		else out.println("<option value=15>" + uni.convert( "�泲" ) + "</option>");

		if(c_region.equals("16")) out.println("<option value=16 selected>" + uni.convert( "����" ) + "</option>");
		else out.println("<option value=16>" + uni.convert( "����" ) + "</option>");
          
 		out.println("</select> </td></tr>");
		out.println("<tr>");
        out.println("<td></td><td><font size=2 color=#fffacd><b>" + uni.convert( "����" ) + "</b></td>");
      	if( !s_c_site.equals( "" ) ) {	
			out.println("<td><input type=text name=c_site size=20 value=\""+ uni.convert( s_c_site )+"\"></td>");
		} else {
			out.println( "<td><input type=text name=c_site size=10 ></td>" );
		}	
		out.println("</tr><tr>");
		out.println("<td><font size=2 color=#e0ffff><b>*</td>");
		out.println("<td><font size=2 color=#fffacd><b>" + uni.convert( "����" ) + "</b></font></td>");
		out.println("<td><select name=j_kind size=1>");

		if(j_kind.equals("1")) out.println("<option value=1 selected>" + uni.convert( "������" ) + "</option>");
		else out.println("<option value=1>" + uni.convert( "������" ) + "</option>");

       	if(j_kind.equals("2")) out.println("<option value=2 selected>" + uni.convert( "����" ) + "</option>");
		else out.println("<option value=2>" + uni.convert( "����" ) + "</option>");
        
		if(j_kind.equals("3")) out.println("<option value=3 selected>" + uni.convert( "�Ƿ�" ) + "</option>");
		else out.println("<option value=3>" + uni.convert( "�Ƿ�" ) + "</option>");
        
		if(j_kind.equals("4")) out.println("<option value=4 selected>" + uni.convert( "����" ) + "</option>");
		else out.println("<option value=4>" + uni.convert( "����" ) + "</option>");
        
		if(j_kind.equals("5")) out.println("<option value=5 selected>" + uni.convert( "���/�ڵ���" ) + "</option>");
		else out.println("<option value=5>" + uni.convert( "���/�ڵ���" ) + "</option>");
       
		if(j_kind.equals("6")) out.println("<option value=6 selected>" + uni.convert( "��ǻ��/���" ) + "</option>");
		else out.println("<option value=6>" + uni.convert( "��ǻ��/���" ) + "</option>");
        	
		if(j_kind.equals("7")) out.println("<option value=7 selected>" + uni.convert( "���񽺾�" ) + "</option>");
		else out.println("<option value=7>" + uni.convert( "���񽺾�" ) + "</option>");

       	if(j_kind.equals("8")) out.println("<option value=8 selected>" + uni.convert( "����" ) + "</option>");
		else out.println("<option value=8>" + uni.convert( "����" ) + "</option>");

       	if(j_kind.equals("9")) out.println("<option value=9 selected>" + uni.convert( "����/���׸���" ) + "</option>");
		else out.println("<option value=9>" + uni.convert( "����/���׸���" ) + "</option>");

       	if(j_kind.equals("10")) out.println("<option value=10 selected>" + uni.convert( "���" ) + "</option>");
		else out.println("<option value=10>" + uni.convert( "���" ) + "</option>");
        
		if(j_kind.equals("11")) out.println("<option value=11 selected>" + uni.convert( "����" ) + "</option>");
		else out.println("<option value=11>" + uni.convert( "����" ) + "</option>");
        	
		if(j_kind.equals("12")) out.println("<option value=12 selected>" + uni.convert( "����" ) + "</option>");
		else out.println("<option value=12>" + uni.convert( "����" ) + "</option>");
        
		if(j_kind.equals("13")) out.println("<option value=13 selected>" + uni.convert( "������" ) + "</option>");
		else out.println("<option value=13>" + uni.convert( "������" ) + "</option>");
        
		if(j_kind.equals("14")) out.println("<option value=14 selected>" + uni.convert( "��������" ) + "</option>");
		else out.println("<option value=14>" + uni.convert( "��������" ) + "</option>");
        
		if(j_kind.equals("15")) out.println("<option value=15 selected>" + uni.convert( "��Ÿ" ) + "</option>");
		else out.println("<option value=15>" + uni.convert( "��Ÿ" ) + "</option>");
		
		out.println("</select></td></tr>");
      	out.println("</table>");
	  	out.println("<br><p><table border=0 width=80% cellspacing=0>");
	    out.println("<tr>");
  		out.println("<td><font size=2 color=#e0ffff><b>*</td>");
		out.println("<td><font size=2 color=#fffacd><b>" + uni.convert( "ȸ���ǥ��ȭ " ) +"</b></font></td>");
		out.println("<td><input type=text name=c_tel size=13=></td>");
		out.println("</tr><tr>");
		out.println("<td></td>");
		out.println( uni.convert("<td><font size=2 color=#fffacd><b>�ѽ�</b></font></td>" ));
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
		out.println( uni.convert( "<td><font size=2 color=#fffacd><b>�ּ�</b></font></td>") );
		out.println("<td><input type=text name=c_address size=40></td>");
        out.println("</tr>");
        out.println("<tr>");
        out.println("<td colspan=3 align=center><br><p>");
        out.println( uni.convert( "<input type=submit value=���>") );
        out.println( uni.convert( "<input type=button value=��� onClick=\"window.history.go(-1);\"></td>") );
        out.println("</tr></table>");
	 	out.println("</form></td>");
    	out.println("</tr>");
		out.println("</table></center>");
		out.println("</body>");
		out.println("</html>");
	}
		
	
	/**
	 *  registerMemberPerson - registerMemberPerson command�� ó���ϴ� method
	 *                         �Է¹��� ������ ���̵� ��û�� ����Ѵ�. 
	 *
	 *  @param req 		 ������ ���� �����Ͱ� ����ִ� ��
	 *  @param out       ����� ����� ��
	 *  @throws UnsupportedEncodingException  
	 */
	public void registerMemberPerson( HttpServletRequest req, PrintWriter out ) throws UnsupportedEncodingException  
	{
		// session�� ���´�.
		HttpSession httpsession = req.getSession( false );
		// session�� ������ ����� ����� ���� �ʾҴٴ� ���̹Ƿ� error ó���Ѵ�.
		if( httpsession == null) {
			out.println( head );
			out.println( "<h1>Error !!! </h1>" );
			out.println( end );
			return;
		}
			
		// request���� parameter�� parsing�Ѵ�.
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

		//tokenizing �ؼ� space���ڸ� �����ش�.
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
		

		// �Է��ڷᰡ ���ڶ�� �ٽ� ������ ���ư���.
		if( ( id.equals( "" ) ) || ( passwd.equals( "" )   ) 
			|| ( name.equals( "" )  ) || ( birth.equals( "" )   ) 
			|| ( ssn.equals( "" )   ) || ( address.equals( "" )   ) 
			|| ( c_name.equals( "" )   )|| ( j_tel.equals( "" )   )   
			|| ( email.equals( "" )  ) || (c_address.equals( "" )  )
			|| ( j_position.equals( "" )   )  ) {
			// ������ �ٽ� ���ư���.
			out.println( head );
			out.println( "<center><h3>" + uni.convert( "�Է��ڷᰡ �����մϴ�. �ٽ� �Է��ϼ���" ) + "<h3>" );
			out.println("<br><p><a href=\"javaScript:window.history.go(-1);\"><font size=2><b><-Back</a></center>");
			out.println( end );
			return;
		}	
		try{
			// �Է� �����Ϳ��� ���鹮�ڸ� �����Ѵ�.
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

		// ȸ�簡 �����ϴ��� Ȯ���ؼ� ������ ���� �����.
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
			 
		// connection���� 		 
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
				// ���ο� ȸ������ �����
				message = uni.convert( "ȸ������ �Է½��� :" );
				// object���� �ð� �Է�
				Calendar date1 = Calendar.getInstance();
		
				String time1 = String.valueOf(date1.get(date1.YEAR)) + String.valueOf(date1.get(date1.MONTH)) + String.valueOf(date1.get(date1.DATE)) + String.valueOf(date1.get(date1.HOUR)) + String.valueOf(date1.get(date1.MINUTE)) + String.valueOf(date1.get(date1.SECOND)); 
				cominfo_id = id +time1;	
				// cominfo ����  
				query2 = new String( "insert into ComInfo (id,address,tel,fax,homepage,email,back) values('"+uni.convert( cominfo_id )+"','"+uni.convert( s_c_address )+"','"+c_tel+"','"+c_fax+"','"+c_homepage+"','"+c_email+"','0');");	
				stmt.executeUpdate( query2 );

				// ���ο� ȸ�縸���
				message = uni.convert( "ȸ�縸��� ���� :" );
				// object���� �ð� �Է�
				Calendar date2 = Calendar.getInstance();
		
				String time2 = String.valueOf(date2.get(date2.YEAR)) + String.valueOf(date2.get(date2.MONTH)) + String.valueOf(date2.get(date2.DATE)) + String.valueOf(date2.get(date2.HOUR)) + String.valueOf(date2.get(date2.MINUTE)) + String.valueOf(date2.get(date2.SECOND)); 
				
				com = time2 + id ;

				if( c_site != null ) s_c_site = s_c_site.trim(); 
				query3 = "insert into Company (id, name, site, region, cominfo, employee) values ('" + uni.convert(com) + "', '" + uni.convert(c_name) + "', '" + uni.convert(c_site) + "', " + Integer.parseInt( c_region ) + ", '" + uni.convert(cominfo_id) + "', '1');";

				//qeury �� ����
				stmt.executeUpdate( query3 );	
								
			} else {
				message = " update employee num ";
				com = (String)httpsession.getValue( "com" );	

				// company�� �����Ѵ�.
				query2 = "select employee from Company where id='" + uni.convert(com) + "';";
				ResultSet company = stmt.executeQuery( query2 );
				company.next();

				int employee = company.getInt( "employee" );
				employee++;
				message = " in query3 ";	
				query3 = "update Company set employee=" + employee + " where id='" + com + "';";
				stmt.executeUpdate( query3 );
				
			}

			// object���� �ð� �Է�
			Calendar date3 = Calendar.getInstance();
		
			String time3 = String.valueOf(date3.get(date3.YEAR)) + String.valueOf(date3.get(date3.MONTH)) + String.valueOf(date3.get(date3.DATE)) + String.valueOf(date3.get(date3.HOUR)) + String.valueOf(date3.get(date3.MINUTE)) + String.valueOf(date3.get(date3.SECOND)); 

			String personinfo_id = id +time3;	

			// PersonInfo �� DB�� �ִ´�.
			message = uni.convert( "����������� ���� : " );	
			query4 = "insert into PersonInfo (id, address, tel, beeper, handphone, email, homepage, back) values ('" + uni.convert(personinfo_id) + "', '" + uni.convert(s_address) + "', '" + tel + "', '" + beeper + "', '" + phone + "', '" + email + "', '" + homepage + "', '0');";
			stmt.executeUpdate( query4 );			


			// object���� �ð� �Է�
			Calendar date4 = Calendar.getInstance();
		
			String time4 = String.valueOf(date4.get(date4.YEAR)) + String.valueOf(date4.get(date4.MONTH)) + String.valueOf(date4.get(date4.DATE)) + String.valueOf(date4.get(date4.HOUR)) + String.valueOf(date4.get(date4.MINUTE)) + String.valueOf(date4.get(date4.SECOND)); 

			String jobinfo_id = time4 + id;	

			// JobInfo�� DB�� �ִ´�.
			message = uni.convert( "����������� ���� : " );
			query5 = "insert into JobInfo (id, posi, part, tel, fax, kind, company, back) values ('" + uni.convert(jobinfo_id) + "', '" + uni.convert( j_position ) + "', '" + uni.convert( j_part ) + "', '" + j_tel + "', '" + j_fax + "', '" + Integer.parseInt(j_kind) + "', '" + uni.convert(com) + "', '0');";

			//query �� ����
			stmt.executeUpdate( query5 );			

			// MemberPerson object�����ϰ� ID Request List�� �ִ´�.
			message = uni.convert( "���̵� ��û ���� : " );
			query6 = "insert into IDReqList (id, password, name, ssn, sex, birthday, description, personinfo, jobinfo, com_id) values ('" + uni.convert(id) + "','" + uni.convert(passwd) + "','" + uni.convert( name ) + "','" + ssn + "','" + sex + "','" + birth + "','" + uni.convert( desc ) + "','" + uni.convert(personinfo_id) + "','" + uni.convert(jobinfo_id) + "','" + uni.convert(com) + "')";
			//query �� ����
			stmt.executeUpdate( query6 ); 	

			// connection�ݱ�
			con.commit();
			stmt.close();
			con.close();		

			// ������ �����ϰ� ��ϵǾ����� ������ ��ٸ���� �޼��� ���
			httpsession.invalidate();
		
			out.println( head );
			out.println( uni.convert("<font size=5 color=white><center><br><br><br><br>��ϵǾ����ϴ�. ������ ��ٸ��ʽÿ�.</center></font>" ));
			out.println( end );	
			out.println( "<meta http-equiv=\"refresh\" content=\"0;url=/cardSystem/userLogin.html\">" );

			
		} catch( SQLException  e) {
			out.println( head );
			out.println( uni.convert( "���̵� ����� SQLException�߻� " ) + e.toString() );
			out.println( end );
			return;
		} catch( Exception e ) {
			out.println( head );
			out.println( "fail to registerPerson " + message + e.toString()  );
			out.println( end );
			return;
		}
	}
	
	
	///////////////////////////// ID Req List �����ֱ� ///////////////////////
	
	/** 
	 *  showIDReqList - �����ڿ��� ID������ ���� ID Request list�� �����ش�.
	 * 
	 *  @param req  ������ �Է¹��� �����Ͱ� ����ִ� �� 
	 *  @param out 	����� ����� ��
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
	
			// IDReqList�� �ִ� Person object list�� �����ش�.
			out.println( head ); 
			out.println( "<center><form method=post action=/namecard/AdminServlet>" );
			out.println( "<input type=hidden name=cmd value=showPersonInfo>" );	
			out.println( "<br><br><br><table>" );
			out.println( "<tr><td></td><td><font size=3 color=#fffacd>ID</td><td><font size=3 color=#fffacd>" + uni.convert( "�̸�" ) + "</td><td><font size=3 color=#fffacd>" ); 
			out.println( uni.convert( "ȸ��" ) + "</td><td><font size=3 color=#fffacd>" + uni.convert( "����" ) + "</td><td><font size=3 color=#fffacd>" );
			out.println( uni.convert( "�μ�" ) + "</td><td><font size=3 color=#fffacd>" + uni.convert( "��å" ) + "</td></tr>" );	
			int num = 0;

			while( idReqList.next() ) {
				num++;
				String id = idReqList.getString( "id" );
				// ID�� input type�� hidden���� �ؼ� element number�� �Ѱ��ش�. 
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
			out.println( num + uni.convert( "���� ��û�ڰ� �ֽ��ϴ�.</font>") );
			out.println(  uni.convert( "<table><td><input type=submit value=����></td>"  ));
			out.println(  uni.convert( "<td><iput type=reset value=���></td></table>" ) );
			out.println( "</form></center>" );
			out.println( end );	
		} catch( SQLException e ){
			out.println( head );
	   		out.println(  uni.convert( "DB���� ���ῡ ������ �ֽ��ϴ�. �ٽ� �õ��ϼ���" ) + e.toString() );
			out.println( end );
	   		return;		
		} catch( CommandException e ) {
			out.println( head );
			out.println( uni.convert( "DB���� ����Ʈ ������ ���� �����߽��ϴ�. �ٽ� �õ��ϼ���!" ) + e.toString());
			out.println( end );

		} catch( Exception e ){
			out.println( head );
			out.println( "fail to showIDReQList" + e.toString() );
			out.println( end );
		}
		
	}
	
	/** 
	 *  showPersonInfo -  showPersonInfo command�� ó���ϴ� method 
	 * 				      ID Requestlist���� ���̵� Ŭ���ϸ� �������� �����ش�.
	 * 
	 *  @param req 	������ ���� �����͸� ������ �ִ� ��
	 *  @param out 	����� ����� ��
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
		// request���� parameter parsing �Ѵ�.
		try{
			id = req.getParameter( "id" ) ;
			if( id == null ) {
				out.println( head );
				out.println( "<br><br><br><font size=3 color=white>" );
				out.println( uni.convert( "<center>�� �̻� �� ������ �����ϴ�. ���ư��ʽÿ�.<br>" )); 
				out.println("<a href=\"javaScript:window.history.go(-4);\"><font size=2><b><-Back</a></center></font>");	 
				out.println( end );
				return;
			}
		} catch( Exception e ) {
			out.println( head );
			out.println( "<br><br><br>" );
			out.println( uni.convert( "<center>�� �̻� �� ������ �����ϴ�. ���ư��ʽÿ�.<br>" )); 
			out.println("<a href=\"javaScript:window.history.go(-1);\"><font size=2><b><-Back</a></center>");	 
			out.println( end );
			return;
		}

		ResultSet member =  null;

		try {
			// IDReqList���� ���õ� ����� ������ DB���� ��´�. 
			member = dbmanager.getIDRequest( id );
			member.next();
			out.println( head );
			out.println( "<br><br><br>" );
			//out.println( "id : " + member.getString( "id" ) );	
			out.println( "<center> " );
			out.println( uni.convert( "<h3><font color=#fffacd>ID Request List�� ��ϵ�<font color=white> " ) + id + uni.convert( "</font>�� ����</h3><br><br> ") );
			out.println( "<table>" );
			out.println( "<tr><td>" + uni.convert( "�̸�</td><td> :</td><td> " )+ member.getString( 2 ) );
			out.println( "</td></tr><tr><td>" + uni.convert( "�ٹ���</td><td> :</td><td> " ) + member.getString( 3 ) );
			out.println( "</td></tr><tr><td>" + uni.convert( "�μ� </td><td>:</td><td> " ) + member.getString( 4 ) );
			out.println( "</td></tr><tr><td>" + uni.convert( "��å</td><td> :</td><td> " ) + member.getString( 5 ) );
			out.println( "</td></tr><tr><td>" + uni.convert( "�ּ�</td><td> :</td><td> ") + member.getString( 6 ) );
			out.println( "</td></tr><tr><td>" + uni.convert( "����</td><td> :</td><td> ") + member.getString( 7 ) );
			out.println( "</td></tr><tr><td>" + uni.convert( "��ȭ</td><td> :</td><td> ") + member.getString( 8 ) );
			out.println( "</td></tr></table> " );
			out.println( "<form method=post action=/namecard/AdminServlet >" );
			out.println( "<input type=hidden name=cmd value=addMemberPerson>" );
			out.println( "<input type=hidden name=add_id value=" + member.getString( 1 ) + "> " );
			out.println( uni.convert( "<input type=submit value=���></form>"  ));
			out.println( "<form method=post action=/namecard/AdminServlet> " );
			out.println( "<input type=hidden name=cmd value=deleteIDRequest>" );
			out.println( "<input type=hidden name=add_id value="+member.getString( 1 ) +">" );
			out.println( uni.convert( "<input type=submit value=���></form> "  ));
			out.println( "</center>" );
			out.println( end );
		} catch( SQLException e ){
				out.println( head );
	    		out.println( uni.convert( "DB���� ���ῡ ������ �ֽ��ϴ�. �ٽ� �õ��ϼ���"  ) + e.toString() );
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

	
	/////////////////////////////////  ID ���  ///////////////////////////////
	
	/**
	 *  addMemberPerson - addMemberPerson command�� ó���ϴ� method
	 *                    IDReqList���� ���� ����ڷ� ���̵� ��Ͻ�Ų��.	
	 * 
	 *  @param req 	form���� �Է¹��� �����Ͱ� ����ִ� ��
	 *  @param out 	����� ������ ��
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
			// request���� parameter parsing �Ѵ�.
			id = asc.convert( req.getParameter( "add_id" ) );
		} catch( NullPointerException e ) {
			out.println( head );
			out.println( uni.convert( "����� ����� �����ϴ�. �ٽ� ���ư�����" ));
			out.println("<a href=\"javaScript:window.history.go(-1);\"><font size=2><b><-Back</a></center>");	
			out.println( end );
			return;
		}
		//out.println( uni.convert(id) + uni.convert( " ��  ����Ϸ���" ));

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

		//���ο� jobInfo�� DB�� �߰��Ѵ�.
		try{
			result = dbmanager.getIDInfo( uni.convert(id) );
			result.next();	
			//query statement
			String query1 = "insert into MemberPerson (id, password, name, ssn, sex, birthday, description, personinfo, jobinfo, com_id) values ('" + result.getString( "id" ) + "', '" + result.getString( "password" ) + "', '" + result.getString( "name" ) + "', '" + result.getString( "ssn" ) + "', '" + result.getString( "sex" ) + "', '" + result.getString( "birthday" ) + "', '" + result.getString( "description" ) + "', '" + result.getString( "personinfo" ) + "', '" + result.getString( "jobinfo" ) + "', '" + result.getString( "com_id" ) + "');";

			//query �� ����
			stmt.executeUpdate( query1 ); 

			//query statement
			String query2 = "delete from IDReqList  where id='" + uni.convert(id) + "';";

			//query �� ����
			stmt.executeUpdate( query2 ); 

			if( !result.getString( "com_id" ).endsWith( id ) ) {
				// �̹� �ִ� ȸ���� �������� �ø���.
				String query3 = "select employee from Company where id='" + result.getString( "com_id" ) + "';";
				ResultSet com = stmt.executeQuery( query3 );
				com.next();
				int employee = com.getInt( "employee" );
				employee++;
		
				String query4 = "update Company set employee='" + employee + "' where id='" + result.getString( "com_id" )+ "';";
				stmt.executeUpdate( query4 );
			}
	
			// commit�ϰ� AutoCommit�� false�� �Ѵ�.	
			con.commit();
			out.println( "<center><br><br>id : " + uni.convert(id + " ���� ����Ͽ����ϴ�.</center>" ));	
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
	 *  deleteIDRequest - ID��û�� DB���� �����ϴ� method 
	 *
	 *  @param req	������ ���� �����Ͱ� ����ִ� ��
	 *  @param out 	����� ����ϴ� ��
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

		// request���� parameter parsing �Ѵ�.
		String id = asc.convert( req.getParameter( "add_id" ) );
		
		// vector ���� object�� �����Ѵ�.
		try {
			dbmanager.cancelIDRequest( uni.convert(id) );
			showIDReqList( req, out );	
		} catch( CommandException e ) {
			out.println( head );
			out.println( uni.convert( "DB �� ���ӿ� ������ �ֽ��ϴ�. �ٽ� �õ��ϼ���" ) + e.toString() );
			out.println( end );
			return; 		
		}	catch( Exception e ) {
			out.println( uni.convert( "���̵� ��Ͽ�û ������ �����Ͽ����ϴ�." ) + e.toString() );			
		}
		
	}

	////////////////////////////  ��������  //////////////////////////////

	/**
	 *  checkDeleteID - checkDeleteID command�� ó���ϴ� method
	 *                  ������ ���̵� ������ ������ Ȯ���Ѵ�.
	 *
	 *  @param req  ������ �Է¹��� �����Ͱ� ����ִ� ��
	 *  @param out  ����� ����� ��
	 */
	public  void checkDeleteID( HttpServletRequest req, PrintWriter out ) throws UnsupportedEncodingException {
		// ���� �˻�
		HttpSession httpsession = req.getSession( false );
		if( httpsession == null ) {
			out.println( head );
			out.println( "<h1>Unauthorized User! </h1>" );
			out.println( end );
			return;
		}
		
		//ID�����ϰڴٴ� �޼����� �����ش�.
		out.println("<html><head><title></title></head>");
		out.println("<body background=/bg.jpg><table width=600 border=0 cellspacing=0>");
		out.println("<tr><td align=center>");
		
		out.println(uni.convert("<br><p><img src=/icon.gif><font size=3 color=#ffff00><b>ID�� �����Ϸ� �մϴ�.<br>"));
		out.println(uni.convert("���� �����Ͻðڽ��ϱ�?<Br><p></td></tr>"));
		out.println("<tr><td align=center>");
		out.println("<form method=post action=/namecard/UserServlet>");
		out.println("<input type=hidden name=cmd value=deleteMemberPerson>");
		out.println("<input type=submit value=Yes>");
		out.println("<input type=button value=No onClick=\"window.history.go(-1);\"></form></td></tr>");
		out.println("</table></body></html>");
	}


	
	/**
	 *  deleteMemberPerson - deleteMemberPerson command ó���ϴ� method
	 *			             ����ڰ� ���������� ���� ��� DB���� ������ �����Ѵ�.
	 *
	 *  @param req  ������ �Է¹��� �����Ͱ� ����ִ� ��
	 *  @param out  ����� ����� ��
	 */
	public  void deleteMemberPerson( HttpServletRequest req, PrintWriter out )throws UnsupportedEncodingException { 
		// ����� ���Ѱ˻縦 �Ѵ�.
		HttpSession httpsession = req.getSession( false );
		if( httpsession == null ) {
			out.println( head );
			out.println( "Unauthorized User !" );
			out.println( end );
			return;
		}
	
		String id = (String)httpsession.getValue( "id" );
		
		try {
			// id�� ����� ���� ȸ�縦 ���� üũ�Ѵ�.
			dbmanager.deleteMemberPerson( id );
			out.println("<html><head><title></title></head><body background=/bg.jpg>");
			out.println("<font size=3 color=#fffacd><b>" + uni.convert( "���̵� �����Ǿ����ϴ�!" ) );
			out.println("<br><p><a href=\"http://ant/cardSystem/userLogin.html\"><font size=2 color=#fffacd><b><-Back</a>" );
			out.println("</body></html>");
			
		} catch( CommandException e ) {
			out.println( uni.convert("���̵� ������ �����Ͽ����ϴ�. �ٽ� �õ��ϼ���" ) + e.toString() );
		}	
	
	}


}
