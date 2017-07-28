/*
 * FormalParameterList.java
 * Title: °³¹Ì
 * Part : FormalParameterList class(tree node)
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
  the non terminal FormalParameterList node.
 
  @author Kim, sung-hoon
 */
public class FormalParameterList extends TreeNode {
	/**
	  Constructor.
	  the node number : 30
	 
	  @param x the line number.
	  @param y the column number.
	 */
	public FormalParameterList(int x,int y) {
		super(x,y,NonTerminal.FORMALPARAMETERLIST);
	}

	/**
	  It converts the subtree into the string.

	  @return converted string.
	 */
	public String toString() {
		if (sizeOfChild()==1) return super.toString();

		TreeNode node=getChild();
		StringBuffer buffer=new StringBuffer("");

		buffer.append(node.toString());
		node=getChild();
		buffer.append(","+node.toString());
		return buffer.toString();
	}
}
