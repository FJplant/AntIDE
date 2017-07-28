/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/property/IdeProperty.java,v 1.5 1999/08/19 09:36:46 remember Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.5 $
 */
package com.antsoft.ant.property;

import java.util.Vector;
import java.util.Enumeration;
import java.io.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import javax.swing.DefaultComboBoxModel;

import com.antsoft.ant.property.*;
import com.antsoft.ant.util.FontList;
import com.antsoft.ant.util.Constants;
import com.antsoft.ant.property.defaultproperty.ColorPanel;
import com.antsoft.ant.tools.htmlgenerator.HtmlGeneratorDlg;
import com.antsoft.ant.tools.print.PrintSetupDlg;


/**
 * IDE property
 *
 * @author kim sang kyun
 */
public class IdeProperty implements Serializable{

  private DefaultPathModel pathModel;
  private Color keywordColor, commentColor, constantColor, stringColor;
  private Color backgroundColor = Color.white;
  private Color htmlKeywordColor, htmlCommentColor, htmlLiteralColor, htmlOtherColor, htmlBackgroundColor;
  private Font font = FontList.dialogInput12;
  private Vector allWebBrowserPaths = new Vector(3,2);
  private String selectedBrowserPath;
  private String selectedBrowserPathToSave;

  private boolean isTab = false;
  private int tabSpaceSize = 2;
  private boolean isSwitchIndent = false;
  private boolean isCloseIndent = false;
  private int intellisenseDelay = 100;
  private boolean intelliOnOff = true;
  private boolean isInternalHelpViewerUse = true;

  private File lastOpenedDir = null;
  private File lastProjectOpenDir = null;
  private Rectangle frameBound = null;

  // 에디터 관련 property
  private boolean isAutoIndentMode = true;
  private boolean isInsertMode = true;
  private boolean isUseTabChar = true;
  private boolean isGroupUndo = false;
  private boolean isSynClring = true;

 //servlet 관련 property  - Itree
  private String servletRunnerPath, servletWebBrowserPath;
  private boolean makingBatch;
  private String servletPort, servletLog, servletMax, servletTimeout, servletDirectory, servletProperty;

 // javadoc option 관련 property - Itree
  private boolean jdk1_1Doc, verboseDoc, authorDoc, versionDoc, noindexDoc, notreeDoc, nodeprecateDoc;
  private int scopeDoc;
  private String classpathDoc, sourcepathDoc, encodingDoc, docencodingDoc, jDoc;
  private boolean newClasspathDoc, newSourcepathDoc;
  private boolean useDoc, nohelpDoc, nonavbarDoc, nodeprecatedlistDoc, splitindexDoc;
  private String docletDoc, docletpathDoc, bootclasspathDoc, extdirsDoc, localeDoc, linkDoc, linkofflineDoc,
                  groupDoc, helpfileDoc, stylesheetfileDoc;
  private String windowtitleDoc, doctitleDoc, headerDoc, footerDoc, bottomDoc;

 // 최근 연 프로젝트 파일 경로 및 연 파일 경로 저장
  private Vector recentOpenedProjects = new Vector(5, 1);
  private Vector recentOpenedFiles = new Vector(10, 1);

 // JDBC 프로토콜 저장
  private Vector jdbcProtocols = new Vector(5, 2);

  //print 관련 property
  private boolean drawLine = true;
  private boolean drawHeader = true;
  private boolean drawPageNumber = true;
  private boolean drawDate = true;
  private boolean drawWrapped = true;
  private boolean drawColored = true;

  private int leftMargin = 0;
  private int rightMargin = 0;
  private int bottomMargin = 0;
  private int topMargin = 0;

