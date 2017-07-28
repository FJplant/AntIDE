
package test.Module1.client;

import java.awt.*;
import java.awt.event.*;

import com.borland.dx.dataset.*;

/**
* Template File
*   ClientFrame.java.template
* IDL Object
*   Module1.Interface1
* Generation Date
*   1999년 7월 28일 수요일 04시24분48초 오후
* IDL Source File
*   C:/WORK/ant/source/test/untitled1.idl
* Abstract
*   Client application frame which is the container for the default client user
*   interface.  Implements the application menubar.
* Description
*   This template receives a vector containing the UI elements to be
*   presented at application startup.  With a Data Modeler generated
*   IDL, this will be structures presenting data fetched from the
*   selected table(s) and interfaces with no visible methods.  Startup
*   involves creating a DataModule, initializing and running a factory
*   interface, and connecting the structure UI beans to the DataModule
*   and EntityDataSet.  If the interfaces did contain visible methods,
*   precedence would be given to factory interfaces.  If there were no
*   factory interfaces, then all interfaces with visible methods would
*   be added to the panel.
*   The Application Generation provides for three types of presentation:
*   tabbed, vertical, or horizontal.  If tabbed, then each bean added
*   becomes a tab, otherwise beans are added to the panel so that they
*   stack vertically or horizontally.  This orientation also affects
*   handling when visible methods return supported data types.  If the
*   presentation is tabbed, then the result from executing a method with
*   the proper data type becomes a tab on the panel, otherwise the result
*   will be presented in a dialog.  Generated code supports returned
*   result types which are an interface, structure(s), or an array of
*   primitive data types.
*/

public class ClientFrame extends javax.swing.JFrame {
  java.util.ResourceBundle res = java.util.ResourceBundle.getBundle("test.Module1.client.ClientResources");
  private BorderLayout borderLayout1 = new BorderLayout();
  private GridBagLayout gridBagLayout1 = new GridBagLayout();
  private javax.swing.JPanel panel1 = new javax.swing.JPanel();
  private javax.swing.JTabbedPane tabPanel1 = new javax.swing.JTabbedPane();

  private int lastResultIndex = 0;
  private BorderLayout borderLayout_Interface1 = new BorderLayout();
  private javax.swing.JPanel panelInterface1 = new javax.swing.JPanel();
  private test.Module1.client.Interface1Bean interface1Bean = new test.Module1.client.Interface1Bean();
  private test.Module1.client.Interface1UIBean interface1UIBean = new test.Module1.client.Interface1UIBean();

  javax.swing.JMenuBar menuBar1 = new javax.swing.JMenuBar();
  javax.swing.JMenu menuFile = new javax.swing.JMenu();
  javax.swing.JMenuItem menuFileExit = new javax.swing.JMenuItem();

  javax.swing.JMenu menuActions = new javax.swing.JMenu();
  javax.swing.JMenuItem menuActionsRemoveTab = new javax.swing.JMenuItem();

  javax.swing.JMenu menuHelp = new javax.swing.JMenu();
  javax.swing.JMenuItem menuHelpAbout = new javax.swing.JMenuItem();

