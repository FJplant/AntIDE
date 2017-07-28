/*
` $Header: /AntIDE/source/ant/wizard/BeanDialog.java 5     99-05-31 4:37p Lila $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 5 $
 */
package com.antsoft.ant.wizard;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.lang.*;

/**
 * @author Youngkyung Kim
 */
public class BeanDialog extends JDialog implements ActionListener, ItemListener, KeyListener{

  Frame frame;

  private CardLayout card = new CardLayout();
  JPanel p1 = new JPanel();
  private int cardState = 1;
  String lastValue;    //table�� ���� ������ �����͸� ������ �ִ� ���� ����

  private boolean isOk = false;

  JButton back = new JButton("<-Back");
  JButton next = new JButton("Next->");
  JButton finish = new JButton("Finish");
  JButton cancel = new JButton("Cancel");

  //ù��° �г�
  BeanClassPanel classPanel = new BeanClassPanel();
  //�ι�° �г�
  EventOptionPanel eventPanel = new EventOptionPanel();
  //����° �г�
  PropertyPanel propertyPanel = new PropertyPanel();
  //�׹�° �г�
  FireEventPanel fireEventPanel = new FireEventPanel();
  //�ټ���° �г�
  EtcPanel etcPanel = new EtcPanel();
  
  //property dialog
  PropertyDialog proDlg;


