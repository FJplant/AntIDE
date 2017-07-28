/*
 * BlockStatement.java
 * Title: °³¹Ì
 * Part : BlockStatement class(tree node)
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
  the non terminal BlockStatement node.
 
  @author Kim, sung-hoon
 */
public class BlockStatement extends TreeNode {
	/**
	  Constructor.
	  the node number : 61
	 
	  @param x the line number.
	  @param y the column number.
	 */
	public BlockStatement(int x,int y) {
		super(x,y,NonTerminal.BLOCKSTATEMENT);
	}
}
