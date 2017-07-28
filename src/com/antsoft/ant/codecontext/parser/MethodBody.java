/*
 * MethodBody.java
 * Title: °³¹Ì
 * Part : MethodBody class(tree node)
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
  the non terminal MethodBody node.
  MethodBody ::= Block | ;
 
  @author Kim, sung-hoon.
 */
public class MethodBody extends TreeNode {
	/**
	  Constructor.
	  the node number : 34
	 
	  @param x the line number.
	  @param y the column number.
	 */
	public MethodBody(int x,int y) {
		super(x,y,NonTerminal.METHODBODY);
	}

	/**
	  It converts the subtree into the string.

	  @return the converted string.
	 */
	public String toString() {
		if (sizeOfChild()==1) return super.toString();
		else return ";";
	}
}
