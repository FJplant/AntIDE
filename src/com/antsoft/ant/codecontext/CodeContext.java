/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/codecontext/CodeContext.java,v 1.105 1999/09/01 07:13:00 kahn Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.105 $
 * Part : Code Context Module
 * Author: Kim, Sung-hoon(kahn@antj.com)
 * Starting at 1999. 1. 20.
 *                   2.  1.
 *                   2.  2.
 *                   2.  3.
 *                   2.  4.
 *                   2.  5.
 *                   2.  6.
 *                   2.  7.
 *                   2.  8.
 *                   2.  9.
 *                   2. 10.
 *                   2. 11.
 *                   3. 10.
 */

package com.antsoft.ant.codecontext;

import com.antsoft.ant.codecontext.codeeditor.*;
import com.antsoft.ant.manager.projectmanager.*;
import com.antsoft.ant.pool.sourcepool.*;
import com.antsoft.ant.pool.librarypool.*;
import com.antsoft.ant.pool.packagepool.*;
import com.antsoft.ant.pool.classpool.*;
import com.antsoft.ant.main.*;
import com.antsoft.ant.util.*;
import com.antsoft.ant.browser.packagebrowser.*;
import com.antsoft.ant.property.JdkInfo;
import com.antsoft.ant.util.QuickSorter;
import com.antsoft.ant.compiler.DepsData;
import com.antsoft.ant.compiler.DepTable;

import java.io.*;
import java.util.*;
import javax.swing.text.*;
import javax.swing.event.*;
import javax.swing.SwingUtilities;

/**
  The module Code Context.

  @author Kim, sung-hoon.
  */
public class CodeContext {
  //////////////////////////////////////////////////////////////////
  // Class Member Field Declaration.

	protected Hashtable totaltabSymbol;  // has table of project symbol table.
	protected Hashtable totaltabImport;  // has table of project import table.

  // the symbol table(it has field & method member.)
  protected Hashtable symtab;		// currently used symbol table.
  protected Hashtable symtabs;	// symbol table list of
																// all opened file(in project).

  // the import list is stored this field.
  protected Hashtable imptab;		// currently used import table.
  protected Hashtable imptabs;	// import table list of
																// all opened file(in project).

  protected Hashtable classtableall = new Hashtable();
  protected Hashtable classtable = null;

  private static Hashtable antiClassTable = null;
  private static Hashtable antiClassTableAll = new Hashtable();

  protected Hashtable openedProject = new Hashtable();

	protected String currentPrj="Files";
	protected String nameOfPackage="dummypack";
																// current package name of the currently
																// activated source code.

	static ClassMemberContainerList containers = new ClassMemberContainerList();
																// class member container list for
																// search class of current project.

  ////////////////////////////////////////////////////////////////////
  // various reference of the object................

	protected static ProjectExplorer projectExplorer;  // ProjectExplorer reference
												// for Conversation to the CodeEditor
												// (that is, ProjectExplorer).

	public static ProjectExplorer getPE() {
  	return projectExplorer;
  }


	protected static SourceEntry se;		// SourceEntry reference.

	protected static JavaDocument currentDocument;
																					// current JavaDocument reference.

  ////////////////////////////////////////////////////////////////////
	// for Listener reference of some event conversation.
	private SourceBrowserEventListener sourceBrowserEventListener;
																			// SourceBrowserEventListener reference.

	private EditFunctionEventListener editFunctionEventListener;	
																			// EditFunctionEventListener reference.

	protected DocListener docListener;	// DocumentListener reference.

  ///////////////////////////////////////////////////////////////////

	private String cachedType = null;

  private boolean isModified = false;

  ///////////////////////////////////////////////////////////////////
  // Constructor.

	/**
	  Constructor.
    */
	public CodeContext() {
   	totaltabSymbol=new Hashtable();
   	totaltabImport=new Hashtable();

    symtabs=new Hashtable();
    imptabs=new Hashtable();

		totaltabSymbol.put("Files",symtabs);
		totaltabImport.put("Files",imptabs);
 	}

  ///////////////////////////////////////////////////////////////////

	protected void setRunnable(String name) {
		se.setRunnableClassName(name);
	}


  //////////////////////////////////////////////////////////////////
  // Project관련 public method declaration...........

	/*
	 * when opening project, the SymbolTable is read in specified table.
	 */
 	public void openPrj(String prj,String path) {
    openedProject.put(path, projectExplorer.getProject());
		int pos = path.indexOf(".apr");
		File f1=new File(path.substring(0,pos) + ".ref");
		File f2=new File(path.substring(0,pos) + ".cor");
		if (f1.exists() && f2.exists()) {
			try {
				FileInputStream istream=new FileInputStream(f1);
				ObjectInputStream stream=new ObjectInputStream(istream);

        classtable=(Hashtable)stream.readObject();

				stream.close();
				istream.close();
			} catch (Exception e) {
				System.out.println("read Object "+e.toString());
        classtable = new Hashtable();
			}

			try {
				FileInputStream istream=new FileInputStream(f2);
				ObjectInputStream stream=new ObjectInputStream(istream);

        antiClassTable=(Hashtable)stream.readObject();

				stream.close();
				istream.close();
			} catch (Exception e) {
				System.out.println("read Object 2"+e.toString());
        classtable = new Hashtable();
			}
		}
   	else {
    	classtable = new Hashtable();

      Vector files = projectExplorer.getProject().getFiles();
      for (int i = 0;i < files.size();++i) {
        ProjectFileEntry entry = (ProjectFileEntry)files.elementAt(i);
        String fileName = entry.getPath() + File.separator + entry.getName();
        File file = new File(fileName);
        if (!file.exists()) continue;

        ClassExtractor extractor = new ClassExtractor(file);
        extractor.doParse();
        String packName = extractor.getPackageName();

        Vector classList = extractor.getClasses();
        if (classList != null) {
          int size = classList.size();
          String fullName = null;
          for (int j = 0;j < size;++j) {
            fullName = packName + "." + (String)classList.elementAt(j);
            classtable.put(fullName, fileName);
          }

          antiClassTable.put(fileName, fullName);
          System.out.println(" filename => "+fileName+" fullName=> "+fullName);
        }
      }
   	}

    symtabs = new Hashtable();
    imptabs = new Hashtable();

		totaltabSymbol.put(prj,symtabs);
		totaltabImport.put(prj,imptabs);
    classtableall.put(prj,classtable);
    antiClassTableAll.put(prj, antiClassTable);
	}

	/*
	 * when tabbing project, the SymbolTable is read in specified table.
	 */
  public void movePrj(String prj, String path) {
    currentPrj = prj;

    classtable=(Hashtable)classtableall.get(prj);
    if (classtable==null) {
      classtable = new Hashtable();
      classtableall.put(prj,classtable);
    }

    antiClassTable = (Hashtable)antiClassTableAll.get(prj);
    if (antiClassTable == null) {
      antiClassTable = new Hashtable();
      antiClassTableAll.put(prj,classtable);
    }

    symtabs=(Hashtable)totaltabSymbol.get(prj);
    imptabs=(Hashtable)totaltabImport.get(prj);
    if (symtabs==null) symtabs=new Hashtable();
    if (imptabs==null) imptabs=new Hashtable();

    containers=new ClassMemberContainerList();
	  Enumeration enum=symtabs.keys();
		while (enum!=null&&enum.hasMoreElements()) {
  	  String key=(String)enum.nextElement();
  		symtab=(Hashtable)symtabs.get(key);
 	  	imptab=(Hashtable)imptabs.get(key);

   		createClassMember();
   	}

    //DepsData.setTable(path);
  }

  /*
   * when adding files, this method is called,
	 * and it parse the files, then makes class table.
   */
  public void addFiles(Vector files) {
    for (int i=0;i<files.size();++i) {
    	String fileName = (String)files.elementAt(i);
      File f = new File(fileName);

      ClassExtractor extractor = new ClassExtractor(f);
      //Parser extractor = new Parser(f);
      //extractor.setDepsFlag(fileName);
      extractor.doParse();
      String packName = extractor.getPackageName();

      Vector classList = extractor.getClasses();
      if (classList != null) {
        int size = classList.size();
        String fullName = null;
        for (int j = 0;j < size;++j) {
          fullName = packName + "." + (String)classList.elementAt(j);
          //System.out.println(" full name ==> "+fullName);
          classtable.put(fullName, fileName);
        }

        antiClassTable.put(fileName, fullName);
      }
    }
  }

	/*
	 * when closing project, the Class Table will be removed.
	 */
	public void closePrj(String prj,String path) {
		if (prj.equals("Files")) {
			return;
		}

    classtableall.remove(prj);
    antiClassTableAll.remove(prj);
    //DepsData.removeTable(path);
	}

	/*
	 * when exit program, the Class Names will be saved in a file.
	 */
  public void exitProgram() {
    Enumeration e = openedProject.keys();
    while (e!=null && e.hasMoreElements()) {
      String path = (String)e.nextElement();
      Project prj = (Project)openedProject.get(path);
      SymbolSave saver = new SymbolSave(path, prj);
      saver.run();
    }
  }

  //////////////////////////////////////////////////////////////////

  public void makeDepsTable() {
  	Vector files = projectExplorer.getProject().getFiles();
    DepsData.deps = new DepTable();

    for (int i = 0;i < files.size();++i) {
      ProjectFileEntry entry = (ProjectFileEntry)files.elementAt(i);
      String fileName = entry.getPath() + File.separator + entry.getName();
      File file = new File(fileName);
      if (!file.exists()) continue;

      Parser extractor = new Parser(file);
      extractor.setDepsFlag(fileName);
      extractor.doParse();
      String packName = extractor.getPackageName();
    }

    files = null;
  }

  //////////////////////////////////////////////////////////////////

  public boolean existUserClass(String cname) {
  	if (classtable.get(cname) == null) return false;
    else return true;
  }

  public String getSourceFileName(String cname) {
  	if (cname == null) return null;

  	return (String)classtable.get(cname.trim());
  }

  public String getRelativeClassFile(String pname) {
  	if (pname == null) return null;

    return ((String)antiClassTable.get(pname.trim())).trim();
  }

  public String getRelativeClassFileName(String pname) {

  	String className = getRelativeClassFile(pname);
    className = className.replace('.',File.separatorChar) + ".class";
    String outputPath = projectExplorer.getProject().getPathModel().getOutputRoot();
    if (outputPath != null)	return outputPath + File.separatorChar + className;
    else return className;
  }

  //////////////////////////////////////////////////////////////////

	/**
	  setting ProjectExplorer reference.

	  @param the ProjectExplorer Ojbect reference.
	  */
	public void setProjectExplorer(ProjectExplorer o) {
    projectExplorer=o;
  }

  //////////////////////////////////////////////////////////////////

  public void doParsing(File f) {
    Parser parser = new Parser(f);
    //Parser.setData(f);
    parser.doParse();
    Hashtable oldsymtab = symtab;
    Hashtable oldimptab = imptab;

    symtab=parser.getTableSymbol();
    imptab=parser.getTableImport();
    String packname = parser.getPackageName();
    createClassMember();

    //String name = f.getName();
    String key = f.getAbsolutePath();
    //System.out.println("table key ==> "+key);

    //key=key.substring(0,key.indexOf(File.separator+name))+name;
    symtabs.put(key,symtab);
    imptabs.put(key,imptab);

    symtab=oldsymtab;
    imptab=oldimptab;
  }

  ////////////////////////////////////////////////////////////////////
  // setting source and parse the file, and make event for source browser

	/**
	  setting current Source Entry reference.

	  @param sourceEntry the SourceEntry Ojbect reference including
						current document reference.
	  */
	public void setSourceEntry(SourceEntry sourceEntry) {
  	//System.out.println(" set source entry !!!");
		final SourceEntry s = sourceEntry;
    SwingUtilities.invokeLater(new Runnable(){
      public void run(){
					setSourceEntryT(s);
      }});
	}

	private void setSourceEntryT(SourceEntry sourceEntry) {
		se=sourceEntry;
		nameOfPackage="dummypack";
		if (docListener!=null)
			currentDocument.removeDocumentListener(docListener);

		currentDocument=(JavaDocument)sourceEntry.getDocument();

		docListener=new DocListener();
    currentDocument.addDocumentListener(docListener);

    // modified by kim sang kyun 1999.08.13
    // files tab에 들어가는 library file일 경우 path가 null이므로
    // getFullPathName()을 키로 사용해야 한다
		String tableKey= se.getFullPathName();
    //System.out.println("table key ==> "+tableKey);

		symtab=(Hashtable)symtabs.get(tableKey);
		if (symtab==null) insertTable(tableKey);
		//if (symtab==null) insertTable(tableKey, true);
		else imptab=(Hashtable)imptabs.get(tableKey);

		//makeSourceBrowserEvent(false);
		makeSourceBrowserEvent();
  }

