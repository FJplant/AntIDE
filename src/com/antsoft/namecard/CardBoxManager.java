package com.antsoft.namecard;

import java.util.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

/**
 *	CardBoxManager - ���丮�� ������ �����ϴ� Ŭ����
 *
 */
class CardBoxManager{

	//variable for NameCardSite instance
	private NameCardSite site;

	//variable for DBManager instance
	private DBManager dbmanager;

	//variable for Asc2Uni instance
	private Uni2Asc uni;

	// variable for Asc2Uni instance
	private Asc2Uni asc;

	static String head = "<html><body background=/bg.jpg>";
	static String end = "</body></html>";
	static String PATH = "/home/httpd/html/cardSystem/logo/";
	static String image = "/cardSystem/logo/";

	/**
	 *	constructor
	 *
	 */
	public CardBoxManager(){
		site = new NameCardSite();
		dbmanager = new DBManager();
		asc = new Asc2Uni();
		uni = new Uni2Asc();
	}

/////////////////////////////// ���� ������  /////////////////////////////
	 
	/**
	 *  showDirList - ����ڿ��� ��ϵǾ��ִ� ���丮 ����Ʈ�� �����ִ� method
	 *
	 *  @param  req      �Էµ� �����Ͱ� ����ִ� ��
	 *  @param  out      ����� ����ϴ� ��
	 *
	 */
	public void showDirList( HttpServletRequest req, PrintWriter out )throws UnsupportedEncodingException {

		// ���� �˻�
		HttpSession httpsession = req.getSession( false );
		if( httpsession == null ) {
			out.println( head );
			out.println( "<h1>Unauthorized User! </h1>" );
			out.println( end );
			return;
		}

		//httpsession���� ���� ����� ���̵� ��´�.
		String id = (String)httpsession.getValue("id");
		
		ResultSet dir_list1 = null;
		ResultSet dir_list2 = null;
		ResultSet dir_list3 = null;

		//����� ���丮 ����Ʈ�� ���´�.
		try{
			dir_list1 = dbmanager.getDirList( id );
			dir_list2 = dbmanager.getDirList( id );
			dir_list3 = dbmanager.getDirList( id );

			// ���� �����ش�.
	    	out.println("<html><head><title></title>");
			out.println("</head>");
			/*out.println("<style type=\"text/css\">");
			out.println("<!-- ");
  			out.println("A:link { text-decoration:none; font-color:white }");
			out.println("A:visited { text-decoration:none; font-color:white } " );
			out.println("-->");
 			out.println("</style>");
			*/	
			out.println("<body background=/cardSystem/bg2.jpg>" );
			out.println("<table border=0 width=800 cellspacing=0><tr>"); 
			out.println("<td><img src=/cardSystem/logo.gif></td>");
			out.println("<td colspan=2>"); 
			out.println("<table border=0 cellspacing=2>");
			out.println("<tr>");
			out.println("<td align=center height=40><a href=/namecard/UserServlet?cmd=showDirList><font color=#FFFFFF size=2>" + uni.convert( "���θ�����" ) + "</font></a></td>");
			out.println("<td align=center><a href=/namecard/UserServlet?cmd=updatePersonForm><font color=#FFFFFF size=2>" + uni.convert( "������������" ) + "</font></a></td>");
			out.println("<td align=center><a href=/namecard/UserServlet?cmd=comInfoForm><font color=#FFFFFF size=2>" + uni.convert( "ȸ����������" ) + "</font></a></td>");
			out.println("<td align=center><a href=/namecard/UserServlet?cmd=jobChangeForm><font size=2 color=#FFFFFF>" + uni.convert( "ȸ������" ) + "</font></a></td>");
			out.println("<td align=center><a href=/namecard/UserServlet?cmd=checkDeleteID><font size=2 color=#FFFFFF>" + uni.convert( "��������" ) + "</font></a></td>");
			out.println("<td align=center><a href=/namecard/UserServlet?cmd=logout><font size=2 color=#FFFFFF>" + uni.convert( "�α׾ƿ�" ) + "</font></a></td>");
			out.println("</tr>");
			out.println("</table>");
			out.println("</td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td height=500 valign=top><br><p>");
			out.println("<table border=0 width=90% align=center cellspacing=0>");
			out.println("<tr>"); 
			out.println("<td colspan=2><br><p><br><p>"); 
			out.println("<b><font color=#FFFF00 size=2>" + uni.convert( "���԰˻�" ) + "</font></b></td>");
			out.println("</tr>");
			out.println("<td colspan=2><img src=/cardSystem/line.gif></td></tr>");
			out.println("<tr><form method=post action=/namecard/UserServlet>");
			out.println("<input type=hidden name=cmd value=search>");
			out.println("<td colspan=2><br><p><font size=2 color=#e6e6fa>" + uni.convert( "���Ի���Ʈ�� ����� �Ǿ��ִ�" ) );
			out.println( "<br>" + uni.convert( " ������� �˻��Ҽ� �ֽ��ϴ�<br>���� �������� ���԰˻��� private��<br>����Ʈ ��ü�� �˻��� public�� ��������" ) + "</font></td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td><font size=2 color=#fffacd><b>" + uni.convert( "�̸�" ) + "</font></td>");
			out.println("<td><input type=text name=name size=10 ></td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td><font color=#fffacd size=2><b>" + uni.convert( "ȸ���̸�" ) + "</font></td>");
			out.println("<td><input type=text name=c_name size=16></td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td><font color=#fffacd size=2><b>" + uni.convert( "����" ) + "</font></td>");
			out.println("<td><input type=text name=site size=10></td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td><font color=#fffacd size=2><b>" + uni.convert( "����" ) + "</font></td>");
			out.println("<td><select name=kind size=1>");
			out.println("<option value=0>" + uni.convert( "���û��׾���" ) + "</option>");
			out.println("<option value=1>" + uni.convert( "������" ) + "</option><option value=2>" + uni.convert( "����" ) + "</option>");
			out.println("<option value=3>" + uni.convert( "�Ƿ�" ) + "</option><option value=4>" + uni.convert( "����" ) + "</option>");
			out.println("<option value=5>" + uni.convert( "���/�ڵ���" ) + "</option><option value=6>" + uni.convert( "��ǻ��/���" ) + "</option>");
			out.println("<option value=7>" + uni.convert( "���񽺾�" ) + "</option><option value=8>" + uni.convert( "����" ) + "</option>");
			out.println("<option value=9>" + uni.convert( "����/���׸���" ) + "</option><option value=10>" + uni.convert( "���" ) + "</option>");
			out.println("<option value=11>" + uni.convert( "����" ) + "</option><option value=12>" + uni.convert( "����" ) + "</option>");
			out.println("<option value=13>" + uni.convert( "������" ) + "</option><option value=14>" + uni.convert( "��������" ) + "</option>");
			out.println("<option value=15>" + uni.convert( "��Ÿ" ) + "</option></select></td>");
			out.println("</tr>");
			out.println("<tr><td><font size=2 color=#fffacd><b>" + uni.convert( "����" ) + "</font></td>");
			out.println("<td><select name=region size=1>");
			out.println("<option value=0>" + uni.convert( "���û��׾���" ) + "</option>");
			out.println("<option value=1>" + uni.convert( "����Ư����" ) + "</option><option value=2>" + uni.convert( "�λ걤����" ) + "</option>");
			out.println("<option value=3>" + uni.convert( "�뱸������" ) + "</option><option value=4>" + uni.convert( "��õ������" ) + "</option>");
			out.println("<option value=5>" + uni.convert( "���ֱ�����" ) + "</option><option value=6>" + uni.convert( "��걤����" ) + "</option>");
			out.println("<option value=7>" + uni.convert( "����������" ) + "</option><option value=8>" + uni.convert( "���" ) + "</option>");
			out.println("<option value=9>" + uni.convert( "����" ) + "</option><option value=10>" + uni.convert( "���" ) + "</option>");
			out.println("<option value=11>" + uni.convert( "�泲" ) + "</option><option value=12>" + uni.convert( "����" ) + "</option>");
			out.println("<option value=13>" + uni.convert( "����" ) + "</option><option value=14>" + uni.convert( "���" ) + "</option>");
			out.println("<option value=15>" + uni.convert( "�泲" ) + "</option><option value=16>" + uni.convert( "����" ) + "</option>");
			out.println("</select></td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td align=right><br><p><input type=radio name=search_kind value=private><font size=2 color=#fffacd>private</td>");
			out.println("<td align=left><br><p><input type=radio name=search_kind checked value=public><font size=2 color=#fffacd>public</td></tr>");
			out.println("<tr><td colspan=2 align=center><input type=submit value=" + uni.convert( "�˻�" ) + "></td>");
			out.println("</form></tr>");
			out.println("</table>");
			out.println("</td>");
			out.println("<td height=500 width=300 valign=top><br><p><br><p><br>");
			out.println("<table border=0 width=90%>");
			out.println("<tr>");
			out.println("<td align=center width=50% bgcolor=#800080><font color=#ffffff size=2><b>" + uni.convert( "���丮 �̸�" ) + "</td>");
			out.println("<td align=center width=50% bgcolor=#800080><font color=#ffffff size=2><b>" + uni.convert( "���Լ�" ) + "</td>");
			out.println("</tr>");
			out.println("<tr><td><br><p></td></tr>");
			
			while( dir_list1.next() ){
				String dir_id = dir_list1.getString( "id" );
				String dir_name =  dir_list1.getString( "name" );
				int num = dir_list1.getInt( "number" ); 
	
				// hidden���� index number�� �Ѱ��ش�.
				out.println( "<tr><td align=center><a href=/namecard/UserServlet?cmd=showCardList&dir_id=" + dir_id + "><font size=2>" +  dir_name + "</font></a></td>" );
				out.println( "<td align=center><font size=2 color=#8b0000>" + num + "</font></td></tr>" );
			}
		} catch( SQLException e ){
			out.println( "</table></center></td>");
			out.println("<td height=500 >&nbsp;</td></tr></table></center>" );
			out.println( "</body></html>");

			out.println( "fail to sql( resultSet ) " + e.toString() );
			return;
		} catch( CommandException e ){
			out.println( "</table></center></td>");
			out.println("<td height=500>&nbsp;</td></tr></table></center>" );
			out.println( "</body></html>");

			out.println( "fail to showDirList " + e.toString() );
			return;
		}

		out.println("</table>");
		out.println("</td>");
		out.println("<td height=500 align=center valign=top>");
		out.println("<table width=90% border=0 cellspacing=0>");
  		out.println("<tr>");
		out.println("<td colspan=2><font size=2 color=#ffff00><br><p><b><br><p><br><p>" + uni.convert( " ���԰����޴�") + "</font></td>");
		out.println("</tr>");
		out.println("<tr>");
		out.println("<td colspan=2><img src=/cardSystem/line.gif></td>");
		out.println("</tr>");
		out.println("<tr>");
		out.println("<td colspan=2><form method=post action=/namecard/UserServlet><input type=hidden name=cmd value=addDir>");
		out.println("<br><p><font size=2 color=#fffacd><b>" + uni.convert( "���丮 �߰�" ) + "</td></tr>");
		out.println("<tr><td><input type=text name=dirName size=10></td>");
		out.println("<td><input type=submit value=add></td>");
		out.println("</form></tr>");
		out.println("<tr>");
		out.println("<td colspan=2><form method=post action=/namecard/UserServlet><input type=hidden name=cmd value=deleteDir>");
		out.println("<br><p><font size=2 color=#fffacd><b>" + uni.convert( "���丮 ����" ) + "</font></td>"); 
		out.println("</tr>");
		out.println("<tr>");
		out.println("<td>");
		out.println("<select size=1 name=dir_id>");
		
		try{
			while( dir_list2.next() ){
				String dir_id = dir_list2.getString( "id" );
				String dir_name =  dir_list2.getString( "name" ) ;
	
				out.println( "<option value=" + dir_id + ">" + dir_name + "</option>" );
			}
		
		}catch( Exception e ){
			out.println( "fail to sql at CardBoxManager " + e.toString() );
			return;
		}

		out.println("</select></td>");
		out.println("<td>");
		out.println("<input type=submit value=delete></td>");
		out.println("</form></tr>");

		out.println("<tr>");
		out.println("<td colspan=2><form method=post action=/namecard/UserServlet><input type=hidden name=cmd value=renameDir>");
		out.println("<br><p><font size=2 color=#fffacd><b>" + uni.convert( "�̸��ٲٱ�" ) + "</font></td>"); 
		out.println("</tr>");
		out.println("<tr>");
		out.println("<td><font size=2 color=#fff0f5><b>from:</font></td>");
		out.println("<td><select size=1 name=dir_id>");
		
		try{
			while( dir_list3.next() ){
				String dir_id = dir_list3.getString( "id" );
				String dir_name = dir_list3.getString( "name" );

				out.println( "<option value=" + dir_id + ">" + dir_name + "</option>" );
			}
		}catch( Exception e ){
			out.println ( "fail to showDirList at CardBoxManager " + e.toString() );
			return;
		}
		
		out.println("</select></td>");
		out.println("</tr>");
		out.println("<tr>");
		out.println("<td><font size=2 color=#fff0f5><b>to:</font></td>");
		out.println("<td>");
		out.println("<input type=text name=dirName size=10></td>");
		out.println("</tr>");
		
		out.println("<tr>");
		out.println("<td colspan=2 algin=center>");
		out.println("<input type=submit value=rename></form></td>");
		out.println("</tr>");
		out.println("</table>");
		out.println("</td></tr>");
		out.println("</table>");
		out.println("</body>");
		out.println("</html>");	
		
	} 	

