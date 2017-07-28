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
	 *  �� Document�� Modify�Ǿ����� ����.
	 */
  public final boolean isModified() {
    return isModified;
  }

	/**
	 *  Dirty�� �����Ѵ�.
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
	 *  �� Document�� ���õ� Context�� ���´�.
	 */
  protected StyleContext getPreferences() {
    return styles;
  }

	/**
	 *  Context�� �����Ѵ�.
	 */

  protected void setPreferences(StyleContext newStyles) {
    if(this.styles != null) this.styles = null;
    this.styles = newStyles;
  }

  /**
   *  Document �� ����Ǿ��� �� ����Ǿ����� �ٸ� ��⿡�� �˸��� ���� ���
   */
  public final void saved() {
  	this.isModified = false;
  	fireChangedUpdate(new AbstractDocument.DefaultDocumentEvent(0, 0, DocumentEvent.EventType.CHANGE));
  }

  /**
   *  Overwrite ��带 �����ϵ��� ����
   */   
  public final void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
		if (insertMode) {
			// Insert mod
			super.insertString(offset, str, a);
    } else {
    	// Overwrite mode
    	
    	// ���� ��ġ�� ������ ���̸�, �׳� ���ڸ� �߰��Ѵ�.
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