  ////////////////////////////////////////////////////////////////////

	/**
	  setting current SourceEntry reference.

	  @param the SourceEntry Ojbect reference including current document reference
	  */
	public void setReloadSourceEntry(SourceEntry sourceEntry) {
		final SourceEntry s = sourceEntry;
    SwingUtilities.invokeLater(new Runnable(){
      public void run(){
					setReloadSourceEntryT(s);
      }});
	}

	private void setReloadSourceEntryT(SourceEntry sourceEntry) {
		se=sourceEntry;
		nameOfPackage="dummypack";
		if (docListener!=null) {
			currentDocument.removeDocumentListener(docListener);
		}

		currentDocument=(JavaDocument)sourceEntry.getDocument();

		docListener=new DocListener();
    currentDocument.addDocumentListener(docListener);

		String tableKey=se.getFullPathName();
    //System.out.println("table key ==> "+tableKey);

		insertTable(tableKey);
		//insertTable(tableKey, true);

		makeSourceBrowserEvent();
  }

  ////////////////////////////////////////////////////////////////////

  private void insertTable(String tableKey) {
  	//insertTable(tableKey, false);
  //}

	//private void insertTable(String tableKey, boolean deps) {
		// 만약 기존에 없는 것이라면.... 새로이 생성한다.
    Parser parser = new Parser(currentDocument);
    //if (deps) {
     	//parser.setDepsFlag(se.getFullPathName());
    //}
    parser.doParse();
    symtab=parser.getTableSymbol();
    imptab=parser.getTableImport();

    if (parser.hasMain) {
      String pack = parser.getPackageName();
      if (pack.equals("dummypack")) setRunnable(parser.getMainClass());
      else setRunnable(pack+"."+parser.getMainClass());
    }
    else { setRunnable(null); }

		// 새로 생성한 table을 table list에 put한다.
		symtabs.put(tableKey,symtab);
		imptabs.put(tableKey,imptab);

		createClassMember();
	}

	private void makeSourceBrowserEvent() {
		// 현재 소스의 package name을 찾는다.
		Enumeration e = imptab.keys();
    //System.out.println("event = start");
		while (e!=null && e.hasMoreElements()) {
			String pstring = (String)e.nextElement();
			SymbolTableEntry ptable = (SymbolTableEntry)imptab.get(pstring);
			if (ptable.getMemberSort() == SymbolTableEntry.PACKAGE) {
				nameOfPackage = pstring;
				break;
			}
		}

		// SourceBrowser가 트리형식으로 내용을 보일수 있도록 event를 만든다.
    SourceBrowserEvent evt=new SourceBrowserEvent();
		Vector v = SymbolTableIterator.iterateKey(symtab, null);
		evt.setEvents(v);

		v=SymbolTableIterator.iterateKey(imptab);
		evt.addEvents(v);		// other import lists.

    if (evt!=null) sourceBrowserEventListener.insertEvent(evt);
	}

	/**
	  modify this symbol table and import list table
		when the user commits the save file.
	  */
	//public void modifyTableWhenSaveFile(boolean flag, boolean deps) {
	public void modifyTableWhenSaveFile(boolean flag) {
		nameOfPackage="dummypack";
		String tableKey=se.getFullPathName();

		Hashtable hashtable=(Hashtable)symtabs.get(tableKey);
		if (hashtable!=null) {
			symtabs.remove(tableKey);
			imptabs.remove(tableKey);
		}

    insertTable(tableKey);
		//insertTable(tableKey, deps);

    isModified = false;
    lastInsertOffset = -1;

		//if (flag) makeSourceBrowserEvent(true);
		if (flag) makeSourceBrowserEvent();
	}

	/**
	  * modify this symbol table and import list table
		* when the user commits the save file or this action is needed.
	  */
	synchronized public void modifyTableWhenSaveFile(boolean flag, int count) {
		if (count != threadCount) return;

		nameOfPackage="dummypack";
		String tableKey=se.getPath()+File.separator+se.getName();

		if (count != threadCount) return;

		Hashtable hashtable=(Hashtable)symtabs.get(tableKey);
		if (hashtable!=null) {
			symtabs.remove(tableKey);
			imptabs.remove(tableKey);
		}

		insertTable(tableKey);

		if (count != threadCount) return;

    isModified = false;
    lastInsertOffset = -1;

		if (flag) makeSourceBrowserEvent();
	}

	// class member container list creating method.
	void createClassMember() {
		if (symtabs.size()==0 && imptabs.size()==0) return;

		String currentPackage = "dummypack";
		Enumeration e=imptab.keys();
		while (e!=null&&e.hasMoreElements()) {
			String key=(String)e.nextElement();

			SymbolTableEntry tab=(SymbolTableEntry)imptab.get(key);
			if (tab.getMemberSort()==SymbolTableEntry.PACKAGE) {
				// 현재의 package name을 구한다.
				currentPackage=key;
				break;
			}
		}

		// 최상위 symbol table의 key를 읽는다.
		Vector classes = new Vector();
		Enumeration en = symtab.keys();
		while (en!=null && en.hasMoreElements()) {
			String key = (String)en.nextElement();
			classes.addElement(key);
		}

		// ClassMemberContainer와 ClassMember 객체를 선언하고서...
		ClassMemberContainer memberContainer;
		ClassMember member;

		// 각 class별로 member들을 search하여 class member container에 입력한다.
		for (int i=0;i<classes.size();++i) {
			memberContainer = new ClassMemberContainer();
			SymbolTableEntry tab = (SymbolTableEntry)symtab.get((String)classes.elementAt(i));

			// 하위 table이 있다면, 즉 member에 대한 table이 있다면.
			if (tab.table!=null) {
				Vector content=SymbolTableIterator.iterateKey(tab.table, SymbolTableIterator.TOP);

				// 모든 Member를 iterate하면서 ClassMember를 만들고,
				// 새로이 만든 객체를 ClassMemberContainer에 담는다.
				for (int j=0;j<content.size();++j) {
					// ClassMember들의 key string.

					String key=((EventContent)content.elementAt(j)).getContent();

					// SymbolTable에서 찾아서
          Hashtable htab = tab.table;
					SymbolTableEntry t = (SymbolTableEntry)htab.get(key);

					// Field, inner class or interface, method들만을 취급한다.
					switch (t.getMemberSort()) {
						case SymbolTableEntry.FIELD:
							// ClassMember객체를 만들고 값을 assign하고
							member=new ClassMember();
							member.setName(key);
							member.setType(t.getType());
							member.setAccessType(t.getAccessType());
							member.setMemberType(ClassMember.FIELD);
              member.setStatic(t.isStatic());

							// ClassMemberContainer에 담는다.
							memberContainer.addContainer(member);
							break;

						case SymbolTableEntry.CLASS:
						case SymbolTableEntry.INTERFACE:
							// ClassMember객체를 만들고 값을 assign하고
							member=new ClassMember();
							member.setName(key);
							member.setType(t.getType());
							member.setAccessType(t.getAccessType());
							member.setMemberType(ClassMember.INNERCLASS);

							// ClassMemberContainer에 담는다.
							memberContainer.addContainer(member);
							break;

						case SymbolTableEntry.METHOD:
							// ClassMember객체를 만들고 값을 assign하고
							member=new ClassMember();

							// method의 경우는 parameter를 잘라서
							// 써야하므로 tokenizing을 한다.
							StringTokenizer tokenizer=new StringTokenizer(key,"(,)",false);

							// 최초 token은 method name.
							if (tokenizer.hasMoreTokens())
								member.setName(tokenizer.nextToken());
							while (tokenizer.hasMoreTokens()) {
								// 두번째부터 parameter type들이다.
								member.addParameterType(tokenizer.nextToken());
							}
              Vector temp = t.getParameters();
              if (temp!=null)
                for (int k=0;k<temp.size();++k) {
                  member.addParameter((String)temp.elementAt(k));
                }

							member.setType(t.getType());
							member.setAccessType(t.getAccessType());
							member.setMemberType(ClassMember.METHOD);
              member.setStatic(t.isStatic());

							// ClassMemberContainer에 담는다.
							memberContainer.addContainer(member);
							break;
					}
				}
			}

			// 최종적으로 패키지 이름과 class이름을 합친 string으로 key를 만들어
			String keyOfContainer=currentPackage+"."+classes.elementAt(i);

			containers.putClassMemberList(keyOfContainer,memberContainer);
																		// ClassMemberContainerList에 put한다.
		}
	}

  //////////////////////////////////////////////////////////////////

  /////////////////////////////////////////////////////////////////
  // add listener of this object.

	/**
	  registering the Source Browser Event Listener Object.

	  @param l Source Browser Event Listener Object.
	  */
	public void addSourceBrowserEventListener(SourceBrowserEventListener l) {
		sourceBrowserEventListener = l;
	}

	/**
	  registering the Edit Function Event Listener Object.

	  @param l Edit Function Event Listener Object.
	  */
	public void setEditFunctionEventListener(EditFunctionEventListener l) {
		editFunctionEventListener=l;
	}
  /////////////////////////////////////////////////////////////////////

  /////////////////////////////////////////////////////////////////
  // additional function for shortcut and menu action.

	/**
	 * if F1 key is pressed on the class name,
	 * the context sensitive helper is activated, and web browser is running.
	 *
	 * @param offset the offset position.
	 */
	public void activateContextSensitiveHelp(int offset) {
	  //System.out.println(" helllllllllp....");
		try {
			Element element = currentDocument.getParagraphElement(offset);

			// 이 element 내용들의 시작과 끝을 전체 Document에 상대적인 값으로 구한다.
			int startOffset=element.getStartOffset();
			int endOffset=element.getEndOffset();

			int length=endOffset-startOffset;
			int curchar=offset-startOffset;

			// 현재 변경되는 (추가되는) 곳의 한 라인의 내용을 가져온다.
			String str=currentDocument.getText(startOffset,length);

			LineTokenizer lineTokenizer=new LineTokenizer(str);

			while (lineTokenizer.getCurrentPosition()<curchar
						&&lineTokenizer.getCurrentPosition()<str.length())
                  lineTokenizer.nextToken();

			String thing=lineTokenizer.getTokenString();
      if (thing==null) return;

      offset = se.getLineFromOffset(offset);

			String thingType=SymbolTableIterator.search(symtab, thing, offset);
      if (thingType==null) thingType=thing;

			//System.out.println("the thing type : "+thingType);

			if (thingType!=null) activateSensitiveHelper(thingType);

			Runtime rt=Runtime.getRuntime();
			long wasFree=rt.freeMemory();
			long isFree;
			do {
				rt.gc();
				isFree=rt.freeMemory();
			} while (isFree<wasFree);
		} catch (BadLocationException e) {
			System.out.println(e.toString());
		}
	}

	/**
	  When Selection is occurred in Source Browser panel,
		moves the position for the caret to move.

	  @param str the member(field, method, import list, constructor...)
						 	to be selected.
	  */
	public void selectSourceBrowser(String cls,String str) {
    //System.out.println(" parent : " + cls + " child : "+str);
    Hashtable table = null;

    if (isModified) {
    	isModified = false;
      //modifyTableWhenSaveFile(false, false);
      modifyTableWhenSaveFile(false);
    }

    table = symtab;
    while (cls != null) {
    	System.out.println(" parent ==> "+cls);
    	int pos = cls.indexOf('.');
      if (pos == -1) {
	      SymbolTableEntry ste = (SymbolTableEntry)table.get(cls);
  	    table = ste.table;
        break;	// break while !!!!
      }
      else {
      	String wanted = cls.substring(0, pos);
        System.out.println(" wanted => " + wanted);
	      SymbolTableEntry ste = (SymbolTableEntry)table.get(wanted);
  	    table = ste.table;
        if (table == null) return;

        cls = cls.substring(pos + 1);
      }
    }

    // symbol table에서 먼저 찾아본다.
    int line = SymbolTableIterator.search(table, str);
    if (line != -1) {
     	projectExplorer.moveLine(line);
     	return;
    }

    // import list search....
		if (str.equals(nameOfPackage)) {
      line = SymbolTableIterator.search(imptab, str);
      if (line!=-1) {
        projectExplorer.moveLine(line);
      }
      return;
    }

		if (str.indexOf(".*")!=-1) {
      PackageBrowser pckBrowser =
								new PackageBrowser(null,"Package Browser",false);
			String packstr=str.substring(0,str.indexOf(".*"));
			if (pckBrowser.showPackage(packstr)) {
        pckBrowser.setVisible(true);
        return;
      }
		}
		else {
    	String fileName = (String)classtable.get(str);
      if (fileName == null && extractSource(str)) return;
      else {
      	File f = new File(fileName);
        fileName = f.getName();
        String pathName = f.getAbsolutePath();

        int pos = f.getAbsolutePath().lastIndexOf(File.separator+fileName);
      	projectExplorer.getProjectPanel().setSelectedFileWithExpand(pathName.substring(0, pos), fileName);

				fileName = null;
        pathName = null;
        return;
      }
		}

    line = SymbolTableIterator.search(imptab, str);
    if (line != -1) {
      projectExplorer.moveLine(line);
    }
	}

