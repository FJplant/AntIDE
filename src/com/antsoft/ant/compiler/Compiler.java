/*
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/compiler/Compiler.java,v 1.15 1999/09/01 07:12:55 kahn Exp $
 * $Revision: 1.15 $
 */

package com.antsoft.ant.compiler;

import java.io.*;
import java.util.Vector;
// by strife
import java.util.StringTokenizer;
import javax.swing.SwingUtilities;

import com.antsoft.ant.main.MainFrame;
import com.antsoft.ant.manager.projectmanager.*;
import com.antsoft.ant.property.*;
import com.antsoft.ant.property.projectproperty.CompilerOptionModel;
import com.antsoft.ant.util.Constants;
import com.antsoft.ant.codecontext.CodeContext;

/**
 *  class Compiler
 *
 *  @author Jinwoo Baek
 */
public class Compiler {//implements Runnable {
	public static final int VERSION_1_1 = 1;
  public static final int VERSION_1_2 = 2;

  // 현재 1.1 버전과 1.2 버전의 옵션이 다르다.
  private int compilerVersion = 0;
  // 컴파일러의 파일 경로
  private String compilerPath = null;
  // 컴파일러 옵션
  private CompilerOptionModel model = null;
	// 커맨드 라인 명령
  private String cmd[] = null;
  // output
  private CompileOutputPanel outputPanel = null;
  // project - 컴파일 관련 정보를 담고 있다.
  private Project project = null;
	private int i = 0; // cmd를 넣기 위한 변수
  private int numOfFiles = 0; // 컴파일할 파일 개수

  private Process p = null;

  public static boolean isCompiling = false;

  /**
   *  Constructor
   */
	public Compiler(Project prj) {
    this.project = prj;

    if (project != null) {
    	model = project.getCompilerModel();
    }
  }

  public int getVersion() {
  	return compilerVersion;
  }

  public OutputStream getOutputStream() {
  	return null;
  }

  /**
   *  파일들을 가지고 컴파일한다.(성훈이 수정)
   */
  public boolean buildInternalVM(Vector fileList, CompileOutputPanel pl) {
  	this.outputPanel = pl;
    boolean error = false;
//		boolean status = false; // 컴파일을 할 수 있는가 본다.
    numOfFiles = fileList.size();
    cmd = new String[16];
    for (int j = 0; j < cmd.length; j++) cmd[j] = "";

		JdkInfo info = project.getPathModel().getCurrentJdkInfo();
    if (info != null) {
			compilerPath = info.getJavacEXEPath();
			String classpath = project.getPathModel().getClassPath();
			String sourcepath = project.getPathModel().getSourcePath();
			String outputroot = project.getPathModel().getOutputRoot();

      if (model != null) {
        if (model.getDebugMode()) cmd[i++] = model.getDebugOption();
        if (model.getOptimizing()) cmd[i++] = model.getOptimizeOption();
        if (model.getWarning()) cmd[i++] = model.getWarningOption();
        if (model.getVerboseMsg()) cmd[i++] = model.getVerboseOption();
        if (model.getDeprecation()) cmd[i++] = model.getDepreOption();
        if (model.getDepend()) cmd[i++] = model.getDependOption();
        if (model.getNoWrite()) cmd[i++] = model.getNoWriteOption();
        if (model.getCommon()) cmd[i++] = model.getCommonOption();
      }

      if ((classpath != null) && (!classpath.equals(""))) {
        cmd[i++] = "-classpath";
        //cmd[i++] = "\"" + classpath + "\"";
        cmd[i++] = classpath;
      }

      /*
      if ((sourcepath != null) && (!sourcepath.equals(""))) {
        cmd[i++] = "-sourcepath";
        cmd[i++] = sourcepath;
      }
      */

      if ((outputroot != null) && (!outputroot.equals(""))) {
        cmd[i++] = "-d";
        cmd[i++] = outputroot;
      }

      Vector v = new Vector(20, 5);
      isCompiling = true;

      ByteArrayOutputStream bo = new ByteArrayOutputStream();  // 에러 메세지를 임의의 출력스트림을 통해 출력한다.
      int index = 0;
      for (int j = 0; j < numOfFiles; ++j) {
        String fileName = (String)fileList.elementAt(j);
        StringTokenizer lex = new StringTokenizer(fileName, " ");
        int h = i;
        while (lex != null && lex.hasMoreTokens()) {
        	String oneFile = (String)lex.nextToken();
          cmd[h] = oneFile.trim();
          h++;
	        final String msg = " Compile [ " + (index++) + " ] : " + oneFile;
  	      SwingUtilities.invokeLater(new Runnable(){
    	      public void run(){
      	      MainFrame.displayMessageAtStatusBar(msg);
        	  }});
	        System.out.println(msg);
        }

        String[] newCmd = new String[h];
        for (int k = 0; k < h; ++k) newCmd[k] = cmd[k];

        //for (int kk = 0; kk <= i; ++kk)
         //	System.out.print(newCmd[kk]+" ");
        //System.out.println();

        sun.tools.javac.Main javac = new sun.tools.javac.Main(bo, "javac");
        error = javac.compile(newCmd);

        if (!error) break;
      }

      isCompiling = false;
      outputPanel.setText(bo.toString());

      bo = null;

      if (!error) MainFrame.displayMessageAtStatusBar(" Compile : Fail with errors");
      else MainFrame.displayMessageAtStatusBar(" Compile : Success with no errors");
    }
		else {
      MainFrame.displayMessageAtStatusBar(" Compile : No JDK !!! ");
    	return false;
		}

    return error;
  }

