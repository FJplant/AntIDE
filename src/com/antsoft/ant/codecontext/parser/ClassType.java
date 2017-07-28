/*
 * ClassType.java
 * Title: °³¹Ì
 * Part : ClassType class(tree node)
 * Copyright (c) 1998 Ant Company, all right reserved.
 * Author: Kim, Sung-hoon(kahn@antj.com)
 * Company: Antsoft
 * Description: Integrated CASE Tool(Ant)
 * Dated at 99.1.13.
 *
 * Version 1.0.0 (starting)
 */

package ant.codecontext.parser;

/**
  the non terminal ClassType node

  @author Kim, sung-hoon
 */
public class ClassType extends TreeNode {
	/**
	  Constructor.
	  the node number : 56

	  @param x the line number
	  @param y the column number
	 */
	public ClassType(int x,int y) {
		super(x,y,NonTerminal.CLASSTYPE);
	}
}
