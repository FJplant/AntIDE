
package test.Module1.client;

import java.awt.*;
import org.omg.CORBA.*;

/**
* Template File
*   InterfaceBean.java.template
* IDL Object
*   Module1.Interface1
* Generation Date
*   1999년 7월 28일 수요일 04시24분51초 오후
* IDL Source File
*   C:/WORK/ant/source/test/untitled1.idl
* Abstract
*   Wraps up CORBA services for the client side of an interface.
*/

public class Interface1Bean {
  private java.util.ResourceBundle res = java.util.ResourceBundle.getBundle("test.Module1.client.ClientResources");
  private boolean bInitialized = false;
  private Module1.Interface1 _interface1;
  private String name = "Interface1";

  public Interface1Bean() {
    try  {
      jbInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
  }

  public void setName(String n) {
    name = n;
  }

  public String getName() {
    return name;
  }

  public boolean init() {
    if (!bInitialized) {
      try {
        org.omg.CORBA.ORB orb = test.Module1ClientApp.getORB();
        _interface1 = Module1.Interface1Helper.bind(orb, name);
        bInitialized = true;
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
    }
    return bInitialized;
  }

  public void setCorbaInterface(Module1.Interface1 ref) {
    _interface1 = ref;
    bInitialized = true;
  }

  public Module1.Interface1 getCorbaInterface() {
    if (bInitialized) {
      return _interface1;
    }
    return null;
  }

  public String getMyCorbaName() {
    if (bInitialized) {
      return _interface1._object_name();
    }
    return null;
  }

  public String operation1(int param1, float param2)  {
    init();
    return _interface1.operation1(param1, param2);
  }
}

