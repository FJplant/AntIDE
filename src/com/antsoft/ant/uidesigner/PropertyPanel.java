/*
 *  PropertyPanel - panel type의 editor
 *  designed by Kim Yun Kyung
 *  date 1999.7.8
 */

package com.antsoft.ant.uidesigner;

import javax.swing.*;
import java.beans.*;
import java.awt.event.*;
import java.awt.*;

public class PropertyPanel extends JPanel implements ActionListener{
  public PropertyEditor editor;
  public PaintablePropertyManager manager;
  private FontDialog dlg;
  public JTextField textField;

  public PropertyPanel(PropertyEditor editor,PaintablePropertyManager manager){
    this.editor = editor;
    this.manager = manager;
    setLayout(new BorderLayout());

    String fontName = (Toolkit.getDefaultToolkit().getFontList())[0];
    String styleName = "PLAIN";
    String size = "12";

    System.out.println(fontName);
    textField = new JTextField(" "+fontName+", "+styleName+", "+size,13);
    textField.setFont(new Font("Arial",Font.PLAIN,11));
    textField.setEditable(false);
    textField.setBackground(Color.white);
    JButton btn = new JButton("..");
    btn.setPreferredSize(new Dimension(3,3));
    btn.addActionListener(this);
    add(textField,BorderLayout.WEST);
    add(btn,BorderLayout.CENTER);
  }

  public void actionPerformed(ActionEvent e) {
    dlg = new FontDialog(editor,this);
    dlg.setVisible(true);
  }

  /**
   *  getFont - dialog에서 선택된 폰트를 리턴한다
   */
  public Font getFont(){
    if(dlg == null) return new Font("Dialog",Font.PLAIN,12);
    else return dlg.getFont();
  }

}
