
package com.antsoft.ant.compiler;

import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import java.io.File;

import com.antsoft.ant.main.MainFrame;
import com.antsoft.ant.pool.classpool.ClassPool;

/*
 * Table of symbols and dependencies.
 *
 * This is the interface to the parser.  It holds a symbol table, and a
 * dependency graph.
 *
 * @author Steven M. Robbins.
 * @author kahn(modify)
 **/

public class DepTable implements java.io.Serializable {
  // The package name currently in effect.
  private transient String packageName;

  // Graph node representing the source file currently being parsed.
  private transient TargetNode sourceNode;


  // Canonical name of class/interface currently being parsed.
  private transient String className;

  // Node in dependency graph representing the class file for the current
  // class being parsed.
  private transient TargetNode classNode;

  // Maps an imported class name to its fully-qualified name.
  // An import of "a.b.c" creates an alias for "c", with value "a.b.c".
  private transient Hashtable imports;

  /*
   * Store the wildcard imports.
   * These are used, as a last resort, for unqualified references.
   * We simply insert all possible strings formed by concatenating
   * all the wildcard imports with the reference name.  Vector elements
   * are Strings.
   */
  private transient Vector wildImports;

  // Table mapping String (type name) to a TargetNode.
  private Hashtable symbols;

  /*
   * Graph of dependency relationships.  The graph nodes are of type
   * TargetNode.  Neighbours of a node v are those targets upon which v
   * depends.
   */
  DirectedGraph g;

  // Record the destination directory if the "-d destdir"
  // option is used.
  // private String destDir;

  // Cache the line separator string here.
  private transient String lineSeparator;

  private Hashtable files = null;
  private transient boolean flag = false;

  /*
   * Create a dependency table.
   * @param destDir specify the destination for compiled class files.
   */
  public DepTable() {// String destDir ) {
    symbols = new Hashtable();
    g = new DirectedGraph();

    //this.destDir = destDir;
    lineSeparator = System.getProperty( "line.separator" );
    files = new Hashtable();
  }

  /*
   * Canonicalize a package-scoped symbol.
   */
  private String canonizePackageSymbol( String s )
  {
    if ( packageName == null || s.indexOf( '.' ) >= 0 ) return s;

    return packageName + "." + s;
  }

  /*
   * Canonicalize a symbol name from an import.  Returns null if
   * the symbol is neither canonical, nor explicitly imported (it may have
   * been imported via wildcards).
   *
   * @return canonical name, or null
   */
  private String canonizeImportSymbol( String s )
  {
  	int pos = s.indexOf('.');
    if ( pos >= 0 ) {
    	if (MainFrame.getCodeContext().existUserClass(s))	return s;
      else s = s.substring(0, pos);
    }

    return (String)imports.get( s );
  }

  /*
   * Canonicalize the class file name.  The name depends on the class name,
   * the source file name, and whether '-d destdir' is used.
  private String canonizeClassFile( String sourceFileName, String className ) {
  	if ( destDir == null ) {
	    // The '-d' option NOT specified.  Canonical name for class x.y.foo
	    // from file a/b/bar.java is: a/b/foo.class.
	    int i = className.lastIndexOf( '.' );
	    String suffix = className.substring( i+1 ) + ".class";
	    String parentDir = (new File( sourceFileName )).getParent();
	    if ( parentDir == null )	return suffix;
	    return parentDir + File.separator + suffix;
  	}

	  // The case that '-d' bar was specified.  Canonical name for class
	  // file for x.y.foo is 'bar/x/y/foo.class'.
	  return destDir + File.separator + className.replace( '.', File.separatorChar ) + ".class";
  }
   */

  /*
   * Look up symbol name.  Returns the associated TargetNode, or null if the
   * symbol name has not been defined.
   */
  private TargetNode lookupSymbol( String s ) {
    return (TargetNode)symbols.get( s );
  }

  /*
   * As above, but also checks that the TargetNode is of the required type.
   */
  private TargetNode lookupSymbol( String s, int type ) {
    TargetNode v = lookupSymbol( s );
    if ( v != null && v.type == type ) return v;
 		return null;
  }

  /*
   * Inserts new node into the dependency graph.
   */
  private TargetNode newTarget( String target ) {
		TargetNode v = new TargetNode( target );
		g.addNode( v );
		return v;
  }

  /*
   * Returns a TargetNode for the given symbol.  If the symbol
   * exists, it is returned unchanged.  If the symbol does not exist, a new
   * symbol is inserted, with the given type of TargetNode.
   */
  private TargetNode findSymbol( String s, int type ) {
		TargetNode v = lookupSymbol( s );

		if ( v == null ) {
    	//v = newTarget( null );
    	v = newTarget( s );
	    v.type = type;
  	  symbols.put( s, v );
		}
    //else flag = true;
		return v;
  }

  private void putFile( String fname, TargetNode node ) {
    files.put(fname, node);
  }

  private TargetNode getFile( String fname ) {
    return (TargetNode)files.get(fname);
  }

  // following method will be called by com.antsoft.ant.codecontext.Parse.

  /*
   * Parser calls this before a new source file is started.
   */
  public void startFile( String fname ) {
    sourceNode = newTarget( fname );
    sourceNode.type = TargetNode.SOURCE;
    /*
    sourceNode = getFile( fname );
    if (sourceNode == null) {
    	sourceNode = newTarget( fname );
		sourceNode.type = TargetNode.SOURCE;
      //putFile( fname, sourceNode );
    }
    */

    packageName = null;

    imports = new Hashtable();
    wildImports = new Vector();
  }

