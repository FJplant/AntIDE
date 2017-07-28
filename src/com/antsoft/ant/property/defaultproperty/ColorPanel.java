/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/property/defaultproperty/ColorPanel.java,v 1.3 1999/07/20 09:18:45 lila Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * Author: Kim, Sang Kyun
 * $Revision: 1.3 $
 */
package com.antsoft.ant.property.defaultproperty;
import java.awt.*;import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.util.*;
import java.io.*;
import java.net.*;

import com.antsoft.ant.util.BorderList;import com.antsoft.ant.util.FontList;

public class ColorPanel extends JPanel {  private JComboBox templateCbox;
  private DefaultComboBoxModel templateM;
  private JList itemList;
  private String currentItem = "Keyword";

  private JButton customBtn;  private JTextArea ta;
  private JButton [][] colorBtns = new JButton[4][3];
  private static DefaultListModel listM = new DefaultListModel();
  private static Hashtable buttonHash = new Hashtable();

  private JButton currentSelectedBtn;
  public static final String KEYWORD = "Keyword";  public static final String COMMENT = "Comment";
  public static final String CONSTANT = "Constant";
  public static final String STRING = "String";
  public static final String BACKGROUND = "Background";

  public static final String USERDEFINE = "UserDefine";  public static final String DEFAULTSTYLE = "DefaultStyle";
  public static final String OLDSTYLE = "OldStyle";

  private Color keywordColor, commentColor, constantColor, stringColor, backgroundColor;  private JLabel htmlLbl;
  private String htmlText;
  private String commentBuf, keywordBuf, constantBuf, backgroundBuf, stringBuf;

  static {
    listM.addElement("Keyword");
    listM.addElement("Comment");
    listM.addElement("Constant");
    listM.addElement("String");
    listM.addElement("Background");
  }

  public ColorPanel() {    try  {
      jbInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  void jbInit() throws Exception {    setBorder(BorderList.etchedBorder5);

    ActionListener al = new ButtonEventHandler();
    ActionListener al2 = new ActionEventHandler();
    ListSelectionListener lsl = new ListSelectionHandler();

    initButtons();
    //template    templateM = new DefaultComboBoxModel();
    templateM.addElement(USERDEFINE);
    templateM.addElement(DEFAULTSTYLE);
    templateM.addElement(OLDSTYLE);

    JLabel templateLbl = new JLabel("Template Color Setting");
    templateLbl.setPreferredSize(new Dimension(150,20));

    templateCbox = new JComboBox(templateM);    templateCbox.setPreferredSize(new Dimension(150,20));    templateCbox.setActionCommand("TEMPLATE");
    templateCbox.addActionListener(al2);

    JPanel p1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
    p1.add(templateLbl);
    p1.add(templateCbox);

    //list    itemList = new JList(listM);
    itemList.setSelectedIndex(0);
    itemList.addListSelectionListener(lsl);

    JScrollPane listPane = new JScrollPane(itemList);    listPane.setPreferredSize(new Dimension(140,100));
    //buttonP    JPanel buttonP = new JPanel(new GridLayout(4,3));
    buttonP.setPreferredSize(new Dimension(140,100));

    for(int i=0; i<4; i++){      for(int j=0; j<3; j++){
        colorBtns[i][j].addActionListener(al);
        colorBtns[i][j].setBorder(BorderList.loweredBorder);
        buttonP.add(colorBtns[i][j]);
      }
    }

    //custom button    customBtn = new JButton("");
    customBtn.setPreferredSize(new Dimension(25,25));
    customBtn.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
         Color col = JColorChooser.showDialog(ColorPanel.this, "Select Color", Color.white);
         if(col == null) return;
         setColor(currentItem, col);
         currentSelectedBtn.setBorder(BorderList.loweredBorder);
      }});
    JPanel customP = new JPanel(new BorderLayout());
    JPanel p = new JPanel();
    p.setPreferredSize(new Dimension(25,77));
    customP.add(p,BorderLayout.CENTER);
    customP.add(customBtn,BorderLayout.SOUTH);

    //color setting panel
    JPanel p2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
    TitledBorder border1 = new TitledBorder(new EtchedBorder(),"Color Setting");
    border1.setTitleColor(Color.black);
    border1.setTitleFont(FontList.regularFont);
    p2.setBorder(border1);
    p2.add(listPane);
    p2.add(buttonP);
    p2.add(customP);

    //sample panel    htmlLbl = new JLabel();
    htmlLbl.setPreferredSize(new Dimension(390,90));
    JPanel p3 = new JPanel(new BorderLayout());
    p3.setBorder(BorderList.previewBorder);
    p3.add(htmlLbl,BorderLayout.CENTER);
    p3.add(new JPanel(),BorderLayout.WEST);
    p3.add(new JPanel(),BorderLayout.EAST);
    p3.add(new JPanel(),BorderLayout.SOUTH);

