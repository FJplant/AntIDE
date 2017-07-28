//Title:        AntProgrammer
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Kim sang-kyun
//Company:      AntSoft. co.
//Description:  IDE 프로젝트

package com;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.event.*;
import com.antsoft.ant.debugger.*;
import com.sun.java.swing.*;
import java.beans.*;
import jclass.bwt.*;

public class Bean1 extends JPanel {
  BorderLayout borderLayout1 = new BorderLayout();
  private transient Vector actionListeners;
  private transient Vector keyListeners;
  private String address;
  private transient Vector watchListeners;
  JLabel jLabel1;
  JLabel jLabel2;
  JTree jTree1;
  JCheckBox jCheckBox1;
  JCheckBox jCheckBox2;
  JCheckBox jCheckBox3;
  jclass.bwt.JCOutliner jCOutliner1;


  public Bean1() {
    try  {
      jbInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    jLabel1 = (JLabel) Beans.instantiate(getClass().getClassLoader(), JLabel.class.getName());
    jLabel2 = (JLabel) Beans.instantiate(getClass().getClassLoader(), JLabel.class.getName());
    jTree1 = (JTree) Beans.instantiate(getClass().getClassLoader(), JTree.class.getName());
    jCheckBox1 = (JCheckBox) Beans.instantiate(getClass().getClassLoader(), JCheckBox.class.getName());
    jCheckBox2 = (JCheckBox) Beans.instantiate(getClass().getClassLoader(), JCheckBox.class.getName());
    jCheckBox3 = (JCheckBox) Beans.instantiate(getClass().getClassLoader(), JCheckBox.class.getName());
    jCOutliner1 = (jclass.bwt.JCOutliner) Beans.instantiate(getClass().getClassLoader(), jclass.bwt.JCOutliner.class.getName());
    jLabel1.setText("Test");
    jLabel2.setText("jLabel2");
    jCheckBox1.setText("jCheckBox1");
    jCheckBox2.setText("jCheckBox2");
    jCheckBox3.setText("jCheckBox3");
    this.setLayout(borderLayout1);
    this.add(jCheckBox3, BorderLayout.NORTH);
    this.add(jLabel1, BorderLayout.SOUTH);
    this.add(jLabel2, BorderLayout.WEST);
    this.add(jTree1, BorderLayout.CENTER);
    this.add(jCheckBox2, BorderLayout.EAST);
    this.add(jCOutliner1, BorderLayout.EAST);
  }

  public synchronized void removeActionListener(ActionListener l) {
    if (actionListeners != null && actionListeners.contains(l)) {
      Vector v = (Vector) actionListeners.clone();
      v.removeElement(l);
      actionListeners = v;
    }
  }

  public synchronized void addActionListener(ActionListener l) {
    Vector v = actionListeners == null ? new Vector(2) : (Vector) actionListeners.clone();
    if (!v.contains(l)) {
      v.addElement(l);
      actionListeners = v;
    }
  }

  protected void fireActionPerformed(ActionEvent e) {
    if (actionListeners != null) {
      Vector listeners = actionListeners;
      int count = listeners.size();
      for (int i = 0; i < count; i++)
        ((ActionListener) listeners.elementAt(i)).actionPerformed(e);
    }
  }

  public synchronized void removeKeyListener(KeyListener l) {
    super.removeKeyListener(l);
    if (keyListeners != null && keyListeners.contains(l)) {
      Vector v = (Vector) keyListeners.clone();
      v.removeElement(l);
      keyListeners = v;
    }
  }

  public synchronized void addKeyListener(KeyListener l) {
    super.addKeyListener(l);
    Vector v = keyListeners == null ? new Vector(2) : (Vector) keyListeners.clone();
    if (!v.contains(l)) {
      v.addElement(l);
      keyListeners = v;
    }
  }

  protected void fireKeyTyped(KeyEvent e) {
    if (keyListeners != null) {
      Vector listeners = keyListeners;
      int count = listeners.size();
      for (int i = 0; i < count; i++)
        ((KeyListener) listeners.elementAt(i)).keyTyped(e);
    }
  }

  protected void fireKeyPressed(KeyEvent e) {
    if (keyListeners != null) {
      Vector listeners = keyListeners;
      int count = listeners.size();
      for (int i = 0; i < count; i++)
        ((KeyListener) listeners.elementAt(i)).keyPressed(e);
    }
  }

  protected void fireKeyReleased(KeyEvent e) {
    if (keyListeners != null) {
      Vector listeners = keyListeners;
      int count = listeners.size();
      for (int i = 0; i < count; i++)
        ((KeyListener) listeners.elementAt(i)).keyReleased(e);
    }
  }

  public void setAddress(String newAddress) {
    String  oldAddress = address;
    address = newAddress;
    firePropertyChange("address", oldAddress, newAddress);
  }

  public String getAddress() {
    return address;    
  }

  public synchronized void removeWatchListener(WatchListener l) {
    if (watchListeners != null && watchListeners.contains(l)) {
      Vector v = (Vector) watchListeners.clone();
      v.removeElement(l);
      watchListeners = v;
    }
  }

  public synchronized void addWatchListener(WatchListener l) {
    Vector v = watchListeners == null ? new Vector(2) : (Vector) watchListeners.clone();
    if (!v.contains(l)) {
      v.addElement(l);
      watchListeners = v;
    }
  }
}

