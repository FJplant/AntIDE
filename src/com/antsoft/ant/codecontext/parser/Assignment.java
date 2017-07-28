/*
 * Assignment.java
 * Title: °³¹Ì
 * Part : Assignment class(tree node)
 * Copyright (c) 1998 Ant Company, all right reserved.
 * Author: Kim, Sung-hoon(kahn@antj.com)
 * Company: Antsoft
 * Description: Integrated CASE Tool(Ant)
 * Dated at 99.1.13
 *
 * Version 1.0.0
 */

package ant.codecontext.parser;

/**
  the non terminal Assignment node.
 
  @author Kim, sung-hoon
 */
public class Assignment extends TreeNode {
	/**
	  Constructor.
	  the node number : 101.
	 
	  @param x the line number.
	  @param y the column number.
	 */
	public Assignment(int x,int y) {
		super(x,y,NonTerminal.ASSIGNMENT);
	}
}