  //modified by kim sang kyun 1999.08.13
	private boolean extractSource(String str) {
		String temp=str;

    Vector roots = ProjectManager.getCurrentProject().getPathModel().getSourceRootsOfLibs();
    SrcFileExtractor.updateSourePath(roots);
		JavaDocument docu=null;//new JavaDocument();

    for(int i=0; i<roots.size(); i++){
      String root =  (String)roots.elementAt(i);
      //System.out.println(" root path => "+root);
      File f = new File(root);

			if (f.isDirectory()) {		// source가 jar(압축)이 아닐때....
				// 화일을 찾아서 직접 document로 만들어 open한다.
				temp=temp.replace('.',File.separatorChar);

				// 각각이 file로 저장되어 있다.
				String fullFilePath= root + File.separator+temp+".java";

        ProjectFileEntry pfe = projectExplorer.getProject().getFileEntry(str+".java");
        if (pfe != null) {
        	projectExplorer.getProjectPanel().setSelectedFileWithExpand(null, pfe.getName());
          return true;
        }

				File srcFile=new File(fullFilePath);
				if (!srcFile.exists()) continue;
        docu = new JavaDocument();
        FileLoader fl = new FileLoader(srcFile, docu);
        break;
				// document를 진우에게 넘겨서 처리하도록 한다.
			}
			else {
				temp=str.replace('.','/');

        System.out.println(" extract zip => "+temp+".java");

        ProjectFileEntry pfe = projectExplorer.getProject().getFileEntry(str+".java");
        if (pfe != null) {
        	projectExplorer.getProjectPanel().setSelectedFileWithExpand(null, pfe.getName());
          return true;
        }

			  String srcContent=SrcFileExtractor.extract(f.getAbsolutePath(),temp + ".java");
				if (srcContent==null) continue;

        docu = new JavaDocument();
				try {
					docu.insertString(docu.getLength(),srcContent,null);
          break;
				} catch (BadLocationException be) {
          be.printStackTrace();
   				return false;
				}
			}
		}

    //마지막으로 decompile을 한다
    if(docu == null){
      ProjectFileEntry pfe = projectExplorer.getProject().getFileEntry(str+".java");
      if (pfe != null) {
      	projectExplorer.getProjectPanel().setSelectedFileWithExpand(null, pfe.getName());
        return true;
      }

      String srcContent = SrcFileExtractor.extract(str);

      if (srcContent != null){
        docu = new JavaDocument();
        try {
          docu.insertString(docu.getLength(),srcContent,null);
        } catch (BadLocationException be) {
          be.printStackTrace();
          return false;
        }
      }
    }

    if(docu != null) {
      MainFrame.showLibSource(docu,str);
      return true;
    }

    return false;
	}

	/**
	 * if F2 key is pressed on the class name or vairable,
	 * the source viewer is activated.
	 *
	 * @param offset the offset position.
	 */
	public void viewJdkSource(int offset) {
    String thingType = getFullClassNameAtCursor(offset);
    //System.out.println(" thing type ==> "+thingType);
    if (thingType == null) return;

		extractSource(thingType);
	}

	/**
	 * get full class name at cursor position.
	 *
	 * @param offset the offset position.
	 */
	public String getFullClassNameAtCursor(int offset) {
		try {
			Element element=currentDocument.getParagraphElement(offset);

			// 이 element 내용들의 시작과 끝을 전체 Document에 상대적인 값으로 구한다.
			int startOffset=element.getStartOffset();
			int endOffset=element.getEndOffset();

			int length=endOffset-startOffset;
			int curchar=offset-startOffset;

			// 현재 변경되는 (추가되는) 곳의 한 라인의 내용을 가져온다.
			String str=currentDocument.getText(startOffset,length);

			LineTokenizer lineTokenizer = new LineTokenizer(str);

			while (lineTokenizer.getCurrentPosition()<curchar
						&&lineTokenizer.getCurrentPosition()<str.length())
				 lineTokenizer.nextToken();
			String thing = lineTokenizer.getTokenString();
      if (thing==null) return null;

			String classType = SymbolTableIterator.search(symtab, thing, se.getLineFromOffset(offset));
      //System.out.println(" class type ==> "+classType);
			if (classType == null) classType = thing;

			String thingType = searchSourceFullClassName(classType);
      //System.out.println(" class type ==> "+classType+" symtab ==>"+symtab+" thingType => "+thingType);
			if (thingType != null) return thingType;
		} catch (BadLocationException e) {
			System.out.println(e.toString());
		}
		return null;
  }

	// jdkSourceViewer에서 이용되는 method.
	// - source 에 대한 full class name을 search한다.
	private String searchSourceFullClassName(String type) {
		Enumeration enum = imptab.keys();
		while (enum!=null && enum.hasMoreElements()) {
			String impString=(String)enum.nextElement();
      //System.out.println(" import ==> "+impString+"  "+nameOfPackage);

      String fullClassName = null;
			if (impString.equals(nameOfPackage)) {
      	fullClassName = impString + "." + type;
    		if (classtable.get(fullClassName) != null) {
          File f = new File((String)classtable.get(fullClassName));
          String fileName = f.getName();
          String pathName = f.getAbsolutePath();

          int pos = f.getAbsolutePath().lastIndexOf(File.separator+fileName);
          projectExplorer.getProjectPanel().setSelectedFileWithExpand(pathName.substring(0, pos), fileName);

          fileName = null;
          pathName = null;
          return null;
        }
		    if (ClassPool.exist(fullClassName)) return fullClassName;
      }

      int pos=impString.lastIndexOf("*");
      if (pos!=-1) fullClassName=impString.substring(0,pos)+type;
      else {
			  int lastDot=impString.lastIndexOf(".");
				if (!type.equals(impString.substring(lastDot+1,impString.length())))
					continue;
        fullClassName=impString;
			}

      //System.out.println(" full class name ==> "+fullClassName);
      if (classtable.get(fullClassName) != null) {
        File f = new File((String)classtable.get(fullClassName));
        String fileName = f.getName();
        String pathName = f.getAbsolutePath();

        int spos = f.getAbsolutePath().lastIndexOf(File.separator+fileName);
        projectExplorer.getProjectPanel().setSelectedFileWithExpand(pathName.substring(0, spos), fileName);

        fileName = null;
        pathName = null;
        return null;
      }
      if (ClassPool.exist(fullClassName)) return fullClassName;
		}

    //System.out.println("java.lang."+type);
    if (ClassPool.exist("java.lang."+type)) return "java.lang."+type;

    //System.out.println(" return; ");
		return null;
	}

	/*
	 * Ctrl + Space is pressed, do Code Insight of "this" Class.
	 */
	public void thisCodeInsight(int offset) {
    dotOffset=offset-1;
    doCodeCompletion(null, se.getLineFromOffset(offset), 1);
	}

	/**
	 * When indentation button is clicked, the indentation is activated,
	 * and the document object is replaced
	 * with the indented document.
	public void indentDocument() {
		try {
			//String src=currentDocument.getText(0,currentDocument.getLength());
      JavaDocument docu=(JavaDocument)se.getDocument();
      String src=docu.getText(0,docu.getLength());

			JSBeautifier beautifier=new JSBeautifier();

			// Beautifier config setup
			boolean isTab=Main.property.isTab();
			if (isTab) beautifier.setTabIndentation();
			else beautifier.setSpaceIndentation(Main.property.getTabSpaceSize());

			beautifier.setMaxInStatementIndetation(40);
			if (!Main.property.isSwitchIndent()) beautifier.setSwitchIndent(false);
			if (Main.property.isCloseIndent()) beautifier.setBracketIndent(true);

			// Beautifier actioned.
			JavaDocument doc=beautifier.indentation(src);	// second parameter is code style...

			// change the document.
			se.setDocument(doc);
      projectExplorer.notifyChangeDocument();
      setSourceEntry(se);
		} catch (BadLocationException e) {
			System.out.println(e.toString());
		}
	}
	 */


  /////////////////////////////////////////////////////////////////////

  public int getInsertPosition(String name) {
  	if (name == null) return -1;
  	//if (se.isModified()) modifyTableWhenSaveFile(false);
    if (isModified) {
    	isModified = false;
      //modifyTableWhenSaveFile(false, false);
      modifyTableWhenSaveFile(false);
    }

    SymbolTableEntry entry = null;
    Hashtable table = symtab;
    while (name != null) {
	    int pos = name.indexOf('.');
	    if (pos == -1) {
        entry = (SymbolTableEntry)table.get(name);
        break;
      }
      else {
      	String parent = name.substring(0, pos);
        entry = (SymbolTableEntry)table.get(parent);
        if (entry == null || entry.table == null) return -1;

        table = entry.table;
        name = name.substring(pos + 1);
      }
    }

    if (entry == null) return -1;

		Element elem = se.getElementAt(entry.getEndLine());
    if (elem == null) return -1;

    try {
			if (entry.isReallyEnd()) return elem.getStartOffset();
			else return elem.getEndOffset();
    } catch (NullPointerException e) {
    	return se.getDocument().getLength();
    }
  }

  public String getShortTypeOfField(String cls,String field) {
  	if (cls == null) return null;

    SymbolTableEntry entry=(SymbolTableEntry)symtab.get(cls);
    if (entry==null) return null;

    SymbolTableEntry fieldEntry=(SymbolTableEntry)entry.table.get(field);
    if (fieldEntry==null) return null;

    return fieldEntry.getType();
  }

	/////////////////////////////////////////////////////////////////////

  private Vector getSuper(String name,Hashtable tab1,Hashtable tab2) {
		if (tab1 == null || tab2 == null) return null;
		if (classtable == null) return null;
    if (name == null) return null;

    SymbolTableEntry entry = (SymbolTableEntry)tab1.get(name);
    if (entry == null) return null;

    String upper = entry.getSuperClass(); // upper is short name.
    if (upper == null) return null;

    if (upper.indexOf(".") != -1) {
      String candidate = upper;
      upper = candidate.substring(candidate.lastIndexOf(".")+1);

      if (classtable.get(candidate) != null) {
				String file = (String)classtable.get(candidate);
				if (file == null) return null;
        File f = new File(file);
        //String fpath = f.getPath();
        //String fname = f.getName();
        //String key = fpath.substring(0,fpath.indexOf(File.separator+fname)) + fname;
        String key = f.getAbsolutePath();

        if (symtabs.get(key) == null) doParsing(f);

        Vector supers = getSuper(upper, (Hashtable)symtabs.get(key), (Hashtable)imptabs.get(key));
				if (supers == null) supers = new Vector(1,1);
        supers.addElement(candidate);
        return supers;
      }
      else if (ClassPool.exist(candidate)) {
        Vector supers = new Vector(1,1);
        supers.addElement(candidate);
        return supers;
      }
    }

    Enumeration enum = tab2.keys();
    while (enum!=null && enum.hasMoreElements()) {
      String pname = (String)enum.nextElement();
      SymbolTableEntry entry2 = (SymbolTableEntry)tab2.get(pname);
      String candidate = null;
      if (entry2.getMemberSort() == SymbolTableEntry.PACKAGE) {
        candidate = pname + "." + upper;
      }
      else if (pname.indexOf(".*") == -1) {
        if (upper.equals(pname.substring(pname.lastIndexOf(".")+1)))
          candidate = pname;
      }
      else {
        candidate = pname.substring(0, pname.lastIndexOf(".*")) + "." + upper;
      }

			if (candidate == null) continue;

      if (classtable.get(candidate) != null) {
        File f = new File((String)classtable.get(candidate));
        //String fpath = f.getPath();
        //String fname = f.getName();
        //String key = fpath.substring(0,fpath.indexOf(File.separator+fname)) + fname;
        String key = f.getAbsolutePath();

        if (symtabs.get(key) == null) doParsing(f);

        Vector supers = getSuper(upper, (Hashtable)symtabs.get(key), (Hashtable)imptabs.get(key));
				if (supers == null) supers = new Vector(1,1);
        supers.addElement(candidate);
        return supers;
      }
      else if (ClassPool.exist(candidate)) {
        Vector supers = new Vector(1,1);
        supers.addElement(candidate);
        return supers;
      }
    }

		if (ClassPool.exist("java.lang."+upper)) {
			Vector supers = new Vector(1,1);
			supers.addElement("java.lang."+upper);
			return supers;
		}

    return null;
  }

