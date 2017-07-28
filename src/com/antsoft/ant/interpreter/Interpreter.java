/*
 * $Id: Interpreter.java,v 1.5 1999/08/25 03:30:02 multipia Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.5 $
 */
package com.antsoft.ant.interpreter;

import java.io.*;
import com.antsoft.ant.manager.projectmanager.*;
import com.antsoft.ant.property.*;
import com.antsoft.ant.property.projectproperty.*;
import com.antsoft.ant.util.Constants;

/**
 *  class Interpreter
 *
 *  @author Jinwoo Baek
 */
public class Interpreter {
	private InterpreterOptionModel model = null;
  private Project project = null;
  private Output stdout = null;
  private Output stderr = null;
  private Process p = null;
  private String[] cmd = null;
  static boolean lock = true;

	public Interpreter(Project prj) {
  	this.project = prj;
    if (project != null) model = project.getInterpreterModel();
  }

  public void interpret(String className, Output stdout, Output stderr ) {
  	this.stdout = stdout;
  	this.stderr = stderr;
  	
  	int index = 0;
		JdkInfo info = project.getPathModel().getCurrentJdkInfo();
    String classPath = "\"" + project.getPathModel().getClassPath() + "\"";
    
  	InterpreterOptionModel model = project.getInterpreterModel();
		String[] cmd2 = new String[15];
    cmd2[index++] = info.getJavawEXEPath();
    if ((classPath != null) && !classPath.trim().equals("")) {
    	cmd2[index++] = "-classpath";
      cmd2[index++] = classPath;
    }

    if (model.getVerboseMode()) cmd2[index++] = model.getVerboseOption();
    if (model.getDebugMode()) cmd2[index++] = model.getDebugOption();
    if (model.getNoAsyncGCMode()) cmd2[index++] = model.getNoAsyncGCOption();
    if (model.getVerboseGCMode()) cmd2[index++] = model.getVerboseGCOption();
    if (model.getNoClassGCMode()) cmd2[index++] = model.getNoClassGCOption();
    if (model.getMaxNatStackMode()) cmd2[index++] = model.getMaxNatStackOption();
    if (model.getMaxJavaStackMode()) cmd2[index++] = model.getMaxJavaStackOption();
    if (model.getInitHeapMode()) cmd2[index++] = model.getInitHeapOption();
    if (model.getMaxHeapMode()) cmd2[index++] = model.getMaxHeapOption();
    if (model.getCommonMode()) cmd2[index++] = model.getCommonOption();
    if (model.getMainClassMode() && !model.getMainClassName().trim().equals(""))
    	cmd2[index++] = model.getMainClassName();
    else cmd2[index++] = className;
    if (project.isUsableParam()) cmd2[index++] = project.getParameters();

    StringBuffer commandStr = new StringBuffer();
  	cmd = new String[index];
    for (int i = 0; i < cmd.length; i++) {
    	cmd[i] = cmd2[i];
      commandStr.append(cmd[i] + " ");
//      System.out.println("cmd [" + i + "] : " + cmd[i]);
    }

    stdout.appendText(commandStr + Constants.lineSeparator);

    try {
    	Runtime rt = Runtime.getRuntime();
      if (p != null) { p.destroy(); p = null; }
      p = rt.exec(cmd);
      Thread t1 = new StreamReaderThread(p.getInputStream(), stdout);
      Thread t2 = new StreamReaderThread(p.getErrorStream(), stderr);
      t1.start();
      t2.start();
    } catch (Exception e) {
    }
  }

  public void kill() {
  	if (p != null) p.destroy();
  }

  class StreamReaderThread extends Thread {
  	LineNumberReader reader = null;
  	Output output = null;
  	public StreamReaderThread( InputStream in, Output output ) {
    	if (in != null)
	     	reader = new LineNumberReader(new InputStreamReader(in));
			this.output = output;
      this.setPriority(Thread.MIN_PRIORITY);
    }

    public void run() {
    	try {
	    	String outStr = null;
  	    while ((reader != null) && (outStr = reader.readLine()) != null) {
    	  	output.appendText(outStr + Constants.lineSeparator);
      	  Thread.yield();
	      }
  	    reader.close();
      } catch (IOException e) {
      }
    }
  }
}
/*
 * $Log: Interpreter.java,v $
 * Revision 1.5  1999/08/25 03:30:02  multipia
 * stdout과 stderr를 구분해서 출력하도록 수정
 *
 */
