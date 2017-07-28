/*
 * LabeledStatementNoShortIf.java
 * Title: °³¹Ì
 * Part : LabeledStatementNoShortIf class(tree node)
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
  the non terminal LabeledStatementNoShortIf node.
 
  @author Kim, sung-hoon
 */
public class LabeledStatementNoShortIf extends TreeNode {
	/**
	  Constructor.
	  the node number : 72
	 
	  @param label the label name in string.
	  @param x the line number.
	  @param y the column number.
	 */
	public LabeledStatementNoShortIf(String label,int x,int y) {
		this(x,y);
		this.label=label;
	}
	
	/**
	  Constructor.
	  the node number : 72
	 
	  @param x the line number.
	  @param y the column number.
	 */
	public LabeledStatementNoShortIf(int x,int y) {
		super(x,y,NonTerminal.LABELEDSTATEMENTNOSHORTIF);
	}
	
	private String label=null;
	
	/**
	  It converts the subtree into the string.
	  
	  @return converted string.
	 */
	public String toString() {
	    return getLabel()+":"+super.toString();
	}

	/**
	  setting the label.

	  @param name the label name.
	  */
	public void setLabel(String name) {
		label=name;
	}

	/**
	  getting the label.

	  @return the label name.
	  */
	public String getLabel() {
		return label;
	}
}
