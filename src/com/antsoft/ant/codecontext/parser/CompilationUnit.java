/*
 * CompilationUnit.java
 * Title: °³¹Ì
 * Part : CompilationUnit class(tree node)
 * Copyright (c) 1998 Ant Company, all right reserved.
 * Author: Kim, Sung-hoon(kahn@antj.com)
 * Company: Antsoft
 * Description: Integrated CASE Tool(Ant)
 * Dated at 99.1.13.
 *
 * Version 1.0.0 (starting)
 *
 * Version 1.0.1
 *   - the method visitor()'s body is discarded. - visitor() will never be used.
 *   - two constructor is added.
 *
 * Version 1.0.2
 *   - the method visitor() is replaced with the toString() method.
 */

package ant.codecontext.parser;

/**
 * In LALR(1) grammar for Java Language, compilationUnit is the starting
 * non terminal symbol. CompilationUnit class represents the structure
 * for the tree node as the root of the tree.
 *
 * @author Kim, sung-hoon(kahn@antj.com)
 */
public class CompilationUnit extends TreeNode {
	/** 
	 * Constructor.
	  the node number : 1
	 *
	 * @param x the line number
	 * @param y the column number
	 */
	public CompilationUnit(int x,int y) {
		super(x,y,NonTerminal.COMPILATIONUNIT);
	}

	/**
	 * Constructor(default)
	 */
	public CompilationUnit() {
		this(0,0);
	}
	
	//  hasPackage indicates whether the source class has package name or not.
	boolean hasPackage=false;

	// hasImports indicates whether the source class has import declaration or not.
	boolean hasImports=false;

	/**
	 * add the child node.
	 *
	 * @param node the child tree node to be added
	 * @param packageorimport indicates that the added child is package 
	 *     declaration or import declaration (packageorimport=1, package
	 *     packageorimport=2, import otherwise class declaration.)
	 */
	public void addChild(TreeNode node,int packageorimport) {
		super.addChild(node);
		if (packageorimport==1) hasPackage=true;
		if (packageorimport==2) hasImports=true;
	}

	/**
	  getting the flag that has the package or not.

	  @return package flag as boolean type.
	  */
	public boolean hasPackage() {
		return hasPackage;
	}

	/**
	  getting the flag that has the import list or not.

	  @return import flag as boolean type.
	  */
	public boolean hasImports() {
		return hasImports;
	}

	/**
	  setting the flag that has the package or not.

	  @param flag the flag as the boolean type.
	  */
	public void setHasPackage(boolean flag) {
		hasPackage=flag;
	}

	/**
	  setting the flag that has the Imports or not.

	  @param flag the flag as the boolean type.
	  */
	public void setHasImports(boolean flag) {
		hasImports=flag;
	}
}