  private Vector getSuperOfInner(String name,Hashtable tab1,Hashtable tab2) {
		if (tab1 == null || tab2 == null) return null;
		if (classtable == null) return null;
    if (name == null) return null;

    Enumeration enum = tab1.elements();
    while (enum != null && enum.hasMoreElements()) {
      SymbolTableEntry temp = (SymbolTableEntry)enum.nextElement();

      if (temp.table == null) continue;

      SymbolTableEntry entry = (SymbolTableEntry)temp.table.get(name);
      if (entry == null) continue;

      String upper = entry.getSuperClass();
      if (upper == null) continue;

      if (upper.indexOf(".") != -1) {
        String candidate = upper;
        upper = candidate.substring(candidate.lastIndexOf(".")+1);

        if (classtable.get(candidate) != null) {
					String file = (String)classtable.get(candidate);
					if (file == null) return null;
        	File f = new File(file);
          //String fpath = f.getPath();
          //String fname = f.getName();
          //String key = fpath.substring(0,fpath.indexOf(File.separator+fname)) + fname;
          String key = f.getAbsolutePath();

          if (symtabs.get(key) == null) doParsing(f);

          Vector supers = getSuper(upper, (Hashtable)symtabs.get(key), (Hashtable)imptabs.get(key));
          supers.addElement(candidate);
          return supers;
        }
        else if (ClassPool.exist(candidate)) {
          Vector supers = new Vector(1,1);
          supers.addElement(candidate);
          return supers;
        }
      }

      Enumeration enum2 = tab2.keys();
      while (enum2 != null && enum2.hasMoreElements()) {
        String pname = (String)enum2.nextElement();
        SymbolTableEntry entry2 = (SymbolTableEntry)tab2.get(pname);
        String candidate = null;
        if (entry2.getMemberSort() == SymbolTableEntry.PACKAGE) {
          candidate = pname + "." + upper;
        }
        else if (pname.indexOf(".*") == -1) {
          if (upper.equals(pname.substring(pname.lastIndexOf(".")+1)))
            candidate = pname;
        }
        else {
          candidate = pname.substring(0, pname.lastIndexOf(".*")) + "." + upper;
        }

				if (candidate == null) continue;

        if (classtable.get(candidate) != null) {
          File f = new File((String)classtable.get(candidate));
          //String fpath = f.getPath();
          //String fname = f.getName();
          //String key = fpath.substring(0,fpath.indexOf(File.separator+fname)) + fname;
          String key = f.getAbsolutePath();

          if (symtabs.get(key) == null) doParsing(f);

          Vector supers = getSuper(upper, (Hashtable)symtabs.get(key), (Hashtable)imptabs.get(key));
          supers.addElement(candidate);
          return supers;
        }
        else if (ClassPool.exist(candidate)) {
          Vector supers = new Vector(1,1);
          supers.addElement(candidate);
          return supers;
        }
      }

			if (ClassPool.exist("java.lang."+upper)) {
				Vector supers = new Vector(1,1);
				supers.addElement("java.lang."+upper);
				return supers;
			}
    }

    return null;
  }

  public Vector getSuperClass(String name, boolean isInner) {
    if (isInner) return getSuperOfInner(name,symtab,imptab);
    else return getSuper(name,symtab,imptab);
  }

	///////////////////////////////////////////////////////////////////////

	public Vector getMethodToOverride(String upper) {
		if (classtable == null) return null;

		String file = (String)classtable.get(upper);
		if (file == null) return null;

		File f = new File(file);
		if (f == null) return null;

    //String fpath = f.getPath();
    //String fname = f.getName();
    //String key = fpath.substring(0,fpath.indexOf(File.separator+fname)) + fname;
    String key = f.getAbsolutePath();

		if (symtabs.get(key) == null) doParsing(f);

		Hashtable tab = (Hashtable)symtabs.get(key);

		String uppershort = upper.substring( upper.lastIndexOf(".") + 1 );
		SymbolTableEntry entry = (SymbolTableEntry)tab.get(uppershort);
		if (entry == null) return null;
		if (entry.table == null) return null;

		tab = entry.table;
		Vector result = null;
		Enumeration e = tab.keys();
		while (e != null && e.hasMoreElements()) {
			key = (String)e.nextElement();
			entry = (SymbolTableEntry)tab.get(key);

			if (entry.getMemberSort() != SymbolTableEntry.METHOD) continue;

			if (entry.getAccessType().equals("private")) continue;

			if (result == null) result = new Vector(1,1);
			result.addElement(entry.toSig(key));
		}

		return result;
	}

  /////////////////////////////////////////////////////////////////////

  /// for Code Completion.

	////////////////////////////////////////////////////////////////////
  public static int timeOut = Main.property.getIntellisenseDelay();
  private static boolean isStatic = false;
	private static boolean keyPressed = false;

 	private static int dotOffset=0;

	///////////////////////////////////////////////////////////////////

 	public static int getDotOffset() {
   	return dotOffset;
 	}

  public static int getOpenOffset(){
    return dotOffset;
  }

	////////////////////////////////////////////////////////////////////

  private void doCodeCompletion(String aLine, int line, int action){
  	if (aLine==null) activateThisIntellisense(line);
    else {
      switch (action) {
        case 1: 
								Thread myThread = new Thread(new InsightThread(this,aLine,line,insightCount));
								myThread.setPriority(Thread.MIN_PRIORITY);
								myThread.start();
								break;
        case 2: activateParameterizing(aLine, line); break;
        //case 3: activateSensitiveHelper(aLine); break;
      }
    }
  }

			//*****************  Intellisense   *********************

  private boolean isCasting = false;

  public void activateIntellisense(String theString,int line,int count) {
		if (count != -1 && count != insightCount) return;
		long s_time = System.currentTimeMillis();

    GetTokenForEvent maker=new GetTokenForEvent(theString);
    String prevToken = maker.getPrevTokenOfDot();
    if (prevToken==null) return;
    if (!Character.isJavaIdentifierStart(prevToken.charAt(0))) return;
		if (count != -1 && count != insightCount) return;

    isCasting = maker.isCasting();

    Hashtable event=getEvent(prevToken,line);
    if (event==null) return;
		if (count != -1 && count != insightCount) return;

    EditFunctionEvent e = new EditFunctionEvent();
    e.setEventType(EditFunctionEvent.INTELLISENSE);

		Enumeration e2 = event.elements();
		if (e2 == null) return;
		Vector newEvent = new Vector();
		while (e2.hasMoreElements())
	  newEvent.addElement(e2.nextElement());

    e.setEvents(getSortedList(newEvent,true));
		if (count != -1 && count != insightCount) return;

    s_time =System.currentTimeMillis() - s_time;

    if (timeOut > (int)s_time) {
     	try {
     		Thread.sleep(timeOut-(int)s_time);
   	  } catch (InterruptedException except) {
    		System.out.println("in intellisenseAction, Exception Occurred :"+except.toString());
     	}
    }

		// if another key is pressed before showing popup window,
		// give up code insight.
		if (count != -1 && count != insightCount) return;
    if (e!=null) editFunctionEventListener.showEventList(e);
  }

	private Hashtable getEvent(String tokenToBeEvaluated,int line) {
		String evaluatedType=tokenizingAndEvaluating(tokenToBeEvaluated,line);
		if (evaluatedType==null) return null;

    //System.out.println(" evaluated type is "+ evaluatedType);

    cachedType = evaluatedType; // caching the previous code
																// insight class full type name.

		return makeEvent(evaluatedType,line);
	}

	private int count=0;	// the subscription count.

	private String tokenizingAndEvaluating(String origin,int line) {
		String type=null;
    //System.out.println(" origin "+origin);

		do {
			// token to be made by tokenizer.
			String[] result=tokenizing(origin,'.',line);
      if (result==null) return null;

			String token=result[0];
			origin=result[1];

      //System.out.println(" token => "+token+ " origin "+origin);

			// for array variable
			count=0;
			for (int i=0;i<token.length();++i) if (token.charAt(i)=='[') count++;
			if (count!=0) token=token.substring(0,token.indexOf("["));

			// converted token by parameter to type converter.
			token=paramToType(token,line);
      if (token==null) return null;

			// finally, evaluated type remains.
			if (type==null) {
      	type=evaluating(token,line);
      	//System.out.println(" if "+type);
      }
			else {
      	type=evaluating(type,token);
        //System.out.println(" else "+type);
      }

			//System.out.println(" type is ==> "+type);

			if (type==null) return null;

			if (type.charAt(0)=='[') {
				if (count==0) type="array";
				else {
					if (type.length()>2) type=type.substring(2,type.length()-1);
					else return null;
				}
			}
		} while (origin!=null);
		return type;
	}

	private String paramToType(String src,int offset) {
		int pos=src.indexOf("(");
		if (pos==-1) return src.toString();	// it is not method, type or variable.

		String body=src.substring(0,pos);
		String parameter=src.substring(pos+1,src.length()-1);

		if (parameter==null||parameter.length()==0)
			return src.toString();	// this is method, but has no parameter.

		// method and has a or some parameter(s).
		StringBuffer buf=new StringBuffer();
		String token=null;
		do {
			String[] result=tokenizing(parameter,',',offset);
			token=result[0];
			parameter=result[1];

			String type=tokenizingAndEvaluating(token,offset);
      if (type==null) return null;
   		if (type.lastIndexOf(".")!=-1) 
				type = type.substring(type.lastIndexOf(".")+1,type.length());
			buf.append(type+",");
		} while (parameter!=null);

		return body+"("+buf.toString().substring(0,buf.length()-1)+")";
	}

	private String[] tokenizing(String src,char delim,int offset) {
		String[] result=new String[2];
    //System.out.println("src is "+src);

 		int i=0;
  	for (int depth = 0;i<src.length();++i) {
  	 	char ch=src.charAt(i);
	  	if (ch=='(') depth++;
  		else if (ch==')') depth--;
  		else if (ch==delim && depth==0) break;
	 	}

		if (i == src.length()) {	// if the src string has no DOT.
			result[0]=src.toString();
			result[1]=null;

			return result;
		}

		result[0]=src.substring(0,i);
		result[1]=src.substring(i+1,src.length());

		return result;
	}

	private String evaluating(String value,int offset) {
    String typeOfToken=null;

	  typeOfToken = SymbolTableIterator.search(symtab, value, offset);
    //System.out.println(" type of token ==> "+typeOfToken);
		if (typeOfToken==null) {
      String upper = null;
      String temp=SymbolTableIterator.getThisClassName(symtab, offset);//anyway
      if (temp != null) {
        SymbolTableEntry entry=(SymbolTableEntry)symtab.get(temp);
        upper = entry.getSuperClass();
        if (upper != null) {
          typeOfToken = evaluating(upper,value);
          if (typeOfToken==null) {
            typeOfToken=value;
            isStatic=true;
          }
        }
      }
    }
    else {
      // TO DO : Static 인지 아닌지 ... 결정을 해야 한다.
      if (value.equals(typeOfToken)) isStatic = true;
      else isStatic=false;
      //isStatic = false;
    }
    //}

		int cnt=0;
		for (int i=0;i<typeOfToken.length();++i)
			if (typeOfToken.charAt(i)=='[') cnt++;
		if ((cnt-count)>0) { return "array"; }

		if (cnt>0) typeOfToken=typeOfToken.substring(0,typeOfToken.indexOf("["));

		String result=searchFullClassName(typeOfToken);
		if (result!=null) return result;
		else {
			if (typeOfToken.indexOf("int")!=-1) return typeOfToken;
			if (typeOfToken.indexOf("char")!=-1) return typeOfToken;
			if (typeOfToken.indexOf("short")!=-1) return typeOfToken;
			if (typeOfToken.indexOf("long")!=-1) return typeOfToken;
			if (typeOfToken.indexOf("boolean")!=-1) return typeOfToken;
			if (typeOfToken.indexOf("float")!=-1) return typeOfToken;
			if (typeOfToken.indexOf("double")!=-1) return typeOfToken;
		}

		return null;
	}

