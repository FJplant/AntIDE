/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/property/projectproperty/InterpreterOptionModel.java,v 1.3 1999/07/22 03:39:32 multipia Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.3 $
 */
package com.antsoft.ant.property.projectproperty;

import java.io.Serializable;

import com.antsoft.ant.manager.projectmanager.ProjectManager;
import com.antsoft.ant.property.JdkInfo;
import com.antsoft.ant.util.Constants;

/**
 *  class Interpreter
 *
 *  @author Jinwoo Baek
 */
public class InterpreterOptionModel implements Serializable {
	private boolean isVerbose = false;
  private boolean isDebug = false;
  private boolean isNoAsyncGC = false;
  private boolean isVerboseGC = false;
  private boolean isNoClassGC = false;
//  private boolean isCheckSource = false;
  private boolean isMaxNatStack = false;
  private boolean isMaxJavaStack = false;
  private boolean isInitHeap = false;
  private boolean isMaxHeap = false;
//  private boolean isReduceOSSignal = false;
//  private boolean isCheckJNIFunc = false;
//  private boolean isVerify = false;
//  private boolean isVerifyRemote = false;
//  private boolean isNoVerify = false;
	private boolean isCommon = false;
  private boolean isMainClass = false;
  private boolean isInternalVM = false;

  private String verboseOption = "-verbose";
  private String verboseOptionMsg = "Enable verbose output";
  private String debugOption11 = "-debug";
  private String debugOption12 = "-Xdebug";
  private String debugOptionMsg = "Enable remote debugging";
  private String noAsyncGCOption = "-noasyncgc";
  private String noAsyncGCOptionMsg = "Don't allow asynchronous garbage collection";
  private String verboseGCOption = "-verbosegc";
  private String verboseGCOptionMsg = "Print a message when garbage collection occurs";
  private String noClassGCOption11 = "-noclassgc";
  private String noClassGCOption12 = "-Xnoclassgc";
  private String noClassGCOptionMsg = "Disable class garbage collection";
//  private String checkSourceOption = "-cs";
//  private String checkSourceOptionMsg = "Check if source is newer when loading classes";
  private String maxNatStackOption = "-ss";
  private String maxNatStackSize = "";
  private String maxNatStackOptionMsg = "Set the maximum native stack size for any thread";
  private String maxJavaStackOption = "-oss";
  private String maxJavaStackSize = "";
  private String maxJavaStackOptionMsg = "Set the maximum Java stack size for any thread";
  private String initHeapOption11 = "-ms";
  private String initHeapOption12 = "-Xms";
  private String initHeapSize = "";
  private String initHeapOptionMsg = "Set the initial Java heap size";
  private String maxHeapOption11 = "-mx";
  private String maxHeapOption12 = "-Xmx";
  private String maxHeapSize = "";
  private String maxHeapOptionMsg = "Set the maximum Java heap size";
/*
  private String reduceOSSignalOption = "-Xrs";
  private String reduceOSSignalOptionMsg = "Reduce the use of OS signals";
  private String checkJNIFuncOption = "-Xcheck:jni";
  private String checkJNIFuncOptionMsg = "Perform additional checks for JNI functions";
  private String verifyOption = "-verify";
  private String verifyOptionMsg = "Verify all classes when read in";
  private String verifyRemoteOption = "-verifyremote";
  private String verifyRemoteOptionMsg = "Verify classes read in over the network(default)";
  private String noVerifyOption = "-noverify";
  private String noVerifyOptionMsg = "Don't verify an class";
*/
  private String commonOption = "";
  private String commonOptionMsg = "Common Options";
  private String mainClassName = "";
  private String mainClassMsg = "Set Executable Main Class";
  private String internalVMOptionMsg = "Use internal VM";
  private String externalVMOptionMsg = "Use external VM";

	public InterpreterOptionModel() {
  }

