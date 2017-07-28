/*
 * ClassMemberDeclaration.java
 * Title: °³¹Ì
 * Part : ClassMemberDeclaration class(tree node)
 * Copyright (c) 1998 Ant Company, all right reserved.
 * Author: Kim, Sung-hoon(kahn@antj.com)
 * Company: Antsoft
 * Description: Integrated CASE Tool(Ant)
 * Dated at 99.1.13.
 *
 * Version 1.0.0 (starting)
 *   - Constructor is defined.
 */

package ant.codecontext.parser;

/**
  the non terminal symbol ClassMemberDeclaration node.
  ClassMemberDeclaration ::= FieldDeclaration
                           | MethodDeclaration
 */
public class ClassMemberDeclaration extends TreeNode {
	/**
	  Constructor.
	  the node number : 21

	  @param x the line number.
	  @param y the column number.
	 */
	public ClassMemberDeclaration(int x,int y) {
		super(x,y,NonTerminal.CLASSMEMBERDECLARATION);
	}
}
