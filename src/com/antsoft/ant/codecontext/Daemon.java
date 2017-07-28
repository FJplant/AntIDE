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

		// ���� ������ ���� ���̶��.... ������ �����Ѵ�.
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

		// ���� ������ table�� table list�� put�Ѵ�.
		symtabs.put(tableKey,symtab);
		imptabs.put(tableKey,imptab);

		createClassMember();
	}

	private void makeSourceBrowserEvent(boolean flag) {
		// ���� �ҽ��� package name�� ã�´�.
		Enumeration e=imptab.keys();
		while (e!=null&&e.hasMoreElements()) {
			String pstring=(String)e.nextElement();
			SymbolTableEntry ptable=(SymbolTableEntry)imptab.get(pstring);
			if (ptable.getMemberSort()==SymbolTableEntry.PACKAGE) {
				nameOfPackage=pstring;
				break;
			}
		}

		// SourceBrowser�� Ʈ���������� ������ ���ϼ� �ֵ��� event�� �����.
    SourceBrowserEvent evt=new SourceBrowserEvent();

		//System.out.println("setSourceEntry========");

		// Symbol table�� iterate�ؼ� event�� ����� �´�.
		SymbolTableIterator iter=new SymbolTableIterator(symtab);
		Vector v=iter.iterateKey(SymbolTableIterator.MEMBER);
		evt.setEvents(v);

		//evt.alignment();

		// import table�� iterate�ؼ� event�� ����� �´�.
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
				// ������ package name�� ���Ѵ�.
				currentPackage=key;
				break;
			}
		}

		// �ֻ��� symbol table�� key�� �д´�.
		Vector classes=new Vector();
		Enumeration en=symtab.keys();
		while (en!=null&&en.hasMoreElements()) {
			String key=(String)en.nextElement();
			classes.addElement(key);
		}

		// ClassMemberContainer�� ClassMember ��ü�� �����ϰ�...
		ClassMemberContainer memberContainer;
		ClassMember member;

		// �� class���� member���� search�Ͽ� class member container�� �Է��Ѵ�.
		for (int i=0;i<classes.size();++i) {
			memberContainer=new ClassMemberContainer();
			SymbolTableEntry tab=(SymbolTableEntry)symtab.get((String)classes.elementAt(i));

			// ���� table�� �ִٸ�, �� member�� ���� table�� �ִٸ�.
			if (tab.table!=null) {
				SymbolTableIterator iter=new SymbolTableIterator(tab.table);
				Vector content=iter.iterateKey(SymbolTableIterator.TOP);

				// ��� Member�� iterate�ϸ鼭 ClassMember�� �����,
				// ������ ���� ��ü�� ClassMemberContainer�� ��´�.
				for (int j=0;j<content.size();++j) {
					// ClassMember���� key string.

					String key=((EventContent)content.elementAt(j)).getContent();

					// SymbolTable���� ã�Ƽ�
                    Hashtable htab=tab.table;
					SymbolTableEntry t=(SymbolTableEntry)htab.get(key);

					// Field, inner class or interface, method�鸸�� ����Ѵ�.
					switch (t.getMemberSort()) {
						case SymbolTableEntry.FIELD:
							// ClassMember��ü�� ����� ���� assign�ϰ�
							member=new ClassMember();
							member.setName(key);
							member.setType(t.getType());
							member.setAccessType(t.getAccessType());
							member.setMemberType(ClassMember.FIELD);

							// ClassMemberContainer�� ��´�.
							memberContainer.addContainer(member);
							break;

						case SymbolTableEntry.CLASS:
						case SymbolTableEntry.INTERFACE:
							// ClassMember��ü�� ����� ���� assign�ϰ� 
							member=new ClassMember();
							member.setName(key);
							member.setType(t.getType());
							member.setAccessType(t.getAccessType());
							member.setMemberType(ClassMember.INNERCLASS);

							// ClassMemberContainer�� ��´�.
							memberContainer.addContainer(member);
							break;

						case SymbolTableEntry.METHOD:
							// ClassMember��ü�� ����� ���� assign�ϰ�
							member=new ClassMember();

							// method�� ���� parameter�� �߶� ����ϹǷ� tokenizing�� �Ѵ�.
							StringTokenizer tokenizer=new StringTokenizer(key,"(,)",false);

							// ���� token�� method name.
							if (tokenizer.hasMoreTokens()) member.setName(tokenizer.nextToken());
							while (tokenizer.hasMoreTokens()) {
								// �ι�°���� parameter type���̴�.
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

							// ClassMemberContainer�� ��´�.
							memberContainer.addContainer(member);
							break;
					}
				}
			}

			// ���������� ��Ű�� �̸��� class�̸��� ��ģ string���� key�� �����
			String keyOfContainer=currentPackage+"."+classes.elementAt(i);

			// ClassMemberContainerList�� put�Ѵ�.
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
