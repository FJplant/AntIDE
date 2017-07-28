
package test.Module1.server;

import java.awt.*;
import java.awt.event.*;

/**
* Template File
*   ServerFrame.java.template
* IDL Object
*   Module1
* Generation Date
*   1999년 7월 28일 수요일 04시24분53초 오후
* IDL Source File
*   C:/WORK/ant/source/test/untitled1.idl
* Abstract
*   Server application frame which is the container for the Server Monitor.
*/

public class ServerFrame extends javax.swing.JFrame {

  private BorderLayout borderLayout1 = new BorderLayout();
  private ServerMonitor serverMonitor = new ServerMonitor();

  public ServerFrame() {
    enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    try  {
      jbInit();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception  {
    this.getContentPane().setLayout(borderLayout1);
    this.setSize(new Dimension(600, 300));
    this.setTitle("Module1 server");
    this.getContentPane().add(serverMonitor);
  }

  protected void processWindowEvent(WindowEvent e) {
    super.processWindowEvent(e);
    if (e.getID() == WindowEvent.WINDOW_CLOSING) {
      System.exit(0);
    }
  }
}

