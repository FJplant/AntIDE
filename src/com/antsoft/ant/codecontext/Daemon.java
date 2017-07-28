/*
 * $Header: /AntIDE/source/ant/codecontext/Daemon.java 23    99-06-21 3:57p Kahn $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 23 $
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

import java.util.*;
import com.antsoft.ant.codecontext.codeeditor.*;
import com.antsoft.ant.pool.classpool.ClassPool;

/**
  @author Kim, Sung-hoon.
  */
public class Daemon extends Thread {
	protected static int depth=0;

	protected static String tableKey=null;
	protected static String nameOfPackage=null;

	protected static Hashtable symtabs=null;
	protected static Hashtable imptabs=null;

	protected Hashtable symtab=null;
	protected Hashtable imptab=null;

	protected ClassMemberContainerList containers=null;

  protected CodeContext codeContext=null;

	public Daemon(CodeContext o) {
    codeContext=o;
	}

	public static void setDepth(int d) {
		depth=d;
	}

  /////////////////////////////////////////////////////////////////////////////////////////////////////////

	private void modifyTableWhenSaveFile(boolean flag) {
		nameOfPackage="dummypack";
		//String tableKey=se.getPath()+se.getName();

		Hashtable hashtable=(Hashtable)symtabs.get(tableKey);
		if (hashtable!=null) {
			symtabs.remove(tableKey);
			imptabs.remove(tableKey);
		}

		insertTable(tableKey);

		if (flag) makeSourceBrowserEvent(flag);
	}

	private void insertTable(String tableKey) {

		// 만약 기존에 없는 것이라면.... 새로이 생성한다.
    //TableCreator tableCreator=new TableCreator(CodeContext.getCurrentDocument());
    Parser parser = new Parser(CodeContext.getCurrentDocument());
    parser.doParse();
    symtab=parser.getTableSymbol();
    imptab=parser.getTableImport();

    if (parser.hasMain) {
      String pack = parser.getPackageName();
      if (pack.equals("dummypack")) codeContext.setRunnable(parser.getMainClass());
      else codeContext.setRunnable(pack+"."+parser.getMainClass());
   	}
   	else {
   		codeContext.setRunnable(null);
   	}

		// 새로 생성한 table을 table list에 put한다.
		symtabs.put(tableKey,symtab);
		imptabs.put(tableKey,imptab);

		createClassMember();
	}

	private void makeSourceBrowserEvent(boolean flag) {
		// 현재 소스의 package name을 찾는다.
		Enumeration e=imptab.keys();
		while (e!=null&&e.hasMoreElements()) {
			String pstring=(String)e.nextElement();
			SymbolTableEntry ptable=(SymbolTableEntry)imptab.get(pstring);
			if (ptable.getMemberSort()==SymbolTableEntry.PACKAGE) {
				nameOfPackage=pstring;
				break;
			}
		}

		// SourceBrowser가 트리형식으로 내용을 보일수 있도록 event를 만든다.
    SourceBrowserEvent evt=new SourceBrowserEvent();

		//System.out.println("setSourceEntry========");

		// Symbol table을 iterate해서 event로 만들어 온다.
		SymbolTableIterator iter=new SymbolTableIterator(symtab);
		Vector v=iter.iterateKey(SymbolTableIterator.MEMBER);
		evt.setEvents(v);

		//evt.alignment();

		// import table을 iterate해서 event로 만들어 온다.
		iter=new SymbolTableIterator(imptab);
		v=iter.iterateKey();
		evt.addEvents(v);		// other import lists.

		Vector sourceBrowserEventListener=CodeContext.getSourceBrowserEventListener();
   	for (int i=0;i<sourceBrowserEventListener.size();++i) {
      SourceBrowserEventListener l=(SourceBrowserEventListener)sourceBrowserEventListener.elementAt(i);
      if (flag) l.clearEvent();
      if (evt!=null) l.insertEvent(evt);
    }
	}

  /////////////////////////////////////////////////////////////////////////////////////////////////////////

