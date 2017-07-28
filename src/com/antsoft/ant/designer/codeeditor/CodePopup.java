/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/designer/codeeditor/CodePopup.java,v 1.21 1999/08/31 12:26:14 multipia Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.21 $
 */
package com.antsoft.ant.designer.codeeditor;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
import javax.swing.text.*;
import java.lang.reflect.Method;

import com.antsoft.ant.pool.sourcepool.SourceEntry;
import com.antsoft.ant.pool.sourcepool.AntDocument;
import com.antsoft.ant.codecontext.CodeContext;
import com.antsoft.ant.main.MainFrame;
import com.antsoft.ant.codecontext.codeeditor.InsightEvent;
import com.antsoft.ant.util.MyCaret;
import com.antsoft.ant.pool.classpool.ClassPool;
import com.antsoft.ant.util.ImageList;

/**
 *  class CodePopup
 *
 *  @author Kim sang kyun
 */
class CodePopup extends JWindow implements KeyListener,DocumentListener{
  private JList list;
  private static JEditorPane editor;
  private TextViewPanel tvp = null;
  private DefaultListModel trimmedM = null;
  private int recentMatchIndex = -1;
  private AntDocument doc;
  private MyCellRenderer r;
  private JScrollPane scroll;
  private ComponentHandler ch;
  private WindowHandler wh;
  private MouseHandler mh;
  private Frame owner;
  private JScrollPane scroller;
  private SourceEntry se;

  private int currMode;
  public static final int INTELLI_MODE = 1;
  public static final int IMPORT_MODE = 2;
  public static final int NEWMODE = 3;
  public static final int IMPLEMENTS_MODE = 4;

  private boolean isIntelliOK = false;
  private Object textViewPanel;

  /**
   * constructor
   */
  public CodePopup(Frame owner, TextViewPanel tvp, JScrollPane scroller){
    super(owner);
    this.owner = owner;
    this.tvp = tvp;
    this.scroller = scroller;

    InsightEvent b;

    ch = new ComponentHandler();
    wh = new WindowHandler();
    mh = new MouseHandler();

    list = new JList();
    r = new MyCellRenderer();
    list.setCellRenderer(r);

    list.addKeyListener(new KeyAdapter(){
      public void keyPressed(KeyEvent evt) {
        int code=evt.getKeyCode();
        if(code == KeyEvent.VK_ENTER || code == KeyEvent.VK_SPACE) enterAction();
        else if(code == KeyEvent.VK_ESCAPE) exitAction();
      }});

    list.addMouseListener(new MouseAdapter(){
      public void mouseClicked(MouseEvent e){
        r.makeSelect();
        recentMatchIndex = list.getSelectedIndex();

        if(e.getClickCount() == 2) enterAction();
      }});

    scroller.getVerticalScrollBar().addMouseListener(new MouseAdapter(){
      public void mousePressed(MouseEvent e){
        if(CodePopup.this.isVisible()) exitAction();
      }
    });

    scroller.getHorizontalScrollBar().addMouseListener(new MouseAdapter(){
      public void mousePressed(MouseEvent e){
        if(CodePopup.this.isVisible()) exitAction();
      }
    });

    scroll = new JScrollPane(list);
    getContentPane().add(scroll);
    pack();
  }

  private boolean inScreen(Point p){
    int width = Toolkit.getDefaultToolkit().getScreenSize().width;
    int height = Toolkit.getDefaultToolkit().getScreenSize().height;

    if(p.x >= 0 && p.x < width && p.y >=0 && p.y <= height &&
        p.x > MainFrame.mainFrame.getLocation().x && p.y > MainFrame.mainFrame.getLocation().y) return true;

    else return false;
  }

  private static int dotOffset=0;

  public Point getPosition(){
    Point p = null;
    Rectangle r = MyCaret.getCurrPosition();
    if(r == null) return null;

    p = new Point(r.x + r.width, r.y + r.height);
    SwingUtilities.convertPointToScreen(p, editor);

    //screen에 맞게 위치를 바꾼다
    int scrH = Toolkit.getDefaultToolkit().getScreenSize().height;
    if(p.y + getSize().height > scrH) p.y = p.y - getSize().height - r.height;

    return p;
  }

