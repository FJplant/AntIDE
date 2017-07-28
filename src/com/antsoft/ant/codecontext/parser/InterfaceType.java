/*
 * InterfaceType.java
 * Title: °³¹Ì
 * Part : InterfaceType class(tree node)
 * Copyright (c) 1998 Ant Company, all right reserved.
 * Author: Kim, Sung-hoon(kahn@antj.com)
 * Company: Antsoft
 * Description: Integrated CASE Tool(Ant)
 * Dated at 99.1.13.
 *
 * Version 1.0.0 (starting)
 *   - the only constructor is defined.
 */

package ant.codecontext.parser;

/**
  the non terminal symbol InterfaceType node.

  @author Kim, sung-hoon.
 */
public class InterfaceType extends TreeNode {
	/**
	  Constructor.
	  the node number : 57

	  @param x the line number.
	  @param y the column number.
	 */
	public InterfaceType(int x,int y) {
		super(x,y,NonTerminal.INTERFACETYPE);
	}
}
