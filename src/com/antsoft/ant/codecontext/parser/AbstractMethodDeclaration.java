/*
 * AbstractMethodDeclaration.java
 * Title: ����
 * Part : AbstractMethodDeclaration class(tree node)
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
  the non terminal AbstractMethodDeclaration node.
 
  @author Kim, sung-hoon
 */
public class AbstractMethodDeclaration extends TreeNode {
	/**
	  Constructor.
	  type number : 46
	 
	  @param x the line number.
	  @param y the column number.
	 */
	public AbstractMethodDeclaration(int x,int y) {
		super(x,y,NonTerminal.ABSTRACTMETHODDECLARATION);
	}

	/**
	  It converts the subtree into the string.

	  @return converted string.
	 */
	public String toString() {
		return super.toString()+";";
	}
}
