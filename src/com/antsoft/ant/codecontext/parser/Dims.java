/*
 * Dims.java
 * Title: °³¹Ì
 * Part : Dims class(tree node)
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
  the non terminal Dims node.
 
  @author Kim, sung-hoon
 */
public class Dims extends TreeNode {
	/**
	  Constructor.
	  the node number : 133
	 
	  @param x the line number.
	  @param y the column number.
	 */
	public Dims(int x,int y) {
		super(x,y,NonTerminal.DIMS);
	}
	
	/**
	  It converts the subtree into the string.
	  
	  @return converted string.
	 */
	public String toString() {
	    if (sizeOfChild()==0) return "[]";
	    
	    return super.toString()+"[]";
	}
}
