/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/property/IdePropertyManager.java,v 1.12 1999/08/30 01:51:09 remember Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.12 $
 */
package com.antsoft.ant.property;

import java.io.*;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;
import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;

import com.antsoft.ant.property.defaultproperty.*;
import com.antsoft.ant.tools.htmlgenerator.HtmlGeneratorDlg;
import com.antsoft.ant.util.FileClassLoader;
import com.antsoft.ant.util.OSVerifier;
import com.antsoft.ant.tools.print.PrintSetupDlg;


/**
 * Ide Property Manager
 *
 * @author kim sang kyun
 */
public class IdePropertyManager {

  /**
   * Install Root Directory
   */
  public static String INSTALL_DIR;

  /**
   * IdeProperty file path
   */
  public static String PROPERTY_PATH;

  /**
   * Parameter Data Property file path
   */
  public static String PARAM_HASH_PATH;

  /**
   * Default User Project Root Directory
   */
  public static String PROJECT_ROOT_DIR;

  /**
   * Program Generated Source Root Dir
   */
  public static String GENERATED_SOURCE_ROOT_DIR;

  /**
   * Default project root dir path
   */
  public static String DEFAULT_PROJECT_ROOT_DIR;

  /**
   *  Custom Wizard Jar File Path
   */
   public static String CUSTOM_WIZARD_PATH;

   /**
    * Custom Wizard Source File Path
    */
   public static String CUSTOM_WIZARD_SOURCE_PATH;

  /**
   * Example project path
   */
  public static String EXAMPLE_PROJECT_PATH;

  static{
    String SEP = File.separator;

    INSTALL_DIR = System.getProperty("lax.dir");
    if(INSTALL_DIR == null) INSTALL_DIR = System.getProperty("user.home");

    GENERATED_SOURCE_ROOT_DIR = INSTALL_DIR+SEP+"generated";
    PROPERTY_PATH =             INSTALL_DIR+SEP+"property"+SEP+"ANT.INI";
    PARAM_HASH_PATH =           INSTALL_DIR+SEP+"property"+SEP+"param.property";
    PROJECT_ROOT_DIR =          INSTALL_DIR+SEP+"Projects";
    DEFAULT_PROJECT_ROOT_DIR =  PROJECT_ROOT_DIR+SEP+"Default"+SEP+"default.apr";
    EXAMPLE_PROJECT_PATH =      INSTALL_DIR + SEP + "example" + SEP + "example.apr";
    CUSTOM_WIZARD_PATH =        INSTALL_DIR+SEP+"wizard";
    CUSTOM_WIZARD_SOURCE_PATH = CUSTOM_WIZARD_PATH+SEP+"source";

    File f1 = new File( GENERATED_SOURCE_ROOT_DIR );
    if(!f1.exists()) f1.mkdir();

    f1 = new File( INSTALL_DIR+SEP+"property" );
    if(!f1.exists()) f1.mkdir();

    f1 = new File( PROJECT_ROOT_DIR );
    if(!f1.exists()) f1.mkdir();

    f1 = new File( PROJECT_ROOT_DIR+SEP+"Default" );
    if(!f1.exists()) f1.mkdir();

    f1 = new File( INSTALL_DIR + SEP + "example" );
    if(!f1.exists()) f1.mkdir();

    f1 = new File( CUSTOM_WIZARD_PATH );
    if(!f1.exists()) f1.mkdir();

    f1 = new File( CUSTOM_WIZARD_SOURCE_PATH );
    if(!f1.exists()) f1.mkdir();
  }

