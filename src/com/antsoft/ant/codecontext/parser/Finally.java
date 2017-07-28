/*
 * Finally.java
 * Title: °³¹Ì
 * Part : Finally class(tree node)
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
  the non terminal Finally node.
 
  @author Kim, sung-hoon
 */
public class Finally extends TreeNode {
	/**
	  Constructor.
	  the node number : 97
	 
	  @param x the line number.
	  @param y the column number.
	 */
	public Finally(int x,int y) {
		super(x,y,NonTerminal.FINALLY);
	}
	
	/**
	  It converted the subtree into the string.
	  
	  @return the converted string.
	 */
	public String toString() {
	    return "finally "+super.toString();
	}
}
