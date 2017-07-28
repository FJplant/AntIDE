/*
 * MethodDeclarator.java
 * Title: °³¹Ì
 * Part : MethodDeclarator class(tree node)
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
  the non terminal MethodDeclarator node.
  MethodDeclarator ::= id ( FormalParameterList<opt> )
                     | MethodDeclarator [ ]
 
  @author Kim, sung-hoon
 */
public class MethodDeclarator extends TreeNode {
	/**
	  Constructor.
	  the node number : 29
	 
	  @param x the line number.
	  @param y the column number.
	 */
	public MethodDeclarator(int x,int y) {
		super(x,y,NonTerminal.METHODDECLARATOR);
	}

	/**
	  Constructor.
	 
	  @param name the identifier name.
	  @param x the line number.
	  @param y the column number.
	 */
	public MethodDeclarator(String name,int x,int y) {
		this(x,y);
		if (name!=null) {
			this.name=new String(name);
			isLeaf=true;
		}
	}

	// indicates that the method has formal parameter list.
	//protected boolean hasparam=false;

	// the method identifier. 
	String name=null;
	
	// indicates that node is the leaf node.
	boolean isLeaf=false;

	/**
	  It converts the subtree into the string.

	  @return converted string.
	 */
	public String toString() {
		String s=super.toString();

		if (!isLeaf) return s+"[ ]";
		else return name+"("+s+")";
	}

	/**
	  setting the method name.

	  @param name the method name.
	  */
	public void setName(String name) {
		this.name=name;
	}

	/**
	  getting the method name.

	  @return the method name.
	  */
	public String getName() {
		return name;
	}

	/**
	  getting the method full name.

	  @return the method full name.
	  */
	public String getFullName() {
		if (isLeaf) return name;
		else return ((MethodDeclarator)getChild()).getName()+"[]";
	}

	/**
	  setting the flag that is a leaf node.

	  @param flag the leaf flag.
	  */
	public void setIsLeaf(boolean flag) {
		isLeaf=flag;
	}

	/**
	  getting the flag that is a leaf node.

	  @return the leaf flag.
	  */
	public boolean isLeaf() {
		return isLeaf;
	}
}
