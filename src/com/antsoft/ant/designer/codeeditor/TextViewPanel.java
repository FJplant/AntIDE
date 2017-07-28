/*
 * $Id: TextViewPanel.java,v 1.45 1999/08/28 01:33:53 kahn Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.45 $
 */
package com.antsoft.ant.designer.codeeditor;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.Vector;
import java.util.StringTokenizer;
import java.util.Hashtable;
import java.util.Enumeration;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import javax.swing.text.html.*;


import com.antsoft.ant.main.*;
import com.antsoft.ant.manager.projectmanager.*;
import com.antsoft.ant.pool.sourcepool.*;
import com.antsoft.ant.property.*;
import com.antsoft.ant.property.defaultproperty.*;
import com.antsoft.ant.util.MyCaret;
import com.antsoft.ant.util.Constants;
import com.antsoft.ant.util.AnonyEventHandlerListDlg;
import com.antsoft.ant.util.HierarchyWindow;
import com.antsoft.ant.util.ImageList;
import com.antsoft.ant.util.AntViewport;

/**
 *  class TextViewPanel
 *
 *  @author Jinwoo Baek
 *  @ahthor kim sang kyun
 */
public class TextViewPanel extends JPanel {
  // Swing Component Attributes
//  private JEditorPane editor = new JEditorPane();
  private AntEditorPane editor = new AntEditorPane();
  private JavaEditorKit javaKit;
  private HtmlEditorKit htmlKit;
  private NormalEditorKit textKit;

	private EditorStatusBar sBar = null;
	private JScrollPane scroller;
	private AntViewport vp;
  private OpenSourcePanel osp;
  // Data Attributes
  private String fileName, pathName;
  private SourceEntry sourceEntry = null;
  private ProjectExplorer pe = null;
  private String fontName = "Courier";
  private int fontSize = 12;
  private int current_line = 1;
  private int current_column = 1;
	private int current_dot = 0;
	private int positionToInsert = 0;
	private Rectangle caretCoords = null;

  // popup menu
  private JPopupMenu editorPopup = new JPopupMenu();
  private JPopupMenu multiBlockEditorPopup = new JPopupMenu();
  private JPopupMenu blockEditorPopup = new JPopupMenu();

  private JPopupMenu editorPopupForHtml = new JPopupMenu();
  private JPopupMenu multiBlockEditorPopupForHtml = new JPopupMenu();
  private JPopupMenu blockEditorPopupForHtml = new JPopupMenu();

  private JMenuItem compile, anony, hierarchy, indent, unIndent,
                    comment, unComment, lineNum, workSpace,
                    indent2, unIndent2, lineNum2, workSpace2;
  
  private Font f;

  // Find & Replace Dlg
  private FindTextDialog ftdlg = null;
  private ReplaceTextDialog rtdlg = null;
  private GotoLineDlg gldlg = null;

  /////////// static popups /////////////
  public CodePopup popup;
  public HierarchyWindow hierarWindow = new HierarchyWindow();

  private TextReplacer textReplacer;
  private TextFinder textFinder;

  private DocumentListener documentHandler = new DocumentEventHandler();

  public static final String UPPER = "upper";
  public static final String BOTTOM = "bottom";

  //위에 것인지 아래것인지를 나타낸다
  private String tvpPosition;
  public static String focusedPosition = BOTTOM;

  //break point ui
  private HighLightManager hManager = new HighLightManager();

  // for selection
  private boolean sel = false;
  private int start = 0;
  private int end = 0;
  // for auto indent
  private String prev = "";
  // for caret position
  private int cp;
  private int fontHeight;
  //action hashtable
  private Hashtable actionHash = new Hashtable();

  //document에 맞는 key-action mapping. 현재 document에 따라서 editor에 갈아끼워준다
  public static final String JAVA_KEY_MAP = "JAVA_KEY_MAP";
  public static final String HTML_KEY_MAP = "HTML_KEY_MAP";
  public static final String TEXT_KEY_MAP = "TEXT_KEY_MAP";

  private JPanel editorWithLine;

  private JTextArea lineTa;
  private JScrollPane linePane;

  private static Hashtable viewPortPositions = new Hashtable(20, 5);

  // for CodeEditor
  public TextViewPanel() { jbInit(); }

  // for TabbedCodeEditor
  public TextViewPanel(ProjectExplorer pe, String position, EditorStatusBar statusBar, OpenSourcePanel osp ) {
    javaKit = pe.getMainFrame().getJavaKit();
    htmlKit = pe.getMainFrame().getHtmlKit();
    textKit = pe.getMainFrame().getNormalKit();

    this.pe = pe;
    setTvpPosition(position);
    sBar = statusBar;
    this.osp = osp;
    jbInit();
  }

  protected void finalize() {
  	if (sourceEntry != null) {
    	sourceEntry.getDocument().removeDocumentListener(documentHandler);
    }
  }

  void jbInit(){
    setLayout(new BorderLayout());
    setAutoscrolls(true);
    setOpaque(true);

    loadKeyMaps();

    //finder, replacer 생성
    textReplacer = new TextReplacer(editor);
    textFinder = new TextFinder(editor);

    //event handler 생성
    KeyListener keyHandlerForEditor = new EditorKeyEventHandler();
    MouseListener mouseHandlerForEditor = new EditorMouseEventHandler();
    FocusListener focusHandlerForEditor = new EditorFocusEventHandler();
    CaretListener caretHandler = new CaretEventHandler();

    editor.addKeyListener(keyHandlerForEditor);
    editor.addMouseListener(mouseHandlerForEditor);
    editor.addFocusListener(focusHandlerForEditor);
    editor.addCaretListener(caretHandler);

    MyCaret caret = new MyCaret();
    MyCaret.setCaretColor( MyCaret.getBiasedColor(editor.getBackground()) );
    editor.setCaret(caret);
   	sBar.setInsertMode(caret.getMode());

		editor.setMinimumSize(new Dimension(150, 0));

 		editor.setEditorKitForContentType("text/java", javaKit);
 		editor.setEditorKitForContentType("text/html", htmlKit);
    editor.setEditorKitForContentType("text/plain", textKit);

		editor.setEditable(true);
		editor.setBorder(null);
   	editor.setCursor(new Cursor(Cursor.TEXT_CURSOR));

    // for JAVA Popup Menu
    makeEditorPopup();
    // ------------------- End of Event Handler --------------------------------

    //break point UI관련

    lineTa = new JTextArea();
    lineTa.setEditable(false);
    lineTa.setForeground(Color.gray);
    lineTa.setColumns(5);

    linePane =  new JScrollPane(lineTa, ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER,
                                        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER  );;

    linePane.setBorder(null);

    editorWithLine = new JPanel(new BorderLayout());
    editorWithLine.add(editor, BorderLayout.CENTER);

    //viewport 설정
    vp = new AntViewport();
    vp.setView(editorWithLine);
    scroller = new JScrollPane();
    scroller.setViewport(vp);

    add(scroller, BorderLayout.CENTER);
		editor.setSize(200, editor.getSize().height);

    //popup 생성
    popup = new CodePopup(MainFrame.mainFrame, this, scroller);


    //환경 설정
 		reloadEnvironment();
    registerKeyboardActions();
  }

