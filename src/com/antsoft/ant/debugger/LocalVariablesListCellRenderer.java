
//Title:        AntDebugger
//Version:      
//Copyright:    Copyright (c) 1998
//Author:       Jae-Hyoung Bae
//Company:      antsoft
//Description:  hello..


package com.antsoft.ant.debugger;

import java.awt.*;
import javax.swing.*;
import sun.tools.debug.*;

public class LocalVariablesListCellRenderer extends JPanel implements ListCellRenderer {
  private JLabel variableName;
  private JLabel variableValue;
  private boolean isObject;

  private RemoteStackVariable remoteStackVariable;
  private RemoteValue remoteValue;
  public LocalVariablesListCellRenderer() {
    variableName = new JLabel();
    variableValue = new JLabel();

    /*
    add(variableName);
    add(variableValue);
    */

    setLayout(new BorderLayout());
    add( variableName, BorderLayout.CENTER);
    add( variableValue, BorderLayout.EAST);
  }

  public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
    //TODO: implement this javax.swing.ListCellRenderer method;
    JLabel label;
    this.setBackground(Color.white);
    variableName.setBackground( Color.white );
    variableValue.setBackground( list.getBackground());
    if( value instanceof String){
      label = new JLabel(value.toString());
      //label.setBackground(Color.green);
      label.setForeground(Color.black);
      return label;
    }else if( value instanceof RemoteStackVariable){
      //System.out.println("[L_V_L_C_Renderer] value instance of RSV : " + value.toString());
      RemoteStackVariable rsv = (RemoteStackVariable)value;
      this.remoteStackVariable = rsv;

      StringBuffer buffer = new StringBuffer();
      //buffer.append("  " + rsv.getName());
      variableName.setText(rsv.getName());
      RemoteValue val = null;
      if (rsv.inScope()) {
        val = rsv.getValue();
        this.remoteValue = val;
        //buffer.append(" = " + (val == null? "null" : val.toString()) );
        variableValue.setText((val == null)? "null" : val.toString());
        if (val != null)
        isObject = val.isObject();
        //System.out.println("[L_V_L_C_Renderer] " + rsv.getName() +" is " + ((val.isObject())?"Object":"variable"));
      } else {
        //buffer.append(" is not in scope");
        variableValue.setText( "Not in Scope");
      }
    }else {
      variableName.setText("Error");
      variableValue.setText("");
    }
    return this;
  }
}