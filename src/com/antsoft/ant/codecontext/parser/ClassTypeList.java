/*
 * ClassTypeList.java
 * Title: °³¹Ì
 * Part : ClassTypeList class(tree node)
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
  the non terminal ClassTypeList node.
 
  @author Kim, sung-hoon
 */
public class ClassTypeList extends TreeNode {
	/**
	  Constructor.
	  the node number : 33
	 
	  @param x the line number.
	  @param y the column number.
	 */
	public ClassTypeList(int x,int y) {
		super(x,y,NonTerminal.CLASSTYPELIST);
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