  public static IdeProperty loadProperty(){

    IdeProperty ret = null;

    File f = new File(PROPERTY_PATH);

    if(f.exists() && (f.length() > 0)){
      FileInputStream istream = null;
      try{
        istream = new FileInputStream(f);
        ret = makeIdeProperty(istream);
      }
      catch(Exception e){
        ret = new IdeProperty();
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
      ret = new IdeProperty();
      saveProperty(ret);
    }
    return ret;
  }

  static private IdeProperty makeIdeProperty(FileInputStream fis) throws Exception{

    IdeProperty ret = new IdeProperty();

    DefaultPathModel m = new DefaultPathModel();
    JdkInfoContainer jdkInfos = new JdkInfoContainer();
    LibInfoContainer allLibInfos = new LibInfoContainer();
    LibInfoContainer selectedLibInfos = new LibInfoContainer();

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

      if(key.equals("ANT_SETUP_VERSION")) {
        if(value.equals("1.0")) {
          line = br.readLine();
          continue;
        }
        else{
          throw new Exception("Not Valid SETUP File");
        }
      }

      // 에디터 옵션
      else if (key.equals("Auto_Indent_Mode")) ret.setAutoIndentMode(Boolean.valueOf(value).booleanValue());
      else if (key.equals("Insert_Mode")) ret.setInsertMode(Boolean.valueOf(value).booleanValue());
      else if (key.equals("Use_Tab_Char")) ret.setUseTabChar(Boolean.valueOf(value).booleanValue());
      else if (key.equals("Group_Undo")) ret.setGroupUndo(Boolean.valueOf(value).booleanValue());
      else if (key.equals("Syntax_Colouring")) ret.setSynClring(Boolean.valueOf(value).booleanValue());

      else if(key.equals("Keyword_Color")) ret.setColor(ColorPanel.KEYWORD, new Color(Integer.parseInt(value)));
      else if(key.equals("Comment_Color")) ret.setColor(ColorPanel.COMMENT, new Color(Integer.parseInt(value)));
      else if(key.equals("Constant_Color")) ret.setColor(ColorPanel.CONSTANT, new Color(Integer.parseInt(value)));
      else if(key.equals("String_Color")) ret.setColor(ColorPanel.STRING, new Color(Integer.parseInt(value)));
      else if(key.equals("Background_Color")) ret.setColor(ColorPanel.BACKGROUND, new Color(Integer.parseInt(value)));

      else if(key.equals("Html_Keyword_Color")) ret.setHtmlColor(HtmlGeneratorDlg.KEYWORD, new Color(Integer.parseInt(value)));
      else if(key.equals("Html_Comment_Color")) ret.setHtmlColor(HtmlGeneratorDlg.COMMENT, new Color(Integer.parseInt(value)));
      else if(key.equals("Html_Literal_Color")) ret.setHtmlColor(HtmlGeneratorDlg.LITERAL, new Color(Integer.parseInt(value)));
      else if(key.equals("Html_Other_Color")) ret.setHtmlColor(HtmlGeneratorDlg.OTHER, new Color(Integer.parseInt(value)));
      else if(key.equals("Html_Background_Color")) ret.setHtmlColor(HtmlGeneratorDlg.BACKGROUND, new Color(Integer.parseInt(value)));

      else if(key.equals("Font")) {
        Font f = new Font(st.nextToken(), Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()));
        ret.setSelectedFont(f);
      }

      else if(key.equals("Selected_WebBrowser_Path")) {
        File f = new File(value);
        if(f.exists()) ret.setSelectedWebBrowserPath(value);
      }
      else if(key.equals("Selected_WebBrowser_Path_ToSave")) {
        File f = new File(value);
        if(f.exists()) ret.setSelectedWebBrowserPathToSave(value);
      }
      else if(key.equals("Browser_Path")) {
        File f = new File(value);
        if(f.exists()) ret.addAllWebBrowserPath(value);
      }

      else if(key.equals("Is_TAB")) ret.setTab((new Boolean(value)).booleanValue());
      else if(key.equals("TAB_Size")) ret.setTabSpaceSize(Integer.parseInt(value));

      else if(key.equals("Is_Switch_Indent")) ret.setSwitchIndent((new Boolean(value)).booleanValue());
      else if(key.equals("Is_Close_Indent")) ret.setCloseIndent((new Boolean(value)).booleanValue());
      else if(key.equals("Intellisense_Delay")) ret.setIntellisenseDelay(Integer.parseInt(value));
      else if(key.equals("Is_Intellisense_On")) ret.setIntelliOnOff((new Boolean(value)).booleanValue());
      else if(key.equals("Is_Internal_HelpViewer_Use")) ret.setInternalHelpViewerUse((new Boolean(value)).booleanValue());

      else if(key.equals("Last_Opened_Dir")) {
        File f = new File(value);
        if(f.exists()) ret.setLastOpenDir(f);
      }

      else if(key.equals("Last_Project_Open_Dir")) {
        File f = new File(value);
        if(f.exists()) ret.setLastProjectOpenDir(f);
      }

      else if(key.equals("Frame_Bound")) ret.setLastFrameBounds(new Rectangle(Integer.parseInt(st.nextToken()),
                                                                           Integer.parseInt(st.nextToken()),
                                                                           Integer.parseInt(st.nextToken()),
                                                                           Integer.parseInt(st.nextToken())));

      else if(key.equals("Servlet_Runner_Path")) {
        File f = new File(value);
        if(f.exists()) ret.setServletRunnerPath(value);
      }
      else if(key.equals("Servlet_WebBrowser_Path")) {
        File f = new File(value);
        if(f.exists()) ret.setServletWebBrowserPath(value);
      }
      else if(key.equals("Servlet_Port")) ret.setServletPort(value);
      else if(key.equals("Servlet_Log")) ret.setServletLog(value);
      else if(key.equals("Servlet_Max")) ret.setServletMax(value);
      else if(key.equals("Servlet_TimeOut")) ret.setServletTimeout(value);
      else if(key.equals("Servlet_Dir")) {
        File f = new File(value);
        if(f.exists()) ret.setServletDirectory(value);
      }
      else if(key.equals("Servlet_Property")) ret.setServletProperty(value);
      else if(key.equals("Making_Batch")) ret.setMakingBatch((new Boolean(value)).booleanValue());

      else if(key.equals("JDK1_1Doc")) ret.setJavadocVersion((new Boolean(value)).booleanValue());
      else if(key.equals("verboseDoc")) ret.setVerboseDoc((new Boolean(value)).booleanValue());

      else if(key.equals("scopeDoc")) ret.setScopeDoc(Integer.parseInt(value));
      else if(key.equals("authorDoc")) ret.setAuthorDoc((new Boolean(value)).booleanValue());
      else if(key.equals("versionDoc")) ret.setVersionDoc((new Boolean(value)).booleanValue());
      else if(key.equals("noindexDoc")) ret.setNoindexDoc((new Boolean(value)).booleanValue());
      else if(key.equals("notreeDoc")) ret.setNotreeDoc((new Boolean(value)).booleanValue());
      else if(key.equals("nodeprecateDoc")) ret.setNodeprecateDoc((new Boolean(value)).booleanValue());
      else if(key.equals("newClasspathDoc")) ret.setNewclasspathDoc((new Boolean(value)).booleanValue());
      else if(key.equals("newSourcepathDoc")) ret.setNewsourcepathDoc((new Boolean(value)).booleanValue());
      else if(key.equals("useDoc")) ret.setUseDoc((new Boolean(value)).booleanValue());
      else if(key.equals("nohelpDoc")) ret.setNohelpDoc((new Boolean(value)).booleanValue());
      else if(key.equals("nonavbarDoc")) ret.setNonavbarDoc((new Boolean(value)).booleanValue());
      else if(key.equals("nodeprecatedlistDoc")) ret.setNodeprecatedlistDoc((new Boolean(value)).booleanValue());
      else if(key.equals("splitindexDoc")) ret.setSplitindexDoc((new Boolean(value)).booleanValue());

      else if(key.equals("Draw_Color_When_Print")) ret.setPrintOption(PrintSetupDlg.COLOR, (new Boolean(value)).booleanValue());
      else if(key.equals("Draw_Date_When_Print")) ret.setPrintOption(PrintSetupDlg.DATE, (new Boolean(value)).booleanValue());
      else if(key.equals("Draw_Header_When_Print")) ret.setPrintOption(PrintSetupDlg.HEADER, (new Boolean(value)).booleanValue());
      else if(key.equals("Draw_Line_When_Print")) ret.setPrintOption(PrintSetupDlg.LINE, (new Boolean(value)).booleanValue());
      else if(key.equals("Draw_PageNumber_When_Print")) ret.setPrintOption(PrintSetupDlg.PAGENUMBER, (new Boolean(value)).booleanValue());
      else if(key.equals("Draw_Wrapped_When_Print")) ret.setPrintOption(PrintSetupDlg.WRAP, (new Boolean(value)).booleanValue());

      else if(key.equals("Left_Margin_When_Print")) ret.setPrintMargin(PrintSetupDlg.LEFT, Integer.parseInt(value));
      else if(key.equals("Right_Margin_When_Print")) ret.setPrintMargin(PrintSetupDlg.RIGHT, Integer.parseInt(value));
      else if(key.equals("Bottom_Margin_When_Print")) ret.setPrintMargin(PrintSetupDlg.BOTTOM, Integer.parseInt(value));
      else if(key.equals("Top_Margin_When_Print")) ret.setPrintMargin(PrintSetupDlg.TOP, Integer.parseInt(value));

      else if(key.equals("classpathDoc")) ret.setClasspathDoc(value);
      else if(key.equals("sourcepathDoc")) ret.setSourcepathDoc(value);
      else if(key.equals("encodingDoc")) ret.setEncodingDoc(value);
      else if(key.equals("docencodingDoc")) ret.setDocencodingDoc(value);
      else if(key.equals("jDoc")) ret.setJDoc(value);
      else if(key.equals("docletDoc")) ret.setDocletDoc(value);
      else if(key.equals("docletpathDoc")) ret.setDocletpathDoc(value);
      else if(key.equals("bootclasspathDoc")) ret.setBootclasspathDoc(value);
      else if(key.equals("extdirsDoc")) ret.setExtdirsDoc(value);
      else if(key.equals("localeDoc")) ret.setLocaleDoc(value);
      else if(key.equals("linkDoc")) ret.setLinkDoc(value);
      else if(key.equals("groupDoc")) ret.setGroupDoc(value);
      else if(key.equals("helpfileDoc")) ret.setHelpfileDoc(value);
      else if(key.equals("stylesheetfileDoc")) ret.setStylesheetfileDoc(value);
      else if(key.equals("windowtitleDoc")) ret.setWindowtitleDoc(value);
      else if(key.equals("doctitleDoc")) ret.setDoctitleDoc(value);
      else if(key.equals("headerDoc")) ret.setHeaderDoc(value);
      else if(key.equals("footerDoc")) ret.setFooterDoc(value);
      else if(key.equals("bottomDoc")) ret.setBottomDoc(value);


      else if(key.equals("ROP")) {
        File f = new File(value);
        if(f.exists()) ret.setLatestOpenedProject(value);
      }
      else if(key.equals("ROF")) {
        File f = new File(value);
        if(f.exists()) ret.setLatestOpenedFile(value);
      }

      else if(key.equals("JDBC_Protocol")) ret.addJDBCProtocol(value);

      else if(key.equals("PM_JDK_Infos")) {
        boolean valid = true;
        JdkInfo jInfo = new JdkInfo();
        while(st.hasMoreElements()){
          String tok = st.nextToken();

          char firstChar = tok.charAt(0);
          if(firstChar == '1' && tok.length() > 1 && !tok.substring(1).equals("null")) {
            File f = new File(tok.substring(1));
            if(!f.exists()) {
              valid = false;
              continue;
            }

            jInfo.setJavaEXEPath(tok.substring(1));
          }
          if(firstChar == '2' && tok.length() > 1 && !tok.substring(1).equals("null")) jInfo.setJavacEXEPath(tok.substring(1));
          if(firstChar == '3' && tok.length() > 1 && !tok.substring(1).equals("null")) jInfo.setAppletviewerEXEPath(tok.substring(1));
          if(firstChar == '4' && tok.length() > 1 && !tok.substring(1).equals("null")) jInfo.setSourcePath(tok.substring(1));
          if(firstChar == '5' && tok.length() > 1 && !tok.substring(1).equals("null")) jInfo.setDocPath(tok.substring(1));
          if(firstChar == '6' && tok.length() > 1 && !tok.substring(1).equals("null")) jInfo.setVersion(tok.substring(1));
          if(firstChar == '7' && tok.length() > 1 && !tok.substring(1).equals("null")) {
            StringTokenizer st2 = new StringTokenizer(tok.substring(1), ",", false);
            Vector v = new Vector(5, 2);
            while(st2.hasMoreElements()){
              v.addElement(st2.nextToken());
            }
            jInfo.setClassPath(v);
          }
          if(firstChar == '8' && tok.length() > 1 && !tok.substring(1).equals("null")) jInfo.setJavadocEXEPath(tok.substring(1));
        }
        if(valid) jdkInfos.addJdkInfo(jInfo);
      }

      else if(key.equals("PM_AllLib_Infos")) {
        boolean valid = true;
        LibInfo lInfo = new LibInfo();
        while(st.hasMoreElements()){
          String tok = st.nextToken();
          char firstChar= tok.charAt(0);
          if(firstChar == '1' && tok.length() > 1 && !tok.substring(1).equals("null")) lInfo.setName(tok.substring(1));
          if(firstChar == '2' && tok.length() > 1 && !tok.substring(1).equals("null")) {
             File f = new File(tok.substring(1));
             if(f.exists()) lInfo.setSourcePath(tok.substring(1));
          }
          if(firstChar == '3' && tok.length() > 1 && !tok.substring(1).equals("null")) lInfo.setDocPath(tok.substring(1));
          if(firstChar == '4' && tok.length() > 1 && !tok.substring(1).equals("null")) {
            StringTokenizer st2 = new StringTokenizer(tok.substring(1), ",", false);
            Vector v = new Vector(5, 2);
            while(st2.hasMoreElements()){

              String path = st2.nextToken();
              File f = new File(path);
             
              if(f.exists()) v.addElement(path);
            }
            if(v.size() == 0) valid = false;
            lInfo.setClassPath(v);
          }
        }
         if(valid) allLibInfos.addLibraryInfo(lInfo);
      }

      else if(key.equals("PM_Selected_Lib_Infos")) {

        String libName = st.nextToken();
        if(libName != null){
          LibInfo info = (LibInfo)allLibInfos.getLibraryInfo(libName);
          if(info != null) selectedLibInfos.addLibraryInfo((LibInfo)info.clone());
        }
      }

      else if(key.equals("PM_Current_JDKInfo")){
        String jdkVersion = st.nextToken();
        if(jdkVersion != null){
          JdkInfo info = jdkInfos.getJdkInfo(jdkVersion);
          if(info != null) m.setCurrentJdkInfo((JdkInfo)info.clone());
        }
      }

      else if(key.equals("PM_Source_Root")) {
        File f = new File(value);
        if(f.exists()) m.setSourceRoot(value);
      }
      else if(key.equals("PM_Output_Root")) {
        File f = new File(value);
        if(f.exists()) m.setOutputRoot(value);
      }
      else if(key.equals("PM_ClassPath")) m.setClassPath(value);
      else if(key.equals("PM_Document_Root")){
       File f = new File(value);
       if(f.exists()) m.setDocumentRoot(value);
      }
      line = br.readLine();
    }

