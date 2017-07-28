/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/wizard/Wizard.java,v 1.22 1999/08/31 12:26:45 multipia Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.22 $
 */

package com.antsoft.ant.wizard;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.zip.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import com.antsoft.ant.main.*;
import com.antsoft.ant.manager.projectmanager.*;
import com.antsoft.ant.util.*;
import com.antsoft.ant.property.*;
import com.antsoft.ant.wizard.customwizard.*;
import com.antsoft.ant.wizard.generalwizard.*;

/**
 * @author YunKyung Kim
 */
public class Wizard extends JDialog implements ActionListener, MouseListener, ChangeListener{
  private JFrame frame = null;
  //general wizard icon
  private MyIcon[] myIcon = new MyIcon[13];
  //custom wizard icon
  private MyIcon[] customIcon;

//  private Color color = Color.red;
  private JButton close,add,remove;

  private String wizardPath = IdePropertyManager.CUSTOM_WIZARD_PATH;

  private JTabbedPane tabPane;

  private JPanel generalWPanel, customWPanel;

  //general wizard icon 선택을 알리는 flag
  boolean[] flag = new boolean[13];
  //custom wizard icon 선택을 알리는 flag
  boolean[] customFlag;

  private CustomWizard customWizard;

  //event adapter
  private CustomWizardEventAdapter adapter = new CustomWizardEventAdapter();

  File[] file;
  String path = Main.property.getPathModel().getSourceRoot();

