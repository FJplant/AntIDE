
/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/runner/ServletPropertyDlg.java,v 1.5 1999/08/19 04:28:19 multipia Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * Author:       Lee Chul Mok
 *
 * $History: ServletPropertyDlg.java $
 * 
 * *****************  Version 6  *****************
 * User: Itree        Date: 99-06-01   Time: 5:36p
 * Updated in $/AntIDE/source/ant/runner
 * 
 * *****************  Version 5  *****************
 * User: Itree        Date: 99-05-27   Time: 8:56a
 * Updated in $/AntIDE/source/ant/runner
 *
 */

package com.antsoft.ant.runner;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;

//import com.antsoft.ant.manager.projectmanager.IdeProperty;
import com.antsoft.ant.property.IdePropertyManager;
import com.antsoft.ant.main.Main;
import com.antsoft.ant.util.WindowDisposer;

/**
 * Servlet Property Dialog
 *
 * @author Lee Chul Mok
 */
public class ServletPropertyDlg extends JDialog {
  private ServletPathPanel pathTab;
  private ServletOptionPanel optionTab;
//  private IdeProperty property;

  private JTabbedPane tabbedPane;
  private JFrame parent;
  private JButton okBtn, cancelBtn, helpBtn;
  private boolean isOK = false;
  private boolean isHelp = false;

  public ServletPropertyDlg(JFrame frame, String title, boolean modal) {
    super(frame, "Servlet Option Property", modal);
    this.parent = frame;

		// register window event handler
		addWindowListener(WindowDisposer.getDisposer());
		addKeyListener(WindowDisposer.getDisposer()); 		

    ActionListener al = new ActionHandler();

    tabbedPane = new JTabbedPane();

    pathTab = new ServletPathPanel(parent);
    tabbedPane.addTab("Path", pathTab);
    tabbedPane.setSelectedIndex(0);

    optionTab = new ServletOptionPanel(parent);
    tabbedPane.addTab("Option", optionTab);

    okBtn = new JButton("OK");
    okBtn.setActionCommand("OK");
    okBtn.addActionListener(al);

    cancelBtn = new JButton("Cancel");
    cancelBtn.setActionCommand("CANCEL");
    cancelBtn.addActionListener(al);

    helpBtn = new JButton("Help");
    helpBtn.setActionCommand("Help");
    // Kahn에게 물어보고 결정해야할 사항.
    //MainFrame.helpBroker.enableHelp(helpBtn, "servlet.option", null);

    //button Panel
    JPanel buttonP = new JPanel();
    buttonP.add(okBtn);
    buttonP.add(cancelBtn);
    buttonP.add(helpBtn);

    //center Panel
    JPanel centerP = new JPanel(new BorderLayout());
    centerP.add(tabbedPane, BorderLayout.CENTER);
    centerP.add(buttonP, BorderLayout.SOUTH);

    this.getContentPane().add(centerP, BorderLayout.CENTER);
    this.getContentPane().add(new JPanel(), BorderLayout.NORTH);
    this.getContentPane().add(new JPanel(), BorderLayout.SOUTH);
    this.getContentPane().add(new JPanel(), BorderLayout.EAST);
    this.getContentPane().add(new JPanel(), BorderLayout.WEST);

    // Size  부분..적당하게 이것을 고치자.
    setSize(310,490);

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension dlgSize = getSize();
    if (dlgSize.height > screenSize.height) dlgSize.height = screenSize.height;
    if (dlgSize.width > screenSize.width)   dlgSize.width = screenSize.width;
    setLocation((screenSize.width - dlgSize.width) / 2, (screenSize.height - dlgSize.height) / 2);
    setResizable(false);

    // 미리 지정되어 있는지 여기서 확인을 한후..
    // 기존에 있는 init property를 설정한다.
//    setInitProperty();
  }

//  private void setInitProperty() {

//  }

  public boolean isOK() {
    return this.isOK;
  }

  public void okPressed() {
    isOK = true;

    // 직접 Property를 바꾸어 준다.
    // MY CODE
//    property = IdePropertyManager.loadProperty();

    Main.property.setServletRunnerPath(pathTab.getServletRunnerPath());
    Main.property.setServletWebBrowserPath(pathTab.getWebBrowserPath());
    Main.property.setMakingBatch(pathTab.getMakingBatch());
    Main.property.setServletPort(optionTab.getPort());
    Main.property.setServletLog(optionTab.getLog());
    Main.property.setServletMax(optionTab.getMax());
    Main.property.setServletTimeout(optionTab.getTimeout());
    Main.property.setServletDirectory(optionTab.getDirectory());
    Main.property.setServletProperty(optionTab.getProperty());

    IdePropertyManager.saveProperty(Main.property);
//    System.out.println(pathTab.getServletRunnerPath());
    // dialog 종료
    dispose();
  }
  public void dispose() {
    int count = getComponentCount();
    for(int i=0; i<count; i++) {
      Component c = getComponent(i);
      this.remove(c);
      c = null;
    }
    super.dispose();
    System.gc();
  }

  class ActionHandler implements ActionListener{
    public void actionPerformed(ActionEvent e) {
      String cmd = e.getActionCommand();

      if(cmd.equals("OK")) {
        okPressed();
      }
      else if(cmd.equals("CANCEL")) {
        isOK = false;
        dispose();
      }
    }
  }
}