	/**
	 *  showCardList - ���õ� ���丮 ���� ���Ե��� ����Ʈ�� �����ִ� method
	 *
	 *  @param  req         �Էµ� �����Ͱ� ����ִ� ��
	 *  @param  out         ����� ����� ��
	 * 
	 */
	public void showCardList( HttpServletRequest req, PrintWriter out )throws UnsupportedEncodingException{
		// session�� ���´�.
		HttpSession httpsession = req.getSession( false );
		
		// session�� ������ ����� ����� ���� �ʾҴٴ� ���̹Ƿ� error ó���Ѵ�.
		if( httpsession == null) {
			out.println( head );
			out.println( "<h1>Error !!! </h1>" );
			out.println( end );
			return;
		}
		
		String id = (String)httpsession.getValue( "id" );
		String dir_id = req.getParameter("dir_id");
	
		// session�� ���� ���丮�� �־�д�.
		httpsession.putValue( "current_dir",  dir_id );
		
		ResultSet card_list = null;
		ResultSet dir = null;
		String dir_name = null;

		try{
			//card list�� ��´�
			card_list = dbmanager.getCardList( dir_id );
			//directory name�� ��´�.
			dir = dbmanager.getDir( dir_id );
			dir.next();
			dir_name = dir.getString( "name" );
		}catch( Exception e ){
			out.println( head );
			out.println( "fail to get card_list at CardBoxManager"+ e.toString() );
			out.println( end );
			return;
		}

		// directory�� ��ϵǾ��ִ� namecard list�� �����ش�.
		out.println("<html><head><title></title>");
		out.println("<meta http-equiv=Content-Type content=text/html; charset=euc-kr>");
		//script ����
		out.println("<script language=JavaScript>");
		out.println("function open_window(url){");
		out.println("card = window.open(url,\"card\",'toolbar=0,location=0,directories=0,status=0,menubar=0,scrollbars=0,resizable=0, width=500, height=500');");
		out.println("card.opener.name = \"opener\";");
		out.println("}");
		out.println("</script>");
 		//script end
		/*out.println("<style type=\"text/css\">");
		out.println("<!-- ");
  		out.println("A:link { text-decoration:none; font-color:white }");
		out.println("A:visited { text-decoration:none; font-color:white } " );
		out.println("-->");
 		out.println("</style>");*/
		out.println("</head>");
		out.println("<body background=/cardSystem/bg2.jpg>" );
		out.println("<table border=0 width=800 cellspacing=0><tr>"); 
		out.println("<td><img src=/cardSystem/logo.gif></td>");
		out.println("<td colspan=2>"); 
		out.println("<table border=0 cellspacing=2>");
		out.println("<tr>");
		out.println("<td align=center height=40><a href=/namecard/UserServlet?cmd=showDirList><font color=#FFFFFF size=2>" + uni.convert( "���θ�����" ) + "</font></a></td>");
		out.println("<td align=center><a href=/namecard/UserServlet?cmd=updatePersonForm><font color=#FFFFFF size=2>" + uni.convert( "������������" ) + "</font></a></td>");
		out.println("<td align=center><a href=/namecard/UserServlet?cmd=comInfoForm><font color=#FFFFFF size=2>" + uni.convert( "ȸ����������" ) + "</font></a></td>");
		out.println("<td align=center><a href=/namecard/UserServlet?cmd=jobChangeForm><font size=2 color=#FFFFFF>" + uni.convert( "ȸ������" ) + "</font></a></td>");
		out.println("<td align=center><a href=/namecard/UserServlet?cmd=checkDeleteID><font size=2 color=#FFFFFF>" + uni.convert( "��������" ) + "</font></a></td>");
		out.println("<td align=center><a href=/namecard/UserServlet?cmd=logout><font size=2 color=#FFFFFF>" + uni.convert( "�α׾ƿ�" ) + "</font></a></td>");
		out.println("</tr>");
		out.println("</table>");
		out.println("</td>");
		out.println("</tr>");
		out.println("<tr>");
		out.println("<td height=500 valign=top><br><p>");
		out.println("<table border=0 width=90% align=center cellspacing=0>");
		out.println("<tr>"); 
		out.println("<td colspan=2><br><p><br><p>"); 
		out.println("<b><font color=#FFFF00 size=2>" + uni.convert( "���԰˻�" ) + "</font></b></td>");
		out.println("</tr>");
		out.println("<td colspan=2><img src=/cardSystem/line.gif></td></tr>");
		out.println("<tr><form method=post action=/namecard/UserServlet>");
		out.println("<input type=hidden name=cmd value=search>");
		out.println("<td colspan=2><br><p><font size=2 color=#e6e6fa>" + uni.convert( "���Ի���Ʈ�� ����� �Ǿ��ִ�" ) );
		out.println( "<br>" + uni.convert( " ������� �˻��Ҽ� �ֽ��ϴ�<br>���� �������� ���԰˻��� private��<br>����Ʈ ��ü�� �˻��� public�� ��������" ) + "</font></td>");
		out.println("</tr>");
		out.println("<tr>");
		out.println("<td><font size=2 color=#fffacd><b>" + uni.convert( "�̸�" ) + "</font></td>");
		out.println("<td><input type=text name=name size=10 ></td>");
		out.println("</tr>");
		out.println("<tr>");
		out.println("<td><font color=#fffacd size=2><b>" + uni.convert( "ȸ���̸�" ) + "</font></td>");
		out.println("<td><input type=text name=c_name size=16></td>");
		out.println("</tr>");
		out.println("<tr>");
		out.println("<td><font color=#fffacd size=2><b>" + uni.convert( "����" ) + "</font></td>");
		out.println("<td><input type=text name=site size=10></td>");
		out.println("</tr>");
		out.println("<tr>");
		out.println("<td><font color=#fffacd size=2><b>" + uni.convert( "����" ) + "</font></td>");
		out.println("<td><select name=kind size=1>");
		out.println("<option value=0>" + uni.convert( "���û��׾���" ) + "</option>");
		out.println("<option value=1>" + uni.convert( "������" ) + "</option><option value=2>" + uni.convert( "����" ) + "</option>");
		out.println("<option value=3>" + uni.convert( "�Ƿ�" ) + "</option><option value=4>" + uni.convert( "����" ) + "</option>");
		out.println("<option value=5>" + uni.convert( "���/�ڵ���" ) + "</option><option value=6>" + uni.convert( "��ǻ��/���" ) + "</option>");
		out.println("<option value=7>" + uni.convert( "���񽺾�" ) + "</option><option value=8>" + uni.convert( "����" ) + "</option>");
		out.println("<option value=9>" + uni.convert( "����/���׸���" ) + "</option><option value=10>" + uni.convert( "���" ) + "</option>");
		out.println("<option value=11>" + uni.convert( "����" ) + "</option><option value=12>" + uni.convert( "����" ) + "</option>");
		out.println("<option value=13>" + uni.convert( "������" ) + "</option><option value=14>" + uni.convert( "��������" ) + "</option>");
		out.println("<option value=15>" + uni.convert( "��Ÿ" ) + "</option></select></td>");
		out.println("</tr>");
		out.println("<tr><td><font size=2 color=#fffacd><b>" + uni.convert( "����" ) + "</font></td>");
		out.println("<td><select name=region size=1>");
		out.println("<option value=0>" + uni.convert( "���û��׾���" ) + "</option>");
		out.println("<option value=1>" + uni.convert( "����Ư����" ) + "</option><option value=2>" + uni.convert( "�λ걤����" ) + "</option>");
		out.println("<option value=3>" + uni.convert( "�뱸������" ) + "</option><option value=4>" + uni.convert( "��õ������" ) + "</option>");
		out.println("<option value=5>" + uni.convert( "���ֱ�����" ) + "</option><option value=6>" + uni.convert( "��걤����" ) + "</option>");
		out.println("<option value=7>" + uni.convert( "����������" ) + "</option><option value=8>" + uni.convert( "���" ) + "</option>");
		out.println("<option value=9>" + uni.convert( "����" ) + "</option><option value=10>" + uni.convert( "���" ) + "</option>");
		out.println("<option value=11>" + uni.convert( "�泲" ) + "</option><option value=12>" + uni.convert( "����" ) + "</option>");
		out.println("<option value=13>" + uni.convert( "����" ) + "</option><option value=14>" + uni.convert( "���" ) + "</option>");
		out.println("<option value=15>" + uni.convert( "�泲" ) + "</option><option value=16>" + uni.convert( "����" ) + "</option>");
		out.println("</select></td>");
		out.println("</tr>");
		out.println("<tr>");
		out.println("<td align=right><br><p><input type=radio name=search_kind value=private><font size=2 color=#fffacd>private</td>");
		out.println("<td align=left><br><p><input type=radio name=search_kind checked value=public><font size=2 color=#fffacd>public</td></tr>");
		out.println("<tr><td colspan=2 align=center><input type=submit value=" + uni.convert( "�˻�" ) + "></td>");
		out.println("</form></tr>");
		out.println("</table>");
		out.println("</td>");
		out.println("<td height=500 width=300 valign=top align=center><br><p>");
		out.println("<b><font color=#000088>"+ dir_name + " </font><font size=2>" + uni.convert( "���丮" ) + "</font><br><p>");
		out.println("<table border=0 width=90%>");
		out.println("<tr>");
		out.println("<td align=center bgcolor=#800080><font color=#ffffff size=2><b>" + uni.convert( "�̸�" ) + "</td>");
		out.println("<td align=center bgcolor=#800080><font color=#ffffff size=2><b>" + uni.convert( "ȸ���̸�" ) + "</td>");
		out.println("<td align=center bgcolor=#800080><font color=#ffffff size=2><b>" + uni.convert( "��å" ) + "</td>");
		out.println("</tr>");
		out.println("<tr><td><br><p></td></tr>");

		ResultSet result = null;
		int num = 0;
      	try{
      		//out.println( "cardlist startin..." );
			while( card_list.next() ){
				String card_id = card_list.getString( "card_id" );
				String person_kind = card_list.getString( "person_kind" );

				result = dbmanager.getCardListContent( card_id, person_kind );
				if(result.next() ){
					num++;
					//�̸��� ��´�.
					String name = result.getString( 1 );
					//ȸ���� �̸��� ���´�.
					String c_name = result.getString( 3 );
					//������ ��´�.
					String position = result.getString( 2 );
					if( position== null ) position="����";	

					out.println("<tr><td height=20 align=center><font size=2 color=#000088><a href=\"javascript:open_window('/namecard/UserServlet?cmd=showNameCard&person_kind=" + person_kind + "&card_id=" + card_id +  "')\">" + name + "</a></font></td>");
					out.println("<td height=20 align=center><font size=2 color=#000088>" +  c_name + "</font></td>");
					out.println("<td height=20 align=center><font size=2 color=#000088>" +  position +"</font></td></tr>");
				}	
			}
		
	  	}catch( Exception e ){
			out.println( "fail to showCardList at CardBoxManager " + e.toString() );
			return;
	  	}
		out.println("<tr><td colspan=3 align=center><br><p><font size=2><font color=red>" + num + "</font>" + uni.convert( "���� ������ ��ϵǾ��ֽ��ϴ�." ) + "</font></td></tr>" );
		out.println("<tr><td colspan=3 align=center><br><p><a href=/namecard/UserServlet?cmd=showDirList><font size=2><b><-Back</a></td></tr>");
		out.println("</table>");
		out.println("</td>");
		
		out.println("<td height=500 align=center valign=top>");
		out.println("<table width=90% border=0 cellspacing=0>");
  		out.println("<tr>");
		out.println("<td><font size=2 color=#ffff00><br><p><b><br><p><br><p>" + uni.convert( "�����Ե��" ) + "</font></td>");
		out.println("</tr>");
		out.println("<tr>");
		out.println("<td><img src=/cardSystem/line.gif></td>");
		out.println("</tr>");
		out.println("<tr>");
		out.println("<td><form method=post action=/namecard/UserServlet><input type=hidden name=cmd value=addCardForm>");
		out.println("<input type=hidden name=dir_id value=\""+dir_id+"\"></td></tr>");
		out.println("<td><br><p><font size=2 color=#fffacd>" + uni.convert( "�� ������ ����Ͻ÷��� ���⸦ �����ּ���" ) + "</td></tr>");
		out.println("<tr><td align=center><input type=submit value=\"" + uni.convert( "���" ) + "\"></form></td></tr>"); 
		out.println("<tr><td><br><p><font size=2 color=#ffff00><b>" + uni.convert( "���Ը��" ) + "</td></tr>");
		out.println("<tr><td><img src=/cardSystem/line.gif></td></tr>");
		out.println("<tr><td><br><p><font size=2 color=#fffacd>"); 
		out.println( uni.convert( "���������� ���� ����� �ֽ��ϴ�.<br>���� ����� �ٲٽ÷���<br> ���⸦ ��������." ) + "</td></tr>");
		out.println("<tr><td align=center><form method=post action=/namecard/UserServlet>");
		out.println("<input type=hidden name=cmd value=selectCardColorForm>");
		out.println("<input type=submit value=\"" + uni.convert( "���Ը��ٲٱ�" ) + "\">");
		out.println("</form></td></tr>");
		out.println("</table>");
		out.println("</td></tr>");
		out.println("</table>");
		out.println("</body>");
		out.println("</html>");	
		

	}

	/**
	 *  showNameCard  - ����Ʈ���� ���õ� ������ �������� �����ִ� method
	 *
	 *  @param  req         �Էµ� �����Ͱ� ����ִ� ��
	 *  @param  out         ����� ����� ��
	 *
	 */
	public void showNameCard( HttpServletRequest req, PrintWriter out )throws UnsupportedEncodingException, CommandException {
		// session�� ���´�.
		HttpSession httpsession = req.getSession( false );
		
		// session�� ������ ����� ����� ���� �ʾҴٴ� ���̹Ƿ� error ó���Ѵ�.
		if( httpsession == null) {
			out.println( head );
			out.println( "<h1>Error !!! </h1>" );
			out.println( end );
			return;
		}
		
		//  session���� ����� id�� ���´�.
		String id = (String)httpsession.getValue( "id" );
		//out.println( "id-" + id );
		
		// session ���� ���� ���丮�� id�� �� ���丮�� ��´�.
		String dir_id = (String)httpsession.getValue( "current_dir" ) ;
		//out.println( "current dir-" + dir_id );
		
		// ���õ� ������id�� ���´�.
		String card_id = req.getParameter( "card_id" );
		String person_kind = req.getParameter( "person_kind" );
		ResultSet dir = null;
		ResultSet person = null;

		String desc = "";
		String comInfo_id = "";
		String c_address = "";
		String name = "";
		String tel = "";
		String handphone = "";
		String beeper = "";
		String email = "";
		String homepage = "";
		String com_id = "";
		String jobInfo_id = "";
		String c_name = "";
		String c_site = "";
		String position = "";
		String part = "";
		String j_tel = "";
		String j_fax = "";
		String bgcolor = "ffffff";
		String font = "000000";
		String logo = "none";

		try{
			//cardContent�� ���´�.( query ��� )
			person = dbmanager.getCardContent(  card_id , person_kind );
			person.next();	
			//�̸��� ��´�.
			name =  asc.convert( person.getString( "name" ) ) ;
			//����ȭ��ȣ�� ��´�.
			tel = person.getString( 13 );
			//�ڵ��� ��ȣ�� ��´�.
			handphone = person.getString( "handphone" );
			//�߻� ��ȣ�� ��´�.
			beeper = person.getString( "beeper" );
			//�̸����� ��´�
			email = person.getString( "email" );
			//Ȩ�������� ��´�
			homepage = person.getString( "homepage" );
			//ȸ���̸��� ��´�.
			c_name = person.getString( 10 );
			//ȸ�� ������ ��´�
			c_site = person.getString( "site" );
			c_address = person.getString( "address" );
			//������ ��´�
			position = person.getString( "posi" );
			//�μ��� ��´�
			part = person.getString( "part" );
			//ȸ�� ���� ��ȭ��ȣ�� ��´�
			j_tel = person.getString( "tel" );
			//ȸ�� �μ� �ѽ��� ��´�
			j_fax = person.getString( "fax" );
			// ���Ի����� ��´�.
			if( person_kind.equals( "m" ) ){
				//descripton�� ��´�
				desc = person.getString( "description" );
				bgcolor = person.getString( "mybg" );
				font = person.getString( "myfont" );
				logo = person.getString( "mylogo" );
			} else {
				ResultSet color = dbmanager.getCardColor( id );
				color.next();
				logo = color.getString( "cardlogo" );
				bgcolor = color.getString( "cardbg" );
				font = color.getString( "cardfont" );
			}				
			//out.println( "bgcolor : " + bgcolor + " in shownameCard" );
		}catch( Exception e ){
			out.println( head );
			out.println( "fail to showNameCard at CardBoxManager " + e.toString() );
			out.println( end );
			return;
		}

		out.println("<html><head><title></title></head>");
 		out.println("<body bgcolor=#ffffff>");
   		out.println("<center><table border=2 width=450>");
   		out.println("<tr>");
		out.println("<td bgcolor=#" + bgcolor + " bordercolor=#ffffff align=center>");
		out.println("<table border=0 width=90% cellspacing=0>");
		out.println("<tr>");
		out.println("<td align=center colspan=4><br><p>"); 
		out.println("<font size=4 color=#" + font + "><b>"+ c_name );
		if( !c_site.equals( "" ) ) out.println(  " ( " + c_site + " )" );
		out.println( "</b></font><br><p></td>");
		out.println("</tr>");

		if( logo.equals("none") ){
			out.println("<tr>");
			out.println("<td colspan=2 width=50%><font size=3 color=#" + font + "><b>"+ uni.convert(name) +"</b></font></td>");
			out.println("<td><b><font size=2 color=#" + font + ">H.P.</td><td><font size=2 color=#" + font + ">" + handphone +"</td></tr>");
			out.println("<tr><td colspan=2><font size=2 color=#" + font + ">" + position /*��å*/ + "</td>");
			out.println("<td><b><font size=2 color=#" + font + ">Beeper</td><td><font size=2 color=#" + font + ">"+ beeper/*�߻�*/  +"</td></tr>");
			out.println("<tr>");
			out.println("<td colspan=2><font size=2 color=#" + font + ">"+ c_address/* ȸ���ּ� */+"</td>");
			out.println("<td><font size=2 color=#" + font + "><b>e-mail</td><td><font size=2 color=#" + font + ">"+ email +"</td></tr>");
			out.println("<tr>");
			out.println("<td><font size=2 color=#" + font + "><b>Com Tel</td><td><font size=2 color=#" + font + ">"+ j_tel/*ȸ����ȭ*/ +"</td>");
			out.println("<td><font size=2 color=#" + font + "><b>Homepage</td><td><font size=2 color=#" + font + ">"+ homepage/*Ȩ������*/+" </td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td><font size=2 color=#" + font + "><b>Fax</td><td><font size=2 color=#" + font + ">"+ j_fax/*�ѽ�*/ +"</td>");
			out.println("<td><font size=2 color=#" + font + "><b>Home Tel</td><td><font size=2 color=#" + font + ">"+ tel/*����ȭ*/+"</td>");
			out.println("</tr>");
			out.println("</table><p>");

		}else{
			out.println("<tr>");
			out.println( "<td rowspan=10 colspan=2 align=center valign=middle><img src=" + image+logo + "></td>" );
			out.println("<td colspan=2><font size=3 color=#" + font + "><b>"+ uni.convert(name) +"</b></font></td></tr>");
			out.println("<tr><td colspan=2><font size=2 color=#" + font + ">" + position + "</td></tr>");
			out.println("<tr><td colspan=2><font size=2 color=#" + font + ">"+ c_address+"</td></tr>");
			out.println("<tr><td><font size=2 color=#" + font + "><b>Com Tel</td><td><font size=2 color=#" + font + ">"+ j_tel +"</td></tr>");
			out.println("<tr><td><font size=2 color=#" + font + "><b>Fax</td><td><font size=2 color=#" + font + ">"+ j_fax +"</td></tr>");
			out.println("<tr><td><b><font size=2 color=#" + font + ">H.P.</td><td>" + handphone +"</td></tr>");
			out.println("<tr><td><b><font size=2 color=#" + font + ">Beeper</td><td><font size=2 color=#" + font + ">"+ beeper  +"</td></tr>");
			out.println("<tr><td><font size=2 color=#" + font + "><b>e-mail</td><td><font size=2 color=#" + font + ">"+ email +"</td></tr>");
			out.println("<tr><td><font size=2 color=#" + font + "><b>Homepage</td><td><font size=2 color=#" + font + ">"+ homepage+" </td></tr>");
			out.println("<tr><td><font size=2 color=#" + font + "><b>Home Tel</td><td><font size=2 color=#" + font + ">"+ tel+"</td></tr>");
			out.println("</table><p>");
		}

		if(person_kind.equals("m")){
			out.println("</td></tr>");
			out.println("<tr>");
			out.println("<td bgcolor=#" + bgcolor + " bordercolor=#ffffff align=center>");
			out.println("<table width=100%><font size=2>");
			out.println("<tr>");
			out.println("<td bordercolor=#ffffff><font size=2 color=#" + font + "><b>Description</b><br>"+desc+"</font></td>");
			out.println("</tr>");
			out.println("</font></table>");
		}
		out.println("</td></tr>");
		out.println("</table>");
		
		// ����, �̵���ư�� �ִ´�.
		out.println("<br><table border=0 width=80% cellspacing=0>");
		out.println("<tr><td width=50% align=center>");
		out.println( "<form method=post action=/namecard/UserServlet target=opener>" );
		out.println( "<input type=hidden name=cmd value=deleteCard>" );
		out.println( "<input type=hidden name=card_id value=\"" + card_id + "\">" );
		out.println( "<input type=hidden name=dir_id value=\"" + dir_id + "\">" );
		out.println( "<input type=hidden name=delete_kind value=myDir>" );
		out.println( "<input type=submit value=" + uni.convert( "����" ) + " onClick=window.close();></form></td>" );
		out.println("<td>");
		out.println( "<form method=post action=/namecard/UserServlet target=opener>" );
		out.println( "<input type=hidden name=cmd value=moveCard>" );
		out.println( "<input type=hidden name=card_id value=\"" + card_id + "\">" );
		out.println( "<select name=dir_id size=1 >" );

		try{
			//dir list ������
			ResultSet dir_list = dbmanager.getDirList( id );
			while( dir_list.next() ){
				String d_id = dir_list.getString( "id" );
				String d_name = dir_list.getString( "name" );

				out.println( "<option value=" + d_id + ">" + d_name + "</option>" );
			}
		}catch( Exception e ){
			out.println( head );
			out.println( "fail to get dir_list " + e.toString() );
			out.println( end );
			return;
		}

		out.println( "</select><font size=2>" + uni.convert(" �� " ) + "<input type=submit value=" + uni.convert( "�̵�" ) + "></form></td></tr>" );
		out.println("<tr><td colspan=2 align=center>");
			out.println("<form method=post action=/namecard/UserServlet target=opener>");
			out.println("<input type=hidden name=cmd value=updateCardForm>");
			out.println("<input type=hidden name=card_id value=\"" + card_id + "\">");
			out.println("<input type=hidden name=dir_id value=\"" + dir_id + "\">");
		if( person_kind.equals( "n" ) ){
			out.println("<input type=submit value=\"" + uni.convert( "����" ) + "\" onClick=window.close();>" );
		}
		out.println("<input type=button value=" + uni.convert( "�ݱ�" ) + " onClick=window.close();></td></tr></table>");
		out.println("</form>");
		out.println("</center></body>");
		out.println("</html>");

		
	}

