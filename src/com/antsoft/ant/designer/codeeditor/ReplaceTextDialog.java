/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/designer/codeeditor/ReplaceTextDialog.java,v 1.3 1999/07/22 05:02:12 multipia Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.3 $
 * 소스 코드에서 특정 문자열을 치환하기 위한 입력 다이얼로그
 */

package com.antsoft.ant.designer.codeeditor;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

/**
 *  class ReplaceTextDialog
 *
 *  @author Jinwoo Baek
 */
class ReplaceTextDialog extends JDialog implements ActionListener {
	JLabel lbl1 = new JLabel("Text to Find");
	JLabel lbl2 = new JLabel("Replace with");
	JComboBox ttf = new JComboBox();
  DefaultComboBoxModel findModel = new DefaultComboBoxModel();
	JComboBox rw = new JComboBox();
  DefaultComboBoxModel replaceModel = new DefaultComboBoxModel();
	JCheckBox cs = new JCheckBox("Case Sensitive");
	JCheckBox wwo = new JCheckBox("Whole Word only");
	JCheckBox ra = new JCheckBox("Replace All");
//	JButton findBtn = new JButton("Find");
	JButton replaceBtn = new JButton("Replace");
	JButton cancelBtn = new JButton("Cancel");

	int selected = 0;
	// Constants
	static final int FIND = 1;
	static final int REPLACE = 2;
	static final int CANCEL = 3;

	/**
	 *  Constructor
	 *
	 *  @param owner Frame owns this dialog
	 */
	ReplaceTextDialog(Frame owner, DefaultComboBoxModel find, DefaultComboBoxModel replace) {
		super(owner, "Find & Replace", true);
		Container contentPane = this.getContentPane();
    if (find != null) findModel = find;
    if (replace != null) replaceModel = replace;

		// Button Action Event Handler
		//replaceBtn.setPreferredSize(new Dimension(100, 25));
		replaceBtn.addActionListener(this);
		//cancelBtn.setPreferredSize(new Dimension(100, 25));
		cancelBtn.addActionListener(this);

		lbl1.setPreferredSize(new Dimension(80, 20));
		ttf.setPreferredSize(new Dimension(220, 20));
    ttf.setModel(findModel);
		ttf.setEditable(true);

    //cs.setPreferredSize(new Dimension(150,20));
    //wwo.setPreferredSize(new Dimension(150,20));

		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent evt) {
        if(evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
					selected = CANCEL;
          dispose();
        }
			}
		});
    ttf.addActionListener(this);
		rw.addActionListener(this);
		lbl2.setPreferredSize(new Dimension(80, 20));
		rw.setPreferredSize(new Dimension(220, 20));
    rw.setModel(replaceModel);
		rw.setEditable(true);

		JPanel p1 = new JPanel();
		p1.setLayout(new FlowLayout(FlowLayout.LEFT));
		p1.add(lbl1);
		p1.add(ttf);

		JPanel p2 = new JPanel();
		p2.setLayout(new FlowLayout(FlowLayout.LEFT));
		p2.add(lbl2);
		p2.add(rw);

		JPanel p3 = new JPanel();
		p3.setLayout(new FlowLayout(FlowLayout.LEFT));
		p3.add(cs);
		p3.add(wwo);
		p3.add(ra);

		JPanel p4 = new JPanel();
		p4.setLayout(new FlowLayout(FlowLayout.CENTER));
		p4.add(replaceBtn);
		p4.add(cancelBtn);

		JPanel p5 = new JPanel();
		p5.setLayout(new GridLayout(3, 1));
		p5.add(p1);
		p5.add(p2);
		p5.add(p3);

    JPanel p6 = new JPanel();
    p6.setBorder(new EtchedBorder());
    p6.add(p5);

    this.getContentPane().setLayout(new BorderLayout());
    this.getContentPane().add(p6,BorderLayout.CENTER);
    this.getContentPane().add(p4,BorderLayout.SOUTH);
    this.getContentPane().add(new JPanel(),BorderLayout.WEST);
    this.getContentPane().add(new JPanel(),BorderLayout.EAST);
    this.getContentPane().add(new JPanel(),BorderLayout.NORTH);

    setSize(370,200);
    this.setResizable(false);
    ttf.requestFocus();
	}

	/**
	 *  입력한 문자열을 얻어온다.
	 *
	 *  @return String 입력한 문자열
	 */
	String getTextToFind() {
		return ttf.getEditor().getItem().toString();
	}

	String getTextToReplace() {
		return rw.getEditor().getItem().toString();
	}

	boolean isCaseSensitive() {
		return cs.isSelected();
	}

	boolean isWholeWordOnly() {
		return wwo.isSelected();
	}

	boolean isReplaceAll() {
		return ra.isSelected();
	}

	int showDialog() {
		setVisible(true);
		return selected;
	}

	/**
	 *  Event Handler
	 */
	public void actionPerformed(ActionEvent evt) {
		if (evt.getSource() == replaceBtn){
			setVisible(false);
      String findText = ttf.getEditor().getItem().toString();
      if (!findText.trim().equals("")) {
      	boolean status = false;
        for (int i = 0; i < findModel.getSize(); i++) {
        	if (findModel.getElementAt(i).toString().equals(findText)) {
          	status = true;
            break;
          }
        }
        if (!status) findModel.addElement(ttf.getEditor().getItem().toString());
      }
      String replaceText = rw.getEditor().getItem().toString();
      if (!replaceText.trim().equals("")) {
      	boolean status = false;
        for (int i = 0; i < replaceModel.getSize(); i++) {
        	if (replaceModel.getElementAt(i).toString().equals(replaceText)) {
          	status = true;
            break;
          }
        }
        if (!status) replaceModel.addElement(rw.getEditor().getItem().toString());
      }

			selected = REPLACE;
    }

		else if (evt.getSource() == cancelBtn) {
			selected = CANCEL;
			setVisible(false);
		}
	}
}

