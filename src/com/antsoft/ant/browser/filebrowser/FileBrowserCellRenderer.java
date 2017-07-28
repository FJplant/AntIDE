/*
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co.
 * All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/browser/filebrowser/FileBrowserCellRenderer.java,v 1.8 1999/07/27 09:20:06 itree Exp $
 * $Revision: 1.8 $
 */
package com.antsoft.ant.browser.filebrowser;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

import com.antsoft.ant.util.ImageList;

/**
 *  class FileBrowserCellRenderer
 *
 *  @author Lee, Chul-Mok
 */
public class FileBrowserCellRenderer extends DefaultTreeCellRenderer {

	/**
	 *  Constructor
	 */
	public FileBrowserCellRenderer() {
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
		FileBrowserEntry entry = (FileBrowserEntry)dmtn.getUserObject();

		switch(entry.getType()) {
			case FileBrowserEntry.COMPUTER:
				setIcon(ImageList.computerIcon);
				break;
			case FileBrowserEntry.DRIVER:
				setIcon(ImageList.driverIcon);
				break;
			case FileBrowserEntry.DIR:
				setIcon(ImageList.dirIcon);
				break;
			case FileBrowserEntry.OPENDIR:
				setIcon(ImageList.opendirIcon);
				break;
      case FileBrowserEntry.JAVA:
        setIcon(ImageList.javaIcon);
        break;
      case FileBrowserEntry.JAVASCRIPT:
        setIcon(ImageList.textIcon);
        break;
      case FileBrowserEntry.PERL:
        setIcon(ImageList.textIcon);
        break;          
      case FileBrowserEntry.TEXT:
        setIcon(ImageList.textIcon);
        break;
      case FileBrowserEntry.HTML:
        setIcon(ImageList.htmlIcon);
        break;
      case FileBrowserEntry.GIF:
        setIcon(ImageList.gifIcon);
        break;
      case FileBrowserEntry.JPG:
        setIcon(ImageList.jpgIcon);
        break;
      case FileBrowserEntry.BMP:
        setIcon(ImageList.bmpIcon);
        break;

			default:
        setIcon(ImageList.tempIcon);
		}

		setText(stringValue);
		return this;
	}
}

/*
 * $Log: FileBrowserCellRenderer.java,v $
 * Revision 1.8  1999/07/27 09:20:06  itree
 * no message
 *
 * Revision 1.7  1999/07/27 04:53:53  multipia
 * Java Script, Perl 추가
 *
 * Revision 1.6  1999/07/27 04:45:52  multipia
 * Header 부분 추가
 *
 */
