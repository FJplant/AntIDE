/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/designer/codeeditor/FindTextDialog.java,v 1.4 1999/07/23 04:06:37 remember Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.4 $
 * 소스 코드에서 특정 문자열을 찾아주기 위한 입력 다이얼로그
 */

package com.antsoft.ant.designer.codeeditor;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

/**
 *  class FindTextDialog
 *
 *  @author Jinwoo Baek
 */
class FindTextDialog extends JDialog implements ActionListener {
	JLabel lbl1 = new JLabel("Text to Find");
	JComboBox ttf = new JComboBox();
  DefaultComboBoxModel findModel = new DefaultComboBoxModel();
	JCheckBox cs = new JCheckBox("Case Sensitive");
	JCheckBox wwo = new JCheckBox("Whole Word only");
	JButton findBtn = new JButton("Find");
	JButton cancelBtn = new JButton("Cancel");

	int selected = 0;
	// Constants
	static final int FIND = 1;
	static final int CANCEL = 2;

	/**
	 *  Constructor
	 *
	 *  @param owner Frame owns this dialog
	 */
	FindTextDialog(Frame owner, DefaultComboBoxModel find) {
		super(owner, "Find...", true);
		Container contentPane = this.getContentPane();
    if (find != null) findModel = find;

		// Button Action Event Handler
		findBtn.addActionListener(this);
		cancelBtn.addActionListener(this);

		lbl1.setPreferredSize(new Dimension(80, 20));
		ttf.setPreferredSize(new Dimension(200, 20));
    cs.setPreferredSize(new Dimension(150,20));
    wwo.setPreferredSize(new Dimension(150,20));
    ttf.setModel(findModel);
		ttf.setEditable(true);
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent evt) {
				if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
        	selected = FIND;
					setVisible(false);
        }
        else if(evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
					selected = CANCEL;
					setVisible(false);
        }
			}
		});

    ttf.getEditor().addActionListener(new ActionListener() {
    	public void actionPerformed(ActionEvent e) {
      	selected = FIND;
      	setVisible(false);
      }
    });

		JPanel p1 = new JPanel();
		p1.setLayout(new FlowLayout(FlowLayout.LEFT));
		p1.add(lbl1);
		p1.add(ttf);

		JPanel p2 = new JPanel();
		p2.setLayout(new FlowLayout(FlowLayout.LEFT));
		p2.add(cs);
		p2.add(wwo);

		JPanel p3 = new JPanel();
		p3.setLayout(new FlowLayout(FlowLayout.CENTER));
		p3.add(findBtn);
		p3.add(cancelBtn);

		JPanel p4 = new JPanel();
		p4.setLayout(new GridLayout(2, 1));
		p4.add(p1);
		p4.add(p2);

    JPanel p5 = new JPanel();
    p5.setBorder(new EtchedBorder());
    p5.add(p4);

    this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(p5,BorderLayout.CENTER);
    this.getContentPane().add(new JPanel(),BorderLayout.WEST);
    this.getContentPane().add(new JPanel(),BorderLayout.EAST);
    this.getContentPane().add(p3,BorderLayout.SOUTH);
    this.getContentPane().add(new JPanel(),BorderLayout.NORTH);

    ttf.setRequestFocusEnabled(true);
    ttf.requestFocus();
    setSize(350,145);
    this.setResizable(false);
	}

	/**
	 *  입력한 문자열을 얻어온다.
	 *
	 *  @return String 입력한 문자열
	 */
	String getTextToFind() {
		return ttf.getEditor().getItem().toString();
	}

	boolean isCaseSensitive() {
		return cs.isSelected();
	}

	boolean isWholeWordOnly() {
		return wwo.isSelected();
	}

	int showDialog() {
		setVisible(true);
		return selected;
	}

	/**
	 *  Event Handler
	 */
	public void actionPerformed(ActionEvent evt) {
    if (evt.getSource() == ttf) {
     	selected = FIND;
    	setVisible(false);
    }
		else if (evt.getSource() == findBtn) {
			setVisible(false);
      String findText = ttf.getEditor().getItem().toString();

      if (findText.trim().equals("")) {
      	boolean status = false;
        for (int i = 0; i < findModel.getSize(); i++) {
        	if (findModel.getElementAt(i).toString().equals(findText)) {
          	status = true;
            break;
          }
        }

        if (!status) findModel.addElement(ttf.getEditor().getItem().toString());
      }
			selected = FIND;
		}
		else if (evt.getSource() == cancelBtn) {
			selected = CANCEL;
			setVisible(false);
		}
	}
}

