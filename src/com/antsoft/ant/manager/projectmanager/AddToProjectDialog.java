/*
 * $Id: AddToProjectDialog.java,v 1.11 1999/08/31 12:27:11 multipia Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.11 $
 */                           
package com.antsoft.ant.manager.projectmanager;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.Vector;
import java.util.Hashtable;

import com.antsoft.ant.util.*;

/**
 *  Add to project를 선택했을 때에 사용되는 Dialog
 * 
 * @author kim sang kyun
 */
public class AddToProjectDialog extends JDialog implements ItemListener {
  private JButton selectAllBtn, okBtn, cancelBtn;
  private boolean isOK = false;
  private File dir;
  private JList list;
  private MyListModel model;
  private JComboBox cbx;
	private JCheckBox recursively;
	
  public static final int JAVA = 0;
  public static final int HTML = 1;
  public static final int TXT = 2;
  public static final int ALL = 3;

  public AddToProjectDialog(Frame frame, String title, boolean modal, File dir, Hashtable files) {
    super(frame, title, modal);
    this.dir = dir;

		// register window event handler
		addWindowListener(WindowDisposer.getDisposer());
		addKeyListener(WindowDisposer.getDisposer()); 		

    MouseListener ml = new MouseHandler();

		recursively = new JCheckBox("Recursively");
		
    cbx = new JComboBox();
    cbx.addItem("  * . java");
    cbx.addItem("  * . html");
    cbx.addItem("  * . txt");
    cbx.addItem("  * . *");

    cbx.addItemListener(this);

    JLabel cbxLbl = new JLabel("File Filter");

    JPanel cbxP = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    cbxP.add(recursively);
    cbxP.add(cbxLbl);
    cbxP.add(cbx);
    cbxP.add(new JLabel(" "));

    model = new MyListModel(files, dir.list());
    list = new JList(model);
    list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

    list.setCellRenderer(new CellRenderer());
    list.addMouseListener(ml);
    model.setFilter(JAVA);

    JScrollPane pane = new JScrollPane(list);

    JPanel listP = new JPanel(new BorderLayout());
    listP.setBorder(new BlackTitledBorder("List to Add"));
    listP.add(pane,BorderLayout.CENTER);
    listP.add(new JPanel(),BorderLayout.WEST);
    listP.add(new JPanel(),BorderLayout.EAST);
    listP.add(new JPanel(),BorderLayout.SOUTH);

    selectAllBtn = new JButton("Select All");
    selectAllBtn.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        if(selectAllBtn.getText().equals("Select All")){
          selectAllBtn.setText("Clear");
          list.setSelectionInterval(0, model.getSize()-1);
        }
        else{
          selectAllBtn.setText("Select All");
          list.clearSelection();
        }
      }
    });

    okBtn = new JButton("OK");
    okBtn.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        isOK = true;
        dispose();
      }
    });

    cancelBtn = new JButton("Cancel");
    cancelBtn.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        isOK = false;
        dispose();
      }
    });

    JPanel btnP = new JPanel();
    btnP.add(selectAllBtn);
    btnP.add(okBtn);
    btnP.add(cancelBtn);

    getContentPane().add(listP, BorderLayout.CENTER);
    getContentPane().add(new JPanel(), BorderLayout.WEST);
    getContentPane().add(new JPanel(), BorderLayout.EAST);
    getContentPane().add(btnP, BorderLayout.SOUTH);
    getContentPane().add(new JPanel(), BorderLayout.NORTH);
    getContentPane().add(cbxP, BorderLayout.NORTH);
    setSize(248, 400);

    Dimension m_ScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension m_dlgSize = getSize();
    if (m_dlgSize.height > m_ScreenSize.height)  m_dlgSize.height = m_ScreenSize.height;
    if (m_dlgSize.width > m_ScreenSize.width)    m_dlgSize.width = m_ScreenSize.width;
    setLocation((m_ScreenSize.width - m_dlgSize.width) / 2, (m_ScreenSize.height - m_dlgSize.height) / 2);

    this.setResizable(false);
  }

  public File [] getSelectedFiles(){
    int [] sels = list.getSelectedIndices();
    File [] ret = new File[sels.length];

    for(int i=0; i<sels.length; i++){
      ret[i] = new File(dir, (String)model.getElementAt(sels[i]));
    }
    return ret;
  }

  public boolean isOK(){
    return isOK;
  }

  public boolean isRecursively() {
  	return recursively.isSelected();
  }

  public void itemStateChanged(ItemEvent e){
    model.setFilter(cbx.getSelectedIndex());
    list.repaint();
  }

  class MyListModel extends AbstractListModel {
    private Vector all = new Vector(10, 2);
    private Vector curr = new Vector(10, 2);
    private Vector dirs = new Vector(5, 2);

    public MyListModel(Hashtable prev, String [] files){
      for(int i=0; i<files.length; i++){
        if(prev!=null && prev.get(files[i]) != null) continue;

        File f = new File(dir, files[i]);
        if(f.isDirectory() && !f.getName().equals("CVS") ) 
        	dirs.addElement(files[i]);
        else all.addElement(files[i]);
      }
    }

    public int getSize(){
      setSize(curr.size());
      return curr.size();
    }

    public void setSize(int newSize) {
      int oldSize = curr.size();
      curr.setSize(newSize);
      if (oldSize > newSize) {
          fireIntervalRemoved(this, newSize, oldSize-1);
      }
      else if (oldSize < newSize) {
          fireIntervalAdded(this, oldSize, newSize-1);
      }
    }

    /**
     * Returns the value at the specified index.
     */
    public Object getElementAt(int index){
      return curr.elementAt(index);
    }

    public void setFilter(int type){
      curr.removeAllElements();
      switch(type){
        case JAVA :
          int index = 0;
          for(int i=0; i<dirs.size(); i++){
            curr.addElement((String)dirs.elementAt(i));
            fireIntervalAdded(this, index, index);
            index++;
          }

          for(int i=0;i<all.size(); i++){
            if(all.elementAt(i).toString().toLowerCase().endsWith(".java")) {
              curr.addElement(all.elementAt(i));
              fireIntervalAdded(this, index, index);
              index++;
            }
          }

          break;

        case HTML :
          for(int i=0;i<all.size(); i++){
            String item = all.elementAt(i).toString().toLowerCase();
            if(item.endsWith(".htm") || item.endsWith(".html")) {
              curr.addElement(item);
              fireIntervalAdded(this, i, i);
            }
          }
          break;

        case TXT :
          for(int i=0;i<all.size(); i++){
            if(all.elementAt(i).toString().toLowerCase().endsWith(".txt")) {
              curr.addElement(all.elementAt(i));
              fireIntervalAdded(this, i, i);
            }
          }
          break;

        case ALL :
          index = 0;
          for(int i=0;i<dirs.size(); i++){
            curr.addElement(dirs.elementAt(i));
            fireIntervalAdded(this, index, index);
            index++;
          }

          for(int i=0;i<all.size(); i++){
            curr.addElement(all.elementAt(i));
            fireIntervalAdded(this, index, index);
            index++;
          }

          break;
      }

      setSize(curr.size());
    }
  }

  class CellRenderer extends JLabel implements ListCellRenderer{
    private Color selBack = ColorList.darkBlue;
    private Color selFore = Color.white;
    private Color unselBack = Color.white;
    private Color unselFore = Color.black;

    public CellRenderer(){
    	setOpaque(true);
    }

    public Component getListCellRendererComponent(  JList list,
                                                    Object value,
                                                    int index,
                                                    boolean isSelected,
                                                    boolean cellHasFocus )
    {
       	if (isSelected) {
    	    this.setBackground(selBack);
	        this.setForeground(selFore);
        }
        else {
          this.setBackground(unselBack);
          this.setForeground(unselFore);
        }

        String text = (value == null) ? "" : value.toString();
        setText(" " + text);

        if(text.toLowerCase().endsWith(".java")){
          setIcon(ImageList.javaIcon);
        }
        else if(text.toLowerCase().endsWith(".htm") || text.toLowerCase().endsWith(".html") ) {
          setIcon(ImageList.htmlIcon);
        }
        else if(text.toLowerCase().endsWith(".txt")) {
          setIcon(ImageList.textIcon);
        }
        else if(text.indexOf(".") == -1){
          File f = new File(dir, text);
          if(f.isDirectory()) setIcon(ImageList.dirIcon);
        }
        else{
          setIcon(ImageList.transIcon);
        }

        this.setEnabled(list.isEnabled());
        this.setFont(list.getFont());

        return this;
    }
  }

  class MouseHandler extends MouseAdapter {
    public void mouseClicked(MouseEvent e){
      if(e.getClickCount() > 1 && !list.isSelectionEmpty()){
        isOK = true;
        dispose();
      }
    }
  }
}