  /** constructor */
  public IdeProperty() {
    //path model setting
    pathModel = new DefaultPathModel();

    //color setting
    setColor(ColorPanel.KEYWORD, new Color(0, 0, 255));
    setColor(ColorPanel.COMMENT, new Color(0, 153, 0));
    setColor(ColorPanel.CONSTANT, new Color(255, 0, 0));
    setColor(ColorPanel.STRING, new Color(0, 0, 0));
    setColor(ColorPanel.BACKGROUND, new Color(255, 255, 255));

    jdbcProtocols.addElement("jdbd:odbc");
		jdbcProtocols.addElement("jdbc:psql");
		jdbcProtocols.addElement("jdbc:mysql");
		jdbcProtocols.addElement("jdbc:msql");
  }

  ///////////////////// Path Property////////////////////////
  public void setPathModel(DefaultPathModel model){
    pathModel = model;
  }

  public DefaultPathModel getPathModel(){
    return this.pathModel;
  }

  public DefaultPathModel getPathModelClone(){
    return (DefaultPathModel)pathModel.clone();
  }

  ///////////////////// Color Property////////////////////////
  public Color getColor(String type){
    Color ret = null;
    if(type.equals(ColorPanel.KEYWORD)) ret = keywordColor;
    else if(type.equals(ColorPanel.COMMENT)) ret = commentColor;
    else if(type.equals(ColorPanel.CONSTANT)) ret = constantColor;
    else if(type.equals(ColorPanel.STRING)) ret = stringColor;
    else if (type.equals(ColorPanel.BACKGROUND)) ret = backgroundColor;

    return ret;
  }

  public void setColor(String type, Color newColor){
    if(type.equals(ColorPanel.KEYWORD)) keywordColor = newColor;
    else if(type.equals(ColorPanel.COMMENT)) commentColor = newColor;
    else if(type.equals(ColorPanel.CONSTANT)) constantColor = newColor;
    else if(type.equals(ColorPanel.STRING)) stringColor = newColor;
    else if (type.equals(ColorPanel.BACKGROUND)) backgroundColor = newColor;
  }
  ///////////////////// Color Property////////////////////////

  ///////////////////// Html Generator Color Property/////////////////////////
  public Color getHtmlColor(String type){
    Color ret = null;
    if(type.equals(HtmlGeneratorDlg.KEYWORD)) ret = htmlKeywordColor;
    else if(type.equals(HtmlGeneratorDlg.COMMENT)) ret = htmlCommentColor;
    else if(type.equals(HtmlGeneratorDlg.BACKGROUND)) ret = htmlBackgroundColor;
    else if(type.equals(HtmlGeneratorDlg.LITERAL)) ret = htmlLiteralColor;
    else if (type.equals(HtmlGeneratorDlg.OTHER)) ret = htmlOtherColor;

    return ret;
  }

  public void setHtmlColor(String type, Color newColor){
    if(type.equals(HtmlGeneratorDlg.KEYWORD)) htmlKeywordColor = newColor;
    else if(type.equals(HtmlGeneratorDlg.COMMENT)) htmlCommentColor = newColor;
    else if (type.equals(HtmlGeneratorDlg.BACKGROUND)) htmlBackgroundColor = newColor;
    else if(type.equals(HtmlGeneratorDlg.LITERAL)) htmlLiteralColor = newColor;
    else if(type.equals(HtmlGeneratorDlg.OTHER)) htmlOtherColor = newColor;
  }
  ///////////////////// Html Generator Color Property/////////////////////////

  //////////////////////////// Font Property/////////////////////////////////
  public Font getSelectedFont(){
    return font;
  }

  public void setSelectedFont(Font newFont){
    font = newFont;
  }
  //////////////////////////// Font Property/////////////////////////////////

  public void addAllWebBrowserPath(String path){
    if(allWebBrowserPaths == null) allWebBrowserPaths = new Vector(2, 2);
    allWebBrowserPaths.addElement(path);
  }

  public Vector getAllWebBrowserPaths(){
    return allWebBrowserPaths;
  }

  public String getWebBrowserPath(){
    return selectedBrowserPath;
  }