  public void hideWindow(){
    if(isVisible()) exitAction();
  }

  /**
   * 화면에 나타낸다
   */

  Point leftTopP = new Point(0, 0);
  public void showWindow(JEditorPane editor){
    this.editor = editor;
    editor.addKeyListener(this);
    editor.addMouseListener(mh);
    editor.getDocument().addDocumentListener(this);
    tvp.getViewPort().addComponentListener(ch);
    owner.addComponentListener(ch);
    owner.addWindowListener(wh);


    Point p = getPosition();
    setLocation(p);
    scroll.getViewport().setViewPosition(leftTopP);
		setVisible(true);
    dotOffset = CodeContext.getDotOffset();
  }


  private void setData(Vector data, AntDocument doc){
    this.doc = doc;
    list.setListData(data);

    int maxWidth = 0;
    if(data != null)
    for(int i=0; i<data.size(); i++){
      String str = data.elementAt(i).toString();
      maxWidth = (str.length() > maxWidth) ? str.length() : maxWidth;
    }

    int charWidth = Toolkit.getDefaultToolkit().getFontMetrics(r.getFont()).charWidth('m');
    setSize(9*charWidth + maxWidth*charWidth , getSize().height);
  }

  public void setImplementsData(Vector data, AntDocument doc, SourceEntry se) {
    currMode =IMPLEMENTS_MODE;
    setData(data, doc);
    this.se = se;
  }

  public void setImportingData(Vector data, AntDocument doc){
    this.currMode = IMPORT_MODE;
    setData(data, doc);
  }

  public void setNewingData(Vector data, AntDocument doc){
    this.currMode = NEWMODE;
    setData(data, doc);  }

  /**
   * data를 setting
   */

  public void setIntelliData(Vector data, AntDocument doc, Object textViewPanel){
    this.currMode = INTELLI_MODE;
    this.textViewPanel = textViewPanel;
    setData(data, doc);
 }

  private int lineEndOffset = 0;
  public void insertUpdate(DocumentEvent event) {

    int offset = event.getOffset();
    Element elem=doc.getParagraphElement(offset);
    lineEndOffset = elem.getEndOffset();
   	int length=offset-dotOffset;
    if(length <= 0)
    {
      editor.removeKeyListener(CodePopup.this);
      editor.getDocument().removeDocumentListener(CodePopup.this);
      CodePopup.this.setVisible(false);
      return;
    }

    String matchStr=null;
    try{

       matchStr = doc.getText(dotOffset+1,length);
       matchList(matchStr);
     }catch(BadLocationException e){}
  }

  public void removeUpdate(DocumentEvent event) {
    int offset = event.getOffset();
    Element elem=doc.getParagraphElement(offset);
    lineEndOffset = elem.getEndOffset();
   	int length=offset-dotOffset-1;

    if(length < 0)
    {
      editor.removeKeyListener(CodePopup.this);
      editor.getDocument().removeDocumentListener(CodePopup.this);
      CodePopup.this.setVisible(false);
      return;
    }

    String matchStr=null;
    try{
       matchStr = doc.getText(dotOffset+1,length);
       matchList(matchStr);
     }catch(BadLocationException e){}
  }
  public void changedUpdate(DocumentEvent event) {}