    Box box = Box.createVerticalBox();
    box.add(p1);
    box.add(p2);
    box.add(p3);

    //default setting    setColor(KEYWORD, Color.black);
    setColor(COMMENT, Color.black);
    setColor(CONSTANT, Color.black);
    setColor(STRING, Color.black);
    setColor(BACKGROUND, Color.black);

    currentItem = (String)itemList.getModel().getElementAt(0);    JButton obj = (JButton)buttonHash.get(getColor(currentItem));
    obj.setBorder(BorderList.raisedBorder);
    Color back = obj.getBackground();

    if(back == Color.lightGray || back == Color.darkGray || back == Color.gray){      obj.setForeground(Color.white);
    }
    else{
      obj.setForeground(new Color(255-back.getRed(), 255-back.getGreen(), 255-back.getBlue()));
    }
    obj.setText(currentItem.charAt(0)+"");
    currentSelectedBtn = obj;

    setLayout(new BorderLayout());
    add(box,BorderLayout.CENTER);
    add(new JPanel(),BorderLayout.WEST);
    add(new JPanel(),BorderLayout.EAST);
    //add(new JPanel(),BorderLayout.SOUTH);
  }

  private void initButtons(){
    colorBtns[0][0] = new JButton();
    colorBtns[0][0].setBackground(Color.black);
    buttonHash.put(Color.black, colorBtns[0][0]);

    colorBtns[0][1] = new JButton();
    colorBtns[0][1].setBackground(Color.blue);
    buttonHash.put(Color.blue, colorBtns[0][1]);

    colorBtns[0][2] = new JButton();
    colorBtns[0][2].setBackground(Color.cyan);
    buttonHash.put(Color.cyan, colorBtns[0][2]);

    colorBtns[1][0] = new JButton();
    colorBtns[1][0].setBackground(Color.darkGray);
    buttonHash.put(Color.darkGray, colorBtns[1][0]);

    colorBtns[1][1] = new JButton();
    colorBtns[1][1].setBackground(Color.gray);
    buttonHash.put(Color.gray, colorBtns[1][1]);

    colorBtns[1][2] = new JButton();
    colorBtns[1][2].setBackground(Color.green);
    buttonHash.put(Color.green, colorBtns[1][2]);

    colorBtns[2][0] = new JButton();
    colorBtns[2][0].setBackground(Color.lightGray);
    buttonHash.put(Color.lightGray, colorBtns[2][0]);

    colorBtns[2][1] = new JButton();
    colorBtns[2][1].setBackground(Color.orange);
    buttonHash.put(Color.orange, colorBtns[2][1]);

    colorBtns[2][2] = new JButton();
    colorBtns[2][2].setBackground(Color.pink);
    buttonHash.put(Color.pink, colorBtns[2][2]);

    colorBtns[3][0] = new JButton();
    colorBtns[3][0].setBackground(Color.red);
    buttonHash.put(Color.red, colorBtns[3][0]);

    colorBtns[3][1] = new JButton();
    colorBtns[3][1].setBackground(Color.white);
    buttonHash.put(Color.white, colorBtns[3][1]);

    colorBtns[3][2] = new JButton();
    colorBtns[3][2].setBackground(Color.yellow);
    buttonHash.put(Color.yellow, colorBtns[3][2]);

  }

  private void reloadHtmlText(){
    htmlText = "<html>\n" +
               "<body bgcolor = " + backgroundBuf + ">\n" +
               "<pre>" +
               "\n" +
               commentBuf + " /** main method */    </font>\n" +
               keywordBuf + " public static void</font> " + stringBuf + "main(String args[]) {   </font>\n" +
               "   " + stringBuf + "Frame f = </font>" + keywordBuf + "new</font>" + stringBuf + " Frame(</font>" + constantBuf + "CardTest</font>" +stringBuf + ");   </font>\n" +
               "   " + stringBuf + "CardTest cardTest = </font> " + keywordBuf + "new</font>" +  stringBuf + " CardTest();    </font>\n" +
               "   " + stringBuf + "cardTest.start();   </font>\n" +
               stringBuf + " }   </font>\n" +
               "</pre>";

    htmlLbl.setText(htmlText);
  }

  public Color getColor(String type){
    Color ret = null;
    if(type.equals(this.KEYWORD)) ret = keywordColor;
    else if(type.equals(this.COMMENT)) ret = commentColor;
    else if(type.equals(this.CONSTANT)) ret = constantColor;
    else if(type.equals(this.STRING)) ret = stringColor;
    else if (type.equals(this.BACKGROUND)) ret = backgroundColor;

    return ret;
  }

  public void setColor(String type, Color newColor){
    if(type.equals(this.KEYWORD)) {
      keywordColor = newColor;
      keywordBuf = "<font color=#" + getHexColorStr(newColor.getRed(), newColor.getGreen(), newColor.getBlue()) + ">";
    }
    else if(type.equals(this.COMMENT)) {
      commentColor = newColor;
      commentBuf = "<font color=#" + getHexColorStr(newColor.getRed(), newColor.getGreen(), newColor.getBlue()) + ">";
    }
    else if(type.equals(this.CONSTANT)) {
      constantColor = newColor;
      constantBuf = "<font color=#" + getHexColorStr(newColor.getRed(), newColor.getGreen(), newColor.getBlue()) + ">";
    }
    else if(type.equals(this.STRING)) {
      stringColor = newColor;
      stringBuf = "<font color=#" + getHexColorStr(newColor.getRed(), newColor.getGreen(), newColor.getBlue()) + ">";
    }
    else if (type.equals(this.BACKGROUND)) {
      backgroundColor = newColor;
      backgroundBuf = "#" + getHexColorStr(newColor.getRed(), newColor.getGreen(), newColor.getBlue());
    }

    reloadHtmlText();
  }

  private String getHexColorStr(int ir, int ig, int ib){
    String r = Integer.toHexString(ir);
    String g = Integer.toHexString(ig);
    String b = Integer.toHexString(ib);

    if(r.length() ==1) r = "0"+r;
    if(g.length() ==1) g = "0"+g;
    if(b.length() ==1) b = "0"+b;

    return r+g+b;
  }

  class ButtonEventHandler implements ActionListener{
    public void actionPerformed(ActionEvent e){
      if(currentSelectedBtn != null) {
        currentSelectedBtn.setBorder(BorderList.loweredBorder);
        currentSelectedBtn.setText("");
      }

      JButton obj = (JButton)e.getSource();
      obj.setBorder(BorderList.raisedBorder);

      Color back = obj.getBackground();
      if(back == Color.lightGray || back == Color.darkGray || back == Color.gray){
        obj.setForeground(Color.white);
      }
      else{
        obj.setForeground(new Color(255-back.getRed(), 255-back.getGreen(), 255-back.getBlue()));
      }
        
      obj.setText(currentItem.charAt(0)+"");

      setColor(currentItem, obj.getBackground());
      currentSelectedBtn = obj;
    }
  }

  class ActionEventHandler implements ActionListener{
    public void actionPerformed(ActionEvent e){
      String cmd = e.getActionCommand();

      if(cmd.equals("TEMPLATE")){
        if(currentSelectedBtn != null) currentSelectedBtn.setBorder(BorderList.loweredBorder);
        currentSelectedBtn = null;

        String style = (String)templateM.getElementAt(templateCbox.getSelectedIndex());

        if(style.equals(DEFAULTSTYLE)){
          setColor(KEYWORD, new Color(0, 0, 255));
          setColor(COMMENT, new Color(0, 153, 0));
          setColor(CONSTANT, new Color(255, 0, 0));
          setColor(STRING, new Color(0, 0, 0));
          setColor(BACKGROUND, new Color(255, 255, 255));
        }

        else if(style.equals(OLDSTYLE)){
          setColor(KEYWORD, new Color(255, 255, 0));
          setColor(COMMENT, new Color(192, 192, 192));
          setColor(CONSTANT, new Color(255, 0, 0));
          setColor(STRING, new Color(255, 255, 255));
          setColor(BACKGROUND, new Color(0, 0, 51));
        }

        reloadHtmlText();
      }
    }
  }

  class ListSelectionHandler implements ListSelectionListener{
    public void valueChanged(ListSelectionEvent e){
      String temp = (String)itemList.getSelectedValue();
      if(temp.equals(currentItem)) return;
      else currentItem = temp;


      if(currentSelectedBtn != null){
        currentSelectedBtn.setBorder(BorderList.loweredBorder);
        currentSelectedBtn.setText("");
      }

      JButton b = (JButton)buttonHash.get(getColor(currentItem));
      if(b == null){
        return;
      }
      b.setBorder(BorderList.raisedBorder);
      Color back = b.getBackground();

      if(back == Color.lightGray || back == Color.darkGray || back == Color.gray){
        b.setForeground(Color.white);
      }
      else{
        b.setForeground(new Color(255-back.getRed(), 255-back.getGreen(), 255-back.getBlue()));
      }
      b.setText(currentItem.charAt(0)+"");
      currentSelectedBtn = b;
    }
  }
}



