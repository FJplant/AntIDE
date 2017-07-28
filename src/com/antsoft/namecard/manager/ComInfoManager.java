package com.antsoft.namecard.manager;

import com.antsoft.namecard.*;
import java.awt.*;
import java.util.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;


/**
 *  ComInfoManager class - ȸ�������� �����ϴ� manager class
 *
 *  This class manages information of company info.
 */
class ComInfoManager  {

	/** dbmanager */
	DBManager dbmanager;
	
	Asc2Uni asc;
	Uni2Asc uni;

	static String head = "<html><body background=/bg.jpg>";
	static String end = "</body></html>";

	/**
	 *  constructor
	 */
	public ComInfoManager(  ) {
		dbmanager = new DBManager();
		asc = new Asc2Uni();
		uni = new Uni2Asc();	
	}
	
	
	
	///////////////////////////  ȸ���������� ��� //////////////////////////
	
	/**
	 *  comInfoForm - comInfoForm command�� ó���ϴ� method
	 *                ȸ������������ ���� ���� �����ش�.
	 *
	 *  @param  req         ������ �Է¹��� �����Ͱ� ����ִ� ��
	 *  @param  out         ����� ����� ��
	 */
	public void comInfoForm( HttpServletRequest req, PrintWriter out )throws UnsupportedEncodingException {
		// ���� �˻�
		HttpSession httpsession = req.getSession( false );
		if( httpsession == null ) {
			out.println( head );
			out.println( "<h1>Unauthorized User! </h1>" );
			out.println( end );
			return;
		}
		
		try {
			String id = (String)httpsession.getValue( "id" );
			// DB���� id�� �̿��ؼ� MemberPerson object�� Company object�� ��´�.
			
			ResultSet info  = dbmanager.getCompanyInfo( id );
			info.next();		
			String num = info.getString( "id" );
			httpsession.putValue( "comid" , num );
	
			// company object�� ����ִ� �����͵��� �����ش�.
			out.println( "<html><head><title></title></head>");
			out.println("<body background=/bg.jpg>");
			out.println("<table border=0 width=600 cellspacing=0>");
			out.println("<tr><td align=center>");
			out.println( "<form method=post action=/namecard/UserServlet> " );
			out.println( "<input type=hidden name=cmd value=registerComInfo>" );
			out.println( "<br><p><img src=/icon.gif><font size=4 color=#ffff00><b>" + info.getString( "name" ) +"</font><font size=2 color=#ffff00>" + uni.convert( " �� ���� ����" ) + "</b></font></td></tr>" );
			out.println("<tr><td align=center><br><p>");
			out.println("<table border=0 width=80% align=center cellspacing=0>");
			out.println("<tr><td><font size=2 color=#fffacd><b>" + uni.convert( "����" ) + "</font></td>");
			out.println("<td><font size=2 color=#e0ffff><b>");
			
			int region = info.getInt( "region" );	
			String c_region = Integer.toString( region );

			if(c_region.equals("1")) out.println( uni.convert( "����Ư����" ));
	   		if(c_region.equals("2")) out.println( uni.convert( "�λ걤����" ));
			if(c_region.equals("3")) out.println( uni.convert( "�뱸������" ));
			if(c_region.equals("4")) out.println( uni.convert( "��õ������" ));
			if(c_region.equals("5")) out.println( uni.convert( "���ֱ�����" ));
			if(c_region.equals("6")) out.println( uni.convert( "��걤����" ));
    		if(c_region.equals("7")) out.println( uni.convert( "����������" ));
			if(c_region.equals("8")) out.println( uni.convert( "���" ));
			if(c_region.equals("9")) out.println( uni.convert( "����" ));
			if(c_region.equals("10")) out.println( uni.convert( "���" ));
			if(c_region.equals("11")) out.println( uni.convert( "�泲" ));
			if(c_region.equals("12")) out.println( uni.convert( "����" ));
			if(c_region.equals("13")) out.println( uni.convert( "����" ));
			if(c_region.equals("14")) out.println( uni.convert( "���" ));
			if(c_region.equals("15")) out.println( uni.convert( "�泲" ));
			if(c_region.equals("16")) out.println( uni.convert( "����" ));
			out.println( "</font></td></tr>" );

			/*
			if(c_region.equals("1")) out.println("<option value=1 selected>" + uni.convert( "����Ư����" ) + "</option>");
			else out.println("<option value=1>" + uni.convert( "����Ư����" ) + "</option><option value=2>" + uni.convert( "�λ걤����" ) + "</option>");
 		 	
	   		if(c_region.equals("2")) out.println("<option value=2 selected>" + uni.convert( "�λ걤����" ) + "</option>");
			else out.println("<option value=2>" + uni.convert( "�λ걤����" ) + "</option>");
	
			if(c_region.equals("3")) out.println("<option value=3 selected>" + uni.convert( "�뱸������" ) + "</option>");
 			else out.println("<option value=3>" + uni.convert( "�뱸������" ) + "</option>");
	
			if(c_region.equals("4")) out.println("<option value=4 selected>" + uni.convert( "��õ������" ) + "</option>");
			else out.println("<option value=3>" + uni.convert( "�뱸������" ) + "</option>");
            		
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
			*/
			out.println( "<tr><td><font size=2 color=#fffacd><b>" + uni.convert( "����" ) + "</b></font></td><td><input type=text size=15 name=site value=\"" + info.getString( "site") + "\"></td></tr>" );
			out.println( "<tr><td><font size=2 color=#fffacd><b>" + uni.convert( "����" ) + "</b></font></td><td><input type=text size=40 name=email value=\"" + info.getString( "email") + "\"></td></tr>" );
			out.println( "<tr><td><font size=2 color=#fffacd><b>" + uni.convert( "�ѽ�" ) + "</b></font></td><td><input type=text size=15 name=fax value=\"" + info.getString( "fax") + "\"></td></tr>" );
			out.println( "<tr><td><font size=2 color=#fffacd><b>" + uni.convert( "��ȭ" ) + "</b></font></td></td><td><input type=text size=13 name=tel value=\"" + info.getString( "tel") + "\"></td></tr>");
			out.println( "<tr><td><font size=2 color=#fffacd><b>" + uni.convert( "Ȩ������" ) + "</b></font></td><td><input type=text size=40 name=homepage value =\"" + info.getString( "homepage") + "\"></td></tr>");
			out.println( "<tr><td><font size=2 color=#fffacd><b>" + uni.convert( "�ּ�" ) + "</b></font></td><td><input type=text size=50 name=address value=\"" + info.getString( "address") + "\"></td></tr>" );
			out.println( "<tr><td colspan=2 align=center><br><p>");
			out.println( "<input type=submit value=" + uni.convert( "����" ) + ">");
			out.println("<input type=button value=" + uni.convert( "���" ) + " onClick=\"window.history.go(-1);\"></td></tr>" );
			out.println( "</table></td></tr>" );
			out.println( "</table></body></html>" );
			
	
		} catch( SQLException e ) {
			out.println( head );
			out.println( "fail to dbmanager " + e.toString() );
			out.println( end );
			return;

		} catch( CommandException e ){
			out.println( head );
			out.println( "fail to ComInfoFrom " + e.toString() );
			out.println( end );
			return;
		}
	}
	
