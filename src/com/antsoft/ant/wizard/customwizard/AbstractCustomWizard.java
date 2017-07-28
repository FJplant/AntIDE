/*
 *  AbstractCustomWizard - ����� �����带 ����� ���� base class
 *  designed by Kim yunKyung
 *  date 1999.8.2
 */
 
package com.antsoft.ant.wizard.customwizard;

import java.awt.Frame;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;

import com.antsoft.ant.util.WindowDisposer;

public class AbstractCustomWizard extends JDialog{

  private transient CustomWizardListener listener;
  private Vector source = new Vector();

  public AbstractCustomWizard(String title){
    super((Frame)null,title,true);

		// register window event handler
		addWindowListener(WindowDisposer.getDisposer());
		addKeyListener(WindowDisposer.getDisposer());
  }

  public synchronized void addCustomWizardListener(CustomWizardListener l) {
    listener = l;
  }
  public synchronized void removeCustomWizardListener(CustomWizardListener l){
    listener = null;
  }

  public void fireCustomWizardEvent(CustomWizardEvent e) {
    switch (e.getID()) {
      case CustomWizardEvent.OK:
          listener.wizardOK(e);
        break;
    }
  }

  /**
   *  source vector�� ���´�
   */
  public Vector getSource(){
    return source;
  }

  /**
   *  addSourceString - source string�� vector�� �߰��Ѵ�
   *  @param  fileName(~.java)
   *  @param  sourceString  
   */
  public void addSourceString(String fileName,String sourceString){
    Source s = new Source(fileName,sourceString);
    source.addElement(s);
  }

  /**
   *  setVisible�� ����ڰ� ������� ���ϵ��� final�� �ٲ۴�
   */

  public final void setVisible(boolean b){
    super.setVisible(b);
  }
}
