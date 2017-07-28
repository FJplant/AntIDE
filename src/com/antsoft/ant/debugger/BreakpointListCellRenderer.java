
//Title:        AntDebugger
//Version:      
//Copyright:    Copyright (c) 1998
//Author:       배재형
//Company:      개미소프트
//Description:  안뇽하세요.
/* $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/debugger/BreakpointListCellRenderer.java,v 1.4 1999/07/29 01:36:52 itree Exp $
 * $Revision: 1.4 $
 * $History: BreakpointListCellRenderer.java $
 */

package com.antsoft.ant.debugger;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

public class BreakpointListCellRenderer extends JPanel implements ListCellRenderer {
  private BreakpointPanel breakpointPanel;
  private JLabel classLabel;
  private JLabel methodLabel;
  private JLabel lineLabel;

  public BreakpointListCellRenderer( BreakpointPanel panel) {
    this.breakpointPanel = panel;

    classLabel = new JLabel();
    classLabel.setOpaque(true);
    classLabel.setBorder( LineBorder.createGrayLineBorder());
    methodLabel = new JLabel();
    methodLabel.setOpaque(true);
    methodLabel.setBorder( LineBorder.createGrayLineBorder());
    lineLabel = new JLabel();
    lineLabel.setOpaque(true);
    lineLabel.setBorder( LineBorder.createGrayLineBorder());
    setLayout(new GridLayout(1, 3, 1, 1));
    add(classLabel);
    add(methodLabel);
    add(lineLabel);
  }

  public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
    //TODO: implement this javax.swing.ListCellRenderer method;
    if( value != null ){
      if( value instanceof BreakpointEvent){
        BreakpointEvent ele = (BreakpointEvent)value;
        classLabel.setText(ele.getClazz());
        methodLabel.setText( ele.getMethod() );
        lineLabel.setText( String.valueOf( ele.getLine() ));
      }
    }else{
      classLabel.setText("ERROR");
      methodLabel.setText("ERROR");
      lineLabel.setText("ERROR");
    }
    if( isSelected ){
      setBackground( list.getSelectionBackground() );
      setForeground( list.getSelectionForeground() );
    }else{
      setBackground( list.getBackground() );
      setForeground( list.getForeground() );
    }
    return this;
  }
}