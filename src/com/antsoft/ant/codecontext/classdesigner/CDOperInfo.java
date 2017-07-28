/*
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Header: /AntIDE/source/ant/codecontext/classdesigner/CDOperInfo.java 2     99-05-16 11:34p Multipia $
 * $Revision: 2 $
 * Part : class designer operation information.
 * Author: Kim, Sung-hoon(kahn@antj.com)
 * Dated at 1999.1.21.22.
 */

package com.antsoft.ant.codecontext.classdesigner;

import java.util.Hashtable;
import java.util.Enumeration;

/**
  @author Kim, Sung-hoon.
  */
public class CDOperInfo {
	public static final String PUBLIC="public";
	public static final String PROTECTED="protected";
	public static final String PRIVATE="private";
	public static final String NOMODIFIER="";

	private String accessType=NOMODIFIER;

	private String returnType;
	private String methodName;

	private boolean staticAttr;
	private boolean finalAttr;
	private boolean syncAttr;
	private boolean abstractAttr;
	private boolean nativeAttr;

	private StringBuffer tabString=new StringBuffer();

	private Hashtable paramInfos;

	private JavaDocInfo javaDocInfo;

	/**
	  setting the access type of this method.

	  @param accessType the access type as the string value.
	  */
	public void setAccessType(String accessType) {
		this.accessType=accessType;
	}

	/**
	  setting the return type of this method.

	  @param type the return type as the string value.
	  */
	public void setReturnType(String type) {
		returnType=type;
	}

	/**
	  setting the method name of this method.

	  @param name the method name as the string value.
	  */
	public void setMethodName(String name) {
		methodName=name;
	}

	/**
	  setting static flag.

	  @param flag the static flag.
	  */
	public void setStatic(boolean flag) {
		staticAttr=flag;
	}

	/**
	  setting final flag.

	  @param flag the final flag.
	  */
	public void setFinal(boolean flag) {
		finalAttr=flag;
	}

	/**
	  setting synchronous flag.

	  @param flag the synchronous flag.
	  */
	public void setSynchronous(boolean flag) {
		syncAttr=flag;
	}

	/**
	  setting abstract flag.

	  @param flag the abstract flag.
	  */
	public void setAbstract(boolean flag) {
		abstractAttr=flag;
	}

	/**
	  setting native flag.

	  @param flag the native flag.
	  */
	public void setNative(boolean flag) {
		nativeAttr=flag;
	}

	/**
	  getting the access type of the method.

	  @return the access type of the method as the string value.
	  */
	public String getAccessType() {
		return accessType;
	}

	/**
	  getting the return type of the method.

	  @return the return type of the method as the string value.
	  */
	public String getReturnType() {
		return returnType;
	}

	/**
	  getting the method name of the method.

	  @return the method name of the method as the string value.
	  */
	public String getMethodName() {
		return methodName;
	}

	/**
	  getting the static flag.

	  @return the static flag as the boolean value.
	  */
	public boolean isStatic() {
		return staticAttr;
	}

	/**
	  getting the final flag.

	  @return the final flag as the boolean value.
	  */
	public boolean isFinal() {
		return finalAttr;
	}

	/**
	  getting the synchronous flag.

	  @return the synchronous flag as the boolean value.
	  */
	public boolean isSynchronous() {
		return syncAttr;
	}

	/**
	  getting abstract flag.

	  @param flag the abstract flag.
	  */
	public boolean isAbstract() {
		return abstractAttr;
	}

	/**
	  getting native flag.

	  @param flag the native flag.
	  */
	public boolean isNative() {
		return nativeAttr;
	}

	/**
	  adding the parameter into the parameter information list.

	  @param param the parameter to be added.
	  */
	public void addParamInfo(CDParamInfo param) {
		if (paramInfos==null) paramInfos=new Hashtable();

		try {
			paramInfos.put(param.getParamName(),param);
		} catch (NullPointerException e) {
			System.out.println(" Just, Parameter is nullable ...");
		}
	}

	/**
	  getting the parameter information list as the enumeration.

	  @return enumeration including the parameter information.
	  */
	public Enumeration getParamInfos() {
		if (paramInfos==null) return null;

		return paramInfos.elements();
	}

	/**
	  getting specified the parameter information by one as the CDParamInfo type.

	  @param the parameter name that you wanna get.
	  */
	public CDParamInfo getParamInfo(String name) {
		return (CDParamInfo)paramInfos.get(name);
	}

	/**
	  removing the specified parameter information from the hashtable.
	  */
	public void delParamInfo(String name) {
		paramInfos.remove(name);
	}

	/**
	  getting the java documentation information.

	  @return the java documentation information.
	  */
	public JavaDocInfo getJavaDoc() {
		return javaDocInfo;
	}

	/**
	  setting the java documentation information.

	  @param info the java documentation information.
	  */
	public void setJavaDoc(JavaDocInfo info) {
		javaDocInfo=info;
	}

	/**
	  Returns the String representation of the object.

	  @return the string representation of this object.
	  */
	public String toString() {
		StringBuffer theCode=new StringBuffer("");

		String str=null;
		String blank=" ";
		String endLine="\n";
		String tab="\t";
		JavaDocInfo javaDoc;

		javaDoc=getJavaDoc();
		if (javaDoc!=null) {
			str=javaDoc.toString(JavaDocInfo.INDENTONE);
			if (str!=null) {
				if (getAccessType().equals("private")) theCode.append(tab+"/*"+endLine);
	  			else theCode.append(tab+"/**"+endLine);
		  		theCode.append(str);
				theCode.append(endLine+tab+" */"+endLine);
			}
		}
		theCode.append(tab);

		str=getAccessType();
		if (str.length()>3) theCode.append(str+blank);

		if (isStatic()) theCode.append("static ");

		if (isFinal()) theCode.append("final ");

		if (isSynchronous()) theCode.append("synchronized ");

		if (isNative()) theCode.append("native ");

		if (isAbstract()) theCode.append("abstract ");

		theCode.append(getReturnType()+blank);

		theCode.append(getMethodName()+blank);

		theCode.append("(");

		Enumeration e=getParamInfos();
		while (e!=null&&e.hasMoreElements()) {
			CDParamInfo param=(CDParamInfo)e.nextElement();
			theCode.append(param.toString());
			if (e.hasMoreElements()) theCode.append(",");
		}

		if (isAbstract()||isNative()) theCode.append(";"+endLine+endLine);
		else {
			theCode.append(") {"+endLine+tab);
			theCode.append("}"+endLine+endLine);
		}

		return theCode.toString();
	}
}
