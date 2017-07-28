/*
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 */
package com.antsoft.ant.pool.sourcepool;

import java.io.*;
import java.util.Hashtable;
import javax.swing.text.*;
import javax.swing.event.*;

import com.antsoft.ant.util.Constants;

/**
 *  class HtmlDocument
 *
 *  @author Kim, Sung-Hoon.
 *  @author Junwoo Baek.
 */
public class NormalDocument extends AntDocument {
  private boolean isModified;
  private NormalContext styles = new NormalContext();
  private boolean mode = true;

	/**
	 *  Constructor
	 */
  public NormalDocument() {
  	this( 1024 );
  }
  
  public NormalDocument( int size ) {
    super(new MyGapContent( size ));
    isModified = false;
  }

	/**
	 *  Context를 셋팅한다.
	 */

  public void setPreferences(StyleContext newStyles) {
    if(this.styles != null) this.styles = null;
    this.styles = (NormalContext)newStyles;
  }

	public Object createScanner() {return null;}
	public int getScannerStart(int p) {return -1;}
}

