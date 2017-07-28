/*
 * @(#)Compile.java	1.81 98/07/17
 *  (Original Source is sun.tools.javac.Main.java)
 *
 * Copyright 1994-1998 by Sun Microsystems, Inc.,
 * 901 San Antonio Road, Palo Alto, California, 94303, U.S.A.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Sun Microsystems, Inc. ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Sun.
 *
 * modified by Kim, Sung-Hoon.
 */

package com.antsoft.ant.compiler;

import com.antsoft.ant.main.MainFrame;

import sun.tools.java.*;
import sun.tools.javac.*;

import java.util.*;
import java.io.*;
import java.text.MessageFormat;

import javax.swing.SwingUtilities;

/**
 * Main program of the Java compiler
 */
public class Build implements sun.tools.java.Constants {
  String program;

  OutputStream out;

  // Constructor.
  public Build(OutputStream out, String program) {
		this.out = out;
		this.program = program;
  }

  /**
   * Exit status.
   * We introduce a separate integer status variable, and do not alter the
   * convention that 'compile' returns a boolean true upon a successful
   * compilation with no errors.  (JavaTest relies on this.)
   */
  public static final int EXIT_OK = 0;	// Compilation completed with no errors.
  public static final int EXIT_ERROR = 1;	// Compilation completed but reported errors.
  public static final int EXIT_CMDERR = 2;	// Bad command-line arguments and/or switches.
  public static final int EXIT_SYSERR = 3;	// System error or resource exhaustion.
  public static final int EXIT_ABNORMAL = 4;  // Compiler terminated abnormally.

  private int exitStatus;

  /**
   * Output a message.
   */
  private void output(String msg) {
		PrintWriter localout = new PrintWriter(out,true);
	  localout.println(msg);  }

  /**
   * Top level error message.  This method is called when the
   * environment could not be set up yet.
   */
  private void error(String msg) {
	  exitStatus = EXIT_CMDERR;
	  output(getText(msg));
  }

  private void error(String msg, String arg1) {
	  exitStatus = EXIT_CMDERR;
	  output(getText(msg, arg1));
  }

  private void error(String msg, String arg1, String arg2) {
	  exitStatus = EXIT_CMDERR;
	  output(getText(msg, arg1, arg2));
  }

  private static ResourceBundle messageRB;

  /**
   * Initialize ResourceBundle
   */
  static void initResource() {
  	try {
	    messageRB =	ResourceBundle.getBundle("com.antsoft.ant.compiler.javac");
	  } catch (MissingResourceException e) {
	    throw new Error("Fatal: Resource for javac is missing");
	  }
  }

  /**
   * get and format message string from resource
   */
  public static String getText(String key) {
	  return getText(key, (String)null);
  }

  public static String getText(String key, int num) {
	  return getText(key, Integer.toString(num));
  }

  public static String getText(String key, String fixed) {
	  return getText(key, fixed, null);
  }

  public static String getText(String key, String fixed1, String fixed2) {
	  return getText(key, fixed1, fixed2, null);
  }

  public static String getText(String key, String fixed1,String fixed2, String fixed3) {
	  if (messageRB == null) {
	    initResource();
	  }
	  try {
	    String message = messageRB.getString(key);
	    String[] args = new String[3];
	    args[0] = fixed1;
	    args[1] = fixed2;
	    args[2] = fixed3;
	    return MessageFormat.format(message, args);
	  } catch (MissingResourceException e) {
	    if (fixed1 == null)  fixed1 = "null";
	    if (fixed2 == null)  fixed2 = "null";
	    if (fixed3 == null)  fixed3 = "null";
	    String args[] = { key, fixed1, fixed2, fixed3 };
	    String message = "JAVAC MESSAGE FILE IS BROKEN: key={0}, arguments={1}, {2}, {3}";
	    return MessageFormat.format(message, args);
	  }
  }