	/**
	 *  registerComInfo - ȸ������������ ���� object�� Request List�� �ִ´�.
	 *
	 *  @param req 	�Է����� ���� �����Ͱ� ����ִ� ��
	 *  @param out 	����� ����ϴ� ��
	 */
	public void registerComInfo( HttpServletRequest req, PrintWriter out) throws UnsupportedEncodingException {
		// session�� �� session���� parameter�� parsing �Ѵ�.
		HttpSession session = req.getSession( false );
		
		if( session == null ) {
			out.println( head );
			out.println( "<h1>Unorthrized User !! </h1> " );
			out.println( end );
			return; 
		}		
	
		// session���� id�� �� ������ ������.
		String id = (String)session.getValue( "id" );	
		String comid = (String)session.getValue( "comid" );
		
		// �Է� �����͸� parsing �Ѵ�.
		String s_site		= asc.convert(req.getParameter( "site" ));
		String s_address	= asc.convert(req.getParameter( "address" ));
		String s_email   	= req.getParameter( "email" );
		String s_fax	   	= req.getParameter( "fax" );
		String s_homepage  	= req.getParameter( "homepage" );
		String s_tel 	 	= req.getParameter( "tel" );

		String c_site= "";
		String address = "";
		String email = "";
		String fax = "";
		String homepage = "";
		String tel = "";
		String d_site= "";
		String d_address = "";
		String d_email = "";
		String d_fax = "";
		String d_homepage = "";
		String d_tel = "";	

		//tokenizing �ؼ� space���ڸ� �����ش�.
		StringTokenizer st = new StringTokenizer( s_site );
		while(st.hasMoreTokens())
			if( c_site == "")  c_site = st.nextToken();
			else c_site  = c_site  + st.nextToken();

		st = new StringTokenizer(s_address);
		while(st.hasMoreTokens())
			if( address== "") address = st.nextToken();
			else address = address + st.nextToken();

		st = new StringTokenizer( s_tel);
		while(st.hasMoreTokens())
			if( tel== "") tel = st.nextToken();
			else tel =tel  + st.nextToken();

		st = new StringTokenizer( s_fax );
		while(st.hasMoreTokens())
			if( fax == "")  fax = st.nextToken();
			else  fax =  fax + st.nextToken();

		st = new StringTokenizer( s_homepage);
		while(st.hasMoreTokens())
			if( homepage == "") homepage  = st.nextToken();
			else homepage  = homepage  + st.nextToken();

	 	st = new StringTokenizer( s_email);
		while(st.hasMoreTokens())
			if( email == "")  email = st.nextToken();
			else  email = email  + st.nextToken();	

		ResultSet cominfo = null;
		try {
			cominfo = dbmanager.getCompanyInfo( id );
			cominfo.next();
		
			st = new StringTokenizer( cominfo.getString( "site" ) );
			while(st.hasMoreTokens())
				if( d_site == "")  d_site = st.nextToken();
				else d_site  = d_site  + st.nextToken();
	
			st = new StringTokenizer(cominfo.getString( "address"));
			while(st.hasMoreTokens())
				if( d_address== "") d_address = st.nextToken();
				else d_address = d_address + st.nextToken();
	
			st = new StringTokenizer( cominfo.getString( "tel"));
			while(st.hasMoreTokens())
				if( d_tel== "") d_tel = st.nextToken();
				else d_tel =d_tel  + st.nextToken();
	
			st = new StringTokenizer(  cominfo.getString( "fax") );
			while(st.hasMoreTokens())
				if( d_fax == "")  d_fax = st.nextToken();
				else  d_fax =  d_fax + st.nextToken();
	
			st = new StringTokenizer( cominfo.getString( "homepage"));
			while(st.hasMoreTokens())
				if( d_homepage == "") d_homepage  = st.nextToken();
				else d_homepage  = d_homepage  + st.nextToken();
	
		 	st = new StringTokenizer(cominfo.getString( "email"));
			while(st.hasMoreTokens())
				if( d_email == "")  d_email = st.nextToken();
				else  d_email = d_email  + st.nextToken();	

			if( uni.convert(address).equals( d_address )  && 
					email.equals( d_email ) && 
					fax.equals( d_fax )  &&
					homepage.equals( d_homepage ) && 
					tel.equals( d_tel ) && 
					uni.convert(c_site).equals( d_site )  ) {
				out.println( head );
				out.println( "<center><font size=4>" + uni.convert( "������ ������ �����ϴ�." ) + "<br><p>" );
				out.println("<a href=\"javaScript:window.history.go(-1);\"><font size=2><b><-Back</a></center>");
				out.println( end );
				return;
			}
			
			
				 
		} catch( SQLException e ){
			out.println( head );
			out.println( "fail to dbmanager " + e.toString() ); 
			out.println( end );
			return;
		} catch( CommandException e) {
			out.println( head );
			out.println( "fail to registerComInfo " + e.toString() ); 
			out.println( end );
			return;
		}

		String back = "comChange";

		try {
			if( ( !c_site.equals( "" ) ) && !uni.convert( c_site ).equals( d_site ) ) {
				back += " " + uni.convert(c_site);
			} 
 
			// �Է¹��� comInfo�� DB�� �����Ѵ�.
			if( ( ( address != null ) && !uni.convert(address).equals( d_address ) ) ||  
				!email.equals( d_email ) || 
				!fax.equals( d_fax ) || 
				!homepage.equals( d_homepage ) || 
				( ( tel != null ) && !tel.equals( d_tel ) ) || 
				( ( c_site != null ) && !uni.convert(c_site).equals( d_site )  )  ){

				ResultSet change = dbmanager.getComChange( comid );
				if( change.next() ){
					String h_site= "";
					String c_address = "";
					String c_email = "";
					String c_fax = "";
					String c_homepage = "";
					String c_tel = "";	

					st = new StringTokenizer( change.getString( "site" ) );
					while(st.hasMoreTokens())
						if( h_site == "")  h_site = st.nextToken();
						else h_site  = h_site  + st.nextToken();
		
					st = new StringTokenizer(change.getString( "address"));
					while(st.hasMoreTokens())
						if( c_address== "") c_address = st.nextToken();
						else c_address = c_address + st.nextToken();
			
					st = new StringTokenizer( change.getString( "tel"));
					while(st.hasMoreTokens())
						if( c_tel== "") c_tel = st.nextToken();
						else c_tel =c_tel  + st.nextToken();
			
					st = new StringTokenizer(  change.getString( "fax") );
					while(st.hasMoreTokens())
						if( c_fax == "")  c_fax = st.nextToken();
						else  c_fax =  c_fax + st.nextToken();
			
					st = new StringTokenizer( change.getString( "homepage"));
					while(st.hasMoreTokens())
						if( c_homepage == "") c_homepage  = st.nextToken();
						else c_homepage  = c_homepage  + st.nextToken();
			
				 	st = new StringTokenizer(change.getString( "email"));
					while(st.hasMoreTokens())
						if( c_email == "")  c_email = st.nextToken();
						else  c_email = c_email  + st.nextToken();	
	
					if( d_site.equals( h_site ) &&  c_address.equals( d_address ) &&
						d_tel.equals( c_tel ) &&  d_fax.equals( c_fax ) &&
						d_homepage.equals( c_homepage ) && d_email.equals( c_email ) ) {
						out.println( head );
						out.println( "�̹� ��ϵǾ��ִ� �����Դϴ�.�� ���Ϸ� �����帮�ڽ��ϴ�" );
						out.println( end );
						return;
					}
 
				}

				// �Է¹��� comInfo�� DB�� �����Ѵ�.
				dbmanager.addComInfo( comid, uni.convert(s_address), tel, fax, homepage, email, back );
			}
		} catch( SQLException e ) {
			out.println( head );
			out.println( "fail to register cominfo at cominfomanager" + e.toString() );
			out.println( end );
			return;
		} catch( CommandException e ) {
			out.println( head );
			out.println( "fail to dbmanager " + e.toString() ); 
			out.println( end );
			return;
		}
				
		out.println(" <html><head><title></title></head><body background=/bg.jpg>");
		out.println( "<center><font size=4>" + uni.convert( "ȸ������������ ��ϵǾ����ϴ�. ���Ϸ� �����帮�ڽ��ϴ�." ) + "<br><p>" );
		out.println("<a href=/namecard/UserServlet?cmd=showDirList><font size=2><b><-Back</a></center>");
		out.println("</body></html>");
		
	
	}
			

