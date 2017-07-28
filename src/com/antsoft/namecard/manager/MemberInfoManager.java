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
	 *  updatePersonForm -  �������������� ���� ���� �����ִ� method
	 *
	 *  @param  req      ������ �Է¹��� �����Ͱ� ����ִ� ��
	 *  @param  out      ����� ����� ��
	 */
	public void updatePersonForm( HttpServletRequest req, PrintWriter out )  throws UnsupportedEncodingException {
		// ���� �˻�
		HttpSession httpsession = req.getSession( false );
		if( httpsession == null ) {
			out.println( head );
			out.println( "<h1>Unauthorized User! </h1>" );
			out.println( end );
			return;
		}
		
		String id = (String)httpsession.getValue( "id" );
	
		ResultSet member = null;

		// httpsession���� ���̵� �� ����ڿ� ���� ���� ������ ��´�.
		try {
			member = dbmanager.getMemberInfo( id );
		 	member.next();	
	
			out.println("<body background=/bg.jpg>");
			out.println("<table width=680 border=0 cellspacing=0>");
			out.println("<tr>");
			out.println("<td height=28 align=center><font color=#ffff00><img src=/icon.gif><b> " + uni.convert( "��������������") +"</b></font></td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td align=center valign=top>");
			out.println("<br><p><br><p><table border=0 width=80% align=center cellspacing=0>");
			out.println("<tr><form method=post action=/namecard/UserServlet>");
			out.println("<input type=hidden name=cmd value=updatePersonInfo>"); 
			out.println("<td><font color=#fffacd size=2><b>ID</b></font></td>");
			out.println("<td><font size=2 color=#e0ffff><b>"+id+"</font></td>");
			out.println("</tr>");
			out.println("<td><font size=2 color=#fffacd ><b>" + uni.convert( "�н����� " ) + "</b></font></td>");
			out.println("<td><input type=password name=passwd size=10 maxlength=10></td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td><font size=2 color=#fffacd><b>" + uni.convert("�н�����Ȯ��") + "</b></font></td>");
			out.println("<td><input type=password name=checkpass size=10 maxlength=10></td>");
			out.println("</tr>");
			out.println("<tr><td><font size=2 color=#fffacd><b>" + uni.convert( "�̸�" ) + "</b></font></td>");
			out.println("<td><font size=2 color=#e0ffff><b>"+member.getString( 3 )+"</font></td></tr>");
			out.println("<tr><td><font size=2 color=#fffacd><b>" + uni.convert( "�ֹε�Ϲ�ȣ" ) + "</b></font></td>");
			out.println("<td><font size=2 color=#e0ffff><b>"+member.getString( 4 )+"</font></td></tr>");
			out.println("<tr><td><font size=2 color=#fffacd><b> " + uni.convert( "�������" ) + "</b></font></td>");
			out.println("<td><font size=2 color=#e0ffff><b>"+member.getString( 5 )+"</font></td></tr>");
			out.println("<tr><td><font size=2 color=#fffacd><b>" + uni.convert( "����" ) + "</b></font></td>");
			
			if(member.getString( 6 ).equals("m")) out.println("<td><font size=2 color=#e0ffff><b>" + uni.convert( "��" ) + "</font></td></tr>");
			else out.println("<td><font size=2 color=#e0ffff><b>" + uni.convert( "��" ) +"</font></td></tr>");
  		
			out.println("<tr><td><font size=2 color=#fffacd><b>" + uni.convert( "�ּ�" ) + "</b></font></td>");
			out.println("<td><input type=text name=address size=40 value=\""+ member.getString( 7 ) +"\"></td></tr>");
			out.println("<tr> <td><font size=2 color=#fffacd><b>" + uni.convert( "��ȭ" ) +"</b></font></td>");
	    	out.println("<td><input type=text name=tel size=12 value=\""+member.getString( 8 )+"\"> </td></tr>");
	  		out.println("<tr><td><font size=2 color=#fffacd><b>" + uni.convert( "�߻�" ) +"</b></font></td>");
	    	out.println("<td><input type=text name=beeper size=13 value=\""+member.getString( 9 )+"\"></td> </tr>");
			out.println("<tr><td><font size=2 color=#fffacd><b>" + uni.convert( "�޴���" ) +"</b></font></td>");
	    	out.println("<td><input type=text name=phone size=13 value=\""+member.getString( 10 )+"\"></td> </tr>");
	  		out.println("<tr><td><font size=2 color=#fffacd><b>" + uni.convert( "Ȩ������" )+"</b></font></td>");
	    	out.println("<td><input type=text name=homepage size=25 value=\""+member.getString( 11 )+"\"></td></tr>");
			out.println("<tr> <td><font size=2 color=#fffacd><b>e-mail</b></font></td>");
   		 	out.println("<td><input type=text name=email size=25 value=\""+member.getString( 12 )+"\"></td></tr>");
 	 		out.println("<tr> <td><font size=2 color=#fffacd><b>" + uni.convert( "����" ) + "</b></font></td>");
	    	out.println("<td><input type=text name=position size=10 value="+member.getString( 13 )+"></td> </tr>");
			out.println("<tr> <td><font size=2 color=#fffacd><b>" + uni.convert( "�μ�" ) + "</b></font></td>");
	    	out.println("<td><input type=text name=part size=15 value=\""+member.getString( 14 )+"\"></td> </tr>");
	  		out.println("<tr> <td><font size=2 color=#fffacd><b>" + uni.convert( "ȸ�簳����ȭ" ) + "</b></font></td> ");
			out.println("<td> <input type=text name=j_tel size=13 value=\""+member.getString( 15 )+"\"></td> </tr>");
			out.println("<tr> <td><font size=2 color=#fffacd><b>" + uni.convert( "ȸ���ѽ�" ) + "</b></font></td>");
	  	  	out.println("<td><input type=text name=j_fax size=13 value=\""+member.getString( 16 )+"\"><br><p></td> </tr>");
			out.println("<tr> <td align=right><br><p><input type=submit value=" + uni.convert( "���" ) +"></form></td>");
			out.println("<form method=get action=/namecard/UserServlet><td align=left>");
			out.println("<input type=hidden name=cmd value=showDirList>");
			out.println("<input type=submit value=" + uni.convert( "���" ) + "></td> </form></tr>");
	        out.println("</table></td> </tr> </table></center> </body> </html>");			
	
		} catch( SQLException e ) {
			out.println( head );
			out.println( e.toString() + uni.convert(" �ٽ� �õ��ϼ���" ));
			out.println( end );
			return;
		} catch( CommandException e ) {
			out.println( head );
			out.println( "DB���� ���ӿ� ������ �ֽ��ϴ�. �ٽ� �õ��ϼ���"  + e.toString() );
			out.println( end );
			return;
		}	
	}
	
	/**
	 *  showUpdatedPersonInfo - ������ ���������� �����ִ� �� 
	 *
	 *  @param  req      ������ �Է¹��� �����Ͱ� ����ִ� ��
	 *  @param  out      ����� ����� ��
	 */
	public void showUpdatedPersonInfo( HttpServletRequest req, PrintWriter out ) throws UnsupportedEncodingException {
		// ���� �˻�
		HttpSession httpsession = req.getSession( false );
		if( httpsession == null ) {
			out.println( head );
			out.println( "<h1>Unauthorized User! </h1>" );
			out.println( end );
			return;
		}
		
		String id = (String)httpsession.getValue( "id" );
		ResultSet member = null;
	
		// httpsession���� ���̵� �� ����ڿ� ���� ���� ������ ��´�.
		try {
			member = dbmanager.getMemberInfo( id );
			member.next();
		
			out.println("<body background=/bg.jpg>");
			out.println("<table width=600 border=0 cellspacing=0>");
			out.println("<tr>");
			out.println("<td height=28 align=center><br><p><font color=#ffff00><img src=/icon.gif><b>" + uni.convert( "��������" ) + "</b></font></td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td align=center valign=top>");
			out.println("<br><p><table border=0 width=80% align=center cellspacing=0>");
			out.println("<td><font size=2 color=#fffacd><b>ID</b></font></td>");
			out.println("<td><font size=2><b>"+id+"</font></td>");
			out.println("</tr>");
			out.println("<tr><td height=20><font size=2 color=#fffacd><b>" + uni.convert( "�̸�" ) + "</b></font></td>");
			out.println("<td><font size=2><b>"+member.getString( 3 )+"</font></td></tr>");
			out.println("<td height=20><font size=2 color=#fffacd><b>"+ uni.convert( "�н�����" ) + "</b></font></td>" );
			out.println("<td><font size=2><b>"+member.getString("password")+"</font></td></tr>"); 
			out.println("<tr height=20><td><font size=2 color=#fffacd><b>" + uni.convert( "�ֹε�Ϲ�ȣ" ) + "</b></font></td>");
			out.println("<td><font size=2><b>"+member.getString(4 )+"</font></td></tr>");
			out.println("<tr><td height=20><font size=2 color=#fffacd><b>" + uni.convert( "�������" ) + "</b></font></td>");
			out.println("<td><font size=2><b>"+member.getString( 5 )+"</font></td></tr>");
			out.println("<tr><td height=20><font size=2 color=#fffacd><b>" + uni.convert( "����" ) +"</b></font></td>");
			
			if(member.getString( 6 ).equals("m")) out.println("<td><font size=2><b>" + uni.convert( "��")+ "</font></td></tr>");
			else out.println("<td><font size=2><b>" + uni.convert( "��" ) +"</font></td></tr>");
 	 		
			out.println("<tr><td height=20><font size=2 color=#fffacd><b>" + uni.convert( "�ּ�" ) + "</b></font></td>");
	    	out.println("<td><font size=2><b>"+member.getString( 7 ) +"</td></tr>");
			out.println("<tr> <td height=20><font size=2 color=#fffacd><b>" + uni.convert( "��ȭ" ) + "</b></font></td>");
	    	out.println("<td><font size=2><b>"+member.getString( 8 )+" </td></tr>");
	  		out.println("<tr><td height=20><font size=2 color=#fffacd><b>" + uni.convert( "�߻�" ) + "</b></font></td>");
	    	out.println("<td><font size=2><b>"+member.getString( 9 )+"</td> </tr>");
			out.println("<tr><td height=20><font size=2 color=#fffacd><b>" + uni.convert( "�޴���" ) + "</b></font></td>");
	    	out.println("<td><font size=2><b>"+member.getString( 10 )+"</td> </tr>");
	  		out.println("<tr><td height=20><font size=2 color=#fffacd><b>" + uni.convert( "Ȩ������" ) + "</b></font></td>");
	    	out.println("<td><font size=2><b>"+member.getString( 11 )+"</td></tr>");
			out.println("<tr> <td height=20><font size=2 color=#fffacd><b>e-mail</b></font></td>");
	    	out.println("<td><font size=2><b>"+member.getString( 12 )+"</td></tr>");
	  		out.println("<tr> <td height=20><font size=2 color=#fffacd><b>" + uni.convert( "����" ) + "</b></font></td>");
	    	out.println("<td><font size=2><b>"+member.getString( 13 )+"</td> </tr>");
			out.println("<tr><td height=20><font size=2 color=#fffacd><b>" + uni.convert( "�μ�" ) + "</b></font></td>");
	    	out.println("<td><font size=2><b>"+member.getString( 14 )+"</td> </tr>");
	  		out.println("<tr> <td height=20><font size=2 color=#fffacd><b>" + uni.convert( "ȸ�簳����ȭ" ) + "</b></font></td> ");
			out.println("<td><font size=2><b>"+member.getString( 15 )+"</td> </tr>");
			out.println("<tr> <td height=20><font size=2 color=#fffacd><b>" + uni.convert( "ȸ���ѽ�" ) + "</b></font></td>");
	    	out.println("<td><font size=2>"+member.getString( 16 )+"<br><p></td> </tr>");
			out.println("<tr> <td colspan=2 align=center>");
			out.println("<a href=\"javaScript:window.history.go(-2);\"><font size=2 color=#ffffff><b><-Back</a><br><p></td></tr>");
	        out.println("</table></td></tr></table></center> </body> </html>");			
	
		} catch( SQLException e ) {
			out.println(head );
			out.println( e.toString() + uni.convert("�ٽ� �õ��ϼ���" ) );
			out.println(end );
		 	return;
		} catch( CommandException e ) {
			out.println(head );
			out.println( e.toString() + uni.convert( "�ٽýõ��ϼ���" ) );
			out.println(end );
			return;
		}	
	}

	/** 
	 *  updatePersonInfo -  updatePersonInfo command�� ó���ϴ� method
	 *  				    ���������� �����ϴ� method
	 *
	 *  @param  req      ������ �Է¹��� �����Ͱ� ����ִ� ��
	 *  @param  out      ����� ����� ��
	 *  @throws UnsupportedEncodingException 
	 */
	public void updatePersonInfo( HttpServletRequest req, PrintWriter out ) throws UnsupportedEncodingException {
		// ���� �˻�
		HttpSession httpsession = req.getSession( false );
		if( httpsession == null ) {
			out.println(head );
			out.println( "<h1>Unauthorized User! </h1>" );
			out.println(end );
			return;
		}
				
		// req���� �Ķ���� �Ľ��Ѵ�.
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

		//tokenizing �ؼ� space���ڸ� �����ش�.
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

		// httpsession���� ���̵� ��´�.
		String id = (String)httpsession.getValue( "id" );
		ResultSet member = null;
		
		// ���������� ��´�.
		try {
			member = dbmanager.getMemberInfo( id );
			member.next();	
		
			// DB�� ����ִ� ���� parsing�Ѵ�.
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
			out.println( e.toString() + uni.convert(" �ٽ� �õ��ϼ���" ));
			out.println(end );
			return;
		} catch( Exception e ) {
			out.println(head );
			out.println( uni.convert( "DB���� ���ῡ ������ �ֽ��ϴ�. �ٽ� �õ��ϼ���" ) + e.toString() );
			out.println(end );
			return;
		}	
		boolean changed = false;

		// �н����� ����
		try {	
			if( passwd != "" ) {
				if( passwd.equals( checkpass ) ) {
					dbmanager.updatePassword( id, uni.convert(passwd) );
					changed = true;
				} else {
					out.println(head );
					out.println( uni.convert( " �н����带 �ٽ� �Է��ϼ���"  ));
					out.println(end );
					return;
				}
			}
			// person info�� ����� ������ �ϳ��� ������ update�Ѵ�.
			if( ( ( address != "" ) && !uni.convert(address).equals( d_address ) )  || 
				( ( tel != "" ) && !tel.equals( d_tel ) )  || 
				( ( beeper != "" ) && !beeper.equals( d_beeper ) ) || 
				( ( phone != "" ) && !phone.equals( d_phone) )  || 
				( ( email != "" ) && !email.equals( d_email) ) || 
				( ( homepage != "" ) && !homepage.equals( d_homepage) ) ) {
				dbmanager.updatePersonInfo( id, uni.convert(s_address), tel, beeper, s_phone, email, homepage );
				changed = true;
			}
		
			// job info�� ����� ������ �ϳ��� ������ update�Ѵ�.
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
				out.println( uni.convert( "<center>������ ������ �����ϴ�. Ȯ���Ͻð� �ٽ� �Է��ϼ���<br>" ) );
				out.println("<br><a href=\"javaScript:window.history.go(-1);\"><font size=2 color=#ffffff><b><-Back</a><br><p></center>");
				out.println(end );
			}	
		} catch( CommandException e ) {
			out.println(head );
			out.println( e.toString() + uni.convert("�ٽ� �õ��ϼ���" ));
			out.println(end );
			return;
		}
		
	}
	
	
	/**
	 *  jobChangeForm - jobChangeForm command ó���ϴ� method
	 *                  ȸ�������� ���� ���� �����ش�.
	 *
	 *  @param  req  ������ �Է¹��� �����Ͱ� ����ִ� ��
	 *  @param  out  ����� ����� ��
	 */
	public void jobChangeForm( HttpServletRequest req, PrintWriter out )  throws UnsupportedEncodingException {
		// ���� �˻�
		HttpSession httpsession = req.getSession( false );
		if( httpsession == null ) {
			out.println(head );
			out.println( "<h1>Unauthorized User! </h1>" );
			out.println(end );
			return;
		}
		String id = (String)httpsession.getValue("id");
		ResultSet member = null;
		
		// memberPerson�� ��´�.
		try {
			member = dbmanager.getMemberPerson( id );
			member.next();
		
		
			// �Է����� �����ش�.
			out.println( "<html><head><title></title></head>");
			out.println("<body background=/bg.jpg><table border=0 width=600 cellspacing=0>");
			out.println("<tr><td align=center><br><p>");
			out.println( "<form method=post action=/namecard/UserServlet> " );
			out.println( "<input type=hidden name=cmd value=jobChangeCheck>" );
			out.println( "<img src=/icon.gif><font size=4 color=#ffff00><b>" + uni.convert( "ȸ������" ) +"</font></td></tr>" );
			out.println("<tr><td align=center><br><p><font size=2 color=#e0ffff>");
			out.println( uni.convert("���� ȸ������ �˻��� �մϴ�. *ǥ ������ �� ä���ֽñ� �ٶ��ϴ�.</td></tr>"));
			out.println("<tr><td align=center><br><p>");
			out.println("<table border=0 width=90% align=center cellspacing=0>");
			
			out.println("<tr><td><font size=2 color=#fffacd><b></td>");
			out.println("<td><font size=2 color=#fffacd><b>ID</b></font></td>");
			out.println("<td><font size=2><b>"+id+"</td></tr>"); 
			out.println("<tr><td><font size=2 color=#fffacd><b></b></font></td>");
			out.println("<td><font size=2 color=#fffacd><b>" + uni.convert( "�̸�" ) +"</b></font></td>");
			out.println("<td><font size=2><b>"+ member.getString( "name" ) +"</td></tr>");
			out.println("<tr><td align=right><font size=2 color=#fffacd><b>*</td>");
			out.println("<td><font size=2 color=#fffacd><b>" + uni.convert( "ȸ���̸�" ) + "</b></font></td>");
			out.println("<td><input type=text name=name size=20></td></tr>");
			out.println( "<tr><td align=right><font size=2 color=#fffacd><b>*</td>");
			out.println( "<td><font size=2 color=#fffacd><b>" + uni.convert( "����" ) + "<td><select name=region size=1>");
			out.println("<option value=1>" + uni.convert( "����Ư����" ) + "</option><option value=2>" + uni.convert( "�λ걤����" ) + "</option>");
			out.println("<option value=3>" + uni.convert( "�뱸������" ) + "</option><option value=4>" + uni.convert( "��õ������" ) + "</option>");
			out.println("<option value=5>" + uni.convert( "���ֱ�����" ) +"</option><option value=6>" + uni.convert( "��걤����" ) + "</option>");
			out.println( "<option value=7>" + uni.convert( "����������" ) + "</option><option value=8>" + uni.convert( "���" ) + "</option>");
			out.println( "<option value=9>" + uni.convert( "����" ) + "</option><option value=10>" + uni.convert( "���" ) + "</option>");
			out.println( "<option value=11>" + uni.convert( "�泲" ) + "</option><option value=12>" + uni.convert( "����" ) + "</option>");
			out.println( "<option value=13>" + uni.convert( "����" ) + "</option><option value=14>" + uni.convert( "���" ) + "</option>");
			out.println( "<option value=15>" + uni.convert( "�泲") + "</option><option value=16>" + uni.convert( "����" ) + "</option>");
			out.println( "</select> </td></tr>");
			out.println( "<tr><td><font size=2 color=#fffacd><b></td>");
			out.println( "<td><font size=2 color=#fffacd><b>" + uni.convert( "����" ) + "</b></td><td><input type=text name=site size=18></td>");
			out.println( "<tr><td align=right><font size=2 color=#fffacd><b>*</td>");
			out.println( "<td><font size=2 color=#fffacd><b>" + uni.convert( "����" ) + "</b></font></td>");
			out.println( "<td><select name=kind size=1>");
			out.println( "<option value=1>" + uni.convert( "������" ) + "</option>");
			out.println( "<option value=2>" + uni.convert( "����" ) + "</option>");
			out.println( "<option value=3>" + uni.convert( "�Ƿ�" ) + "</option>");
	 		out.println( "<option value=4>" + uni.convert( "����" ) + "</option>");
			out.println( "<option value=5>" + uni.convert( "���/�ڵ���" ) + "</option>");
			out.println( "<option value=6>" + uni.convert( "��ǻ��/���" ) + "</option>");
			out.println( "<option value=7>" + uni.convert( "���񽺾�" ) + "</option>");
		  	out.println( "<option value=8>" + uni.convert( "����" ) + "</option>");
		  	out.println( "<option value=9>" + uni.convert( "����/���׸���" ) + "</option>");
			out.println( "<option value=10>" + uni.convert( "���" ) + "</option>");
			out.println( "<option value=11>" + uni.convert( "����" ) + "</option>");
			out.println( "<option value=12>" + uni.convert( "����" ) + "</option>");
			out.println( "<option value=13>" + uni.convert( "������" ) + "</option>");
			out.println( "<option value=14>" + uni.convert( "��������" ) + "</option>");
			out.println( "<option value=15>" + uni.convert( "��Ÿ" ) + "</option>");
			out.println( "</select></td></tr>"); 
			out.println( "<tr><td colspan=3 align=center><br><p>");
			out.println( "<input type=submit value=" + uni.convert( "�˻�" ) + ">");
			out.println( "<input type=button value=" + uni.convert( "���" ) + " onClick=\"window.history.go(-1);\"></td></tr>" );
			out.println( "</table></body></html>" );
		} catch( SQLException e ) {
			out.println( head );
			out.println( e.toString() + uni.convert(" �ٽ� �õ��ϼ���" ) );
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
	 *  jobChangeCheck -  ȸ�������� ���� ���� �����ִ� method
	 *
	 *  @param  req      ������ �Է¹��� �����Ͱ� ����ִ� ��
	 *  @param  out      ����� ����� ��
	 */
	public void jobChangeCheck( HttpServletRequest req, PrintWriter out ) throws UnsupportedEncodingException {
		// ���� �˻�
		HttpSession httpsession = req.getSession( false );
		if( httpsession == null ) {
			out.println( head );
			out.println( "<h1>Unauthorized User! </h1>" );
			out.println( end );
			return;
		}
		
		String id = (String)httpsession.getValue( "id" );
		
		// �Ķ���� �Ľ��Ѵ�.
		String kind   = req.getParameter( "kind" );
		String s_name = asc.convert(req.getParameter( "name" ));
		String region = req.getParameter( "region" );
		String s_site = asc.convert(req.getParameter( "site" ));

		String name= "";
		String c_site = "";

		//tokenizing �ؼ� space���ڸ� �����ش�.
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
			out.println(uni.convert( "<center><br><br><font size=2 color=white> �Է��� �����մϴ�. �ٽ� �õ��Ͻʽÿ�.<br><br>" ));
			out.println("<a href=\"javaScript:window.history.go(-1);\"><font size=2><b><-back</a></center>");
			out.println( end );
			return;
		}
		
		ResultSet member = null;
		ResultSet company = null;
		String com_id = null;
		boolean com = false;		
		// httpsession���� ���̵� �� ����ڿ� ���� ���� ������ ��´�.
		try {
			member = dbmanager.getJobChange( id );
			if( !member.next()) out.println( "member is null" );	

			// ���� �ٹ��ϰ� �ִ� ȸ��� ���Ѵ�.
			if( uni.convert(name).equals( member.getString( 4 ) ) &&  (Integer.parseInt( region ) == member.getInt( "region" )  ) &&  (Integer.parseInt( kind ) == member.getInt( "kind" ) ) && uni.convert( c_site ).equals( member.getString( "site" ) ) ) {
				out.println( head );
				out.println( uni.convert( "���� �ٹ��ϰ� �ִ� ȸ���Դϴ�. ������ ȸ���� �̸��� ������ �Է��ϼ���" ) );
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
			out.println( e.toString() + uni.convert(" �ٽ� �õ��ϼ���" ));
			out.println( end );
			return;	
		} catch( CommandException e ) {
			out.println( head );
			out.println( uni.convert("DB���� ���ῡ ������ �ֽ��ϴ� �ٽ� �õ��ϼ���") + e.toString() );
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
	
		// selection���� �Է��� �޾Ƽ� �ٲ۴�.
		if( !region.equals( "0" ) ) {
			if( region.equals( "1" ) )  region = "����Ư����" ;	
			if( region.equals( "2" ) )  region = "�λ걤����";
			if( region.equals( "3" ) )  region = "�뱸������";
			if( region.equals( "4" ) )  region = "��õ������";
			if( region.equals( "5" ) )  region = "���ֱ�����";
			if( region.equals( "6" ) )  region = "��걤����";
			if( region.equals( "7" ) )  region = "����������";
			if( region.equals( "8" ) )  region = "���";
			if( region.equals( "9" ) )  region = "����";;	
			if( region.equals( "10" ) ) region = "���" ;
			if( region.equals( "11" ) ) region = "�泲";
			if( region.equals( "12" ) ) region = "����";		
			if( region.equals( "13" ) ) region = "����";
			if( region.equals( "14" ) ) region = "���";
			if( region.equals( "15" ) ) region = "�泲";
			if( region.equals( "16" ) ) region = "����";
		}
		
		if( kind != null ) {
			if( kind.equals( "1" ) )  kind = "������";
			if( kind.equals( "2" ) )  kind = "����";
			if( kind.equals( "3" ) )  kind = "�Ƿ�";
			if( kind.equals( "4" ) )  kind = "����";
			if( kind.equals( "5" ) )  kind = "���/�ڵ���";
			if( kind.equals( "6" ) )  kind = "��ǻ��/���";
			if( kind.equals( "7" ) )  kind = "���񽺾�";
			if( kind.equals( "8" ) )  kind = "����";
			if( kind.equals( "9" ) )  kind = "����/���׸���";
			if( kind.equals( "10" ) ) kind = "���";
			if( kind.equals( "11" ) ) kind = "����";
			if( kind.equals( "12" ) ) kind = "����";
			if( kind.equals( "13" ) ) kind = "������";
			if( kind.equals( "14" ) ) kind = "��������";
			if( kind.equals( "15" ) ) kind = "��Ÿ" ;
		}			
		// �Է����� �����ش�.
		out.println("<html><head><title></title></head>");
		out.println("<body background=/bg.jpg><form method=post action=/namecard/UserServlet>" );
		out.println( "<input type=hidden name=cmd value=jobChange>" );
		out.println( "<input type=hidden name=name value=\"" + uni.convert(name) + "\">" );
		out.println( "<input type=hidden name=kind value=\"" + kind_num + "\">" );
		out.println( "<input type=hidden name=region value=" + region_num + ">" );
		out.println( "<input type=hidden name=site value=\"" + uni.convert(c_site) + "\">" );
		
		try {	
			out.println("<table border=0 cellspacing=0 width=600>");
			out.println("<tr><td align=center><br><p><img src=/icon.gif><font size=4 color=#ffff00><b>" + uni.convert( "ȸ������" ) + "</td></tr>" );
			if( !com ) out.println("<tr><td align=center><br><p><font size=2 color=#e0ffff>" + uni.convert( "�˻��� ȸ�簡 �����ϴ�. �Ʒ� ���� ä���ֽʽÿ�.( *������ �ݵ�� ������ �ֽʽÿ�. )" ) + "</font></td></tr>");	
			out.println( "<tr><td align=center><br><p>");
			out.println("<table border=0 cellspacing=0 width=80%>" );
			out.println( "<tr><td></td><td><font size=2 color=#fffacd><b>ID </td><td><font size=2 color=#e0ffff><b>" + id + "</td></tr>" );
			out.println( "<tr><td></td><td><font size=2 color=#fffacd><b>" + uni.convert( "�̸�" ) + "</tr><td><font size=2 color=#e0ffff><b>" + member.getString( 2 ) + "</td></tr>" );
			out.println( "<tr><td></td><td><font size=2 color=#fffacd><b>" + uni.convert( "ȸ���̸�" ) + "</td><td><font size=2 color=#e0ffff><b>" + uni.convert(name) + "</td></tr> ");
			out.println( "<tr><td></td><td><font size=2 color=#fffacd><b>" + uni.convert( "����" ) + " </td><td><font size=2><b>" + uni.convert(region) + "</td></tr> " );
			if( !c_site.equals( "" ) ) 	out.println( "<tr><td><font size=2 color=#fffacd><b>" + uni.convert( "����" ) + "</td><td><font size=2 color=#e0ffff><b>" + uni.convert(c_site) + "</td></tr>");
			out.println( "<tr><td></td><td><font size=2 color=#fffacd><b>" + uni.convert( "����" ) + "</b></font></td><td><font size=2 color=#e0ffff><b>" + uni.convert(kind) + "</td></tr>" );
				
			if( com ) {     // �̹� �����ϴ� ȸ���� ���
				httpsession.putValue( "com", com_id );
				httpsession.putValue( "exist", "true" );
				if( !company.getString( "cominfo" ).equals( "none" ) ) {
					company = dbmanager.getComInfo( com_id );
					if( company.next( ) ) {	
						httpsession.putValue( "cominfo", "true" );
					}
					out.println( "<tr><td></td><td><font size=2 color=#fffacd><b>" + uni.convert( "��ȭ" ) + " </td><td><font size=2 color=#e0ffff><b>" + member.getString( 6  ) + "</td></tr>" );
					out.println( "<tr><td></td><td><font size=2 color=#fffacd><b>" + uni.convert( "�ѽ�" ) + " </td><td><font size=2 color=#e0ffff><b>" + member.getString( 7 ) + "</td></tr>" );
					out.println( "<tr><td></td><td><font size=2 color=#fffacd><b>" + uni.convert( "�̸���" ) + " </td><td><font size=2 color=#e0ffff><b>" + member.getString( 8 ) + "</td></tr>" );
					out.println( "<tr><td></td><td><font size=2 color=#fffacd><b>" + uni.convert( "Ȩ������" ) + " </td><td><font size=2 color=#e0ffff><b>" + member.getString( 9 ) + "</td></tr>" );
					out.println( "<tr><td></td><td><font size=2 color=#fffacd><b>" + uni.convert( "�ּ�" ) + " </td><td><font size=2 color=#e0ffff><b>" + member.getString(  10 ) + "</td></tr>" );
					out.println("<input type=hidden name=tel value=\""+member.getString( 6 )+"\">");
					out.println("<input type=hidden name=fax value=\""+member.getString(  7 )+"\">");
					out.println("<input type=hidden name=email value=\""+member.getString(  8 )+"\">");
					out.println("<input type=hidden name=homepage value=\""+member.getString(  9 )+"\">");
					out.println("<input type=hidden name=address value=\""+member.getString( 10 )+"\">");
				} else {
					httpsession.putValue( "cominfo", "false" );

					out.println( "<tr><td align=right><font size=2 color=#fffacd><b>*</td><td><font size=2 color=#fffacd><b>" + uni.convert( "��ȭ" ) + " </td><td><input type=text name=tel></td></tr>" );
					out.println( "<tr><td></td><td><font size=2 color=#fffacd><b>" + uni.convert( "�ѽ�" ) + " </td><td><input type=text name=fax></td></tr>" );
					out.println( "<tr><td></td><td><font size=2 color=#fffacd><b>" + uni.convert( "�̸���" ) + " </td><td><input type=text name=email></td></tr>" );
					out.println( "<tr><td></td><td><font size=2 color=#fffacd><b>" + uni.convert( "Ȩ������" ) + " </td><td><input type=text name=homepage></td></tr>" );
					out.println( "<tr><td align=right><font size=2 color=#fffacd><b>*</td><td><font size=2 color=#fffacd><b>" + uni.convert( "�ּ�") + " </td><td><input type=text name=address></td></tr>" );
				}
	
			} else { // ���ο� ȸ���� ���
				httpsession.putValue( "exist", "false" );
				 	
				out.println( "<tr><td align=right><font size=2 color=#fffacd><b>*</td><td><font size=2 color=#fffacd><b>" + uni.convert( "��ȭ" ) + " </td><td><input type=text name=tel></td></tr>" );
				out.println( "<tr><td></td><td><font size=2 color=#fffacd><b>" + uni.convert( "�ѽ�" ) + " </td><td><input type=text name=fax></td></tr>" );
				out.println( "<tr><td></td><td><font size=2 color=#fffacd><b>" + uni.convert( "�̸���" ) + " </td><td><input type=text name=email></td></tr>" );
				out.println( "<tr><td></td><td><font size=2 color=#fffacd><b>" + uni.convert( "Ȩ������" ) + " </td><td><input type=text name=homepage></td></tr>" );
				out.println( "<tr><td align=right><font size=2 color=#fffacd><b>*</td><td><font size=2 color=#fffacd><b>" + uni.convert( "�ּ�") + " </td><td><input type=text name=address></td></tr>" );
			}
		
			out.println( "<tr><td></td><td><font size=2 color=#fffacd><b>" + uni.convert( "�μ�" ) + " </td><td><input type=text name=part></td></tr>" );
			out.println( "<tr><td align=right><font size=2 color=#ffffacd><b>*</td><td><font size=2 color=#fffacd><b>" + uni.convert( "��å" ) + " </td><td><input type=text name=position></td></tr>" );  	
			out.println( "<tr><td align=right><font size=2 color=#fffacd><b>*</td><td><font size=2 color=#fffacd><b>" + uni.convert( "�μ���ȭ" ) + " </td><td><input type=text name=j_tel></td></tr>" );
			out.println( "<tr><td></td><td><font size=2 color=#fffacd><b>" + uni.convert( "�μ��ѽ�" ) + " </td><td><input type=text name=j_fax ></td></tr>" );
			out.println( "<tr><td colspan=3 align=center><br><p><input type=submit value=" + uni.convert( "����" ) + ">" );
			out.println( "<input type=button value=" + uni.convert( "���" ) + " onClick=\"window.history.go(-2);\"></td></tr>" );
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
	 *  jobChange -  jobChange command�� ó���ϴ� method
	 *               ȸ�������� �����Ѵ�.
	 *
	 *  @param  req      ������ �Է¹��� �����Ͱ� ����ִ� ��
	 *  @param  out      ����� ����� ��
	 */
	public void jobChange( HttpServletRequest req, PrintWriter out )throws UnsupportedEncodingException  {
		// ���� �˻�
		HttpSession httpsession = req.getSession( false );
		if( httpsession == null ) {
			out.println( head );
			out.println( "<h1>Unauthorized User! </h1>" );
			out.println( end );
			return;
		}
				
		// req���� �Ķ���� �Ľ��Ѵ�.
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
		
		//tokenizing �ؼ� space���ڸ� �����ش�.
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
			out.println( "<center><br><br><font size=2 color=white> �Է��� �����մϴ�. �ٽ� �õ��Ͻʽÿ�.<br><br>" );
		out.println("<a href=\"javaScript:window.history.go(-1);\"><font size=2><b><-back</a></font></center>"); 
			out.println( end );
			return;
		}
	
		// httpsession���� ���̵� ��´�.
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
 
		// DB connection����
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
			// ���������� ��´�.
			String query1 = "select MemberPerson.com_id, JobInfo.kind, " +
					" MemberPerson.jobinfo, Company.employee,MemberPerson.name " +
					" from  MemberPerson, Company, JobInfo  " +
					" where MemberPerson.id='" + id + "'" +
					" and JobInfo.id=MemberPerson.jobinfo " +
					" and Company.id=MemberPerson.com_id;"; 
			member = stmt.executeQuery( query1 );	
			member.next();

			String num = null;   // ���ο� ȸ���� ���̵�	
			// ȸ�簡 �����ϴ� ���
			if( exist.equals( "true" ) ) {
				num =  (String)httpsession.getValue( "com" ); 	
				String infonum = (String)httpsession.getValue( "cominfo" );

				// cominfo�� ������ ���� �����.
				if( infonum.equals("false"  )) {
					message = "fail to add ComInfo in query2 ";
					// object���� �ð� �Է�
					Calendar date1 = Calendar.getInstance();
			
					String time1 = String.valueOf(date1.get(date1.YEAR)) + String.valueOf(date1.get(date1.MONTH)) + String.valueOf(date1.get(date1.DATE)) + String.valueOf(date1.get(date1.HOUR)) + String.valueOf(date1.get(date1.MINUTE)) + String.valueOf(date1.get(date1.SECOND));
					// ȸ�� ���̵� ����
					String cominfo = time1 + id;

					// ���ο� ȸ�������� ���� ��Ͻ�Ų��.
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
				// object���� �ð� �Է�
				Calendar date1 = Calendar.getInstance();
		
				String time1 = String.valueOf(date1.get(date1.YEAR)) + String.valueOf(date1.get(date1.MONTH)) + String.valueOf(date1.get(date1.DATE)) + String.valueOf(date1.get(date1.HOUR)) + String.valueOf(date1.get(date1.MINUTE)) + String.valueOf(date1.get(date1.SECOND));
				// ȸ�� ���̵� ����
				String cominfo = time1 + id;

				// ���ο� ȸ�������� ���� ��Ͻ�Ų��.
				String query2 = "insert into ComInfo " +
							" (id,address,tel,fax,homepage,email,back) " +
							"values('" +cominfo+"','"+uni.convert(s_address)+"','"+
							tel+"','"+fax+"','"+
							homepage+"','"+email+"','0');";	
				stmt.executeUpdate( query2 );	

				message = "fail to add Company in query3";
				// object���� �ð� �Է�
				Calendar date2 = Calendar.getInstance();
				String time2 = String.valueOf(date2.get(date2.YEAR)) + String.valueOf(date2.get(date2.MONTH)) + String.valueOf(date2.get(date2.DATE)) + String.valueOf(date2.get(date2.HOUR)) + String.valueOf(date2.get(date2.MINUTE)) + String.valueOf(date2.get(date2.SECOND));
				// ȸ�� ���̵� ����
				num = id + time2;
				//query statement
				String query3 = "insert into Company " +
								" (id, name, site, region, cominfo, employee) " +
								"values ('" + num + "', '" + uni.convert(c_name) + "', '" + 
								uni.convert(c_site) + "', " + 
								region + ", '" + cominfo + "', '0');";
				stmt.executeUpdate( query3 );
			} // end of if-else  - ȸ�����
				
			// ������ ȸ�� ���� ����Ʈ���� �����.
			message = "fail to get Company for increasing employee number in query4 ";
			// company�� �����Ѵ�.
			String query4 = "select employee, cominfo from Company where id='" + member.getString( "com_id" ) + "';";
			ResultSet com = stmt.executeQuery( query4 );
			com.next();
			int employee = com.getInt( "employee" );

			// ������ 1����̸� ȸ�絵 �����Ѵ�.
			if( employee == 1 ) {
				ResultSet cominfo = null;
				String query5 = null;
				String query6 = null;

				// connection���� statement�� �����ؼ� query�Ѵ�.
				message = "fail to get ComInfo for deleting ComInfo in query5";
				String back = com.getString( "cominfo" );

				while( !back.equals( "0" ) ) {			
					// com�� ���̵�� �ϴ� cominfo�� back�� ��´�. 
					query5 = "select back from ComInfo where id='" + back + "';";
					cominfo = stmt.executeQuery( query5 );
					cominfo.next();

					// cominfo�� �����Ѵ�.	
					query6 = "delete from ComInfo where id='" + back + "';";
					stmt.executeUpdate( query6 ); 
					back = cominfo.getString( "back" ); 
				}	
				
				message = "fail to delete Company in query7";
				// Company�� �����Ѵ�.
				String query7 = "delete from Company where id='" + member.getString( "com_id" ) + "';";
				stmt.executeUpdate( query7 );
			} else { 
				message = "fail to update employee number in query8";
				// company���� �������� ���ͼ� 1 ���ҽ�Ų��.
				employee = employee - 1;

				String query8 = "update Company set employee='" + employee + "' where id='" + member.getString( "com_id" )  + "';"; 
				stmt.executeUpdate( query8 ); 	
			} // end of if-else  - employee update
			
			// ���ο� ���� ������ ����Ѵ�.	
			message = "fail to add JobInfo in query9";
			current = member.getString( "jobinfo" );
	
			// object���� �ð� �Է�
			Calendar date3 = Calendar.getInstance();
			String time3 = String.valueOf(date3.get(date3.YEAR)) + String.valueOf(date3.get(date3.MONTH)) + String.valueOf(date3.get(date3.DATE)) + String.valueOf(date3.get(date3.HOUR)) + String.valueOf(date3.get(date3.MINUTE)) + String.valueOf(date3.get(date3.SECOND));
	
			String new_jobInfo = time3 + id;	

			//  ���ο� jobInfo ���	
			String query9 = "insert into JobInfo (id, posi, part, tel, fax, kind, company, back) values ('" + new_jobInfo + "', '" + uni.convert(position) + "', '" + uni.convert(part) + "', '" + j_tel + "', '" + j_fax + "', '" + member.getString( "kind" ) + "', '" + num + "', '" + current + "');";
			stmt.executeUpdate( query9 );			

			message = "fail to update JobInfo in query10";
			// MemberPerson�� �ֽ� jobInfo update	
			String query10 ="update MemberPerson set jobinfo='" + new_jobInfo + "' where id='" + id + "';";
			stmt.executeUpdate( query10  ); 			


			message = "fail to get employee number for increasing in query11";
			// ���ο� ���忡 ȸ�������� ������Ų��.
			String query11 = "select employee from Company where id='" + num + "';"; 
			company = stmt.executeQuery(query11 );
			if( !company.next() ) 
				out.println( "fail to get employee number from company " );

			// company���� �������� ���ͼ� 1 ������Ų��.
			employee = Integer.parseInt( company.getString( "employee" ) );
			employee++;
			
			message = "fail to update employee number for newcom in query12";
			String query12 = "update Company set employee='" + employee + "' where id='" + num + "';";
			stmt.executeUpdate( query12 );

			message = "fail to get ComInfo for show info in query13"; 
			// ��ϵ� ȸ�������� ��´�.	
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

		if( region.equals( "1" ) )  region = "����Ư����" ;	
		else if( region.equals( "2" ) )  region = "�λ걤����";
		else if( region.equals( "3" ) )  region = "�뱸������";
		else if( region.equals( "4" ) )  region = "��õ������";
		else if( region.equals( "5" ) )  region = "���ֱ�����";
		else if( region.equals( "6" ) )  region = "��걤����";
		else if( region.equals( "7" ) )  region = "����������";
		else if( region.equals( "8" ) )  region = "���";
		else if( region.equals( "9" ) )  region = "����";	
		else if( region.equals( "10" ) ) region = "���" ;
		else if( region.equals( "11" ) ) region = "�泲";
		else if( region.equals( "12" ) ) region = "����";		
		else if( region.equals( "13" ) ) region = "����";
		else if( region.equals( "14" ) ) region = "���";
		else if( region.equals( "15" ) ) region = "�泲";
		else if( region.equals( "16" ) ) region = "����";

		if( kind.equals( "1" ) )  kind = "������";
		else if( kind.equals( "2" ) )  kind = "����";
		else if( kind.equals( "3" ) )  kind = "�Ƿ�";
		else if( kind.equals( "4" ) )  kind = "����";
		else if( kind.equals( "5" ) )  kind = "���/�ڵ���";
		else if( kind.equals( "6" ) )  kind = "��ǻ��/���";
		else if( kind.equals( "7" ) )  kind = "���񽺾�";
		else if( kind.equals( "8" ) )  kind = "����";
		else if( kind.equals( "9" ) )  kind = "����/���׸���";
		else if( kind.equals( "10" ) ) kind = "���";
		else if( kind.equals( "11" ) ) kind = "����";
		else if( kind.equals( "12" ) ) kind = "����";
		else if( kind.equals( "13" ) ) kind = "������";
		else if( kind.equals( "14" ) ) kind = "��������";
		else if( kind.equals( "15" ) ) kind = "��Ÿ" ;

		//������ ȸ�翡 ���� ������ �����ش�.
		try {	
			out.println("<html><head><title></title></head>");
			out.println("<body background=/bg.jpg><form method=post action=/namecard/UserServlet>" );
			out.println( "<input type=hidden name=cmd value=jobChange>" );
			out.println("<table border=0 cellspacing=0 width=600>");
			out.println("<tr><td align=center><br><p><font size=4 color=#ffff00><b>" + uni.convert( "�Է��� ȸ�������� �ٲ�����ϴ�." ) + "</td></tr>" );
			out.println( "<tr><td align=center>");
			out.println("<table border=0 cellspacing=0 width=80%>" );
			out.println( "<tr><td><font size=2 color=#fffacd><b>ID </td><td><font size=2 color=#e0ffff><b>" + id + "</td></tr>" );
			out.println( "<tr><td><font size=2 color=#fffacd><b>" + uni.convert( "�̸�" ) + " </tr><td><font size=2 color=#e0ffff><b>" + member.getString( "name" ) + "</td></tr>" );
			out.println( "<tr><td><font size=2 color=#fffacd><b>" + uni.convert( "ȸ���̸�" ) + "</td><td><font size=2 color=#e0ffff><b>" + uni.convert( c_name ) + "</td></tr> ");
			out.println( "<tr><td><font size=2 color=#fffacd><b>" + uni.convert( "����" ) + " </td><td><font size=2 color=#e0ffff><b>" + uni.convert( region ) + "</td></tr> " );
			if( !c_site.equals( "" ) ){
			 	out.println( "<tr><td><font size=2 color=#fffacd><b>" +   
	        	uni.convert( "����" ) + "</b></td><td><font size=2 color=#e0ffff><b>"
			 	+ newcom.getString( "site" ) + "</td></tr>");
			}
			out.println( "<tr><td><font size=2 color=#fffacd><b>" + uni.convert( "����" ) + "</b></font></td><td><font size=2 color=#e0ffff><b>" + uni.convert( kind ) + "</td></tr>" );
			out.println( "<tr><td><font size=2 color=#fffacd><b>" + uni.convert( "��ȭ" ) + " </td><td><font size=2 color=#e0ffff><b>" + newcom.getString( "tel"  ) + "</td></tr>" );
			out.println( "<tr><td><font size=2 color=#fffacd><b>" + uni.convert( "�ѽ�" ) + " </td><td><font size=2 color=#e0ffff><b>" + newcom.getString( "fax" ) + "</td></tr>" );
			out.println( "<tr><td><font size=2 color=#fffacd><b>" + uni.convert( "�̸���" ) + " </td><td><font size=2 color=#e0ffff><b>" + newcom.getString( "email" ) + "</td></tr>" );
			out.println( "<tr><td><font size=2 color=#fffacd><b>" + uni.convert( "Ȩ������" ) + " </td><td><font size=2 color=#e0ffff><b>" + newcom.getString( "homepage") + "</td></tr>" );
			out.println( "<tr><td><font size=2 color=#fffacd><b>" + uni.convert( "�ּ�" ) + " </td><td><font size=2 color=#e0ffff><b>" + newcom.getString( "address" )  + "</td></tr>" );
			out.println( "<tr><td><font size=2 color=#fffacd><b>" + uni.convert( "�μ�" ) + " </td><td><font size=2 color=#e0ffff><b>" + uni.convert( position )  + "</td></tr>" );
			out.println( "<tr><td><font size=2 color=#fffacd><b>" + uni.convert( "��å" ) + " </td><td><font size=2 color=#e0ffff><b>" + uni.convert( part ) + "</td></tr>" );
			out.println( "<tr><td><font size=2 color=#fffacd><b>" + uni.convert( "�μ���ȭ" ) + " </td><td><font size=2 color=#e0ffff><b>"+ j_tel +"</td></tr>" );
			out.println( "<tr><td><font size=2 color=#fffacd><b>" + uni.convert( "�μ��ѽ�" ) + " </td><td><font size=2 color=#e0ffff><b>"+ j_fax +"</td></tr>" );
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
