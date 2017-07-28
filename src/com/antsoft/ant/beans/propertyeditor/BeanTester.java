package com.antsoft.ant.beans.propertyeditor;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class BeanTester extends JFrame implements MouseListener{

  private static BeanList beanList;
  private static PropertySheet proSheet;
  private static BeanTester tester;
  private static PropertyEditors proEditor;
  private String currentPath;
  public JPanel p0;
  public int x;
  public int y;
  public int width;
  public int height;

  public BeanTester(String title, String currentPath){
    super(title);
    this.currentPath = currentPath;
    proEditor = new PropertyEditors(this,currentPath);
    proSheet = new PropertySheet();
    beanList = new BeanList(this,proSheet,proEditor,currentPath);
    //p0 = new JPanel();
    //p0.setPreferredSize(new Dimension(350,350));
    //EtchedBorder border = new EtchedBorder();
    //p0.setBorder(border);
    getContentPane().setLayout(null);
    //JPanel panel = (JPanel)getGlassPane();
    //panel.setLayout(null);
    //p0.setLayout(null);
    addMouseListener(this);
    //getContentPane().add(p0);
  }

  public static void main(String[] args){
    String currentPath = "D:\\";
    tester = new BeanTester("Bean Tester",currentPath);
    tester.addWindowListener(new WindowAdapter(){
      public void windowClosing(WindowEvent e){
          System.exit(0);
      }
    });
    tester.setSize(400,400);
    tester.setVisible(true);
  }

  public void paint(Graphics g){
    Graphics graphics = getGlassPane().getGraphics();
    graphics.drawRect(x,y,width,height);
    super.paint(g);
  }
  //////////////////////////   event Ã³¸®   //////////////////////////////////
  public void mouseClicked(MouseEvent e){
    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    beanList.list.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    if(e.getSource() == tester){
      Point p = e.getPoint();
      int index = beanList.getSelectedIndex();
      String beanName = beanList.getSelectedBean();
      proEditor.showBean(proSheet,index,beanName,p);
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
}
