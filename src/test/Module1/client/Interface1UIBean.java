
package test.Module1.client;

import java.awt.*;
import java.awt.event.*;

import com.borland.dx.dataset.*;

/**
* Template File
*   InterfaceUIBean.java.template
* IDL Object
*   Module1.Interface1
* Generation Date
*   1999년 7월 28일 수요일 04시24분51초 오후
* IDL Source File
*   C:/WORK/ant/source/test/untitled1.idl
* Abstract
*   Provides an user interface for a particular IDL interface.
*/

public class Interface1UIBean extends javax.swing.JPanel {
  private java.util.ResourceBundle res = java.util.ResourceBundle.getBundle("test.Module1.client.ClientResources");
  private GridBagLayout gridBagLayout1 = new GridBagLayout();
  private GridLayout gridLayout1 = new GridLayout(1, 2, 3, 0);
  private boolean bOrbInitialized;
  protected Interface1Bean interface1Bean1;

  public Interface1_operation1_UIBean methodOperation1 = new Interface1_operation1_UIBean();

  public Interface1UIBean() {
    try  {
      jbInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    bOrbInitialized = false;
    this.setLayout(gridBagLayout1);

    this.add(methodOperation1,
      new java.awt.GridBagConstraints(1, 1, 2, 1, 1.0, 1.0,
        java.awt.GridBagConstraints.NORTH, java.awt.GridBagConstraints.HORIZONTAL,
        new Insets(3, 3, 3, 3), 0, 0));
  }

  public boolean init(Interface1Bean ref) {
    interface1Bean1 = ref;
    if ((bOrbInitialized = interface1Bean1.init()) == true) {
      setCorbaObject(ref);
    }
    return bOrbInitialized;
  }

  public void setCorbaObject(Interface1Bean ref) {
    interface1Bean1 = ref;
    bOrbInitialized = true;
    methodOperation1.setCorbaObject(ref);
  }

  public Module1.Interface1 getCorbaInterface() {
    return interface1Bean1.getCorbaInterface();
  }

  public String getMyCorbaName() {
    if (bOrbInitialized) {
      return interface1Bean1.getMyCorbaName();
    }
    return null;
  }
}











