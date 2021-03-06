/*
 * ForUpdate.java
 * Title: ����
 * Part : ForUpdate class(tree node)
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
  the non terminal ForUpdate node.
 
  @author Kim, sung-hoon
 */
public class ForUpdate extends TreeNode {
	/**
	  Constructor.
	  the node number : 87.
	 
	  @param x the line number.
	  @param y the column number.
	 */
	public ForUpdate(int x,int y) {
		super(x,y,NonTerminal.FORUPDATE);
	}
}