	/**
	 * showSearchCard - �˻��� ī�� �����ִ� �Լ�
	 *  
	 *  @param  req ������ �Է¹��� ����Ÿ�� �ִ� ��
	 *  @param  out ����� ����� ��
	 *
	 **/
	public void showSearchCard( HttpServletRequest req, PrintWriter out ) throws CommandException, UnsupportedEncodingException{
		// session�� ���´�.
		HttpSession httpsession = req.getSession( false );
		
		// session�� ������ ����� ����� ���� �ʾҴٴ� ���̹Ƿ� error ó���Ѵ�.
		if( httpsession == null) {
			out.println( head );
			out.println( "<h1>Error !!! </h1>" );
			out.println( end );
			return;
		}

		//req���� String �Ķ���� �н��Ѵ�.
		String card_id = uni.convert( req.getParameter("card_id") );
		String person_kind = req.getParameter( "person_kind" );
		String search_kind = req.getParameter("search_kind");

		String dir_id = null;
		String id = null;

		if( !search_kind.equals( "out" ) ) {
			//�� ī�尡 �����ִ� ���丮 ���̵�
			dir_id = req.getParameter("dir_id");

			//session���� ���� ����� id���		
			id = ( String )httpsession.getValue( "id" );
		}
		ResultSet dir = null;

		String desc = "" ; 
		String comInfo_id = "";
		String c_address = "";
		String name = "";
		String tel = "";
		String handphone = "";
		String beeper = "";
		String email = "";
		String homepage = "";
		String com_id = "";
		String jobInfo_id = "";
		String c_name = "";
		String c_site = "";
		String position = "";
		String part = "";
		String j_tel = "";
		String j_fax = "";
		String bgcolor = "ffffff";
		String font = "000000";
		String logo = "none";

		try{
			//card content�� ��´� 
			ResultSet person = dbmanager.getCardContent( card_id, person_kind );
			if(!person.next()) out.println( " person is null" );

			// ���� ����� �̹����� ��´�.
			if( person_kind.equals( "m" )) {
				//description�� ��´�.
				desc = person.getString( "description" );
				bgcolor = person.getString( "mybg" );
				font = person.getString( "myfont" );
				logo = person.getString( "mylogo" );
			} else {
				ResultSet color = dbmanager.getCardColor( id );
				color.next();
				bgcolor = color.getString( "cardbg" );
				font = color.getString( "cardfont" );
				logo = color.getString( "cardlogo" );
			}	
			//out.println( "bgcolor : " + bgcolor + " in showSearchCard" );
			//ȸ�� �ּҸ� ��´�.
			c_address = person.getString( "address" );
			//�̸��� ��´�.
			name = person.getString( 1 );
			//����ȭ��ȣ�� ��´�.
			tel = person.getString( 13 );
			//�ڵ��� ��ȣ�� ��´�.
			handphone = person.getString( "handphone" );
			//�߻� ��ȣ�� ��´�.
			beeper = person.getString( "beeper" );
			//�̸����� ��´�
			email = person.getString( "email" );
			//Ȩ�������� ��´�
			homepage = person.getString( "homepage" );
			//ȸ���̸��� ��´�.
			c_name = person.getString( 10 );
			//ȸ�� ������ ��´�
			c_site = person.getString( "site" );
			//������ ��´�
			position = person.getString( "posi" );
			//�μ��� ��´�
			part = person.getString( "part" );
			//ȸ�� ���� ��ȭ��ȣ�� ��´�
			j_tel = person.getString( "tel" );
			//ȸ�� �μ� �ѽ��� ��´�
			j_fax = person.getString( "fax" );
		}catch( Exception e ){
			out.println( head );
			out.println( "fail to showSearchCard at DBManager "  + e.toString() );
			out.println( end );
			return;
		}	
	
		//�ش� ���� �����ֱ�
		out.println("<html><head><title></title></head>");
 		out.println("<body bgcolor=#ffffff>");
   		out.println("<center><table border=2 width=450>");
   		out.println("<tr>");
		out.println("<td bgcolor=#" + bgcolor + " bordercolor=#ffffff align=center>");
 		// ����̹����� �����ش�. 
		out.println("<table border=0 width=90% cellspacing=0>");
		out.println("<tr>");
   		out.println("<td align=center colspan=4><br><p>"); 
		out.println("<font size=4 color=#" + font + "><b>"+ c_name );
		if( !c_site.equals( "" ) ) out.println(  " ( " + c_site + " )" );
		out.println( "</b></font><br><p></td>");
		out.println("</tr>");
		
		if( logo.equals("none") ){
			out.println("<tr>");
			out.println("<td colspan=2 width=50%><font size=3 color=#" + font + "><b>"+ name +"</b></font></td>");
			out.println("<td><b><font size=2 color=#" + font + ">H.P.</td><td><font size=2 color=#" + font + ">" + handphone +"</td></tr>");
			out.println("<tr><td colspan=2><font size=2 color=#" + font + ">" + position /*��å*/ + "</td>");
			out.println("<td><b><font size=2 color=#" + font + ">Beeper</td><td><font size=2 color=#" + font + ">"+ beeper/*�߻�*/  +"</td></tr>");
			out.println("<tr>");
			out.println("<td colspan=2><font size=2 color=#" + font + ">"+ c_address/* ȸ���ּ� */+"</td>");
			out.println("<td><font size=2 color=#" + font + "><b>e-mail</td><td><font size=2 color=#" + font + ">"+ email +"</td></tr>");
			out.println("<tr>");
			out.println("<td><font size=2 color=#" + font + "><b>Com Tel</td><td><font size=2 color=#" + font + ">"+ j_tel/*ȸ����ȭ*/ +"</td>");
			out.println("<td><font size=2 color=#" + font + "><b>Homepage</td><td><font size=2 color=#" + font + ">"+ homepage/*Ȩ������*/+" </td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td><font size=2 color=#" + font + "><b>Fax</td><td><font size=2 color=#" + font + ">"+ j_fax/*�ѽ�*/ +"</td>");
			out.println("<td><font size=2 color=#" + font + "><b>Home Tel</td><td><font size=2 color=#" + font + ">"+ tel/*����ȭ*/+"</td>");
			out.println("</tr>");
			out.println("</table><p>");

		}else{
			out.println("<tr>");
			out.println( "<td rowspan=10 colspan=2 align=center valign=middle><img src=" + image+logo + "></td>" );
			out.println("<td colspan=2><font size=3 color=#" + font + "><b>"+ name +"</b></font></td></tr>");
			out.println("<tr><td colspan=2><font size=2 color=#" + font + ">" + position /*��å*/ + "</td></tr>");
			out.println("<tr><td colspan=2><font size=2 color=#" + font + ">"+ c_address/* ȸ���ּ� */+"</td></tr>");
			out.println("<tr><td><font size=2 color=#" + font + "><b>Com Tel</td><td><font size=2 color=#" + font + ">"+ j_tel/*ȸ����ȭ*/ +"</td></tr>");
			out.println("<tr><td><font size=2 color=#" + font + "><b>Fax</td><td><font size=2 color=#" + font + ">"+ j_fax/*�ѽ�*/ +"</td></tr>");
			out.println("<tr><td><b><font size=2 color=#" + font + ">H.P.</td><td><font size=2 color=#" + font + ">" + handphone +"</td></tr>");
			out.println("<tr><td><b><font size=2 color=#" + font + ">Beeper</td><td><font size=2 color=#" + font + ">"+ beeper/*�߻�*/  +"</td></tr>");
			out.println("<tr><td><font size=2 color=#" + font + "><b>e-mail</td><td><font size=2 color=#" + font + ">"+ email +"</td></tr>");
			out.println("<tr><td><font size=2 color=#" + font + "><b>Homepage</td><td><font size=2 color=#" + font + ">"+ homepage/*Ȩ������*/+" </td></tr>");
			out.println("<tr><td><font size=2 color=#" + font + "><b>Home Tel</td><td><font size=2 color=#" + font + ">"+ tel/*����ȭ*/+"</td></tr>");
			out.println("</table><p>");
		}

		if(person_kind.equals("m")){
			out.println("</td></tr>");
			out.println("<tr>");
			out.println("<td bgcolor=#" + bgcolor + " bordercolor=#ffffff align=center>");
			out.println("<table width=100%><font size=2>");
			out.println("<tr>");
			out.println("<td bordercolor=#ffffff><font size=2 color=#" + font + "><b>Description</b><br>"+desc+"</font></td>");
			out.println("</tr>");
			out.println("</font></table>");
		}
		out.println("</td></tr>");
		out.println("</table>");
		
		out.println("<br><p>");
		if(search_kind.equals("public")){
			
			out.println("<img src=/icon.gif><font size=2><b>" + uni.convert( "���丮�� ���" ) + "</font></b>");
			
			out.println("<table width=80% border=0 cellspacing=0>");
			out.println("<tr><td align=center><form method=post action=/namecard/UserServlet target=opener>");
			out.println("<input type=hidden name=cmd value=addPublicCard>");
			out.println("<font size=2><b>" + uni.convert( "���丮 �̸�" ) + ":</b></font>");
			out.println("<select size=1 name=dir_id>");
		
			//���丮 ����Ʈ�� DB���� ���´�.
			ResultSet d_list = dbmanager.getDirList( id );
			String d_id = "";
			String d_name = "";

			try{
				while( d_list.next() ){
					//���丮 ���̵� ���´�
					d_id = d_list.getString( "id" );
					//���丮 �̸��� ���´�
					d_name = d_list.getString( "name" );

					out.println( "<option value=" + d_id + ">" + d_name + "</option>" );
				}
			}catch( Exception e ){
				out.println( head );
				out.println( "fail to get dir list at showSearchCard " + e.toString() );
				out.println( end );
				return;
			}
	
			out.println("</select>");
			out.println("<input type=hidden name=dir_id value=\""+d_id+"\">");
			out.println("<input type=hidden name=card_id value=\""+card_id+"\">");
			out.println("<input type=submit value=" + uni.convert("���") + " onClick=\"window.close()\"></form></td></tr>");
		}
		
		else if( search_kind.equals( "private" ) ){
			String dir_name = "";

			try{
				ResultSet result = dbmanager.getDir( dir_id );
				if( result.next() ) {
					dir_name = result.getString( "name" );
				} 
			} catch( SQLException e ){
				out.println(  "fail to get dir_name " );
				return;
			}
			out.println("<table width=80% border=0 cellspacing=0>");
			if( !dir_name.equals("") ) {
				out.println("<tr><td colspan=2><center><font size=2><font color=red>" +  dir_name + uni.convert("</font> ���丮�� ��ϵ� �����Դϴ�" ) + "</font></center></td></tr>" );
				out.println("<tr><td></td></tr>" );
			}
			out.println("<tr><td align=right><form method=post action=/namecard/UserServlet target=opener>");
			out.println("<input type=hidden name=cmd value=deleteCard>");
			out.println("<input type=hidden name=dir_id value=\""+dir_id+"\">");
			out.println("<input type=hidden name=card_id value=\""+card_id+"\">");
			out.println("<input type=hidden name=delete_kind value=search>");
			out.println("<input type=submit value=" + uni.convert("����" ) + " onClick=\"window.close();\"></form></td>");
			out.println("<td width=50% align=left><form method=post action=/namecard/UserServlet target=opener>");
			out.println("<input type=hidden name=cmd value=updateCardForm>");
			out.println("<input type=hidden name=card_id value=\"" + card_id + "\">");
			out.println("<input type=hidden name=dir_id value=\"" + dir_id + "\">");
			out.println("<input type=submit value=\"" + uni.convert( "����" ) + "\" onClick=\"window.close();\"></form></td></tr>");

		}

		out.println("<tr><td colspan=2 align=center>");
		out.println("<input type=button value=" + uni.convert("�ݱ�") + " onClick=\"window.close();\">"); 
		out.println("</td></tr></table>");
		out.println("</center></body>");
		out.println("</html>");
	}


	///////////////////////////////  ���丮 ����  ///////////////////////////
	
	/**
	 *  addDir - ���ο� ���丮�� �߰��ϴ� method
	 *
	 *  @param req  �Էµ� �����Ͱ� ����ִ� ��
	 *  @param out  ����� ����� ��
	 *
	 */
	public void addDir( HttpServletRequest req, PrintWriter out ) throws CommandException, UnsupportedEncodingException {
		// session�� ���´�.
		HttpSession httpsession = req.getSession( false );
		
		// session�� ������ ����� ����� ���� �ʾҴٴ� ���̹Ƿ� error ó���Ѵ�.
		if( httpsession == null) {
			out.println( head );
			out.println( "<h1>Error !!! </h1>" );
			out.println( end );
			return;
		}
	
		// session���� ���� ����ڿ� ���� ������ ��´�.
		String id = (String)httpsession.getValue( "id" );

		// ����ϰ��� �ϴ� ���丮 �̸��� ��´�.
		String dirname = asc.convert(req.getParameter( "dirName" ));
		String name = "";
		//tokenizing �ؼ� space���ڸ� �����ش�.
		StringTokenizer st = new StringTokenizer(dirname);
		while(st.hasMoreTokens())
			if(name == "") name = st.nextToken();
			else name = name + st.nextToken();

		if( name.equals( "" ) ){
			out.println( head );
			out.println( uni.convert("<br><br><center><font size=2 color=white>���丮 �̸��� �����ϴ�. �̸��� �Է��Ͻð� �ٽ� �õ��ϼ��� <br><p>") ); 
			out.println("<a href=\"javaScript:window.history.go(-1);\"><font size=2><b><-back</a></center></font>");
			out.println( end );
			return;
		}	

		
		try {
			//card id�� �����.
			Calendar date = Calendar.getInstance();
			String dir_id = id + String.valueOf(date.get(date.YEAR)) + String.valueOf(date.get(date.MONTH)) + String.valueOf(date.get(date.DATE)) + String.valueOf(date.get(date.HOUR)) + String.valueOf(date.get(date.MINUTE)) + String.valueOf(date.get(date.SECOND));

			//DB�� �߰��Ѵ�.
			dbmanager.addDir( id, dir_id, dirname  );
			showDirList( req, out );
		} catch( Exception e ) {
			out.println( head );
			out.println( " fail to add new directory at CardBoxManager " + e.toString() );
			out.println( end );
			return;
		} 
	}


