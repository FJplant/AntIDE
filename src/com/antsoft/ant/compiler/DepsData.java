
package com.antsoft.ant.compiler;

import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.io.File;

import com.antsoft.ant.main.MainFrame;
import com.antsoft.ant.util.QuickSorter;

/*
 * @author kahn(modify)
 **/
public class DepsData {
	//private static Hashtable tables = new Hashtable();
	public static DepTable deps = null;
  public static Vector depends;

  private static int caseNum;

  private static final int MAKEPROJECT = 1;
  private static final int BUILDPROJECT = 2;
  private static final int MAKEFILES = 3;
  private static final int BUILDFILES = 4;
  private static final int MAKEFILE = 5;
  private static final int BUILDFILE = 6;

  private static double counter = 0.001;

  private static Hashtable deptable = null;

  public static void reportDeps() {
  	long elapse = System.currentTimeMillis();

    for ( Enumeration e = deps.g.getComponents();e.hasMoreElements(); ) {
			Vector comp = (Vector)e.nextElement();
			if ( comp.size() == 1 ) emitSimpleBuildRule( (TargetNode)comp.firstElement());
			else emitCompoundBuildRule(comp);
    }

    elapse = System.currentTimeMillis() - elapse;
    //System.out.println(" elapsed time => "+elapse);
  }

  public static void makeBuildProjectData() {
  	depends = new Vector(1,1);
    caseNum = BUILDPROJECT;

    for ( Enumeration e = deps.g.getComponents();e.hasMoreElements(); ) {
			Vector comp = (Vector)e.nextElement();
			if ( comp.size() == 1 ) emitSimpleBuildRule( (TargetNode)comp.firstElement());
			else emitCompoundBuildRule( comp);
    }
	}

  public static void makeMakeProjectData() {
  	depends = new Vector(1,1);
    caseNum = MAKEPROJECT;

    for ( Enumeration e = deps.g.getComponents();e.hasMoreElements(); ) {
			Vector comp = (Vector)e.nextElement();
			if ( comp.size() == 1 ) emitSimpleBuildRule( (TargetNode)comp.firstElement());
			else emitCompoundBuildRule( comp);
    }
	}

  ////////////////////////////////////////////////////////////////////////////////

  public static void makeFileData(String fileName) {
  	deptable = new Hashtable();
    caseNum = MAKEFILE;

    for ( Enumeration e = deps.g.getComponents();e.hasMoreElements(); ) {
			Vector comp = (Vector)e.nextElement();
			if ( comp.size() == 1 ) emitSimpleBuildRule( (TargetNode)comp.firstElement());
			else emitCompoundBuildRule( comp);
    }

    depends = new Vector(1,1);
    String className = MainFrame.getCodeContext().getRelativeClassFile(fileName);

  	Vector v = (Vector)deptable.get(className);
    makeFileData(v, className);
    if (depends.size() == 0) depends.addElement(fileName);
    deptable = null;
  }

  private static void makeFileData(Vector v, String origin) {
    StringBuffer buf = new StringBuffer("");
  	for (int i = 0; i < v.size() ; ++i) {
    	String classFile = (String)v.elementAt(i);
      if (classFile.trim().endsWith(".java"))	{
      	if (toBeMaked(classFile.trim())) {
        	depends.removeElement(classFile.trim());
        	buf.append(classFile.trim()+" ");
        }
      }
      else {
        Vector v2 = (Vector)deptable.get(classFile.trim());
        if (v2 != null) makeFileData(v2, classFile);
      }
    }

    String putData = buf.toString().trim();
    if (!putData.equals("")) {
      depends.removeElement(putData);
    	depends.addElement(putData);
    }
  }

  public static void buildFileData(String fileName) {
  	deptable = new Hashtable();
    caseNum = BUILDFILE;

    for ( Enumeration e = deps.g.getComponents();e.hasMoreElements(); ) {
			Vector comp = (Vector)e.nextElement();
			if ( comp.size() == 1 ) emitSimpleBuildRule( (TargetNode)comp.firstElement());
			else emitCompoundBuildRule( comp);
    }

    depends = new Vector(1,1);
    String className = MainFrame.getCodeContext().getRelativeClassFile(fileName);

  	Vector v = (Vector)deptable.get(className);
    buildFileData(v, className);
    deptable = null;
  }

  private static void buildFileData(Vector v, String origin) {
    StringBuffer buf = new StringBuffer("");
  	for (int i = 0; i < v.size() ; ++i) {
    	String classFile = (String)v.elementAt(i);
      if (classFile.trim().endsWith(".java"))	{
      	if (toBeMaked(classFile.trim())) {
        	depends.removeElement(classFile.trim());
        	buf.append(classFile.trim()+" ");
        }
      }
      else {
        Vector v2 = (Vector)deptable.get(classFile.trim());
        if (v2 != null) buildFileData(v2, classFile);
      }
    }

    String putData = buf.toString().trim();
    if (!putData.equals("")) {
      depends.removeElement(putData);
    	depends.addElement(putData);
    }
  }


