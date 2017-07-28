/*
 * $Header: /AntIDE/source/ant/codecontext/CodeGenerator.java 2     99-05-17 12:15a Multipia $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 2 $
 * Part : From Class Designer inforamtion, The Code generated.
 * Author: Kim, Sung-hoon(kahn@antj.com)
 * Dated at 1999.1.22.
 */

package com.antsoft.ant.codecontext;

import com.antsoft.ant.codecontext.classdesigner.*;
import java.util.Enumeration;

/**
  @author Kim, Sung-hoon.
  */
public class CodeGenerator {
	private String theClassName;
	private String packName;

	private CDGeneralInfo generalInfo;
	private CDAttrInfoContainer attrContainer;
	private CDOperInfoContainer operContainer;

	// the reserved string pattern
	private String commentStart="/**\n";
	private String commentEnd=" */\n";
	private String blockStart="{\n";
	private String blockEnd="}\n";
	private String endLine="\n";
	private String blank=" ";

	//private int tabSize=3;

	/**
	  * get the package name.
	  *
	  * @return the package name.
	  */
	public String getPackageName() {
		return packName;
	}

	/**
	  It read the Class Designer Information, and create new Document Object.
	  When the ClassDesigner creates the OK ro APPLY Event and the Class name
	  is the new, then this method is invocated.

	  @param info the ClassDesigner information as the CDInfo class type.
	  @return newly generated source code as the String type.
	  */
	public String newDocGenerate(CDInfo info) {
		generalInfo=info.getCDGeneralInfo();
		attrContainer=info.getCDAttrInfoContainer();
		operContainer=info.getCDOperInfoContainer();

		// setting the current class anem.
		theClassName=generalInfo.getClassName();

		StringBuffer theCode=new StringBuffer();

		String str=generalInfoCode();
		if (str!=null) theCode.append(str);

		return theCode.toString();
	}

	// the CDGeneralInfo data is coded.
	String generalInfoCode() {
		String str=null;
		StringBuffer theCode=new StringBuffer("");
		JavaDocInfo javadocinfo=null;

		// the package declaration....
		str=generalInfo.getPackageName();
		if (str!=null) {
			theCode.append("package "+str+";"+endLine+endLine);
			packName=str.toString();
		}

		//theCode.append(endLine);

		// the import declarations....
		str=generalInfo.nextImport();
		if (str!=null) {
			while (str!=null) {
				theCode.append("import "+str+" ;"+endLine);
				str=generalInfo.nextImport();
			}

			theCode.append(endLine);
		}

		// the class declaration....
		// first, document is converted.
		javadocinfo=generalInfo.getJavaDoc();
		if (javadocinfo!=null) {
			str=javadocinfo.toString(JavaDocInfo.INDENTZERO);
			if (str!=null) {
				theCode.append(commentStart);
				theCode.append(str+endLine);
				theCode.append(commentEnd);
			}
		}
		//  document is O.V.E.R.

		//  next modifier is written.
		str=generalInfo.nextModifier();
		while (str!=null) {
			theCode.append(str+blank);
			str=generalInfo.nextModifier();
		}

		theCode.append("class "+generalInfo.getClassName()+blank);

		str=generalInfo.getSuperName();
		if (str!=null) theCode.append("extends "+str+blank);

		str=generalInfo.nextImplements();
		if (str!=null) theCode.append("implements "+str+blank);
		str=generalInfo.nextImplements();
		while (str!=null) {
			theCode.append(","+str+blank);
			str=generalInfo.nextImplements();
		}

		theCode.append(blockStart);

		str=attrInfoContainerCode();
		if (str!=null) theCode.append(attrInfoContainerCode());

		str=operInfoContainerCode();
		if (str!=null) theCode.append(operInfoContainerCode());

		theCode.append(blockEnd);

		return theCode.toString();
	}

	String attrInfoContainerCode() {
		CDAttrInfo attr=null;
		StringBuffer theCode=new StringBuffer("");
		Enumeration e=null;

		e=attrContainer.getAttrInfos();
		while (e!=null&&e.hasMoreElements()) {
				attr=(CDAttrInfo)e.nextElement();
				theCode.append(attr.toString()+endLine);
		}

		return theCode.toString();
	}

	String operInfoContainerCode() {
		CDOperInfo oper=null;
		StringBuffer theCode=new StringBuffer("");
		Enumeration e=operContainer.getOperInfos();

		while (e!=null&&e.hasMoreElements()) {
			oper=(CDOperInfo)e.nextElement();
			theCode.append(oper.toString());
		}

		return theCode.toString();
	}

	/**
	  getting The Class Name of the Code Generator.

	  @return the specifying class name that the current CodeGenerator has.
	  */
	public String getTheClassName() {
		return theClassName;
	}

	/**
	  Constructor
	  */
	public CodeGenerator() {}
}