	// class member container list creating method.
	void createClassMember() {
		if (symtabs.size()==0&&imptabs.size()==0) return;

		String currentPackage="dummypack";
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
		Vector classes=new Vector();
		Enumeration en=symtab.keys();
		while (en!=null&&en.hasMoreElements()) {
			String key=(String)en.nextElement();
			classes.addElement(key);
		}

		// ClassMemberContainer와 ClassMember 객체를 선언하고서...
		ClassMemberContainer memberContainer;
		ClassMember member;

		// 각 class별로 member들을 search하여 class member container에 입력한다.
		for (int i=0;i<classes.size();++i) {
			memberContainer=new ClassMemberContainer();
			SymbolTableEntry tab=(SymbolTableEntry)symtab.get((String)classes.elementAt(i));

			// 하위 table이 있다면, 즉 member에 대한 table이 있다면.
			if (tab.table!=null) {
				SymbolTableIterator iter=new SymbolTableIterator(tab.table);
				Vector content=iter.iterateKey(SymbolTableIterator.TOP);

				// 모든 Member를 iterate하면서 ClassMember를 만들고,
				// 새로이 만든 객체를 ClassMemberContainer에 담는다.
				for (int j=0;j<content.size();++j) {
					// ClassMember들의 key string.

					String key=((EventContent)content.elementAt(j)).getContent();

					// SymbolTable에서 찾아서
                    Hashtable htab=tab.table;
					SymbolTableEntry t=(SymbolTableEntry)htab.get(key);

					// Field, inner class or interface, method들만을 취급한다.
					switch (t.getMemberSort()) {
						case SymbolTableEntry.FIELD:
							// ClassMember객체를 만들고 값을 assign하고
							member=new ClassMember();
							member.setName(key);
							member.setType(t.getType());
							member.setAccessType(t.getAccessType());
							member.setMemberType(ClassMember.FIELD);

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

							// method의 경우는 parameter를 잘라서 써야하므로 tokenizing을 한다.
							StringTokenizer tokenizer=new StringTokenizer(key,"(,)",false);

							// 최초 token은 method name.
							if (tokenizer.hasMoreTokens()) member.setName(tokenizer.nextToken());
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

							// ClassMemberContainer에 담는다.
							memberContainer.addContainer(member);
							break;
					}
				}
			}

			// 최종적으로 패키지 이름과 class이름을 합친 string으로 key를 만들어
			String keyOfContainer=currentPackage+"."+classes.elementAt(i);

			// ClassMemberContainerList에 put한다.
			containers.putClassMemberList(keyOfContainer,memberContainer);
		}
	}

  /////////////////////////////////////////////////////////////////////////////////////////////////////////

	public static void setTableKey(String key) {
		tableKey=key;
	}

	public static void setTable(Hashtable s,Hashtable i) {
		symtabs=s;
		imptabs=i;
	}

	public static void setPackageName(String pack) {
		nameOfPackage=pack;
	}

	public static boolean insertUpdateFlag=false;

  /////////////////////////////////////////////////////////////////////////////////////////////////////////

	public void run() {
    /*while (true) {
      try {
        Thread.sleep(1000);
      } catch (InterruptedException except) {
        while (true) {
          try {
            insertUpdateFlag = false;
            Thread.sleep(300);
            if (insertUpdateFlag) continue;

    	  		containers=CodeContext.getClassMemberContainerList();

		 	    	if (depth<2) modifyTableWhenSaveFile(true);
		 		    else modifyTableWhenSaveFile(false);

            codeContext.setImpTab(imptab);
            codeContext.setSymTab(symtab);
	          break;
          } catch (InterruptedException e2) {
            break;
          }
        }
      }
    }*/
		suspend();

		while (true) {
			try {
				insertUpdateFlag=false;
				Thread.sleep(300);
				if (insertUpdateFlag) continue;

				containers=CodeContext.getClassMemberContainerList();

		 		if (depth<2) modifyTableWhenSaveFile(true);
		 		else modifyTableWhenSaveFile(false);
        //modifyTableWhenSaveFile(false);

        codeContext.setSymTab(symtab);
        codeContext.setImpTab(imptab);

				suspend();
			} catch (InterruptedException e) {
        System.out.println("interruptedd..............................");
				suspend();
			}
		}
	}
}
