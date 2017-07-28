/*
 *  CustomWizardDialog - 사용자 위저드를 등록받는 다이얼로그
 *  designed by Kim YunKyung
 *  date 1999.7.29
 */

package com.antsoft.ant.wizard.customwizard;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.io.*;
import java.awt.event.*;

import com.antsoft.ant.util.*;
import com.antsoft.ant.property.*;

public class AddCustomWizardDialog extends JDialog implements ActionListener, KeyListener {

  public JTextField jarFile, wizardName;
//  public JCheckBox useDefaultIcon;
  private JButton ok, cancel, browse;
  private boolean isOK = false;
  private JFrame parent;
  private CustomWizard customWizard;
  private String wizardPath = IdePropertyManager.CUSTOM_WIZARD_PATH;

  public AddCustomWizardDialog(JFrame parent,CustomWizard customWizard) {
    super(parent,"Add Custom Wizard",true);
    this.parent = parent;
    this.customWizard = customWizard;
    try  {
      jInit();

      setSize(350, 150);
      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      Dimension frameSize = this.getSize();
      if (frameSize.height > screenSize.height) frameSize.height = screenSize.height;
      if (frameSize.width > screenSize.width) frameSize.width = screenSize.width;
      this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
      setResizable(false);
      setVisible(true);
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private void jInit(){
		// register window event handler
		addWindowListener(WindowDisposer.getDisposer());
		addKeyListener(WindowDisposer.getDisposer());

    JLabel wizardNameLbl = new JLabel("Wizard Name");
    wizardName = new JTextField(17);
    JLabel jarFileLbl = new JLabel("Jar File");
    jarFile = new JTextField(17);
    browse = new JButton("..");
    ok = new JButton("OK");
    ok.setEnabled(false);
    cancel = new JButton("Cancel");

    //setSize
    wizardNameLbl.setPreferredSize(new Dimension(80,20));
    jarFileLbl.setPreferredSize(new Dimension(80,20));
    browse.setPreferredSize(new Dimension(20,20));

    //listener에 추가
    jarFile.addKeyListener(this);
    wizardName.addKeyListener(this);
    browse.addActionListener(this);
    ok.addActionListener(this);
    cancel.addActionListener(this);

    //wizard name panel
    JPanel wizardP = new JPanel(new FlowLayout(FlowLayout.LEFT));
    wizardP.add(wizardNameLbl);
    wizardP.add(wizardName);

    //jarFile panel
    JPanel jarFileP = new JPanel(new FlowLayout(FlowLayout.LEFT));
    jarFileP.add(jarFileLbl);
    jarFileP.add(jarFile);
    jarFileP.add(browse);

    //button panel
    JPanel buttonP = new JPanel(new FlowLayout(FlowLayout.CENTER));
    buttonP.add(ok);
    buttonP.add(cancel);

    //total Panel
    JPanel totalP = new JPanel(new GridLayout(2,1));
    totalP.setBorder(new EtchedBorder());
    totalP.add(wizardP);
    totalP.add(jarFileP);

    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(totalP,BorderLayout.CENTER);
    getContentPane().add(buttonP,BorderLayout.SOUTH);
    getContentPane().add(new JPanel(),BorderLayout.EAST);
    getContentPane().add(new JPanel(),BorderLayout.WEST);
    getContentPane().add(new JPanel(),BorderLayout.NORTH);
  }

  ////////////////// event 처리  ////////////////////////////////////////
  public void actionPerformed(ActionEvent e) {
    try{
      boolean flag = false;
      if(e.getSource() == ok){
        System.out.println("ok1");
        File file = new File(wizardPath);
        if(file.exists() && file.isDirectory()){
          String[] list = file.list();
          for(int i=0; i<list.length; i++){
            //이미 존재하는 위저드이름이면 message를 보여줌
            if((wizardName.getText()+".jar").equals(list[i])){
              System.out.println("same name");
              JOptionPane.showMessageDialog(this,"This name already exists!");
              flag = false;
              break;
            }else flag = true;
          }
          if(list.length == 0) flag = true;
          //같은 이름의 위저드가 없으면 다이얼로그를 닫고 위저드를 등록한다
          if(flag){
            System.out.println("ok");
            //isOK = true;
            dispose();
            customWizard.registerWizard(jarFile.getText(),wizardName.getText());
          }
        }else{
          //wizard dir이 없으면 만들어 주고 다이얼로그를 닫는다
          file.mkdir();
          dispose();
          customWizard.registerWizard(jarFile.getText(),wizardName.getText());
        }
      }else if(e.getSource() == cancel){
        dispose();
      }else if(e.getSource() == browse){
        JFileChooser dlg = new JFileChooser(wizardPath);
        dlg.showOpenDialog(parent);
        File file = dlg.getSelectedFile();
        jarFile.setText(file.getAbsolutePath());
        if(StripString.isNull(jarFile.getText())) ok.setEnabled(false);
        else ok.setEnabled(true);
      }
    }catch(Exception ex){
      ex.printStackTrace();
    }
  }

  public void keyTyped(KeyEvent e) {
  }
  public void keyPressed(KeyEvent e) {
  }
  public void keyReleased(KeyEvent e) {
      if( !StripString.isNull(jarFile.getText())
          && !StripString.isNull(wizardName.getText()) ) ok.setEnabled(true);
      else ok.setEnabled(false);
  }
}
