/*
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co.
 * All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/browser/sourcebrowser/SourceBrowserTreeCellRenderer.java,v 1.6 1999/08/27 07:24:23 remember Exp $
 * $Revision: 1.6 $
 * SourceBrowser를 멋있게(?) 보여주기 위한 Renderer
 */

package com.antsoft.ant.browser.sourcebrowser;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

import com.antsoft.ant.util.ImageList;
import com.antsoft.ant.codecontext.codeeditor.EventContent;

/**
 *  class SourceBrowserTreeCellRenderer
 *
 *  @author Jinwoo Baek
 */
public class SourceBrowserTreeCellRenderer extends DefaultTreeCellRenderer {

	/**                                    
	 *  Constructor
	 */
	public SourceBrowserTreeCellRenderer() {
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
		EventContent currentEntry = (EventContent)dmtn.getUserObject();
		// set icon by TYPE
		switch(currentEntry.getContentType()) {
		case EventContent.FILEROOTNODE:
			setIcon(ImageList.fileRootIcon);
			break;
    case EventContent.IMPORTROOTNODE:
      setIcon(ImageList.fileRootIcon);
      break;  
		case EventContent.PACKAGE:
			setIcon(ImageList.packageIcon);
			break;
		case EventContent.CLASS:
			setIcon(ImageList.classIcon);
			break;
		case EventContent.INTERFACE:
			setIcon(ImageList.interfaceIcon);
			break;
		case EventContent.ATTR:
			setIcon(ImageList.memberIcon);
			break;
		case EventContent.OPER:
			setIcon(ImageList.methodIcon);
			break;
		case EventContent.INNER:
			setIcon(ImageList.innerIcon);
			break;
		case EventContent.IMPORT:
			setIcon(ImageList.packageBrowserLibNodeIcon);
			break;
		default:
		}
		setText(stringValue);
		return this;
	}
}

