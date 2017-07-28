/*
 * InterfaceDeclaration.java
 * Title: °³¹Ì
 * Part : Interfaceeclaration class(tree node)
 * Copyright (c) 1998 Ant Company, all right reserved.
 * Author: Kim, Sung-hoon(kahn@antj.com)
 * Company: Antsoft
 * Description: Integrated CASE Tool(Ant)
 * Dated at 99.1.13.
 *
 * Version 1.0.0
 *   - only Constructor is defined.
 */

package ant.codecontext.parser;

/**
  It represent InterfaceDeclaration node.
 
  @author Kim, sung-hoon
 */
public class InterfaceDeclaration extends TreeNode {
	/**
	  Constructor.
	  the node number : 40

	  @param x the line number.
	  @param y the column number.
	 */
	public InterfaceDeclaration(int x,int y) {
		super(x,y,NonTerminal.INTERFACEDECLARATION);
	}

	/**
	  Constructor

	  @param name the interface name.
	  @param x the line number.
	  @param y the column number.
	  @param flag the optional indicato.
	 */
	public InterfaceDeclaration(String name,int x,int y,int flag) {
		this(x,y);
		interfaceName=name;
		switch (flag) {
			case 3: hasModifier=true;
					hasSuper=true;
					break;
			case 2: hasModifier=true;
					break;
			case 1: hasSuper=true;
					break;
		}
	}

	protected String interfaceName=null;

	/**
	  setting the interface name.

	  @param name interface name.
	 */
	public void setInterfaceName(String name) {
		interfaceName=name;
	}

	private boolean hasModifier=false;
	private boolean hasSuper=false;

	/**
	  It converts the subtree into the string.

	  @return converted string.
	 */
	public String toString() {
		StringBuffer buffer=new StringBuffer("");
		TreeNode node=getChild();

		if (hasModifier) {
			buffer.append(node.toString());
			node=getChild();
		}
		buffer.append("interface "+interfaceName+" ");
		if (hasSuper) {
			buffer.append(node.toString());
			node=getChild();
		}
		buffer.append(node.toString());

		return buffer.toString();
	}

	/**
	  getting the interface name.

	  @return the interface name of the node.
	  */
	public String getInterfaceName() {
		return interfaceName;
	}

	/**
	  setting the flag that indicates that has modifiers or not.

	  @param flag the modifier flag.
	  */
	public void setHasModifier(boolean flag) {
		hasModifier=flag;
	}

	/**
	  setting the flag that indicates that has super interface or not.

	  @param flag the super flag.
	  */
	public void setHasSuper(boolean flag) {
		hasSuper=flag;
	}

	/**
	  getting the flag that indicates that has modifiers or not.

	  @return the modifier flag.
	  */
	public boolean hasModifier() {
		return hasModifier;
	}

	/**
	  getting the flag that indicates that has super or not.

	  @return the super flag.
	  */
	public boolean hasSuper() {
		return hasSuper;
	}
}
