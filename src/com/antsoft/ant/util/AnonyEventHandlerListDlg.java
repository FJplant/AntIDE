/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/util/AnonyEventHandlerListDlg.java,v 1.9 1999/08/31 12:26:34 multipia Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.9 $
 */
package com.antsoft.ant.util;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.zip.*;
import java.lang.reflect.*;

import com.antsoft.ant.main.MainFrame;
import com.antsoft.ant.property.IdePropertyManager;
import com.antsoft.ant.util.QuickSorter;
import com.antsoft.ant.pool.packagepool.PackagePool;
import com.antsoft.ant.pool.classpool.ClassPool;
/**
 *  class EventHandlerList
 *
 *  @author Kim sang kyun
 */
public class AnonyEventHandlerListDlg extends JDialog {
  private JList awtList, swingList;
  private JButton okBtn, cancelBtn;
  private JCheckBox beautifyCbx;
  private Box methodBox;
  private JScrollPane methodPane;

  private Hashtable model = new Hashtable();
  private FileClassLoader loader = new FileClassLoader();
  private MyCellRenderer awtR = new MyCellRenderer();
  private MyCellRenderer swingR = new MyCellRenderer();

  private boolean isAwt = true;
  private String currSel;
  private StringBuffer source = new StringBuffer();;
  private boolean isOK = false;

  public static final String AWT_EVENT_PACKAGE = "java.awt.event";
  public static final String SWING_EVENT_PACKAGE = "javax.swing.event";

  public AnonyEventHandlerListDlg(){
    super(MainFrame.mainFrame, "Event Listeners", true);

		// register window event handler
		addWindowListener(WindowDisposer.getDisposer());
		addKeyListener(WindowDisposer.getDisposer()); 		

    ActionListener al = new ActionEventHandler();
    MouseListener ml = new MouseEventHandler();
    ListSelectionListener lsl = new ListSelectionHandler();

    awtList = new JList();
    awtList.setCellRenderer(awtR);
    awtList.addMouseListener(ml);
    awtList.addListSelectionListener(lsl);

    swingList = new JList();
    swingList.setCellRenderer(swingR);
    swingList.addMouseListener(ml);
    swingList.addListSelectionListener(lsl);

    JPanel labelP = new JPanel(new GridLayout(1,2, 10, 0));
    JLabel awtLbl = new JLabel("AWT", SwingConstants.CENTER);
    JLabel swingLbl = new JLabel("SWING", SwingConstants.CENTER);
    labelP.add(awtLbl);
    labelP.add(swingLbl);

    JPanel listP = new JPanel(new GridLayout(1,2, 10, 0));
    listP.add(new JScrollPane(awtList));
    listP.add(new JScrollPane(swingList));

    JPanel listWrapP = new JPanel(new BorderLayout());
    listWrapP.add(labelP, BorderLayout.NORTH);
    listWrapP.add(listP, BorderLayout.CENTER);

    okBtn = new JButton("OK");
    okBtn.setActionCommand("OK");
    okBtn.addActionListener(al);

    cancelBtn = new JButton("Cancel");
    cancelBtn.setActionCommand("CANCEL");
    cancelBtn.addActionListener(al);

    JPanel choiceP = new JPanel(new FlowLayout(FlowLayout.CENTER));
    beautifyCbx = new JCheckBox("Code beautify after source generation");
    beautifyCbx.setSelected(true);
    choiceP.add(beautifyCbx);

    JPanel btnP = new JPanel(new GridLayout(1,2, 10, 0));
    btnP.add(okBtn);
    btnP.add(cancelBtn);

    JPanel btnRapP = new JPanel(new BorderLayout());
    btnRapP.add(choiceP, BorderLayout.NORTH);
    btnRapP.add(btnP, BorderLayout.CENTER);

    JPanel centerP = new JPanel(new BorderLayout());
    centerP.add(listWrapP, BorderLayout.CENTER);
    centerP.add(btnRapP, BorderLayout.SOUTH);

    methodBox = Box.createVerticalBox();
    methodPane = new JScrollPane(methodBox);
    methodPane.setPreferredSize(new Dimension(380, 180));

    getContentPane().add(centerP, BorderLayout.CENTER);
    getContentPane().add(methodPane, BorderLayout.SOUTH);
    setSize(380, 480);

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension dlgSize = getSize();
    if (dlgSize.height > screenSize.height) dlgSize.height = screenSize.height;
    if (dlgSize.width > screenSize.width)   dlgSize.width = screenSize.width;
    setLocation((screenSize.width - dlgSize.width) / 2, (screenSize.height - dlgSize.height) / 2);
  }


  public String getGeneratedSource()
  {
    return source.toString();
  }

  public boolean isBeautifyOn(){
    return beautifyCbx.isSelected();
  }

  public boolean isOK(){
    return isOK;
  }

  public void showWindow(){
    setData();
    setVisible(true);
  }

  private void setMethodBoxEnabled(boolean isEnabled){
    for(int i=0; i<methodBox.getComponentCount(); i++){
      JCheckBox box = (JCheckBox)methodBox.getComponent(i);
      box.setEnabled(isEnabled);
    }
  }

  private void setData(){
    if(model.isEmpty()) extract();

    Vector awt = new Vector(15, 5);
    Vector swing = new Vector(15, 5);

    for(Enumeration e = model.keys(); e.hasMoreElements(); )
    {
      String element = (String)e.nextElement();
      if(element.endsWith("Listener"))
      {
        if(element.startsWith("java.awt.event")) awt.addElement(element.substring(15));
        else if(element.startsWith("javax.swing.event")) swing.addElement(element.substring(18));
      }
    }
    awtList.setListData(getSortedNames(awt));
    swingList.setListData(getSortedNames(swing));
  }

