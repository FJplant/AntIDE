/*
 * FormalParameter.java
 * Title: ����
 * Part : FormalParameter class(tree node)
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
  the non terminal FormalParameter node.
 
  @author Kim, sung-hoon
 */
public class FormalParameter extends TreeNode {
	/**
	  Constructor.
	  the node number : 31
	 
	  @param x the line number.
	  @param y the column number.
	 */
	public FormalParameter(int x,int y) {
		super(x,y,NonTerminal.FORMALPARAMETER);
	}

	/**
	  */
	public String toString() {
		return getChild().toString()+" "+getChild().toString();
	}
}
