
package com.antsoft.ant.compiler;

import java.util.Enumeration;

/*
 * @author kahn
 */
public class TargetNode extends Node {
  // Left side of rule is the target name; the right side is given by the
  // names of our neighbours.
  String targetName;

  // These are the allowed types of target nodes
  public static final int UNKNOWN = 0;
  public static final int REFERENCE = 1;
  public static final int SOURCE = 2;
  public static final int CLASS = 3;
  public static final int HEADER = 4;
  public static final int STUB = 5;

  int type;

  public TargetNode( String s, int t ) {
		targetName = s;
		type = t;
  }

  public TargetNode( String s ) {
		this( s, UNKNOWN );
  }

  public String neighbours( int type ) {
		String r = "";
		for ( Enumeration e = neighbours(); e.hasMoreElements(); ) {
    	TargetNode v = (TargetNode)e.nextElement();
	    if ( v.type == type ) r += v.targetName + " ";
		}

		return r;
  }
}


