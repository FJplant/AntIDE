
package com.antsoft.ant.wizard;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.*;


public class EventDialog extends JDialog implements ActionListener,KeyListener{

  JTextField packName = new JTextField(20);
  JTextField eventName = new JTextField("CustomEvent",20);
  JTextField listenerName = new JTextField("CustomListener",20);
  JLabel packNameLbl = new JLabel("Package Name");
  JLabel eventNameLbl = new JLabel("Event Name");
  JLabel listenerNameLbl = new JLabel("Listener Name");

  JTable table = new JTable();
  String[] colNames = {"Function Name", "Event ID"};
  DefaultTableModel model = new DefaultTableModel(colNames,1);
  JScrollPane sp = new JScrollPane(table);

  JButton add = new JButton("Add");
  JButton remove = new JButton("Remove");
  JButton ok = new JButton("OK");
  JButton cancel = new JButton("Cancel");

  JLabel lbl = new JLabel("Add member function of Listener interface and event id.");

  TitledBorder border1 = new TitledBorder("Event Name");
  TitledBorder border2 = new TitledBorder("Event method,ID");

  boolean isOk = false;
  String lastValue;

  public EventDialog(Frame frame, String title, boolean modal) {
    super(frame, title, modal);
    try  {
      jbInit();
      pack();

      setSize(400, 430);
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
    packNameLbl.setPreferredSize(new Dimension(100,20));
    packName.setPreferredSize(new Dimension(250,20));
    eventNameLbl.setPreferredSize(new Dimension(100,20));
    eventName.setPreferredSize(new Dimension(250,20));
    listenerNameLbl.setPreferredSize(new Dimension(100,20));
    listenerName.setPreferredSize(new Dimension(250,20));
    sp.setPreferredSize( new Dimension( 340, 100 ) );
    lbl.setPreferredSize( new Dimension(340,20) );

    JPanel p1 = new JPanel();
    p1.setLayout( new GridLayout(1,1) );
    p1.add(lbl);

    JPanel p10 = new JPanel();
    p10.setLayout(new FlowLayout(FlowLayout.LEFT));
    p10.add(packNameLbl);
    p10.add(packName);
    
    JPanel p2 = new JPanel();
    p2.setLayout( new FlowLayout(FlowLayout.LEFT) );
    p2.add(eventNameLbl);
    p2.add(eventName);
    eventName.addKeyListener(this);
    
    JPanel p3 = new JPanel();
    p3.setLayout( new FlowLayout(FlowLayout.LEFT) );
    p3.add(listenerNameLbl);
    p3.add(listenerName);
    listenerName.setEditable(false);


    JPanel p4 = new JPanel();
    p4.setLayout( new GridLayout(3,1) );
    p4.setBorder(border1);
    p4.add(p10);
    p4.add(p2);
    p4.add(p3);

    JPanel p5 = new JPanel();
    p5.setLayout( new GridLayout(1,1) );
    p5.add(sp);

    //table
    table.setModel(model);
    table.setRowHeight(20);
		model.setValueAt( "", 0, 0 );
		model.setValueAt( "", 0, 1 );

    //Button
    JPanel p6 = new JPanel();
    p6.setLayout( new FlowLayout(FlowLayout.CENTER) );
    p6.add(add);
    p6.add(remove);
    add.addActionListener(this);
    remove.addActionListener(this);

    JPanel p7 = new JPanel();
    p7.setLayout( new BorderLayout() );
    p7.setBorder(border2);
    p7.add(p1,BorderLayout.NORTH);
    p7.add(p5,BorderLayout.CENTER);
    p7.add(p6,BorderLayout.SOUTH);

    JPanel p8 = new JPanel();
    p8.setLayout(new BorderLayout());
    p8.add(p4,BorderLayout.NORTH);
    p8.add(p7,BorderLayout.CENTER);

    JPanel p9 = new JPanel();
    p9.setLayout( new FlowLayout(FlowLayout.CENTER) );
    p9.add(ok);
    p9.add(cancel);
    ok.addActionListener(this);
    cancel.addActionListener(this);

    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(p8,BorderLayout.CENTER);
    getContentPane().add(p9,BorderLayout.SOUTH);
    getContentPane().add(new JPanel(),BorderLayout.NORTH);
    getContentPane().add(new JPanel(),BorderLayout.EAST);
    getContentPane().add(new JPanel(),BorderLayout.WEST);
  }

  public boolean isOk(){
    return isOk;
  }

  public void actionPerformed(ActionEvent e){
    if(e.getSource() == ok){
      DefaultCellEditor editor = (DefaultCellEditor)table.getCellEditor(model.getRowCount()-1,1);
      lastValue = (String)editor.getCellEditorValue();
      isOk = true;
      dispose();
    }else if(e.getSource() == cancel){
      dispose();
    }else if(e.getSource() == add){
      model.addRow( colNames );
			model.setValueAt( "", model.getRowCount() - 1, 0 );
			model.setValueAt( "", model.getRowCount() - 1, 1 );
    }else if(e.getSource() == remove){
      int[] rows = table.getSelectedRows();
      for( int i=0; i<rows.length; i++ ){
        for( int j=rows[i]+1; j<model.getRowCount(); j++ ){
          model.setValueAt( model.getValueAt(j,0),j-1,0 );
          model.setValueAt( model.getValueAt(j,1),j-1,1 );
        }
        model.removeRow( model.getRowCount()-1 );
        if(i < rows.length-1) rows[i+1]--;
      }
    }
  }

  public void keyReleased(KeyEvent e){
    if( e.getSource() == eventName ){
      String en = eventName.getText();
      int index = en.indexOf("Event");
      if(index == -1)
        listenerName.setText(en+"Listener");
      else{
        String name = en.substring(0,index);
        listenerName.setText(name+"Listener");
      }
      e.consume();
    }
  }

  public void keyTyped(KeyEvent e){
  }

  public void keyPressed(KeyEvent e){

  }
}

 