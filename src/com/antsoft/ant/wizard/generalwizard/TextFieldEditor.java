/*
 * $Id: TextFieldEditor.java,v 1.2 1999/08/19 05:25:07 multipia Exp $
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
import java.awt.Component;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.util.*;
import com.antsoft.ant.util.*;


/**
 *  DBTableCellRenderer
 */
public class TextFieldEditor extends JTextField implements TableCellEditor {
	/** list */
	Vector list;

	/** listeners */
	protected transient Vector listeners;
	
	/** originalValue */
	protected transient String data;

	public TextFieldEditor( ) {
		super( );
		setPreferredSize( new Dimension( 80, 20 ) );
		
		list = new Vector( 1, 1 );
		list.addElement( new Boolean( true ) );

		setText( "" );
		listeners = new Vector();

	}

	/**
	 *  getTableCellEditorComponent
	 */
	public Component getTableCellEditorComponent( JTable table, Object value, boolean isSelected, int row, int col ) { 
		if( value instanceof JTextField ) {
			data =  ((JTextField)value).getText();
			setText( data );
		} else if( value instanceof String ) {
			data =  (String)value;
			setText( data );
		}	


		return this;
	}


	/**
	 *  addedNewRow
	 */
	public void addedNewRow( ) {
		list.addElement( new Boolean( true ) );
	}

	/**
	 *  removedRow
	 */
	public void removedRow( int row ) {
		System.out.println( "total : " + list.size() + " row : " + row );
		list.removeElementAt( row );
	}

	/**
	 *  cancelCellEditing
	 */
	public void cancelCellEditing() {
		fireEditingCanceled();
	}

	/**
	 * getCellEditorValue
	 */
	public Object getCellEditorValue() {
		return getText();
	}

	/**
	 *  setCellEditorValue
	 */
	public void setCellEditorValue( String data ) {
		this.data = data;
		setText( data );
	}


	/**
	 *  isCellEditable
	 */
	public boolean isCellEditable( EventObject eo ) {
		return true;
	}

	/**
	 *  shouldSelectCell
	 */
	public boolean shouldSelectCell( EventObject eo ) {
		return true;
	}

	/**
	 *  stopCellEditing
	 */
	public boolean stopCellEditing() {
		fireEditingStopped();
		return true;
	}

	/**
	 *  addCellEditorListener
	 */
	public void addCellEditorListener( CellEditorListener e ) {
		listeners.addElement( e );
	}

	/**
	 *  removeCellEditorListener
	 */
	public void removeCellEditorListener( CellEditorListener cel ) {
		listeners.removeElement( cel );
	}

	/**
	 *  fireEditingCanceled
	 */
	protected void fireEditingCanceled() {
		setText( data );
		ChangeEvent ce = new ChangeEvent( this );
		for( int i = listeners.size()  ; i >= 0 ; i-- ) {
			( ( CellEditorListener)listeners.elementAt( i ) ).editingCanceled( ce );
		}
	}

	/**
	 *  fireEditingStopped
	 */
	protected void fireEditingStopped() {
		ChangeEvent ce = new ChangeEvent( this );
		for( int i = listeners.size() - 1; i >= 0 ; i-- ) {
			( ( CellEditorListener)listeners.elementAt( i ) ).editingStopped( ce );
		}
	}
}
