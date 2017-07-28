
package test.Module1.clienthtml;


import java.io.*;
import java.util.*;
import java.beans.Beans;



import javax.servlet.*;
import javax.servlet.http.*;

import java.io.PrintWriter;
import java.io.IOException;


public class TestModule1 extends HttpServlet {

   PrintWriter    pw=null;
   int errorCount;


   public void print(String s) {
    try {
      if (pw != null)
        pw.println(s);
    }
    catch (Exception e) {
    }
   }


    private void checkClass(String className, String displayName,String error) {
      try {
         Class clazz = Class.forName(className);
         Object  obj = clazz.newInstance();
         print("" + displayName + "...ok<BR>");
      } catch (Exception e) {
         print("<FONT color=red> Error " + displayName + "<BR>");
         print(error + "</FONT><BR>");
         errorCount++;
      }
    }


    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException
    {
        res.setContentType("text/html");
        res.setHeader("Cache-Control", "private");
        pw = res.getWriter();

        print("<H2>Test Gateway For test.Module1</H2> <BR>");
        print("" + new Date() );
        print("<BR><BR>");

        errorCount = 0;

        checkClass("javax.servlet.ServletException","HttpServlet","jsdk.jar Not Found");
        checkClass("com.visigenic.vbroker.orb.ORB","Visibroker orb","vborb.jar Not Found");
        checkClass("com.visigenic.vbroker.ds.DSUser","Visibroker orb","vbapp.jar Not Found");
        checkClass("com.borland.orbutil.httpgateway.GatewayBase","GatewayBase","orbutil3.0.jar Not Found");

        int i=0;
        String gatewayClass = req.getParameter("class" + i++);

        while (gatewayClass != null ) {
           checkClass(gatewayClass,gatewayClass,"");
           gatewayClass = req.getParameter("class" + i++);
        }

        if (errorCount == 0) {
           print("<BR>All Found<BR>");
        }

        print("<BR>ClassPath:<BR>" + System.getProperty("java.class.path"));
        print("</body></html>");
    }

}