	private String searchInProject(Hashtable table,String type) {
		if (table == null) table = imptab;

    Enumeration e = table.keys();
    ClassMemberContainer cmc;
    String packname;
    while (e!=null&&e.hasMoreElements()) {
      packname=(String)e.nextElement();
			//System.out.println(" package name : "+packname);
      int pos=packname.lastIndexOf(".*");
      if (pos!=-1) packname=packname.substring(0,pos)+"."+type;
      else {
				int lastDot=packname.lastIndexOf(".");
				if (!type.equals(packname.substring(lastDot+1,packname.length()))) continue;
			}
     
  		// package에서 현재 type이 존재하는지 검사.
	  	cmc=(ClassMemberContainer)containers.getClassMemberList(packname);
		  if (cmc!=null) return packname;

      String filename = (String)classtable.get(packname);

      if (filename!=null) {
        File f = new File(filename);
        doParsing(f);
        return packname;
      }
    }

    packname=nameOfPackage+"."+type;
  	cmc=(ClassMemberContainer)containers.getClassMemberList(packname);
    if (cmc!=null) return packname;

    String file = (String)classtable.get(packname);
    if (file!=null) {
      File f = new File(file);
      doParsing(f);
      return packname;
    }

    return null;
	}

	private String searchFullClassName(String type) {
		String string = searchInProject(null,type);
		if (string != null) return string;

		// search in JDK api.
		return searchInJDK(null,type);
	}

	private String searchNameInIt(Hashtable table,String type) {
		String string = searchInProject(table,type);
		//System.out.println(" string : "+string);
		if (string!=null) return string;

		// search in JDK api.
		string = searchInJDK(table,type);
		//System.out.println(" string2: "+string);
		if (string != null) return string;

		return type;
	}

	public String searchInJDK(Hashtable table,String type) {
    if (type==null) return null;

		if (table == null) table = imptab;

		// package에 없으면 import list의 것을 차례로 검사한다.
		Enumeration enu=table.keys();
		while (enu!=null&&enu.hasMoreElements()) {
			String impString=(String)enu.nextElement();
			SymbolTableEntry entry = (SymbolTableEntry)table.get(impString);

			//if (impString.equals(nameOfPackage)) continue;
			if (entry.getMemberSort() == SymbolTableEntry.PACKAGE) continue;

      String fullClassName=null;
      int pos=impString.lastIndexOf("*");
      if (pos!=-1) fullClassName=impString.substring(0,pos)+type;
      else {
				int lastDot=impString.lastIndexOf(".");
				if (!type.equals(impString.substring(lastDot+1,impString.length()))) continue;
       	fullClassName=impString;
			}

      //System.out.println(" full class name "+fullClassName);
      if (ClassPool.exist(fullClassName)) return fullClassName;
		}

    // the java.lang package is imported with default.
    if (ClassPool.exist("java.lang."+type)) return "java.lang."+type;
		if (type.indexOf(".")!=-1) return searchFullClassName(type.substring(type.indexOf(".")+1,type.length()));
		return null;
	}

	private String evaluating(String type,String token) {
  	if (type == null || token == null) return null;
    isStatic=false;
		int flag=type.indexOf(".");
		if (flag==-1) type=searchFullClassName(type);

		Vector bundle;
  	ClassMemberContainer cmc=(ClassMemberContainer)containers.getClassMemberList(type);
    if (cmc!=null) {
			bundle=cmc.getContainer();

			for (int i=0;i<bundle.size();++i) {
				ClassMember member=(ClassMember)bundle.elementAt(i);
				if (token.equals(member.getFullName())) {
					String newType = member.getType();
					//System.out.println(" new type => "+newType);
					if (newType.indexOf(".") == -1) {
						String key = (String)classtable.get(type);
						//System.out.println(" key is "+key);
						if (key != null) {
							Hashtable it = (Hashtable)imptabs.get(key);
							if (it != null) 
								newType = searchNameInIt(it,newType);
						}
					}
					return newType;
				}
			}

      String name = type.substring( type.lastIndexOf(".") + 1 , type.length());
      Enumeration e = symtabs.elements();
      while (e != null && e.hasMoreElements()) {
        Hashtable t = (Hashtable)e.nextElement();
        SymbolTableEntry entry = (SymbolTableEntry)t.get(name);
        if (entry == null) return null;
        type = entry.getSuperClass();
        if (type != null) return evaluating(type,token);
      }
		}
		else {
   	  if (token.indexOf("(")!=-1) {  // 최종의 type을 결정할 겻이, method의 형태일 때
			  bundle=ClassPool.getMethodSignatures(type);

			  for (int i=0;bundle!=null&&i<bundle.size();++i) {
				  SigModel m=(SigModel)bundle.elementAt(i);

          // formal parameter가 Object type일 경우, actual parameter가 무엇이든 match시켜 준다.
				  if (token.equals(m.toString())) return m.getTypeFullName();
        	  if (token.indexOf("(")==token.indexOf(")")-1) continue;
        	  String temp=token.substring(0,token.indexOf("("));
          	if (temp.equals(m.getName())) {
          		Class[] paramTypes=m.getParameterTypes();
          		for (int j=0;j<paramTypes.length;++j)
           			if (paramTypes[j].getName().equals("java.lang.Object"))	return m.getTypeFullName();
       	  	}
    		}
		  }
		  else {
 			  bundle=ClassPool.getFieldSignatures(type);

			  for (int i=0;bundle!=null&&i<bundle.size();++i) {
				  SigModel m=(SigModel)bundle.elementAt(i);
				  if (token.equals(m.toString())) return m.getTypeFullName();
			  }
		  }
		}	// outside else

		return null;
	}

	private Hashtable makeEvent(String type, int line) {
		//System.out.println("make Event ");
		Hashtable result=new Hashtable();

		if (type.equals("array") || type.indexOf("[]") != -1) {
			result.put("length:int", new InsightEvent(InsightEvent.MEMBER, "length:int",true));
			return result;
		}

		int flag=type.indexOf(".");
    //System.out.println(" 1st type ==> "+type);
		if (flag==-1) type = searchFullClassName(type);
    //System.out.println(" 2nd type ==> "+type);
   	if (type==null) return null;

    String packname = SymbolTableIterator.getThisPackageName(imptab);
		if (packname != null && type.startsWith(packname)) {
    	if (type.endsWith(SymbolTableIterator.getThisClassName(symtab,line))) flag = 2;//outer
      else flag = 1;
    }
    else flag = 0;
    //System.out.println(" type ==> "+type+" flag is "+flag);

    if (isCasting) isStatic = false;

		Vector bundle;
		ClassMemberContainer cmc
							= (ClassMemberContainer)containers.getClassMemberList(type);
    if (cmc != null) {
			bundle=cmc.getContainer();

			for (int i=0;bundle!=null&&i<bundle.size();++i) {
				ClassMember m=(ClassMember)bundle.elementAt(i);
				if (m.getMemberType()==ClassMember.METHOD) {
        	if (flag == 2
          		|| (flag == 1 && !m.getAccessType().equals("private"))
          		|| (flag == 0 && m.getAccessType().equals("public"))) {
          	if (!isStatic || (isStatic && m.isStatic())) {
    				  String kkey = m.getSig();
	    			  result.put(kkey,new InsightEvent(InsightEvent.METHOD, kkey, true));
            }
          }
				}
			}

			for (int i=0;bundle!=null&&i<bundle.size();++i) {
				ClassMember m=(ClassMember)bundle.elementAt(i);
				if (m.getMemberType()==ClassMember.FIELD) {
        	if (flag == 2
          		|| (flag == 1 && !m.getAccessType().equals("private"))
          		|| (flag == 0 && m.getAccessType().equals("public"))) {
          	if (!isStatic || (isStatic && m.isStatic())) {
    				  String kkey = m.getSig();
	    			  result.put(kkey,new InsightEvent(InsightEvent.MEMBER, kkey, true));
            }
          }
				}
			}

      if (flag == 2 && !isStatic) {
      	String thisClassName = SymbolTableIterator.getThisClassName(symtab, line);
        SymbolTableEntry entry = (SymbolTableEntry)symtab.get(thisClassName);
        if (entry.table != null) {
		      boolean isInner = SymbolTableIterator.isInInnerClass(entry.table, line);

  	    	if (isInner) {
    	      Vector innerVector = SymbolTableIterator.getInnerMember(entry.table, line);
      	    int size = innerVector.size();
        	  for (int i=0; i<size; ++i) {
          		EventContent ec = (EventContent)innerVector.elementAt(i);
            	String kkey = ec.getContent();
	            if (ec.getContentType() == EventContent.ATTR)
  	          	result.put(kkey, new InsightEvent(InsightEvent.MEMBER, kkey, true));
    	        else if (ec.getContentType() == EventContent.OPER) {
      	      	result.put(kkey, new InsightEvent(InsightEvent.METHOD, kkey, true));
              }
        	  }
	        }
        }
      }

      /*
			for (int i=0;bundle!=null&&i<bundle.size();++i) {
				ClassMember m=(ClassMember)bundle.elementAt(i);
				if (m.getMemberType()==ClassMember.INNERCLASS) {
        	if (!isStatic) {
  				  String kkey = m.getSig();
	  			  result.put(kkey,new InsightEvent(InsightEvent.INNERCLASS, kkey, true));
          }
				}
			}
      */

      String name=type.substring(type.lastIndexOf(".")+1,type.length());
      Enumeration e = symtabs.keys();
      while (e!=null&&e.hasMoreElements()) {
      	String keys = (String)e.nextElement();
        Hashtable t=(Hashtable)symtabs.get(keys);
        SymbolTableEntry entry=(SymbolTableEntry)t.get(name);
        if (entry == null) continue;
        type = entry.getSuperClass();
        if (type != null) {
        	Hashtable table = (Hashtable)imptabs.get(keys);
        	if (table != null) {
          	String fullName = searchNameInIt(table, type);
            if (fullName != null) type = fullName;
          }
          Hashtable temp = makeEvent(type, line);
          if (temp!=null) {
	  				Enumeration e2 = temp.elements();
		  			if (e2 != null)
			  		while (e2.hasMoreElements()) {
				  	  InsightEvent eve = (InsightEvent)e2.nextElement();
					  	eve.setIsMine(false);
		    		  result.put(eve.toString(),eve);
  					}
          }
        }
        break;
      }
		}
    // jdk에서 내용을 뽑을때
		else {
      if (isStatic) bundle=ClassPool.getStaticMethodSignatures(type);
			else bundle=ClassPool.getMethodSignatures(type);
			for (int i=0;bundle!=null&&i<bundle.size();++i) {
				SigModel m=(SigModel)bundle.elementAt(i);
        String msig=new String(m.getName()+":"+m.getTypeShortName());
				  result.put(msig,new InsightEvent(InsightEvent.METHOD, msig,m.isMine()));
			}

      if (isStatic) bundle=ClassPool.getStaticFieldSignatures(type);
			else bundle=ClassPool.getFieldSignatures(type);
			for (int i=0;bundle!=null&&i<bundle.size();++i) {
				SigModel m=(SigModel)bundle.elementAt(i);
        String msig=new String(m.getName()+":"+m.getTypeShortName());
				  result.put(msig,new InsightEvent(InsightEvent.MEMBER, msig,m.isMine()));
			}
		}

		if (result.size()==0) return null;
		return result;
	}
			//*****************  Intellisense   *********************



			//*****************  Parameterizing   *********************

