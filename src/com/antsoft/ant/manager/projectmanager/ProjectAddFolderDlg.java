/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/manager/projectmanager/ProjectAddFolderDlg.java,v 1.4 1999/08/30 08:07:24 remember Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.4 $
 * ProjectPanel에서 폴더를 추가할 경우 폴더 이름을 넣기 위한 대화상자
 */

package com.antsoft.ant.manager.projectmanager;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.event.*;
import java.awt.*;

/**
 *  class ProjectAddFolderDlg
 *
 *  @author Jinwoo Baek                          
 */
class ProjectAddFolderDlg extends JDialog {
	JLabel lbl = new JLabel("Folder Name");
	JTextField jtf = new JTextField();
	JButton okBtn = new JButton("Ok");
	JButton cancelBtn = new JButton("Cancel");
	boolean isOK = false;

	/**
	 *  Constructor
	 *
	 *  @param parent parent frame to display this dialog
	 *  @param title title to display in caption bar
	 *  @param modal makes this dialog do modal or not
	 */
	ProjectAddFolderDlg(Frame parent, String title, boolean modal) {
		super(parent, title, modal);

		lbl.setPreferredSize(new Dimension(80, 20));
		jtf.setPreferredSize(new Dimension(170, 20));

		jtf.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent event) {
				if (event.getKeyCode() == KeyEvent.VK_ENTER) {
					setVisible(false);
					isOK = true;
				}
			}
		});
		okBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				setVisible(false);
				isOK = true;
			}
		});

		cancelBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				setVisible(false);
				isOK = false;
			}
		});

		JPanel btnPl = new JPanel();
		btnPl.setLayout(new FlowLayout(FlowLayout.CENTER));
		btnPl.add(okBtn);
		btnPl.add(cancelBtn);

		JPanel inputPl = new JPanel();
		inputPl.setLayout(new FlowLayout(FlowLayout.LEFT));
    inputPl.setBorder(new EtchedBorder());
		inputPl.add(lbl);
		inputPl.add(jtf);

    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(inputPl,BorderLayout.CENTER);
    getContentPane().add(btnPl,BorderLayout.SOUTH);
    getContentPane().add(new JPanel(),BorderLayout.WEST);
    getContentPane().add(new JPanel(),BorderLayout.EAST);
    getContentPane().add(new JPanel(),BorderLayout.NORTH);

		setSize(300,130);
		setLocation(300, 300);
		setResizable(false);

    jtf.requestFocus();
	}

	/**
	 *  사용자가 "Ok" 버튼을 눌렀는지 "Cancel"버튼을 눌렀는지 확인한다.
	 */
	boolean isOK() {
		return isOK;
	}

	/**
	 *  사용자가 입력한 값을 반환한다.
	 *
	 *  @return String 사용자가 입력한 폴더이름
	 */
	String getValue() {
    String value = jtf.getText().trim();
    if(value.length() > 0) return value;
    else return null;
	}
}