  int prevDocumentType = -1;
  private void changeMapToActions(int documentType){

    if(prevDocumentType == documentType) return;

    switch(documentType){
      case SourceEntry.JAVA :
         editor.setKeymap(JTextComponent.getKeymap(JAVA_KEY_MAP));
         break;

      case SourceEntry.HTML :
         editor.setKeymap(JTextComponent.getKeymap(HTML_KEY_MAP));
         break;

      case SourceEntry.TEXT :
         editor.setKeymap(JTextComponent.getKeymap(TEXT_KEY_MAP));
         break;

      default :
         editor.setKeymap(JTextComponent.getKeymap(TEXT_KEY_MAP));
         break;
    }

    prevDocumentType = documentType;
  }


  private void loadKeyMaps(){
    Keymap parent = editor.getKeymap();

    javaKit.setKeyMap(JTextComponent.addKeymap(JAVA_KEY_MAP, parent), editor);
    htmlKit.setKeyMap(JTextComponent.addKeymap(HTML_KEY_MAP, parent), editor);
    textKit.setKeyMap(JTextComponent.addKeymap(TEXT_KEY_MAP, parent), editor);
  }


  public void showImageFile(String path, String filename) {
    JPanel imagePanel = new JPanel(new BorderLayout());
//    imagePanel.setBackground(Color.lightGray);
    System.out.println("meorng");

    JButton imageBtn = new JButton(new ImageIcon(path+filename));
    if (imageBtn == null)
      System.out.println("image null");

    imagePanel.add(imageBtn,BorderLayout.CENTER);

    scroller = new JScrollPane(imagePanel);
    scroller.repaint();
    //scroller.show(true);

    System.out.println("superman");
  }


  public void findString(boolean isDialogShow){
    textFinder.do_Action(sourceEntry, vp, isDialogShow);
  }

  public void replaceString(boolean isDialogShow){
    textReplacer.do_Action(sourceEntry, vp, isDialogShow);
  }

  /**
   * sourceEntry를 null로 만들고 event listener를 해제 시킨다, 팝업을 사라지게 한다
   * file close할때 정리하는 작업 
   */
  public void clear(){
    if(sourceEntry != null) {
      //이 소스에 대한 에디터의 위치를 기억시킨다
      viewPortPositions.put(sourceEntry.getFullPathName(), new ViewportPosition(vp.getViewSize(), vp.getViewPosition()));

      if(sourceEntry.getDocumentType() == SourceEntry.JAVA ) {
        sourceEntry.getDocument().removeDocumentListener(documentHandler);
      }

      sourceEntry = null;
    }
    hidePopups();    
  }

  public void setSourceEntry(SourceEntry newSourceEntry){

    if (newSourceEntry == null) return;
    if(sourceEntry != null && newSourceEntry != null &&
         sourceEntry.equals(newSourceEntry) && !newSourceEntry.isExternallyUpdated()) return;

    if(sourceEntry != null) {
      //이 소스에 대한 에디터의 위치를 기억시킨다
      viewPortPositions.put(sourceEntry.getFullPathName(), new ViewportPosition(vp.getViewSize(), vp.getViewPosition()));

      if(sourceEntry.getDocumentType() == SourceEntry.JAVA ) {
        sourceEntry.getDocument().removeDocumentListener(documentHandler);
      }

      sourceEntry = null;
      hidePopups();
    }

    sourceEntry = newSourceEntry;
    fileName = newSourceEntry.getName();
    pathName = newSourceEntry.getPath();

    //java document 일 경우만 document listen 한다
    if(sourceEntry.getDocumentType() == SourceEntry.JAVA ) {
      sourceEntry.getDocument().addDocumentListener(documentHandler);
    }

    int type = sourceEntry.getDocumentType();
    switch(type){

       case SourceEntry.JAVA :
            editor.setContentType("text/java");
            break;

       case SourceEntry.HTML :
            editor.setContentType("text/html");
            break;

       case SourceEntry.TEXT :
            editor.setContentType("text/plain");
            break;

       default :
            editor.setContentType("text/plain");
    }

    changeMapToActions(type);
  	editor.setDocument(sourceEntry.getDocument());

    Object  o = viewPortPositions.get(sourceEntry.getFullPathName());
    if(o != null){
      ViewportPosition pos = (ViewportPosition)o;
      vp.setViewSize(pos.getViewSize());
      vp.setViewPosition(pos.getViewPosition());
    }
    else{
      vp.setViewPosition(new Point(0, 0));
    }

    int numOfLines = sourceEntry.getNumOfLines();

    sBar.setLineCount(1, 1, numOfLines);

    if(isLineVisible) setSourceLines();

    //AntEditorPane 에 SourceEntry를 setting한다 . undo redo action을 위하여
    editor.setSourceEntry(sourceEntry);
  }

  public void setSourceLines(){
    if(sourceEntry == null && !isLineVisible) return;

    int numOfLines = sourceEntry.getNumOfLines();
    int col = Integer.toString(numOfLines).length();
    lineTa.setColumns( col+2 );

    int oldPolicy = scroller.getVerticalScrollBarPolicy();
    scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

    lineTa.setText("");
    for(int i= 1 ; i<=numOfLines; i++){

      String white = "";
      int delta = col - (i+"").length()+1;

      for(int j=0; j<delta ; j++){
         white += " ";
      }
      lineTa.append(white + i + "\n");
    }
    scroller.setVerticalScrollBarPolicy(oldPolicy);

    prevLine = sourceEntry.getNumOfLines();
  }

