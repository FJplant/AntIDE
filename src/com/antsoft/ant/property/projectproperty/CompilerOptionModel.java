/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/property/projectproperty/CompilerOptionModel.java,v 1.3 1999/07/22 03:39:32 multipia Exp $
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

import com.antsoft.ant.manager.projectmanager.*;
import com.antsoft.ant.property.*;
import com.antsoft.ant.util.Constants;

/**
 *  class CompilerOptionModel
 *
 *  @author Jinwoo Baek
 */
public class CompilerOptionModel implements Serializable {
	private boolean isShowDebugMsg = false;
  private boolean isOptimizeCode = false;
  private boolean isNoWarningMsg = false;
  private boolean isVerboseMsg = false;
  private boolean isDeprecation = false;

  private boolean isDepend = false;

  // options for 1.1.x
//  private boolean isDebug = false;
  private boolean isNoWrite = false;

  // options for 1.2

  private boolean isCommon = false;

  private boolean isInternalVM = false;

  private String debugOption = "-g";
  private String debugOptionMsg = "Generate Debugging Info";
  private String optimizeOption = "-O";
  private String optimizeOptionMsg = "Optimize Code";
  private String warningOption = "-nowarn";
  private String warningOptionMsg = "Generate No Warning";
  private String verboseOption = "-verbose";
  private String verboseOptionMsg = "Output Messages about what the compiler is doing";
  private String depreOption = "-deprecation";
  private String depreOptionMsg = "Output source locations where deprecated APIs are used";

  private String dependOption11 = "-depend";
  private String dependOption12 = "-Xdepend";
  private String dependOptionMsg = "Recursively search for more recent source files to recompile";
//  private String debugOption11 = "-debug";
//  private String debugOption11Msg = "";
  private String noWriteOption11 = "-nowrite";
  private String noWriteOptionMsg = "No write";
  private String commonOption = "";
  private String commonOptionMsg = "Common Option";
  private String internalVMOptionMsg = "Use Internal VM";
  private String externalVMOptionMsg = "Use External VM";

  public CompilerOptionModel() {
  }

  /**
   *  프로젝트 파일에 저장되는 형태로 나타낸다.
   */
  public String toString() {
  	StringBuffer out = new StringBuffer();

    // 옵션 값들
    out.append("Compile_ShowDebugMsg=" + isShowDebugMsg + Constants.lineSeparator);
    out.append("Compile_Optimize=" + isOptimizeCode + Constants.lineSeparator);
    out.append("Compile_Warning=" + isNoWarningMsg + Constants.lineSeparator);
    out.append("Compile_Verbose=" + isVerboseMsg + Constants.lineSeparator);
    out.append("Compile_Deprecation=" + isDeprecation + Constants.lineSeparator);
    out.append("Compile_Depend=" + isDepend + Constants.lineSeparator);
//    out.append("Compile_Debug=" + isDebug + Constants.lineSeparator);
    out.append("Compile_NoWrite=" + isNoWrite + Constants.lineSeparator);
    out.append("Compile_Common=" + isCommon + Constants.lineSeparator);
    out.append("Compile_CommonOption=" + commonOption + Constants.lineSeparator);
    out.append("Compile_internalVM=" + isInternalVM + Constants.lineSeparator);
/*
    // 각종 문자열 값들
    out.append("Compile_gOption=" + debugOption + Constants.lineSeparator);
    out.append("Compile_OptimizeOption=" + optimizeOption + Constants.lineSeparator);
    out.append("Compile_WarningOption=" + warningOption + Constants.lineSeparator);
    out.append("Compile_VerboseOption=" + verboseOption + Constants.lineSeparator);
    out.append("Compile_DepreOption=" + depreOption + Constants.lineSeparator);
    out.append("Compile_DependOption11=" + dependOption11 + Constants.lineSeparator);
    out.append("Compile_DependOption12=" + dependOption12 + Constants.lineSeparator);
//    out.append("Compile_DebugOption=" + debugOption11 + Constants.lineSeparator);
    out.append("Compile_NoWriteOption11=" + noWriteOption11 + Constants.lineSeparator);

    out.append("Compile_gOptionMsg=" + debugOptionMsg + Constants.lineSeparator);
    out.append("Compile_OptimizeOptionMsg=" + optimizeOptionMsg + Constants.lineSeparator);
    out.append("Compile_WarningOptionMsg=" + warningOptionMsg + Constants.lineSeparator);
    out.append("Compile_VerboseOptionMsg=" + verboseOptionMsg + Constants.lineSeparator);
    out.append("Compile_DepreOptionMsg=" + depreOptionMsg + Constants.lineSeparator);
    out.append("Compile_DependOptionMsg=" + dependOptionMsg + Constants.lineSeparator);
//    out.append("Compile_DebugOptionMsg=" + debugOption11Msg + Constants.lineSeparator);
    out.append("Compile_NoWriteOptionMsg=" + noWriteOptionMsg + Constants.lineSeparator);
    out.append("Compile_CommonOptionMsg=" + commonOptionMsg + Constants.lineSeparator);
    out.append("Compile_InternalVMOptionMsg" + internalVMOptionMsg + Constants.lineSeparator);
    out.append("Compile_ExternalVMOptionMsg" + externalVMOptionMsg + Constants.lineSeparator);
*/
    return out.toString();
  }