  public boolean isInternalHelpViewerUse(){
    return this.isInternalHelpViewerUse;
  }

  public void setInternalHelpViewerUse(boolean flag){
    this.isInternalHelpViewerUse = flag;
  }

  public void setAllWebBrowserPaths(Vector model){
    allWebBrowserPaths = model;
  }

  public void setSelectedWebBrowserPath(String path){
    selectedBrowserPath = path;
  }

  public void setSelectedWebBrowserPathToSave(String path){
    this.selectedBrowserPathToSave = path;
  }

  public String getWebBrowserPathToRestore(){
    return this.selectedBrowserPathToSave;
  }

  public void setTab(boolean isTab){
    this.isTab = isTab;
  }

  public boolean isTab(){
    return isTab;
  }

  public void setLastFrameBounds( Rectangle bound ){
    frameBound = bound;
  }

  public Rectangle getLastFrameBounds(){
    return frameBound;
  }

  public void setTabSpaceSize(int size){
    tabSpaceSize = size;
  }

  public int getTabSpaceSize(){
    return tabSpaceSize;
  }

  public void setSwitchIndent(boolean flag){
    isSwitchIndent = flag;
  }

  public boolean isSwitchIndent(){
    return isSwitchIndent;
  }

  public void setCloseIndent(boolean flag){
    isCloseIndent = flag;
  }

  public boolean isCloseIndent(){
    return isCloseIndent;
  }

  public void setIntellisenseDelay(int delay){
    intellisenseDelay = delay;
  }

  public int getIntellisenseDelay(){
    return intellisenseDelay;
  }

  public void setIntelliOnOff(boolean onOff){
    intelliOnOff = onOff;
  }

  public boolean isIntelliOn(){
    return intelliOnOff;
  }

  public void setLastOpenDir(File dir){
    lastOpenedDir = dir;
  }

  public File getLastOpenedDir(){
    return lastOpenedDir;
  }

  public void setLastProjectOpenDir(File dir){
    lastProjectOpenDir = dir;
  }

  public File getLastProjectOpenDir(){
    return lastProjectOpenDir;
  }

  // 최근에 연 파일이 있음을 알린다.
  public void setLatestOpenedProject(String projectFileName) {
  	if (recentOpenedProjects.contains(projectFileName)) {
    	recentOpenedProjects.removeElement(projectFileName);
    } else if (recentOpenedProjects.size() >= 5)
    	recentOpenedProjects.removeElementAt(recentOpenedProjects.size() - 1);
    recentOpenedProjects.insertElementAt(projectFileName, 0);
  }

  public void setLatestOpenedFile(String fileName) {
  	if (recentOpenedFiles.contains(fileName)) {
    	recentOpenedFiles.removeElement(fileName);
    } else if (recentOpenedProjects.size() >= 10)
    	recentOpenedFiles.removeElementAt(recentOpenedFiles.size() - 1);
  	recentOpenedFiles.insertElementAt(fileName, 0);
  }

  // 최근에 연 파일들을 얻어온다.
  public Vector getLatestProjects() {
  	return recentOpenedProjects;
  }

  public Vector getLatestFiles() {
  	return recentOpenedFiles;
  }

  // 프로토콜을 추가한다.
  public void addJDBCProtocol(String protocol) {
  	if (protocol != null) {
    	if (!jdbcProtocols.contains(protocol))
      	jdbcProtocols.addElement(protocol);
    }
  }

  // 프로토콜을 제거한다.
  public void removeJDBCProtocol(String protocol) {
  	if (protocol != null)
    	jdbcProtocols.removeElement(protocol);
  }

  public Vector getProtocols() {
  	return jdbcProtocols;
  }

