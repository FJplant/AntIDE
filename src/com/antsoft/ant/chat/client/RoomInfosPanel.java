package com.antsoft.ant.chat.client;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;

import com.antsoft.ant.chat.*;
import com.antsoft.ant.util.BorderList;


/**
 * project room과 속해 있는 member들을 보여주는 panel
 *
 * @author kim sang kyun
 */
public class RoomInfosPanel extends JPanel implements ItemListener, ActionListener, ListSelectionListener{

  private JComboBox roomNameCbx;
  private JList memberList;
  private JButton joinBtn;
  private DefaultListModel model;
  private ChatClientFrame parent;
  private UserInfoPanel uip;
  private JTextField totalTf;
  private DefaultComboBoxModel cbxM;

  public RoomInfosPanel(ChatClientFrame parent) {
    this.parent = parent;

    setLayout(new BorderLayout());
    cbxM = new DefaultComboBoxModel();
    
    roomNameCbx = new JComboBox(cbxM);
    roomNameCbx.addItemListener(this);

    JLabel roomNameLbl = new JLabel("   Room List");
    roomNameLbl.setForeground(Color.yellow);
    JPanel roomNameP = new JPanel(new FlowLayout(FlowLayout.LEFT));
    roomNameP.add(roomNameLbl);
    roomNameP.setBackground(Color.black);

    JPanel roomP = new JPanel(new BorderLayout());
    roomP.setBackground(Color.black);
    roomP.add(roomNameP, BorderLayout.NORTH);
    roomP.add(roomNameCbx, BorderLayout.CENTER);

    model = new DefaultListModel();
    memberList = new JList(model);
    memberList.addListSelectionListener(this);

    JScrollPane pane = new JScrollPane(memberList);

    JLabel memberLbl = new JLabel(" Members");
    memberLbl.setForeground(Color.yellow);
    JPanel memberP = new JPanel(new FlowLayout(FlowLayout.LEFT));
    memberP.add(memberLbl);
    memberP.setBackground(Color.black);

    JPanel dummyp = new JPanel();
    dummyp.setBackground(Color.black);

    JPanel dummyp2 = new JPanel();
    dummyp2.setBackground(Color.black);

    Box box = Box.createVerticalBox();
    box.add(dummyp);
    box.add(memberLbl);
    box.add(dummyp2);

    JLabel totalLbl= new JLabel("   Total    ");
    totalLbl.setForeground(Color.yellow);
    totalTf = new JTextField();
    totalTf.setEditable(false);

    JPanel totalP = new JPanel(new BorderLayout());
    totalP.add(totalLbl, BorderLayout.WEST);
    totalP.add(totalTf, BorderLayout.CENTER);
    totalP.setBackground(Color.darkGray);

    JPanel memberListP = new JPanel(new BorderLayout());
    memberListP.add(box, BorderLayout.NORTH);
    memberListP.add(pane, BorderLayout.CENTER);
    memberListP.add(totalP, BorderLayout.SOUTH);
    memberListP.setBackground(Color.black);

    uip = new UserInfoPanel();

    JPanel memberRapP = new JPanel(new BorderLayout());
    memberListP.setPreferredSize(new Dimension(150, 1));
    memberRapP.add(memberListP, BorderLayout.WEST);
    memberRapP.add(uip, BorderLayout.CENTER);

    joinBtn = new JButton("Join");
    joinBtn.addActionListener(this);

    JPanel bottomP = new JPanel();
    bottomP.add(joinBtn);
    bottomP.setBackground(Color.black);

    add(roomP, BorderLayout.NORTH);
    add(memberRapP, BorderLayout.CENTER);
    add(bottomP, BorderLayout.SOUTH);
  }

  public void clear(){
    model.removeAllElements();
    cbxM.removeAllElements();
    uip.clear();
  }


  public boolean exist(String room){

    for(int i=0; i<cbxM.getSize(); i++){
      RoomInfo info = (RoomInfo)cbxM.getElementAt(i);
      if(room.equals(info.name)) return true;
    }

    return false;
  }

  public void setData(Vector datas){

    for(int i=0; i<datas.size(); i++){
      RoomInfo info = (RoomInfo)datas.elementAt(i);
      cbxM.addElement(info);
    }
  }

  public RoomInfo getRoomInfo(String roomName){
    RoomInfo ret = null;
    for(int i=0; i<cbxM.getSize(); i++){
      RoomInfo info = (RoomInfo)cbxM.getElementAt(i);
      if(info.name.equals(roomName)){
        ret = info;
        break;
      }
    }

    return ret;
  }

  public void addData(RoomInfo info){
    cbxM.addElement(info);
  }

  public void addMember(String room, User u){

    RoomInfo info;
    for(int i=0; i<cbxM.getSize(); i++){
      info = (RoomInfo)cbxM.getElementAt(i);
      if(room.equals(info.name)){
        info.members.addElement(u);
        if(info.equals(cbxM.getSelectedItem()))  model.addElement(u);

        break;
      }
    }
  }

  public void removeRoom(String room){

    for(int i=0; i<cbxM.getSize(); i++){
      RoomInfo info = (RoomInfo)cbxM.getElementAt(i);
      if(room.equals(info.name)){
        cbxM.removeElementAt(i);
        break;
      }
    }
  }

  public void removeMember(String room, User u){

    for(int i=0; i<cbxM.getSize(); i++){
      RoomInfo info = (RoomInfo)cbxM.getElementAt(i);
      if(room.equals(info.name)){
        info.members.removeElement(u);
        if(roomNameCbx.getSelectedIndex() == i){
          model.removeElement(u);
        }

        if(uip.getUser() != null && uip.getUser().equals(u)){
          uip.clear();
        }
        break;
      }
    }
  }