	//////////////////////////////  ȸ���������� ó�� ////////////////////////////
	
	/**
	 *  comChangeList - comChangeList command�� ó���ϴ� method
	 *                  ȸ���������� request list�� �����ش�.
	 * 
	 *  @param req      ������ �Է¹��� �����Ͱ� ����ִ� ��
	 *  @param out  	����� ����� ��
	 */
	public void comChangeList( HttpServletRequest req, PrintWriter out  )throws UnsupportedEncodingException {
		// session�� �� session���� parameter�� parsing �Ѵ�.
		HttpSession session = req.getSession( false );
		
		if( session == null ) {
			out.println( head );
			out.println( "<h1>Unorthrized User !! </h1> " );
			out.println( end );
			return; 
		}		


		// ComReqList�� �ִ� ComInfo object list�� �����ش�.
		try {
			out.println( "<br><center>Com Info Update Request List<p><br>" );
			out.println( "<form method=get action=/namecare/AdminServlet><table>" );
			out.println( "<table>" );
			out.println( "<tr><td>" + uni.convert( "ȸ���̸�" ) + "</td><td>" + uni.convert( "����" ) + "</td><td>" + uni.convert( "����" ) + "</td><tr>" );

			ResultSet comChangeList = dbmanager.getComChangeList();

			// DB���� list�� ���´�.	
			while( comChangeList.next() ) {	
				String back = comChangeList.getString( "back" );
				if( !back.startsWith( "comChange" ) ) continue;

				String num = comChangeList.getString( "id" );
				out.println( "<tr><td><a href=/namecard/AdminServlet?cmd=showComInfo&index=" + num + ">" + comChangeList.getString( "name") + "</a></td><td>" );
				int region_num =  comChangeList.getInt( "region" );	

				if( region_num == 1)  
						out.println( uni.convert( "����Ư����" ));
				else if( region_num == 2 ) 
						out.println( uni.convert( "�λ걤����" ));
				else if( region_num == 3 ) 
						out.println( uni.convert( "�뱸������" ));
				else if( region_num == 4 ) 
						out.println( uni.convert( "��õ������" ));
				else if( region_num == 5 ) 
						out.println( uni.convert( "���ֱ�����" ));
				else if( region_num == 6 ) 
						out.println( uni.convert( "��걤����" ));
    			else if( region_num == 7 ) 
						out.println( uni.convert( "����������" ));
			 	else if( region_num ==8 ) 
						out.println( uni.convert( "���" ));
				else if( region_num == 9 ) 
						out.println( uni.convert( "����" ));
				else if( region_num == 10 ) 
						out.println( uni.convert( "���" ));
				else if( region_num == 11 ) 
						out.println( uni.convert( "�泲" ));
				else if( region_num == 12 ) 
						out.println( uni.convert( "����" ));
				else if( region_num == 13 ) 
						out.println( uni.convert( "����" ));
				else if( region_num == 14 ) 
						out.println( uni.convert( "���" ));
				else if( region_num == 15 ) 
						out.println( uni.convert( "�泲" ));
			 	else if( region_num == 16 ) 
						out.println( uni.convert( "����" ));
			 	out.println( "</td><td>" + comChangeList.getString( "site" ) + "</td></tr>" );	
			}
			out.println( "</table></form></center>");
				
		} catch( SQLException e ) {
			out.println( head );
			out.println( "fail to dbmanager" + e.toString() );
			out.println( end );
			return;

		} catch( CommandException e){
			out.println( head );
			out.println( "fail to comChangeList" + e.toString() );
			out.println( end );
			return;
		}

	
	}
	
