/*
 * $Id: TextFieldRenderer.java,v 1.2 1999/08/19 05:25:07 multipia Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.2 $
 */
package com.antsoft.ant.wizard.generalwizard;

import javax.swing.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import java.util.*;
import com.antsoft.ant.util.*;


/**
 *  DBTableCellRenderer
 */
public class TextFieldRenderer extends DefaultTableCellRenderer {
	/** data */
	String data;

	public TextFieldRenderer(  ) {
		super();
		setOpaque( true );
		setRequestFocusEnabled( true );
	}

	/**
	 *  getTableCellRendererComponent
	 */
	public Component getTableCellRendererComponent( JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col ) {

		if( value instanceof JTextField ) {
			data =  ((JTextField)value).getText();
			setText( data );
		} else if( value instanceof String ) {
			data =  (String)value;
			setText( data );
		}

		return super.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, col );
	}

	/**
	 *  setCellRendererValue
	 */
	public void setCellRendererValue( String data ) {
		this.data = data;
		setText( data );
	}
}
