package com.antsoft.ant.chat.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.util.Vector;
import com.antsoft.ant.util.BorderList;
import com.antsoft.ant.util.ImageList;
import com.antsoft.ant.main.MainFrame;
import com.antsoft.ant.chat.*;

/**
 * Chat Client UI Panel class
 *
 * @author kim sang kyun
 */
public class ChatClientFrame extends JFrame {
  private JButton registerBtn, loginBtn, logoutBtn, makeroomBtn, roomsBtn,
                  joinRoomBtn;
  private JPanel cards, wCards;
  private LoginForm loginForm;
  private RegisterForm registerForm;
  private MakeRoomForm makeroomForm;

  private JPanel blankForm;
  private JTextArea ta;

  private ChatClient client;
  private JPanel generalPanel;
  private RoomInfosPanel roomInfosPanel;
  private JTabbedPane roomTab;

  public static final String LOGIN_FORM = "login";
  public static final String MAKEROOM_FORM = "makeroom";

  public static final String REGISTER_FORM = "register";
  public static final String GENERAL_PANEL = "general";
  public static final String ROOMINFOS_PANEL = "roominfos";
  public static final String ROOM_PANEL = "room";

  //private String myID;
  public User myInfo;

  public ChatClientFrame() {
    setTitle("Ant Live Connection");
    client = new ChatClient(this);

    ActionListener al = new ActionHandler();

    setIconImage(ImageList.frameIcon.getImage());
    getContentPane().setLayout(new BorderLayout());

    registerBtn = new JButton("Register");
    registerBtn.addActionListener(al);
    registerBtn.setBorder(BorderList.raisedBorder);

    loginBtn = new JButton("Login");
    loginBtn.addActionListener(al);
    loginBtn.setBorder(BorderList.raisedBorder);

    logoutBtn = new JButton("Logout");
    logoutBtn.addActionListener(al);
    logoutBtn.setBorder(BorderList.raisedBorder);
    logoutBtn.setEnabled(false);

    makeroomBtn = new JButton("MakeRoom");
    makeroomBtn.addActionListener(al);
    makeroomBtn.setBorder(BorderList.raisedBorder);
    makeroomBtn.setEnabled(false);

    roomsBtn = new JButton("Rooms");
    roomsBtn.addActionListener(al);
    roomsBtn.setBorder(BorderList.raisedBorder);
    roomsBtn.setEnabled(false);

    joinRoomBtn = new JButton("JoinedRooms");
    joinRoomBtn.addActionListener(al);
    joinRoomBtn.setBorder(BorderList.raisedBorder);
    joinRoomBtn.setEnabled(true);

    JPanel topMenuP = new JPanel(new FlowLayout(FlowLayout.LEFT));
    topMenuP.add(registerBtn);
    topMenuP.add(loginBtn);
    topMenuP.add(logoutBtn);
    topMenuP.add(makeroomBtn);
    topMenuP.add(roomsBtn);
    topMenuP.add(joinRoomBtn);
    topMenuP.setBorder(BorderList.lightLoweredBorder);
    topMenuP.setBackground(Color.black);

    JPanel topMenuRapP = new JPanel(new BorderLayout());
    topMenuRapP.add(topMenuP, BorderLayout.CENTER);
    JLabel dummy = new JLabel();
    dummy.setBorder(BorderList.raisedBorder);
    topMenuRapP.add(dummy, BorderLayout.SOUTH);

    cards = new JPanel();
    cards.setLayout(new CardLayout());
    cards.setBackground(Color.black);

    registerForm = new RegisterForm(this);
    loginForm = new LoginForm(this);
    makeroomForm = new MakeRoomForm(this);

    cards.add(LOGIN_FORM, loginForm);
    cards.add(MAKEROOM_FORM, makeroomForm);

    CardLayout cl = (CardLayout)cards.getLayout();
    cl.show(cards, LOGIN_FORM);

    ta = new JTextArea();
    ta.setForeground(Color.white);
    ta.setFont(new Font("DialogInput", Font.PLAIN, 12));
    ta.setBackground(Color.black);
    JScrollPane pane = new JScrollPane(ta);
    pane.setBackground(Color.black);

    generalPanel = new JPanel(new BorderLayout());
    generalPanel.add(cards, BorderLayout.NORTH);
    generalPanel.add(pane, BorderLayout.CENTER);

    roomInfosPanel = new RoomInfosPanel(this);

    //roomPanel = new RoomPanel(this);
    roomTab = new JTabbedPane();

    wCards = new JPanel();
    wCards.setLayout(new CardLayout());
    wCards.setBackground(Color.black);

    wCards.add(this.GENERAL_PANEL, generalPanel);
    wCards.add(this.ROOMINFOS_PANEL, roomInfosPanel);
    wCards.add(REGISTER_FORM, registerForm);
    wCards.add(this.ROOM_PANEL, roomTab);

    getContentPane().add(topMenuRapP, BorderLayout.NORTH);
    getContentPane().add(wCards, BorderLayout.CENTER);

    CardLayout cl2 = (CardLayout)wCards.getLayout();
    cl2.show(wCards, GENERAL_PANEL);

    setBounds(150,150,500,500);
  }