  // Servlet 관련 Property 설정 함수들
  public void setServletRunnerPath(String path) {
    servletRunnerPath = path;
  }
  public String getServletRunnerPath() {
    return servletRunnerPath;
  }
  public void setServletWebBrowserPath(String path) {
    servletWebBrowserPath = path;
  }
  public String getServletWebBrowserPath() {
    return servletWebBrowserPath;
  }
  public void setMakingBatch(boolean b) {
    makingBatch = b;
  }
  public boolean getMakingBatch() {
    return makingBatch;
  }
  public void setServletPort(String port) {
    servletPort = port;
  }
  public String getServletPort() {
    return servletPort;
  }
  public void setServletLog(String log) {
    servletLog = log;
  }
  public String getServletLog() {
    return servletLog;
  }
  public void setServletMax(String max) {
    servletMax = max;
  }
  public String getServletMax() {
    return servletMax;
  }
  public void setServletTimeout(String timeout) {
    servletTimeout = timeout;
  }
  public String getServletTimeout() {
    return servletTimeout;
  }
  public void setServletDirectory(String directory) {
    servletDirectory = directory;
  }
  public String getServletDirectory() {
    return servletDirectory;
  }
  public void setServletProperty(String property) {
    servletProperty = property;
  }
  public String getServletProperty() {
    return servletProperty;
  }

  // javadoc option 관련 함수
  public void setJavadocVersion(boolean b) {
    // true : 1.1 version
    // false : 1.2 version
    jdk1_1Doc = b;
  }
  public void setScopeDoc(int i) {
    scopeDoc = i;
  }
  public void setVerboseDoc(boolean b) {
    verboseDoc = b;
  }
  public void setAuthorDoc(boolean b) {
    authorDoc = b;
  }
  public void setVersionDoc(boolean b) {
    versionDoc = b;
  }
  public void setNoindexDoc(boolean b) {
    noindexDoc = b;
  }
  public void setNotreeDoc(boolean b) {
    notreeDoc = b;
  }
  public void setNodeprecateDoc(boolean b) {
    nodeprecateDoc = b;
  }
  public void setNewclasspathDoc(boolean b) {
    newClasspathDoc = b;
  }
  public void setNewsourcepathDoc(boolean b) {
    newSourcepathDoc = b;
  }
  public void setClasspathDoc(String s) {
    classpathDoc = s;
  }
  public void setSourcepathDoc(String s) {
    sourcepathDoc = s;
  }
  public void setEncodingDoc(String s) {
    encodingDoc = s;
  }
  public void setDocencodingDoc(String s) {
    docencodingDoc = s;
  }
  public void setJDoc(String s) {
    jDoc = s;
  }
  public void setUseDoc(boolean b) {
    useDoc = b;
  }
  public void setNohelpDoc(boolean b) {
    nohelpDoc = b;
  }
  public void setNonavbarDoc(boolean b) {
    nonavbarDoc = b;
  }
  public void setNodeprecatedlistDoc(boolean b) {
    nodeprecatedlistDoc = b;
  }
  public void setSplitindexDoc(boolean b) {
    splitindexDoc = b;
  }
  public void setDocletDoc(String s) {
    docletDoc = s;
  }
  public void setDocletpathDoc(String s) {
    docletpathDoc = s;
  }
  public void setBootclasspathDoc(String s) {
    bootclasspathDoc = s;
  }
  public void setExtdirsDoc(String s) {
    extdirsDoc = s;
  }
  public void setLocaleDoc(String s) {
    localeDoc = s;
  }
  public void setLinkDoc(String s) {
    linkDoc = s;
  }
  public void setLinkofflineDoc(String s) {
    linkofflineDoc = s;
  }
  public void setGroupDoc(String s) {
    groupDoc = s;
  }
  public void setHelpfileDoc(String s) {
    helpfileDoc = s;
  }
  public void setStylesheetfileDoc(String s) {
    stylesheetfileDoc= s;
  }
  public void setWindowtitleDoc(String s) {
  windowtitleDoc = s;
  }
  public void setDoctitleDoc(String s) {
    doctitleDoc = s;
  }
  public void setHeaderDoc(String s) {
    headerDoc = s;
  }
  public void setFooterDoc(String s) {
    footerDoc = s;
  }
  public void setBottomDoc(String s) {
    bottomDoc = s;
  }
    //// get 함수 모음