  public void keyPressed(KeyEvent evt) {
    int code=evt.getKeyCode();
    String keyText = evt.getKeyText(code);

    if(keyText.equals("."))
    {
      exitAction();
      return;
    }

    switch(code){

      //여는 괄호
      case KeyEvent.VK_9:
          if(evt.isShiftDown()){
            exitAction();
            break;
          }

      case KeyEvent.VK_UP :
          r.makeSelect();
          if(list.getSelectedIndex() <= 0) return;
          list.setSelectedValue(list.getModel().getElementAt(list.getSelectedIndex()-1), true);
          --recentMatchIndex;
          break;

      case KeyEvent.VK_DOWN :
          r.makeSelect();
          if(list.getSelectedIndex() == list.getModel().getSize()-1) return;
          list.setSelectedValue(list.getModel().getElementAt(list.getSelectedIndex()+1), true);
          ++recentMatchIndex;
          break;

      case KeyEvent.VK_HOME :
          r.makeSelect();
          list.setSelectedValue(list.getModel().getElementAt(0), true);
          recentMatchIndex = 0;
          break;

      case KeyEvent.VK_END :
          r.makeSelect();
          list.setSelectedValue(list.getModel().getElementAt(list.getModel().getSize()-1), true);
          recentMatchIndex = list.getModel().getSize() -1;
          break;

      case KeyEvent.VK_LEFT :
          if((editor.getCaretPosition() - CodeContext.getDotOffset()) <= 1 ) exitAction();
          break;

      case KeyEvent.VK_RIGHT :
          if(isVisible()) evt.consume();
          if((lineEndOffset-editor.getCaretPosition())<=1) exitAction();
          break;

      case KeyEvent.VK_PAGE_UP :
          evt.consume();
          int count = list.getVisibleRowCount();
          int diff = list.getSelectedIndex() - count;
          int selIndex = (diff < 0 ) ? 0 : diff;
          list.setSelectedValue(list.getModel().getElementAt(selIndex), true);
          recentMatchIndex = selIndex;
          break;


      case KeyEvent.VK_PAGE_DOWN :
          evt.consume();
          int count2 = list.getVisibleRowCount();
          int sum = list.getSelectedIndex() + count2;
          int selIndex2 = (sum > list.getModel().getSize()-1) ? list.getModel().getSize()-1 : sum;
          list.setSelectedValue(list.getModel().getElementAt(selIndex2), true);
          recentMatchIndex = selIndex2;
          break;

      case KeyEvent.VK_ESCAPE :
          editor.removeKeyListener(CodePopup.this);
          editor.getDocument().removeDocumentListener(CodePopup.this);
          CodePopup.this.setVisible(false);
          break;

      case KeyEvent.VK_ENTER :
      case KeyEvent.VK_SPACE :
          enterAction();
          break;

      default : break;
    }
  }

  public void keyTyped(KeyEvent e){}
  public void keyReleased(KeyEvent e){}

  ///////////////////// Private methods /////////////////////////////

  /**
   * 현재의 match 문자열을 가지고 match되는게 있는지 알아본다
   *
   * @param matchStr current match string
   */
  private void matchList(String matchStr){

    if(list.getModel() == null) return;

    if(matchStr.length() == 0){
      recentMatchIndex = -1;
      r.makeUnselect();
      scroll.repaint();
      return;
    }

    int i=0;
    for(; i<list.getModel().getSize(); i++)
    {
      String eleOfList = list.getModel().getElementAt(i).toString();

      if(eleOfList.toLowerCase().startsWith(matchStr.toLowerCase()))
      {
        list.setSelectedValue(list.getModel().getElementAt(i), true);
        break;
      }
    }

    r.makeSelect();

    if(i == list.getModel().getSize()){
       recentMatchIndex = -1;
       r.makeUnselect();
    }
    else recentMatchIndex = i;

    scroll.repaint();
  }

  /**
   * Cancel action. 각종 listener들을 해제한다
   */
  private void exitAction(){

    editor.removeKeyListener(CodePopup.this);
    editor.getDocument().removeDocumentListener(CodePopup.this);
    tvp.getViewPort().removeComponentListener(ch);
    owner.removeComponentListener(ch);
    owner.removeWindowListener(wh);
    setVisible(false);
  }

  public boolean isIntelliOK(){
    return isIntelliOK;
  }  

