package com.antsoft.ant.beans.propertyeditor;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.*;
import java.awt.*;
import java.beans.*;
import com.antsoft.ant.util.WindowDisposer;

public class PropertyDialog extends JDialog implements ActionListener{
  private JButton ok = new JButton("OK");
  private JButton cancel = new JButton("Cancel");
  private PropertyEditor editor;

  public PropertyDialog(Frame frame,PropertyEditor editor){
    super(frame);
    this.editor = editor;
    jInit();
    setSize(new Dimension(200,200));
  }

  private void jInit(){
	addWindowListener(WindowDisposer.getDisposer());
	addKeyListener(WindowDisposer.getDisposer()); 		
    
    Component com = editor.getCustomEditor();
    if(com != null){
      System.out.println("jInit come in");
      if(com instanceof Panel){
        System.out.println("panel type");
      }
      ok.addActionListener(this);
      cancel.addActionListener(this);
      JPanel p1 = new JPanel();
      p1.setLayout(new FlowLayout(FlowLayout.CENTER));
      p1.add(ok);
      p1.add(cancel);
      getContentPane().setLayout(new BorderLayout());
      getContentPane().add(com,BorderLayout.CENTER);
      getContentPane().add(p1,BorderLayout.SOUTH);
    }else{
      System.out.println("component is null!");
    }
  }

  public void actionPerformed(ActionEvent e){
    dispose();
  }
}
