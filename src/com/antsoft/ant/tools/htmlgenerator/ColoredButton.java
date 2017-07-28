/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/tools/htmlgenerator/ColoredButton.java,v 1.3 1999/07/22 03:09:23 multipia Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.3 $
 */
package com.antsoft.ant.tools.htmlgenerator;

import javax.swing.event.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import com.antsoft.ant.util.BorderList;

public class ColoredButton extends JButton implements ActionListener{
  public ColoredButton(){
    super();
    addActionListener(this);
    setBorder(BorderList.raisedBorder);
  }

  public void actionPerformed(ActionEvent e){
    Color col = JColorChooser.showDialog(this, "Select Color", Color.white);
    if(col != null){
      setBackground(col);
    }
  }
}

