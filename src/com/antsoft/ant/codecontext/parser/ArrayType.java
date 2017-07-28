/*
 * ArrayType.java
 * Title: °³¹Ì
 * Part : ArrayType class(tree node)
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
  the non terminal ArrayType node.
 
  @author Kim, sung-hoon
 */
public class ArrayType extends TreeNode {
	/**
	  Constructor.
	  the node number : 58
	 
	  @param x the line number.
	  @param y the column number.
	 */
	public ArrayType(int x,int y) {
		super(x,y,NonTerminal.ARRAYTYPE);
	}
	
	/**
	  It converts the subtree into the string.
	  
	  @return converted string.
	 */
	public String toString() {
		return super.toString()+"[]";
	}
}
