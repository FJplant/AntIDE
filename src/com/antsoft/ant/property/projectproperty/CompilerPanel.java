/*
 * $Header: /AntIDE/source/ant/property/projectproperty/CompilerPanel.java 2     99-05-16 11:57p Multipia $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 2 $
 */
package com.antsoft.ant.property.projectproperty;

import java.awt.*;
import javax.swing.*;

/**
 * compile option을 정하는 panel
 *
 * @author kim sang kyun
 */
public class CompilerPanel extends JPanel {

  JCheckBox optimizeCbx, warningCbx, verboseCbx, depriCbx;
  public CompilerPanel() {
    optimizeCbx = new JCheckBox("Optimize", false);
    warningCbx = new JCheckBox("Generate warning messages", true);
    verboseCbx = new JCheckBox("Verbose output messages", true);
    depriCbx = new JCheckBox("Deprication", true);
  
  }
}

 