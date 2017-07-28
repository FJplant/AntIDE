/*
 * IfThenStatement.java
 * Title: °³¹Ì
 * Part : IfThenStatement class(tree node)
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
  the non terminal IfThenStatement node.
 
  @author Kim, sung-hoon
 */
public class IfThenStatement extends TreeNode {
	/**
	  Constructor.
	  the node number : 67.
	 
	  @param x the line number.
	  @param y the column number.
	 */
	public IfThenStatement(int x,int y) {
		super(x,y,NonTerminal.IFTHENSTATEMENT);
	}
	
	/**
	  It converts the subtree into the string.
	  
	  @return the converted string.
	 */
	public String toString() {
	    StringBuffer buffer=new StringBuffer();
	    TreeNode node=getChild();
	    
	    buffer.append("if (");
	    buffer.append(node.toString());
	    buffer.append(")");
	    
	    node=getChild();
	    buffer.append(node.toString());
	    
	    return buffer.toString();
	}
}
