/*
 * InterfaceBody.java
 * Title: °³¹Ì
 * Part : InterfaceBody class(tree node)
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
  the non terminal InterfaceBody node.
 
  @author Kim, sung-hoon
 */
public class InterfaceBody extends TreeNode {
	/**
	  Constructor.
	  the node number : 42
	 
	  @param x the line number.
	  @param y the column number.
	 */
	public InterfaceBody(int x,int y) {
		super(x,y,NonTerminal.INTERFACEBODY);
	}

	/**
	  It converts the subtree into the string.

	  @return converted string.
	 */
	public String toString() {
		return "{"+super.toString()+"}";
	}
}