  public boolean getJavadocVersion() {
    // true : 1.1 version
    // false : 1.2 version
    return jdk1_1Doc;
  }
  public int getScopeDoc() {
    return scopeDoc;
  }
  public boolean getVerboseDoc() {
    return verboseDoc;
  }
  public boolean getAuthorDoc() {
    return authorDoc;
  }
  public boolean getVersionDoc() {
    return versionDoc;
  }
  public boolean getNoindexDoc() {
    return noindexDoc;
  }
  public boolean getNotreeDoc() {
    return notreeDoc;
  }
  public boolean getNodeprecateDoc() {
    return nodeprecateDoc;
  }
  public boolean getNewclasspathDoc() {
    return newClasspathDoc;
  }
  public boolean getNewsourcepathDoc() {
    return newSourcepathDoc;
  }
  public String getClasspathDoc() {
    return classpathDoc;
  }
  public String getSourcepathDoc() {
    return sourcepathDoc;
  }
  public String getEncodingDoc() {
    return encodingDoc;
  }
  public String getDocencodingDoc() {
    return docencodingDoc;
  }
  public String getJDoc() {
    return jDoc;
  }
  public boolean getUseDoc() {
    return useDoc;
  }
  public boolean getNohelpDoc() {
    return nohelpDoc;
  }
  public boolean getNonavbarDoc() {
    return nonavbarDoc;
  }
  public boolean getNodeprecatedlistDoc() {
    return nodeprecatedlistDoc;
  }
  public boolean getSplitindexDoc() {
    return splitindexDoc;
  }
  public String getDocletDoc() {
    return docletDoc;
  }
  public String getDocletpathDoc() {
    return docletpathDoc;
  }
  public String getBootclasspathDoc() {
    return bootclasspathDoc;
  }
  public String getExtdirsDoc() {
    return extdirsDoc;
  }
  public String getLocaleDoc() {
    return localeDoc;
  }
  public String getLinkDoc() {
    return linkDoc;
  }
  public String getLinkofflineDoc() {
    return linkofflineDoc;
  }
  public String getGroupDoc() {
    return groupDoc;
  }
  public String getHelpfileDoc() {
    return helpfileDoc;
  }
  public String getStylesheetfileDoc() {
    return stylesheetfileDoc;
  }
  public String getWindowtitleDoc() {
    return windowtitleDoc;
  }
  public String getDoctitleDoc() {
    return doctitleDoc;
  }
  public String getHeaderDoc() {
    return headerDoc;
  }
  public String getFooterDoc() {
    return footerDoc;
  }
  public String getBottomDoc() {
    return bottomDoc;
  }

  // 에디터 옵션 관련 함수
  public boolean isAutoIndentMode() {
  	return isAutoIndentMode;
  }

  public boolean isInsertMode() {
  	return isInsertMode;
  }

  public boolean isUseTabChar() {
  	return isUseTabChar;
  }

  public boolean isGroupUndo() {
  	return isGroupUndo;
  }

  public boolean isSynClring() {
  	return isSynClring;
  }

  public void setAutoIndentMode(boolean b) {
  	this.isAutoIndentMode = b;
  }

  public void setInsertMode(boolean b) {
  	this.isInsertMode = b;
  }

  public void setUseTabChar(boolean b) {
  	this.isUseTabChar = b;
  }

  public void setGroupUndo(boolean b) {
  	this.isGroupUndo = b;
  }

  public void setSynClring(boolean b) {
  	this.isSynClring = b;
  }

