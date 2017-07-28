/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/manager/projectmanager/ProjectManager.java,v 1.12 1999/08/30 08:08:05 remember Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.12 $
 * IDE에서 프로젝트들을 관리하는 모듈
 */

package com.antsoft.ant.manager.projectmanager;

import java.io.*;
import java.util.Vector;
import java.util.StringTokenizer;
import java.util.Hashtable;
import javax.swing.tree.*;

import com.antsoft.ant.main.*;
import com.antsoft.ant.property.*;
import com.antsoft.ant.property.projectproperty.*;
import com.antsoft.ant.util.FileClassLoader;
import com.antsoft.ant.util.Constants;
import com.antsoft.ant.util.DayInfo;
/**
 *  class ProjectManager
 *                                          
 *  @author Jinwoo Baek
 */
public class ProjectManager {
  static private Vector projects = new Vector(1, 1);
	static private Project current = null;

	/**
	 *  새로운 프로젝트를 생성할 때
	 *
	 *  @param pathName 생성할 프로젝트 파일 경로
	 *  @param fileName 생성할 프로젝트 이름
	 */

  static public Project newProject(String pathName, String prjName, String comment) {

		Project prj = new Project(prjName, pathName);
		if (prj != null) {
			projects.addElement(prj);
			prj.setPathModel(Main.property.getPathModelClone());
      File f = new File(pathName);
      ((DefaultPathModel)prj.getPathModel()).setSourceRoot(f.getParent() + File.separator + "source");
      ((DefaultPathModel)prj.getPathModel()).setOutputRoot(f.getParent() + File.separator + "classes");
      ((DefaultPathModel)prj.getPathModel()).setDocumentRoot(f.getParent() + File.separator + "docs");
      prj.setComment(comment);
      prj.setCreatedTime( DayInfo.getFormattedDayStr() );
		}
		return getProject(prjName);
  }

	/**
	 *  이름으로 프로젝트 객체를 얻는다.
	 */
  static public Project getProject(String prjName) {
    Project prj = findProject(prjName);
    return prj;
  }

