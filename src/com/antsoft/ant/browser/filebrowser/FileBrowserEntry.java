/*
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co.
 * All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/browser/filebrowser/FileBrowserEntry.java,v 1.5 1999/07/27 04:53:53 multipia Exp $
 * $Revision: 1.5 $
 */
package com.antsoft.ant.browser.filebrowser;

/**
 *  class SourceBrowserTreeEntry
 *
 *  @author Chul-Mok Lee
 */
public class FileBrowserEntry {
	// constants
	public static final int DRIVER   = 1;
	public static final int DIR      = 2;
	public static final int OPENDIR  = 3;
	public static final int COMPUTER = 4;
  // files
  public static final int JAVA = 5;
  public static final int JAVASCRIPT = 6;
  public static final int PERL = 7;
  public static final int TEXT = 8;
  public static final int HTML = 9;
  public static final int GIF  = 10;
  public static final int JPG  = 11;
  public static final int BMP  = 12;

	private int type;
  private boolean check;
	private String content = null;
	private String[] childs = null;
	/**
	 *  Constructor
	 */
	public FileBrowserEntry(String content, int type, String[] childs, boolean check) {
		this.type = type;
		this.content = content;
		this.check = check;
		this.childs = childs;
	}

	/**
	 *  노드의 타입을 얻는다.
	 */
	public int getType() {
		return type;
	}

  public void setType(int type) {
    this.type = type;
  }

	/**
	 *  노드의 내용을 얻는다.
	 */
	public String getContent() {
		return content;
	}

	/**
	 *  노드에 내용을 넣는다.
	 */
	public void setContent(String content) {
		this.content = content;
	}
		/**
	 * Gets the check
	 * @return check
	 */
	public boolean getCheck ()
	{
		return check;
	}

	/**
	 * Sets the check
	 * @param check
	 */
	public void setCheck ( boolean check )
	{
		this.check = check;
	}

	/**
	 * Gets the childs
	 * @return childs
	 */
	public String[] getChilds ()
	{
		return childs;
	}

	/**
	 * Sets the childs
	 * @param childs
	 */
	public void setChilds ( String[] childs )
	{
		this.childs = childs;
	}


	/**
	 *  노드를 나타내는 값을 얻는다.
	 */
	public String toString() {
		return content;
	}


  /**
   *  두 객체가 같은지 본다.
   */
  public boolean equals(Object obj) {
  	if (obj instanceof FileBrowserEntry) {
    	FileBrowserEntry sbte = (FileBrowserEntry)obj;
      if ((sbte.getType() == this.type) && this.content.equals(sbte.getContent()))
      	return true;
    }
    return false;
  }
}

/*
 * $Log: FileBrowserEntry.java,v $
 * Revision 1.5  1999/07/27 04:53:53  multipia
 * Java Script, Perl 추가
 *
 * Revision 1.4  1999/07/27 04:45:52  multipia
 * Header 부분 추가
 *
 */
