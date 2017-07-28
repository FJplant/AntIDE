/*
 * AssingmentExpression.java
 * Title: °³¹Ì
 * Part : AssingmentExpression class(tree node)
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
  the non terminal AssingmentExpression node.
 
  @author Kim, sung-hoon
 */
public class AssignmentExpression extends TreeNode {
	/**
	  Constructor.
	  the node number : 100.
	 
	  @param x the line number.
	  @param y the column number.
	 */
	public AssignmentExpression(int x,int y) {
		super(x,y,NonTerminal.ASSIGNMENTEXPRESSION);
	}
}
