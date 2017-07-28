package com.antsoft.ant.chat.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import com.antsoft.ant.chat.*;

public class RoomPanel extends JPanel implements ActionListener{

  private JSplitPane sp;
  private JTextArea ta;
  private JList list;
  private DefaultListModel listM;
  private JTextField tf;
  private ChatClientFrame parent;
  private JButton exitB;
  private String name;

  public RoomPanel(ChatClientFrame parent) {
    this.parent = parent;

    setLayout(new BorderLayout());
    ta = new JTextArea();
    ta.setEditable(false);
    JScrollPane taPane = new JScrollPane(ta);

    listM = new DefaultListModel();
    list = new JList(listM);

    JScrollPane listPane = new JScrollPane(list);

    sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, taPane, listPane);
    int width = getSize().width;
    sp.setDividerLocation(330);

    tf = new JTextField();
    tf.addActionListener(this);

    exitB = new JButton("Leave");
    exitB.addActionListener(this);

    JPanel bottomP = new JPanel(new BorderLayout());
    bottomP.add(tf, BorderLayout.CENTER);
    bottomP.add(exitB, BorderLayout.EAST);

    add(sp, BorderLayout.CENTER);
    add(bottomP, BorderLayout.SOUTH);

    tf.requestFocus();
  }

  public void setFocus(){
    tf.requestFocus();
  }

  public void addUser(User u){
    for(int i=0; i<listM.size(); i++){
      User u2 = (User)listM.elementAt(i);
      if(u2.equals(u)) return;
    }

    listM.addElement(u);
  }

  public void appendMessage(String fromID, String msg){
    String str = "["+fromID+"] " + msg + "\n";
    ta.append(str);
  }

  public void setRoomInfo(RoomInfo info){
    for(int i=0; i<info.members.size(); i++){
      User u = (User)info.members.elementAt(i);
      listM.addElement(u);
    }

    this.name = info.name;
  }

  public void removeUser(User u){
    listM.removeElement(u);
  }

  public String getName(){
    return name;
  }

  public void actionPerformed(ActionEvent e){
    if(e.getSource() == exitB){
      parent.leaveRoom(name);
   }
    else if(e.getSource() == tf){
      parent.sendMessage(name, tf.getText());
      tf.setText("");
    }
  }
}