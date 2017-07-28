/*
 *	package : com.antsoft.ant.debugger
 *	source  : LoadedClassesTreeCellRenderer.java
 *	date    : 1999.8.9
 *  
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 * This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * Author:    Lee, Chul Mok
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/debugger/LoadedClassesTreeCellRenderer.java,v 1.2 1999/08/10 04:59:36 itree Exp $
 * $Revision: 1.2 $
 * $History: LoadedClassesTreeCellRenderer.java $
 */

package com.antsoft.ant.debugger;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

import com.antsoft.ant.util.ImageList;

public class LoadedClassesTreeCellRenderer extends DefaultTreeCellRenderer { 

	public LoadedClassesTreeCellRenderer() {
		super();
	}
	/**
   * This is messaged from JTree whenever it needs to get the size
   * of the component or it wants to draw it.
   * This attempts to set the font based on value, which will be
   * a TreeNode.
   */
	public Component getTreeCellRendererComponent(JTree tree, Object value,
						boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
	
		super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
		String stringValue = tree.convertValueToText(value, selected, expanded, leaf, row, hasFocus);

	
		// Tooltips used by the tree.
		setToolTipText(stringValue);
		DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode)value;
		LoadedClassesEntry entry = (LoadedClassesEntry)dmtn.getUserObject();
		
		switch( entry.getType()) {
		case LoadedClassesEntry.ROOT:
			setIcon(ImageList.computerIcon);
			setText(stringValue);
			break;
		case LoadedClassesEntry.ROOTJAVA:
			setIcon(ImageList.tabIcon);
			setText(stringValue);
			break;
		case LoadedClassesEntry.ROOTSUN:
			setIcon(ImageList.javaIcon);
			setText(stringValue);
			break;
		case LoadedClassesEntry.ROOTUSER:
			setIcon(ImageList.addFolderBtnIcon);
			setText(stringValue);
			break;
		case LoadedClassesEntry.JDKPACKAGE:
			setIcon(ImageList.dirIcon);
			setText(stringValue);
			break;
		case LoadedClassesEntry.SUNPACKAGE:
			setIcon(ImageList.dirIcon);
			setText(stringValue);
			break;
		case LoadedClassesEntry.USERPACKAGE:
			setIcon(ImageList.dirIcon);
			setText(stringValue);
			break;			
		case LoadedClassesEntry.JDK:
			setIcon(ImageList.packageIcon);
			setText(stringValue);
			break;
		case LoadedClassesEntry.SUN:
			setIcon(ImageList.classIcon);
			setText(stringValue);
			break;
		case LoadedClassesEntry.USER:
			setIcon(ImageList.innerIcon);
			setText(stringValue);
			break;
		default:
			break;
		}	
		//setText(stringValue);
		return this;
	}
}
