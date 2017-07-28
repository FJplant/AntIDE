/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/debugger/JavaEditorKit.java,v 1.1 1999/08/03 00:49:25 strife Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.1 $
 * $History: JavaEditorKit.java $
 * 
 * *****************  Version 2  *****************
 * User: Multipia     Date: 99-05-17   Time: 12:17a
 * Updated in $/AntIDE/source/ant/designer/codeeditor
 * 
 * *****************  Version 1  *****************
 * User: Insane       Date: 98-09-12   Time: 5:16p
 * Created in $/Ant/src/ant/designer/codeeditor
 * 자바 프로그래밍용 텍스트 편집기
 *
 */
package com.antsoft.ant.debugger;

import javax.swing.text.*;

/**
 * This kit supports a fairly minimal handling of
 * editing java text content.  It supports syntax
 * highlighting and produces the lexical structure
 * of the document as best it can.
 *
 * @author  Timothy Prinzing
 * @version 1.2 05/04/98
 */
public class JavaEditorKit extends DefaultEditorKit { //implements ViewFactory {
	public JavaEditorKit() {
		super();
	}
  
	public JavaContext getStylePreferences() {
		if (preferences == null) {
			preferences = new JavaContext();
		}
		return preferences;
	}

	public void setStylePreferences(JavaContext prefs) {
		preferences = prefs;
	}

	// --- EditorKit methods -------------------------

	/**
	 * Get the MIME type of the data that this
	 * kit represents support for.  This kit supports
	 * the type <code>text/java</code>.
	 */
	public String getContentType() {
		return "text/java";
	}

	/**
	 * Create a copy of the editor kit.  This
	 * allows an implementation to serve as a prototype
	 * for others, so that they can be quickly created.
	 */
	public Object clone() {
		JavaEditorKit kit = new JavaEditorKit();
		kit.preferences = preferences;
		return kit;
	}

	/**
	 * Creates an uninitialized text storage model
	 * that is appropriate for this type of editor.
	 *
	 * @return the model
	 */
	public Document createDefaultDocument() {
		return new JavaDocument();
	}

	/**
	 * Fetches a factory that is suitable for producing
	 * views of any models that are produced by this
	 * kit.  The default is to have the UI produce the
	 * factory, so this method has no implementation.
	 *
	 * @return the view factory
	 */
	public final ViewFactory getViewFactory() {
		return getStylePreferences();
	}

	JavaContext preferences;
}
