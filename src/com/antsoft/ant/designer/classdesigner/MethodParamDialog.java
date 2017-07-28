/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/designer/classdesigner/MethodParamDialog.java,v 1.7 1999/08/19 06:30:46 multipia Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.7 $
 * $History: MethodParamDialog.java $
 * 
 * *****************  Version 4  *****************
 * User: Multipia     Date: 99-05-16   Time: 11:44p
 * Updated in $/AntIDE/source/ant/designer/classdesigner
 * 
 * *****************  Version 3  *****************
 * User: Remember     Date: 99-05-07   Time: 2:35p
 * Updated in $/AntIDE/source/ant/designer/classdesigner
 * 
 * *****************  Version 2  *****************
 * User: Remember     Date: 99-05-02   Time: 1:34a
 * Updated in $/AntIDE/source/ant/designer/classdesigner
 * 
 * *****************  Version 8  *****************
 * User: Remember     Date: 98-10-10   Time: 3:17a
 * Updated in $/JavaProjects/src/ant/designer/classdesigner
 * com.sun.java -> javax
 * 
 * *****************  Version 7  *****************
 * User: Remember     Date: 98-09-29   Time: 1:57a
 * Updated in $/JavaProjects/src/ant/designer/classdesigner
 * class name change
 * 
 * *****************  Version 6  *****************
 * User: Remember     Date: 98-09-23   Time: 6:35a
 * Updated in $/JavaProjects/src/ant/designer/classdesigner
 * Class 이름 바꾸었음.
 * 
 * *****************  Version 5  *****************
 * User: Remember     Date: 98-09-22   Time: 12:43a
 * Updated in $/JavaProjects/src/ant/designer/classdesigner
 * ClassName 바뀜, 전체적으로 Dialog 처리방식 바뀜
 * 
 * *****************  Version 4  *****************
 * User: Remember     Date: 98-09-21   Time: 9:45p
 * Updated in $/JavaProjects/src/ant/designer/classdesigner
 * 
 * *****************  Version 3  *****************
 * User: Remember     Date: 98-09-18   Time: 10:28p
 * Updated in $/JavaProjects/src/ant/designer/classdesigner
 * 
 * *****************  Version 2  *****************
 * User: Remember     Date: 98-09-16   Time: 12:03p
 * Updated in $/JavaProjects/src/ant/designer/classdesigner
 *
 * *****************  Version 1  *****************
 * User: Remember     Date: 98-09-15   Time: 3:08a
 * Created in $/JavaProjects/src/ant/designer/classdesigner
 *
 * *****************  Version 2  *****************
 * User: Remember     Date: 98-09-07   Time: 10:07p
 * Updated in $/Ant/src/ant/designer/classdesigner
 * package name 변경
 *
 * *****************  Version 1  *****************
 * User: Remember     Date: 98-09-03   Time: 12:59a
 * Created in $/Ant/src
 * Attrubute add GUI
 *
 */
package com.antsoft.ant.designer.classdesigner;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import com.antsoft.ant.util.*;

/**
 * @author  Kim sang kyun
 */
public class MethodParamDialog extends JDialog {
  public static final int ADD = 0;
  public static final int UPDATE = 1;

  private JTextField typeTf, nameTf;
  private JTextArea descTa;
  private JButton okOrupdateBtn, cancelBtn;
  private int updateRow;

  /** Dialog 종료시 OK, Cancel 선택 여부를 표시하는 flag */
  private boolean isOK = false;

  /** OK 선택시 채워져야 할 data */
  private String type, name, desc;

  /** Dialog mode */
  private int mode;

