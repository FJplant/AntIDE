/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/codecontext/SymbolSave.java,v 1.10 1999/08/30 09:37:45 kahn Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.10 $
 * Part : Symbol Save when all job over.
 * Author: Kim, Sung-hoon(kahn@antj.com)
 * Starting at 1999. 2. 10.
 */

package com.antsoft.ant.codecontext;

import java.util.*;
import java.io.*;
import com.antsoft.ant.manager.projectmanager.*;

import com.antsoft.ant.compiler.DepsData;
import com.antsoft.ant.compiler.DepTable;

/**
  @author Kim, sung-hoon.
  */
public class SymbolSave { //implements Runnable {
  private String path = null;
  private Project project = null;

	/**
	  default Constructor.
	  */
	public SymbolSave(String path , Project project) {
  	int pos = path.indexOf(".apr");
    this.path = path.substring(0, pos);
    this.project = project;
  }

  public void run() {
  	if (path == null) return;

    long c_time = System.currentTimeMillis();

    Hashtable refTable = new Hashtable();
    Hashtable classFileTable = new Hashtable();

    Vector files = project.getFiles();
    for (int i = 0;i < files.size();++i) {
      ProjectFileEntry entry = (ProjectFileEntry)files.elementAt(i);
      String fileName = entry.getPath() + File.separator + entry.getName();
      File file = new File(fileName);
      if (!file.exists()) continue;

      ClassExtractor extractor = new ClassExtractor(file);
      extractor.doParse();
      String packName = extractor.getPackageName();

      Vector classList = extractor.getClasses();
      if (classList != null && classList.size() != 0) {
      	int size = classList.size();
        String fullName = null;
        for (int j = 0;j < size;++j) {
          fullName = packName + "." + (String)classList.elementAt(j);
          //System.out.println(" full name ==> "+fullName);
          refTable.put(fullName, fileName);
        }

        classFileTable.put(fileName, fullName);
        //System.out.println(" full name ==> "+fullName+ " filen => "+fileName);
      }
    }

		try {
			FileOutputStream ostream=new FileOutputStream(path + ".ref");
			ObjectOutputStream stream=new ObjectOutputStream(ostream);

      stream.writeObject(refTable);

			stream.close();
			ostream.close();
		} catch (IOException e) {
			System.out.println("IOException(SymbolSave:run() 1) - "+e.toString());
		} catch (Exception e) {
			System.out.println("Exception(SymbolSave:run() 1) - "+e.toString());
		}

		try {
			FileOutputStream ostream=new FileOutputStream(path + ".cor");
			ObjectOutputStream stream=new ObjectOutputStream(ostream);

      stream.writeObject(classFileTable);

			stream.close();
			ostream.close();
		} catch (IOException e) {
			System.out.println("IOException(SymbolSave:run() 2) - "+e.toString());
		} catch (Exception e) {
			System.out.println("Exception(SymbolSave:run() 2) - "+e.toString());
		}


    // dependency information maker
    /*
    DepsData.removeTable(path);
    DepsData.setTable(path);

    for (int i = 0;i < files.size();++i) {
      ProjectFileEntry entry = (ProjectFileEntry)files.elementAt(i);
      String fileName = entry.getPath() + File.separator + entry.getName();
      File file = new File(fileName);
      if (!file.exists()) continue;

      Parser extractor = new Parser(file);
      extractor.setDepsFlag(fileName);
      extractor.doParse();
    }

		try {
			FileOutputStream ostream=new FileOutputStream(path + ".dep");
			ObjectOutputStream stream=new ObjectOutputStream(ostream);

      stream.writeObject(DepsData.deps);

			stream.close();
			ostream.close();
		} catch (IOException e) {
			System.out.println("IOException(SymbolSave:run() 3) - "+e.toString());
		} catch (Exception e) {
			System.out.println("Exception(SymbolSave:run() 3) - "+e.toString());
		} catch (StackOverflowError error) {
    	File f = new File(path + ".dep");
      f.delete();
    }
    */
  }
}