  int prevLine = 0;
  public void adjustSourceLines(){
    if(!isLineVisible) return;

    int currLine = sourceEntry.getNumOfLines();
    if(prevLine == currLine) return;

    int gap = (currLine+"").length() - (lineTa.getColumns() - 2);

    if(currLine > prevLine){
      if(gap > 0) {
        setSourceLines();
      }
      else if(gap == 0){
        int col =  (currLine + "").length();
        PlainDocument lineDoc = (PlainDocument)lineTa.getDocument();
        Element root = lineDoc.getDefaultRootElement();

        for(int i=prevLine+1; i<=currLine; i++){

          String white = "";
          int delta = col - (i+"").length()+1;

          for(int j=0; j<delta ; j++){
             white += " ";
          }

          if(i>=lineTa.getLineCount()) lineTa.append(white + i + "\n");
          else {
            Element child = root.getElement(i-1);
            lineTa.replaceRange(white + i + "\n",  child.getStartOffset(), child.getEndOffset()-1);
          }
        }
      }
    }
    else if(currLine < prevLine){
      if(gap < 0){
        setSourceLines();
      }
      else if(gap == 0){

        PlainDocument lineDoc = (PlainDocument)lineTa.getDocument();
        Element root = lineDoc.getDefaultRootElement();

        if(!root.isLeaf()){
          for(int i=prevLine; i>=currLine; i--){
            Element child = root.getElement(i);
            lineTa.replaceRange(null, child.getStartOffset(), child.getEndOffset()-1);
          }
        }
      }
    }

    prevLine = currLine;
  }

	/**
	 *  Syntax Colouring 관련 변수를 리셋한다.
	 */
	public void reloadEnvironment() {

    //font 설정
    Font font = Main.property.getSelectedFont();
    editor.setFont(font);
    lineTa.setFont(font);

    //background color 설정
    Color background = Main.property.getColor(ColorPanel.BACKGROUND);
    if (background != null) editor.setBackground(background);

    //scroll increment 설정
    FontMetrics fm = editor.getFontMetrics(editor.getFont());
    int fontHeight = fm.getHeight();

    scroller.getVerticalScrollBar().setUnitIncrement(fontHeight);
    vp.setUnitIncrement(fontHeight);
		repaint();
	}

  /**
   * file 선택이 달라졌을때 사라지도록 하기 위함
   */
  public void hidePopups(){
    if(popup.isVisible()) popup.setVisible(false);
    if(paramPopups != null){
      for(int i=0; i<paramPopups.size(); i++){
        ParamPopup window =(ParamPopup)paramPopups.elementAt(i);
        window.destoryWindow();
      }
      paramPopups.removeAllElements();
    }
  }

  /**
   *
   * status bar의 참조를 얻는다
   */
  public EditorStatusBar getEditorStatusBar(){
    return sBar;
  }


	/**
	 *  에디터를 얻는다.
	 */
  public JEditorPane getEditor() {
    return editor;
  }

  public void moveLines(boolean isForward){
    if(sBar.isReadOnlyMode()) return;

    int start = editor.getSelectionStart();
    int end  = editor.getSelectionEnd()-1;

    int tabSize = Main.property.getTabSpaceSize();
    String whiteStr = "";
    for(int i=0; i<tabSize; i++){
      whiteStr += " ";
    }

    AntDocument doc = sourceEntry.getDocument();
    int line = sourceEntry.getLineFromOffset(start);
    Element el = sourceEntry.getElementAt(line);

    int lineStart = el.getStartOffset();
    while(lineStart <= end){
      try{
      	if (el != null) {
          if(isForward) doc.insertString(lineStart, whiteStr);
          else {
            String lineStr = el.getDocument().getText(lineStart, tabSize);

            int whiteNum = 0;
            for(int j=0; j<tabSize; j++){
              if(Character.isWhitespace( lineStr.charAt(j) )) ++whiteNum;
              else break;
            }
            doc.remove(lineStart, whiteNum);
          }
        }
      }catch(BadLocationException e){}

      el = sourceEntry.getElementAt(++line);
      lineStart = el.getStartOffset();
      end = editor.getSelectionEnd();
    }
  }

  public void commentLines(){
    if(sBar.isReadOnlyMode()) return;

    int start = editor.getSelectionStart();
    int end = editor.getSelectionEnd()-1;

    JavaDocument doc = (JavaDocument)sourceEntry.getDocument();

    int line = sourceEntry.getLineFromOffset(start);
    Element el = sourceEntry.getElementAt(line);
    int lineStart = el.getStartOffset();
    while(lineStart <= end){
      try{
      	if (el != null) doc.insertString(lineStart, "//");
      }catch(BadLocationException e){}

      el = sourceEntry.getElementAt(++line);
      lineStart = el.getStartOffset();
      end = editor.getSelectionEnd();
    }
  }

  public void unCommentLines(){
    if(sBar.isReadOnlyMode()) return;

    int start = editor.getSelectionStart();
    int end = editor.getSelectionEnd()-1;

    JavaDocument doc = (JavaDocument)sourceEntry.getDocument();

    int line = sourceEntry.getLineFromOffset(start);
    Element el = sourceEntry.getElementAt(line);

    int lineStart = el.getStartOffset();
    while(lineStart <= end){
      try{
      	if (el != null && el.getDocument().getText(lineStart, 2).equals("//")) {
          doc.remove(lineStart, 2);

        }
      }catch(BadLocationException e){}

      el = sourceEntry.getElementAt(++line);
      lineStart = el.getStartOffset();
      end = editor.getSelectionEnd();
    }
  }

  /**
   * View Port를 얻는다. Code Popup, Param Popup에서 위치 잡는데 사용
   */
  public AntViewport getViewPort(){
    return vp;
  }

	/**
	 *  에디터 킷을 얻는다.
	 */
  public DefaultEditorKit getEditorKit() {
    if(sourceEntry == null) return null;

    switch(sourceEntry.getDocumentType()){
      case SourceEntry.JAVA :
           return javaKit;
      case SourceEntry.HTML :
           return htmlKit;
      case SourceEntry.TEXT :
           return textKit;
      default :
           return textKit;
    }
  }

	/**
	 *  현재 에디터의 소스를 얻는다.
	 */
  public SourceEntry getSourceEntry() {
    return sourceEntry;
  }

	/**
	 *  현 소스의 파일 Path 를 얻는다.
	 */
  public String getPath() {
    return sourceEntry.getPath();
  }

