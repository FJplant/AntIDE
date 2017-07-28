/*
 * $Id: ExternalUpdateFileDlg.java,v 1.4 1999/08/31 04:59:30 multipia Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.4 $
 */
package com.antsoft.ant.manager.projectmanager;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.Vector;
import javax.swing.*;

/**
 *  External Update File Dlg
 *
 *  @author Kim sang kyun
 */
class ExternalUpdateFileDlg extends JDialog implements ActionListener {
	private DefaultListModel listModel = new DefaultListModel();
	private JList list = new JList(listModel);
	private Vector itemsModel = new Vector();
	//JPanel items = new JPanel();
	private Box items = Box.createVerticalBox();
	boolean isOk = false;                                            
	private JButton okBtn = new JButton("Ok");
	private JButton cancelBtn = new JButton("Cancel");
  private Vector selectedItems = new Vector(5, 2);
  private Vector unSelectedItems = new Vector(5, 2);


	/**
	 *  Constructor
	 */
	public ExternalUpdateFileDlg(Frame owner, String title) {
		super(owner, title, true);
		okBtn.addActionListener(this);
    okBtn.setPreferredSize(new Dimension(50, 25));
		cancelBtn.addActionListener(this);
    cancelBtn.setPreferredSize(new Dimension(80, 25));

		JPanel pl = new JPanel();
		pl.setLayout(new FlowLayout(FlowLayout.CENTER));
		pl.add(okBtn);
		pl.add(cancelBtn);

		getContentPane().add(new JScrollPane(items), BorderLayout.CENTER);
		getContentPane().add(pl, BorderLayout.SOUTH);
    setSize(450, 250);

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension dlgSize = getSize();
    if (dlgSize.height > screenSize.height) dlgSize.height = screenSize.height;
    if (dlgSize.width > screenSize.width)   dlgSize.width = screenSize.width;
    setLocation((screenSize.width - dlgSize.width) / 2, (screenSize.height - dlgSize.height) / 2);
	}

  public void actionPerformed(ActionEvent evt) {
    if (evt.getSource() == okBtn) {
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
	public void addItem(ProjectFileEntry pfe) {
		String text = pfe.getPath() + File.separator + pfe.getName();
		ReloadItem si = new ReloadItem(pfe.getPath() + File.separator + pfe.getName(), pfe);
		itemsModel.addElement(si);
		items.add(si);
	}

	/**
	 *  선택된 아이템들의 객체를 모아서 넘겨준다.
	 *
	 *  @return Vector 선택된 아이템들
	 */
	public Vector getSelectedItems() {
    selectedItems.removeAllElements();
    unSelectedItems.removeAllElements();

		for (int i = 0; i < itemsModel.size(); i++) {
			ReloadItem si = (ReloadItem)itemsModel.elementAt(i);
			if (si.isSelected()) {
        selectedItems.addElement(si.getUserObject());
      }
      else {
        unSelectedItems.addElement(si.getUserObject());
      }
		}
		return selectedItems;
	}

  public Vector getAllItems(){
    Vector allItems = new Vector(10, 2);
		for (int i = 0; i < itemsModel.size(); i++) {
			ReloadItem si = (ReloadItem)itemsModel.elementAt(i);
      allItems.addElement(si.getUserObject());
		}

    return allItems;
  }

  public Vector getUnselectedItems(){
		return unSelectedItems;
  }

	/**
	 *  OK를 선택했는지 아닌지
	 *
	 *  @return boolean
	 */
  public	boolean isOK() {
		return isOk;
	}

	/**
	 *  아이템이 있는지 본다.
	 *
	 *  @return boolean
	 */
	private boolean doesContainItem() {
		if (itemsModel.size() > 0) return true;
		else return false;
	}

	/**
	 *  class SaveItem
	 *
	 *  @author Jinwoo Baek
	 */
	class ReloadItem extends JCheckBox {
		private Object obj = null;

		/**
		 *  Constructor
		 */
		public ReloadItem(String text, Object obj) {
			super(text);
			this.obj = obj;
			setPreferredSize(new Dimension(300, 20));
      this.setSelected(true);
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
/*
 * $Log: ExternalUpdateFileDlg.java,v $
 * Revision 1.4  1999/08/31 04:59:30  multipia
 * no message
 *
 */

