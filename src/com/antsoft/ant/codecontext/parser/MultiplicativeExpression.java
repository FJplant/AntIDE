/*
 * MultiplicativeExpression.java
 * Title: °³¹Ì
 * Part : MultiplicativeExpression class(tree node)
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
  the non terminal MultiplicativeExpression node.
 
  @author Kim, sung-hoon
 */
public class MultiplicativeExpression extends TreeNode {
	/**
	  Constructor.
	  the node number : 114
	 
	  @param x the line number.
	  @param y the column number.
	  @param op the operator token number.
	 */
	public MultiplicativeExpression(int x,int y,int op) {
		this(x,y);
		optype=op;
	}

	/**
	  Constructor.
	  the node number : 114
	 
	  @param x the line number.
	  @param y the column number.
	 */
	public MultiplicativeExpression(int x,int y) {
		super(x,y,NonTerminal.MULTIPLICATIVEEXPRESSION);
	}
	
	private int optype=0;
	
	private String opstring() {
	    switch (optype) {
			case sym.MUL : return "*";
			case sym.DIV : return "/";
			case sym.MOD : return "%";
	    }
	    return "";
	}
	
	/**
	  It converts the subtree into the string.
	  
	  @return converted string.
	 */
	public String toString() {
	    if (sizeOfChild()==1) return super.toString();
	    
	    return getChild().toString()+opstring()+getChild().toString();
	}

	/**
	  setting the operator type.

	  @param type the operator type as integer.
	  */
	public void setOp(int o) {
		optype=o;
	}

	/**
	  getting the operator type.

	  @return the operator type as integer.
	  */
	public int setOp() {
		return optype;
	}
}
