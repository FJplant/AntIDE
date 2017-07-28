/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/designer/codeeditor/JavaEditorKit.java,v 1.15 1999/08/24 07:47:58 remember Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.15 $
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
import java.awt.datatransfer.*;

import com.antsoft.ant.pool.sourcepool.*;
import com.antsoft.ant.util.*;

/**
 * This kit supports a fairly minimal handling of
 * editing java text content.  It supports syntax
 * highlighting and produces the lexical structure
 * of the document as best it can.
 *
 * @author  Timothy Prinzing
 * @version 1.2 05/04/98
 */
public class JavaEditorKit extends DefaultEditorKit {

  public static final String selectionPageUpAction = "selection-page-up";
  public static final String selectionPageDownAction = "selection-page-down";
  public static final String antUndo = "ant-undo";
  public static final String antRedo = "ant-redo";

  public static final String oneLineDelete = "one-line-delete";
  public static final String ctrlPageUpAction = "ctrl-pageup";
  public static final String ctrlPageDownAction = "ctrl-pagedown";
  public static final String ctrlUpAction = "ctrl-up";
  public static final String ctrlDownAction = "ctrl-down";

  public static final Action antPgDownAction = new AntPageDownAction(DefaultEditorKit.pageDownAction, false);
  public static final Action antPgUpAction = new AntPageUpAction(DefaultEditorKit.pageUpAction, false);
  public static final Action antSelectPgDownAction = new AntPageDownAction(JavaEditorKit.selectionPageDownAction, true);
  public static final Action antSelectPgUpAction = new AntPageUpAction(JavaEditorKit.selectionPageUpAction, true);
  public static final Action antUpAction = new AntNextVisualPositionAction(DefaultEditorKit.upAction, false, SwingConstants.NORTH);
  public static final Action antDownAction = new AntNextVisualPositionAction(DefaultEditorKit.downAction, false, SwingConstants.SOUTH);
  public static final Action antUndoAction = new AntUndoRedoAction(JavaEditorKit.antUndo, true);
  public static final Action antRedoAction = new AntUndoRedoAction(JavaEditorKit.antRedo, false);
  public static final Action oneLineDeleteAction = new OneLineDeleteAction(JavaEditorKit.oneLineDelete);
  public static final Action antCtrlPageUpAction = new AntCtrlPageUpAction(JavaEditorKit.ctrlPageUpAction);
  public static final Action antCtrlPageDownAction = new AntCtrlPageDownAction(JavaEditorKit.ctrlPageDownAction);
  public static final Action antCtrlUpAction = new AntCtrlUpAction(JavaEditorKit.ctrlUpAction);
  public static final Action antCtrlDownAction = new AntCtrlDownAction(JavaEditorKit.ctrlDownAction);
  public static final Action antPasteAction = new AntPasteAction(DefaultEditorKit.pasteAction);

