
package test.Module1.server;

import java.awt.*;

/**
* Template File
*   ServerMonitorPage.java.template
* IDL Object
*   Module1
* Generation Date
*   1999년 7월 28일 수요일 04시24분53초 오후
* IDL Source File
*   C:/WORK/ant/source/test/untitled1.idl
* Abstract
*   Implements a Server Monitor page to display interface counters.
*/

public class ServerMonitorPage extends javax.swing.JPanel implements ServerMonitorInterface {
  private java.util.ResourceBundle res = java.util.ResourceBundle.getBundle("test.Module1.server.ServerResources");
  private GridBagLayout gridBagLayout1 = new GridBagLayout();
  private GridLayout gridLayout1 = new GridLayout(1, 2, 3, 0);
  private javax.swing.JPanel panelOuter1 = new javax.swing.JPanel();

  private javax.swing.JPanel panelObjects1 = new javax.swing.JPanel();
  private javax.swing.JLabel labelObjects1 = new javax.swing.JLabel();
  private com.borland.dbswing.JdbTextField textObjects1 = new com.borland.dbswing.JdbTextField();
  private int objectsCounter = 0;

  private javax.swing.JPanel panelReads1 = new javax.swing.JPanel();
  private javax.swing.JLabel labelReads1 = new javax.swing.JLabel();
  private com.borland.dbswing.JdbTextField textReads1 = new com.borland.dbswing.JdbTextField();
  private int readsCounter = 0;

  private javax.swing.JPanel panelWrites1 = new javax.swing.JPanel();
  private javax.swing.JLabel labelWrites1 = new javax.swing.JLabel();
  private com.borland.dbswing.JdbTextField textWrites1 = new com.borland.dbswing.JdbTextField();
  private int writesCounter = 0;

  private Object monitoredObject;


  public ServerMonitorPage(Object obj) {
    monitoredObject = obj;
    try {
      jbInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private void jbInit() throws Exception{
    panelOuter1.setLayout(gridBagLayout1);
    panelObjects1.setLayout(gridLayout1);
    textObjects1.setEnabled(false);
    labelObjects1.setText(res.getString("numberObjects"));
    labelObjects1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    panelObjects1.setVisible(false);
    panelObjects1.add(labelObjects1);
    panelObjects1.add(textObjects1);
    panelOuter1.add(panelObjects1,
      new java.awt.GridBagConstraints(1, 2, 2, 1, 1.0, 1.0,
        java.awt.GridBagConstraints.NORTH, java.awt.GridBagConstraints.HORIZONTAL,
        new Insets(3, 0, 3, 3), 0, 0));

    panelReads1.setLayout(gridLayout1);
    textReads1.setEnabled(false);
    labelReads1.setText(res.getString("numberRead"));
    labelReads1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    panelReads1.setVisible(false);
    panelReads1.add(labelReads1);
    panelReads1.add(textReads1);
    panelOuter1.add(panelReads1,
      new java.awt.GridBagConstraints(1, 3, 2, 1, 1.0, 1.0,
        java.awt.GridBagConstraints.NORTH, java.awt.GridBagConstraints.HORIZONTAL,
        new Insets(3, 0, 3, 3), 0, 0));

    panelWrites1.setLayout(gridLayout1);
    textWrites1.setEnabled(false);
    labelWrites1.setText(res.getString("numberWritten"));
    labelWrites1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    panelWrites1.setVisible(false);
    panelWrites1.add(labelWrites1);
    panelWrites1.add(textWrites1);
    panelOuter1.add(panelWrites1,
      new java.awt.GridBagConstraints(1, 4, 2, 1, 1.0, 1.0,
        java.awt.GridBagConstraints.NORTH, java.awt.GridBagConstraints.HORIZONTAL,
        new Insets(3, 0, 3, 3), 0, 0));

    add(panelOuter1);
  }

  public void showObjectCounter(boolean bVisible) {
    panelObjects1.setVisible(bVisible);
    refresh();
  }

  public void showReadCounter(boolean bVisible) {
    panelReads1.setVisible(bVisible);
    refresh();
  }

  public void showWriteCounter(boolean bVisible) {
    panelWrites1.setVisible(bVisible);
    refresh();
  }

  public synchronized void updateObjectCounter(int n) {
    objectsCounter += n;
    textObjects1.setText(String.valueOf(objectsCounter));
  }

  public synchronized void updateReadCounter(int n) {
    readsCounter += n;
    textReads1.setText(String.valueOf(readsCounter));
  }

  public synchronized void updateWriteCounter(int n) {
    writesCounter += n;
    textWrites1.setText(String.valueOf(writesCounter));
  }

  public void refresh() {
    textObjects1.setText(String.valueOf(objectsCounter));
    textReads1.setText(String.valueOf(readsCounter));
    textWrites1.setText(String.valueOf(writesCounter));
  }
}