  public int getPrintMargin(int type){
    int ret=0;
    switch(type){
      case PrintSetupDlg.LEFT :
        ret = leftMargin;
        break;
      case PrintSetupDlg.RIGHT :
        ret = rightMargin;
        break;
      case PrintSetupDlg.BOTTOM :
        ret = bottomMargin;
        break;
      case PrintSetupDlg.TOP :
        ret = topMargin;
        break;
    }
    return ret;
  }


  public void setPrintMargin(int type, int value){
    switch(type){
      case PrintSetupDlg.LEFT :
        leftMargin = value;
        break;
      case PrintSetupDlg.RIGHT :
        rightMargin = value;
        break;
      case PrintSetupDlg.BOTTOM :
        bottomMargin = value;
        break;
      case PrintSetupDlg.TOP :
        topMargin = value;
        break;
    }
  }

  public boolean getPrintOption(int type){
    boolean ret=false;
    switch(type){
      case PrintSetupDlg.COLOR :
        ret = drawColored;
        break;
      case PrintSetupDlg.DATE :
        ret = drawDate;
        break;
      case PrintSetupDlg.HEADER :
        ret = drawHeader;
        break;
      case PrintSetupDlg.LINE :
        ret = drawLine;
        break;
      case PrintSetupDlg.PAGENUMBER :
        ret = drawPageNumber;
        break;
      case PrintSetupDlg.WRAP :
        ret = drawWrapped;
        break;
    }
    return ret;
  }

  public void setPrintOption(int type, boolean value){
    switch(type){
      case PrintSetupDlg.COLOR :
        drawColored = value;
        break;
      case PrintSetupDlg.DATE :
        drawDate = value;
        break;
      case PrintSetupDlg.HEADER :
        drawHeader = value;
        break;
      case PrintSetupDlg.LINE :
        drawLine = value;
        break;
      case PrintSetupDlg.PAGENUMBER :
        drawPageNumber = value;
        break;
      case PrintSetupDlg.WRAP :
        drawWrapped = value;
        break;
    }
  }

