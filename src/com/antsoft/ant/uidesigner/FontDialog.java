package com.antsoft.ant.uidesigner;

import javax.swing.*;
import javax.swing.border.*;
import java.beans.*;
import java.awt.*;
import java.awt.event.*;

import com.antsoft.ant.util.WindowDisposer;

public class FontDialog extends JDialog implements ActionListener{

  private PropertyEditor editor;
  private JButton ok = new JButton("OK");
  private JButton cancel = new JButton("Cancel");
  private JComboBox fontName = new JComboBox();
  private JComboBox fontType = new JComboBox();
  private JTextField fontSize = new JTextField(3);
  private Font selFont;

  private PropertyPanel panel;

  public FontDialog(PropertyEditor editor,PropertyPanel panel) {
    this.editor = editor;
    this.panel = panel;
    setTitle("Font Dialog");
    jInit();
    setSize(300,200);
  }

  private void jInit(){
		// register window event handler
		addWindowListener(WindowDisposer.getDisposer());
		addKeyListener(WindowDisposer.getDisposer()); 		

    ok.addActionListener(this);
    cancel.addActionListener(this);
    JPanel p1 = new JPanel();
    p1.setLayout(new FlowLayout(FlowLayout.CENTER));
    p1.add(ok);
    p1.add(cancel);

    //font list를 받아와서 콤보박스에 추가한다
    String[] fontNames = Toolkit.getDefaultToolkit().getFontList();
    for(int i=0; i<fontNames.length; i++){
      fontName.addItem(fontNames[i]);
    }
    //font type을 콤보에 추가한다
    for(int j=0; j<PaintablePropertyManager.fontList.length; j++){
      fontType.addItem(PaintablePropertyManager.fontList[j]);
    }

    //font Name을 panel에 추가
    JPanel p2 = new JPanel();
    p2.setLayout(new FlowLayout(FlowLayout.LEFT));
    JLabel lbl1 = new JLabel("Font Name");
    lbl1.setPreferredSize(new Dimension(70,20));
    p2.add(lbl1);
    p2.add(fontName);

    //font type을 panel에 추가
    JPanel p3 = new JPanel();
    p3.setLayout(new FlowLayout(FlowLayout.LEFT));
    JLabel lbl2 = new JLabel("Font Type");
    lbl2.setPreferredSize(new Dimension(70,20));
    p3.add(lbl2);
    p3.add(fontType);

    //font size를 panel에 추가
    JPanel p4 = new JPanel();
    p4.setLayout(new FlowLayout(FlowLayout.LEFT));
    JLabel lbl3 = new JLabel("Font Size");
    lbl3.setPreferredSize(new Dimension(70,20));
    p4.add(lbl3);
    p4.add(fontSize);

    JPanel p5 = new JPanel();
    p5.setLayout(new GridLayout(3,1));
    p5.setBorder(new EtchedBorder());
    p5.add(p2);
    p5.add(p3);
    p5.add(p4);

    //이전에 선택한 font가 있으면 그걸로 dialog를 setting한다
    Font f = (Font)editor.getValue();
    if(f == null){
      fontName.setSelectedIndex(0);
      fontType.setSelectedIndex(2);
      fontSize.setText("12");
    }else{
      fontName.setSelectedItem(f.getFontName());
      fontType.setSelectedItem(panel.manager.getFontType(f.getStyle()));
      fontSize.setText(Integer.toString(f.getSize()));
    }

    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(p5,BorderLayout.CENTER);
    getContentPane().add(p1,BorderLayout.SOUTH);
    getContentPane().add(new JPanel(),BorderLayout.EAST);
    getContentPane().add(new JPanel(),BorderLayout.WEST);
    getContentPane().add(new JPanel(),BorderLayout.NORTH);
  }

  public void actionPerformed(ActionEvent e){
    String fName = null;
    String fType = null;
    if(e.getSource() == ok){
      fName = (String)fontName.getSelectedItem();
      fType = (String)fontType.getSelectedItem();
      int type = 0;
      if(fType.equals(PaintablePropertyManager.fontList[0])) type = Font.BOLD;
      else if(fType.equals(PaintablePropertyManager.fontList[1])) type = Font.ITALIC;
      else if(fType.equals(PaintablePropertyManager.fontList[2])) type = Font.PLAIN;
      else System.out.println("font type is invalided");
      String fSize = fontSize.getText();

      //editor에 set
      System.out.println("fontDialog type- " +fType);
      System.out.println("fontDialog type(int)- "+type);
      selFont = new Font(fName,type,Integer.parseInt(fSize));
      editor.setValue(selFont);
      panel.textField.setText(" "+fName+", "+fType+", "+fSize);

    }
    dispose();
  }

  /**
   *  getFont - 선택된 font룰 리턴한다
   */
  public Font getFont(){
    return selFont;
  }
}
