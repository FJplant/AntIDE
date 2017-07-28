/* * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/codecontext/SymbolTableIterator.java,v 1.17 1999/08/27 07:25:54 kahn Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.17 $
 * Part : Symbol Table Iterator Class
 * Copyright (c) 1998,1999 Ant Company, all right reserved.
 * Author: Kim, Sung-hoon(kahn@antj.com)
 * Starting at 1999. 2. 9.
 */

package com.antsoft.ant.codecontext;

import java.util.*;
import com.antsoft.ant.codecontext.codeeditor.*;

/**
  @author Kim, Sung-Hoon.
  */
public class SymbolTableIterator {
	//private Hashtable table=null;
	public static final int TOP=0;
	public static final int MEMBER=1;
	public static final int ALL=2;

	/**
	  Constructor.

    @param table the table to be iterated.
	public SymbolTableIterator(Hashtable table) {
		// iterate할 table을 설정한다.
		this.table=table;
	}
	  */

	/**
	  the Iteration method that iterates to specified depth in the import table
	  */
	public static Vector iterateKey(Hashtable table) {
    if (table == null) return null;

		Enumeration enum=table.keys();
		Vector output=new Vector();
		while (enum != null && enum.hasMoreElements()) {
			String keyValue=(String)enum.nextElement();
      //System.out.println(" key value "+keyValue);
			SymbolTableEntry nextOne=(SymbolTableEntry)table.get(keyValue);

			EventContent content=new EventContent();
			content.setContent(keyValue);
			switch (nextOne.getMemberSort()) {
				case SymbolTableEntry.IMPORT :
					content.setContentType(EventContent.IMPORT);
					break;
				case SymbolTableEntry.PACKAGE :
					content.setContentType(EventContent.PACKAGE);
					break;
			}

			output.addElement(content);
		}

		return output;
	}

  private static String parent = null;


	/**
	  the Iteration method that iterates to specified depth in the symbol table
	  except import list.

	  @param d the specified depth as int.
	  */
	public static Vector iterateKey(Hashtable table,int d) {
		if (d < 0) return null;
    if (table == null) return null;

		// return 할 vector object.
		Vector output = new Vector();

		Enumeration e = table.keys();
		if (e != null) {
			while (e.hasMoreElements()) {
				// key를 받아서
				String keyValue = (String)e.nextElement();
				// table에서 꺼내고...
				SymbolTableEntry nextOne = (SymbolTableEntry)table.get(keyValue);

				// EventContent object를 만들고 나서
				EventContent content = new EventContent();
				// EventContent의 Content를 setting하고
				content.setContent(keyValue);
        //System.out.println(" d = "+d+" key "+keyValue);

				// member의 종류에 따라서 ContentType을 setting한다.
				switch (nextOne.getMemberSort()) {
					case SymbolTableEntry.FIELD:
            if (d==1 && nextOne.getDepth()==0) continue;
						content.setContentType(EventContent.ATTR);
						break;

					case SymbolTableEntry.METHOD:
					case SymbolTableEntry.CONSTRUCTOR:
            if (d==1 && nextOne.getDepth()==0) continue;
						content.setContentType(EventContent.OPER);
						break;

					case SymbolTableEntry.CLASS:
						if (nextOne.getDepth() == 0) content.setContentType(EventContent.CLASS);
						else content.setContentType(EventContent.INNER);
						break;
					case SymbolTableEntry.INTERFACE:
						if (nextOne.getDepth() == 0) content.setContentType(EventContent.INTERFACE);
						else content.setContentType(EventContent.INNER);
						break;
          case SymbolTableEntry.OTHERS: continue;
				}

				// return할 Vector Object인 output 에 element를 add하고
				output.addElement(content);

				// 현재의 symbol table content의 또 다른 table을 포함한다면
				// recursive하게 그것 또한 iterate한다.
				if (nextOne.table!=null) {
					Vector temp = iterateKey(nextOne.table, d-1);
					if (temp!=null)
						for (int i=0;i<temp.size();++i)
							output.addElement(temp.elementAt(i));
				}
			}
		}
		return output;
	}

