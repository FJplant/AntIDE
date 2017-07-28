/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/property/projectproperty/CompilerOptionPanel.java,v 1.2 1999/07/16 09:13:32 lila Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.2 $
 */
package com.antsoft.ant.property.projectproperty;

import java.awt.*;
import javax.swing.*;

import com.antsoft.ant.manager.projectmanager.ProjectManager;
import com.antsoft.ant.property.JdkInfo;
import com.antsoft.ant.property.DefaultPathModel;import com.antsoft.ant.util.*;


/**
 * compile option을 정하는 panel
 *
 * @author kim sang kyun
 */
public class CompilerOptionPanel extends JPanel {
	JCheckBox debugCbx = null;
  JCheckBox optimizeCbx = null;
  JCheckBox warningCbx = null;
  JCheckBox verboseCbx = null;
  JCheckBox depreCbx = null;

  JCheckBox dependCbx = null;
  JCheckBox noWriteCbx = null;
  JCheckBox commonCbx = null;
  JTextField cmnOpt = null;

  CompilerOptionModel model = null;
  JRadioButton internalVM = null;
  JRadioButton externalVM = null;
  ButtonGroup bg = new ButtonGroup();

  /**
   *  Constructor
   */
  public CompilerOptionPanel() {    setBorder(BorderList.etchedBorder5);
    
  	debugCbx = new JCheckBox("Show Debug Messages", false);
    optimizeCbx = new JCheckBox("Optimize Code", false);
    warningCbx = new JCheckBox("Don't show warning messages", false);
    verboseCbx = new JCheckBox("Verbose output messages", false);
    depreCbx = new JCheckBox("Deprecation", false);

    dependCbx = new JCheckBox("Search for source files to recompile", false);
    noWriteCbx = new JCheckBox("No Write", false);
    commonCbx = new JCheckBox("Common Options", false);

    internalVM = new JRadioButton("Use Internal VM", false);
    externalVM = new JRadioButton("Use External VM", true);

    bg.add(internalVM);
    bg.add(externalVM);

    cmnOpt = new JTextField(30);

    commonCbx.setPreferredSize(new Dimension(350, 23));

    JPanel pl = new JPanel(new FlowLayout(FlowLayout.LEFT));
    pl.setPreferredSize(new Dimension(350, 23));
    pl.add(cmnOpt);

    JPanel pl1 = new JPanel(new GridLayout(1, 2));
		pl1.setPreferredSize(new Dimension(350, 23));
    pl1.add(internalVM);
    pl1.add(externalVM);

    setLayout(new FlowLayout(FlowLayout.LEFT));
    add(debugCbx);
    add(optimizeCbx);
    add(warningCbx);
    add(verboseCbx);
    add(depreCbx);
    add(dependCbx);
    add(noWriteCbx);
    add(commonCbx);
    add(pl);
    add(pl1);
/*
    Box box = Box.createVerticalBox();
    box.add(debugCbx);
    box.add(optimizeCbx);
    box.add(warningCbx);
    box.add(verboseCbx);
    box.add(depreCbx);

    box.add(dependCbx);
    box.add(noWriteCbx);
    box.add(commonCbx);
    box.add(pl);
    box.add(pl1);
    add(box);
*/
    DefaultPathModel pm = ProjectManager.getCurrentProject().getPathModel();
    if(pm != null){
    	JdkInfo info = pm.getCurrentJdkInfo();

      if (info != null) {
        String ver = info.getVersion();
        if ((ver != null) && ver.indexOf("1.1") != -1) {
          noWriteCbx.setEnabled(true);
        }
        else {
          noWriteCbx.setSelected(false);
          noWriteCbx.setEnabled(false);
        }
      }
    }  
  }

  /**
   *  내부 데이터를 설정한다.
   */
  public void setModel(CompilerOptionModel model) {
  	this.model = model;
    debugCbx.setText(model.getDebugOptionMsg());
    debugCbx.setSelected(model.getDebugMode());
    optimizeCbx.setText(model.getOptimizeOptionMsg());
    optimizeCbx.setSelected(model.getOptimizing());
    warningCbx.setText(model.getWarningOptionMsg());
    warningCbx.setSelected(model.getWarning());
    verboseCbx.setText(model.getVerboseOptionMsg());
    verboseCbx.setSelected(model.getVerboseMsg());
    depreCbx.setText(model.getDepreOptionMsg());
    depreCbx.setSelected(model.getDeprecation());

    dependCbx.setText(model.getDependOptionMsg());
    dependCbx.setSelected(model.getDepend());
    noWriteCbx.setText(model.getNoWriteOptionMsg());
    noWriteCbx.setSelected(model.getNoWrite());
    commonCbx.setText(model.getCommonOptionMsg());
    commonCbx.setSelected(model.getCommon());
    cmnOpt.setText(model.getCommonOption());
    internalVM.setText(model.getInternalVMOptionMsg());
    externalVM.setText(model.getExternalVMOptionMsg());
    internalVM.setSelected(model.getInternalVMMode());
  	JdkInfo info = ProjectManager.getCurrentProject().getPathModel().getCurrentJdkInfo();
    if (info != null) {
	    String ver = info.getVersion();
  		if ((ver != null) && (ver.indexOf("1.1") != -1)) {
    		noWriteCbx.setEnabled(true);
	    }
  	  else
    		noWriteCbx.setEnabled(false);
    }
  }

  public CompilerOptionModel getModel() {
    if (model != null) {
    	model.setDebugMode(debugCbx.isSelected());
      model.setOptimizing(optimizeCbx.isSelected());
      model.setWarning(warningCbx.isSelected());
      model.setVerboseMsg(verboseCbx.isSelected());
      model.setDeprecation(depreCbx.isSelected());
      model.setDepend(dependCbx.isSelected());
      model.setNoWrite(noWriteCbx.isSelected());
      model.setCommon(commonCbx.isSelected());
      model.setCommonOption(cmnOpt.getText());
      model.setInternalVMMode(internalVM.isSelected());
    }
    return model;
  }

  public boolean isShowingDebug() {
  	return debugCbx.isSelected();
  }

  public boolean isOptimizing() {
  	return optimizeCbx.isSelected();
  }

  public boolean isNoWarning() {
  	return warningCbx.isSelected();
  }

  public boolean isVerboseMsg() {
  	return verboseCbx.isSelected();
  }

  public boolean isDeprecation() {
  	return depreCbx.isSelected();
  }

  public boolean isDepend() {
  	return dependCbx.isSelected();
  }

  public boolean isNoWrite() {
  	return noWriteCbx.isSelected();
  }

  public boolean isCommon() {
  	return commonCbx.isSelected();
  }
}

