/*
 *	package : test
 *	source  : MyServlet.java
 *	date    : 1999.8.9
 */

package test;

import javax.servlet.http.*;
import javax.servlet.*;
import java.beans.*;

public class MyServlet extends HttpServlet { 
	protected void doGet( HttpServletRequest req, HttpServletResponse resp ) 
		throws ServletException, java.io.IOException {
		//TO DO (implementation here)
		resp.setContentType("text/html");
		Introspector.
		java.io.PrintWriter out = 
			new java.io.PrintWriter(resp.getOutputStream());
			
		out.println("<html>");
		out.println("<head>");
		out.println("<title>Employee List</title>");
		out.println("</head>");
		out.println("<h2><center>");
		out.println("Employees for Nezzer's Chocolate Factory");
		out.println("</center></h2>");
		out.println("<br>");
	}
}
