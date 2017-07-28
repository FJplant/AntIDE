/*
 * Expression.java
 * Title: °³¹Ì
 * Part : Expression class(tree node)
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
  the non terminal Expression node.
 
  @author Kim, sung-hoon
 */
public class Expression extends TreeNode {
	/**
	  Constructor.
	  the node number : 99
	 
	  @param x the line number.
	  @param y the column number.
	 */
	public Expression(int x,int y) {
		super(x,y,NonTerminal.EXPRESSION);
	}
}