  public MethodParamDialog(JFrame parent, String title, boolean modal, int mode) {
    super(parent, title, modal);
    this.mode = mode;
    try  {
      jbInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void setOK(boolean flag){
    this.isOK = flag;
  }

  public boolean isOK(){
    return this.isOK;
  }

  public String getType(){
    return this.type;
  }

  public String getName(){
    return this.name;
  }

  public String getDesc(){
    return this.desc;
  }

  void jbInit() throws Exception {
    // add window disposer
		addWindowListener(WindowDisposer.getDisposer());
		addKeyListener(WindowDisposer.getDisposer()); 		

    ActionListener actionEventHandler = new ActionEventHandler();

    //type, name, init label
    JLabel typeL = new JLabel("Type");
    typeL.setPreferredSize(new Dimension(40,20));
    JLabel nameL = new JLabel("Name");
    nameL.setPreferredSize(new Dimension(40,20));

    //type, name, init textfield
    typeTf = new JTextField(14);
    nameTf = new JTextField(14);

    //by lila
    JPanel p1 = new JPanel();
    p1.setLayout(new FlowLayout(FlowLayout.LEFT));
    p1.add(typeL);
    p1.add(typeTf);

    JPanel p2 = new JPanel();
    p2.setLayout(new FlowLayout(FlowLayout.LEFT));
    p2.add(nameL);
    p2.add(nameTf);

    JPanel p3 = new JPanel();
    p3.setLayout(new GridLayout(2,1));
    p3.add(p1);
    p3.add(p2);

    //Desc text area
    descTa = new JTextArea();

    //scrollpane
    JScrollPane descSp = new JScrollPane(descTa);
    descSp.setPreferredSize(new Dimension(190,75));

    //by lila
    JPanel p4 = new JPanel();
    TitledBorder border = new TitledBorder(new EtchedBorder(),"Java Doc Comment");
    border.setTitleFont(FontList.regularFont);
    border.setTitleColor(Color.black);
    p4.setBorder(border);
    p4.setLayout(new FlowLayout(FlowLayout.CENTER));
    p4.add(descSp);

    JPanel p5 = new JPanel();
    p5.setLayout(new BorderLayout());
    p5.add(p3,BorderLayout.NORTH);
    p5.add(p4,BorderLayout.CENTER);

    if(getMode() == UPDATE){
      okOrupdateBtn = new JButton("Update");
      okOrupdateBtn.setActionCommand("UPDATE");
      okOrupdateBtn.addActionListener(actionEventHandler);
    }
    else{
      okOrupdateBtn = new JButton("OK");
      okOrupdateBtn.setActionCommand("OK");
      okOrupdateBtn.addActionListener(actionEventHandler);
    }
    cancelBtn = new JButton("Cancel");
    cancelBtn.setActionCommand("CANCEL");
    cancelBtn.addActionListener(actionEventHandler);

    JPanel p6 = new JPanel();
    p6.setLayout(new FlowLayout(FlowLayout.CENTER));
    p6.add(okOrupdateBtn);
    p6.add(cancelBtn);

    JPanel p7 = new JPanel();
    p7.setBorder(new EtchedBorder());
    p7.add(p5,BorderLayout.CENTER);

    this.getContentPane().setLayout(new BorderLayout());
    this.getContentPane().add(p7,BorderLayout.CENTER);
    this.getContentPane().add(new JPanel(),BorderLayout.WEST);
    this.getContentPane().add(new JPanel(),BorderLayout.EAST);
    this.getContentPane().add(new JPanel(),BorderLayout.NORTH);
    this.getContentPane().add(p6,BorderLayout.SOUTH);

    setSize(248, 258);
    this.setResizable(false);

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension dlgSize = getSize();
    if (dlgSize.height > screenSize.height)
      dlgSize.height = screenSize.height;
    if (dlgSize.width > screenSize.width)
      dlgSize.width = screenSize.width;
    setLocation((screenSize.width - dlgSize.width) / 2,
                (screenSize.height - dlgSize.height) / 2);

    //font 지정
    nameL.setFont(FontList.regularFont);
    typeL.setFont(FontList.regularFont);
    nameL.setForeground(Color.black);
    typeL.setForeground(Color.black);
  }

  public void setUpdateRow(int row){
    this.updateRow = row;
  }

  public int getUpdateRow(){
    return this.updateRow;
  }

  /**
   * mode를 알아낸다
   *
   * @return current mode
   */
  public int getMode(){
    return this.mode;
  }

  public void restoreData(String type, String name, String desc){
    typeTf.setText(type);
    nameTf.setText(name);
    descTa.setText(desc);
  }

  //Action event handler
  class ActionEventHandler implements ActionListener {
    private String actionCommand;
    public void actionPerformed(ActionEvent e){
      actionCommand = e.getActionCommand();

      if(actionCommand.equals("OK")){
        setOK(true);
        type = typeTf.getText();
        name = nameTf.getText();
        desc = descTa.getText();

        if(type.equals("") || name.equals("")){
          JOptionPane.showMessageDialog(null, "Type or Name not Specified!!", "Error", JOptionPane.ERROR_MESSAGE, ClassDesigner.errorIcon );
        }
        else{
          dispose();
        }
      }
      else if(actionCommand.equals("UPDATE")){
        setOK(true);
        int row = getUpdateRow();

        type = typeTf.getText();
        name = nameTf.getText();
        desc = descTa.getText();

        if(type.equals("") || name.equals("")){
          JOptionPane.showMessageDialog(null, "Type or Name not Specified!!", "Error", JOptionPane.ERROR_MESSAGE, ClassDesigner.errorIcon );
        }
        else{
          dispose();
        }
      }
      else if(actionCommand.equals("CANCEL")){
        setOK(false);
        dispose();
      }
      else if(actionCommand.equals("HELP")){
      }
    }
  }
}
