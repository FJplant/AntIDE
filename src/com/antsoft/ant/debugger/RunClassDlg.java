/*
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * Author:    Lee, Chul Mok
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/debugger/RunClassDlg.java,v 1.2 1999/08/19 03:41:08 multipia Exp $
 * $Revision: 1.2 $
 * $History: RunClassDlg.java $
 */
package com.antsoft.ant.debugger;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import java.io.File;
import java.io.FilenameFilter;
import javax.swing.*;
import javax.swing.event.*;

import com.antsoft.ant.util.WindowDisposer;

public class RunClassDlg extends JDialog implements ListSelectionListener{
  private JFrame parent = null;
  private Toolkit toolkit = Toolkit.getDefaultToolkit();
  private JList pathList;
  private MyListModel pathModel;
  private JScrollPane scrListPane;
  private JButton runBtn,cancelBtn,helpBtn;
  private Vector sourcePath;
  private boolean isHelp = false;
  private boolean bisOK = false;
  private String runClassPath = null;

  public RunClassDlg(JFrame frame, String title, Vector sourcePath, boolean modal) {
    super(frame, "Select class file which you want to run", modal);
    this.parent = frame;
    this.sourcePath = sourcePath;

    // add window disposer
		addWindowListener(WindowDisposer.getDisposer());
		addKeyListener(WindowDisposer.getDisposer()); 		

    ActionListener al = new ActionHandler();

    //library list
    pathModel = new MyListModel(sourcePath);
    pathList = new JList(pathModel);
    pathList.setSelectedIndex(0);
    pathList.addListSelectionListener(this);
    pathList.addMouseListener(new MouseAdapter(){
      public void mousePressed(MouseEvent e){
        if(e.getClickCount() > 1){
//          editLibrary(libModel.getElementAt(libraryList.getSelectedIndex()).toString());
        }
      }}
    );

    scrListPane = new JScrollPane(pathList);
    scrListPane.setPreferredSize(new Dimension(500,250));

    runBtn = new JButton("Run");
    runBtn.setActionCommand("RUN");
    runBtn.addActionListener(al);

    cancelBtn = new JButton("Cancel");
    cancelBtn.setActionCommand("CANCEL");
    cancelBtn.addActionListener(al);

    helpBtn = new JButton("help");
    helpBtn.setActionCommand("HELP");
    helpBtn.addActionListener(al);

    JPanel btnPanel = new JPanel(new FlowLayout());
    btnPanel.add(runBtn);
    JLabel emptyL = new JLabel("");
    emptyL.setPreferredSize(new Dimension(50,10));
    btnPanel.add(emptyL);
    btnPanel.add(cancelBtn);
    btnPanel.add(helpBtn);

    this.getContentPane().add(scrListPane,BorderLayout.CENTER);
    this.getContentPane().add(btnPanel, BorderLayout.SOUTH);
    this.getContentPane().add(new JPanel(), BorderLayout.EAST);
    this.getContentPane().add(new JPanel(), BorderLayout.WEST);
    this.getContentPane().add(new JPanel(), BorderLayout.NORTH);

    setSize(400,300);
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension dlgSize = getSize();
    if (dlgSize.height > screenSize.height) dlgSize.height = screenSize.height;
    if (dlgSize.width > screenSize.width)   dlgSize.width = screenSize.width;
    setLocation((screenSize.width - dlgSize.width) / 2, (screenSize.height - dlgSize.height) / 2);
    setResizable(true);
  }

  public void valueChanged(ListSelectionEvent e) {
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
  public boolean isOK() {
  	return this.bisOK;
  }
  
  public void setRunClassPath(String s) {
  	this.runClassPath = s;
  	bisOK = true;
  }

  public String getRunClassPath() {
  	return this.runClassPath;
  }

  public Vector getSourcePath() {
    return pathModel.getAllSourcePath();
  }

  public void addPathList(String path) {
    if (!pathModel.hasPath(path))
      pathModel.addElement(path);

  }

  public void removePathList(int index) {
    pathModel.removeElementAt(index);
  }

  class MyListModel extends AbstractListModel {
    Vector path = new Vector();
    public MyListModel(Vector path) {
      this.path = path;
    }

    public int getSize(){
      return path.size();
    }

    public Object getElementAt(int index){
      return path.elementAt(index);
    }

    public Vector getAllSourcePath() {
      return path;
    }

    public boolean hasPath(String p) {
      if(path.contains(p))
        return true;
      else
        return false;
    }
    public void removeElementAt(int index){
      path.removeElementAt(index);
      fireIntervalRemoved(this, index, index);
    }

    public void addElement(String p) {
      int index = path.size();
      path.addElement(p);
      fireIntervalAdded(this, index, index);
    }
    public void test() {
      for(int i=0; i<path.size(); i++)
        System.out.println((String)path.elementAt(i));
    }
  }


  class ActionHandler implements ActionListener{
    public void actionPerformed(ActionEvent e){
      String cmd = e.getActionCommand();

      if(cmd.equals("RUN")){
        String runClassPath = null;
        if (sourcePath.size()>0)
          runClassPath = (String)pathList.getSelectedValue();
        setRunClassPath(runClassPath);
        dispose();
      }

      else if(cmd.equals("CANCEL")){
        dispose();
      }
      else if(cmd.equals("HELP")){
        pathModel.test();
      }
    }
  }
}
