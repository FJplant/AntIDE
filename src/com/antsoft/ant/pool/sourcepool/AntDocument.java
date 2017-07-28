package com.antsoft.ant.pool.sourcepool;

import java.io.*;
import java.util.Hashtable;
import javax.swing.text.*;
import javax.swing.event.*;
import com.antsoft.ant.util.Constants;
import javax.swing.text.AbstractDocument.Content;


/**
 *  class JavaDocument
 *
 *  @author Jinwoo Baek
 */
abstract public class AntDocument extends PlainDocument {
  private boolean isModified;
  private boolean insertMode;
  private StyleContext styles;

	/**
	 *  Constructor
	 */
  public AntDocument(AbstractDocument.Content content) {
    super(content);
    isModified = false;
    insertMode = true;
  }

	/**
	 *  현 Document가 Modify되었는지 본다.
	 */
  public final boolean isModified() {
    return isModified;
  }

	/**
	 *  Dirty를 셋팅한다.
	 */
  public final void setModified(boolean mod) {
    isModified = mod;
  }

  public final void setInsertMode(boolean b) {
  	insertMode = b;
  }

  final public boolean isInsertMode() {
  	return insertMode;
  }
  
	/**
	 *  현 Document에 관련된 Context를 얻어온다.
	 */
  protected StyleContext getPreferences() {
    return styles;
  }

	/**
	 *  Context를 셋팅한다.
	 */

  protected void setPreferences(StyleContext newStyles) {
    if(this.styles != null) this.styles = null;
    this.styles = newStyles;
  }

  /**
   *  Document 가 저장되었을 때 저장되었음을 다른 모듈에게 알리기 위한 편법
   */
  public final void saved() {
  	this.isModified = false;
  	fireChangedUpdate(new AbstractDocument.DefaultDocumentEvent(0, 0, DocumentEvent.EventType.CHANGE));
  }

  /**
   *  Overwrite 모드를 지원하도록 수정
   */   
  public final void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
		if (insertMode) {
			// Insert mod
			super.insertString(offset, str, a);
    } else {
    	// Overwrite mode
    	
    	// 현재 위치가 라인의 끝이면, 그냥 문자를 추가한다.
    	if ( !getText(offset, 1).equals( "\n" ) )
    	  remove(offset, str.length());
    	super.insertString(offset, str, a);
		}
  }

  public final void insertString(int offset, String str) throws BadLocationException {
  	super.insertString(offset, str, null);
  }

	/**
	 * Updates document structure as a result of text insertion.  This
	 * will happen within a write lock.  The superclass behavior of
	 * updating the line map is executed followed by marking any comment
	 * areas that should backtracked before scanning.
	 *
	 * @param chng the change event
	 * @param attr the set of attributes
	 */


	public final void insertUpdate(AbstractDocument.DefaultDocumentEvent chng, AttributeSet attr) {
    isModified = true;
		super.insertUpdate(chng, attr);
	}

	/**
	 * Updates any document structure as a result of text removal.
	 * This will happen within a write lock.  The superclass behavior of
	 * updating the line map is executed followed by placing a lexical
	 * update command on the analyzer queue.
	 *
	 * @param chng the change event
	 */
	public final void removeUpdate(DefaultDocumentEvent chng) {
    isModified = true;
		super.removeUpdate(chng);
  }

	// --- variables ------------------------------------------------

	/**
	 * Fetch a reasonable location to start scanning
	 * given the desired start location.  This allows
	 * for adjustments needed to accomodate multiline
	 * comments.
	 */
	abstract protected int getScannerStart(int p);
	abstract protected Object createScanner();
 ////////////////////////////////////////////////////////////////////////////////////
}

