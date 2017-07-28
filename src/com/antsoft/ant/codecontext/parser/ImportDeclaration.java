/*
 * ImportDeclaration.java
 * Title: °³¹Ì
 * Part : ImportDeclaration class(tree node)
 * Copyright (c) 1998 Ant Company, all right reserved.
 * Author: Kim, Sung-hoon(kahn@antj.com)
 * Company: Antsoft
 * Description: Integrated CASE Tool(Ant)
 * Dated at 99.1.13.
 *
 * Version 1.0.0
 */

package ant.codecontext.parser;

public class ImportDeclaration extends TreeNode {
	/**
	 * Constructor.
	  the node number : 7
	 *
	 * @param x the line number
	 * @param y the line number
	 */
	public ImportDeclaration(int x,int y) {
		super(x,y,NonTerminal.IMPORTDECLARATION);
	}
}