  private void setCardsVisible(boolean isVisible){

    if(isVisible && generalPanel.getComponentCount() == 1){
      generalPanel.add(cards, BorderLayout.NORTH);
      generalPanel.doLayout();
    }
    else if(!isVisible && generalPanel.getComponentCount() == 2){
      generalPanel.remove(cards);
      generalPanel.doLayout();
    }
  }

  public boolean isConnected(){
    return client.isConnected();
  }

  public void connectToServer(){
    try{
      setMessage("Connecting to AntLive Server...");
      client.connect(InetAddress.getByName("remember"), 5432);
    }catch(Exception e){}

    setMessage("Connection Success !!");
  }

  private void changeCard(String name){
    CardLayout cl = (CardLayout)cards.getLayout();
    cl.show(cards, name);
    cards.doLayout();
    cards.repaint();
  }

  private void changeWCard(String name){
    CardLayout cl = (CardLayout)wCards.getLayout();
    cl.show(wCards, name);
    wCards.doLayout();
    wCards.repaint();
  }

  public void treatRegister(GeneralObject obj){

    if(obj.isError){
      SerObject sobj = (SerObject)obj;
      registerForm.setMessage(sobj.datas[0]);
    }
    else {
      SerObject sobj = (SerObject)obj;
      setCardsVisible(false);
      registerForm.setMessage(sobj.datas[0]);
      registerForm.setMessage("Congraturation....");
    }
  }

  public void treatMakeRoom(GeneralObject obj){

    MakeRoomObject mobj = (MakeRoomObject)obj;
    RoomInfo info = mobj.roomInfo;
    roomInfosPanel.addData(info);

    RoomPanel roomPanel = new RoomPanel(this);

    if(myInfo.getID().equals(mobj.user.getID())){
      roomPanel.setRoomInfo(info);
      roomTab.addTab(info.name, roomPanel);
      roomTab.setSelectedComponent(roomPanel);

      changeWCard(this.ROOM_PANEL);
    }
  }

  public void treatLogin(GeneralObject obj){

    if(obj.isError){
      SerObject sobj = (SerObject)obj;
      setMessage(sobj.datas[0]);
    }
    else{
      LoginObject lobj = (LoginObject)obj;
      Vector datas = lobj.datas;
      myInfo = lobj.user;

      roomInfosPanel.setData(datas);
      changeWCard(ROOMINFOS_PANEL);

      loginBtn.setEnabled(false);
      logoutBtn.setEnabled(true);
      makeroomBtn.setEnabled(true);
      roomsBtn.setEnabled(true);
      joinRoomBtn.setEnabled(true);      
    }
  }

  public void treatLeaveRoom(GeneralObject obj){
    LeaveRoomObject rec = (LeaveRoomObject)obj;
    String roomName = rec.roomName;
    User u = rec.user;

    //room info panel에서 삭제 
    roomInfosPanel.removeMember(roomName, u);


    //현재 참가해있는 room에서 삭제
    if(u.equals(myInfo)){
      for(int i=0; i<roomTab.getTabCount(); i++){
        if(roomName.equals(roomTab.getTitleAt(i))){
          roomTab.removeTabAt(i);
          break;
        }
      }
    }

    else
    for(int i=0; i<roomTab.getTabCount(); i++){
      if(roomName.equals(roomTab.getTitleAt(i))){
        RoomPanel rp = (RoomPanel)roomTab.getComponentAt(i);
        rp.removeUser(u);
      }
    }
  }

  public void treatRoomDestory(GeneralObject obj){
    SerObject sobj = (SerObject)obj;
    String roomName = sobj.datas[0];

    roomInfosPanel.removeRoom(roomName);

    for(int i=0; i<roomTab.getTabCount(); i++){
      if(roomTab.getTitleAt(i).equals(roomName)){
        roomTab.removeTabAt(i);
      }
    }
  }

