/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/designer/codeeditor/ParamPopup.java,v 1.15 1999/08/31 12:26:14 multipia Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.15 $
 */
package com.antsoft.ant.designer.codeeditor;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.util.*;
import javax.swing.text.*;
import javax.swing.event.*;

import com.antsoft.ant.pool.sourcepool.AntDocument;
import com.antsoft.ant.util.BorderList;
import com.antsoft.ant.codecontext.CodeContext;
import com.antsoft.ant.util.MyCaret;
import com.antsoft.ant.util.AntLineBorder;
import com.antsoft.ant.main.MainFrame;

/**
 *  class CodePopup
 *
 *  @author Kim sang kyun
 */
class ParamPopup extends JWindow {

  private JList list;
  private JEditorPane editor;
  private TextViewPanel tvp;

  private ComponentHandler ch;
  private WindowHandler wh;
  private MouseHandler mh;
  private KeyHandler kh;
  private DocumentHandler dh;

  private Frame owner;
  private AntDocument doc;
  private MyCellRenderer r;
  private int originalOpenOffset;
  private JScrollPane scroller;

  public ParamPopup(Frame owner, TextViewPanel tvp, JScrollPane scroller){
    super(owner);
    this.owner = owner;
    this.tvp = tvp;
    this.scroller = scroller;

    ch = new ComponentHandler();
    wh = new WindowHandler();
    mh = new MouseHandler();
    kh = new KeyHandler();
    dh = new DocumentHandler();

    list = new JList();
    r = new MyCellRenderer();
    list.setBorder(LineBorder.createBlackLineBorder());
    list.setCellRenderer(r);


    JPanel p = new JPanel(new BorderLayout());
    p.add(list);

    scroller.getVerticalScrollBar().addMouseListener(mh);
    scroller.getHorizontalScrollBar().addMouseListener(mh);

    getContentPane().add(p);
    pack();
  }

  public void destoryWindow(){
    removeHandlers();

    r.setBoldCol(0);
    list.repaint();
    dispose();
    setNull();
  }

  /**
   * 새로운걸 보여야 하기 때문에 잠시 보이지 않게 한다
   */
  public void hideWindow(){
    if(isVisible()) {
      removeHandlers();
      editor = null;
      setVisible(false);
    }
  }

  /**
   * 다시 보이게 한다
   */
  public void reShowWindow(JEditorPane newEditor){
    editor = null;
    editor = newEditor;
    editor.addKeyListener(kh);
    editor.getDocument().addDocumentListener(dh);
    owner.addComponentListener(ch);
    tvp.getViewPort().addComponentListener(ch);
    owner.addWindowListener(wh);

    boldUpdate();

    setVisible(true);
  }

  public void showWindow(JEditorPane editor){
    this.editor = editor;
    editor.addKeyListener(kh);
    editor.addMouseListener(mh);
    editor.getDocument().addDocumentListener(dh);
    tvp.getViewPort().addComponentListener(ch);
    originalOpenOffset = CodeContext.getOpenOffset();
    owner.addComponentListener(ch);
    owner.addWindowListener(wh);

    Point p = getPosition();
    setLocation(p) ;
		setVisible(true);
  }

  public void setParamData(Vector data, AntDocument doc){
    this.doc = doc;
    list.setListData(data);
    this.setSize(list.getPreferredSize().width+30, list.getPreferredSize().height);
  }

  private void removeHandlers(){
  	try {
      scroller.getVerticalScrollBar().removeMouseListener(mh);
      scroller.getHorizontalScrollBar().removeMouseListener(mh);
      editor.removeMouseListener(mh);
      editor.removeKeyListener(kh);
      editor.getDocument().removeDocumentListener(dh);
      tvp.getViewPort().removeComponentListener(ch);
      owner.removeComponentListener(ch);
      owner.removeWindowListener(wh);
    } catch (NullPointerException e) {
    }
  }

  private void setNull(){
    setRootPane(null);

    editor = null;
    tvp = null;
    owner = null;
    doc = null;
    scroller = null;
  }

  ////////////////// private methods ///////////////////////////////////
  private void exitAction(){
    removeHandlers();
    r.setBoldCol(0);
    list.repaint();
    dispose();
    tvp.paramPopupDestoryed();
    setNull();
  }

  int openOffset=0;
  int lineEndOffset=0;
  int caretPosition=0;

  private void boldUpdate(){
    openOffset = originalOpenOffset;

    try{
      Element elem = doc.getParagraphElement(openOffset);
      lineEndOffset = elem.getEndOffset();
      String paramText = doc.getText(openOffset+1, lineEndOffset-openOffset+2).trim();

      /*
      if(paramText.indexOf(")") != -1)
        paramText = paramText.substring(0, paramText.indexOf(")")+1);
      */

      int lastQutationPos = paramText.lastIndexOf("\"");
      int currPosition=0;
      int boldCol = 0;

      boolean isInternalBrace = false;
      if(paramText != null)
      for(int i=0; i<paramText.length(); i++)
      {
        currPosition = openOffset+1+i;
        char c = paramText.charAt(i);
        if(c == '(') {
          isInternalBrace = true;
          continue;
        }

        else if(c == ')') {
          if(isInternalBrace) {
            isInternalBrace = false;
            continue;
          }
        }

        if(!isInternalBrace && (c == ',') && (caretPosition >= currPosition)){
          ++boldCol;
        }
        r.setBoldCol(boldCol);
        list.repaint();
      }
    }catch(BadLocationException e){
    }catch(NullPointerException e){}
  }

