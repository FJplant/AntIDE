package com.antsoft.namecard;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class UserServlet extends HttpServlet{

	//html tag 정의	
	static String htmlHeader = "<html><head><title></title></head><body>";
	static String htmlEnd = "</body></html>";

	static IDManager idmanager = new IDManager();
	static MemberInfoManager membermanager = new MemberInfoManager();
	static ComInfoManager commanager = new ComInfoManager();
	static CardBoxManager boxmanager = new CardBoxManager();

	Uni2Asc asc = new Uni2Asc();

	public void doGet ( HttpServletRequest req , HttpServletResponse res )
	throws ServletException , IOException
	{
		HttpSession httpSession = req.getSession(true);

		res.setContentType("text/html");

		PrintWriter out = res.getWriter() ;

		if ( httpSession == null ) {
			out.println("<h1>Unauthorized access.</h1>");
			return;
		}

		//cmd받아오기
		String cmd = req.getParameter("cmd");
		if(cmd == null || cmd.equals( "" )){
			out.println("<h1>Invalid command</h1>");
			return;
		}

		//cmd 체크해서 해당 함수 호출하기
		//System.out.println(cmd);
		try{

			//등록버튼을 누르면 등록 폼 보여주기
			if(cmd.equals("addPersonForm")){
				addPersonForm(out);
			}//디렉토리에 들어있는 명함리스트 보여주기
			else if(cmd.equals("showCardList")){
				boxmanager.showCardList(req, out);
			}//선택된 명함 보여주는 함수 부르기
			else if(cmd.equals("showNameCard")){
				boxmanager.showNameCard(req, out);
			}//검색한 명함 보여주는 함수 부르기 
			else if(cmd.equals("showSearchCard")){
				boxmanager.showSearchCard(req, out);
			} //회사정보수정하는 폼 부르기
			else if(cmd.equals("comInfoForm")){
				commanager.comInfoForm(req, out);
			}
			else if(cmd.equals("showDirList")){
				boxmanager.showDirList(req, out);
			}
			else if(cmd.equals("updatePersonForm")){
				membermanager.updatePersonForm(req, out);
			}
			else if(cmd.equals("jobChangeForm")){
				membermanager.jobChangeForm(req, out);
			}
			else if(cmd.equals("jobChangeCheck")){
				membermanager.jobChangeCheck(req, out);
			}
			else if(cmd.equals("checkDeleteID")){
				idmanager.checkDeleteID(req, out);
			}else if(cmd.equals("logout")){
				idmanager.logout( req, out );
			}
			
		}catch( CommandException e ){
			out.println( "command Error!" + e.toString() );
		}

		out.close();
	}



	public void doPost ( HttpServletRequest req , HttpServletResponse res )
	throws ServletException , IOException
	{
		//session 받아오기
		HttpSession httpSession = req.getSession(true);

		res.setContentType("text/html");

		PrintWriter out =  res.getWriter();
		

    	//cmd받아오기
		String cmd = req.getParameter("cmd");
		if(cmd == null || cmd == ""){
			out.println("<h1>Invalid command</h1>");
			return;
		}

		//cmd 체크해서 해당 함수 호출하기

		try{
			//첫화면에서 로그인을 클릭하면 가입자 메뉴보여주기
			if(cmd.equals("login")){
				idmanager.login(req, out);
			} // logout 버튼을 누르면 초기화면을 보여주기
			else if( cmd.equals("logout")){
				idmanager.logout( req, out);
			}//등록폼에서 회사검색버튼을 누르면 검색 함수 부르기
			else if(cmd.equals("checkCompany")){
				idmanager.checkCompany(req, out);
			}//등록폼에서 정보 다 입력하고 마지막 제출버튼을 누르면 어드민 큐에 등록하는 함수 부르기 
			else if(cmd.equals("registerMemberPerson")){
				idmanager.registerMemberPerson(req, out);
			}//디렉토리 추가 함수 부르기
  			else if(cmd.equals("addDir")){
				boxmanager.addDir(req, out);
			}//디렉토리 삭제 함수 부르기
			else if(cmd.equals("deleteDir")){
				boxmanager.deleteDir(req, out);
			}//디렉토리 이름 바꾸는 함수 부르기
			else if(cmd.equals("renameDir")){
				boxmanager.renameDir(req, out);
			}//명함 추가하는 폼 부르기
			else if(cmd.equals("addCardForm")){
				addCardForm(req, out);
			}//명함 추가하는 함수 부르기
			else if(cmd.equals("addNewCard")){
				boxmanager.addNewCard(req, out);
			}//명함 삭제하는 함수 부르기
			else if(cmd.equals("deleteCard")){
				boxmanager.deleteCard(req, out);
			}//명함 옮기는 함수 부르기
			else if(cmd.equals("moveCard")){
				boxmanager.moveCard(req, out);
			}//명함수정폼을 보여주는 함수 부르기
		 	else if(cmd.equals("updateCardForm")){
				boxmanager.updateCardForm(req, out);
			}//명함내용을 수정하는 함수 부르기
			else if(cmd.equals("updateCard")){
				boxmanager.updateCard(req, out);
			}//개인 명함함 검색하는 함수 부르기
			else if(cmd.equals("search")){
				boxmanager.search(req, out);
			}
			else if(cmd.equals("registerComInfo")){
				commanager.registerComInfo(req, out);
			}
			else if(cmd.equals("addPublicCard")){
				boxmanager.addPublicCard(req, out);
			}
			else if(cmd.equals("updatePersonInfo")){
				membermanager.updatePersonInfo(req, out);
			}
			else if(cmd.equals("jobChangeCheck")){
				membermanager.jobChangeCheck(req, out);
			}
			else if(cmd.equals("jobChange")){
				membermanager.jobChange(req, out);
			}
			else if(cmd.equals("deleteMemberPerson")){
				idmanager.deleteMemberPerson(req, out);
			}
			else if(cmd.equals("selectCardColorForm")){
				boxmanager.selectCardColorForm( req, out );
			}
			else if(cmd.equals("selectCardColor")){
				boxmanager.selectCardColor( req, out );
			}
		}catch( CommandException e ){
			out.println("command Error!"+e.toString());
		}

		out.close();

	}

	/** 
		새로운 사용자를 받기위한 등록폼
	*/
	public void addPersonForm( PrintWriter out )throws CommandException, UnsupportedEncodingException
	{
		out.println("<html><head><title>Untitled Document</title><meta http-equiv=Content-Type content=text/html; charset=euc-kr></head>");
		out.println("</head>");
		out.println("<body background=/bg.jpg>");
		out.println("<table border=0 cellspacing=0>");
  	  	out.println("<tr>");
    	out.println("<td height=28 align=center valign=middle><font size=4 color=#ffff00>");
    	out.println("<img src=/icon.gif><b>" + asc.convert( "개인정보" ) + "</b></font></td>");
  	  	out.println("</tr>");
  		out.println("<tr>");
		out.println("<td align=center valign=top>");
		out.println("<form method=post action=/namecard/UserServlet >");
		out.println("<input type=hidden name=cmd value=checkCompany>");
		out.println("<br><p>");
		out.println("<table border=0 width=80% align=center cellspacing=0>");
		out.println("<tr><td colspan=3 align=center><font size=2 color=#e6e6fa>" + asc.convert( "* 표가 되어있는 항목은 반드시 기입해 주세요." ) );
		out.println("</font><br><p></td></tr>");
		out.println("<tr>"); 
		out.println("<td><font size=2 color=#e0ffff><b>*</td>");
		out.println("<td><font color=#fffacd size=2><b>ID</td>");
		out.println("<td> <input type=text name=id size=10 maxlength=10></td>");
		out.println("</tr>");
		out.println("<tr>");
		out.println("<td><font size=2 color=#e0ffff><b>*</font></td>");
		out.println("<td><font color=#fffacd size=2><b>" + asc.convert( "패스워드" ) + "</b></font></td>");
		out.println("<td><input type=password name=password size=10 maxlength=10></td>");
		out.println("</tr>");
		out.println("<tr><td><font size=2 color=#e0ffff><b>*</font></td>");
		out.println("<td><font color=#fffacd size=2><b>" + asc.convert( "패스워드확인용" ) + "</td>");
		out.println("<td><input type=password name=checkPasswd size=10 maxlength=10></td></tr>");
		out.println("<tr>");
		out.println("<td><font size=2 color=#e0ffff><b>*</font></td>");
		out.println("<td><font color=#fffacd size=2><b>" + asc.convert( "이름" ) + "</b></td>");
		out.println("<td><input type=text name=name size=10 maxlength=10></td>");
		out.println("</tr><tr>");
		out.println("<td><font size=2 color=#e0ffff><b>*</font></td>");
		out.println("<td><font color=#fffacd size=2><b>" + asc.convert( "주민등록번호" ) + "</b></td>");
		out.println("<td><input type=text name=ssn size=13 maxlength=13>");
		out.println("<font size=2 color=#000080>" + asc.convert( "주민등록번호 입력은") + "</font><font size=2 color=#e0ffff>" + asc.convert(" 가운데 '-'를 빼고 입력하세요." ) + "</td>"); 
		out.println("</tr>");
		out.println("<tr>");
		out.println("<td><font size=2 color=#e0ffff><b>*</font></td>");
		out.println("<td><font color=#fffacd size=2><b>" + asc.convert( "생년월일" ) + "</b></td>");
		out.println("<td><input type=text name=birth size=10 maxlength=10>");
		out.println(" <font size=2 color=#000080>ex) xxxx/xx/xx</td>");
		out.println("</tr></td>");
		out.println("<td></td><td><font color=#fffacd size=2><b>" + asc.convert( "성별" ) + "</b></td>");
		out.println("<td><font color=#fffacd size=2><b>" + asc.convert( "남" ) + "<input type=radio name=sex checked value=m>" + asc.convert( "여" ) + "<input type=radio name=sex value=f></b></td>");
		out.println("</tr>");
		out.println("<tr>");
		out.println("<td><font size=2 color=#e0ffff><b>*</font></td>");
		out.println("<td><font color=#fffacd size=2><b>" + asc.convert( "주소" ) + "</b></td>");
		out.println("<td> <input type=text name=address size=40></td>");
		out.println("</tr><tr>");
		out.println("<td></td><td><font color=#fffacd size=2><b>" + asc.convert( "전화" ) + "</b></td>");
		out.println("<td> <input type=text name=tel size=12> </td>");
		out.println("</tr>");
		out.println("<tr>");
		out.println("<td></td><td><font color=#fffacd size=2><b>" + asc.convert( "삐삐" ) + "</b></td>");
		out.println("<td> <input type=text name=beeper size=13></td>");
		out.println("</tr><tr>");
		out.println("<td></td><td><font color=#fffacd size=2><b>" + asc.convert( "휴대폰" ) + "</b></td>");
		out.println("<td> <input type=text name=handphone size=13></td>");
		out.println("</tr>");
		out.println("<tr>");
		out.println("<td></td><td><font color=#fffacd size=2><b>" + asc.convert( "홈페이지" ) + "</b></td>");
		out.println("<td><input type=text name=homepage size=40></td>");
		out.println("</tr><tr>");
		out.println("<td><font size=2 color=#e0ffff><b>*</font></td>");
		out.println("<td><font color=#fffacd size=2><b>e-mail<b></td>");
		out.println("<td><input type=text name=email size=40></td>");
		out.println("</tr>");
		out.println("<tr>");
		out.println("<td><font size=2 color=#e0ffff><b>*</font></td>");
		out.println("<td><font color=#fffacd size=2><b>" + asc.convert( "지위" ) + "</b></td>");
		out.println("<td><input type=text name=position size=20></td>");
		out.println("</tr><tr>");
		out.println("<td></td><td><font color=#fffacd size=2><b>" + asc.convert( "부서" ) + "</b></td>");
		out.println("<td><input type=text name=part size=20></td>");
		out.println("</tr>");
		out.println("<tr>");
		out.println("<td><font size=2 color=#e0ffff><b>*</font></td>");
		out.println("<td><font color=#fffacd size=2><b>" + asc.convert( "회사개인전화" ) + "</b></td> ");
		out.println("<td><input type=text name=c_tel size=13>");
		out.println("<font size=2 color=#e0ffff>" + asc.convert( "회사개인전화가 없을시 회사전화번호를 입력하세요." ) + " </td>");
		out.println("</tr><tr>");
		out.println("<td></td><td><font color=#fffacd size=2><b>" + asc.convert( "회사팩스" ) + "</b></td>");
		out.println("<td><input type=text name=c_fax size=13></td>");
		out.println("</tr>");
		
		out.println("<tr>");
		out.println("<td></td><td><font color=#fffacd size=2><b>description</b></font></td>");
		out.println("<td><font size=2 color=#e0ffff>description" + asc.convert( "은 위의 사항 이외에 자신에 대한 간단한 소개나 경력을 기입할 수 있는 곳입니다." ) + "</td></tr>"); 
		out.println("<td colspan=3><textarea name=description cols=50 rows=4></textarea></td>");
		out.println("</tr>");
	    out.println("</table><br><p></td>");
	    out.println("</tr>"); 
	    out.println("<tr>");
	    out.println("<td align=center height=28><b><font size=4 color=#ffff00><img src=/icon.gif>" + asc.convert( "회사검색" ) + "</b></td>");
		out.println("</tr>");
		out.println("<tr>");  
		out.println("<td valign=top><br><p><center><font size=2 color=#e6e6fa>");
		out.println( asc.convert( "자신이 다니고 있는 회사가 이미 등록되어 있는 회사인지 검색해 주세요") + "<br></font><br><p>");   
		out.println("<table border=0 width=90% cellspacing=0>");
		out.println("<tr>");
		out.println("<td><font size=2 color=#e0ffff><b>*</font></td>");
		out.println("<td><font color=#fffacd size=2><b>" + asc.convert( "회사이름" ) + "</b></td>");
		out.println("<td> <input type=text name=s_name size=20></td>");
		out.println("<td><font size=2 color=#e0ffff><b>*</font></td>");
		out.println("<td><font color=#fffacd size=2><b>" + asc.convert( "지역" ) + "</b></td>");
		out.println("<td><select name=s_region size=1>");
		out.println("<option value=1>" + asc.convert( "서울특별시" ) + "</option><option value=2>" + asc.convert( "부산광역시" ) + "</option>");
		out.println("<option value=3>" + asc.convert( "대구광역시" ) + "</option><option value=4>" + asc.convert( "인천광역시" ) + "</option>");
		out.println("<option value=5>" + asc.convert( "광주광역시" ) + "</option><option value=6>" + asc.convert( "울산광역시" ) + "</option>");
		out.println("<option value=7>" + asc.convert( "대전광역시" ) + "</option><option value=8>" + asc.convert( "경기" ) + "</option>");
		out.println("<option value=9>" + asc.convert( "강원" ) + "</option><option value=10>" + asc.convert( "충북" ) + "</option>");
		out.println("<option value=11>" + asc.convert( "충남" ) + "</option><option value=12>" + asc.convert( "전북" ) + "</option>");
		out.println("<option value=13>" + asc.convert( "전남" ) + "</option><option value=14>" + asc.convert( "경북" ) + "</option>");
		out.println("<option value=15>" + asc.convert( "경남" ) + "</option><option value=16>" + asc.convert( "제주" ) + "</option>");
		out.println("</select> </td>");
		out.println("</tr>");
		out.println("<tr>");
		out.println("<td></td><td><font color=#fffacd size=2><b>" + asc.convert( "지점" ) + "</b></td>");
		out.println("<td><input type=text name=s_site size=20></td>");
		out.println("<td><font size=2 color=#e0ffff><b>*</td>");
		out.println("<td><font color=#fffacd size=2><b>" + asc.convert( "업종" ) + "</b></font></td>");
		out.println("<td><select name=j_kind size=1>");
        out.println("<option value=1>" + asc.convert( "공무원" ) + "</option>");
        out.println("<option value=2>" + asc.convert( "교원" ) + "</option>");
        out.println("<option value=3>" + asc.convert( "의료" ) + "</option>");
        out.println("<option value=4>" + asc.convert( "유통" ) + "</option>");
        out.println("<option value=5>" + asc.convert( "기계/자동차" ) + "</option>");
        out.println("<option value=6>" + asc.convert( "컴퓨터/통신" ) + "</option>");
        out.println("<option value=7>" + asc.convert( "서비스업" ) + "</option>");
        out.println("<option value=8>" + asc.convert( "금융" ) + "</option>");
        out.println("<option value=9>" + asc.convert( "건축/인테리어" ) + "</option>");
        out.println("<option value=10>" + asc.convert( "방송" ) + "</option>");
        out.println("<option value=11>" + asc.convert( "예술" ) + "</option>");
        out.println("<option value=12>" + asc.convert( "무역" ) + "</option>");
        out.println("<option value=13>" + asc.convert( "스포츠" ) + "</option>");
        out.println("<option value=14>" + asc.convert( "프리랜서" ) + "</option>");
        out.println("<option value=15>" + asc.convert( "기타" ) + "</option>");
        out.println("</select></td></tr>"); 
		out.println("<tr>");
		out.println("<td colspan=6 align=center><br><input type=submit value='" + asc.convert( "검색" ) + "'>");
		out.println("<input type=button value='" + asc.convert( "취소" ) + "' onClick=\"window.history.go(-1);\"></td>" );
		out.println("</tr>");
		out.println("</table></center></td>");
		out.println("</tr>");
		out.println("</table></form>");
		out.println("</body>");
		out.println("</html>");
	}

	//가입자가 로그인 한후 첫메뉴보여주기	
	public void showMenu( PrintWriter out ) 
	{
				out.println("<html><head><title></title><meta http-equiv=Content-Type content=text/html; charset=euc-kr></head>");
		out.println("<body bgcolor=#FFFFFF><div align=left></div>");
		out.println("<table border=0 width=580>");
		  out.println("<tr>"); 
		    out.println("<td width=140 bgcolor=#FFFFFF align=left valign=top rowspan=2 bordercolor=#FFFFFF><img src=c:\\temp\\antsoft_logo.JPG width=140 height=50></td>");
			out.println("<td bgcolor=#FFFFFF height=21 bordercolor=#FFFFFF>&nbsp;</td></tr>");
		  out.println("<tr><td bgcolor=#0C0A55 colspan=2 bordercolor=#FFFFFF><font color=#FFFFFF></font>"); 
		    out.println("<table border=0  cellspacing=0 align=center>");
			  out.println("<tr><td width=21%><font color=#FFFFFF size=2><b>개인명함함</b></font></td>");
		 	  out.println("<td width=24%><font color=#FFFFFF size=2><b>개인정보수정</b></font></td>");
			  out.println("<td bordercolor=#0C0A55 width=24%><font color=#FFFFFF size=2><b>회사정보수정</b></font></td>");
			  out.println("<td width=17%><font size=2 color=#FFFFFF><b>회사이전</b></font></td>");
		      out.println("<td width=14%><b><font size=2 color=#FFFFFF>가입해지</font></b></td>");
		    out.println("</tr></table></td></tr>");
		    out.println("<tr><td width=140 bgcolor=#8080C0 height=500 bordercolor=#8080C0 valign=top><p>&nbsp;</p>");
			  out.println("<table border=0 width=90% align=center cellspacing=0><tr>"); 
			  out.println("<td bgcolor=#7B007B height=20 align=center valign=middle>"); 
			  out.println("<div align=center><b><font color=#FFFFFF size=2>명함검색</font></b></div></td></tr>");
			  out.println("<tr><td bgcolor=#8080C0 height=55><font size=1 color=#CCFFFF>개인디렉토리에서만 검색을 하려면 private를 전체 검색을 하려면 public를 누르세요</font></td></tr>");
			  out.println("<tr><td height=23><font size=2 color=#FFFFFF>이름</font></td></tr>");
			  out.println("<tr><td><input type=text name=name size=5 maxlength=7></td></tr>");
			  out.println("<tr><td height=23><font color=#FFFFFF size=2>회사이름</font></td></tr>");
			  out.println("<tr><td><input type=text name=c_name size=8></td></tr>");
			  out.println("<tr><td height=23><font color=#FFFFFF size=2>업종</font></td></tr>");
			  out.println("<tr><td><select name=select size=1>");
			  out.println("<option value=1>공무원</option><option value=2>교원</option>");
			  out.println("<option value=3>의료</option><option value=4>유통</option>");
			  out.println("<option value=5>기계/자동차</option><option value=6>컴퓨터/통신</option>");
			  out.println("<option value=7>서비스업</option><option value=8>금융</option>");
			  out.println("<option value=9>건축/인테리어</option><option value=10>방송</option>");
			  out.println("<option value=11>예술</option><option value=12>무역</option>");
			  out.println("<option value=13>스포츠</option><option value=14>프리랜서</option>");
			  out.println("<option value=15>기타</option></select></td></tr>");
			  out.println("<tr><td height=47><input type=submit value=public>");
			  out.println("<input type=submit value=private></td></tr></table></td>");
			  out.println("<td bgcolor=#FFFFFF width=300 height=500 valign=top align=center><p></p><p>&nbsp;</p>");
			  out.println("<table border=0 width=90% cellspacing=5>");
			  out.println("<tr><td bgcolor=#7B007B height=21 align=center width=50%><b><font color=#FFFFFF size=2>디렉토리 이름</font></b></td>");
			  out.println("<td bgcolor=#7B007B height=21 align=center width=50%><b><font color=#FFFFFF size=2>명함수</font></b></td></tr>");
		
		// iteration은 니가 알아서
			out.println("<tr><td height=20>/* 디렉토리 이름*/</td>");
			out.println("<td height=20>/* 명함수 */</td></tr></table></td>");
			out.println("<td width=140 bgcolor=#8080C0 height=500 bordercolor=#8080C0>&nbsp;</td></tr></table></body></html>");
	}


	/** 
	 *	addCardForm - 개인명함함에 명함을 등록시키는 폼
	 *  @param req	폼에서 입력받은 데이터가 들어있는 곳
	 *  @param out	결과를 출력할 곳
     *
	 */
	public void addCardForm(HttpServletRequest req, PrintWriter out) throws UnsupportedEncodingException
	{
  
		String dir_id = req.getParameter("dir_id");
		out.println("<html><head><title>Untitled Document</title><meta http-equiv=Content-Type content=text/html; charset=euc-kr></head>");
		out.println("</head>");
		out.println("<body background=/bg.jpg>");
		out.println("<table border=0 cellspacing=0 width=670>");
  	  	out.println("<tr>");
    	out.println("<td height=28 align=center valign=middle><font size=4 color=#ffff00>");
    	out.println("<img src=/icon.gif><b>" + asc.convert( "개인정보" ) + "</b></font></td>");
  	  	out.println("</tr>");
  		out.println("<tr>");
		out.println("<td align=center valign=top>");
		out.println("<form method=post action=/namecard/UserServlet >");
		out.println("<input type=hidden name=cmd value=addNewCard>");
		out.println("<input type=hidden name=dir_id value='" + dir_id + "'>");
		out.println("<br><p>");
		out.println("<table border=0 width=80% align=center cellspacing=0>");
		out.println("<tr><td colspan=3 align=center><font size=2 color=#e6e6fa>" + asc.convert( "* 표가 되어있는 항목은 반드시 기입해 주세요." ) );
		out.println("</font><br><p></td></tr>");
		out.println("<tr>");
		out.println("<td><font size=2 color=#e0ffff><b>*</font></td>");
		out.println("<td><font color=#fffacd size=2><b>" + asc.convert( "이름" ) + "</b></td>");
		out.println("<td><input type=text name=name size=10 maxlength=10></td>");
		out.println("</tr>");
		out.println("<tr>");
		out.println("<td></td><td><font color=#fffacd size=2><b>" + asc.convert( "전화" ) + "</b></td>");
		out.println("<td> <input type=text name=tel size=12> </td>");
		out.println("</tr>");
		out.println("<tr>");
		out.println("<td></td><td><font color=#fffacd size=2><b>" + asc.convert( "삐삐" ) + "</b></td>");
		out.println("<td> <input type=text name=beeper size=13></td>");
		out.println("</tr><tr>");
		out.println("<td></td><td><font color=#fffacd size=2><b>" + asc.convert( "휴대폰" ) + "</b></td>");
		out.println("<td> <input type=text name=handphone size=13></td>");
		out.println("</tr>");
		out.println("<tr>");
		out.println("<td></td><td><font color=#fffacd size=2><b>" + asc.convert( "홈페이지" ) + "</b></td>");
		out.println("<td><input type=text name=homepage size=40></td>");
		out.println("</tr><tr>");
		out.println("<td><font size=2 color=#e0ffff><b></font></td>");
		out.println("<td><font color=#fffacd size=2><b>email<b></td>");
		out.println("<td><input type=text name=email size=40></td>");
		out.println("</tr>");
		out.println("<tr>");
		out.println("<td><font size=2 color=#e0ffff><b>*</font></td>");
		out.println("<td><font color=#fffacd size=2><b>" + asc.convert( "지위" ) + "</b></td>");
		out.println("<td><input type=text name=position size=20></td>");
		out.println("</tr><tr>");
		out.println("<td></td><td><font color=#fffacd size=2><b>" + asc.convert( "부서" ) + "</b></td>");
		out.println("<td><input type=text name=part size=20></td>");
		out.println("</tr>");
		out.println("<tr>");
		out.println("<td><font size=2 color=#e0ffff><b>*</font></td>");
		out.println("<td><font color=#fffacd size=2><b>" + asc.convert( "회사개인전화" ) + "</b></td> ");
		out.println("<td><input type=text name=c_tel size=13>");
		out.println("<font size=2 color=#e0ffff>" + asc.convert( "회사개인전화가 없을시 회사전화번호를 입력하세요." ) + " </td>");
		out.println("</tr><tr>");
		out.println("<td></td><td><font color=#fffacd size=2><b>" + asc.convert( "회사팩스" ) + "</b></td>");
		out.println("<td><input type=text name=c_fax size=13></td>");
		out.println("</tr>");
	    out.println("</table><br><p></td>");
	    out.println("</tr>"); 
	    out.println("<tr>");
	    out.println("<td align=center height=28><b><font size=4 color=#ffff00><img src=/icon.gif>" + asc.convert( "회사정보" ) + "</b><br><p></td>");
		out.println("</tr>");
		out.println("<tr><td align=center>");
		out.println("<table border=0 width=80% cellspacing=0>");
		out.println("<tr>");
		out.println("<td><font size=2 color=#e0ffff><b>*</font></td>");
		out.println("<td><font color=#fffacd size=2><b>" + asc.convert( "회사이름" ) + "</b></td>");
		out.println("<td> <input type=text name=c_name size=20></td>");
		out.println("<td><font size=2 color=#e0ffff><b>*</font></td>");
		out.println("<td><font color=#fffacd size=2><b>" + asc.convert( "지역" ) + "</b></td>");
		out.println("<td><select name=c_region size=1>");
		out.println("<option value=1>" + asc.convert( "서울특별시" ) + "</option><option value=2>" + asc.convert( "부산광역시" ) + "</option>");
		out.println("<option value=3>" + asc.convert( "대구광역시" ) + "</option><option value=4>" + asc.convert( "인천광역시" ) + "</option>");
		out.println("<option value=5>" + asc.convert( "광주광역시" ) + "</option><option value=6>" + asc.convert( "울산광역시" ) + "</option>");
		out.println("<option value=7>" + asc.convert( "대전광역시" ) + "</option><option value=8>" + asc.convert( "경기" ) + "</option>");
		out.println("<option value=9>" + asc.convert( "강원" ) + "</option><option value=10>" + asc.convert( "충북" ) + "</option>");
		out.println("<option value=11>" + asc.convert( "충남" ) + "</option><option value=12>" + asc.convert( "전북" ) + "</option>");
		out.println("<option value=13>" + asc.convert( "전남" ) + "</option><option value=14>" + asc.convert( "경북" ) + "</option>");
		out.println("<option value=15>" + asc.convert( "경남" ) + "</option><option value=16>" + asc.convert( "제주" ) + "</option>");
		out.println("</select> </td>");
		out.println("</tr>");
		out.println("<tr>");
		out.println("<td></td><td><font color=#fffacd size=2><b>" + asc.convert( "지점" ) + "</b></td>");
		out.println("<td><input type=text name=c_site size=20></td>");
		out.println("<td><font size=2 color=#e0ffff><b>*</td>");
		out.println("<td><font color=#fffacd size=2><b>" + asc.convert( "업종" ) + "</b></font></td>");
		out.println("<td><select name=j_kind size=1>");
        out.println("<option value=1>" + asc.convert( "공무원" ) + "</option>");
        out.println("<option value=2>" + asc.convert( "교원" ) + "</option>");
        out.println("<option value=3>" + asc.convert( "의료" ) + "</option>");
        out.println("<option value=4>" + asc.convert( "유통" ) + "</option>");
        out.println("<option value=5>" + asc.convert( "기계/자동차" ) + "</option>");
        out.println("<option value=6>" + asc.convert( "컴퓨터/통신" ) + "</option>");
        out.println("<option value=7>" + asc.convert( "서비스업" ) + "</option>");
        out.println("<option value=8>" + asc.convert( "금융" ) + "</option>");
        out.println("<option value=9>" + asc.convert( "건축/인테리어" ) + "</option>");
        out.println("<option value=10>" + asc.convert( "방송" ) + "</option>");
        out.println("<option value=11>" + asc.convert( "예술" ) + "</option>");
        out.println("<option value=12>" + asc.convert( "무역" ) + "</option>");
        out.println("<option value=13>" + asc.convert( "스포츠" ) + "</option>");
        out.println("<option value=14>" + asc.convert( "프리랜서" ) + "</option>");
        out.println("<option value=15>" + asc.convert( "기타" ) + "</option>");
        out.println("</select></td></tr>"); 
		out.println("<tr>");
		out.println("<td><font size=2 color=#e0ffff><b>*</font></td>");
		out.println("<td><font color=#fffacd size=2><b>" + asc.convert( "주소" ) + "</b></td>");
		out.println("<td colspan=5> <input type=text name=address size=40></td></tr>");
		out.println("<tr>");
		out.println("<td colspan=6 align=center><br><input type=submit value='" + asc.convert( "등록" ) + "'>");
		out.println("<input type=button value='" + asc.convert( "취소" ) + "' onClick=\"window.history.go(-1);\"></td>" );
		out.println("</tr>");
		out.println("</table></center></td>");
		out.println("</tr>");
		out.println("</table></form>");
		out.println("</body>");
		out.println("</html>");
	}
}

