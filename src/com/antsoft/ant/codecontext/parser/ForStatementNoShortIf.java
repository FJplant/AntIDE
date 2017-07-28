/*
 * ForStatementNoShortIf.java
 * Title: °³¹Ì
 * Part : ForStatementNoShortIf class(tree node)
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
  the non terminal ForStatementNoShortIf node.
 
  @author Kim, sung-hoon
 */
public class ForStatementNoShortIf extends TreeNode {
	/**
	  Constructor.
	  the node number : 85.
	 
	  @param x the line number.
	  @param y the column number.
	  @param option the option indicator.
	 */
	public ForStatementNoShortIf(int x,int y,int option) {
		this(x,y);
		
		switch (option) {
			case 7 : op1=true; op2=true; op3=true; break;
			case 6 : op1=true; op2=true; break;
			case 5 : op1=true; op3=true; break;
			case 4 : op1=true; break;
			case 3 : op2=true; op3=true; break;
			case 2 : op2=true; break;
			case 1 : op3=true; break;
		}
	}
	
	/**
	  Constructor.
	  the node number : 85.
	 
	  @param x the line number.
	  @param y the column number.
	 */
	public ForStatementNoShortIf(int x,int y) {
		super(x,y,NonTerminal.FORSTATEMENTNOSHORTIF);
	}
		
	private boolean op1=false;
	private boolean op2=false;
	private boolean op3=false;
	
	/**
	  It converts the subtree into the string.
	  
	  @return converted string.
	 */
	public String toString() {
	    StringBuffer buffer=new StringBuffer();

	    TreeNode node=getChild();
	    
	    buffer.append("for(");
	    if (op1) {
		buffer.append(node.toString());
		node=getChild();
	    }

	    buffer.append(";");

	    if (op2) {
		buffer.append(node.toString());
		node=getChild();
	    }

	    buffer.append(";");
	    
	    if (op2) {
		buffer.append(node.toString());
		node=getChild();
	    }

	    buffer.append(")");
	    
	    buffer.append(node.toString());
	    
	    return buffer.toString();
	}

	/**
	  setting the first operator flag.

	  @param flag the first operator flag.
	  */
	public void setInit(boolean flag) {
		op1=flag;
	}

	/**
	  setting the second operator flag.

	  @param flag the second operator flag.
	  */
	public void setExpr(boolean flag) {
		op2=flag;
	}

	/**
	  setting the third operator flag.

	  @param flag the third operator flag.
	  */
	public void setUpdate(boolean flag) {
		op3=flag;
	}

	/**
	  getting the first operator flag.

	  @return the first operator flag.
	  */
	public boolean getInit() {
		return op1;
	}

	/**
	  getting the second operator flag.

	  @return the second operator flag.
	  */
	public boolean getExpr() {
		return op2;
	}

	/**
	  getting the third operator flag.

	  @return the third operator flag.
	  */
	public boolean getUpdate() {
		return op3;
	}
}