	/**
	 *  현 소스의 파일 이름을 얻는다.
	 */
  public String getSourceName() {
    return sourceEntry.getName();
  }

	/**
	 *  현 소스의 케럿 위치의 라인을 얻는다.
	 */
  public int getCurrentLine() {
    return current_line;
  }

	/**
	 *  현 소스의 케럿 위치의 컬럼을 얻는다.
	 */
  public int getCurrentColumn() {
    return current_column;
  }

  /**
   * 위의 것인지 아래것인지를 설정한다
   */
  public void setTvpPosition(String position) {
  	tvpPosition = position;
  }
                                                 
  /**
   * 위의 것인지 아래것인지를 알려준다
   */
  public String getTvpPosition(){
    return tvpPosition;
  }

  /**
   *  현 콤포넌트에 포커스가 오도록 요청할 때 에디터에 포커스가 가도록 한다.
   */
  public void requestFocus() {
    editor.requestFocus();
  }

	/**
	 *  특정 위치로 caret을 이동시킨다.
	 */
	public void moveCaret(int offset, boolean highlight) {
  	try {
			editor.setCaretPosition(offset);
    } catch (Exception e) {
    	System.err.println("TextViewPanel : " + e);
    }

    if(highlight) hManager.setLine(offset);
    else hManager.clearLine();

		try {
			Rectangle rect = editor.modelToView(offset);
			int coord_y = vp.getExtentSize().height / 2;
			coord_y = rect.y - coord_y;
			if (coord_y < 0) coord_y = 0;
			vp.setViewPosition(new Point(0, coord_y));
      vp.repaint();
		} catch (BadLocationException e) {
			System.err.println("TextViewPanel : " + e);
		} catch (Exception e) {
    }
	}

	/**
	 *  케럿을 특정라인의 맨 처음으로 오도록 한다.
	 */
	public void moveLine(int line, boolean highlight) {
		Element el = sourceEntry.getElementAt(line);
  	if (el != null) moveCaret(el.getStartOffset(), highlight);
    else JOptionPane.showMessageDialog(this, "Out of Range by Line", "Go to Line", JOptionPane.ERROR_MESSAGE);
	}

  public void gotoLine(Frame owner) {
  	if (gldlg == null) gldlg = new GotoLineDlg(owner);
		Point p = this.getAccessibleContext().getAccessibleComponent().getLocationOnScreen();
		gldlg.setLocation(p.x + 30, p.y + 30);
   	gldlg.setVisible(true);
    if (gldlg.isOk()) {
    	int i = gldlg.getLineNumber();
      if (i > 0) moveLine(i - 1, false);
      else
      	JOptionPane.showMessageDialog(this, "Out of Range by Line", "Go to Line", JOptionPane.ERROR_MESSAGE);
    }
  }

  public Rectangle getCaretCoords(){
    return caretCoords;
  }

  public int getCurrentDot(){
    return this.current_dot;
  }

  public void showImporting(Vector eventData) {
    if(!popup.isVisible()){
      popup.setImportingData(eventData, sourceEntry.getDocument());
      popup.showWindow(editor);
    }
  }

  public void showNewing(Vector eventData) {
    if(!popup.isVisible()) {
      popup.setNewingData(eventData, sourceEntry.getDocument());
      popup.showWindow(editor);
    }
  }

  public void showImplements(Vector eventData) {
    if(!popup.isVisible()) {
      popup.setImplementsData(eventData, sourceEntry.getDocument(), sourceEntry);
      popup.showWindow(editor);
    }
  }

	/**
	 *  실제로 Intelligence를 보여준다.
	 *
	 *  @param eventData 리스트에 들어갈 데이터
	 *  @param owner 윈도우를 찍을 때 owner Window가 필요하다.
	 */
	public synchronized void showIntellisense(Vector eventData) {
	

    if(!popup.isVisible()) {
      popup.setIntelliData(eventData, sourceEntry.getDocument(), this);
      popup.showWindow(editor);
    }
  }

	/**
	 *  실제로 Parameterizing을 보여준다.
	 *
	 *  @param eventData 리스트에 들어갈 데이터
	 *  @param owner 윈도우를 찍을 때 owner Window가 필요하다.
	 */

  Vector paramPopups = new Vector(3, 2);
	public synchronized void showParameterizing(Vector eventData) {

    ParamPopup window = new ParamPopup(MainFrame.mainFrame, this, this.scroller);
    window.setParamData(eventData, sourceEntry.getDocument());

    if(paramPopups.size() > 0){
      ParamPopup prevWindow = (ParamPopup)paramPopups.lastElement();
      prevWindow.hideWindow();
    }
    paramPopups.addElement(window);
    window.showWindow(editor);
	}

  public void paramPopupDestoryed(){

    paramPopups.removeElementAt(paramPopups.size()-1);

    if(paramPopups.size() > 0){
      ParamPopup window = (ParamPopup)paramPopups.lastElement();
      window.reShowWindow(editor);
    }
  }

  public void setDocument(JavaDocument doc) {
    // PENDING(prinz) This should have a customizer and
		// be serialized.  This is a bogus initialization.
    editor.setDocument(doc);
		reloadEnvironment();
  }

