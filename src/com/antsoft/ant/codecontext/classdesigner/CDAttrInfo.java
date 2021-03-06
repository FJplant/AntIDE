/*
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Header: /AntIDE/source/ant/codecontext/classdesigner/CDAttrInfo.java 2     99-05-16 11:34p Multipia $
 * $Revision: 2 $
 * Part : Class Designer Attribute Information.
 * Author: Kim, Sung-hoon(kahn@antj.com)
 * Dated at 1999.1.20.21.22.
 */

package com.antsoft.ant.codecontext.classdesigner;

/**
  @author Kim, Sung-hoon.
  */
public class CDAttrInfo 
{
	public static final String PUBLIC="public";
	public static final String PROTECTED="protected";
	public static final String PRIVATE="private";
	public static final String NOMODIFIER="";

	private String accessType=NOMODIFIER;	// It's value is PUBLIC,PROTECTED,PRIVATE,NOMODIFIER

	private String type=null;
	private String attrName=null;
	private String initValue=null;

	private boolean getterAttr;
	private boolean setterAttr;
	private boolean staticAttr;
	private boolean finalAttr;
	private boolean transientAttr;
	private boolean volatileAttr;

	private String description=null;

	//private JavaDocInfo javaDocInfo=null;

	//private StringBuffer tabString=new StringBuffer();

	/**
	  setting the access type of the attribute.
	  
	  @param accessType the access type(public, protected, private)
	  */
	public void setAccessType(String accessType) {
		this.accessType=accessType;
	}

	/**
	  setting the attribute type(int,float..., classname or interfacename...).

	  @param type the string of the type.
	  */
	public void setType(String type) {
		this.type=type;
	}

	/**
	  setting the attribute name as the string.

	  @param name the string of the name.
	  */
	public void setAttrName(String name) {
		attrName=name;
	}

	/**
	  setting the initial value as the string.

	  @param value the string of initial value
	  */
	public void setInitValue(String value) {
		initValue=value;
	}

	/**
	  switching the Getter method on/off

	  @param flag ON/OFF flag
	  */
	public void setGetter(boolean flag) {
		getterAttr=flag;
	}

	/**
	  switching the Setter method on/off

	  @param flag ON/OFF flag
	  */
	public void setSetter(boolean flag) {
		setterAttr=flag;
	}

	/**
	  switching the attribute is static or not.

	  @param flag static ON/OFF flag
	  */
	public void setStatic(boolean flag) {
		staticAttr=flag;
	}

	/**
	  switching the attribute is final or not.

	  @param flag final ON/OFF flag
	  */
	public void setFinal(boolean flag) {
		finalAttr=flag;
	}

	/**
	  switching the attribute is transient or not.

	  @param flag transient ON/OFF flag
	  */
	public void setTransient(boolean flag) {
		transientAttr=flag;
	}

	/**
	  switching the attribute is volatile or not.

	  @param flag volatile ON/OFF flag
	  */
	public void setVolatile(boolean flag) {
		volatileAttr=flag;
	}

	/**
	  getting the access type of the attribute.

	  @return access type as the String value.
	  */
	public String getAccessType() {
		return accessType;
	}

	/**
	  getting the type of the attribute.

	  @return type as the string value.
	  */
	public String getType() {
		return type;
	}

	/**
	  getting the name of the attribute.

	  @return attribute name as the string value.
	  */
	public String getAttrName() {
		return attrName;
	}

	/**
	  getting the initial value of the attribute.

	  @return initial value as the string value.
	  */
	public String getInitValue() {
		if (initValue==null) return null;
		else return initValue;
	}

	/**
	  getting the getter flag.

	  @return getter flag as the boolean value.
	  */
	public boolean isGetter() {
		return getterAttr;
	}

	/**
	  getting the setter flag.

	  @return setter flag as the boolean value.
	  */
	public boolean isSetter() {
		return setterAttr;
	}

	/**
	  getting the static flag.

	  @return static flag as the boolean value.
	  */
	public boolean isStatic() {
		return staticAttr;
	}

	/**
	  getting the final flag.

	  @return final flag as the boolean value.
	  */
	public boolean isFinal() {
		return finalAttr;
	}

	/**
	  getting the transient flag.

	  @return transient flag as the boolean value.
	  */
	public boolean isTransient() {
		return transientAttr;
	}


	/**
	  getting the volatile flag.

	  @return volatile flag as the boolean value.
	  */
	public boolean isVolatile() {
		return volatileAttr;
	}

	private String comment=null;

	/**
	  setting the java documentain information of the attribute.

	  @param java documentain information as the String value.
	  */
	public void setJavaDoc(String doc) {
		comment=doc;
	}

	/**
	  getting the java documentain information of the attribute.

	  @return java documentain information as the String value.
	  */
	public String getJavaDoc() {
		return comment;
	}

	/**
	  Returns the string representation of the object. In general, It
	  returns a string that "textually represents" this object.

	  @return a string representation of the object.
	  */
	public String toString() {
		StringBuffer theCode=new StringBuffer("");
		String str=null;
		String blank=" ";
		String endLine="\n";
		String tab="\t";
		String startComment="/** ";

		if (comment!=null) {
			//theCode.append(tab+"/**"+endLine+tab);
			//theCode.append(tab+" * "+comment);
			//theCode.append(endLine+tab+"*/"+endLine);
			if (getAccessType().equals("private")) theCode.append(tab+"/* "+comment+" */"+endLine);
			else theCode.append(tab+startComment+comment+" */"+endLine);
		}

		theCode.append(tab);         

		str=getAccessType();
		if (str.length()>3) theCode.append(str+blank);

		if (isStatic()) theCode.append("static ");

		if (isFinal()) theCode.append("final ");

		//if (isSynchronous()) theCode.append("synchronized ");

		if (isTransient()) theCode.append("transient ");

		if (isVolatile()) theCode.append("volatile ");

		theCode.append(getType()+blank);

		theCode.append(getAttrName()+blank);

		str=getInitValue();
		if (str!=null&&!str.equals("")) theCode.append("= "+str);

		theCode.append(";"+endLine+endLine);

		/*
		if (isGetter()) {
			theCode.append("public "+getType()+" get"+getAttrName()+"() {"+endLine);
			theCode.append(TabString+"return "+getAttrName()+";"+endLine);
			theCode.append("}"+endLine);
		}

		if (isSetter()) {
			theCode.append("public void set"+getAttrName()+"("+getType()+blank+"formalParam) {"+endLine);
			theCode.append(TabString+getAttrName()+"=formalParam;"+endLine);
			theCode.append("}"+endLine);
		}
		*/
//System.out.println("the attr info==== "+theCode.toString());

		return theCode.toString();
	}
}