  /**
   *  프로젝트 파일에 저장되는 형태로 나타낸다.
   */
  public String toString() {
  	StringBuffer out = new StringBuffer();

  	// 옵션 값들
    out.append("Interpreter_Verbose=" + isVerbose + Constants.lineSeparator);
    out.append("Interpreter_Debug=" + isDebug + Constants.lineSeparator);
    out.append("Interpreter_NoAsyncGC=" + isNoAsyncGC + Constants.lineSeparator);
    out.append("Interpreter_VerboseGC=" + isVerboseGC + Constants.lineSeparator);
    out.append("Interpreter_NoClassGC=" + isNoClassGC + Constants.lineSeparator);
    out.append("Interpreter_MaxNatStack=" + isMaxNatStack + Constants.lineSeparator);
    out.append("Interpreter_MaxNatStackSize=" + maxNatStackSize + Constants.lineSeparator);
    out.append("Interpreter_MaxJavaStack=" + isMaxJavaStack + Constants.lineSeparator);
    out.append("Interpreter_MaxJavaStackSize=" + maxJavaStackSize + Constants.lineSeparator);
    out.append("Interpreter_InitHeap=" + isInitHeap + Constants.lineSeparator);
    out.append("Interpreter_InitHeapSize=" + initHeapSize + Constants.lineSeparator);
    out.append("Interpreter_MaxHeap=" + isMaxHeap + Constants.lineSeparator);
    out.append("Interpreter_MaxHeapSize=" + maxHeapSize + Constants.lineSeparator);
    out.append("Interpreter_Common=" + isCommon + Constants.lineSeparator);
    out.append("Interpreter_CommonOption=" + commonOption + Constants.lineSeparator);
    out.append("Interpreter_MainClass=" + isMainClass + Constants.lineSeparator);
    out.append("Interpreter_MainClassName=" + mainClassName + Constants.lineSeparator);
    out.append("Interpreter_InternalVM=" + isInternalVM + Constants.lineSeparator);
/*
    // 각종 문자열 값들
    out.append("Interpreter_VerboseOption=" + verboseOption + Constants.lineSeparator);
    out.append("Interpreter_DebugOption11=" + debugOption11 + Constants.lineSeparator);
    out.append("Interpreter_DebugOption12=" + debugOption12 + Constants.lineSeparator);
    out.append("Interpreter_NoAsyncGCOption=" + noAsyncGCOption + Constants.lineSeparator);
    out.append("Interpreter_VerboseGCOption=" + verboseGCOption + Constants.lineSeparator);
    out.append("Interpreter_NoClassGCOption11=" + noClassGCOption11 + Constants.lineSeparator);
    out.append("Interpreter_NoClassGCOption12=" + noClassGCOption12 + Constants.lineSeparator);
    out.append("Interpreter_MaxNatStackOption=" + maxNatStackOption + Constants.lineSeparator);
    out.append("Interpreter_MaxJavaStatckOption=" + maxJavaStackOption + Constants.lineSeparator);
    out.append("Interpreter_InitHeapOption11=" + initHeapOption11 + Constants.lineSeparator);
    out.append("Interpreter_InitHeapOption12=" + initHeapOption12 + Constants.lineSeparator);
    out.append("Interpreter_MaxHeapOption11=" + maxHeapOption11 + Constants.lineSeparator);
    out.append("Interpreter_MaxHeapOption12=" + maxHeapOption12 + Constants.lineSeparator);

    out.append("Interpreter_VerboseOptionMsg=" + verboseOptionMsg + Constants.lineSeparator);
    out.append("Interpreter_DebugOptionMsg=" + debugOptionMsg + Constants.lineSeparator);
    out.append("Interpreter_NoAsyncGCOptionMsg=" + noAsyncGCOptionMsg + Constants.lineSeparator);
    out.append("Interpreter_VerboseGCOptionMsg=" + verboseGCOptionMsg + Constants.lineSeparator);
    out.append("Interpreter_NoClassGCOptionMsg=" + noClassGCOptionMsg + Constants.lineSeparator);
    out.append("Interpreter_MaxNatStackOptionMsg=" + maxNatStackOptionMsg + Constants.lineSeparator);
    out.append("Interpreter_MaxJavaStatckOptionMsg=" + maxJavaStackOptionMsg + Constants.lineSeparator);
    out.append("Interpreter_InitHeapOptionMsg=" + initHeapOptionMsg + Constants.lineSeparator);
    out.append("Interpreter_MaxHeapOptionMsg=" + maxHeapOptionMsg + Constants.lineSeparator);
    out.append("Interpreter_CommonOptionMsg=" + commonOptionMsg + Constants.lineSeparator);
    out.append("Interpreter_MainClassOptionMsg=" + mainClassMsg + Constants.lineSeparator);
    out.append("Interpreter_InternalVMOptionMsg=" + internalVMOptionMsg + Constants.lineSeparator);
    out.append("Interpreter_ExternalVMOptionMsg=" + externalVMOptionMsg + Constants.lineSeparator);
*/
    return out.toString();
  }

