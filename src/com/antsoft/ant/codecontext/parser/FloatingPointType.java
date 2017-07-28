/*
 * FloatingPointType.java
 * Title: °³¹Ì
 * Part : FloatingPointType class(tree node)
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
  the non terminal FloatingPointType node.
 
  @author Kim, sung-hoon
 */
public class FloatingPointType extends TreeNode {
	/**
	  Constructor.
	  the node number : 53
	 
	  @param x the line number.
	  @param y the column number.
	  @param type the type name(float,double)
	 */
	public FloatingPointType(int x,int y,String type) {
		this(x,y);
		typeName=type;
	}

	/**
	  Constructor.
	  the node number : 53
	 
	  @param x the line number.
	  @param y the column number.
	 */
	public FloatingPointType(int x,int y) {
		super(x,y,NonTerminal.FLOATINGPOINTTYPE);
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
