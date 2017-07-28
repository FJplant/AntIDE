/*
 *	package : com.antsoft.ant.debugger
 *	source  : LocalVariablesTreeCellRenderer.java
 *	date    : 1999.8.14
 *
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 * This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * Author:    Lee, Chul Mok
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/debugger/LocalVariablesTreeCellRenderer.java,v 1.1 1999/08/16 03:17:34 itree Exp $
 * $Revision: 1.1 $
 * $History: LocalVariablesTreeCellRenderer.java $
 */

package com.antsoft.ant.debugger;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

public class LocalVariablesTreeCellRenderer extends DefaultTreeCellRenderer {

  private ImageIcon folderIcon = null;
  private ImageIcon objectIcon = null;
  private ImageIcon stringIcon = null;
  private ImageIcon generalIcon = null;

	public LocalVariablesTreeCellRenderer() {
		super();
    folderIcon = new ImageIcon(LocalVariablesTreeCellRenderer.class.getResource("image/dir.gif"));
    objectIcon = new ImageIcon(LocalVariablesTreeCellRenderer.class.getResource("image/folder.gif"));
    stringIcon = new ImageIcon(LocalVariablesTreeCellRenderer.class.getResource("image/string.gif"));
    generalIcon = new ImageIcon(LocalVariablesTreeCellRenderer.class.getResource("image/smallbreak.gif"));
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
		LocalVariablesEntry entry = (LocalVariablesEntry)dmtn.getUserObject();

    switch(entry.getEntryType()) {
      case LocalVariablesEntry.LOCAL :
        setIcon(folderIcon);

        break;
      case LocalVariablesEntry.METHOD :
        setIcon(folderIcon);

        break;
      case LocalVariablesEntry.OBJECT :
        setIcon(objectIcon);

        break;
      case LocalVariablesEntry.ARRAY :
        setIcon(objectIcon);

        break;
      case LocalVariablesEntry.GENERAL :
        setIcon(generalIcon);

        break;
      case LocalVariablesEntry.STRING :
        setIcon(stringIcon);

        break;
      default :
      
    }
    StringBuffer text = new StringBuffer();
    if (entry.getName() != null)
      text.append(entry.getName());
    if (entry.getTypeName() != null)
      text.append(" (" + entry.getTypeName() + ")");
    if (entry.getDescription() != null)
      text.append(" = " + entry.getDescription());


    setText(text.toString());
		//setText(stringValue);
		return this;
	}
}