	/** 
	 *  deleteDir - ���� ��ϵǾ��ִ� ���丮�� �����ϴ� method
	 *
	 *  @param req  �Էµ� �����Ͱ� ����ִ� ��
	 *  @param out  ����� ����� ��
	 *
	 */
	public void deleteDir( HttpServletRequest req, PrintWriter out )  {
		// session�� ���´�.
		HttpSession httpsession = req.getSession( false );
		
		// session�� ������ ����� ����� ���� �ʾҴٴ� ���̹Ƿ� error ó���Ѵ�.
		if( httpsession == null) {
			out.println( head );
			out.println( "<h1>Error !!! </h1>" );
			out.println( end );
			return;
		}
	
		// session���� ���� ����ڿ� ���� ������ ��´�.
		String id = (String)httpsession.getValue( "id" );


		// ������� �ϴ� ���丮 �̸��� ��´�.
		String dir_id = req.getParameter( "dir_id" );
		
		
		try {
			dbmanager.deleteDir( dir_id );
			//System.out.println("removed directory");
			showDirList( req, out );
		} catch( Exception e ) {
			out.println( head );
			out.println( " fail to remove directory " + e.toString() );
			out.println( end );
			return;
		}
	}
	
	/**
	 *  renameDir - ���丮 �̸��� �ٲٴ� method
	 *
	 *  @param req  �Էµ� �����Ͱ� ����ִ� ��
	 *  @param out  ����� ����� ��
	 *
	 */
	public void renameDir( HttpServletRequest req, PrintWriter out )throws UnsupportedEncodingException {
	    //out.println("came in boxmanager.renameDir");	
		// session�� ���´�.
		HttpSession httpsession = req.getSession( false );
		
		// session�� ������ ����� ����� ���� �ʾҴٴ� ���̹Ƿ� error ó���Ѵ�.
		if( httpsession == null) {
			out.println( head );
			out.println( "<h1>Error !!! </h1>" );
			out.println( end );
			return;
		}
	
		// session���� ���� ����ڿ� ���� ������ ��´�.
		String id = (String)httpsession.getValue( "id" );
		
		//�ٲ� �̸��� ��´�. 
		String name = asc.convert(req.getParameter( "dirName" ));
		String to = "";
		//tokenizing �ؼ� space���ڸ� �����ش�.
		StringTokenizer st = new StringTokenizer(name);
		while(st.hasMoreTokens())
			if(to == "") to = st.nextToken();
			else to = to + st.nextToken();

		if( to.equals( "" ) ){
			out.println( head );
			out.println( uni.convert("<center><br><br><font size=2 color=white>���丮 �̸��� �����ϴ�. �̸��� �Է��Ͻð� �ٽ� �õ��ϼ��� <br><br><p>") ); 
			out.println("<a href=\"javaScript:window.history.go(-1);\"><font size=2><b><-back</a></font></center>");
			out.println( end );
			return;
		}	

		
		// �ٲ� ���丮 id�� ��´�.
		String dir_id = req.getParameter( "dir_id" );
		
		ResultSet dir = null;

		// �̸��� �����Ѵ�.
		try {
			//DB���� ���丮�� ���� ������ ��´�.
			dir = dbmanager.getDir( dir_id );
			dir.next();
			//���丮 ���� �ִ� ī����� ��´�.
			int num = dir.getInt( "number" );

			//���丮�� �����Ѵ�.
			dbmanager.updateDir( dir_id, uni.convert(to), num );
			showDirList( req, out );
		} catch( Exception e ) {
			out.println( head );
			out.println( " fail to set directory name  " + e.toString() );
			out.println( end );
			return;
		}
		
		
	}


	////////////////////////////  ���԰���  //////////////////////////////
	
	/**
	 *  addNewCard - ���ο� ������ ���԰����Կ� ��Ͻ�Ű�� method 
	 *
	 *  @param  req         �Էµ� �����Ͱ� ����ִ� ��
	 *  @param  out         ����� ����� ��
	 *
	 */
	public void addNewCard( HttpServletRequest req, PrintWriter out ) throws  UnsupportedEncodingException {
		// ���� �˻�
		HttpSession httpsession = req.getSession( false );
		if( httpsession == null ) {
			out.println( head );
			out.println( "<h1>Unauthorized User! </h1>" );
			out.println( end );
			return;
		}
		
		//  session���� person object�� ���´�.
		String id = (String)httpsession.getValue( "id" );
		
		//  session���� ���丮 index�� �� ���丮�� ��´�.
		String dir_id = ( String )httpsession.getValue( "current_dir" );
		
		// req���� parameter parsing�Ѵ�.
		String s_name     = asc.convert(req.getParameter( "name" ));
		String s_c_name   = asc.convert(req.getParameter( "c_name" ));
		int    c_region   = Integer.parseInt( req.getParameter( "c_region" ) );
		String s_c_site   = asc.convert(req.getParameter( "c_site" ));
		String s_address  = asc.convert(req.getParameter( "address" ));
		String beeper 	  = req.getParameter( "beeper" );
		String s_email 	  = req.getParameter( "email" );
		String homepage   = req.getParameter( "homepage" );
		String handphone  = req.getParameter( "handphone" );
		String tel 		  = req.getParameter( "tel" );
		// job info
		String j_fax	  = req.getParameter( "c_fax" );
		String s_j_tel	  = req.getParameter( "c_tel" );
		int j_kind 		  = Integer.parseInt( req.getParameter( "j_kind" ));
		String part   	  = asc.convert(req.getParameter( "part" ));
		String s_position = asc.convert(req.getParameter( "position" ));
	
		String name = "";
		String c_name = "";
		String address = "";
		String email = "";
		String j_tel = "";
		String position = "";
		String c_site = "";
	
		//tokenizing �ؼ� space���ڸ� �����ش�.
		StringTokenizer st1 = new StringTokenizer(s_name);
		while(st1.hasMoreTokens())
			if(name == "") name = st1.nextToken();
			else name = name + st1.nextToken();
	
		//tokenizing �ؼ� space���ڸ� �����ش�.
		StringTokenizer st2 = new StringTokenizer(s_c_name);
		while(st2.hasMoreTokens())
			if(c_name == "") c_name = st2.nextToken();
			else c_name = c_name + st2.nextToken();

		//tokenizing �ؼ� space���ڸ� �����ش�.
		StringTokenizer st3 = new StringTokenizer(s_address);
		while(st3.hasMoreTokens())
			if(address == "") address = st3.nextToken();
			else address = address + st3.nextToken();

		//tokenizing �ؼ� space���ڸ� �����ش�.
/*		StringTokenizer st4 = new StringTokenizer( s_email );
		while(st4.hasMoreTokens())
			if(email == "") email = st4.nextToken();
			else email = email + st4.nextToken();
*/
		//tokenizing �ؼ� space���ڸ� �����ش�.
		StringTokenizer st5 = new StringTokenizer(s_j_tel);
		while(st5.hasMoreTokens())
			if(j_tel == "") j_tel = st5.nextToken();
			else j_tel = j_tel + st5.nextToken();

		//tokenizing �ؼ� space���ڸ� �����ش�.
		StringTokenizer st6 = new StringTokenizer(s_position);
		while(st6.hasMoreTokens())
			if(position == "") position = st6.nextToken();
			else position = position + st6.nextToken();

		//tokenizing �ؼ� space���ڸ� �����ش�.
		StringTokenizer st7 = new StringTokenizer(s_c_site);
		while(st7.hasMoreTokens())
			if(c_site == "") c_site = st7.nextToken();
			else c_site = c_site + st6.nextToken();
			
		// �Էµ� ������ Ȯ���Ѵ�.
		if( name.equals( "" ) || c_name.equals( "" ) || 
			address.equals( "" ) ||  
			position.equals( "" ) || j_tel.equals("" ) ) {
			out.println( head );
			out.println( uni.convert( "<center><br><br><font size=2 color=white>�Է��� �����մϴ�. �ڼ��� ������ �Է��Ͻð�  �ٽ� �õ��ϼ���<br>") );
			out.println("<br><p><a href=\"javaScript:window.history.go(-1);\"><font size=2><b><-Back</a></center></font>");
			out.println( end );
			return;
		}
 
		String message = "";
		Connection con = null;	
		Statement stmt = null;
		// ������ �̹� ��ϵǾ��ִ��� Ȯ���Ѵ�.
		try{
			//��ϵǾ� �ִ� ������� ���Ѵ�.
			String r = dbmanager.existCard( id, s_name, s_c_name, c_region) ;
			if( r != null ){
				out.println( head );
				out.println( uni.convert("<font size=2 color=white><br><br>" + r+ " ���丮�� �̹� ��ϵ� ����� �Դϴ�.<br><br>") );	
				out.println("<a href=\"javaScript:window.history.go(-1);\"><font size=2><b><-back</a></center></font>"); 
				out.println( end );
				return;
			}
				
			// object���� �ð� �Է�
			Calendar date = Calendar.getInstance();
			String num = id + String.valueOf(date.get(date.YEAR)) + String.valueOf(date.get(date.MONTH)) + String.valueOf(date.get(date.DATE)) + String.valueOf(date.get(date.HOUR)) + String.valueOf(date.get(date.MINUTE)) + String.valueOf(date.get(date.SECOND));
		
			ResultSet com = null;
			message = " in searchCompany ";
			// ȸ�簡 ������ �״�� ��ũ, ������ �����Ѵ�.
			com = dbmanager.searchCompany( uni.convert(c_name), c_region, uni.convert(c_site));
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

			// ȸ�� ���̵� 
			String com_id = null;	
			if( !com.next() ) {
				//ȸ�簡 ������� Company DB�� �ִ´�.
				message = " in query1";
				String query1 = "insert into Company (id, name, site, region, employee, cominfo) values ('" + num + "', '" + uni.convert(c_name) + "', '" + uni.convert(c_site) + "','" + c_region + "', '1', 'none');";

				//qeury �� ����
				stmt.executeUpdate( query1 );
				com_id = num;
			} else {
				message = " in update ";
				com_id = com.getString( "id" );
				// �̹� �ִ� ȸ���� �������� �ø���.
				int employee = com.getInt( "employee" );
				employee++;
				String update = "update Company set employee='" + employee + "' where id='" + com_id+ "';";
				stmt.executeUpdate( update );		
			} 

			//���ο� ����ڿ� ���� ������ PersonInfo DB�� �߰��Ѵ�.
			message = " in query2 ";
			String query2 = "insert into PersonInfo (id, address, tel, beeper, handphone, email, homepage) values ('" + num + "', '" + uni.convert( s_address ) + "', '" + tel + "', '" + beeper + "', '" + handphone + "', '" + email + "', '" + homepage + "');";

			//query �� ����
			stmt.executeUpdate( query2 );			

			//���ο� ����ڿ� ���� ������ JobInfo DB�� �߰��Ѵ�.
			message = " in query3 ";
			String query3 = "insert into JobInfo (id, posi, part, tel, fax, kind, company) values ('" + num + "', '" + uni.convert(position) + "', '" +uni.convert( part) + "', '" + j_tel + "', '" + j_fax + "', '" + j_kind + "', '" + com_id + "');";

			//query �� ����
			stmt.executeUpdate( query3 );

			//���ο� ����ڸ� NonMemberPerson DB�� ����Ѵ�.
			message = " in query4";
			String query4 = "insert into NonMemberPerson (id, name, personinfo, jobInfo, com_id) values ('" + num + "', '" + uni.convert(name) + "', '" + num + "', '" + num + "', '" + com_id + "');";

			//query �� ����
			stmt.executeUpdate( query4 );			

				
			//���丮�� ������ ��Ͻ�Ų��.
			message = " in query5";
			String query5 = "insert into Card (dir_id, card_id, person_kind) values ('" + dir_id + "', '" + num + "', 'n' );";

			//query�� ����
			stmt.executeUpdate( query5 );
	
			// ��ϵ� ���Լ��� ��´�.
			message = " in query6";
			String query6 = "select number from Directory where id='" + dir_id + "';";
		 	ResultSet result = stmt.executeQuery( query6 );
			result.next();
		 	int number = result.getInt( "number" );
		 	number = number + 1;

			message = " in query7";
			String query7 = "update Directory set number='" + number + "' where id='" + dir_id + "';";
			stmt.executeUpdate( query7 );

			// connection ����
			con.commit();
			con.close();

			showCardList( req, out );
	
			return;

		}catch( SQLException e ) {
			out.println( " fail to addnewCard in sql " + message + e.toString() );

		} catch( Exception e ) {
			out.println( " fail to addNewCard at CardBoxManager "  + message + e.toString() );
		}

		 		
	}
	
	/**
	 *  addPublicCard - ����� ������ ���� ���丮��  ��Ͻ�Ű�� method 
	 *
	 *  @param  req         ������ �Է¹��� �����Ͱ� ����ִ� ��
	 *  @param  out         ����� ����� ��
	 *
	 */
	public void addPublicCard( HttpServletRequest req, PrintWriter out ) throws UnsupportedEncodingException {
		// ���� �˻�
		HttpSession httpsession = req.getSession( false );
		if( httpsession == null ) {
			out.println( head );
			out.println( "<h1>Unauthorized User! </h1>" );
			out.println( end );
			return;
		}
	
		String id = (String)httpsession.getValue( "id" );

		//req���� �Ķ���� �Ľ�
		String dir_id = req.getParameter("dir_id");  //������ �߰��� ���丮
		String card_id = uni.convert( req.getParameter("card_id"));      

		httpsession.putValue( "current_dir" , dir_id );
		try {
			String r = dbmanager.existCard( card_id, id );
 	
			// ã�� ������ �̹� ��ϵǾ��ִ��� Ȯ���Ѵ�.
			if(  r != null ){			
				//���丮 �̸��� ��´�.
				out.println("<html><head><title></title>");
				out.println("<body bgcolor=#9acd32><center>"); 
				out.println("<font size=3 color=#2f4f4f><b>");
				out.println( uni.convert( r + " ���丮�� �̹� ��ϵǾ��ִ� �����Դϴ�." ) + "</b></font><br><br>" );
				out.println("<a href=\"javaScript:window.history.go(-1);\"><font size=2><b><-back</a></center>"); 
				out.println("</center></body></html>");
				return;
			}else{
				// ���丮�� ������ ��Ͻ�Ų��.
				dbmanager.addCard( card_id, dir_id, "m" );
				showCardList( req, out );
			}
		}catch( Exception e ){
			out.println( head );
			out.println( "fail to get dir list at addPublicCard " + e.toString() );
			out.println( end );
			return;
		}
	}