  public boolean getDebugMode() {
  	return isShowDebugMsg;
  }

  public void setDebugMode(boolean isShowDebugMsg) {
  	this.isShowDebugMsg = isShowDebugMsg;
  }

  public boolean getOptimizing() {
		return isOptimizeCode;
  }

  public void setOptimizing(boolean isOptimizeCode) {
  	this.isOptimizeCode = isOptimizeCode;
  }

  public boolean getWarning() {
  	return isNoWarningMsg;
  }

  public void setWarning(boolean isNoWarningMsg) {
		this.isNoWarningMsg = isNoWarningMsg;
  }

  public boolean getVerboseMsg() {
  	return isVerboseMsg;
  }

  public void setVerboseMsg(boolean isVerboseMsg) {
  	this.isVerboseMsg = isVerboseMsg;
  }

  public boolean getDeprecation() {
  	return isDeprecation;
  }

  public void setDeprecation(boolean isDeprecation) {
  	this.isDeprecation = isDeprecation;
  }

  public boolean getDepend() {
  	return isDepend;
  }

  public void setDepend(boolean b) {
  	isDepend = b;
  }

  public boolean getNoWrite() {
  	return isNoWrite;
  }

  public void setNoWrite(boolean b) {
  	isNoWrite = b;
  }

  public boolean getCommon() {
  	return isCommon;
  }

  public void setCommon(boolean b) {
  	isCommon = b;
  }

  public boolean getInternalVMMode() {
  	return isInternalVM;
  }

  public void setInternalVMMode(boolean b) {
  	isInternalVM = b;
  }

  ///////////////////////////////
  public String getDebugOption() {
  	return debugOption;
  }

  public String getOptimizeOption() {
  	return optimizeOption;
  }

  public String getWarningOption() {
  	return warningOption;
  }

  public String getVerboseOption() {
  	return verboseOption;
  }

  public String getDepreOption() {
  	return depreOption;
  }

  public String getDependOption() {
  	JdkInfo info = ProjectManager.getCurrentProject().getPathModel().getCurrentJdkInfo();
    String ver = info.getVersion();
  	if (ver.indexOf("1.2") != -1) return dependOption12;
    else return dependOption11;
  }

  public String getNoWriteOption() {
  	return noWriteOption11;
	}

  public String getCommonOption() {
  	return commonOption;
  }

  public void setDebugOption(String optStr) {
  	this.debugOption = optStr;
  }

  public void setOptimizeOption(String optStr) {
  	this.optimizeOption = optStr;
  }

  public void setWarningOption(String optStr) {
  	this.warningOption = optStr;
  }

  public void setVerboseOption(String optStr) {
  	this.verboseOption = optStr;
  }

  public void setDepreOption(String optStr) {
  	this.depreOption = optStr;
  }

  public void setDependOption(String op) {
  	JdkInfo info = ProjectManager.getCurrentProject().getPathModel().getCurrentJdkInfo();
    String ver = info.getVersion();
  	if (ver.indexOf("1.2") != -1) dependOption12 = op;
    else dependOption11 = op;
  }

  public void setNoWriteOption(String op) {
  	noWriteOption11 = op;
	}

  public void setCommonOption(String op) {
  	commonOption = op;
  }

  ///////////////////////////////
  public String getDebugOptionMsg() {
  	return debugOptionMsg;
  }

  public String getOptimizeOptionMsg() {
  	return optimizeOptionMsg;
  }

  public String getWarningOptionMsg() {
  	return warningOptionMsg;
  }

  public String getVerboseOptionMsg() {
  	return verboseOptionMsg;
  }

  public String getDepreOptionMsg() {
  	return depreOptionMsg;
  }

  public String getDependOptionMsg() {
  	return dependOptionMsg;
  }

  public String getNoWriteOptionMsg() {
  	return noWriteOptionMsg;
  }

  public String getCommonOptionMsg() {
  	return commonOptionMsg;
  }

  public String getInternalVMOptionMsg() {
  	return internalVMOptionMsg;
  }

  public String getExternalVMOptionMsg() {
  	return externalVMOptionMsg;
  }

  public void setDebugOptionMsg(String optStr) {
  	this.debugOptionMsg = optStr;
  }

  public void setOptimizeOptionMsg(String optStr) {
  	this.optimizeOptionMsg = optStr;
  }

  public void setWarningOptionMsg(String optStr) {
  	this.warningOptionMsg = optStr;
  }

  public void setVerboseOptionMsg(String optStr) {
  	this.verboseOptionMsg = optStr;
  }

  public void setDepreOptionMsg(String optStr) {
  	this.depreOptionMsg = optStr;
  }

  public void setDependOptionMsg(String msg) {
  	dependOptionMsg = msg;
  }

  public void setNoWriteOptionMsg(String msg) {
  	noWriteOptionMsg = msg;
  }

  public void setCommonOptionMsg(String msg) {
  	commonOptionMsg = msg;
  }

  public void setInternalVMOptionMsg(String msg) {
  	internalVMOptionMsg = msg;
  }

  public void setExternalVMOptionMsg(String msg) {
  	externalVMOptionMsg = msg;
  }
}
