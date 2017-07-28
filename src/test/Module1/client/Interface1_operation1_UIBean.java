
package test.Module1.client;

import java.awt.*;
import java.awt.event.*;

import com.borland.dx.dataset.*;

/**
* Template File
*   MethodUIBean.java.template
* IDL Object
*   Module1.Interface1.operation1()
* Generation Date
*   1999년 7월 28일 수요일 04시24분52초 오후
* IDL Source File
*   C:/WORK/ant/source/test/untitled1.idl
* Abstract
*   Provides an user interface a particular IDL method.  Entry fields are
*   provided for parameters and a pushbutton to execute the method.
* Description
*   Method parameters which are primitive data types are defined here.  Other
*   data types have a separate UI bean.  A pushbutton is provided to execute
*   the method.  If a primitive data type is returned by the method, a readonly
*   field is generated to the right of the button to display that result.
*   After the method is executed, an event is fired which contains the value of
*   all parameters and any return result.  Event listeners are ClientPanel.java
*   and the UI bean for the interface.  ClientPanel.java has default
*   implementation to display some types of return result including structures
*   and interfaces.
*/

public class Interface1_operation1_UIBean extends javax.swing.JPanel {
  private java.util.ResourceBundle res = java.util.ResourceBundle.getBundle("test.Module1.client.ClientResources");
  private GridBagLayout gridBagLayout1 = new GridBagLayout();
  private GridLayout gridLayout1 = new GridLayout(1, 2, 3, 0);
  protected Interface1Bean interface1Bean1;
  private javax.swing.JPanel panelParam1 = new javax.swing.JPanel();
  private javax.swing.JLabel labelParam1 = new javax.swing.JLabel();
  private com.borland.dbswing.JdbTextField fieldParam1 = new com.borland.dbswing.JdbTextField();
  private javax.swing.JPanel panelParam2 = new javax.swing.JPanel();
  private javax.swing.JLabel labelParam2 = new javax.swing.JLabel();
  private com.borland.dbswing.JdbTextField fieldParam2 = new com.borland.dbswing.JdbTextField();
  private transient java.util.Vector OpExecutedListenersOperation1;
  private javax.swing.JButton buttonOperation1 = new javax.swing.JButton();
  private GridLayout gridLayout2 = new GridLayout();
  private javax.swing.JPanel panel1 = new javax.swing.JPanel();
  private com.borland.dbswing.JdbTextField fieldResultOperation1 = new com.borland.dbswing.JdbTextField();

