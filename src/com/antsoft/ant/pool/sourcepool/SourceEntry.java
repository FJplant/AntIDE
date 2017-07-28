/*
 * $Id: SourceEntry.java,v 1.18 1999/08/30 09:49:04 remember Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.18 $
 * $History: SourceEntry.java $
 */
package com.antsoft.ant.pool.sourcepool;

import java.io.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.undo.*;
import javax.swing.event.*;
import java.util.Hashtable;

/**
 *  파일 하나당 대입되는 관계로, Document를 처리한다.
 *  파일에 관련된 정보 및 Document 정보를 가지고 있다.
 *  또한, Undo/Redo 관련 기능들을 제공한다.
 *
 *  @author Jin-woo Baek
 *  @author kim sang kyun
 *  @author Kwon, Young Mo
 */
public class SourceEntry {
  private AntDocument doc;
	private AntDocument old_Doc;
  private String pathName;
  private String fileName;
  private File fileObj;
  private Hashtable breakPoints = new Hashtable();
  private String runnableClassName = null;
  private boolean isLibraryFile = false;
  /** 파일이 자동으로 저장 되었는지를 표시한다. */
  private boolean autoSaved = false;

  /** Listener for the edits on the current document. */
  protected UndoableEditListener undoHandler = new UndoHandler();

  /** UndoManager that we add edits to. */
  protected UndoManager undo = new UndoManager();

  private boolean isExternalUpdated = false;

  public static final int JAVA = 0;
  public static final int HTML = 1;
  public static final int TEXT = 2;

  private int fileType;

	/**
	 *  Constructor. file이 실재로 존재 할때 사용
	 */
	public SourceEntry( File file, AntDocument doc ) {
		fileObj = file;
		pathName = fileObj.getParent();
		fileName = fileObj.getName();

		if ( doc == null ) 
			this.doc = openFile();
		else
			this.doc = doc;
			
    if ( this.doc != null ) {
	  this.doc.setModified(false);
  	  this.doc.addUndoableEditListener(undoHandler);
  	}
	}
	 
  public SourceEntry(String pathName, String fileName) {
    this( new File(pathName, fileName), null );
  }

  public SourceEntry(String fullPath){
    this( new File(fullPath), null );
  }

  public SourceEntry(String pathName, String fileName, AntDocument doc){
  	this( new File( pathName, fileName ), doc );
  }

  public SourceEntry(String fileName, AntDocument doc){
    this.fileName = fileName;
    this.doc = doc;

    if ( doc != null ) {
	    doc.setModified(false);
  	  doc.addUndoableEditListener(undoHandler);
  	}

    this.isLibraryFile = true;
  }

	/**
	 *  Constructor. file이 실존하지 않고 만들어진 source일때 사용
	 */
	public SourceEntry(String pathName, String fileName, String data) {
		this.pathName = pathName;
		this.fileName = fileName;

    AntDocument doc = null;
    if(this.isJavaFile(fileName)){
      doc = new JavaDocument();
      fileType = JAVA;
    }
    else if(this.isHtmlFile(fileName)){
      doc = new HtmlDocument();
      fileType = HTML;
    }
    else {
      doc = new NormalDocument();
      fileType = TEXT;
    }

    try {
      doc.insertString(0, data, null);
    } catch (BadLocationException ble) {
      ble.printStackTrace();
    }
    if(pathName == null)  fileObj = null;
    else fileObj = new File(pathName, fileName);

    this.doc = doc;
    doc.setModified(true);
		doc.addUndoableEditListener(undoHandler);
	}

  public void setExternalUpdate(boolean flag){
    this.isExternalUpdated = flag;
  }

  public boolean isExternallyUpdated(){
    return isExternalUpdated;
  }  

  public int getDocumentType(){
    return fileType;
  }

  public String getFullPathName(){
    if(isLibraryFile) return this.fileName;
    else return fileObj.getAbsolutePath();
  }

  private File getBufferFile(){
    if(isLibraryFile) return null;
    else return new File(pathName, fileName.substring(0, fileName.lastIndexOf(".")) + ".~ant");
  }

  public Hashtable getBreakPoints(){
    return breakPoints;
  }

  private boolean isJavaFile(String name){
    return name.toLowerCase().endsWith(".java");
  }

  private boolean isHtmlFile(String name){
    return name.toLowerCase().endsWith(".htm") || name.toLowerCase().endsWith(".html") ;
  }


