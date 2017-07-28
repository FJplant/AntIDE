/*
 *  AbstractCustomWizard - 사용자 위저드를 만들기 위한 base class
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
   *  source vector를 얻어온다
   */
  public Vector getSource(){
    return source;
  }

  /**
   *  addSourceString - source string을 vector에 추가한다
   *  @param  fileName(~.java)
   *  @param  sourceString  
   */
  public void addSourceString(String fileName,String sourceString){
    Source s = new Source(fileName,sourceString);
    source.addElement(s);
  }

  /**
   *  setVisible을 사용자가 사용하지 못하도록 final로 바꾼다
   */

  public final void setVisible(boolean b){
    super.setVisible(b);
  }
}