  public boolean getVerboseMode() {
  	return isVerbose;
  }

  public void setVerboseMode(boolean b) {
  	isVerbose = b;
  }

  public boolean getDebugMode() {
  	return isDebug;
	}

  public void setDebugMode(boolean b) {
  	isDebug = b;
  }

  public boolean getNoAsyncGCMode() {
  	return isNoAsyncGC;
  }

  public void setNoAsyncGCMode(boolean b) {
  	isNoAsyncGC = b;
  }

  public boolean getVerboseGCMode() {
  	return isVerboseGC;
  }

  public void setVerboseGCMode(boolean b) {
  	isVerboseGC = b;
  }

  public boolean getNoClassGCMode() {
  	return isNoClassGC;
  }

  public void setNoClassGCMode(boolean b) {
  	isNoClassGC = b;
  }
/*
  public boolean getCheckSourceMode() {
  	return isCheckSource;
  }

  public void setCheckSourceMode(boolean b) {
  	isCheckSource = b;
  }
*/
  public boolean getMaxNatStackMode() {
  	return isMaxNatStack;
	}

  public void setMaxNatStackMode(boolean b) {
  	isMaxNatStack = b;
  }

  public boolean getMaxJavaStackMode() {
  	return isMaxJavaStack;
  }

  public void setMaxJavaStackMode(boolean b) {
  	isMaxJavaStack = b;
  }

  public boolean getInitHeapMode() {
  	return isInitHeap;
  }

  public void setInitHeapMode(boolean b) {
  	isInitHeap = b;
  }

  public boolean getMaxHeapMode() {
  	return isMaxHeap;
  }

  public void setMaxHeapMode(boolean b) {
  	isMaxHeap = b;
  }
/*
  public boolean getReduceOSSignalMode() {
  	return isReduceOSSignal;
  }

  public void setReduceOSSignalMode(boolean b) {
  	isReduceOSSignal = b;
  }

  public boolean getCheckJNIFuncMode() {
  	return isCheckJNIFunc;
  }

  public void setCheckJNIFuncMode(boolean b) {
  	isCheckJNIFunc = b;
  }

  public boolean getVerifyMode() {
  	return isVerify;
  }

  public void setVerifyMode(boolean b) {
  	isVerify = b;
  }

  public boolean getVerifyRemoteMode() {
  	return isVerifyRemote;
	}

  public void setVerifyRemoteMode(boolean b) {
  	isVerifyRemote = b;
  }

  public boolean getNoVerifyMode() {
  	return isNoVerify;
  }

  public void setNoVerifyMode(boolean b) {
  	isNoVerify = b;
  }
*/
	public boolean getCommonMode() {
  	return isCommon;
  }

  public void setCommonMode(boolean b) {
  	isCommon = b;
  }

  public boolean getMainClassMode() {
  	return isMainClass;
  }

  public void setMainClassMode(boolean b) {
  	isMainClass = b;
  }

  public boolean getInternalVMMode() {
  	return isInternalVM;
  }

  public void setInternalVMMode(boolean b) {
  	isInternalVM = b;
  }

  //////////////////////////////////////
  public void setMaxNatStackSize(String size) {
  	maxNatStackSize = size;
  }

  public String getMaxNatStackSize() {
  	return maxNatStackSize;
  }

  public void setMaxJavaStackSize(String size) {
  	maxJavaStackSize = size;
  }

  public String getMaxJavaStackSize() {
  	return maxJavaStackSize;
  }

  public void setInitHeapSize(String size) {
  	initHeapSize = size;
  }

  public String getInitHeapSize() {
  	return initHeapSize;
  }

  public void setMaxHeapSize(String size) {
  	maxHeapSize = size;
  }

