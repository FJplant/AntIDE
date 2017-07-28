
package com.antsoft.ant.compiler;

import java.util.Stack;
import java.util.Vector;
import java.util.Enumeration;

/*
 * A Directed Graph, used for Depth-First Search, and
 * computing strongly-connected components.  Nodes may not have links to
 * themselves, nor multiple edges to another node.
 *
 * @author Steven M. Robbins.
 * @author kahn(modify)
 */
public class DirectedGraph implements java.io.Serializable {
  // Nodes are of type Node
  Vector nodes;

  public DirectedGraph() {
		nodes = new Vector();
  }

  public Enumeration nodes() {
		return nodes.elements();
  }

  public void addNode( Node v ) {
		nodes.addElement( v );
  }

  private void DFSReset()  {
		for( Enumeration e = nodes(); e.hasMoreElements(); ) {
    	Node v = (Node)e.nextElement();
	    v.DFSReset();
		}
  }

  /*
   * Depth-First search(DFS) routine.
   *
   * @param discover invoked on the visited node at discovery time
   * @param finish invoked on the visited node at finish time
   */
  public void depthFirstSearch( VisitNode discover, VisitNode finish ) {
		DFSReset();
		for( Enumeration e = nodes(); e.hasMoreElements(); ) {
    	Node v = (Node)e.nextElement();
	    if (! v.isDiscovered )
  		v.DFSVisit( discover, finish );
		}
  }

  /*
   * Transposes this graph.  Transpose again to get the original.
   */
  public void transpose() {
		for( Enumeration e = nodes(); e.hasMoreElements(); ) {
    	Node v = (Node)e.nextElement();
	  	v.transpose();
		}
  }

  /*
   * Compute the strongly-connected components of the graph.  Each component
   * is returned as a Vector listing the Nodes of the component.
   *
   * @return Enumeration of components (Vectors of Nodes)
   */
  public Enumeration getComponents() {
		final Stack compRoot = new Stack();
		final Vector components = new Vector();

		class pushCompRoot implements VisitNode {
	    public void visit( Node v ) {
			  compRoot.push( v );
	    }
		};

    // The algorithm follows that of Cormen, Lieserson, Rivest
    // Step 1: do DFS, recording the nodes in reverse order of finish
    // time.
    transpose();
    depthFirstSearch( null, new pushCompRoot() );

    // Step 2: peel off the components, one by one, by starting DFS at the
    // nodes in order generated in step 1
    transpose();
    DFSReset();
    while ( ! compRoot.empty() ) {
	    Node v = (Node)compRoot.pop();

  	  if ( ! v.isDiscovered ) {
			  final Vector comp = new Vector();

			  class addCompNode implements VisitNode {
		      public void visit( Node n ) {
				    comp.addElement( n );
		      }
			  };

				v.DFSVisit( new addCompNode(), null );
		  	components.addElement( comp );
		  }
		}

		return components.elements();
  }
}

