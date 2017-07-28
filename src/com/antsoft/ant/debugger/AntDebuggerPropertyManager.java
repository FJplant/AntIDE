/*
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * Author:    Lee, Chul Mok
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/debugger/AntDebuggerPropertyManager.java,v 1.4 1999/08/04 01:22:11 itree Exp $
 * $Revision: 1.4 $
 * $History: AntDebuggerPropertyManager.java $
 */

package com.antsoft.ant.debugger;

import java.io.*;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;
import java.awt.Rectangle;

public class AntDebuggerPropertyManager {
  public static String INSTALL_DIR;
  public static String PROPERTY_PATH;

  static{
    boolean isMSWindow = OSVerifier2.getOS().equals(OSVerifier2.MS_WINDOW);

    String SEP = File.separator;

    //테스트 할때 다음과 같이 한다
    if(isMSWindow){
      INSTALL_DIR = "c:\\temp";
    }
    else {
      INSTALL_DIR = System.getProperty("INSTALL_DIR");
    }


    if(isMSWindow){
      PROPERTY_PATH =  INSTALL_DIR+SEP+"ANTDEBUGGER.INI";
    }
    else{
      PROPERTY_PATH =  System.getProperty("user.home") + SEP + "ANTDEBUGGER.INI";
      if(PROPERTY_PATH == null) {
        System.out.println("your home directory is not specified .... exit...");
        System.exit(-1);
      }
    }

    //실제 인스톨 만들때 다음과 같이 한다
/*
    INSTALL_DIR = System.getProperty("lax.root.install.dir");
    PROPERTY_PATH =             INSTALL_DIR+SEP+"property"+SEP+"ANT.INI";
*/
  }

  public static AntDebuggerProperty loadProperty(){

    AntDebuggerProperty ret = null;

    File f = new File(PROPERTY_PATH);

    if(f.exists() && (f.length() > 0)){
      FileInputStream istream = null;
      try{
        istream = new FileInputStream(f);
        ret = makeAntDebuggerProperty(istream);
      }
      catch(Exception e){
        ret = new AntDebuggerProperty();
        boolean success = f.delete();

        //first time save action
        saveProperty(ret);
      }
      finally{
        try{
     	    istream.close();
        }catch(IOException e2){}
      }
    }
    else {
      if(f.exists() && f.length() == 0)  f.delete();
      ret = new AntDebuggerProperty();
      saveProperty(ret);
    }
    return ret;
  }

  static private AntDebuggerProperty makeAntDebuggerProperty(FileInputStream fis) throws Exception{
    AntDebuggerProperty ret = new AntDebuggerProperty();

    InputStreamReader isr = new InputStreamReader(fis);
    BufferedReader br = new BufferedReader(isr);

    String line = br.readLine();

    while(line != null){

      if(line.trim().equals("") || line.startsWith("#")) {
        line = br.readLine();
        continue;
      }

      int index = line.indexOf("=");
      if(index == -1) {
        line = br.readLine();
        continue;
      }

      String key = line.substring(0, index);
      String value = line.substring(index+1).trim();
      StringTokenizer st = new StringTokenizer(value, "#", false);

      if(key.equals("ANTDEBUGGER_SETUP_VERSION")) {
        if(value.equals("1.0")) {
          line = br.readLine();
          continue;
        }
        else{
          throw new Exception("Not Valid SETUP File");
        }
      }

      if(key.equals("Frame_Bound")) ret.setLastFrameBound(new Rectangle(Integer.parseInt(st.nextToken()),
                                                                           Integer.parseInt(st.nextToken()),
                                                                           Integer.parseInt(st.nextToken()),
                                                                           Integer.parseInt(st.nextToken())));
      if(key.equals("User_Source_Path")) {
        ret.setUserSourcePath(value);
      }
      if(key.equals("User_Class_Path")) {
        ret.setUserClassPath(value);
      }

      if(key.equals("CMP_Size"))
        ret.setClassMethodPaneSize(Integer.parseInt(value));
      if(key.equals("CSP_Size"))
        ret.setClassSourcePaneSize(Integer.parseInt(value));
      if(key.equals("LWP_Size"))
        ret.setLocalWatchPaneSize(Integer.parseInt(value));
      if(key.equals("LiWP_Size"))
        ret.setListWatchPaneSize(Integer.parseInt(value));


      line = br.readLine();
    }
    br.close();
    isr.close();

    return ret;
  }

  public static void saveProperty(AntDebuggerProperty property){
    try{
      FileOutputStream ostream = new FileOutputStream(PROPERTY_PATH);
      ostream.write(property.toString().getBytes());
      ostream.close();
    }
    catch(IOException e){}
    catch(Exception e1){}
  }
}

class OSVerifier2 {

  public static final String MS_WINDOW = "ms_window";
  public static final String LINUX = "linux";
  public static final String MAC = "mac";
  public static final String UNIX = "unix";

  public static String getOS(){
    String osName = System.getProperty("os.name");

    if(osName.toLowerCase().indexOf("win") != -1){
      return OSVerifier2.MS_WINDOW;
    }
    else if(osName.toLowerCase().indexOf("linux") != -1){
      return OSVerifier2.LINUX;
    }
    else {
      return OSVerifier2.UNIX;
    }
  }
}

