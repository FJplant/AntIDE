package com.antsoft.ant.beans.propertyeditor;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import java.io.*;

public class BeanList extends JFrame implements MouseListener, ActionListener{

  private  BeanTester tester;
  private static PropertySheet proSheet;
  private static PropertyEditors proEditor;
  public JList list;
  private JScrollPane sp;
  //private Vector beanNames;
  //private Vector classPathes;
  private String currentPath;
  private JLabel beanListLbl = new JLabel("Bean list");
  private JButton add = new JButton("Add");
  private JButton remove = new JButton("Remove");

  public BeanList(BeanTester tester,PropertySheet proSheet,PropertyEditors proEditor,String currentPath){
    super("Bean List");
    this.tester = tester;
    this.proSheet = proSheet;
    this.proEditor = proEditor;
    this.currentPath = currentPath;
    jInit();
    setSize(170,400);
    setVisible(true);
  }

  private void jInit(){
    //list 생성
    Vector beanNames = proEditor.readBeanFile();
    Vector classPathes = proEditor.readPathFile();
    list = new JList(beanNames);
    list.addMouseListener(this);
    sp = new JScrollPane(list,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

    //set size
    beanListLbl.setPreferredSize(new Dimension(150,20));
    //list.setPreferredSize(new Dimension(150,330));
    sp.setPreferredSize(new Dimension(150,320));

    add.addActionListener(this);
    remove.addActionListener(this);

    EtchedBorder border = new EtchedBorder(BevelBorder.LOWERED);
    JPanel p1 = new JPanel();
    p1.setLayout(new FlowLayout(FlowLayout.CENTER));
    p1.setBorder(border);
    p1.add(sp);

    JPanel p2 = new JPanel();
    p2.setLayout(new FlowLayout(FlowLayout.CENTER));
    p2.add(add);
    p2.add(remove);

    getContentPane().add(p1,BorderLayout.CENTER);
    getContentPane().add(p2,BorderLayout.SOUTH);
  }

  public int getSelectedIndex(){
    return list.getSelectedIndex();
  }

  public String getSelectedBean(){
    return (String)list.getSelectedValue();
  }

  //////////////// event 처리   ///////////////////////////////////////////////
  public void mouseClicked(MouseEvent e){
    //list 선택시 커서를 변화시킨다.
    if(e.getSource() == list){
      list.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
      tester.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
    }
  }
  public void mouseEntered(MouseEvent e){
  }
  public void mouseExited(MouseEvent e){
  }
  public void mousePressed(MouseEvent e){
  }
  public void mouseReleased(MouseEvent e){
  }

  public void actionPerformed(ActionEvent e){
    Vector beanNames = null;
    try{
      if(e.getSource() == add){
        JFileChooser chooser = new JFileChooser(currentPath);
        chooser.showOpenDialog(tester);
        File file = chooser.getSelectedFile();
        String path = file.getAbsolutePath();
        String name = file.getName();
        path = path.substring(0,path.length()-name.length());
        proEditor.writePathFile(path);
        proEditor.writeBeanFile(name.substring(0,name.length()-4));
        beanNames = proEditor.readBeanFile();
        list.setListData(beanNames);
      }else if(e.getSource() == remove){
        if(list.isSelectionEmpty()){}
        else{
          System.out.println("1");
          int index = list.getSelectedIndex();
          System.out.println(index);
          proEditor.updateFiles(index);
          beanNames = proEditor.readBeanFile();
          if(beanNames.size() == 0)System.out.println("size 0");
          list.setListData(beanNames);
          list.revalidate();
        }
      }
    }catch(Exception ex){
    }
  }
}
