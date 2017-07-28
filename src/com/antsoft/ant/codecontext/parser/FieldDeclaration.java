/*
 * FieldDeclaration.java
 * Title: °³¹Ì
 * Part : FieldDecalration class(tree node)
 * Copyright (c) 1998 Ant Company, all right reserved.
 * Author: Kim, Sung-hoon(kahn@antj.com)
 * Company: Antsoft
 * Description: Integrated CASE Tool(Ant)
 * Dated at 99.1.13.
 *
 * Version 1.0.0 (starting)
 *   - Constructor, toString() is defined.
 */

package ant.codecontext.parser;

/**
  the non terminal symbol FieldDeclaration node.
  FieldDeclaration ::= Modifiers<opt> Type VariableDeclarators ;

  @author Kim, sung-hoon.
 */
public class FieldDeclaration extends TreeNode {
	/**
	  Constructor.
	  the node number : 22

	  @param x the line number.
	  @param y the column number.
	 */
	public FieldDeclaration(int x,int y) {
		super(x,y,NonTerminal.FIELDDECLARATION);
	}

	/**
	  It converts the subtree into the string.

	  @return converted string.
	 */
	public String toString() {
		return (super.toString()+";\n");
	}

	/**
	  return true if it has modifier child node,
	  		 false o/w.
	  */
	public boolean hasModifier() {
		if (sizeOfChild()==3) return true;
		else return false;
	}
}