  private void extract(){
    Vector awt = PackagePool.getEventClassesFromPackage(AWT_EVENT_PACKAGE);
    Vector swing = PackagePool.getEventClassesFromPackage(SWING_EVENT_PACKAGE);

    if(awt != null) {
      int size = awt.size();
      for(int i=0; i<size; i++){
        String fullClassName = AWT_EVENT_PACKAGE + "." + (String)awt.elementAt(i);
        Class obj = ClassPool.getClassInstance(fullClassName);
        model.put(fullClassName, obj);
      }
    }

    if(swing != null) {
      int size = swing.size();
      for(int i=0; i<size; i++){
        String fullClassName = SWING_EVENT_PACKAGE + "." + (String)swing.elementAt(i);
        Class obj = ClassPool.getClassInstance(fullClassName);
        model.put(fullClassName, obj);
      }
    }
  }

  private void okSelected(){

    Class instance = (Class)model.get(currSel);
    String gap = "\t";
    String listenerName = currSel.substring(currSel.lastIndexOf(".")+1);
    String adapter = currSel.substring(0, currSel.indexOf("Listener")) + "Adapter";

    if(model.get(adapter) == null) source.append(" new " + listenerName + "() {" + "\n");
    else source.append(" new " + adapter.substring(adapter.lastIndexOf(".")+1) + "() {" + "\n");

    for(int i=0; i<methodBox.getComponentCount(); i++)
    {
       JCheckBox cbx = (JCheckBox)methodBox.getComponent(i);
       if(cbx.isSelected())
       {
         source.append(gap + gap + cbx.getText() + " {" + "\n");
         source.append(gap + gap + gap + "//TO DO (implementation here)" +"\n");
         source.append(gap + gap + "}\n");
       }

       if(i != methodBox.getComponentCount()-1) source.append("\n");
    }
    source.append(gap + "});" + "\n");
    isOK = true;

  }

  class ActionEventHandler implements ActionListener{
    public void actionPerformed(ActionEvent e){
      String cmd = e.getActionCommand();

      if(cmd.equals("OK"))
      {
        okSelected();
        dispose();
      }

      else if(cmd.equals("CANCEL"))
      {
        isOK = false;
        dispose();
      }
    }
  }

  class ListSelectionHandler implements ListSelectionListener{

    private void changeMethodList(String item){

      Class instance = (Class)model.get(item);
      Method [] methods = instance.getMethods();
      String adapter = item.substring(0, item.indexOf("Listener")) + "Adapter";

      methodBox.removeAll();
      for(int i=0; i<methods.length; i++)
      {
        String paramStr = "";
        Class [] params = methods[i].getParameterTypes();

        for(int j=0; j<params.length; j++)
        {
           paramStr = params[j].getName().substring(params[j].getName().lastIndexOf(".")+1) + " e";
           if(j != params.length-1) paramStr += ", ";
        }
        String str = "public " + "void " + methods[i].getName() + "( " + paramStr + " )";
        JCheckBox box = new JCheckBox(str, false);


        if(model.get(adapter) != null) box.setEnabled(true);
        else {
          box.setSelected(true);
          box.setEnabled(false);
        }  

        methodBox.add(box);
      }
      methodBox.validate();
      methodBox.repaint();
    }

    public void valueChanged(ListSelectionEvent e){

      if(e.getSource() == awtList)
      {
        awtR.makeSelect();
        swingR.makeUnselect();
        swingList.repaint();

        isAwt = true;
        currSel = "java.awt.event." + (String)awtList.getSelectedValue();
        changeMethodList(currSel);
      }

      else if(e.getSource() == swingList)
      {
        swingR.makeSelect();
        awtR.makeUnselect();
        awtList.repaint();

        isAwt = false;
        currSel = "javax.swing.event." + (String)swingList.getSelectedValue();
        changeMethodList(currSel);
      }
    }
  }

  class MouseEventHandler extends MouseAdapter{
    public void mouseClicked(MouseEvent e){
      if(e.getClickCount() == 2)  {
        okSelected();
        dispose();
      }
    }
  }

  public Vector getSortedNames(Vector unSorted) {
    return QuickSorter.sort(unSorted, QuickSorter.LESS_STRING);
  }

  class MyCellRenderer extends JLabel implements ListCellRenderer {
     public Color sfc = Color.white;
     public Color sbc = ColorList.darkBlue;
     public Color dfc = Color.black;
     private Color dbc = Color.white;

     private JLabel left, center, right;
     private StringTokenizer st;

     public MyCellRenderer() {
       setOpaque(true);
       this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
       this.setFont(new Font("DialogInput", Font.PLAIN, 12));
     }

     public void makeUnselect(){
       sfc = Color.black;
       sbc = Color.white;
       dfc = Color.black;
       dbc = Color.white;
     }

     public void makeSelect(){
       sfc = Color.white;
       sbc = ColorList.darkBlue;
       dfc = Color.black;
       dbc = Color.white;
     }

     public Component getListCellRendererComponent(
         JList list,
         Object value,
         int index,
         boolean isSelected,
         boolean cellHasFocus)
     {

         setText(" "+(String)value);
         this.setBackground(isSelected ? sbc : dbc);
         this.setForeground(isSelected ? sfc : dfc);
         return this;
     }
  }
}