  /**
   *  파일들을 가지고 컴파일한다.
   */
  public boolean buildExternalVM(Vector fileList, CompileOutputPanel pl) {
  	this.outputPanel = pl;
    boolean error = false;
//		boolean status = false; // 컴파일을 할 수 있는가 본다.
    numOfFiles = fileList.size();
    cmd = new String[15 + numOfFiles];
    for (int j = 0; j < cmd.length; j++) cmd[j] = "";

		JdkInfo info = project.getPathModel().getCurrentJdkInfo();
    if (info != null) {
			compilerPath = info.getJavacEXEPath();
			String classpath = project.getPathModel().getClassPath();
			String sourcepath = project.getPathModel().getSourcePath();
			String outputroot = project.getPathModel().getOutputRoot();
			if ((compilerPath != null) && (!compilerPath.equals(""))) {
				cmd[i++] = compilerPath;

	    	if (model != null) {
  	    	if (model.getDebugMode()) cmd[i++] = model.getDebugOption();
    	    if (model.getOptimizing()) cmd[i++] = model.getOptimizeOption();
      	  if (model.getWarning()) cmd[i++] = model.getWarningOption();
        	if (model.getVerboseMsg()) cmd[i++] = model.getVerboseOption();
	        if (model.getDeprecation()) cmd[i++] = model.getDepreOption();
          if (model.getDepend()) cmd[i++] = model.getDependOption();
          if (model.getNoWrite()) cmd[i++] = model.getNoWriteOption();
          if (model.getCommon()) cmd[i++] = model.getCommonOption();
  	    }

				if ((classpath != null) && (!classpath.equals(""))) {
					cmd[i++] = "-classpath";
					cmd[i++] = "\"" + classpath + "\"";
				}

        /*
				if ((sourcepath != null) && (!sourcepath.equals(""))) {
					cmd[i++] = "-sourcepath";
					cmd[i++] = sourcepath;
				}
        */

				if ((outputroot != null) && (!outputroot.equals(""))) {
					cmd[i++] = "-d";
					cmd[i++] = outputroot;
				}

        Vector v = new Vector(20, 5);
        isCompiling = true;

	      try {
          for (int j = 0; j < numOfFiles; j++) {
          	cmd[i] = (String)fileList.elementAt(j);
            ++i;
          }
//          for (int j = 0; j < numOfFiles; j++) {
          //String fileName = (String)fileList.elementAt(j);
          final String msg = " Compile.... Wait a minite !!! ";
          //final String msg = " Compile[ "+j+" ] "+fileName;
          SwingUtilities.invokeLater(new Runnable(){
            public void run(){
              MainFrame.displayMessageAtStatusBar(msg);
            }});
          //System.out.println(msg);
          //cmd[i] = fileName;

          Runtime rt = Runtime.getRuntime();
          p = rt.exec(cmd);

          BufferedInputStream out = new BufferedInputStream(p.getInputStream());

          LineNumberReader lnr = new LineNumberReader(new InputStreamReader(p.getErrorStream()));
          String outStr = null;
          while((outStr = lnr.readLine()) != null){
            v.addElement(outStr);
            if (!error &&
            		(outStr.toLowerCase().indexOf("error") != -1 || outStr.toLowerCase().indexOf("오류") != -1))
              error = true;
          }

          //if (error) break;
          out.close();

          //}

          outputPanel.setText(v);
        } catch (IOException e) {
          MainFrame.displayMessageAtStatusBar(" Exception Occurred on Compiling ..");
          System.out.println(e.toString());
        }

        isCompiling = false;

        outputPanel.setText("compilaion done..." + Constants.lineSeparator);

        if (error) MainFrame.displayMessageAtStatusBar(" Compile : Fail with errors");
        else MainFrame.displayMessageAtStatusBar(" Compile : Success with no errors");
        if (p != null) p.destroy();
			}
		}

    return !error;
	}