	/** 
	 *  showComInfo - ȸ���������� ����Ʈ�� �ִ� �������� �����ش�.
	 *
	 *  @param req  				������ ���� �Է��� ����ִ� ��
	 *  @param out  				����� ����� ��
	 */
	public void showComInfo( HttpServletRequest req, PrintWriter out )throws UnsupportedEncodingException{
		// session�� �� session���� parameter�� parsing �Ѵ�.
		HttpSession session = req.getSession( false );
		
		if( session == null ) {
			out.println( head );
			out.println( "<h1>Unorthrized User !! </h1> " );
			out.println( end );
			return; 
		}		

		int update = 0;
		
		// request���� parameter parsing�ؼ� ȸ�� ���̵� ��´�. 
		String num  =  req.getParameter( "index" );
			
		ResultSet change = null;
		ResultSet info = null;
	
		// ���õ� object�� list���� ������.
		try {
			change = dbmanager.getComChange( num );
			change.next();
			info = dbmanager.getComInfo( num );
			info.next();
		} catch( SQLException e ) {
			out.println( head );
			out.println( "fail to dbmanager" + e.toString() );
			out.println( end );
			return;

		} catch( CommandException e ) {
			out.println( head );
			out.println( "fail to showComInfo" + e.toString() );
			out.println( end );
			return;
		}	

		// session ���� company object�� �Ѱ��ش�.
		session.putValue( "com" , num );

		try {
			// ComReqList���� ���õ� object�� ������ �����ش�.
			out.println( "<h2>" + uni.convert( "ȸ����������" ) + " - " + change.getString( "name" ) + "</h2>" );
			String back = change.getString( "back" );
			if( back.length() > 9 ){
				String c_site = back.substring( 10 );
				out.println( "<br>" + uni.convert( "����" ) + ": " + change.getString( "site" ) + "-> " +
				c_site );
				update++;
			}			
			if( !change.getString( "address").equals( info.getString("address") ) ) {
				String address = info.getString( "address" );
				if( address == "" ) address = uni.convert( "�������" );
				out.println( "<br>" + uni.convert( "�ּ� " ) + ": " + address + " -> " + change.getString( "address") );
				update++;
			}
			if( !change.getString( "tel").equals( info.getString( "tel") ) ) {
				String tel = info.getString( "tel" );
				if( tel == "" ) tel = uni.convert( "�������" );
				out.println( "<br>" + uni.convert( "��ȭ" ) + " : " + tel + " -> " + change.getString( "tel") );
				update++;
			}
			if( !change.getString( "fax").equals( info.getString( "fax") ) ) {
				String fax = info.getString( "fax" );
				if( fax == "" ) fax = uni.convert( "�������" );
				out.println( "<br>" + uni.convert( "�ѽ�" ) + " : " + fax + " -> " + change.getString("fax") );
				update++;
			}
			if( !change.getString( "email").equals( info.getString( "email") ) ) {
				String email = info.getString( "email" );
				if( email == "" ) email = uni.convert( "�������" );
				out.println( "<br>" + uni.convert( "����" ) + " : " + email + " -> " + change.getString( "email" ) );
				update++;
			}
			if( !change.getString( "homepage").equals( info.getString( "homepage") ) ) {
				String homepage = info.getString( "homepage" );
				if( homepage == "" ) homepage = uni.convert( "�������" ); 
				out.println( "<br>" + uni.convert( "Ȩ������" ) + " : " + homepage + " -> " + change.getString( "homepage") );
				update++;
			}
			
			if( update == 0 ) {
				out.println( "<h1>" + uni.convert( "������ ������ ��� ����Ʈ���� �����մϴ�." ) + "</h1>" );
				// request list�� �ִ� object�� �����Ѵ�. �׸��� �̸����� �߼��Ѵ�.
				dbmanager.deleteComChange( num );
					
			} else {
				// ������ư
				out.println( "<form method=post action=/namecard/AdminServlet>" );
				out.println( "<input type=hidden name=cmd value=updateCompanyInfo>" );
				out.println( "<input type=hidden name=num value=\"" + num + "\">" );
				out.println( "<input type=submit value=" + uni.convert( "����" ) + "></form>" );
				// ��ҹ�ư 
				out.println( "<form method=post action=/namecard/AdminServlet>" );
				out.println( "<input type=hidden name=cmd value=cancelCompanyInfo>" );
				out.println( "<input type=hidden name=num value=\"" + num + "\">" );
				out.println( "<input type=submit value=" + uni.convert( "���" ) + "></form>" );
			
			}
		} catch( SQLException e ) {
			out.println( head );
			out.println( "fail to dbmanager " + e.toString() );
			out.println( end );
			return;

		} catch( CommandException e ) {
			out.println( head );
			out.println( "fail to showComInfo " + e.toString() );
			out.println( end );
			return;
		}	

	}


