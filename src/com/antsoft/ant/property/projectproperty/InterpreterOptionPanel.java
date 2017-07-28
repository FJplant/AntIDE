/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/property/projectproperty/InterpreterOptionPanel.java,v 1.2 1999/07/16 09:13:32 lila Exp $
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
import com.antsoft.ant.property.JdkInfo;import com.antsoft.ant.util.*;

/**
 *  class InterpreterOptionPanel
 *
 *  @author Jinwoo Baek
 */
public class InterpreterOptionPanel extends JPanel {
	JCheckBox verboseCbx = null;
  JCheckBox debugCbx = null;
	JCheckBox noAsyncGCCbx = null;
  JCheckBox verboseGCCbx = null;
  JCheckBox noClassGCCbx = null;
//  JCheckBox csCbx = null;
  JCheckBox ssCbx = null;
  JCheckBox ossCbx = null;
  JCheckBox msCbx = null;
  JCheckBox mxCbx = null;
//  JCheckBox rsCbx = null;
//  JCheckBox checkJniCbx = null;
//  JCheckBox verifyCbx = null;
//  JCheckBox verifyRemoteCbx = null;
//  JCheckBox noVerifyCbx = null;
	JCheckBox commonCbx = null;
  JCheckBox mainClassCbx = null;
  JRadioButton internalVM = null;
  JRadioButton externalVM = null;
  ButtonGroup bg = new ButtonGroup();

  JTextField ssSize = null;
  JTextField ossSize = null;
  JTextField msSize = null;
  JTextField mxSize = null;
  JTextField commonOpt = null;
  JTextField mainClassName = null;

  InterpreterOptionModel model = new InterpreterOptionModel();

