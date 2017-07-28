/*
 *	package : test
 *	source  : ServletTest.java
 *	date    : 1999.5.16
 */

package test;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;


public class ServletTest extends HttpServlet { 

	/**
	 *  ServletTest -  constructor 
	 */
	public void ServletTest() {
	}

	/**
	 *	doGet
	 */
	public void doGet( HttpServletRequest req, HttpServletResponse res )
						throws ServletException, IOException {

		res.setContentType( "text/html" );
		PrintWriter out = new PrintWriter( res.getOutputStream() );

		out.close();
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

	/**
	 *	doPut
	 */
	public void doPut( HttpServletRequest req, HttpServletResponse res )
						throws ServletException, IOException {

	}

	/**
	 *	doDelete
	 */
	public void doDelete( HttpServletRequest req, HttpServletResponse res )
						throws ServletException, IOException {

	}
}