  public boolean compile(Vector fileList, CompileOutputPanel pl) {
    if (fileList.size()==0) {
      MainFrame.displayMessageAtStatusBar(" Compile completed with no Error !!!");
      return true;
    }

    //for (int i = 0; i < fileList.size() ; ++i)
    	//System.out.println(" file["+i+"]"+fileList.elementAt(i));

    boolean flag = false;

    if (model.getInternalVMMode()) flag = buildInternalVM(fileList,pl);
    else flag = buildExternalVM(fileList,pl);

    DepsData.depends = null;
    DepsData.deps = null;

    return flag;
  }

  public boolean makeProject(CompileOutputPanel pl) {
    MainFrame.displayMessageAtStatusBar(" Preparing to Compile");

    if (!model.getInternalVMMode()) {
    	Vector files = CodeContext.getPE().getProject().getFiles();
      Vector list = new Vector(files.size());
      for (int i = 0; i < files.size(); ++i) {
      	ProjectFileEntry pfe = (ProjectFileEntry)files.elementAt(i);
        String fullName = pfe.getFullPathName();
        String classFileName = MainFrame.getCodeContext().getRelativeClassFileName(fullName);
        File f = new File(classFileName);

        if (!f.exists() || f.lastModified() < pfe.getLastModifiedTime())
        	list.addElement(fullName);
			}
      return compile(list , pl);
    }

  	MainFrame.getCodeContext().makeDepsTable();
		DepsData.makeMakeProjectData();
    return compile(DepsData.depends, pl);
  }

  public boolean buildProject(CompileOutputPanel pl) {
    MainFrame.displayMessageAtStatusBar(" Preparing to Compile");

    if (!model.getInternalVMMode()) {
    	Vector files = CodeContext.getPE().getProject().getFiles();
      Vector list = new Vector(files.size());
      for (int i = 0; i < files.size(); ++i) {
      	ProjectFileEntry pfe = (ProjectFileEntry)files.elementAt(i);
      	list.addElement(pfe.getFullPathName());
			}
      return compile(list , pl);
    }

  	MainFrame.getCodeContext().makeDepsTable();
		DepsData.makeBuildProjectData();
    return compile(DepsData.depends, pl);
  }

  public boolean makeFile(String fileName, CompileOutputPanel pl) {
    MainFrame.displayMessageAtStatusBar(" Preparing to Compile");

  	/*
  	MainFrame.getCodeContext().makeDepsTable();
  	DepsData.makeFileData(fileName);
  	return compile(DepsData.depends, pl);
    */

    Vector list = new Vector(1,1);
    list.addElement(fileName);
 		return compile(list, pl);
  }

  public boolean buildFile(String fileName, CompileOutputPanel pl) {
    MainFrame.displayMessageAtStatusBar(" Preparing to Compile");

  	/*
  	MainFrame.getCodeContext().makeDepsTable();
  	DepsData.buildFileData(fileName);
  	return compile(DepsData.depends, pl);
    */
    if (!model.getInternalVMMode()) {
    	Vector list = new Vector(1,1);
    	return compile(list, pl);
    }

  	MainFrame.getCodeContext().makeDepsTable();
  	DepsData.makeFileData(fileName);
  	return compile(DepsData.depends, pl);
  }

  public boolean makeFiles(Vector fileList, CompileOutputPanel pl) {
    MainFrame.displayMessageAtStatusBar(" Preparing to Compile");

    if (!model.getInternalVMMode()) {
    	return compile(fileList, pl);
    }

  	MainFrame.getCodeContext().makeDepsTable();
    DepsData.makeFilesData(fileList);
  	return compile(DepsData.depends, pl);
  }

  public boolean buildFiles(Vector fileList, CompileOutputPanel pl) {
    MainFrame.displayMessageAtStatusBar(" Preparing to Compile");

    if (!model.getInternalVMMode()) return compile(fileList, pl);

  	MainFrame.getCodeContext().makeDepsTable();
    DepsData.buildFilesData(fileList);
  	return compile(DepsData.depends, pl);
  }

  public void setModel(CompilerOptionModel model) {
  	this.model = model;
  }

  public CompilerOptionModel getModel() {
  	return model;
  }
}

