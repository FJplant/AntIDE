package com.antsoft.ant.beans.propertyeditor;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.beans.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.table.*;

public class ViewFrame extends JFrame implements MouseListener, ActionListener{

  //components
  JList list;
  JPanel panel = new JPanel();
  JButton add = new JButton("Add Beans");
  JButton remove = new JButton("Remove Beans");
  JLabel beanListLbl = new JLabel("    Bean List");
  JLabel viewerLbl = new JLabel("    Viewer");
  JPanel proSheet = new JPanel();
  BevelBorder border1 = new BevelBorder(BevelBorder.RAISED);
  EtchedBorder border2 = new EtchedBorder();
  TitledBorder border3 = new TitledBorder("Property Sheet");
  EtchedBorder border4 = new EtchedBorder(BevelBorder.LOWERED);
  JPanel p2 = new JPanel();
  JPanel p6 = new JPanel();

  Vector beanNames;
  Vector classPathes;

  String beanName;
  static PropertyPanel propertyPanel;
  static PropertyEditors proEditor;

  static String path = "d:\\";

  AddDialog dlg;

  //등록되어 있는 bean의 이름을 가지고 있는 파일
  public ViewFrame(String title) {
    super(title);
    proEditor = new PropertyEditors(this,path);
    jInit();
  }

  public static void main(String[] args) {
    ViewFrame frame1 = new ViewFrame("Bean Viewer");

    frame1.addWindowListener(new WindowAdapter(){
      public void windowClosing(WindowEvent e){
        try{
          proEditor.rf1.close();
          proEditor.rf2.close();
          System.exit(0);
        }catch(IOException ex){
        }
      }
    });

    frame1.setSize(910,500);
    frame1.setVisible(true);
  }

  void jInit(){
    try{
      beanNames = proEditor.readBeanFile();
      classPathes = proEditor.readPathFile();
      list = new JList(beanNames);

      //set size
      beanListLbl.setPreferredSize(new Dimension(150,20));
      list.setPreferredSize(new Dimension(150,380));
      viewerLbl.setPreferredSize(new Dimension(380,20));
      panel.setPreferredSize(new Dimension(360,380));
      proSheet.setPreferredSize(new Dimension(300,360));

      list.addMouseListener(this);

      panel.setBorder(border1);
      panel.setLayout(new FlowLayout(FlowLayout.CENTER));
      proSheet.setLayout(new FlowLayout(FlowLayout.CENTER));
      proSheet.setBorder(border3);

      JPanel p7 = new JPanel();
      p7.setLayout(new FlowLayout(FlowLayout.CENTER));
      p7.setBorder(border4);
      p7.add(list);

      JPanel p1 = new JPanel();
      p1.setLayout(new BorderLayout());
      p1.add(beanListLbl,BorderLayout.NORTH);
      p1.add(p7,BorderLayout.CENTER);
      p1.add(new Panel(), BorderLayout.WEST);

      p2.setLayout(new BorderLayout());
      p2.add(viewerLbl,BorderLayout.NORTH);
      p2.add(panel,BorderLayout.CENTER);
      p2.add(new JPanel(),BorderLayout.WEST);

      p6.setLayout(new BorderLayout());
      p6.add(proSheet,BorderLayout.CENTER);
      p6.add(new JPanel(),BorderLayout.WEST);
      p6.add(new JPanel(),BorderLayout.NORTH);
      p6.add(new JPanel(),BorderLayout.EAST);

      JPanel p3 = new JPanel();
      p3.setLayout(new BorderLayout());
      p3.add(p1,BorderLayout.WEST);
      p3.add(p2,BorderLayout.CENTER);
      p3.add(p6,BorderLayout.EAST);

      JPanel p4 = new JPanel();
      p4.setLayout(new FlowLayout(FlowLayout.CENTER));
      p4.add(add);
      p4.add(remove);
      add.addActionListener(this);
      remove.addActionListener(this);

      JPanel p5 = new JPanel();
      p5.setBorder(border2);
      p5.setLayout(new FlowLayout(FlowLayout.CENTER));
      p5.add(p3);

      getContentPane().setLayout(new BorderLayout());
      getContentPane().add(new JPanel(),BorderLayout.NORTH);
      getContentPane().add(new JPanel(),BorderLayout.EAST);
      getContentPane().add(new JPanel(),BorderLayout.WEST);
      getContentPane().add(p5,BorderLayout.CENTER);
      getContentPane().add(p4,BorderLayout.SOUTH);
    }catch(Exception e){
    }
  }


  public void mouseClicked(MouseEvent e){
    try{
      if(e.getClickCount() == 2){
        proSheet.removeAll();
        panel.removeAll();
        proSheet.repaint();
        //panel.revalidate();
        panel.repaint();

        //path를 읽어온다.
        classPathes = proEditor.readPathFile();
        int index = list.getSelectedIndex();
        //선택된 빈 load시키기
        String jarPath = (String)classPathes.elementAt(index);
        beanName = (String)list.getSelectedValue();
        //jar file풀어서 bean instantiate
        System.out.println("jar-"+jarPath);
        proEditor.jarLoader(jarPath,beanName);
        //property Sheet에 editor 뿌려주기
        Class custom = proEditor.getCustomizer();

        if(custom == null){
          Component[] components = proEditor.getComponents();
          JLabel[] labels = proEditor.getLabels();
          propertyPanel = new PropertyPanel(this,components,labels);
        }else{
          propertyPanel = new PropertyPanel(this,custom);
        }

        //panel에 추가
        proSheet.add(propertyPanel,BorderLayout.CENTER);
        proSheet.revalidate();
        //viewer에 visual bean 보여주기
        Component com = (Component)proEditor.bean;
        //com.setSize(new Dimension(100,100));
        panel.add(com);
        panel.revalidate();

      }
    }catch(Exception ex){
      ex.printStackTrace();
    }
  }

  public void mouseEntered(MouseEvent e){
  }

  public void mouseReleased(MouseEvent e){
  }
  public void mouseExited(MouseEvent e){
  }
  public void mousePressed(MouseEvent e){
  }

  public void actionPerformed(ActionEvent e){
    try{
      if(e.getSource() == add){
        dlg = new AddDialog(this,"Add Beans",true,path);
        dlg.setVisible(true);
        if( dlg.isOk() ){
          String s = dlg.jar.getText();
          String path = s.substring(0,s.length()-dlg.fileName.length());
          proEditor.writePathFile(path);
          proEditor.writeBeanFile(dlg.fileName.substring(0,dlg.fileName.length()-4));
          beanNames = proEditor.readBeanFile();
          classPathes = proEditor.readPathFile();
          list.setListData(beanNames);
        }
      }else if(e.getSource() == remove){
        if(list.isSelectionEmpty()){}
        else{
          int index = list.getSelectedIndex();
          proEditor.updateFiles(index);
          beanNames = proEditor.readBeanFile();
          list.setListData(beanNames);
        }
      }
    }catch(Exception ex){
    }
  }

}

