/*
 * MethodDeclaration.java
 * Title: °³¹Ì
 * Part : MethodDeclaration class(tree node)
 * Copyright (c) 1998 Ant Company, all right reserved.
 * Author: Kim, Sung-hoon(kahn@antj.com)
 * Company: Antsoft
 * Description: Integrated CASE Tool(Ant)
 * Dated at 99.1.13.
 *
 * Version 1.0.0
 *   - only constructor is defined.
 */

package ant.codecontext.parser;

/**
 * the non terminal MethodDeclaration node
 *
 * @author Kim, sung-hoon
 */
public class MethodDeclaration extends TreeNode {
	/**
	  Constructor.
	  the node number : 27
	  
	  @param x the line number.
	  @param y the column number.
	 */
	public MethodDeclaration(int x,int y) {
		super(x,y,NonTerminal.METHODDECLARATION);
	}
}
