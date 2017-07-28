/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/designer/classdesigner/ClassDesigner.java,v 1.11 1999/08/20 06:27:28 remember Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.11 $
 * $History: ClassDesigner.java $
 * 
 * *****************  Version 5  *****************
 * User: Remember     Date: 99-05-20   Time: 4:57p
 * Updated in $/AntIDE/source/ant/designer/classdesigner
 * 
 * *****************  Version 4  *****************
 * User: Multipia     Date: 99-05-16   Time: 11:44p
 * Updated in $/AntIDE/source/ant/designer/classdesigner
 *
 * *****************  Version 3  *****************
 * User: Remember     Date: 99-05-07   Time: 2:34p
 * Updated in $/AntIDE/source/ant/designer/classdesigner
 * 
 * *****************  Version 2  *****************
 * User: Kahn         Date: 99-04-22   Time: 11:19a
 * Updated in $/AntIDE/source/ant/designer/classdesigner
 * for help 1.0
 * 
 * *****************  Version 11  *****************
 * User: Remember     Date: 98-10-17   Time: 4:08a
 * Updated in $/JavaProjects/src/ant/designer/classdesigner
 *
 * *****************  Version 10  *****************
 * User: Remember     Date: 98-10-14   Time: 9:10p
 * Updated in $/JavaProjects/src/ant/designer/classdesigner
 *
 * *****************  Version 9  *****************
 * User: Remember     Date: 98-10-10   Time: 3:17a
 * Updated in $/JavaProjects/src/ant/designer/classdesigner
 * com.sun.java -> javax
 * 
 * *****************  Version 8  *****************
 * User: Remember     Date: 98-09-23   Time: 6:35a
 * Updated in $/JavaProjects/src/ant/designer/classdesigner
 * Class 이름 바꾸었음.
 *
 * *****************  Version 7  *****************
 * User: Remember     Date: 98-09-22   Time: 12:43a
 * Updated in $/JavaProjects/src/ant/designer/classdesigner
 * ClassName 바뀜, 전체적으로 Dialog 처리방식 바뀜
 *
 * *****************  Version 6  *****************
 * User: Remember     Date: 98-09-21   Time: 9:45p
 * Updated in $/JavaProjects/src/ant/designer/classdesigner
 *
 * *****************  Version 5  *****************
 * User: Remember     Date: 98-09-18   Time: 10:28p
 * Updated in $/JavaProjects/src/ant/designer/classdesigner
 * 
 * *****************  Version 4  *****************
 * User: Remember     Date: 98-09-18   Time: 12:15a
 * Updated in $/JavaProjects/src/ant/designer/classdesigner
 * 
 * *****************  Version 3  *****************
 * User: Remember     Date: 98-09-16   Time: 12:03p
 * Updated in $/JavaProjects/src/ant/designer/classdesigner
 *
 * *****************  Version 2  *****************
 * User: Remember     Date: 98-09-07   Time: 10:06p
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
import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.*;
import java.util.*;

// By kahn
//import javax.javahelp.*;
//import javax.help.*;
import com.antsoft.ant.main.MainFrame;
// By kahn
import com.antsoft.ant.pool.classpool.ClassPool;
import com.antsoft.ant.pool.librarypool.LibraryPool;
import com.antsoft.ant.pool.packagepool.PackagePool;
import com.antsoft.ant.util.*;

/**
 * @author  Kim sang kyun
 * @author  Kim sung hoon
 */

public class ClassDesigner extends JDialog{
  private JTabbedPane tabbedPane;
  private JButton okBtn, cancelBtn, helpBtn;

  private GeneralPanel generalTab;
  private FieldPanel attrTab;
  private MethodPanel operTab;

  /** Error message dialog에서 사용하는 icon */
  public static Icon errorIcon;
  /** MainFrame 의 참조. Dialog들의 parent 가 된다 */
  public static JFrame parent;

  private boolean isOK = false;

  /** class designer를 통해 사용자로 부터 받은 정보를 저장하는 객체들 */
  private CDGeneralInfo cdGeneralInfo = new CDGeneralInfo();
  private CDAttrInfoContainer cdAttrInfoContainer = new CDAttrInfoContainer();
  private CDOperInfoContainer cdOperInfoContainer = new CDOperInfoContainer();

  private String source, packageName, className;

