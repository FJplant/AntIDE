/*
 * BlockStatements.java
 * Title: °³¹Ì
 * Part : BlockStatements class(tree node)
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
  the non terminal BlockStatements node.
  BlockStatements ::= BlockStatement
                    | BlockStatements BlockStatement
 
  @author Kim, sung-hoon
 */
public class BlockStatements extends TreeNode {
	/**
	  Constructor.
	  the node number : 60
	 
	  @param x the line number.
	  @param y the column number.
	 */
	public BlockStatements(int x,int y) {
		super(x,y,NonTerminal.BLOCKSTATEMENTS);
	}
}