	/**
	 *  파일로부터 프로젝트 객체를 연다.
	 */
  static public Project openProject(String pathName, String fileName) {
		Project prj = null;

    long first = System.currentTimeMillis();

    Hashtable files = new Hashtable(50, 0.9F);
		if ((pathName != null) && (fileName != null) && fileName.toLowerCase().endsWith(".apr")) {
			try {
        File projFile = new File(pathName, fileName);
	    	prj = new Project("", projFile.getCanonicalPath());
        DefaultPathModel pathModel = new DefaultPathModel();
        LibInfoContainer alic = new LibInfoContainer();
        LibInfoContainer slic = new LibInfoContainer();
        JdkInfoContainer ajic = new JdkInfoContainer();
        CompilerOptionModel compilerModel = new CompilerOptionModel();
        InterpreterOptionModel interpreterModel = new InterpreterOptionModel();
        DefaultTreeModel treeModel = null;
        DefaultMutableTreeNode parent = null;
        DefaultMutableTreeNode treeRoot = null;
        String recentFolderPID = null;

        BufferedReader in = new BufferedReader(new FileReader(projFile));
        String line = null;
        Hashtable nodes = new Hashtable(30, 0.9F);

        StringTokenizer st = null;

        //project file의 이름을 제외한 path
        String prjFilePath = prj.getPathFile().getParent();

        while ((line = in.readLine()) != null) {
          st = new StringTokenizer(line);
          String key = st.nextToken("=");

          // key 값에 따라 적당하게 프로젝트의 데이터들을 채운다.
          if (key.equals("File")) {
            if (st.hasMoreElements()) {
              String path = st.nextToken("#");
              String fullPath = pathModel.getSourceRoot() + path.substring(1);

              ProjectFileEntry pfe = new ProjectFileEntry( fullPath, pathModel.getSourceRoot());
              files.put(pfe.getFullPathName(), pfe);
              parent.insert(new ProjectPanelTreeNode(pfe), 0);
            }
            continue;
          }

          else if (key.equals("Folder")) {
          	if (st.hasMoreElements()) {
	          	String id = st.nextToken("#");
  	          String name = st.nextToken("#");
    	        String pid = st.nextToken("#");
              recentFolderPID = pid;

      	      ProjectPanelTreeNode pptn = new ProjectPanelTreeNode(name);
        	    pptn.setID(id);
          	  pptn.setParentID(pid);

              Object p = nodes.get(pid);
              if(p != null) parent = (ProjectPanelTreeNode)p;
              else parent = treeRoot;

		          treeModel.insertNodeInto(pptn, parent, 0);

  	          parent = pptn;
              nodes.put(pptn.getID(), pptn);
            }
            continue;
          }

          else if (key.equals("ProjectVersion")) {
            if (st.hasMoreElements()) {
            	if (!st.nextToken().equals("1.0")) {
                throw new Exception();
              }
          	}
            else {
                throw new Exception();
            }
          } else if (key.equals("ProjectName")) {
          	if (st.hasMoreElements()) {
	          	prj.setProjectName(st.nextToken());
  	          treeModel = new DefaultTreeModel(new DefaultMutableTreeNode(prj.getProjectName()));
    	        parent = (DefaultMutableTreeNode)treeModel.getRoot();
              treeRoot = parent;
            }
          } else if (key.equals("Comment")) {
          	if (st.hasMoreElements()) {
	          	prj.setComment(st.nextToken());
            }
          } else if (key.equals("LastID")) {
          	if (st.hasMoreElements()) {
	          	try {
		          	prj.setLastID(Long.parseLong(st.nextToken()));
    	        } catch (NumberFormatException e) {
      	      	prj.setLastID(0L);
        	    }
            }
          } else if (key.equals("CreatedTime")) {
          	if (st.hasMoreElements()) {
            	prj.setCreatedTime(st.nextToken());
            }
          } else if (key.equals("UseParam")) {
          	if (st.hasMoreElements()) {
	          	boolean b = Boolean.valueOf(st.nextToken()).booleanValue();
  	          prj.setUsableParam(b);
            }
          } else if (key.equals("Parameters")) {
          	if (st.hasMoreElements()) {
            	prj.setParameters(st.nextToken());
            }

          }  else if (key.equals("PM_AllLib_Infos")) {
		          String libName = st.nextToken();
              if(libName != null){
                LibInfo info = (LibInfo)Main.property.getPathModel().getAllLibInfoContainer().getLibraryInfo(libName);
                if(info != null) alic.addLibraryInfo((LibInfo)info.clone());
              }

          } else if (key.equals("PM_JDK_Infos")) {
		          String jdkVersion = st.nextToken();
              if(jdkVersion != null){
                JdkInfo info = Main.property.getPathModel().getJdkInfoContainer().getJdkInfo(jdkVersion);
                if(info != null) ajic.addJdkInfo((JdkInfo)info.clone());
              }

          } else if (key.equals("PM_Selected_Lib_Infos")) {
		          String libName = st.nextToken();
              if(libName != null){
                LibInfo info = (LibInfo)Main.property.getPathModel().getAllLibInfoContainer().getLibraryInfo(libName);
                if(info != null) slic.addLibraryInfo((LibInfo)info.clone());
              }

          } else if (key.equals("PM_Current_JDKInfo")) {
		          String jdkVersion = st.nextToken();
              if(jdkVersion != null){
                JdkInfo info = Main.property.getPathModel().getJdkInfoContainer().getJdkInfo(jdkVersion);
                if(info != null) pathModel.setCurrentJdkInfo((JdkInfo)info.clone());
              }
          } else if (key.equals("PM_Source_Root")) {
          	if (st.hasMoreElements()) {
              String path = st.nextToken("#");
              File f = new File(path);
              if(!f.exists()) f.mkdir();
	          	pathModel.setSourceRoot(path);

            }
          } else if (key.equals("PM_Output_Root")) {
	         	if (st.hasMoreElements()) {
              String path = st.nextToken("#");
              File f = new File(path);
              if(!f.exists()) f.mkdir();
  	        	pathModel.setOutputRoot(path);
            }
          } else if (key.equals("PM_Document_Root")) {
            if (st.hasMoreElements()) {
              String path = st.nextToken("#");
              File f = new File(path);
              if(!f.exists()) f.mkdir();
              pathModel.setDocumentRoot(path);
            }
          } else if (key.equals("PM_ClassPath")) {
          	if (st.hasMoreElements()) {
	          	pathModel.setClassPath(st.nextToken("#"));
            }
          }
          // Compiler Option
          else if (key.equals("Compile_ShowDebugMsg")) {
          	if (st.hasMoreElements()) {
	          	boolean b = Boolean.valueOf(st.nextToken()).booleanValue();
							compilerModel.setDebugMode(b);
            }
          } else if (key.equals("Compile_Optimize")) {
          	if (st.hasMoreElements()) {
	          	boolean b = Boolean.valueOf(st.nextToken()).booleanValue();
  	          compilerModel.setOptimizing(b);
            }
          } else if (key.equals("Compile_Warning")) {
          	if (st.hasMoreElements()) {
	          	boolean b = Boolean.valueOf(st.nextToken()).booleanValue();
  	          compilerModel.setWarning(b);
            }
          } else if (key.equals("Compile_Verbose")) {
          	if (st.hasMoreElements()) {
	          	boolean b = Boolean.valueOf(st.nextToken()).booleanValue();
  	          compilerModel.setVerboseMsg(b);
            }
          } else if (key.equals("Compile_Deprecation")) {
          	if (st.hasMoreElements()) {
	          	boolean b = Boolean.valueOf(st.nextToken()).booleanValue();
  	          compilerModel.setDeprecation(b);
            }
          } else if (key.equals("Compile_Depend")) {
          	if (st.hasMoreElements()) {
	          	boolean b = Boolean.valueOf(st.nextToken()).booleanValue();
  	          compilerModel.setDebugMode(b);
            }
          } else if (key.equals("Compile_NoWrite")) {
          	if (st.hasMoreElements()) {
	          	boolean b = Boolean.valueOf(st.nextToken()).booleanValue();
  	          compilerModel.setNoWrite(b);
            }
          } else if (key.equals("Compile_Common")) {
          	if (st.hasMoreElements()) {
	          	boolean b = Boolean.valueOf(st.nextToken()).booleanValue();
  	          compilerModel.setCommon(b);
            }
          } else if (key.equals("Compile_CommonOption")) {
          	if (st.hasMoreElements()) {
	          	String str = st.nextToken();
  	          compilerModel.setCommonOption(str);
            }
          } else if (key.equals("Compile_internalVM")) {
          	if (st.hasMoreElements()) {
	          	boolean b = Boolean.valueOf(st.nextToken()).booleanValue();
  	          compilerModel.setInternalVMMode(b);
            }

					// Interpreter Option
          } else if (key.equals("Interpreter_Verbose")) {
          	if (st.hasMoreElements()) {
	          	boolean b = Boolean.valueOf(st.nextToken()).booleanValue();
  	          interpreterModel.setVerboseMode(b);
            }
          } else if (key.equals("Interpreter_Debug")) {
          	if (st.hasMoreElements()) {
	          	boolean b = Boolean.valueOf(st.nextToken()).booleanValue();
  	          interpreterModel.setDebugMode(b);
            }
          } else if (key.equals("Interpreter_NoAsyncGC")) {
          	if (st.hasMoreElements()) {
	          	boolean b = Boolean.valueOf(st.nextToken()).booleanValue();
  	          interpreterModel.setNoAsyncGCMode(b);
            }
          } else if (key.equals("Interpreter_VerboseGC")) {
          	if (st.hasMoreElements()) {
	          	boolean b = Boolean.valueOf(st.nextToken()).booleanValue();
  	          interpreterModel.setVerboseGCMode(b);
            }
          } else if (key.equals("Interpreter_NoClassGC")) {
          	if (st.hasMoreElements()) {
	          	boolean b = Boolean.valueOf(st.nextToken()).booleanValue();
  	          interpreterModel.setNoClassGCMode(b);
            }
          } else if (key.equals("Interpreter_MaxNatStack")) {
          	if (st.hasMoreElements()) {
	          	boolean b = Boolean.valueOf(st.nextToken()).booleanValue();
  	          interpreterModel.setMaxNatStackMode(b);
            }
          } else if (key.equals("Interpreter_MaxNatStackSize")) {
          	if (st.hasMoreElements()) {
	          	String str = st.nextToken();
  	          interpreterModel.setMaxNatStackSize(str);
            }
          } else if (key.equals("Interpreter_MaxJavaStack")) {
          	if (st.hasMoreElements()) {
	          	boolean b = Boolean.valueOf(st.nextToken()).booleanValue();
  	          interpreterModel.setMaxJavaStackMode(b);
            }
          } else if (key.equals("Interpreter_MaxJavaStackSize")) {
          	if (st.hasMoreElements()) {
	          	String str = st.nextToken();
  	          interpreterModel.setMaxJavaStackSize(str);
            }
          } else if (key.equals("Interpreter_InitHeap")) {
          	if (st.hasMoreElements()) {
	          	boolean b = Boolean.valueOf(st.nextToken()).booleanValue();
  	          interpreterModel.setInitHeapMode(b);
            }
          } else if (key.equals("Interpreter_InitHeapSize")) {
          	if (st.hasMoreElements()) {
	          	String str = st.nextToken();
			        interpreterModel.setInitHeapSize(str);
            }
          } else if (key.equals("Interpreter_MaxHeap")) {
          	if (st.hasMoreElements()) {
	          	boolean b = Boolean.valueOf(st.nextToken()).booleanValue();
  	          interpreterModel.setMaxHeapMode(b);
            }
          } else if (key.equals("Interpreter_MaxHeapSize")) {
          	if (st.hasMoreElements()) {
	          	String str = st.nextToken();
  	          interpreterModel.setMaxHeapSize(str);
          	}
          } else if (key.equals("Interpreter_Common")) {
          	if (st.hasMoreElements()) {
	          	boolean b = Boolean.valueOf(st.nextToken()).booleanValue();
  	          interpreterModel.setCommonMode(b);
            }
          } else if (key.equals("Interpreter_CommonOption")) {
            String option="";
          	if (st.hasMoreElements()) {
              option = st.nextToken("#");
            }

            if(!option.equals("")) interpreterModel.setCommonOption(option);

          } else if (key.equals("Interpreter_MainClass")) {
          	if (st.hasMoreElements()) {
	          	boolean b = Boolean.valueOf(st.nextToken()).booleanValue();
  	          interpreterModel.setMainClassMode(b);
            }
          } else if (key.equals("Interpreter_MainClassName")) {
          	if (st.hasMoreElements()) {
	          	String str = st.nextToken();
  	          interpreterModel.setMainClassName(str);
            }
          } else if (key.equals("Interpreter_InternalVM")) {
          	if (st.hasMoreElements()) {
	          	boolean b = Boolean.valueOf(st.nextToken()).booleanValue();
	            interpreterModel.setInternalVMMode(b);
            }
          }
        }
        if (prj != null) {
	        pathModel.setAllLibInfoContainer(alic);
  	      pathModel.setSelectedLibInfoContainer(slic);
    	    pathModel.setJdkInfoContainer(ajic);

          prj.setLastSavedTime();
	        prj.setTreeModel(treeModel);
  	      prj.setPathModel(pathModel);
    	    prj.setCompilerModel(compilerModel);
      	  prj.setInterpreterModel(interpreterModel);
        	prj.isModified = false;
          prj.initLibraryPool();
          prj.syncEntrysWithRealFile();
          projects.addElement(prj);
        }

        nodes.clear();
        nodes = null;

			} catch (IOException e) { return null;
			} catch (Exception e2) { return null; }
		}

    if(prj != null){
      prj.setFiles(files);
      prj.updateClassLoader();
      long end = System.currentTimeMillis() - first;
    }  
		return prj;
  }

