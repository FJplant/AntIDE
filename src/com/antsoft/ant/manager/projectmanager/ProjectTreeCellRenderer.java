/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/manager/projectmanager/ProjectTreeCellRenderer.java,v 1.5 1999/08/30 08:08:05 remember Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.5 $
 */
package com.antsoft.ant.manager.projectmanager;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.tree.*;
import com.antsoft.ant.util.ColorList;
import com.antsoft.ant.util.ImageList;

public class ProjectTreeCellRenderer extends DefaultTreeCellRenderer {//JLabel implements TreeCellRenderer{
  private String imagePath;
	/**
	 *  Constructor
	 */
  public ProjectTreeCellRenderer() {
    super();
  }                                   

  /** Whether or not the item that was last configured is selected. */
//  protected boolean selected;

  /**
   * This is messaged from JTree whenever it needs to get the size
   * of the component or it wants to draw it.
   * This attempts to set the font based on value, which will be
   * a TreeNode.
   */
  public Component getTreeCellRendererComponent(JTree tree, Object value,
					  boolean selected, boolean expanded,
					  boolean leaf, int row,
						boolean hasFocus) {
    super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
    String stringValue = tree.convertValueToText(value, selected, expanded, leaf, row, hasFocus);

    if(selected) this.setForeground(ColorList.treeSelectionForegroundColor);
    else this.setBackground(ColorList.treeSelectionBackgroundColor);


    /* Tooltips used by the tree. */
    setToolTipText(stringValue);

    /* Set the color and the font based on the SampleData userObject. */
		DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode)value;
		if (dmtn instanceof ProjectPanelTreeNode) {
			ProjectPanelTreeNode pptn = (ProjectPanelTreeNode)dmtn;
			if (pptn.isFile()){
        setIcon(ImageList.openedIcon);
      }

			else if (pptn.isFolder()) {
				if (expanded) setIcon(ImageList.folderOpen);
				else setIcon(ImageList.folderClosed);
			}
		}
		else setIcon(ImageList.projectIcon);

    /* Set the text. */
    setText(stringValue);

    return this;
  }
}