  /**
   *  Constructor
   */
	public InterpreterOptionPanel() {    setBorder(BorderList.etchedBorder5);
    
  	verboseCbx = new JCheckBox("Enable verbose output", false);
    debugCbx = new JCheckBox("Enable remote debugging", false);
    noAsyncGCCbx = new JCheckBox("Don't allow asynchronous garbage collection", false);
    verboseGCCbx = new JCheckBox("Print a message when garbage collection occurs", false);
    noClassGCCbx = new JCheckBox("Disable class garbage collection", false);
//    csCbx = new JCheckBox("Check if source is newer when loading classes", false);
    ssCbx = new JCheckBox("Set the maximum native stack size for any thread", false);
    ossCbx = new JCheckBox("Set the maximum Java stack size for any thread", false);
    msCbx = new JCheckBox("Set the initial java heap size", false);
    mxCbx = new JCheckBox("Set the maximum java heap size", false);
//    rsCbx = new JCheckBox("Reduce the use of OS signals", false);
//    checkJniCbx = new JCheckBox("Perform additional checks for JNI functions", false);
//    verifyCbx = new JCheckBox("Verify all classes when read in", false);
//    verifyRemoteCbx = new JCheckBox("Verify classes read in over the network(default)", false);
//    noVerifyCbx = new JCheckBox("Don't verify an class", false);
		commonCbx = new JCheckBox("Common Options", false);
    mainClassCbx = new JCheckBox("Set executable main class", false);
    internalVM = new JRadioButton("Use Internal VM", false);
    externalVM = new JRadioButton("Use External VM", true);
    bg.add(internalVM);
    bg.add(externalVM);

    ssSize = new JTextField(2);
    ossSize = new JTextField(2);
    msSize = new JTextField(2);
    mxSize = new JTextField(2);
    commonOpt = new JTextField(30);
    mainClassName = new JTextField(30);

/*
    verboseCbx.setPreferredSize(new Dimension(180, 22));
    debugCbx.setPreferredSize(new Dimension(180, 22));
    noAsyncGCCbx.setPreferredSize(new Dimension(180, 22));
    verboseGCCbx.setPreferredSize(new Dimension(180, 22));
    noClassGCCbx.setPreferredSize(new Dimension(180, 22));
    msCbx.setPreferredSize(new Dimension(180, 22));
    mxCbx.setPreferredSize(new Dimension(180, 22));
    rsCbx.setPreferredSize(new Dimension(180, 22));
    checkJniCbx.setPreferredSize(new Dimension(180, 22));
    verifyCbx.setPreferredSize(new Dimension(180, 22));
    verifyRemoteCbx.setPreferredSize(new Dimension(180, 22));
    noVerifyCbx.setPreferredSize(new Dimension(180, 22));
    mainClassCbx.setPreferredSize(new Dimension(180, 22));
    mainClassName.setPreferredSize(new Dimension(180, 22));
*/
    JPanel pl1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
    pl1.setPreferredSize(new Dimension(350, 23));
    pl1.add(verboseCbx);

    JPanel pl2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
    pl2.setPreferredSize(new Dimension(350, 23));
    pl2.add(debugCbx);

    JPanel pl3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
    pl3.setPreferredSize(new Dimension(350, 23));
    pl3.add(noAsyncGCCbx);

    JPanel pl4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
    pl4.setPreferredSize(new Dimension(350, 23));
    pl4.add(verboseGCCbx);

    JPanel pl5 = new JPanel(new FlowLayout(FlowLayout.LEFT));
    pl5.setPreferredSize(new Dimension(350, 23));
    pl5.add(noClassGCCbx);

		JPanel pl6 = new JPanel(new FlowLayout(FlowLayout.LEFT));
    pl6.setPreferredSize(new Dimension(350, 23));
    pl6.add(ssCbx);
    pl6.add(ssSize);

    JPanel pl7 = new JPanel(new FlowLayout(FlowLayout.LEFT));
    pl7.setPreferredSize(new Dimension(350, 23));
    pl7.add(ossCbx);
    pl7.add(ossSize);

    JPanel pl8 = new JPanel(new FlowLayout(FlowLayout.LEFT));
  	pl8.setPreferredSize(new Dimension(350, 23));
    pl8.add(msCbx);
    pl8.add(msSize);

    JPanel pl9 = new JPanel(new FlowLayout(FlowLayout.LEFT));
  	pl9.setPreferredSize(new Dimension(350, 23));
    pl9.add(mxCbx);
    pl9.add(mxSize);
/*
    JPanel pl10 = new JPanel(new FlowLayout(FlowLayout.LEFT));
    pl10.setPreferredSize(new Dimension(350, 23));
    pl10.add(rsCbx);

    JPanel pl11 = new JPanel(new FlowLayout(FlowLayout.LEFT));
    pl11.setPreferredSize(new Dimension(350, 23));
    pl11.add(checkJniCbx);

    JPanel pl12 = new JPanel(new FlowLayout(FlowLayout.LEFT));
    pl12.setPreferredSize(new Dimension(350, 23));
    pl12.add(verifyCbx);

    JPanel pl13 = new JPanel(new FlowLayout(FlowLayout.LEFT));
    pl13.setPreferredSize(new Dimension(350, 23));
    pl13.add(verifyRemoteCbx);

    JPanel pl14 = new JPanel(new FlowLayout(FlowLayout.LEFT));
    pl14.setPreferredSize(new Dimension(350, 23));
    pl14.add(noVerifyCbx);
*/
		JPanel pl15 = new JPanel(new FlowLayout(FlowLayout.LEFT));
    pl15.setPreferredSize(new Dimension(350, 23));
    pl15.add(commonCbx);

    JPanel pl16 = new JPanel(new FlowLayout(FlowLayout.LEFT));
    pl16.setPreferredSize(new Dimension(350, 23));
    pl16.add(commonOpt);

    JPanel pl17 = new JPanel(new FlowLayout(FlowLayout.LEFT));
    pl17.setPreferredSize(new Dimension(350, 23));
    pl17.add(mainClassCbx);

    JPanel pl18 = new JPanel(new FlowLayout(FlowLayout.LEFT));
    pl18.setPreferredSize(new Dimension(350, 23));
    pl18.add(mainClassName);

    JPanel pl19 = new JPanel(new GridLayout(1, 2));
    pl19.setPreferredSize(new Dimension(350, 23));
    pl19.add(internalVM);
    pl19.add(externalVM);

//    Box box = Box.createVerticalBox();
//    JPanel box = new JPanel(new GridLayout(0, 1));
		setLayout(new GridLayout(0, 1));
/*
    box.add(pl1);
    box.add(pl2);
    box.add(pl3);
    box.add(pl4);
    box.add(pl5);
    box.add(pl6);
    box.add(pl7);
    box.add(pl8);
    box.add(pl9);
    box.add(pl10);
    box.add(pl11);
    box.add(pl12);
    box.add(pl13);
    box.add(pl14);
    box.add(pl15);
    box.add(pl16);
*/
    add(pl1);
    add(pl2);
    add(pl3);
    add(pl4);
    add(pl5);
    add(pl6);
    add(pl7);
    add(pl8);
    add(pl9);
//    add(pl10);
//    add(pl11);
//    add(pl12);
//    add(pl13);
//    add(pl14);
    add(pl15);
    add(pl16);
    add(pl17);
    add(pl18);
    add(pl19);
/*
		box.add(verboseCbx);
    box.add(debugCbx);
    box.add(noAsyncGCCbx);
    box.add(verboseGCCbx);
    box.add(noClassGCCbx);
    box.add(csCbx);
    box.add(pa1);
    box.add(pa2);
    box.add(pl2);
    box.add(pl3);
    box.add(rsCbx);
    box.add(checkJniCbx);
    box.add(verifyCbx);
    box.add(verifyRemoteCbx);
    box.add(noVerifyCbx);
*/
//    add(box);
  	JdkInfo info = ProjectManager.getCurrentProject().getPathModel().getCurrentJdkInfo();
    if (info != null) {
	    String ver = info.getVersion();
      // 1.1 version
  		if ((ver != null) && (ver.indexOf("1.1") != -1)) {
    		noAsyncGCCbx.setEnabled(true);
        verboseGCCbx.setEnabled(true);
        ssCbx.setEnabled(true);
        ossCbx.setEnabled(true);
	    }
  	  else {
    		noAsyncGCCbx.setSelected(false);
        verboseGCCbx.setSelected(false);
        ssCbx.setSelected(false);
        ossCbx.setSelected(false);
    		noAsyncGCCbx.setEnabled(false);
        verboseGCCbx.setEnabled(false);
        ssCbx.setEnabled(false);
        ossCbx.setEnabled(false);
      }
    }
  }

