/*
 * ContinueStatement.java
 * Title: °³¹Ì
 * Part : ContinueStatement class(tree node)
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
  the non terminal ContinueStatement node.
 
  @author Kim, sung-hoon
 */
public class ContinueStatement extends TreeNode {
	/**
	  Constructor.
	  the node number : 90
	 
	  @param name the continue label.
	  @param x the line number.
	  @param y the column number.
	 */
	public ContinueStatement(String name,int x,int y) {
		this(x,y);
		contiLabel=name;
	}
	
	/**
	  Constructor.
	  the node number : 90
	 
	  @param x the line number.
	  @param y the column number.
	 */
	public ContinueStatement(int x,int y) {
		super(x,y,NonTerminal.CONTINUESTATEMENT);
	}
	
	private String contiLabel=null;
	
	/**
	  It converts the subtree into the string.
	  
	  @return converted string.
	 */
	public String toString() {
	    if (contiLabel==null) return "continue;";
	    else return "continue "+contiLabel+";";
	}

	/**
	  setting the Conti label name.

	  @param name the Conti label name.
	  */
	public void setContiLabel(String name) {
		contiLabel=name;
	}

	/**
	  getting the Conti label name.

	  @return the Conti label name.
	  */
	public String setContiLabel() {
		return contiLabel;
	}
}
