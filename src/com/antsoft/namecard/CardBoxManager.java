package com.antsoft.namecard;

import java.util.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

/**
 *	CardBoxManager - 디렉토리와 명함을 관리하는 클래스
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

/////////////////////////////// 명함 관리함  /////////////////////////////
	 
	/**
	 *  showDirList - 사용자에게 등록되어있는 디렉토리 리스트를 보여주는 method
	 *
	 *  @param  req      입력된 데이터가 들어있는 곳
	 *  @param  out      결과를 출력하는 곳
	 *
	 */
	public void showDirList( HttpServletRequest req, PrintWriter out )throws UnsupportedEncodingException {

		// 권한 검사
		HttpSession httpsession = req.getSession( false );
		if( httpsession == null ) {
			out.println( head );
			out.println( "<h1>Unauthorized User! </h1>" );
			out.println( end );
			return;
		}

		//httpsession에서 현재 사용자 아이디를 얻는다.
		String id = (String)httpsession.getValue("id");
		
		ResultSet dir_list1 = null;
		ResultSet dir_list2 = null;
		ResultSet dir_list3 = null;

		//사용자 디렉토리 리스트를 얻어온다.
		try{
			dir_list1 = dbmanager.getDirList( id );
			dir_list2 = dbmanager.getDirList( id );
			dir_list3 = dbmanager.getDirList( id );

			// 폼을 보여준다.
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
			out.println("<td align=center height=40><a href=/namecard/UserServlet?cmd=showDirList><font color=#FFFFFF size=2>" + uni.convert( "개인명함함" ) + "</font></a></td>");
			out.println("<td align=center><a href=/namecard/UserServlet?cmd=updatePersonForm><font color=#FFFFFF size=2>" + uni.convert( "개인정보수정" ) + "</font></a></td>");
			out.println("<td align=center><a href=/namecard/UserServlet?cmd=comInfoForm><font color=#FFFFFF size=2>" + uni.convert( "회사정보수정" ) + "</font></a></td>");
			out.println("<td align=center><a href=/namecard/UserServlet?cmd=jobChangeForm><font size=2 color=#FFFFFF>" + uni.convert( "회사이전" ) + "</font></a></td>");
			out.println("<td align=center><a href=/namecard/UserServlet?cmd=checkDeleteID><font size=2 color=#FFFFFF>" + uni.convert( "가입해지" ) + "</font></a></td>");
			out.println("<td align=center><a href=/namecard/UserServlet?cmd=logout><font size=2 color=#FFFFFF>" + uni.convert( "로그아웃" ) + "</font></a></td>");
			out.println("</tr>");
			out.println("</table>");
			out.println("</td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td height=500 valign=top><br><p>");
			out.println("<table border=0 width=90% align=center cellspacing=0>");
			out.println("<tr>"); 
			out.println("<td colspan=2><br><p><br><p>"); 
			out.println("<b><font color=#FFFF00 size=2>" + uni.convert( "명함검색" ) + "</font></b></td>");
			out.println("</tr>");
			out.println("<td colspan=2><img src=/cardSystem/line.gif></td></tr>");
			out.println("<tr><form method=post action=/namecard/UserServlet>");
			out.println("<input type=hidden name=cmd value=search>");
			out.println("<td colspan=2><br><p><font size=2 color=#e6e6fa>" + uni.convert( "명함사이트에 등록이 되어있는" ) );
			out.println( "<br>" + uni.convert( " 사람들을 검색할수 있습니다<br>개인 명함함의 명함검색은 private를<br>사이트 전체의 검색은 public을 누르세요" ) + "</font></td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td><font size=2 color=#fffacd><b>" + uni.convert( "이름" ) + "</font></td>");
			out.println("<td><input type=text name=name size=10 ></td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td><font color=#fffacd size=2><b>" + uni.convert( "회사이름" ) + "</font></td>");
			out.println("<td><input type=text name=c_name size=16></td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td><font color=#fffacd size=2><b>" + uni.convert( "지점" ) + "</font></td>");
			out.println("<td><input type=text name=site size=10></td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td><font color=#fffacd size=2><b>" + uni.convert( "업종" ) + "</font></td>");
			out.println("<td><select name=kind size=1>");
			out.println("<option value=0>" + uni.convert( "선택사항없음" ) + "</option>");
			out.println("<option value=1>" + uni.convert( "공무원" ) + "</option><option value=2>" + uni.convert( "교원" ) + "</option>");
			out.println("<option value=3>" + uni.convert( "의료" ) + "</option><option value=4>" + uni.convert( "유통" ) + "</option>");
			out.println("<option value=5>" + uni.convert( "기계/자동차" ) + "</option><option value=6>" + uni.convert( "컴퓨터/통신" ) + "</option>");
			out.println("<option value=7>" + uni.convert( "서비스업" ) + "</option><option value=8>" + uni.convert( "금융" ) + "</option>");
			out.println("<option value=9>" + uni.convert( "건축/인테리어" ) + "</option><option value=10>" + uni.convert( "방송" ) + "</option>");
			out.println("<option value=11>" + uni.convert( "예술" ) + "</option><option value=12>" + uni.convert( "무역" ) + "</option>");
			out.println("<option value=13>" + uni.convert( "스포츠" ) + "</option><option value=14>" + uni.convert( "프리랜서" ) + "</option>");
			out.println("<option value=15>" + uni.convert( "기타" ) + "</option></select></td>");
			out.println("</tr>");
			out.println("<tr><td><font size=2 color=#fffacd><b>" + uni.convert( "지역" ) + "</font></td>");
			out.println("<td><select name=region size=1>");
			out.println("<option value=0>" + uni.convert( "선택사항없음" ) + "</option>");
			out.println("<option value=1>" + uni.convert( "서울특별시" ) + "</option><option value=2>" + uni.convert( "부산광역시" ) + "</option>");
			out.println("<option value=3>" + uni.convert( "대구광역시" ) + "</option><option value=4>" + uni.convert( "인천광역시" ) + "</option>");
			out.println("<option value=5>" + uni.convert( "광주광역시" ) + "</option><option value=6>" + uni.convert( "울산광역시" ) + "</option>");
			out.println("<option value=7>" + uni.convert( "대전광역시" ) + "</option><option value=8>" + uni.convert( "경기" ) + "</option>");
			out.println("<option value=9>" + uni.convert( "강원" ) + "</option><option value=10>" + uni.convert( "충북" ) + "</option>");
			out.println("<option value=11>" + uni.convert( "충남" ) + "</option><option value=12>" + uni.convert( "전북" ) + "</option>");
			out.println("<option value=13>" + uni.convert( "전남" ) + "</option><option value=14>" + uni.convert( "경북" ) + "</option>");
			out.println("<option value=15>" + uni.convert( "경남" ) + "</option><option value=16>" + uni.convert( "제주" ) + "</option>");
			out.println("</select></td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td align=right><br><p><input type=radio name=search_kind value=private><font size=2 color=#fffacd>private</td>");
			out.println("<td align=left><br><p><input type=radio name=search_kind checked value=public><font size=2 color=#fffacd>public</td></tr>");
			out.println("<tr><td colspan=2 align=center><input type=submit value=" + uni.convert( "검색" ) + "></td>");
			out.println("</form></tr>");
			out.println("</table>");
			out.println("</td>");
			out.println("<td height=500 width=300 valign=top><br><p><br><p><br>");
			out.println("<table border=0 width=90%>");
			out.println("<tr>");
			out.println("<td align=center width=50% bgcolor=#800080><font color=#ffffff size=2><b>" + uni.convert( "디렉토리 이름" ) + "</td>");
			out.println("<td align=center width=50% bgcolor=#800080><font color=#ffffff size=2><b>" + uni.convert( "명함수" ) + "</td>");
			out.println("</tr>");
			out.println("<tr><td><br><p></td></tr>");
			
			while( dir_list1.next() ){
				String dir_id = dir_list1.getString( "id" );
				String dir_name =  dir_list1.getString( "name" );
				int num = dir_list1.getInt( "number" ); 
	
				// hidden으로 index number를 넘겨준다.
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
		out.println("<td colspan=2><font size=2 color=#ffff00><br><p><b><br><p><br><p>" + uni.convert( " 명함관리메뉴") + "</font></td>");
		out.println("</tr>");
		out.println("<tr>");
		out.println("<td colspan=2><img src=/cardSystem/line.gif></td>");
		out.println("</tr>");
		out.println("<tr>");
		out.println("<td colspan=2><form method=post action=/namecard/UserServlet><input type=hidden name=cmd value=addDir>");
		out.println("<br><p><font size=2 color=#fffacd><b>" + uni.convert( "디렉토리 추가" ) + "</td></tr>");
		out.println("<tr><td><input type=text name=dirName size=10></td>");
		out.println("<td><input type=submit value=add></td>");
		out.println("</form></tr>");
		out.println("<tr>");
		out.println("<td colspan=2><form method=post action=/namecard/UserServlet><input type=hidden name=cmd value=deleteDir>");
		out.println("<br><p><font size=2 color=#fffacd><b>" + uni.convert( "디렉토리 삭제" ) + "</font></td>"); 
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
		out.println("<br><p><font size=2 color=#fffacd><b>" + uni.convert( "이름바꾸기" ) + "</font></td>"); 
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
	 *  showCardList - 선택된 디렉토리 내의 명함들의 리스트를 보여주는 method
	 *
	 *  @param  req         입력된 데이터가 들어있는 곳
	 *  @param  out         결과를 출력할 곳
	 * 
	 */
	public void showCardList( HttpServletRequest req, PrintWriter out )throws UnsupportedEncodingException{
		// session을 얻어온다.
		HttpSession httpsession = req.getSession( false );
		
		// session이 없으면 등록이 제대로 되지 않았다는 말이므로 error 처리한다.
		if( httpsession == null) {
			out.println( head );
			out.println( "<h1>Error !!! </h1>" );
			out.println( end );
			return;
		}
		
		String id = (String)httpsession.getValue( "id" );
		String dir_id = req.getParameter("dir_id");
	
		// session에 현재 디렉토리를 넣어둔다.
		httpsession.putValue( "current_dir",  dir_id );
		
		ResultSet card_list = null;
		ResultSet dir = null;
		String dir_name = null;

		try{
			//card list를 얻는다
			card_list = dbmanager.getCardList( dir_id );
			//directory name을 얻는다.
			dir = dbmanager.getDir( dir_id );
			dir.next();
			dir_name = dir.getString( "name" );
		}catch( Exception e ){
			out.println( head );
			out.println( "fail to get card_list at CardBoxManager"+ e.toString() );
			out.println( end );
			return;
		}

		// directory에 등록되어있는 namecard list를 보여준다.
		out.println("<html><head><title></title>");
		out.println("<meta http-equiv=Content-Type content=text/html; charset=euc-kr>");
		//script 시작
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
		out.println("<td align=center height=40><a href=/namecard/UserServlet?cmd=showDirList><font color=#FFFFFF size=2>" + uni.convert( "개인명함함" ) + "</font></a></td>");
		out.println("<td align=center><a href=/namecard/UserServlet?cmd=updatePersonForm><font color=#FFFFFF size=2>" + uni.convert( "개인정보수정" ) + "</font></a></td>");
		out.println("<td align=center><a href=/namecard/UserServlet?cmd=comInfoForm><font color=#FFFFFF size=2>" + uni.convert( "회사정보수정" ) + "</font></a></td>");
		out.println("<td align=center><a href=/namecard/UserServlet?cmd=jobChangeForm><font size=2 color=#FFFFFF>" + uni.convert( "회사이전" ) + "</font></a></td>");
		out.println("<td align=center><a href=/namecard/UserServlet?cmd=checkDeleteID><font size=2 color=#FFFFFF>" + uni.convert( "가입해지" ) + "</font></a></td>");
		out.println("<td align=center><a href=/namecard/UserServlet?cmd=logout><font size=2 color=#FFFFFF>" + uni.convert( "로그아웃" ) + "</font></a></td>");
		out.println("</tr>");
		out.println("</table>");
		out.println("</td>");
		out.println("</tr>");
		out.println("<tr>");
		out.println("<td height=500 valign=top><br><p>");
		out.println("<table border=0 width=90% align=center cellspacing=0>");
		out.println("<tr>"); 
		out.println("<td colspan=2><br><p><br><p>"); 
		out.println("<b><font color=#FFFF00 size=2>" + uni.convert( "명함검색" ) + "</font></b></td>");
		out.println("</tr>");
		out.println("<td colspan=2><img src=/cardSystem/line.gif></td></tr>");
		out.println("<tr><form method=post action=/namecard/UserServlet>");
		out.println("<input type=hidden name=cmd value=search>");
		out.println("<td colspan=2><br><p><font size=2 color=#e6e6fa>" + uni.convert( "명함사이트에 등록이 되어있는" ) );
		out.println( "<br>" + uni.convert( " 사람들을 검색할수 있습니다<br>개인 명함함의 명함검색은 private를<br>사이트 전체의 검색은 public을 누르세요" ) + "</font></td>");
		out.println("</tr>");
		out.println("<tr>");
		out.println("<td><font size=2 color=#fffacd><b>" + uni.convert( "이름" ) + "</font></td>");
		out.println("<td><input type=text name=name size=10 ></td>");
		out.println("</tr>");
		out.println("<tr>");
		out.println("<td><font color=#fffacd size=2><b>" + uni.convert( "회사이름" ) + "</font></td>");
		out.println("<td><input type=text name=c_name size=16></td>");
		out.println("</tr>");
		out.println("<tr>");
		out.println("<td><font color=#fffacd size=2><b>" + uni.convert( "지점" ) + "</font></td>");
		out.println("<td><input type=text name=site size=10></td>");
		out.println("</tr>");
		out.println("<tr>");
		out.println("<td><font color=#fffacd size=2><b>" + uni.convert( "업종" ) + "</font></td>");
		out.println("<td><select name=kind size=1>");
		out.println("<option value=0>" + uni.convert( "선택사항없음" ) + "</option>");
		out.println("<option value=1>" + uni.convert( "공무원" ) + "</option><option value=2>" + uni.convert( "교원" ) + "</option>");
		out.println("<option value=3>" + uni.convert( "의료" ) + "</option><option value=4>" + uni.convert( "유통" ) + "</option>");
		out.println("<option value=5>" + uni.convert( "기계/자동차" ) + "</option><option value=6>" + uni.convert( "컴퓨터/통신" ) + "</option>");
		out.println("<option value=7>" + uni.convert( "서비스업" ) + "</option><option value=8>" + uni.convert( "금융" ) + "</option>");
		out.println("<option value=9>" + uni.convert( "건축/인테리어" ) + "</option><option value=10>" + uni.convert( "방송" ) + "</option>");
		out.println("<option value=11>" + uni.convert( "예술" ) + "</option><option value=12>" + uni.convert( "무역" ) + "</option>");
		out.println("<option value=13>" + uni.convert( "스포츠" ) + "</option><option value=14>" + uni.convert( "프리랜서" ) + "</option>");
		out.println("<option value=15>" + uni.convert( "기타" ) + "</option></select></td>");
		out.println("</tr>");
		out.println("<tr><td><font size=2 color=#fffacd><b>" + uni.convert( "지역" ) + "</font></td>");
		out.println("<td><select name=region size=1>");
		out.println("<option value=0>" + uni.convert( "선택사항없음" ) + "</option>");
		out.println("<option value=1>" + uni.convert( "서울특별시" ) + "</option><option value=2>" + uni.convert( "부산광역시" ) + "</option>");
		out.println("<option value=3>" + uni.convert( "대구광역시" ) + "</option><option value=4>" + uni.convert( "인천광역시" ) + "</option>");
		out.println("<option value=5>" + uni.convert( "광주광역시" ) + "</option><option value=6>" + uni.convert( "울산광역시" ) + "</option>");
		out.println("<option value=7>" + uni.convert( "대전광역시" ) + "</option><option value=8>" + uni.convert( "경기" ) + "</option>");
		out.println("<option value=9>" + uni.convert( "강원" ) + "</option><option value=10>" + uni.convert( "충북" ) + "</option>");
		out.println("<option value=11>" + uni.convert( "충남" ) + "</option><option value=12>" + uni.convert( "전북" ) + "</option>");
		out.println("<option value=13>" + uni.convert( "전남" ) + "</option><option value=14>" + uni.convert( "경북" ) + "</option>");
		out.println("<option value=15>" + uni.convert( "경남" ) + "</option><option value=16>" + uni.convert( "제주" ) + "</option>");
		out.println("</select></td>");
		out.println("</tr>");
		out.println("<tr>");
		out.println("<td align=right><br><p><input type=radio name=search_kind value=private><font size=2 color=#fffacd>private</td>");
		out.println("<td align=left><br><p><input type=radio name=search_kind checked value=public><font size=2 color=#fffacd>public</td></tr>");
		out.println("<tr><td colspan=2 align=center><input type=submit value=" + uni.convert( "검색" ) + "></td>");
		out.println("</form></tr>");
		out.println("</table>");
		out.println("</td>");
		out.println("<td height=500 width=300 valign=top align=center><br><p>");
		out.println("<b><font color=#000088>"+ dir_name + " </font><font size=2>" + uni.convert( "디렉토리" ) + "</font><br><p>");
		out.println("<table border=0 width=90%>");
		out.println("<tr>");
		out.println("<td align=center bgcolor=#800080><font color=#ffffff size=2><b>" + uni.convert( "이름" ) + "</td>");
		out.println("<td align=center bgcolor=#800080><font color=#ffffff size=2><b>" + uni.convert( "회사이름" ) + "</td>");
		out.println("<td align=center bgcolor=#800080><font color=#ffffff size=2><b>" + uni.convert( "직책" ) + "</td>");
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
					//이름을 얻는다.
					String name = result.getString( 1 );
					//회사의 이름을 얻어온다.
					String c_name = result.getString( 3 );
					//지위를 얻는다.
					String position = result.getString( 2 );
					if( position== null ) position="없음";	

					out.println("<tr><td height=20 align=center><font size=2 color=#000088><a href=\"javascript:open_window('/namecard/UserServlet?cmd=showNameCard&person_kind=" + person_kind + "&card_id=" + card_id +  "')\">" + name + "</a></font></td>");
					out.println("<td height=20 align=center><font size=2 color=#000088>" +  c_name + "</font></td>");
					out.println("<td height=20 align=center><font size=2 color=#000088>" +  position +"</font></td></tr>");
				}	
			}
		
	  	}catch( Exception e ){
			out.println( "fail to showCardList at CardBoxManager " + e.toString() );
			return;
	  	}
		out.println("<tr><td colspan=3 align=center><br><p><font size=2><font color=red>" + num + "</font>" + uni.convert( "개의 명함이 등록되어있습니다." ) + "</font></td></tr>" );
		out.println("<tr><td colspan=3 align=center><br><p><a href=/namecard/UserServlet?cmd=showDirList><font size=2><b><-Back</a></td></tr>");
		out.println("</table>");
		out.println("</td>");
		
		out.println("<td height=500 align=center valign=top>");
		out.println("<table width=90% border=0 cellspacing=0>");
  		out.println("<tr>");
		out.println("<td><font size=2 color=#ffff00><br><p><b><br><p><br><p>" + uni.convert( "새명함등록" ) + "</font></td>");
		out.println("</tr>");
		out.println("<tr>");
		out.println("<td><img src=/cardSystem/line.gif></td>");
		out.println("</tr>");
		out.println("<tr>");
		out.println("<td><form method=post action=/namecard/UserServlet><input type=hidden name=cmd value=addCardForm>");
		out.println("<input type=hidden name=dir_id value=\""+dir_id+"\"></td></tr>");
		out.println("<td><br><p><font size=2 color=#fffacd>" + uni.convert( "새 명함을 등록하시려면 여기를 눌러주세요" ) + "</td></tr>");
		out.println("<tr><td align=center><input type=submit value=\"" + uni.convert( "등록" ) + "\"></form></td></tr>"); 
		out.println("<tr><td><br><p><font size=2 color=#ffff00><b>" + uni.convert( "명함모양" ) + "</td></tr>");
		out.println("<tr><td><img src=/cardSystem/line.gif></td></tr>");
		out.println("<tr><td><br><p><font size=2 color=#fffacd>"); 
		out.println( uni.convert( "여러종류의 명함 모양이 있습니다.<br>명함 모양을 바꾸시려면<br> 여기를 누르세요." ) + "</td></tr>");
		out.println("<tr><td align=center><form method=post action=/namecard/UserServlet>");
		out.println("<input type=hidden name=cmd value=selectCardColorForm>");
		out.println("<input type=submit value=\"" + uni.convert( "명함모양바꾸기" ) + "\">");
		out.println("</form></td></tr>");
		out.println("</table>");
		out.println("</td></tr>");
		out.println("</table>");
		out.println("</body>");
		out.println("</html>");	
		

	}

	/**
	 *  showNameCard  - 리스트에서 선택된 명함의 상세정보를 보여주는 method
	 *
	 *  @param  req         입력된 데이터가 들어있는 곳
	 *  @param  out         결과를 출력할 곳
	 *
	 */
	public void showNameCard( HttpServletRequest req, PrintWriter out )throws UnsupportedEncodingException, CommandException {
		// session을 얻어온다.
		HttpSession httpsession = req.getSession( false );
		
		// session이 없으면 등록이 제대로 되지 않았다는 말이므로 error 처리한다.
		if( httpsession == null) {
			out.println( head );
			out.println( "<h1>Error !!! </h1>" );
			out.println( end );
			return;
		}
		
		//  session에서 사용자 id를 얻어온다.
		String id = (String)httpsession.getValue( "id" );
		//out.println( "id-" + id );
		
		// session 에서 현재 디렉토리의 id를 얻어서 디렉토리를 얻는다.
		String dir_id = (String)httpsession.getValue( "current_dir" ) ;
		//out.println( "current dir-" + dir_id );
		
		// 선택된 명함의id를 얻어온다.
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
			//cardContent을 얻어온다.( query 결과 )
			person = dbmanager.getCardContent(  card_id , person_kind );
			person.next();	
			//이름을 얻는다.
			name =  asc.convert( person.getString( "name" ) ) ;
			//집전화번호를 얻는다.
			tel = person.getString( 13 );
			//핸드폰 번호를 얻는다.
			handphone = person.getString( "handphone" );
			//삐삐 번호를 얻는다.
			beeper = person.getString( "beeper" );
			//이메일을 얻는다
			email = person.getString( "email" );
			//홈페이지를 얻는다
			homepage = person.getString( "homepage" );
			//회사이름을 얻는다.
			c_name = person.getString( 10 );
			//회사 지점을 얻는다
			c_site = person.getString( "site" );
			c_address = person.getString( "address" );
			//지위를 얻는다
			position = person.getString( "posi" );
			//부서를 얻는다
			part = person.getString( "part" );
			//회사 개인 전화번호를 얻는다
			j_tel = person.getString( "tel" );
			//회사 부서 팩스를 얻는다
			j_fax = person.getString( "fax" );
			// 명함색깔을 얻는다.
			if( person_kind.equals( "m" ) ){
				//descripton을 얻는다
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
			out.println("<tr><td colspan=2><font size=2 color=#" + font + ">" + position /*직책*/ + "</td>");
			out.println("<td><b><font size=2 color=#" + font + ">Beeper</td><td><font size=2 color=#" + font + ">"+ beeper/*삐삐*/  +"</td></tr>");
			out.println("<tr>");
			out.println("<td colspan=2><font size=2 color=#" + font + ">"+ c_address/* 회사주소 */+"</td>");
			out.println("<td><font size=2 color=#" + font + "><b>e-mail</td><td><font size=2 color=#" + font + ">"+ email +"</td></tr>");
			out.println("<tr>");
			out.println("<td><font size=2 color=#" + font + "><b>Com Tel</td><td><font size=2 color=#" + font + ">"+ j_tel/*회사전화*/ +"</td>");
			out.println("<td><font size=2 color=#" + font + "><b>Homepage</td><td><font size=2 color=#" + font + ">"+ homepage/*홈페이지*/+" </td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td><font size=2 color=#" + font + "><b>Fax</td><td><font size=2 color=#" + font + ">"+ j_fax/*팩스*/ +"</td>");
			out.println("<td><font size=2 color=#" + font + "><b>Home Tel</td><td><font size=2 color=#" + font + ">"+ tel/*집전화*/+"</td>");
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
		
		// 삭제, 이동버튼을 넣는다.
		out.println("<br><table border=0 width=80% cellspacing=0>");
		out.println("<tr><td width=50% align=center>");
		out.println( "<form method=post action=/namecard/UserServlet target=opener>" );
		out.println( "<input type=hidden name=cmd value=deleteCard>" );
		out.println( "<input type=hidden name=card_id value=\"" + card_id + "\">" );
		out.println( "<input type=hidden name=dir_id value=\"" + dir_id + "\">" );
		out.println( "<input type=hidden name=delete_kind value=myDir>" );
		out.println( "<input type=submit value=" + uni.convert( "삭제" ) + " onClick=window.close();></form></td>" );
		out.println("<td>");
		out.println( "<form method=post action=/namecard/UserServlet target=opener>" );
		out.println( "<input type=hidden name=cmd value=moveCard>" );
		out.println( "<input type=hidden name=card_id value=\"" + card_id + "\">" );
		out.println( "<select name=dir_id size=1 >" );

		try{
			//dir list 얻어오기
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

		out.println( "</select><font size=2>" + uni.convert(" 로 " ) + "<input type=submit value=" + uni.convert( "이동" ) + "></form></td></tr>" );
		out.println("<tr><td colspan=2 align=center>");
			out.println("<form method=post action=/namecard/UserServlet target=opener>");
			out.println("<input type=hidden name=cmd value=updateCardForm>");
			out.println("<input type=hidden name=card_id value=\"" + card_id + "\">");
			out.println("<input type=hidden name=dir_id value=\"" + dir_id + "\">");
		if( person_kind.equals( "n" ) ){
			out.println("<input type=submit value=\"" + uni.convert( "수정" ) + "\" onClick=window.close();>" );
		}
		out.println("<input type=button value=" + uni.convert( "닫기" ) + " onClick=window.close();></td></tr></table>");
		out.println("</form>");
		out.println("</center></body>");
		out.println("</html>");

		
	}

	/**
	 * showSearchCard - 검색한 카드 보여주는 함수
	 *  
	 *  @param  req 폼에서 입력받은 데이타가 있는 곳
	 *  @param  out 결과를 출력할 곳
	 *
	 **/
	public void showSearchCard( HttpServletRequest req, PrintWriter out ) throws CommandException, UnsupportedEncodingException{
		// session을 얻어온다.
		HttpSession httpsession = req.getSession( false );
		
		// session이 없으면 등록이 제대로 되지 않았다는 말이므로 error 처리한다.
		if( httpsession == null) {
			out.println( head );
			out.println( "<h1>Error !!! </h1>" );
			out.println( end );
			return;
		}

		//req에서 String 파라메터 패싱한다.
		String card_id = uni.convert( req.getParameter("card_id") );
		String person_kind = req.getParameter( "person_kind" );
		String search_kind = req.getParameter("search_kind");

		String dir_id = null;
		String id = null;

		if( !search_kind.equals( "out" ) ) {
			//그 카드가 속해있는 디렉토리 아이디
			dir_id = req.getParameter("dir_id");

			//session에서 현재 사용자 id얻기		
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
			//card content를 얻는다 
			ResultSet person = dbmanager.getCardContent( card_id, person_kind );
			if(!person.next()) out.println( " person is null" );

			// 명함 색깔과 이미지를 얻는다.
			if( person_kind.equals( "m" )) {
				//description을 얻는다.
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
			//회사 주소를 얻는다.
			c_address = person.getString( "address" );
			//이름을 얻는다.
			name = person.getString( 1 );
			//집전화번호를 얻는다.
			tel = person.getString( 13 );
			//핸드폰 번호를 얻는다.
			handphone = person.getString( "handphone" );
			//삐삐 번호를 얻는다.
			beeper = person.getString( "beeper" );
			//이메일을 얻는다
			email = person.getString( "email" );
			//홈페이지를 얻는다
			homepage = person.getString( "homepage" );
			//회사이름을 얻는다.
			c_name = person.getString( 10 );
			//회사 지점을 얻는다
			c_site = person.getString( "site" );
			//지위를 얻는다
			position = person.getString( "posi" );
			//부서를 얻는다
			part = person.getString( "part" );
			//회사 개인 전화번호를 얻는다
			j_tel = person.getString( "tel" );
			//회사 부서 팩스를 얻는다
			j_fax = person.getString( "fax" );
		}catch( Exception e ){
			out.println( head );
			out.println( "fail to showSearchCard at DBManager "  + e.toString() );
			out.println( end );
			return;
		}	
	
		//해당 명함 보여주기
		out.println("<html><head><title></title></head>");
 		out.println("<body bgcolor=#ffffff>");
   		out.println("<center><table border=2 width=450>");
   		out.println("<tr>");
		out.println("<td bgcolor=#" + bgcolor + " bordercolor=#ffffff align=center>");
 		// 배경이미지를 보여준다. 
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
			out.println("<tr><td colspan=2><font size=2 color=#" + font + ">" + position /*직책*/ + "</td>");
			out.println("<td><b><font size=2 color=#" + font + ">Beeper</td><td><font size=2 color=#" + font + ">"+ beeper/*삐삐*/  +"</td></tr>");
			out.println("<tr>");
			out.println("<td colspan=2><font size=2 color=#" + font + ">"+ c_address/* 회사주소 */+"</td>");
			out.println("<td><font size=2 color=#" + font + "><b>e-mail</td><td><font size=2 color=#" + font + ">"+ email +"</td></tr>");
			out.println("<tr>");
			out.println("<td><font size=2 color=#" + font + "><b>Com Tel</td><td><font size=2 color=#" + font + ">"+ j_tel/*회사전화*/ +"</td>");
			out.println("<td><font size=2 color=#" + font + "><b>Homepage</td><td><font size=2 color=#" + font + ">"+ homepage/*홈페이지*/+" </td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td><font size=2 color=#" + font + "><b>Fax</td><td><font size=2 color=#" + font + ">"+ j_fax/*팩스*/ +"</td>");
			out.println("<td><font size=2 color=#" + font + "><b>Home Tel</td><td><font size=2 color=#" + font + ">"+ tel/*집전화*/+"</td>");
			out.println("</tr>");
			out.println("</table><p>");

		}else{
			out.println("<tr>");
			out.println( "<td rowspan=10 colspan=2 align=center valign=middle><img src=" + image+logo + "></td>" );
			out.println("<td colspan=2><font size=3 color=#" + font + "><b>"+ name +"</b></font></td></tr>");
			out.println("<tr><td colspan=2><font size=2 color=#" + font + ">" + position /*직책*/ + "</td></tr>");
			out.println("<tr><td colspan=2><font size=2 color=#" + font + ">"+ c_address/* 회사주소 */+"</td></tr>");
			out.println("<tr><td><font size=2 color=#" + font + "><b>Com Tel</td><td><font size=2 color=#" + font + ">"+ j_tel/*회사전화*/ +"</td></tr>");
			out.println("<tr><td><font size=2 color=#" + font + "><b>Fax</td><td><font size=2 color=#" + font + ">"+ j_fax/*팩스*/ +"</td></tr>");
			out.println("<tr><td><b><font size=2 color=#" + font + ">H.P.</td><td><font size=2 color=#" + font + ">" + handphone +"</td></tr>");
			out.println("<tr><td><b><font size=2 color=#" + font + ">Beeper</td><td><font size=2 color=#" + font + ">"+ beeper/*삐삐*/  +"</td></tr>");
			out.println("<tr><td><font size=2 color=#" + font + "><b>e-mail</td><td><font size=2 color=#" + font + ">"+ email +"</td></tr>");
			out.println("<tr><td><font size=2 color=#" + font + "><b>Homepage</td><td><font size=2 color=#" + font + ">"+ homepage/*홈페이지*/+" </td></tr>");
			out.println("<tr><td><font size=2 color=#" + font + "><b>Home Tel</td><td><font size=2 color=#" + font + ">"+ tel/*집전화*/+"</td></tr>");
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
			
			out.println("<img src=/icon.gif><font size=2><b>" + uni.convert( "디렉토리에 등록" ) + "</font></b>");
			
			out.println("<table width=80% border=0 cellspacing=0>");
			out.println("<tr><td align=center><form method=post action=/namecard/UserServlet target=opener>");
			out.println("<input type=hidden name=cmd value=addPublicCard>");
			out.println("<font size=2><b>" + uni.convert( "디렉토리 이름" ) + ":</b></font>");
			out.println("<select size=1 name=dir_id>");
		
			//디렉토리 리스트를 DB에서 얻어온다.
			ResultSet d_list = dbmanager.getDirList( id );
			String d_id = "";
			String d_name = "";

			try{
				while( d_list.next() ){
					//디렉토리 아이디를 얻어온다
					d_id = d_list.getString( "id" );
					//디렉토리 이름을 얻어온다
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
			out.println("<input type=submit value=" + uni.convert("등록") + " onClick=\"window.close()\"></form></td></tr>");
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
				out.println("<tr><td colspan=2><center><font size=2><font color=red>" +  dir_name + uni.convert("</font> 디렉토리에 등록된 명함입니다" ) + "</font></center></td></tr>" );
				out.println("<tr><td></td></tr>" );
			}
			out.println("<tr><td align=right><form method=post action=/namecard/UserServlet target=opener>");
			out.println("<input type=hidden name=cmd value=deleteCard>");
			out.println("<input type=hidden name=dir_id value=\""+dir_id+"\">");
			out.println("<input type=hidden name=card_id value=\""+card_id+"\">");
			out.println("<input type=hidden name=delete_kind value=search>");
			out.println("<input type=submit value=" + uni.convert("삭제" ) + " onClick=\"window.close();\"></form></td>");
			out.println("<td width=50% align=left><form method=post action=/namecard/UserServlet target=opener>");
			out.println("<input type=hidden name=cmd value=updateCardForm>");
			out.println("<input type=hidden name=card_id value=\"" + card_id + "\">");
			out.println("<input type=hidden name=dir_id value=\"" + dir_id + "\">");
			out.println("<input type=submit value=\"" + uni.convert( "수정" ) + "\" onClick=\"window.close();\"></form></td></tr>");

		}

		out.println("<tr><td colspan=2 align=center>");
		out.println("<input type=button value=" + uni.convert("닫기") + " onClick=\"window.close();\">"); 
		out.println("</td></tr></table>");
		out.println("</center></body>");
		out.println("</html>");
	}


	///////////////////////////////  디렉토리 관리  ///////////////////////////
	
	/**
	 *  addDir - 새로운 디렉토리를 추가하는 method
	 *
	 *  @param req  입력된 데이터가 들어있는 곳
	 *  @param out  결과를 출력할 곳
	 *
	 */
	public void addDir( HttpServletRequest req, PrintWriter out ) throws CommandException, UnsupportedEncodingException {
		// session을 얻어온다.
		HttpSession httpsession = req.getSession( false );
		
		// session이 없으면 등록이 제대로 되지 않았다는 말이므로 error 처리한다.
		if( httpsession == null) {
			out.println( head );
			out.println( "<h1>Error !!! </h1>" );
			out.println( end );
			return;
		}
	
		// session에서 현재 사용자에 대한 정보를 얻는다.
		String id = (String)httpsession.getValue( "id" );

		// 등록하고자 하는 디렉토리 이름을 얻는다.
		String dirname = asc.convert(req.getParameter( "dirName" ));
		String name = "";
		//tokenizing 해서 space문자를 없애준다.
		StringTokenizer st = new StringTokenizer(dirname);
		while(st.hasMoreTokens())
			if(name == "") name = st.nextToken();
			else name = name + st.nextToken();

		if( name.equals( "" ) ){
			out.println( head );
			out.println( uni.convert("<br><br><center><font size=2 color=white>디렉토리 이름이 없습니다. 이름을 입력하시고 다시 시도하세요 <br><p>") ); 
			out.println("<a href=\"javaScript:window.history.go(-1);\"><font size=2><b><-back</a></center></font>");
			out.println( end );
			return;
		}	

		
		try {
			//card id를 만든다.
			Calendar date = Calendar.getInstance();
			String dir_id = id + String.valueOf(date.get(date.YEAR)) + String.valueOf(date.get(date.MONTH)) + String.valueOf(date.get(date.DATE)) + String.valueOf(date.get(date.HOUR)) + String.valueOf(date.get(date.MINUTE)) + String.valueOf(date.get(date.SECOND));

			//DB에 추가한다.
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
	 *  deleteDir - 현재 등록되어있는 디렉토리를 삭제하는 method
	 *
	 *  @param req  입력된 데이터가 들어있는 곳
	 *  @param out  결과를 출력할 곳
	 *
	 */
	public void deleteDir( HttpServletRequest req, PrintWriter out )  {
		// session을 얻어온다.
		HttpSession httpsession = req.getSession( false );
		
		// session이 없으면 등록이 제대로 되지 않았다는 말이므로 error 처리한다.
		if( httpsession == null) {
			out.println( head );
			out.println( "<h1>Error !!! </h1>" );
			out.println( end );
			return;
		}
	
		// session에서 현재 사용자에 대한 정보를 얻는다.
		String id = (String)httpsession.getValue( "id" );


		// 지우고자 하는 디렉토리 이름을 얻는다.
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
	 *  renameDir - 디렉토리 이름을 바꾸는 method
	 *
	 *  @param req  입력된 데이터가 들어있는 곳
	 *  @param out  결과를 출력할 곳
	 *
	 */
	public void renameDir( HttpServletRequest req, PrintWriter out )throws UnsupportedEncodingException {
	    //out.println("came in boxmanager.renameDir");	
		// session을 얻어온다.
		HttpSession httpsession = req.getSession( false );
		
		// session이 없으면 등록이 제대로 되지 않았다는 말이므로 error 처리한다.
		if( httpsession == null) {
			out.println( head );
			out.println( "<h1>Error !!! </h1>" );
			out.println( end );
			return;
		}
	
		// session에서 현재 사용자에 대한 정보를 얻는다.
		String id = (String)httpsession.getValue( "id" );
		
		//바꿀 이름을 얻는다. 
		String name = asc.convert(req.getParameter( "dirName" ));
		String to = "";
		//tokenizing 해서 space문자를 없애준다.
		StringTokenizer st = new StringTokenizer(name);
		while(st.hasMoreTokens())
			if(to == "") to = st.nextToken();
			else to = to + st.nextToken();

		if( to.equals( "" ) ){
			out.println( head );
			out.println( uni.convert("<center><br><br><font size=2 color=white>디렉토리 이름이 없습니다. 이름을 입력하시고 다시 시도하세요 <br><br><p>") ); 
			out.println("<a href=\"javaScript:window.history.go(-1);\"><font size=2><b><-back</a></font></center>");
			out.println( end );
			return;
		}	

		
		// 바꿀 디렉토리 id를 얻는다.
		String dir_id = req.getParameter( "dir_id" );
		
		ResultSet dir = null;

		// 이름을 수정한다.
		try {
			//DB에서 디렉토리에 대한 정보를 얻는다.
			dir = dbmanager.getDir( dir_id );
			dir.next();
			//디렉토리 내에 있는 카드수를 얻는다.
			int num = dir.getInt( "number" );

			//디렉토리를 수정한다.
			dbmanager.updateDir( dir_id, uni.convert(to), num );
			showDirList( req, out );
		} catch( Exception e ) {
			out.println( head );
			out.println( " fail to set directory name  " + e.toString() );
			out.println( end );
			return;
		}
		
		
	}


	////////////////////////////  명함관리  //////////////////////////////
	
	/**
	 *  addNewCard - 새로운 명함을 명함관리함에 등록시키는 method 
	 *
	 *  @param  req         입력된 데이터가 들어있는 곳
	 *  @param  out         결과를 출력한 곳
	 *
	 */
	public void addNewCard( HttpServletRequest req, PrintWriter out ) throws  UnsupportedEncodingException {
		// 권한 검사
		HttpSession httpsession = req.getSession( false );
		if( httpsession == null ) {
			out.println( head );
			out.println( "<h1>Unauthorized User! </h1>" );
			out.println( end );
			return;
		}
		
		//  session에서 person object를 얻어온다.
		String id = (String)httpsession.getValue( "id" );
		
		//  session에서 디렉토리 index를 얻어서 디렉토리를 얻는다.
		String dir_id = ( String )httpsession.getValue( "current_dir" );
		
		// req에서 parameter parsing한다.
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
	
		//tokenizing 해서 space문자를 없애준다.
		StringTokenizer st1 = new StringTokenizer(s_name);
		while(st1.hasMoreTokens())
			if(name == "") name = st1.nextToken();
			else name = name + st1.nextToken();
	
		//tokenizing 해서 space문자를 없애준다.
		StringTokenizer st2 = new StringTokenizer(s_c_name);
		while(st2.hasMoreTokens())
			if(c_name == "") c_name = st2.nextToken();
			else c_name = c_name + st2.nextToken();

		//tokenizing 해서 space문자를 없애준다.
		StringTokenizer st3 = new StringTokenizer(s_address);
		while(st3.hasMoreTokens())
			if(address == "") address = st3.nextToken();
			else address = address + st3.nextToken();

		//tokenizing 해서 space문자를 없애준다.
/*		StringTokenizer st4 = new StringTokenizer( s_email );
		while(st4.hasMoreTokens())
			if(email == "") email = st4.nextToken();
			else email = email + st4.nextToken();
*/
		//tokenizing 해서 space문자를 없애준다.
		StringTokenizer st5 = new StringTokenizer(s_j_tel);
		while(st5.hasMoreTokens())
			if(j_tel == "") j_tel = st5.nextToken();
			else j_tel = j_tel + st5.nextToken();

		//tokenizing 해서 space문자를 없애준다.
		StringTokenizer st6 = new StringTokenizer(s_position);
		while(st6.hasMoreTokens())
			if(position == "") position = st6.nextToken();
			else position = position + st6.nextToken();

		//tokenizing 해서 space문자를 없애준다.
		StringTokenizer st7 = new StringTokenizer(s_c_site);
		while(st7.hasMoreTokens())
			if(c_site == "") c_site = st7.nextToken();
			else c_site = c_site + st6.nextToken();
			
		// 입력된 정보를 확인한다.
		if( name.equals( "" ) || c_name.equals( "" ) || 
			address.equals( "" ) ||  
			position.equals( "" ) || j_tel.equals("" ) ) {
			out.println( head );
			out.println( uni.convert( "<center><br><br><font size=2 color=white>입력이 부족합니다. 자세한 정보를 입력하시고  다시 시도하세요<br>") );
			out.println("<br><p><a href=\"javaScript:window.history.go(-1);\"><font size=2><b><-Back</a></center></font>");
			out.println( end );
			return;
		}
 
		String message = "";
		Connection con = null;	
		Statement stmt = null;
		// 명함이 이미 등록되어있는지 확인한다.
		try{
			//등록되어 있는 사람인지 비교한다.
			String r = dbmanager.existCard( id, s_name, s_c_name, c_region) ;
			if( r != null ){
				out.println( head );
				out.println( uni.convert("<font size=2 color=white><br><br>" + r+ " 디렉토리에 이미 등록된 사용자 입니다.<br><br>") );	
				out.println("<a href=\"javaScript:window.history.go(-1);\"><font size=2><b><-back</a></center></font>"); 
				out.println( end );
				return;
			}
				
			// object생성 시간 입력
			Calendar date = Calendar.getInstance();
			String num = id + String.valueOf(date.get(date.YEAR)) + String.valueOf(date.get(date.MONTH)) + String.valueOf(date.get(date.DATE)) + String.valueOf(date.get(date.HOUR)) + String.valueOf(date.get(date.MINUTE)) + String.valueOf(date.get(date.SECOND));
		
			ResultSet com = null;
			message = " in searchCompany ";
			// 회사가 있으면 그대로 링크, 없으면 생성한다.
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

			// 회사 아이디 
			String com_id = null;	
			if( !com.next() ) {
				//회사가 없을경우 Company DB에 넣는다.
				message = " in query1";
				String query1 = "insert into Company (id, name, site, region, employee, cominfo) values ('" + num + "', '" + uni.convert(c_name) + "', '" + uni.convert(c_site) + "','" + c_region + "', '1', 'none');";

				//qeury 문 실행
				stmt.executeUpdate( query1 );
				com_id = num;
			} else {
				message = " in update ";
				com_id = com.getString( "id" );
				// 이미 있는 회사의 직원수를 늘린다.
				int employee = com.getInt( "employee" );
				employee++;
				String update = "update Company set employee='" + employee + "' where id='" + com_id+ "';";
				stmt.executeUpdate( update );		
			} 

			//새로운 사용자에 대한 정보를 PersonInfo DB에 추가한다.
			message = " in query2 ";
			String query2 = "insert into PersonInfo (id, address, tel, beeper, handphone, email, homepage) values ('" + num + "', '" + uni.convert( s_address ) + "', '" + tel + "', '" + beeper + "', '" + handphone + "', '" + email + "', '" + homepage + "');";

			//query 문 실행
			stmt.executeUpdate( query2 );			

			//새로운 사용자에 대한 정보를 JobInfo DB에 추가한다.
			message = " in query3 ";
			String query3 = "insert into JobInfo (id, posi, part, tel, fax, kind, company) values ('" + num + "', '" + uni.convert(position) + "', '" +uni.convert( part) + "', '" + j_tel + "', '" + j_fax + "', '" + j_kind + "', '" + com_id + "');";

			//query 문 실행
			stmt.executeUpdate( query3 );

			//새로운 사용자를 NonMemberPerson DB에 등록한다.
			message = " in query4";
			String query4 = "insert into NonMemberPerson (id, name, personinfo, jobInfo, com_id) values ('" + num + "', '" + uni.convert(name) + "', '" + num + "', '" + num + "', '" + com_id + "');";

			//query 문 실행
			stmt.executeUpdate( query4 );			

				
			//디렉토리에 명함을 등록시킨다.
			message = " in query5";
			String query5 = "insert into Card (dir_id, card_id, person_kind) values ('" + dir_id + "', '" + num + "', 'n' );";

			//query문 실행
			stmt.executeUpdate( query5 );
	
			// 등록된 명함수를 얻는다.
			message = " in query6";
			String query6 = "select number from Directory where id='" + dir_id + "';";
		 	ResultSet result = stmt.executeQuery( query6 );
			result.next();
		 	int number = result.getInt( "number" );
		 	number = number + 1;

			message = " in query7";
			String query7 = "update Directory set number='" + number + "' where id='" + dir_id + "';";
			stmt.executeUpdate( query7 );

			// connection 종료
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
	 *  addPublicCard - 멤버의 명함을 지정 디렉토리에  등록시키는 method 
	 *
	 *  @param  req         폼에서 입력받은 데이터가 들어있는 곳
	 *  @param  out         결과를 출력한 곳
	 *
	 */
	public void addPublicCard( HttpServletRequest req, PrintWriter out ) throws UnsupportedEncodingException {
		// 권한 검사
		HttpSession httpsession = req.getSession( false );
		if( httpsession == null ) {
			out.println( head );
			out.println( "<h1>Unauthorized User! </h1>" );
			out.println( end );
			return;
		}
	
		String id = (String)httpsession.getValue( "id" );

		//req에서 파라메터 파싱
		String dir_id = req.getParameter("dir_id");  //명함을 추가할 디렉토리
		String card_id = uni.convert( req.getParameter("card_id"));      

		httpsession.putValue( "current_dir" , dir_id );
		try {
			String r = dbmanager.existCard( card_id, id );
 	
			// 찾은 명함이 이미 등록되어있는지 확인한다.
			if(  r != null ){			
				//디렉토리 이름을 얻는다.
				out.println("<html><head><title></title>");
				out.println("<body bgcolor=#9acd32><center>"); 
				out.println("<font size=3 color=#2f4f4f><b>");
				out.println( uni.convert( r + " 디렉토리에 이미 등록되어있는 명함입니다." ) + "</b></font><br><br>" );
				out.println("<a href=\"javaScript:window.history.go(-1);\"><font size=2><b><-back</a></center>"); 
				out.println("</center></body></html>");
				return;
			}else{
				// 디렉토리에 명함을 등록시킨다.
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
	 *  updateCardForm - 개인명함디렉토리에 들어있는 넌멤버 명함을 수정하기 위한 폼 제공한다.
	 *
	 *  @param  req         입력된 데이터가 들어있는 곳
	 *  @param  out         결과를 출력한 곳
	 *
	 */
	 public void updateCardForm( HttpServletRequest req, PrintWriter out ) throws UnsupportedEncodingException{
		// 권한 검사
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
			//cardContent을 얻어온다.( query 결과 )
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
   		 	out.println("<img src=/icon.gif><b>" + uni.convert( "개인정보" ) + "</b></font></td>");
  		  	out.println("</tr>");
  			out.println("<tr>");
			out.println("<td align=center valign=top>");
			out.println("<form method=post action=/namecard/UserServlet >");
			out.println("<input type=hidden name=card_id value=" + card_id + ">");
			out.println("<input type=hidden name=dir_id value=" + dir_id + ">");
			out.println("<input type=hidden name=cmd value=updateCard>");
			out.println("<br><p>");
			out.println("<table border=0 width=80% align=center cellspacing=0>");
			out.println("<tr><td colspan=3 align=center><font size=2 color=#e6e6fa>" + uni.convert( "* 표가 되어있는 항목은 반드시 기입해 주세요." ) );
			out.println("</font><br><p></td></tr>");
			out.println("<tr>");
			out.println("<td><font size=2 color=#e0ffff><b>*</font></td>");
			out.println("<td><font color=#fffacd size=2><b>" + uni.convert( "이름" ) + "</b></td>");
			out.println("<td><font size=2 color=#e0ffff>" + person.getString( "name" ) + "</font></font></td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td></td><td><font color=#fffacd size=2><b>" + uni.convert( "전화" ) + "</b></td>");
			out.println("<td><font size=2 color=#e0ffff><input type=text name=tel value=\"" + person.getString( 13 ) + "\" size=12> </font></td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td></td><td><font color=#fffacd size=2><b>" + uni.convert( "삐삐" ) + "</b></td>");
			out.println("<td><font size=2 color=#e0ffff> <input type=text name=beeper value=\"" + person.getString( "beeper" )+ "\" size=13></font></td>");
			out.println("</tr><tr>");
			out.println("<td></td><td><font color=#fffacd size=2><b>" + uni.convert( "휴대폰" ) + "</b></td>");
			out.println("<td><font size=2 color=#e0ffff> <input type=text name=handphone value=\"" + person.getString( "handphone" ) +"\" size=13></font></td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td></td><td><font color=#fffacd size=2><b>" + uni.convert( "홈페이지" ) + "</b></td>");
			out.println("<td><font size=2 color=#e0ffff><input type=text name=homepage value=\"" +person.getString( "homepage" )+ "\" size=40></font></td>");
			out.println("</tr><tr>");
			out.println("<td><font size=2 color=#e0ffff><b></font></td>");
			out.println("<td><font color=#fffacd size=2><b>email<b></td>");
			out.println("<td><font size=2 color=#e0ffff><input type=text name=email value=\"" + person.getString( "email" ) +"\" size=40></font></td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td><font size=2 color=#e0ffff><b>*</font></td>");
			out.println("<td><font color=#fffacd size=2><b>" + uni.convert( "지위" ) + "</b></td>");
			out.println("<td><font size=2 color=#e0ffff><input type=text name=position value=\"" + person.getString( "posi" )+ "\" size=20></font></td>");
			out.println("</tr><tr>");
			out.println("<td></td><td><font color=#fffacd size=2><b>" + uni.convert( "부서" ) + "</b></td>");
			out.println("<td><font size=2 color=#e0ffff><input type=text name=part value=\"" + person.getString( "part" ) + "\" size=20></font></td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td><font size=2 color=#e0ffff><b>*</font></td>");
			out.println("<td><font color=#fffacd size=2><b>" + uni.convert( "회사개인전화" ) + "</b></td> ");
			out.println("<td><font size=2 color=#e0ffff><input type=text name=c_tel value=\"" + person.getString( "tel" ) + "\" size=13></font>");
			out.println("<font size=2 color=#e0ffff>" + uni.convert( "회사개인전화가 없을시 회사전화번호를 입력하세요." ) + " </td>");
			out.println("</tr><tr>");
			out.println("<td></td><td><font color=#fffacd size=2><b>" + uni.convert( "회사팩스" ) + "</b></td>");
			out.println("<td><font size=2 color=#e0ffff><input type=text name=c_fax value=\"" + person.getString( "fax" ) + "\" size=13></font></td>");
			out.println("</tr>");
		    out.println("</table><br><p></td>");
		    out.println("</tr>"); 
		    out.println("<tr>");
		    out.println("<td align=center height=28><b><font size=4 color=#ffff00><img src=/icon.gif>" + uni.convert( "회사정보" ) + "</b><br><p></td>");
			out.println("</tr>");
			out.println("<tr><td align=center>");
			out.println("<table border=0 width=80% cellspacing=0>");
			out.println("<tr>");
			out.println("<td><font size=2 color=#e0ffff><b>*</font></td>");
			out.println("<td><font color=#fffacd size=2><b>" + uni.convert( "회사이름" ) + "</b></td>");
			out.println("<td><font size=2 color=#e0ffff> <input type=text name=c_name value=\"" + person.getString( 10 ) + "\" size=20></font></td>");
			out.println("<td><font size=2 color=#e0ffff><b>*</font></td>");
			out.println("<td><font color=#fffacd size=2><b>" + uni.convert( "지역" ) + "</b></td>");
			out.println("<td><font size=2 color=#e0ffff><select name=c_region size=1>");

			if(c_region ==1) out.println("<option value=1 selected>" + uni.convert( "서울특별시" ) + "</option>");
			else out.println("<option value=1>" + uni.convert( "서울특별시" ) + "</option>");
 		 	
		   	if(c_region == 2) out.println("<option value=2 selected>" + uni.convert( "부산광역시" ) + "</option>");
			else out.println("<option value=2>" + uni.convert( "부산광역시" ) + "</option>");
	
			if(c_region == 3) out.println("<option value=3 selected>" + uni.convert( "대구광역시" ) + "</option>");
 			else out.println("<option value=3>" + uni.convert( "대구광역시" ) + "</option>");
	
			if(c_region == 4) out.println("<option value=4 selected>" + uni.convert( "인천광역시" ) + "</option>");
			else out.println("<option value=3>" + uni.convert( "인천광역시" ) + "</option>");
            		
			if(c_region == 5) out.println("<option value=5 selected>" + uni.convert( "광주광역시" ) + "</option>");
			else out.println("<option value=5>" + uni.convert( "광주광역시" ) + "</option>");

			if(c_region ==6) out.println("<option value=6 selected>" + uni.convert( "울산광역시" ) + "</option>");
			else out.println("<option value=6>" + uni.convert( "울산광역시" ) + "</option>");
        
   			if(c_region ==7) out.println("<option value=7 selected>" + uni.convert( "대전광역시" ) + "</option>");
			else out.println("<option value=7>" + uni.convert( "대전광역시" ) + "</option>");
	
			if(c_region ==8) out.println("<option value=8 selected>" + uni.convert( "경기" ) + "</option>");
			else out.println("<option value=8>" + uni.convert( "경기" ) + "</option>");
            
			if(c_region ==9) out.println("<option value=9 selected>" + uni.convert( "강원" ) + "</option>");
			else out.println("<option value=9>" + uni.convert( "강원" ) + "</option>");
	
			if(c_region ==10) out.println("<option value=10 selected>" + uni.convert( "충북" ) + "</option>");
			else out.println("<option value=10>" + uni.convert( "충북" ) + "</option>");
            
			if(c_region ==11) out.println("<option value=11 selected>" + uni.convert( "충남" ) + "</option>");
			else out.println("<option value=11>" + uni.convert( "충남" ) + "</option>");

			if(c_region ==12) out.println("<option value=12 selected>" + uni.convert( "전북" ) + "</option>");
			else out.println("<option value=12>" + uni.convert( "전북" ) + "</option>");
            
			if(c_region ==13) out.println("<option value=13 selected>" + uni.convert( "전남" ) + "</option>");
			else out.println("<option value=13>" + uni.convert( "전남" ) + "</option>");

			if(c_region ==14) out.println("<option value=14 selected>" + uni.convert( "경북" ) + "</option>");
			else out.println("<option value=14>" + uni.convert( "경북" ) + "</option>");
            		
			if(c_region ==15) out.println("<option value=15 selected>" + uni.convert( "경남" ) + "</option>");
			else out.println("<option value=15>" + uni.convert( "경남" ) + "</option>");

			if(c_region ==16) out.println("<option value=16 selected>" + uni.convert( "제주" ) + "</option>");
			else out.println("<option value=16>" + uni.convert( "제주" ) + "</option>");
			out.println("</select></font> </td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td></td><td><font color=#fffacd size=2><b>" + uni.convert( "지점" ) + "</b></td>");
			out.println("<td><font size=2 color=#e0ffff><input type=text name=c_site value=\"" + person.getString( "site" ) + "\" size=20></font></td>");
			out.println("<td><font size=2 color=#e0ffff><b>*</td>");
			out.println("<td><font color=#fffacd size=2><b>" + uni.convert( "업종" ) + "</b></font></td>");
			out.println("<td><font size=2 color=#e0ffff><select name=j_kind size=1>");

			if(j_kind == 1) out.println("<option value=1 selected>" + uni.convert( "공무원" ) + "</option>");
			else out.println("<option value=1>" + uni.convert( "공무원" ) + "</option>");

  		   	if(j_kind ==2) out.println("<option value=2 selected>" + uni.convert( "교원" ) + "</option>");
			else out.println("<option value=2>" + uni.convert( "교원" ) + "</option>");
        
			if(j_kind ==3) out.println("<option value=3 selected>" + uni.convert( "의료" ) + "</option>");
			else out.println("<option value=3>" + uni.convert( "의료" ) + "</option>");
        
			if(j_kind ==4) out.println("<option value=4 selected>" + uni.convert( "유통" ) + "</option>");
			else out.println("<option value=4>" + uni.convert( "유통" ) + "</option>");
        
			if(j_kind ==5) out.println("<option value=5 selected>" + uni.convert( "기계/자동차" ) + "</option>");
			else out.println("<option value=5>" + uni.convert( "기계/자동차" ) + "</option>");
       
			if(j_kind ==6) out.println("<option value=6 selected>" + uni.convert( "컴퓨터/통신" ) + "</option>");
			else out.println("<option value=6>" + uni.convert( "컴퓨터/통신" ) + "</option>");
        	
			if(j_kind ==7) out.println("<option value=7 selected>" + uni.convert( "서비스업" ) + "</option>");
			else out.println("<option value=7>" + uni.convert( "서비스업" ) + "</option>");

   	    	if(j_kind ==8) out.println("<option value=8 selected>" + uni.convert( "금융" ) + "</option>");
			else out.println("<option value=8>" + uni.convert( "금융" ) + "</option>");
	
   	    	if(j_kind ==9) out.println("<option value=9 selected>" + uni.convert( "건축/인테리어" ) + "</option>");
			else out.println("<option value=9>" + uni.convert( "건축/인테리어" ) + "</option>");

   	    	if(j_kind ==10) out.println("<option value=10 selected>" + uni.convert( "방송" ) + "</option>");
			else out.println("<option value=10>" + uni.convert( "방송" ) + "</option>");
        
			if(j_kind ==11) out.println("<option value=11 selected>" + uni.convert( "예술" ) + "</option>");
			else out.println("<option value=11>" + uni.convert( "예술" ) + "</option>");
        	
			if(j_kind ==12) out.println("<option value=12 selected>" + uni.convert( "무역" ) + "</option>");
			else out.println("<option value=12>" + uni.convert( "무역" ) + "</option>");
        
			if(j_kind ==13) out.println("<option value=13 selected>" + uni.convert( "스포츠" ) + "</option>");
			else out.println("<option value=13>" + uni.convert( "스포츠" ) + "</option>");
        
			if(j_kind ==14) out.println("<option value=14 selected>" + uni.convert( "프리랜서" ) + "</option>");
			else out.println("<option value=14>" + uni.convert( "프리랜서" ) + "</option>");
        
			if(j_kind ==15) out.println("<option value=15 selected>" + uni.convert( "기타" ) + "</option>");
			else out.println("<option value=15>" + uni.convert( "기타" ) + "</option>");
		
   		    out.println("</select></font></td></tr>"); 
			out.println("<tr>");
			out.println("<td><font size=2 color=#e0ffff><b>*</font></td>");
			out.println("<td><font color=#fffacd size=2><b>" + uni.convert( "주소" ) + "</b></td>");
			out.println("<td colspan=5><font size=2 color=#e0ffff> <input type=text name=address value=\"" + person.getString( "address" ) + "\" size=50></font></td></tr>");
			out.println("<tr>");
			out.println("<td colspan=6 align=center><br><input type=submit value='" + uni.convert( "등록" ) + "'>");
			out.println("<input type=button value='" + uni.convert( "취소" ) + "' onClick=\"window.history.go(-1);\"></td>" );
			out.println("</tr>");
			out.println("</table></center></td>");
			out.println("</tr>");
			out.println("</table></form>");
			out.println("</body>");
			out.println("</html>");
		} catch( SQLException e ) {
			out.println( head );
			out.println( uni.convert( "폼을 보여주는 것이 실패했습니다" ) + e.toString() );
			out.println( end );
		} catch( Exception e ) {
			out.println( head );
			out.println( uni.convert( "폼보여주기 실패" ) + e.toString() );
			out.println( end );
		}
	}

	/**
	 *  updateCard - 디렉토리에 등록된 명함의 내용을 수정한다.
	 * 
	 *  @param  req  임력된 데이터가 들어있는 곳
	 *  @param  out  결과를 출력할 곳
	 */
	public void updateCard( HttpServletRequest req, PrintWriter out ) throws UnsupportedEncodingException {
		// 권한 검사
		HttpSession httpsession = req.getSession( false );
		if( httpsession == null ) {
			out.println( head );
			out.println( "<h1>Unauthorized User! </h1>" );
			out.println( end );
			return;
		}

		String card_id = req.getParameter( "card_id" );
		//  session에서 person object를 얻어온다.
		String id = (String)httpsession.getValue( "id" );
		
		// req에서 parameter parsing한다.
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
		//tokenizing 해서 space문자를 없애준다.
		StringTokenizer st= new StringTokenizer(s_c_name);
		while(st.hasMoreTokens())
			if(c_name == "") c_name = st.nextToken();
			else c_name = c_name + st.nextToken();

		//tokenizing 해서 space문자를 없애준다.
		st = new StringTokenizer(s_c_site);
		while(st.hasMoreTokens())
			if(c_site == "") c_site = st.nextToken();
			else c_site = c_site + st.nextToken();

		//tokenizing 해서 space문자를 없애준다.
		st = new StringTokenizer(s_address);
		while(st.hasMoreTokens())
			if(address == "") address = st.nextToken();
			else address = address + st.nextToken();

		//tokenizing 해서 space문자를 없애준다.
		st = new StringTokenizer(s_beeper);
		while(st.hasMoreTokens())
			if(beeper == "") beeper = st.nextToken();
			else beeper = beeper + st.nextToken();

		//tokenizing 해서 space문자를 없애준다.
		st = new StringTokenizer(s_email);
		while(st.hasMoreTokens())
			if(email == "") email = st.nextToken();
			else email = email + st.nextToken();

		//tokenizing 해서 space문자를 없애준다.
		st = new StringTokenizer(s_homepage);
		while(st.hasMoreTokens())
			if(homepage == "") homepage = st.nextToken();
			else homepage = homepage + st.nextToken();

		//tokenizing 해서 space문자를 없애준다.
		st = new StringTokenizer(s_handphone);
		while(st.hasMoreTokens())
			if(handphone == "") handphone = st.nextToken();
			else handphone = handphone + st.nextToken();

		//tokenizing 해서 space문자를 없애준다.
		st = new StringTokenizer(s_tel);
		while(st.hasMoreTokens())
			if(tel == "") tel = st.nextToken();
			else tel = tel + st.nextToken();

		//tokenizing 해서 space문자를 없애준다.
		st = new StringTokenizer(s_j_tel);
		while(st.hasMoreTokens())
			if(j_tel == "") j_tel = st.nextToken();
			else j_tel = j_tel + st.nextToken();

		//tokenizing 해서 space문자를 없애준다.
		st = new StringTokenizer(s_j_fax);
		while(st.hasMoreTokens())
			if(j_fax == "") j_fax = st.nextToken();
			else j_fax = j_fax + st.nextToken();

		//tokenizing 해서 space문자를 없애준다.
		st = new StringTokenizer(s_position);
		while(st.hasMoreTokens())
			if( position == "") position = st.nextToken();
			else position = position + st.nextToken();

		//tokenizing 해서 space문자를 없애준다.
		st = new StringTokenizer(s_part);
		while(st.hasMoreTokens())
			if( part == "") part = st.nextToken();
			else part = part + st.nextToken();
message = "check input";
		// 입력된 정보를 확인한다.
		if( c_name.equals( "" ) || address.equals( "" ) || 
			position.equals( "" ) || j_tel.equals( "" ) ) {
			out.println( head );
			out.println( uni.convert( "<center><br><br><font size=2 color=white>입력이 부족합니다. 자세한 정보를 입력하시고  다시 시도하세요<br>") );
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
	
		// DB connection열기
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
			//cardContent을 얻어온다.( query 결과 )
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
			//tokenizing 해서 space문자를 없애준다.
			st= new StringTokenizer(person.getString( 10 ));
			while(st.hasMoreTokens())
				if(d_name == "") d_name = st.nextToken();
				else d_name = d_name + st.nextToken();
			//tokenizing 해서 space문자를 없애준다.
			st = new StringTokenizer(person.getString( "site" ) );
			while(st.hasMoreTokens())
				if(d_site == "") d_site = st.nextToken();
				else d_site = d_site + st.nextToken();
			//tokenizing 해서 space문자를 없애준다.
			st = new StringTokenizer( person.getString( "address" ) );
			while(st.hasMoreTokens())
				if(d_address == "") d_address = st.nextToken();
				else d_address = d_address + st.nextToken();
			//tokenizing 해서 space문자를 없애준다.
			st = new StringTokenizer( person.getString( "beeper" ));
			while(st.hasMoreTokens())
				if(d_beeper == "") d_beeper = st.nextToken();
				else d_beeper = d_beeper + st.nextToken();
			//tokenizing 해서 space문자를 없애준다.
			st = new StringTokenizer( person.getString( "email" ));
			while(st.hasMoreTokens())
				if(d_email == "") d_email = st.nextToken();
				else d_email = d_email + st.nextToken();
			//tokenizing 해서 space문자를 없애준다.
			st = new StringTokenizer( person.getString( "homepage" ));
			while(st.hasMoreTokens())
				if(d_homepage == "") d_homepage = st.nextToken();
				else d_homepage = d_homepage + st.nextToken();
			//tokenizing 해서 space문자를 없애준다.
			st = new StringTokenizer(person.getString( "handphone"));
			while(st.hasMoreTokens())
				if(d_handphone == "") d_handphone = st.nextToken();
				else d_handphone = d_handphone + st.nextToken();
message = "tel";
			//tokenizing 해서 space문자를 없애준다.
			st = new StringTokenizer(person.getString( 13));
			while(st.hasMoreTokens())
				if(d_tel == "") d_tel = st.nextToken();
				else d_tel = d_tel + st.nextToken();
			//tokenizing 해서 space문자를 없애준다.
			st = new StringTokenizer(person.getString( 8 ));
			while(st.hasMoreTokens())
				if(d_j_tel == "") d_j_tel = st.nextToken();
				else d_j_tel = d_j_tel + st.nextToken();
			//tokenizing 해서 space문자를 없애준다.
			st = new StringTokenizer(person.getString( "fax" ));
			while(st.hasMoreTokens())
				if(d_j_fax == "") d_j_fax = st.nextToken();
				else d_j_fax = d_j_fax + st.nextToken();
			//tokenizing 해서 space문자를 없애준다.
			st = new StringTokenizer(person.getString( "posi" ));
			while(st.hasMoreTokens())
				if(d_position == "") d_position = st.nextToken();
				else d_position = d_position + st.nextToken();
			//tokenizing 해서 space문자를 없애준다.
			st = new StringTokenizer(person.getString( "part" ));
			while(st.hasMoreTokens())
				if(d_part == "") d_part = st.nextToken();
				else d_part = d_part + st.nextToken();
			int d_region = person.getInt( "region" );
			int d_kind = person.getInt( "kind" );
message = "update check";	
			// 수정할게 있는지 확인한다.
			if( c_name.equals( d_name ) && c_site.equals( d_site ) && ( c_region == d_region ) && 
			email.equals( d_email ) && beeper.equals( d_beeper ) && tel.equals( d_tel ) &&
			handphone.equals( d_handphone ) && homepage.equals( d_homepage ) &&
			address.equals( d_address ) && j_fax.equals( d_j_fax ) && j_tel.equals( d_j_tel ) &&
			part.equals( d_part ) && position.equals( d_position ) && ( j_kind == d_kind ) ) {
			out.println( head );
			out.println( uni.convert( "수정할 정보가 없습니다. 다시 입력하시고 시도하세요" ) );
			out.println( end );
			return;
		} 	
			// personinfo, jobinfo 아이디 얻기
			String personinfo = person.getString( 14 );
			String jobinfo = person.getString( 15 );

message = "company check";
			if( !c_name.equals( d_name ) && !c_site.equals( d_site ) && (c_region !=  d_region)) {	
				// 먼저 회사가 존재하는지 찾는다.
				String query1 = "select * from Company where name='" + uni.convert(c_name) +
								"'  and region=" + c_region + 
								" and site='" + uni.convert(c_site) + "';";
				ResultSet com = stmt.executeQuery( query1 );
				String comid = null;
	
				if( !com.next() ) {
					// object생성 시간 입력
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
	
					// 이미 있는 회사의 직원수를 늘린다.
					int employee = com.getInt( "employee" );
					employee++;
					String query4 = "update Company set employee='" + employee + "' where id='" + comid+ "';";
					stmt.executeUpdate( query4 );									
				}
				String query5 = "update NenMemberPerson set comid='"+ comid + "'" + 
								" where id='" + card_id + "';";
				stmt.executeUpdate( query5 );
			} // 회사 수정


message = "update personinfo";
			String update = null;
			// handphone 수정
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
			out.println( uni.convert( "명함수정이 잘못되었습니다. 죄송합니다. " ) + e.toString());
			out.println( end );
		} catch( Exception e ) {
			out.println( head );
			out.println( uni.convert( "명함수정이 실패하였습니다. 죄송합니다." )  + message + e.toString() );			
			out.println( end );
		}
	}


	/**
	 *  deleteCard - 디렉토리에서 삭제하고자 하는 명함을 삭제한다.
	 *
	 *  @param  req         입력된 데이터가 들어있는 곳
	 *  @param  out         결과를 출력한 곳
	 *
	 */
	public void deleteCard( HttpServletRequest req, PrintWriter out ) throws  UnsupportedEncodingException {
		// 권한 검사
		HttpSession httpsession = req.getSession( false );
		if( httpsession == null ) {
			out.println( head );
			out.println( "<h1>Unauthorized User! </h1>" );
			out.println( end );
			return;
		}
		
		//  session에서 person object를 얻어온다.
		String id = (String)httpsession.getValue( "id" );


		/* delete할 때 디렉토리에서 명함을 클릭해서 삭제를 할 경우는 세션에서 현제 디렉토리를 
		 * 얻어오고 검색해서 얻은 명함을 본후 삭제할 경우에는 파라메터로 그 명함이 있는 디렉토리를 
         * 받아온다. 
		 **/
		String delete_kind = req.getParameter("delete_kind");    
		String dir_id = null;

		if(delete_kind.equals("myDir")){
			// session 에서 현재 디렉토리의 index를 얻어서 디렉토리를 얻는다.
			dir_id = (String)httpsession.getValue( "current_dir" );	
		}
		if(delete_kind.equals("search")){
			//parameter로 받아온다
			dir_id = req.getParameter("dir_id");
		}
		
		// req에서 삭제할 명함을 parsing한다.
		String card_id = uni.convert(req.getParameter( "card_id" ));
		
		//명함을 card DB에서 삭제한다.
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
	 *  moveCard - 현재 있는 디렉토리에서 다른 디렉토리로 명함을 옮기는 method
	 *
	 *  @param  req         입력된 데이터가 들어있는 곳
	 *  @param  out         결과를 출력한 곳
	 *
	 */
	public void moveCard( HttpServletRequest req, PrintWriter out ) throws UnsupportedEncodingException {
		// 권한 검사
		HttpSession httpsession = req.getSession( false );
		if( httpsession == null ) {
			out.println( head );
			out.println( "<h1>Unauthorized User! </h1>" );
			out.println( end );
			return;
		}
		
		//  session에서 person object를 얻어온다.
		String id = (String)httpsession.getValue( "id" );
		
		// session 에서 현재 디렉토리의 index를 얻어서 디렉토리를 얻는다.
		String dir_id = (String)httpsession.getValue( "current_dir" ) ;
		
		// req에서 옮길 명함과 옮겨갈 디렉토리 이름을 parsing한다.
		String card_id = uni.convert(req.getParameter( "card_id" ));
		String to  = req.getParameter( "dir_id" ) ;
		
		// 원래 있던 디렉토리에서 명함을 지우고 새로운 디렉토리에 추가시킨다.
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

////////////////////////////  디렉토리 서치  /////////////////////////////////
	 
	/**
	 *  myPersonSearch  - 명함관리함에 등록된 명함에서 서치하는 method
	 *
	 *  @param  req         폼에서 입력받은 데이터가 들어있는 곳
	 *  @param  out         결과를 출력할 곳
	 *
	 */
	public void myPersonSearch( HttpServletRequest req, PrintWriter out ) throws UnsupportedEncodingException{
		// 권한 검사
		HttpSession httpsession = req.getSession( false );
		if( httpsession == null ) {
			out.println( head );
			out.println( "<h1>Unauthorized User! </h1>" );
			out.println( end );
			return;
		}
		
		// req에서 파라메터 파싱한다.
		String t_name   = asc.convert( req.getParameter( "name" ) );     // 이름
		String t_c_name = asc.convert(req.getParameter( "c_name" ) );    // 회사이름
		String kind     = req.getParameter( "kind" );                    // 업종
		String c_region = req.getParameter( "region" );                  // 회사가 있는 지역
		String t_c_site = asc.convert( req.getParameter( "site" ) );     // 지점이름

		//tokenizing한 data를 가지는 변수
		String name = "";
		String c_name = "";
		String c_site = "";

		//tokenizing 해서 space문자를 없애준다.
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

		// 입력을 확인한다.
		if( name.equals( "" ) && c_name.equals("" )  &&  kind.equals( "0" )  
				&&  c_region.equals( "0" )  &&  c_site.equals( "" )  ) {
			out.println( head );
			out.println( "<center><font size=3 color=white><br><br>" + uni.convert( "입력된 데이터가 없습니다. 입력을 제대로 하고 시도하십시요" ) + "<br><br>" );
			out.println("<a href=\"javaScript:window.history.go(-1);\"><font size=2><b><-back</a></center></font>");
			out.println( end );
			return;
		}	

		
		// 검색대상 
		int num = 0;             // 검색개수

		Vector dir_ids = new Vector();   //그 명함이 속해있는 dir id
		Vector result = new Vector();   //검색한 card id
		Vector person_kd = new Vector();   //검색한 card의 person kind

		boolean cont = true;     // 중단여부
		
		// httpsession에서 사용자 아이디를 얻는다.
		String id = (String)httpsession.getValue( "id" );
	

		// 결과 보여주기
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
			// 디렉토리에 들어있는 명함을 검색한다.
			result1 = dbmanager.searchMem_MyCard( uni.convert(id), uni.convert(name), uni.convert(c_name), uni.convert(c_site),  c_region ,  kind  ); 		
		result2 = dbmanager.searchNonmem_MyCard( uni.convert(id), uni.convert(name), uni.convert(c_name),  c_region ,  uni.convert(c_site),  kind  ); 	
			out.println("<table border=0 cellspacing=0 width=500>" );
			out.println("<tr><td colspan=3 align=center><img src=/icon.gif><font size=4 color=#ffff00><b>" + uni.convert( "검색결과" ) + "<br><p><br><p></td></tr>");
			out.println( "<tr><td width=20% align=center><font size=2 color=#fffacd><b>" + uni.convert( "이름" ) + "<br><p></td>");
			out.println( "<td width=30% align=center><font size=2 color=#fffacd><b>" + uni.convert( "회사이름" ) + "<br><p></td>");
			out.println( "<td width=25% align=center><font size=2 color=#fffacd><b>" + uni.convert( "부서" ) + "<br><p></td>" );
			out.println( "<td width=25% align=center><font size=2 color=#fffacd><b>" + uni.convert( "직책" ) + "<br><p></td></tr>");		

			ResultSet searchdata = null;
			
			//검색한 멤버의 명함 리스트을 보여준다.				
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

				// 정보를 보여준다.		
				out.println( "<tr><td width=20% align=center><a href=\"javascript:open_window('/namecard/UserServlet?cmd=showSearchCard&search_kind=private&dir_id="+dir_id+"&person_kind=" + person_kind + "&card_id="+card_id+ "')\"><font size=2>" + name + "</a></td>");
				out.println( "<td width=30% align=center><font size=2>" + s_c_name+ "</td>");
				out.println( "<td width=25% align=center><font size=2>" + s_part +"</td>" );
				out.println( "<td width=25% align=center><font size=2>" + s_position + "</td></tr>");
			}
			
			//검색한 넌멤버의 명함 리스트을 보여준다.				
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

				// 정보를 보여준다.		
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

		out.println("<tr><td colspan=3 align=center><br><p><font size=2 color=red>"+num+"</font><font size=2 color=#e0ffff>" + uni.convert( "건의 결과를 얻었습니다." ) + "</td></tr>");
		out.println("<tr><td colspan=3 align=center>");
		out.println("<br><p><a href=\"javaScript:window.history.go(-1);\"><font size=2><b><-Back</a><br><p></td></tr>");
		out.println("</table></body></html>");
	}

	/**
	 *  publicSearch - publicSearch command 처리하는 method
	 *				  입력된 검색어로 명함을 찾는다.
	 *
	 *  @param req  입력된 데이터가 들어있는 곳
	 *  @param out  결과를 출력한 곳
	 *
	 */
	public void publicSearch( HttpServletRequest req, PrintWriter out ) throws UnsupportedEncodingException  {
		// 권한 검사
		HttpSession httpsession = req.getSession( false );
		if( httpsession == null ) {
			out.println( head );
			out.println( "<h1>Unauthorized User! </h1>" );
			out.println( end );
			return;
		}
		
		String search_kind = asc.convert( req.getParameter( "search_kind" ));
		// req에서 파라메터 파싱한다.
		String t_name = asc.convert( req.getParameter( "name" ) );      // 이름
		String t_c_name = asc.convert(req.getParameter( "c_name" ) );    // 회사이름
		String kind   = req.getParameter( "kind" );    // 업종
		String c_region = req.getParameter( "region" );   // 회사가 있는 지역
		String t_c_site = asc.convert( req.getParameter( "site" ) );      // 지점이름
		//tokenizing한 data를 가지는 변수
		String name = "";
		String c_name = "";
		String c_site = "";
		String card_id = "";
		String part = "";
		String position = "";

		ResultSet result = null;
		int num = 0;

		//tokenizing 해서 space문자를 없애준다.
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

		// 입력을 확인한다.

		if( name.equals( "" )  &&  c_name.equals( "" ) &&  kind.equals( "0" )
			&&  c_region.equals( "0" ) &&  c_site.equals( "" ) ) {
			out.println( head );
			out.println( "<center><font size=3 color=white><br><br>" + uni.convert( "입력된 검색어가 없습니다. 검색어를 입력하시고 시도하십시요" ) + "<br><br>" );
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

		// 검색결과 list를 보여준다.
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
		out.println("<tr><td colspan=3 align=center><img src=/icon.gif><font size=4 color=#ffff00><b>" + uni.convert( "검색결과" ) + "<br><p><br><p></td></tr>");	
		out.println( "<tr><td align=center width=20%><font size=2 color=#fffacd><b>" + uni.convert( "이름" ) + "<br><p></td>");
		out.println( "<td align=center width=30%><font size=2 color=#fffacd><b>" + uni.convert( "회사이름" ) + "<br><p></td>");
		out.println( "<td align=center width=25%><font size=2 color=#fffacd><b>" + uni.convert( "부서" ) + "<br><p></td>" );
		out.println( "<td align=center width=25%><font size=2 color=#fffacd><b>" + uni.convert( "직책" ) + "<br><p></td></tr>");

		try {
			ResultSet member = null; 

			while( result.next() ){
				num++;
				// 검색된 결과를 하나씩 꺼내온다.
				member = dbmanager.getMemberInfo( result.getString( 1 ) );
				if( member.next() ) { 
					card_id = member.getString( 1 );
					name = member.getString( 3 );
					c_name = member.getString( 17 );
					part = member.getString( 14 );
					position = member.getString( 13 );
			
					// 정보를 보여준다.		
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
		out.println( "<font size=2 color=red><b>" + num + "</font><font size=2 color=#ef0000>" + uni.convert( "건의 결과를 얻었습니다." ) + "</font></td></tr>" );
		out.println("<tr><td colspan=3 align=center><br>");
		out.println("<a href=\"javaScript:window.history.go(-1);\"><font size=2><b><-Back</a><br><p></td></tr>");
		out.println("</table></body></html>");
	}

	/**
	 *	search - 사이트 전체 검색과 개인명함함 검색을 처리한다.
	 *
	 *  @param req 입력된 데이터가 들어있는 곳 
	 *  @param out 결과를 출력한 곳
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

		//req에서 파라메터 받아와서 null체크
		String s_name        = asc.convert(req.getParameter("name"));
		String s_c_name      = asc.convert(req.getParameter("c_name"));
		String kind        = req.getParameter("kind");
		String region      = req.getParameter("region");
		String s_c_site      = asc.convert(req.getParameter("site"));
			
		String name= "";
		String c_name = "";
		String c_site = "";

		//tokenizing 해서 space문자를 없애준다.
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
			out.println( uni.convert( "<center><br><br><font size=2 color=white>입력된 검색어가 없습니다. 검색어를 올바르게 입력하시고 다시 시도하세요.<br><br>") );
			out.println( "<a href=\"javaScript:window.history.go(-1);\"><font size =2><b><-Back</a></font></center>");
			out.println( end );
			return;
		}
 
		String id = (String)httpsession.getValue("id");	

		
	
		// cardbox manager의 method를 호출한다.
		try{
			if(search_kind.equals("private")) myPersonSearch(req, out); 
			else if(search_kind.equals("public")) publicSearch(req, out);
			else if(search_kind.equals("out"))publicSearch(req, out);
			else{ 
				out.println( head );
				out.println( uni.convert( "seasrch_kind가 잘못되었음") );
				out.println( end );
			}
		}catch(Exception e){
			out.println( head );
			//out.println( "fail to search in search" + e.toString() );
			out.println( uni.convert( "<br><br><br><font size=3 color=white>검색중 오류가 발생했습니다. 다시 시도하십시요. <br><br>" ) );
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
 
		//검색한 넌멤버의 명함 리스트을 보여준다.				
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

				// 정보를 보여준다.		
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
	 * selectCardColorForm - 명함의 모양을 바꾸는 폼을 제공하는 함수 
	 *
	 * @param req	폼에서 파라메터 읽어온다 
	 * @param out	출력 Writer
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
		out.println( "<center><br><p><br><p><img src=/icon.gif><font size=3 color=#ffff00><b>" + uni.convert( "명함배경" ) + "</b>" );
		out.println( "<br><p><font size=2 color=#e6e6fa>" + uni.convert( "*원하는 명함배경색을 선택하세요." ) );
		
		out.println( "<table border=0 width=500 cellspacing=0>" );
		out.println( "<form method=post action=/namecard/UploadServlet enctype=multipart/form-data>" );
		out.println( "<input type=hidden name=cmd value=preview>" );
		out.println( "<tr><td colspan=4 align=center><font size=2 color=#e6e6fa>" + uni.convert( "원하는 배경색과 글자색을 입력하시고 미리보기를 누르세요." )+ "<br>" + uni.convert( "회사로고가 없으시면 로고를 등록하시지 않아도 됩니다." ) + "<p></td></tr>" );
		out.println( "<tr><td align=center><font size=2 color=#fffacd><b>" + uni.convert( "명함배경색" ) + "</td>" );
		out.println( "<td><input type=text name=background size=10></td>" );  
		out.println( "<td align=center><font size=2 color=#fffacd><b>" + uni.convert( "명함글자색" ) + "</td>");
		out.println( "<td><input type=text name=font size=10></td></tr>" );
		out.println( "<tr><td align=center><font size=2 color=#fffacd><b>" + uni.convert( "회사로고" ) + "</td>" );
		out.println( "<td colspan=3><input type=file name=logo size=25></td></tr>" );
		out.println( "</table><br><p>" );
		out.println( uni.convert("배경을 바꿀 명함의 종류를 선택해주세요<br>" ) );
		out.println( "<table><tr><td align=right>" );
		out.println( "<input type=radio name=kind checked value=m><font size=2 color=#fffacd><b>" + uni.convert( "내명함" ) +"</td>" );
		out.println( "<td align=left><input type=radio name=kind value=c><font size=2 color=#fffacd><b>" + uni.convert( "내가 볼 명함" ) + "</td></tr>" );
		out.println( "<tr><td align=right><br><p><input type=submit value=\"" + uni.convert( "미리보기" ) + "\" ></td>" );
		out.println( "<td align=left><br><p>" );
		out.println( "<input type=submit value=\"" + uni.convert( "취소" ) + "\" onClick=\"javaScript:window.history.go(-1);\"></td></tr>" );
		out.println( "</form></talbe></center></body></html>" );
	}

	/**
	 *	preview - 선택한 명함배경색과 명함글자색을 사용한 명함샘플을 보여준다.
	 *		
	 *	@param 	req		파라메터 받아옴
	 *	@pramr	out		출력 Wirter 
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

			// color  넣기 
			//dbmanager.insertColor();

			bgcolor = fileup.getParameter( "background" );
			font = fileup.getParameter( "font" );
		} catch( CommandException e ) {
			out.println( head );
			out.println( uni.convert( "<center><br><br><br><font size=3 color=white>파일 업로드에 문제가 발생했습니다. 다시 시도해 주십시요." ) );
			//out.println( "fail to create FileUpload " + e.toString() );
			out.println( "<br><br>" );
			out.println( "<a href=\"javaScript:window.history.go(-1);\"><font size =2><b><-Back</a></font></center>");	
			out.println( end );
			return;
		} catch( Exception e ) {
			out.println(  "fail to create FileUpload " + e.toString() );
			return;
		}

		// 입력을 확인한다
		try{
			if( bgcolor.equals( "" ) && font.equals( "" ) && logo.equals( "" )) {
				out.println( head );
				out.println( "<center><br><br><font size=3 color=white>" );
				out.println( uni.convert( "입력된 컬러와 이미지가 없습니다.<br> 이미지파일이나 컬러번호를 선택해주세요<br><br>" ) );
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
				out.println( uni.convert( "입력자료가 틀렸습니다. 1에서 140까지의 색깔중에 입력해 주십시오.<br><br>" ) );
				out.println( "<a href=\"javaScript:window.history.go(-1);\"><-Back</a>" );
				out.println( "</font></center>" );
				out.println( end );
				return;
			}
			
			// 이미지 파일인지 확장자 확인 
			/*if( !bgimage.endsWith( "gif" ) || !bgimage.endsWith( "jpg" ) ) {
				out.println( head );
				out.println( "<center><br><br><font size=3 color=white>" );
				out.println( uni.convert( "파일 형식이 맞지 않습니다. 다시 입력하세요<br><br>" ));
				out.println( "<a href=\"javaScript:window.history.go(-1);\"><-Back</a>" );
				out.println( "</font></center>" );
				out.println( end ); 
			}*/ 
			 
		}catch( Exception e ){
			//out.println( "fail to preview at boxmanager" + e.toString() );
		}

		// DB에서 컬러번호를 얻는다.
		try {
			if( !bgcolor.equals( "" ) ) bgcolor = dbmanager.getColor( bgcolor );
			if( !font.equals( "" ) ) font = dbmanager.getColor( font );
		} catch( CommandException e ) {
			out.println( head );
			out.println( uni.convert("<center><br><br><font size=3 color=white>컬러 얻기가 실패했습니다" + e.toString() ) );
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

			//tokenizing 해서 space문자를 없애준다.
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
			out.println( uni.convert("<center><br><br><font size=3 color=white>파일얻기가 실패했습니다." + e.toString() ) );
			out.println( "<br><br><a href=\"javaScript:window.history.go(-1);\"><-Back</a>" );
			out.println( "</center></font>" );
			out.println( end );
			return;
		} catch( IOException e ) {
			out.println( head );
			out.println( uni.convert("<center><br><br><font size=3 color=white>파일얻기가 실패했습니다." + e.toString() ) );
			out.println( "<br><br><a href=\"javaScript:window.history.go(-1);\"><-Back</a>" );
			out.println( "</center></font>" );
			out.println( end );
			return;

		} catch( Exception e ) {
			out.println( head );
			out.println( uni.convert("<center><br><br><font size=3 color=white>파일얻기가 실패했습니다." + e.toString() ) );
			out.println( "<br><br><a href=\"javaScript:window.history.go(-1);\"><-Back</a>" );
			out.println( "</center></font>" );
			out.println( end );
			return;
		}

		out.println( "<html><head><title></title></head>" );
		out.println( "<body background=/bg.jpg>" );
		out.println( "<br><p><center><img src=/icon.gif><font size=3 color=#ffff00><b>" + uni.convert( "명함샘플" ) );
		out.println( "<br><p><br><p><table border=2 bordercolor=#ffffff width=450 height=250 cellspacing=0>" );
		out.println( "<tr><td bgcolor=#" + bgcolor + " align=center valign=middle>" );
		out.println( "<table border=0 cellspacing=0 width=90%><tr>" );
		
		if( logo.equals("") ){
			//선택된 색으로 명함 보여주기
			out.println( "<td colspan=4 align=center><font color=#" + font + "><b>" + uni.convert( "개미소프트(본점)" ) + "<br><p></td></tr>" );
			out.println( "<tr><td colspan=2><font size=3 color=#" + font + "><b>" + uni.convert( "웁스" ) + "</td>" );
			out.println( "<td><font size=2 color=#" + font + "><b>H.P.</td><td><font size=2 color=#" + font + ">011-985-0355</td></tr>" );
			out.println( "<tr><td colspan=2><font size=2 color=#" + font + ">" + uni.convert( "개발팀 팀장" ) + "</td>" );
			out.println( "<td><font size=2 color=#" + font + "><b>Beeper</td><td><font size=2 color=#" + font + ">012-985-0355</td></tr>" );
			out.println( "<tr><td colspan=2><font size=2 color=#" + font + ">" + uni.convert( "대구광역시 동구 검사동 청운빌딩 5층" ) + "</td>" );
			out.println( "<td><font size=2 color=#" + font + "><b>Home Tel</td><td><font size=2 color=#" + font + ">053-985-0355</td></tr>" );
			out.println( "<tr><td><font size=2 color=#" + font + "><b>Com Tel</td><td><font size=2 color=#" + font + ">053-985-1111</td>" );
			out.println( "<td></td>" );
			out.println( "<tr><td><font size=2 color=#" + font + "><b>Fax</td><td><font size=2 color=#" + font + ">053-986-1235</td><td></td></tr>" );
			
		}else{
			out.println( "<td colspan=3 align=center><font color=#" + font + "><b>" + uni.convert( "개미소프트(본점)" ) + "<br><p></td></tr>" );
			out.println( "<tr><td rowspan=8 width=40% align=center valign=middle><img src=" + logopath + "></td>" );
			out.println( "<td colspan=2><font size=3 color=#" + font + "><b>" + uni.convert( "웁스" ) + "</td></tr>" );
			out.println( "<tr><td colspan=2><font size=2 color=#" + font + ">" + uni.convert( "개발팀 팀장" ) + "</td></tr>" );
			out.println( "<tr><td colspan=2 height=30><font size=2 color=#" + font + ">" + uni.convert( "대구광역시 동구 검사동 청운빌딩 5층" ) + "</td></tr>" );
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
		out.println( "<input type=submit value=\"" + uni.convert( "설정" ) + "\">");
		out.println( "</form><br><p><br><p>" );

		out.println( "<table border=0 width=500 cellspacing=0>" );
		out.println( "<form method=post action=/namecard/UploadServlet enctype=multipart/form-data>" );
		out.println( "<input type=hidden name=cmd value=preview>" );
		out.println( "<tr><td colspan=4 align=center><font size=2 color=#e6e6fa>" + uni.convert( "다시 선택하고 싶으시면 원하는 배경색과 글자색을 입력하세요." )+ "<br><p></td></tr>" );
		out.println( "<tr><td align=center><font size=2 color=#fffacd><b>" + uni.convert( "명함배경색" ) + "</td>" );
		out.println( "<td><input type=text name=background size=10></td>" );  
		out.println( "<td align=center><font size=2 color=#fffacd><b>" + uni.convert( "명함글자색" ) + "</td>");
		out.println( "<td><input type=text name=font size=10></td></tr>" );
		out.println( "<tr><td align=center><font size=2 color=#fffacd><b>" + uni.convert( "회사로고" ) + "</td>" );
		out.println( "<td colspan=3><input type=file name=logo size=25></td></tr>" );
		out.println( "</table><br><p>" );
		out.println( uni.convert("배경을 바꿀 명함의 종류를 선택해주세요<br>" ) );
		out.println( "<table><tr><td align=right>" );
		out.println( "<input type=radio name=kind checked value=m><font size=2 color=#fffacd><b>" + uni.convert( "내명함" ) +"</td>" );
		out.println( "<td align=left><input type=radio name=kind value=c><font size=2 color=#fffacd><b>" + uni.convert( "내가 볼 명함" ) + "</td></tr>" );
		out.println( "<tr><td align=right><br><p><input type=submit value=\"" + uni.convert( "미리보기" ) + "\" ></td>" );
		out.println( "<td align=left><br><p>" );
		out.println( "<input type=submit value=\"" + uni.convert( "취소" ) + "\" onClick=\"javaScript:window.history.go(-2);\"></td></tr>" );
		out.println( "</form></talbe></center></body></html>" );
	}

	
	/**
	 *  selectCardColor  - 명함에 들어갈 이미지와 색깔을 선택한 대로 저장한다.
	 *
	 *  @param  req  폼에서 입력받은 데이터가 들어있는 곳
	 *  @param  out  결과를 출력할 곳
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

		//tokenizing 해서 space문자를 없애준다.
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
			out.println( uni.convert( "수정할 정보가 없습니다. 명함색, 글자색, 로고 입력 후 다시 시도하세요" ) );
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
			out.println( uni.convert( "명함색깔 바꾸기가 실패하였습니다. 죄송합니다. 다시 시도하세요." ) + e.toString()  );
			out.println( "<br><br>" );
			out.println( "<a href=\"javaScript:window.history.go(-1);\"><-Back</a>" );	
			out.println( "</font></center>" );
			out.println( end );
			return;
		}
	}
	
}


	
