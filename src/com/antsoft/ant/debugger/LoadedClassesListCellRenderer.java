/*
 *	package : com.antsoft.ant.debugger
 *	source  : LoadedClassesListCellRenderer.java
 *	date    : 1999.8.9
 *
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * Author:       Lee, Chul Mok
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/debugger/LoadedClassesListCellRenderer.java,v 1.2 1999/08/11 07:31:51 itree Exp $
 * $Revision: 1.2 $
 * $History: LoadedClassesListCellRenderer.java $
 * 
 */

package com.antsoft.ant.debugger;


import java.awt.*;
import javax.swing.*;
import com.antsoft.ant.util.ImageList;

public class LoadedClassesListCellRenderer extends JPanel implements ListCellRenderer { 

	private JLabel entryList = null;
	private Font f;
	/**
	 *	LoadedClassesListCellRenderer -  constructor 
	 */
	public LoadedClassesListCellRenderer() {
		JButton b = new JButton();
    f = new Font(b.getFont().getName(), Font.PLAIN, 12);
    entryList = new JLabel();
    entryList.setFont(f);
    entryList.setForeground(Color.darkGray);
    
		setLayout(new BorderLayout());
 		this.add(entryList, BorderLayout.CENTER);
 		this.setBackground(Color.white);	
	}
	
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)	{
		//TO DO 
		LoadedClassesEntry entry = (LoadedClassesEntry) value; 
		
		if (isSelected){
			this.setBackground(list.getSelectionBackground());			
		} else {
			this.setBackground(list.getBackground());
		}

			
		switch(entry.getType()) {
		case LoadedClassesEntry.JDK:
			entryList.setIcon(ImageList.packageIcon);
			entryList.setText(entry.getName());
			entryList.setForeground(Color.darkGray);
			break;
		case LoadedClassesEntry.SUN:
			entryList.setIcon(ImageList.classIcon);
			entryList.setText(entry.getName());
			entryList.setForeground(Color.darkGray);
			break;
		case LoadedClassesEntry.USER:
			entryList.setIcon(ImageList.innerIcon);
			entryList.setText(entry.getName());
			entryList.setForeground(new Color(0,0,120));
			break;
		default :
		}	
			  
		return this;
	}
}
