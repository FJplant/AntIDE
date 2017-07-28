package com.antsoft.namecard;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class UploadServlet extends HttpServlet{

	//html tag Á¤ÀÇ	
	static String htmlHeader = "<html><head><title></title></head><body>";
	static String htmlEnd = "</body></html>";

	static CardBoxManager boxmanager = new CardBoxManager();

	Uni2Asc asc = new Uni2Asc();

	public void doPost( HttpServletRequest req, HttpServletResponse res ) throws IOException, ServletException {
		
		res.setContentType( "text/html" );
		PrintWriter out = res.getWriter();
		try{
			boxmanager.preview(req, out);
		} catch( IOException e ) {
			out.println(e.toString() ); 
		}catch( Exception e ) {
			out.println( e.toString() );
		}
			out.close();
	}
}
