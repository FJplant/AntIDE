/*
 * $Id: ParameterTable.java,v 1.2 1999/08/19 05:25:07 multipia Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.2 $
 */
package com.antsoft.ant.wizard.generalwizard;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.util.*;

/**
 *  ParameterTable
 */
public class ParameterTable extends JTable {
	/**  column size */
	int columns;
	/**  table model */
	DefaultTableModel model;

	/**
	 *  DBTable
	 */
	public ParameterTable( DefaultTableModel model, int columns ) {
		super( model );
		this.columns = columns;
		this.model = model;
		setSelectionMode( ListSelectionModel.SINGLE_SELECTION );

	}

	/**
	 *  getColumn
	 */
	public TableColumn getColumn( int column ) {
		return getColumnModel().getColumn( column );
	}


	/**
	 *  getSelectedCellEditor
	 */
	public TextFieldEditor getSelectedCellEditor( ) {
		return getCellEditor( getSelectedColumn() );
	}


	/**
	 *  getCellEditor
	 */
	public TextFieldEditor getCellEditor( int column ) {
		return ((TextFieldEditor)getColumn( column ).getCellEditor() );
	}

	
	/**
	 *  getCellRenderer
	 */
	public TextFieldRenderer getCellRenderer( int column ) {
		return ((TextFieldRenderer)getColumn( column ).getCellRenderer() );
	}


	/**
	 *  getCellSelectedRenderer
	 */
	public TextFieldRenderer getSelectedCellRenderer(  ) {
		return getCellRenderer( getSelectedColumn() );
	}
	/**
	 *  resetRow
	 */
	void resetRow( int row ) {
		for( int i = 0 ; i < columns ; i++ ) {
			getCellEditor( i ).setCellEditorValue( "" );
			getCellRenderer( i ).setCellRendererValue( "" );		
		}
	}	

	/** 
	 *  resetCell
	 */
	void resetCell( int row, int col ) {
		getCellEditor( col ).setCellEditorValue( (String)model.getValueAt( row, col ) );
		getCellRenderer( col ).setCellRendererValue( (String)model.getValueAt( row, col ) );	
	}


	/**
	 *  setEditingCell
	 */
	public void setEditingCell() {
		model.setValueAt( getCellEditor( getEditingRow() , getEditingColumn()  ).getCellEditorValue(), getEditingRow() , getEditingColumn() );
		model.setValueAt( getCellEditor( getSelectedRow() , getSelectedColumn()).getCellEditorValue(), getSelectedRow() , getSelectedColumn() );

	}


}
