/*
 * ClassBodyDeclaration.java
 * Title: °³¹Ì
 * Part : ClassBodyDeclaration class(tree node)
 * Copyright (c) 1998 Ant Company, all right reserved.
 * Author: Kim, Sung-hoon(kahn@antj.com)
 * Company: Antsoft
 * Description: Integrated CASE Tool(Ant)
 * Dated at 99.1.13.
 *
 * Version 1.0.0 (starting)
 *   - the only Constructor is defined.
 */

package ant.codecontext.parser;

/**
  the non terminal symbol ClassBodyDeclaration node.
  ClassBodyDeclaration ::= ClassMemberDeclaration
                         | StaticInitializer
						 | ConstructorDeclaration

  @author Kim, sung-hoon.
 */
public class ClassBodyDeclaration extends TreeNode {
	/**
	  Constructor.
	  the node number : 20

	  @param x the line number.
	  @param y the column number.
	 */
	public ClassBodyDeclaration(int x,int y) {
		super(x,y,NonTerminal.CLASSBODYDECLARATION);
	}
}
