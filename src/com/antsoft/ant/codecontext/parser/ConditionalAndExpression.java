/*
 * ConditionalAndExpression.java
 * Title: °³¹Ì
 * Part : ConditionalAndExpression class(tree node)
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
  the non terminal ConditionalAndExpression node.
 
  @author Kim, sung-hoon
 */
public class ConditionalAndExpression extends TreeNode {
	/**
	  Constructor.
	  the node number : 106
	 
	  @param x the line number.
	  @param y the column number.
	 */
	public ConditionalAndExpression(int x,int y) {
		super(x,y,NonTerminal.CONDITIONALANDEXPRESSION);
	}
	
	/**
	  It converts the subtree into the string.
	  
	  @return converted string.
	 */
	public String toString() {
	    if (sizeOfChild()==1) return super.toString();
	    
	    return getChild().toString()+"&&"+getChild().toString();
	}
}