  public BeanDialog(Frame frame, String title, boolean modal) {

    super(frame, title, modal);

    this.frame = frame;
    try  {
      jbInit();
      pack();

      proDlg = new PropertyDialog(frame,"Property Dialog",true);
      setSize(400, 300);
      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      Dimension frameSize = this.getSize();
      if (frameSize.height > screenSize.height) frameSize.height = screenSize.height;
      if (frameSize.width > screenSize.width) frameSize.width = screenSize.width;
      this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);

      setResizable(false);
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  void jbInit() throws Exception {
    //cardLayout
    p1.setLayout(card);
    p1.add( classPanel, "beanClass" );
    p1.add( eventPanel, "event" );
    p1.add( propertyPanel, "property" );
    p1.add( fireEventPanel, "fireEvent" );
    p1.add( etcPanel, "etc" );

    //button
    JPanel p2 = new JPanel();
    p2.setLayout( new FlowLayout(FlowLayout.CENTER) );
    p2.add(back);
    p2.add(next);
    p2.add(finish);
    p2.add(cancel);
    back.setEnabled(false);
    next.setEnabled(false);
    finish.setEnabled(false);

    //��ư���� listener�� �߰�
    back.addActionListener(this);
    next.addActionListener(this);
    finish.addActionListener(this);
    cancel.addActionListener(this);

    //ù��° �г��� ����� listener�� �߰�
    classPanel.createEvent.addItemListener(this);
    classPanel.eventName.addKeyListener(this);
    classPanel.className.addKeyListener(this);

    //�ι�° �г��� ������� listener�� �߰�
    eventPanel.add.addActionListener(this);
    eventPanel.remove.addActionListener(this);

    //������ �г��� ������� listener�� �߰�
    propertyPanel.add.addActionListener(this);
    propertyPanel.remove.addActionListener(this);
    propertyPanel.edit.addActionListener(this);

    card.show(p1, "beanClass");

    getContentPane().setLayout( new BorderLayout() );
    getContentPane().add( p1, BorderLayout.CENTER );
    getContentPane().add( p2, BorderLayout.SOUTH );
    getContentPane().add( new JPanel(), BorderLayout.NORTH );
    getContentPane().add( new JPanel(), BorderLayout.EAST );
    getContentPane().add( new JPanel(), BorderLayout.WEST );

  }

  private void clear(){

  }

  public boolean isOk(){
    return isOk;
  }

  //event ó�� -------------------------------------------------------
  public void actionPerformed( ActionEvent e ){
    DefaultTableModel eModel = eventPanel.model;
    DefaultTableModel pModel = propertyPanel.model;

    JTable eTable = eventPanel.table;
    JTable pTable = propertyPanel.table;

    //��ư �̺�Ʈ
    if( e.getSource() == back ){
      if( cardState == 2 ){
        back.setEnabled(false);
        next.setEnabled(true);
        finish.setEnabled(false);
        card.show(p1,"beanClass");
        cardState--;
      }else if( cardState == 3 ){
        if( classPanel.createEvent.isSelected() ){
          back.setEnabled(true);
          next.setEnabled(true);
          finish.setEnabled(false);
          card.show(p1,"event");
          cardState--;
        }else{
          back.setEnabled(false);
          next.setEnabled(true);
          finish.setEnabled(false);
          card.show(p1,"beanClass");
          cardState = cardState-2;
        }
      }else if( cardState == 4 ){
        back.setEnabled(true);
        next.setEnabled(true);
        finish.setEnabled(false);
        card.show(p1,"property");
        cardState--;
      }else if( cardState == 5 ){
        back.setEnabled(true);
        next.setEnabled(true);
        finish.setEnabled(false);
        card.show(p1,"fireEvent");
        cardState--;
      }

    }else if( e.getSource() == next ){
      DefaultCellEditor editor = (DefaultCellEditor)eventPanel.table.getCellEditor(eModel.getRowCount()-1,1);
      lastValue = (String)editor.getCellEditorValue();
      if( cardState == 1 ){
        if( classPanel.createEvent.isSelected() ){
          back.setEnabled(true);
          next.setEnabled(true);
          finish.setEnabled(false);
          card.show(p1,"event");
          cardState++;
        }else{
          back.setEnabled(true);
          next.setEnabled(true);
          finish.setEnabled(false);
          card.show(p1,"property");
          cardState = cardState+2;
        }
      }else if( cardState == 2 ){
        back.setEnabled(true);
        next.setEnabled(true);
        finish.setEnabled(false);
        card.show(p1,"property");
        cardState++;
      }else if( cardState == 3 ){
        back.setEnabled(true);
        next.setEnabled(true);
        finish.setEnabled(false);
        card.show(p1,"fireEvent");
        cardState++;
      }else if( cardState == 4 ){
        back.setEnabled(true);
        next.setEnabled(false);
        finish.setEnabled(true);
        card.show(p1,"etc");
        cardState++;
      }

    }else if( e.getSource() == finish ){
      isOk = true;
      dispose();
    }else if( e.getSource() == cancel ){
      dispose();
    }

    //�ι�° �г��� ��ư �̺�Ʈ( add, remove )
    else if( e.getSource() == eventPanel.add ){

      eModel.addRow( eventPanel.colNames );
			eModel.setValueAt( "", eModel.getRowCount() - 1, 0 );
			eModel.setValueAt( "", eModel.getRowCount() - 1, 1 );


    }else if( e.getSource() == eventPanel.remove ){
      int[] rows = eTable.getSelectedRows();
      for( int i=0; i<rows.length; i++ ){
        for( int j=rows[i]+1; j<eModel.getRowCount(); j++ ){
          eModel.setValueAt( eModel.getValueAt(j,0),j-1,0 );
          eModel.setValueAt( eModel.getValueAt(j,1),j-1,1 );
        }
        eModel.removeRow( eModel.getRowCount()-1 );
        if(i < rows.length-1) rows[i+1]--;
      }
    }

    //������ �г��� ��ư �̺�Ʈ
    else if( e.getSource() == propertyPanel.add ){
      proDlg = new PropertyDialog( frame, "Property Dialog", true );
      proDlg.setVisible(true);

      if( proDlg.isOk() ){
        pModel.addRow( propertyPanel.colNames );
	  		pModel.setValueAt( proDlg.name.getText(), pModel.getRowCount() - 1, 0 );
		  	pModel.setValueAt( proDlg.type.getSelectedItem(), pModel.getRowCount() - 1, 1 );

        if( proDlg.read.isSelected() )
          pModel.setValueAt( "V", pModel.getRowCount() - 1, 2 );
        else
          pModel.setValueAt( "", pModel.getRowCount() - 1, 2 );

        if( proDlg.write.isSelected() )
          pModel.setValueAt( "V", pModel.getRowCount() - 1, 3 );
        else
          pModel.setValueAt( "", pModel.getRowCount() - 1, 3 );

        if( proDlg.bound.isSelected() )
          pModel.setValueAt( "V", pModel.getRowCount() - 1, 4 );
        else
          pModel.setValueAt( "", pModel.getRowCount() - 1, 4 );

        if( proDlg.constrained.isSelected() )
          pModel.setValueAt( "V", pModel.getRowCount() - 1, 5 );
        else
          pModel.setValueAt( "", pModel.getRowCount() - 1, 5 );
      }

    }else if( e.getSource() == propertyPanel.edit ){
      proDlg = new PropertyDialog( frame, "Property Dialog", true );

      //property dialog setting
      int row = pTable.getSelectedRow();
      proDlg.name.setText( (String)pModel.getValueAt(row,0) );
      proDlg.type.setSelectedItem( pModel.getValueAt(row,1) );

      if( pModel.getValueAt(row,2).equals("V") )
        proDlg.read.setSelected(true);

      if( pModel.getValueAt(row,3).equals("V") )
        proDlg.write.setSelected(true);

      if( pModel.getValueAt(row,4).equals("V") )
        proDlg.bound.setSelected(true);

      if( pModel.getValueAt(row,5).equals("V") )
        proDlg.constrained.setSelected(true);

      proDlg.setVisible(true);

      if( proDlg.isOk() ){
	  		pModel.setValueAt( proDlg.name.getText(), row, 0 );
		  	pModel.setValueAt( proDlg.type.getSelectedItem(), row, 1 );

        if( proDlg.read.isSelected() )
          pModel.setValueAt( "V", row, 2 );
        else
          pModel.setValueAt( "", row, 2 );

        if( proDlg.write.isSelected() )
          pModel.setValueAt( "V", row, 3 );
        else
          pModel.setValueAt( "", row, 3 );

        if( proDlg.bound.isSelected() )
          pModel.setValueAt( "V", row, 4 );
        else
          pModel.setValueAt( "", row, 4 );

        if( proDlg.constrained.isSelected() )
          pModel.setValueAt( "V", row, 5 );
        else
          pModel.setValueAt( "", row, 5 );
      }

    }else if( e.getSource() == propertyPanel.remove ){
      int[] rows = pTable.getSelectedRows();
      for( int i=0; i<rows.length; i++ ){
        for( int j=rows[i]+1; j<pModel.getRowCount(); j++ ){
          pModel.setValueAt( pModel.getValueAt(j,0),j-1,0 );
          pModel.setValueAt( pModel.getValueAt(j,1),j-1,1 );
          pModel.setValueAt( pModel.getValueAt(j,2),j-1,2 );
          pModel.setValueAt( pModel.getValueAt(j,3),j-1,3 );
          pModel.setValueAt( pModel.getValueAt(j,4),j-1,4 );
          pModel.setValueAt( pModel.getValueAt(j,5),j-1,5 );
        }
        pModel.removeRow( pModel.getRowCount()-1 );
        if(i < rows.length-1) rows[i+1]--;
      }
    }
  }

  public void itemStateChanged( ItemEvent e ){
    if( classPanel.createEvent.isSelected() ){
      classPanel.eventNameLbl.setEnabled(true);
      classPanel.eventName.setEnabled(true);
      classPanel.listenerNameLbl.setEnabled(true);
      classPanel.listenerName.setEnabled(true);
    }else{
      classPanel.eventNameLbl.setEnabled(false);
      classPanel.eventName.setDisabledTextColor( Color.lightGray );
      classPanel.eventName.setEnabled(false);
      classPanel.listenerNameLbl.setEnabled(false);
      classPanel.listenerName.setDisabledTextColor( Color.lightGray );
      classPanel.listenerName.setEnabled(false);
    }
  }

  public void keyPressed( KeyEvent e ){
  }

  public void keyReleased( KeyEvent e ){
    if( e.getSource() == classPanel.eventName ){
      String eventName = classPanel.eventName.getText();
      int index = eventName.indexOf("Event");
      if(index == -1)
        classPanel.listenerName.setText(eventName+"Listener");
      else{
        String name = eventName.substring(0,index);
        classPanel.listenerName.setText(name+"Listener");
      }
      e.consume();
    }else if( e.getSource() == classPanel.className ){
      next.setEnabled(true);
      e.consume();
    }
  }

  public void keyTyped( KeyEvent e ){
  }
}
