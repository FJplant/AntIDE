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
 *   - only Constructor is defined.
 */

package ant.codecontext.parser;

/**
 * It is used for representing the non terminal symbol Modifiers node.
 *
 * @author Kim, sung-hoon
 */
public class Modifiers extends TreeNode {
	/**
	  Constructor.
	  the node number : 13
	  
	  @param x the line number.
	  @param y the column number.
	 */
	public Modifiers(int x,int y) {
		super(x,y,NonTerminal.MODIFIERS);
	}
}
