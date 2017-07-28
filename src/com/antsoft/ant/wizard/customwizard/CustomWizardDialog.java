/*
 *  CustomWizardDialog - custom wizard생성을 위한 위저드
 *  desined by Kim YunKyung
 *  date 1999.8.2
 */

package com.antsoft.ant.wizard.customwizard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;

import com.antsoft.ant.util.*;

public class CustomWizardDialog extends JDialog implements ActionListener, KeyListener{
  private CustomWizard customWizard;
  private JTextField packName, className;
  private JButton ok, cancel;

  public CustomWizardDialog(JFrame parent, String title, boolean modal, CustomWizard customWizard){
    super(parent, title, true);
    this.customWizard = customWizard;
    try  {
      jInit();
      setSize(330, 150);
      setVisible(true);
      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      Dimension frameSize = this.getSize();
      if (frameSize.height > screenSize.height) frameSize.height = screenSize.height;
      if (frameSize.width > screenSize.width) frameSize.width = screenSize.width;
      this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
      setResizable(false);
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private void jInit(){
		// register window event handler
		addWindowListener(WindowDisposer.getDisposer());
		addKeyListener(WindowDisposer.getDisposer());

    JLabel packNameLbl = new JLabel("Package Name");
    packName = new JTextField(17);
    JLabel classNameLbl = new JLabel("Class Name");
    className = new JTextField(17);
    ok = new JButton("OK");
    ok.setEnabled(false);
    cancel = new JButton("Cancel");

    //setSize
    packNameLbl.setPreferredSize(new Dimension(90,20));
    classNameLbl.setPreferredSize(new Dimension(90,20));

    //listener에 추가
    className.addKeyListener(this);
    ok.addActionListener(this);
    cancel.addActionListener(this);

    //class panel
    JPanel packNameP = new JPanel(new FlowLayout(FlowLayout.LEFT));
    packNameP.add(packNameLbl);
    packNameP.add(packName);

    //class panel
    JPanel classNameP = new JPanel(new FlowLayout(FlowLayout.LEFT));
    classNameP.add(classNameLbl);
    classNameP.add(className);

    //button panel
    JPanel buttonP = new JPanel(new FlowLayout(FlowLayout.CENTER));
    buttonP.add(ok);
    buttonP.add(cancel);

    //total panel
    JPanel totalP = new JPanel(new GridLayout(2,1));
    totalP.setBorder(new EtchedBorder());
    totalP.add(packNameP);
    totalP.add(classNameP);

    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(totalP,BorderLayout.CENTER);
    getContentPane().add(buttonP,BorderLayout.SOUTH);
    getContentPane().add(new JPanel(),BorderLayout.NORTH);
    getContentPane().add(new JPanel(),BorderLayout.WEST);
    getContentPane().add(new JPanel(),BorderLayout.EAST);
  }

  ////////////////////  event 처리  //////////////////////////////////////
  public void actionPerformed(ActionEvent e){
    if(e.getSource() == ok){
      dispose();
      customWizard.generateCustomWizardSource(packName.getText(),className.getText());
    }else if(e.getSource() == cancel){
      dispose();
    }
  }

  public void keyReleased(KeyEvent e){
    if(!StripString.isNull(className.getText())) ok.setEnabled(true);
    else ok.setEnabled(false);
  }
  public void keyPressed(KeyEvent e){
  }
  public void keyTyped(KeyEvent e){
  }
}