  private boolean activateParameterizing(String theString, int offset) {
    GetTokenForEvent maker=new GetTokenForEvent(theString);
    String prevToken=maker.getPrevTokenOfLeftParen();
    if (prevToken==null) return false;

    if (!Character.isJavaIdentifierStart(prevToken.charAt(0))) return false;

		Hashtable eventString=null;

    int pos = prevToken.lastIndexOf('.');
		if (pos != -1) {
	  	String methodName = prevToken.substring(pos + 1 , prevToken.length());
	  	String sub = prevToken.substring(0, pos);

      String type=tokenizingAndEvaluating(sub, offset);

      if (type != null) eventString = makeEventForParam(type,methodName);
		}
		else {// 거의가 Constructor의 parameterizing이므로 super에 대해서는 생각안함
			// "this" keyword is replaced to the original class name.
			if (prevToken.equals("this")) {
				prevToken = SymbolTableIterator.getThisClassName(symtab, offset); // anyway
				if (prevToken==null) return false;
			}
      else if (prevToken.equals("super")) {
				prevToken = SymbolTableIterator.getSuperClassName(symtab, offset);
       	if (prevToken==null) return false;
      }

			Vector loaded = SymbolTableIterator.searchAllOverloadedMethod(symtab, prevToken, offset);

			if (loaded == null) {  // not in project, so will find it in the library.
				String typeOfMethod = searchFullClassName(prevToken);
				if (typeOfMethod != null) {	// Constructor.
         	boolean found = false;
         	if (Main.isParamLoadEnd&&Main.paramHash.size()!=0) {
         		Object o=Main.paramHash.get(typeOfMethod+"."+prevToken);
         		if (o!=null) {
         			if (o instanceof String) {
           			if (eventString==null) eventString=new Hashtable();
             			eventString.put((String)o,new InsightEvent((String)o));
           		}
           		else {
							  Vector o2 = (Vector)o;
							  int size = o2.size();
								if (eventString == null) eventString = new Hashtable();
								for (int i=0;i<size;++i)
                  eventString.put((String)o2.elementAt(i),new InsightEvent((String)o2.elementAt(i)));
              }

              found = true;
            }
            else found = false;
          }

          if (!found) {
            Vector bundle=null;
            bundle=ClassPool.getConstructorSignatures(typeOfMethod);

            for (int i=0;bundle!=null&&i<bundle.size();++i) {
              SigModel m=(SigModel)bundle.elementAt(i);
              String mstr=m.getName();
              if (prevToken.equals(mstr.substring(mstr.lastIndexOf(".")+1,mstr.length()))) {
                if (eventString==null) eventString = new Hashtable();
                String param=m.toString();
                int x=param.indexOf("(");
                int y=param.indexOf(")",x);
                if (y==x+1) eventString.put("<no parameters>",new InsightEvent("<no parameters>"));
                else {
                  param=param.substring(x+1,y);
                  StringBuffer buff=new StringBuffer();
                  StringTokenizer st=new StringTokenizer(param,",");
                  int count=0;
                  while (st!=null&&st.hasMoreTokens()) {
                    buff.append((String)st.nextToken());
                    buff.append(" x"+count+",");
                    count++;
                  }
                  param=buff.toString();
									String key = param.substring(0,param.length()-1);
                  eventString.put(key,new InsightEvent(key));
                }
              }
		  		  }
				  }
      	}
        else {
		  		String sub = SymbolTableIterator.getThisClassName(symtab, offset);// outer
		      String type = tokenizingAndEvaluating(sub,offset);
    		  if (type != null) eventString = makeEventForParam(type,prevToken);
        }
      }
			else {
			  int size = loaded.size();
				if (eventString == null) eventString = new Hashtable();
				for (int i=0; i < size; ++i) {
				  String s = (String)loaded.elementAt(i);
				  eventString.put(s,new InsightEvent(s));
				}
			}
		}

		if (eventString == null) return false;

    EditFunctionEvent e=new EditFunctionEvent();
    e.setEventType(EditFunctionEvent.PARAMETERING);
		Enumeration enumer = eventString.elements();
		if (enumer==null) return false;
		while (enumer.hasMoreElements()) e.addEvent((InsightEvent)enumer.nextElement());
		e.setEvents(getSortedList(e.getEvents(), true));

    if (e!=null) editFunctionEventListener.showEventList(e);

    return true;
  }


  private Hashtable makeEventForParam(String type, String methodName) {
    Hashtable eventString = null;
    int flag = type.indexOf(".");
    if (flag == -1) type = searchFullClassName(type);
    if (type == null) return null;

    Vector bundle;
    ClassMemberContainer cmc = (ClassMemberContainer)containers.getClassMemberList(type);
    if (cmc != null) {
      bundle=cmc.getContainer();

      for (int i=0;bundle!=null&&i<bundle.size();++i) {
        ClassMember m=(ClassMember)bundle.elementAt(i);
        if (m.getMemberType()==ClassMember.METHOD) {
          if (methodName.equals(m.getName())) {
          	//if (m.getAccessType().equals("public")) {
              if (eventString==null) eventString=new Hashtable();

              if (m.getParamsAsString()==null)
			  			  eventString.put("<no parameters>",new InsightEvent("<no parameters>"));
              else
						    eventString.put(m.getParamsAsString(),new InsightEvent(m.getParamsAsString()));
            //}
          }
        }
      }

      String name=type.substring(type.lastIndexOf(".")+1,type.length());
      Enumeration e = symtabs.keys();
      while (e!=null&&e.hasMoreElements()) {
      	String keys = (String)e.nextElement();
        Hashtable t = (Hashtable)symtabs.get(keys);
        SymbolTableEntry entry=(SymbolTableEntry)t.get(name);
        if (entry!=null) {
          type=entry.getSuperClass();
          if (type!=null) {
          	Hashtable table = (Hashtable)imptabs.get(keys);
            if (table != null) {
            	String fullName = searchNameInIt(table, type);
              if (fullName != null) type = fullName;
            }
            Hashtable temp=makeEventForParam(type,methodName);
            if (temp!=null) {
              if (eventString==null) eventString=new Hashtable();
              //for (int i=0;i<temp.size();++i)
                //if (!eventString.contains((String)temp.elementAt(i)))
							Enumeration e2 = temp.keys();
							if (e2!=null)
							while (e2.hasMoreElements()) {
							  String s = (String)e2.nextElement();
								eventString.put(s,temp.get(s));
							}
            }
          }
          break;
        }  // if (entry!=null)
      }
    }
    else {  // in jdk library
      boolean Found=false;
      String clsType=type;

      if(Main.isParamLoadEnd&&Main.paramHash.size()!=0) {
        while (!Found) {
          Object o=Main.paramHash.get(clsType+"."+methodName);
          if (o!=null) {
            if (o instanceof String) {
              if (eventString==null) eventString=new Hashtable();
              eventString.put((String)o,new InsightEvent((String)o));
            }
            else {
							Vector o2 = (Vector)o;
							int size = o2.size();
              if (eventString == null) eventString = new Hashtable();
							for (int i=0;i<size;++i) {
                String s = (String)o2.elementAt(i);
                eventString.put(s,new InsightEvent(s));
              }
            }
            Found=true;
            break;
          }
          if (clsType.equals("java.lang.Object")) break;
          clsType=ClassPool.getSuperClass(clsType);
        }
      }

      if (!Found) {
        bundle=ClassPool.getMethodSignatures(type);
        for (int i=0;bundle!=null&&i<bundle.size();++i) {
          SigModel m=(SigModel)bundle.elementAt(i);
          if (methodName.equals(m.getName())) {
            if (eventString==null) eventString=new Hashtable();

            String param=m.toString();
            int x=param.indexOf("(");
            int y=param.indexOf(")",x);
            if (y==x+1) eventString.put("<no parameters>",new InsightEvent("<no parameters>"));
            else {
              param=param.substring(x+1,y);
              StringBuffer buff=new StringBuffer();
              StringTokenizer st=new StringTokenizer(param,",");
              int count=0;
              while (st!=null&&st.hasMoreTokens()) {
                buff.append((String)st.nextToken());
                buff.append(" x"+count+",");
                count++;
              }
              param=buff.toString();
							String key = param.substring(0,param.length()-1);
              eventString.put(key,new InsightEvent(key));
            }
          }
        }
        bundle=ClassPool.getConstructorSignatures(type);
        for (int i=0;bundle!=null&&i<bundle.size();++i) {
          SigModel m=(SigModel)bundle.elementAt(i);
          if (methodName.equals(m.getName())) {
            if (eventString==null) eventString=new Hashtable();

            String param=m.toString();
            int x=param.indexOf("(");
            int y=param.indexOf(")",x);
            if (y==x+1) eventString.put("<no parameters>",new InsightEvent("<no parameters>"));
            else {
              param=param.substring(x+1,y);
              StringBuffer buff=new StringBuffer();
              StringTokenizer st=new StringTokenizer(param,",");
              int count=0;
              while (st!=null&&st.hasMoreTokens()) {
                buff.append((String)st.nextToken());
                buff.append(" x"+count+",");
                count++;
              }
              param=buff.toString();
							String key = param.substring(param.length()-1);
              eventString.put(key,new InsightEvent(key));
            }
          }
        }
      }  // if (!found);
    } // outer else

    return eventString;
  }

	public Vector getParametersWithCachedType(String method) {
		dotOffset += method.length();
		Hashtable table = makeEventForParam(cachedType,method);
		Vector result = new Vector();

		Enumeration e = table.elements();
		while (e != null && e.hasMoreElements()) {
			InsightEvent evt = (InsightEvent)e.nextElement();
			result.addElement(evt);
		}

		return result;
	}

			//*****************  Parameterizing   *********************


			//*****************  sensitive help   *********************

  private static JavaDocViewer docViewer = null;

	private void activateSensitiveHelper(String _class) {
		String name = searchInJDK(null,_class);
		if (name == null) return;

		// full path name of the html help(document) file.
    JdkInfo jdkInfo = ProjectManager.getCurrentProject().getPathModel().getCurrentJdkInfo();
    if (jdkInfo == null) return;
		String docRootDir=jdkInfo.getDocPath();
		if (docRootDir==null) return;
		String exeArgv=docRootDir+File.separator+name+".html";

		// check that the html help file exist actually.
		File f=new File(exeArgv);
		if (!f.exists()) {
			name=name.replace('.',File.separatorChar);

			// full path name of the html help(document) file.
			exeArgv=docRootDir+File.separator+name+".html";

			if (exeArgv==null) return;

 			// check that the html help file exist actually.
  		f=new File(exeArgv);
			if (!f.exists()) return;
		}

    /** 내부 JavaDoc Viewer를 사용한다면 */
    if(Main.property.isInternalHelpViewerUse()){
      final String name2 = name;
      final String docRootDir2 = docRootDir;

        javax.swing.SwingUtilities.invokeLater(new Runnable(){
        public void run(){
          if(docViewer == null){
            docViewer = JavaDocViewer.createJavaDocViewer(docRootDir2, name2+".html");
            docViewer.setBounds(20, 20, 800, 600);
            docViewer.setVisible(true);
          }
          else{
            docViewer.setDocFile(docRootDir2, name2+".html");
            docViewer.setVisible(true);
          }}
        });
    }
    /** 일반 웹브라우저를 사용한다면 */
    else{
  		// full path name of the web browser(i.e. iexplore.exe ...)
	  	String exeFile=Main.property.getWebBrowserPath();
		  if (exeFile==null) return;

  		String[] command=new String[2];
	  	command[0]=exeFile;
  		command[1]=exeArgv;

      try {
          Runtime runtime=Runtime.getRuntime();
		      if (runtime!=null) runtime.exec(command);
      } catch (IOException e) {}
  	}
  }

			//*****************  sensitive help   *********************

  // this intellisense method.
  // Ctrl+Space를 눌렀을 경우, this의 Code Insight와 같은 행동을 한다.
  private void activateThisIntellisense(int line) {
    // intellisense의 event를 만들고 action을 취한다.
    EditFunctionEvent e=new EditFunctionEvent();
    e.setEventType(EditFunctionEvent.INTELLISENSE);

    // previous token을 evaluation하여 type을 생산하고 해당 type의 event를 return한다.
    Hashtable event=getEvent("this",line);

    Vector localVar=SymbolTableIterator.getLocalVarEvent(symtab, line);
    if (event == null && localVar == null) return;

    if (event == null) event = new Hashtable();
    if (localVar != null) {
      for (int i=0; i<localVar.size(); ++i) {
			  String s = (String)localVar.elementAt(i);
	    	event.put(s,new InsightEvent(InsightEvent.LOCAL,s));
			}
    }

    Vector newEvent = new Vector();
		Enumeration e2 = event.elements();
		if (e2 == null) return;
		while (e2.hasMoreElements()) newEvent.addElement(e2.nextElement());
		e.setEvents(getSortedList(newEvent,true));

    if (e!=null) editFunctionEventListener.showEventList(e);
  }
  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