  public void itemStateChanged(ItemEvent e){
    model.removeAllElements();

    Object obj = cbxM.getSelectedItem();
    if(obj == null) return;

    RoomInfo info = (RoomInfo)obj;
    for(int j=0; j<info.members.size(); j++){
      User u = (User)info.members.elementAt(j);
       model.addElement(u);
    }

    totalTf.setText("   "+model.size());
  }

  public void valueChanged(ListSelectionEvent e){
    User u = (User)memberList.getSelectedValue();
    if(u != null) uip.setUser(u);
    else uip.clear();
  }

  public void actionPerformed(ActionEvent e){
    if(e.getSource() == joinBtn){
      if(cbxM.getSelectedItem() == null) return;

      RoomInfo info = (RoomInfo)cbxM.getSelectedItem();
      if(parent.checkPrevJoinRoom(info.name)) return;

      int size = info.members.size();
      if(info.limit != -1 && info.limit <= size){
        JOptionPane.showMessageDialog(this, "Member Limit reached!!!", "Error", JOptionPane.ERROR_MESSAGE);
      }

      else if(info.isPrivate){
        String ret = null;
        ret = JOptionPane.showInputDialog(this, "Enter PASSWORD", "PASSWORD", JOptionPane.PLAIN_MESSAGE);
        if(ret != null && ret.equals(info.passwd)) parent.joinRoom(info.name);
        else{
          JOptionPane.showMessageDialog(this, "Password is not equal!!", "Error", JOptionPane.ERROR_MESSAGE);
        }
      }
      else parent.joinRoom(info.name);
    }
  }

  class UserInfoPanel extends JPanel{

    private JTextField idTf, passwdTf, emailTf, homePageTf, sexTf, skillTf;
    private User currUser;

    public void clear(){
      idTf.setText("");
      emailTf.setText("");
      homePageTf.setText("");
      sexTf.setText("");
      skillTf.setText("");

      currUser = null;
    }

    public UserInfoPanel() {
      setLayout(new BorderLayout());
      setBorder(BorderList.lightLoweredBorder);
      setBackground(Color.black);

      JLabel idLbl = new JLabel("        ID");
      idLbl.setForeground(Color.yellow);
      idLbl.setFont(new Font("DialogInput", Font.BOLD, 12));
      idTf = new JTextField(15);
      idTf.setFont(new Font("DialogInput", Font.BOLD, 12));
      idTf.setEditable(false);

      JPanel idP = new JPanel(new FlowLayout(FlowLayout.LEFT));
      idP.add(idLbl);
      idP.add(idTf);
      idP.setBackground(Color.black);

      JLabel sexLbl = new JLabel("       SEX");
      sexLbl.setForeground(Color.yellow);
      sexLbl.setFont(new Font("DialogInput", Font.BOLD, 12));
      sexTf = new JTextField(15);
      sexTf.setFont(new Font("DialogInput", Font.BOLD, 12));
      sexTf.setEditable(false);

      JPanel sexP = new JPanel(new FlowLayout(FlowLayout.LEFT));
      sexP.add(sexLbl);
      sexP.add(sexTf);
      sexP.setBackground(Color.black);

      JLabel skillLbl = new JLabel("JAVA SKILL");
      skillLbl.setForeground(Color.yellow);
      skillLbl.setFont(new Font("DialogInput", Font.BOLD, 12));
      skillTf = new JTextField(15);
      skillTf.setFont(new Font("DialogInput", Font.BOLD, 12));
      skillTf.setEditable(false);

      JPanel skillP = new JPanel(new FlowLayout(FlowLayout.LEFT));
      skillP.add(skillLbl);
      skillP.add(skillTf);
      skillP.setBackground(Color.black);

      JLabel emailLbl = new JLabel("    E-MAIL");
      emailLbl.setForeground(Color.yellow);
      emailLbl.setFont(new Font("DialogInput", Font.BOLD, 12));
      emailTf = new JTextField(30);
      emailTf.setFont(new Font("DialogInput", Font.BOLD, 12));
      emailTf.setEditable(false);

      JPanel emailP = new JPanel(new FlowLayout(FlowLayout.LEFT));
      emailP.add(emailLbl);
      emailP.add(emailTf);
      emailP.setBackground(Color.black);

      JLabel homePageLbl = new JLabel("  HOMEPAGE");
      homePageLbl.setForeground(Color.yellow);
      homePageLbl.setFont(new Font("DialogInput", Font.BOLD, 12));
      homePageTf = new JTextField(30);
      homePageTf.setFont(new Font("DialogInput", Font.BOLD, 12));
      homePageTf.setEditable(false);

      JPanel homePageP = new JPanel(new FlowLayout(FlowLayout.LEFT));
      homePageP.add(homePageLbl);
      homePageP.add(homePageTf);
      homePageP.setBackground(Color.black);

      Box box = Box.createVerticalBox();
      box.add(makeDummy());
      box.add(idP);
      box.add(emailP);
      box.add(homePageP);
      box.add(skillP);
      box.add(sexP);
      box.add(skillP);

      add(box, BorderLayout.NORTH);
    }

    public void setUser(User user){
      currUser = user;

      idTf.setText(user.getID());
      emailTf.setText(user.getEmail());
      homePageTf.setText(user.getHomePage());
      sexTf.setText(user.getSex());
      skillTf.setText(user.getSkill());
    }

    public User getUser(){
      return currUser;
    }

    private JPanel makeDummy(){
      JPanel p = new JPanel();
      p.setBackground(Color.black);
      return p;
    }
  }
}
