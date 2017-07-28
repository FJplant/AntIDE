
package test.Module1.server;

import java.sql.*;
import java.util.*;
import java.math.*;


/**
* Template File
*   InterfaceServerImpl.java.template
* IDL Object
*   Module1.Interface1
* Generation Date
*   1999년 7월 28일 수요일 04시24분51초 오후
* IDL Source File
*   C:/WORK/ant/source/test/untitled1.idl
* Abstract
*   Provides default implementation for the server side of a CORBA interface.
*/

public class Interface1Impl extends Module1._Interface1ImplBase {
  public static ServerMonitorInterface monitor = null;

  private void init() {
    if (monitor == null) {
      monitor = ServerMonitor.addPage(this, "Interface1");
      monitor.showObjectCounter(true);
    }
    monitor.updateObjectCounter(1);

  }

  public Interface1Impl(java.lang.String name, java.lang.String creationParameters) {
    super(name);
    init();
  }

  public Interface1Impl(java.lang.String name) {
    super(name);
    init();
  }

  public Interface1Impl() {
    super("Interface1");
    init();
  }

  public String operation1(int param1, float param2)  {
    ServerMonitor.log("(" + _object_name() + ") Interface1Impl.java operation1()");
    return "";
  }
}