	public static Vector iterateKey(Hashtable table,EventContent parent) {
    if (table == null) return null;
    boolean recurse = false;

		// return 할 vector object.
		Vector output = new Vector();

		Enumeration e = table.keys();
		if (e != null) {
			while (e.hasMoreElements()) {
				// key를 받아서
				String keyValue = (String)e.nextElement();
				// table에서 꺼내고...
				SymbolTableEntry nextOne = (SymbolTableEntry)table.get(keyValue);

				// EventContent object를 만들고 나서
				EventContent content = new EventContent();
				// EventContent의 Content를 setting하고
				content.setContent(keyValue);
        //System.out.println(" key " + keyValue);

				// member의 종류에 따라서 ContentType을 setting한다.
				switch (nextOne.getMemberSort()) {
					case SymbolTableEntry.FIELD:
          	recurse = false;
            if (nextOne.getDepth()==0) continue;
						content.setContentType(EventContent.ATTR);
            if (parent != null) content.setParent(parent);
    				break;

					case SymbolTableEntry.METHOD:
					case SymbolTableEntry.CONSTRUCTOR:
          	recurse = false;
            if (nextOne.getDepth()==0) continue;
						content.setContentType(EventContent.OPER);
            if (parent != null) content.setParent(parent);
						break;

					case SymbolTableEntry.CLASS:
          	recurse = true;
						if (nextOne.getDepth() == 0) {
            	content.setContentType(EventContent.CLASS);
            }
						else {
            	content.setContentType(EventContent.INNER);
	            if (parent != null) content.setParent(parent);
            }

						break;
					case SymbolTableEntry.INTERFACE:
          	recurse = true;
						if (nextOne.getDepth() == 0) {
            	content.setContentType(EventContent.INTERFACE);
            }
						else {
            	content.setContentType(EventContent.INNER);
	            if (parent != null) content.setParent(parent);
            }
						break;
          case SymbolTableEntry.OTHERS: continue;
				}

        // return할 Vector Object인 output 에 element를 add하고
        output.addElement(content);

        // 현재의 symbol table content의 또 다른 table을 포함한다면
        // recursive하게 그것 또한 iterate한다.
        if (recurse && nextOne.table != null) {
          Vector temp = iterateKey(nextOne.table, content);
          if (temp!=null)
            for (int i=0;i<temp.size();++i)
              output.addElement(temp.elementAt(i));
        }
			}
		}

		return output;
	}

	/**
	  search for specified token with absolute offset
		and return its result type.

	  @param symbol the symbol string to search for.
	  @param offset the symbol's start offset from the
									beginning of the current document.
	  */
	public static String search(Hashtable table, String symbol, int line) {
    if (table == null) return null;

    if (symbol.equals("this")) return getThisClassName(table,line);
    else if (symbol.equals("super")) return getSuperClassName(table,line);

    SymbolTableEntry elem = null;

		// 없으면....
		Enumeration enumerator=table.elements();
    if (enumerator != null)
		while (enumerator.hasMoreElements()) {
			// 그 아래 포함된 table들을 검색한다.
			elem=(SymbolTableEntry)enumerator.nextElement();
      //System.out.println(" c_line => "+line+" "+elem.getType()+" s_line => "+elem.getStartLine()+" e_line => "+elem.getEndLine());
			if (elem.table==null) continue;		// 하위 table이 존재하지 않는다.
												// 즉, field나 variable 선언이고 block이 아니다.

			if (elem.getStartLine() > line) continue; // 현재의 offset이 포함되지 않는 block

			if (elem.getEndLine() < line) continue; // 현재의 offset이 포함되지 않는 block

			String returnType = search(elem.table, symbol, line);
      if (returnType != null) return returnType;
		}

		// 현재의 hash table(symbol table)에서 symbol을 찾아보고
		elem = (SymbolTableEntry)table.get(symbol);

		// 있으면 그것의 type을 return한다.
		if (elem != null) return elem.getType();

		return null;
	}

	/**
	  search the offset in the document of the specified String.
      if not found, then return -1.

	  @param symbol specified string
	  @return the start offset in the document.
	  */
	public static int search(Hashtable table, String symbol) {
    if (table == null) return -1;

		// 현재의 hash table(symbol table)에서 symbol을 찾아보고		SymbolTableEntry elem = (SymbolTableEntry)table.get(symbol);

		// 있으면 그것의 type을 return한다.		if (elem!=null) return elem.getStartLine();

		// 없으면....		Enumeration enumerator = table.elements();
		while (enumerator != null && enumerator.hasMoreElements()) {
			// 그 아래 포함된 table들을 검색한다.
			elem = (SymbolTableEntry)enumerator.nextElement();
			if (elem.table == null) continue;		// 하위 table이 존재하지 않는다.
												// 즉, field나 variable 선언이고 block이 아니다.

			int retValue = search(elem.table, symbol);
			if (retValue != -1) return retValue;
		}

		return -1;
	}

