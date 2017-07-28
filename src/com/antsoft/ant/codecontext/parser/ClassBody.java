/*
 * ClassBody.java
 * Title: °³¹Ì
 * Part : ClassBody class(tree node)
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
  the non terminal symbol ClassBody node.
  ClassBody ::= { ClassBodyDeclarations<opt> }

  @author Kim, sung-hoon.
 */
public class ClassBody extends TreeNode {
	/**
	  Constructor.
	  the node number : 18

	  @param x the line number.
	  @param y the column number.
	 */
	public ClassBody(int x,int y) {
		super(x,y,NonTerminal.CLASSBODY);
	}

	/**
	  It converts the subtree into the string.

	  @return converted string.
	 */
	public String toString() {
		if (sizeOfChild()==0) return "{ }";

		return ("{\n"+super.toString()+"}\n");
	}
}