    if(jdkInfos.getSize() > 0) m.setJdkInfoContainer(jdkInfos);
    if(allLibInfos.getSize() > 0) m.setAllLibInfoContainer(allLibInfos);
    if(selectedLibInfos.getSize() > 0) m.setSelectedLibInfoContainer(selectedLibInfos);

    ret.setPathModel(m);

    br.close();
    isr.close();

    return ret;
  }

  public static void saveProperty(IdeProperty property){
    try{
      FileOutputStream ostream = new FileOutputStream(PROPERTY_PATH);
      ostream.write(property.toString().getBytes());
      ostream.close();
    }
    catch(IOException e){}
    catch(Exception e1){}
  }


  /////////////////// parameter hashtable ///////////////////////

  public static Hashtable loadParamHash(){
    Hashtable ret = null;

    File f = new File(PARAM_HASH_PATH);

    if(f.exists() && (f.length() > 0)){
      try{
        FileInputStream istream = new FileInputStream(f);
        ObjectInputStream p = new ObjectInputStream(istream);
        ret = (Hashtable)p.readObject();
        p.close();
   	    istream.close();
      }
      catch(Exception e){
        ret = new Hashtable(500,0.8F);
        boolean success = f.delete();

        //first time save action
        saveParamHash(ret);
      }
    }
    else {
      if(f.exists() && f.length() == 0)  f.delete();
      ret = new Hashtable(20000,0.8F);
    }

    return ret;
  }

  public static void saveParamHash(Hashtable param){

    final Hashtable paramHash = param;
    Thread t = new Thread(new Runnable(){
      public void run(){
        try{
          FileOutputStream ostream = new FileOutputStream(PARAM_HASH_PATH);
          ObjectOutputStream p = new ObjectOutputStream(ostream);
          p.writeObject(paramHash);
          ostream.close();
          p.close();
        }
        catch(IOException e){}
        catch(Exception e1){}
     }
    });

    t.setPriority(Thread.MIN_PRIORITY);
    t.start();
  }  
}