	/**
	 *  updateCompanyInfo - updateCompanyInfo commmand�� ó���ϴ� method
	 *                      ����Ʈ���� ������ ȸ���� ������ �����Ѵ�.
	 * 
	 *  @param req 		�Է¹��� �����Ͱ� ����ִ� ��
	 *  @param out  	����� ����� ��
	 */
	public void updateCompanyInfo( HttpServletRequest req, PrintWriter out)throws UnsupportedEncodingException {
		// session�� ��´�.
		HttpSession session = req.getSession( false );
		
		if( session == null ) {
			out.println( head );
			out.println( "<h1>Unorthrized User !! </h1> " );
			out.println( end );
			return; 
		}		
		
		// parameter parsing �� �Ѵ�.
		String num = req.getParameter( "num" );
		
		// session���� company object��  ComInfoChange object�� ��´�.
	 	String comid = (String)session.getValue( "com" );
	 	ResultSet change;
	  
	  	session.removeValue( "com" );
	  
	  	try {
	  		// ���� ����Ǿ��ִ� ������ ��´�.
			change = dbmanager.getComChange( num );	
			change.next();
			// ���ο� ComInfo�� �����.
			dbmanager.updateComInfo( num, change.getString( "address" ), change.getString( "tel" ), change.getString( "fax" ), change.getString( "homepage" ), change.getString( "email" ), change.getString( "back" ) );
			
			
			dbmanager.deleteComChange( num );
		} catch( SQLException e ) {
			out.println( head );
			out.println( "fail to dbmanager " + e.toString() );
			out.println( end );
			return;	
		} catch( CommandException e ){
			out.println( head );
			out.println( "fail to updateCompanyInfo " + e.toString() );
			out.println( end );
			return;
		}	
		
		out.println( head );
		out.println( "<h2>" + uni.convert( "ȸ�������� �����Ǿ����ϴ�." ) + "</h2>" );
		out.println( end );
		// �̸����� �߼��Ѵ�.
		
		return;
	}
	
	
	/**
	 *  cancelCompanyInfo - cancelCompanyInfo command�� ó���ϴ� method
	 *                      ȸ������������ ����Ѵ�.
	 *
	 *  @param req  ������ �Է¹��� �����Ͱ� ����ִ� ��
	 *  @param out	����� ����� ��
	 */
	public void cancelCompanyInfo( HttpServletRequest req, PrintWriter out)throws UnsupportedEncodingException {
		// session�� ��´�.
		HttpSession session = req.getSession( false );
		
		if( session == null ) {
			out.println( head );
			out.println( "<h1>Unorthrized User !! </h1> " );
			out.println( end );
			return; 
		}		
	
	  	// parameter parsing �� �Ѵ�.
		String num = req.getParameter( "num" );
			
		// session�� �־��� ������ �����.
	  	session.removeValue( "com" );

		try {
			// DB���� �������� �����͸� �����Ѵ�. 
			dbmanager.deleteComChange( num );
		} catch( CommandException e ) {
			out.println( head );
			out.println( "fail to dbmanager " + e.toString() );
			out.println( end );
			return;			
		}
		
		out.println( "<h2>" + uni.convert( "ȸ������ ������ ��ҵǾ����ϴ�." ) + "</h2>" );
		//  ������ �߼��Ѵ�.	
	}

}
