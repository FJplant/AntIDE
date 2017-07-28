/*
 * ExplicitConstructorInvocation.java
 * Title: °³¹Ì
 * Part : ExplicitConstructorInvocation class(tree node)
 * Copyright (c) 1998 Ant Company, all right reserved.
 * Author: Kim, Sung-hoon(kahn@antj.com)
 * Company: Antsoft
 * Description: Integrated CASE Tool(Ant)
 * Dated at 99.1.13.15.
 *
 * Version 1.0.1
 *   - the grammar is changed, and toString() is modified.
 */

package ant.codecontext.parser;

/**
  the non terminal ExplicitConstructorInvocation node.
 
  @author Kim, sung-hoon
 */
public class ExplicitConstructorInvocation extends TreeNode {
	/**
	  Constructor.
	  the node number : 39
	 
	  @param x the line number.
	  @param y the column number.
	 */
	public ExplicitConstructorInvocation(int x,int y) {
		super(x,y,NonTerminal.EXPLICITCONSTRUCTORINVOCATION);
	}

	/**
	  Constructor.
	 
	  @param x the line number.
	  @param y the column number.
	  @param param parameter indicator.
	  @param name the name will be "super" or "this".
	  @param exflag java 1.1 extension flag
	 */
	public ExplicitConstructorInvocation(int x,int y,boolean param,String name,boolean exflag) {
		this(x,y);
		hasParam=param;
		invocationName=name;
		extension=exflag;
	}

	boolean hasParam=false;
	String invocationName=null;
	boolean extension=false;

	/** 
	  It converts the subtree into the string.

	  @return converted string.
	 */
	public String toString() {
		if (extension) {
			if (hasParam) 
				return getChild().toString()+".super("+getChild().toString()+");";
			else 
				return super.toString()+".super();";
		}
		else {
			StringBuffer buffer=new StringBuffer("");

			buffer.append(invocationName+"(");
			buffer.append(super.toString());
			buffer.append(");");

			return buffer.toString();
		}
	}

	/**
	  setting the invocation name with specified string.

	  @param name the specified invocation name.
	  */
	public void setInvocationName(String name) {
		invocationName=name;
	}

	/**
	  getting the invocation name.

	  @return the invocation name.
	  */
	public String getInvocationName() {
		return invocationName;
	}

	/**
	  setting the flag that has parameter or not.

	  @param flag the parameter flag.
	  */
	public void setHasParam(boolean flag) {
		hasParam=flag;
	}

	/**
	  getting the parameter flag.

	  @return the parameter flag.
	  */
	public boolean hasParam() {
		return hasParam;
	}

	/**
	  setting the flag that is extended on the grammar.

	  @param flag the extension flag.
	  */
	public void setExtension(boolean flag) {
		extension=flag;
	}

	/**
	  getting the extension flag.

	  @return the extension flag.
	  */
	public boolean isExtension() {
		return extension;
	}
}
