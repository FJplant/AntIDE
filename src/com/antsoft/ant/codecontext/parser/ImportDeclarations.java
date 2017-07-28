/*
 * ImportDeclarations.java
 * Title: °³¹Ì
 * Part : ImportDeclarations class(tree node)
 * Copyright (c) 1998 Ant Company, all right reserved.
 * Author: Kim, Sung-hoon(kahn@antj.com)
 * Company: Antsoft
 * Description: Integrated CASE Tool(Ant)
 * Dated at 99.1.13.
 *
 * Version 1.0.0
 *   - visitor(), addChild() method is deleted.
 *   - only constructor is defined.
 */

package ant.codecontext.parser;

/**
 * It will have import declaration list.
 *
 * @author Kim, sung-hoon
 */
public class ImportDeclarations extends TreeNode {
	/**
	  Constructor.
	  the node number : 6
	  
	  @param x the line number.
	  @param y the column number.
	 */
	public ImportDeclarations(int x,int y) {
		super(x,y,NonTerminal.IMPORTDECLARATIONS);
	}
}
