/*
 * $Header: /AntIDE/source/ant/codecontext/DispatchClassInfo.java 8     99-06-01 3:38p Kahn $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 8 $
 * Part : dispatch class information from file.
 * Author: Kim, Sung-hoon(kahn@antj.com)
 * Starting at 1999. 2. 6.
 *                   2. 9. (전면 수정)
 */

package com.antsoft.ant.codecontext;

import java.util.*;
import javax.swing.text.*;
import java.io.*;
import com.antsoft.ant.pool.sourcepool.*;

/**
  @author Kim, Sung-Hoon.
  */
public class DispatchClassInfo {
	// text to be parsed.
	private String text=null;

	// the length of the text.
	private int length;

	// current character counter.
  private int charCount=0;

  protected boolean inComment=false;

  private Hashtable table = new Hashtable();
  private String packagename="dummypack";

  // 최초 특정 file가 activate되면, 그것을 분석하여
  // symbol table과 import list table을 구성한다.
  public DispatchClassInfo(File f) {
		BufferedReader in = null;
    try {
			in = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
      String line;
      StringBuffer buf = new StringBuffer("");
      while ( ( line = in.readLine() ) != null ) {
        line += '\n';
        buf.append(line);
      }
      text = buf.toString();
      length = text.length();
    } catch (FileNotFoundException e) {
      System.out.println(" File not fount "+e.toString());
      text = null;
    } catch (IOException e) {
      System.out.println(" IO Exception "+e.toString());
      text = null;
    } finally {
			try {
				in.close();
			} catch ( IOException ioe ) {
				System.err.println( "Can't close the file." );
			}
    }
  }

  public void makeClassInfo() {
    if (text == null) return;

		// first one line.
		String string=nextOneLine();
		String paramString=null;
    int depth=0;

    SymbolTableEntry currentElement;
    String key="";

    while (string != null) {
      if (depth==0) {
  			LineParser parser=new LineParser(string,false);
	  		currentElement=parser.parse();

  			if (currentElement!=null) {	// declaration : the class, interface, field, method
  				//System.out.println("the key = "+parser.getKey());
	  			switch (currentElement.getMemberSort()) {
					  case SymbolTableEntry.CLASS:
					  case SymbolTableEntry.INTERFACE:
 							table.put(parser.getKey(),currentElement);
              break;
  					case SymbolTableEntry.PACKAGE:
              packagename=parser.getKey();
			  			break;
  				}
	  		}
      }

		  for (int i=0;i<string.length();++i) {
				char c=string.charAt(i);
				if (c=='{') depth++;
				if (c=='}')	depth--;
			}

			// next one line.
			string=nextOneLine();
    }
  }

  /**
    getting the symbol table.

    @return reference of the symbol table.
    */
  public Hashtable getTable() {
    return table;
  }

  /**
    getting the package name of the current file.

    @return package name as String.
    */
  public String getPackageName() {
    return packagename;
  }

  // get next one line.
	private String nextOneLine() {
    if (charCount>=length) return null;
    StringBuffer strbuf=new StringBuffer("");

    //char c=text.charAt(charCount);
    char c;
		do {
			if (charCount<length) c=text.charAt(charCount);
			else break;
			strbuf.append(c);
			charCount++;
		} while (c!='\n');

		String line=strbuf.toString();
		if (line.length()<=0) return "";

		int p=0;

		if (inComment) {
			p=line.indexOf("*/");
			if (p==-1) return nextOneLine();
			else {
				inComment=false;
				p+=2;
				if (p==line.length()) return nextOneLine();
				else return line.substring(p,line.length());
			}
		}
		else {
			p=line.indexOf("/*");
			if (p!=-1) {
				int q=line.indexOf("*/");
				if (q==-1) {
					inComment=true;
					//System.out.println("in Comment");
					return nextOneLine();
				}
				else {
					StringBuffer temp=new StringBuffer();
					boolean fflag=false;
					if (p!=0) {
						fflag=true;
						temp.append(line.substring(0,p));
					}
					if (line.length()!=q+2) {
						if (fflag) temp.append(" ");
						fflag=true;
						temp.append(line.substring(q+2,line.length()));
					}

					if (fflag) return temp.toString();
					else return nextOneLine();
				}
			}
		}

		p=line.indexOf("//");
		if (p!=-1) {
			if (p==0) return nextOneLine();
			else return line.substring(0,p);
		}

		return line;
	}
}
