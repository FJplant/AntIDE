/*
 * ClassOrInterfaceType.java
 * Title: °³¹Ì
 * Part : ClassOrInterfaceType class(tree node)
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
  the non terminal ClassOrInterfaceType node.
  ClassOrInterfaceType ::= Name 

  @author Kim, sung-hoon
 */
public class ClassOrInterfaceType extends TreeNode {
	/**
	  Constructor.
	  the node number : 55
	 
	  @param x the line number.
	  @param y the column number.
	 */
	public ClassOrInterfaceType(int x,int y) {
		super(x,y,NonTerminal.CLASSORINTERFACETYPE);
	}
}
