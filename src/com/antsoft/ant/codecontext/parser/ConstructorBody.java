/*
 * ConstructorBody.java
 * Title: °³¹Ì
 * Part : ConstructorBody class(tree node)
 * Copyright (c) 1998 Ant Company, all right reserved.
 * Author: Kim, Sung-hoon(kahn@antj.com)
 * Company: Antsoft
 * Description: Integrated CASE Tool(Ant)
 * Dated at 99.1.13.
 *
 * Version 1.0.0
 *   - constructor, toString()
 */

package ant.codecontext.parser;

/**
  the non terminal ConstructorBody node.
 
  @author Kim, sung-hoon
 */
public class ConstructorBody extends TreeNode {
	/**
	  Constructor.
	  the node number : 38
	 
	  @param x the line number.
	  @param y the column number.
	 */
	public ConstructorBody(int x,int y) {
		super(x,y,NonTerminal.CONSTRUCTORBODY);
	}

	/**
	  It converts the subtree into the string.

	  @return converted string.
	 */
	public String toString() {
		return "{"+super.toString()+"}";
	}
}
