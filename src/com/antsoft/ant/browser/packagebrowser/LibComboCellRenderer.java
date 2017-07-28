/*
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co.
 * All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/browser/packagebrowser/LibComboCellRenderer.java,v 1.5 1999/07/24 01:18:21 remember Exp $
 * $Revision: 1.5 $
 * $History: LibComboCellRenderer.java $
 * 
 * *****************  Version 3  *****************
 * User: Remember     Date: 99-06-10   Time: 6:06p
 * Updated in $/AntIDE/source/ant/browser/packagebrowser
 * 
 * *****************  Version 2  *****************
 * User: Multipia     Date: 99-05-16   Time: 11:25p
 * Updated in $/AntIDE/source/ant/browser/packagebrowser
 * 
 * *****************  Version 1  *****************
 * User: Remember     Date: 99-05-06   Time: 10:55p
 * Created in $/AntIDE/source/ant/browser/packagebrowser
 * 새로 했음
 * 
 * *****************  Version 1  *****************
 * User: Remember     Date: 98-10-19   Time: 8:57a
 * Created in $/JavaProjects/src/ant/browser/packagebrowser
 *
 */
package com.antsoft.ant.browser.packagebrowser;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import java.awt.Color;
import java.awt.Component;
import com.antsoft.ant.util.ColorList;
import com.antsoft.ant.util.ImageList;
/**
 * library combobox cell renderer class
 *
 * @author Kim sang kyun
 */
final class LibComboCellRenderer extends JLabel implements ListCellRenderer {
  /** default constructor */
  public LibComboCellRenderer() {
    setOpaque(true);
  }

  final public Component getListCellRendererComponent(JList list, Object value, int index,
                                                boolean isSelected, boolean cellHasFocus){
    setIcon(ImageList.packageBrowserLibNodeIcon);
    setText(value.toString());
    setBackground(isSelected ? ColorList.listSelectionBackgroundColor : Color.white);
    setForeground(isSelected ? ColorList.listSelectionForegroundColor : Color.black);

    return this;
 }
}

