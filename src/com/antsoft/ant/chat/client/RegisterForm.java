package com.antsoft.ant.chat.client;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.antsoft.ant.util.BorderList;
import com.antsoft.ant.util.ImageList;
import com.antsoft.ant.chat.*;

/**
 * ID µî·Ï form
 *
 * @author kim sang kyun
 */
public class RegisterForm extends JPanel implements ActionListener{

  private JTextField idTf, passwdTf, emailTf, homePageTf;
  private JRadioButton male, female;
  private JTextArea ta;
  private JButton okBtn;
  private ChatClientFrame parent;
  private JComboBox skill;
  private JTextArea messageTa;

  public static final String ID_ERROR_MESSAGE = "Invalid ID";
  public static final String PASSWORD_ERROR_MESSGE = "Invalid PASSWORD";

  public RegisterForm(ChatClientFrame parent) {
    this.parent = parent;

    setLayout(new BorderLayout());
    setBorder(BorderList.lightLoweredBorder);
    setBackground(Color.black);

    JLabel idLbl = new JLabel("        ID");
    idLbl.setForeground(Color.yellow);
    idLbl.setFont(new Font("DialogInput", Font.BOLD, 12));
    idTf = new JTextField(15);
    idTf.setFont(new Font("DialogInput", Font.BOLD, 12));

    JPanel idP = new JPanel(new FlowLayout(FlowLayout.LEFT));
    idP.add(idLbl);
    idP.add(idTf);
    idP.setBackground(Color.black);

    JLabel passwdLbl = new JLabel("  PASSWORD");
    passwdLbl.setForeground(Color.yellow);
    passwdLbl.setFont(new Font("DialogInput", Font.BOLD, 12));
    passwdTf = new JTextField(15);
    passwdTf.setFont(new Font("DialogInput", Font.BOLD, 12));

    JPanel passwdP = new JPanel(new FlowLayout(FlowLayout.LEFT));
    passwdP.setBackground(Color.black);
    passwdP.add(passwdLbl);
    passwdP.add(passwdTf);

    JLabel emailLbl = new JLabel("    E-MAIL");
    emailLbl.setForeground(Color.yellow);
    emailLbl.setFont(new Font("DialogInput", Font.BOLD, 12));
    emailTf = new JTextField(30);
    emailTf.setFont(new Font("DialogInput", Font.BOLD, 12));

    JPanel emailP = new JPanel(new FlowLayout(FlowLayout.LEFT));
    emailP.add(emailLbl);
    emailP.add(emailTf);
    emailP.setBackground(Color.black);

    JLabel homePageLbl = new JLabel("  HOMEPAGE");
    homePageLbl.setForeground(Color.yellow);
    homePageLbl.setFont(new Font("DialogInput", Font.BOLD, 12));
    homePageTf = new JTextField(30);
    homePageTf.setFont(new Font("DialogInput", Font.BOLD, 12));
    homePageTf.setText("HTTP:\\\\");

    JPanel homePageP = new JPanel(new FlowLayout(FlowLayout.LEFT));
    homePageP.add(homePageLbl);
    homePageP.add(homePageTf);
    homePageP.setBackground(Color.black);

    male = new JRadioButton("M");
    male.setBackground(Color.black);
    male.setForeground(Color.white);
    male.setSelected(true);

    female = new JRadioButton("F");
    female.setBackground(Color.black);
    female.setForeground(Color.white);

    ButtonGroup br = new ButtonGroup();
    br.add(male);
    br.add(female);

    JLabel sexLbl = new JLabel("       SEX");
    sexLbl.setForeground(Color.yellow);
    sexLbl.setFont(new Font("DialogInput", Font.BOLD, 12));

    JPanel sexP = new JPanel(new FlowLayout(FlowLayout.LEFT));
    sexP.add(sexLbl);
    sexP.add(male);
    sexP.add(female);
    sexP.setBackground(Color.black);

    JLabel skillLbl = new JLabel("Java Skill");
    skillLbl.setForeground(Color.yellow);
    skillLbl.setFont(new Font("DialogInput", Font.BOLD, 12));
    skill = new JComboBox();
    skill.addItem("High");
    skill.addItem("Everage");
    skill.addItem("Low");
    skill.setFont(new Font("DialogInput", Font.BOLD, 12));

    okBtn = new JButton("Register");
    okBtn.addActionListener(this);

    JPanel skillP = new JPanel(new FlowLayout(FlowLayout.LEFT));
    skillP.add(skillLbl);
    skillP.add(skill);
    skillP.add(makeDummy());
    skillP.add(okBtn);
    skillP.setBackground(Color.black);

    JLabel logoLbl = new JLabel(ImageList.chatLogoIcon);
    logoLbl.setBackground(Color.black);

    JPanel logoP =new JPanel(new BorderLayout());
    logoP.setBackground(Color.black);
    logoP.add(logoLbl, BorderLayout.NORTH);

    JLabel messageLbl = new JLabel("   MESSAGE");
    messageLbl.setForeground(Color.yellow);
    messageLbl.setFont(new Font("DialogInput", Font.BOLD, 12));
    messageTa = new JTextArea(5, 50);
    messageTa.setEnabled(false);
    messageTa.setFont(new Font("DialogInput", Font.BOLD, 12));

    JPanel messageP = new JPanel(new FlowLayout(FlowLayout.LEFT));
    messageP.add(messageLbl);
    messageP.add(new JScrollPane(messageTa));
    messageP.setBackground(Color.black);

    Box box = Box.createVerticalBox();
    box.add(makeDummy());
    box.add(idP);
    box.add(passwdP);
    box.add(emailP);
    box.add(homePageP);
    box.add(sexP);
    box.add(skillP);
    box.add(makeDummy());
    box.add(messageP);

    box.setBackground(Color.yellow);

    JPanel boxP =new JPanel(new BorderLayout());
    boxP.add(box, BorderLayout.CENTER);
    boxP.add(logoP, BorderLayout.EAST);
    boxP.setBackground(Color.black);

    add(boxP, BorderLayout.NORTH);
    idTf.requestFocus();
  }

  private JPanel makeDummy(){
    JPanel p = new JPanel();
    p.setBackground(Color.black);
    return p;
  }

  public void clear(){
    idTf.setText("");
    passwdTf.setText("");
    emailTf.setText("");
    homePageTf.setText("HTTP:\\\\");
    male.setSelected(true);
    skill.setSelectedIndex(0);
  }

  public void setMessage(String msg){
    messageTa.append(msg + "\n");
  }

  public void actionPerformed(ActionEvent e){

    String id = idTf.getText();
    String passwd = passwdTf.getText();
    String email = emailTf.getText();
    String homepage = homePageTf.getText();
    String skillStr = (String)skill.getSelectedItem();
    String sex = (male.isSelected()) ? "male" : "female";

    if(id.trim().length() == 0){
      setMessage(ID_ERROR_MESSAGE);
    }

    else if( passwd.trim().length() == 0 ){
      setMessage(PASSWORD_ERROR_MESSGE);
    }

    else {

      setMessage("Tring to register...");

      RegisterObject obj = new RegisterObject(Protocol.REGISTER);
      User u = new User(id, passwd, email, homepage, skillStr, sex);
      obj.user = u;

      try{
        parent.put(obj);
      }catch(Exception e1){
        setMessage("Can't connect to Server!!");
      }
    }
  }
}


