/*
 * ConstructorDeclarator.java
 * Title: °³¹Ì
 * Part : ConstructorDeclarator class(tree node)
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
  the non terminal ConstructorDeclarator node.
 
  @author Kim, sung-hoon
 */
public class ConstructorDeclarator extends TreeNode {
	/**
	  Constructor.
	  the node number : 37
	 
	  @param x the line number.
	  @param y the column number.
	 */
	public ConstructorDeclarator(int x,int y) {
		super(x,y,NonTerminal.CONSTRUCTORDECLARATOR);
	}

	/**
	  Constructor.
	 
	  @param x the line number.
	  @param y the column number.
	  @param param parameterlist indicator.
	 */
	public ConstructorDeclarator(int x,int y,boolean param) {
		this(x,y);
		hasparam=param;
	}

	private boolean hasparam=false;

	/**
	  It returns class name.

	  @return class name.
	 */
	public String getId() {
		return getChild(0).toString();
	}

	/**
	  It converts the subtree into the string.

	  @return converted string.
	 */
	public String toString() {
		if (hasparam) 
		    return getId()+"("+getChild(1).toString()+")";
		else return super.toString()+"()";
	}
}
