/*
 *	package : test
 *	source  : Servlet1.java
 *	date    : 1999.8.8
 */

package test;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;


public class Servlet1 extends HttpServlet { 

	/**
	 *  Servlet1 -  constructor 
	 */
	public void Servlet1() {
	}

	/**
	 *	doPost
	 */
	public void doPost( HttpServletRequest req, HttpServletResponse res )
						throws ServletException, IOException {
		res.setContentType( "text/html" );
		PrintWriter out = new PrintWriter( res.getOutputStream() );

		out.close();
	}
}