  public Interface1_operation1_UIBean() {
    try  {
      jbInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    this.setLayout(gridBagLayout1);

    labelParam1.setText("Param1");
    labelParam1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    panelParam1.setLayout(gridLayout1);
    panelParam1.add(labelParam1,
      new java.awt.GridBagConstraints(1, 1, 1, 1, 1.0, 1.0,
        java.awt.GridBagConstraints.NORTH, java.awt.GridBagConstraints.HORIZONTAL,
        new Insets(3, 3, 3, 3), 0, 0));
    panelParam1.add(fieldParam1,
      new java.awt.GridBagConstraints(2, 1, 1, 1, 1.0, 1.0,
        java.awt.GridBagConstraints.NORTH, java.awt.GridBagConstraints.HORIZONTAL,
        new Insets(3, 3, 3, 3), 0, 0));
    this.add(panelParam1,
      new java.awt.GridBagConstraints(1, 1, 2, 1, 1.0, 1.0,
        java.awt.GridBagConstraints.NORTH, java.awt.GridBagConstraints.HORIZONTAL,
        new Insets(3, 0, 3, 3), 0, 0));

    labelParam2.setText("Param2");
    labelParam2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    panelParam2.setLayout(gridLayout1);
    panelParam2.add(labelParam2,
      new java.awt.GridBagConstraints(1, 1, 1, 1, 1.0, 1.0,
        java.awt.GridBagConstraints.NORTH, java.awt.GridBagConstraints.HORIZONTAL,
        new Insets(3, 3, 3, 3), 0, 0));
    panelParam2.add(fieldParam2,
      new java.awt.GridBagConstraints(2, 1, 1, 1, 1.0, 1.0,
        java.awt.GridBagConstraints.NORTH, java.awt.GridBagConstraints.HORIZONTAL,
        new Insets(3, 3, 3, 3), 0, 0));
    this.add(panelParam2,
      new java.awt.GridBagConstraints(1, 2, 2, 1, 1.0, 1.0,
        java.awt.GridBagConstraints.NORTH, java.awt.GridBagConstraints.HORIZONTAL,
        new Insets(3, 0, 3, 3), 0, 0));
    buttonOperation1.setText("Operation1");
    buttonOperation1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
    buttonOperation1_actionPerformed(e);
       }
     });
    panel1.setLayout(gridLayout2);
    panel1.add(buttonOperation1,
      new java.awt.GridBagConstraints(1, 1, 1, 1, 1.0, 1.0,
        java.awt.GridBagConstraints.NORTH, java.awt.GridBagConstraints.HORIZONTAL,
        new Insets(3, 3, 3, 3), 0, 0));
    panel1.add(fieldResultOperation1,
      new java.awt.GridBagConstraints(2, 1, 1, 1, 1.0, 1.0,
        java.awt.GridBagConstraints.NORTH, java.awt.GridBagConstraints.HORIZONTAL,
        new Insets(3, 0, 3, 3), 0, 0));
    this.add(panel1,
      new java.awt.GridBagConstraints(1, 3, 2, 1, 1.0, 1.0,
        java.awt.GridBagConstraints.NORTH, java.awt.GridBagConstraints.HORIZONTAL,
        new Insets(3, 0, 3, 3), 0, 0));
    fieldResultOperation1.setEnabled(false);
  }

  public void setCorbaObject(Interface1Bean ref) {
    interface1Bean1 = ref;
  }

  public Module1.Interface1 getCorbaInterface() {
    return interface1Bean1.getCorbaInterface();
  }

  public int getParameterParam1() {
    String strNew = fieldParam1.getText();
    return Integer.valueOf(strNew).intValue();
  }

  public void setParameterParam1(int var) {
    fieldParam1.setText(String.valueOf(var));
  }

  public float getParameterParam2() {
    String strNew = fieldParam2.getText();
    return Float.valueOf(strNew).floatValue();
  }

  public void setParameterParam2(float var) {
    fieldParam2.setText(String.valueOf(var));
  }

  public synchronized void removeOpExecutedListener(Interface1_operation1_OpExecutedListener l) {
    if ((OpExecutedListenersOperation1 != null) && OpExecutedListenersOperation1.contains(l)) {
      java.util.Vector v = (java.util.Vector) OpExecutedListenersOperation1.clone();
      v.removeElement(l);
      OpExecutedListenersOperation1 = v;
    }
  }

  public synchronized void addOpExecutedListener(Interface1_operation1_OpExecutedListener l) {
    java.util.Vector v = (OpExecutedListenersOperation1 == null) ? new java.util.Vector(2) : (java.util.Vector) OpExecutedListenersOperation1.clone();
    if (!v.contains(l)) {
      v.addElement(l);
      OpExecutedListenersOperation1 = v;
    }
  }

  protected void fireOpExecuted(Interface1_operation1_OpExecutedEvent e) {
    if (OpExecutedListenersOperation1 != null) {
      java.util.Vector listeners = OpExecutedListenersOperation1;
      int count = listeners.size();
      for (int i = 0; i < count; i++) {
        ((Interface1_operation1_OpExecutedListener) listeners.elementAt(i)).OpExecuted(e);
      }
    }
  }

  private void buttonOperation1_actionPerformed(ActionEvent e)  {
    Interface1_operation1_OpExecutedEvent opEvent = new Interface1_operation1_OpExecutedEvent(this);
    try {
      opEvent.parameterParam1 = getParameterParam1();
    }
    catch (Exception ex) {
      getToolkit().beep();
      return;
    }
    try {
      opEvent.parameterParam2 = getParameterParam2();
    }
    catch (Exception ex) {
      getToolkit().beep();
      return;
    }
    opEvent.result = interface1Bean1.operation1(
      opEvent.parameterParam1
      ,opEvent.parameterParam2
      );
    fieldResultOperation1.setText(opEvent.result);
    fireOpExecuted(opEvent);
  }
}


