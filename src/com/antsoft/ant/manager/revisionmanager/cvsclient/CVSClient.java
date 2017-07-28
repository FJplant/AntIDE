/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/manager/revisionmanager/cvsclient/CVSClient.java,v 1.3 1999/07/22 03:02:45 multipia Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.3 $
 * $History: CVSClient.java $
 * 
 * *****************  Version 2  *****************
 * User: Multipia     Date: 99-05-16   Time: 11:51p
 * Updated in $/AntIDE/source/ant/manager/revisionmanager/cvsclient
 * 
 * *****************  Version 9  *****************
 * User: Flood        Date: 98-09-18   Time: 1:59p
 * Updated in $/JavaProjects/src/ant/manager/revisionmanager/cvsclient
 * 
 * *****************  Version 8  *****************
 * User: Multipia     Date: 98-09-09   Time: 2:33p
 * Updated in $/Ant/src/ant/revisionmanager
 * package name changed to
 * package ant.revisionmanager;
 * 
 * *****************  Version 6  *****************
 * User: Flood        Date: 98-08-11   Time: 11:03a
 * Updated in $/Ant/src
 * 
 * *****************  Version 5  *****************
 * User: Flood        Date: 98-07-29   Time: 5:04a
 * Updated in $/Ant/src
 * readReponse 가 입력을 parse 하도록함.
 * 
 * *****************  Version 4  *****************
 * User: Flood        Date: 98-07-28   Time: 4:33a
 * Updated in $/Ant/src
 *
 * *****************  Version 3  *****************
 * User: Flood        Date: 98-07-28   Time: 2:21a
 * Updated in $/Ant/src
 */


 package ant.manager.revisionmanager.cvsclient;

 import java.io.InputStream;
 import java.io.OutputStream;
// import java.io.BufferedReader;
// import java.io.InputStreamReader;
 import java.io.IOException;
 import java.io.EOFException;
 import java.net.Socket;
 import java.net.UnknownHostException;
 import java.security.InvalidParameterException;