  public String getMaxHeapSize() {
  	return maxHeapSize;
  }

  /////////////////////////////////////
  public String getVerboseOption() {
  	return verboseOption;
  }

  public void setVerboseOption(String op) {
  	verboseOption = op;
  }

  public String getDebugOption() {
  	JdkInfo info = ProjectManager.getCurrentProject().getPathModel().getCurrentJdkInfo();
    String ver = info.getVersion();
  	if (ver.indexOf("1.2") != -1) return debugOption12;
    else return debugOption11;
  }

  public void setDebugOption(String op) {
  	debugOption11 = op;
  }

  public String getNoAsyncGCOption() {
  	return noAsyncGCOption;
  }

  public void setNoAsyncGCOption(String op) {
  	noAsyncGCOption = op;
  }

  public String getVerboseGCOption() {
  	return verboseGCOption;
  }

  public void setVerboseGCOption(String op) {
  	verboseGCOption = op;
  }

  public String getNoClassGCOption() {
  	JdkInfo info = ProjectManager.getCurrentProject().getPathModel().getCurrentJdkInfo();
    String ver = info.getVersion();
  	if (ver.indexOf("1.2") != -1) return noClassGCOption12;
    else return noClassGCOption11;
	}
/*
  public String getCheckSourceOption() {
  	return checkSourceOption;
  }

  public void setCheckSourceOption(String op) {
  	checkSourceOption = op;
  }
*/
  public String getMaxNatStackOption() {
    if (!maxNatStackSize.equals(""))
    	return maxNatStackOption + maxNatStackSize + "m";
    else return maxNatStackOption;
  }

  public void setMaxNatStackOption(String op) {
  	maxNatStackOption = op;
  }

  public String getMaxJavaStackOption() {
  	if (!maxJavaStackSize.equals(""))
    	return maxJavaStackOption + maxJavaStackSize + "m";
    else return maxJavaStackOption;
  }

  public void setMaxJavaStackOption(String op) {
  	maxJavaStackOption = op;
  }

  public String getInitHeapOption() {
  	JdkInfo info = ProjectManager.getCurrentProject().getPathModel().getCurrentJdkInfo();
    String ver = info.getVersion();
  	if (ver.indexOf("1.2") != -1) {
	  	if (!initHeapSize.equals(""))
  	  	return initHeapOption12 + initHeapSize + "m";
    	else return initHeapOption12;
    }
    else {
	  	if (!initHeapSize.equals(""))
  	  	return initHeapOption11 + initHeapSize + "m";
    	else return initHeapOption11;
    }
  }

  public void setInitHeapOption(String op) {
  	initHeapOption11 = op;
  }

  public String getMaxHeapOption() {
  	JdkInfo info = ProjectManager.getCurrentProject().getPathModel().getCurrentJdkInfo();
    String ver = info.getVersion();
  	if (ver.indexOf("1.2") != -1) {
	  	if (!maxHeapSize.equals(""))
  	  	return maxHeapOption12 + maxHeapSize + "m";
    	else return maxHeapOption12;
    }
    else {
	  	if (!maxHeapSize.equals(""))
  	  	return maxHeapOption11 + maxHeapSize + "m";
    	else return maxHeapOption11;
    }
  }

  public void setMaxHeapOption(String op) {
  	maxHeapOption11 = op;
  }
/*
  public String getReduceOSSignalOption() {
  	return reduceOSSignalOption;
  }

  public void setReduceOSSignalOption(String op) {
  	reduceOSSignalOption = op;
	}

  public String getCheckJNIFuncOption() {
  	return checkJNIFuncOption;
  }

  public void setCheckJNIFuncOption(String op) {
  	checkJNIFuncOption = op;
  }

  public String getVerifyOption() {
  	return verifyOption;
  }

  public void setVerifyOption(String op) {
  	verifyOption = op;
  }

  public String getVerifyRemoteOption() {
  	return verifyRemoteOption;
  }

  public void setVerifyRemoteOption(String op) {
  	verifyRemoteOption = op;
  }

  public String getNoVerifyOption() {
  	return noVerifyOption;
  }

  public void setNoVerifyOption(String op) {
  	noVerifyOption = op;
  }
*/
  public String getCommonOption() {
  	return commonOption;
  }

