
package test.Module1.client;

import java.awt.*;
import java.awt.event.*;

/**
* Template File
*   ClientAboutBoxDialog.java.template
* IDL Object
*   Module1
* Generation Date
*   1999년 7월 28일 수요일 04시24분47초 오후
* IDL Source File
*   C:/WORK/ant/source/test/untitled1.idl
* Abstract
*   Implements the client Help About dialog.
*/

public class ClientAboutBoxDialog extends Dialog implements ActionListener {
  java.util.ResourceBundle res = java.util.ResourceBundle.getBundle("test.Module1.client.ClientResources");
  javax.swing.JPanel panel1 = new javax.swing.JPanel();
  javax.swing.JPanel panel2 = new javax.swing.JPanel();
  javax.swing.JPanel insetsPanel1 = new javax.swing.JPanel();
  javax.swing.JPanel insetsPanel2 = new javax.swing.JPanel();
  javax.swing.JPanel insetsPanel3 = new javax.swing.JPanel();
  javax.swing.JButton button1 = new javax.swing.JButton();
  com.borland.dbswing.JdbLabel imageControl1 = new com.borland.dbswing.JdbLabel();
  javax.swing.JLabel label1 = new javax.swing.JLabel();
  javax.swing.JLabel label2 = new javax.swing.JLabel();
  javax.swing.JLabel label3 = new javax.swing.JLabel();
  javax.swing.JLabel label4 = new javax.swing.JLabel();
  BorderLayout borderLayout1 = new BorderLayout();
  BorderLayout borderLayout2 = new BorderLayout();
  FlowLayout flowLayout1 = new FlowLayout();
  FlowLayout flowLayout2 = new FlowLayout();
  GridLayout gridLayout1 = new GridLayout();

  String graphicPath = "";
  String product = "Module1";
  String version;
  String copyright;
  String comments;

  public ClientAboutBoxDialog(Frame parent) {
    super(parent);
    enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    try  {
      jbInit();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    pack();
  }

  private void jbInit() throws Exception  {
    version = res.getString("Version");
    copyright = res.getString("Copyright");
    comments = res.getString("Comments");
    this.setTitle(res.getString("About"));
    setResizable(false);
    panel1.setLayout(borderLayout1);
    panel2.setLayout(borderLayout2);
    insetsPanel1.setLayout(flowLayout1);
    insetsPanel2.setLayout(flowLayout1);
    gridLayout1.setRows(4);
    gridLayout1.setColumns(1);
    label1.setText(product);
    label2.setText(version);
    label3.setText(copyright);
    label4.setText(comments);
    insetsPanel3.setLayout(gridLayout1);
    button1.setText(res.getString("OK"));
    button1.addActionListener(this);
    imageControl1.setIcon(new javax.swing.ImageIcon(graphicPath));
    insetsPanel2.add(imageControl1, null);
    panel2.add(insetsPanel2, BorderLayout.WEST);
    this.add(panel1, null);
    insetsPanel3.add(label1, null);
    insetsPanel3.add(label2, null);
    insetsPanel3.add(label3, null);
    insetsPanel3.add(label4, null);
    panel2.add(insetsPanel3, BorderLayout.CENTER);
    insetsPanel1.add(button1, null);
    panel1.add(insetsPanel1, BorderLayout.SOUTH);
    panel1.add(panel2, BorderLayout.NORTH);
  }

  protected void processWindowEvent(WindowEvent e) {
    if (e.getID() == WindowEvent.WINDOW_CLOSING) {
      cancel();
    }
    super.processWindowEvent(e);
  }

  void cancel() {
    dispose();
  }

  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == button1) {
      cancel();
    }
  }
}

