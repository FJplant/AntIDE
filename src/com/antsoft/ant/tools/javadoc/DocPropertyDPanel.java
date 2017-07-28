/**
 * Javadoc Option 설정 Tabbed Panel;
 *
 * @author Lee Chul-Mok.
 */

package com.antsoft.ant.tools.javadoc;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.io.*;

import com.antsoft.ant.util.BorderList;
import com.antsoft.ant.util.FontList;
import com.antsoft.ant.property.*;

public class DocPropertyDPanel extends JPanel {
  private JFrame parent;
  private JTextField windowtitleFld;
  private JTextArea doctitleTa, headerTa, footerTa, bottomTa;
  private JLabel windowtitleLbl, doctitleLbl, headerLbl, footerLbl, bottomLbl;
  public static IdeProperty property;
  
  public DocPropertyDPanel(JFrame parent) {
    this.parent = parent;
    property = IdePropertyManager.loadProperty();
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  void jbInit() throws Exception {

    setLayout(new BorderLayout());
    setBorder(BorderList.etchedBorder5);

    // windowtitle panel
    windowtitleLbl = new JLabel("-windowtitle <text>");
    windowtitleLbl.setForeground(Color.black);
    windowtitleFld = new JTextField(20);
//    windowtitleFld.setPreferredSize(new Dimension(200,15));
    windowtitleFld.setText(property.getWindowtitleDoc());
    JPanel windowtitleP = new JPanel(new BorderLayout());
    windowtitleP.add(windowtitleLbl,BorderLayout.NORTH);
    windowtitleP.add(windowtitleFld,BorderLayout.CENTER);
    JPanel emP = new JPanel();
    emP.setPreferredSize(new Dimension(200,50));
    windowtitleP.add(emP, BorderLayout.SOUTH);

    JPanel textP = new JPanel(new BorderLayout());
    textP.add(windowtitleP, BorderLayout.CENTER);
    textP.add(new JPanel(),BorderLayout.EAST);
    textP.add(new JPanel(),BorderLayout.WEST);
    textP.add(new JPanel(),BorderLayout.SOUTH);
    textP.setBorder(BorderList.textBorder);

    // doctitle panel
    doctitleLbl = new JLabel("-doctitle <html>");
    doctitleTa = new JTextArea(2,20);
    doctitleTa.setText(convertString(property.getDoctitleDoc()));

    JPanel doctitleTaP = new JPanel(new BorderLayout());
    doctitleTaP.add(doctitleTa, BorderLayout.CENTER);
    doctitleTaP.setBorder(BorderList.lightLoweredBorder);
    JScrollPane pane1 = new JScrollPane(doctitleTaP);

    JPanel doctitleP = new JPanel(new BorderLayout());
    doctitleP.add(doctitleLbl,BorderLayout.NORTH);
    doctitleP.add(pane1,BorderLayout.CENTER);

    // header panel
    headerLbl = new JLabel("-header <html>");
    headerTa = new JTextArea(2,20);
    headerTa.setText(convertString(property.getHeaderDoc()));

    JPanel headerTaP = new JPanel(new BorderLayout());
    headerTaP.add(headerTa, BorderLayout.CENTER);
    headerTaP.setBorder(BorderList.lightLoweredBorder);
    JScrollPane pane2 = new JScrollPane(headerTaP);

    JPanel headerP = new JPanel(new BorderLayout());
    headerP.add(headerLbl,BorderLayout.NORTH);
    headerP.add(pane2,BorderLayout.CENTER);

    // footer panel
    footerLbl = new JLabel("-footer <html>");
    footerTa = new JTextArea(2,20);
    footerTa.setText(convertString(property.getFooterDoc()));

    JPanel footerTaP = new JPanel(new BorderLayout());
    footerTaP.add(footerTa, BorderLayout.CENTER);
    footerTaP.setBorder(BorderList.lightLoweredBorder);
    JScrollPane pane3 = new JScrollPane(footerTaP);

    JPanel footerP = new JPanel(new BorderLayout());
    footerP.add(footerLbl,BorderLayout.NORTH);
    footerP.add(pane3,BorderLayout.CENTER);

    // bottom panel
    bottomLbl = new JLabel("-bottom <html>");
    bottomTa = new JTextArea(3,20);
    bottomTa.setText(convertString(property.getBottomDoc()));

    JPanel bottomTaP = new JPanel(new BorderLayout());
    bottomTaP.add(bottomTa, BorderLayout.CENTER);
    bottomTaP.setBorder(BorderList.lightLoweredBorder);
    JScrollPane pane4 = new JScrollPane(bottomTaP);

    JPanel bottomP = new JPanel(new BorderLayout());
    bottomP.add(bottomLbl,BorderLayout.NORTH);
    bottomP.add(pane4,BorderLayout.CENTER);

    JPanel htmlP = new JPanel(new GridLayout(4,1));
    htmlP.add(doctitleP);
    htmlP.add(headerP);
    htmlP.add(footerP);
    htmlP.add(bottomP);
//    htmlP.setBorder(BorderList.htmlBorder);

    JPanel p = new JPanel(new BorderLayout());
    p.setBorder(BorderList.htmlBorder);
    p.add(htmlP,BorderLayout.CENTER);
    p.add(new JPanel(),BorderLayout.EAST);
    p.add(new JPanel(),BorderLayout.WEST);
    p.add(new JPanel(),BorderLayout.SOUTH);

    Box box = Box.createVerticalBox();
    box.add(textP);
    box.add(p);

    setBorder(BorderList.etchedBorder5);
    add(box, BorderLayout.CENTER);
    add(new JPanel(), BorderLayout.WEST);
    add(new JPanel(), BorderLayout.EAST);
    add(new JPanel(), BorderLayout.NORTH);
    add(new JPanel(), BorderLayout.SOUTH);
  }

  // option의 값을 넘겨주는 함수들
  public String getWindowtitleDoc() {
    return windowtitleFld.getText();
  }
  public String getDoctitleDoc() {
    return doctitleTa.getText();
  }
  public String getHeaderDoc() {
    return headerTa.getText();
  }
  public String getFooterDoc() {
    return footerTa.getText();
  }
  public String getBottomDoc() {
    return bottomTa.getText();
  }

  private String convertString(String str) {
    StringBuffer buf = new StringBuffer();
    for(int i=0; i<str.length(); i++) {
      if (str.charAt(i) == '*') {
        i++;
        if (str.charAt(i) == '*') {
          i++;
          if(str.charAt(i) == '*') {
            buf.append('\n');
          }
          else {
            buf.append("**" + str.charAt(i));
          }
        }
        else {
          buf.append('*');
          buf.append(str.charAt(i));
        }
      }
      else {
         buf.append(str.charAt(i));
      }
    }

    return buf.toString();
  }




  public void setDisableAllComponent(boolean disable) {
    if(disable) {
      windowtitleLbl.setForeground(Color.gray);
      doctitleLbl.setForeground(Color.gray);
      headerLbl.setForeground(Color.gray);
      footerLbl.setForeground(Color.gray);
      bottomLbl.setForeground(Color.gray);

      windowtitleFld.setEditable(false);
      doctitleTa.setEditable(false);
      headerTa.setEditable(false);
      footerTa.setEditable(false);
      bottomTa.setEditable(false);

      windowtitleFld.setEnabled(false);
      doctitleTa.setEnabled(false);
      headerTa.setEnabled(false);
      footerTa.setEnabled(false);
      bottomTa.setEnabled(false);
    }
    else {
      windowtitleLbl.setForeground(Color.black);
      doctitleLbl.setForeground(Color.black);
      headerLbl.setForeground(Color.black);
      footerLbl.setForeground(Color.black);
      bottomLbl.setForeground(Color.black);

      windowtitleFld.setEditable(true);
      doctitleTa.setEditable(true);
      headerTa.setEditable(true);
      footerTa.setEditable(true);
      bottomTa.setEditable(true);

      windowtitleFld.setEnabled(true);
      doctitleTa.setEnabled(true);
      headerTa.setEnabled(true);
      footerTa.setEnabled(true);
      bottomTa.setEnabled(true);
    }
  }
}