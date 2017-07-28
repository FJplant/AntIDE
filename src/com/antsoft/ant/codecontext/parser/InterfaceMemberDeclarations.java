/*
 * InterfaceMemberDeclarations.java
 * Title: °³¹Ì
 * Part : InterfaceMemberDeclarations class(tree node)
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
  the non terminal InterfaceMemberDeclarations node.
 
  @author Kim, sung-hoon
 */
public class InterfaceMemberDeclarations extends TreeNode {
	/**
	  Constructor.
	  the node number : 43
	 
	  @param x the line number.
	  @param y the column number.
	 */
	public InterfaceMemberDeclarations(int x,int y) {
		super(x,y,NonTerminal.INTERFACEMEMBERDECLARATIONS);
	}
}
