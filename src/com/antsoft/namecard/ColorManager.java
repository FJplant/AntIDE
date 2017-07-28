package com.antsoft.namecard;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import java.util.*;


public class ColorManager {
	DBManager dbmanager;
	Uni2Asc uni;
	Asc2Uni asc;
	static String head = "<html><body background=/bg.jpg>";
	static String end = "</body></html>";
	static String PATH = "/home/httpd/html/cardSystem/background/";

	public ColorManager() {
		dbmanager = new DBManager();
		uni = new Uni2Asc();
		asc = new Asc2Uni();
	}


	/**
	 *	preview - 선택한 명함배경색과 명함글자색을 사용한 명함샘플을 보여준다.
	 *		
	 *	@param 	req		파라메터 받아옴
	 *	@pramr	out		출력 Wirter 
	 */
	 public void preview( HttpServletRequest req, PrintWriter out ) throws UnsupportedEncodingException{
		/*
		HttpSession httpsession = req.getSession( false );
		if( httpsession == null ) {
			out.println( head );
			out.println( "<h1>Unauthorized User! </h1>" );
			out.println( end );
			return;
		}

		String id = (String)httpsession.getValue( "id" );
		String dir_id = (String)httpsession.getValue( "current_dir" );
*/
		String bgcolor = "";
		String font = "";
		String logo = "";
		String kind = "";

		FileUpload fileup = null;

		try {
			fileup = new FileUpload( req.getInputStream() );

			bgcolor = fileup.getParameter( "background" );
			font = fileup.getParameter( "font" );
		} catch( CommandException e ) {
			out.println( "fail to create FileUpload " + e.toString() );
			return;
		} catch( Exception e ) {
			out.println(  "fail to create FileUpload " + e.toString() );
			return;
		}

		// 입력을 확인한다
		try{
			if( bgcolor.equals( "" ) && font.equals( "" ) ) {
				out.println( head );
				out.println( "<center><br><br><font size=3 color=white>" );
				out.println( uni.convert( "입력된 컬러와 이미지가 없습니다.<br> 이미지파일이나 컬러번호를 선택해주세요<br><br>" ) );
				out.println( "<a href=\"javaScript:window.history.go(-1);\"><-Back</a>" );
				out.println( "</font></center>" );
				out.println( end );
				return;
			}
			if( ( bgcolor.length( ) > 3 ) || ( font.length() > 3 ) ){
				out.println( head );
				out.println( "<center><br><br><font size=3 color=white>" );
				out.println( uni.convert( "입력자료가 틀렸습니다. 다시 입력해 주십시오.<br><br>" ) );
				out.println( "<a href=\"javaScript:window.history.go(-1);\"><-Back</a>" );
				out.println( "</font></center>" );
				out.println( end );
				return;
			}
			
		}catch( Exception e ){
			out.println( "fail to preview at boxmanager" + e.toString() );
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

		// bgimage file copy
		try {
			String fname = asc.convert(fileup.getFileName());
			bgimage = PATH + uni.convert(fname);

			if( fname != null ) {
				File file = new File( bgimage );
				
				if( !file.exists() ){
					file.createNewFile(); 
				}
				FileOutputStream outFile = new FileOutputStream( file );
				fileup.upFile( outFile );

				outFile.close();
			} else {
				return;
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

		
		out.println("<html><head><title></title></head>");
 		out.println("<body" );
   		// 배경이미지가 있으면 이미지를 넣는다.
 		if( !bgimage.equals( "" ) ) out.println( " background=" + bgimage );
 		out.println( ">");
   		out.println("<center><table border=2 width=90%>");
   		out.println("<tr>");
		out.println("<td bgcolor=#" + bgcolor + " bordercolor=#ffffff align=center>");
		out.println("<table border=0 width=90% cellspacing=0>");
		out.println("<tr>");
		out.println("<td align=center colspan=2 height=55>"); 
		out.println("<font size=4 color=#" + font + "><b>" + uni.convert( "개미소프트(본사)" ) );
		out.println( "</b></font></td>");
		out.println("</tr>");
		out.println("<tr>");
		out.println("<td><b></font><font size=2 color=#" + font + ">");
		out.println( uni.convert( "개발팀" ) );
		out.println(  "  " + uni.convert( "연구원" ) + "</font></b></td>");
		out.println("<td><b><font size=2 color=#" + font + ">H.P.:011-504-1111</td>");
		out.println("</tr>");
		out.println("<tr>");
		out.println("<td>");
		out.println("<font size=3 color=#" + font + "><b>"+ uni.convert( "웁스" ) +"</b></font></td>");
		out.println("<td><b><font size=2 color=#" + font + ">beeper:012-1420-1234<font></b></td>");
		out.println("</tr>");
		out.println("<tr>");
		out.println("<td><b><font size=2 color=#" + font + ">"+ uni.convert( "대구광역시 동구 검사동 990-198 청운빌딩 5?" ) + "</td>");
		out.println("<td><font size=2 color=#" + font + "><b>e-mail:ant@antsoft.co.kr</td>");
		out.println("</tr>");
		out.println("<tr>");
		out.println("<td><font size=2 color=#" + font + "><b>tel:053-985-0355</td>");
		out.println("<td><font size=2 color=#" + font + "><b>homepage:http://antsoft.co.kr/antsoft</td>");
		out.println("</tr>");
		out.println("<tr>");
		out.println("<td><font size=2 color=#" + font + "><b>fax:053-986-1235</td>");
		out.println("<td><font size=2 color=#" + font + "><b>home tel:053-985-0355</td>");
		out.println("</tr>");
		out.println("</table><p>");

		out.println("</td></tr>");
		out.println("<tr>");
		out.println("<td bgcolor=#" + bgcolor + " bordercolor=#ffffff align=center>");
		out.println("<table width=100%><font size=2>");
		out.println("<tr>");
		out.println("<td bordercolor=#ffffff><font size=2 color=#" + font + "><b>description:</b><br>" + uni.convert( "안녕하세요. 저는 개미소프트의 캐릭터 웁스입니다." ) + "</td>");
		out.println("</tr>");
		out.println("</font></table>");
		out.println("</td></tr>");
		out.println("</table>");
		
		out.println("<br><table border=0 width=80% cellspacing=0>");
		out.println( "<form method=post action=/namecard/UserServlet>" );
		out.println( "<input type=hidden name=cmd value=selectCardColor>" );
		out.println( "<input type=hidden name=background value=\"" + bgcolor + "\">" );
		out.println( "<input type=hidden name=font value=\"" + font + "\">" );
		out.println( "<input type=hidden name=bgimage value=\"" + bgimage + "\">" );
		//out.println( "<input type=hidden name=logo value=\"" + logo + "\">" );
		out.println( "<input type=hidden name=kind value=" + kind + ">" );
		//out.println( "<input type=hidden name=dir_id value=\"" + dir_id + "\">" );
		out.println( "<tr><td width=50% algin=center>");
		out.println( "<input type=submit value=" + uni.convert( "설정" ) + "></td>" );
		out.println( "<td align=center>");
		out.println( "<input type=submit value=\"" + uni.convert( "취소" ) + "\" onClick=\"javaScript:window.history.go(-1);\"></td></tr>" );
				out.println( "</form></table>" );

		out.println("</center></body>");
		out.println("</html>");	
	}
/*
	public void preview( HttpServletRequest req, PrintWriter out) throws UnsupportedEncodingException {
		try{		
			InputStreamReader ir = new InputStreamReader( req.getInputStream() ); 
			BufferedReader in = new BufferedReader( ir );
			String str = null;
			int num = 0;
			while( (str= in.readLine() ) != null ) {
				num++;
				out.println( str + " num:" + num + "\n" );
			}
	
		} catch ( IOException e ) {
			out.println( e.toString() );
		}catch( Exception e ){
			out.println( e.toString() );
		}		
	}*/ 
}