  /*
   * Parser calls this when "package x.y.z" is found.
   */
  public void startPackage( String pname ) {
		packageName = pname;
  }

  /*
   * Parser calls this when "import a.b.c" is found.
   */
  public void addImport( String iname ) {
  	if (iname.startsWith("java") || iname.startsWith("javax")) return;
    int i = iname.lastIndexOf( '.' );

    // Ignore non-qualified import.  What does it mean, anyway??
    if ( i == -1 ) return;

    String prefix = iname.substring( 0, i );
    String suffix = iname.substring( i+1 );

    if ( suffix.equals( "*" ) ) wildImports.addElement( prefix );
		else imports.put( suffix, iname );
  }

  /*
   * Parser calls this when "class x" or "interface x" is found.
   */
  public void startDefinition( String dname ) {
    className = canonizePackageSymbol( dname );

    // FIXME: should we emit error if a symbol with the current className
    // already exists?
    classNode = findSymbol( className, TargetNode.CLASS );

    /*
    flag = false;
    classNode = findSymbol( className, TargetNode.CLASS );
    if (flag) {
      classNode.init();
      flag = false;
    }
    */

    //classNode.targetName = canonizeClassFile( sourceNode.targetName, className );
    classNode.targetName = className;
    classNode.type = TargetNode.CLASS;
    classNode.linkTo( sourceNode );

    Enumeration e = imports.elements();
    if (e != null)
    	while (e.hasMoreElements()) {
      	String fullName = (String)e.nextElement();
        if (MainFrame.getCodeContext().existUserClass(fullName))
				  classNode.linkTo( findSymbol( fullName, TargetNode.REFERENCE ) );
    //System.out.println(classNode.targetName + " 6 ===> " + fullName);
      }
    //System.out.println(classNode.targetName + " 1 ===> " + sourceNode.targetName);
  }

  /*
   * Given the name of a type, add a reference to it.
   * Parser calls this whenever some type name is referenced.
   * This can be: member variable type, static method call, cast,
   * etc, etc.
   */
  public void addTypeReference( String rname ) {
    // This algorithm is complicated by the fact that we don't properly
    // handle wildcard imports by actually finding what symbols are
    // imported.  There are three cases for a referenced type name.

    //System.out.println(" rname => "+rname);

    String pname = canonizePackageSymbol( rname );
    String iname = canonizeImportSymbol( rname );

    // Case 1: the name has already been defined

    TargetNode v = lookupSymbol( pname, TargetNode.CLASS );
    if ( v != null ) {
	    classNode.linkTo( v );
    //System.out.println(classNode.targetName + " 2 ===> " + v.targetName);
  	  return;
		}

    // Case 2: the name has been explicitly imported; we create a
    // new target, if necessary

    if ( iname != null ) {
		  classNode.linkTo( findSymbol( iname, TargetNode.REFERENCE ) );
    //System.out.println(classNode.targetName + " 3 ===> " + iname);
    	return;
		}

    // Case 3: none of the above; either the name will be defined in the
    // package, (in future) or it came from a wildcard import which may or
    // may not be defined in future; we add REFERENCE type TargetNodes for
    // all possibilities
    //
    // Another possibility is that it is an error/typo in the source; the
    // compiler will catch it, and then the user must run JavaDeps again
    // to build the correct dependencies

    if (MainFrame.getCodeContext().existUserClass(pname)) {
	    classNode.linkTo( findSymbol( pname, TargetNode.REFERENCE ) );
    //System.out.println(classNode.targetName + " 4 ===> " + pname);
      return;
    }

    for ( Enumeration e = wildImports.elements(); e.hasMoreElements(); ) {
		  String pkg = (String)e.nextElement() + "." + rname;
      if (MainFrame.getCodeContext().existUserClass(pkg)) {
	    	classNode.linkTo( findSymbol( pkg, TargetNode.REFERENCE ) );
    //System.out.println(classNode.targetName + " 5 ===> " + pkg);
      	break;
      }
		}
  }

  /*
   * This is used when a whole lot of references are parsed all at
   * once.  For example, a list of "implements".
   */
  public void addTypeReference( Vector v ) {
		for ( Enumeration e = v.elements(); e.hasMoreElements(); )
    	addTypeReference( (String)e.nextElement() );
  }

  /*
   * Given the name of a class member, add a reference to the class
   * containing it.
   */
  public void addMemberReference( String rname ) {
    // If the member is a simple name, (e.g. "foo") then it refers to the
    // current class, and we ignore it.  Otherwise, it is a compound name,
    // (e.g. "x.y.foo") so we strip off the last component, and add a
    // reference to the class name.

    int i = rname.lastIndexOf( '.' );
    if ( i >= 0 ) addTypeReference( rname.substring( 0, i ) );
  }

  /*
   * Called to indicate that the current class has a native method.
  public void hasNative() {
    TargetNode v = newTarget( className.replace( '.', '_' ) + ".h" );
    v.type = TargetNode.HEADER;
    v.linkTo( classNode );

    v = newTarget( className.replace( '.', '_' ) + ".c" );
    v.type = TargetNode.STUB;
    v.linkTo( classNode );
  }
   */
}
