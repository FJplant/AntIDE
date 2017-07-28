/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/property/defaultproperty/HelpPropertyDlg.java,v 1.4 1999/08/19 04:20:45 multipia Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * Author: Kim, Sang Kyun
 * $Revision: 1.4 $
 */
package com.antsoft.ant.property.defaultproperty;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;

// by kahn
import com.antsoft.ant.main.MainFrame;

import com.antsoft.ant.property.*;
import com.antsoft.ant.codecontext.*;
import com.antsoft.ant.pool.librarypool.*;
import com.antsoft.ant.manager.projectmanager.*;
import com.antsoft.ant.main.Main;
import com.antsoft.ant.util.WindowDisposer;

/**
 * default property dialog
 *
 * @author kim sang kyun
 */
public class HelpPropertyDlg extends JDialog{

  private JButton okBtn, cancelBtn, helpBtn;
  private WebPanel webP;
  private JFrame parent;
  private boolean isOK = false;

  public HelpPropertyDlg(JFrame frame, String title, boolean modal) {
    super(frame, title, modal);
    this.parent = frame;

		// register window event handler
		addWindowListener(WindowDisposer.getDisposer());
		addKeyListener(WindowDisposer.getDisposer()); 		

    ActionListener al = new ActionHandler();

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

    webP = new WebPanel(parent);

    //center panel
    JPanel centerP = new JPanel(new BorderLayout());
    centerP.add(webP, BorderLayout.CENTER);
    centerP.add(buttonP, BorderLayout.SOUTH);

    this.getContentPane().add(centerP, BorderLayout.CENTER);
    this.getContentPane().add(new JPanel(), BorderLayout.NORTH);
    this.getContentPane().add(new JPanel(), BorderLayout.SOUTH);
    this.getContentPane().add(new JPanel(), BorderLayout.EAST);
    this.getContentPane().add(new JPanel(), BorderLayout.WEST);

    setSize(420, 450);

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension dlgSize = getSize();
    if (dlgSize.height > screenSize.height) dlgSize.height = screenSize.height;
    if (dlgSize.width > screenSize.width) dlgSize.width = screenSize.width;
    setLocation((screenSize.width - dlgSize.width) / 2, (screenSize.height - dlgSize.height) / 2);
    setResizable(false);

    setInitProperty();
  }

  private void setInitProperty(){

    if(Main.property.getAllWebBrowserPaths() != null){
      webP.setAllBrowserPaths(Main.property.getAllWebBrowserPaths());
    }

    if(Main.property.getWebBrowserPathToRestore() != null){
      webP.setSelectedBrowserPath(Main.property.getWebBrowserPathToRestore());
    }

    webP.setInternal(Main.property.isInternalHelpViewerUse());
  }


  public boolean isOK(){
    return this.isOK;

  }

  public void dispose(){

    int count = getComponentCount();
    for(int i=0; i<count; i++){
      Component c = getComponent(i);
      this.remove(c);
      c = null;
    }
    super.dispose();
    System.gc();
  }

  public void okPressed(){
    isOK = true;

    //web browser path

    Main.property.setAllWebBrowserPaths(webP.getAllBrowserPaths());
    Main.property.setSelectedWebBrowserPath(webP.getSelectedBrowserPath());
    Main.property.setSelectedWebBrowserPathToSave(webP.getSelectedBrowserPathToSave());
    Main.property.setInternalHelpViewerUse(webP.isInternalUse());

    //save to file
    IdePropertyManager.saveProperty(Main.property);

    dispose();
  }

  class ActionHandler implements ActionListener{
    public void actionPerformed(ActionEvent e){
      String cmd = e.getActionCommand();

      if(cmd.equals("OK")){
        okPressed();
      }

      else if(cmd.equals("CANCEL")){
        isOK = false;
        dispose();
      }
    }
  }
}
