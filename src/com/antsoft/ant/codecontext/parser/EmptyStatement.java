/*
 * EmptyStatement.java
 * Title: °³¹Ì
 * Part : EmptyStatement class(tree node)
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
  the non terminal EmptyStatement node.
 
  @author Kim, sung-hoon
 */
public class EmptyStatement extends TreeNode {
	/**
	  Constructor.
	  the node number : 70
	 
	  @param x the line number.
	  @param y the column number.
	 */
	public EmptyStatement(int x,int y) {
		super(x,y,NonTerminal.EMPTYSTATEMENT);
	}
	
	/**
	  It returns the contained string. But It has only semicolon(";"),
	  so it returns the ";".
	  
	  @return the string ";"
	 */
	public String toString () {
	    return ";";
	}
}
