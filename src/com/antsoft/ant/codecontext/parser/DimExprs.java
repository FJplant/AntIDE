/*
 * DimExprs.java
 * Title: °³¹Ì
 * Part : DimExprs class(tree node)
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
  the non terminal DimExprs node.
 
  @author Kim, sung-hoon
 */
public class DimExprs extends TreeNode {
	/**
	  Constructor.
	  the node number : 131
	 
	  @param x the line number.
	  @param y the column number.
	 */
	public DimExprs(int x,int y) {
		super(x,y,NonTerminal.DIMEXPRS);
	}
}
