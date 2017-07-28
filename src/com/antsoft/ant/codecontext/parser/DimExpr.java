/*
 * DimExpr.java
 * Title: °³¹Ì
 * Part : DimExpr class(tree node)
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
  the non terminal DimExpr node.
 
  @author Kim, sung-hoon
 */
public class DimExpr extends TreeNode {
	/**
	  Constructor.
	  the node number : 132
	 
	  @param x the line number.
	  @param y the column number.
	 */
	public DimExpr(int x,int y) {
		super(x,y,NonTerminal.DIMEXPR);
	}
	
	/**
	  It converts the subtree into the string.
	  
	  @return converted string.
	 */
	public String toString() {
	    return "["+super.toString()+"]";
	}
}
