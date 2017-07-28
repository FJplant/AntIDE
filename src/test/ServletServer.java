package test;
import sun.servlet.http.*;

import javax.servlet.*;
import javax.swing.*;
import java.awt.*;


public class ServletServer {

  //
  static public void main(String[] args) {
    boolean portSet = false;
    boolean servletDirSet = false;


    for(int i = 0; i < args.length; i++) {
      if(args[i].equals("-p")) {
        portSet = true;
      }
      if(args[i].equals("-d")) {
        servletDirSet = true;
      }
    }
    int i = 0;

    String[] arguments = new String[args.length + (servletDirSet ? 0 : 2) + (portSet ? 0 : 2)];
    for(; i < args.length; i++) {
      arguments[i] = args[i];
    }

    if(!portSet) {
      arguments[i++] = "-p";
      arguments[i++] = "8080";
    }
    String servletDir = "";
    if(!servletDirSet) {
      arguments[i++] = "-d";
      servletDir = System.getProperty("java.class.path");
      servletDir = servletDir.substring(0,servletDir.indexOf(java.io.File.pathSeparator));
      String pack;

      pack = java.io.File.separator + "test.Module1" +
             java.io.File.separator + "Module1" +
             java.io.File.separator + "clienthtml";
      System.out.println(servletDir + pack);
      arguments[i++] = servletDir + pack;
    }

    ServletServerFrame frame = new ServletServerFrame("8080",servletDir);
    frame.show();
    HttpServer.main(arguments);

  }
}

class ServletServerFrame extends JFrame {

    java.awt.FlowLayout flowLayout1 = new java.awt.FlowLayout();
    java.awt.GridBagLayout gridBagLayout1 = new java.awt.GridBagLayout();

    JLabel jLabel1 = new JLabel();
    JTextField jTextField1 = new JTextField();

    JLabel jLabel2 = new JLabel();
    JTextField jTextField2 = new JTextField();

    JLabel jLabel3 = new JLabel();
    JTextField jTextField3 = new JTextField();

    JLabel jLabel4 = new JLabel();



    String _port;
    String _servletDir;


  public ServletServerFrame(String port, String servletDir) {
    enableEvents(java.awt.AWTEvent.WINDOW_EVENT_MASK);
    _servletDir = servletDir;
    _port = port;
    try  {
      jbInit();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception  {

    this.setTitle("ServletServer");

    this.getContentPane().setLayout(gridBagLayout1);
    this.setSize(new java.awt.Dimension(200, 200));

    jLabel1.setText("Port: ");
    jTextField1.setText(_port);

    jLabel2.setText("ServletDir: ");
    jTextField2.setText(_servletDir);

    jLabel3.setText("Paste the following URL into a web browser to load the SHTML file");

      String pack;

      pack = "test.Module1.clienthtml";


    jTextField3.setText("http://localhost:8080/servlet/" + pack + ".ShtmlLoader");

    this.getContentPane().add(jLabel1, new GridBagConstraints(0, 0, 1, 2, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(25, 24, 0, 0), 37, 10));

    this.getContentPane().add(jLabel2, new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(11, 24, 0, 0), 20, 8));

    this.getContentPane().add(jTextField1, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(25, 20, 0, 0), 40, 1));

    this.getContentPane().add(jTextField2, new GridBagConstraints(1, 2, 3, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(13, 20, 0, 32), 161, 1));

    this.getContentPane().add(jLabel3, new GridBagConstraints(0, 3, 4, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(24, 24, 0, 0), 22, 11));

    this.getContentPane().add(jTextField3, new GridBagConstraints(0, 4, 7, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 24, 0, 32), 274, 1));

    this.getContentPane().add(jLabel4, new GridBagConstraints(0, 5, 6, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(14, 24, 63, 32), 22, 15));

    jTextField1.setEnabled(false);
    jTextField2.setEnabled(false);

    this.setSize(new java.awt.Dimension(500, 250));

  }

  protected void processWindowEvent(java.awt.event.WindowEvent e) {
    super.processWindowEvent(e);
    if (e.getID() == java.awt.event.WindowEvent.WINDOW_CLOSING) {
      System.exit(0);
    }
  }
}