  /**
   * Default Constructor
   */
  public ClassDesigner( JFrame parent, String title, boolean ismodal ) {
    super(parent, title, ismodal);

    this.parent = parent;
    try  {
      jbInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    tabbedPane = new JTabbedPane();

    // add window disposer
		addWindowListener(WindowDisposer.getDisposer());
		addKeyListener(WindowDisposer.getDisposer()); 		

    //General Tab
    generalTab = new GeneralPanel(cdGeneralInfo);
    //by lila
    tabbedPane.setFont(FontList.regularFont);

    tabbedPane.addTab("General", generalTab);
    tabbedPane.setSelectedIndex(0);

    //Operations Tab
    operTab = new MethodPanel(cdOperInfoContainer);
    tabbedPane.addTab("Method", operTab);

    //Attributes Tab
    attrTab = new FieldPanel(cdAttrInfoContainer);
    attrTab.setMethodPanel(operTab);
    tabbedPane.addTab("Field", attrTab );

    ActionListener actionEventHandler = new ActionEventHandler();

    //ok, cancel, help button
    okBtn = new JButton("OK");
    //by lila
    okBtn.setFont(FontList.regularFont);
    okBtn.setActionCommand("OK");
    okBtn.addActionListener(actionEventHandler);

    cancelBtn = new JButton("Cancel");
    //by lila
    cancelBtn.setFont(FontList.regularFont);
    cancelBtn.setActionCommand("CANCEL");
    cancelBtn.addActionListener(actionEventHandler);

    helpBtn = new JButton("Help");
    //by lila
    helpBtn.setFont(FontList.regularFont);
    helpBtn.setActionCommand("HELP");
    // By Kahn
    //helpBtn.addActionListener(actionEventHandler);
    //MainFrame.helpBroker.enableHelpOnButton(helpBtn,"func.cdesigner",null);
    // By Kahn

    //ok, cancel, help button panel
    JPanel okP = new JPanel();
    FlowLayout okL = new FlowLayout(FlowLayout.CENTER);
    okP.setLayout(okL);
    okP.add(okBtn);
    okP.add(cancelBtn);
    okP.add(helpBtn);

    this.getContentPane().add(tabbedPane, BorderLayout.CENTER);
    this.getContentPane().add(okP, BorderLayout.SOUTH);
    //by lila
    this.getContentPane().add(new JPanel(), BorderLayout.NORTH);
    this.getContentPane().add(new JPanel(), BorderLayout.EAST);
    this.getContentPane().add(new JPanel(), BorderLayout.WEST);

    setSize(638, 408);
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

  /**
   * method panel의 reference를 반환. attrpanel에서 사용(get, set)
   *
   * @return MethodPanel
   */
  public MethodPanel getMethodPanel(){
    return this.operTab;
  }

  public void setPackageName(String packageName){
    generalTab.setPackageName(packageName);
  }

  private void OKPressed(){
    className = generalTab.getClassName();
    if(className==null || className.equals("")){
      isOK = false;
      JOptionPane.showMessageDialog(MainFrame.mainFrame, "You Must Specify Class Name Field", "Error", JOptionPane.ERROR_MESSAGE, ClassDesigner.errorIcon );
    }
    else{
      CDInfo cdInfo = new CDInfo();
      cdInfo.setCDGeneralInfo(cdGeneralInfo);
      cdInfo.setCDAttrInfoContainer(cdAttrInfoContainer);
      cdInfo.setCDOperInfoContainer(cdOperInfoContainer);

      CodeGenerator codeGen = new CodeGenerator();
      source = codeGen.newDocGenerate(cdInfo);
      packageName = codeGen.getPackageName();
      className = codeGen.getTheClassName();
      isOK = true;
      dispose();
    }  
  }

  public String getGeneratedSource(){
    return source;
  }

  public String getPackageName(){
    if(packageName != null) packageName = packageName.trim();
    return packageName;
  }

  public String getClassName(){
    return className;
  }

  public boolean isOK(){
    return isOK;
  }

  //Action event handler
  class ActionEventHandler implements ActionListener {
    private String actionCommand;
    public void actionPerformed(ActionEvent e){
      actionCommand = e.getActionCommand();

      if(actionCommand.equals("OK")){
        OKPressed();
      }
      else if(actionCommand.equals("CANCEL")){
        isOK = false;
        dispose();
      }
      else if(actionCommand.equals("APPLY")){
      }
      else if(actionCommand.equals("HELP")){
      }
    }
  }
}

