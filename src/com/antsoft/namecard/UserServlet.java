package com.antsoft.namecard;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class UserServlet extends HttpServlet{

	//html tag ����	
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

		//cmd�޾ƿ���
		String cmd = req.getParameter("cmd");
		if(cmd == null || cmd.equals( "" )){
			out.println("<h1>Invalid command</h1>");
			return;
		}

		//cmd üũ�ؼ� �ش� �Լ� ȣ���ϱ�
		//System.out.println(cmd);
		try{

			//��Ϲ�ư�� ������ ��� �� �����ֱ�
			if(cmd.equals("addPersonForm")){
				addPersonForm(out);
			}//���丮�� ����ִ� ���Ը���Ʈ �����ֱ�
			else if(cmd.equals("showCardList")){
				boxmanager.showCardList(req, out);
			}//���õ� ���� �����ִ� �Լ� �θ���
			else if(cmd.equals("showNameCard")){
				boxmanager.showNameCard(req, out);
			}//�˻��� ���� �����ִ� �Լ� �θ��� 
			else if(cmd.equals("showSearchCard")){
				boxmanager.showSearchCard(req, out);
			} //ȸ�����������ϴ� �� �θ���
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
		//session �޾ƿ���
		HttpSession httpSession = req.getSession(true);

		res.setContentType("text/html");

		PrintWriter out =  res.getWriter();
		

    	//cmd�޾ƿ���
		String cmd = req.getParameter("cmd");
		if(cmd == null || cmd == ""){
			out.println("<h1>Invalid command</h1>");
			return;
		}

		//cmd üũ�ؼ� �ش� �Լ� ȣ���ϱ�

		try{
			//ùȭ�鿡�� �α����� Ŭ���ϸ� ������ �޴������ֱ�
			if(cmd.equals("login")){
				idmanager.login(req, out);
			} // logout ��ư�� ������ �ʱ�ȭ���� �����ֱ�
			else if( cmd.equals("logout")){
				idmanager.logout( req, out);
			}//��������� ȸ��˻���ư�� ������ �˻� �Լ� �θ���
			else if(cmd.equals("checkCompany")){
				idmanager.checkCompany(req, out);
			}//��������� ���� �� �Է��ϰ� ������ �����ư�� ������ ���� ť�� ����ϴ� �Լ� �θ��� 
			else if(cmd.equals("registerMemberPerson")){
				idmanager.registerMemberPerson(req, out);
			}//���丮 �߰� �Լ� �θ���
  			else if(cmd.equals("addDir")){
				boxmanager.addDir(req, out);
			}//���丮 ���� �Լ� �θ���
			else if(cmd.equals("deleteDir")){
				boxmanager.deleteDir(req, out);
			}//���丮 �̸� �ٲٴ� �Լ� �θ���
			else if(cmd.equals("renameDir")){
				boxmanager.renameDir(req, out);
			}//���� �߰��ϴ� �� �θ���
			else if(cmd.equals("addCardForm")){
				addCardForm(req, out);
			}//���� �߰��ϴ� �Լ� �θ���
			else if(cmd.equals("addNewCard")){
				boxmanager.addNewCard(req, out);
			}//���� �����ϴ� �Լ� �θ���
			else if(cmd.equals("deleteCard")){
				boxmanager.deleteCard(req, out);
			}//���� �ű�� �Լ� �θ���
			else if(cmd.equals("moveCard")){
				boxmanager.moveCard(req, out);
			}//���Լ������� �����ִ� �Լ� �θ���
		 	else if(cmd.equals("updateCardForm")){
				boxmanager.updateCardForm(req, out);
			}//���Գ����� �����ϴ� �Լ� �θ���
			else if(cmd.equals("updateCard")){
				boxmanager.updateCard(req, out);
			}//���� ������ �˻��ϴ� �Լ� �θ���
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
		���ο� ����ڸ� �ޱ����� �����
	*/
	public void addPersonForm( PrintWriter out )throws CommandException, UnsupportedEncodingException
	{
		out.println("<html><head><title>Untitled Document</title><meta http-equiv=Content-Type content=text/html; charset=euc-kr></head>");
		out.println("</head>");
		out.println("<body background=/bg.jpg>");
		out.println("<table border=0 cellspacing=0>");
  	  	out.println("<tr>");
    	out.println("<td height=28 align=center valign=middle><font size=4 color=#ffff00>");
    	out.println("<img src=/icon.gif><b>" + asc.convert( "��������" ) + "</b></font></td>");
  	  	out.println("</tr>");
  		out.println("<tr>");
		out.println("<td align=center valign=top>");
		out.println("<form method=post action=/namecard/UserServlet >");
		out.println("<input type=hidden name=cmd value=checkCompany>");
		out.println("<br><p>");
		out.println("<table border=0 width=80% align=center cellspacing=0>");
		out.println("<tr><td colspan=3 align=center><font size=2 color=#e6e6fa>" + asc.convert( "* ǥ�� �Ǿ��ִ� �׸��� �ݵ�� ������ �ּ���." ) );
		out.println("</font><br><p></td></tr>");
		out.println("<tr>"); 
		out.println("<td><font size=2 color=#e0ffff><b>*</td>");
		out.println("<td><font color=#fffacd size=2><b>ID</td>");
		out.println("<td> <input type=text name=id size=10 maxlength=10></td>");
		out.println("</tr>");
		out.println("<tr>");
		out.println("<td><font size=2 color=#e0ffff><b>*</font></td>");
		out.println("<td><font color=#fffacd size=2><b>" + asc.convert( "�н�����" ) + "</b></font></td>");
		out.println("<td><input type=password name=password size=10 maxlength=10></td>");
		out.println("</tr>");
		out.println("<tr><td><font size=2 color=#e0ffff><b>*</font></td>");
		out.println("<td><font color=#fffacd size=2><b>" + asc.convert( "�н�����Ȯ�ο�" ) + "</td>");
		out.println("<td><input type=password name=checkPasswd size=10 maxlength=10></td></tr>");
		out.println("<tr>");
		out.println("<td><font size=2 color=#e0ffff><b>*</font></td>");
		out.println("<td><font color=#fffacd size=2><b>" + asc.convert( "�̸�" ) + "</b></td>");
		out.println("<td><input type=text name=name size=10 maxlength=10></td>");
		out.println("</tr><tr>");
		out.println("<td><font size=2 color=#e0ffff><b>*</font></td>");
		out.println("<td><font color=#fffacd size=2><b>" + asc.convert( "�ֹε�Ϲ�ȣ" ) + "</b></td>");
		out.println("<td><input type=text name=ssn size=13 maxlength=13>");
		out.println("<font size=2 color=#000080>" + asc.convert( "�ֹε�Ϲ�ȣ �Է���") + "</font><font size=2 color=#e0ffff>" + asc.convert(" ��� '-'�� ���� �Է��ϼ���." ) + "</td>"); 
		out.println("</tr>");
		out.println("<tr>");
		out.println("<td><font size=2 color=#e0ffff><b>*</font></td>");
		out.println("<td><font color=#fffacd size=2><b>" + asc.convert( "�������" ) + "</b></td>");
		out.println("<td><input type=text name=birth size=10 maxlength=10>");
		out.println(" <font size=2 color=#000080>ex) xxxx/xx/xx</td>");
		out.println("</tr></td>");
		out.println("<td></td><td><font color=#fffacd size=2><b>" + asc.convert( "����" ) + "</b></td>");
		out.println("<td><font color=#fffacd size=2><b>" + asc.convert( "��" ) + "<input type=radio name=sex checked value=m>" + asc.convert( "��" ) + "<input type=radio name=sex value=f></b></td>");
		out.println("</tr>");
		out.println("<tr>");
		out.println("<td><font size=2 color=#e0ffff><b>*</font></td>");
		out.println("<td><font color=#fffacd size=2><b>" + asc.convert( "�ּ�" ) + "</b></td>");
		out.println("<td> <input type=text name=address size=40></td>");
		out.println("</tr><tr>");
		out.println("<td></td><td><font color=#fffacd size=2><b>" + asc.convert( "��ȭ" ) + "</b></td>");
		out.println("<td> <input type=text name=tel size=12> </td>");
		out.println("</tr>");
		out.println("<tr>");
		out.println("<td></td><td><font color=#fffacd size=2><b>" + asc.convert( "�߻�" ) + "</b></td>");
		out.println("<td> <input type=text name=beeper size=13></td>");
		out.println("</tr><tr>");
		out.println("<td></td><td><font color=#fffacd size=2><b>" + asc.convert( "�޴���" ) + "</b></td>");
		out.println("<td> <input type=text name=handphone size=13></td>");
		out.println("</tr>");
		out.println("<tr>");
		out.println("<td></td><td><font color=#fffacd size=2><b>" + asc.convert( "Ȩ������" ) + "</b></td>");
		out.println("<td><input type=text name=homepage size=40></td>");
		out.println("</tr><tr>");
		out.println("<td><font size=2 color=#e0ffff><b>*</font></td>");
		out.println("<td><font color=#fffacd size=2><b>e-mail<b></td>");
		out.println("<td><input type=text name=email size=40></td>");
		out.println("</tr>");
		out.println("<tr>");
		out.println("<td><font size=2 color=#e0ffff><b>*</font></td>");
		out.println("<td><font color=#fffacd size=2><b>" + asc.convert( "����" ) + "</b></td>");
		out.println("<td><input type=text name=position size=20></td>");
		out.println("</tr><tr>");
		out.println("<td></td><td><font color=#fffacd size=2><b>" + asc.convert( "�μ�" ) + "</b></td>");
		out.println("<td><input type=text name=part size=20></td>");
		out.println("</tr>");
		out.println("<tr>");
		out.println("<td><font size=2 color=#e0ffff><b>*</font></td>");
		out.println("<td><font color=#fffacd size=2><b>" + asc.convert( "ȸ�簳����ȭ" ) + "</b></td> ");
		out.println("<td><input type=text name=c_tel size=13>");
		out.println("<font size=2 color=#e0ffff>" + asc.convert( "ȸ�簳����ȭ�� ������ ȸ����ȭ��ȣ�� �Է��ϼ���." ) + " </td>");
		out.println("</tr><tr>");
		out.println("<td></td><td><font color=#fffacd size=2><b>" + asc.convert( "ȸ���ѽ�" ) + "</b></td>");
		out.println("<td><input type=text name=c_fax size=13></td>");
		out.println("</tr>");
		
		out.println("<tr>");
		out.println("<td></td><td><font color=#fffacd size=2><b>description</b></font></td>");
		out.println("<td><font size=2 color=#e0ffff>description" + asc.convert( "�� ���� ���� �̿ܿ� �ڽſ� ���� ������ �Ұ��� ����� ������ �� �ִ� ���Դϴ�." ) + "</td></tr>"); 
		out.println("<td colspan=3><textarea name=description cols=50 rows=4></textarea></td>");
		out.println("</tr>");
	    out.println("</table><br><p></td>");
	    out.println("</tr>"); 
	    out.println("<tr>");
	    out.println("<td align=center height=28><b><font size=4 color=#ffff00><img src=/icon.gif>" + asc.convert( "ȸ��˻�" ) + "</b></td>");
		out.println("</tr>");
		out.println("<tr>");  
		out.println("<td valign=top><br><p><center><font size=2 color=#e6e6fa>");
		out.println( asc.convert( "�ڽ��� �ٴϰ� �ִ� ȸ�簡 �̹� ��ϵǾ� �ִ� ȸ������ �˻��� �ּ���") + "<br></font><br><p>");   
		out.println("<table border=0 width=90% cellspacing=0>");
		out.println("<tr>");
		out.println("<td><font size=2 color=#e0ffff><b>*</font></td>");
		out.println("<td><font color=#fffacd size=2><b>" + asc.convert( "ȸ���̸�" ) + "</b></td>");
		out.println("<td> <input type=text name=s_name size=20></td>");
		out.println("<td><font size=2 color=#e0ffff><b>*</font></td>");
		out.println("<td><font color=#fffacd size=2><b>" + asc.convert( "����" ) + "</b></td>");
		out.println("<td><select name=s_region size=1>");
		out.println("<option value=1>" + asc.convert( "����Ư����" ) + "</option><option value=2>" + asc.convert( "�λ걤����" ) + "</option>");
		out.println("<option value=3>" + asc.convert( "�뱸������" ) + "</option><option value=4>" + asc.convert( "��õ������" ) + "</option>");
		out.println("<option value=5>" + asc.convert( "���ֱ�����" ) + "</option><option value=6>" + asc.convert( "��걤����" ) + "</option>");
		out.println("<option value=7>" + asc.convert( "����������" ) + "</option><option value=8>" + asc.convert( "���" ) + "</option>");
		out.println("<option value=9>" + asc.convert( "����" ) + "</option><option value=10>" + asc.convert( "���" ) + "</option>");
		out.println("<option value=11>" + asc.convert( "�泲" ) + "</option><option value=12>" + asc.convert( "����" ) + "</option>");
		out.println("<option value=13>" + asc.convert( "����" ) + "</option><option value=14>" + asc.convert( "���" ) + "</option>");
		out.println("<option value=15>" + asc.convert( "�泲" ) + "</option><option value=16>" + asc.convert( "����" ) + "</option>");
		out.println("</select> </td>");
		out.println("</tr>");
		out.println("<tr>");
		out.println("<td></td><td><font color=#fffacd size=2><b>" + asc.convert( "����" ) + "</b></td>");
		out.println("<td><input type=text name=s_site size=20></td>");
		out.println("<td><font size=2 color=#e0ffff><b>*</td>");
		out.println("<td><font color=#fffacd size=2><b>" + asc.convert( "����" ) + "</b></font></td>");
		out.println("<td><select name=j_kind size=1>");
        out.println("<option value=1>" + asc.convert( "������" ) + "</option>");
        out.println("<option value=2>" + asc.convert( "����" ) + "</option>");
        out.println("<option value=3>" + asc.convert( "�Ƿ�" ) + "</option>");
        out.println("<option value=4>" + asc.convert( "����" ) + "</option>");
        out.println("<option value=5>" + asc.convert( "���/�ڵ���" ) + "</option>");
        out.println("<option value=6>" + asc.convert( "��ǻ��/���" ) + "</option>");
        out.println("<option value=7>" + asc.convert( "���񽺾�" ) + "</option>");
        out.println("<option value=8>" + asc.convert( "����" ) + "</option>");
        out.println("<option value=9>" + asc.convert( "����/���׸���" ) + "</option>");
        out.println("<option value=10>" + asc.convert( "���" ) + "</option>");
        out.println("<option value=11>" + asc.convert( "����" ) + "</option>");
        out.println("<option value=12>" + asc.convert( "����" ) + "</option>");
        out.println("<option value=13>" + asc.convert( "������" ) + "</option>");
        out.println("<option value=14>" + asc.convert( "��������" ) + "</option>");
        out.println("<option value=15>" + asc.convert( "��Ÿ" ) + "</option>");
        out.println("</select></td></tr>"); 
		out.println("<tr>");
		out.println("<td colspan=6 align=center><br><input type=submit value='" + asc.convert( "�˻�" ) + "'>");
		out.println("<input type=button value='" + asc.convert( "���" ) + "' onClick=\"window.history.go(-1);\"></td>" );
		out.println("</tr>");
		out.println("</table></center></td>");
		out.println("</tr>");
		out.println("</table></form>");
		out.println("</body>");
		out.println("</html>");
	}

	//�����ڰ� �α��� ���� ù�޴������ֱ�	
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
			  out.println("<tr><td width=21%><font color=#FFFFFF size=2><b>���θ�����</b></font></td>");
		 	  out.println("<td width=24%><font color=#FFFFFF size=2><b>������������</b></font></td>");
			  out.println("<td bordercolor=#0C0A55 width=24%><font color=#FFFFFF size=2><b>ȸ����������</b></font></td>");
			  out.println("<td width=17%><font size=2 color=#FFFFFF><b>ȸ������</b></font></td>");
		      out.println("<td width=14%><b><font size=2 color=#FFFFFF>��������</font></b></td>");
		    out.println("</tr></table></td></tr>");
		    out.println("<tr><td width=140 bgcolor=#8080C0 height=500 bordercolor=#8080C0 valign=top><p>&nbsp;</p>");
			  out.println("<table border=0 width=90% align=center cellspacing=0><tr>"); 
			  out.println("<td bgcolor=#7B007B height=20 align=center valign=middle>"); 
			  out.println("<div align=center><b><font color=#FFFFFF size=2>���԰˻�</font></b></div></td></tr>");
			  out.println("<tr><td bgcolor=#8080C0 height=55><font size=1 color=#CCFFFF>���ε��丮������ �˻��� �Ϸ��� private�� ��ü �˻��� �Ϸ��� public�� ��������</font></td></tr>");
			  out.println("<tr><td height=23><font size=2 color=#FFFFFF>�̸�</font></td></tr>");
			  out.println("<tr><td><input type=text name=name size=5 maxlength=7></td></tr>");
			  out.println("<tr><td height=23><font color=#FFFFFF size=2>ȸ���̸�</font></td></tr>");
			  out.println("<tr><td><input type=text name=c_name size=8></td></tr>");
			  out.println("<tr><td height=23><font color=#FFFFFF size=2>����</font></td></tr>");
			  out.println("<tr><td><select name=select size=1>");
			  out.println("<option value=1>������</option><option value=2>����</option>");
			  out.println("<option value=3>�Ƿ�</option><option value=4>����</option>");
			  out.println("<option value=5>���/�ڵ���</option><option value=6>��ǻ��/���</option>");
			  out.println("<option value=7>���񽺾�</option><option value=8>����</option>");
			  out.println("<option value=9>����/���׸���</option><option value=10>���</option>");
			  out.println("<option value=11>����</option><option value=12>����</option>");
			  out.println("<option value=13>������</option><option value=14>��������</option>");
			  out.println("<option value=15>��Ÿ</option></select></td></tr>");
			  out.println("<tr><td height=47><input type=submit value=public>");
			  out.println("<input type=submit value=private></td></tr></table></td>");
			  out.println("<td bgcolor=#FFFFFF width=300 height=500 valign=top align=center><p></p><p>&nbsp;</p>");
			  out.println("<table border=0 width=90% cellspacing=5>");
			  out.println("<tr><td bgcolor=#7B007B height=21 align=center width=50%><b><font color=#FFFFFF size=2>���丮 �̸�</font></b></td>");
			  out.println("<td bgcolor=#7B007B height=21 align=center width=50%><b><font color=#FFFFFF size=2>���Լ�</font></b></td></tr>");
		
		// iteration�� �ϰ� �˾Ƽ�
			out.println("<tr><td height=20>/* ���丮 �̸�*/</td>");
			out.println("<td height=20>/* ���Լ� */</td></tr></table></td>");
			out.println("<td width=140 bgcolor=#8080C0 height=500 bordercolor=#8080C0>&nbsp;</td></tr></table></body></html>");
	}


	/** 
	 *	addCardForm - ���θ����Կ� ������ ��Ͻ�Ű�� ��
	 *  @param req	������ �Է¹��� �����Ͱ� ����ִ� ��
	 *  @param out	����� ����� ��
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
    	out.println("<img src=/icon.gif><b>" + asc.convert( "��������" ) + "</b></font></td>");
  	  	out.println("</tr>");
  		out.println("<tr>");
		out.println("<td align=center valign=top>");
		out.println("<form method=post action=/namecard/UserServlet >");
		out.println("<input type=hidden name=cmd value=addNewCard>");
		out.println("<input type=hidden name=dir_id value='" + dir_id + "'>");
		out.println("<br><p>");
		out.println("<table border=0 width=80% align=center cellspacing=0>");
		out.println("<tr><td colspan=3 align=center><font size=2 color=#e6e6fa>" + asc.convert( "* ǥ�� �Ǿ��ִ� �׸��� �ݵ�� ������ �ּ���." ) );
		out.println("</font><br><p></td></tr>");
		out.println("<tr>");
		out.println("<td><font size=2 color=#e0ffff><b>*</font></td>");
		out.println("<td><font color=#fffacd size=2><b>" + asc.convert( "�̸�" ) + "</b></td>");
		out.println("<td><input type=text name=name size=10 maxlength=10></td>");
		out.println("</tr>");
		out.println("<tr>");
		out.println("<td></td><td><font color=#fffacd size=2><b>" + asc.convert( "��ȭ" ) + "</b></td>");
		out.println("<td> <input type=text name=tel size=12> </td>");
		out.println("</tr>");
		out.println("<tr>");
		out.println("<td></td><td><font color=#fffacd size=2><b>" + asc.convert( "�߻�" ) + "</b></td>");
		out.println("<td> <input type=text name=beeper size=13></td>");
		out.println("</tr><tr>");
		out.println("<td></td><td><font color=#fffacd size=2><b>" + asc.convert( "�޴���" ) + "</b></td>");
		out.println("<td> <input type=text name=handphone size=13></td>");
		out.println("</tr>");
		out.println("<tr>");
		out.println("<td></td><td><font color=#fffacd size=2><b>" + asc.convert( "Ȩ������" ) + "</b></td>");
		out.println("<td><input type=text name=homepage size=40></td>");
		out.println("</tr><tr>");
		out.println("<td><font size=2 color=#e0ffff><b></font></td>");
		out.println("<td><font color=#fffacd size=2><b>email<b></td>");
		out.println("<td><input type=text name=email size=40></td>");
		out.println("</tr>");
		out.println("<tr>");
		out.println("<td><font size=2 color=#e0ffff><b>*</font></td>");
		out.println("<td><font color=#fffacd size=2><b>" + asc.convert( "����" ) + "</b></td>");
		out.println("<td><input type=text name=position size=20></td>");
		out.println("</tr><tr>");
		out.println("<td></td><td><font color=#fffacd size=2><b>" + asc.convert( "�μ�" ) + "</b></td>");
		out.println("<td><input type=text name=part size=20></td>");
		out.println("</tr>");
		out.println("<tr>");
		out.println("<td><font size=2 color=#e0ffff><b>*</font></td>");
		out.println("<td><font color=#fffacd size=2><b>" + asc.convert( "ȸ�簳����ȭ" ) + "</b></td> ");
		out.println("<td><input type=text name=c_tel size=13>");
		out.println("<font size=2 color=#e0ffff>" + asc.convert( "ȸ�簳����ȭ�� ������ ȸ����ȭ��ȣ�� �Է��ϼ���." ) + " </td>");
		out.println("</tr><tr>");
		out.println("<td></td><td><font color=#fffacd size=2><b>" + asc.convert( "ȸ���ѽ�" ) + "</b></td>");
		out.println("<td><input type=text name=c_fax size=13></td>");
		out.println("</tr>");
	    out.println("</table><br><p></td>");
	    out.println("</tr>"); 
	    out.println("<tr>");
	    out.println("<td align=center height=28><b><font size=4 color=#ffff00><img src=/icon.gif>" + asc.convert( "ȸ������" ) + "</b><br><p></td>");
		out.println("</tr>");
		out.println("<tr><td align=center>");
		out.println("<table border=0 width=80% cellspacing=0>");
		out.println("<tr>");
		out.println("<td><font size=2 color=#e0ffff><b>*</font></td>");
		out.println("<td><font color=#fffacd size=2><b>" + asc.convert( "ȸ���̸�" ) + "</b></td>");
		out.println("<td> <input type=text name=c_name size=20></td>");
		out.println("<td><font size=2 color=#e0ffff><b>*</font></td>");
		out.println("<td><font color=#fffacd size=2><b>" + asc.convert( "����" ) + "</b></td>");
		out.println("<td><select name=c_region size=1>");
		out.println("<option value=1>" + asc.convert( "����Ư����" ) + "</option><option value=2>" + asc.convert( "�λ걤����" ) + "</option>");
		out.println("<option value=3>" + asc.convert( "�뱸������" ) + "</option><option value=4>" + asc.convert( "��õ������" ) + "</option>");
		out.println("<option value=5>" + asc.convert( "���ֱ�����" ) + "</option><option value=6>" + asc.convert( "��걤����" ) + "</option>");
		out.println("<option value=7>" + asc.convert( "����������" ) + "</option><option value=8>" + asc.convert( "���" ) + "</option>");
		out.println("<option value=9>" + asc.convert( "����" ) + "</option><option value=10>" + asc.convert( "���" ) + "</option>");
		out.println("<option value=11>" + asc.convert( "�泲" ) + "</option><option value=12>" + asc.convert( "����" ) + "</option>");
		out.println("<option value=13>" + asc.convert( "����" ) + "</option><option value=14>" + asc.convert( "���" ) + "</option>");
		out.println("<option value=15>" + asc.convert( "�泲" ) + "</option><option value=16>" + asc.convert( "����" ) + "</option>");
		out.println("</select> </td>");
		out.println("</tr>");
		out.println("<tr>");
		out.println("<td></td><td><font color=#fffacd size=2><b>" + asc.convert( "����" ) + "</b></td>");
		out.println("<td><input type=text name=c_site size=20></td>");
		out.println("<td><font size=2 color=#e0ffff><b>*</td>");
		out.println("<td><font color=#fffacd size=2><b>" + asc.convert( "����" ) + "</b></font></td>");
		out.println("<td><select name=j_kind size=1>");
        out.println("<option value=1>" + asc.convert( "������" ) + "</option>");
        out.println("<option value=2>" + asc.convert( "����" ) + "</option>");
        out.println("<option value=3>" + asc.convert( "�Ƿ�" ) + "</option>");
        out.println("<option value=4>" + asc.convert( "����" ) + "</option>");
        out.println("<option value=5>" + asc.convert( "���/�ڵ���" ) + "</option>");
        out.println("<option value=6>" + asc.convert( "��ǻ��/���" ) + "</option>");
        out.println("<option value=7>" + asc.convert( "���񽺾�" ) + "</option>");
        out.println("<option value=8>" + asc.convert( "����" ) + "</option>");
        out.println("<option value=9>" + asc.convert( "����/���׸���" ) + "</option>");
        out.println("<option value=10>" + asc.convert( "���" ) + "</option>");
        out.println("<option value=11>" + asc.convert( "����" ) + "</option>");
        out.println("<option value=12>" + asc.convert( "����" ) + "</option>");
        out.println("<option value=13>" + asc.convert( "������" ) + "</option>");
        out.println("<option value=14>" + asc.convert( "��������" ) + "</option>");
        out.println("<option value=15>" + asc.convert( "��Ÿ" ) + "</option>");
        out.println("</select></td></tr>"); 
		out.println("<tr>");
		out.println("<td><font size=2 color=#e0ffff><b>*</font></td>");
		out.println("<td><font color=#fffacd size=2><b>" + asc.convert( "�ּ�" ) + "</b></td>");
		out.println("<td colspan=5> <input type=text name=address size=40></td></tr>");
		out.println("<tr>");
		out.println("<td colspan=6 align=center><br><input type=submit value='" + asc.convert( "���" ) + "'>");
		out.println("<input type=button value='" + asc.convert( "���" ) + "' onClick=\"window.history.go(-1);\"></td>" );
		out.println("</tr>");
		out.println("</table></center></td>");
		out.println("</tr>");
		out.println("</table></form>");
		out.println("</body>");
		out.println("</html>");
	}
}