  public void setModel(InterpreterOptionModel model) {
  	this.model = model;
    if (model != null) {
    	verboseCbx.setText(model.getVerboseOptionMsg());
      verboseCbx.setSelected(model.getVerboseMode());
      debugCbx.setText(model.getDebugOptionMsg());
      debugCbx.setSelected(model.getDebugMode());
      noAsyncGCCbx.setText(model.getNoAsyncGCOptionMsg());
      noAsyncGCCbx.setSelected(model.getNoAsyncGCMode());
      verboseGCCbx.setText(model.getVerboseGCOptionMsg());
      verboseGCCbx.setSelected(model.getVerboseGCMode());
      noClassGCCbx.setText(model.getNoClassGCOptionMsg());
      noClassGCCbx.setSelected(model.getNoClassGCMode());
      ssCbx.setText(model.getMaxNatStackOptionMsg());
      ssCbx.setSelected(model.getMaxNatStackMode());
      ssSize.setText(model.getMaxNatStackSize());
      ossCbx.setText(model.getMaxJavaStackOptionMsg());
      ossCbx.setSelected(model.getMaxJavaStackMode());
      ossSize.setText(model.getMaxJavaStackSize());
      msCbx.setText(model.getInitHeapOptionMsg());
      msCbx.setSelected(model.getInitHeapMode());
      msSize.setText(model.getInitHeapSize());
      mxCbx.setText(model.getMaxHeapOptionMsg());
      mxCbx.setSelected(model.getMaxHeapMode());
      mxSize.setText(model.getMaxHeapSize());
      commonCbx.setText(model.getCommonOptionMsg());
      commonCbx.setSelected(model.getCommonMode());
      commonOpt.setText(model.getCommonOption());
      mainClassCbx.setText(model.getMainClassMsg());
      mainClassCbx.setSelected(model.getMainClassMode());
      mainClassName.setText(model.getMainClassName());
      internalVM.setText(model.getInternalVMOptionMsg());
      externalVM.setText(model.getExternalVMOptionMsg());
      internalVM.setSelected(model.getInternalVMMode());
    }
    
  	JdkInfo info = ProjectManager.getCurrentProject().getPathModel().getCurrentJdkInfo();
    if (info != null) {
	    String ver = info.getVersion();
      // 1.1 version
  		if ((ver != null) && (ver.indexOf("1.1") != -1)) {
    		noAsyncGCCbx.setEnabled(true);
        verboseGCCbx.setEnabled(true);
        ssCbx.setEnabled(true);
        ossCbx.setEnabled(true);
	    }
  	  else {
    		noAsyncGCCbx.setSelected(false);
        verboseGCCbx.setSelected(false);
        ssCbx.setSelected(false);
        ossCbx.setSelected(false);
    		noAsyncGCCbx.setEnabled(false);
        verboseGCCbx.setEnabled(false);
        ssCbx.setEnabled(false);
        ossCbx.setEnabled(false);
      }
    }
  }

  public InterpreterOptionModel getModel() {
  	if (model != null) {
    	model.setVerboseMode(verboseCbx.isSelected());
      model.setDebugMode(debugCbx.isSelected());
      model.setNoAsyncGCMode(noAsyncGCCbx.isSelected());
      model.setVerboseGCMode(verboseGCCbx.isSelected());
      model.setNoClassGCMode(noClassGCCbx.isSelected());
      model.setMaxNatStackMode(ssCbx.isSelected());
      model.setMaxNatStackSize(ssSize.getText());
      model.setMaxJavaStackMode(ossCbx.isSelected());
      model.setMaxJavaStackSize(ossSize.getText());
      model.setInitHeapMode(msCbx.isSelected());
      model.setInitHeapSize(msSize.getText());
      model.setMaxHeapMode(mxCbx.isSelected());
      model.setMaxHeapSize(mxSize.getText());
      model.setCommonMode(commonCbx.isSelected());
      model.setCommonOption(commonOpt.getText());
      model.setMainClassMode(mainClassCbx.isSelected());
      model.setMainClassName(mainClassName.getText());
      model.setInternalVMMode(internalVM.isSelected());
    }
  	return model;
  }
}