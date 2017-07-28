/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/manager/revisionmanager/cvsclient/CVSRequest.java,v 1.3 1999/07/22 03:02:45 multipia Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.3 $
 * $History: CVSRequest.java $
 * 
 * *****************  Version 2  *****************
 * User: Multipia     Date: 99-05-16   Time: 11:51p
 * Updated in $/AntIDE/source/ant/manager/revisionmanager/cvsclient
 * 
 * *****************  Version 6  *****************
 * User: Flood        Date: 98-09-18   Time: 1:59p
 * Updated in $/JavaProjects/src/ant/manager/revisionmanager/cvsclient
 * 
 * *****************  Version 5  *****************
 * User: Multipia     Date: 98-09-09   Time: 2:33p
 * Updated in $/Ant/src/ant/revisionmanager
 * package name changed to
 * package ant.revisionmanager;
 * 
 * *****************  Version 3  *****************
 * User: Flood        Date: 98-08-11   Time: 11:03a
 * Updated in $/Ant/src
 * 
 * *****************  Version 2  *****************
 * User: Flood        Date: 98-07-28   Time: 4:33a
 * Updated in $/Ant/src
 *
 * *****************  Version 1  *****************
 * User: Flood        Date: 98-07-28   Time: 2:23a
 * Created in $/Ant/src
 * CVS request
 */


 package ant.manager.revisionmanager.cvsclient;

/**
 * @author Ç¥ÅÂ¼ö
 * CVS Request
 */

 public class CVSRequest extends Object
 {
 	private String hostname;
  private int port;
  private String username,password;
  private String command;
  private String cvsroot;

  final CVSResponseHandler responsehandler;


    /**
     * returns the hostname
     * @return hostname
     */

     public String getHostname()
     {
      return hostname;
     }

    /**
     * returns the port
     * @return port
     */

     public int getPort()
     {
      return port;
     }

    /**
     * set the host
     * @param hostname
     * @param port
     */

     public void setHost(String hostname,int port)
     {
      this.hostname = hostname;
      this.port = port;
     }

    /**
     * returns the Username
     * @return username
     */

     public String getUsername()
     {
      return username;
     }

    /**
     * returns the password
     * @return password
     */

     public String getPassword()
     {
      return password;
     }

    /**
     * set user
     * @param username
     * @param password
     */

     public void setUser(String username,String password)
     {
      this.username = username;
      this.password = CVSScramble.scramble(password);
     }

    /**
     * returns the command
     * @return command
     */

     public String getCommand()
     {
      return command;
     }

    /**
     * set the command
     * @param command
     */

     public void setCommand(String command)
     {
      this.command = command;
     }

    /**
     * returns the cvsroot
     * @return cvsroot
     */

     public String getCvsroot()
     {
      return cvsroot;
     }

    /**
     * set the cvsroot
     * @param cvsroot
     */

     public void setCvsroot(String cvsroot)
     {
      this.cvsroot = cvsroot;
     }

 } // CVSRequest