  /**
   * OK action
   */
  private void enterAction(){
    editor.getDocument().removeDocumentListener(CodePopup.this);
    editor.removeKeyListener(CodePopup.this);
    tvp.getViewPort().removeComponentListener(ch);
    owner.removeComponentListener(ch);
    owner.removeWindowListener(wh);
    setVisible(false);

    if(recentMatchIndex == -1) return;

    int offset = editor.getCaretPosition();
    Element elem=doc.getParagraphElement(offset);
 	  int length=elem.getEndOffset()-dotOffset-1;

    String str=null;
    try{
       str = doc.getText(dotOffset+1,length);
       //System.out.print("str = " + str + "helloworld");
       StringTokenizer st=new StringTokenizer(str,". \t\n\r({[;)");
       String toRemove = null;
       boolean insertFlag = true;
       if (st.hasMoreTokens()) toRemove = st.nextToken();
       if(toRemove != null) {
         int len = toRemove.length();
         if(str.charAt(len) == '(') {
         	 len++;
           insertFlag = false;
         }

         doc.remove(dotOffset+1, len);
       }

       InsightEvent originalEvent = (InsightEvent)list.getModel().getElementAt(list.getSelectedIndex());
       String original = originalEvent.toString();
       String toInsert = original;

       if(currMode == INTELLI_MODE){
         toInsert = original.substring(0, original.lastIndexOf(":"));
         if(originalEvent.getEventType() == InsightEvent.METHOD) toInsert += "(";
       }

       else if(currMode == NEWMODE || currMode == IMPLEMENTS_MODE) {
         if(original.indexOf(":") != -1) {
           toInsert = original.substring(0, original.lastIndexOf(":"));
         }
         else toInsert = original;
       }

       doc.insertString(dotOffset+1, toInsert);
       dotOffset += toInsert.length() + 1;

       if(currMode == INTELLI_MODE && originalEvent.getEventType() == InsightEvent.METHOD){
         TextViewPanel tvp = (TextViewPanel)textViewPanel;
         String key = toInsert.substring(0, toInsert.lastIndexOf("("));
         Vector params = MainFrame.getCodeContext().getParametersWithCachedType(key.trim());

         if(params.size() != 0){
           if(insertFlag && params.size() == 1 && params.elementAt(0).toString().indexOf(",") == -1){
             String param = ((InsightEvent)params.elementAt(0)).toString().trim();

             if(param.equals("<no parameters>")){
               doc.insertString(dotOffset, ")");
             }
             else{
               String type = param.substring(0, param.indexOf(" "));
               doc.insertString(dotOffset, type + ")");
               editor.setSelectionStart(dotOffset);
               editor.setSelectionEnd(dotOffset + type.length());
             }
           }
           else{
             tvp.showParameterizing(params);
           }
         }
       }

       else if(currMode == IMPLEMENTS_MODE){
         String fullInterfaceName = "";
         String packageName = null;
         String className = "";

         if(original.indexOf(":") != -1) {
           className = original.substring(0, original.lastIndexOf(":"));
           packageName = original.substring(original.lastIndexOf(":")+1);
           fullInterfaceName = packageName + "." + className;
         }
         else{
           fullInterfaceName = toInsert;
         }
         String source = getImplementsSource(fullInterfaceName);

         int line = se.getLineFromOffset(dotOffset) + 1;

         while(true){
           Element ele = se.getElementAt( line );
           Segment se = new Segment();
           doc.getText(ele.getStartOffset(), 10, se );
           if(se.toString().trim().equals("") || se.toString().trim().startsWith("{")){
             line++;
           }
           else {
             doc.insertString(ele.getStartOffset(), source);
             break;
           }
         }
       }
    }
    catch(BadLocationException e){}
    catch(Exception e2){}
  }

  class MyCellRenderer extends JPanel implements ListCellRenderer {
     public Color darkBlue = new Color(0, 0, 31);
     public Color sfc = Color.yellow;
     public Color sbc = darkBlue;
     public Color dfc = Color.black;
     private Color dbc = Color.white;

