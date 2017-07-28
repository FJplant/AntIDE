/*
 * Modifiers.java
 * Title: °³¹Ì
 * Part : Modifiers class(tree node)
 * Copyright (c) 1998 Ant Company, all right reserved.
 * Author: Kim, Sung-hoon(kahn@antj.com)
 * Company: Antsoft
 * Description: Integrated CASE Tool(Ant)
 * Dated at 99.1.13.
 *
 * Version 1.0.0
 *   - modifiername field is defined.
 *   - toString() method is defined.
 */

package ant.codecontext.parser;

/**
 * It is used for representing the non terminal symbol Modifier node.
 * It contains the specified modifer name only one.
 *
 * @author Kim, sung-hoon
 */
public class Modifier extends TreeNode {
	/** 
	  Specified modifier name.
	  One of public protected private
	         static
			 final transient volatile abstract native synchronized.
	 */
	public String modifierName=null;

	/**
	  Constructor.
	  the node number : 14
	 
	  @param x the line number.
	  @param y the column number.
	 */
	public Modifier(int x,int y) {
		super(x,y,NonTerminal.MODIFIER);
	}

	/**
	  Constructor.
	 
	  @param name the modifier name.
	  @param x the line number.
	  @param y the column number.
	 */
	public Modifier(String name,int x,int y) {
		this(x,y);
		modifierName=name;
	}

	/**
	  It converts subtree into the string.
	 
	  @return converted string.
	 */
	public String toString() {
		return modifierName+" ";
	}

	/**
	  It sets the modifier name with Constructor(x,y) without name parameter.

	  @param name specified modifer name.
	 */ 
	public void setModifierName(String name) {
		modifierName=name;
	}

	/**
	  gets the modifier name of the node.

	  @return the modifer name of the node..
	 */ 
	public String getModifierName() {
		return modifierName;
	}
}
