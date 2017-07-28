/*
 * Name.java
 * Title: °³¹Ì
 * Part : Name class(tree node)
 * Copyright (c) 1998 Ant Company, all right reserved.
 * Author: Kim, Sung-hoon(kahn@antj.com)
 * Company: Antsoft
 * Description: Integrated CASE Tool(Ant)
 * Dated at 99.1.13.
 *
 * Version 1.0.0 (starting)
 *
 * Version 1.0.1
 *   - the toString() method is deleted.
 */

package ant.codecontext.parser;

/**
 * Non terminal Name node is represented by the Name class.
 * This class has one child node. It supports the method toString() that
 * complete sentence containing all sub terminal symbol.
 *
 * @author Kim, sung-hoon(kahn@antj.com)
 */
public class Name extends TreeNode {
	/**
	  Constructor.
	  the node number : 3
	  
	  @param x the line number.
	  @param y the column number.
	 */
	public Name(int x,int y) {
		super(x,y,NonTerminal.NAME);
	}
}
