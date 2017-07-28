/*
 * Interfaces.java
 * Title: °³¹Ì
 * Part : Interfaces class(tree node)
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
  the non terminal Interfaces node

  @author Kim, sung-hoon
 */
public class Interfaces extends TreeNode {
	/**
	  Constructor.
	  the node number : 16

	  @param x the line number.
	  @param y the column number.
	 */
	public Interfaces(int x,int y) {
		super(x,y,NonTerminal.INTERFACES);
	}

	/**
	  It converts the subtree into string.

	  @return converted string.
	 */
	public String toString() {
		return ("implements "+super.toString());
	}
}