  /**
   * Run the compiler
   */
  public synchronized boolean compile(String argv[]) {

    MainFrame.displayMessageAtStatusBar(" Compiling !!!");
    Compiler.isCompiling = true;

  	String classPathArg = null;
  	File destDir = null;
  	int flags = F_WARNINGS;

  	Vector v = new Vector();
  	String prior_O = null;

	  exitStatus = EXIT_OK;

    int fileIndex = 0;  // number of compiled source files.

  	// Parse arguments
	  for (int i = 0 ; i < argv.length ; i++) {
	    if (argv[i].equals("-O")) {
    		if (prior_O!=null && !(prior_O.equals("-O")))
		      error("main.conflicting.options", prior_O, "-O");
    		prior_O = "-O";
	    } else if (argv[i].equals("-nowarn")) {
		    flags &= ~F_WARNINGS;
	    } else if (argv[i].equals("-deprecation")) {
    		flags |= F_DEPRECATION;
	    } else if (argv[i].equals("-classpath")) {
    		if ((i + 1) < argv.length) {
		      classPathArg = argv[++i];
    		} else {
		      return false;  // Stop processing now
    		}
	    } else if (argv[i].equals("-d")) {
    		if ((i + 1) < argv.length) {
		      destDir = new File(argv[++i]);
  		    if (!destDir.exists()) {
      			return false; // Stop processing now
		      }
     		} else {
		      return false; // Stop processing now
  	   	}
	    } else if (argv[i].endsWith(".java")) {
		    v.addElement(argv[i]);
	    } else {
    		return false; // Stop processing now
	    }
	  }
	  if (v.size() == 0 || exitStatus == EXIT_CMDERR) {
	    return false;
	  }

    if (classPathArg == null) {
	    classPathArg = System.getProperty("java.class.path");
	    if (classPathArg == null) classPathArg = ".";
	  }

  	ClassPath classPath = new ClassPath(classPathArg);
    //System.out.println(" class path => "+classPathArg+ " 2 ==> "+classPath);
	  BatchEnvironment env = new BatchEnvironment(out,classPath);

  	env.flags |= flags;
  	String noMemoryErrorString = getText("main.no.memory");
	  String stackOverflowErrorString = getText("main.stack.overflow");

	  try {
	    // Parse all input files
	    for (Enumeration e = v.elements() ; e.hasMoreElements() ;) {
    		File file = new File((String)e.nextElement());
	      try {
          env.parseFile(new ClassFile(file));
	      } catch (FileNotFoundException ee) {
		      env.error(0, "cant.read", file.getPath());
		      exitStatus = EXIT_CMDERR;
	      }
	    }

	    // Do a post-read check on all newly-parsed classes,
	    // after they have all been read.
	    for (Enumeration e = env.getClasses() ; e.hasMoreElements() ; ) {
		    ClassDeclaration c = (ClassDeclaration)e.nextElement();
		    if (c.getStatus() == CS_PARSED) {
		      if (c.getClassDefinition().isLocal()) continue;
		      try {
			      c.getClassDefinition(env);
		      } catch (ClassNotFound ee) {}
		    }
	    }

	    // compile all classes that need compilation
	    ByteArrayOutputStream buf = new ByteArrayOutputStream(4096);
	    boolean done;

      String lastFile = null;
      String newFile = null;

	    do {
        done = true;
	      env.flushErrors();
	      for (Enumeration e = env.getClasses() ; e.hasMoreElements() ; ) {
	        ClassDeclaration c = (ClassDeclaration)e.nextElement();
					//System.out.println(" class ==> "+c.getName());
		      SourceClass src;

		    switch (c.getStatus()) {
		      case CS_UNDEFINED:
				    if (!env.dependencies()) break;
      			// fall through

		      case CS_SOURCE:
    				done = false;
		    		env.loadDefinition(c);
				    if (c.getStatus() != CS_PARSED) break;
      			// fall through

		      case CS_PARSED:
            //System.out.println(" CS_PARSED1 ");
      			if (c.getClassDefinition().isInsideLocal()) continue;
            //System.out.println(" CS_PARSED2 ");
      			done = false;
      			src = (SourceClass)c.getClassDefinition(env);
			      src.check(env);
      			c.setDefinition(src, CS_CHECKED);
			      // fall through

		      case CS_CHECKED:
      			src = (SourceClass)c.getClassDefinition(env);
			      // bail out if there were any errors
      			if (src.getError()) {
			        c.setDefinition(src, CS_COMPILED);
			        break;
      			}
      			done = false;
			      buf.reset();

            String msg = " Compiling ["+ ++fileIndex +"] "+src.getAbsoluteName();
            MainFrame.displayMessageAtStatusBar(msg);

      			src.compile(buf);
						//System.out.println(" src == > "+ msg);
      			c.setDefinition(src, CS_COMPILED);
			      src.cleanup(env);

      			String pkgName = c.getName().getQualifier().toString().replace('.', File.separatorChar);
			      String className = c.getName().getFlatName().toString().replace('.', SIGC_INNERCLASS) + ".class";

      			File file;
			      if (destDir != null) {
			        if (pkgName.length() > 0) {
        				file = new File(destDir, pkgName);
				        if (!file.exists()) {
      				    file.mkdirs();
			        	}
       		  		file = new File(file, className);
			        } else {
				        file = new File(destDir, className);
    			    }
		      	} else {
			        ClassFile classfile = (ClassFile)src.getSource();
    			    if (classfile.isZipped()) {
		        		env.error(0, "cant.write", classfile.getPath());
        				exitStatus = EXIT_CMDERR;
				        continue;
     			    }
		    	    file = new File(classfile.getPath());
			        file = new File(file.getParent(), className);
      			}

			      // Create the file
      			try {
              //newFile = src.getAbsoluteName();
              if (lastFile==null||!newFile.equals(lastFile)) {
                //lastFile = newFile;
                //final String msg = " Compiling ["+ ++fileIndex +"] "+lastFile;

                //SwingUtilities.invokeLater(new Runnable(){
                  //public void run(){
                    //MainFrame.displayMessageAtStatusBar(msg);
                  //}
                //});
              }

    			    FileOutputStream out = new FileOutputStream(file.getPath());
		    	    buf.writeTo(out);
			        out.close();
	        	} catch (IOException ee) {
      			  env.error(0, "cant.write", file.getPath());
	      		  exitStatus = EXIT_CMDERR;
		      	}
		      }
		    }
	    } while (!done);
      System.out.println(" loop out ");
	  } catch (OutOfMemoryError ee) {
	    env.output(noMemoryErrorString);
	    exitStatus = EXIT_SYSERR;
	    return false;
	  } catch (StackOverflowError ee) {
	    env.output(stackOverflowErrorString);
	    exitStatus = EXIT_SYSERR;
	    return false;
	  } catch (Error ee) {
	    if (env.nerrors == 0 || env.dump()) {
		    ee.printStackTrace();
		    env.error(0, "fatal.error");
		    exitStatus = EXIT_ABNORMAL;
	    }
	  } catch (Exception ee) {
	    if (env.nerrors == 0 || env.dump()) {
		    ee.printStackTrace();
		    env.error(0, "fatal.exception");
		    exitStatus = EXIT_ABNORMAL;
	    }
	  }

	  env.flushErrors();
	  env.shutdown();

	  boolean status = true;
	  if (env.nerrors > 0) {
	    String msg = "";
	    if (env.nerrors > 1) {
		    msg = getText("main.errors", env.nerrors);
	    } else {
		    msg = getText("main.1error");
	    }
	    if (env.nwarnings > 0) {
		    if (env.nwarnings > 1) {
		      msg += ", " + getText("main.warnings", env.nwarnings);
		    } else {
		      msg += ", " + getText("main.1warning");
		    }
	    }
	    output(msg);
	    if (exitStatus == EXIT_OK) {
		    // Allow EXIT_CMDERR or EXIT_ABNORMAL to take precedence.
		    exitStatus = EXIT_ERROR;
	    }
	    status = false;
	  } else {
	    if (env.nwarnings > 0) {
		    if (env.nwarnings > 1) {
		      output(getText("main.warnings", env.nwarnings));
		    } else {
		      output(getText("main.1warning"));
		    }
	    }
	  }

    Compiler.isCompiling = false;

  	return status;

	}

  /*
  public static void main(String[] argv) {
  	Build build = new Build(System.out, "test");
    System.out.println(" argv ");
    for (int i=0;i<argv.length;++i) System.out.println(i+"th ==> "+argv[i]);
    build.compile(argv);
  }
  */
}
