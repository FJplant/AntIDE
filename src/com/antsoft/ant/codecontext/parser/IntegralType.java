/*
 * IntegralType.java
 * Title: °³¹Ì
 * Part : IntegralType class(tree node)
 * Copyright (c) 1998 Ant Company, all right reserved.
 * Author: Kim, Sung-hoon(kahn@antj.com)
 * Company: Antsoft
 * Description: Integrated CASE Tool(Ant)
 * Dated at 99.1.13.
 *
 * Version 1.0.0
 */

package ant.codecontext.parser;

/**
  the non terminal IntegralType node.
 
  @author Kim, sung-hoon
 */
public class IntegralType extends TreeNode {
	/**
	  Constructor.
	  the node number : 52
	 
	  @param x the line number.
	  @param y the column number.
	  @param type the type name(byte,short,char,int,long)
	 */
	public IntegralType(int x,int y,String type) {
		this(x,y);
		typeName=type;
	}

	/**
	  Constructor.
	  the node number : 52
	 
	  @param x the line number.
	  @param y the column number.
	 */
	public IntegralType(int x,int y) {
		super(x,y,NonTerminal.INTEGRALTYPE);
	}
	
	
	protected String typeName=null;
	
	/**
	  It converts the subtree into the string.
	  
	  @return converted string.
	 */
	public String toString() {
	    return typeName;
	}

	/**
	  setting the type name.

	  @param name the type name.
	  */
	public void setTypeName(String name) {
		typeName=name;
	}

	/**
	  getting the type name.

	  @return the type name.
	  */
	public String getTypeName() {
		return typeName;
	}
}
