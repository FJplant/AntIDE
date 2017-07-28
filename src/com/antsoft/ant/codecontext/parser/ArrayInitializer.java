/*
 * ArrayInitializer.java
 * Title: °³¹Ì
 * Part : ArrayInitializer class(tree node)
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
  the non terminal ArrayInitializer node.
 
  @author Kim, sung-hoon
 */
public class ArrayInitializer extends TreeNode {
	/**
	  Constructor.
	  the node number : 47
	 
	  @param x the line number.
	  @param y the column number.
	 */
	public ArrayInitializer(int x,int y) {
		super(x,y,NonTerminal.ARRAYINITIALIZER);
	}

	private boolean hasLastComma=false;

	/**
	  It converts the subtree into the string.

	  @return converted string.
	 */
	public String toString() {
		StringBuffer buffer=new StringBuffer("{\n");

		buffer.append(super.toString());
		if (hasLastComma) buffer.append(",");
		buffer.append("\n}");

		return buffer.toString();
	}

	/**
	  setting the flag that has the last comma in initializer or not.

	  @param flag the last comma flag.
	  */
	public void setHasLastComma(boolean flag) {
		hasLastComma=flag;
	}

	/**
	  getting the flag that has the last comma in initializer or not.

	  @return the last comma flag.
	  */
	public boolean hasLastComma() {
		return hasLastComma;
	}
}