	/**
	 *  디스크에서 파일을 읽는다.
	 * @return 파일 열기에 성공하였으면, 열린 AntDocument
	 *         실패했으면 null
	 */
  public AntDocument openFile() {

    if ((fileObj != null) && fileObj.canRead()) {
    	// Document의 초기 크기를 계산한다.
    	int size = (int)fileObj.length();		// 2G가 넘는 source file은 없겠지.
    	size = (int)(size * 1.1); 					// 10%정도 크게 잡는다. 추가되는 text를 위해

    	try {
    		AntDocument doc;
        if(isJavaFile(fileObj.getName())){
          doc = new JavaDocument(size);
          fileType = JAVA;
        }
        else if(isHtmlFile(fileObj.getName())){
          doc = new HtmlDocument(size);
          fileType = HTML;
        }
  
        else {
          doc = new NormalDocument(size);
          fileType = TEXT;
        }
        FileLoader fl = new FileLoader(fileObj, doc);
				return doc;
				
     	} catch ( OutOfMemoryError e ) {
     		// TODO: 메모리가 부족해서 더 이상 파일을 열지 못할 때의 
     		//      error handling을 좀더 세밀하게 해야 한다.
     		return null;
     	}
    }
    
    fileType = TEXT;
    return new NormalDocument();
  }

	/**
	 *  디스크에 저장한다.
   *
   * @return success or failure
	 */
  public boolean saveFile() {
    if (fileObj != null) {
      File parent = new File(fileObj.getParent());
    	if ( !fileObj.exists() || (fileObj.exists() && fileObj.canWrite())) {

        // 현재의 document를 원본 파일로 저장한다.
	      boolean flag = FileFlusher.doAction(fileObj, doc);
        if(flag == true){
          doc.saved();
          // 현재의 파일을 버퍼 파일로 복사한다.
          copyFile(fileObj, getBufferFile());

          return true;
        }
      }
    }

    return false;
  }

  /**
   *  파일을 복사하는 method
   */
  private static boolean copyFile(File srcFile, File desFile){
    if(srcFile != null && srcFile.exists()){
      if (desFile.exists()) desFile.delete();

      RandomAccessFile is= null;
      RandomAccessFile os= null;

      try {
        is=new RandomAccessFile(srcFile,"r");
        os=new RandomAccessFile(desFile,"rw");

        byte[] data=new byte[1024];

        while (true) {
          int len=is.read(data,0,1024);
          if (len<0) break;
          os.write(data,0,len);
        }
      } catch (Exception e) {
        System.err.println(" IOException occurred in CopyFile() : "+e.toString());
        return false;
      } finally {
        try{
          os.close();
          is.close();
        }catch(IOException e2){
          os = null;
          is = null;
        }
        is = null;
        os = null;
      }
    }

    return true;
  }


  /**
   * 원본 파일을 buffer file로 복사 한다
   */
  public static boolean syncWithRealFile(String pathName, String fileName){
    File srcFile = new File(pathName, fileName);
    File desFile = new File(pathName, fileName.substring(0, fileName.lastIndexOf(".")) + ".~ant");

    boolean ret = copyFile(srcFile, desFile);

    srcFile = null;
    desFile = null;
    
    return ret;
  }

  /**
   * buffer file을 원본파일로 복사 한다
   */
  public static boolean syncWithBuffer(String pathName, String fileName){
    File srcFile = new File(pathName, fileName.substring(0, fileName.lastIndexOf(".")) + ".~ant");
    File desFile = new File(pathName, fileName);

    boolean ret = copyFile(srcFile, desFile);

    srcFile = null;
    desFile = null;

    return ret;
  }

	/**
	 *  파일이름을 얻는다.
	 */
  public String getName() {
  	if ((fileObj != null))
    	return (fileName = fileObj.getName());
    return fileName;
  }

	/**
	 *  파일 경로를 얻는다.
	 */
  public String getPath() {
  	if ((fileObj != null))
    	return (pathName = fileObj.getParent());
    return pathName;
  }

	/**
	 *  Document 를 얻는다.
	 */
  public AntDocument getDocument() {
    return doc;
  }

	/**
	 *  Document를 세팅한다.
	 */
	public void setDocument(AntDocument doc2) {
    if(this.old_Doc != null){
      this.old_Doc = null;
    }

		this.old_Doc = this.doc;

    if(this.doc != null) {
      this.doc.removeUndoableEditListener(undoHandler);
      this.doc = null;
    }

		this.doc = doc2;
		this.doc.addUndoableEditListener(undoHandler);
	}

	/**
	 *  Beautifier 를 위함
	 */
	public AntDocument getOldDocument() {
		return old_Doc;
	}

	/**
	 *  주어진 offset의 line을 얻어온다.
	 *
	 *  @param offset 오프셋
	 */
	public int getLineFromOffset(int offset) {
		if (doc != null) {
			Element root = doc.getDefaultRootElement();
			if (!root.isLeaf()) {
				int child = root.getElementIndex(offset);
				if (child >= 0) return child;
			}
		}
		return -1;
	}