  private void makeEditorPopup(){
    ActionListener actionHandler = new ActionEventHandler();

    compile = new JMenuItem("Make...");
    anony = new JMenuItem("Add Anonymous Class");
    hierarchy = new JMenuItem("Show Hierarchy");
    indent = new JMenuItem("Indent Block");
    unIndent = new JMenuItem("UnIndent Block");
    comment = new JMenuItem("Comment Block");
    unComment = new JMenuItem("UnComment Block");
    lineNum = new JMenuItem("");
    workSpace = new JMenuItem("");

    lineNum2 = new JMenuItem("");
    workSpace2 = new JMenuItem("");

    indent2 = new JMenuItem("Indent Block");
    unIndent2 = new JMenuItem("UnIndent Block");

    //icon 설정
    compile.setIcon(ImageList.make);
    indent.setIcon(ImageList.indent);
    indent2.setIcon(ImageList.indent);
	  unIndent.setIcon(ImageList.unindent);
	  unIndent2.setIcon(ImageList.unindent);
	  comment.setIcon(ImageList.comment);
	  unComment.setIcon(ImageList.uncomment);

	  lineNum.setIcon(ImageList.linecount);
	  lineNum2.setIcon(ImageList.linecount);

    compile.addActionListener(actionHandler);
    anony.addActionListener(actionHandler);
    hierarchy.addActionListener(actionHandler);
    indent.addActionListener(actionHandler);
    unIndent.addActionListener(actionHandler);
    comment.addActionListener(actionHandler);
    unComment.addActionListener(actionHandler);

    indent2.addActionListener(actionHandler);
    unIndent2.addActionListener(actionHandler);

    lineNum.addActionListener(actionHandler);
    workSpace.addActionListener(actionHandler);

    lineNum2.addActionListener(actionHandler);
    workSpace2.addActionListener(actionHandler);

    editorPopup.add(compile);
    editorPopup.addSeparator();
    editorPopup.add(javaKit.getActionForKeymap(DefaultEditorKit.pasteAction));
    editorPopup.add(javaKit.getActionForKeymap(JavaEditorKit.antUndo));
    editorPopup.add(javaKit.getActionForKeymap(JavaEditorKit.antRedo));
    editorPopup.addSeparator();
    editorPopup.add(lineNum);
    editorPopup.add(workSpace);
    editorPopup.addSeparator();
    editorPopup.add(anony);
    editorPopup.add(hierarchy);

    blockEditorPopup.add(javaKit.getActionForKeymap(DefaultEditorKit.copyAction));
    blockEditorPopup.add(javaKit.getActionForKeymap(DefaultEditorKit.cutAction));
    blockEditorPopup.addSeparator();
    blockEditorPopup.add(javaKit.getActionForKeymap(JavaEditorKit.antUndo));
    blockEditorPopup.add(javaKit.getActionForKeymap(JavaEditorKit.antRedo));

    multiBlockEditorPopup.add(javaKit.getActionForKeymap(DefaultEditorKit.copyAction));
    multiBlockEditorPopup.add(javaKit.getActionForKeymap(DefaultEditorKit.cutAction));

    multiBlockEditorPopup.add(javaKit.getActionForKeymap(JavaEditorKit.antUndo));
    multiBlockEditorPopup.add(javaKit.getActionForKeymap(JavaEditorKit.antRedo));
    multiBlockEditorPopup.addSeparator();
    multiBlockEditorPopup.add(indent);
    multiBlockEditorPopup.add(unIndent);
    multiBlockEditorPopup.add(comment);
    multiBlockEditorPopup.add(unComment);

    editorPopupForHtml.add(javaKit.getActionForKeymap(DefaultEditorKit.pasteAction));
    editorPopupForHtml.add(javaKit.getActionForKeymap(JavaEditorKit.antUndo));
    editorPopupForHtml.add(javaKit.getActionForKeymap(JavaEditorKit.antRedo));
    editorPopupForHtml.addSeparator();
    editorPopupForHtml.add(lineNum2);
    editorPopupForHtml.add(workSpace2);

    blockEditorPopupForHtml.add(javaKit.getActionForKeymap(DefaultEditorKit.copyAction));
    blockEditorPopupForHtml.add(javaKit.getActionForKeymap(DefaultEditorKit.cutAction));
    blockEditorPopupForHtml.addSeparator();
    blockEditorPopupForHtml.add(javaKit.getActionForKeymap(JavaEditorKit.antUndo));
    blockEditorPopupForHtml.add(javaKit.getActionForKeymap(JavaEditorKit.antRedo));

    multiBlockEditorPopupForHtml.add(javaKit.getActionForKeymap(DefaultEditorKit.copyAction));
    multiBlockEditorPopupForHtml.add(javaKit.getActionForKeymap(DefaultEditorKit.cutAction));

    multiBlockEditorPopupForHtml.add(javaKit.getActionForKeymap(JavaEditorKit.antUndo));
    multiBlockEditorPopupForHtml.add(javaKit.getActionForKeymap(JavaEditorKit.antRedo));
    multiBlockEditorPopupForHtml.addSeparator();
    multiBlockEditorPopupForHtml.add(indent2);
    multiBlockEditorPopupForHtml.add(unIndent2);
  }

  private void registerKeyboardActions(){

		editor.registerKeyboardAction(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				pe.getMainFrame().getCodeContext().activateContextSensitiveHelp(editor.getCaretPosition());
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0),
		JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

		editor.registerKeyboardAction(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				pe.getMainFrame().getCodeContext().viewJdkSource(editor.getCaretPosition());
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0),
		JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);


		editor.registerKeyboardAction(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (sourceEntry != null) {
        	if (pe != null) pe.compileFile(sourceEntry.getPath(), sourceEntry.getName());
				}
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_F10, java.awt.Event.CTRL_MASK),
		JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

