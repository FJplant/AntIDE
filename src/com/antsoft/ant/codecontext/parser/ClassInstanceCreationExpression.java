/*
 * ClassInstanceCreationExpression.java
 * Title: °³¹Ì
 * Part : ClassInstanceCreationExpression class(tree node)
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
  the non terminal ClassInstanceCreationExpression node.
 
  @author Kim, sung-hoon
 */
public class ClassInstanceCreationExpression extends TreeNode {
	/**
	  Constructor.
	  the node number : 125.
	 
	  @param name the identifier name(case 5,6,7,8)
	  @param x the line number.
	  @param y the column number.
	  @param cases the case indicator.
	 */
	public ClassInstanceCreationExpression(String name,int x,int y,int cases) {
		super(x,y,NonTerminal.CLASSINSTANCECREATIONEXPRESSION);
		identifier=name;
		caseflag=cases;
	}
	
	private String identifier=null;
	private int caseflag=0;

	/**
	  It converts the subtree into the string.
	  
	  @return the converted string.
	 */
	public String toString() {
		switch (caseflag) {
			case 1 : return "new "+getChild().toString()+"("
					 		+getChild().toString()+")";
			case 2 : return "new "+getChild().toString()+"()";
			case 3 : return "new "+getChild().toString()+"("
					 		+getChild().toString()+")"+getChild().toString();
			case 4 : return "new "+getChild().toString()+"()"
					 		+getChild().toString();
			case 5 : return getChild().toString()+".new "+identifier+"("
					 		+getChild().toString()+")"+getChild().toString();
			case 6 : return getChild().toString()+".new "+identifier+"("
					 		+getChild().toString()+")";
			case 7 : return getChild().toString()+".new "+identifier+"()"
					 		+getChild().toString();
			case 8 : return getChild().toString()+".new "+identifier+"()";
			default : return "";
		}
	}
}
