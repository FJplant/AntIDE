/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/main/MetalThemeMenu.java,v 1.3 1999/07/22 03:00:45 multipia Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.3 $
 * Author:       Kwon, Young Mo
 * $History: MetalThemeMenu.java $
 * 
 * *****************  Version 2  *****************
 * User: Multipia     Date: 99-05-16   Time: 11:49p
 * Updated in $/AntIDE/source/ant/main
 * 
 * *****************  Version 1  *****************
 * User: Multipia     Date: 99-05-02   Time: 2:38p
 * Created in $/AntIDE/source/ant/main
 * Metal Theme 들을 표시하는 메뉴 Class
 */

package com.antsoft.ant.main;

import javax.swing.plaf.metal.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Metal Theme 목록들을 표시하는 Menu Class 임
 */
public class MetalThemeMenu extends JMenu implements ActionListener{
  MetalTheme[] themes;
  public MetalThemeMenu(String name, MetalTheme[] themeArray) {
    super(name);
    themes = themeArray;
    ButtonGroup group = new ButtonGroup();
    for (int i = 0; i < themes.length; i++) {
      JRadioButtonMenuItem item = new JRadioButtonMenuItem( themes[i].getName() );
	    group.add(item);
      add( item );
    	item.setActionCommand(i+"");
    	item.addActionListener(this);
    	if ( i == 0)
   	    item.setSelected(true);
    }
  }

  public void actionPerformed(ActionEvent e) {
    String numStr = e.getActionCommand();
    MetalTheme selectedTheme = themes[ Integer.parseInt(numStr) ];
    MetalLookAndFeel.setCurrentTheme(selectedTheme);
    try {
    	UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
    } catch (Exception ex) {
      System.out.println("Failed loading Metal");
    	System.out.println(ex);
    }
  }
}
