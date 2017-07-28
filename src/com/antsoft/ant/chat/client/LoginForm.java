package com.antsoft.ant.chat.client;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.antsoft.ant.util.BorderList;
import com.antsoft.ant.chat.*;

/**
 * Login form
 *
 * @author kim sang kyun
 */
public class LoginForm extends JPanel implements ActionListener{

  private JTextField idTf;
  private JPasswordField passwdTf;
  private JButton okBtn;
  private ChatClientFrame parent;

  public static final String ID_ERROR_MESSAGE = "Invalid ID";
  public static final String PASSWORD_ERROR_MESSGE = "Invalid PASSWORD";

  public LoginForm(ChatClientFrame parent) {
    this.parent = parent;

    setLayout(new BorderLayout());
    setBorder(BorderList.lightLoweredBorder);
    setBackground(Color.black);

    JLabel idLbl = new JLabel("      ID");
    idLbl.setForeground(Color.yellow);
    idLbl.setFont(new Font("DialogInput", Font.BOLD, 12));
    idTf = new JTextField(15);
    idTf.setFont(new Font("DialogInput", Font.BOLD, 12));

    JPanel idP = new JPanel(new FlowLayout(FlowLayout.LEFT));
    idP.add(idLbl);
    idP.add(idTf);
    idP.setBackground(Color.blue);

    JLabel passwdLbl = new JLabel("PASSWORD");
    passwdLbl.setForeground(Color.yellow);
    passwdLbl.setFont(new Font("DialogInput", Font.BOLD, 12));
    passwdTf = new JPasswordField(15);
    passwdTf.setFont(new Font("DialogInput", Font.BOLD, 12));
    okBtn = new JButton("Login");
    okBtn.addActionListener(this);

    JPanel passwdP = new JPanel(new FlowLayout(FlowLayout.LEFT));
    passwdP.setBackground(Color.blue);
    passwdP.add(passwdLbl);
    passwdP.add(passwdTf);
    passwdP.add(okBtn);

    Box box = Box.createVerticalBox();
    box.add(idP);
    box.add(passwdP);
    box.setBackground(Color.yellow);

    add(box, BorderLayout.CENTER);
    idTf.requestFocus();
  }

  public void clear(){
    idTf.setText("");
    passwdTf.setText("");
  }

  public void actionPerformed(ActionEvent e){

    String id = idTf.getText();
    String passwd = new String( passwdTf.getPassword() );
    passwdTf.setText("");
    idTf.setText("");

    if(id.trim().length() == 0){
      parent.setMessage(ID_ERROR_MESSAGE);
    }

    else if( passwd.trim().length() == 0 ){
      parent.setMessage(PASSWORD_ERROR_MESSGE);
    }

    else {
      if(!parent.isConnected()) parent.connectToServer();

      SerObject obj = new SerObject(Protocol.LOGIN);
      obj.datas = new String[2];
      obj.datas[0] = id;
      obj.datas[1] = passwd;
      try{
        parent.put(obj);
      }catch(Exception e1){
        parent.setMessage("Can't connect to Server!!");
      }

      clear();
    }
  }
}