  public void setCommonOption(String op) {
  	commonOption = op;
  }

  public String getMainClassName() {
  	return mainClassName;
  }

  public void setMainClassName(String name) {
  	mainClassName = name;
  }

  //////////////////////////////////////
  public String getVerboseOptionMsg() {
  	return verboseOptionMsg;
  }

  public void setVerboseOptionMsg(String msg) {
  	verboseOptionMsg = msg;
  }

  public String getDebugOptionMsg() {
  	return debugOptionMsg;
  }

  public void setDebugOptionMsg(String msg) {
  	debugOptionMsg = msg;
  }

  public String getNoAsyncGCOptionMsg() {
  	return noAsyncGCOptionMsg;
  }

  public void setNoAsyncGCOptionMsg(String msg) {
  	noAsyncGCOptionMsg = msg;
  }

  public String getVerboseGCOptionMsg() {
  	return verboseGCOptionMsg;
  }

  public void setVerboseGCOptionMsg(String msg) {
  	verboseGCOptionMsg = msg;
  }

  public String getNoClassGCOptionMsg() {
  	return noClassGCOptionMsg;
  }

  public void setNoClassGCOptionMsg(String msg) {
  	noClassGCOptionMsg = msg;
  }
/*
  public String getCheckSourceOptionMsg() {
  	return checkSourceOptionMsg;
  }

  public void setCheckSourceOptionMsg(String msg) {
  	checkSourceOptionMsg = msg;
  }
*/
  public String getMaxNatStackOptionMsg() {
  	return maxNatStackOptionMsg;
  }

  public void setMaxNatStackOptionMsg(String msg) {
  	maxNatStackOptionMsg = msg;
  }

  public String getMaxJavaStackOptionMsg() {
  	return maxJavaStackOptionMsg;
  }

  public void setMaxJavaStackOptionMsg(String msg) {
  	maxJavaStackOptionMsg = msg;
  }

  public String getInitHeapOptionMsg() {
  	return initHeapOptionMsg;
  }

  public void setInitHeapOptionMsg(String msg) {
  	initHeapOptionMsg = msg;
  }

  public String getMaxHeapOptionMsg() {
  	return maxHeapOptionMsg;
  }

  public void setMaxHeapOptionMsg(String msg) {
  	maxHeapOptionMsg = msg;
  }
/*
  public String getReduceOSSignalOptionMsg() {
  	return reduceOSSignalOptionMsg;
  }

  public void setReduceOSSignalOptionMsg(String msg) {
  	reduceOSSignalOptionMsg = msg;
  }

  public String getCheckJNIFuncOptionMsg() {
		return checkJNIFuncOptionMsg;
  }

  public void setCheckJNIFuncOptionMsg(String msg) {
  	checkJNIFuncOptionMsg = msg;
  }

  public String getVerifyOptionMsg() {
  	return verifyOptionMsg;
  }

  public void setVerifyOptionMsg(String msg) {
  	verifyOptionMsg = msg;
  }

  public String getVerifyRemoteOptionMsg() {
  	return verifyRemoteOptionMsg;
  }

  public void setVerifyRemoteOptionMsg(String msg) {
  	verifyRemoteOptionMsg = msg;
  }

  public String getNoVerifyOptionMsg() {
  	return noVerifyOptionMsg;
  }

  public void setNoVerifyOptionMsg(String msg) {
  	noVerifyOptionMsg = msg;
  }
*/
  public String getCommonOptionMsg() {
  	return commonOptionMsg;
  }

  public void setCommonOptionMsg(String msg) {
  	commonOptionMsg = msg;
  }

  public String getMainClassMsg() {
  	return mainClassMsg;
  }

  public void setMainClassMsg(String msg) {
  	mainClassMsg = msg;
  }

  public String getInternalVMOptionMsg() {
  	return internalVMOptionMsg;
  }

  public void setInternalVMOptionMsg(String msg) {
  	internalVMOptionMsg = msg;
  }

  public String getExternalVMOptionMsg() {
  	return externalVMOptionMsg;
  }

  public void setExternalVMOptionMsg(String msg) {
  	externalVMOptionMsg = msg;
  }
}
