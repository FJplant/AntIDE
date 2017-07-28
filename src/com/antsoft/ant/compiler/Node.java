
package com.antsoft.ant.compiler;

import java.util.Vector;
import java.util.Enumeration;

/*
 *
 * @author Steven M. Robbins.
 * @author kahn(modify)
 */
public class Node implements java.io.Serializable {
  Vector outEdges, inEdges;     // elements are nodes
  boolean isDiscovered, isFinished;

  public Node() {
		outEdges = new Vector(1,1);
		inEdges = new Vector(1,1);
		DFSReset();
  }

  public void init() {
    outEdges = new Vector();
    inEdges = new Vector();
    DFSReset();
  }

  public void linkTo( Node v ) {
		if ( v != this && ! outEdges.contains( v ) ) {
  	  outEdges.addElement( v );
    	v.inEdges.addElement( this );
		}
  }

  public Enumeration neighbours() {
		return outEdges.elements();
  }

  void transpose() {
		Vector tmp = outEdges;
		outEdges = inEdges;
		inEdges = tmp;
  }

  void DFSReset() {
		isDiscovered = isFinished = false;
  }

  void DFSVisit( VisitNode discover, VisitNode finish ) {
    isDiscovered = true;
    if ( discover != null ) discover.visit( this );

		for ( Enumeration e = neighbours(); e.hasMoreElements(); ) {
    	Node v = (Node)e.nextElement();
	    if ( ! v.isDiscovered ) v.DFSVisit( discover, finish );
		}

		isFinished = true;
		if ( finish != null ) finish.visit( this );
  }
}