     private JLabel left, right;
     private StringTokenizer st;
     Font f = new Font("DialogInput", Font.PLAIN, 12);
     Font boldF = new Font("DialogInput", Font.BOLD, 12);

     public Font getFont(){
       return f;
     }

     public MyCellRenderer() {
       this.setLayout(new BorderLayout());
       left = new JLabel();
       right = new JLabel();

       left.setOpaque(true);
       right.setOpaque(true);

       setOpaque(true);
       this.setBackground(Color.white);

       left.setFont(f);
       right.setFont(f);

       add(left, BorderLayout.WEST);
       add(right, BorderLayout.EAST);
     }

     public void makeUnselect(){
       sfc = Color.black;
       sbc = Color.white;
       dfc = Color.black;
       dbc = Color.white;
     }

     public void makeSelect(){
       sfc = Color.yellow;
       sbc = darkBlue;
       dfc = Color.black;
       dbc = Color.white;
     }

     public Component getListCellRendererComponent(
         JList list,
         Object value,
         int index,
         boolean isSelected,
         boolean cellHasFocus)
     {

         InsightEvent ie = (InsightEvent)value;
         if(ie.isMine()) left.setFont(boldF);
         else left.setFont(f);

         st = new StringTokenizer(value.toString(), " :", false);
         left.setText(st.nextToken());
         if (st.hasMoreTokens()) right.setText(st.nextToken() + " ");
         else right.setText("");

         left.setBackground(isSelected ? sbc : dbc);
         left.setForeground(isSelected ? sfc : dfc);
         right.setBackground(isSelected ? sbc : dbc);
         right.setForeground(isSelected ? sfc : dfc);
         this.setBackground(isSelected ? sbc : dbc);
         this.setForeground(isSelected ? sfc : dfc);


         switch(ie.getEventType()){
           case InsightEvent.MEMBER :
                left.setIcon(ImageList.memberIcon);
                break;
           case InsightEvent.METHOD :
                left.setIcon(ImageList.methodIcon);
                break;
           case InsightEvent.CLASS :
                left.setIcon(ImageList.classIcon);
                break;
           case InsightEvent.PACKAGE :
                left.setIcon(ImageList.packageIcon);
                break;
           case InsightEvent.ALL :
                left.setIcon(ImageList.allIcon);
                break;
           case InsightEvent.INTERFACE :
                left.setIcon(ImageList.interfaceIcon);
                break;

         }


         return this;
     }
  }

  boolean offEnter=false;
  public void setOffEnter() {
    offEnter=true;
  }

  /** component event handler */
  class ComponentHandler extends ComponentAdapter{

    public void componentResized(ComponentEvent e){
      if(CodePopup.this.isVisible()) exitAction();
    }
    public void componentMoved(ComponentEvent e){
      if(CodePopup.this.isVisible()) exitAction();
    }
  }

  class WindowHandler extends WindowAdapter{
    public void windowIconified(WindowEvent e){
      if(CodePopup.this.isVisible()) exitAction();
    }
  }

  class MouseHandler extends MouseAdapter{
    public void mousePressed(MouseEvent e){
     if(CodePopup.this.isVisible())  exitAction();
    }
  }

  private String getImplementsSource(String fullInterfaceName){

    Class instance = ClassPool.getClassInstance(fullInterfaceName);
    StringBuffer buf = new StringBuffer();
    String gap = "\t";
    buf.append("\n");

    Method [] methods = instance.getMethods();

    for(int i=0; i<methods.length; i++)
    {
      String paramStr = "";
      Class [] params = methods[i].getParameterTypes();

      for(int j=0; j<params.length; j++)
      {
         paramStr = params[j].getName().substring(params[j].getName().lastIndexOf(".")+1) + " e";
         if(j != params.length-1) paramStr += ", ";
      }
      buf.append(gap + "public " + "void " + methods[i].getName() + "( " + paramStr + " )");
      buf.append(gap + "{\n");
      buf.append(gap + gap + "//TO DO \n" );
      buf.append(gap + "}\n\n");
    }

    return buf.toString();
  }
}

