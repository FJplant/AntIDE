/*
 * ConstructorDeclaration.java
 * Title: °³¹Ì
 * Part : ConstructorDeclaration class(tree node)
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
  the non terminal ConstructorDeclaration node.
 
  @author Kim, sung-hoon
 */
public class ConstructorDeclaration extends TreeNode {
	/**
	  Constructor.
	  the node number : 36
	 
	  @param x the line number.
	  @param y the column number.
	 */
	public ConstructorDeclaration(int x,int y) {
		super(x,y,NonTerminal.CONSTRUCTORDECLARATION);
	}

	/**
	  Constructor.
	 
	  @param x the line number.
	  @param y the column number.
	  @param modifiers modifiers indicator.
	  @param thr throws indicator.
	 */
	public ConstructorDeclaration(int x,int y,int flag) {
		this(x,y);
		switch (flag) {
			case 3: hasModifier=true;
					hasThrow=true;
					break;
			case 2: hasModifier=true;
					break;
			case 1: hasThrow=true;
					break;
		}
	}

	private boolean hasModifier=false;
	private boolean hasThrow=false;

	/*
	  ConstructorDeclarator has the same Id as the Class Id and it returns
	  the identifier.

	  @return class identifier.
	public String getName() {
		if (hasModifiers) return getChild(1).toString();
		else return getChild(0).toString();
	}
	*/

	/**
	  setting the flag that indicates that has modifier or not.

	  @param flag the modifier flag.
	  */
	public void setHasModifier(boolean flag) {
		hasModifier=flag;
	}

	/**
	  setting the flag that indicates that has throws or not.

	  @param flag the throws flag.
	  */
	public void setHasThrow(boolean flag) {
		hasThrow=flag;
	}

	/**
	  getting the flag that indicates that has modifier or not.

	  @return the modifier flag.
	  */
	public boolean hasModifier() {
		return hasModifier;
	}

	/**
	  getting the flag that indicates that has throws or not.

	  @return the throws flag.
	  */
	public boolean hasThrow() {
		return hasThrow;
	}
}
