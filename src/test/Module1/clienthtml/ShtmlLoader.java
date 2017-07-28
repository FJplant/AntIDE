//package test.Module1;
package test.Module1.clienthtml;

import java.io.*;
import java.net.*;

import javax.servlet.*;
import javax.servlet.http.*;



public class ShtmlLoader extends HttpServlet{

    int _port;

    class ShtmlFilter implements FilenameFilter {
        public boolean accept(File dir, String name) {
           return (name.toUpperCase().indexOf(".SHTML") != -1);
        }
    }


   public void doGet (HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException
    {


     String fileName = req.getQueryString();
     File file=null;
     String html="File not found.";

     _port = req.getServerPort();
     String pakage = "test.Module1";
     pakage = pakage.replace('.','\\');
     file = new File("C:\\WORK\\ant\\source\\" + pakage + "\\clienthtml");
     File[] fileList  = file.listFiles(new ShtmlFilter());

     int size = fileList.length;
     int j=0;
     file=null;
     while (j != size && file == null) {
       if ((fileName == null && fileList[j].getName().startsWith("Form")) ||
          (fileList[j].getName().equalsIgnoreCase(fileName)) ){
          file = fileList[j];
       }
       j++;
     }


     if (file != null) {
      if (file.exists()) {
        try {
          RandomAccessFile raf = new RandomAccessFile(file,"r");
          long length = file.length();

          byte [] b = new byte[(int) file.length()];
          raf.read(b);
          html = processServletTag(new String(b));
          raf.close();
        }
        catch (IOException e) {
          System.out.println("Error reading file:" + fileName);
        }
      }
     }


      PrintWriter out = new PrintWriter (res.getOutputStream());
      out.println(html);
      out.close();
    }


    String processServletTag(String buffer) {
      return replaceServletTag(buffer,buffer.toUpperCase());
    }

    String replaceServletTag(String buffer,String copy)
    {
       String servletEnd = "</SERVLET>";
       String servletResult = "";
       int i = copy.indexOf("<SERVLET",0);
       if (i != -1 ) {
         int j = copy.indexOf(servletEnd,0) + servletEnd.length();
         if (j != -1) {
            String servletTag = buffer.substring(i,j);
            servletResult = processServlet(servletTag);
            buffer = buffer.substring(0,i) + servletResult +
                     replaceServletTag(buffer.substring(i+servletTag.length() ),
                                       copy.substring(j));
         }
       }
       return buffer;
    }


    private String processServlet(String servletTag)
    {

       java.util.Vector names= new java.util.Vector();
       java.util.Vector values= new java.util.Vector();
       String code="";

       int i=0;
       int s=0;
       int e=0;
       s = servletTag.indexOf("<");
       e = servletTag.indexOf(">",0);
       while (s > -1 && e > -1 && (s < e)) {
         String temp = servletTag.substring(s,e);
         String utemp = temp.toUpperCase();
         if (utemp.indexOf("PARAM") != -1) {
            names.add(getString("NAME",temp));
            values.add(getString("VALUE",temp));
         }
         else if (utemp.indexOf("CODE") != -1) {
            code = getString("CODE",temp);
         }
         s = servletTag.indexOf("<",s+1);
         e = servletTag.indexOf(">",e+1);
       }

       String queryString="";
       i = 0;
       while (i != names.size()) {
          if (i != 0 ) {
            queryString += "&";
          }
          queryString += names.elementAt(i);
          if (values.elementAt(i) != null) {
            queryString += "=" + values.elementAt(i);
          }
          i++;
       }

       if (queryString.length() != 0) {
          code += "?" + queryString;
       }

       String result="";
       try {
         result = loadServlet(code);
       }
       catch (Exception ex) {
       }

       return result;
    }

    private String loadServlet(String code)
    throws IOException
    {

        URL url = null;
        try {
          url = new URL("http","localhost",_port ,"/servlet/"+ code);
        }
        catch (Exception e) {
          e.printStackTrace();
          return "";
        }

        Socket          socket=null;
        PrintStream     out;
        DataInputStream in;
        String          line;
        int             i, j;

        if (url.getPort() == -1)
            socket = new Socket(url.getHost(), 80);
        else
            socket = new Socket(url.getHost(), url.getPort());

        //
        // Construct a simple HTTP request, write it out.
        //
        out = new PrintStream(socket.getOutputStream());
        out.println("GET " + url.getFile() + " HTTP/1.0");
          out.println();

        //
        // Get the HTTP response.
        //
        in = new DataInputStream(socket.getInputStream());
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        line = reader.readLine();

        String reply = "";


        while ((line = reader.readLine()) != null && line.length()!=0) {
          line = reader.readLine(); // Skip the header
        }

        while ((line = reader.readLine()) != null) {
          reply += line + "\r";
        }

        out.close();
        reader.close();

        if (socket != null)
          socket.close();

     return reply;
  }


   private String  getString(String word,String from) {
      String temp = from.toUpperCase();
      int v = temp.indexOf(word);

      String ret="";
      if (v != -1) {
         temp = from.substring(v);
         int len = temp.length();
         int i=word.length();
         while (i < len && temp.charAt(i) != '=') i++;
         if (i < len) {
            i++; // skip the = and any leading ' '
            while (i < len && temp.charAt(i) == ' ') i++;
         }
         if (i < len) {
             char endchar = ' ';
             if (temp.charAt(i) == '"') {
                 endchar = '"';
                 i++;
             }
             ret = temp.substring(i);
             int end = ret.indexOf(endchar);
             int cr = ret.indexOf('\r');
             if (cr != -1 && cr < end)
                end = cr;
             if (end != -1 )
                ret = ret.substring(0,end);
         }
      }
      return ret;
   }
}