  public Wizard(JFrame frame, String title, boolean modal, CustomWizard customWizard) {
    super(frame, title, modal);
    this.frame = frame;
    this.customWizard = customWizard;
    try  {
      jbInit();
      setBackground( Color.white );
      
      setResizable( false );
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  void jbInit() throws Exception {
		// register window event handler
		addWindowListener(WindowDisposer.getDisposer());
		addKeyListener(WindowDisposer.getDisposer()); 		

    //tabPane create
    tabPane = new JTabbedPane();
    tabPane.addChangeListener(this);

    setGeneralWPanel();
    setCustomWPanel();

    close = new JButton("Close");
    add = new JButton("Add");
    remove = new JButton("Remove");
    add.setEnabled(false);
    remove.setEnabled(false);  

    add.addActionListener(this);
    remove.addActionListener(this);
    close.addActionListener(this);

    JPanel buttonP = new JPanel();
    buttonP.setLayout( new FlowLayout( FlowLayout.CENTER ) );
    buttonP.add(add);
    buttonP.add(remove);
    buttonP.add(close);

    tabPane.addTab("General Wizard",generalWPanel);
    tabPane.addTab("Custom Wizard", customWPanel);

    JPanel panel = (JPanel)getContentPane();
    panel.setLayout( new BorderLayout() );
    panel.add( tabPane, BorderLayout.CENTER );
    panel.add( new JPanel(), BorderLayout.EAST );
    panel.add( new JPanel(), BorderLayout.WEST );
    panel.add( buttonP, BorderLayout.SOUTH );
  }

  public void setGeneralWPanel(){
    try{
      //Icon create
      String[] name = {"Application","Applet","Servlet","JDBC","Beans","Event","Frame","Class",
      									"Panel","Dialog","HTML","Text","CustomWizard"};
      ImageIcon[] image = {ImageList.appIcon,ImageList.appletIcon,ImageList.servletIcon,
                          ImageList.jdbcIcon,ImageList.beansIcon,ImageList.newEventIcon,ImageList.newFrameIcon,
                          ImageList.newClassIcon,ImageList.panelIcon,ImageList.dialogIcon,
                          ImageList.newHtmlIcon,ImageList.newTextIcon,ImageList.customWIcon};
      createGeneralIcon(name,image);

      //panel에 label넣기
      JPanel p1 = new JPanel();
      p1.setBackground( Color.white );
      p1.setLayout(new FlowLayout(FlowLayout.LEFT));
      p1.add( myIcon[0] );
      p1.add( new JLabel(" ") );
      p1.add( myIcon[1] );
      p1.add( new JLabel("   ") );
      p1.add( myIcon[2] );
      p1.add( new JLabel("   ") );
      p1.add( myIcon[3] );
      p1.add( new JLabel("   ") );
      p1.add( myIcon[4] );

      JPanel p2 = new JPanel();
      p2.setBackground(Color.white);
      p2.setLayout(new FlowLayout(FlowLayout.LEFT));
      p2.add( new JLabel(" ") );
      p2.add( myIcon[5] );
      p2.add( new JLabel("     ") );
      p2.add( myIcon[6] );
      p2.add( new JLabel("   ") );
      p2.add( myIcon[7] );
      p2.add( new JLabel("   ") );
      p2.add( myIcon[8] );
      p2.add( new JLabel("   ") );
      p2.add( myIcon[9] );

      JPanel p3 = new JPanel();
      p3.setBackground(Color.white);
      p3.setLayout(new FlowLayout(FlowLayout.LEFT));
      p3.add( new JLabel(" ") );
      p3.add(myIcon[10]);
      p3.add( new JLabel("   ") );
      p3.add(myIcon[11]);
      p3.add( new JLabel("   ") );
      p3.add(myIcon[12]);
      
      JPanel p4 = new JPanel();
      p4.setLayout(new GridLayout(3,1));
      p4.setBackground(Color.white);
      p4.add(p1);
      p4.add(p2);
      p4.add(p3);

      generalWPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      generalWPanel.setBorder(BorderList.etchedBorder5);
      generalWPanel.add(p4);
      generalWPanel.setBackground( Color.white );
      
    }catch(Exception ex){
      ex.printStackTrace();
    }

  }

  public void setCustomWPanel(){
    try{
      customWPanel = null;
      customIcon = null;
      customFlag = null;

      customWPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      customWPanel.setBackground(Color.white);
      customWPanel.setBorder(BorderList.etchedBorder5);

      //wizard dir에서 wizard
      File file = new File(wizardPath);
      if(!file.exists() || !file.isDirectory()) file.mkdir();
      String[] list = file.list();
      if((list != null) && (list.length != 0)){
	      //label initialize
	      customIcon = new MyIcon[list.length];
	      //flag initialize
	      customFlag = new boolean[list.length];

	      String[] wizardName = new String[list.length];

	      for(int i=0; i<list.length; i++){
        	if(list[i].endsWith(".jar")){
	        	//wiard name 구하기
  	      	wizardName[i] = list[i].substring(0,list[i].lastIndexOf("."));
  	      }
        }
        //icon label 만들기
        createCustomIcon(wizardName);

        if(list.length != 0){
          //panel에 label넣기
          JPanel[] panel = new JPanel[list.length/5+1];
          int k = 0;
          LOOP: for(int j=0; j<panel.length; j++){
            panel[j] = new JPanel(new FlowLayout(FlowLayout.LEFT,8,0));
            panel[j].setBackground(Color.white);
            for(int p=0; p<4; p++){
              if(customIcon.length > j*5+p){
                panel[j].add(customIcon[j*5+p]);
              }else{
                break LOOP;
              }
            }
            if(customIcon.length > j*5+4)
              panel[j].add(customIcon[j*5+4]);
            else break;
          }

          JPanel totalP = new JPanel(new GridLayout(list.length/5+1,1));
          totalP.setBackground(Color.white);
          for(int p=0; p<list.length/5+1; p++){
            totalP.add(panel[p]);
          }
          customWPanel.add(totalP);
        }
      }
    }catch(Exception ex){
      ex.printStackTrace();
    }
  }

  /**
   *  createGeneralIcon - 아이콘을 만든다
   *  @name   wizard 이름
   *  @image  image
   */
  private void createGeneralIcon(String[] name,ImageIcon[] image){
    JPanel[] pl = new JPanel[name.length];
    for(int i=0; i<name.length; i++){
      myIcon[i] = new MyIcon(name[i],image[i]);
      myIcon[i].addMouseListener(this);
    }
  }

  /**
   *  createCustomIcon - 아이콘을 만든다
   *  @name   wizard 이름
   */
  private void createCustomIcon(String[] name){
    for(int i=0; i<name.length; i++){
      customIcon[i] = new MyIcon(name[i], ImageList.cwDefaultIcon);
      customIcon[i].addMouseListener(adapter);
    }
  }

  public void setPath(String path) {
  	this.path = path;
  }

  public File[] getFiles() {
  	return file;
  }

  class MyIcon extends JPanel {
    private boolean isSelected = false;
    public JPanel p;
    public JLabel l;

    public MyIcon(String text, Icon icon){
      this.setBackground(Color.white);
      isSelected = false;

      JLabel i = new JLabel(icon);
      l = new JLabel(text);
      p = new JPanel();
      p.setBackground(Color.white);
      p.add(l);

      this.setLayout(new BorderLayout(0,5));
      add(i,BorderLayout.CENTER);
      add(p,BorderLayout.SOUTH);
    }

    public MyIcon(String text){
      this.setBackground(Color.white);
      isSelected = false;

      l = new JLabel(text);
      p = new JPanel();
      p.setBackground(Color.white);
      p.add(l);

      JPanel tmp = new JPanel();
      tmp.setBackground(Color.white);
      this.setLayout(new BorderLayout(3,0));
      add(tmp,BorderLayout.CENTER);
      add(p,BorderLayout.SOUTH);
    }
  }

  //////////////////// event 처리   //////////////////////////////////////////////////////////
  public void actionPerformed( ActionEvent e ){
    if(e.getSource() == add){
      new AddCustomWizardDialog(frame,customWizard);
      tabPane.remove(customWPanel);
      customWPanel.removeAll();
      setCustomWPanel();
      tabPane.addTab("Custom Wizard",customWPanel);
      tabPane.setSelectedIndex(1);
      tabPane.revalidate();
    }else if(e.getSource() == remove){
      //new RemoveCustomWizardDialog(frame,customWizard);
      for(int i=0; i<customIcon.length; i++){
        if(customFlag[i])
          customWizard.removeWizard(customIcon[i].l.getText());
      }
      tabPane.remove(customWPanel);
      customWPanel.removeAll();
      setCustomWPanel();
      tabPane.addTab("Custom Wizard",customWPanel);
      tabPane.setSelectedIndex(1);
      tabPane.revalidate();
    }else if(e.getSource() == close){
      System.out.println("close");
      dispose();
    }
  }

  public void mouseClicked( MouseEvent e ){
    if( e.getClickCount() == 1 ){
      for(int i=0; i<myIcon.length; i++){
        if( e.getSource() == myIcon[i] ){
          if( flag[i] ){
          flag[i] = false;
          myIcon[i].p.setBackground(Color.white);
          myIcon[i].l.setForeground(Color.black);
          }else{
            flag[i] = true;
            myIcon[i].p.setBackground(ColorList.darkBlue);
            myIcon[i].l.setForeground(Color.white);
          }
        }else{
          flag[i] = false;
          myIcon[i].p.setBackground(Color.white);
          myIcon[i].l.setForeground(Color.black);
        }
      }
    }

    if( e.getClickCount() == 2 ){
      dispose();
      if( e.getSource() == myIcon[0] ){
        ApplicationWizard aw = new ApplicationWizard(frame, path);
        file = aw.getFiles();
      }else if( e.getSource() == myIcon[1] ){
        AppletWizard apw = new AppletWizard( frame, path );
        file = apw.getFiles();
      }else if( e.getSource() == myIcon[2] ){
        ServletWizard sw = new ServletWizard( frame, path );
        file = sw.getFiles();
      }else if( e.getSource() == myIcon[3] ){
        JDBCWizard jw = new JDBCWizard( frame, path );
        file = jw.getFiles();
      }else if( e.getSource() == myIcon[4] ){
        BeanWizard bw = new BeanWizard( frame, path );
        file = bw.getFiles();
      }else if( e.getSource() == myIcon[5] ){
        EventWizard ew = new EventWizard( frame, path );
        file = ew.getFiles();
      }else if(e.getSource() == myIcon[6]){
        FrameWizard fw = new FrameWizard(frame,path);
        file = fw.getFiles();
      }else if(e.getSource() == myIcon[7]){
        ClassWizard cw = new ClassWizard(frame,path);
        file = cw.getFiles();
      }else if(e.getSource() == myIcon[8]){
        PanelWizard pw = new PanelWizard(frame,path);
        file = pw.getFiles();
      }else if(e.getSource() == myIcon[9]){
        DialogWizard dw = new DialogWizard(frame,path);
        file = dw.getFiles();
      }else if(e.getSource() == myIcon[10]){
      	HtmlWizard hw = new HtmlWizard(frame,path);
      	file = hw.getFiles();    
      }else if(e.getSource() == myIcon[11]){
  			TextDialog tw = new TextDialog(frame,path);
  			file = tw.getFiles();
      }else if(e.getSource() == myIcon[12]){
      	CustomWizardDialog cuw = new CustomWizardDialog(frame,"Create Custom Wizard",true,customWizard);
        file = customWizard.getFiles();
      }
    }
  }

  public void mouseEntered( MouseEvent e ){
  }
  public void mouseExited( MouseEvent e ){
  }
  public void mouseReleased( MouseEvent e ){
  }
  public void mousePressed( MouseEvent e ){
  }
  
  public void stateChanged(ChangeEvent e){
  	JTabbedPane pane = (JTabbedPane)e.getSource();
  	if(pane.getSelectedIndex()==0){
  		add.setEnabled(false);
  		remove.setEnabled(false); 
  	}else{
  		add.setEnabled(true);
  		remove.setEnabled(true);
  	}
  } 

  /*
   *  custom event 처리 크래스
   */
  public class CustomWizardEventAdapter extends MouseAdapter {

    public void mouseClicked(MouseEvent e){
      if( e.getClickCount() == 1 ){
        for(int i=0; i<customIcon.length; i++){
          if( e.getSource() == customIcon[i] ){
            if( customFlag[i] ){
            customFlag[i] = false;
            customIcon[i].p.setBackground(Color.white);
            customIcon[i].l.setForeground(Color.black);
            }else{
              customFlag[i] = true;
              customIcon[i].p.setBackground(ColorList.darkBlue);
              customIcon[i].l.setForeground(Color.white);
            }
          }else{
            customFlag[i] = false;
            customIcon[i].p.setBackground(Color.white);
            customIcon[i].l.setForeground(Color.black);
          }
        }
      }
      if(e.getClickCount() == 2){
        dispose();
        MyIcon icon = (MyIcon)e.getSource();
        customWizard.startWizard(icon.l.getText()+".jar");
        file = customWizard.getFiles();
      }
    }
  }
}