  public String toString(){
    StringBuffer buf = new StringBuffer();

    //version
    buf.append("##Do not modify next line!!!" + "\r\n");
    buf.append("ANT_SETUP_VERSION=1.0" + "\r\n\r\n");

    // Editor Property
    buf.append("Auto_Indent_Mode=" + isAutoIndentMode + Constants.lineSeparator);
    buf.append("Insert_Mode=" + isInsertMode + Constants.lineSeparator);
    buf.append("Use_Tab_Char=" + isUseTabChar + Constants.lineSeparator);
    buf.append("Group_Undo=" + isGroupUndo + Constants.lineSeparator);
    buf.append("Syntax_Colouring=" + isSynClring + Constants.lineSeparator);

    //color
    if(keywordColor != null)  buf.append("Keyword_Color=" + keywordColor.getRGB() +"\r\n");
    if(commentColor != null)  buf.append("Comment_Color=" + commentColor.getRGB() +"\r\n");
    if(constantColor != null)  buf.append("Constant_Color=" + constantColor.getRGB() +"\r\n");
    if(stringColor != null)  buf.append("String_Color=" + stringColor.getRGB() +"\r\n");
    if(backgroundColor != null)  buf.append("Background_Color=" + backgroundColor.getRGB() +"\r\n");

    //html color
    if(htmlKeywordColor != null) buf.append("Html_Keyword_Color=" + htmlKeywordColor.getRGB() + "\r\n");
    if(htmlCommentColor != null) buf.append("Html_Comment_Color=" + htmlCommentColor.getRGB() + "\r\n");
    if(htmlLiteralColor != null) buf.append("Html_Literal_Color=" + htmlLiteralColor.getRGB() + "\r\n");
    if(htmlOtherColor != null) buf.append("Html_Other_Color=" + htmlOtherColor.getRGB() + "\r\n");
    if(htmlBackgroundColor != null) buf.append("Html_Background_Color=" + htmlBackgroundColor.getRGB() + "\r\n");

    //font
    if(font != null) {
      buf.append("Font=" + font.getName() + "#" + font.getStyle() + "#" + font.getSize() + "\r\n");
    }

    //browser path
    if(selectedBrowserPath!=null) buf.append("Selected_WebBrowser_Path=" + selectedBrowserPath + "\r\n");
    if(selectedBrowserPathToSave!=null) buf.append("Selected_WebBrowser_Path_ToSave=" + selectedBrowserPathToSave + "\r\n");
    if(allWebBrowserPaths != null)
    for(int i=0; i<allWebBrowserPaths.size(); i++)
      buf.append("Browser_Path=" + (String)allWebBrowserPaths.elementAt(i) +"\r\n");

    //tab
    buf.append("Is_TAB=" + isTab + "\r\n");
    buf.append("TAB_Size=" + tabSpaceSize + "\r\n");

    //print options
    buf.append("Draw_Color_When_Print=" + drawColored + "\r\n");
    buf.append("Draw_Date_When_Print=" + drawDate + "\r\n");
    buf.append("Draw_Header_When_Print=" + drawHeader + "\r\n");
    buf.append("Draw_Line_When_Print=" + drawLine + "\r\n");
    buf.append("Draw_PageNumber_When_Print=" + drawPageNumber + "\r\n");
    buf.append("Draw_Wrapped_When_Print=" + drawWrapped + "\r\n");

    buf.append("Left_Margin_When_Print=" + leftMargin + "\r\n");
    buf.append("Right_Margin_When_Print=" + rightMargin + "\r\n");
    buf.append("Bottom_Margin_When_Print=" + bottomMargin + "\r\n");
    buf.append("Top_Margin_When_Print=" + topMargin + "\r\n");

    //options
    buf.append("Is_Switch_Indent=" + isSwitchIndent + "\r\n");
    buf.append("Is_Close_Indent=" + isCloseIndent + "\r\n");
    buf.append("Intellisense_Delay=" + intellisenseDelay + "\r\n");
    buf.append("Is_Intellisense_On=" + intelliOnOff + "\r\n");
    buf.append("Is_Internal_HelpViewer_Use=" + isInternalHelpViewerUse + "\r\n");

    if(lastOpenedDir != null)
    buf.append("Last_Opened_Dir=" + lastOpenedDir.getAbsolutePath() + "\r\n");

    if(lastProjectOpenDir != null)
    buf.append("Last_Project_Open_Dir=" + lastProjectOpenDir.getAbsolutePath() + "\r\n");

    if(frameBound != null)
    buf.append("Frame_Bound=" + frameBound.x + "#" + frameBound.y + "#" + frameBound.width + "#" + frameBound.height + "\r\n");

    //servlet

    if(servletRunnerPath!=null) buf.append("Servlet_Runner_Path=" + servletRunnerPath + "\r\n");
    if(servletWebBrowserPath!=null) buf.append("Servlet_WebBrowser_Path=" +  servletWebBrowserPath + "\r\n");
    if(servletPort!=null) buf.append("Servlet_Port=" + servletPort + "\r\n");
    if(servletLog!=null) buf.append("Servlet_Log=" + servletLog + "\r\n");
    if(servletMax!=null) buf.append("Servlet_Max=" + servletMax + "\r\n");
    if(servletTimeout!=null) buf.append("Servlet_TimeOut=" + servletTimeout + "\r\n");
    if(servletDirectory!=null) buf.append("Servlet_Dir=" + servletDirectory + "\r\n");
    if(servletProperty!=null) buf.append("Servlet_Property=" + servletProperty + "\r\n");
    buf.append("Making_Batch=" + makingBatch + "\r\n");

    //javadoc option
    buf.append("JDK1_1Doc=" + jdk1_1Doc + "\r\n");
    buf.append("verboseDoc=" + verboseDoc + "\r\n");
    buf.append("scopeDoc=" + scopeDoc + "\r\n");
    buf.append("authorDoc=" + authorDoc + "\r\n");
    buf.append("versionDoc=" + versionDoc + "\r\n");
    buf.append("noindexDoc=" + noindexDoc + "\r\n");
    buf.append("notreeDoc=" + notreeDoc + "\r\n");
    buf.append("nodeprecateDoc=" + nodeprecateDoc + "\r\n");
    buf.append("newClasspathDoc=" + newClasspathDoc + "\r\n");
    buf.append("newSourcepathDoc=" + newSourcepathDoc + "\r\n");
    if(classpathDoc!=null) buf.append("classpathDoc=" + classpathDoc + "\r\n");
    if(sourcepathDoc!=null) buf.append("sourcepathDoc=" + sourcepathDoc + "\r\n");
    if(encodingDoc!=null) buf.append("encodingDoc=" + encodingDoc + "\r\n");
    if(docencodingDoc!=null) buf.append("docencodingDoc=" + docencodingDoc + "\r\n");
    if(jDoc!=null) buf.append("jDoc=" + jDoc + "\r\n");
    buf.append("useDoc=" + useDoc + "\r\n");
    buf.append("nohelpDoc=" + nohelpDoc + "\r\n");
    buf.append("nonavbarDoc=" + nonavbarDoc + "\r\n");
    buf.append("nodeprecatedlistDoc=" + nodeprecatedlistDoc + "\r\n");
    buf.append("splitindexDoc=" + splitindexDoc + "\r\n");
    if(docletDoc!=null) buf.append("docletDoc=" + docletDoc + "\r\n");
    if(docletpathDoc!=null) buf.append("docletpathDoc=" + docletpathDoc + "\r\n");
    if(bootclasspathDoc!=null) buf.append("bootclasspathDoc=" + bootclasspathDoc + "\r\n");
    if(extdirsDoc!=null) buf.append("extdirsDoc=" + extdirsDoc + "\r\n");
    if(localeDoc!=null) buf.append("localeDoc=" + localeDoc + "\r\n");
    if(linkDoc!=null) buf.append("linkDoc=" + linkDoc + "\r\n");
    if(linkofflineDoc!=null) buf.append("linkofflineDoc=" + linkofflineDoc + "\r\n");
    if(groupDoc!=null) buf.append("groupDoc=" + groupDoc + "\r\n");
    if(helpfileDoc!=null) buf.append("helpfileDoc=" + helpfileDoc + "\r\n");
    if(stylesheetfileDoc!=null) buf.append("stylesheetfileDoc=" + stylesheetfileDoc + "\r\n");
    if(windowtitleDoc!=null) buf.append("windowtitleDoc=" + windowtitleDoc + "\r\n");
    if(doctitleDoc!=null) buf.append("doctitleDoc=" + doctitleDoc + "\r\n");
    if(headerDoc!=null) buf.append("headerDoc=" + headerDoc + "\r\n");
    if(footerDoc!=null) buf.append("footerDoc=" + footerDoc + "\r\n");
    if(bottomDoc!=null) buf.append("bottomDoc=" + bottomDoc + "\r\n");

    //history
    if(recentOpenedProjects != null){
      for(int i=0; i<recentOpenedProjects.size(); i++)
        buf.append("ROP=" + (String)recentOpenedProjects.elementAt(i) +"\r\n");
    }

    if(recentOpenedFiles != null)
    for(int i=0; i<recentOpenedFiles.size(); i++)
      buf.append("ROF=" + (String)recentOpenedFiles.elementAt(i) +"\r\n");


    //jdbc protocol
    if(jdbcProtocols != null){
      for(int i=0; i<jdbcProtocols.size(); i++)
        buf.append("JDBC_Protocol=" + (String)jdbcProtocols.elementAt(i) +"\r\n");
    }

    //pathModel
    if(pathModel != null) buf.append(pathModel.toString());

    return buf.toString();
  }
}