/**
 * @author 표태수
 * CVS 의 client part
 *
 * 참고 : http://www.loria.fr/~molli/cvs/doc/cvsclient_toc.html
 *
 */


 class CVSClient extends Object
 {
    private Socket socket;
    private InputStream input;
    private OutputStream output;
    private CVSRequest request;
    //private String result;

    /**
     *  CVSClient contructor
     */

     public CVSClient(CVSRequest request)
     {
      this.request = request;
      init();
     }

    /**
     * initialize
     *
     */

     private void init()
     {
        input = null;
        output = null;
        socket = null;
      //  result = null;
     }

    /**
     * establish the connection
     */

      public void open() throws UnknownHostException, IOException
      {
        init();
        socket = new Socket(request.getHostname(),request.getPort());
        input = socket.getInputStream();
        output = socket.getOutputStream();
      }

    /**
     * close the connection
     */

     public void close() throws IOException
     {
      if(socket == null) return;
      socket.close();
     }

     // =======================================================================

     /**
     * send verification
     */

     public void sendVerification() throws IOException
     {
     	sendLine("BEGIN VERIFICATION REQUEST");
      sendLine(request.getCvsroot());
      sendLine(request.getUsername());
      sendLine(request.getPassword());
      sendLine("END VERIFICATION REQUEST");

      readLine();
     }

     /**
     * send login
     */

     public void sendLogin() throws IOException
     {
     	sendLine("BEGIN AUTH REQUEST");
      sendLine(request.getCvsroot());
      sendLine(request.getUsername());
      sendLine(request.getPassword());
      sendLine("END AUTH REQUEST");

      readLine();
     }

    /**
     * send valid-responses
     */

     public void sendValidResponses() throws IOException
     {
      // Valid-responses E M ok error \
      // Valid-requests Created Merged Updated Update-existing \
      // Removed Remove-entry New-entry Checked-in Checksum \
      // Copy-file Notified Clear-sticky Set-sticky Clear-static-directory \
      // Set-static-directory

     	sendLine("Valid-responses "
              + "E M ok error Valid-requests "
      				+ "Created Merged Updated "
              + "Update-existing "
              + "Removed Remove-entry New-entry Checked-in Checksum "
              + "Copy-file Notified Clear-sticky Set-sticky Clear-static-directory "
              + "Set-static-directory");

      //result = null;
     }

    /**
     * send valid-requests
     */

     public void sendValidRequests() throws IOException
     {
     	sendLine("valid-requests");

      readResponse();
     }

    /**
     * send noop
     */

     public void sendNoop() throws IOException
     {
     	sendLine("noop");
      readResponse();
     }

    /**
     * send argument
     * @param arg argument
     */

     public void sendArgument(String arg) throws IOException
     {
     	sendLine("Argument " + arg);
      //result = null;
     }

    /**
     * send argumentx
     */

     public void sendArgumentx(String arg) throws IOException
     {
     	sendLine("Argumentx " + arg);
      //result = null;
     }

    /**
     * send directory
     */

     public void sendDirectory(String localdir,String repository) throws IOException
     {
     	sendLine("Directory " + localdir);
      sendLine(repository);
      //result = null;
     }

    /**
     * send root
     */

     public void sendRoot(String root) throws IOException
     {
     	sendLine("Root " + root);
      //result = null;
     }

    /**
     * send expandmodules
     */

     public void sendExpandModules(String module) throws IOException
     {
     	sendLine("expand-modules " + module);
      readResponse();
     }

    /**
     * send co
     */

     public void sendCo() throws IOException
     {
     	sendLine("co");
      readResponse();
     }

    /**
     * send status
     */

     public void sendStatus() throws IOException
     {
     	sendLine("status");
      readResponse();
     }

    /**
     * send history
     */

     public void sendHistory() throws IOException
     {
     	sendLine("history");
      readResponse();
     }

     // =======================================================================

    /**
     * send request
     */

     public void processRequest() throws IOException
     {
     	// send valid requests
      // send set variables
      // send valid responses
      // send global arguments
      // send notifies
      // send entries
     	//*send arguments
      // send command
      // read & parse response
     }

     // =======================================================================

    /**
     * send a line
     *
     * @param line string to send
     */

      void sendLine(String line) throws IOException
      {
        if(output == null) return;

        output.write((line + "\012").getBytes());   // line + linefeed(#10)
        output.flush();

        System.out.println("sendline : " + line);
      }


    /**
     * read a line
     *
     * @return read string
     */

      String readLine() throws IOException
      {
        if(input == null) throw new IOException("socket is not open");

        StringBuffer line = new StringBuffer("");
        int ch;

        for(;;)
        {
          ch = input.read();
          if(ch < 0)
          {
          		System.out.println("readline : " + line + "EOF");
          		throw new EOFException("readline:EOF");
          }
          if((char)ch == '\012') break;

          line.append((char)ch);
        }

        System.out.println("readline : " + line);
        return line.toString();
      }

    /**
     * read response
     *
     */

     public static boolean startsWithWord(String s,String w)
     {
       if( ! s.startsWith(w) )
          return false;
       if( s.length() == w.length() )
       		return true;
       if( Character.isWhitespace(s.charAt(w.length())) )
       		return true;
       return false;
     }

    /**
     *
     * read & parse response
     *
     */

      void readResponse() throws IOException
      {
        if(input == null) throw new IOException("socket is not open");

        String line;

        System.out.println("<respose start>");

        // .......................................................

        for(;;)
        {
          line = readLine();



          if(startsWithWord(line,"M"))	continue;
          if(startsWithWord(line,"E"))	continue;
          if(startsWithWord(line,"F"))	continue;
          if(startsWithWord(line,"MT"))	continue;


          // result.append(line + "\012");


          if(line.startsWith("/"))
          	processDirectory(line);
					else
          if(startsWithWord(line,"Valid-requests"))	processValidRequests(line);
/*
					else
          if(startsWithWord(line,"Checked-in"))    	processCheckedIn(line);
          else
          if(startsWithWord(line,"New-entry"))     	processNewEntry(line);
          else
          if(startsWithWord(line,"Updated"))       	processUpdated(line);
*/          else
          if(startsWithWord(line,"Created"))       	processCreated(line);
/*          else
          if(startsWithWord(line,"Update-existing"))processUpdateExisting(line);
          else
          if(startsWithWord(line,"Merged"))       	processMerged(line);
          else
          if(startsWithWord(line,"Rcs-diff"))      	processRcsDiff(line);
          else
          if(startsWithWord(line,"Patched"))       	processPatched(line);
          else
          if(startsWithWord(line,"Mode"))	        	processMode(line);
          else
          if(startsWithWord(line,"Mod-time"))      	processModTime(line);
          else
          if(startsWithWord(line,"Checksum"))      	processChecksum(line);
          else
          if(startsWithWord(line,"Copy-file"))     	processCopyFile(line);
          else
          if(startsWithWord(line,"Removed"))       	processRemoved(line);
          else
          if(startsWithWord(line,"Remove-entry"))		processRemoveEntry(line);
          else
          if(startsWithWord(line,"Set-static-directory"))		processSetStaticDirectory(line);
*/          else
          if(startsWithWord(line,"Clear-static-directory"))	processClearStaticDirectory(line);
/*          else
          if(startsWithWord(line,"Set-sticky"))			processSetSticky(line);
*/          else
          if(startsWithWord(line,"Clear-sticky"))		processClearSticky(line);
/*          else
          if(startsWithWord(line,"Template"))				processTemplate(line);
          else
          if(startsWithWord(line,"Set-Checkin-prog"))				processSetCheckinProg(line);
          else
          if(startsWithWord(line,"Set-update-prog"))				processSetUpdateProg(line);
          else
          if(startsWithWord(line,"Notified"))				processNotified(line);
*/
          else
          if(startsWithWord(line,"Module-expansion"))      	processModuleExpansion(line);
/*          else
          if(startsWithWord(line,"Wrapper-rcsOption"))			processWrapperRcsOption(line);
*/

          if(line.startsWith("ok"))
          	break;
          if(line.startsWith("error"))
          	break;
        }

        // .......................................................

        System.out.println("<respose end>");
      }
    /**
     *
     *  process directory
     *
     */

      void processDirectory(String line)
      {
      		// if not exists , make it
      }

    /**
     *
     *  process Valid-requests response
     *
     */

      void processValidRequests(String line)
      {
      		// check the valid requests
      }

    /**
     *
     *  process Module-expansion response
     *
     */

      void processModuleExpansion(String line)
      {
      }

    /**
     *
     *  process Clear-static-directory response
     *
     */

      void processClearStaticDirectory(String line)
      {
      }
    /**
     *
     *  process Clear-sticky response
     *
     */

      void processClearSticky(String line)
      {
      }

    /**
     *
     *  process Created response
     *
     */

      void processCreated(String line) throws IOException
      {
        System.out.println("<Created>");
        String fname = readLine();
      	String entry = readLine();
        String mode = readLine();
        int fsize = Integer.parseInt(readLine());
        readFile(fsize);
      }

      // -----------------------------------------------------------------------

    /**
     *
     *  read file stream
     *
     */

      void readFile(int fsize) throws IOException
      {
        System.out.println("<Filesize : " + fsize + " >");
        final int bufsize = 1024;
        byte[] buf = new byte[bufsize];
        int r;

        while(fsize > 0)
        {
	        r = input.read(buf);
  	      if(r < 0) throw new EOFException("readline:EOF");
          fsize -= r;
        }
        System.out.println("< ok >");
      }


 } // CVSClient




