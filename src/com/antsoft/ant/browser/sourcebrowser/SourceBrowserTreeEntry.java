/*
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co.
 * All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Header: /AntIDE/source/ant/browser/sourcebrowser/SourceBrowserTreeEntry.java 4     99-05-16 11:27p Multipia $
 * $Revision: 4 $
 * SourceBrowserTree���� ��带 ȿ�������� �����ϱ� ���� ��ü
 */

package com.antsoft.ant.browser.sourcebrowser;

import com.antsoft.ant.util.JavaModifier;

/**
 *  class SourceBrowserTreeEntry
 *
 *  @author Jinwoo Baek
 */
public class SourceBrowserTreeEntry {
	// constants
	public static final int FILEROOTNODE   = 1;
	public static final int CLASSNODE      = 2;
	public static final int INTERFACENODE  = 3;
	public static final int FIELDNODE      = 4;
	public static final int METHODNODE     = 5;
	public static final int INNERCLASSNODE = 6;
	public static final int LIBNODE        = 7;
	public static final int PACKAGENODE    = 8;
  public static final int IMPORTNODE     = 9;
	// current node's type (see above)
	private int type;
  /** ����� Modifier ������ ��� �ִ� ���� */
  private JavaModifier modifier;

	private String content = null;

	/**
	 *  Constructor
	 */
	public SourceBrowserTreeEntry(String content, int type) {
		this.type = type;
		this.content = content;
	}

  public SourceBrowserTreeEntry(String content, int type, JavaModifier modifier) {
    this( content, type );
    this.modifier = modifier;
  }

	/**
	 *  ����� Ÿ���� ��´�.
	 */
	public int getType() {
		return type;
	}

	/**
	 *  ����� ������ ��´�.
	 */
	public String getContent() {
		return content;
	}

  public JavaModifier getModifier() {
    return modifier;
  }

	/**
	 *  ��忡 ������ �ִ´�.
	 */
	public void setContent(String content) {
		this.content = content;
	}

  public void setModifier( JavaModifier modifier ) {
    this.modifier = modifier;
  }
  
	/**
	 *  ��带 ��Ÿ���� ���� ��´�.
	 */
	public String toString() {
		return content;
	}

  /**
   *  �� ��ü�� ������ ����.
   */
  public boolean equals(Object obj) {
  	if (obj instanceof SourceBrowserTreeEntry) {
    	SourceBrowserTreeEntry sbte = (SourceBrowserTreeEntry)obj;
      if ((sbte.getType() == this.type) && this.content.equals(sbte.getContent()))
      	return true;
    }
    return false;
  }
}
