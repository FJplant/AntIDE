/*
 * BreakStatement.java
 * Title: °³¹Ì
 * Part : BreakStatement class(tree node)
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
  the non terminal BreakStatement node.
 
  @author Kim, sung-hoon
 */
public class BreakStatement extends TreeNode {
	/**
	  Constructor.
	  the node number : 89
	 
	  @param name the break label.
	  @param x the line number.
	  @param y the column number.
	 */
	public BreakStatement(String name,int x,int y) {
		this(x,y);
		breakLabel=name;
	}
	
	/**
	  Constructor.
	  the node number : 89
	 
	  @param x the line number.
	  @param y the column number.
	 */
	public BreakStatement(int x,int y) {
		super(x,y,NonTerminal.BREAKSTATEMENT);
	}
	
	private String breakLabel=null;
	
	/**
	  It converts the subtree into the string.
	  
	  @return converted string.
	 */
	public String toString() {
	    if (breakLabel==null) return "break;";
	    else return "break "+breakLabel+";";
	}

	/**
	  setting the break label name.

	  @param name the break label name.
	  */
	public void setBreakLabel(String name) {
		breakLabel=name;
	}

	/**
	  getting the break label name.

	  @return the break label name.
	  */
	public String setBreakLabel() {
		return breakLabel;
	}
}
