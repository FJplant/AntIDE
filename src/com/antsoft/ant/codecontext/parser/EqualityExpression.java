/*
 * EqualityExpression.java
 * Title: °³¹Ì
 * Part : EqualityExpression class(tree node)
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
  the non terminal EqualityExpression node.
 
  @author Kim, sung-hoon
 */
public class EqualityExpression extends TreeNode {
	/**
	  Constructor.
	  the node number : 110
	 
	  @param x the line number.
	  @param y the column number.
	  @param op the operator token number.
	 */
	public EqualityExpression(int x,int y,int op) {
		this(x,y);
		optype=op;
	}
	
	/**
	  Constructor.
	  the node number : 110
	 
	  @param x the line number.
	  @param y the column number.
	 */
	public EqualityExpression(int x,int y) {
		super(x,y,NonTerminal.EQUALITYEXPRESSION);
	}

	private int optype=0;
	
	private String opstring() {
	    switch (optype) {
		case sym.EQ : return "==";
		case sym.NE : return "!=";
	    }
	    return null;
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
