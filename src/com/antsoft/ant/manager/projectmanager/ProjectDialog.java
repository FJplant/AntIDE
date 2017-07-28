/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/manager/projectmanager/ProjectDialog.java,v 1.7 1999/08/30 08:07:24 remember Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.7 $
 */
package com.antsoft.ant.manager.projectmanager;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.io.File;
import javax.swing.border.*;
import com.antsoft.ant.util.*;

/**
 * NewProject Dialog
 *                                            
 * @author kim sang kyun
 */
public class ProjectDialog extends JDialog {
	static public final String FILE = "FILE";
	static public final String OK = "OK";
	static public final String CANCEL = "CANCEL";
	
  private JTextField nameTf, fileTf;
  private JTextArea descTa;
  private JButton fileBtn, okBtn, cancelBtn;
  private String projectFile;
  private String projectName;
  private JFrame parent;
  private boolean isOK = false;


  public ProjectDialog(JFrame frame, String title, boolean modal) {

    super(frame, title, modal);
    this.parent = frame;
    try  {
      jbInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public ProjectDialog() {
    this(null, "", true);
  }

  void jbInit() throws Exception {
		// register window event handler
		addWindowListener(WindowDisposer.getDisposer());
		addKeyListener(WindowDisposer.getDisposer()); 		

    ActionListener al = new ActionEventHandler();
    DocumentListener dl = new DocumentEventHandler();

    //name panel
    JLabel nameLbl = new JLabel("Name");
    nameLbl.setPreferredSize(new Dimension(40,20));
    //nameLbl.setForeground(Color.black);
    nameTf = new JTextField(17);

    JPanel nameP = new JPanel(new FlowLayout(FlowLayout.LEFT));
    nameP.add(nameLbl);
    nameP.add(nameTf);

    //file panel
    JLabel fileLbl = new JLabel("File");
    fileLbl.setPreferredSize(new Dimension(40,20));
    //fileLbl.setForeground(Color.black);
    fileTf = new JTextField(17);
    fileTf.getDocument().addDocumentListener(dl);
    fileBtn = new JButton("..");
    fileBtn.setPreferredSize(new Dimension(20,20));
    fileBtn.setActionCommand(FILE);
    fileBtn.addActionListener(al);

    JPanel fileP = new JPanel(new FlowLayout(FlowLayout.LEFT));
    fileP.add(fileLbl);
    fileP.add(fileTf);
    fileP.add(fileBtn);

    //top panel
    Box box = Box.createVerticalBox();
    box.add(nameP);
    box.add(fileP);

    //desc panel
    JLabel descLbl = new JLabel("Description");
    //descLbl.setForeground(Color.black);

    descTa = new JTextArea();
    descTa.setMargin(new Insets(6, 6, 6, 6));
    JScrollPane pane = new JScrollPane(descTa);
    pane.setPreferredSize(new Dimension(230,170));

    JPanel descP = new JPanel(new BorderLayout());
    descP.add(descLbl, BorderLayout.NORTH);
    descP.add(pane, BorderLayout.CENTER);

    //button panel
    okBtn = new JButton("OK");
    okBtn.setEnabled(false);
    okBtn.setActionCommand(OK);
    okBtn.addActionListener(al);

    cancelBtn = new JButton("Cancel");
    cancelBtn.setActionCommand(CANCEL);
    cancelBtn.addActionListener(al);

    JPanel buttonP = new JPanel();
    buttonP.add(okBtn);
    buttonP.add(cancelBtn);

    JPanel mainP = new JPanel(new BorderLayout());
    TitledBorder border = new TitledBorder(new EtchedBorder(),"New Project");
    border.setTitleColor(Color.black);
    border.setTitleFont(FontList.regularFont);
    mainP.setBorder(border);
    mainP.add(box, BorderLayout.NORTH);
    mainP.add(descP, BorderLayout.CENTER);
    mainP.add(new JPanel(),BorderLayout.WEST);
    mainP.add(new JPanel(),BorderLayout.EAST);
    mainP.add(new JPanel(),BorderLayout.SOUTH);

    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(mainP, BorderLayout.CENTER);
    getContentPane().add(buttonP,BorderLayout.SOUTH);
    getContentPane().add(new JPanel(),BorderLayout.WEST);
    getContentPane().add(new JPanel(),BorderLayout.EAST);

    setSize(310, 280);
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

  public boolean isOK(){
    return this.isOK;
  }

  public void fileSelected(){
    FileDialog filedlg = new FileDialog(parent, "New Project");
    filedlg.setFile("*.apr");
    filedlg.setVisible(true);

    if (filedlg.getFile() != null && filedlg.getFile().endsWith(".apr")) {
      fileTf.setText(filedlg.getDirectory()+filedlg.getFile());
      if(fileTf.getText().endsWith("apr")) okBtn.setEnabled(true);
    }
  }

  public String getProjectFilePath(){
    return this.fileTf.getText();
  }

  public String getProjectName(){
    return this.nameTf.getText();
  }

  public String getComment(){
    String ret = descTa.getText();
    if(ret == null || ret.length() == 0) return null;
    else return ret;
  }

  class DocumentEventHandler implements DocumentListener{

    public void process(DocumentEvent e){
      if(fileTf.getText().endsWith("apr")) okBtn.setEnabled(true);
    }
    public void insertUpdate(DocumentEvent e){ process(e);  }
    public void changedUpdate(DocumentEvent e){ process(e); }
    public void removeUpdate(DocumentEvent e){ process(e); }
  }


  class ActionEventHandler implements ActionListener{
    public void actionPerformed(ActionEvent e){
      String cmd = e.getActionCommand();

      // We can use == because cmd is static
      if(cmd == FILE){
        fileSelected();
      }
      else if(cmd == OK){
        isOK = true;
        dispose();
      }
      else if(cmd == CANCEL ){
        isOK = false;
        dispose();
      }
    }
  }
}

