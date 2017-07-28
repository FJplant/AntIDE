/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/property/projectproperty/ProjectPropertyDlg.java,v 1.10 1999/08/24 06:51:59 remember Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.10 $
 */
package com.antsoft.ant.property.projectproperty;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;

import com.antsoft.ant.property.*;
import com.antsoft.ant.codecontext.*;
import com.antsoft.ant.pool.librarypool.*;
import com.antsoft.ant.manager.projectmanager.*;
import com.antsoft.ant.main.MainFrame;
import com.antsoft.ant.util.WindowDisposer;

/**
 * project property dialog
 *
 * @author kim sang kyun
 */
public class ProjectPropertyDlg extends JDialog{
  private ProjectPathPanel pathPanel;
  private JButton okBtn, cancelBtn, helpBtn;
  private JdkInfoContainer jdkInfoContainer;
  private LibInfoContainer libraryInfoContainer;
  private PathPropertyInfo pathPropertyInfo;
  private JFrame parent;
  private DefaultPathModel pathModel;
  private boolean isOK = false;

  // by Jinwoo
  private JTabbedPane mainPane = null;
  private ProjectGeneralPanel generalPanel = null;
  private CompilerOptionModel compilerModel= null;
  private CompilerOptionPanel compilerPanel = null;
  private InterpreterOptionModel interpreterModel = null;
  private InterpreterOptionPanel interpreterPanel = null;


  public ProjectPropertyDlg(JFrame frame, String title, boolean modal) {
    super(frame, title, modal);
    this.parent = frame;

		// register window event handler
		addWindowListener(WindowDisposer.getDisposer());
		addKeyListener(WindowDisposer.getDisposer());

    ActionListener al = new ActionHandler();

    generalPanel = new ProjectGeneralPanel();

    pathModel = new DefaultPathModel();
    pathPanel = new ProjectPathPanel(parent);
    pathPanel.setModel(pathModel);

    // -------------------------------------- added
    mainPane = new JTabbedPane();
		compilerModel = new CompilerOptionModel();
    compilerPanel = new CompilerOptionPanel();
    compilerPanel.setModel(compilerModel);

  	interpreterModel = new InterpreterOptionModel();
    interpreterPanel = new InterpreterOptionPanel();
    interpreterPanel.setModel(interpreterModel);

    //button panel
    okBtn = new JButton("OK");
    okBtn.setActionCommand("OK");
    okBtn.addActionListener(al);

    cancelBtn = new JButton("Cancel");
    cancelBtn.setActionCommand("CANCEL");
    cancelBtn.addActionListener(al);

    helpBtn = new JButton("Help");

    JPanel buttonP = new JPanel();
    buttonP.add(okBtn);
    buttonP.add(cancelBtn);
    buttonP.add(helpBtn);

    mainPane.addTab("General", generalPanel);
    mainPane.addTab("Path", pathPanel);
    mainPane.addTab("Compiler", compilerPanel);
    mainPane.addTab("Interpreter", interpreterPanel);
    //center panel
    JPanel centerP = new JPanel(new BorderLayout());
    centerP.add(mainPane, BorderLayout.CENTER);
    centerP.add(buttonP, BorderLayout.SOUTH);

    this.getContentPane().add(centerP, BorderLayout.CENTER);
    this.getContentPane().add(new JPanel(), BorderLayout.NORTH);
    this.getContentPane().add(new JPanel(), BorderLayout.SOUTH);
    this.getContentPane().add(new JPanel(), BorderLayout.EAST);
    this.getContentPane().add(new JPanel(), BorderLayout.WEST);

    setSize(420, 480);
    this.setResizable(false);

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension dlgSize = getSize();
    if (dlgSize.height > screenSize.height)
      dlgSize.height = screenSize.height;
    if (dlgSize.width > screenSize.width)
      dlgSize.width = screenSize.width;
    setLocation((screenSize.width - dlgSize.width) / 2,
                (screenSize.height - dlgSize.height) / 2);

  }




  /**
   * set DefaultPathModel
   *
   * @param newModel DefaultPathModel
   */
  public void setPathModel(DefaultPathModel newModel){
    pathModel = newModel;
    pathPanel.setModel(newModel);
  }

  public DefaultPathModel getPathModel(){
    return pathModel;
  }

  public void setCompilerModel(CompilerOptionModel model) {
  	this.compilerModel = model;
    compilerPanel.setModel(compilerModel);
  }

  public CompilerOptionModel getCompilerModel() {
  	return compilerModel;
  }

  public void setInterpreterModel(InterpreterOptionModel model) {
  	this.interpreterModel = model;
    interpreterPanel.setModel(interpreterModel);
  }

  public boolean isOK(){
    return this.isOK;
  }

  public void okPressed(){
    if(pathModel.getSourceRoot() == null){
       JOptionPane.showMessageDialog(MainFrame.mainFrame, "Source Root Direcory must be specified !!", "Error", JOptionPane.ERROR_MESSAGE, null );
       return;
    }

    if(pathModel.getOutputRoot() == null){
       JOptionPane.showMessageDialog(MainFrame.mainFrame, "Output Root Direcory must be specified !!", "Error", JOptionPane.ERROR_MESSAGE, null );
       return;
    }

    isOK = true;

    compilerModel = compilerPanel.getModel();
    interpreterModel = interpreterPanel.getModel();
    pathModel = pathPanel.getModel();

    Vector datas = pathModel.getLibraryPoolDatas();
    pathModel.updateClassPath();

    Project project = ProjectManager.getCurrentProject();
    project.setCompilerModel(compilerModel);
    project.setInterpreterModel(interpreterModel);
    project.setPathModel(pathModel);

    project.updateClassLoader();
    String currProjName = project.getProjectName();
    LibraryPool.removeAllProjectLibs(currProjName);

    for(int i = 0; i < datas.size(); i++){
      LibraryPool.addProjectLibraryInfo(currProjName, (LibraryInfo)datas.elementAt(i));
    }

    dispose();
    Runnable r = new DelayedFocus(parent);
    SwingUtilities.invokeLater(r);

  }

  public void cancelSelected(){
    isOK = false;
    dispose();

    Runnable r = new DelayedFocus(parent);
    SwingUtilities.invokeLater(r);

  }

  class DelayedFocus implements Runnable {
    Component comp;
    public DelayedFocus(Component comp) {
      this.comp = comp;
    }

    public void run() {
      comp.requestFocus();
    }
  }

  class ActionHandler implements ActionListener{
    public void actionPerformed(ActionEvent e){
      String cmd = e.getActionCommand();

      if(cmd.equals("OK")){
        okPressed();
      }

      else if(cmd.equals("CANCEL")){
        cancelSelected();
      }
    }
  }
}
