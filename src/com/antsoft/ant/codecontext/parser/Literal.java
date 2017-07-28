/*
 * Literal.java
 * Title: °³¹Ì
 * Part : Literal class(tree node)
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
  the non terminal Literal node.
 
  @author Kim, sung-hoon
 */
public class Literal extends TreeNode {
	/**
	  Constructor.
	  the node number : 134
	 
	  @param value the value that the literal represents.
	  @param x the line number.
	  @param y the column number.
	  @param type the literal type.
	 */
	public Literal(String value,int x,int y,int type) {
		super(x,y,NonTerminal.LITERAL);
		literalvalue=value.toString();
		literaltype=type;
	}
	
	private String literalvalue=null;

	/*
	   case 1 : integer literal
	   case 2 : floatingpoint literal
	   case 3 : character literal
	   case 4 : string literal
	   case 5 : false
	   case 6 : true
	   case 7 : null
	*/
	private int literaltype=0;
	
	/**
	  It converts the subtree into the string.
	  
	  @return converted string.
	 */
	public String toString() {
	    return literalvalue;
	}
}
