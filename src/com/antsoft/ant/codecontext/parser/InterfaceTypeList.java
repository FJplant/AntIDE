/*
 * InterfaceTypeList.java
 * Title: °³¹Ì
 * Part : InterfaceTypeList class(tree node)
 * Copyright (c) 1998 Ant Company, all right reserved.
 * Author: Kim, Sung-hoon(kahn@antj.com)
 * Company: Antsoft
 * Description: Integrated CASE Tool(Ant)
 * Dated at 99.1.13.
 *
 * Version 1.0.0 (starting)
 */

package ant.codecontext.parser;

/**
  the non terminal symbol InterfaceTypeList node.

  @author Kim, sung-hoon.
 */
public class InterfaceTypeList extends TreeNode {
	/** 
	  Constructor.
	  the node number : 17

	  @param x the line number
	  @param y the column number
	 */
	public InterfaceTypeList(int x,int y) {
		super(x,y,NonTerminal.INTERFACETYPELIST);
	}

	/**
	  It converts the subtree into the string.

	  @return converted string.
	 */
	public String toString() {
		int hasChild=sizeOfChild();
		TreeNode node=getChild();

		if (hasChild==1) return node.toString();
		else {
			StringBuffer buffer=new StringBuffer();
			
			buffer.append(node.toString()+",");
			node=getChild();
			buffer.append(node.toString());

			return buffer.toString();
		}
	}
}
