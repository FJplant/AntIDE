package test;

import test.Module1.server.*;
import javax.swing.UIManager;
import java.awt.*;

/**
* Template File
*   ServerApp.java.template
* IDL Object
*   Module1
* Generation Date
*   1999년 7월 28일 수요일 04시24분52초 오후
* IDL Source File
*   C:/WORK/ant/source/test/untitled1.idl
* Abstract
*   CORBA server application.
*/

public class Module1ServerApp {

  private boolean packFrame = false;


  public Module1ServerApp() {
    ServerFrame frame = new ServerFrame();

    if (packFrame)
      frame.pack();
    else
      frame.validate();

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension frameSize = frame.getSize();
    if (frameSize.height > screenSize.height)
      frameSize.height = screenSize.height;
    if (frameSize.width > screenSize.width)
      frameSize.width = screenSize.width;
    frame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    frame.setVisible(true);
  }

  public static void main(String[] args) {
    try  {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      //UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
      //UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
      //UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
    }
    catch (Exception ex) {
    }
    new Module1ServerApp();

    try {
      java.util.ResourceBundle res = java.util.ResourceBundle.getBundle("test.Module1.server.ServerResources");
      String name;

      //(debug support)System.getProperties().put("ORBdebug", "true");
      //(debug support)System.getProperties().put("ORBwarn", "2");
      System.getProperties().put("ORBagentPort", "14000");
      org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init(args,System.getProperties());
      org.omg.CORBA.BOA boa = ((com.visigenic.vbroker.orb.ORB)orb).BOA_init();

      name = "Interface1";
      Interface1Impl interface1 = new Interface1Impl(name);
      ServerMonitor.log(ServerResources.format(res.getString("created"), "Module1ServerApp.java Interface1"));
      boa.obj_is_ready(interface1);
      ServerMonitor.log(ServerResources.format(res.getString("isReady"), "Module1ServerApp.java Interface1"));
    }
    catch(Exception ex) {
      System.err.println(ex);
    }
  }
}