	/**
	 *  updateCardForm - ���θ��Ե��丮�� ����ִ� �͸�� ������ �����ϱ� ���� �� �����Ѵ�.
	 *
	 *  @param  req         �Էµ� �����Ͱ� ����ִ� ��
	 *  @param  out         ����� ����� ��
	 *
	 */
	 public void updateCardForm( HttpServletRequest req, PrintWriter out ) throws UnsupportedEncodingException{
		// ���� �˻�
		HttpSession httpsession = req.getSession( false );
		if( httpsession == null ) {
			out.println( head );
			out.println( "<h1>Unauthorized User! </h1>" );
			out.println( end );
			return;
		}
		String id = (String)httpsession.getValue( "id" );	
		String card_id = req.getParameter( "card_id" );
		String dir_id = req.getParameter( "dir_id" );
		ResultSet person = null;	
		try {
			//cardContent�� ���´�.( query ��� )
			person = dbmanager.getCardContent( card_id , "n" );
			person.next();	

			int c_region = person.getInt( "region" );
			int j_kind = person.getInt( "kind" );

			out.println("<html><head><title>Untitled Document</title><meta http-equiv=Content-Type content=text/html; charset=euc-kr></head>");
			out.println("</head>");
			out.println("<body background=/bg.jpg>");
			out.println("<table border=0 cellspacing=0 width=670>");
  		  	out.println("<tr>");
   		 	out.println("<td height=28 align=center valign=middle><font size=4 color=#ffff00>");
   		 	out.println("<img src=/icon.gif><b>" + uni.convert( "��������" ) + "</b></font></td>");
  		  	out.println("</tr>");
  			out.println("<tr>");
			out.println("<td align=center valign=top>");
			out.println("<form method=post action=/namecard/UserServlet >");
			out.println("<input type=hidden name=card_id value=" + card_id + ">");
			out.println("<input type=hidden name=dir_id value=" + dir_id + ">");
			out.println("<input type=hidden name=cmd value=updateCard>");
			out.println("<br><p>");
			out.println("<table border=0 width=80% align=center cellspacing=0>");
			out.println("<tr><td colspan=3 align=center><font size=2 color=#e6e6fa>" + uni.convert( "* ǥ�� �Ǿ��ִ� �׸��� �ݵ�� ������ �ּ���." ) );
			out.println("</font><br><p></td></tr>");
			out.println("<tr>");
			out.println("<td><font size=2 color=#e0ffff><b>*</font></td>");
			out.println("<td><font color=#fffacd size=2><b>" + uni.convert( "�̸�" ) + "</b></td>");
			out.println("<td><font size=2 color=#e0ffff>" + person.getString( "name" ) + "</font></font></td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td></td><td><font color=#fffacd size=2><b>" + uni.convert( "��ȭ" ) + "</b></td>");
			out.println("<td><font size=2 color=#e0ffff><input type=text name=tel value=\"" + person.getString( 13 ) + "\" size=12> </font></td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td></td><td><font color=#fffacd size=2><b>" + uni.convert( "�߻�" ) + "</b></td>");
			out.println("<td><font size=2 color=#e0ffff> <input type=text name=beeper value=\"" + person.getString( "beeper" )+ "\" size=13></font></td>");
			out.println("</tr><tr>");
			out.println("<td></td><td><font color=#fffacd size=2><b>" + uni.convert( "�޴���" ) + "</b></td>");
			out.println("<td><font size=2 color=#e0ffff> <input type=text name=handphone value=\"" + person.getString( "handphone" ) +"\" size=13></font></td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td></td><td><font color=#fffacd size=2><b>" + uni.convert( "Ȩ������" ) + "</b></td>");
			out.println("<td><font size=2 color=#e0ffff><input type=text name=homepage value=\"" +person.getString( "homepage" )+ "\" size=40></font></td>");
			out.println("</tr><tr>");
			out.println("<td><font size=2 color=#e0ffff><b></font></td>");
			out.println("<td><font color=#fffacd size=2><b>email<b></td>");
			out.println("<td><font size=2 color=#e0ffff><input type=text name=email value=\"" + person.getString( "email" ) +"\" size=40></font></td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td><font size=2 color=#e0ffff><b>*</font></td>");
			out.println("<td><font color=#fffacd size=2><b>" + uni.convert( "����" ) + "</b></td>");
			out.println("<td><font size=2 color=#e0ffff><input type=text name=position value=\"" + person.getString( "posi" )+ "\" size=20></font></td>");
			out.println("</tr><tr>");
			out.println("<td></td><td><font color=#fffacd size=2><b>" + uni.convert( "�μ�" ) + "</b></td>");
			out.println("<td><font size=2 color=#e0ffff><input type=text name=part value=\"" + person.getString( "part" ) + "\" size=20></font></td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td><font size=2 color=#e0ffff><b>*</font></td>");
			out.println("<td><font color=#fffacd size=2><b>" + uni.convert( "ȸ�簳����ȭ" ) + "</b></td> ");
			out.println("<td><font size=2 color=#e0ffff><input type=text name=c_tel value=\"" + person.getString( "tel" ) + "\" size=13></font>");
			out.println("<font size=2 color=#e0ffff>" + uni.convert( "ȸ�簳����ȭ�� ������ ȸ����ȭ��ȣ�� �Է��ϼ���." ) + " </td>");
			out.println("</tr><tr>");
			out.println("<td></td><td><font color=#fffacd size=2><b>" + uni.convert( "ȸ���ѽ�" ) + "</b></td>");
			out.println("<td><font size=2 color=#e0ffff><input type=text name=c_fax value=\"" + person.getString( "fax" ) + "\" size=13></font></td>");
			out.println("</tr>");
		    out.println("</table><br><p></td>");
		    out.println("</tr>"); 
		    out.println("<tr>");
		    out.println("<td align=center height=28><b><font size=4 color=#ffff00><img src=/icon.gif>" + uni.convert( "ȸ������" ) + "</b><br><p></td>");
			out.println("</tr>");
			out.println("<tr><td align=center>");
			out.println("<table border=0 width=80% cellspacing=0>");
			out.println("<tr>");
			out.println("<td><font size=2 color=#e0ffff><b>*</font></td>");
			out.println("<td><font color=#fffacd size=2><b>" + uni.convert( "ȸ���̸�" ) + "</b></td>");
			out.println("<td><font size=2 color=#e0ffff> <input type=text name=c_name value=\"" + person.getString( 10 ) + "\" size=20></font></td>");
			out.println("<td><font size=2 color=#e0ffff><b>*</font></td>");
			out.println("<td><font color=#fffacd size=2><b>" + uni.convert( "����" ) + "</b></td>");
			out.println("<td><font size=2 color=#e0ffff><select name=c_region size=1>");

			if(c_region ==1) out.println("<option value=1 selected>" + uni.convert( "����Ư����" ) + "</option>");
			else out.println("<option value=1>" + uni.convert( "����Ư����" ) + "</option>");
 		 	
		   	if(c_region == 2) out.println("<option value=2 selected>" + uni.convert( "�λ걤����" ) + "</option>");
			else out.println("<option value=2>" + uni.convert( "�λ걤����" ) + "</option>");
	
			if(c_region == 3) out.println("<option value=3 selected>" + uni.convert( "�뱸������" ) + "</option>");
 			else out.println("<option value=3>" + uni.convert( "�뱸������" ) + "</option>");
	
			if(c_region == 4) out.println("<option value=4 selected>" + uni.convert( "��õ������" ) + "</option>");
			else out.println("<option value=3>" + uni.convert( "��õ������" ) + "</option>");
            		
			if(c_region == 5) out.println("<option value=5 selected>" + uni.convert( "���ֱ�����" ) + "</option>");
			else out.println("<option value=5>" + uni.convert( "���ֱ�����" ) + "</option>");

			if(c_region ==6) out.println("<option value=6 selected>" + uni.convert( "��걤����" ) + "</option>");
			else out.println("<option value=6>" + uni.convert( "��걤����" ) + "</option>");
        
   			if(c_region ==7) out.println("<option value=7 selected>" + uni.convert( "����������" ) + "</option>");
			else out.println("<option value=7>" + uni.convert( "����������" ) + "</option>");
	
			if(c_region ==8) out.println("<option value=8 selected>" + uni.convert( "���" ) + "</option>");
			else out.println("<option value=8>" + uni.convert( "���" ) + "</option>");
            
			if(c_region ==9) out.println("<option value=9 selected>" + uni.convert( "����" ) + "</option>");
			else out.println("<option value=9>" + uni.convert( "����" ) + "</option>");
	
			if(c_region ==10) out.println("<option value=10 selected>" + uni.convert( "���" ) + "</option>");
			else out.println("<option value=10>" + uni.convert( "���" ) + "</option>");
            
			if(c_region ==11) out.println("<option value=11 selected>" + uni.convert( "�泲" ) + "</option>");
			else out.println("<option value=11>" + uni.convert( "�泲" ) + "</option>");

			if(c_region ==12) out.println("<option value=12 selected>" + uni.convert( "����" ) + "</option>");
			else out.println("<option value=12>" + uni.convert( "����" ) + "</option>");
            
			if(c_region ==13) out.println("<option value=13 selected>" + uni.convert( "����" ) + "</option>");
			else out.println("<option value=13>" + uni.convert( "����" ) + "</option>");

			if(c_region ==14) out.println("<option value=14 selected>" + uni.convert( "���" ) + "</option>");
			else out.println("<option value=14>" + uni.convert( "���" ) + "</option>");
            		
			if(c_region ==15) out.println("<option value=15 selected>" + uni.convert( "�泲" ) + "</option>");
			else out.println("<option value=15>" + uni.convert( "�泲" ) + "</option>");

			if(c_region ==16) out.println("<option value=16 selected>" + uni.convert( "����" ) + "</option>");
			else out.println("<option value=16>" + uni.convert( "����" ) + "</option>");
			out.println("</select></font> </td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td></td><td><font color=#fffacd size=2><b>" + uni.convert( "����" ) + "</b></td>");
			out.println("<td><font size=2 color=#e0ffff><input type=text name=c_site value=\"" + person.getString( "site" ) + "\" size=20></font></td>");
			out.println("<td><font size=2 color=#e0ffff><b>*</td>");
			out.println("<td><font color=#fffacd size=2><b>" + uni.convert( "����" ) + "</b></font></td>");
			out.println("<td><font size=2 color=#e0ffff><select name=j_kind size=1>");

			if(j_kind == 1) out.println("<option value=1 selected>" + uni.convert( "������" ) + "</option>");
			else out.println("<option value=1>" + uni.convert( "������" ) + "</option>");

  		   	if(j_kind ==2) out.println("<option value=2 selected>" + uni.convert( "����" ) + "</option>");
			else out.println("<option value=2>" + uni.convert( "����" ) + "</option>");
        
			if(j_kind ==3) out.println("<option value=3 selected>" + uni.convert( "�Ƿ�" ) + "</option>");
			else out.println("<option value=3>" + uni.convert( "�Ƿ�" ) + "</option>");
        
			if(j_kind ==4) out.println("<option value=4 selected>" + uni.convert( "����" ) + "</option>");
			else out.println("<option value=4>" + uni.convert( "����" ) + "</option>");
        
			if(j_kind ==5) out.println("<option value=5 selected>" + uni.convert( "���/�ڵ���" ) + "</option>");
			else out.println("<option value=5>" + uni.convert( "���/�ڵ���" ) + "</option>");
       
			if(j_kind ==6) out.println("<option value=6 selected>" + uni.convert( "��ǻ��/���" ) + "</option>");
			else out.println("<option value=6>" + uni.convert( "��ǻ��/���" ) + "</option>");
        	
			if(j_kind ==7) out.println("<option value=7 selected>" + uni.convert( "���񽺾�" ) + "</option>");
			else out.println("<option value=7>" + uni.convert( "���񽺾�" ) + "</option>");

   	    	if(j_kind ==8) out.println("<option value=8 selected>" + uni.convert( "����" ) + "</option>");
			else out.println("<option value=8>" + uni.convert( "����" ) + "</option>");
	
   	    	if(j_kind ==9) out.println("<option value=9 selected>" + uni.convert( "����/���׸���" ) + "</option>");
			else out.println("<option value=9>" + uni.convert( "����/���׸���" ) + "</option>");

   	    	if(j_kind ==10) out.println("<option value=10 selected>" + uni.convert( "���" ) + "</option>");
			else out.println("<option value=10>" + uni.convert( "���" ) + "</option>");
        
			if(j_kind ==11) out.println("<option value=11 selected>" + uni.convert( "����" ) + "</option>");
			else out.println("<option value=11>" + uni.convert( "����" ) + "</option>");
        	
			if(j_kind ==12) out.println("<option value=12 selected>" + uni.convert( "����" ) + "</option>");
			else out.println("<option value=12>" + uni.convert( "����" ) + "</option>");
        
			if(j_kind ==13) out.println("<option value=13 selected>" + uni.convert( "������" ) + "</option>");
			else out.println("<option value=13>" + uni.convert( "������" ) + "</option>");
        
			if(j_kind ==14) out.println("<option value=14 selected>" + uni.convert( "��������" ) + "</option>");
			else out.println("<option value=14>" + uni.convert( "��������" ) + "</option>");
        
			if(j_kind ==15) out.println("<option value=15 selected>" + uni.convert( "��Ÿ" ) + "</option>");
			else out.println("<option value=15>" + uni.convert( "��Ÿ" ) + "</option>");
		
   		    out.println("</select></font></td></tr>"); 
			out.println("<tr>");
			out.println("<td><font size=2 color=#e0ffff><b>*</font></td>");
			out.println("<td><font color=#fffacd size=2><b>" + uni.convert( "�ּ�" ) + "</b></td>");
			out.println("<td colspan=5><font size=2 color=#e0ffff> <input type=text name=address value=\"" + person.getString( "address" ) + "\" size=50></font></td></tr>");
			out.println("<tr>");
			out.println("<td colspan=6 align=center><br><input type=submit value='" + uni.convert( "���" ) + "'>");
			out.println("<input type=button value='" + uni.convert( "���" ) + "' onClick=\"window.history.go(-1);\"></td>" );
			out.println("</tr>");
			out.println("</table></center></td>");
			out.println("</tr>");
			out.println("</table></form>");
			out.println("</body>");
			out.println("</html>");
		} catch( SQLException e ) {
			out.println( head );
			out.println( uni.convert( "���� �����ִ� ���� �����߽��ϴ�" ) + e.toString() );
			out.println( end );
		} catch( Exception e ) {
			out.println( head );
			out.println( uni.convert( "�������ֱ� ����" ) + e.toString() );
			out.println( end );
		}
	}

