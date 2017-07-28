/*
 * MethodHeader.java
 * Title: °³¹Ì
 * Part : MethodHeader class(tree node)
 * Copyright (c) 1998 Ant Company, all right reserved.
 * Author: Kim, Sung-hoon(kahn@antj.com)
 * Company: Antsoft
 * Description: Integrated CASE Tool(Ant)
 * Dated at 99.1.13.
 *
 * Version 1.0.0
 *   - constructor, isvoid is defined.
 *   - toString() is defined.
 */

package ant.codecontext.parser;

/**
 * the non terminal MethodHeader node.
 * MethodHeader ::= Modifiers (void|Type) MethodDeclarator Throws
 *
 * @author Kim, sung-hoon.
 */
public class MethodHeader extends TreeNode {
	// indicates whether return type is void or not 
	boolean isVoid=false;

	boolean hasModifier=false;
	boolean hasThrow=false;

	/**
	  Constructor.
	  the node number : 28
	 
	  @param x the line number.
	  @param y the column number.
	  @param flag the optional indicator.
	  @param v the indicator that is void or not.
	 */
	public MethodHeader(int x,int y,int flag,boolean v) {
		super(x,y,NonTerminal.METHODHEADER);
		switch (flag) {
			case 3:hasModifier=true;
				   hasThrow=true;
				   break;
			case 2:hasModifier=true;
				   hasThrow=false;
				   break;
			case 1:hasThrow=true;
				   hasModifier=false;
				   break;
			default:hasModifier=false;
					hasThrow=false;
		}
		isVoid=v;
	}

	/**
	  It converts the sub tree into the string.

	  @return converted string.
	 */
	public String toString() {
		if (!isVoid) return super.toString();

		TreeNode node=getChild();
		StringBuffer buffer=new StringBuffer();

		if (hasModifier()) {
			buffer.append(node.toString());
			node=getChild();
		}
		buffer.append(" void ");
		buffer.append(node.toString());
		if (hasThrow()) {
			node=getChild();
			buffer.append(node.toString());
		}

		return buffer.toString();
	}

	/**
	  setting the flag that is void or not.

	  @param flag the void flag.
	  */
	public void setIsVoid(boolean flag) {
		isVoid=flag;
	}

	/**
	  getting the flag that is void or not.

	  @return the void flag.
	  */
	public boolean isVoid() {
		return isVoid;
	}

	/**
	  setting the flag that has modifier or not.

	  @param flag the modifier flag.
	  */
	public void setHasModifier(boolean flag) {
		hasModifier=flag;
	}

	/**
	  getting the flag that is modifier or not.

	  @return the modifier flag.
	  */
	public boolean hasModifier() {
		return hasModifier;
	}

	/**
	  setting the flag that has throws or not.

	  @param flag the throws flag.
	  */
	public void setHasThrow(boolean flag) {
		hasThrow=flag;
	}

	/**
	  getting the flag that is Throw or not.

	  @return the Throw flag.
	  */
	public boolean hasThrow() {
		return hasThrow;
	}
}