	/**
	 *  주어진 line의 Element를 얻어온다.
	 *
	 *  @param line 얻을 라인
	 */
	public Element getElementAt(int line) {
		if (doc != null) {
			Element root = doc.getDefaultRootElement();
			if (!root.isLeaf()) {
				Element child = root.getElement(line);
				if (child != null) return child;
			}
		}
		return null;
	}

  /**
   *  전체 라인수를 얻어온다.
   */
  public int getNumOfLines() {
  	if (doc != null) {
    	Element root = doc.getDefaultRootElement();
      if (!root.isLeaf()) {
      	return root.getElementCount();
      }
    }
    return -1;
  }

	/**
	 *  파일을 세팅한다.
	 */
  public void setFile(String path, String file, boolean option) {
    if (option) fileObj = new File(path, file);
    if (fileObj != null) {
	    pathName = fileObj.getParent();
  	  fileName = fileObj.getName();
    }
    else {
    	pathName = path;
      fileName = file;
    }
  }

  public File getFile() {
  	return fileObj;
  }

	/**
	 *  Document가 수정되었는지 본다.
	 */
  public boolean isModified() {
     return doc.isModified();
  }

  /**
   *  파일이 ReadOnly 인지 본다.
   */
  public boolean isReadOnly() {
   	if (fileObj != null) return !fileObj.canWrite();
    return true;
  }

  public void setRunnableClassName(String name){
    runnableClassName = name;
  }

  public String getRunnableClassName(){
    return runnableClassName;
  }

  public boolean equals(SourceEntry toCompare){
    if(getFullPathName().equals(toCompare.getFullPathName())) return true;
    else return false;
  }

	/**
	 *  Inner class UndoHandler
	 *
	 *  @author Jinwoo Baek
	 */
  class UndoHandler implements UndoableEditListener {
    /**
     * Messaged when the Document has created an edit, the edit is
     * added to <code>undo</code>, an instance of UndoManager.
     */
    public void undoableEditHappened(UndoableEditEvent e) {
      undo.addEdit(e.getEdit());
      undoAction.update();
      redoAction.update();
    }
  }

	/**
	 *  UNDO action
	 */
  public void undo() {
    undoAction.actionPerformed(null);
  }

	/**
	 *  REDO action
	 */
  public void redo() {
    redoAction.actionPerformed(null);
  }

  private UndoAction undoAction = new UndoAction();
  private RedoAction redoAction = new RedoAction();

	/**
	 *  Inner class UndoAction
	 *
	 *  @author Jinwoo Baek
	 */
  class UndoAction extends AbstractAction {
    public UndoAction() {
      super("Undo");
      setEnabled(false);
    }

    public void actionPerformed(ActionEvent e) {
      try {
        if (undo.canUndo()) {
          undo.undo();
          doc.setModified(true);
        }
      } catch (CannotUndoException ex) {
        ex.printStackTrace();
      }
      update();
      redoAction.update();
    }

    protected void update() {
      if(undo.canUndo()) {
        setEnabled(true);
        putValue(Action.NAME, undo.getUndoPresentationName());
      }
      else {
        setEnabled(false);
        putValue(Action.NAME, "Undo");
      }
    }
  }

	/**
	 *  Inner class RedoAction
	 *
	 *  @author Jinwoo Baek
	 */
  class RedoAction extends AbstractAction {
    public RedoAction() {
      super("Redo");
      setEnabled(false);
    }

    public void actionPerformed(ActionEvent e) {
      try {
        if (undo.canRedo()) {
          undo.redo();
          doc.setModified(true);
        }
      } catch (CannotRedoException ex) {
        ex.printStackTrace();
      }
      update();
      undoAction.update();
    }

    protected void update() {
      if(undo.canRedo()) {
        setEnabled(true);
        putValue(Action.NAME, undo.getRedoPresentationName());
      }
      else {
        setEnabled(false);
        putValue(Action.NAME, "Redo");
      }
    }
  }
}
/*
 * $Log: SourceEntry.java,v $
 * Revision 1.18  1999/08/30 09:49:04  remember
 * no message
 *
 * Revision 1.17  1999/08/30 07:59:07  remember
 * no message
 *
 * Revision 1.16  1999/08/30 03:13:07  remember
 * no message
 *
 * Revision 1.15  1999/08/25 11:36:43  multipia
 * 소스 정리
 *
 * Revision 1.14  1999/08/24 07:44:51  remember
 * no message
 *
 * Revision 1.13  1999/08/23 03:43:28  multipia
 * 버그 수정
 *
 * Revision 1.12  1999/08/23 03:41:23  multipia
 * 버그 수정
 *
 * Revision 1.11  1999/08/23 02:54:40  multipia
 * 소스 정리 및 버그 수정
 *
 * Revision 1.10  1999/08/21 05:05:19  multipia
 * 버그 수정
 *
 */