	/**
	 *  updateCard - ���丮�� ��ϵ� ������ ������ �����Ѵ�.
	 * 
	 *  @param  req  �ӷµ� �����Ͱ� ����ִ� ��
	 *  @param  out  ����� ����� ��
	 */
	public void updateCard( HttpServletRequest req, PrintWriter out ) throws UnsupportedEncodingException {
		// ���� �˻�
		HttpSession httpsession = req.getSession( false );
		if( httpsession == null ) {
			out.println( head );
			out.println( "<h1>Unauthorized User! </h1>" );
			out.println( end );
			return;
		}

		String card_id = req.getParameter( "card_id" );
		//  session���� person object�� ���´�.
		String id = (String)httpsession.getValue( "id" );
		
		// req���� parameter parsing�Ѵ�.
		String s_c_name   = asc.convert(req.getParameter( "c_name" ));
		int    c_region   = Integer.parseInt( req.getParameter( "c_region" ) );
		String s_c_site   = asc.convert(req.getParameter( "c_site" ));
		String s_address  = asc.convert(req.getParameter( "address" ));
		String s_beeper   = req.getParameter( "beeper" );
		String s_email 	  = req.getParameter( "email" );
		String s_homepage = req.getParameter( "homepage" );
		String s_handphone= req.getParameter( "handphone" );
		String s_tel 	  = req.getParameter( "tel" );
		// job info
		String s_j_fax	  = req.getParameter( "c_fax" );
		String s_j_tel	  = req.getParameter( "c_tel" );
		int j_kind 		  = Integer.parseInt( req.getParameter( "j_kind" ));
		String s_part  	  = asc.convert(req.getParameter( "part" ));
		String s_position = asc.convert(req.getParameter( "position" ));

		String c_name = "";
		String c_site = "";
		String address = "";
		String beeper = "";
		String email = "";
		String homepage = "";
		String handphone = "";
		String tel = ""; 
		String j_fax = "";
		String j_tel = "";
		String position = "";
		String part = ""; 	

		String d_name = "";
		String d_site = "";
		String d_address = "";
		String d_beeper = "";
		String d_email = "";
		String d_homepage = "";
		String d_handphone = "";
		String d_tel = ""; 
		String d_j_fax = "";
		String d_j_tel = "";
		String d_position = "";
		String d_part = ""; 		
		String message = "";
		//tokenizing �ؼ� space���ڸ� �����ش�.
		StringTokenizer st= new StringTokenizer(s_c_name);
		while(st.hasMoreTokens())
			if(c_name == "") c_name = st.nextToken();
			else c_name = c_name + st.nextToken();

		//tokenizing �ؼ� space���ڸ� �����ش�.
		st = new StringTokenizer(s_c_site);
		while(st.hasMoreTokens())
			if(c_site == "") c_site = st.nextToken();
			else c_site = c_site + st.nextToken();

		//tokenizing �ؼ� space���ڸ� �����ش�.
		st = new StringTokenizer(s_address);
		while(st.hasMoreTokens())
			if(address == "") address = st.nextToken();
			else address = address + st.nextToken();

		//tokenizing �ؼ� space���ڸ� �����ش�.
		st = new StringTokenizer(s_beeper);
		while(st.hasMoreTokens())
			if(beeper == "") beeper = st.nextToken();
			else beeper = beeper + st.nextToken();

		//tokenizing �ؼ� space���ڸ� �����ش�.
		st = new StringTokenizer(s_email);
		while(st.hasMoreTokens())
			if(email == "") email = st.nextToken();
			else email = email + st.nextToken();

		//tokenizing �ؼ� space���ڸ� �����ش�.
		st = new StringTokenizer(s_homepage);
		while(st.hasMoreTokens())
			if(homepage == "") homepage = st.nextToken();
			else homepage = homepage + st.nextToken();

		//tokenizing �ؼ� space���ڸ� �����ش�.
		st = new StringTokenizer(s_handphone);
		while(st.hasMoreTokens())
			if(handphone == "") handphone = st.nextToken();
			else handphone = handphone + st.nextToken();

		//tokenizing �ؼ� space���ڸ� �����ش�.
		st = new StringTokenizer(s_tel);
		while(st.hasMoreTokens())
			if(tel == "") tel = st.nextToken();
			else tel = tel + st.nextToken();

		//tokenizing �ؼ� space���ڸ� �����ش�.
		st = new StringTokenizer(s_j_tel);
		while(st.hasMoreTokens())
			if(j_tel == "") j_tel = st.nextToken();
			else j_tel = j_tel + st.nextToken();

		//tokenizing �ؼ� space���ڸ� �����ش�.
		st = new StringTokenizer(s_j_fax);
		while(st.hasMoreTokens())
			if(j_fax == "") j_fax = st.nextToken();
			else j_fax = j_fax + st.nextToken();

		//tokenizing �ؼ� space���ڸ� �����ش�.
		st = new StringTokenizer(s_position);
		while(st.hasMoreTokens())
			if( position == "") position = st.nextToken();
			else position = position + st.nextToken();

		//tokenizing �ؼ� space���ڸ� �����ش�.
		st = new StringTokenizer(s_part);
		while(st.hasMoreTokens())
			if( part == "") part = st.nextToken();
			else part = part + st.nextToken();
message = "check input";
		// �Էµ� ������ Ȯ���Ѵ�.
		if( c_name.equals( "" ) || address.equals( "" ) || 
			position.equals( "" ) || j_tel.equals( "" ) ) {
			out.println( head );
			out.println( uni.convert( "<center><br><br><font size=2 color=white>�Է��� �����մϴ�. �ڼ��� ������ �Է��Ͻð�  �ٽ� �õ��ϼ���<br>") );
			out.println("<br><p><a href=\"javaScript:window.history.go(-1);\"><font size=2><b><-Back</a></center></font>");
			out.println( end );
			return;
		}

		try { 	
			Class.forName( site.DRIVER );
		} catch( ClassNotFoundException e) {
			out.println( head );
			out.println( "fail to connect DB " + e.toString() );
			out.println( end );
		}
	 
		Connection con = null;
		Statement stmt = null;
		ResultSet person = null; 
	
		// DB connection����
	    try {
			con = (Connection)DriverManager.getConnection( NameCardSite.URL, NameCardSite.ID, NameCardSite.PASSWD );
			stmt = con.createStatement();
			con.setAutoCommit( false );
		} catch( SQLException e ) {
			out.println( head );
			out.println( "fail to connect DB " );
			out.println( end );
			return;
		}

		message = "select";
		try {
			//cardContent�� ���´�.( query ��� )
 			String select = "select NonMemberPerson.name," +
					" PersonInfo.handphone, PersonInfo.email, PersonInfo.homepage," +
					" PersonInfo.beeper, JobInfo.posi, JobInfo.part, JobInfo.tel," +
					" Company.site, Company.name, PersonInfo.address, " +
					" JobInfo.fax, PersonInfo.tel, PersonInfo.id, " +
					" JobInfo.id, Company.region, JobInfo.kind  " +
					" from NonMemberPerson, PersonInfo, JobInfo, Company" +
					" where NonMemberPerson.id ='" + card_id +
					"' and NonMemberPerson.personinfo =PersonInfo.id" +
					" and NonMemberPerson.jobinfo =JobInfo.id" +
					" and NonMemberPerson.com_id =Company.id;"; 
			person = stmt.executeQuery( select );
			person.next();
message = "d_parsing";
			//tokenizing �ؼ� space���ڸ� �����ش�.
			st= new StringTokenizer(person.getString( 10 ));
			while(st.hasMoreTokens())
				if(d_name == "") d_name = st.nextToken();
				else d_name = d_name + st.nextToken();
			//tokenizing �ؼ� space���ڸ� �����ش�.
			st = new StringTokenizer(person.getString( "site" ) );
			while(st.hasMoreTokens())
				if(d_site == "") d_site = st.nextToken();
				else d_site = d_site + st.nextToken();
			//tokenizing �ؼ� space���ڸ� �����ش�.
			st = new StringTokenizer( person.getString( "address" ) );
			while(st.hasMoreTokens())
				if(d_address == "") d_address = st.nextToken();
				else d_address = d_address + st.nextToken();
			//tokenizing �ؼ� space���ڸ� �����ش�.
			st = new StringTokenizer( person.getString( "beeper" ));
			while(st.hasMoreTokens())
				if(d_beeper == "") d_beeper = st.nextToken();
				else d_beeper = d_beeper + st.nextToken();
			//tokenizing �ؼ� space���ڸ� �����ش�.
			st = new StringTokenizer( person.getString( "email" ));
			while(st.hasMoreTokens())
				if(d_email == "") d_email = st.nextToken();
				else d_email = d_email + st.nextToken();
			//tokenizing �ؼ� space���ڸ� �����ش�.
			st = new StringTokenizer( person.getString( "homepage" ));
			while(st.hasMoreTokens())
				if(d_homepage == "") d_homepage = st.nextToken();
				else d_homepage = d_homepage + st.nextToken();
			//tokenizing �ؼ� space���ڸ� �����ش�.
			st = new StringTokenizer(person.getString( "handphone"));
			while(st.hasMoreTokens())
				if(d_handphone == "") d_handphone = st.nextToken();
				else d_handphone = d_handphone + st.nextToken();
message = "tel";
			//tokenizing �ؼ� space���ڸ� �����ش�.
			st = new StringTokenizer(person.getString( 13));
			while(st.hasMoreTokens())
				if(d_tel == "") d_tel = st.nextToken();
				else d_tel = d_tel + st.nextToken();
			//tokenizing �ؼ� space���ڸ� �����ش�.
			st = new StringTokenizer(person.getString( 8 ));
			while(st.hasMoreTokens())
				if(d_j_tel == "") d_j_tel = st.nextToken();
				else d_j_tel = d_j_tel + st.nextToken();
			//tokenizing �ؼ� space���ڸ� �����ش�.
			st = new StringTokenizer(person.getString( "fax" ));
			while(st.hasMoreTokens())
				if(d_j_fax == "") d_j_fax = st.nextToken();
				else d_j_fax = d_j_fax + st.nextToken();
			//tokenizing �ؼ� space���ڸ� �����ش�.
			st = new StringTokenizer(person.getString( "posi" ));
			while(st.hasMoreTokens())
				if(d_position == "") d_position = st.nextToken();
				else d_position = d_position + st.nextToken();
			//tokenizing �ؼ� space���ڸ� �����ش�.
			st = new StringTokenizer(person.getString( "part" ));
			while(st.hasMoreTokens())
				if(d_part == "") d_part = st.nextToken();
				else d_part = d_part + st.nextToken();
			int d_region = person.getInt( "region" );
			int d_kind = person.getInt( "kind" );
message = "update check";	
			// �����Ұ� �ִ��� Ȯ���Ѵ�.
			if( c_name.equals( d_name ) && c_site.equals( d_site ) && ( c_region == d_region ) && 
			email.equals( d_email ) && beeper.equals( d_beeper ) && tel.equals( d_tel ) &&
			handphone.equals( d_handphone ) && homepage.equals( d_homepage ) &&
			address.equals( d_address ) && j_fax.equals( d_j_fax ) && j_tel.equals( d_j_tel ) &&
			part.equals( d_part ) && position.equals( d_position ) && ( j_kind == d_kind ) ) {
			out.println( head );
			out.println( uni.convert( "������ ������ �����ϴ�. �ٽ� �Է��Ͻð� �õ��ϼ���" ) );
			out.println( end );
			return;
		} 	
			// personinfo, jobinfo ���̵� ���
			String personinfo = person.getString( 14 );
			String jobinfo = person.getString( 15 );

message = "company check";
			if( !c_name.equals( d_name ) && !c_site.equals( d_site ) && (c_region !=  d_region)) {	
				// ���� ȸ�簡 �����ϴ��� ã�´�.
				String query1 = "select * from Company where name='" + uni.convert(c_name) +
								"'  and region=" + c_region + 
								" and site='" + uni.convert(c_site) + "';";
				ResultSet com = stmt.executeQuery( query1 );
				String comid = null;
	
				if( !com.next() ) {
					// object���� �ð� �Է�
					Calendar date = Calendar.getInstance();
					comid = id + String.valueOf(date.get(date.YEAR)) + String.valueOf(date.get(date.MONTH)) + String.valueOf(date.get(date.DATE)) + String.valueOf(date.get(date.HOUR)) + String.valueOf(date.get(date.MINUTE)) + String.valueOf(date.get(date.SECOND));
			
					String query2 = "insert into Company (id, name, site, region, employee, cominfo ) " +
									"values ('" + comid + "', '" + uni.convert(c_name) + "', '" +
									uni.convert(c_site) + "', '" + c_region + "', '1', 'none');";
					stmt.executeUpdate( query2 );
					String query3 = "update JobInfo set kind='" + j_kind + "' where id='" + jobinfo + "';";
					stmt.executeUpdate( query3 );
				} else {
					comid = com.getString( "id" );					 	
	
					// �̹� �ִ� ȸ���� �������� �ø���.
					int employee = com.getInt( "employee" );
					employee++;
					String query4 = "update Company set employee='" + employee + "' where id='" + comid+ "';";
					stmt.executeUpdate( query4 );									
				}
				String query5 = "update NenMemberPerson set comid='"+ comid + "'" + 
								" where id='" + card_id + "';";
				stmt.executeUpdate( query5 );
			} // ȸ�� ����


message = "update personinfo";
			String update = null;
			// handphone ����
			if( !handphone.equals( d_handphone ) ) {
				update = "update PersonInfo set handphone='" + handphone + "' " +
							"where id='" + personinfo + "';";
				stmt.executeUpdate( update );
			} // address 
			if( !address.equals( d_address ) ) {
				update = "update PersonInfo set address='" + uni.convert(s_address) + "' " +
						" where id='" +  personinfo + "';";
				stmt.executeUpdate( update );
			} // beeper
			if( !beeper.equals( d_beeper ) ) {
				update = "update PersonInfo set beeper='" + beeper + "' " +
						" where id='" + personinfo + "';";
				stmt.executeUpdate( update );
			} // email
message = "email";
			if( !email.equals( d_email ) ) {
				update = "update PersonInfo set email='" + email + "' " +
						" where id='" + personinfo + "';";
				stmt.executeUpdate( update );
			} // homepage
			if( !homepage.equals( d_homepage ) ) {
				update = "update PersonInfo set homepage='" + homepage + "' "+  
						" where id='" + personinfo + "';";
				stmt.executeUpdate( update );
			} // hanephone
			if( !handphone.equals( d_handphone ) ){
				update = "update PersonInfo set handphone='" + handphone + "' "+  
						" where id='" + personinfo + "';";
				stmt.executeUpdate( update );
			} // tel
			if( !tel.equals( d_tel ) ) {
				update = "update PersonInfo set tel='" + tel + "' "+  
						" where id='" + personinfo + "';";
				stmt.executeUpdate( update );
			} // j_tel
message = "update jobinfo";
			if( !j_tel.equals( d_j_tel ) ){
				update = "update JobInfo set tel='" + j_tel + "' "+  
						" where id='" + jobinfo + "';";
				stmt.executeUpdate( update );
			} // j_fax
			if( !j_fax.equals( d_j_fax ) ){
				update = "update JobInfo set fax='" + j_fax + "' "+  
						" where id='" + jobinfo + "';";
				stmt.executeUpdate( update );
			} // position
			if( !uni.convert(position).equals( d_position ) ) {
				update = "update JobInfo set posi='" + uni.convert(position) + "' "+  
						" where id='" + jobinfo + "';";
				stmt.executeUpdate( update );
			} // part
			if( !uni.convert(part).equals( d_part ) ) {
				update = "update JobInfo set part='" + uni.convert(part) + "' "+  
						" where id='" + jobinfo + "';";
				stmt.executeUpdate( update );
			}
			
			con.commit();
			con.close();
			showCardList( req, out );

		} catch( SQLException e ) {
			out.println( head );
			out.println( uni.convert( "���Լ����� �߸��Ǿ����ϴ�. �˼��մϴ�. " ) + e.toString());
			out.println( end );
		} catch( Exception e ) {
			out.println( head );
			out.println( uni.convert( "���Լ����� �����Ͽ����ϴ�. �˼��մϴ�." )  + message + e.toString() );			
			out.println( end );
		}
	}


	/**
	 *  deleteCard - ���丮���� �����ϰ��� �ϴ� ������ �����Ѵ�.
	 *
	 *  @param  req         �Էµ� �����Ͱ� ����ִ� ��
	 *  @param  out         ����� ����� ��
	 *
	 */
	public void deleteCard( HttpServletRequest req, PrintWriter out ) throws  UnsupportedEncodingException {
		// ���� �˻�
		HttpSession httpsession = req.getSession( false );
		if( httpsession == null ) {
			out.println( head );
			out.println( "<h1>Unauthorized User! </h1>" );
			out.println( end );
			return;
		}
		
		//  session���� person object�� ���´�.
		String id = (String)httpsession.getValue( "id" );


		/* delete�� �� ���丮���� ������ Ŭ���ؼ� ������ �� ���� ���ǿ��� ���� ���丮�� 
		 * ������ �˻��ؼ� ���� ������ ���� ������ ��쿡�� �Ķ���ͷ� �� ������ �ִ� ���丮�� 
         * �޾ƿ´�. 
		 **/
		String delete_kind = req.getParameter("delete_kind");    
		String dir_id = null;

		if(delete_kind.equals("myDir")){
			// session ���� ���� ���丮�� index�� �� ���丮�� ��´�.
			dir_id = (String)httpsession.getValue( "current_dir" );	
		}
		if(delete_kind.equals("search")){
			//parameter�� �޾ƿ´�
			dir_id = req.getParameter("dir_id");
		}
		
		// req���� ������ ������ parsing�Ѵ�.
		String card_id = uni.convert(req.getParameter( "card_id" ));
		
		//������ card DB���� �����Ѵ�.
		try{
			dbmanager.deleteCard( card_id, dir_id );
			showCardList( req, out );
		}catch( Exception e ){
			out.println( head );
			out.println( "fail to deleteCard at CardBoxManager " + e.toString() );
			out.println( end );
			return;
		}
	}	

	/*
	 *  moveCard - ���� �ִ� ���丮���� �ٸ� ���丮�� ������ �ű�� method
	 *
	 *  @param  req         �Էµ� �����Ͱ� ����ִ� ��
	 *  @param  out         ����� ����� ��
	 *
	 */
	public void moveCard( HttpServletRequest req, PrintWriter out ) throws UnsupportedEncodingException {
		// ���� �˻�
		HttpSession httpsession = req.getSession( false );
		if( httpsession == null ) {
			out.println( head );
			out.println( "<h1>Unauthorized User! </h1>" );
			out.println( end );
			return;
		}
		
		//  session���� person object�� ���´�.
		String id = (String)httpsession.getValue( "id" );
		
		// session ���� ���� ���丮�� index�� �� ���丮�� ��´�.
		String dir_id = (String)httpsession.getValue( "current_dir" ) ;
		
		// req���� �ű� ���԰� �Űܰ� ���丮 �̸��� parsing�Ѵ�.
		String card_id = uni.convert(req.getParameter( "card_id" ));
		String to  = req.getParameter( "dir_id" ) ;
		
		// ���� �ִ� ���丮���� ������ ����� ���ο� ���丮�� �߰���Ų��.
		try {
			dbmanager.updateCard( card_id, dir_id, to );   
			showCardList( req, out );

		} catch( Exception e ) {
			out.println( head );
			out.println( " fail to move namecard  " + e.toString()  );
			out.println( end );
			return;
		}
	}

////////////////////////////  ���丮 ��ġ  /////////////////////////////////
	 
