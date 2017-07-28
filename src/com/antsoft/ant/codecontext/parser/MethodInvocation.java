/*
 * MethodInvocation.java
 * Title: °³¹Ì
 * Part : MethodInvocation class(tree node)
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
  the non terminal MethodInvocation node.
 
  @author Kim, sung-hoon
 */
public class MethodInvocation extends TreeNode {
	/**
	  Constructor.
	  the node number : 128.
	 
	  @param name the identifier name.
	  @param x the line number.
	  @param y the column number.
	  @param typ the class type.
	  @param arg the indicator that the method has arguments.
	 */
	public MethodInvocation(String name,int x,int y,int typ,boolean arg) {
		this(x,y);
		this.name=name;
		reduceType=typ;
		hasParam=arg;
	}

	/**
	  Constructor.
	  the node number : 128.
	 
	  @param x the line number.
	  @param y the column number.
	 */
	public MethodInvocation(int x,int y) {
		super(x,y,NonTerminal.METHODINVOCATION);
	}
	
	private String name=null;
	private int reduceType=0;
	private boolean hasParam=false;
	
	/**
	  It converts the subtree into the string.
	  
	  @return Converted string.
	 */
	public String toString() {
	    switch (reduceType) {
			case 1:
		    	if (hasParam) 
					return getChild().toString()+"("+getChild().toString()+")";
				else return getChild().toString()+"()";
			case 2:
		    	if (hasParam)
					return getChild().toString()+"."+name
			    			+"("+getChild().toString()+")";
				else return getChild().toString()+"."+name+"()";
	    }
	    
	    if (hasParam) return "super."+name+"("+super.toString()+")";
		else return "super."+name+"()";
	}

	/**
	  setting the name.

	  @param the name.
	  */
	public void setName(String name) {
		this.name=name;
	}
	
	/**
	  getting the name.

	  @return the name.
	  */
	public String getName() {
		return name;
	}

	/**
	  setting the reduce type in the grammar.

	  @param typ the reduce type.
	  */
	public void setReduceType(int typ) {
		reduceType=typ;
	}
	
	/**
	  getting the reduce type in the grammar.

	  @return the reduce type.
	  */
	public int getReduceType() {
		return reduceType;
	}

	/**
	  setting the flag that has parameter.

	  @param flag the parameter flag.
	  */
	public void setHasParam(boolean flag) {
		hasParam=flag;
	}
	
	/**
	  getting the flag that has parameter.

	  @return the parameter flag.
	  */
	public boolean hasParam() {
		return hasParam;
	}
}
