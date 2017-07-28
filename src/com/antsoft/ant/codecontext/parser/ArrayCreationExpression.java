/*
 * ArrayCreationExpression.java
 * Title: °³¹Ì
 * Part : ArrayCreationExpression class(tree node)
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
  the non terminal ArrayCreationExpression node.
 
  @author Kim, sung-hoon
 */
public class ArrayCreationExpression extends TreeNode {
	/**
	  Constructor.
	  the node number : 130
	 
	  @param x the line number.
	  @param y the column number.
	 */
	public ArrayCreationExpression(int x,int y) {
		super(x,y,NonTerminal.ARRAYCREATIONEXPRESSION);
	}
	
	/**
	  It converts the subtree into the string.
	  
	  @return converted string.
	 */
	public String toString() {
	    return "new "+super.toString();
	}
}
