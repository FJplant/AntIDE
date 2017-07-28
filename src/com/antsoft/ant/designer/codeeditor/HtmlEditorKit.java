/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/designer/codeeditor/HtmlEditorKit.java,v 1.5 1999/08/23 04:57:55 remember Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.5 $
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
package com.antsoft.ant.designer.codeeditor;

import javax.swing.text.*;
import javax.swing.*;
import java.util.Hashtable;
import java.awt.*;
import java.awt.event.*;
import javax.swing.plaf.basic.BasicTextUI;

import com.antsoft.ant.pool.sourcepool.*;

/**
 * This kit supports a fairly minimal handling of
 * editing java text content.  It supports syntax
 * highlighting and produces the lexical structure
 * of the document as best it can.
 *
 * @author  Timothy Prinzing
 * @version 1.2 05/04/98
 */
public class HtmlEditorKit extends DefaultEditorKit {
  private Hashtable actionHash = new Hashtable();
  
	public HtmlEditorKit() {
		super();
	}

	public HtmlContext getStylePreferences() {
		if (preferences == null) {
			preferences = new HtmlContext();
		}
		return preferences;
	}

	public void setStylePreferences(HtmlContext prefs) {
		preferences = prefs;
	}

	// --- EditorKit methods -------------------------

	/**
	 * Get the MIME type of the data that this
	 * kit represents support for.  This kit supports
	 * the type <code>text/java</code>.
	 */
	public String getContentType() {
		return "text/html";
	}

	/**
	 * Create a copy of the editor kit.  This
	 * allows an implementation to serve as a prototype
	 * for others, so that they can be quickly created.
	 */
	public Object clone() {
		HtmlEditorKit kit = new HtmlEditorKit();
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
		return new HtmlDocument();
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

  /**
   * html document에 맞는 keymap을 만들어서 반환
   */
  public void setKeyMap(Keymap map, JEditorPane editor){

    //새로 추가할 keymap의 keystroke를 정의
    KeyStroke shiftPgDown = KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN,
                            InputEvent.SHIFT_MASK, false);

    KeyStroke shiftPgUp = KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP,
                            InputEvent.SHIFT_MASK, false);

    KeyStroke shiftInsert = KeyStroke.getKeyStroke(KeyEvent.VK_INSERT,
                            InputEvent.SHIFT_MASK, false);

    KeyStroke ctrlInsert = KeyStroke.getKeyStroke(KeyEvent.VK_INSERT,
                            InputEvent.CTRL_MASK, false);

    KeyStroke shiftDelete = KeyStroke.getKeyStroke(KeyEvent.VK_DELETE,
                            InputEvent.SHIFT_MASK, false);

    //add the new mappings

    map.addActionForKeyStroke(shiftPgDown, JavaEditorKit.antSelectPgDownAction);
    map.addActionForKeyStroke(shiftPgUp, JavaEditorKit.antSelectPgUpAction);
    map.addActionForKeyStroke(shiftInsert, (Action)JavaEditorKit.defaultActions.get(DefaultEditorKit.pasteAction));
    map.addActionForKeyStroke(shiftDelete, (Action)JavaEditorKit.defaultActions.get(DefaultEditorKit.cutAction));
    map.addActionForKeyStroke(ctrlInsert, (Action)JavaEditorKit.defaultActions.get(DefaultEditorKit.copyAction));
    map.addActionForKeyStroke(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN, 0), JavaEditorKit.antPgDownAction );
    map.addActionForKeyStroke(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP, 0), JavaEditorKit.antPgUpAction);
    map.addActionForKeyStroke(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), JavaEditorKit.antDownAction);
    map.addActionForKeyStroke(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), JavaEditorKit.antUpAction);
    map.addActionForKeyStroke(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_MASK, false), JavaEditorKit.oneLineDeleteAction);
    map.addActionForKeyStroke(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP, InputEvent.CTRL_MASK, false), JavaEditorKit.antCtrlPageUpAction);
    map.addActionForKeyStroke(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN, InputEvent.CTRL_MASK, false), JavaEditorKit.antCtrlPageDownAction);
    map.addActionForKeyStroke(KeyStroke.getKeyStroke(KeyEvent.VK_UP, InputEvent.CTRL_MASK, false), JavaEditorKit.antCtrlUpAction);
    map.addActionForKeyStroke(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, InputEvent.CTRL_MASK, false), JavaEditorKit.antCtrlDownAction);

    editor.setKeymap(map);
  }

	HtmlContext preferences;
}

