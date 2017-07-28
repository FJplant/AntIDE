/*
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Header: /AntIDE/source/ant/codecontext/classdesigner/CDGeneralInfo.java 2     99-05-16 11:34p Multipia $
 * $Revision: 2 $
 * CDGneralInfo.java
 * Part : Class Designer General Information
 * Author: Kim, Sung-hoon(kahn@antj.com)
 * Dated at 1999.1.20.22.
 */

package com.antsoft.ant.codecontext.classdesigner;

import java.util.Vector;

/**
  @author Kim, sung-hoon.
  @author Kim, sang-kyun.
  */
public class CDGeneralInfo {
	private Vector modifier;
	private String className=null;
    private String packageName=null;
	private String superName=null;

	private boolean isAbstract=false;
	private boolean isFinal=false;
	private boolean isPublic=false;

	private Vector importList=null;
	private Vector implementsList=null;

	private JavaDocInfo javaDocInfo;

	/**
	  Setting the class name of this class.

	  @param name the specified class name.
	  */
	public void setClassName(String name) {
		className=name;
	}

	/**
	  Setting the package name of this class.

	  @param name the specified package name.
	  */
	public void setPackageName(String name) {
		packageName=name;
	}

	/**
	  Setting the super class name of this class.

	  @param name the specified super class name.
	  */
	public void setSuperName(String name) {
		superName=name;
	}

	/**
	  getting the class name of this class.

	  @return the class name.
	  */
	public String getClassName() {
		return className;
	}

	/**
	  getting the package name of this class.

	  @return the package name.
	  */
	public String getPackageName() {
		return packageName;
	}

	/**
	  getting the super class name of this class.

	  @return the super class name.
	  */
	public String getSuperName() {
		return superName;
	}

	/**
	  adding the import full name into the import list.

	  @param the import name.(import is comment)
	  */
	public void addImport(String argu) {
		if (importList==null) importList=new Vector();

		importList.addElement(argu);
	}

    /**
     * remove import element
     *
     * @param i index
     */
    public void removeImportAt(int i){
        importList.removeElementAt(i);
    }

	/**
	  adding the implemented interface name into the implemented interface list.

	  @param the implement name.(implement is comment)
	  */
	public void addImplements(String argu) {
		if (implementsList==null) implementsList=new Vector();

		implementsList.addElement(argu);
	}

    /**
     * remove implements at i
     *
     * @param i index
     */
    public void removeImplementsAt(int i){
        implementsList.removeElementAt(i);
    }

	private int importCount;

	/**
	  return next import name in the import list.

	  @return next import name as String.
	  */
	public String nextImport() {
		if (importList==null) return null;

		if (importCount>=importList.size()) {
			importCount=0;
			return null;
		}

		String ret=null;
		try {
			ret=(String)importList.elementAt(importCount);
			importCount++;
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println(" Exception Occurs in NextImport().....");
		}

		return ret;
	}

	private int implementsCount;

	/**
	  return next implemented interface name in the implemented interface list.

	  @return next implemented interface name as String.
	  */
	public String nextImplements() {
		if (implementsList==null) return null;

		if (implementsCount>=implementsList.size()) {
			implementsCount=0;
			return null;
		}

		String ret=null;
		try {
			ret=(String)implementsList.elementAt(implementsCount);
			implementsCount++;
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Exception Occurs in nextImplements().....");
		}

		return ret;
	}

    /**
     * 특정 index에 interface를 삽입한다
     *
     * @param newImplements 삽입할 interface name
     * @i index
     */
    public void setImplementsAt(String newImplements, int i) {
        implementsList.setElementAt(newImplements, i);
    }



	/**
	  adding the class modifier into the list.

	  @param item the modifier to be added.
	  */
	public void addModifier(String item) {
		if (modifier==null) modifier=new Vector();

		modifier.addElement(item);
	}

	private int modifierCount=0;

	/**
	  getting the next modifier in the list.

	  @return the modifier string.
	  */
	public String nextModifier() {
		if (modifier==null) return null;

		if(modifierCount>=modifier.size()) {
			modifierCount=0;
			return null;
		}

		String ret=null;
		try {
			ret=(String)modifier.elementAt(modifierCount);
			modifierCount++;
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println(" Exception occurs in the getModifier....");
		}

		return ret;
	}

	/**
	  setting the Java documentation information.

	  @param the java documentation information reference.
	  */
	public void setJavaDoc(JavaDocInfo info) {
		javaDocInfo=info;
	}

	/**
	  getting the Java documentation information.

	  @return the java documentation information reference.
	  */
	public JavaDocInfo getJavaDoc() {
		if (javaDocInfo==null) return null;

		return javaDocInfo;
	}

	/**
	  getting the class modifier Abstract flag

	  @return the class modifier Abstract flag
	  */
	public boolean isAbstract() {
		return isAbstract;
	}

	/**
	  getting the class modifier final flag

	  @return the class modifier final flag
	  */
	public boolean isFinal() {
		return isFinal;
	}

	/**
	  getting the class modifier Public flag

	  @return the class modifier Public flag
	  */
	public boolean isPublic() {
		return isPublic;
	}

	/**
	  setting the class modifier Public flag

	  @param flag the class modifier Public flag
	  */
	public void setPublic(boolean flag) {
		isPublic=flag;
	}

	/**
	  setting the class modifier Final flag

	  @param flag the class modifier Final flag
	  */
	public void setFinal(boolean flag) {
		isFinal=flag;
	}

	/**
	  setting the class modifier Abstract flag

	  @param flag the class modifier Abstract flag
	  */
	public void setAbstract(boolean flag) {
		isAbstract=flag;
	}
}