	/**	  * iterate the symbol table(hashtable of the tree structure)
		* and modifies the line of starting and ending with specified length.
    *
	  * @param line specified line to be standard.
	  */
	public static void modifyLineCountInTable(Hashtable table, int line) {
    if (table == null) return;

		Enumeration e = table.elements();		while (e!=null && e.hasMoreElements()) {
			SymbolTableEntry object=(SymbolTableEntry)e.nextElement();
			int sos=object.getStartLine();	// start offset.
			int eos=object.getEndLine();		// end offset.

			if (sos > line) {
				object.setStartLine(sos+1);
				object.setEndLine(eos+1);

				if (object.table!=null) {
					modifyLineCountInTable(object.table, line);
				}
			}
			else if (eos > line) {
				object.setEndLine(eos+1);

				if (object.table!=null) {					modifyLineCountInTable(object.table, line);
				}
			}
		}
	}

	/**	  search the all overloading method.

	  @param str the specified method name without paramater type style.	  @param offset the offset that the str is appeared.
	  @return the method parameter list as the Vector value.
	  */
	public static Vector searchAllOverloadedMethod(Hashtable table, String str, int line) {
    if (table == null) return null;

		Enumeration e = table.keys();
		SymbolTableEntry wanted = null;	// wanted class symbol entry.
		while (e!=null && e.hasMoreElements()) {
			String s = (String)e.nextElement();
			SymbolTableEntry elem = (SymbolTableEntry)table.get(s);
			if (elem.getEndLine() > line) {
				wanted=elem;
				break;
			}
		} // end of while.

		if (wanted==null) return null;
		Hashtable tab=null;		if (wanted.table != null) tab = wanted.table;
		else return null;

   	Vector eventString=null;
   	Enumeration en=tab.keys();
		while (en != null && en.hasMoreElements()) {
     	String string=(String)en.nextElement();
			if (string.indexOf("(")==-1) continue;
     	String comp=string.substring(0,string.indexOf("("));
     	if (!comp.equals(str)) continue;

			if (eventString==null) eventString=new Vector();
      SymbolTableEntry entry = (SymbolTableEntry)tab.get(string);      Vector temp = entry.getParameters();
     	if (temp==null)	eventString.addElement("<no parameter>");
			else {
        StringBuffer buf = new StringBuffer();
        for (int i=0;i<temp.size();++i) buf.append(temp.elementAt(i)+",");
        eventString.addElement(buf.toString().substring(0,buf.length()-1));
      }
		}

   	return eventString;
 	}

  /**    get the depth of the specified offset.

	  @param offset the offset.    @return the depth as the int value.
    */
  public static int getDepth(Hashtable table, int line) {
    if (table == null) return 0;

    Enumeration e=table.keys();    while (e!=null&&e.hasMoreElements()) {
      String key=(String)e.nextElement();
      SymbolTableEntry elem=(SymbolTableEntry)table.get(key);
      //System.out.println(" start : "+elem.getStartLine()+" end : "+elem.getEndLine()+ " line : "+line);
      if (elem.getStartLine() < line && elem.getEndLine() >= line) {
        if (elem.table!=null) {
          return getDepth(elem.table, line)+1;
        }

        return 0;      }
    }

    return 0;  }

	/**	  get the class name corresponding with "this" token.

	  @param line the line number	  @return the class name as String value.
	  */
	public static String getThisClassName(Hashtable table, int line) {
		Enumeration e = table.keys();
		while (e!=null&&e.hasMoreElements()) {
			String str=(String)e.nextElement();

			SymbolTableEntry element = (SymbolTableEntry)table.get(str);			if (element.getStartLine() <= line && element.getEndLine() >= line) return str;
		}

		return null;	}

	/**	  get the class name corresponding with "super" token.

	  @param line the line number	  @return the class name as String value.
	  */
	public static String getSuperClassName(Hashtable table, int line) {
		Enumeration e = table.keys();
		while (e!=null&&e.hasMoreElements()) {
			String str=(String)e.nextElement();

			SymbolTableEntry element = (SymbolTableEntry)table.get(str);			if (element.getStartLine() <= line && element.getEndLine() >= line) {
      	str = element.getSuperClass();
      	return str;
      }
		}

		return null;	}

