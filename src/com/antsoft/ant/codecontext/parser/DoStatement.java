/*
 * DoStatement.java
 * Title: ����
 * Part : DoStatement class(tree node)
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
  the non terminal DoStatement node.
 
  @author Kim, sung-hoon
 */
public class DoStatement extends TreeNode {
	/**
	  Constructor.
	  the node number : 83
	 
	  @param x the line number.
	  @param y the column number.
	 */
	public DoStatement(int x,int y) {
		super(x,y,NonTerminal.DOSTATEMENT);
	}
	
	/**
	  It converts the subtree into the string.
	  
	  @return converted string.
	 */
	public String toString() {
	    StringBuffer buffer=new StringBuffer();
	    
	    TreeNode node=getChild();
	    
	    buffer.append("do");
	    buffer.append(node.toString());
	    buffer.append("while (");
	    node=getChild();
	    buffer.append(node.toString());
	    buffer.append(");");
	    
	    return buffer.toString();
	}
}
