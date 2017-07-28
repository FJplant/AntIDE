/*
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/designer/classdesigner/JavaDocInfo.java,v 1.4 1999/07/29 08:14:14 remember Exp $
 * $Revision: 1.4 $
 * Part : Class Designer JavaDoc Information is stored.
 * Author: Kim, Sung-hoon(kahn@antj.com)
 * Dated at 1999.1.20.22.
 */

package com.antsoft.ant.designer.classdesigner;

import java.util.Vector;
import java.util.StringTokenizer;

/**
  @author Kim, sung-hoon.
  @author Kim, sagn-kyun.
  */
public class JavaDocInfo {
	public static final int INDENTZERO=0;
	public static final int INDENTONE=1;

	private Vector see=null;
	private Vector author=null;
	private Vector param=null;
	private Vector exception=null;

    private String tempSee = null;
    private String tempAuthor = null;

	private String version=null;
	private String returns=null;
	private String since=null;
	private String depricated=null;

	private String description=null;     

	/**
	  remove all param items from the param vector.
	  */
	public void removeAllParams() {
		if (param!=null) param.removeAllElements();
	}

	/**
	  set see item into the see

	  @param param the see item.
	  */
	public void setSee(String argu) {
      tempSee = argu;
	}

	/**
	  setting the description of the attribute as the comment.

	  @param desc description of the attribute.
	  */
	public void setDescription(String desc) {
		description=desc;
	}

	/**
	  getting the description of the attribute.

	  @return description as the String value.
	  */
	public String getDescription() {
		return description;
	}

	/**
	  adding author item into the see lise.

	  @param param the author item.
	  */
	public void setAuthor(String argu) {
      tempAuthor = argu;
	}

	/**
	  adding param item into the see lise.

	  @param param the param item.
	  */
	public void addParam(String argu) {
		if (param==null) param=new Vector();

		param.addElement(argu);
	}

	/**
	  adding exception item into the see lise.

	  @param param the exception item.
	  */
	public void addException(String argu) {
		if (exception==null) exception=new Vector();

		exception.addElement(argu);
	}

	/**
	  setting version.

	  @param param version param.
	  */
	public void setVersion(String argu) {
		version=argu;
	}

	/**
	  setting returns.

	  @param param return contents.
	  */
	public void setReturn(String argu) {
		returns=argu;
	}

	/**
	  setting since.

	  @param param since contents.
	  */
	public void setSince(String argu) {
		since=argu;
	}

	/**
	  setting depricated.

	  @param param depricated indicator.
	  */
	public void setDepricated(String argu) {
		depricated=argu;
	}

	private int seeCounter = 0;

	/**
	  return the next see item from the see list.

	  @return the next see item.
	  */
	public String nextSee() {
        if(tempSee == null) return null;

		if (see==null){
          see = new Vector();
          StringTokenizer st = new StringTokenizer(tempSee, ",", false);
          while(st.hasMoreElements()){
            see.addElement((String)st.nextElement());
          }
        }

		if (seeCounter>=see.size()) return null;

		String ret=null;
		try {
			ret=(String)see.elementAt(seeCounter);
			seeCounter++;
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println(" Exception Occurs in nextSee().....");
		}

		return ret;
	}

	/**
	  return whole see string with comma(delimiter).

	  @return the see item list with comma.
	  */
	public String getSee() {
		return tempSee;
	}

	private int authorCounter = 0;

	/**
	  return the next author item from the see list.

	  @return the next author item.
	  */
	public String nextAuthor() {
		if (tempAuthor==null) return null;

        if(author == null){
          author = new Vector();
          StringTokenizer st = new StringTokenizer(tempAuthor, ",", false);
          while(st.hasMoreElements()){
            author.addElement((String)st.nextElement());
          }
        }

		if (authorCounter>=author.size()) return null;

		String ret=null;
		try {
			ret=(String)author.elementAt(authorCounter);
			authorCounter++;
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println(" Exception Occurs in nextAuthor().....");
		}

		return ret;
	}

	private int paramCounter = 0;

	/**
	  return the next param item from the see list.

	  @return the next param item.
	  */
	public String nextParam() {
		if (param==null) return null;

		if (paramCounter>=param.size()) return null;

		String ret=null;
		try {
			ret=(String)param.elementAt(paramCounter);
			paramCounter++;
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println(" Exception Occurs in nextParam().....");
		}

		return ret;
	}

	private int exceptionCounter = 0;

	/**
	  return the next param item from the see list.

	  @return the next param item.
	  */
	public String nextException() {
		if (exception==null) return null;

		if (exceptionCounter>=exception.size()) return null;

		String ret=null;
		try {
			ret=(String)exception.elementAt(exceptionCounter);
			exceptionCounter++;
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println(" Exception Occurs in nextException().....");
		}

		return ret;
	}

	/**
	  return version contents.

	  @return the version.
	  */
	public String getVersion() {
		return version;
	}

	/**
	  return return contents.

	  @return the return.
	  */
	public String getReturns() {
		return returns;
	}

	/**
	  return since contents.

	  @return the since.
	  */
	public String getSince() {
		return since;
	}

	/**
	  return the depricated indicator.

	  @return the depricated.
	  */
	public String getDepricated() {
		return depricated;
	}

	public String toString(int indent) {
		StringBuffer theCode=new StringBuffer("");
		String endLine="\n";
		String tab="\t";

		String str=null;

		if (indent==INDENTZERO) tab="";

		if (description!=null) theCode.append(tab+" * "+getDescription()+endLine+endLine);

		if (depricated!=null) theCode.append(tab+" * @depricated "+depricated+endLine+endLine);

		seeCounter=0;
		str=nextSee();
		while (str!=null) {
			theCode.append(tab+" * @see "+str+endLine);
            str=nextSee();
		}

		authorCounter=0;
        str=null;
		str=nextAuthor();
		while (str!=null) {
			theCode.append(tab+" * @author "+str+endLine);
            str=nextAuthor();
		}

		paramCounter=0;
        str=null;
		str=nextParam();
		while (str!=null) {
			theCode.append(tab+" * @param "+str+endLine);
            str=nextParam();
		}

		exceptionCounter=0;
        str=null;
 		str=nextException();
		while (str!=null) {
			theCode.append(tab+" * @exception "+str+endLine);
            str=nextException();
		}

        str=null;
 		str=getVersion();
		if (str!=null) theCode.append(tab+" * @version "+str+endLine);

		str=getReturns();
		if (str!=null) theCode.append(tab+" * @return "+str+endLine);

		str=getSince();
		if (str!=null) theCode.append(tab+" * @since "+str+endLine);

		if (theCode.toString().equals("")) {
System.out.println("the java doc is empty string......");
      return null;
    }
		return theCode.toString();
	}
}
