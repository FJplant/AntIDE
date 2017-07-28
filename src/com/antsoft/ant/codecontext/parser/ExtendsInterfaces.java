/*
 * ExtendsInterfaces.java
 * Title: °³¹Ì
 * Part : ExtendsInterfaces class(tree node)
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
  the non terminal ExtendsInterfaces node.
 
  @author Kim, sung-hoon
 */
public class ExtendsInterfaces extends TreeNode {
	/**
	  Constructor.
	  the node number : 41
	 
	  @param x the line number.
	  @param y the column number.
	 */
	public ExtendsInterfaces(int x,int y) {
		super(x,y,NonTerminal.EXTENDSINTERFACES);
	}

	/**
	  Constructor.
	 
	  @param x the line number.
	  @param y the column number.
	  @param leaf leaf node indicator.
	 */
	public ExtendsInterfaces(int x,int y,boolean leaf) {
		this(x,y);
		isLeaf=leaf;
	}

	private boolean isLeaf=false;

	/**
	  It converts the subtree into the string.

	  @return converted string.
	 */
	public String toString() {
		if (isLeaf) return "extends "+super.toString();
		else return getChild().toString()+","+getChild().toString();
	}

	/**
	  setting the flag that is a leaf node or not.

	  @param flag a leaf node flag.
	  */
	public void setIsLeaf(boolean flag) {
		isLeaf=flag;
	}

	/**
	  getting the flag that is a leaf node or not.

	  @return a leaf node flag.
	  */
	public boolean isLeaf() {
		return isLeaf;
	}
}
