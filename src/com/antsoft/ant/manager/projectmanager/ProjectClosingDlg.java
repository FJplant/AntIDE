/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/manager/projectmanager/ProjectClosingDlg.java,v 1.3 1999/08/30 08:07:24 remember Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.3 $
 * 프로젝트 종료시 프로젝트 및 프로젝트에 속한 파일을 저장할 것인지 묻는다.
 */
package com.antsoft.ant.manager.projectmanager;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.Vector;
import javax.swing.*;

/**
 *  Class ProjectClosingDlg
 *
 *  @author Jinwoo Baek
 */                                                   
class ProjectClosingDlg extends JDialog implements ActionListener {
	DefaultListModel listModel = new DefaultListModel();
	JList list = new JList(listModel);
	Vector itemsModel = new Vector();
	//JPanel items = new JPanel();
	Box items = Box.createVerticalBox();
	boolean isOk = false;
  JButton selAllBtn = new JButton("Select All");
  JButton selNoneBtn = new JButton("Select None");
	JButton okBtn = new JButton("Ok");
	JButton cancelBtn = new JButton("Cancel");

	/**
	 *  Constructor
	 */
	ProjectClosingDlg(Frame owner, String title) {
		super(owner, title, true);
		//items.setLayout(new FlowLayout(FlowLayout.LEFT));
		//items.setPreferredSize(new Dimension(325, 500));
		//items.setLayout(new GridLayout(0, 1));
    selAllBtn.addActionListener(this);
    selNoneBtn.addActionListener(this);
		okBtn.addActionListener(this);
    okBtn.setPreferredSize(new Dimension(50, 25));
		cancelBtn.addActionListener(this);
    cancelBtn.setPreferredSize(new Dimension(80, 25));

		JPanel pl = new JPanel();
		pl.setLayout(new FlowLayout(FlowLayout.CENTER));
    pl.add(selAllBtn);
    pl.add(selNoneBtn);
		pl.add(okBtn);
		pl.add(cancelBtn);

		getContentPane().add(new JScrollPane(items), BorderLayout.CENTER);
		getContentPane().add(pl, BorderLayout.SOUTH);
		//pack();
		//setBounds(300, 300, 350, 250);
    setSize(350, 250);
	}

  public void actionPerformed(ActionEvent evt) {
    if (evt.getSource() == selAllBtn) {
  		for (int i = 0; i < itemsModel.size(); i++) {
	  		SaveItem si = (SaveItem)itemsModel.elementAt(i);
		  	si.setSelected(true);
  		}
    }
    else if (evt.getSource() == selNoneBtn) {
  		for (int i = 0; i < itemsModel.size(); i++) {
	  		SaveItem si = (SaveItem)itemsModel.elementAt(i);
		  	si.setSelected(false);
  		}
    }
    else if (evt.getSource() == okBtn) {
      isOk = true;
      setVisible(false);
    }
    else if (evt.getSource() == cancelBtn) {
      isOk = false;
      setVisible(false);
    }
  }

	/**
	 *  아이템을 추가한다.
	 *
	 *  @param pfe ProjectFileEntry
	 */
	void addItem(ProjectFileEntry pfe) {
		String text = pfe.getPath() + File.separator + pfe.getName();
		SaveItem si = new SaveItem(pfe.getPath() + File.separator + pfe.getName(), pfe);
		itemsModel.addElement(si);
		items.add(si);
	}

	/**
	 *  아이템을 추가한다.
	 *
	 *  @param project Project
	 */
	void addItem(Project project) {
		SaveItem si = new SaveItem(project.getPath(), project);
		itemsModel.addElement(si);
		items.add(si);
		//listModel.addElement(new SaveItem(project.getPath(), project));
	}

	/**
	 *  선택된 아이템들의 객체를 모아서 넘겨준다.
	 *
	 *  @return Vector 선택된 아이템들
	 */
	Vector getItems() {
		Vector selectedItems = new Vector();
		for (int i = 0; i < itemsModel.size(); i++) {
			SaveItem si = (SaveItem)itemsModel.elementAt(i);
			if (si.isSelected()) selectedItems.addElement(si.getUserObject());
		}
		return selectedItems;
	}

	/**
	 *  OK를 선택했는지 아닌지
	 *
	 *  @return boolean
	 */
	boolean isOK() {
		return isOk;
	}

	/**
	 *  아이템이 있는지 본다.
	 *
	 *  @return boolean
	 */
	boolean doesContainItem() {
		if (itemsModel.size() > 0) return true;
		else return false;
	}

	/**
	 *  class SaveItem
	 *
	 *  @author Jinwoo Baek
	 */
	class SaveItem extends JCheckBox {
		Object obj = null;

		/**
		 *  Constructor
		 */
		SaveItem(String text, Object obj) {
			super(text);
			this.obj = obj;
			setPreferredSize(new Dimension(300, 20));
		}

		/**
		 *  포함하고 있는 객체를 돌려준다.
		 *
		 *  @return Object
		 */
		Object getUserObject() {
			return obj;
		}
	}
}

