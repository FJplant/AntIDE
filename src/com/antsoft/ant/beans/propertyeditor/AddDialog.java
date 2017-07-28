package com.antsoft.ant.beans.propertyeditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import java.io.*;

public class AddDialog extends JDialog implements ActionListener{

  JLabel jarLbl = new JLabel("Jar file");
  JTextField jar = new JTextField(20);
  Button btn = new Button("..");
  JButton ok = new JButton("OK");
  JButton cancel = new JButton("Cancel");

  TitledBorder border = new TitledBorder("Jar loading");

  String path;
  File dlgPath;
  JFileChooser chooser;
  String fileName;

  boolean isOk = false;

  public AddDialog(Frame frame, String title, boolean modal,String path){
    super(frame,title,modal);
    try  {
      this.path = path;
      dlgPath = new File(path);
      jInit();
      pack();

      setSize(320, 170);
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

  void jInit(){
    //set size
    jarLbl.setPreferredSize(new Dimension(60,20));
    jar.setPreferredSize(new Dimension(100,20));

    btn.addActionListener(this);
    ok.addActionListener(this);
    cancel.addActionListener(this);

    JPanel p1 = new JPanel();
    p1.setLayout(new FlowLayout(FlowLayout.LEFT));
    p1.add(jarLbl);
    p1.add(jar);
    p1.add(btn);

    JPanel p2 = new JPanel();
    p2.setLayout(new FlowLayout(FlowLayout.CENTER));
    p2.add(ok);
    p2.add(cancel);

    JPanel p3 = new JPanel();
    p3.setLayout(new GridLayout(1,1));
    p3.setBorder(border);
    p3.add(p1);

    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(p3,BorderLayout.CENTER);
    getContentPane().add(p2,BorderLayout.SOUTH);
    getContentPane().add(new JPanel(),BorderLayout.EAST);
    getContentPane().add(new JPanel(),BorderLayout.WEST);
    getContentPane().add(new JPanel(),BorderLayout.NORTH);

  }

  public void actionPerformed(ActionEvent e){
    String p = null;
    File file = null;

    if(e.getSource() == btn){
      chooser = new JFileChooser(dlgPath);
      chooser.showOpenDialog(this);
      file = chooser.getSelectedFile();
      p = file.getAbsolutePath();
      fileName = file.getName();
      jar.setText(p);
    }else if(e.getSource() == ok){
      if(jar.getText() == "")
        new JOptionPane("Invalide input type!",JOptionPane.ERROR_MESSAGE,JOptionPane.OK_OPTION);
      isOk = true;
      dispose();
    }else if(e.getSource() == cancel){
      dispose();
    }
  }

  public boolean isOk(){
    return isOk;
  }
}