 	private Vector getSortedList(Vector v) {
      return QuickSorter.sort(v, QuickSorter.LESS_STRING);
 	}

 	private Vector getSortedList(Vector v, boolean ic) {
      return QuickSorter.sort(v, QuickSorter.LESS_STRING,ic);
 	}

  ////////////////////////////////////////////////////////////////
  private void doImportCompletion(String packageName) {
		long s_time = System.currentTimeMillis();
		keyPressed = false;

	  Hashtable result = new Hashtable();
    Vector gets = PackagePool.getImportDatas(packageName);
    if (gets != null) {
		  int size = gets.size();
			for (int i = 0; i<size; ++i) {
			  InsightEvent ie = (InsightEvent)gets.elementAt(i);
			  result.put(ie.toString(),ie);
			}
		}

    boolean isClassLevel = false;
    Enumeration e = classtable.keys();
    if (e != null)
    while (e.hasMoreElements()) {
      String key = (String)e.nextElement();
      if (packageName == "") {
			  String str = key.substring(0,key.indexOf("."));
				if (!str.equals("dummypack"))
	      	result.put(str,new InsightEvent(InsightEvent.PACKAGE,str));
      }
      else {
        if (key.indexOf(packageName) == 0) {
          int pos = packageName.length()+1;
          int pos2 = key.indexOf(".",pos);
          if (pos2 == -1) {
            isClassLevel = true;
						String str = key.substring(pos)+";";
						result.put(str,new InsightEvent(InsightEvent.CLASS,str));
          }
          else {
					  String str = key.substring(pos,pos2);
		    		result.put(str,new InsightEvent(InsightEvent.PACKAGE,str));
          }
        }
      }
    }

    if (result.size() == 0) return;

    if (isClassLevel) result.put("*;",new InsightEvent(InsightEvent.ALL,"*;"));

		s_time = System.currentTimeMillis() - s_time;
		try {
			if (timeOut > (int)s_time) {
				Thread.sleep(timeOut-(int)s_time);
			}
		} catch (InterruptedException ex) {
		}

		if (keyPressed) return;

    EditFunctionEvent evt = new EditFunctionEvent();
    evt.setEventType(EditFunctionEvent.IMPORTING);

		Enumeration enumer = result.elements();
		if (enumer == null) return;
		while (enumer.hasMoreElements()) 
			evt.addEvent((InsightEvent)enumer.nextElement());
		evt.setEvents(getSortedList(evt.getEvents(), true));

    if (evt!=null) editFunctionEventListener.showEventList(evt);
  }

	//////////////////////////////////////////////////////////////////////

	//////////////////////////////////////////////////////////////////////

  private void doPackageCompletion(String packageName) {
		long s_time = System.currentTimeMillis();
		keyPressed = false;

	  Hashtable result = new Hashtable();

    Enumeration e = classtable.keys();
    if (e != null)
    while (e.hasMoreElements()) {
      String key = (String)e.nextElement();
      if (packageName == "") {
			  String str = key.substring(0,key.indexOf("."));
				if (!str.equals("dummypack"))
	      	result.put(str,new InsightEvent(InsightEvent.PACKAGE,str));
      }
      else {
        if (key.indexOf(packageName) == 0) {
          int pos = packageName.length()+1;
          int pos2 = key.indexOf(".",pos);
					if (pos2 != -1) {
					  String str = key.substring(pos,pos2);
		    		result.put(str,new InsightEvent(InsightEvent.PACKAGE,str));
          }
        }
      }
    }

    if (result.size() == 0) return;

		s_time = System.currentTimeMillis() - s_time;
		try {
			if (timeOut > (int)s_time)
				Thread.sleep(timeOut - (int)s_time);
		} catch (InterruptedException ex) {
		}

		if (keyPressed) return;

    EditFunctionEvent evt = new EditFunctionEvent();
    evt.setEventType(EditFunctionEvent.IMPORTING);

		Enumeration enumer = result.elements();
		if (enumer == null) return;
		while (enumer.hasMoreElements()) 
			evt.addEvent((InsightEvent)enumer.nextElement());
		evt.setEvents(getSortedList(evt.getEvents(), true));

    if (evt!=null) editFunctionEventListener.showEventList(evt);
  }

	//////////////////////////////////////////////////////////////////////

	//////////////////////////////////////////////////////////////////////

  synchronized private void insertConstructor(int offset,String inserted) {
    final int offsetF = offset;
    final String insertedF = inserted;
    SwingUtilities.invokeLater(new Runnable(){
      public void run(){
        try {
          currentDocument.insertString(offsetF,insertedF+" ");
					projectExplorer.getCurrentTvp().getEditor().setSelectionStart(offsetF);
					projectExplorer.getCurrentTvp().getEditor().setSelectionEnd(offsetF+insertedF.length());
        } catch (BadLocationException e) {
        }
      }});
  }

  private void doConstructorCompletion(String string, int line) {
		long s_time = System.currentTimeMillis();
		keyPressed = false;

    int len = string.length();
    if (len != 0) {
      if (string.charAt(len-1) == '=') {
        //LineTokenizer lt = new LineTokenizer(string);
        Lexer lexer = new Lexer(string);
        String candidate = null;
        int type = -1;
        while ((type = lexer.nextToken()) != Sym.EOF)
        	if (type == Sym.ID) candidate = lexer.getValue();

        //modifyTableWhenSaveFile(false, false);
	      modifyTableWhenSaveFile(false);

        candidate = SymbolTableIterator.search(symtab, candidate, line);

				if (candidate == null) return;
				insertConstructor(dotOffset+1,candidate);
        return;
      }
    }

		Hashtable constructors = getConstructors();

		s_time = System.currentTimeMillis() - s_time;
		try {
			if (timeOut > (int)s_time) Thread.sleep(timeOut - (int)s_time);
		} catch (InterruptedException ex) {
		}

		if (keyPressed) return;

    EditFunctionEvent evt = new EditFunctionEvent();
    evt.setEventType(EditFunctionEvent.NEWING);
		Enumeration enumer = constructors.elements();
		if (enumer == null) return;
		while (enumer.hasMoreElements())
			evt.addEvent((InsightEvent)enumer.nextElement());
		evt.setEvents(getSortedList(evt.getEvents(), true));
    if (evt!=null) editFunctionEventListener.showEventList(evt);
  }

	private Hashtable getConstructors() {
    Hashtable constructors = new Hashtable();
    Enumeration e = imptab.keys();
		if (e!=null)
    while (e.hasMoreElements()) {
      String name = (String)e.nextElement();
      SymbolTableEntry entry = (SymbolTableEntry)imptab.get(name);
      if (entry.getMemberSort() == SymbolTableEntry.PACKAGE) {
        Enumeration e2 = classtable.keys();
				if (e2 != null)
        while (e2.hasMoreElements()) {
          String candidate = (String)e2.nextElement();
          int pos = candidate.lastIndexOf(".");
          if (name.equals(candidate.substring(0,pos))) {
					  String key = candidate.substring(pos+1)+":"+name;
            constructors.put(key,new InsightEvent(InsightEvent.CLASS,key,true));
					}
        }
      }
      else {
        int pos = name.lastIndexOf(".*");
        if (pos == -1) {
				  pos = name.lastIndexOf(".");
				  String key = name.substring(pos+1)+":"+name.substring(0,pos);
          if (classtable.get(name) != null)
  					constructors.put(key,new InsightEvent(InsightEvent.CLASS,key,true));
          else
  					constructors.put(key,new InsightEvent(InsightEvent.CLASS,key,false));
				}
        else {
          String candidate = name.substring(0, pos);
          Vector ret = PackagePool.getClassesFromPackageForNew(candidate);

          if (ret != null) {
					  int size = ret.size();
						for (int i=0;i<size;++i) {
              String list = (String)ret.elementAt(i);
							String key = list+":"+candidate;
              constructors.put(key,new InsightEvent(InsightEvent.CLASS,key,false));
						}
					}
          else if (!currentPrj.equals("Files")) {
            Enumeration e2 = classtable.keys();
            while (e2 != null && e2.hasMoreElements()) {
              String list = (String)e2.nextElement();
              int pos2 = list.lastIndexOf(".");
              if (candidate.equals(list.substring(0,pos2))) {
							  String key = list.substring(pos+1)+":"+candidate;
                constructors.put(key,new InsightEvent(InsightEvent.CLASS,key,true));
							}
            }
          }
        }
      }
    }

    Vector ret = PackagePool.getClassesFromPackage("java.lang");
    if (ret != null) {
			int size = ret.size();
			for (int i=0;i<size;++i) {
        String list = (String)ret.elementAt(i);
				String key = list+":java.lang";
        constructors.put(key,new InsightEvent(InsightEvent.CLASS,key,false));
			}
		}

		return constructors;
	}

	public void doConstructorCompletionNoNew(int offset) {
		dotOffset = offset - 1; 
		doConstructorCompletion("",0);
	}

  /////////////////////////////////////////////////////////////////////

	////////////////////////////////////////////////////////////////////////
	// exception class insight
  private void doExceptionCompletion() {
		long s_time = System.currentTimeMillis();
		keyPressed = false;

    Hashtable exceptions = new Hashtable();
    Enumeration e = imptab.keys();

		if (e!=null)
    while (e.hasMoreElements()) {
      String name = (String)e.nextElement();
      SymbolTableEntry entry = (SymbolTableEntry)imptab.get(name);

      if (entry.getMemberSort() == SymbolTableEntry.PACKAGE) {
        Enumeration e2 = classtable.keys();

				if (e2 != null)
        while (e2.hasMoreElements()) {
          String candidate = (String)e2.nextElement();
          int pos = candidate.lastIndexOf(".");
          if (name.equals(candidate.substring(0,pos))) {
					  String key = candidate.substring(pos+1);
						if (key.indexOf("Exception") != -1) {
							key = key+":"+name;
            	exceptions.put(key,new InsightEvent(InsightEvent.CLASS,key,true));
						}
					}
        }
      }

      else {
        int pos = name.lastIndexOf(".*");

        if (pos == -1) {
				  pos = name.lastIndexOf(".");
					if (name.indexOf("Exception") != -1) {
				  	String key = name.substring(pos+1)+":"+name.substring(0,pos);
          	if (classtable.get(name) != null)
  						exceptions.put(key,new InsightEvent(InsightEvent.CLASS,key,true));
          	else
  						exceptions.put(key,new InsightEvent(InsightEvent.CLASS,key,false));
					}
				}

        else {
          String candidate = name.substring(0, pos);
          Vector ret = PackagePool.getExceptionClassesFromPackage(candidate);

          if (ret != null) {
					  int size = ret.size();
						for (int i=0;i<size;++i) {
              String list = (String)ret.elementAt(i);
							String key = list+":"+candidate;
              exceptions.put(key,new InsightEvent(InsightEvent.CLASS,key,false));
						}
					}

          else if (!currentPrj.equals("Files")) {
            Enumeration e2 = classtable.keys();
            while (e2 != null && e2.hasMoreElements()) {
              String list = (String)e2.nextElement();
              int pos2 = list.lastIndexOf(".");
              if (candidate.equals(list.substring(0,pos2))) {
							  String key = list.substring(pos+1);
								if (key.indexOf("Exception") != -1) {
								  key = key + ":" + candidate;
                	exceptions.put(key,new InsightEvent(InsightEvent.CLASS,key,true));
								}
							}
            }
          }
        }
      }
    }

    Vector ret = PackagePool.getExceptionClassesFromPackage("java.lang");
    if (ret != null) {
			int size = ret.size();
			for (int i=0;i<size;++i) {
        String list = (String)ret.elementAt(i);
				String key = list+":java.lang";
        exceptions.put(key,new InsightEvent(InsightEvent.CLASS,key,false));
			}
		}

		s_time = System.currentTimeMillis() - s_time;
		try {
			if (timeOut > (int)s_time) Thread.sleep(timeOut - (int)s_time);
		} catch (InterruptedException ex) {
		}

		if (keyPressed) return;

    EditFunctionEvent evt = new EditFunctionEvent();
    evt.setEventType(EditFunctionEvent.NEWING);
		Enumeration enumer = exceptions.elements();
		if (enumer == null) return;
		while (enumer.hasMoreElements())
			evt.addEvent((InsightEvent)enumer.nextElement());
		evt.setEvents(getSortedList(evt.getEvents(), true));
    if (evt!=null) editFunctionEventListener.showEventList(evt);
  }

