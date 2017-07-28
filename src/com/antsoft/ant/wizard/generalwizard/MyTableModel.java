/*
 * $Id: MyTableModel.java,v 1.2 1999/08/19 05:25:07 multipia Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.2 $
 */
package com.antsoft.ant.wizard.generalwizard;

import javax.swing.table.*;

public class MyTableModel extends DefaultTableModel {

  static String[] colNames = {"Name", "Type", "Read", "Write", "Bound", "Constrained"};
  Object[] rowData = { "","",new Boolean(false),new Boolean(false),new Boolean(false),new Boolean(false) };

  public MyTableModel(){
    super(colNames,0);
  }

  public int getColumnCount() {
		return colNames.length;
	}

  public String getColumnName(int columnIndex){
    return colNames[columnIndex];
  }

  public Class getColumnClass(int index){
    if((index == 0) || (index == 1)) return String.class;
    else return Boolean.class;
  }

  public int getColumnIndex( Object identifiers ) {
		String title = (String)identifiers;

		for( int i = 0 ; i < colNames.length ; i++ )
			if( colNames[i].equals( title ) ) return i;

		return -1;
	}

  public boolean getBooleanValue(int row, int col){
    Boolean value = (Boolean)super.getValueAt(row,col);
    return value.booleanValue();
  }

  public void addRow(){
    super.addRow(rowData);
  }
}