	/**
	 *  myPersonSearch  - ���԰����Կ� ��ϵ� ���Կ��� ��ġ�ϴ� method
	 *
	 *  @param  req         ������ �Է¹��� �����Ͱ� ����ִ� ��
	 *  @param  out         ����� ����� ��
	 *
	 */
	public void myPersonSearch( HttpServletRequest req, PrintWriter out ) throws UnsupportedEncodingException{
		// ���� �˻�
		HttpSession httpsession = req.getSession( false );
		if( httpsession == null ) {
			out.println( head );
			out.println( "<h1>Unauthorized User! </h1>" );
			out.println( end );
			return;
		}
		
		// req���� �Ķ���� �Ľ��Ѵ�.
		String t_name   = asc.convert( req.getParameter( "name" ) );     // �̸�
		String t_c_name = asc.convert(req.getParameter( "c_name" ) );    // ȸ���̸�
		String kind     = req.getParameter( "kind" );                    // ����
		String c_region = req.getParameter( "region" );                  // ȸ�簡 �ִ� ����
		String t_c_site = asc.convert( req.getParameter( "site" ) );     // �����̸�

		//tokenizing�� data�� ������ ����
		String name = "";
		String c_name = "";
		String c_site = "";

		//tokenizing �ؼ� space���ڸ� �����ش�.
		StringTokenizer st1 = new StringTokenizer(t_name);
		while(st1.hasMoreTokens())
			if(name == "") name = st1.nextToken();
			else name = name + st1.nextToken();

		StringTokenizer st2 = new StringTokenizer(t_c_name);
		while(st2.hasMoreTokens())  
			if(c_name == "") c_name = st2.nextToken();
			else c_name = c_name + st2.nextToken();

		StringTokenizer st3 = new StringTokenizer(t_c_site);
		while(st3.hasMoreTokens())
 			if(c_site == "") c_site = st3.nextToken();
			else c_site = c_site + st3.nextToken(); 

		// �Է��� Ȯ���Ѵ�.
		if( name.equals( "" ) && c_name.equals("" )  &&  kind.equals( "0" )  
				&&  c_region.equals( "0" )  &&  c_site.equals( "" )  ) {
			out.println( head );
			out.println( "<center><font size=3 color=white><br><br>" + uni.convert( "�Էµ� �����Ͱ� �����ϴ�. �Է��� ����� �ϰ� �õ��Ͻʽÿ�" ) + "<br><br>" );
			out.println("<a href=\"javaScript:window.history.go(-1);\"><font size=2><b><-back</a></center></font>");
			out.println( end );
			return;
		}	

		
		// �˻���� 
		int num = 0;             // �˻�����

		Vector dir_ids = new Vector();   //�� ������ �����ִ� dir id
		Vector result = new Vector();   //�˻��� card id
		Vector person_kd = new Vector();   //�˻��� card�� person kind

		boolean cont = true;     // �ߴܿ���
		
		// httpsession���� ����� ���̵� ��´�.
		String id = (String)httpsession.getValue( "id" );
	

		// ��� �����ֱ�
		out.println( "<html><head><title></title>");
		out.println("<script language = JavaScript>");
		out.println("function open_window(url){");
		out.println("card = window.open(url,\"card\",'toolbar=0,location=0,directories=0,status=0,menubar=0,scrollbars=0,resizable=0, width=500, height=500');");
		out.println("card.opener.name = \"opener\";");
		out.println("}");
		out.println("</script>");
 		//script end
		out.println("</head>");
		out.println( "<body background=/bg.jpg><br><p>");

		ResultSet result1 = null;
		ResultSet result2 = null;

		String card_id = null;
		String s_c_name = null;
		String s_part = null;
		String s_position = null;
		String dir_id = null;
		String person_kind = null;

		try {
			// ���丮�� ����ִ� ������ �˻��Ѵ�.
			result1 = dbmanager.searchMem_MyCard( uni.convert(id), uni.convert(name), uni.convert(c_name), uni.convert(c_site),  c_region ,  kind  ); 		
		result2 = dbmanager.searchNonmem_MyCard( uni.convert(id), uni.convert(name), uni.convert(c_name),  c_region ,  uni.convert(c_site),  kind  ); 	
			out.println("<table border=0 cellspacing=0 width=500>" );
			out.println("<tr><td colspan=3 align=center><img src=/icon.gif><font size=4 color=#ffff00><b>" + uni.convert( "�˻����" ) + "<br><p><br><p></td></tr>");
			out.println( "<tr><td width=20% align=center><font size=2 color=#fffacd><b>" + uni.convert( "�̸�" ) + "<br><p></td>");
			out.println( "<td width=30% align=center><font size=2 color=#fffacd><b>" + uni.convert( "ȸ���̸�" ) + "<br><p></td>");
			out.println( "<td width=25% align=center><font size=2 color=#fffacd><b>" + uni.convert( "�μ�" ) + "<br><p></td>" );
			out.println( "<td width=25% align=center><font size=2 color=#fffacd><b>" + uni.convert( "��å" ) + "<br><p></td></tr>");		

			ResultSet searchdata = null;
			
			//�˻��� ����� ���� ����Ʈ�� �����ش�.				
			while( result1.next() ){
				num++;
				card_id = result1.getString( 1 );
				searchdata = dbmanager.getMemberInfo( card_id );
				searchdata.next();
				name = searchdata.getString( 3 );
				s_c_name = searchdata.getString( 17 );
				s_part = searchdata.getString( 14 );
				s_position = searchdata.getString( 13 );
				dir_id = result1.getString( 2 );
				person_kind = "m";

				// ������ �����ش�.		
				out.println( "<tr><td width=20% align=center><a href=\"javascript:open_window('/namecard/UserServlet?cmd=showSearchCard&search_kind=private&dir_id="+dir_id+"&person_kind=" + person_kind + "&card_id="+card_id+ "')\"><font size=2>" + name + "</a></td>");
				out.println( "<td width=30% align=center><font size=2>" + s_c_name+ "</td>");
				out.println( "<td width=25% align=center><font size=2>" + s_part +"</td>" );
				out.println( "<td width=25% align=center><font size=2>" + s_position + "</td></tr>");
			}
			
			//�˻��� �͸���� ���� ����Ʈ�� �����ش�.				
			while( result2.next() ){
				num++;
				card_id = result2.getString( 1 );
				searchdata = dbmanager.getNonMemberInfo( card_id );
				searchdata.next();
				name = searchdata.getString( 2 );
				s_c_name = searchdata.getString( 3 );
				s_part = searchdata.getString( 4 );
				s_position = searchdata.getString( 5 );
				dir_id = result2.getString( 2);
				person_kind = result2.getString ( 3 );

				// ������ �����ش�.		
				out.println( "<tr><td width=20% align=center><a href=\"javascript:open_window('/namecard/UserServlet?cmd=showSearchCard&search_kind=private&dir_id="+dir_id+"&person_kind=" + person_kind + "&card_id="+card_id+ "')\"><font size=2>" + name + "</a></td>");
				out.println( "<td width=30% align=center><font size=2>" + s_c_name + "</td>");
				out.println( "<td width=25% align=center><font size=2>" + s_part +"</td>" );
				out.println( "<td width=25% align=center><font size=2>" + s_position + "</td></tr>");
			}	
					
	
			
			//int cardnum = searchCardList( result1, out );	 
			//num = num + cardnum;
		} catch( Exception e ) {
				out.println( head );
				out.println( "fail to myPersonSearch" + e.toString() );
				out.println( end );
				return;
		}

		out.println("<tr><td colspan=3 align=center><br><p><font size=2 color=red>"+num+"</font><font size=2 color=#e0ffff>" + uni.convert( "���� ����� ������ϴ�." ) + "</td></tr>");
		out.println("<tr><td colspan=3 align=center>");
		out.println("<br><p><a href=\"javaScript:window.history.go(-1);\"><font size=2><b><-Back</a><br><p></td></tr>");
		out.println("</table></body></html>");
	}

	/**
	 *  publicSearch - publicSearch command ó���ϴ� method
	 *				  �Էµ� �˻���� ������ ã�´�.
	 *
	 *  @param req  �Էµ� �����Ͱ� ����ִ� ��
	 *  @param out  ����� ����� ��
	 *
	 */
	public void publicSearch( HttpServletRequest req, PrintWriter out ) throws UnsupportedEncodingException  {
		// ���� �˻�
		HttpSession httpsession = req.getSession( false );
		if( httpsession == null ) {
			out.println( head );
			out.println( "<h1>Unauthorized User! </h1>" );
			out.println( end );
			return;
		}
		
		String search_kind = asc.convert( req.getParameter( "search_kind" ));
		// req���� �Ķ���� �Ľ��Ѵ�.
		String t_name = asc.convert( req.getParameter( "name" ) );      // �̸�
		String t_c_name = asc.convert(req.getParameter( "c_name" ) );    // ȸ���̸�
		String kind   = req.getParameter( "kind" );    // ����
		String c_region = req.getParameter( "region" );   // ȸ�簡 �ִ� ����
		String t_c_site = asc.convert( req.getParameter( "site" ) );      // �����̸�
		//tokenizing�� data�� ������ ����
		String name = "";
		String c_name = "";
		String c_site = "";
		String card_id = "";
		String part = "";
		String position = "";

		ResultSet result = null;
		int num = 0;

		//tokenizing �ؼ� space���ڸ� �����ش�.
		StringTokenizer st1 = new StringTokenizer(t_name);
		while(st1.hasMoreTokens())
			if(name == "") name = st1.nextToken();
			else name = name + st1.nextToken();

		StringTokenizer st2 = new StringTokenizer(t_c_name);
		while(st2.hasMoreTokens())  
			if(c_name == "") c_name = st2.nextToken();
			else c_name = c_name + st2.nextToken();

		StringTokenizer st3 = new StringTokenizer(t_c_site);
		while(st3.hasMoreTokens())
 			if(c_site == "") c_site = st3.nextToken();
			else c_site = c_site + st3.nextToken(); 
		if( c_site == "" ) c_site = "";

		// �Է��� Ȯ���Ѵ�.

		if( name.equals( "" )  &&  c_name.equals( "" ) &&  kind.equals( "0" )
			&&  c_region.equals( "0" ) &&  c_site.equals( "" ) ) {
			out.println( head );
			out.println( "<center><font size=3 color=white><br><br>" + uni.convert( "�Էµ� �˻�� �����ϴ�. �˻�� �Է��Ͻð� �õ��Ͻʽÿ�" ) + "<br><br>" );
			out.println( "<a href=\"javaScript:window.history.go(-1);\"><font size =2><b><-Back</a></font></center>");
			out.println( end );
			return;
		}

		try {
			result = dbmanager.searchPublicCard( uni.convert(name), uni.convert(c_name), c_region, uni.convert(c_site), kind ); 
		} catch( Exception e ) {
			out.println( head );
			out.println( "fail to publicSearch" + e.toString() );	
			out.println( end );
			return;
		}

		// �˻���� list�� �����ش�.
		out.println( "<html><head><title></title>");
		out.println("<script language = JavaScript>");
		out.println("function open_window(url){");
		out.println("card = window.open(url,\"card\",'toolbar=0,location=0,directories=0,status=0,menubar=0,scrollbars=0,resizable=0, width=500, height=500');");
		out.println("card.opener.name = \"opener\";");
		out.println("}");
		out.println("</script>");
 		//script end
		out.println("</head>");
		out.println( "<body background=/bg.jpg>" );
	
		out.println("<table width=500 cellspacing=0 border=0>");
		out.println("<tr><td colspan=3 align=center><img src=/icon.gif><font size=4 color=#ffff00><b>" + uni.convert( "�˻����" ) + "<br><p><br><p></td></tr>");	
		out.println( "<tr><td align=center width=20%><font size=2 color=#fffacd><b>" + uni.convert( "�̸�" ) + "<br><p></td>");
		out.println( "<td align=center width=30%><font size=2 color=#fffacd><b>" + uni.convert( "ȸ���̸�" ) + "<br><p></td>");
		out.println( "<td align=center width=25%><font size=2 color=#fffacd><b>" + uni.convert( "�μ�" ) + "<br><p></td>" );
		out.println( "<td align=center width=25%><font size=2 color=#fffacd><b>" + uni.convert( "��å" ) + "<br><p></td></tr>");

		try {
			ResultSet member = null; 

			while( result.next() ){
				num++;
				// �˻��� ����� �ϳ��� �����´�.
				member = dbmanager.getMemberInfo( result.getString( 1 ) );
				if( member.next() ) { 
					card_id = member.getString( 1 );
					name = member.getString( 3 );
					c_name = member.getString( 17 );
					part = member.getString( 14 );
					position = member.getString( 13 );
			
					// ������ �����ش�.		
					out.println( "<tr><td width=20% align=center><a href=\"javascript:open_window('/namecard/UserServlet?cmd=showSearchCard&search_kind=" + search_kind+ "&person_kind=m&dir_id=100000&card_id=" + card_id+"')\"><font size=2>" + name + "</a></td>");
					out.println( "<td width=30% align=center><font size=2>" + c_name + "</td>");
					out.println( "<td width=25% align=center><font size=2>" + part +"</td>" );
					out.println( "<td width=25% align=center><font size=2>" + position + "</td></tr>");
				}
			}

		} catch( Exception e ) {
			out.println( head );
			out.println( "fail to publicSearch" + card_id +num + e.toString() );
			out.println( end );
			return;
		}
		
		out.println("<tr><td colspan=3 align=center><br><p><br><p>");
		out.println( "<font size=2 color=red><b>" + num + "</font><font size=2 color=#ef0000>" + uni.convert( "���� ����� ������ϴ�." ) + "</font></td></tr>" );
		out.println("<tr><td colspan=3 align=center><br>");
		out.println("<a href=\"javaScript:window.history.go(-1);\"><font size=2><b><-Back</a><br><p></td></tr>");
		out.println("</table></body></html>");
	}

	/**
	 *	search - ����Ʈ ��ü �˻��� ���θ����� �˻��� ó���Ѵ�.
	 *
	 *  @param req �Էµ� �����Ͱ� ����ִ� �� 
	 *  @param out ����� ����� ��
	 *
	 **/	
	public void search(HttpServletRequest req, PrintWriter out)throws UnsupportedEncodingException, IOException {

		HttpSession httpsession = req.getSession( false );
		// search kind	
		String search_kind = req.getParameter("search_kind");
		if( !search_kind.equals( "out" ) ) 
			if( httpsession == null ) {
				out.println( head );
				out.println( "<h1>Unauthorized User! </h1>" );
				out.println( end );
				return;
			}

		//req���� �Ķ���� �޾ƿͼ� nullüũ
		String s_name        = asc.convert(req.getParameter("name"));
		String s_c_name      = asc.convert(req.getParameter("c_name"));
		String kind        = req.getParameter("kind");
		String region      = req.getParameter("region");
		String s_c_site      = asc.convert(req.getParameter("site"));
			
		String name= "";
		String c_name = "";
		String c_site = "";

		//tokenizing �ؼ� space���ڸ� �����ش�.
		StringTokenizer st = new StringTokenizer(s_name);
		while(st.hasMoreTokens())
			if(name == "") name = st.nextToken();
			else name = name + st.nextToken();

		st = new StringTokenizer(s_c_name);
		while(st.hasMoreTokens())
			if(c_name == "") c_name = st.nextToken();
			else c_name = c_name + st.nextToken();

		st = new StringTokenizer(s_c_site);
		while(st.hasMoreTokens())
			if(c_site == "") c_site = st.nextToken();
			else c_site = c_site + st.nextToken();

		if((name.equals( "" ) ) && (c_name.equals( "" ) ) && kind.equals( "0" ) && 
			region.equals( "0" ) && c_site.equals( "" ) ) {
			out.println( head );
			out.println( uni.convert( "<center><br><br><font size=2 color=white>�Էµ� �˻�� �����ϴ�. �˻�� �ùٸ��� �Է��Ͻð� �ٽ� �õ��ϼ���.<br><br>") );
			out.println( "<a href=\"javaScript:window.history.go(-1);\"><font size =2><b><-Back</a></font></center>");
			out.println( end );
			return;
		}
 
		String id = (String)httpsession.getValue("id");	

		
	
		// cardbox manager�� method�� ȣ���Ѵ�.
		try{
			if(search_kind.equals("private")) myPersonSearch(req, out); 
			else if(search_kind.equals("public")) publicSearch(req, out);
			else if(search_kind.equals("out"))publicSearch(req, out);
			else{ 
				out.println( head );
				out.println( uni.convert( "seasrch_kind�� �߸��Ǿ���") );
				out.println( end );
			}
		}catch(Exception e){
			out.println( head );
			//out.println( "fail to search in search" + e.toString() );
			out.println( uni.convert( "<br><br><br><font size=3 color=white>�˻��� ������ �߻��߽��ϴ�. �ٽ� �õ��Ͻʽÿ�. <br><br>" ) );
			out.println( "<a href=\"javaScript:window.history.go(-1);\"><font size =2><b><-Back</a></font></center>");
			out.println( end );
		}
		
	 }
	
	/**
	 * searchCardList
	 */
	public int searchCardList( ResultSet result, PrintWriter out ) {
		ResultSet searchdata = null; 
		String card_id = null;
		String name = null;
		String s_c_name = null;
		String s_part = null;
		String s_position = null;
		String dir_id = null;
		String person_kind = null;
		int num = 0;
 
		//�˻��� �͸���� ���� ����Ʈ�� �����ش�.				
		try {
			while( result.next() ){
				num++;
				searchdata = dbmanager.getNonMemberInfo( result.getString( 1 ) );
				searchdata.next();
				card_id = result.getString( 1 );
				name = searchdata.getString( 2 );
				s_c_name = searchdata.getString( 3 );
				s_part = searchdata.getString( 4 );
				s_position = searchdata.getString( 5 );
				dir_id = result.getString( 2 );
				person_kind = result.getString ( 3 );

				// ������ �����ش�.		
				out.println( "<tr><td width=20% align=center><a href=\"javascript:open_window('/namecard/UserServlet?cmd=showSearchCard&search_kind=private&dir_id="+dir_id+"&person_kind=" + person_kind + "&card_id="+card_id+ "')\"><font size=2>" + name + "</a></td>");
				out.println( "<td width=30% align=center><font size=2>" + s_c_name + "</td>");
				out.println( "<td width=25% align=center><font size=2>" + s_part +"</td>" );
				out.println( "<td width=25% align=center><font size=2>" + s_position + "</td></tr>");
			}	

		} catch( SQLException e ) {
			out.println( "fail to searchCardList in sql! " + e.toString() );
		} catch(CommandException e ) {
			out.println( "fail to searchCardList !" + e.toString() );
		}
		return num;
	}

	
	/**
	 * selectCardColorForm - ������ ����� �ٲٴ� ���� �����ϴ� �Լ� 
	 *
	 * @param req	������ �Ķ���� �о�´� 
	 * @param out	��� Writer
	 */
	 public void selectCardColorForm( HttpServletRequest req, PrintWriter out )throws UnsupportedEncodingException{
		HttpSession httpsession = req.getSession( false );
		if( httpsession == null ) {
			out.println( head );
			out.println( "<h1>Unauthorized User! </h1>" );
			out.println( end );
			return;
		}


		out.println( "<html><head><title></title>" );
		out.println("<script language=JavaScript>");
		out.println("card = window.open(\"/cardSystem/colorTable.html\",\"card\",'toolbar=0,location=0,directories=0,status=0,menubar=0,scrollbars=0,resizable=0,width=500,height=700');");
		//out.println("card.opener.name = \"opener\";");
		//out.println("}");
		out.println("</script>");
		out.println( "</head>" );
		out.println( "<body background=/bg.jpg>" );
		out.println( "<center><br><p><br><p><img src=/icon.gif><font size=3 color=#ffff00><b>" + uni.convert( "���Թ��" ) + "</b>" );
		out.println( "<br><p><font size=2 color=#e6e6fa>" + uni.convert( "*���ϴ� ���Թ����� �����ϼ���." ) );
		
		out.println( "<table border=0 width=500 cellspacing=0>" );
		out.println( "<form method=post action=/namecard/UploadServlet enctype=multipart/form-data>" );
		out.println( "<input type=hidden name=cmd value=preview>" );
		out.println( "<tr><td colspan=4 align=center><font size=2 color=#e6e6fa>" + uni.convert( "���ϴ� ������ ���ڻ��� �Է��Ͻð� �̸����⸦ ��������." )+ "<br>" + uni.convert( "ȸ��ΰ� �����ø� �ΰ� ����Ͻ��� �ʾƵ� �˴ϴ�." ) + "<p></td></tr>" );
		out.println( "<tr><td align=center><font size=2 color=#fffacd><b>" + uni.convert( "���Թ���" ) + "</td>" );
		out.println( "<td><input type=text name=background size=10></td>" );  
		out.println( "<td align=center><font size=2 color=#fffacd><b>" + uni.convert( "���Ա��ڻ�" ) + "</td>");
		out.println( "<td><input type=text name=font size=10></td></tr>" );
		out.println( "<tr><td align=center><font size=2 color=#fffacd><b>" + uni.convert( "ȸ��ΰ�" ) + "</td>" );
		out.println( "<td colspan=3><input type=file name=logo size=25></td></tr>" );
		out.println( "</table><br><p>" );
		out.println( uni.convert("����� �ٲ� ������ ������ �������ּ���<br>" ) );
		out.println( "<table><tr><td align=right>" );
		out.println( "<input type=radio name=kind checked value=m><font size=2 color=#fffacd><b>" + uni.convert( "������" ) +"</td>" );
		out.println( "<td align=left><input type=radio name=kind value=c><font size=2 color=#fffacd><b>" + uni.convert( "���� �� ����" ) + "</td></tr>" );
		out.println( "<tr><td align=right><br><p><input type=submit value=\"" + uni.convert( "�̸�����" ) + "\" ></td>" );
		out.println( "<td align=left><br><p>" );
		out.println( "<input type=submit value=\"" + uni.convert( "���" ) + "\" onClick=\"javaScript:window.history.go(-1);\"></td></tr>" );
		out.println( "</form></talbe></center></body></html>" );
	}