	////////////////////////////////////////////////////////////////////////
	// implemented interface insight

  private void doImplementsCompletion() {
		long s_time = System.currentTimeMillis();
		keyPressed = false;

    Hashtable interfaces = new Hashtable();
    Enumeration e = imptab.keys();

		if (e!=null)
    while (e.hasMoreElements()) {
      String name = (String)e.nextElement();
      SymbolTableEntry entry = (SymbolTableEntry)imptab.get(name);

      if (entry.getMemberSort() == SymbolTableEntry.PACKAGE) continue;

      else {
        int pos = name.lastIndexOf(".*");

        if (pos == -1) {
				  pos = name.lastIndexOf(".");

				  try {
						if (ClassPool.isInterface(name)) {
					  	String key = name.substring(pos+1)+":"+name.substring(0,pos);
  							interfaces.put(key,new InsightEvent(InsightEvent.INTERFACE,key));
						}
					}catch (ClassNotFoundException e2) {
					}
				}

        else {
          String candidate = name.substring(0, pos);
          Vector ret = PackagePool.getInterfaceNamesFromPackage(candidate);

          if (ret != null) {
					  int size = ret.size();
						for (int i=0;i<size;++i) {
              String list = (String)ret.elementAt(i);
							String key = list+":"+candidate;
              interfaces.put(key,new InsightEvent(InsightEvent.INTERFACE,key));
						}
					}
        }
      }
    }

    Vector ret = PackagePool.getInterfaceNamesFromPackage("java.lang");
    if (ret != null) {
			int size = ret.size();
			for (int i=0;i<size;++i) {
        String list = (String)ret.elementAt(i);
				String key = list+":java.lang";
        interfaces.put(key,new InsightEvent(InsightEvent.INTERFACE,key,false));
			}
		}

		s_time = System.currentTimeMillis() - s_time;
		try {
			if (timeOut > (int)s_time) Thread.sleep(timeOut - (int)s_time);
		} catch (InterruptedException ex) {
		}

		if (keyPressed) return;

    EditFunctionEvent evt = new EditFunctionEvent();
    evt.setEventType(EditFunctionEvent.IMPLEMENTS);
		Enumeration enumer = interfaces.elements();
		if (enumer == null) return;
		while (enumer.hasMoreElements())
			evt.addEvent((InsightEvent)enumer.nextElement());
		evt.setEvents(getSortedList(evt.getEvents(), true));
    if (evt!=null) editFunctionEventListener.showEventList(evt);
	}

	////////////////////////////////////////////////////////////////////

	private int threadCount = 0;

	private void runMyThread(boolean flag) {
		threadCount++;

		if (threadCount > 10000) threadCount = 0;
		Thread myThread = new Thread(new MyThread(this,threadCount,flag));
		myThread.setPriority(Thread.MIN_PRIORITY);
		myThread.start();
	}


  ////////////////////////////////////////////////////////////////////
  // Document Listener............

	private int insightCount = 0;
	private int lastInsertOffset = -1;

  /**
    The inner class DocListener implements the Document interface,
    and when the DocumentEvent occurs, corresponding action is made.
    */
  class DocListener implements DocumentListener/*,UndoableEditListener*/ {
	  public void insertUpdate(DocumentEvent evt) {
    	isModified = true;
			if (evt.getLength() > 10) runMyThread(true);

			boolean anyInserted = false;
      keyPressed = true;
			insightCount++;
			if (insightCount > 10000) insightCount = 0;

      try {
        int offset=evt.getOffset();    // 현재 삽입되는 곳의 offset을 구한다.

			  Element elem=currentDocument.getParagraphElement(offset);

        int startOffset=elem.getStartOffset();
   	    int endOffset=elem.getEndOffset();
       	int length=endOffset-startOffset;

				if (startOffset <= lastInsertOffset &&
						endOffset >= lastInsertOffset) anyInserted = true;

				lastInsertOffset = offset;

        String str=currentDocument.getText(startOffset,length);
        int localOffset=offset-startOffset;

        char c=str.charAt(localOffset);

        int curLine = se.getLineFromOffset(offset);
				int curDepth = SymbolTableIterator.getDepth(symtab, curLine);

        //System.out.println(" current depth ==> "+curDepth);

        dotOffset=offset;

				// 1. member Insight
        if (c=='.') {
					//System.out.println(" inserted ==> .....");
         	if (!Main.property.isIntelliOn()) return;
          if (localOffset == 0) return;
          char prev = str.charAt(localOffset-1);
          if (prev == '.' || prev == '/' || Character.isWhitespace(prev))
						return;

          //modifyTableWhenSaveFile(false, false);
					//int curDepth = SymbolTableIterator.getDepth(symtab, curLine);
          if (curDepth >= 1) {
					//if (str.indexOf("import") == -1) {
						// Main Code Insight & Completion.
						final String string = str.substring(0,localOffset);
						final int cur = curLine;
            //System.out.println(" string==> "+string);
          	//modifyTableWhenSaveFile(false, false);

    				SwingUtilities.invokeLater(new Runnable(){
      				public void run(){
								//System.out.println(" String 2 ==> "+string);
            		doCodeCompletion(string,cur,1);
      				}});
          }

					// import Insight.
          else if (curDepth == 0) {
					//else {
					  if (str.indexOf("import ") != -1) {
            	int pos = str.lastIndexOf(" ");
							final String string = str.substring(pos+1,localOffset);
    					SwingUtilities.invokeLater(new Runnable(){
      					public void run(){
            			doImportCompletion(string);
      					}});
						}
						else if (str.indexOf("package ") != -1) {
            	int pos = str.lastIndexOf(" ");
							final String string = str.substring(pos+1,localOffset);
    					SwingUtilities.invokeLater(new Runnable(){
      					public void run(){
            			doPackageCompletion(string);
      					}});
						}
						else return;
          }
				}

				// 2. parameter insight.
        else if (c=='(') {
          //openOffset=offset;
        	doCodeCompletion(str.substring(0,localOffset),curLine,2);
				}

				// 3. other insight.
        else if (c == ' ') {
					str = str.substring(0,localOffset).trim();
					//System.out.println("string is => "+str);
					int posi = -1;

					// new Insight.
					if (str.endsWith("new")) {
					  if (curDepth == 0) return;
						posi = str.lastIndexOf("new");

						if (posi > 0) {
							char prevChar = str.charAt(posi-1);
							if (Character.isJavaIdentifierPart(prevChar)) return;
						}

						final String data = str.substring(0,posi).trim();
						final int cur = curLine;
    				SwingUtilities.invokeLater(new Runnable(){
      				public void run(){
           			doConstructorCompletion(data, cur);
      				}});
					}

					// import Insight.
					else if (str.endsWith("import")) {
						posi = str.lastIndexOf("import");

						if (posi > 0) {
							char prevChar = str.charAt(posi-1);
							//if (Character.isJavaIdentifierPart(prevChar)) return;
							if (!Character.isWhitespace(prevChar)) return;
						}

						//if (posi == 0) {
    				SwingUtilities.invokeLater(new Runnable(){
      				public void run(){
								doImportCompletion("");
      				}});
						//}
					}

					else if (str.endsWith("package")) {
						posi = str.lastIndexOf("package");

						if (posi > 0)
							if (!Character.isWhitespace(str.charAt(posi - 1))) return;

						SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								doPackageCompletion("");
							}});
					}

					// extends Insight.
					else if (str.endsWith("extends")) {
					  posi = str.lastIndexOf("extends");
					  //if (curDepth > 1) return;

						if (posi > 0 && !Character.isWhitespace(str.charAt(posi - 1))) return;
						//if (str.length() > posi+7 && str.charAt(posi+7) != ' ') return;

						// 실제 Class 또는 Interface의 선언 부분인가 확인한다.
						if (str.indexOf("class ")==-1 && str.indexOf("interface ")==-1) {
							elem = se.getElementAt(curLine-1);

       				startOffset=elem.getStartOffset();
 	    				endOffset=elem.getEndOffset();
							str = se.getDocument().getText(startOffset,endOffset-startOffset);
							if (str.indexOf("class ")==-1 && str.indexOf("interface ")==-1)
								return;
						}

						final int cur = curLine;
    				SwingUtilities.invokeLater(new Runnable(){
      				public void run(){
								doConstructorCompletion("",cur);
      				}});
					}

					// implements insight.
					else if (str.endsWith("implements")) {
						posi = str.indexOf("implements");
						//if (curDepth > 1) return;

						if (posi > 0 && !Character.isWhitespace(str.charAt(posi - 1))) return;

						// 실제 Class 또는 Interface의 선언 부분인가 확인한다.
						if (str.indexOf("class ")==-1) {
							elem = se.getElementAt(curLine-1);

       				startOffset=elem.getStartOffset();
 	    				endOffset=elem.getEndOffset();
							str = se.getDocument().getText(startOffset,endOffset-startOffset);
							if (str.indexOf("class ")==-1)
								return;
						}

						//final int cur = curLine;
    				SwingUtilities.invokeLater(new Runnable(){
      				public void run(){
								doImplementsCompletion();
      				}});
					}

					// throws Insight.
					else if (str.endsWith("throws")) {
					  posi = str.lastIndexOf("throws");

						if (posi > 0 && !Character.isWhitespace(str.charAt(posi - 1))) return;
						if (str.length() > posi+7 && !Character.isWhitespace(str.charAt(posi+7))) return;

    				SwingUtilities.invokeLater(new Runnable(){
      				public void run(){
					  		doExceptionCompletion();
      				}});
					}

					/*
					// statement completion.
					if (curDepth > 1) {
				  	posi = str.indexOf("try ");
						if (posi != -1) {
					  	StringBuffer buf = new StringBuffer("");// make indent.
							for (int i=0;i<posi;++i) {
						 		char cha = str.charAt(i);
						  	if (Character.isWhitespace(cha)) buf.append(cha);
							}
							String blk = buf.toString();						// make indent.

             	insertDocument(offset+1,"{\n"+blk+"} catch {\n"+blk+"}\n");
						}
					}
					*/

				}	// end of else if (c == ' ')

				// statement completion.
				/*
				else if (c == '{') {
					int posi = str.indexOf("try{");
					if (posi != -1 &&
								!Character.isJavaIdentifierPart(str.charAt(posi-1))) {
				  	StringBuffer buf = new StringBuffer("");// make indent.
						for (int i=0;i<posi;++i) {
					 		char cha = str.charAt(i);
					  	if (Character.isWhitespace(cha)) buf.append(cha);
						}
						String blk = buf.toString();						// make indent.

           	insertDocument(offset+1,"\n"+blk+"} catch {\n"+blk+"}\n");
					}
				}
				*/

				// otherwise.....
       	else if (c=='\n') {
					if (str.trim().equals("")) {
          	SymbolTableIterator.modifyLineCountInTable(symtab, curLine);
          	SymbolTableIterator.modifyLineCountInTable(imptab, curLine);
            isModified = false;
          }
					else {
						if (anyInserted) {
	            runMyThread(true);
              //System.out.println(" parsing................ ");
            }
						else {
            	SymbolTableIterator.modifyLineCountInTable(symtab, curLine);
            	SymbolTableIterator.modifyLineCountInTable(imptab, curLine);
              isModified = false;
            }
					}
        }

        // implements Insight.
        else if (c == ',') {
          int posi;
        	if ((posi = str.indexOf("implements ")) != -1) {

						if (posi > 0)
							if (Character.isWhitespace(str.charAt(posi-1))) return;

	    			SwingUtilities.invokeLater(new Runnable(){
  	    			public void run(){
								doImplementsCompletion();
      				}});
          }
          else if ((posi = str.indexOf("throws ")) != -1) {
						if (posi > 0)
							if (Character.isWhitespace(str.charAt(posi-1))) return;

    				SwingUtilities.invokeLater(new Runnable(){
      				public void run(){
					  		doExceptionCompletion();
      				}});
          }
        }

        else if (c == '{') {
        	anyInserted = false;
          runMyThread(true);
        }

        else if (c == '}') {
        	anyInserted = false;
          runMyThread(true);
        }
      } catch (BadLocationException e) {
        System.out.println("exception.... in the insertUpdate().");
      }
		}

		public void changedUpdate(DocumentEvent evt) {
			System.out.println(" change update !!!");
		}

		public void removeUpdate(DocumentEvent evt) {
			if (evt.getLength() > 10) runMyThread(true);
      else isModified = true;
		}
  }
  ///////////////////////////////////////////////////////////////////////////
}