  ////////////////////////////////////////////////////////////////////////////////

  private static Hashtable proof;

  public static void makeFilesData(Vector fileList) {
  	deptable = new Hashtable();
    caseNum = MAKEFILES;
    counter = 0;

    for ( Enumeration e = deps.g.getComponents();e.hasMoreElements(); ) {
			Vector comp = (Vector)e.nextElement();
			if ( comp.size() == 1 ) emitSimpleBuildRule( (TargetNode)comp.firstElement());
			else emitCompoundBuildRule( comp);
    }
    depends = new Vector(1,1);
    proof = new Hashtable();
    for (int i = 0; i < fileList.size() ; ++i) {
    	String fileName = (String)fileList.elementAt(i);
	    String className = MainFrame.getCodeContext().getRelativeClassFile(fileName);

      if (proof.get(className) == null) {
        Vector v = (Vector)deptable.get(className);
        makeFilesData(v, className);
      }
    }

		fileList = QuickSorter.sort(depends, QuickSorter.LESS_STRING);
    depends = null;

    depends = new Vector();
    for (int i = 0; i < fileList.size() ; ++i) {
    	StringTokenizer t = new StringTokenizer((String)fileList.elementAt(i), " ");
      t.nextElement();
      depends.addElement(t.nextElement());
    }

    deptable = null;
    proof = null;
  }

  private static void makeFilesData(Vector v, String origin) {
    StringBuffer buf = new StringBuffer("");
    buf.append((String)v.firstElement()+" ");
  	for (int i = 1; i < v.size() ; ++i) {
    	String classFile = ((String)v.elementAt(i)).trim();
      if (classFile.endsWith(".java"))	{
      	if (toBeMaked(classFile)) {
        	depends.removeElement(classFile);
        	buf.append(classFile+" ");
        }
        proof.put(MainFrame.getCodeContext().getRelativeClassFile(classFile), "");
      }
      else {
        Vector v2 = (Vector)deptable.get(classFile);
        if (v2 != null) makeFilesData(v2, classFile);
      }
    }

    String putData = buf.toString().trim();
    if (!putData.equals("")) {
      depends.removeElement(putData);
    	depends.addElement(putData);
    }
  }

  public static void buildFilesData(Vector fileList) {
  	deptable = new Hashtable();
    caseNum = BUILDFILES;
    counter = 0.001;

    for ( Enumeration e = deps.g.getComponents();e.hasMoreElements(); ) {
			Vector comp = (Vector)e.nextElement();
			if ( comp.size() == 1 ) emitSimpleBuildRule( (TargetNode)comp.firstElement() );
			else emitCompoundBuildRule( comp );
    }

    depends = new Vector(1,1);
    proof = new Hashtable();
    for (int i = 0; i < fileList.size() ; ++i) {
    	String fileName = (String)fileList.elementAt(i);
	    String className = MainFrame.getCodeContext().getRelativeClassFile(fileName);

      if (proof.get(className) == null) {
        Vector v = (Vector)deptable.get(className);
        buildFilesData(v, className);
      }
    }

		fileList = QuickSorter.sort(depends, QuickSorter.LESS_STRING);
    depends = null;

    depends = new Vector();
    for (int i = 0; i < fileList.size() ; ++i) {
    	StringTokenizer t = new StringTokenizer((String)fileList.elementAt(i), " ");
      t.nextElement();
      depends.addElement(t.nextElement());
    }

    deptable = null;
    proof = null;
  }

  private static void buildFilesData(Vector v, String origin) {
    StringBuffer buf = new StringBuffer("");
    buf.append((String)v.firstElement()+" ");
  	for (int i = 1; i < v.size() ; ++i) {
    	String classFile = ((String)v.elementAt(i)).trim();
      if (classFile.endsWith(".java"))	{
      	depends.removeElement(classFile);
      	buf.append(classFile+" ");
        proof.put(MainFrame.getCodeContext().getRelativeClassFile(classFile), "");
      }
      else {
        Vector v2 = (Vector)deptable.get(classFile.trim());
        if (v2 != null) buildFilesData(v2, classFile);
      }
    }

    String putData = buf.toString().trim();
    if (!putData.equals("")) {
      depends.removeElement(putData);
    	depends.addElement(putData);
    }
  }

  //////////////////////////////////////////////////////////////////////////////

  // write file names to Vector object.
  private static void emitRule( String sources) {
  	//System.out.println(" source => "+sources);
  	depends.removeElement(sources);
  	depends.addElement(sources);
  }

