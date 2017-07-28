/*
 * AssignmentOperator.java
 * Title: °³¹Ì
 * Part : AssignmentOperator class(tree node)
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
  the non terminal AssignmentOperator node.
 
  @author Kim, sung-hoon
 */
public class AssignmentOperator extends TreeNode {
	/**
	  Constructor.
	  the node nunber : 103.
	 
	  @param x the line number.
	  @param y the column number.
	  @param type the operator type as integer.
	 */
	public AssignmentOperator(int x,int y,int type) {
		this(x,y);
		optype=type;
	}

	/**
	  Constructor.
	  the node nunber : 103.
	 
	  @param x the line number.
	  @param y the column number.
	 */
	public AssignmentOperator(int x,int y) {
		super(x,y,NonTerminal.ASSIGNMENTOPERATOR);
	}
	
	private int optype=0;
	
	private String opconvert() {
	    switch (optype) {
			case sym.ASSIGN : return "=";
			case sym.AMUL : return "*=";
			case sym.ADIV : return "/=";
			case sym.AMOD : return "%=";
			case sym.AADD : return "+=";
			case sym.ASUB : return "-=";
			case sym.ABWA : return "&=";
			case sym.ABWO : return "|=";
			case sym.ABWN : return "^=";
			case sym.ASL : return "<<=";
			case sym.ASR : return ">>=";
			case sym.AUSR : return ">>>=";
	    }
	    return "";
	}
	
	/**
	  It converts the subtree(only operator) into the string.
	  
	  @return converted string.
	 */
	public String toString() {
	    return opconvert();
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