	/**
	 *	preview - ������ ���Թ����� ���Ա��ڻ��� ����� ���Ի����� �����ش�.
	 *		
	 *	@param 	req		�Ķ���� �޾ƿ�
	 *	@pramr	out		��� Wirter 
	 */
	 public void preview( HttpServletRequest req, PrintWriter out ) throws UnsupportedEncodingException{
		HttpSession httpsession = req.getSession( false );
		if( httpsession == null ) {
			out.println( head );
			out.println( "<h1>Unauthorized User! </h1>" );
			out.println( end );
			return;
		}

		String id = (String)httpsession.getValue( "id" );
		String dir_id = (String)httpsession.getValue( "current_dir" );

		String bgcolor = "";
		String font = "";
		String logo = "";
		String kind = "";

		FileUpload fileup = null;

		try {
			fileup = new FileUpload( req.getInputStream() );

			// color  �ֱ� 
			//dbmanager.insertColor();

			bgcolor = fileup.getParameter( "background" );
			font = fileup.getParameter( "font" );
		} catch( CommandException e ) {
			out.println( head );
			out.println( uni.convert( "<center><br><br><br><font size=3 color=white>���� ���ε忡 ������ �߻��߽��ϴ�. �ٽ� �õ��� �ֽʽÿ�." ) );
			//out.println( "fail to create FileUpload " + e.toString() );
			out.println( "<br><br>" );
			out.println( "<a href=\"javaScript:window.history.go(-1);\"><font size =2><b><-Back</a></font></center>");	
			out.println( end );
			return;
		} catch( Exception e ) {
			out.println(  "fail to create FileUpload " + e.toString() );
			return;
		}

		// �Է��� Ȯ���Ѵ�
		try{
			if( bgcolor.equals( "" ) && font.equals( "" ) && logo.equals( "" )) {
				out.println( head );
				out.println( "<center><br><br><font size=3 color=white>" );
				out.println( uni.convert( "�Էµ� �÷��� �̹����� �����ϴ�.<br> �̹��������̳� �÷���ȣ�� �������ּ���<br><br>" ) );
				out.println( "<a href=\"javaScript:window.history.go(-1);\"><-Back</a>" );
				out.println( "</font></center>" );
				out.println( end );
				return;
			}

			int bgnum = 0;
			int fontnum = 0;

			if( !bgcolor.equals( "" ) ) bgnum = Integer.parseInt( bgcolor );
			if( !font.equals( "" ) ) fontnum = Integer.parseInt( font );
	
			if(  ( bgnum < 1 ) || (bgnum > 140 ) || ( fontnum < 0 ) || ( fontnum > 140 ) ){
				out.println( head );
				out.println( "<center><br><br><font size=3 color=white>" );
				out.println( uni.convert( "�Է��ڷᰡ Ʋ�Ƚ��ϴ�. 1���� 140������ �����߿� �Է��� �ֽʽÿ�.<br><br>" ) );
				out.println( "<a href=\"javaScript:window.history.go(-1);\"><-Back</a>" );
				out.println( "</font></center>" );
				out.println( end );
				return;
			}
			
			// �̹��� �������� Ȯ���� Ȯ�� 
			/*if( !bgimage.endsWith( "gif" ) || !bgimage.endsWith( "jpg" ) ) {
				out.println( head );
				out.println( "<center><br><br><font size=3 color=white>" );
				out.println( uni.convert( "���� ������ ���� �ʽ��ϴ�. �ٽ� �Է��ϼ���<br><br>" ));
				out.println( "<a href=\"javaScript:window.history.go(-1);\"><-Back</a>" );
				out.println( "</font></center>" );
				out.println( end ); 
			}*/ 
			 
		}catch( Exception e ){
			//out.println( "fail to preview at boxmanager" + e.toString() );
		}

		// DB���� �÷���ȣ�� ��´�.
		try {
			if( !bgcolor.equals( "" ) ) bgcolor = dbmanager.getColor( bgcolor );
			if( !font.equals( "" ) ) font = dbmanager.getColor( font );
		} catch( CommandException e ) {
			out.println( head );
			out.println( uni.convert("<center><br><br><font size=3 color=white>�÷� ��Ⱑ �����߽��ϴ�" + e.toString() ) );
			out.println( "<br><br><a href=\"javaScript:window.history.go(-1);\"><-Back</a>" );
			out.println( "</center></font>" );
			out.println( end );
			return;
		}

		String fullname = "";
		String logopath= "";

		// bgimage file copy
		try {
			String s_logo = asc.convert(fileup.getFileName());

			//tokenizing �ؼ� space���ڸ� �����ش�.
			StringTokenizer st = new StringTokenizer(s_logo);
			while(st.hasMoreTokens())
				if(logo == "") logo = st.nextToken();
				else logo = logo + st.nextToken();
	
			fullname = PATH + uni.convert(logo);

			if( !logo.equals( "" ) ) {
				File file = new File( fullname );
				
				if( !file.exists() ){
					file.createNewFile(); 
				} else {
					logo = uni.convert(id) + uni.convert( logo );
					fullname= PATH +  logo ;
					file = new File( fullname );
					file.createNewFile();
				} 
				logopath = image + uni.convert( logo );
				FileOutputStream outFile = new FileOutputStream( file );
				fileup.upFile( outFile );

				outFile.close();
			} 
			kind = fileup.getParameter( "kind" );
		} catch( CommandException e ) {
			out.println( head );
			out.println( uni.convert("<center><br><br><font size=3 color=white>���Ͼ�Ⱑ �����߽��ϴ�." + e.toString() ) );
			out.println( "<br><br><a href=\"javaScript:window.history.go(-1);\"><-Back</a>" );
			out.println( "</center></font>" );
			out.println( end );
			return;
		} catch( IOException e ) {
			out.println( head );
			out.println( uni.convert("<center><br><br><font size=3 color=white>���Ͼ�Ⱑ �����߽��ϴ�." + e.toString() ) );
			out.println( "<br><br><a href=\"javaScript:window.history.go(-1);\"><-Back</a>" );
			out.println( "</center></font>" );
			out.println( end );
			return;

		} catch( Exception e ) {
			out.println( head );
			out.println( uni.convert("<center><br><br><font size=3 color=white>���Ͼ�Ⱑ �����߽��ϴ�." + e.toString() ) );
			out.println( "<br><br><a href=\"javaScript:window.history.go(-1);\"><-Back</a>" );
			out.println( "</center></font>" );
			out.println( end );
			return;
		}

		out.println( "<html><head><title></title></head>" );
		out.println( "<body background=/bg.jpg>" );
		out.println( "<br><p><center><img src=/icon.gif><font size=3 color=#ffff00><b>" + uni.convert( "���Ի���" ) );
		out.println( "<br><p><br><p><table border=2 bordercolor=#ffffff width=450 height=250 cellspacing=0>" );
		out.println( "<tr><td bgcolor=#" + bgcolor + " align=center valign=middle>" );
		out.println( "<table border=0 cellspacing=0 width=90%><tr>" );
		
		if( logo.equals("") ){
			//���õ� ������ ���� �����ֱ�
			out.println( "<td colspan=4 align=center><font color=#" + font + "><b>" + uni.convert( "���̼���Ʈ(����)" ) + "<br><p></td></tr>" );
			out.println( "<tr><td colspan=2><font size=3 color=#" + font + "><b>" + uni.convert( "��" ) + "</td>" );
			out.println( "<td><font size=2 color=#" + font + "><b>H.P.</td><td><font size=2 color=#" + font + ">011-985-0355</td></tr>" );
			out.println( "<tr><td colspan=2><font size=2 color=#" + font + ">" + uni.convert( "������ ����" ) + "</td>" );
			out.println( "<td><font size=2 color=#" + font + "><b>Beeper</td><td><font size=2 color=#" + font + ">012-985-0355</td></tr>" );
			out.println( "<tr><td colspan=2><font size=2 color=#" + font + ">" + uni.convert( "�뱸������ ���� �˻絿 û����� 5��" ) + "</td>" );
			out.println( "<td><font size=2 color=#" + font + "><b>Home Tel</td><td><font size=2 color=#" + font + ">053-985-0355</td></tr>" );
			out.println( "<tr><td><font size=2 color=#" + font + "><b>Com Tel</td><td><font size=2 color=#" + font + ">053-985-1111</td>" );
			out.println( "<td></td>" );
			out.println( "<tr><td><font size=2 color=#" + font + "><b>Fax</td><td><font size=2 color=#" + font + ">053-986-1235</td><td></td></tr>" );
			
		}else{
			out.println( "<td colspan=3 align=center><font color=#" + font + "><b>" + uni.convert( "���̼���Ʈ(����)" ) + "<br><p></td></tr>" );
			out.println( "<tr><td rowspan=8 width=40% align=center valign=middle><img src=" + logopath + "></td>" );
			out.println( "<td colspan=2><font size=3 color=#" + font + "><b>" + uni.convert( "��" ) + "</td></tr>" );
			out.println( "<tr><td colspan=2><font size=2 color=#" + font + ">" + uni.convert( "������ ����" ) + "</td></tr>" );
			out.println( "<tr><td colspan=2 height=30><font size=2 color=#" + font + ">" + uni.convert( "�뱸������ ���� �˻絿 û����� 5��" ) + "</td></tr>" );
			out.println( "<tr><td><font size=2 color=#" + font + "><b>Com Tel</td>" );
			out.println( "<td><font size=2 color=#" + font + ">053-985-0355</td></tr>" );
			out.println( "<tr><td><font size=2 color=#" + font + "><b>Fax</td>" );
			out.println( "<td><font size=2 color=#" + font + ">053-986-1235</td></tr>" );
			out.println( "<tr><td><font size=2 color=#" + font + "><b>H.P.</td>" );
			out.println( "<td><font size=2 color=#" + font + ">011-985-0355</td></tr>" );
			out.println( "<tr><td><font size=2 color=#" + font + "><b>Beeper</td>" );
			out.println( "<td><font size=2 color=#" + font + ">012-985-0355</td></tr>" );
			out.println( "<tr><td><font size=2 color=#" + font + "><b>Home Tel</td>" );
			out.println( "<td><font size=2 color=#" + font + ">053-985-1111</td></tr>" );
		}

		out.println( "</table></td></tr></table><br><p><br><p>" );
		out.println( "<form method=post action=/namecard/UserServlet>" );
		out.println( "<input type=hidden name=cmd value=selectCardColor>" );
		out.println( "<input type=hidden name=bgcolor value=\"" + bgcolor + "\">" );
		out.println( "<input type=hidden name=font value=\"" + font + "\">" );
		out.println( "<input type=hidden name=logo value=\"" + uni.convert(logo) + "\">" );
		out.println( "<input type=hidden name=kind value=\"" + kind + "\">" );
		out.println( "<input type=hidden name=dir_id value=" + dir_id + ">" );
		out.println( "<input type=submit value=\"" + uni.convert( "����" ) + "\">");
		out.println( "</form><br><p><br><p>" );

		out.println( "<table border=0 width=500 cellspacing=0>" );
		out.println( "<form method=post action=/namecard/UploadServlet enctype=multipart/form-data>" );
		out.println( "<input type=hidden name=cmd value=preview>" );
		out.println( "<tr><td colspan=4 align=center><font size=2 color=#e6e6fa>" + uni.convert( "�ٽ� �����ϰ� �����ø� ���ϴ� ������ ���ڻ��� �Է��ϼ���." )+ "<br><p></td></tr>" );
		out.println( "<tr><td align=center><font size=2 color=#fffacd><b>" + uni.convert( "���Թ���" ) + "</td>" );
		out.println( "<td><input type=text name=background size=10></td>" );  
		out.println( "<td align=center><font size=2 color=#fffacd><b>" + uni.convert( "���Ա��ڻ�" ) + "</td>");
		out.println( "<td><input type=text name=font size=10></td></tr>" );
		out.println( "<tr><td align=center><font size=2 color=#fffacd><b>" + uni.convert( "ȸ��ΰ�" ) + "</td>" );
		out.println( "<td colspan=3><input type=file name=logo size=25></td></tr>" );
		out.println( "</table><br><p>" );
		out.println( uni.convert("����� �ٲ� ������ ������ �������ּ���<br>" ) );
		out.println( "<table><tr><td align=right>" );
		out.println( "<input type=radio name=kind checked value=m><font size=2 color=#fffacd><b>" + uni.convert( "������" ) +"</td>" );
		out.println( "<td align=left><input type=radio name=kind value=c><font size=2 color=#fffacd><b>" + uni.convert( "���� �� ����" ) + "</td></tr>" );
		out.println( "<tr><td align=right><br><p><input type=submit value=\"" + uni.convert( "�̸�����" ) + "\" ></td>" );
		out.println( "<td align=left><br><p>" );
		out.println( "<input type=submit value=\"" + uni.convert( "���" ) + "\" onClick=\"javaScript:window.history.go(-2);\"></td></tr>" );
		out.println( "</form></talbe></center></body></html>" );
	}

	
	/**
	 *  selectCardColor  - ���Կ� �� �̹����� ������ ������ ��� �����Ѵ�.
	 *
	 *  @param  req  ������ �Է¹��� �����Ͱ� ����ִ� ��
	 *  @param  out  ����� ����� ��
	 */
	public void selectCardColor( HttpServletRequest req, PrintWriter out )  throws UnsupportedEncodingException {
		HttpSession httpsession = req.getSession( false );
		if( httpsession == null ) {
			out.println( head );
			out.println( "<h1>Unauthorized User! </h1>" );
			out.println( end );
			return;
		}	

		String id = (String)httpsession.getValue( "id" );
		//String dir_id = (String)httpsession.getValue( "current_dir" );
		
		String s_bgcolor = req.getParameter( "bgcolor" );
		String s_font = req.getParameter( "font" );
		String s_logo = req.getParameter( "logo" );
		String kind = req.getParameter( "kind" );

		String bgcolor="";
		String font = "";
		String logo = "";

		//tokenizing �ؼ� space���ڸ� �����ش�.
		StringTokenizer st = new StringTokenizer(s_bgcolor);
		while(st.hasMoreTokens())
			if(bgcolor == "") bgcolor = st.nextToken();
			else bgcolor = bgcolor + st.nextToken();

		st = new StringTokenizer(s_font);
		while(st.hasMoreTokens())
			if(font == "") font = st.nextToken();
			else font = font + st.nextToken();

		st = new StringTokenizer(s_logo);
		while(st.hasMoreTokens())
			if(logo == "") logo = st.nextToken();
			else logo = logo + st.nextToken();


		if( bgcolor.equals( "" ) && font.equals( "" ) && logo.equals( "" ) ) {
			out.println( head );
			out.println( "<br><br><br><font size=3 color=white>" );
			out.println( uni.convert( "������ ������ �����ϴ�. ���Ի�, ���ڻ�, �ΰ� �Է� �� �ٽ� �õ��ϼ���" ) );
			out.println( "<br><br><a href=\"javaScript:window.history.go(-1);\"><-Back</a>" );
			out.println( end );
			return;
		}


		try {
			if( kind.equals( "m" ) ){
				dbmanager.setMyCardColor( id, bgcolor, font, logo ); 
			}
			else dbmanager.setCardColor( id, bgcolor, font, logo);

			showCardList( req, out );
		} catch( CommandException e ) {
			out.println( head );
			out.println( "<center><br><br><font size=3 color=white>" );
			out.println( uni.convert( "���Ի��� �ٲٱⰡ �����Ͽ����ϴ�. �˼��մϴ�. �ٽ� �õ��ϼ���." ) + e.toString()  );
			out.println( "<br><br>" );
			out.println( "<a href=\"javaScript:window.history.go(-1);\"><-Back</a>" );	
			out.println( "</font></center>" );
			out.println( end );
			return;
		}
	}
	
}


	