  private static void emitRule( String target, String sources) {
  	Vector v = new Vector(1);
    v.addElement(sources);
    //System.out.println(" source => "+sources);
    deptable.put(target, v);
  }

  private static void emitRule( String target, Vector sources) {
  	//System.out.println(" sources ==> "+sources);
    deptable.put(target, sources);
  }

  private static void emitSimpleBuildRule( TargetNode v ) {
		switch ( v.type ) {
      case TargetNode.REFERENCE:
      case TargetNode.SOURCE:
          break;

      case TargetNode.CLASS:
				String sources = v.neighbours( TargetNode.SOURCE );
				String classes = v.neighbours( TargetNode.CLASS );

        switch (caseNum) {
					case MAKEPROJECT :
          	if (toBeMaked(sources)) emitRule (sources);
            break;
        	case BUILDPROJECT :
          	emitRule(sources);
            break;

        	case MAKEFILE :
        	case BUILDFILE :
          	Vector temp1 = new Vector(1,1);
            temp1.addElement(sources);
            if (!classes.equals("")) temp1.addElement(classes);
          	emitRule(v.targetName, temp1);
            //emitRule(v.targetName, classes);
          	break;

        	case BUILDFILES :
        	case MAKEFILES :
          	Vector temp = new Vector(2);
            temp.addElement(""+counter);
            counter = counter + 0.001;
            temp.addElement(sources);
            if (!classes.equals("")) temp.addElement(classes);
            emitRule(v.targetName, temp);
          	break;
	      }
				break;

			default:
	  	  System.out.println( "warning : uknown node in dependency graph" );
		}
  }

  private static void emitCompoundBuildRule( Vector comp ) {
    Hashtable neighbourHash = new Hashtable();
    Vector targets = new Vector(1,1);
    Vector sources = new Vector(1,1);
    Vector classes = new Vector(1,1);

    // Initialize the neighbourHash with the class nodes in the component,
    // since we don't want circular .class dependencies
    for ( Enumeration e = comp.elements(); e.hasMoreElements(); ) {
	    TargetNode v = (TargetNode)e.nextElement();
      try {
		    neighbourHash.put( v.targetName, v );
      } catch (NullPointerException eee) {
      	System.out.println(eee.toString()+" "+v.targetName);
      }
		}

		for ( Enumeration e = comp.elements(); e.hasMoreElements(); ) {
	    TargetNode v = (TargetNode)e.nextElement();
	    if ( v.type != TargetNode.CLASS ) {
				System.out.println( "warning : non-CLASS node in compound rule" );
				continue;
	    }

	    targets.addElement(v.targetName);
      //classes.addElement(v.targetName);
	    for ( Enumeration e2 = v.neighbours(); e2.hasMoreElements(); ) {
				TargetNode u = (TargetNode)e2.nextElement();
				if ( u.targetName != null && neighbourHash.get( u.targetName ) == null ) {
			    neighbourHash.put( u.targetName, u );
			    if ( u.type == TargetNode.SOURCE ) {//sources += u.targetName + " ";
          	sources.addElement(u.targetName);
            classes.addElement(u.targetName);
          }
			    else if ( u.type == TargetNode.CLASS ) //classes += u.targetName + " ";
          	classes.addElement(u.targetName);
				}
	    }
		}

    switch (caseNum) {
      case MAKEPROJECT :
      	StringBuffer buf1 = new StringBuffer();
        for (int i = 0; i < sources.size() ; ++i) {
        	String file = (String)sources.elementAt(i);
	        if (toBeMaked(file)) buf1.append(file + " ");
				}
        emitRule(buf1.toString());
        break;
      case BUILDPROJECT :
      	StringBuffer buf2 = new StringBuffer();
        for (int i = 0; i < sources.size() ; ++i) {
        	String file = (String)sources.elementAt(i);
	        buf2.append(file + " ");
				}
        emitRule(buf2.toString());
        break;

      case MAKEFILE :
      case BUILDFILE :
      	for (int i = 0; i < targets.size() ; ++i) {
					String target = (String)targets.elementAt(i);
          emitRule(target, classes);
        }
        break;

      case BUILDFILES :
      case MAKEFILES :
		    classes.insertElementAt(""+counter, 0);
        counter = counter + 0.001;
      	for (int i = 0; i < targets.size() ; ++i) {
					String target = (String)targets.elementAt(i);
          emitRule(target, classes);
        }
        break;
    }

  }

  private static boolean toBeMaked(String sourceName) {
    String className = MainFrame.getCodeContext().getRelativeClassFileName(sourceName);

    //System.out.println(" class name => "+className);
    if (className == null) return true;

    File f = new File(className.trim());
    if (!f.exists()) return true;

    File f2 = new File(sourceName);
    if (f2.exists() && (f.lastModified() < f2.lastModified())) return true;

    return false;
  }
}