	public JavaEditorKit() {
		super();
    makeDefaultActionHash();
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

  public static Action getActionForKeymap(String key){
    return (Action)defaultActions.get(key);
  }

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
    map.addActionForKeyStroke(shiftPgDown, antSelectPgDownAction);
    map.addActionForKeyStroke(shiftPgUp, antSelectPgUpAction);
    map.addActionForKeyStroke(shiftInsert, (Action)defaultActions.get(DefaultEditorKit.pasteAction));
    map.addActionForKeyStroke(shiftDelete, (Action)defaultActions.get(DefaultEditorKit.cutAction));
    map.addActionForKeyStroke(ctrlInsert, (Action)defaultActions.get(DefaultEditorKit.copyAction));
    map.addActionForKeyStroke(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN, 0), antPgDownAction );
    map.addActionForKeyStroke(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP, 0), antPgUpAction);
    map.addActionForKeyStroke(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), antDownAction);
    map.addActionForKeyStroke(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), antUpAction);
    map.addActionForKeyStroke(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_MASK, false), oneLineDeleteAction);
    map.addActionForKeyStroke(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP, InputEvent.CTRL_MASK, false), antCtrlPageUpAction);
    map.addActionForKeyStroke(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN, InputEvent.CTRL_MASK, false), antCtrlPageDownAction);
    map.addActionForKeyStroke(KeyStroke.getKeyStroke(KeyEvent.VK_UP, InputEvent.CTRL_MASK, false), antCtrlUpAction);
    map.addActionForKeyStroke(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, InputEvent.CTRL_MASK, false), antCtrlDownAction);                

    editor.setKeymap(map);
  }

  //////////////////////////// actionHash //////////////////////////////

  public static Hashtable defaultActions = new Hashtable();
  public void  makeDefaultActionHash(){
    Action [] actions = getActions();
    for(int i=0; i<actions.length; i++){
      String name = (String)actions[i].getValue(Action.NAME);

      //make action pretty
      if(name.equals(DefaultEditorKit.copyAction)){
        actions[i].putValue(Action.SMALL_ICON, ImageList.copy);
        actions[i].putValue(Action.NAME, "Copy");
      }
      else if(name.equals(DefaultEditorKit.cutAction)){
        actions[i].putValue(Action.SMALL_ICON, ImageList.cut);
        actions[i].putValue(Action.NAME, "Cut");
      }
      else if(name.equals(DefaultEditorKit.pasteAction)){
        actions[i] = antPasteAction;
        actions[i].putValue(Action.SMALL_ICON, ImageList.paste);
        actions[i].putValue(Action.NAME, "Paste");
      }

      defaultActions.put(name, actions[i]);
    }

    //ant undo , redo action setting
    antUndoAction.putValue(Action.SMALL_ICON, ImageList.undo);
    antUndoAction.putValue(Action.NAME, "Undo");
    defaultActions.put(JavaEditorKit.antUndo, antUndoAction);

    antRedoAction.putValue(Action.SMALL_ICON, ImageList.redo);
    antRedoAction.putValue(Action.NAME, "Redo");
    defaultActions.put(JavaEditorKit.antRedo, antRedoAction);        
  }

	JavaContext preferences;

  static class AntUndoRedoAction extends TextAction {
    private boolean isUndo;
    public AntUndoRedoAction(String nm, boolean isUndo){
      super(nm);
      this.isUndo = isUndo;
    }
      
    public void actionPerformed(ActionEvent e) {
      JTextComponent target = getTextComponent(e);
      if (target != null && target instanceof TextViewPanel.AntEditorPane) {
        TextViewPanel.AntEditorPane editor = (TextViewPanel.AntEditorPane)target;
        SourceEntry se = editor.getSourceEntry();
        if(se != null) {
          if(isUndo) se.undo();
          else se.redo();
        }  
      }
    }
  }      
  
  static class AntNextVisualPositionAction extends TextAction {

    AntNextVisualPositionAction(String nm, boolean select, int direction) {
      super(nm);
      this.select = select;
      this.direction = direction;
    }

    /** The operation to perform when this action is triggered. */


    public void actionPerformed(ActionEvent e) {
      JTextComponent target = getTextComponent(e);
      if (target != null) {
            Caret caret = target.getCaret();
            MyCaret bidiCaret = (caret instanceof MyCaret) ?
                                    (MyCaret)caret : null;
            int dot = caret.getDot();
            Position.Bias[] bias = new Position.Bias[1];

            try {
                Point p = null;
                if(caret != null && (direction == SwingConstants.NORTH || direction == SwingConstants.SOUTH)) {
                    p = caret.getMagicCaretPosition();

                    if (p == null) {
                        Rectangle r = (bidiCaret != null) ? target.getUI().modelToView(target, dot, Position.Bias.Forward) :
                        target.modelToView(dot);
                        caret.setMagicCaretPosition(new Point(r.x, r.y));
                    }
                }

                if(direction == SwingConstants.NORTH && p != null){
                   dot = Utilities.getPositionAbove(target, dot, p.x);
                }

                else if(direction == SwingConstants.SOUTH && p != null){
                   dot = Utilities.getPositionBelow(target, dot, p.x);
                }

                else{
                  dot = target.getUI().getNextVisualPositionFrom(target, dot,
                      (bidiCaret != null) ? Position.Bias.Forward :  Position.Bias.Forward, direction, bias);
                }


                if(bias[0] == null) {
                  bias[0] = Position.Bias.Forward;
                }
                if(bidiCaret != null) {
                  if (select) {
                      bidiCaret.moveDot(dot);
                  } else {
                      bidiCaret.setDot(dot);
                  }
                }
                else {
                  if (select) {
                    caret.moveDot(dot);
                  } else {
                    caret.setDot(dot);
                  }
                }

                if(direction == SwingConstants.EAST || direction == SwingConstants.WEST) {
                  target.getCaret().setMagicCaretPosition(null);
                }
              } catch (BadLocationException ex) {
              }
          }
      }

      private boolean select;
      private int direction;
  }

  static class AntPageDownAction extends TextAction {
    private boolean select;
    /* Create this object with the appropriate identifier. */
    AntPageDownAction(String nm, boolean select) {
        super(nm);
        this.select = select;
    }

   	/** The operation to perform when this action is triggered. */
    int prevY = 0;
    public void actionPerformed(ActionEvent e) {

        JTextComponent target = getTextComponent(e);
        if (target != null) {
            int scrollOffset;
            int selectedIndex;
            Rectangle visible = new Rectangle();
            Rectangle r;
            target.computeVisibleRect(visible);
            scrollOffset = visible.y;
            visible.y += visible.height;

            if((visible.y+visible.height) > target.getHeight()){
                visible.y = (target.getHeight() - visible.height);
            }

            scrollOffset = visible.y - scrollOffset;

            target.scrollRectToVisible(visible);
            selectedIndex = target.getCaretPosition();

            try {
                if(selectedIndex != -1) {
                    r = target.modelToView(selectedIndex);
                    int line = (int)(scrollOffset / r.height);

                    r.y += line * r.height;
                    if( ( (visible.y+visible.height) != target.getHeight() ) && scrollOffset != 0)  r.y -= r.height;

                    selectedIndex = target.viewToModel(new Point(r.x,r.y));
                    Document doc = target.getDocument();

                    if(scrollOffset == 0) selectedIndex = doc.getLength();
                    if ((selectedIndex != 0) &&
                        (selectedIndex  > (doc.getLength()-1))) {
                        selectedIndex = doc.getLength()-1;
                    }
                    if (selectedIndex  < 0) {
                        selectedIndex = 0;
                    }
                    if (select)
                        target.moveCaretPosition(selectedIndex);
                    else
                        target.setCaretPosition(selectedIndex);
                }
            } catch(BadLocationException bl) {
                target.getToolkit().beep();
            }
        }
    }
  }

  static class AntPageUpAction extends TextAction {

    private boolean select;

    /** Create this object with the appropriate identifier. */
    public AntPageUpAction(String nm, boolean select) {
        super(nm);
        this.select = select;
    }

  	/** The operation to perform when this action is triggered. */
    public void actionPerformed(ActionEvent e) {
	    JTextComponent target = getTextComponent(e);
	    if (target != null) {
        int scrollOffset;
        int selectedIndex;
        Rectangle visible = new Rectangle();
        Rectangle r;
        target.computeVisibleRect(visible);
        scrollOffset = visible.y;
        visible.y -= visible.height;
        if(visible.y < 0)
            visible.y = 0;
        scrollOffset = scrollOffset - visible.y;
        target.scrollRectToVisible(visible);

        selectedIndex = target.getCaretPosition();
        try {
            if(selectedIndex != -1) {
              r = target.modelToView(selectedIndex);

              int line = (int)(scrollOffset / r.height);

              r.y -= line * r.height;
              if( visible.y != 0 && scrollOffset != 0)  r.y += r.height;

              selectedIndex = target.viewToModel(new Point(r.x,r.y));
              Document doc = target.getDocument();

              if(scrollOffset == 0) selectedIndex = -1;

              if ((selectedIndex != 0) &&
                  (selectedIndex  > (doc.getLength()-1))) {
                  selectedIndex = doc.getLength()-1;
              }
              if(selectedIndex  < 0) {
                  selectedIndex = 0;
              }
              if (select)
                  target.moveCaretPosition(selectedIndex);
              else
                  target.setCaretPosition(selectedIndex);
            }
        } catch(BadLocationException bl) {
            target.getToolkit().beep();
        }
	    }
   	}
  }

  public static class OneLineDeleteAction extends TextAction {

    /** Create this object with the appropriate identifier. */
    public OneLineDeleteAction(String nm) {
      super(nm);
    }

    /**
     * The operation to perform when this action is triggered.
     *
     * @param e the action event
     */
    public void actionPerformed(ActionEvent e) {
      JTextComponent target = getTextComponent(e);
      if (target != null) {
        Element root = target.getDocument().getDefaultRootElement();
        Element elem = root.getElement(root.getElementIndex(target.getCaretPosition()));
        int start = elem.getStartOffset();
        int end = elem.getEndOffset();
        try{
          target.getDocument().remove(start, end-start);
        }catch(BadLocationException e2){}
      }
    }
  }

  static class AntCtrlPageUpAction extends TextAction {

    /** Create this object with the appropriate identifier. */
    public AntCtrlPageUpAction(String nm) {
        super(nm);
    }

  	/** The operation to perform when this action is triggered. */
    public void actionPerformed(ActionEvent e) {
	    JTextComponent target = getTextComponent(e);
	    if (target != null) {
        int scrollOffset;
        int selectedIndex;
        Rectangle visible = new Rectangle();
        Rectangle r;
        target.computeVisibleRect(visible);

        int caretOffset = -1;
        try{
          Rectangle caretR = target.modelToView( target.getCaretPosition() );
          caretOffset = visible.height - ( visible.y + visible.height - ( caretR.y + caretR.height ) );
        }catch(BadLocationException e2){}
        scrollOffset = visible.y;
        visible.y -= caretOffset;

        if(visible.y < 0) visible.y = 0;
        scrollOffset = scrollOffset - visible.y;

        selectedIndex = target.getCaretPosition();
        try {
            if(selectedIndex != -1) {
              r = target.modelToView(selectedIndex);

              int line = (int)(scrollOffset / r.height);

              r.y -= line * r.height;
              if( visible.y != 0 && scrollOffset != 0)  r.y += r.height;

              selectedIndex = target.viewToModel(new Point(r.x,r.y));
              Document doc = target.getDocument();

              if(scrollOffset == 0) selectedIndex = -1;

              if ((selectedIndex != 0) &&
                  (selectedIndex  > (doc.getLength()-1))) {
                  selectedIndex = doc.getLength()-1;
              }
              if(selectedIndex  < 0) {
                  selectedIndex = 0;
              }
              target.setCaretPosition(selectedIndex);
            }
        } catch(BadLocationException bl) {
            target.getToolkit().beep();
        }
	    }
   	}
  }

  static class AntCtrlPageDownAction extends TextAction {

    /* Create this object with the appropriate identifier. */
    AntCtrlPageDownAction(String nm) {
        super(nm);
    }

   	/** The operation to perform when this action is triggered. */
    int prevY = 0;
    public void actionPerformed(ActionEvent e) {

        JTextComponent target = getTextComponent(e);
        if (target != null) {
            int scrollOffset;
            int selectedIndex;
            Rectangle visible = new Rectangle();
            Rectangle r;
            target.computeVisibleRect(visible);
            scrollOffset = visible.y;

            int caretOffset = -1;
            try{
              Rectangle caretR = target.modelToView( target.getCaretPosition() );
              caretOffset = visible.y + visible.height - caretR.y;
            }catch(BadLocationException e2){}
            scrollOffset = visible.y;
            visible.y += caretOffset;

            if((visible.y+visible.height) > target.getHeight()){
                visible.y = (target.getHeight() - visible.height);
            }

            scrollOffset = visible.y - scrollOffset;
            selectedIndex = target.getCaretPosition();

            try {
                if(selectedIndex != -1) {
                    r = target.modelToView(selectedIndex);
                    int line = (int)(scrollOffset / r.height);

                    r.y += line * r.height;
                    if( ( (visible.y+visible.height) != target.getHeight() ) && scrollOffset != 0)  r.y -= r.height;

                    selectedIndex = target.viewToModel(new Point(r.x,r.y));
                    Document doc = target.getDocument();

                    if(scrollOffset == 0) selectedIndex = doc.getLength();
                    if ((selectedIndex != 0) &&
                        (selectedIndex  > (doc.getLength()-1))) {
                        selectedIndex = doc.getLength()-1;
                    }
                    if (selectedIndex  < 0) {
                        selectedIndex = 0;
                    }
                    target.setCaretPosition(selectedIndex);
                }
            } catch(BadLocationException bl) {
                target.getToolkit().beep();
            }
        }
    }
  }

  static class AntCtrlUpAction extends TextAction {

    /** Create this object with the appropriate identifier. */
    public AntCtrlUpAction(String nm){
        super(nm);
    }

  	/** The operation to perform when this action is triggered. */
    public void actionPerformed(ActionEvent e) {
	    JTextComponent target = getTextComponent(e);
	    if (target != null) {

        Rectangle visible = new Rectangle();
        target.computeVisibleRect(visible);

        int selectedIndex = target.getCaretPosition();
        try{
          Rectangle r = target.modelToView(selectedIndex);
          visible.y -= r.height;
          if(visible.y < 0)
              visible.y = 0;

          AntViewport.mode = AntViewport.CTRL_UP_DOWN_MODE;
          target.scrollRectToVisible(visible);
          AntViewport.mode = AntViewport.GENERAL_MODE;

        }catch(BadLocationException e2){}

        try{
          Rectangle cr = target.modelToView( target.getCaretPosition() );
          if(cr.y + cr.height > visible.y + visible.height) antUpAction.actionPerformed(e);
        }catch(BadLocationException e2){}
	    }
   	}
  }


  static class AntCtrlDownAction extends TextAction {

    /** Create this object with the appropriate identifier. */
    public AntCtrlDownAction(String nm){
        super(nm);
    }

  	/** The operation to perform when this action is triggered. */
    public void actionPerformed(ActionEvent e) {
	    JTextComponent target = getTextComponent(e);
	    if (target != null) {

        Rectangle visible = new Rectangle();
        target.computeVisibleRect(visible);

        int selectedIndex = target.getCaretPosition();
        try{
          Rectangle r = target.modelToView(selectedIndex);
          visible.y += r.height;
          if((visible.y+visible.height) > target.getHeight()){
              visible.y = (target.getHeight() - visible.height);
          }

          AntViewport.mode = AntViewport.CTRL_UP_DOWN_MODE;
          target.scrollRectToVisible(visible);
          AntViewport.mode = AntViewport.GENERAL_MODE;


        }catch(BadLocationException e2){}

        try{
          Rectangle cr = target.modelToView( target.getCaretPosition() );
          if(cr.y < visible.y) antDownAction.actionPerformed(e);
        }catch(BadLocationException e2){}
	    }
   	}
  }

  public static class AntPasteAction extends TextAction {

    /** Create this object with the appropriate identifier. */
    public AntPasteAction(String nm) {
        super(nm);
    }

    /**
     * The operation to perform when this action is triggered.
     *
     * @param e the action event
     */
    public void actionPerformed(ActionEvent e) {
      JTextComponent target = getTextComponent(e);

      if (target != null) {
        Rectangle visible = new Rectangle();
        target.computeVisibleRect(visible);
        target.paste();
        target.computeVisibleRect(visible);

        int selectedIndex = target.getCaretPosition();
        try{
          Rectangle r = target.modelToView(selectedIndex);

          if(r.y + r.height > visible.y + visible.height){
            int gap = (r.y + r.height) - (visible.y + visible.height);
            visible.y += gap;

            AntViewport.mode = AntViewport.BLOCK_PASTE_MODE;
            target.scrollRectToVisible(visible);
            AntViewport.mode = AntViewport.GENERAL_MODE;
          }
        }catch(BadLocationException e2){}
      }
    }
  }
}