  public Point getPosition(){
    Point p = null;

    MyCaret c = (MyCaret)editor.getCaret();
    Rectangle r = c.getCurrPosition();
    if(r == null) return null;

    p = new Point(r.x + r.width, r.y - getSize().height);

    SwingUtilities.convertPointToScreen(p, editor);
    //screen에 맞게 위치를 바꾼다
    int scrH = Toolkit.getDefaultToolkit().getScreenSize().height;
    if(p.y - getSize().height < 0) p.y = p.y + r.height + getSize().height;
    return p;
  }


  /**
   * 닫는 괄호를 체크한다
   *
   * @param offset insert 일때 offset, 또는 VK_RIGHT일때 caret offset
   */
  private void closeCheck(int offset){

    try{
    	Element elem=doc.getParagraphElement(offset);
      int startOffset=elem.getStartOffset();
      int endOffset=elem.getEndOffset();
     	int length=endOffset-startOffset;
      String str=doc.getText(startOffset,length);
      int localOffset=offset-startOffset;
      char c=str.charAt(localOffset);

      if(c == ')')
      {
        int prev_start=originalOpenOffset+1;
        int prev_end = offset;
        int prev_length= prev_end-prev_start;
        String prev_str = doc.getText(prev_start, prev_length);

        boolean even = true;
        for(int i=0; i<prev_str.length(); i++){
          if(prev_str.charAt(i) == '"') even = !even;
        }
        if(even) exitAction();
      }
    }catch(BadLocationException e){}
  }

  class KeyHandler extends KeyAdapter{
    public void keyPressed(KeyEvent evt) {

      int code=evt.getKeyCode();
      switch(code){

        case KeyEvent.VK_LEFT :
          --caretPosition;
          if(caretPosition<originalOpenOffset) {
            exitAction();
            return;
          }
          boldUpdate();
          break;

        case KeyEvent.VK_RIGHT :
          ++caretPosition;
          closeCheck(caretPosition);
          boldUpdate();
          break;

        case KeyEvent.VK_ESCAPE :
        case KeyEvent.VK_PAGE_UP :
        case KeyEvent.VK_PAGE_DOWN :
          exitAction();
          break;

        default :
          break;
      }
    }
  }

  class DocumentHandler implements DocumentListener {
    public void insertUpdate(DocumentEvent event) {
      caretPosition = editor.getCaretPosition();
      boldUpdate();
      closeCheck(event.getOffset());
    }

    public void removeUpdate(DocumentEvent event) {
      caretPosition = editor.getCaretPosition();
      boldUpdate();
      if(event.getOffset() ==  originalOpenOffset) exitAction();
    }

    public void changedUpdate(DocumentEvent event) {
      caretPosition = editor.getCaretPosition();
      boldUpdate();
    }
  }

  class MyCellRenderer extends JPanel implements ListCellRenderer {
     private Font bold = new Font("DialogInput", Font.BOLD, 12);
     private Font plain = new Font("DialogInput", Font.PLAIN, 12);
     private final int MAX = 10;
     private JLabel [] labels = new JLabel[MAX];
     private int boldCol = 0;
     private Color backColor = new Color(255, 255, 200);

     public MyCellRenderer() {
         setOpaque(true);
         this.setLayout(new FlowLayout(FlowLayout.LEFT,0,1));
         setBorder(new AntLineBorder(Color.black, 1, false, true, false, true));
         this.setBackground(backColor);

         for(int i=0; i<labels.length; i++)
         {
           labels[i] = new JLabel();
           labels[i].setBackground(backColor);
           labels[i].setForeground(Color.black);
           labels[i].setOpaque(true);
         }

         add(new JLabel(" "));
     }

     public void setBoldCol(int col){
       boldCol = col;
     }

     public Component getListCellRendererComponent(
         JList list,
         Object value,
         int index,
         boolean isSelected,
         boolean cellHasFocus)
     {

         StringTokenizer st = new StringTokenizer(value.toString(), ",", false);
         int tokenNum = st.countTokens();

         for(int i=0; i<tokenNum; i++){
           String token = st.nextToken().trim();
           String text = (i==tokenNum-1) ? token : token+", ";

           labels[i].setText(text);
           if(i==boldCol) labels[i].setFont(bold);
           else labels[i].setFont(plain);
         }

         removeAll();
         for(int i=0; i<tokenNum; i++) add(labels[i]);
         return this;
     }

     public void finalize() {
       bold = null;
       plain = null;
       backColor = null;
       for(int i=0;i<labels.length; i++) labels[i] = null;
     }
  }

  /** component event handler */
  class ComponentHandler extends ComponentAdapter{

    public void componentResized(ComponentEvent e){
      exitAction();
    }
    public void componentMoved(ComponentEvent e){
      exitAction();
    }

    public void componentHidden(ComponentEvent e) {
      exitAction();
    }
  }

  class WindowHandler extends WindowAdapter{
    public void windowIconified(WindowEvent e){
      exitAction();
    }
  }

  class MouseHandler extends MouseAdapter{
    public void mousePressed(MouseEvent e){
     if(ParamPopup.this.isVisible())  exitAction();
    }
  }
}

