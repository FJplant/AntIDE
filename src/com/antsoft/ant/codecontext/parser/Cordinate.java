/*
 * Cordinate.java
 * Title: °³¹Ì
 * Part : Cordinate class(used in the tree node)
 * Copyright (c) 1998 Ant Company, all right reserved.
 * Author: Kim, Sung-hoon(kahn@antj.com)
 * Company: Antsoft
 * Description: Integrated CASE Tool(Ant)
 * Dated at 99.1.13.
 *
 * Version 1.0.0 (Completed)
 */

package ant.codecontext.parser;

/**
 * It represents the cordinate x,y(line,column) that the symbol has appered.
 */
public class Cordinate {
	/**
	 * Cordinate x is the line number.
	 */
	protected int x=0;

	/** Cordinate y is the column number in the line. */
	protected int y=0;

	/**
	 *
	 */
	public Cordinate(int x,int y) {
		this.x=x;
		this.y=y;
	}
}
