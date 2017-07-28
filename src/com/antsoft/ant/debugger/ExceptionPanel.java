
//Title:        Your Product Name
//Version:
//Copyright:    Copyright (c) 1999
//Author:       Jinwoo Baek
//Company:      Antsoft
//Description:  Your description

package com.antsoft.ant.debugger;

import java.awt.*;
import java.util.Vector;
import java.awt.event.*;
import javax.swing.*;
import sun.tools.debug.*;

public class ExceptionPanel extends JPanel implements ActionListener, WindowListener {
  // Components
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel pl1 = new JPanel();
  JPanel pl2 = new JPanel();
  JLabel exLbl = new JLabel();
  BorderLayout borderLayout2 = new BorderLayout();
  JScrollPane scroller = new JScrollPane();
  JList exList = new JList();
  BorderLayout borderLayout3 = new BorderLayout();

  JFrame wrapper;

  // Data Members
  private DebuggerProxy debuggerProxy;
  JButton refreshBtn;
  JButton addBtn;
  JButton removeBtn;

  public ExceptionPanel(DebuggerProxy debuggerProxy) {
    this.debuggerProxy = debuggerProxy;
    try  {
      jbInit();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }
  public void reset(DebuggerProxy debuggerProxy) {
    this.debuggerProxy = debuggerProxy;
  }

  private void jbInit() throws Exception {
    this.setLayout(borderLayout1);
    exLbl.setText("Exceptions be Caught");
    pl2.setLayout(borderLayout2);
    pl1.setLayout(borderLayout3);

    addBtn = new JButton("Catch");
    addBtn.setMargin(new Insets(0, 0, 0, 0));
    removeBtn = new JButton("Ignore");
    removeBtn.setMargin(new Insets(0, 0, 0, 0));
    refreshBtn = new JButton("Refresh");
    refreshBtn.setMargin(new Insets(0, 0, 0, 0));

    JPanel pl3 = new JPanel();
    pl3.add(addBtn, null);
    pl3.add(removeBtn, null);
    pl3.add(refreshBtn, null);

    this.add(pl1, BorderLayout.NORTH);
    pl1.add(exLbl, BorderLayout.WEST);
    pl1.add(pl3, BorderLayout.EAST);
    this.add(pl2, BorderLayout.CENTER);
    pl2.add(scroller, BorderLayout.CENTER);
    scroller.getViewport().add(exList, null);

    exList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

    addBtn.addActionListener(this);
    removeBtn.addActionListener(this);
    refreshBtn.addActionListener(this);
  }

  public void showFrame() {
    if ((wrapper != null) && wrapper.isVisible()) return;
    wrapper = new JFrame("Exceptions");
    wrapper.getContentPane().add(this, null);
    wrapper.pack();
    wrapper.addWindowListener(this);
//    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//    Dimension dlgSize = wrapper.getSize();
//    if (dlgSize.height > screenSize.height) dlgSize.height = screenSize.height;
//    if (dlgSize.width > screenSize.width) dlgSize.width = screenSize.width;
//    dlg.setLocation((screenSize.width - dlgSize.width) / 2, (screenSize.height - dlgSize.height) / 2);
    wrapper.setVisible(true);
  }

  public void update() throws Exception {
    if (debuggerProxy != null) {
      RemoteDebugger debugger = debuggerProxy.getRemoteDebugger();
      exList.setListData(debugger.getExceptionCatchList());
    }
  }

  public void actionPerformed(ActionEvent e) {
    //TODO: implement this java.awt.event.ActionListener method;
    if (e.getSource() == refreshBtn) {
      try {
        update();
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    } else if (e.getSource() == addBtn) {
      ExceptionSelDlg dlg = new ExceptionSelDlg(wrapper, "Exception Availables", true);
      Vector v = new Vector();
      try {
        RemoteClass[] cls = debuggerProxy.getRemoteDebugger().listClasses();
        for (int i = 0; i < cls.length; i++) {
          if (cls[i].getName().indexOf("Exception") > 0) v.addElement(cls[i].getName());
        }
      } catch (Exception ex) {
        return;
      }
      dlg.setAvailableExceptions(v);
      dlg.setVisible(true);
      if (dlg.isOk()) {
        Object[] cl = dlg.getSelected();
        for (int i = 0; i < cl.length; i++) {
          try {
            debuggerProxy.catchException((String)cl[i]);
          } catch (Exception ex) {
            continue;
          }
        }
        if (!dlg.getUsrException().trim().equals("")) {
          try {
            debuggerProxy.catchException(dlg.getUsrException());
          } catch (Exception ex) {
            ex.printStackTrace();
          }
        }
      }
      try {
        update();
      } catch (Exception ex) {
      }
    } else if (e.getSource() == removeBtn) {
      // 리스트에서 선택된 클래스들을 Ignore시킨다.
      Object obj[] = exList.getSelectedValues();
      for (int i = 0; i < obj.length; i++) {
        try {
          debuggerProxy.ignoreException((String)obj[i]);
        } catch (Exception ex) {
          continue;
        }
      }
      try {
        update();
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
  }

  public void windowOpened(WindowEvent e) {
    //TODO: implement this java.awt.event.WindowListener method;
  }

  public void windowClosing(WindowEvent e) {
    //TODO: implement this java.awt.event.WindowListener method;
    wrapper.removeAll();
    wrapper.dispose();
    wrapper = null;
  }

  public void windowClosed(WindowEvent e) {
    //TODO: implement this java.awt.event.WindowListener method;
  }

  public void windowIconified(WindowEvent e) {
    //TODO: implement this java.awt.event.WindowListener method;
  }

  public void windowDeiconified(WindowEvent e) {
    //TODO: implement this java.awt.event.WindowListener method;
  }

  public void windowActivated(WindowEvent e) {
    //TODO: implement this java.awt.event.WindowListener method;
  }

  public void windowDeactivated(WindowEvent e) {
    //TODO: implement this java.awt.event.WindowListener method;
  }
}
