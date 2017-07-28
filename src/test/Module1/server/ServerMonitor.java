
package test.Module1.server;

import java.awt.*;
import java.text.*;

/**
* Template File
*   ServerMonitor.java.template
* IDL Object
*   Module1
* Generation Date
*   1999년 7월 28일 수요일 04시24분53초 오후
* IDL Source File
*   C:/WORK/ant/source/test/untitled1.idl
* Abstract
*   Maintains the server log and is the container for all the Server Monitor pages.
*/

public class ServerMonitor extends javax.swing.JPanel {
  private static com.borland.dbswing.JdbTextArea myLog = null;
  private static ServerMonitor monitor;
  private java.util.ResourceBundle res = java.util.ResourceBundle.getBundle("test.Module1.server.ServerResources");
  private BorderLayout borderLayout1 = new BorderLayout();
  private BorderLayout borderLayout2 = new BorderLayout();
  private GridBagLayout gridBagLayout1 = new GridBagLayout();
  private javax.swing.JTabbedPane tabPanel1 = new javax.swing.JTabbedPane();
  private javax.swing.JPanel panel1 = new javax.swing.JPanel();

  private BorderLayout moduleBorderLayoutModule1 = new BorderLayout();
  private javax.swing.JPanel panelModule1 = new javax.swing.JPanel();
  private com.borland.dbswing.JdbTextArea textModule1 = new com.borland.dbswing.JdbTextArea();
  private javax.swing.JScrollPane scrollModule1 = new javax.swing.JScrollPane();
  private java.util.Vector pagesToRefresh = new java.util.Vector();

  public ServerMonitor() {
    monitor = this;
    try {
      jbInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private void jbInit() throws Exception{
    this.setLayout(borderLayout1);
    this.add(panel1, BorderLayout.CENTER);
    panel1.setLayout(borderLayout2);
    panel1.add(tabPanel1, BorderLayout.CENTER );

    textModule1.setEnabled(true);
    panelModule1.setLayout(moduleBorderLayoutModule1);
    scrollModule1.getViewport().add(textModule1);
    panelModule1.add(scrollModule1, BorderLayout.CENTER);
    myLog = textModule1;
    tabPanel1.addTab(ServerResources.format(res.getString("logTitle"), "Module1"), panelModule1);
    tabPanel1.setSelectedComponent(panelModule1);
  }

  private void addPage(ServerMonitorPage page, String name) {
    tabPanel1.addTab(name, page);
    pagesToRefresh.addElement(page);
    //panel1.updateUI();
    //tabPanel1.setEnabledAt(0, true);
  }

  private static ServerFrame createFrame() {
    ServerFrame frame = new ServerFrame();
    frame.pack();
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension frameSize = frame.getSize();
    if (frameSize.height > screenSize.height)
      frameSize.height = screenSize.height;
    if (frameSize.width > screenSize.width)
      frameSize.width = screenSize.width;
    frame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    frame.setVisible(true);

    return(frame);
  }

  protected static void ensureFrame() {
    if (ServerMonitor.monitor == null) {
      ServerMonitor.monitor = new ServerMonitor();
      ServerFrame frame = createFrame();
      frame.getContentPane().add(ServerMonitor.monitor);
    }
  }

  public static synchronized ServerMonitorInterface addPage(Object obj, String name) {
    ensureFrame();
    ServerMonitorPage page = new ServerMonitorPage(obj);
    ServerMonitor.monitor.addPage(page, name);
    return page;
  }

  public static synchronized void log(String str) {
    if (myLog != null) {
      DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.LONG);
      myLog.append(df.format(new java.util.Date()) + " " + str + System.getProperty("line.separator"));
    }
  }
}
