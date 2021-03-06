/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/codecontext/ClassMember.java,v 1.4 1999/08/20 01:43:30 kahn Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.4 $
 * author: Kim, Sung-hoon(kahn@antj.com)
 * Starting at 1999. 2. 10.
 */
package com.antsoft.ant.codecontext;

import java.util.Vector;

/**
  @author Kim, sung-hoon.
  */
public class ClassMember {
	/**
	  default Constructor.
	  */
	public ClassMember() {}

	private String name="";
	private String type="";
	private String accessType;
	private Vector parameterTypes;
  private Vector parameter;
	private int memberType;

  private boolean isStatic = false;

	/**
	  the final variable for access type.
	  */
	public static final String PUBLIC="public";
	public static final String PROTECTED="protected";
	public static final String PACKAGE="package";
	public static final String PRIVATE="private";

	public static final int FIELD=1;
	public static final int METHOD=2;
	public static final int INNERCLASS=3;

	/**
	  setting the member name with package name and class name(that is, full name).

	  @param fullname the full member name.
	  */
	public void setName(String fullname) {
		name=fullname;
	}

	/**
	  setting the member type.

	  @param type the member type.
	  */
	public void setType(String type) {
		this.type=type;
	}

	/**
	  setting the member access type.

	  @param type the member access type.
	  */
	public void setAccessType(String type) {
		accessType=type;
	}

	/**
	  setting the member type of the kind.

	  @param type the member kind type.
	  */
	public void setMemberType(int type) {
		memberType=type;
	}

  public void setStatic(boolean isStatic) {
  	this.isStatic = isStatic;
  }

  public boolean isStatic() {
  	return isStatic;
  }

	/**
	  setting the member parameter list.

	  @param params the parameter list as String[].
	  */
	public void setParameterTypes(String[] params) {
    parameterTypes=new Vector(params.length);
    for (int i=0;i<params.length;++i) parameterTypes.addElement(params[i]);
	}

	/**
	  adding one parameter type into the parameter list.

	  @param param the parameter type to be added.
	  */
	public void addParameterType(String param) {
    if (parameterTypes==null) parameterTypes=new Vector();
    parameterTypes.addElement(param);
	}

	/**
	  getting the member name with package name and class name(that is, full name).

	  @return the full member name.
	  */
	public String getName() {
		return name;
	}

	/**
	  getting the member name with package name and class name(that is, full name) if the method..

	  @return the full member name.
	  */
	public String getFullName() {
		if (memberType!=METHOD) return getName();

		//System.out.println("the name = "+name);
		if (parameterTypes==null) return name+"()";

		StringBuffer buf=new StringBuffer();
    for (int i=0;i<parameterTypes.size();++i) buf.append(parameterTypes.elementAt(i)+",");
		return name+"("+buf.toString().substring(0,buf.length()-1)+")";
	}

	/**
	  getting the member type.

	  @return the member type.
	  */
	public String getType() {
		return type;
	}

	/**
	  getting the member access type.

	  @return the member access type.
	  */
	public String getAccessType() {
		return accessType;
	}

	/**
	  getting the member type of the kind.

    @return the member kind type.
	  */
	public int getMemberType() {
		return memberType;
	}

	/**
	  getting the member parameter list.

	  @return the parameter list as String[].
	  */
	public String[] getParameterTypes() {
    String[] array=new String[parameterTypes.size()];
    for (int i=0;i<parameterTypes.size();++i) array[i]=(String)parameterTypes.elementAt(i);
		return array;
	}

	/**
	  getting the member parameter list as the string only.

	  @return the parameter list as String.
	  */
	public String getParamsAsString() {
    //System.out.println(" parameter as string..............");
		if (parameter==null) return null;

		StringBuffer buf=new StringBuffer();
    for (int i=0;i<parameter.size();++i) buf.append(parameter.elementAt(i)+",");
    //System.out.println(" buffered string ==> "+buf.toString());
		return buf.toString().substring(0,buf.length()-1);
	}

  public void addParameter(String param) {
    //System.out.println(" parameter ==> "+param);
    if (parameter==null) parameter=new Vector();
    parameter.addElement(param);
  }

  public Vector getParameter() {
    return parameter;
  }

	/**
	  return String representation of the member with signature.

	  @return the String representation.
	  */
	public String getSig() {
		switch (memberType) {
			case FIELD: return name+":"+type;
			case INNERCLASS: return name+":";
			case METHOD: return name+":"+type;
			default:return "";
		}
	}

	/**
	  return String representation of this object.

	  @return the String representation.
	  */
	public String toString() {
		switch (memberType) {
			case FIELD:
			case INNERCLASS:
				return name;

			case METHOD:
        StringBuffer buf=new StringBuffer();
        if (parameterTypes==null) return name+"()";
        for (int i=0;i<parameterTypes.size();i++) buf.append(parameterTypes.elementAt(i)+",");
				return name+"("+buf.toString().substring(0,buf.length()-1)+")";

			default:return "";
		}
	}
}
