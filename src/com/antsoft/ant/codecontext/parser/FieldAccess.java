/*
 * FieldAccess.java
 * Title: °³¹Ì
 * Part : FieldAccess class(tree node)
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
  the non terminal FieldAccess node.
 
  @author Kim, sung-hoon
 */
public class FieldAccess extends TreeNode {
	/**
	  Constructor.
	  the node number : 127.
	 
	  @param name the identifier name.
	  @param x the line number.
	  @param y the column number.
	 */
	public FieldAccess(String name,int x,int y) {
		this(x,y);
		this.name=name;
	}

	/**
	  Constructor.
	  the node number : 127.
	 
	  @param x the line number.
	  @param y the column number.
	 */
	public FieldAccess(int x,int y) {
		super(x,y,NonTerminal.FIELDACCESS);
	}
	
	private String name=null;
	
	/**
	  It converts the subtree into the string.
	  
	  @return converted string.
	 */
	public String toString() {
	    if (sizeOfChild()==0) return "super."+name;
	    
	    return super.toString()+"."+name;
	}

	/**
	  setting the field name.

	  @param name the field name.
	  */
	public void setName(String name) {
		this.name=name;
	}

	/**
	  getting the field name.

	  @return the field name.
	  */
	public String setName() {
		return name;
	}
}
