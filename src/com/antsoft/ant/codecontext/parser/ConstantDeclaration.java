/*
 * ConstantDeclaration.java
 * Title: °³¹Ì
 * Part : ConstantDeclaration class(tree node)
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
  the non terminal ConstantDeclaration node.
 
  @author Kim, sung-hoon
 */
public class ConstantDeclaration extends TreeNode {
	/**
	  Constructor.
	  the node number : 45
	 
	  @param x the line number.
	  @param y the column number.
	 */
	public ConstantDeclaration(int x,int y) {
		super(x,y,NonTerminal.CONSTANTDECLARATION);
	}
}