	/**
	 *  해당 프로젝트 저장
	 *
	 *  @param prj 프로젝트 객체
	 */
	static public void saveProject(Project prj) {
		if (prj != null) {
			prj.isModified = false;
      prj.setLastSavedTime();

			try {
	      PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
        										new FileOutputStream(new File(prj.getPath())))));
      	out.print("ProjectVersion=1.0" + Constants.lineSeparator);
				out.print(prj.toString());
        out.flush();
        out.close();

			} catch (IOException e) {
      	e.printStackTrace();
			}
		}
	}

	/**
	 *  프로젝트가 열려있는지 본다.
	 */
  static public Project findProject(String prjName) {
    for (int i = 0; i < projects.size(); i++) {
      Project prj = (Project)projects.elementAt(i);
      if (prj.getProjectName().equals(prjName)) {
        return prj;
      }
    }

    return null;
  }

  /**
   *  프로젝트를 삭제한다.
   */
  static public void removeProject(String prjName) {
    for (int i = 0; i < projects.size(); i++) {
      Project prj = (Project)projects.elementAt(i);
      if (prj.getProjectName().equals(prjName)) {
        projects.removeElementAt(i);
        System.gc();
        break;
      }
    }
	}

	/**
	 *  현재 활성화된 프로젝트를 나타낸다.
	 *
	 *  @param current 현재 활성화된 프로젝트 객체
	 */
	static public void setCurrentProject(Project prj) {
		current = prj;
	}

	/**
	 *  현재 활성화된 프로젝트를 얻어온다.
	 *
	 *  @return Project 현재 활성화된 프로젝트 객체
	 */
	static public Project getCurrentProject() {
		return current;
	}

  /**
   *  열었던 모든 프로젝트를 얻어온다.
   */
  static public Vector getProjects() {
  	return projects;
  }

	/**
	 *  저장되지 않은 프로젝트가 있는지 본다.
	 */
  static public void canClose() {
    for (int i = 0; i < projects.size(); i++) {
      Project prj = (Project)projects.elementAt(i);
      if (prj.isModified()) {
				try {
					ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(
																					new File(prj.getPath(), prj.getProjectName())));
					if (out != null) {
						out.writeObject(prj);
						out.flush();
					}
					out.close();
				} catch (IOException e) {
				}
			}
    }
  }
}