 	public static Vector getLocalVarEvent(Hashtable table, int line) {    if (table == null) return null;

    Vector localVar = new Vector();
		Enumeration e = table.keys();		while (e!=null && e.hasMoreElements()) {
      String name = (String)e.nextElement();
			SymbolTableEntry object = (SymbolTableEntry)table.get(name);
      int depth = object.getDepth();
            //System.out.println(" depth => "+depth);
      if (depth < 2) {
         if (object.table != null) {
           if (object.getStartLine() > line || object.getEndLine() < line) continue;
					 Vector temp = getLocalVarEvent(object.table, line);
           if (temp != null)
             for (int i=0;i<temp.size();++i) localVar.addElement(temp.elementAt(i));
         }
         continue;
      }

      //System.out.println(" start => "+object.getStartLine()+" end => "+object.getEndLine()+" line => "+line+" key "+name);		  int sos = object.getStartLine();	// start offset. 			if (sos > line) continue;
      if (object.table != null) {
    	  int eos = object.getEndLine();		// end offset.
    		if (eos < line) continue;
				Vector temp = getLocalVarEvent(object.table, line);
        if (temp != null)
          for (int i=0; i < temp.size();++i) localVar.addElement(temp.elementAt(i));
      } else {
          //System.out.println(" name -===> "+name+" "+object.getMemberSort());
		    if (object.getMemberSort() == SymbolTableEntry.FIELD) {
  	      if (sos <= line) localVar.addElement(name+":Local:"+object.getType());
        }
      }
		}

    if (localVar.size() == 0) return null;
    else return localVar;	}

  public static String getThisPackageName(Hashtable table) {
  	if (table == null) return null;

    Enumeration e = table.keys();
    if (e != null)
    	while (e.hasMoreElements()) {
      	String key = (String)e.nextElement();
        SymbolTableEntry entry = (SymbolTableEntry)table.get(key);

        if (entry.getMemberSort() == SymbolTableEntry.PACKAGE) return key;
      }

    return "dummypack";
  }

  public static boolean isInInnerClass(Hashtable table, int line) {
  	if (table == null) return false;

  	Enumeration e = table.elements();
    if (e != null) while (e.hasMoreElements()) {
    	SymbolTableEntry entry = (SymbolTableEntry)e.nextElement();

      int sLine = entry.getStartLine();
      int eLine = entry.getEndLine();

      if (entry.getMemberSort() == SymbolTableEntry.CLASS ||
      		entry.getMemberSort() == SymbolTableEntry.INTERFACE)
	      if (sLine <= line && eLine >= line)
        	return true;
    }

    return false;
  }

  public static Vector getInnerMember(Hashtable table, int line) {
  	if (table == null) return null;

  	SymbolTableEntry entry = null;
    Enumeration e = table.elements();
    boolean found = false;
    if (e != null) while (e.hasMoreElements()) {
    	entry = (SymbolTableEntry)e.nextElement();

      int sLine = entry.getStartLine();
      int eLine = entry.getEndLine();

      if (entry.getMemberSort() == SymbolTableEntry.CLASS ||
      		entry.getMemberSort() == SymbolTableEntry.INTERFACE)
	      if (sLine <= line && eLine >= line) {
        	found = true;
          break;
        }
    }

    if (found && entry.table != null) {
    	table = entry.table;
      Vector members = new Vector(1,1);
    	e = table.keys();
      if (e != null) while (e.hasMoreElements()) {
      	String key = (String)e.nextElement();
        entry = (SymbolTableEntry)table.get(key);

        EventContent ec = new EventContent();
        switch (entry.getMemberSort()) {
        	case SymbolTableEntry.FIELD :
          	String result = key + ":" + entry.getType();
            ec.setContent(result);
            ec.setContentType(EventContent.ATTR);
            break;

          case SymbolTableEntry.CONSTRUCTOR :
          case SymbolTableEntry.METHOD :
          	int pos = key.indexOf('(');
            if (pos != -1) key = key.substring(0, pos);
            result = key + ":" + entry.getType();
            ec.setContent(result);
            ec.setContentType(EventContent.OPER);
            break;
          default : continue;
        }

        members.addElement(ec);
      }
    	//return iterateKey(entry.table, 1);
      return members;
    }

    return null;
  }
}