		editor.registerKeyboardAction(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				pe.buildProject();
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_F9, java.awt.Event.CTRL_MASK),
		JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
  }


  boolean isLineVisible = false;
  private void toggleLineNum(){

    if(!isLineVisible) {
      if(lineTa.getText().equals("")) setSourceLines();
       editorWithLine.add(linePane, BorderLayout.WEST);
    }
    else {
      lineTa.setText("");
      editorWithLine.remove(linePane);
    }

    isLineVisible = !isLineVisible;

    editorWithLine.doLayout();
    editorWithLine.repaint();
  }

  private void toggleWorkSpace(){
    pe.toggleWorkSpace();
  }

  /////////////////////////////////////////////////////////////////////////////////////

	class LineHighlighter implements Highlighter.HighlightPainter {
    private Color highLightC, breakC;
    private int lineNo = -1;
    private Rectangle mRect;
    private int mHeight;

    public LineHighlighter(){
      highLightC = Color.cyan;
      breakC = Color.red;
    }

    public void setLineNum(int lineNo){
      this.lineNo = lineNo;
    }

    public int getLineNum(){
      return lineNo;
    }

    public int getYpos(int line){
    /*
      if(mRect==null) return -1;
      else return mRect.y + mHeight * line + mHeight/3;
    */
      return mHeight * line + mHeight/3;
    }

		public void paint(Graphics g, int p0, int p1, Shape bounds,	JTextComponent textComponent)	{

			Document doc = editor.getDocument();
			FontMetrics metrics = g.getFontMetrics();
			Rectangle rect = (Rectangle)bounds;
			int height = metrics.getHeight();
			int x = rect.x;
			int y = rect.y + height * lineNo;
			g.setColor(highLightC);

      mRect = rect;
      mHeight = height;

      if(lineNo != -1) g.fillRect(x, y, editor.getWidth(), height);

      for(Enumeration e = sourceEntry.getBreakPoints().keys(); e.hasMoreElements(); ){
        Integer line = (Integer)e.nextElement();
        int num = line.intValue();
  			y = rect.y + height * num;
      	g.setColor(breakC);
   			g.fillRect(x, y, editor.getWidth(), height);
      }
		}
	}

  /////////////////////////////////////////////////////////////////////////////////////////////////

  class HighLightManager {

  	private Object selObj = null;
    private LineHighlighter lineHighlighter = new LineHighlighter();

    public LineHighlighter getLineHighlighter(){
      return lineHighlighter;
    }


    public int getYpos(int line){
      return lineHighlighter.getYpos(line);
    }

    public void setLine(int offset){
			try	{
   	    AntDocument doc = sourceEntry.getDocument();
      	Element root = doc.getDefaultRootElement();
        int lineNo = root.getElementIndex(offset);
        lineHighlighter.setLineNum(lineNo);

    		Document doc2 = editor.getDocument();
 		    Element map = doc2.getDefaultRootElement();
    		Element lineElement = map.getElement(lineNo);

      	int start = lineElement.getStartOffset();
  			int end = lineElement.getEndOffset();

        if(selObj == null) selObj = editor.getHighlighter().addHighlight(0, 0, lineHighlighter);
        else editor.getHighlighter().changeHighlight(selObj, start, end);

			} catch(BadLocationException bl) {
				bl.printStackTrace();
			}
    }

    public void clearLine(){
      if(selObj != null){
        editor.getHighlighter().removeHighlight(selObj);
        selObj = null;
        vp.repaint();
      }
    }
	}

  /////////////////////////////////////////////////////////////////////////////////////////////////

  /** DocumentEventHandelr Inner Class */
  class DocumentEventHandler implements DocumentListener{
    public void insertUpdate(DocumentEvent evt) {
      try {
        int offset = evt.getOffset();

        PlainDocument doc=(PlainDocument)editor.getDocument();
        Element element=doc.getParagraphElement(offset);
        // 제일 마지막에 문자를 추가할 때에 BadLocationException이 뜨는 것을
        // 방지하기 위해서 넣음.
        // Element의 offset은 는 앞과 뒤의 Line Break('\n')을 무조건 포함하는 것 같음.
        // 따라서, 제일 마지막의 실제로 '\n'이 마지막에 붇지 않은 라인의 경우에는
        // 문제 발생
        int length = (element.getEndOffset() > doc.getLength()) ?
                     doc.getLength() - element.getStartOffset() + 1:
                     element.getEndOffset() - element.getStartOffset() + 1;
        String string=doc.getText(element.getStartOffset(), length);
        int local=offset-element.getStartOffset();
        char c=string.charAt(local);
        if (c=='*') {
          if (local-1>=0&&string.charAt(local-1)=='/') {
            repaint();
            return;
          }
          else if (local+1<string.length()&&string.charAt(local+1)=='/') {
            repaint();
            return;
          }
        }

        if (c=='/') {
          if (local+1<string.length()&&string.charAt(local+1)=='*') {
            repaint();
            return;
          }
          else if (local-1>=0&&string.charAt(local-1)=='*') {
            repaint();
            return;
          }
          else if ( (local-1>=0&&string.charAt(local-1)=='/') && (string.substring(local).indexOf("/*") != -1) ){
            repaint();
            return;
          }
          else if ( (local+1<string.length()&&string.charAt(local+1)=='/') && (string.substring(local+1).indexOf("/*") != -1) ){
             repaint();
            return;
          }
        }
      } catch (BadLocationException e) {
        System.out.println("TextViewPanel 22: "+e.toString());
      }
      sBar.setModified(sourceEntry.isModified());
      osp.notifyModifiedSource(sourceEntry.isModified());
      adjustSourceLines();
    }

    public void removeUpdate(DocumentEvent evt) {
      // 즉각적인 coloring을 위해.... by kahn
      repaint();
      sBar.setModified(sourceEntry.isModified());
      osp.notifyModifiedSource(sourceEntry.isModified());
      adjustSourceLines();
    }

    public void changedUpdate(DocumentEvent evt) {
      sBar.setModified(sourceEntry.isModified());
      osp.notifyModifiedSource(sourceEntry.isModified());
    }
  }

  /////////////////////////////////////////////////////////////////////////////////////////////////
  /** ActionEventHandelr Inner Class */
  class ActionEventHandler implements ActionListener{
    public void actionPerformed(ActionEvent evt){
      Object src = evt.getSource();
      if (src==indent || src==indent2) moveLines(true);
      else if (src==unIndent || src==unIndent2) moveLines(false);
      else if (src == comment) commentLines();
      else if (src == unComment)unCommentLines();
      else if (src==lineNum || src==lineNum2) toggleLineNum();
      else if (src==workSpace || src==workSpace2) toggleWorkSpace();
      else if (src == compile) {
        if (sourceEntry != null && pe != null) { pe.compileFile(sourceEntry.getPath(), sourceEntry.getName()); }
      }
      else if (src == hierarchy) {
        hierarWindow.showWindow(editor);
      }
      else if (src == anony) {
        if (sourceEntry == null) return;

        AnonyEventHandlerListDlg dlg = new AnonyEventHandlerListDlg();
        dlg.showWindow();

        if(dlg.isOK()){
          try {
            int offsetInserted = editor.getCaretPosition();
            Element elem = sourceEntry.getDocument().getParagraphElement(offsetInserted);
            sourceEntry.getDocument().insertString(offsetInserted, dlg.getGeneratedSource());
          }catch (BadLocationException e) {
            System.err.println(e);
          }
        }
      }
    }
  }

  /////////////////////////////////////////////////////////////////////////////////////////////////
  /** Editor FocusEventHandelr Inner Class */
  class EditorFocusEventHandler extends FocusAdapter{
    public void focusLost(FocusEvent evt) {

      int old_selStart = editor.getSelectionStart();
      int old_selEnd = editor.getSelectionEnd();

      if(old_selStart != old_selEnd){
        editor.setSelectionStart(old_selStart);
        editor.setSelectionEnd(old_selEnd);

        editor.repaint();
      }
    }

    public void focusGained(FocusEvent evt) {
      //현재 포커스가 있는 tvp를 설정한다
      if(sourceEntry == null) return;

      pe.checkExternalFileUpdate();
      sBar.setInsertMode(((MyCaret)editor.getCaret()).getMode());

      File f = sourceEntry.getFile();

      //generated source from classdesigner
      if(f != null){
        if( !f.exists() ) editor.setEditable(true);
        else editor.setEditable(f.canWrite());
      }

      sBar.setModified(sourceEntry.isModified());
      osp.notifyModifiedSource(sourceEntry.isModified());

      sBar.setReadOnlyMode(!editor.isEditable());
      editor.getCaret().setVisible(true);

      if(!focusedPosition.equals(getTvpPosition())){
        focusedPosition = getTvpPosition();
        pe.focusOfTextViewPanelChanged(sourceEntry);
      }
    }
  }

  /////////////////////////////////////////////////////////////////////////////////////////////////
 /** Editor MouseEventHandelr Inner Class */
  class EditorMouseEventHandler extends MouseAdapter{

    public void mouseReleased(MouseEvent evt) {
      if(hierarWindow.isVisible()) hierarWindow.dispose();
      editorPopup.setVisible(false);
      blockEditorPopup.setVisible(false);
      multiBlockEditorPopup.setVisible(false);

      if (evt.isPopupTrigger()) {
        String sel = editor.getSelectedText();
        if(sel != null){
          if(sel.indexOf("\n") == -1)  {
            if(sourceEntry.getDocumentType() == SourceEntry.JAVA) blockEditorPopup.show(editor, evt.getX(), evt.getY());
            else blockEditorPopupForHtml.show(editor, evt.getX(), evt.getY());
          }
          else  {
            if(sourceEntry.getDocumentType() == SourceEntry.JAVA) multiBlockEditorPopup.show(editor, evt.getX(), evt.getY());
            else multiBlockEditorPopupForHtml.show(editor, evt.getX(), evt.getY());
          }
        }
        else {
          if(TextViewPanel.this.isLineVisible) {
            lineNum.setText("Hide Line Number");
            lineNum2.setText("Hide Line Number");
          }
          else {
            lineNum.setText("Show Line Number");
            lineNum2.setText("Show Line Number");
          }

          if(pe.isWorkSpaceVisible()) {
            workSpace.setText("Hide WorkSpace");
            workSpace2.setText("Hide WorkSpace");
          }
          else{
            workSpace.setText("Show WorkSpace");
            workSpace2.setText("Show WorkSpace");
          }

          if(sourceEntry.getDocumentType() == SourceEntry.JAVA){
            HierarchyWindow.currentFullClassName = pe.getMainFrame().getCodeContext().getFullClassNameAtCursor( editor.getCaretPosition() );
            if(HierarchyWindow.currentFullClassName == null) hierarchy.setEnabled(false);
            else hierarchy.setEnabled(true);
            editorPopup.show(editor, evt.getX(), evt.getY());
          }
          else{
            editorPopupForHtml.show(editor, evt.getX(), evt.getY());
          }
        }
      }
      cp = 0;
    }

    public void mousePressed(MouseEvent evt) {
      pe.setFocusedComponent(ProjectExplorer.TEXTVIEW_PANEL);
      cp = 0;

      if(evt.getModifiers() != MouseEvent.BUTTON1_MASK && editor.getSelectedText() == null) {
        editor.setCaretPosition(editor.viewToModel(evt.getPoint()));
      }
    }
  }

  /////////////////////////////////////////////////////////////////////////////////////////////////
  /** Editor CaretEventHandelr Inner Class */
  class CaretEventHandler implements CaretListener{
   int prev_dot = -1;
   public void caretUpdate(CaretEvent evt) {

  		current_dot = evt.getDot();
      if(prev_dot == current_dot) return;
      prev_dot = current_dot;

      try {
        caretCoords = editor.modelToView(current_dot);
      } catch (BadLocationException e) {
        caretCoords = new Rectangle(0, 0, 0, 0);
        System.err.println("Exception occurred : " + e);
      }

      // 현재 Line과 Col을 구해서 적당히 표시해 준다.
      if (sourceEntry != null) {
        int as = editor.getCaretPosition();
        int current_line = sourceEntry.getLineFromOffset(as);
        if (current_line != -1) {
          int first = sourceEntry.getElementAt(current_line).getStartOffset();
          current_column = as - first;
          sBar.setLineCount(current_line + 1, current_column + 1, sourceEntry.getNumOfLines());
        }
      }

      hManager.clearLine();
    }
  }

  /////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 마우스 툴팁을 이용하여서, 특정 오브젝트의 타입을 나타내어 준다.
     */
  class MouseMotionHandler extends MouseMotionAdapter {
    public void mouseMoved( MouseEvent e ) {
      if ( true ) {
      // TODO:
      // 1. 현재, 마우스 캐럿의 위치에서부터 해당되는 영역의 String을
      //   White space 또는 . , {, [, ...앞까지 짤라온다.
      // 2. 짤라온 내용의 Type을 알아낸다.
      // 3. 타입을 ToolTip에 지정한다.
          String type = "java.awt.MouseEvent"; // 왼쪽과 같은 값으로
          setToolTipText( type );
      } else // 타입을 알 수 없는 위치에 있으면
          setToolTipText( "" );
    }
  }

   /** Editor KeyEventHandelr Inner Class */
   class EditorKeyEventHandler extends KeyAdapter{

    //for Ctrl-Space
    private boolean typedOff = false;
    public void keyTyped(KeyEvent evt){
      if(typedOff) {
        evt.consume();
        typedOff = false;
      }
    }

    public void keyPressed(KeyEvent evt) {

      switch(evt.getKeyCode()) {

      case KeyEvent.VK_ESCAPE :
        if(hierarWindow != null && hierarWindow.isVisible())
          hierarWindow.setVisible(false);

        if(editorPopup.isVisible()) editorPopup.setVisible(false);

        if(editor.getSelectedText() != null){
          int big = (editor.getSelectionEnd() > editor.getSelectionStart()) ? editor.getSelectionEnd() : editor.getSelectionStart();
          editor.setSelectionStart(big);
          editor.setSelectionEnd(big);
        }
        break;

      case KeyEvent.VK_UP :
        if(popup.isVisible()) {
          evt.consume(); return;
        }

        if(paramPopups != null){
          for(int i=0; i<paramPopups.size(); i++){
            ParamPopup window =(ParamPopup)paramPopups.elementAt(i);
            window.destoryWindow();
          }
           paramPopups.removeAllElements();          
        }

        break;

      case KeyEvent.VK_DOWN :
        if(popup.isVisible()) {
          evt.consume();
          return;
        }

        if(paramPopups != null){
          for(int i=0; i<paramPopups.size(); i++){
            ParamPopup window =(ParamPopup)paramPopups.elementAt(i);
            window.destoryWindow();
          }
          paramPopups.removeAllElements();          
        }
        break;

      case KeyEvent.VK_INSERT:
        if (sBar != null && !evt.isShiftDown() && !evt.isControlDown()) {
          MyCaret caret_sbar = (MyCaret)editor.getCaret();
          sBar.setInsertMode(!caret_sbar.getMode());
          ((JavaDocument)sourceEntry.getDocument()).setInsertMode(!caret_sbar.getMode());
          caret_sbar.setMode(!caret_sbar.getMode());
          evt.consume();
        }

        break;

      case KeyEvent.VK_SPACE:

        if(popup.isVisible()) {
          evt.consume();
        }

        else if (evt.isControlDown()) {
          typedOff = true;
          pe.getMainFrame().getCodeContext().thisCodeInsight(editor.getCaretPosition());
          evt.consume();
        }

        else if (evt.isAltDown()) {
          typedOff = true;
          pe.getMainFrame().getCodeContext().doConstructorCompletionNoNew(editor.getCaretPosition());
          evt.consume();
        }

        break;

      case KeyEvent.VK_F:
        if (evt.isControlDown()) {
          findString(true);
          evt.consume();
        }
        break;

      case KeyEvent.VK_J:
        if (evt.isControlDown()) {
          pe.gotoLine();
          evt.consume();
        }
        break;

      case KeyEvent.VK_R:
        if (evt.isControlDown()) {
          replaceString(true);
          evt.consume();
        }
        break;

      case KeyEvent.VK_F3:
        findString(false);
        evt.consume();
        break;

      case KeyEvent.VK_0:
        if (evt.isShiftDown()) {
          if ((popup != null) && (popup.isVisible())) {
            popup.setVisible(false);
          }
          evt.consume();
        }
        break;

      case KeyEvent.VK_PAGE_UP:
      case KeyEvent.VK_PAGE_DOWN:
        if(popup.isVisible()) {
          evt.consume();
          return;
        }
        break;

      // for use Tab char
      case KeyEvent.VK_TAB:
      	if (popup.isVisible()) {
        	evt.consume();
          return;
        }

        if (!Main.property.isUseTabChar()) {
          AntDocument doc = sourceEntry.getDocument();
          int offset = editor.getCaretPosition();
          try {
          	int tabSize = Main.property.getTabSpaceSize();
            StringBuffer buf = new StringBuffer();
            for (int i = 0; i < tabSize; i++) buf.append(" ");
            doc.insertString(offset, buf.toString());
          } catch (BadLocationException e) {
          	System.err.println(e.getMessage());
          }
          evt.consume();
        }
      	break;

      // for auto Indent
      case KeyEvent.VK_ENTER:
        if(popup.isVisible()) {
          evt.consume();
          return;
        }

        if (Main.property.isAutoIndentMode() && editor.isEditable()) {
          AntDocument doc = sourceEntry.getDocument();
          int offset = editor.getCaretPosition();
          Element prevLine = doc.getParagraphElement(offset);
          // Indent에 필요한 정보를 찾는다.
          Element parentLine = prevLine.getParentElement();
          try {
            for (int i = parentLine.getElementIndex(offset); i >= 0; i--) {
              Element elm = parentLine.getElement(i);
              String st = doc.getText(elm.getStartOffset(), elm.getEndOffset() - elm.getStartOffset() - 1);
              prev = "";
              if (!st.trim().equals("")) {
                StringTokenizer token = new StringTokenizer(st);
                String temp = token.nextToken();
                i = st.indexOf(temp);
                if (i > 0) prev = st.substring(0, i);
                break;
              }
            }
            String str = doc.getText(prevLine.getStartOffset(),
                              prevLine.getEndOffset() - prevLine.getStartOffset() - 1);
//            if (str.trim() != "")
            StringTokenizer token = new StringTokenizer(str);
            String temp = null;

            //java document에서는 개행문자가 system에 의존하는것이 아니라
            //무조건 '\n'이다
            if (token.hasMoreTokens()) {
              temp = token.nextToken();
              int i = str.indexOf(temp);
              if (i > 0) {
                if (offset > i + prevLine.getStartOffset())
                  doc.insertString(offset, "\n" + prev);
                else {
                  String s = doc.getText(prevLine.getStartOffset(), offset - prevLine.getStartOffset());
                  doc.insertString(offset, "\n" + s);
                }
              } else if (i < 0) {
                doc.insertString(offset, "\n" + str);
              } else {
                doc.insertString(offset, "\n");
              }
            }
            else {
              doc.insertString(offset, "\n" + prev);
//              doc.insertString(offset, "\n" + str);
            }
          } catch (BadLocationException e) {
            System.err.println(e.getMessage());
          }
	        evt.consume();
        }
        break;
      }
    }
  }  
  
  //editor pane으로 상속받아 sourceEntry를 참조하도록 한다 
  class AntEditorPane extends JEditorPane {
    private SourceEntry se;
    
    public AntEditorPane(){
      super();
    }  
    
    public void setSourceEntry(SourceEntry se){
      if(this.se != null && se != null && this.se.equals(se)) return;
      this.se = null;
      this.se = se;
    }  
    
    public SourceEntry getSourceEntry(){
      return se;
    }
  }

  class ViewportPosition {
    public Dimension viewSize;
    public Point viewPos;

    public ViewportPosition(Dimension d, Point p){
      viewSize = d;
      viewPos = p;
    }

    public Dimension getViewSize(){
      return viewSize;
    }

    public Point getViewPosition(){
      return viewPos;
    }
  }  
}

/*
 * $Log: TextViewPanel.java,v $
 * Revision 1.45  1999/08/28 01:33:53  kahn
 * no message
 *
 * Revision 1.44  1999/08/24 08:29:59  remember
 * no message
 *
 * Revision 1.43  1999/08/24 06:05:38  remember
 * no message
 *
 * Revision 1.42  1999/08/23 08:25:51  remember
 * no message
 *
 * Revision 1.41  1999/08/23 07:51:20  lila
 * no message
 *
 * Revision 1.40  1999/08/23 02:49:33  remember
 * no message
 *
 * Revision 1.39  1999/08/21 06:40:20  multipia
 * JavaDocument.setInsertMode 수정
 *
 */

