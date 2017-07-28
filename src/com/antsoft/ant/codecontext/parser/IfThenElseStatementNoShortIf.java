/*
 * IfThenElseStatementNoShortIf.java
 * Title: °³¹Ì
 * Part : IfThenElseStatementNoShortIf class(tree node)
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
  the non terminal IfThenElseStatementNoShortIf node.
 
  @author Kim, sung-hoon
 */
public class IfThenElseStatementNoShortIf extends TreeNode {
	/**
	  Constructor.
	  the node number : 69.
	 
	  @param x the line number.
	  @param y the column number.
	 */
	public IfThenElseStatementNoShortIf(int x,int y) {
		super(x,y,NonTerminal.IFTHENELSESTATEMENTNOSHORTIF);
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
	    
	    node=getChild();
	    buffer.append(" else "+node.toString());

	    return buffer.toString();
	}
}