  public void treatJoinRoom(GeneralObject obj){

    JoinRoomObject jobj = (JoinRoomObject)obj;
    String roomName = jobj.roomName;

    roomInfosPanel.addMember(roomName, jobj.user);    

    if(jobj.user.equals(myInfo)){
      RoomPanel rp = new RoomPanel(this);
      rp.setRoomInfo(roomInfosPanel.getRoomInfo(roomName));
      roomTab.addTab(roomName, rp);
      roomTab.setSelectedComponent(rp);
      this.changeWCard(this.ROOM_PANEL);
    }
    else{
      for(int i=0; i<roomTab.getTabCount(); i++){
        if(roomTab.getTitleAt(i).equals(roomName)){
          RoomPanel rp = (RoomPanel)roomTab.getComponentAt(i);
          rp.addUser(jobj.user);
        }
      }
    }
  }

  public void treatChat(GeneralObject obj){
    ChatObject cobj = (ChatObject)obj;
    String roomName = cobj.room;

    for(int i=0; i<roomTab.getTabCount();i++){
      RoomPanel rp = (RoomPanel)roomTab.getComponentAt(i);
      if(rp.getName().equals(roomName)){
        rp.appendMessage(cobj.fromID, cobj.message);
        break;
      }
    }
  }

  public void sendMessage(String room, String msg){
    ChatObject obj = new ChatObject(Protocol.CHAT);
    obj.room = room;
    obj.fromID = myInfo.getID();
    obj.message = msg;

    try{
      put(obj);
    }catch(Exception e){e.printStackTrace();}
  }

  public void setMessage(String msg){
    ta.append(msg + "\n");
  }


  /**
   * send message object to server
   */
  public void put(Object obj) throws Exception{
    client.put(obj);
  }

  public boolean isExistRoom(String room){
    return roomInfosPanel.exist(room);
  }

  public void leaveRoom(String roomName){
    if(roomTab.getTabCount() == 1) this.changeWCard(ROOMINFOS_PANEL);

    SerObject obj = new SerObject(Protocol.LEAVEROOM);
    obj.datas = new String[2];
    obj.datas[0] = roomName;
    obj.datas[1] = myInfo.getID();


    try{
      put(obj);
    }catch(Exception e){e.printStackTrace();}
  }

  public boolean checkPrevJoinRoom(String roomName){
    for(int i=0; i< roomTab.getTabCount(); i++){
      if(roomTab.getTitleAt(i).equals(roomName)){
        this.changeWCard(this.ROOM_PANEL);
        roomTab.setSelectedIndex(i);
        return true;
      }
    }

    return false;
  }

  public void joinRoom(String roomName){

    JoinRoomObject obj = new JoinRoomObject(Protocol.JOINROOM);
    obj.roomName = roomName;
    obj.user = myInfo;

    try{
      put(obj);
    }catch(Exception e){}
  }

  private void doLogout(){

    loginBtn.setEnabled(true);
    logoutBtn.setEnabled(false);
    roomsBtn.setEnabled(false);
    makeroomBtn.setEnabled(false);
    joinRoomBtn.setEnabled(false);

    roomTab.removeAll();
    roomInfosPanel.clear();
    loginForm.clear();
    makeroomForm.clear();
    registerForm.clear();

    client.disconnect();

    changeWCard(this.GENERAL_PANEL);
    changeCard(this.LOGIN_FORM);
    setMessage("Disconnected from AntLive Server. Goodbye...");
  }

  class ActionHandler implements ActionListener{
     public void actionPerformed(ActionEvent e){
       if(e.getSource() == registerBtn){
         changeWCard(REGISTER_FORM);
       }
       else if(e.getSource() == loginBtn){
         setCardsVisible(true);
         changeWCard(GENERAL_PANEL);
         changeCard(LOGIN_FORM);
       }
       else if(e.getSource() == logoutBtn){
         doLogout();
       }
       else if(e.getSource() == makeroomBtn){
         setCardsVisible(true);
         changeWCard(GENERAL_PANEL);
         changeCard(MAKEROOM_FORM);
       }
       else if(e.getSource() == roomsBtn){
         changeWCard(ROOMINFOS_PANEL);
       }

       else if(e.getSource() == joinRoomBtn){
         changeWCard(ROOM_PANEL);
       }
     }
  }
}
