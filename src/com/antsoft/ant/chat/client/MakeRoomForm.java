package com.antsoft.ant.chat.client;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Vector;
import com.antsoft.ant.util.BorderList;
import com.antsoft.ant.chat.*;

/**
 * Login form
 *
 * @author kim sang kyun
 */
public class MakeRoomForm extends JPanel implements ActionListener{

  private JTextField nameTf, passwdTf;
  private JComboBox limitCbx;
  private JButton okBtn;
  private ChatClientFrame parent;

  public MakeRoomForm(ChatClientFrame parent) {
    this.parent = parent;

    setLayout(new BorderLayout());
    setBorder(BorderList.lightLoweredBorder);
    setBackground(Color.black);

    JLabel nameLbl = new JLabel("    NAME");
    nameLbl.setForeground(Color.yellow);
    nameLbl.setFont(new Font("DialogInput", Font.BOLD, 12));
    nameTf = new JTextField(15);
    nameTf.setFont(new Font("DialogInput", Font.BOLD, 12));

    JPanel nameP = new JPanel(new FlowLayout(FlowLayout.LEFT));
    nameP.add(nameLbl);
    nameP.add(nameTf);
    nameP.setBackground(Color.blue);

    JLabel passwdLbl = new JLabel("PASSWORD");
    passwdLbl.setForeground(Color.yellow);
    passwdLbl.setFont(new Font("DialogInput", Font.BOLD, 12));
    passwdTf = new JTextField(15);
    passwdTf.setFont(new Font("DialogInput", Font.BOLD, 12));

    JPanel passwdP = new JPanel(new FlowLayout(FlowLayout.LEFT));
    passwdP.setBackground(Color.blue);
    passwdP.add(passwdLbl);
    passwdP.add(passwdTf);

    JLabel limitLbl = new JLabel("   LIMIT");
    limitLbl.setForeground(Color.yellow);
    limitLbl.setFont(new Font("DialogInput", Font.BOLD, 12));
    limitCbx = new JComboBox();
    limitCbx.addItem("NO");
    for(int i=0;i<10; i++){
      limitCbx.addItem(""+i);
    }

    okBtn = new JButton("Make");
    okBtn.addActionListener(this);

    JPanel limitP = new JPanel(new FlowLayout(FlowLayout.LEFT));
    limitP.setBackground(Color.blue);
    limitP.add(limitLbl);
    limitP.add(limitCbx);
    limitP.add(okBtn);

    Box box = Box.createVerticalBox();
    box.add(nameP);
    box.add(passwdP);
    box.add(limitP);
    box.setBackground(Color.yellow);

    add(box, BorderLayout.CENTER);
    nameTf.requestFocus();
  }

  public void clear(){
    nameTf.setText("");
    passwdTf.setText("");
  }

  public void actionPerformed(ActionEvent e){

    String name = nameTf.getText();
    String passwd = passwdTf.getText();
    String limit = (String)limitCbx.getSelectedItem();

    if(name.trim().length() == 0){
      parent.setMessage("Name is Invalid");
    }

    else {
      try{
      if(parent.isExistRoom(name)) {
        parent.setMessage("Already exist room name...");
        return;
      }

      MakeRoomObject obj = new MakeRoomObject(Protocol.MAKEROOM);
      obj.user = parent.myInfo;

      Vector v = new Vector(5, 2);
      v.addElement(obj.user);
      int _limit = -1;

      if(!limit.equals("NO")) _limit = Integer.parseInt(limit);

      RoomInfo rInfo = new RoomInfo(name, passwd, v, _limit);
      obj.roomInfo = rInfo;

      try{
        parent.put(obj);
      }catch(Exception e1){
        parent.setMessage("Can't connect to Server!!");
        e1.printStackTrace();
      }
      clear();
      }catch(Exception e2){e2.printStackTrace();}
    }
  }
}

