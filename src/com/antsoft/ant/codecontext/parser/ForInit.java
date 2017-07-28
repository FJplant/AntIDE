/*
 * ForInit.java
 * Title: °³¹Ì
 * Part : ForInit class(tree node)
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
  the non terminal ForInit node.
 
  @author Kim, sung-hoon
 */
public class ForInit extends TreeNode {
	/**
	  Constructor.
	  the node number : 86
	 
	  @param x the line number.
	  @param y the column number.
	 */
	public ForInit(int x,int y) {
		super(x,y,NonTerminal.FORINIT);
	}
}