  public ClientFrame() {
    enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    try  {
      jbInit();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception  {
    this.setSize(new Dimension(400, 200));
    this.setTitle("Module1 client");
    this.getContentPane().setLayout(borderLayout1);
    panel1.setLayout(gridBagLayout1);
    this.getContentPane().add(panel1, BorderLayout.CENTER);
    panel1.add(tabPanel1, new java.awt.GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
      java.awt.GridBagConstraints.SOUTH, java.awt.GridBagConstraints.BOTH,
      new Insets(3, 3, 0, 3), 0, 0));

    panelInterface1.setLayout(borderLayout_Interface1);
    panelInterface1.add(interface1UIBean, BorderLayout.CENTER);
    tabPanel1.addTab("Interface1", panelInterface1);
    try {
      interface1UIBean.init(interface1Bean);
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }

    interface1UIBean.methodOperation1.addOpExecutedListener(new test.Module1.client.Interface1_operation1_OpExecutedListener() {
      public void OpExecuted(test.Module1.client.Interface1_operation1_OpExecutedEvent e) {
        Interface1_operation1_OpExecuted((test.Module1.client.Interface1_operation1_OpExecutedEvent)e);
      }
    });
    tabPanel1.setSelectedIndex(0);

    menuFile.setText(res.getString("File"));
    menuFile.setMnemonic(res.getString("FileShortcut").charAt(0));

    menuFileExit.setText(res.getString("Exit"));
    menuFileExit.setMnemonic(res.getString("ExitShortcut").charAt(0));
    menuFileExit.setAccelerator(
      javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4,
                                         java.awt.event.KeyEvent.ALT_MASK));
    menuFileExit.addActionListener(new ActionListener()  {
      public void actionPerformed(ActionEvent e) {
        fileExit_actionPerformed(e);
      }
    });

    menuActions.setText(res.getString("Actions"));
    menuActions.setMnemonic(res.getString("ActionsShortcut").charAt(0));
    menuActionsRemoveTab.setText(res.getString("RemoveTab"));
    menuActionsRemoveTab.setMnemonic(res.getString("RemoveTabShortcut").charAt(0));
    menuActionsRemoveTab.setAccelerator(
      javax.swing.KeyStroke.getKeyStroke(res.getString("RemoveTabShortcut").charAt(0),
                                         java.awt.event.KeyEvent.CTRL_MASK));
    menuActionsRemoveTab.addActionListener(new ActionListener()  {
      public void actionPerformed(ActionEvent e) {
        removeTab_actionPerformed(e);
      }
    });

    menuHelp.setText(res.getString("Help"));
    menuHelp.setMnemonic(res.getString("HelpShortcut").charAt(0));
    menuHelpAbout.setText(res.getString("About"));
    menuHelpAbout.setMnemonic(res.getString("AboutShortcut").charAt(0));
    menuHelpAbout.addActionListener(new ActionListener()  {
      public void actionPerformed(ActionEvent e) {
        helpAbout_actionPerformed(e);
      }
    });

    menuFile.add(menuFileExit);
    menuActions.add(menuActionsRemoveTab);
    menuHelp.add(menuHelpAbout);

    menuBar1.add(menuFile);
    menuBar1.add(menuActions);
    menuBar1.add(menuHelp);
    this.setJMenuBar(menuBar1);
  }

  protected void processWindowEvent(WindowEvent e) {
    super.processWindowEvent(e);
    if (e.getID() == WindowEvent.WINDOW_CLOSING) {
      System.exit(0);
    }
  }

  public void fileExit_actionPerformed(ActionEvent e) {
    System.exit(0);
  }

  public void removeTab_actionPerformed(ActionEvent e) {
    int index = tabPanel1.getSelectedIndex();
    if (index <= 0) {
      getToolkit().beep();
    }
    else {
      tabPanel1.removeTabAt(index);
      if (tabPanel1.getTabCount() == index) {
        index--;
      }
      tabPanel1.setSelectedIndex(index);
      //tabPanel1.setEnabledAt(index, true);
    }
  }

  public void helpAbout_actionPerformed(ActionEvent e) {
    ClientAboutBoxDialog dlg = new ClientAboutBoxDialog(this);
    Dimension dlgSize = dlg.getPreferredSize();
    Dimension frmSize = getSize();
    Point loc = getLocation();
    dlg.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
    dlg.setModal(true);
    dlg.show();
  }

  void displayComponent(Component component, String title) {
    if (title == null) {
      title = res.getString("result") + String.valueOf(++lastResultIndex);
    }
    for (int i = 0; i < tabPanel1.getTabCount(); i++) {
      if (title.equals(tabPanel1.getTitleAt(i))) {
        int index = tabPanel1.getSelectedIndex();
        tabPanel1.removeTabAt(i);
        if (index == i) {
          if (tabPanel1.getTabCount() == index) {
            index--;
          }
          tabPanel1.setSelectedIndex(index);
        }
      }
    }
    tabPanel1.addTab(title, component);
    //tabPanel1.setEnabledAt(0, true);
  }

  void Interface1_operation1_OpExecuted(test.Module1.client.Interface1_operation1_OpExecutedEvent e) {
    System.out.println("ClientPanel.java Interface1_operation1_OpExecuted()");
  }
}
