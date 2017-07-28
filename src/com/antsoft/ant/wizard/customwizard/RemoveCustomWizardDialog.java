/**
 *  RemoveCustomWizardDialog - 사용자 위저드를 삭제하는 다이얼로그
 *  designed by Kim YumKyung
 *  date 1999.7.30
 */

package com.antsoft.ant.wizard.customwizard;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.io.*;
import java.awt.event.*;

import com.antsoft.ant.property.*;
import com.antsoft.ant.util.WindowDisposer;

public class RemoveCustomWizardDialog extends JDialog implements ActionListener{
  private JComboBox list;
  private JButton ok,cancel;

  //wizard list
  private String[] wizardList;
  private CustomWizard customWizard;

  public RemoveCustomWizardDialog(JFrame parent,CustomWizard customWizard) {
    super(parent,"Remove Custom Wizard",true);
    this.customWizard = customWizard;
    try  {
      jInit();
      //pack();
      setSize(330, 130);
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
    try{
  		// register window event handler
  		addWindowListener(WindowDisposer.getDisposer());
  		addKeyListener(WindowDisposer.getDisposer());

      File file = new File(IdePropertyManager.CUSTOM_WIZARD_PATH);
      //wizard dir이 없는경우 만든다
      if(!file.exists() || !file.isDirectory()){
        file.mkdir();
      }
      //wizard dir로 부터 wizard list를 얻는다
      wizardList = file.list();
      list = new JComboBox();
      //combo에 추가한다
      for(int i=0; i<wizardList.length; i++){
        list.addItem(wizardList[i].substring(0,wizardList[i].lastIndexOf(".")));
      }

      JLabel wizardListLbl = new JLabel("Wizard List");
      ok = new JButton("OK");
      cancel = new JButton("Cancel");

      //setSize
      wizardListLbl.setPreferredSize(new Dimension(80,20));
      list.setPreferredSize(new Dimension(150,20));

      //listener에 추가
      ok.addActionListener(this);
      cancel.addActionListener(this);

      JPanel totalP = new JPanel(new FlowLayout(FlowLayout.LEFT));
      totalP.add(wizardListLbl);
      totalP.add(list);

      JPanel p = new JPanel(new BorderLayout());
      p.setBorder(new EtchedBorder());
      p.add(totalP,BorderLayout.CENTER);
      p.add(new JPanel(),BorderLayout.WEST);
      p.add(new JPanel(),BorderLayout.EAST);
      p.add(new JPanel(),BorderLayout.SOUTH);
      p.add(new JPanel(),BorderLayout.NORTH);

      JPanel buttonP = new JPanel(new FlowLayout(FlowLayout.CENTER));
      buttonP.add(ok);
      buttonP.add(cancel);

      getContentPane().setLayout(new BorderLayout());
      getContentPane().add(p,BorderLayout.CENTER);
      getContentPane().add(buttonP,BorderLayout.SOUTH);
      getContentPane().add(new JPanel(),BorderLayout.EAST);
      getContentPane().add(new JPanel(),BorderLayout.WEST);
      getContentPane().add(new JPanel(),BorderLayout.NORTH);
      
    }catch(Exception ex){
      ex.printStackTrace();
    }
  }

  //////////////////// event 처리 /////////////////////////////////////////
  public void actionPerformed(ActionEvent e){
    if(e.getSource() == ok){

      customWizard.removeWizard((String)(list.getSelectedItem()));
    }
    dispose();
  }

}
