/*
 * ClassDeclaration.java
 * Title: °³¹Ì
 * Part : ClassDeclaration class(tree node)
 * Copyright (c) 1998 Ant Company, all right reserved.
 * Author: Kim, Sung-hoon(kahn@antj.com)
 * Company: Antsoft
 * Description: Integrated CASE Tool(Ant)
 * Dated at 99.1.13.
 *
 * Version 1.0.0
 *   - only Constructor is defined.
 *
 * Version 1.1.0
 *   - some field is defined.
 */

package ant.codecontext.parser;

/**
 * It represent ClassDeclaration node.
 *
 * @author Kim, sung-hoon
 */
public class ClassDeclaration extends TreeNode {
	// It holds the class name.
	String className=null;

	// It indicates whether it has modifiers or not.
	boolean hasModifier=false;

	// It indicates whether it extends superclass or not.
	boolean hasSuper=false;

	// It indicates whether it implements interface or not.
	boolean hasInterface=false;

	/**
	  Constructor.
	  the node number : 12
	 
	  @param x the line number.
	  @param y the column number.
	 */
	public ClassDeclaration(int x,int y) {
		super(x,y,NonTerminal.CLASSDECLARATION);
	}

	/**
	 * Constructor.
	  the node number : 12
	 *
	 * @param x the line number.
	 * @param y the column number.
	 */
	public ClassDeclaration(String s,int x,int y,int flag) {
		this(x,y);
		className=s;

		/*
		   <------    MSD     LSD    ------>
		   hasModifier hasSuper hasInterface
		   flag = 7 : all has--- is on.
		   flag = 5 : hasModifier and hasInterface is on.
		   flag = 0 : all has--- is off.
		*/
		int a=flag;
		int b=a/4;
		if (b==1) hasModifier=true;

		a=a%4;
		b=a/2;
		if (b==1) hasSuper=true;

		a=a%2;
		if (a==1) hasInterface=true;
	}

	/*
	 * add the children node.
	 *
	 * @param node object to be added.
	 * @param fieldflag flag for special distinct, between field member.
	 *                  if 1, hasmodifiers is set.
	 *                  if 2, hassuper is set.
	 *                  if 3, hasinterface is set.
	public void addChild(TreeNode node) {
		super.addChild(node);
	}
	 */


	/**
	 * It converts subtree into the string.
	 *
	 * @return converted string(so that class declaration..)
	 */
	public String toString() {
		StringBuffer buffer=new StringBuffer();
		TreeNode node=getChild();

		if (hasModifier) {
			buffer.append(node.toString());
			node=getChild();
		}
		buffer.append(" class "+className+" ");
		if (hasSuper) {
			buffer.append("interface "+node.toString()+" ");
			node=getChild();
		}
		if (hasInterface) {
			buffer.append("implements "+node.toString()+" ");
			node=getChild();
		}
		buffer.append(node.toString());

		return buffer.toString();
	}

	/**
	 * set the class name.
	 *
	 * @param name the class name.
	 */
	public void setClassName(String name) {
		className=name;
	}

	/**
	  get the class name.

	  @return the class name.
	  */
	public String getClassName() {
		return className;
	}

	/**
	  getting the flag of the modifier.

	  @return the modifier flag.
	  */
	public boolean hasModifier() {
		return hasModifier;
	}

	/**
	  getting the flag of the extends.

	  @return the extends flag.
	  */
	public boolean hasSuper() {
		return hasSuper;
	}
	
	/**
	  getting the flag of the interface.

	  @return the interface flag.
	  */
	public boolean hasInterface() {
		return hasInterface;
	}
	
	/**
	  setting the flag of the modifier.

	  @param flag the modifier flag.
	  */
	public void setModifier(boolean flag) {
		hasModifier=flag;
	}
	
	/**
	  setting the flag of the super.

	  @param flag the super flag.
	  */
	public void setSuper(boolean flag) {
		hasSuper=flag;
	}
	
	/**
	  setting the flag of the interface.

	  @param flag the interface flag.
	  */
	public void setInterface(boolean flag) {
		hasInterface=flag;
	}
}
